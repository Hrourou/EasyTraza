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
 */
@Entity
public class AlbaraProveidor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataRecepcio;

    // Relación: Muchos albaranes pueden pertenecer a un Proveedor
    @ManyToOne
    @JoinColumn(name = "proveidor_cif", nullable = false)
    private Proveidor proveidor;

    // Relación de composición: Un albarán contiene muchos Lotes
    // mappedBy indica que el dueño de la relación es el atributo "albara" en
    // LotProveidor
    // cascade = CascadeType.ALL permite que al guardar el albarán, se guarden sus
    // lotes automáticamente
    @OneToMany(mappedBy = "albara", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LotProveidor> lots = new ArrayList<>();

    @Column(name = "foto_albara_url")
    private String fotoAlbaraUrl;

    public AlbaraProveidor() {
        this.dataRecepcio = LocalDate.now(); // Por defecto la fecha actual
    }

    // Método de ayuda para añadir lotes y mantener la sincronización bidireccional
    public void addLot(LotProveidor lot) {
        lots.add(lot);
        lot.setAlbara(this);
    }

    // --- Getters y Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataRecepcio() {
        return dataRecepcio;
    }

    public void setDataRecepcio(LocalDate dataRecepcio) {
        this.dataRecepcio = dataRecepcio;
    }

    public Proveidor getProveidor() {
        return proveidor;
    }

    public void setProveidor(Proveidor proveidor) {
        this.proveidor = proveidor;
    }

    public List<LotProveidor> getLots() {
        return lots;
    }

    public void setLots(List<LotProveidor> lots) {
        this.lots = lots;
    }

    public String getFotoAlbaraUrl() {
        return fotoAlbaraUrl;
    }

    public void setFotoAlbaraUrl(String fotoAlbaraUrl) {
        this.fotoAlbaraUrl = fotoAlbaraUrl;
    }
}