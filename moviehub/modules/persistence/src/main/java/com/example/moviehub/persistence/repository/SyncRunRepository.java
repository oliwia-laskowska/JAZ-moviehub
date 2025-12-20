package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.SyncRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyncRunRepository extends JpaRepository<SyncRun, Long> {
    Optional<SyncRun> findTopByOrderByRunAtDesc();
}
