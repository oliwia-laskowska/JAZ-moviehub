package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovie_Id(Long movieId);
    List<Review> findByUser_Username(String username);
}
