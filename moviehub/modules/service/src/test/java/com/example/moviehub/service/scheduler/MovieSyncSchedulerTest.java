package com.example.moviehub.service.scheduler;

import com.example.moviehub.service.SyncRunService;
import com.example.moviehub.service.SyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class MovieSyncSchedulerTest {

    private SyncService syncService;
    private SyncRunService syncRunService;
    private MovieSyncScheduler scheduler;

    @BeforeEach
    void setUp() {
        syncService = mock(SyncService.class);
        syncRunService = mock(SyncRunService.class);
        scheduler = new MovieSyncScheduler(syncService, syncRunService);
    }

    @Test
    void scheduledSync_shouldSaveRun_whenSyncSucceeds() {
        when(syncService.syncFromInternet()).thenReturn(12);

        scheduler.scheduledSync();

        verify(syncRunService).saveRun(12, "scheduler", "SCHEDULER");
    }

    @Test
    void scheduledSync_shouldNotSaveRun_whenSyncFails() {
        when(syncService.syncFromInternet()).thenThrow(new RuntimeException("boom"));

        scheduler.scheduledSync();

        verifyNoInteractions(syncRunService);
    }
}
