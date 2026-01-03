package com.example.moviehub.api.controller.rest;

import com.example.moviehub.common.error.ReviewDto;
import com.example.moviehub.domain.mapper.ReviewMapper;
import com.example.moviehub.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewRestController {

    private final ReviewService reviewService;

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/by-movie/{movieId}")
    public List<ReviewDto> byMovie(@PathVariable Long movieId) {
        return reviewService.listByMovie(movieId).stream().map(ReviewMapper::toDto).toList();
    }

    public record CreateReviewRequest(Long movieId, int rating, String comment) {}

    @PostMapping
    public ReviewDto add(Authentication auth, @RequestBody CreateReviewRequest req) {
        var saved = reviewService.addReview(auth.getName(), req.movieId(), req.rating(), req.comment());
        return ReviewMapper.toDto(saved);
    }
}
