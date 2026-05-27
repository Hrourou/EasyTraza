/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
/**
 *
 * @author HAMZA
 * per enviar i rebre dades des del client
 * Data Transfer Object per a la classe Proveidor
 */
public class ProveidorDTO {

    @NotBlank(message = "El CIF/NIF és obligatori")
    private String cif;

    @NotBlank(message = "El nom no pot estar buit")
    private String nom;

    @NotBlank(message = "El telèfon no pot estar buit")
    private String telefon;

    @NotBlank(message = "L'email no pot estar buit")
    @Email(message = "El format de l'email no és vàlid")
    private String email;

    private String direccio;
    private String observacions;
    private String registreSanitari;

    public ProveidorDTO() {}

    public ProveidorDTO(String cif, String nom, String telefon, String email, String direccio, String observacions, String registreSanitari) {
        this.cif = cif;
        this.nom = nom;
        this.telefon = telefon;
        this.email = email;
        this.direccio = direccio;
        this.observacions = observacions;
        this.registreSanitari = registreSanitari;
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