/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

/**
 *
 * @author HAMZA
 */
public class VendesDiaDTO {

    private int dia;
    private long total;

    public VendesDiaDTO() {}

    public VendesDiaDTO(int dia, long total) {
        this.dia = dia;
        this.total = total;
    }

    public int getDia() { return dia; }
    public void setDia(int dia) { this.dia = dia; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
}
