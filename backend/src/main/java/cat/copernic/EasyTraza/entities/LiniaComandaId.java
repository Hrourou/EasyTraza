/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import java.io.Serializable;
import java.util.Objects;
/**
 *
 * @author HAMZA
 * Clave compuesta para LiniaComanda (Comanda compuesta + Producte).
 */

public class LiniaComandaId implements Serializable {
    private ComandaId comanda;
    private Long producte;

    public LiniaComandaId() {}

    public ComandaId getComanda() { return comanda; }
    public void setComanda(ComandaId comanda) { this.comanda = comanda; }

    public Long getProducte() { return producte; }
    public void setProducte(Long producte) { this.producte = producte; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiniaComandaId that = (LiniaComandaId) o;
        return Objects.equals(comanda, that.comanda) && Objects.equals(producte, that.producte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comanda, producte);
    }
}
