package com.example.moviehub.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration // Konfiguracja beanów pomocniczych do Thymeleaf
public class ThymeleafUtilConfig {

    @Bean(name = "timeUtil") // Bean dostępny w widokach Thymeleaf jako: ${timeUtil}
    public TimeUtil timeUtil() {
        return new TimeUtil();
    }

    // Prosty wrapper na utilsy formatowania czasu
    public static class TimeUtil {

        // Format daty/godziny w stylu PL
        public String formatPl(Instant instant) {
            return com.example.moviehub.api.util.TimeFormatUtil.formatPl(instant);
        }

        // Zwraca tekst typu "5 minut temu"
        public String ago(Instant instant) {
            return com.example.moviehub.api.util.TimeFormatUtil.ago(instant);
        }
    }
}
