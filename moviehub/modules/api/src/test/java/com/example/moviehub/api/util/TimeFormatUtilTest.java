package com.example.moviehub.api.util;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class TimeFormatUtilTest {

    @Test
    void formatPl_null_returnsDash() {
        assertThat(TimeFormatUtil.formatPl(null)).isEqualTo("-");
    }

    @Test
    void ago_null_returnsDash() {
        assertThat(TimeFormatUtil.ago(null)).isEqualTo("-");
    }

    @Test
    void ago_futureInstant_returnsZeroSecondsAgo() {
        Instant future = Instant.now().plusSeconds(120);
        assertThat(TimeFormatUtil.ago(future)).isEqualTo("0 s temu");
    }

    @Test
    void ago_seconds() {
        Instant t = Instant.now().minusSeconds(10);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("10 s temu");
    }

    @Test
    void ago_minutes() {
        Instant t = Instant.now().minusSeconds(2 * 60 + 5);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("2 min temu");
    }

    @Test
    void ago_hours() {
        Instant t = Instant.now().minusSeconds(3 * 3600 + 10);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("3 h temu");
    }

    @Test
    void ago_days() {
        Instant t = Instant.now().minusSeconds(2 * 24 * 3600 + 1);
        assertThat(TimeFormatUtil.ago(t)).isEqualTo("2 dni temu");
    }

    @Test
    void formatPl_formatsNonNull() {
        Instant t = Instant.parse("2025-01-01T10:00:00Z");
        String formatted = TimeFormatUtil.formatPl(t);

        // format zależy od systemDefault() (timezone), więc nie porównujemy 1:1 do konkretnej wartości
        assertThat(formatted).contains(".");
        assertThat(formatted).contains(":");
        assertThat(formatted).hasSizeGreaterThan(10);
    }
}
