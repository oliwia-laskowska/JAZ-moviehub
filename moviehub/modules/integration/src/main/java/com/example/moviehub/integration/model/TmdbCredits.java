package com.example.moviehub.integration.model;

import java.util.List;

// Model odpowiedzi TMDB dla endpointu /movie/{id}/credits
public record TmdbCredits(
        Long id, // ID filmu w TMDB
        List<CrewMember> crew // lista członków ekipy (reżyser, scenarzysta itd.)
) {
    // Pojedynczy członek ekipy  w TMDB
    public record CrewMember(
            String name, // imię i nazwisko
            String job, // rola
            String department // dział
    ) {}
}
