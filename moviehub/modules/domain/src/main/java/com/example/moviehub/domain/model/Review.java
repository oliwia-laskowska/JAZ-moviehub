package com.example.moviehub.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "reviews", indexes = {
        @Index(name = "idx_review_user_movie", columnList = "user_id,movie_id")
})
public class Review extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Movie movie;

    @Min(1) @Max(10)
    @Column(nullable = false)
    private int rating;

    @NotBlank
    @Column(nullable = false, length = 2000)
    private String comment;

    public Review() {}

    public Review(User user, Movie movie, int rating, String comment) {
        this.user = user;
        this.movie = movie;
        this.rating = rating;
        this.comment = comment;
    }

    public User getUser() { return user; }
    public Movie getMovie() { return movie; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }

    public void setRating(int rating) { this.rating = rating; }
    public void setComment(String comment) { this.comment = comment; }
}
