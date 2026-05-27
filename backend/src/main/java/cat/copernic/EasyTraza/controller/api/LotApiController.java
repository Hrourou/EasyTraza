/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller.api;

import cat.copernic.EasyTraza.business.LotProveidorService;
import cat.copernic.EasyTraza.entities.LotProveidor;
import cat.copernic.EasyTraza.enums.EstatLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 *
 * @author HAMZA
 */
@RestController
@RequestMapping("/api/lots")
public class LotApiController {

    @Autowired
    private LotProveidorService lotService;

    // Llista de lots segons l'estat per al mòbil (SENSE BUCLE INFINIT)
    @GetMapping("/estat/{estat}")
    public ResponseEntity<List<Map<String, Object>>> llistarPerEstat(@PathVariable EstatLot estat) {
        List<LotProveidor> lots = lotService.obtenirLotsPerEstat(estat);
        
        // Transformem manualment per evitar el bucle recursiu d'Hibernate
        List<Map<String, Object>> response = lots.stream().map(lot -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", lot.getIdLot()); 
            map.put("proveidorCif", lot.getProveidorCif());
            if (lot.getAlbara() != null && lot.getAlbara().getProveidor() != null) {
                map.put("proveidorNom", lot.getAlbara().getProveidor().getNom());
            }
            map.put("estat", lot.getEstat().name());
            map.put("quantitat", lot.getQuantitat());
            
            if (lot.getMateriaPrimera() != null) {
                Map<String, Object> materiaMap = new HashMap<>();
                materiaMap.put("id", lot.getMateriaPrimera().getId());
                materiaMap.put("nom", lot.getMateriaPrimera().getNom());
                map.put("materiaPrimera", materiaMap);
            }
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // AHORA RECIBE PROVEIDOR_CIF Y ID_LOT
    @PostMapping("/{proveidorCif}/{idLot}/iniciar")
    public ResponseEntity<?> iniciar(@PathVariable String proveidorCif, @PathVariable String idLot, @RequestParam(defaultValue = "false") boolean confirmar, @RequestParam(required = false) String nifUsuari) {
        try {
            lotService.iniciarLot(proveidorCif, idLot, confirmar, nifUsuari);
            return ResponseEntity.ok(Map.of("message", "Lot iniciat correctament"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // AHORA RECIBE PROVEIDOR_CIF Y ID_LOT
    @PostMapping("/{proveidorCif}/{idLot}/finalitzar")
    public ResponseEntity<?> finalitzar(@PathVariable String proveidorCif, @PathVariable String idLot, @RequestParam(required = false) String nifUsuari) {
        try {
            lotService.finalitzarLot(proveidorCif, idLot, nifUsuari);
            return ResponseEntity.ok(Map.of("message", "Lot finalitzat correctament"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}