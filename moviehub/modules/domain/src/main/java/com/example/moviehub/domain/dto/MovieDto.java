package com.example.moviehub.domain.dto;

import java.util.Set;

public record MovieDto(
        Long id,
        String title,
        String description,
        String director,
        String producer,
        Integer releaseYear,
        Integer rtScore,
        Set<String> genres
) {}
