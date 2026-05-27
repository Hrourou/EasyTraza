/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

/**
 *
 * @author HAMZA
 */
public class LiniaClientDTO {
    
    private Long producteId;
    private Integer quantitat = null;
    
    // AÑADIDO: La variable operari con sus getters y setters
    private String operari = "";

    // --- Getters y Setters ---
    public Long getProducteId() { return producteId; }
    public void setProducteId(Long producteId) { this.producteId = producteId; }

    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }
    
    public String getOperari() { return operari; }
    public void setOperari(String operari) { this.operari = operari; }
}