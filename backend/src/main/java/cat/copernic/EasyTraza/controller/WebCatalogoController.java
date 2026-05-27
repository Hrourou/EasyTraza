package cat.copernic.EasyTraza.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import cat.copernic.EasyTraza.business.MateriaPrimeraService;
import cat.copernic.EasyTraza.business.ProducteService;
import cat.copernic.EasyTraza.dto.MateriaPrimeraDTO;
import cat.copernic.EasyTraza.dto.ProducteDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author HAMZA
 */
@Controller
@RequestMapping("/admin/catalogo")
public class WebCatalogoController {

    @Autowired
    private MateriaPrimeraService materiaPrimeraService;

    @Autowired
    private ProducteService producteService;

    // Redirige /admin/catalogo a materias primas por defecto
    @GetMapping
    public String redirigirCatalogo() {
        return "redirect:/admin/catalogo/materias";
    }

    // Página de Materias Primas
    @GetMapping("/materias")
    public String veureMaterias(Model model) {
        List<MateriaPrimeraDTO> materies = materiaPrimeraService.getAllMateries();
        model.addAttribute("materies", materies);
        // Build a map of materiaId -> hasLots for conditional delete button
        Map<Long, Boolean> materiesAmbLots = materies.stream()
                .collect(Collectors.toMap(MateriaPrimeraDTO::getId, m -> materiaPrimeraService.teLotsAssociats(m.getId())));
        model.addAttribute("materiesAmbLots", materiesAmbLots);
        return "CatalogoMateriasPrimas";
    }

    // Página de Productos Finales
    @GetMapping("/productos")
    public String veureProductos(Model model) {
        List<ProducteDTO> productes = producteService.getAllProductes();
        model.addAttribute("productes", productes);
        Map<Long, Boolean> productesAmbAlbarans = productes.stream()
                .collect(Collectors.toMap(ProducteDTO::getId, p -> producteService.teAlbaransAssociats(p.getId())));
        model.addAttribute("productesAmbAlbarans", productesAmbAlbarans);
        return "CatalogoProductesFinales";
    }

    // ----- RUTAS PARA MATERIAS PRIMAS -----
    @PostMapping("/materia/save")
    public String guardarMateria(@ModelAttribute MateriaPrimeraDTO dto, Model model) {
        try {
            materiaPrimeraService.createMateria(dto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureMaterias(model);
        }
        return "redirect:/admin/catalogo/materias";
    }

    @PostMapping("/materia/update")
    public String actualizarMateria(@ModelAttribute MateriaPrimeraDTO dto, Model model) {
        try {
            materiaPrimeraService.updateMateria(dto.getId(), dto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureMaterias(model);
        }
        return "redirect:/admin/catalogo/materias";
    }

    @PostMapping("/materia/delete")
    public String eliminarMateria(@RequestParam("id") Long id, Model model) {
        try {
            materiaPrimeraService.deleteMateria(id);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureMaterias(model);
        }
        return "redirect:/admin/catalogo/materias";
    }

    // ----- RUTAS PARA PRODUCTOS FINALES -----
    @PostMapping("/producte/save")
    public String guardarProducte(@ModelAttribute ProducteDTO dto, Model model) {
        try {
            producteService.createProducte(dto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureProductos(model);
        }
        return "redirect:/admin/catalogo/productos";
    }

    @PostMapping("/producte/update")
    public String actualizarProducte(@ModelAttribute ProducteDTO dto, Model model) {
        try {
            producteService.updateProducte(dto.getId(), dto);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureProductos(model);
        }
        return "redirect:/admin/catalogo/productos";
    }

    @PostMapping("/producte/delete")
    public String eliminarProducte(@RequestParam("id") Long id, Model model) {
        try {
            producteService.deleteProducte(id);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return veureProductos(model);
        }
        return "redirect:/admin/catalogo/productos";
    }
}