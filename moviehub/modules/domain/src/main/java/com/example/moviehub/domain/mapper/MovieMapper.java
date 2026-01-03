package com.example.moviehub.domain.mapper;

import com.example.moviehub.common.error.MovieDto;
import com.example.moviehub.domain.model.Movie;

import java.util.stream.Collectors;

public class MovieMapper {

    private MovieMapper() {}

    // Mapuje encję Movie na DTO używane w API
    public static MovieDto toDto(Movie m) {
        return new MovieDto(
                m.getId(), // ID filmu
                m.getTitle(), // tytuł
                m.getDescription(), // opis
                m.getDirector(), // reżyser
                m.getProducer(), // producent
                m.getReleaseYear(), // rok wydania
                m.getRtScore(), // ocena
                // mapuje encje Genre -> nazwy gatunków (Set<String>)
                m.getGenres().stream()
                        .map(g -> g.getName())
                        .collect(Collectors.toSet())
        );
    }
}
