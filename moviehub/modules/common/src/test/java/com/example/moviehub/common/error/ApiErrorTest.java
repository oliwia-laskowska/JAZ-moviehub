package com.example.moviehub.common.error;


import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ApiErrorTest {

    @Test
    void record_shouldStoreAllFields() {
        Instant ts = Instant.parse("2024-01-01T00:00:00Z");

        ApiError error = new ApiError(
                ts,
                400,
                "Bad Request",
                "Invalid input",
                "/movies"
        );

        assertThat(error.timestamp()).isEqualTo(ts);
        assertThat(error.status()).isEqualTo(400);
        assertThat(error.error()).isEqualTo("Bad Request");
        assertThat(error.message()).isEqualTo("Invalid input");
        assertThat(error.path()).isEqualTo("/movies");
    }
}
