package com.example.moviehub.service;

import com.example.moviehub.domain.model.SyncRun;
import com.example.moviehub.persistence.repository.SyncRunRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class SyncRunService {

    private final SyncRunRepository repo;

    public SyncRunService(SyncRunRepository repo) {
        this.repo = repo;
    }

    public SyncRun saveRun(int upserts, String triggeredBy, String source) {
        return repo.save(new SyncRun(Instant.now(), upserts, triggeredBy, source));
    }

    public Optional<SyncRun> lastRun() {
        return repo.findTopByOrderByRunAtDesc();
    }
}
