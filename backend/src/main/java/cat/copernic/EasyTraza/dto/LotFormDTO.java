/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

/**
 *
 * @author HAMZA
 */
public class LotFormDTO {
    
    // Forzamos a que nazcan como textos vacíos en lugar de 'null'
    private String nomMateriaPrimeraDetectada = ""; 
    private Long materiaPrimeraId = null; 
    private String idLot = ""; 
    private Integer unitats = null; 

    // --- Getters y Setters ---
    public String getNomMateriaPrimeraDetectada() { return nomMateriaPrimeraDetectada; }
    public void setNomMateriaPrimeraDetectada(String nomMateriaPrimeraDetectada) { this.nomMateriaPrimeraDetectada = nomMateriaPrimeraDetectada; }

    public Long getMateriaPrimeraId() { return materiaPrimeraId; }
    public void setMateriaPrimeraId(Long materiaPrimeraId) { this.materiaPrimeraId = materiaPrimeraId; }

    public String getIdLot() { return idLot; }
    public void setIdLot(String idLot) { this.idLot = idLot; }

    public Integer getUnitats() { return unitats; }
    public void setUnitats(Integer unitats) { this.unitats = unitats; }
}