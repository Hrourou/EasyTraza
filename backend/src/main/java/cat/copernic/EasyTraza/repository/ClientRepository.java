/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
/**
 *
 * @author HAMZA
 */
public interface ClientRepository extends JpaRepository<Client, String> {

    // Busca la palabra clave en NIF, nom, cognoms, telefon o email ignorando mayúsculas
    @Query("SELECT c FROM Client c WHERE " +
           "LOWER(c.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.cognoms) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.nif) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.telefon) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Client> searchByKeyword(@Param("keyword") String keyword);
}