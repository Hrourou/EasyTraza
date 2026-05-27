/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.repository;

import cat.copernic.EasyTraza.entities.Usuari;
import cat.copernic.EasyTraza.enums.UserRole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author HAMZA
 */
public interface UsuariRepository extends JpaRepository<Usuari, String> {
    
    // Cuenta cuántos usuarios hay de un rol específico (ej. ADMIN)
    long countByRol(UserRole rol);
    
    boolean existsByEmail(String email);
    Optional<Usuari> findByEmail(String email);
}