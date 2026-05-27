/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.Proveidor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author HAMZA
 */
public interface ProveidorRepository extends JpaRepository<Proveidor, String> {
    // JpaRepository<Entidad, Tipo_de_la_PK>
}