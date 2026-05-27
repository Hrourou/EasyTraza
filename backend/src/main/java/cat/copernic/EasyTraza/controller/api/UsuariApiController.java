/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.controller.api;

import cat.copernic.EasyTraza.business.UsuariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 * @author HAMZA
 */
@RestController
@RequestMapping("/api/usuaris")
public class UsuariApiController {

    @Autowired
    private UsuariService usuariService;

    @Autowired
    private cat.copernic.EasyTraza.business.FileStorageService fileStorageService;

    // Aquest endpoint retorna tots els usuaris en format JSON per al mòbil
    @GetMapping
    public ResponseEntity<?> getUsuarisPerAlMobil() {
        return ResponseEntity.ok(usuariService.getAllUsuaris());
    }

    @org.springframework.web.bind.annotation.PostMapping("/{email}/foto")
    public ResponseEntity<?> uploadFotoPerfil(
            @org.springframework.web.bind.annotation.PathVariable String email, 
            @org.springframework.web.bind.annotation.RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            cat.copernic.EasyTraza.dto.UsuariDTO usuari = usuariService.getUsuariByEmail(email);
            if (usuari == null) {
                return ResponseEntity.badRequest().body(java.util.Map.of("error", "Usuari no trobat"));
            }

            String fotoUrl = fileStorageService.saveProfileImage(file);
            usuari.setFotoPerfilUrl(fotoUrl);
            usuariService.updateUsuari(usuari.getDni(), usuari, email);

            return ResponseEntity.ok(java.util.Map.of(
                "message", "Foto de perfil actualizada",
                "fotoPerfilUrl", fotoUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("error", e.getMessage()));
        }
    }
}