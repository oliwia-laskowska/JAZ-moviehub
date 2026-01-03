package com.example.moviehub.api.controller.rest;

import com.example.moviehub.api.websocket.NotificationsController;
import com.example.moviehub.service.SyncRunService;
import com.example.moviehub.service.SyncService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // REST endpointy dla admina (bez widoków)
public class AdminRestController {

    private final SyncService syncService; // wykonuje synchronizację danych
    private final SyncRunService syncRunService; // zapis historii uruchomień sync
    private final NotificationsController notifications; // powiadomienia przez WebSocket

    public AdminRestController(
            SyncService syncService,
            SyncRunService syncRunService,
            NotificationsController notifications
    ) {
        this.syncService = syncService;
        this.syncRunService = syncRunService;
        this.notifications = notifications;
    }

    // Prosta odpowiedź JSON zwracana po sync
    public record SyncResponse(int upserts) {}

    @PostMapping("/api/admin/sync") // Uruchamia sync z poziomu API
    public SyncResponse sync(Authentication auth) {
        int n = syncService.syncFromInternet(); // wykonuje sync i zwraca liczbę upsertów

        // zapis kto uruchomił sync
        syncRunService.saveRun(n, auth.getName(), "API");

        // powiadomienie klientów (np. panel admina) o zakończeniu
        notifications.notifySync("API sync completed: upserts=" + n);

        return new SyncResponse(n); // JSON: {"upserts": ...}
    }
}
