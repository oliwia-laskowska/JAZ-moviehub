package com.example.moviehub.service.scheduler;

import com.example.moviehub.service.SyncRunService;
import com.example.moviehub.service.SyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component // Komponent Springa uruchamiający cykliczny sync
public class MovieSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(MovieSyncScheduler.class); // Logger do logów sync

    private final SyncService syncService; // wykonuje synchronizację danych
    private final SyncRunService syncRunService; // zapisuje historię uruchomień

    public MovieSyncScheduler(SyncService syncService, SyncRunService syncRunService) {
        this.syncService = syncService;
        this.syncRunService = syncRunService;
    }

    @Scheduled(
            // Opóźnienie pomiędzy zakończeniem jednego a startem kolejnego uruchomienia
            fixedDelayString = "${moviehub.sync.fixedDelayMs:3600000}", // domyślnie 1h
            // Opóźnienie pierwszego uruchomienia po starcie aplikacji
            initialDelayString = "${moviehub.sync.initialDelayMs:30000}" // domyślnie 30s
    )
    public void scheduledSync() {
        try {
            int n = syncService.syncFromInternet(); // uruchamia sync
            syncRunService.saveRun(n, "scheduler", "SCHEDULER"); // zapis do historii
            log.info("Scheduled sync OK, upserts={}", n); // log sukcesu
        } catch (Exception e) {
            log.warn("Scheduled sync FAILED: {}", e.getMessage(), e); // log błędu bez crasha aplikacji
        }
    }
}
