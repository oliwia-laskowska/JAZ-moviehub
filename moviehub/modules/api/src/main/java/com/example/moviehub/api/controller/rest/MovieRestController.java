package com.example.moviehub.api.controller.rest;

import com.example.moviehub.common.error.MovieDto;
import com.example.moviehub.domain.mapper.MovieMapper;
import com.example.moviehub.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API dla zasobu "movies"
@RequestMapping("/api/movies") // Bazowa ścieżka dla endpointów filmów
public class MovieRestController {

    private final MovieService movieService; // logika biznesowa dla filmów

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping // GET /api/movies -> lista filmów
    public List<MovieDto> list() {
        // Pobiera filmy z serwisu i mapuje encje na DTO
        return movieService.list().stream()
                .map(MovieMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}") // GET /api/movies/{id} -> szczegóły filmu
    public MovieDto get(@PathVariable Long id) {
        // Pobiera film po ID i zwraca jako DTO
        return MovieMapper.toDto(movieService.getById(id));
    }
}
