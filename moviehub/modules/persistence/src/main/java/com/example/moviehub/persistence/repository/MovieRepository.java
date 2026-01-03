package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Repozytorium JPA dla encji Movie
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Szuka filmu po źródle + ID zewnętrznym
    Optional<Movie> findByExternalSourceAndExternalId(String externalSource, String externalId);

    @Override
    @EntityGraph(attributePaths = {"genres"}) // dociąga genres w jednym zapytaniu
    List<Movie> findAll();

    @Override
    @EntityGraph(attributePaths = {"genres"}) // dociąga genres przy pobieraniu po ID
    Optional<Movie> findById(Long id);
}
