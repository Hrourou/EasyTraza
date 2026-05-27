/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import jakarta.persistence.*;

/**
 *
 * @author HAMZA
 */
@Entity
public class MateriaPrimera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    // AÑADIDO: unique = true y nullable = false
    @Column(unique = true, nullable = false)
    private String descripcio;

    public MateriaPrimera() {}

    public MateriaPrimera(String nom, String descripcio) {
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