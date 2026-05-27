/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller.api;

import cat.copernic.EasyTraza.business.MateriaPrimeraService;
import cat.copernic.EasyTraza.business.OcrService;
import cat.copernic.EasyTraza.business.ProveidorService;
import cat.copernic.EasyTraza.business.RecepcioService;
import cat.copernic.EasyTraza.dto.AlbaraLotFormDTO;
import cat.copernic.EasyTraza.business.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
/**
 *
 * @author HAMZA
 * 
 * 
 */
@RestController
@RequestMapping("/api/recepcio")
public class RecepcioApiController {

    @Autowired private OcrService ocrService;
    @Autowired private RecepcioService recepcioService;
    @Autowired private ProveidorService proveidorService;
    @Autowired private MateriaPrimeraService materiaPrimeraService;
    @Autowired private FileStorageService fileStorageService;

    // 1. Enviar lista de proveedores al móvil
    @GetMapping("/proveidors")
    public ResponseEntity<?> getProveidorsDesDelMobil() {
        return ResponseEntity.ok(proveidorService.getAllProveidors());
    }

    // 2. Enviar lista de materias primas al móvil
    @GetMapping("/materies")
    public ResponseEntity<?> getMateriesDesDelMobil() {
        return ResponseEntity.ok(materiaPrimeraService.getAllMateries());
    }

    // 3. El móvil manda la foto -> Devolvemos el JSON extraído
    @PostMapping("/ocr")
    public ResponseEntity<?> processarImatgeDesDelMobil(@RequestParam("file") MultipartFile file) {
        try {
            // Guardar imagen en el servidor
            String fotoUrl = fileStorageService.saveAlbaranImage(file);
            
            // Procesar con Gemini
            AlbaraLotFormDTO resultatOCR = ocrService.llegirAlbara(file);
            
            // Adjuntar la URL de la imagen guardada
            resultatOCR.setFotoAlbaraUrl(fotoUrl);
            
            return ResponseEntity.ok(resultatOCR);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4. El móvil manda el JSON revisado y confirmado -> Guardamos en Base de Datos
    @PostMapping("/save")
    public ResponseEntity<?> guardarAlbaraDesDelMobil(@RequestBody AlbaraLotFormDTO dto) {
        try {
            recepcioService.guardarRecepcio(dto);
            return ResponseEntity.ok(Map.of("message", "Albarán y lotes guardados correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}