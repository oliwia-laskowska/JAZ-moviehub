package com.example.moviehub.service;

import com.example.moviehub.domain.model.SyncRun;
import com.example.moviehub.persistence.repository.SyncRunRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service // Serwis do zapisu i odczytu historii uruchomień synchronizacji
public class SyncRunService {

    private final SyncRunRepository repo; // repozytorium SyncRun

    public SyncRunService(SyncRunRepository repo) {
        this.repo = repo;
    }

    // Zapisuje wpis o wykonanym sync (kiedy, ile upsertów, kto uruchomił, skąd)
    public SyncRun saveRun(int upserts, String triggeredBy, String source) {
        return repo.save(new SyncRun(Instant.now(), upserts, triggeredBy, source));
    }

    // Zwraca ostatnie uruchomienie sync (jeśli istnieje)
    public Optional<SyncRun> lastRun() {
        return repo.findTopByOrderByRunAtDesc();
    }
}
