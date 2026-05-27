/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import jakarta.persistence.*;

/**
 *
 * @author HAMZA
 * Entitat que representa un proveïdor a la base de dades.
 * Gestiona la informació de contacte i dades 
 */
@Entity
public class Proveidor {

    @Id // El CIF es la Primary Key
    @Column(length = 9)
    private String cif;

    @Column(nullable = false)
    private String nom;

    private String telefon;
    
    // --- NUEVOS CAMPOS AÑADIDOS ---
    private String email;
    private String direccio;
    
    @Column(length = 500) // Le damos más espacio por si escriben mucho
    private String observacions;
    // ------------------------------

    private String registreSanitari;

    public Proveidor() {}

    public Proveidor(String cif, String nom) {
        this.cif = cif;
        this.nom = nom;
    }

    // --- Getters i Setters ---
    public String getCif() { return cif; }
    public void setCif(String cif) { this.cif = cif; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccio() { return direccio; }
    public void setDireccio(String direccio) { this.direccio = direccio; }

    public String getObservacions() { return observacions; }
    public void setObservacions(String observacions) { this.observacions = observacions; }

    public String getRegistreSanitari() { return registreSanitari; }
    public void setRegistreSanitari(String registreSanitari) { this.registreSanitari = registreSanitari; }
}