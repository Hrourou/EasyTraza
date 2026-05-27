/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.AlbaraClientService;
import cat.copernic.EasyTraza.dto.AlbaraClientFormDTO;
import cat.copernic.EasyTraza.dto.LiniaClientDTO;
import cat.copernic.EasyTraza.entities.AlbaraClient;
import cat.copernic.EasyTraza.entities.LiniaClient;
import cat.copernic.EasyTraza.repository.ClientRepository;
import cat.copernic.EasyTraza.repository.ProducteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/albara-client")
public class WebAlbaraClientController {

    @Autowired private AlbaraClientService albaraClientService;
    @Autowired private ClientRepository clientRepo;
    @Autowired private ProducteRepository producteRepo;

    // 1. PANTALLA CREAR
    @GetMapping
    public String veurePantalla(Model model) {
        model.addAttribute("clients", clientRepo.findAll());
        model.addAttribute("productes", producteRepo.findAll());
        
        AlbaraClientFormDTO dto = new AlbaraClientFormDTO();
        dto.setDataProduccio(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        dto.getLinies().add(new LiniaClientDTO());
        dto.setEdit(false);
        
        model.addAttribute("albaraDTO", dto);
        model.addAttribute("isEdit", false); // Indica que es modo Crear
        return "albaraclient"; 
    }

    // 2. GUARDAR (Sirve para Crear y Editar)
    @PostMapping("/save")
    public String guardarAlbara(@ModelAttribute("albaraDTO") AlbaraClientFormDTO dto, Model model) {
        try {
            if (!dto.isEdit() && dto.getDataProduccio() != null) {
                LocalDateTime now = LocalDateTime.now();
                dto.setDataProduccio(dto.getDataProduccio().withSecond(now.getSecond()).withNano(now.getNano()));
            }
            albaraClientService.crearAlbaraClient(dto);
            return "redirect:/albara-client/llistat?success=guardat"; 
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("clients", clientRepo.findAll());
            model.addAttribute("productes", producteRepo.findAll());
            model.addAttribute("isEdit", false);
            return "albaraclient";
        }
    }

    // 3. PANTALLA LLISTAT (Historial)
    @GetMapping("/llistat")
    public String veureLlistat(Model model) {
        model.addAttribute("albarans", albaraClientService.getAllAlbarans());
        return "llistat-albarans-client";
    }

    // 4. PANTALLA EDITAR
    @GetMapping("/editar")
    public String editarAlbara(@RequestParam String nif, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data, Model model) {
        try {
            AlbaraClient albara = albaraClientService.getAlbaraById(nif, data);
            
            AlbaraClientFormDTO dto = new AlbaraClientFormDTO();
            dto.setNifClient(albara.getClient().getNif());
            dto.setDataProduccio(albara.getDataProduccio());
            dto.setEdit(true);
            
            // Volcar las líneas de la Base de Datos al Formulario
            for(LiniaClient linia : albara.getLinies()) {
                LiniaClientDTO lDto = new LiniaClientDTO();
                lDto.setProducteId(linia.getProducte().getId());
                lDto.setQuantitat(linia.getQuantitat());
                dto.getLinies().add(lDto);
            }
            
            model.addAttribute("albaraDTO", dto);
            model.addAttribute("clients", clientRepo.findAll());
            model.addAttribute("productes", producteRepo.findAll());
            model.addAttribute("isEdit", true); // Indica que es modo Editar (Bloquea NIF y Fecha)
            
            return "albaraclient";
        } catch (Exception e) {
            return "redirect:/albara-client/llistat?error=No_s_ha_trobat_albara";
        }
    }

    // 5. ELIMINAR
    @PostMapping("/eliminar")
    public String eliminarAlbara(@RequestParam String nif, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
        try {
            albaraClientService.eliminarAlbara(nif, data);
            return "redirect:/albara-client/llistat?success=eliminat";
        } catch (Exception e) {
            return "redirect:/albara-client/llistat?error=" + e.getMessage();
        }
    }

    // 6. MARCAR COMO ENTREGADO
    @PostMapping("/entregar")
    public String entregarAlbara(@RequestParam String nif, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data) {
        try {
            albaraClientService.entregarAlbara(nif, data);
            return "redirect:/albara-client/llistat?success=entregat";
        } catch (Exception e) {
            return "redirect:/albara-client/llistat?error=" + e.getMessage();
        }
    }
}