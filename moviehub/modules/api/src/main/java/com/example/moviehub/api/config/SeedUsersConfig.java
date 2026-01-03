package com.example.moviehub.api.config;

import com.example.moviehub.domain.model.Role;
import com.example.moviehub.domain.model.User;
import com.example.moviehub.persistence.repository.RoleRepository;
import com.example.moviehub.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Konfiguracja seedowania danych startowych
public class SeedUsersConfig {

    @Bean // Uruchamia się automatycznie przy starcie aplikacji
    CommandLineRunner seedSecurityUsers(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {

            // Tworzy rolę ADMIN jeśli nie istnieje
            Role adminRole = roleRepo.findByName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role("ADMIN")));

            // Tworzy rolę USER jeśli nie istnieje
            Role userRole = roleRepo.findByName("USER")
                    .orElseGet(() -> roleRepo.save(new Role("USER")));

            // Jeśli user "admin" istnieje -> dopina rolę i aktywuje konto, jeśli nie -> tworzy nowego
            userRepo.findByUsername("admin").ifPresentOrElse(admin -> {
                admin.getRoles().add(adminRole); // zapewnia rolę ADMIN
                admin.setEnabled(true); // konto aktywne
                userRepo.save(admin);
            }, () -> {
                User admin = new User("admin", encoder.encode("admin123")); // hasło zahashowane
                admin.getRoles().add(adminRole); // nadaje rolę ADMIN
                admin.setEnabled(true); // konto aktywne
                userRepo.save(admin); // zapis do bazy
            });
        };
    }
}
