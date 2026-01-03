package com.example.moviehub.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// Model pojedynczego filmu z TMDB
public record TmdbMovie(
        Long id, // ID filmu w TMDB
        String title, // tytuł
        String overview, // opis (overview)
        @JsonProperty("release_date") String releaseDate, // JSON: release_date -> releaseDate
        @JsonProperty("vote_average") Double voteAverage // JSON: vote_average -> voteAverage (średnia ocen)
) {}
