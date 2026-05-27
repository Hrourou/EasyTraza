/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;


import cat.copernic.EasyTraza.business.UsuariService;
import cat.copernic.EasyTraza.dto.UsuariDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;
/**
 *
 * @author HAMZA
 */
@Controller
public class RecuperarPasswordController {

    @Autowired
    private UsuariService usuariService;

    @Autowired
    private JavaMailSender mailSender;

    // 1. Recibe el email y envía el código
    @PostMapping("/recuperar-password")
    public String solicitarRecuperacion(@RequestParam("email") String email, HttpSession session, Model model) {
        UsuariDTO usuari = usuariService.getUsuariByEmail(email);

        if (usuari == null) {
            return "redirect:/login?errorMsg=No existe ninguna cuenta con ese email.";
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));
        session.setAttribute("codigoRecuperacion", codigo);
        session.setAttribute("emailRecuperacion", email);
        session.setAttribute("codigoExpiracion", System.currentTimeMillis() + (5 * 60 * 1000)); // 5 minutos

        enviarEmailRecuperacion(email, codigo);

        // Recargamos la vista de login pero le decimos que abra el modal de verificar código
        model.addAttribute("usuaris", usuariService.getAllUsuaris());
        model.addAttribute("showVerifyModal", true);
        return "login";
    }

    // 2. Comprueba si el código es correcto
    @PostMapping("/verificar-codigo")
    public String verificarCodigo(@RequestParam("codigo") String codigoIngresado, HttpSession session, Model model) {
        String codigoGuardado = (String) session.getAttribute("codigoRecuperacion");
        Long codigoExpiracion = (Long) session.getAttribute("codigoExpiracion");
        model.addAttribute("usuaris", usuariService.getAllUsuaris()); // Siempre recargar usuarios para el fondo

        if (codigoExpiracion != null && System.currentTimeMillis() > codigoExpiracion) {
            session.removeAttribute("codigoRecuperacion");
            session.removeAttribute("emailRecuperacion");
            session.removeAttribute("codigoExpiracion");
            model.addAttribute("errorVerificacion", "El código ha caducado (han pasado más de 5 minutos). Por favor, solicita uno nuevo.");
            model.addAttribute("showVerifyModal", true);
            return "login";
        }

        if (codigoGuardado != null && codigoGuardado.equals(codigoIngresado)) {
            // Código correcto, abrimos el modal para poner la nueva contraseña
            model.addAttribute("showNewPasswordModal", true);
            return "login";
        }

        // Código incorrecto, volvemos a mostrar el modal de verificar con un error
        model.addAttribute("errorVerificacion", "El código introducido es incorrecto.");
        model.addAttribute("showVerifyModal", true);
        return "login";
    }

    // 3. Guarda la contraseña nueva
    @PostMapping("/guardar-nueva-password")
    public String guardarNuevaPassword(@RequestParam("password") String nuevaPassword, HttpSession session, org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes, Model model) {
        String email = (String) session.getAttribute("emailRecuperacion");

        if (email != null) {
            if (nuevaPassword == null || nuevaPassword.length() < 8) {
                model.addAttribute("usuaris", usuariService.getAllUsuaris());
                model.addAttribute("errorNuevaPassword", "La contraseña debe tener al menos 8 caracteres.");
                model.addAttribute("showNewPasswordModal", true);
                return "login";
            }

            UsuariDTO usuari = usuariService.getUsuariByEmail(email);
            usuari.setPassword(nuevaPassword);
            
            usuariService.updateUsuari(usuari.getDni(), usuari, email);
            
            session.removeAttribute("codigoRecuperacion");
            session.removeAttribute("emailRecuperacion");
            session.removeAttribute("codigoExpiracion");

            redirectAttributes.addAttribute("logoutMsg", "Contraseña actualizada correctamente. Ya puedes entrar.");
            return "redirect:/login";
        }
        return "redirect:/login";
    }

    private void enviarEmailRecuperacion(String destino, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destino);
        message.setSubject("Código de Recuperación - EasyTraza");
        message.setText("Has solicitado recuperar tu contraseña. Tu código de verificación es: " + codigo + "\n\nEste código caducará en 5 minutos.\n\nSi no has sido tú, ignora este correo.");
        mailSender.send(message);
    }
}