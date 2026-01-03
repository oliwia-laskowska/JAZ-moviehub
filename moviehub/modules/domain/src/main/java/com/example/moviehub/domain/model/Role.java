package com.example.moviehub.domain.model;

import jakarta.persistence.*;

@Entity // Encja: rola użytkownika (np. ADMIN, USER)
@Table(name = "roles") // Mapa na tabelę roles
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50) // Nazwa roli: wymagana, unikalna, max 50 znaków
    private String name;

    protected Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() { return name; } // getter nazwy roli
    public void setName(String name) { this.name = name; } // setter nazwy roli
}
