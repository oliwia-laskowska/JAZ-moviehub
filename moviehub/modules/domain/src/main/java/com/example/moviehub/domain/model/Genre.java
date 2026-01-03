package com.example.moviehub.domain.model;

import jakarta.persistence.*;

@Entity // Encja JPA reprezentująca gatunek filmu
@Table(name = "genres") // Mapa na tabelę "genres"
public class Genre extends BaseEntity {

    @Column(nullable = false, unique = true, length = 80) // Nazwa gatunku: wymagana, unikalna, max 80 znaków
    private String name;

    protected Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public String getName() { return name; } // getter nazwy
    public void setName(String name) { this.name = name; } // setter nazwy
}
