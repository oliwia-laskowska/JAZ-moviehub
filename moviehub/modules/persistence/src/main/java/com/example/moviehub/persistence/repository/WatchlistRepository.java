package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {
    List<WatchlistItem> findByUser_Username(String username);
    boolean existsByUser_UsernameAndMovie_Id(String username, Long movieId);
}
