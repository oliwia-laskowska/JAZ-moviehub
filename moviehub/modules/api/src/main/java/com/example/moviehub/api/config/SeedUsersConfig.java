package com.example.moviehub.api.config;

import com.example.moviehub.domain.model.Role;
import com.example.moviehub.domain.model.User;
import com.example.moviehub.persistence.repository.RoleRepository;
import com.example.moviehub.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedUsersConfig {

    @Bean
    CommandLineRunner seedSecurityUsers(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder encoder) {
        return args -> {
            Role adminRole = roleRepo.findByName("ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role("ADMIN")));

            Role userRole = roleRepo.findByName("USER")
                    .orElseGet(() -> roleRepo.save(new Role("USER")));

            userRepo.findByUsername("admin").ifPresentOrElse(admin -> {
                admin.getRoles().add(adminRole);
                admin.setEnabled(true);
                userRepo.save(admin);
            }, () -> {
                User admin = new User("admin", encoder.encode("admin123"));
                admin.getRoles().add(adminRole);
                admin.setEnabled(true);
                userRepo.save(admin);
            });
        };
    }
}
