package com.example.moviehub.service;

import com.example.moviehub.domain.model.Movie;
import com.example.moviehub.domain.model.Review;
import com.example.moviehub.domain.model.User;
import com.example.moviehub.persistence.repository.MovieRepository;
import com.example.moviehub.persistence.repository.ReviewRepository;
import com.example.moviehub.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private MovieRepository movieRepository;
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        userRepository = mock(UserRepository.class);
        movieRepository = mock(MovieRepository.class);
        reviewService = new ReviewService(reviewRepository, userRepository, movieRepository);
    }

    @Test
    void listByMovie_shouldReturnReviews() {
        Review r1 = mock(Review.class);
        Review r2 = mock(Review.class);

        when(reviewRepository.findByMovie_Id(5L)).thenReturn(List.of(r1, r2));

        List<Review> result = reviewService.listByMovie(5L);

        assertThat(result).containsExactly(r1, r2);
        verify(reviewRepository).findByMovie_Id(5L);
    }

    @Test
    void addReview_shouldCreateAndSaveReview() {
        User user = new User("oliwia", "hash");
        Movie movie = new Movie("Spirited Away", "GHIBLI", "x");

        when(userRepository.findByUsername("oliwia")).thenReturn(Optional.of(user));
        when(movieRepository.findById(10L)).thenReturn(Optional.of(movie));

        // symulujemy, że save zwraca ten sam obiekt (często tak jest w unit testach)
        when(reviewRepository.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));

        Review saved = reviewService.addReview("oliwia", 10L, 9, "Great!");

        // sprawdzamy że save dostał Review z dobrymi polami
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review toSave = captor.getValue();
        assertThat(toSave.getUser()).isSameAs(user);
        assertThat(toSave.getMovie()).isSameAs(movie);
        assertThat(toSave.getRating()).isEqualTo(9);
        assertThat(toSave.getComment()).isEqualTo("Great!");

        assertThat(saved).isSameAs(toSave);
    }

    @Test
    void addReview_shouldThrow_whenUserNotFound() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.addReview("missing", 1L, 5, "x"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found: missing");

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void addReview_shouldThrow_whenMovieNotFound() {
        when(userRepository.findByUsername("oliwia")).thenReturn(Optional.of(new User("oliwia", "hash")));
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.addReview("oliwia", 999L, 5, "x"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Movie not found: 999");

        verify(reviewRepository, never()).save(any());
    }
}
