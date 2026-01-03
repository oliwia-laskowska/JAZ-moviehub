package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repozytorium JPA dla encji User
public interface UserRepository extends JpaRepository<User, Long> {

    // Szuka użytkownika po username (Optional, bo może nie istnieć)
    Optional<User> findByUsername(String username);

    // Szybkie sprawdzenie czy użytkownik o danym username istnieje
    boolean existsByUsername(String username);
}
