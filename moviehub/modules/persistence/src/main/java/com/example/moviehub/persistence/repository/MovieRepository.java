package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.Movie;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByExternalId(String externalId);

    @Override
    @EntityGraph(attributePaths = {"genres"})
    List<Movie> findAll();

    @Override
    @EntityGraph(attributePaths = {"genres"})
    Optional<Movie> findById(Long id);
}
