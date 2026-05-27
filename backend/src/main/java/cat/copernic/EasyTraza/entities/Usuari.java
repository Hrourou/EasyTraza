/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;
import cat.copernic.EasyTraza.enums.UserRole;
import jakarta.persistence.*;

/**
 *
 * @author HAMZA
 * Entitat que representa un usuari/operari del sistema
 */
@Entity
@Table(name = "usuaris")
public class Usuari {

    @Id
    @Column(name = "dni", nullable = false, length = 9, unique = true)
    private String dni;

    
    @Column(name = "email", nullable = false)
    private String email;

    private String nom;
    private String cognom;
    
    private String direccio;
    @Column(name = "telefon")
    private String telefon;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole rol;

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    public Usuari() {}

    public Usuari(String email, String nom, String cognom, String dni, String direccio, String telefon, String password, UserRole rol) {
        this.email = email;
        this.nom = nom;
        this.cognom = cognom;
        this.dni = dni;
        this.direccio = direccio;
        this.telefon = telefon;
        this.password = password;
        this.rol = rol;
    }

    // --- Getters i Setters ---

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