package com.example.moviehub.domain.model;

import jakarta.persistence.*;
import java.time.Instant;

@MappedSuperclass // Klasa bazowa dla encji  (pola dziedziczone przez encje)
public abstract class BaseEntity {

    @Id // Klucz główny
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID generowane przez bazę
    private Long id;

    @Column(nullable = false, updatable = false) // Ustawiane przy tworzeniu, nie zmienia się później
    private Instant createdAt = Instant.now();

    @Column(nullable = false) // Aktualizowane przy update
    private Instant updatedAt = Instant.now();

    @PreUpdate // Hook JPA wywoływany przed aktualizacją encji
    void preUpdate() {
        this.updatedAt = Instant.now(); // odświeża updatedAt przy każdym update
    }

    public Long getId() { return id; } // getter ID
    public Instant getCreatedAt() { return createdAt; } // getter createdAt
    public Instant getUpdatedAt() { return updatedAt; } // getter updatedAt
}
