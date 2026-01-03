package com.example.moviehub.api.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeFormatUtilTest {

    @Test
    void formatPl_null_returnsDash() {
        // Null -> domyślny placeholder
        assertThat(TimeFormatUtil.formatPl(null)).isEqualTo("-");
    }

    @Test
    void ago_null_returnsDash() {
        // Null -> domyślny placeholder
        assertThat(TimeFormatUtil.ago(null)).isEqualTo("-");
    }

    @Test
    void ago_futureInstant_returnsZeroSecondsAgo() {
        // Data z przyszłości nie powinna zwracać ujemnego czasu -> 0 s temu
        Instant future = Instant.now().plusSeconds(120);
        assertThat(TimeFormatUtil.ago(future)).isEqualTo("0 s temu");
    }

    @Test
    void ago_seconds() {
        // Różnica < 60s -> format w sekundach
        Instant t = Instant.now().minusSeconds(10);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("10 s temu");
    }

    @Test
    void ago_minutes() {
        // Różnica < 60min -> format w minutach (ucięte sekundy)
        Instant t = Instant.now().minusSeconds(2 * 60 + 5);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("2 min temu");
    }

    @Test
    void ago_hours() {
        // Różnica < 24h -> format w godzinach
        Instant t = Instant.now().minusSeconds(3 * 3600 + 10);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("3 h temu");
    }

    @Test
    void ago_days() {
        // Różnica >= 24h -> format w dniach
        Instant t = Instant.now().minusSeconds(2 * 24 * 3600 + 1);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("2 dni temu");
    }

    @Test
    void formatPl_formatsNonNull() {
        // Sprawdza, że formatowanie daje sensowny string (bez zależności od strefy czasowej)
        Instant t = Instant.parse("2025-01-01T10:00:00Z");
        String formatted = TimeFormatUtil.formatPl(t);

        // format zależy od systemDefault() (timezone), więc nie porównujemy 1:1 do konkretnej wartości
        assertThat(formatted).contains(".");
        assertThat(formatted).contains(":");
        assertThat(formatted).hasSizeGreaterThan(10);
    }
}
