package com.example.moviehub.domain.model;

import jakarta.persistence.*;

@Entity // Encja: element watchlisty użytkownika
@Table(
        name = "watchlist_items",
        uniqueConstraints = {
                // Jeden film może być dodany do watchlisty użytkownika tylko raz
                @UniqueConstraint(name="uq_watchlist_user_movie", columnNames = {"user_id","movie_id"})
        }
)
public class WatchlistItem extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Wpis watchlisty należy do użytkownika
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY) // Wpis watchlisty wskazuje na konkretny film
    private Movie movie;

    protected WatchlistItem() {}

    // Konstruktor pomocniczy do utworzenia wpisu watchlisty
    public WatchlistItem(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    public User getUser() { return user; } // właściciel wpisu
    public Movie getMovie() { return movie; } // film dodany do watchlisty
}
