/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;

import cat.copernic.EasyTraza.dto.ComandaFormDTO;
import cat.copernic.EasyTraza.dto.LiniaComandaDTO;
import cat.copernic.EasyTraza.entities.*;
import cat.copernic.EasyTraza.repository.ComandaRepository;
import cat.copernic.EasyTraza.repository.ClientRepository;
import cat.copernic.EasyTraza.repository.ProducteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author HAMZA
 */
@Service
public class ComandaService {

    @Autowired private ComandaRepository comandaRepo;
    @Autowired private ClientRepository clientRepo;
    @Autowired private ProducteRepository producteRepo;

    public List<Comanda> getAllComandes() {
        return comandaRepo.findAll();
    }

    public Optional<Comanda> getComandaByNifAndData(String nif, LocalDate data) {
        return comandaRepo.findByClientNifAndDataComanda(nif, data);
    }

    @Transactional
    public void crearComanda(ComandaFormDTO dto) {
        if (dto.getNifClient() == null || dto.getNifClient().isEmpty()) {
            throw new RuntimeException("Client obligatori.");
        }
        if (dto.getDataComanda() == null) {
            throw new RuntimeException("Data de la comanda obligatòria.");
        }

        Optional<Comanda> existing = comandaRepo.findByClientNifAndDataComanda(dto.getNifClient(), dto.getDataComanda());

        Comanda comanda;
        if (existing.isPresent()) {
            if (dto.isEdit()) {
                // Modo edición: actualizamos la comanda existente
                comanda = existing.get();
                comanda.getLinies().clear();
            } else {
                throw new RuntimeException("Ja existeix una comanda per aquest client en aquesta data.");
            }
        } else {
            comanda = new Comanda();
            Client client = clientRepo.findById(dto.getNifClient())
                    .orElseThrow(() -> new RuntimeException("Client no trobat."));
            comanda.setClient(client);
            comanda.setDataComanda(dto.getDataComanda());
        }

        // Añadir las líneas del formulario
        for (LiniaComandaDTO liniaDTO : dto.getLinies()) {
            if (liniaDTO.getProducteId() != null && liniaDTO.getQuantitat() != null && liniaDTO.getQuantitat() > 0) {
                Producte producte = producteRepo.findById(liniaDTO.getProducteId())
                        .orElseThrow(() -> new RuntimeException("Producte no trobat."));
                LiniaComanda linia = new LiniaComanda();
                linia.setProducte(producte);
                linia.setQuantitat(liniaDTO.getQuantitat());
                comanda.addLinia(linia);
            }
        }

        if (comanda.getLinies().isEmpty()) {
            throw new RuntimeException("Ha de contenir al menys un producte amb quantitat > 0.");
        }

        comandaRepo.save(comanda);
    }

    @Transactional
    public void eliminarComanda(String nif, LocalDate data) {
        Comanda comanda = comandaRepo.findByClientNifAndDataComanda(nif, data)
                .orElseThrow(() -> new RuntimeException("Comanda no trobada."));
        comandaRepo.delete(comanda);
    }
}
