package com.example.moviehub.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.moviehub")
@EnableJpaRepositories(basePackages = "com.example.moviehub.persistence.repository")
@EntityScan(basePackages = "com.example.moviehub.domain.model")
public class MoviehubApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviehubApplication.class, args);
    }
}
