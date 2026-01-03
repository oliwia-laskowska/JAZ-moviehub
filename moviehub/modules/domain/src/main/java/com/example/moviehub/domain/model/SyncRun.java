package com.example.moviehub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity // Encja: zapis pojedynczego uruchomienia synchronizacji
@Table(name = "sync_runs")
public class SyncRun extends BaseEntity {

    @Column(nullable = false) // Kiedy wykonano sync
    private Instant runAt;

    @Column(nullable = false) // Ile rekordów zostało zaktualizowanych/dodanych (upsert)
    private int upserts;

    @Column(nullable = false, length = 80) // Kto uruchomił sync (np. admin, scheduler)
    private String triggeredBy;

    @Column(nullable = false, length = 40) // Skąd uruchomiono sync (UI / SCHEDULER / API)
    private String source; // UI / SCHEDULER / API

    protected SyncRun() {}

    // Konstruktor pomocniczy do tworzenia wpisu sync run
    public SyncRun(Instant runAt, int upserts, String triggeredBy, String source) {
        this.runAt = runAt;
        this.upserts = upserts;
        this.triggeredBy = triggeredBy;
        this.source = source;
    }

    public Instant getRunAt() { return runAt; } // timestamp uruchomienia
    public int getUpserts() { return upserts; } // liczba upsertów
    public String getTriggeredBy() { return triggeredBy; } // kto uruchomił
    public String getSource() { return source; } // źródło uruchomienia
}
