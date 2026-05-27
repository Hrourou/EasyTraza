/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.UsuariService;
import cat.copernic.EasyTraza.dto.UsuariDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author HAMZA
 */

@Controller
public class LoginController {

    @Autowired
    private UsuariService usuariService;

    @GetMapping("/login")
    public String showLogin(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        // Obtenemos los usuarios para pintar las tarjetitas
        model.addAttribute("usuaris", usuariService.getAllUsuaris());
        
        // Si Spring Security nos redirige aquí porque falló la contraseña, pasamos el error a la vista
        if (error != null) {
            model.addAttribute("errorMsg", "Credenciales incorrectas.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Sesión cerrada correctamente.");
        }
        
        return "login";
    }

    // El registro de nuevo trabajador (Esto lo mantienes igual)
    @PostMapping("/usuaris/save-worker")
    public String saveWorker(@ModelAttribute UsuariDTO usuariDTO) {
        // OJO: Asegúrate de que usuariService.createUsuari() está hasheando la contraseña con BCrypt
        usuariService.createUsuari(usuariDTO);
        return "redirect:/login"; 
    }

}