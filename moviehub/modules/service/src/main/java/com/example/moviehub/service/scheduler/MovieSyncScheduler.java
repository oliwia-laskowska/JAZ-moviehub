package com.example.moviehub.service.scheduler;

import com.example.moviehub.service.SyncRunService;
import com.example.moviehub.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MovieSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(MovieSyncScheduler.class);

    private final SyncService syncService;
    private final SyncRunService syncRunService;

    public MovieSyncScheduler(SyncService syncService, SyncRunService syncRunService) {
        this.syncService = syncService;
        this.syncRunService = syncRunService;
    }

    @Scheduled(
            fixedDelayString = "${moviehub.sync.fixedDelayMs:3600000}",
            initialDelayString = "${moviehub.sync.initialDelayMs:30000}"
    )
    public void scheduledSync() {
        try {
            int n = syncService.syncFromInternet();
            syncRunService.saveRun(n, "scheduler", "SCHEDULER");
            log.info("Scheduled sync OK, upserts={}", n);
        } catch (Exception e) {
            log.warn("Scheduled sync FAILED: {}", e.getMessage(), e);
        }
    }
}
