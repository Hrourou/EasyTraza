/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
/**
 *
 * @author HAMZA
 * Entitat física o jurídica a la que se li lliura el producte final.
 */
@Entity
public class Client {

    @Id
    @Column(length = 9)
    private String nif;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String cognoms;

    @Column(nullable = false)
    private String adreca;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 9)
    private String telefon;

    // Aquest camp és opcional 
    private String observacions; 

    // Constructor buit per a Hibernate
    public Client() {}

    // Getters i Setters
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCognoms() { return cognoms; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }
    public String getAdreca() { return adreca; }
    public void setAdreca(String adreca) { this.adreca = adreca; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getObservacions() { return observacions; }
    public void setObservacions(String observacions) { this.observacions = observacions; }
}