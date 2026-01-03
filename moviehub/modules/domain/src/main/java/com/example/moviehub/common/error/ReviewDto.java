package com.example.moviehub.common.error;

public record ReviewDto(
        Long id,
        Long movieId,
        String username,
        int rating,
        String comment
) {}
