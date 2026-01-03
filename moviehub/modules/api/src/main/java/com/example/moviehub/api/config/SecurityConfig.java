package com.example.moviehub.api.config;

import com.example.moviehub.persistence.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.stream.Collectors;

@Configuration
@EnableMethodSecurity // Włącza @PreAuthorize / @Secured na metodach
public class SecurityConfig {

    @Bean // Haszowanie haseł
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // Ładuje użytkownika z bazy i mapuje role na ROLE_*
    UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
                .map(u -> org.springframework.security.core.userdetails.User.withUsername(u.getUsername())
                        .password(u.getPasswordHash())
                        .disabled(!u.isEnabled()) // jeśli user zablokowany -> nie zaloguje się
                        .authorities(u.getRoles().stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                                .collect(Collectors.toSet()))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Bean // Provider autoryzacji oparty o UserDetailsService + PasswordEncoder
    DaoAuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean // Główny łańcuch filtrów bezpieczeństwa
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
        return http
                .authenticationProvider(authenticationProvider) // używa providera
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS dla frontu
                .csrf(csrf -> csrf.disable()) // dla API/frontu (cookies + SPA)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // preflight CORS
                        .requestMatchers("/", "/movies", "/css/**", "/js/**", "/webjars/**", "/ws/**").permitAll() // publiczne UI/statyczne
                        .requestMatchers("/admin/**").hasRole("ADMIN") // panel admina
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // admin API
                        .requestMatchers("/api/**").permitAll() // publiczne endpointy API
                        .anyRequest().authenticated() // reszta wymaga logowania
                )

                .formLogin(form -> form
                        .loginPage("/login") // własny endpoint logowania
                        .successHandler(roleBasedSuccessHandler()) // redirect zależny od roli
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // endpoint wylogowania
                        .logoutSuccessUrl("/") // po wylogowaniu wraca na /
                        .invalidateHttpSession(true) // czyści sesję
                        .deleteCookies("JSESSIONID") // usuwa cookie sesyjne
                )

                // Basic auth  do testów API przez curl
                .httpBasic(Customizer.withDefaults())

                // API zwraca 401 zamiast przekierowania, UI przekierowuje na /login
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                (req, res, e) -> res.sendError(401),
                                new AntPathRequestMatcher("/api/**")
                        )
                        .defaultAuthenticationEntryPointFor(
                                new org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint("/login"),
                                new AntPathRequestMatcher("/**")
                        )
                )
                .build();
    }

    @Bean // Redirect po loginie na podstawie roli (admin -> /admin)
    public RoleBasedSuccessHandler roleBasedSuccessHandler() {
        return new RoleBasedSuccessHandler();
    }

    @Bean // Globalna konfiguracja CORS
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // pozwala na cookies / auth
        config.addAllowedOriginPattern("http://localhost:517*"); // dopuszcza localhost:517x
        config.addAllowedHeader("*"); // dowolne nagłówki
        config.addAllowedMethod("*"); // dowolne metody

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // stosuje do wszystkich ścieżek
        return source;
    }
}
