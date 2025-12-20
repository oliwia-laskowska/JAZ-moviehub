package com.example.moviehub.service;

import com.example.moviehub.domain.model.Movie;
import com.example.moviehub.persistence.repository.MovieRepository;
import com.example.moviehub.service.cache.CacheNames;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Cacheable(cacheNames = CacheNames.MOVIES)
    public List<Movie> list() {
        return movieRepository.findAll();
    }

    @Cacheable(cacheNames = CacheNames.MOVIE_BY_ID, key = "#id")
    public Movie getById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id));
    }

    @CacheEvict(cacheNames = {CacheNames.MOVIES, CacheNames.MOVIE_BY_ID}, allEntries = true)
    public void evictCaches() {}
}
