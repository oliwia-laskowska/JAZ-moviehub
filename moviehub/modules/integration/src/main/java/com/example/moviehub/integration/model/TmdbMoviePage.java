package com.example.moviehub.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Model odpowiedzi TMDB dla endpointów paginowanych
public record TmdbMoviePage(
        Integer page, // aktualna strona wyników
        List<TmdbMovie> results, // lista filmów na tej stronie
        @JsonProperty("total_pages") Integer totalPages, // JSON: total_pages -> totalPages
        @JsonProperty("total_results") Integer totalResults // JSON: total_results -> totalResults
) {}
