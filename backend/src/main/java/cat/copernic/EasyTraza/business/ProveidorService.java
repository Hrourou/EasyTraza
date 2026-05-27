/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;
import cat.copernic.EasyTraza.dto.ProveidorDTO;
import cat.copernic.EasyTraza.entities.Proveidor;
import cat.copernic.EasyTraza.repository.AlbaraProveidorRepository;
import cat.copernic.EasyTraza.repository.ProveidorRepository;
import cat.copernic.EasyTraza.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author HAMZA
 * Servei que gestiona la lògica de negoci dels proveïdors.
 */
@Service
public class ProveidorService {

    @Autowired
    private ProveidorRepository proveidorRepository;

    @Autowired
    private AlbaraProveidorRepository albaraProveidorRepository;

    private ProveidorDTO mapToDTO(Proveidor p) {
        return new ProveidorDTO(p.getCif(), p.getNom(), p.getTelefon(), p.getEmail(), p.getDireccio(), p.getObservacions(), p.getRegistreSanitari());
    }

    public List<ProveidorDTO> getAllProveidors() {
        return proveidorRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ProveidorDTO getProveidorById(String cif) {
        return proveidorRepository.findById(cif).map(this::mapToDTO).orElse(null);
    }

    public boolean teAlbaransAssociats(String cif) {
        return albaraProveidorRepository.existsByProveidor_Cif(cif);
    }

    public ProveidorDTO createProveidor(ProveidorDTO dto) {
        // 1. Validar NIF/CIF matemáticamente
        if (!Validator.validarNifCif(dto.getCif())) {
            throw new RuntimeException("El CIF/NIF introduït no és vàlid matemàticament.");
        }
        
        // 2. Comprobar si ya existe
        if (proveidorRepository.existsById(dto.getCif())) {
            throw new RuntimeException("Ja existeix un proveïdor amb aquest CIF/NIF.");
        }
        
        Proveidor p = new Proveidor(dto.getCif().toUpperCase(), dto.getNom());
        p.setTelefon(dto.getTelefon());
        p.setEmail(dto.getEmail());
        p.setDireccio(dto.getDireccio());
        p.setObservacions(dto.getObservacions());
        p.setRegistreSanitari(dto.getRegistreSanitari());
        
        return mapToDTO(proveidorRepository.save(p));
    }

    public ProveidorDTO updateProveidor(String cif, ProveidorDTO dto) {
        Proveidor existent = proveidorRepository.findById(cif)
                .orElseThrow(() -> new RuntimeException("Proveïdor no trobat"));

        existent.setNom(dto.getNom());
        existent.setTelefon(dto.getTelefon());
        existent.setEmail(dto.getEmail());
        existent.setDireccio(dto.getDireccio());
        existent.setObservacions(dto.getObservacions());
        existent.setRegistreSanitari(dto.getRegistreSanitari());
        
        return mapToDTO(proveidorRepository.save(existent));
    }

    public void deleteProveidor(String cif) {
        if (!proveidorRepository.existsById(cif)) {
            throw new RuntimeException("Proveïdor no trobat");
        }
        if (albaraProveidorRepository.existsByProveidor_Cif(cif)) {
            throw new RuntimeException("No es pot esborrar aquest proveïdor perquè té albarans associats a ell.");
        }
        proveidorRepository.deleteById(cif);
    }
}