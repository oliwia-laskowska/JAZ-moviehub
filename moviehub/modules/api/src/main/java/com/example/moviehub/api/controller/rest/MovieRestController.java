package com.example.moviehub.api.controller.rest;

import com.example.moviehub.domain.dto.MovieDto;
import com.example.moviehub.domain.mapper.MovieMapper;
import com.example.moviehub.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieRestController {

    private final MovieService movieService;

    public MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDto> list() {
        return movieService.list().stream().map(MovieMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public MovieDto get(@PathVariable Long id) {
        return MovieMapper.toDto(movieService.getById(id));
    }
}
