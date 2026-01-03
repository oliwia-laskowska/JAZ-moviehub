package com.example.moviehub.integration.client;

import com.example.moviehub.integration.model.ExternalFilm;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

public class ExternalMovieClient {

    private final RestClient restClient; // Klient HTTP do komunikacji z zewnętrznym API

    public ExternalMovieClient(RestClient restClient) {
        this.restClient = restClient;
    }

    // Pobiera listę filmów z endpointu /films i mapuje na obiekty ExternalFilm
    public List<ExternalFilm> fetchFilms() {
        ExternalFilm[] films = restClient.get()
                .uri("/films") // Ścieżka endpointu
                .retrieve() // wykonuje request i przygotowuje response
                .body(ExternalFilm[].class); // mapuje JSON na tablicę ExternalFilm

        // Zwraca pustą listę jeśli API zwróci null
        return films == null ? List.of() : Arrays.asList(films);
    }
}
