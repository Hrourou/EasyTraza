/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

import cat.copernic.EasyTraza.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
/**
 *
 * @author HAMZA
 */
public class UsuariDTO {

    private Long id;

    @NotBlank(message = "L'email és obligatori")
    @Email(message = "Format d'email invàlid")
    private String email;

    @NotBlank(message = "El nom és obligatori")
    private String nom;

    @NotBlank(message = "El cognom és obligatori")
    private String cognom;

    @NotBlank(message = "El DNI és obligatori")
    private String dni;

    private String direccio;
    private String telefon;

    @NotBlank(message = "La contrasenya és obligatòria")
    private String password;

    private UserRole rol;

    private String fotoPerfilUrl;

    public UsuariDTO() {}

    // --- Getters i Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCognom() { return cognom; }
    public void setCognom(String cognom) { this.cognom = cognom; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getDireccio() { return direccio; }
    public void setDireccio(String direccio) { this.direccio = direccio; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRol() { return rol; }
    public void setRol(UserRole rol) { this.rol = rol; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }
    public void setFotoPerfilUrl(String fotoPerfilUrl) { this.fotoPerfilUrl = fotoPerfilUrl; }
}