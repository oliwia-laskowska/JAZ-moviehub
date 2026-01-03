package com.example.moviehub.service;

import com.example.moviehub.domain.model.SyncRun;
import com.example.moviehub.persistence.repository.SyncRunRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SyncRunServiceTest {

    private SyncRunRepository repo;
    private SyncRunService service;

    @BeforeEach
    void setUp() {
        repo = mock(SyncRunRepository.class);
        service = new SyncRunService(repo);
    }

    @Test
    void saveRun_shouldPersistNewSyncRun() {
        when(repo.save(any(SyncRun.class))).thenAnswer(inv -> inv.getArgument(0));

        SyncRun saved = service.saveRun(12, "admin", "API");

        verify(repo).save(any(SyncRun.class));

        assertThat(saved.getUpserts()).isEqualTo(12);
        assertThat(saved.getTriggeredBy()).isEqualTo("admin");
        assertThat(saved.getSource()).isEqualTo("API");
        assertThat(saved.getRunAt()).isNotNull();
    }

    @Test
    void lastRun_shouldReturnRepoResult() {
        SyncRun run = mock(SyncRun.class);
        when(repo.findTopByOrderByRunAtDesc()).thenReturn(Optional.of(run));

        Optional<SyncRun> result = service.lastRun();

        assertThat(result).contains(run);
        verify(repo).findTopByOrderByRunAtDesc();
    }
}
