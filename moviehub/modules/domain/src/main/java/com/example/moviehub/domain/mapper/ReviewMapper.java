package com.example.moviehub.domain.mapper;

import com.example.moviehub.common.error.ReviewDto;
import com.example.moviehub.domain.model.Review;

public class ReviewMapper {

    private ReviewMapper() {}

    // Mapuje encję Review na DTO używane w API
    public static ReviewDto toDto(Review r) {
        return new ReviewDto(
                r.getId(), // ID recenzji
                r.getMovie().getId(), // ID filmu, którego dotyczy recenzja
                r.getUser().getUsername(), // login autora
                r.getRating(), // ocena
                r.getComment() // komentarz
        );
    }
}
