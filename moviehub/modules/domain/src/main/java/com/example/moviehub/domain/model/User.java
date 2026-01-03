package com.example.moviehub.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity // Encja: użytkownik aplikacji
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank // Walidacja: username nie może być pusty
    @Column(nullable = false, unique = true, length = 80) // Unikalny login, max 80 znaków
    private String username;

    @NotBlank // Walidacja: hash hasła nie może być pusty
    @Column(nullable = false, length = 255) // Hash hasła
    private String passwordHash;

    @Column(nullable = false) // Flaga aktywności konta
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER) // Role ładowane od razu
    @JoinTable(
            name = "user_roles", // tabela łącząca users <-> roles
            joinColumns = @JoinColumn(name="user_id"), // FK do users
            inverseJoinColumns = @JoinColumn(name="role_id") // FK do roles
    )
    private Set<Role> roles = new HashSet<>();

    protected User() {} // Wymagane przez JPA

    // Konstruktor pomocniczy (passwordHash powinien być już zahashowany)
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; } // login
    public String getPasswordHash() { return passwordHash; } // hash hasła
    public boolean isEnabled() { return enabled; } // czy konto aktywne
    public Set<Role> getRoles() { return roles; } // role użytkownika

    public void setUsername(String username) { this.username = username; } // zmiana loginu
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; } // zmiana hasha
    public void setEnabled(boolean enabled) { this.enabled = enabled; } // aktywacja/dezaktywacja
}
