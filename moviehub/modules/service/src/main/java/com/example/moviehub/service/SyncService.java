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

@Service
public class SyncService {

    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    private static final String SRC_GHIBLI = "GHIBLI";
    private static final String SRC_TMDB = "TMDB";

    private final ExternalMovieClient ghibliClient;
    private final TmdbClient tmdbClient;

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieService movieService;

    public SyncService(ExternalMovieClient ghibliClient,
                       TmdbClient tmdbClient,
                       MovieRepository movieRepository,
                       GenreRepository genreRepository,
                       MovieService movieService) {
        this.ghibliClient = ghibliClient;
        this.tmdbClient = tmdbClient;
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieService = movieService;
    }

    @Transactional
    public int syncFromInternet() {
        int upserts = 0;

        // ===== GHIBLI =====
        Genre anime = getOrCreateGenre("Anime");
        int ghibliUpserts = 0;

        for (var f : ghibliClient.fetchFilms()) {
            Movie m = movieRepository
                    .findByExternalSourceAndExternalId(SRC_GHIBLI, f.id())
                    .orElseGet(() -> new Movie(safeTitle(f.title()), SRC_GHIBLI, f.id()));

            m.setExternalSource(SRC_GHIBLI);

            m.setTitle(safeTitle(f.title()));
            m.setDescription(f.description());
            m.setDirector(f.director());
            m.setProducer(f.producer());
            m.setReleaseYear(parseIntSafe(f.releaseDate()));
            m.setRtScore(parseIntSafe(f.rtScore()));
            m.getGenres().add(anime);

            movieRepository.save(m);
            upserts++;
            ghibliUpserts++;
        }

        // ===== TMDB =====
        Genre tmdbTag = getOrCreateGenre("TMDB");
        int tmdbUpserts = 0;

        var tmdbMovies = tmdbClient.fetchPopular(5); // ~100

        for (var t : tmdbMovies) {
            String extId = String.valueOf(t.id());

            Movie m = movieRepository
                    .findByExternalSourceAndExternalId(SRC_TMDB, extId)
                    .orElseGet(() -> new Movie(safeTitle(t.title()), SRC_TMDB, extId));

            m.setExternalSource(SRC_TMDB);

            m.setTitle(safeTitle(t.title()));
            m.setDescription(t.overview());
            m.setReleaseYear(parseYearFromDate(t.releaseDate()));
            m.setRtScore(t.voteAverage() == null ? null : (int) Math.round(t.voteAverage() * 10.0));

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

            m.getGenres().add(tmdbTag);

            movieRepository.save(m);
            upserts++;
            tmdbUpserts++;
        }

        movieService.evictCaches();
        log.info("Sync finished: {} upserts (GHIBLI={}, TMDB={})", upserts, ghibliUpserts, tmdbUpserts);
        return upserts;
    }

    private Genre getOrCreateGenre(String name) {
        return genreRepository.findByName(name).orElseGet(() -> {
            try {
                return genreRepository.save(new Genre(name));
            } catch (DataIntegrityViolationException e) {
                return genreRepository.findByName(name).orElseThrow(() -> e);
            }
        });
    }

    private String safeTitle(String s) {
        if (s == null) return "Untitled";
        String t = s.trim();
        return t.isEmpty() ? "Untitled" : t;
    }

    private Integer parseIntSafe(String s) {
        try { return s == null ? null : Integer.parseInt(s.trim()); }
        catch (Exception e) { return null; }
    }

    private Integer parseYearFromDate(String yyyyMmDd) {
        try {
            if (yyyyMmDd == null || yyyyMmDd.length() < 4) return null;
            return Integer.parseInt(yyyyMmDd.substring(0, 4));
        } catch (Exception e) {
            return null;
        }
    }
}
