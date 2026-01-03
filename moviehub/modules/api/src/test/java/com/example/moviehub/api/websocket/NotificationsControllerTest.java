package com.example.moviehub.api.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

class NotificationsControllerTest {

    @Test
    void notifySync_sendsMessageToTopicSync() {
        // Mock template, żeby nie odpalać prawdziwego brokera
        SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        NotificationsController controller = new NotificationsController(template);

        // Wywołanie metody wysyłającej powiadomienie
        controller.notifySync("hello");

        // Sprawdza, czy wiadomość została wysłana na poprawny topic
        verify(template).convertAndSend("/topic/sync", "hello");
        // Upewnia się, że nie było żadnych dodatkowych wywołań
        verifyNoMoreInteractions(template);
    }
}
