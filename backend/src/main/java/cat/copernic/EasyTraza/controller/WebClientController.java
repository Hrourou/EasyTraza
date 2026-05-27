/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.ClientService;
import cat.copernic.EasyTraza.dto.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador de la vista de Clients.
 * @author HAMZA
 */
@Controller
@RequestMapping("/admin/clients")
public class WebClientController {

    @Autowired
    private ClientService clientService;

    /**
     * Mostra la llista principal de clients amb opció de cerca.
     */
    @GetMapping
    public String veureClients(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("clients", clientService.getClients(keyword));
        model.addAttribute("keyword", keyword);
        return "clients"; 
    }

    /**
     * Desa un nou client i captura els errors de validació.
     */
    @PostMapping("/save")
    public String guardarClient(@ModelAttribute ClientDTO dto, Model model) {
        try {
            clientService.createClient(dto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureClients(null, model); // <-- Aquí estaba el fallo antes
        }
        return "redirect:/admin/clients";
    }

    /**
     * Actualitza la informació d'un client.
     */
    @PostMapping("/update")
    public String actualitzarClient(@ModelAttribute ClientDTO dto, Model model) {
        try {
            clientService.updateClient(dto.getNif(), dto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureClients(null, model);
        }
        return "redirect:/admin/clients";
    }

    /**
     * Elimina un client, protegint la base de dades.
     */
    @PostMapping("/delete")
    public String eliminarClient(@RequestParam("nif") String nif, Model model) {
        try {
            clientService.deleteClient(nif);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "No es pot esborrar aquest client perquè té albarans associats a ell.");
            return veureClients(null, model);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error inesperat al intentar esborrar el client.");
            return veureClients(null, model);
        }
        return "redirect:/admin/clients";
    }
}