package com.example.moviehub.service.cache;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CacheNamesTest {

    @Test
    void constants_shouldHaveExpectedValues() {
        assertThat(CacheNames.MOVIES).isEqualTo("movies");
        assertThat(CacheNames.MOVIE_BY_ID).isEqualTo("movieById");
    }
}
