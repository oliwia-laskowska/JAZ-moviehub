package com.example.moviehub.api.controller.mvc;

import com.example.moviehub.api.websocket.NotificationsController;
import com.example.moviehub.service.MovieService;
import com.example.moviehub.service.SyncRunService;
import com.example.moviehub.service.SyncService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // Kontroler MVC dla widoku panelu admina
public class AdminPageController {

    private final SyncService syncService; // uruchamia synchronizację danych
    private final SyncRunService syncRunService; // zapisuje/odczytuje historię synców
    private final NotificationsController notifications; // powiadomienia przez WebSocket
    private final MovieService movieService; // dostęp do filmów

    public AdminPageController(
            SyncService syncService,
            SyncRunService syncRunService,
            NotificationsController notifications,
            MovieService movieService
    ) {
        this.syncService = syncService;
        this.syncRunService = syncRunService;
        this.notifications = notifications;
        this.movieService = movieService;
    }

    @GetMapping("/admin") // Strona panelu admina
    public String adminPage(Model model) {
        model.addAttribute("lastRun", syncRunService.lastRun().orElse(null)); // info o ostatnim syncu
        model.addAttribute("moviesCount", movieService.list().size()); // liczba filmów w bazie
        model.addAttribute("logsUrl", "/api/admin/logs?lines=200"); // link do logów dla UI
        return "admin"; // nazwa widoku (admin.html)
    }

    @PostMapping("/admin/sync") // Akcja sync z przycisku w panelu admina
    public String syncFromUi(Authentication auth, RedirectAttributes ra) {
        int upserts = syncService.syncFromInternet(); // wykonuje synchronizację

        // Zapisuje kto uruchomił sync (dla audytu)
        String who = (auth != null ? auth.getName() : "anonymous");
        syncRunService.saveRun(upserts, who, "UI");

        // Powiadomienie klientów (np. admin panel) o zakończeniu sync
        notifications.notifySync("UI sync completed: upserts=" + upserts);

        // Flash message po redirect (widoczny po odświeżeniu /admin)
        ra.addFlashAttribute("syncMessage", "Sync OK. Upserts: " + upserts);
        return "redirect:/admin"; // PRG pattern (Post/Redirect/Get)
    }
}
