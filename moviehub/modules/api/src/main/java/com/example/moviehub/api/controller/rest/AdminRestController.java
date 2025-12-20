package com.example.moviehub.api.controller.rest;

import com.example.moviehub.api.websocket.NotificationsController;
import com.example.moviehub.service.SyncRunService;
import com.example.moviehub.service.SyncService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminRestController {

    private final SyncService syncService;
    private final SyncRunService syncRunService;
    private final NotificationsController notifications;

    public AdminRestController(SyncService syncService, SyncRunService syncRunService, NotificationsController notifications) {
        this.syncService = syncService;
        this.syncRunService = syncRunService;
        this.notifications = notifications;
    }

    public record SyncResponse(int upserts) {}

    @PostMapping("/api/admin/sync")
    public SyncResponse sync(Authentication auth) {
        int n = syncService.syncFromInternet();
        syncRunService.saveRun(n, auth.getName(), "API");
        notifications.notifySync("API sync completed: upserts=" + n);
        return new SyncResponse(n);
    }
}
