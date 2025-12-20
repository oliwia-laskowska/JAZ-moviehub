package com.example.moviehub.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalFilm(
        String id,
        String title,
        String description,
        String director,
        String producer,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("rt_score") String rtScore
) {}
