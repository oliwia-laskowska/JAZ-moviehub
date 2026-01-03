package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repozytorium JPA dla encji Genre (CRUD + metody Spring Data)
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Szuka gatunku po nazwie (zwraca Optional, bo może nie istnieć)
    Optional<Genre> findByName(String name);
}
