package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repozytorium JPA dla encji Review
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Zwraca recenzje dla konkretnego filmu (po movie_id)
    List<Review> findByMovie_Id(Long movieId);

    // Zwraca recenzje napisane przez konkretnego użytkownika (po username)
    List<Review> findByUser_Username(String username);
}
