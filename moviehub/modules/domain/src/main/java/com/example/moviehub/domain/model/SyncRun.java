package com.example.moviehub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "sync_runs")
public class SyncRun extends BaseEntity {

    @Column(nullable = false)
    private Instant runAt;

    @Column(nullable = false)
    private int upserts;

    @Column(nullable = false, length = 80)
    private String triggeredBy; // np. admin, scheduler, api

    @Column(nullable = false, length = 40)
    private String source; // UI / SCHEDULER / API

    protected SyncRun() {}

    public SyncRun(Instant runAt, int upserts, String triggeredBy, String source) {
        this.runAt = runAt;
        this.upserts = upserts;
        this.triggeredBy = triggeredBy;
        this.source = source;
    }

    public Instant getRunAt() { return runAt; }
    public int getUpserts() { return upserts; }
    public String getTriggeredBy() { return triggeredBy; }
    public String getSource() { return source; }
}
