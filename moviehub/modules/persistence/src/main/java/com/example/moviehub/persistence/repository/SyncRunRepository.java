package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.SyncRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repozytorium JPA dla encji SyncRun (historia uruchomień synchronizacji)
public interface SyncRunRepository extends JpaRepository<SyncRun, Long> {

    // Zwraca ostatnie uruchomienie sync (malejąco)
    Optional<SyncRun> findTopByOrderByRunAtDesc();
}
