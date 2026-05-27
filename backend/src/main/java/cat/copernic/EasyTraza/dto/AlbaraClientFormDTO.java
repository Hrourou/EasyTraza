/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author HAMZA
 */
public class AlbaraClientFormDTO {
    
    private String nifClient = "";
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // Permite con y sin segundos
    private LocalDateTime dataProduccio;
    
    private boolean isEdit = false; // Flag para saber si estamos editando
    
    private List<LiniaClientDTO> linies = new ArrayList<>();

    // --- Getters y Setters ---
    public String getNifClient() { return nifClient; }
    public void setNifClient(String nifClient) { this.nifClient = nifClient; }

    public LocalDateTime getDataProduccio() { return dataProduccio; }
    public void setDataProduccio(LocalDateTime dataProduccio) { this.dataProduccio = dataProduccio; }

    public List<LiniaClientDTO> getLinies() { return linies; }
    public void setLinies(List<LiniaClientDTO> linies) { this.linies = linies; }

    public boolean isEdit() { return isEdit; }
    public void setEdit(boolean isEdit) { this.isEdit = isEdit; }
}