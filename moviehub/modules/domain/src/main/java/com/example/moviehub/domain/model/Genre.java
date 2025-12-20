package com.example.moviehub.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "genres")
public class Genre extends BaseEntity {

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    protected Genre() {}

    public Genre(String name) { this.name = name; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
