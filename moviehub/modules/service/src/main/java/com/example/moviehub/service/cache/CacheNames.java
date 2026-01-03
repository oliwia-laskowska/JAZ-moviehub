package com.example.moviehub.service.cache;

// Stałe nazwy cache używane w adnotacjach
public final class CacheNames {

    private CacheNames() {}

    public static final String MOVIES = "movies"; // cache dla listy filmów
    public static final String MOVIE_BY_ID = "movieById"; // cache dla pojedynczego filmu po ID
}
