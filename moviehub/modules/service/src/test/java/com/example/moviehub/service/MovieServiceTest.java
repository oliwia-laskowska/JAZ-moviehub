package com.example.moviehub.service;

import com.example.moviehub.domain.model.Movie;
import com.example.moviehub.persistence.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    private MovieRepository movieRepository;
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieRepository = mock(MovieRepository.class);
        movieService = new MovieService(movieRepository);
    }

    @Test
    void list_shouldReturnAllMovies() {
        Movie m1 = new Movie("A", "GHIBLI", "1");
        Movie m2 = new Movie("B", "TMDB", "2");
        when(movieRepository.findAll()).thenReturn(List.of(m1, m2));

        List<Movie> result = movieService.list();

        assertThat(result).containsExactly(m1, m2);
        verify(movieRepository).findAll();
    }

    @Test
    void getById_shouldReturnMovie_whenExists() {
        Movie m = new Movie("Spirited Away", "GHIBLI", "x");
        when(movieRepository.findById(10L)).thenReturn(Optional.of(m));

        Movie result = movieService.getById(10L);

        assertThat(result).isSameAs(m);
        verify(movieRepository).findById(10L);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> movieService.getById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Movie not found: 99");

        verify(movieRepository).findById(99L);
    }

    @Test
    void evictCaches_shouldNotThrow() {
        assertThatCode(() -> movieService.evictCaches()).doesNotThrowAnyException();
    }
}
