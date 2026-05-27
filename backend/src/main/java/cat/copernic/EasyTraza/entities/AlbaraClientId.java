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
public class AlbaraClientId implements Serializable {
    private String client; // Coincide con el nombre de la variable en la Entidad
    private LocalDateTime dataProduccio;

    // Constructores, Getters, Setters, Equals y HashCode (Obligatorios para Ids compuestos)
    public AlbaraClientId() {}
    
    public AlbaraClientId(String client, LocalDateTime dataProduccio) {
        this.client = client;
        this.dataProduccio = dataProduccio;
    }

    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public LocalDateTime getDataProduccio() { return dataProduccio; }
    public void setDataProduccio(LocalDateTime dataProduccio) { this.dataProduccio = dataProduccio; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbaraClientId that = (AlbaraClientId) o;
        return Objects.equals(client, that.client) && Objects.equals(dataProduccio, that.dataProduccio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, dataProduccio);
    }
}