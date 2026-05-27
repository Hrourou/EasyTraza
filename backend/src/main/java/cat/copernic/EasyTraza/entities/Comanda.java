/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author HAMZA
 * Clave compuesta: (client_nif, dataComanda).
 * Entidad que representa un pedido anticipado (comanda) de un cliente.
 */


@Entity
@IdClass(ComandaId.class)
public class Comanda {

    @Id
    @ManyToOne
    @JoinColumn(name = "client_nif")
    private Client client;

    @Id
    @Column(name = "data_comanda")
    private LocalDate dataComanda;

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiniaComanda> linies = new ArrayList<>();

    // --- Getters y Setters ---
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public LocalDate getDataComanda() { return dataComanda; }
    public void setDataComanda(LocalDate dataComanda) { this.dataComanda = dataComanda; }

    public List<LiniaComanda> getLinies() { return linies; }
    public void setLinies(List<LiniaComanda> linies) { this.linies = linies; }

    public void addLinia(LiniaComanda linia) {
        linies.add(linia);
        linia.setComanda(this);
    }
}
