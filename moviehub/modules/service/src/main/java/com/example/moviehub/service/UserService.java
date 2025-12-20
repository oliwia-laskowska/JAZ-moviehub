package com.example.moviehub.service;

import com.example.moviehub.domain.model.Role;
import com.example.moviehub.domain.model.User;
import com.example.moviehub.persistence.repository.RoleRepository;
import com.example.moviehub.persistence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @Transactional
    public User ensureUser(String username, String rawPassword, String roleName) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            Role role = roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(roleName)));
            User u = new User(username, encoder.encode(rawPassword));
            u.getRoles().add(role);
            return userRepository.save(u);
        });
    }
}
