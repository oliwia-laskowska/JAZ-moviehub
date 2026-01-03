package com.example.moviehub.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity // Encja: recenzja filmu
@Table(
        name = "reviews",
        indexes = {
                // Index pod szybkie wyszukiwanie recenzji usera dla danego filmu
                @Index(name = "idx_review_user_movie", columnList = "user_id,movie_id")
        }
)
public class Review extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Recenzja należy do jednego użytkownika
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Recenzja dotyczy jednego filmu
    private Movie movie;

    @Min(1) @Max(10) // Walidacja: zakres ocen 1-10
    @Column(nullable = false) // Ocena jest wymagana
    private int rating;

    @NotBlank // Walidacja: komentarz nie może być pusty
    @Column(nullable = false, length = 2000) // Maks 2000 znaków
    private String comment;

    public Review() {}

    // Konstruktor pomocniczy do tworzenia nowej recenzji
    public Review(User user, Movie movie, int rating, String comment) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.comment = comment;
    }

    public User getUser() { return user; } // autor
    public Movie getMovie() { return movie; } // film
    public int getRating() { return rating; } // ocena
    public String getComment() { return comment; } // komentarz

    public void setRating(int rating) { this.rating = rating; } // aktualizacja oceny
    public void setComment(String comment) { this.comment = comment; } // aktualizacja komentarza
}
