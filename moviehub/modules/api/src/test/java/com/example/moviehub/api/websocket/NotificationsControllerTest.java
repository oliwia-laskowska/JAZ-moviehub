package com.example.moviehub.api.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

class NotificationsControllerTest {

    @Test
    void notifySync_sendsMessageToTopicSync() {
        SimpMessagingTemplate template = mock(SimpMessagingTemplate.class);
        NotificationsController controller = new NotificationsController(template);

        controller.notifySync("hello");

        verify(template).convertAndSend("/topic/sync", "hello");
        verifyNoMoreInteractions(template);
    }
}
