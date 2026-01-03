package com.example.moviehub.domain.mapper;

import com.example.moviehub.common.error.ReviewDto;
import com.example.moviehub.domain.model.Review;

public class ReviewMapper {
    private ReviewMapper() {}

    public static ReviewDto toDto(Review r) {
        return new ReviewDto(
                r.getId(),
                r.getMovie().getId(),
                r.getUser().getUsername(),
                r.getRating(),
                r.getComment()
        );
    }
}
