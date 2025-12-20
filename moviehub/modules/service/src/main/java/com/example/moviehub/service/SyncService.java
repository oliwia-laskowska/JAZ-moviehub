package com.example.moviehub.service;

import com.example.moviehub.domain.model.Genre;
import com.example.moviehub.domain.model.Movie;
import com.example.moviehub.integration.client.ExternalMovieClient;
import com.example.moviehub.integration.model.ExternalFilm;
import com.example.moviehub.persistence.repository.GenreRepository;
import com.example.moviehub.persistence.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SyncService {

    private static final Logger log = LoggerFactory.getLogger(SyncService.class);

    private final ExternalMovieClient client;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieService movieService;

    public SyncService(ExternalMovieClient client,
                       MovieRepository movieRepository,
                       GenreRepository genreRepository,
                       MovieService movieService) {
        this.client = client;
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.movieService = movieService;
    }

    @Transactional
    public int syncFromInternet() {
        List<ExternalFilm> films = client.fetchFilms();
        Genre anime = genreRepository.findByName("Anime").orElseGet(() -> {
            try {
                return genreRepository.save(new Genre("Anime"));
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                return genreRepository.findByName("Anime")
                        .orElseThrow(() -> e);
            }
        });

        int upserts = 0;
        for (ExternalFilm f : films) {
            Movie m = movieRepository.findByExternalId(f.id()).orElseGet(() -> new Movie(f.title(), f.id()));
            m.setTitle(f.title());
            m.setDescription(f.description());
            m.setDirector(f.director());
            m.setProducer(f.producer());
            m.setReleaseYear(parseIntSafe(f.releaseDate()));
            m.setRtScore(parseIntSafe(f.rtScore()));
            m.getGenres().add(anime);

            movieRepository.save(m);
            upserts++;
        }

        movieService.evictCaches();
        log.info("Sync finished: {} movies upserted", upserts);
        return upserts;
    }

    private Integer parseIntSafe(String s) {
        try { return s == null ? null : Integer.parseInt(s.trim()); }
        catch (Exception e) { return null; }
    }


}
