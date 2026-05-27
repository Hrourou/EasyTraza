/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;

import cat.copernic.EasyTraza.enums.EstatAlbaraClient;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author HAMZA
 */
@Entity
@IdClass(AlbaraClientId.class)
public class AlbaraClient {

    @Id
    @ManyToOne
    @JoinColumn(name = "client_nif")
    private Client client;

    @Id
    @Column(name = "data_produccio") // Aseguramos el nombre físico en la BD
    private LocalDateTime dataProduccio;

    @Enumerated(EnumType.STRING)
    private EstatAlbaraClient estat = EstatAlbaraClient.PENDENT_DE_LLIURAR;

    @OneToMany(mappedBy = "albaraClient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiniaClient> linies = new ArrayList<>();

    // --- Getters y Setters ---
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public LocalDateTime getDataProduccio() { return dataProduccio; }
    public void setDataProduccio(LocalDateTime dataProduccio) { this.dataProduccio = dataProduccio; }

    public EstatAlbaraClient getEstat() { return estat; }
    public void setEstat(EstatAlbaraClient estat) { this.estat = estat; }

    public List<LiniaClient> getLinies() { return linies; }
    public void setLinies(List<LiniaClient> linies) { this.linies = linies; }
    
    public void addLinia(LiniaClient linia) {
        linies.add(linia);
        linia.setAlbaraClient(this); // Al asociarlo, ya hereda la fecha automáticamente
    }
}