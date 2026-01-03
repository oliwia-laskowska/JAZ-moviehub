package com.example.moviehub.api.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component // Komponent do wysyłania powiadomień przez WebSocket
public class NotificationsController {

    private final SimpMessagingTemplate template; // narzędzie do publikowania wiadomości na topic

    public NotificationsController(SimpMessagingTemplate template) {
        this.template = template;
    }


    public void notifySync(String message) {
        template.convertAndSend("/topic/sync", message);
    }
}
