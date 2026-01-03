package com.example.moviehub.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

// Model danych z zewnętrznego API (Studio Ghibli)
public record ExternalFilm(
        String id, // ID filmu w zewnętrznym źródle
        String title, // tytuł
        String description, // opis
        String director, // reżyser
        String producer, // producent
        @JsonProperty("release_date") String releaseDate, // JSON: release_date -> releaseDate
        @JsonProperty("rt_score") String rtScore // JSON: rt_score -> rtScore
) {}
