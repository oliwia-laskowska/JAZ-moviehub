package com.example.moviehub.integration.client;

import com.example.moviehub.integration.model.TmdbCredits;
import com.example.moviehub.integration.model.TmdbMovie;
import com.example.moviehub.integration.model.TmdbMoviePage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

public class TmdbClient {

    private final RestClient restClient; // Klient HTTP do TMDB API
    private final String apiKey; // Klucz API z konfiguracji
    private final String language; // Język odpowiedzi

    public TmdbClient(
            RestClient tmdbRestClient,
            @Value("${tmdb.api-key}") String apiKey,
            @Value("${tmdb.language:en-US}") String language
    ) {
        this.restClient = tmdbRestClient;
        this.apiKey = apiKey;
        this.language = language;
    }

    // Pobiera popularne filmy z TMDB
    public List<TmdbMovie> fetchPopular(int pages) {
        List<TmdbMovie> all = new ArrayList<>();

        for (int page = 1; page <= pages; page++) {
            final int p = page; // effectively final dla lambdy

            TmdbMoviePage resp = restClient.get()
                    .uri(uri -> uri
                            .path("/movie/popular") // endpoint TMDB
                            .queryParam("api_key", apiKey) // auth przez API key
                            .queryParam("language", language) // język odpowiedzi
                            .queryParam("page", p) // numer strony
                            .build())
                    .retrieve()
                    .body(TmdbMoviePage.class); // mapowanie JSON -> record TmdbMoviePage

            // Jeśli API zwróci pustą stronę -> kończymy pętlę
            if (resp == null || resp.results() == null || resp.results().isEmpty()) break;

            all.addAll(resp.results()); // dopisuje wyniki ze strony do listy
        }

        return all; // lista filmów z wielu stron
    }

    // Pobiera nazwę reżysera filmu
    public String fetchDirectorName(long movieId) {
        TmdbCredits credits = restClient.get()
                .uri(uri -> uri
                        .path("/movie/{id}/credits") // endpoint credits
                        .queryParam("api_key", apiKey)
                        .queryParam("language", language)
                        .build(movieId)) // podstawia {id}
                .retrieve()
                .body(TmdbCredits.class); // mapowanie JSON -> TmdbCredits

        if (credits == null || credits.crew() == null) return null; // brak danych

        // Szuka osoby z job="Director" i zwraca jej nazwę
        return credits.crew().stream()
                .filter(c -> "Director".equalsIgnoreCase(c.job()))
                .map(TmdbCredits.CrewMember::name)
                .findFirst()
                .orElse(null);
    }
}
