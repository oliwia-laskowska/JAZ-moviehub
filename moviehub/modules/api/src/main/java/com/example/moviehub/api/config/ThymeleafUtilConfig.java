package com.example.moviehub.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
public class ThymeleafUtilConfig {

    @Bean(name = "timeUtil")
    public TimeUtil timeUtil() {
        return new TimeUtil();
    }

    public static class TimeUtil {
        public String formatPl(Instant instant) {
            return com.example.moviehub.api.util.TimeFormatUtil.formatPl(instant);
        }

        public String ago(Instant instant) {
            return com.example.moviehub.api.util.TimeFormatUtil.ago(instant);
        }
    }
}
