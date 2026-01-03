package com.example.moviehub.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbMovie(
        Long id,
        String title,
        String overview,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("vote_average") Double voteAverage
) {}
