/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.EasyTraza.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 *
 * @author HAMZA
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- PORTERO 1: PARA LA APP MÓVIL (API) ---
    @Bean
    @Order(1) // Este se ejecuta primero
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            // Aplica SOLO a rutas que empiecen por /api/
            .securityMatcher("/api/**") 
            .csrf(csrf -> csrf.disable()) // Las APIs REST no usan CSRF normalmente
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Por ahora, dejamos la API móvil abierta al 100% (Netflix style)
            );
        return http.build();
    }

    // --- PORTERO 2: PARA LA WEB (THYMELEAF) ---
    @Bean
    @Order(2) // Este se ejecuta después para el resto de rutas web
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivado por ahora para evitar problemas con tus POSTs
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/images/**", "/js/**", "/uploads/**").permitAll() // Dejar cargar los estilos e imágenes
                .requestMatchers("/login", "/usuaris/save-worker", "/recuperar-password", "/verificar-codigo", "/guardar-nueva-password").permitAll() // Permitir ver la página de login y registrarse
                .requestMatchers("/error").permitAll() // Permitir ver errores en vez de redirigir a login
                
                // ═══ RUTAS SOLO PARA ADMIN ═══
                .requestMatchers("/admin/**").hasRole("ADMIN")           // Usuarios, Clientes, Catálogos
                .requestMatchers("/proveidors/**").hasRole("ADMIN")      // Proveedores
                .requestMatchers("/ph-agua/**").hasRole("ADMIN")         // PH Agua
                
                // ═══ RUTAS PARA TODOS (ADMIN + TREBALLADOR) ═══
                // /recepcio, /lotes, /historial-albarans, /albara-client/**, /informe, /perfil
                // Estas quedan cubiertas por .anyRequest().authenticated()
                
                .anyRequest().authenticated() // El resto de la web REQUIERE estar logueado
            )
            .formLogin(form -> form
                .loginPage("/login") // Nuestra vista personalizada de Login
                .loginProcessingUrl("/login-process") // La URL a la que tu HTML hace el POST
                .usernameParameter("email") // En tu HTML se llama name="email"
                .passwordParameter("password") // En tu HTML se llama name="password"
                .successHandler(successHandler) // Redirige según el rol: ADMIN → /proveidors, TREBALLADOR → /recepcio
                .failureUrl("/login?error=true") // A dónde ir si fallas
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL para cerrar sesión
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .sessionManagement(session -> session
                .invalidSessionUrl("/login?timeout=true")
            );

        return http.build();
    }
}
