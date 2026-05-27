/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller;
import cat.copernic.EasyTraza.business.ProveidorService;
import cat.copernic.EasyTraza.dto.ProveidorDTO;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author HAMZA
 * Controlador REST per a la gestió de proveïdors.
 */
@RestController
@RequestMapping("/api/proveidors")
public class ProveidorController {

    @Autowired
    private ProveidorService proveidorService;

    /**
     * Obté tots els proveïdors.
     */
    @GetMapping
    public ResponseEntity<List<ProveidorDTO>> getAllProveidors() {
        return new ResponseEntity<>(proveidorService.getAllProveidors(), HttpStatus.OK);
    }

    /**
     * Obté un proveïdor específic per CIF (Ara és la Primary Key).
     */
    @GetMapping("/{cif}")
    public ResponseEntity<ProveidorDTO> getProveidorByCif(@PathVariable String cif) {
        try {
            ProveidorDTO proveidor = proveidorService.getProveidorById(cif);
            if (proveidor != null) {
                return new ResponseEntity<>(proveidor, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea un nou proveïdor. Ojo a la etiqueta @Valid per comprovar el format exacte del CIF.
     */
    @PostMapping
    public ResponseEntity<?> createProveidor(@Valid @RequestBody ProveidorDTO proveidorDTO) {
        try {
            ProveidorDTO created = proveidorService.createProveidor(proveidorDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si el CIF ja existeix, retornem un Bad Request amb el missatge d'error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualitza un proveïdor existent.
     */
    @PutMapping("/{cif}")
    public ResponseEntity<?> updateProveidor(@PathVariable String cif, @Valid @RequestBody ProveidorDTO proveidorDTO) {
        try {
            ProveidorDTO updated = proveidorService.updateProveidor(cif, proveidorDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            // Si no el troba o hi ha algun problema
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un proveïdor.
     */
    @DeleteMapping("/{cif}")
    public ResponseEntity<Void> deleteProveidor(@PathVariable String cif) {
        try {
            proveidorService.deleteProveidor(cif);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content si va bé
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}