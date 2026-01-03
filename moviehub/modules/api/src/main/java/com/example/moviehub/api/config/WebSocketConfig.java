package com.example.moviehub.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // Konfiguracja WebSocket + STOMP
@EnableWebSocketMessageBroker // Włącza broker wiadomości dla STOMP
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint, pod który łączy się klient WebSocket
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*"); // CORS dla WS
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // Kanały publikacji (subscribe)
        registry.setApplicationDestinationPrefixes("/app"); // Kanały wysyłania do backendu (send) np. /app/sync
    }
}
