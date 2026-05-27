/*
 * Handler personalizado para redirigir al usuario según su rol después del login.
 * ADMIN → /proveidors (panel completo)
 * TREBALLADOR → /recepcio (panel limitado)
 */
package cat.copernic.EasyTraza.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author HAMZA
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = "/recepcio"; // Por defecto, los TREBALLADORS van a Recepción

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                redirectUrl = "/proveidors"; // Los ADMIN van a Proveedores (panel completo)
                break;
            }
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
