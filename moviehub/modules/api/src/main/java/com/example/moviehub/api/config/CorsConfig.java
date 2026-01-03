package com.example.moviehub.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                // API aplikacji - pozwala frontendowi  na komunikację z backendem
                registry.addMapping("/api/**") // Dotyczy endpointów /api/*
                        .allowedOriginPatterns("http://localhost:517*") // Dopuszcza localhost na porcie 517x
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Dozwolone metody HTTP
                        .allowedHeaders("*") // Dowolne nagłówki w requestach
                        .allowCredentials(true); // Pozwala na cookies / auth headers

                // Actuator - opcjonalnie do monitoringu
                registry.addMapping("/actuator/**") // Dotyczy endpointów /actuator/*
                        .allowedOriginPatterns("http://localhost:517*")
                        .allowedMethods("GET") // Tylko odczyt
                        .allowedHeaders("*"); // Dowolne nagłówki
            }
        };
    }
}
