package com.example.moviehub.domain.mapper;

import com.example.moviehub.common.error.MovieDto;
import com.example.moviehub.domain.model.Movie;

import java.util.stream.Collectors;

public class MovieMapper {

    private MovieMapper() {}

    public static MovieDto toDto(Movie m) {
        return new MovieDto(
                m.getId(),
                m.getTitle(),
                m.getDescription(),
                m.getDirector(),
                m.getProducer(),
                m.getReleaseYear(),
                m.getRtScore(),
                m.getGenres().stream().map(g -> g.getName()).collect(Collectors.toSet())
        );
    }
}
