package com.example.moviehub.integration.client;

import com.example.moviehub.integration.model.ExternalFilm;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

public class ExternalMovieClient {

    private final RestClient restClient;

    public ExternalMovieClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<ExternalFilm> fetchFilms() {
        ExternalFilm[] films = restClient.get()
                .uri("/films")
                .retrieve()
                .body(ExternalFilm[].class);
        return films == null ? List.of() : Arrays.asList(films);
    }
}
