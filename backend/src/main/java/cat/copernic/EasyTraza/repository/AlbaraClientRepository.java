/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.AlbaraClient;
import cat.copernic.EasyTraza.entities.AlbaraClientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author HAMZA
 */
@Repository
public interface AlbaraClientRepository extends JpaRepository<AlbaraClient, AlbaraClientId> {
    
    // Método útil para listar solo los albaranes pendientes (para poder editarlos)
    List<AlbaraClient> findByEstat(cat.copernic.EasyTraza.enums.EstatAlbaraClient estat);
    
    // Método para buscar todos los albaranes de un cliente en concreto
    List<AlbaraClient> findByClientNif(String nif);
    
    // Método que busca el albarán ignorando los nanosegundos (busca dentro del mismo segundo exacto)
    @Query("SELECT a FROM AlbaraClient a WHERE a.client.nif = :nif AND a.dataProduccio >= :data AND a.dataProduccio < :nextSecond")
    Optional<AlbaraClient> findByNifAndDataIgnoringNanos(@Param("nif") String nif, @Param("data") LocalDateTime data, @Param("nextSecond") LocalDateTime nextSecond);
}