package com.example.moviehub.api.controller.rest;

import com.example.moviehub.common.error.ReviewDto;
import com.example.moviehub.domain.mapper.ReviewMapper;
import com.example.moviehub.service.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // REST API dla recenzji
@RequestMapping("/api/reviews") // Bazowa ścieżka endpointów recenzji
public class ReviewRestController {

    private final ReviewService reviewService; // logika biznesowa dla recenzji

    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/by-movie/{movieId}") // GET /api/reviews/by-movie/{movieId} -> recenzje dla filmu
    public List<ReviewDto> byMovie(@PathVariable Long movieId) {
        // Pobiera recenzje dla filmu i mapuje je na DTO
        return reviewService.listByMovie(movieId).stream()
                .map(ReviewMapper::toDto)
                .toList();
    }

    // Request body dla dodania recenzji
    public record CreateReviewRequest(Long movieId, int rating, String comment) {}

    @PostMapping // POST /api/reviews -> dodaje recenzję
    public ReviewDto add(Authentication auth, @RequestBody CreateReviewRequest req) {
        // Używa zalogowanego użytkownika jako autora recenzji
        var saved = reviewService.addReview(auth.getName(), req.movieId(), req.rating(), req.comment());
        return ReviewMapper.toDto(saved); // zwraca zapisany obiekt jako DTO
    }
}
