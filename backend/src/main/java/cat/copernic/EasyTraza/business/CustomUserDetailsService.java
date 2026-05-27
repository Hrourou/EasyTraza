/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.business;
import cat.copernic.EasyTraza.entities.Usuari;
import cat.copernic.EasyTraza.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 *
 * @author HAMZA
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuariRepository usuariRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // En tu login.html pasas el email, así que buscamos por email
        Usuari usuari = usuariRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuari no trobat amb email: " + email));

        // Convertimos tu UserRole a un formato que Spring Security entienda (ROLE_ADMIN o ROLE_TREBALLADOR)
        String rolString = "ROLE_" + usuari.getRol().name();
        
        // Devolvemos un objeto 'User' de Spring Security
        return new User(
                usuari.getEmail(),
                usuari.getPassword(), // Esta contraseña debe estar hasheada en BD con BCrypt
                Collections.singleton(new SimpleGrantedAuthority(rolString))
        );
    }
}