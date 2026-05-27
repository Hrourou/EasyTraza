/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;
import jakarta.persistence.*;
/**
 *
 * @author HAMZA
 * Línea de una comanda: un producto y su cantidad encargada.
 */

@Entity
@IdClass(LiniaComandaId.class)
public class LiniaComanda {

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "comanda_client_nif", referencedColumnName = "client_nif"),
        @JoinColumn(name = "comanda_data_comanda", referencedColumnName = "data_comanda")
    })
    private Comanda comanda;

    @Id
    @ManyToOne
    @JoinColumn(name = "producte_id")
    private Producte producte;

    @Column(nullable = false)
    private Integer quantitat;

    // --- Getters y Setters ---
    public Comanda getComanda() { return comanda; }
    public void setComanda(Comanda comanda) { this.comanda = comanda; }

    public Producte getProducte() { return producte; }
    public void setProducte(Producte producte) { this.producte = producte; }

    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }
}
