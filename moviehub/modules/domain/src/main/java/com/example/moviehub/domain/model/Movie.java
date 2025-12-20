package com.example.moviehub.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies", indexes = {
        @Index(name = "idx_movie_external_id", columnList = "externalId", unique = true)
})
public class Movie extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    @Column(length = 120)
    private String director;

    @Column(length = 120)
    private String producer;

    private Integer releaseYear;

    private Integer rtScore;

    @Column(nullable = false, length = 100)
    private String externalId;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name="movie_id"),
            inverseJoinColumns = @JoinColumn(name="genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    protected Movie() {}

    public Movie(String title, String externalId) {
        this.title = title;
        this.externalId = externalId;
    }

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
