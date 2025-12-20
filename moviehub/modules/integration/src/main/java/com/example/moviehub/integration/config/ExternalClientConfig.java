package com.example.moviehub.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ExternalClientConfig {

    @Bean
    RestClient ghibliRestClient() {
        return RestClient.builder()
                .baseUrl("https://ghibliapi.vercel.app")
                .build();
    }
}
