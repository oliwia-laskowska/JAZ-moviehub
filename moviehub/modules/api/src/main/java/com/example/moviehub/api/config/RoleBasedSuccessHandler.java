package com.example.moviehub.api.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

// Handler wykonywany po udanym logowaniu
public class RoleBasedSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        // Sprawdza czy zalogowany użytkownik ma rolę ADMIN
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // Przekierowanie zależne od roli użytkownika
        if (isAdmin) {
            response.sendRedirect("/admin"); // Admin trafia do panelu admina
        } else {
            response.sendRedirect("/"); // Zwykły user trafia na stronę główną
        }
    }
}
