/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.entities.AlbaraProveidor;
import cat.copernic.EasyTraza.entities.Proveidor;
import cat.copernic.EasyTraza.enums.EstatLot;
import cat.copernic.EasyTraza.repository.AlbaraProveidorRepository;
import cat.copernic.EasyTraza.repository.LotProveidorRepository;
import cat.copernic.EasyTraza.repository.MateriaPrimeraRepository;
import cat.copernic.EasyTraza.repository.ProveidorRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/historial-albarans")
public class WebAlbaransController {

    @Autowired private AlbaraProveidorRepository albaraRepo;
    @Autowired private ProveidorRepository proveidorRepo;
    @Autowired private MateriaPrimeraRepository materiaRepo;
    @Autowired private LotProveidorRepository lotRepo;

    // 1. PANTALLA DEL LISTADO CON FILTROS
    @GetMapping
    public String llistarAlbarans(
            @RequestParam(required = false) String cif,
            @RequestParam(required = false) Long materiaId,
            Model model) {
        
        // Ejecutamos nuestra Query mágica
        List<AlbaraProveidor> albarans = albaraRepo.findByFiltres(cif, materiaId);
        
        // Pasamos los datos al HTML
        model.addAttribute("albarans", albarans);
        model.addAttribute("proveidors", proveidorRepo.findAll());
        model.addAttribute("materies", materiaRepo.findAll());
        
        Map<Long, Boolean> albaransAmbLotsBloqueants = albarans.stream()
                .collect(Collectors.toMap(
                        AlbaraProveidor::getId,
                        a -> lotRepo.existsByAlbara_IdAndEstatIn(a.getId(), Arrays.asList(EstatLot.OBERT, EstatLot.ACABAT)),
                        (existing, replacement) -> existing
                ));
        model.addAttribute("albaransAmbLotsBloqueants", albaransAmbLotsBloqueants);
        
        // Devolvemos los filtros actuales para que los desplegables no se reseteen
        model.addAttribute("filtreCif", cif);
        model.addAttribute("filtreMateria", materiaId);
        
        return "llistat-albarans"; 
    }

    // 2. PANTALLA DE DETALLE DEL ALBARÁN (Para ver qué lotes tiene dentro)
    @GetMapping("/{id}")
    public String veureDetallAlbara(@PathVariable Long id, Model model) {
        AlbaraProveidor albara = albaraRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Albarà no trobat"));
        
        model.addAttribute("albara", albara);
        return "detall-albara";
    }
    // 3. ELIMINAR ALBARÁN Y SUS LOTES
    @GetMapping("/eliminar/{id}")
    public String eliminarAlbara(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Check if any lots have status OBERT or ACABAT
            boolean teLotsBloqueants = lotRepo.existsByAlbara_IdAndEstatIn(
                    id, Arrays.asList(EstatLot.OBERT, EstatLot.ACABAT));
            if (teLotsBloqueants) {
                redirectAttributes.addFlashAttribute("error", 
                        "No es pot esborrar aquest albarà perquè té lots amb estat \"Obert\" o \"Acabat\".");
                return "redirect:/historial-albarans";
            }
            albaraRepo.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Albarán y sus lotes eliminados correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se ha podido eliminar el albarán por seguridad en la base de datos.");
        }
        return "redirect:/historial-albarans";
    }

    // 4. ACTUALIZAR ALBARÁN (fecha y proveedor)
    @PostMapping("/editar/{id}")
    public String editarAlbara(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataRecepcio,
            @RequestParam String cifProveidor,
            RedirectAttributes redirectAttributes) {
        try {
            AlbaraProveidor albara = albaraRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Albarà no trobat: " + id));
            albara.setDataRecepcio(dataRecepcio);
            Proveidor proveidor = proveidorRepo.findById(cifProveidor)
                    .orElseThrow(() -> new RuntimeException("Proveïdor no trobat: " + cifProveidor));
            albara.setProveidor(proveidor);
            albaraRepo.save(albara);
            redirectAttributes.addFlashAttribute("success", "Albarán #ALB-" + id + " actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el albarán: " + e.getMessage());
        }
        return "redirect:/historial-albarans";
    }
}