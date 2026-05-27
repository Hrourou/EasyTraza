/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.AlbaraProveidor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 *
 * @author HAMZA
 */
public interface AlbaraProveidorRepository extends JpaRepository<AlbaraProveidor, Long> {

    // Consulta dinámica: Filtra por CIF y/o por ID de Materia Prima. Ordenado por fecha descendente.
    @Query("SELECT DISTINCT a FROM AlbaraProveidor a LEFT JOIN a.lots l " +
           "WHERE (:cif IS NULL OR :cif = '' OR a.proveidor.cif = :cif) " +
           "AND (:materiaId IS NULL OR l.materiaPrimera.id = :materiaId) " +
           "ORDER BY a.dataRecepcio DESC")
    List<AlbaraProveidor> findByFiltres(@Param("cif") String cif, @Param("materiaId") Long materiaId);

    // Delete protection: check if a proveidor has any albarans
    boolean existsByProveidor_Cif(String cif);
}