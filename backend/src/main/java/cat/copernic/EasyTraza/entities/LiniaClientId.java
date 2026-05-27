/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDateTime;
/**
 *
 * @author HAMZA
 */
public class LiniaClientId implements Serializable {
    private AlbaraClientId albaraClient; // Hereda la clave compuesta del padre
    private Long producte; 

    public LiniaClientId() {}

    public AlbaraClientId getAlbaraClient() { return albaraClient; }
    public void setAlbaraClient(AlbaraClientId albaraClient) { this.albaraClient = albaraClient; }

    public Long getProducte() { return producte; }
    public void setProducte(Long producte) { this.producte = producte; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiniaClientId that = (LiniaClientId) o;
        return Objects.equals(albaraClient, that.albaraClient) && 
               Objects.equals(producte, that.producte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albaraClient, producte);
    }
}