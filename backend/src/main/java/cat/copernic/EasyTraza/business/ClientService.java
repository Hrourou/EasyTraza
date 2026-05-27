package cat.copernic.EasyTraza.business;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import cat.copernic.EasyTraza.dto.ClientDTO;
import cat.copernic.EasyTraza.entities.Client;
import cat.copernic.EasyTraza.repository.ClientRepository;
import cat.copernic.EasyTraza.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 *
 * @author HAMZA
 */
@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepo;

    /**
     * Retorna la llista de tots els clients (sense filtre).
     */
    public List<Client> getAllClients() {
        return clientRepo.findAll();
    }

    /**
     * Retorna la llista de clients, filtrada si hi ha una paraula clau.
     */
    public List<Client> getClients(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return clientRepo.searchByKeyword(keyword);
        }
        return clientRepo.findAll();
    }

    /**
     * Crea un client nou realitzant les validacions pertinents.
     */
    public void createClient(ClientDTO dto) {
        if (!Validator.validarNif(dto.getNif())) {
            throw new RuntimeException("El NIF introduït no és vàlid.");
        }
        if (clientRepo.existsById(dto.getNif())) {
            throw new RuntimeException("Ja existeix un client amb aquest NIF.");
        }
        if (!Validator.validarEmail(dto.getEmail())) {
            throw new RuntimeException("El format del correu electrònic no és correcte.");
        }
        if (!Validator.validarTelefon(dto.getTelefon())) {
            throw new RuntimeException("El telèfon ha de tenir 9 dígits.");
        }

        Client client = new Client();
        mapDtoToEntity(dto, client);
        clientRepo.save(client);
    }

    /**
     * Actualitza les dades d'un client existent.
     */
    public void updateClient(String nif, ClientDTO dto) {
        Client client = clientRepo.findById(nif)
                .orElseThrow(() -> new RuntimeException("Client no trobat."));

        if (!Validator.validarEmail(dto.getEmail())) {
            throw new RuntimeException("El format del correu electrònic no és correcte.");
        }
        if (!Validator.validarTelefon(dto.getTelefon())) {
            throw new RuntimeException("El telèfon ha de tenir 9 dígits.");
        }

        mapDtoToEntity(dto, client);
        clientRepo.save(client);
    }

    /**
     * Elimina un client per la seva clau primària.
     */
    public void deleteClient(String nif) {
        clientRepo.deleteById(nif);
    }

    // Mètode auxiliar per passar dades del DTO a l'Entitat
    private void mapDtoToEntity(ClientDTO dto, Client client) {
        if (client.getNif() == null) {
            client.setNif(dto.getNif());
        }
        client.setNom(dto.getNom());
        client.setCognoms(dto.getCognoms());
        client.setAdreca(dto.getAdreca());
        client.setEmail(dto.getEmail());
        client.setTelefon(dto.getTelefon());
        client.setObservacions(dto.getObservacions());
    }
}