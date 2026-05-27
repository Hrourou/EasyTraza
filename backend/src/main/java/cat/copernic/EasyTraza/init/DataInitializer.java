/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.init;

import cat.copernic.EasyTraza.entities.Usuari;
import cat.copernic.EasyTraza.enums.UserRole;
import cat.copernic.EasyTraza.repository.UsuariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
/**
 *
 * @author HAMZA
 * Esta clase se ejecuta automáticamente al arrancar Spring Boot.
 * Garantiza que siempre exista al menos un Administrador en el sistema.
 * 
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Comprobamos si no hay ningún administrador en la base de datos
        if (usuariRepository.countByRol(UserRole.ADMIN) == 0) {
            
            System.out.println("No se han encontrado administradores en la Base de Datos.");
            System.out.println(" Creando el Administrador por defecto...");

            Usuari adminMaestro = new Usuari();
            adminMaestro.setDni("00000000T"); // Un DNI genérico
            adminMaestro.setNom("Admin");
            adminMaestro.setCognom("EazyTraza");
            adminMaestro.setEmail("easytraza@gmail.com");
            
            // ¡IMPORTANTE! Hasheamos la contraseña por defecto para que funcione el Login
            adminMaestro.setPassword(passwordEncoder.encode("admin123")); 
            
            adminMaestro.setRol(UserRole.ADMIN);
            adminMaestro.setTelefon("000000000");
            adminMaestro.setDireccio("Sistema EasyTraza");

            // Guardamos directamente en el repositorio (saltándonos el Service para evitar bucles o validaciones web)
            usuariRepository.save(adminMaestro);

            
        }
    }
}