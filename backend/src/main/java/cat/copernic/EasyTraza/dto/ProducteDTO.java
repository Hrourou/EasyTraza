/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

import jakarta.validation.constraints.NotBlank;

/**
 *
 * @author HAMZA
 */
public class ProducteDTO {

    private Long id;
    
    private String nom;

    @NotBlank(message = "La descripció és obligatòria")
    private String descripcio;

    public ProducteDTO() {}

    // --- Getters i Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }
}