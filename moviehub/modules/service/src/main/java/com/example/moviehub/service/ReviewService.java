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

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public List<Review> listByMovie(Long movieId) {
        return reviewRepository.findByMovie_Id(movieId);
    }

    @Transactional
    public Review addReview(String username, Long movieId, int rating, String comment) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));
        return reviewRepository.save(new Review(user, movie, rating, comment));
    }
}
