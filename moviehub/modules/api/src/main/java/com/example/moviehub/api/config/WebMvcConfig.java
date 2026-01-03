package com.example.moviehub.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // Konfiguracja MVC
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // CORS tylko dla endpointów API
                .allowedOrigins("http://localhost:5173", "http://localhost:3000") // Frontend
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS") // Dozwolone metody HTTP
                .allowedHeaders("*") // Dowolne nagłówki
                .allowCredentials(true); // Pozwala na cookies / sesję / auth
    }
}
