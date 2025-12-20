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

                // PUBLIC API
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("http://localhost:517*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                // ACTUATOR (opcjonalnie)
                registry.addMapping("/actuator/**")
                        .allowedOriginPatterns("http://localhost:517*")
                        .allowedMethods("GET")
                        .allowedHeaders("*");
            }
        };
    }
}
