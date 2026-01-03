package com.example.moviehub.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // Włącza obsługę zadań cyklicznych (@Scheduled)
public class SchedulerConfig {}
