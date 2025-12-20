package com.example.moviehub.domain.dto;

public record ReviewDto(
        Long id,
        Long movieId,
        String username,
        int rating,
        String comment
) {}
