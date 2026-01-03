package com.example.moviehub.integration.client;

import com.example.moviehub.integration.model.TmdbCredits;
import com.example.moviehub.integration.model.TmdbMovie;
import com.example.moviehub.integration.model.TmdbMoviePage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

public class TmdbClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String language;

    public TmdbClient(RestClient tmdbRestClient,
                      @Value("${tmdb.api-key}") String apiKey,
                      @Value("${tmdb.language:en-US}") String language) {
        this.restClient = tmdbRestClient;
        this.apiKey = apiKey;
        this.language = language;
    }

    public List<TmdbMovie> fetchPopular(int pages) {
        List<TmdbMovie> all = new ArrayList<>();
        for (int page = 1; page <= pages; page++) {
            final int p = page;

            TmdbMoviePage resp = restClient.get()
                    .uri(uri -> uri
                            .path("/movie/popular")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", language)
                            .queryParam("page", p)
                            .build())
                    .retrieve()
                    .body(TmdbMoviePage.class);

            if (resp == null || resp.results() == null || resp.results().isEmpty()) break;
            all.addAll(resp.results());
        }
        return all;
    }

    public String fetchDirectorName(long movieId) {
        TmdbCredits credits = restClient.get()
                .uri(uri -> uri
                        .path("/movie/{id}/credits")
                        .queryParam("api_key", apiKey)
                        .queryParam("language", language)
                        .build(movieId))
                .retrieve()
                .body(TmdbCredits.class);

        if (credits == null || credits.crew() == null) return null;

        return credits.crew().stream()
                .filter(c -> "Director".equalsIgnoreCase(c.job()))
                .map(TmdbCredits.CrewMember::name)
                .findFirst()
                .orElse(null);
    }
}
