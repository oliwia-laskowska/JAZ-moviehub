package com.example.moviehub.api.controller.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // REST endpointy związane z autoryzacją
public class AuthRestController {

    // Prosta odpowiedź JSON z danymi o zalogowanym użytkowniku
    public record MeResponse(String username) {}

    @GetMapping("/api/auth/me") // Zwraca aktualnie zalogowanego użytkownika
    public MeResponse me(Authentication auth) {
        return new MeResponse(auth.getName()); // auth.getName() = username z kontekstu Spring Security
    }
}
