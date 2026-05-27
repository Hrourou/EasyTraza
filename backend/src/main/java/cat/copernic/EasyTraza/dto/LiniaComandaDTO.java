/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

/**
 *
 * @author HAMZA
 * DTO para una línea de comanda (producto + cantidad).
 */
public class LiniaComandaDTO {

    private Long producteId;
    private Integer quantitat;

    // --- Getters y Setters ---
    public Long getProducteId() { return producteId; }
    public void setProducteId(Long producteId) { this.producteId = producteId; }

    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }
}
