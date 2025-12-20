package com.example.moviehub.api.controller.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthRestController {

    public record MeResponse(String username) {}

    @GetMapping("/api/auth/me")
    public MeResponse me(Authentication auth) {
        return new MeResponse(auth.getName());
    }
}
