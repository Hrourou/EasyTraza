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
public class MateriaPrimeraDTO {

    private Long id;

    private String nom;

    // AÑADIDO: Validación de no nulo/blanco
    @NotBlank(message = "La descripció és obligatòria")
    private String descripcio;

    public MateriaPrimeraDTO() {}

    public MateriaPrimeraDTO(Long id, String nom, String descripcio) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
    }

    // --- Getters i Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }
}