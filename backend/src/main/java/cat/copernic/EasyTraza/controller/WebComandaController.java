/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.ComandaService;
import cat.copernic.EasyTraza.dto.ComandaFormDTO;
import cat.copernic.EasyTraza.dto.LiniaComandaDTO;
import cat.copernic.EasyTraza.entities.Comanda;
import cat.copernic.EasyTraza.entities.LiniaComanda;
import cat.copernic.EasyTraza.repository.ClientRepository;
import cat.copernic.EasyTraza.repository.ProducteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/comandes")
public class WebComandaController {

    @Autowired private ComandaService comandaService;
    @Autowired private ClientRepository clientRepo;
    @Autowired private ProducteRepository producteRepo;

    // 1. PANTALLA PRINCIPAL (listado + formulario crear)
    @GetMapping
    public String veurePantalla(Model model) {
        model.addAttribute("comandes", comandaService.getAllComandes());
        model.addAttribute("clients", clientRepo.findAll());
        model.addAttribute("productes", producteRepo.findAll());

        ComandaFormDTO dto = new ComandaFormDTO();
        dto.getLinies().add(new LiniaComandaDTO());
        model.addAttribute("comandaDTO", dto);
        model.addAttribute("isEdit", false);

        return "comandes";
    }

    // 2. GUARDAR (Crear / Editar)
    @PostMapping("/save")
    public String guardarComanda(@ModelAttribute("comandaDTO") ComandaFormDTO dto, Model model) {
        try {
            comandaService.crearComanda(dto);
            return "redirect:/comandes?success=guardat";
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("comandes", comandaService.getAllComandes());
            model.addAttribute("clients", clientRepo.findAll());
            model.addAttribute("productes", producteRepo.findAll());
            model.addAttribute("isEdit", dto.isEdit());
            return "comandes";
        }
    }

    // 3. PANTALLA EDITAR (carga el formulario con los datos de la comanda)
    @GetMapping("/editar")
    public String editarComanda(@RequestParam String nif,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                Model model) {
        try {
            Comanda comanda = comandaService.getComandaByNifAndData(nif, data)
                    .orElseThrow(() -> new RuntimeException("Comanda no trobada."));

            ComandaFormDTO dto = new ComandaFormDTO();
            dto.setNifClient(comanda.getClient().getNif());
            dto.setDataComanda(comanda.getDataComanda());
            dto.setEdit(true);

            for (LiniaComanda linia : comanda.getLinies()) {
                LiniaComandaDTO lDto = new LiniaComandaDTO();
                lDto.setProducteId(linia.getProducte().getId());
                lDto.setQuantitat(linia.getQuantitat());
                dto.getLinies().add(lDto);
            }

            model.addAttribute("comandaDTO", dto);
            model.addAttribute("comandes", comandaService.getAllComandes());
            model.addAttribute("clients", clientRepo.findAll());
            model.addAttribute("productes", producteRepo.findAll());
            model.addAttribute("isEdit", true);
            model.addAttribute("openEditModal", true);

            return "comandes";
        } catch (Exception e) {
            return "redirect:/comandes?error=No_s_ha_trobat_comanda";
        }
    }

    // 4. ELIMINAR
    @PostMapping("/delete")
    public String eliminarComanda(@RequestParam String nif,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            comandaService.eliminarComanda(nif, data);
            return "redirect:/comandes?success=eliminat";
        } catch (Exception e) {
            return "redirect:/comandes?error=" + e.getMessage();
        }
    }

    // 5. COMPROVAR COMANDA (usado por el JavaScript de albaraclient.html para precarga automática)
    @GetMapping("/comprovar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> comprovarComanda(
            @RequestParam String nifClient,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        Optional<Comanda> optComanda = comandaService.getComandaByNifAndData(nifClient, data);
        Map<String, Object> response = new HashMap<>();

        if (optComanda.isPresent()) {
            Comanda comanda = optComanda.get();
            response.put("found", true);

            List<Map<String, Object>> linies = new ArrayList<>();
            for (LiniaComanda linia : comanda.getLinies()) {
                Map<String, Object> liniaMap = new HashMap<>();
                liniaMap.put("producteId", linia.getProducte().getId());
                liniaMap.put("producteNom", linia.getProducte().getNom());
                liniaMap.put("quantitat", linia.getQuantitat());
                linies.add(liniaMap);
            }
            response.put("linies", linies);
        } else {
            response.put("found", false);
            response.put("linies", List.of());
        }

        return ResponseEntity.ok(response);
    }
}
