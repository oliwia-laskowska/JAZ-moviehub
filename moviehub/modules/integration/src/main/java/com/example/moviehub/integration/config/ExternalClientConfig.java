package com.example.moviehub.integration.config;

import com.example.moviehub.integration.client.ExternalMovieClient;
import com.example.moviehub.integration.client.TmdbClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration // Konfiguracja beanów klientów do zewnętrznych API
public class ExternalClientConfig {

    @Bean("ghibliRestClient") // RestClient do Studio Ghibli API
    RestClient ghibliRestClient() {
        return RestClient.builder()
                .baseUrl("https://ghibliapi.vercel.app") // stały baseUrl do Ghibli
                .build();
    }

    @Bean("tmdbRestClient") // RestClient do TMDB (baseUrl z application.properties/yml)
    RestClient tmdbRestClient(@Value("${tmdb.base-url}") String baseUrl) {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean // Klient integracyjny dla Ghibli (/films)
    ExternalMovieClient externalMovieClient(
            @Qualifier("ghibliRestClient") RestClient ghibliRestClient
    ) {
        return new ExternalMovieClient(ghibliRestClient);
    }

    @Bean // Klient integracyjny dla TMDB (popular + credits)
    TmdbClient tmdbClient(
            @Qualifier("tmdbRestClient") RestClient tmdbRestClient,
            @Value("${tmdb.api-key}") String apiKey, // klucz API z configu
            @Value("${tmdb.language:en-US}") String language // język odpowiedzi
    ) {
        return new TmdbClient(tmdbRestClient, apiKey, language);
    }
}
