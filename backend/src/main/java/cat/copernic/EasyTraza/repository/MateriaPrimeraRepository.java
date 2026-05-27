/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.MateriaPrimera;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author HAMZA
 */
public interface MateriaPrimeraRepository extends JpaRepository<MateriaPrimera, Long> {

    // Método para comprobar duplicados
    boolean existsByDescripcio(String descripcio);

    // Buscar por nombre
    Optional<MateriaPrimera> findByNom(String nom);
}
