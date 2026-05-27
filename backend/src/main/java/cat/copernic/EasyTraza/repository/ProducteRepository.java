/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.Producte;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author HAMZA
 */
public interface ProducteRepository extends JpaRepository<Producte, Long> {
    boolean existsByDescripcio(String descripcio);
}
