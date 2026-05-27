/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;
import cat.copernic.EasyTraza.entities.LotProveidor;
import cat.copernic.EasyTraza.entities.LotProveidorId;
import cat.copernic.EasyTraza.enums.EstatLot;
import cat.copernic.EasyTraza.repository.LotProveidorRepository;
import cat.copernic.EasyTraza.repository.MateriaPrimeraRepository;
import cat.copernic.EasyTraza.repository.ProveidorRepository;
import cat.copernic.EasyTraza.repository.LiniaClientRepository;
import cat.copernic.EasyTraza.entities.LiniaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/lotes")
public class WebLotesController {

    @Autowired private LotProveidorRepository lotRepo;
    @Autowired private ProveidorRepository proveidorRepo;
    @Autowired private MateriaPrimeraRepository materiaRepo;
    @Autowired private LiniaClientRepository liniaClientRepo;

    @GetMapping
    public String gestioLotes(
            @RequestParam(required = false) Long albaraId,
            @RequestParam(required = false) String cif,
            @RequestParam(required = false) Long materiaId,
            @RequestParam(required = false) String estat,
            @RequestParam(required = false) String codiLot,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntradaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEntradaHasta,
            @RequestParam(required = false, defaultValue = "materiaPrimera.nom") String ordenCampo,
            @RequestParam(required = false, defaultValue = "asc") String ordenDireccion,
            Model model) {

        // Convertir el string del estado al enum (si viene)
        EstatLot estatEnum = null;
        if (estat != null && !estat.isEmpty()) {
            try {
                estatEnum = EstatLot.valueOf(estat);
            } catch (IllegalArgumentException e) {
                // Estado inválido, ignorar filtro
            }
        }

        // Construir objeto Sort
        Sort.Direction direction = "desc".equalsIgnoreCase(ordenDireccion) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, ordenCampo);

        // 1. Ejecutar la búsqueda con los filtros que vengan
        List<LotProveidor> lotes = lotRepo.findByFiltres(albaraId, cif, materiaId, estatEnum, codiLot, dataEntradaDesde, dataEntradaHasta, sort);

        // 2. Pasar datos a la vista
        model.addAttribute("lotes", lotes);
        model.addAttribute("proveidors", proveidorRepo.findAll());
        model.addAttribute("materies", materiaRepo.findAll());
        model.addAttribute("estats", EstatLot.values());

        // 3. Devolver los filtros a la vista para mantener los desplegables marcados
        model.addAttribute("filtreAlbara", albaraId);
        model.addAttribute("filtreCif", cif);
        model.addAttribute("filtreMateria", materiaId);
        model.addAttribute("filtreEstat", estat);
        model.addAttribute("filtreCodiLot", codiLot);
        model.addAttribute("filtreDataEntradaDesde", dataEntradaDesde);
        model.addAttribute("filtreDataEntradaHasta", dataEntradaHasta);
        model.addAttribute("ordenCampo", ordenCampo);
        model.addAttribute("ordenDireccion", ordenDireccion);

        return "lotes"; // Llama al archivo lotes.html
    }

    // DELETE lote
    @GetMapping("/eliminar/{proveidorCif}/{idLot}")
    public String eliminarLot(@PathVariable String proveidorCif, @PathVariable String idLot, RedirectAttributes redirectAttributes) {
        try {
            lotRepo.deleteById(new LotProveidorId(idLot, proveidorCif));
            redirectAttributes.addFlashAttribute("success", "Lote '" + idLot + "' eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el lote: " + e.getMessage());
        }
        return "redirect:/lotes";
    }

    // UPDATE lote (modificar materia prima, proveedor, unidades, estado)
    @PostMapping("/actualizar/{proveidorCif}/{idLot}")
    public String actualizarLot(@PathVariable String proveidorCif, @PathVariable String idLot,
                                @RequestParam Long materiaId,
                                @RequestParam String nouProveidorCif,
                                @RequestParam Integer unitats,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<LotProveidor> optLot = lotRepo.findById(new LotProveidorId(idLot, proveidorCif));
            if (optLot.isPresent()) {
                LotProveidor lote = optLot.get();
                lote.setMateriaPrimera(materiaRepo.findById(materiaId).orElse(lote.getMateriaPrimera()));
                // Update proveedor on the albara
                lote.getAlbara().setProveidor(proveidorRepo.findById(nouProveidorCif).orElse(lote.getAlbara().getProveidor()));
                lote.setUnitats(unitats);
                lotRepo.save(lote);
                redirectAttributes.addFlashAttribute("success", "Lote '" + idLot + "' actualizado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Lote no encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo actualizar el lote: " + e.getMessage());
        }
        return "redirect:/lotes";
    }

    // TRAZABILIDAD (RF20)
    @GetMapping("/tracabilitat/{proveidorCif}/{idLot}")
    public String veureTracabilitat(@PathVariable String proveidorCif, @PathVariable String idLot, Model model, RedirectAttributes redirectAttributes) {
        Optional<LotProveidor> optLot = lotRepo.findById(new LotProveidorId(idLot, proveidorCif));
        if (optLot.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Lote no encontrado.");
            return "redirect:/lotes";
        }
        
        LotProveidor lote = optLot.get();
        model.addAttribute("lote", lote);

        if (lote.getEstat() == EstatLot.EN_ESTOC || lote.getDataObertura() == null) {
            model.addAttribute("liniesTracabilitat", List.of());
            return "tracabilitat-lot";
        }

        LocalDateTime start = lote.getDataObertura().toLocalDate().atStartOfDay();
        LocalDateTime end;
        if (lote.getEstat() == EstatLot.OBERT || lote.getDataAcabament() == null) {
            end = LocalDate.now().atTime(LocalTime.MAX); // Up to 23:59:59 of today if open
        } else {
            end = lote.getDataAcabament().toLocalDate().atTime(LocalTime.MAX); // Up to 23:59:59 of exact day of closure
        }

        List<LiniaClient> linies = liniaClientRepo.findTrazabilidadByDates(start, end);
        model.addAttribute("liniesTracabilitat", linies);

        return "tracabilitat-lot";
    }
}