/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

/**
 *
 * @author HAMZA
 * Objecte de transferència de dades per a Client.
 */
public class ClientDTO {
    private String nif;
    private String nom;
    private String cognoms;
    private String adreca;
    private String email;
    private String telefon;
    private String observacions;

    // Getters I Setters
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