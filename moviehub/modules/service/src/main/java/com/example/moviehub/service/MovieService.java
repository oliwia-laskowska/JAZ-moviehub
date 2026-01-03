package com.example.moviehub.service;

import com.example.moviehub.domain.model.Movie;
import com.example.moviehub.persistence.repository.MovieRepository;
import com.example.moviehub.service.cache.CacheNames;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Warstwa serwisowa dla filmów
public class MovieService {

    private final MovieRepository movieRepository; // dostęp do bazy danych

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Cacheable(cacheNames = CacheNames.MOVIES) // Cache'uje listę filmów (klucz domyślny)
    public List<Movie> list() {
        return movieRepository.findAll(); // jeśli cache pusty -> pobiera z DB
    }

    @Cacheable(cacheNames = CacheNames.MOVIE_BY_ID, key = "#id") // Cache'uje film po ID
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id)); // 400 w GlobalExceptionHandler
    }

    @CacheEvict(
            cacheNames = {CacheNames.MOVIES, CacheNames.MOVIE_BY_ID},
            allEntries = true // czyści wszystkie wpisy w obu cache
    )
    public void evictCaches() {} // wywoływane np. po sync / update danych
}
