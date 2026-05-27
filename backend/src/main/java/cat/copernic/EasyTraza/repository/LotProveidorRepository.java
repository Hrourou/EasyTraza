/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.LotProveidor;
import cat.copernic.EasyTraza.entities.LotProveidorId;
import cat.copernic.EasyTraza.enums.EstatLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
/**
 *
 * @author HAMZA
 */
public interface LotProveidorRepository extends JpaRepository<LotProveidor, LotProveidorId> {

    // Filtra los lotes por Albarán, Proveedor, Materia Prima, Estado, Código de Lote y Fecha de Entrada
    @Query("SELECT l FROM LotProveidor l " +
           "WHERE (:albaraId IS NULL OR l.albara.id = :albaraId) " +
           "AND (:cif IS NULL OR :cif = '' OR l.albara.proveidor.cif = :cif) " +
           "AND (:materiaId IS NULL OR l.materiaPrimera.id = :materiaId) " +
           "AND (:estat IS NULL OR l.estat = :estat) " +
           "AND (:codiLot IS NULL OR :codiLot = '' OR l.idLot LIKE CONCAT('%', :codiLot, '%')) " +
           "AND (:dataEntradaDesde IS NULL OR l.albara.dataRecepcio >= :dataEntradaDesde) " +
           "AND (:dataEntradaHasta IS NULL OR l.albara.dataRecepcio <= :dataEntradaHasta)")
    List<LotProveidor> findByFiltres(@Param("albaraId") Long albaraId, 
                                     @Param("cif") String cif, 
                                     @Param("materiaId") Long materiaId,
                                     @Param("estat") EstatLot estat,
                                     @Param("codiLot") String codiLot,
                                     @Param("dataEntradaDesde") LocalDate dataEntradaDesde,
                                     @Param("dataEntradaHasta") LocalDate dataEntradaHasta,
                                     Sort sort);
                                     
    List<LotProveidor> findByEstat(EstatLot estat);

    // Delete protection queries
    boolean existsByMateriaPrimera_Id(Long materiaId);
    
    boolean existsByAlbara_Proveidor_Cif(String cif);
    
    boolean existsByAlbara_IdAndEstatIn(Long albaraId, Collection<EstatLot> estats);
}