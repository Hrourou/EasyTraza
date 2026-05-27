/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HAMZA
 */
public class AlbaraLotFormDTO {
    
    // Forzamos a que nazcan vacíos
    private String cifProveidor = "";
    private String nomProveidor = "";
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataRecepcio;
    
    private String fotoAlbaraUrl;
    
    private List<LotFormDTO> liniesLote = new ArrayList<>();

    // --- Getters y Setters ---
    public String getCifProveidor() { return cifProveidor; }
    public void setCifProveidor(String cifProveidor) { this.cifProveidor = cifProveidor; }

    public LocalDate getDataRecepcio() { return dataRecepcio; }
    public void setDataRecepcio(LocalDate dataRecepcio) { this.dataRecepcio = dataRecepcio; }

    public List<LotFormDTO> getLiniesLote() { return liniesLote; }
    public void setLiniesLote(List<LotFormDTO> liniesLote) { this.liniesLote = liniesLote; }
    
    public String getNomProveidor() { return nomProveidor; }
    public void setNomProveidor(String nomProveidor) { this.nomProveidor = nomProveidor; }

    public String getFotoAlbaraUrl() { return fotoAlbaraUrl; }
    public void setFotoAlbaraUrl(String fotoAlbaraUrl) { this.fotoAlbaraUrl = fotoAlbaraUrl; }
}