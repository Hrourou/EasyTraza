/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.Comanda;
import cat.copernic.EasyTraza.entities.ComandaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
/**
 *
 * @author HAMZA
 */
@Repository
public interface ComandaRepository extends JpaRepository<Comanda, ComandaId> {

    Optional<Comanda> findByClientNifAndDataComanda(String nif, LocalDate dataComanda);
}
