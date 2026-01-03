package com.example.moviehub.common.error;

// DTO recenzji zwracany przez API
public record ReviewDto(
        Long id,        // identyfikator recenzji
        Long movieId,   // ID filmu, którego dotyczy recenzja
        String username,// autor recenzji
        int rating,     // ocena
        String comment  // treść komentarza
) {}
