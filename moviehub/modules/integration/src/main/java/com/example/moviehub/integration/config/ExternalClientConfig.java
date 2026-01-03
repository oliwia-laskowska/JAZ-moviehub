package com.example.moviehub.integration.config;

import com.example.moviehub.integration.client.ExternalMovieClient;
import com.example.moviehub.integration.client.TmdbClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ExternalClientConfig {

    @Bean("ghibliRestClient")
    RestClient ghibliRestClient() {
        return RestClient.builder()
                .baseUrl("https://ghibliapi.vercel.app")
                .build();
    }

    @Bean("tmdbRestClient")
    RestClient tmdbRestClient(@Value("${tmdb.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    ExternalMovieClient externalMovieClient(@Qualifier("ghibliRestClient") RestClient ghibliRestClient) {
        return new ExternalMovieClient(ghibliRestClient);
    }

    @Bean
    TmdbClient tmdbClient(@Qualifier("tmdbRestClient") RestClient tmdbRestClient,
                          @Value("${tmdb.api-key}") String apiKey,
                          @Value("${tmdb.language:en-US}") String language) {
        return new TmdbClient(tmdbRestClient, apiKey, language);
    }
}
