package com.example.moviehub.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbMoviePage(
        Integer page,
        List<TmdbMovie> results,
        @JsonProperty("total_pages") Integer totalPages,
        @JsonProperty("total_results") Integer totalResults
) {}
