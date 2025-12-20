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

@Controller
public class AdminPageController {

    private final SyncService syncService;
    private final SyncRunService syncRunService;
    private final NotificationsController notifications;
    private final MovieService movieService;

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

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("lastRun", syncRunService.lastRun().orElse(null));
        model.addAttribute("moviesCount", movieService.list().size());
        model.addAttribute("logsUrl", "/api/admin/logs?lines=200");
        return "admin";
    }

    @PostMapping("/admin/sync")
    public String syncFromUi(Authentication auth, RedirectAttributes ra) {
        int upserts = syncService.syncFromInternet();

        String who = (auth != null ? auth.getName() : "anonymous");
        syncRunService.saveRun(upserts, who, "UI");

        notifications.notifySync("UI sync completed: upserts=" + upserts);

        ra.addFlashAttribute("syncMessage", "Sync OK. Upserts: " + upserts);
        return "redirect:/admin";
    }
}
