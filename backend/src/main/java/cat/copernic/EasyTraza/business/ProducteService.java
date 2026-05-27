/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.dto.ProducteDTO;
import cat.copernic.EasyTraza.entities.Producte;
import cat.copernic.EasyTraza.repository.ProducteRepository;
import cat.copernic.EasyTraza.repository.LiniaClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author HAMZA
 */
@Service
public class ProducteService {

    @Autowired
    private ProducteRepository producteRepository;

    @Autowired
    private LiniaClientRepository liniaClientRepository;

    private ProducteDTO mapToDTO(Producte p) {
        ProducteDTO dto = new ProducteDTO();
        dto.setId(p.getId());
        dto.setNom(p.getNom());
        dto.setDescripcio(p.getDescripcio());
        return dto;
    }

    public List<ProducteDTO> getAllProductes() {
        return producteRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ProducteDTO getProducteById(Long id) {
        return producteRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    public ProducteDTO createProducte(ProducteDTO dto) {
        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new RuntimeException("El nom és obligatori");
        }
        if (dto.getDescripcio() == null || dto.getDescripcio().isBlank()) {
            throw new RuntimeException("La descripció és obligatòria");
        }
        if (producteRepository.existsByDescripcio(dto.getDescripcio())) {
            throw new RuntimeException("Ja existeix un producte amb aquesta descripció");
        }

        Producte producte = new Producte(dto.getNom(), dto.getDescripcio());
        return mapToDTO(producteRepository.save(producte));
    }

    public ProducteDTO updateProducte(Long id, ProducteDTO dto) {
        Producte existent = producteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producte no trobat"));

        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new RuntimeException("El nom és obligatori");
        }

        if (dto.getDescripcio() == null || dto.getDescripcio().isBlank()) {
            throw new RuntimeException("La descripció és obligatòria");
        }

        if (!dto.getDescripcio().equals(existent.getDescripcio()) && 
            producteRepository.existsByDescripcio(dto.getDescripcio())) {
            throw new RuntimeException("Ja existeix un producte amb aquesta descripció");
        }

        existent.setNom(dto.getNom());
        existent.setDescripcio(dto.getDescripcio());
        return mapToDTO(producteRepository.save(existent));
    }

    public boolean teAlbaransAssociats(Long id) {
        return liniaClientRepository.existsByProducte_Id(id);
    }

    public void deleteProducte(Long id) {
        if (!producteRepository.existsById(id)) {
            throw new RuntimeException("Producte no trobat");
        }
        if (teAlbaransAssociats(id)) {
            throw new RuntimeException("No es pot esborrar aquest producte perquè té albarans associats.");
        }
        producteRepository.deleteById(id);
    }
}