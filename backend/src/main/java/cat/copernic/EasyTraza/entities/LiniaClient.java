/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.entities;
import cat.copernic.EasyTraza.enums.EstatLiniaClient;
import jakarta.persistence.*;

/**
 *
 * @author HAMZA
 */
@Entity
@IdClass(LiniaClientId.class)
public class LiniaClient {

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "albara_client_nif", referencedColumnName = "client_nif"),
        // AQUÍ ESTÁ LA MAGIA QUE ARREGLA TU ERROR:
        @JoinColumn(name = "albara_data_produccio", referencedColumnName = "data_produccio") 
    })
    private AlbaraClient albaraClient;

    @Id
    @ManyToOne
    @JoinColumn(name = "producte_id")
    private Producte producte;

    private Integer quantitat;
    
    private String operari; // Guardaremos el nombre o DNI del operario

    @Enumerated(EnumType.STRING)
    private EstatLiniaClient estatLinia = EstatLiniaClient.SENSE_LLIURAR;

    // --- Getters y Setters ---
    public AlbaraClient getAlbaraClient() { return albaraClient; }
    public void setAlbaraClient(AlbaraClient albaraClient) { this.albaraClient = albaraClient; }

    public Producte getProducte() { return producte; }
    public void setProducte(Producte producte) { this.producte = producte; }

    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }
    
    public String getOperari() { return operari; }
    public void setOperari(String operari) { this.operari = operari; }

    public EstatLiniaClient getEstatLinia() { return estatLinia; }
    public void setEstatLinia(EstatLiniaClient estatLinia) { this.estatLinia = estatLinia; }
}