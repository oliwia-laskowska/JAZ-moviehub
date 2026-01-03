package com.example.moviehub.persistence.repository;

import com.example.moviehub.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repozytorium JPA dla encji Role
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Szuka roli po nazwie (zwraca Optional, bo może nie istnieć)
    Optional<Role> findByName(String name);
}
