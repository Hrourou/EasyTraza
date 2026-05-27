/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.UsuariService;
import cat.copernic.EasyTraza.dto.UsuariDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
/**
 *
 * @author HAMZA
 * 
 * Esta clase inyecta variables automáticamente en TODOS los Modelos (HTML)
 * de la aplicación, para no tener que repetir código en cada controlador.
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UsuariService usuariService;

    // Este método se ejecuta automáticamente antes de cargar cualquier HTML.
    // Inyecta el "usuariLoguejat" para que el Sidebar muestre siempre tu nombre.
    @ModelAttribute("usuariLoguejat")
    public UsuariDTO afegirUsuariLoguejat(Principal principal) {
        if (principal != null) {
            // Si hay alguien logueado, buscamos sus datos y los mandamos al HTML
            return usuariService.getUsuariByEmail(principal.getName());
        }
        // Si no hay nadie (ej. pantalla de login), mandamos nulo
        return null;
    }
}