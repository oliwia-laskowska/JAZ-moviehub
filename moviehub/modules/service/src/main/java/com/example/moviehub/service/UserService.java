package com.example.moviehub.service;

import com.example.moviehub.domain.model.Role;
import com.example.moviehub.domain.model.User;
import com.example.moviehub.persistence.repository.RoleRepository;
import com.example.moviehub.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // Serwis do operacji na użytkownikach
public class UserService {

    // Stałe nazw ról używanych w Security (format ROLE_*)
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository; // repo użytkowników
    private final RoleRepository roleRepository; // repo ról
    private final PasswordEncoder encoder; // hashowanie haseł (BCrypt)

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Transactional // Zapewnia spójność: role + user zapisują się razem
    public User ensureUser(String username, String rawPassword, String roleName) {

        // Jeśli user istnieje -> zwraca go, jeśli nie -> tworzy nowego
        return userRepository.findByUsername(username).orElseGet(() -> {

            // Pobiera rolę lub tworzy ją jeśli nie istnieje
            Role role = roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));

            // Tworzy użytkownika z hashem hasła (rawPassword nie zapisuje się do DB)
            User u = new User(username, encoder.encode(rawPassword));
            u.getRoles().add(role); // przypisuje rolę

            return userRepository.save(u); // zapis nowego usera
        });
    }
}
