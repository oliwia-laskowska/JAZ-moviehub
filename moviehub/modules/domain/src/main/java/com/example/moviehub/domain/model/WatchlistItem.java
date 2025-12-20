package com.example.moviehub.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "watchlist_items", uniqueConstraints = {
        @UniqueConstraint(name="uq_watchlist_user_movie", columnNames = {"user_id","movie_id"})
})
public class WatchlistItem extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Movie movie;

    protected WatchlistItem() {}

    public WatchlistItem(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    public User getUser() { return user; }
    public Movie getMovie() { return movie; }
}
