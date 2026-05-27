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
 */
public class LotProveidorId implements Serializable {
    private String idLot;
    private String proveidorCif;

    public LotProveidorId() {
    }

    public LotProveidorId(String idLot, String proveidorCif) {
        this.idLot = idLot;
        this.proveidorCif = proveidorCif;
    }

    public String getIdLot() {
        return idLot;
    }

    public void setIdLot(String idLot) {
        this.idLot = idLot;
    }

    public String getProveidorCif() {
        return proveidorCif;
    }

    public void setProveidorCif(String proveidorCif) {
        this.proveidorCif = proveidorCif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LotProveidorId that = (LotProveidorId) o;
        return Objects.equals(idLot, that.idLot) && Objects.equals(proveidorCif, that.proveidorCif);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLot, proveidorCif);
    }
}
