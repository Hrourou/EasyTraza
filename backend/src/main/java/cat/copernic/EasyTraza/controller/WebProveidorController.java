/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.business.ProveidorService;
import cat.copernic.EasyTraza.dto.ProveidorDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/proveidors")
public class WebProveidorController {

    @Autowired
    private ProveidorService proveidorService;

    private void populateModelForProveidorsList(Model model) {
        List<ProveidorDTO> proveidors = proveidorService.getAllProveidors();
        model.addAttribute("proveidors", proveidors);
        // Build a map of cif -> hasAlbarans for conditional delete button
        Map<String, Boolean> proveidorsAmbAlbarans = proveidors.stream()
                .collect(Collectors.toMap(ProveidorDTO::getCif, p -> proveidorService.teAlbaransAssociats(p.getCif())));
        model.addAttribute("proveidorsAmbAlbarans", proveidorsAmbAlbarans);
    }

    @GetMapping
    public String showProveidorsList(Model model) {
        populateModelForProveidorsList(model);
        model.addAttribute("proveidor", new ProveidorDTO());
        return "proveidors"; 
    }

    @PostMapping("/save")
    public String saveProveidor(@Valid @ModelAttribute("proveidor") ProveidorDTO proveidorDTO, BindingResult result, Model model) {
        // Si fallan las validaciones básicas (NotBlank, Email)
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            populateModelForProveidorsList(model);
            return "proveidors"; 
        }

        try {
            // Si falla el validador NIF/CIF o ya existe, salta al catch
            proveidorService.createProveidor(proveidorDTO);
            return "redirect:/proveidors";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            populateModelForProveidorsList(model);
            return "proveidors"; // Mantiene los datos en el formulario
        }
    }

    @PostMapping("/update")
    public String updateProveidor(@Valid @ModelAttribute("proveidor") ProveidorDTO proveidorDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            populateModelForProveidorsList(model);
            return "proveidors";
        }

        try {
            proveidorService.updateProveidor(proveidorDTO.getCif(), proveidorDTO);
            return "redirect:/proveidors";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            populateModelForProveidorsList(model);
            return "proveidors";
        }
    }

    @PostMapping("/delete/{cif}")
    public String deleteProveidor(@PathVariable String cif, Model model) {
        try {
            proveidorService.deleteProveidor(cif);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            populateModelForProveidorsList(model);
            model.addAttribute("proveidor", new ProveidorDTO());
            return "proveidors";
        }
        return "redirect:/proveidors";
    }
}