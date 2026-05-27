/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.UsuariService;
import cat.copernic.EasyTraza.dto.UsuariDTO;
import cat.copernic.EasyTraza.validation.Validator;
import cat.copernic.EasyTraza.business.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuariService usuariService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public String veurePerfil(Model model, Principal principal) {
        UsuariDTO usuariConnectat = usuariService.getUsuariByEmail(principal.getName());
        
        // Si el usuario no existe en BD por algún motivo, lo mandamos al login por seguridad
        if (usuariConnectat == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("perfil", usuariConnectat);
        model.addAttribute("usuariLoguejat", usuariConnectat); 
        return "perfil";
    }

    @PostMapping("/update")
    public String actualitzarPerfil(@ModelAttribute("perfil") UsuariDTO dto,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam(value = "deletePhoto", defaultValue = "false") boolean deletePhoto,
                                    Principal principal, Model model) {
        String emailUsuariActual = principal.getName();
        UsuariDTO usuariOriginal = usuariService.getUsuariByEmail(emailUsuariActual);
        
        // Evitamos el NullPointerException: si el usuario original es null, abortamos
        if (usuariOriginal == null) {
            return "redirect:/login";
        }
        
        // --- VALIDACIONES ---
        if (!Validator.validarEmail(dto.getEmail())) {
            model.addAttribute("error", "Error: El formato del correo electrónico no es válido.");
            return recargarVistaError(model, dto, usuariOriginal);
        }
        if (dto.getTelefon() != null && !dto.getTelefon().isEmpty() && !Validator.validarTelefon(dto.getTelefon())) {
            model.addAttribute("error", "Error: El teléfono introducido no es válido.");
            return recargarVistaError(model, dto, usuariOriginal);
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty() && dto.getPassword().length() < 8) {
            model.addAttribute("error", "Error: La nueva contraseña debe tener al menos 8 caracteres.");
            return recargarVistaError(model, dto, usuariOriginal);
        }

        try {
            // Mantenemos DNI y ROL originales
            dto.setDni(usuariOriginal.getDni());
            dto.setRol(usuariOriginal.getRol());
            
            // Si hay un nuevo archivo de imagen, lo guardamos
            if (file != null && !file.isEmpty()) {
                String fotoUrl = fileStorageService.saveProfileImage(file);
                dto.setFotoPerfilUrl(fotoUrl);
            } else if (deletePhoto) {
                dto.setFotoPerfilUrl(null);
            } else {
                // Mantenemos la foto actual si no se sube una nueva y no se pidió borrar
                dto.setFotoPerfilUrl(usuariOriginal.getFotoPerfilUrl());
            }

            // Guardamos en Base de Datos
            usuariService.updateUsuari(dto.getDni(), dto, emailUsuariActual);
            
            // --- MAGIA: ACTUALIZAR SESIÓN EN CALIENTE SI CAMBIA EL EMAIL ---
            if (!emailUsuariActual.equals(dto.getEmail())) {
                Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
                
                // Creamos un nuevo "Principal" con el nuevo email pero manteniendo sus roles
                User newPrincipal = new User(dto.getEmail(), "", currentAuth.getAuthorities());
                Authentication newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, currentAuth.getCredentials(), currentAuth.getAuthorities());
                
                // Sobrescribimos la sesión actual sin desconectar al usuario
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
            
            model.addAttribute("success", "Perfil actualizado correctamente.");
            
            // Recargamos los datos actualizados usando el NUEVO email
            UsuariDTO usuariActualitzat = usuariService.getUsuariByEmail(dto.getEmail());
            model.addAttribute("perfil", usuariActualitzat);
            model.addAttribute("usuariLoguejat", usuariActualitzat);
            
            return "perfil";
            
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return recargarVistaError(model, dto, usuariOriginal);
        }
    }

    // Método auxiliar para no repetir código
    private String recargarVistaError(Model model, UsuariDTO dto, UsuariDTO usuariOriginal) {
        model.addAttribute("perfil", dto);
        model.addAttribute("usuariLoguejat", usuariOriginal);
        return "perfil";
    }

    @GetMapping("/delete-photo")
    public String eliminarFotoPerfil(Principal principal, RedirectAttributes redirectAttributes) {
        String email = principal.getName();
        UsuariDTO usuari = usuariService.getUsuariByEmail(email);
        
        if (usuari != null && usuari.getFotoPerfilUrl() != null) {
            usuari.setFotoPerfilUrl(null);
            // Actualizamos en base de datos
            usuariService.updateUsuari(usuari.getDni(), usuari, email);
            redirectAttributes.addFlashAttribute("success", "Foto de perfil eliminada correctamente.");
        }
        
        return "redirect:/perfil";
    }
}