package cat.copernic.EasyTraza.controller;

import cat.copernic.EasyTraza.dto.VendesDiaDTO;
import cat.copernic.EasyTraza.repository.LiniaClientRepository;
import cat.copernic.EasyTraza.repository.ProducteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HAMZA
 * Controlador de la vista web /informe i endpoint de dades per al gràfic.
 */
@Controller
public class WebInformeController {

    @Autowired
    private ProducteRepository producteRepo;

    @Autowired
    private LiniaClientRepository liniaClientRepo;

    // Vista Thymeleaf
    @GetMapping("/informe")
    public String veureInforme(Model model) {
        model.addAttribute("productes", producteRepo.findAll());
        return "informe";
    }

    // Endpoint de dades per al gràfic (cridat via fetch des del JS)
    @GetMapping("/informe/vendes")
    @ResponseBody
    public List<VendesDiaDTO> getVendesMensuals(
            @RequestParam String anyMes,
            @RequestParam(required = false) Long idProducte) {

        String[] parts = anyMes.split("-");
        int any = Integer.parseInt(parts[0]);
        int mes = Integer.parseInt(parts[1]);

        List<Object[]> rows;
        if (idProducte == null) {
            rows = liniaClientRepo.findVendesDiariesByMes(any, mes);
        } else {
            rows = liniaClientRepo.findVendesDiariesByMesAndProducte(any, mes, idProducte);
        }

        // Convertir Object[] a VendesDiaDTO
        List<VendesDiaDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            int dia = ((Number) row[0]).intValue();
            long total = ((Number) row[1]).longValue();
            result.add(new VendesDiaDTO(dia, total));
        }
        return result;
    }
}
