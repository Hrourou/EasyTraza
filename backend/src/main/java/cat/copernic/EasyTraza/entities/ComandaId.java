/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author HAMZA
 * Clave compuesta para la entidad Comanda (NIF del cliente + Fecha de la comanda).
 */
public class ComandaId implements Serializable {
    private String client; // Coincide con el nombre de la variable en Comanda
    private LocalDate dataComanda;

    public ComandaId() {}

    public ComandaId(String client, LocalDate dataComanda) {
        this.client = client;
        this.dataComanda = dataComanda;
    }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public LocalDate getDataComanda() { return dataComanda; }
    public void setDataComanda(LocalDate dataComanda) { this.dataComanda = dataComanda; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComandaId that = (ComandaId) o;
        return Objects.equals(client, that.client) && Objects.equals(dataComanda, that.dataComanda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, dataComanda);
    }
}
