/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.LiniaClient;
import cat.copernic.EasyTraza.entities.LiniaClientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author HAMZA
 */
@Repository
public interface LiniaClientRepository extends JpaRepository<LiniaClient, LiniaClientId> {
    
    @Query("SELECT lc FROM LiniaClient lc JOIN lc.albaraClient ac WHERE ac.dataProduccio >= :start AND ac.dataProduccio <= :end ORDER BY ac.dataProduccio DESC")
    List<LiniaClient> findTrazabilidadByDates(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Vendes diàries de TOTS els productes en un mes concret (Native MySQL)
    @Query(value = "SELECT DAY(ac.data_produccio) AS dia, COALESCE(SUM(lc.quantitat), 0) AS total " +
           "FROM linia_client lc JOIN albara_client ac ON lc.albara_client_nif = ac.client_nif AND lc.albara_data_produccio = ac.data_produccio " +
           "WHERE YEAR(ac.data_produccio) = :any AND MONTH(ac.data_produccio) = :mes " +
           "GROUP BY DAY(ac.data_produccio) ORDER BY dia", nativeQuery = true)
    List<Object[]> findVendesDiariesByMes(@Param("any") int any, @Param("mes") int mes);

    // Vendes diàries d'UN producte concret en un mes concret (Native MySQL)
    @Query(value = "SELECT DAY(ac.data_produccio) AS dia, COALESCE(SUM(lc.quantitat), 0) AS total " +
           "FROM linia_client lc JOIN albara_client ac ON lc.albara_client_nif = ac.client_nif AND lc.albara_data_produccio = ac.data_produccio " +
           "WHERE YEAR(ac.data_produccio) = :any AND MONTH(ac.data_produccio) = :mes AND lc.producte_id = :idProducte " +
           "GROUP BY DAY(ac.data_produccio) ORDER BY dia", nativeQuery = true)
    List<Object[]> findVendesDiariesByMesAndProducte(@Param("any") int any, @Param("mes") int mes, @Param("idProducte") Long idProducte);

    boolean existsByProducte_Id(Long producteId);
}
