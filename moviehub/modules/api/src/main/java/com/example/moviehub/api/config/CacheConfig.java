package com.example.moviehub.api.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.time.Duration;

@Configuration
@EnableCaching // Włącza obsługę cache
public class CacheConfig {

    @Bean // Rejestruje CacheManager jako bean w kontekście Springa
    CacheManager cacheManager() {
        CaffeineCacheManager mgr = new CaffeineCacheManager(); // CacheManager oparty o Caffeine
        mgr.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1_000) // Maksymalnie 1000 wpisów w cache
                .expireAfterWrite(Duration.ofMinutes(10))); // Wpisy wygasają po 10 min od zapisu
        return mgr;
    }
}
