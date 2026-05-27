/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.dto.MateriaPrimeraDTO;
import cat.copernic.EasyTraza.entities.MateriaPrimera;
import cat.copernic.EasyTraza.repository.LotProveidorRepository;
import cat.copernic.EasyTraza.repository.MateriaPrimeraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author HAMZA
 * conversión entre Entidad y DTO, y las operaciones CRUD
 */
@Service
public class MateriaPrimeraService {

    @Autowired
    private MateriaPrimeraRepository materiaPrimeraRepository;

    @Autowired
    private LotProveidorRepository lotProveidorRepository;

    private MateriaPrimeraDTO mapToDTO(MateriaPrimera mp) {
        return new MateriaPrimeraDTO(mp.getId(), mp.getNom(), mp.getDescripcio());
    }

    private MateriaPrimera mapToEntity(MateriaPrimeraDTO dto) {
        MateriaPrimera mp = new MateriaPrimera(dto.getNom(), dto.getDescripcio());
        mp.setId(dto.getId());
        return mp;
    }

    public List<MateriaPrimeraDTO> getAllMateries() {
        return materiaPrimeraRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MateriaPrimeraDTO getMateriaById(Long id) {
        return materiaPrimeraRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    public boolean teLotsAssociats(Long id) {
        return lotProveidorRepository.existsByMateriaPrimera_Id(id);
    }

    public MateriaPrimeraDTO createMateria(MateriaPrimeraDTO dto) {
        // VALIDACIÓN: Descripción obligatoria
        if (dto.getDescripcio() == null || dto.getDescripcio().isBlank()) {
            throw new RuntimeException("La descripció és obligatòria per a la matèria prima");
        }
        // VALIDACIÓN: Descripción única
        if (materiaPrimeraRepository.existsByDescripcio(dto.getDescripcio())) {
            throw new RuntimeException("Ja existeix una matèria prima amb aquesta descripció");
        }

        MateriaPrimera saved = materiaPrimeraRepository.save(mapToEntity(dto));
        return mapToDTO(saved);
    }

    public MateriaPrimeraDTO updateMateria(Long id, MateriaPrimeraDTO dto) {
        MateriaPrimera existent = materiaPrimeraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matèria primera no trobada"));

        // VALIDACIÓN: Descripción obligatoria
        if (dto.getDescripcio() == null || dto.getDescripcio().isBlank()) {
            throw new RuntimeException("La descripció és obligatòria per a la matèria prima");
        }

        // VALIDACIÓN: Descripción única (ignorando si es la misma que ya tenía)
        if (!dto.getDescripcio().equals(existent.getDescripcio()) &&
            materiaPrimeraRepository.existsByDescripcio(dto.getDescripcio())) {
            throw new RuntimeException("Ja existeix una matèria prima amb aquesta descripció");
        }

        existent.setNom(dto.getNom());
        existent.setDescripcio(dto.getDescripcio());

        MateriaPrimera updated = materiaPrimeraRepository.save(existent);
        return mapToDTO(updated);
    }

    public void deleteMateria(Long id) {
        if (!materiaPrimeraRepository.existsById(id)) {
            throw new RuntimeException("Matèria primera no trobada");
        }
        if (lotProveidorRepository.existsByMateriaPrimera_Id(id)) {
            throw new RuntimeException("No es pot esborrar aquesta matèria primera perquè té lots associats.");
        }
        materiaPrimeraRepository.deleteById(id);
    }
}