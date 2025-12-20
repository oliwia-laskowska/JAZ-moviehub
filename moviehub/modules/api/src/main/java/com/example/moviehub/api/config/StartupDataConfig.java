package com.example.moviehub.api.config;

import com.example.moviehub.api.websocket.NotificationsController;
import com.example.moviehub.service.SyncService;
import com.example.moviehub.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupDataConfig {

    private static final Logger log = LoggerFactory.getLogger(StartupDataConfig.class);

    @Bean
    CommandLineRunner seedUsers(UserService userService, SyncService syncService, NotificationsController notifications) {
        return args -> {
            userService.ensureUser("admin", "admin123", UserService.ROLE_ADMIN);
            userService.ensureUser("user", "user123", UserService.ROLE_USER);

            // initial sync (nie blokuj startu na błąd sieci)
            try {
                int n = syncService.syncFromInternet();
                notifications.notifySync("Initial sync completed: upserts=" + n);
            } catch (Exception e) {
                log.warn("Initial sync skipped/failed: {}", e.getMessage());
            }
        };
    }
}
