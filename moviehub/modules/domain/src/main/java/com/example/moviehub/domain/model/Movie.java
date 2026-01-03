package com.example.moviehub.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "movies",
        indexes = {
                // Unikalność filmu w obrębie źródła
                @Index(name = "idx_movie_source_external", columnList = "externalSource,externalId", unique = true)
        }
)
public class Movie extends BaseEntity {

    @NotBlank // Walidacja: tytuł nie może być pusty
    @Column(nullable = false, length = 200) // Kolumna obowiązkowa, max 200 znaków
    private String title;

    @Lob // Dłuższy tekst (np. TEXT/CLOB)
    private String description;

    @Column(length = 120) // Opcjonalne pole, max 120 znaków
    private String director;

    @Column(length = 120) // Opcjonalne pole, max 120 znaków
    private String producer;

    private Integer releaseYear; // Rok wydania (może być null)
    private Integer rtScore; // Ocena

    @Column(nullable = false, length = 100) // ID filmu w zewnętrznym źródle (np. TMDB)
    private String externalId;

    @ManyToMany // Relacja wiele-do-wielu: film <-> gatunki
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name="movie_id"), // FK do movies
            inverseJoinColumns = @JoinColumn(name="genre_id") // FK do genres
    )
    private Set<Genre> genres = new HashSet<>();

    @Column(nullable = false, length = 20) // Źródło danych (np. GHIBLI, TMDB)
    private String externalSource;

    public Movie() {}


    public Movie(String title, String externalSource, String externalId) {
        this.title = title;
        this.externalSource = externalSource;
        this.externalId = externalId;
    }

    public String getExternalSource() { return externalSource; }
    public void setExternalSource(String externalSource) { this.externalSource = externalSource; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDirector() { return director; }
    public String getProducer() { return producer; }
    public Integer getReleaseYear() { return releaseYear; }
    public Integer getRtScore() { return rtScore; }
    public String getExternalId() { return externalId; }
    public Set<Genre> getGenres() { return genres; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDirector(String director) { this.director = director; }
    public void setProducer(String producer) { this.producer = producer; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public void setRtScore(Integer rtScore) { this.rtScore = rtScore; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
}
