package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repozytorium JPA dla elementów watchlisty
public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {

    // Zwraca watchlistę użytkownika po username
    List<WatchlistItem> findByUser_Username(String username);

    // Sprawdza czy dany film jest już na watchliście użytkownika
    boolean existsByUser_UsernameAndMovie_Id(String username, Long movieId);
}
