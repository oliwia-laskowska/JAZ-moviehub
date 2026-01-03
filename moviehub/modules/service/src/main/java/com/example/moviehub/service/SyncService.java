package com.example.moviehub.service;

import com.example.moviehub.domain.model.Genre;
import com.example.moviehub.domain.model.Movie;
import com.example.moviehub.integration.client.ExternalMovieClient;
import com.example.moviehub.integration.client.TmdbClient;
import com.example.moviehub.persistence.repository.GenreRepository;
import com.example.moviehub.persistence.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Serwis odpowiedzialny za synchronizację filmów z zewnętrznych API
public class SyncService {

    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    // Stałe oznaczające źródło danych
    private static final String SRC_GHIBLI = "GHIBLI";
    private static final String SRC_TMDB = "TMDB";

    private final ExternalMovieClient ghibliClient; // klient do Ghibli API
    private final TmdbClient tmdbClient; // klient do TMDB API

    private final MovieRepository movieRepository; // zapis/odczyt filmów
    private final GenreRepository genreRepository; // zapis/odczyt gatunków/tagów
    private final MovieService movieService; // do czyszczenia cache po sync

    public SyncService(
            ExternalMovieClient ghibliClient,
            TmdbClient tmdbClient,
            MovieRepository movieRepository,
            GenreRepository genreRepository,
            MovieService movieService
    ) {
        this.ghibliClient = ghibliClient;
        this.tmdbClient = tmdbClient;
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieService = movieService;
    }

    @Transactional // Cały sync w jednej transakcji (upsert + powiązania)
    public int syncFromInternet() {
        int upserts = 0;

        // ===== GHIBLI =====
        Genre anime = getOrCreateGenre("Anime"); // tag dla filmów Ghibli
        int ghibliUpserts = 0;

        for (var f : ghibliClient.fetchFilms()) {

            // Upsert: jeśli film istnieje po (source+externalId) -> update, inaczej -> create
            Movie m = movieRepository
                    .findByExternalSourceAndExternalId(SRC_GHIBLI, f.id())
                    .orElseGet(() -> new Movie(safeTitle(f.title()), SRC_GHIBLI, f.id()));

            m.setExternalSource(SRC_GHIBLI); // na wszelki wypadek utrzymuje spójność

            // Mapowanie pól z API na encję
            m.setTitle(safeTitle(f.title()));
            m.setDescription(f.description());
            m.setDirector(f.director());
            m.setProducer(f.producer());
            m.setReleaseYear(parseIntSafe(f.releaseDate()));
            m.setRtScore(parseIntSafe(f.rtScore()));
            m.getGenres().add(anime); // dopina tag/gatunek

            movieRepository.save(m); // zapis do DB
            upserts++;
            ghibliUpserts++;
        }

        // ===== TMDB =====
        Genre tmdbTag = getOrCreateGenre("TMDB"); // tag dla filmów z TMDB
        int tmdbUpserts = 0;

        var tmdbMovies = tmdbClient.fetchPopular(5); // pobiera popularne filmy (ok. 100)

        for (var t : tmdbMovies) {
            String extId = String.valueOf(t.id()); // ID z TMDB jako String

            // Upsert po (TMDB + id)
            Movie m = movieRepository
                    .findByExternalSourceAndExternalId(SRC_TMDB, extId)
                    .orElseGet(() -> new Movie(safeTitle(t.title()), SRC_TMDB, extId));

            m.setExternalSource(SRC_TMDB);

            // Mapowanie danych z TMDB
            m.setTitle(safeTitle(t.title()));
            m.setDescription(t.overview());
            m.setReleaseYear(parseYearFromDate(t.releaseDate()));
            m.setRtScore(t.voteAverage() == null ? null : (int) Math.round(t.voteAverage() * 10.0)); // 0-10 -> 0-100

            // Jeśli nie ma reżysera -> dociąga credits i próbuje go ustawić
            if (m.getDirector() == null || m.getDirector().isBlank()) {
                try {
                    String director = tmdbClient.fetchDirectorName(t.id());
                    if (director != null && !director.isBlank()) {
                        m.setDirector(director);
                    }
                } catch (Exception e) {
                    log.warn("TMDB credits failed for id={}, title={}", t.id(), t.title(), e);
                }
            }

            m.getGenres().add(tmdbTag); // dopina tag TMDB

            movieRepository.save(m);
            upserts++;
            tmdbUpserts++;
        }

        // Po zmianach danych czyści cache, żeby API/UI nie pokazywało starych wyników
        movieService.evictCaches();

        log.info("Sync finished: {} upserts (GHIBLI={}, TMDB={})", upserts, ghibliUpserts, tmdbUpserts);
        return upserts; // liczba zapisanych/zmienionych rekordów
    }

    // Zwraca istniejący Genre albo tworzy nowy (z obsługą race condition)
    private Genre getOrCreateGenre(String name) {
        return genreRepository.findByName(name).orElseGet(() -> {
            try {
                return genreRepository.save(new Genre(name));
            } catch (DataIntegrityViolationException e) {
                // jeśli w międzyczasie ktoś już dodał ten gatunek -> pobierz z DB
                return genreRepository.findByName(name).orElseThrow(() -> e);
            }
        });
    }

    // Zabezpiecza tytuł przed null/pustym stringiem
    private String safeTitle(String s) {
        if (s == null) return "Untitled";
        String t = s.trim();
        return t.isEmpty() ? "Untitled" : t;
    }

    // Bezpieczne parsowanie int (null/invalid -> null)
    private Integer parseIntSafe(String s) {
        try { return s == null ? null : Integer.parseInt(s.trim()); }
        catch (Exception e) { return null; }
    }

    // Wyciąga rok z daty typu yyyy-MM-dd (invalid -> null)
    private Integer parseYearFromDate(String yyyyMmDd) {
        try {
            if (yyyyMmDd == null || yyyyMmDd.length() < 4) return null;
            return Integer.parseInt(yyyyMmDd.substring(0, 4));
        } catch (Exception e) {
            return null;
        }
    }
}
