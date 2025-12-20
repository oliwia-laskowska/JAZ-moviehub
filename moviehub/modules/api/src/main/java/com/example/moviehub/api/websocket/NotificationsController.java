package com.example.moviehub.api.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificationsController {

    private final SimpMessagingTemplate template;

    public NotificationsController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void notifySync(String message) {
        template.convertAndSend("/topic/sync", message);
    }
}
