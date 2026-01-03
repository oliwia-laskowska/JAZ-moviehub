package com.example.moviehub.service;

import com.example.moviehub.domain.model.Movie;
import com.example.moviehub.domain.model.Review;
import com.example.moviehub.domain.model.User;
import com.example.moviehub.persistence.repository.MovieRepository;
import com.example.moviehub.persistence.repository.ReviewRepository;
import com.example.moviehub.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Serwis obsługujący recenzje
public class ReviewService {

    private final ReviewRepository reviewRepository; // operacje na recenzjach
    private final UserRepository userRepository; // lookup usera po username
    private final MovieRepository movieRepository; // lookup filmu po ID

    public ReviewService(
            ReviewRepository reviewRepository,
            UserRepository userRepository,
            MovieRepository movieRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // Zwraca recenzje dla danego filmu
    public List<Review> listByMovie(Long movieId) {
        return reviewRepository.findByMovie_Id(movieId);
    }

    @Transactional // Całość w jednej transakcji (pobrania + zapis)
    public Review addReview(String username, Long movieId, int rating, String comment) {

        // Pobiera autora recenzji
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Pobiera film, którego dotyczy recenzja
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));

        // Tworzy i zapisuje nową recenzję
        return reviewRepository.save(new Review(user, movie, rating, comment));
    }
}
