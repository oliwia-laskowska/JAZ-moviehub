package com.example.moviehub.api.config;

import com.example.moviehub.api.websocket.NotificationsController;
import com.example.moviehub.service.SyncService;
import com.example.moviehub.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // Konfiguracja uruchamiania danych/akcji startowych
public class StartupDataConfig {

    private static final Logger log = LoggerFactory.getLogger(StartupDataConfig.class); // Logger dla startu aplikacji

    @Bean // Kod wykona się automatycznie po starcie Spring Boota
    CommandLineRunner seedUsers(UserService userService, SyncService syncService, NotificationsController notifications) {
        return args -> {

            // Tworzy lub aktualizuje użytkowników startowych (jeśli nie istnieją)
            userService.ensureUser("admin", "admin123", UserService.ROLE_ADMIN);
            userService.ensureUser("user", "user123", UserService.ROLE_USER);

            // Początkowa synchronizacja danych (błędy sieci nie mogą zablokować startu)
            try {
                int n = syncService.syncFromInternet(); // pobranie danych z internetu i zapis (upsert)
                notifications.notifySync("Initial sync completed: upserts=" + n); // info przez WebSocket
            } catch (Exception e) {
                log.warn("Initial sync skipped/failed: {}", e.getMessage()); // log ostrzegawczy zamiast crasha
            }
        };
    }
}
