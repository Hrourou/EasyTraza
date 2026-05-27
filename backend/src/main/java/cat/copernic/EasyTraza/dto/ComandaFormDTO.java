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
 * DTO para el formulario de crear/editar una comanda.
 */
public class ComandaFormDTO {

    private String nifClient = "";

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataComanda;

    private boolean isEdit = false;

    private List<LiniaComandaDTO> linies = new ArrayList<>();

    // --- Getters y Setters ---
    public String getNifClient() { return nifClient; }
    public void setNifClient(String nifClient) { this.nifClient = nifClient; }

    public LocalDate getDataComanda() { return dataComanda; }
    public void setDataComanda(LocalDate dataComanda) { this.dataComanda = dataComanda; }

    public boolean isEdit() { return isEdit; }
    public void setEdit(boolean edit) { isEdit = edit; }

    public List<LiniaComandaDTO> getLinies() { return linies; }
    public void setLinies(List<LiniaComandaDTO> linies) { this.linies = linies; }
}
