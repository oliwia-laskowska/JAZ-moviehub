package com.example.moviehub.service;

import com.example.moviehub.domain.model.Role;
import com.example.moviehub.domain.model.User;
import com.example.moviehub.persistence.repository.RoleRepository;
import com.example.moviehub.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        encoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, roleRepository, encoder);
    }

    @Test
    void ensureUser_shouldReturnExistingUser_whenUserExists() {
        User existing = new User("oliwia", "hash");
        when(userRepository.findByUsername("oliwia")).thenReturn(Optional.of(existing));

        User result = userService.ensureUser("oliwia", "pass", UserService.ROLE_USER);

        assertThat(result).isSameAs(existing);

        verify(userRepository).findByUsername("oliwia");
        verifyNoInteractions(roleRepository);
        verifyNoInteractions(encoder);
        verify(userRepository, never()).save(any());
    }

    @Test
    void ensureUser_shouldCreateUser_withExistingRole() {
        when(userRepository.findByUsername("oliwia")).thenReturn(Optional.empty());

        Role role = new Role(UserService.ROLE_USER);
        when(roleRepository.findByName(UserService.ROLE_USER)).thenReturn(Optional.of(role));

        when(encoder.encode("pass")).thenReturn("ENC(pass)");

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.ensureUser("oliwia", "pass", UserService.ROLE_USER);

        assertThat(saved.getUsername()).isEqualTo("oliwia");
        assertThat(saved.getPasswordHash()).isEqualTo("ENC(pass)");
        assertThat(saved.getRoles()).contains(role);

        verify(roleRepository, never()).save(any()); // rola już była
        verify(userRepository).save(any(User.class));
    }

    @Test
    void ensureUser_shouldCreateRoleAndUser_whenRoleMissing() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());

        when(roleRepository.findByName(UserService.ROLE_ADMIN)).thenReturn(Optional.empty());

        // save role -> zwróć argument
        when(roleRepository.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        when(encoder.encode("secret")).thenReturn("ENC(secret)");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User saved = userService.ensureUser("admin", "secret", UserService.ROLE_ADMIN);

        assertThat(saved.getUsername()).isEqualTo("admin");
        assertThat(saved.getPasswordHash()).isEqualTo("ENC(secret)");
        assertThat(saved.getRoles()).extracting(Role::getName).contains(UserService.ROLE_ADMIN);

        verify(roleRepository).save(any(Role.class));
        verify(userRepository).save(any(User.class));
    }
}
