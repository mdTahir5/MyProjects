package com.phonebook.service;

import com.phonebook.dto.auth.AuthResponse;
import com.phonebook.dto.auth.LoginRequest;
import com.phonebook.dto.auth.RegisterRequest;
import com.phonebook.exception.AuthenticationFailedException;
import com.phonebook.exception.DuplicateResourceException;
import com.phonebook.exception.ValidationException;
import com.phonebook.repository.RevokedTokenRepository;
import com.phonebook.repository.UserRepository;
import com.phonebook.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Registration / login / logout behaviour.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RevokedTokenRepository revokedTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        revokedTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    private RegisterRequest validRegistration() {
        return new RegisterRequest("Jane Doe", "jane@example.com", "Password@123", "Password@123");
    }

    @Test
    @DisplayName("registration stores a BCrypt hash, never the plaintext password")
    void registrationHashesPassword() {
        authService.register(validRegistration());

        var stored = userRepository.findByEmail("jane@example.com").orElseThrow();
        assertThat(stored.getPasswordHash()).isNotEqualTo("Password@123");
        assertThat(stored.getPasswordHash()).startsWith("$2");
        assertThat(passwordEncoder.matches("Password@123", stored.getPasswordHash())).isTrue();
    }

    @Test
    @DisplayName("registration normalises the email to lower case")
    void registrationNormalisesEmail() {
        authService.register(new RegisterRequest("Jane Doe", "  JANE@Example.COM ", "Password@123", "Password@123"));
        assertThat(userRepository.findByEmail("jane@example.com")).isPresent();
    }

    @Test
    @DisplayName("mismatched confirmation is rejected")
    void rejectsPasswordMismatch() {
        RegisterRequest request = new RegisterRequest("Jane Doe", "jane@example.com", "Password@123", "Other@123");
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("duplicate email is rejected")
    void rejectsDuplicateEmail() {
        authService.register(validRegistration());
        assertThatThrownBy(() -> authService.register(validRegistration()))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("login returns a verifiable token carrying the user id")
    void loginIssuesValidToken() {
        authService.register(validRegistration());

        AuthResponse response = authService.login(new LoginRequest("jane@example.com", "Password@123"));
        assertThat(response.accessToken()).isNotBlank();
        assertThat(response.user().email()).isEqualTo("jane@example.com");

        Claims claims = tokenProvider.parse(response.accessToken());
        assertThat(claims.getSubject()).isEqualTo(String.valueOf(response.user().id()));
        assertThat(claims.getId()).isNotBlank();
    }

    @Test
    @DisplayName("wrong password fails with a generic error")
    void rejectsWrongPassword() {
        authService.register(validRegistration());
        assertThatThrownBy(() -> authService.login(new LoginRequest("jane@example.com", "Wrong@123")))
                .isInstanceOf(AuthenticationFailedException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    @DisplayName("logout adds the token id to the deny-list")
    void logoutRevokesToken() {
        authService.register(validRegistration());
        AuthResponse response = authService.login(new LoginRequest("jane@example.com", "Password@123"));

        Claims claims = tokenProvider.parse(response.accessToken());
        authService.logout(claims.getId(), claims.getExpiration().toInstant(), response.user().id());

        assertThat(revokedTokenRepository.existsByJti(claims.getId())).isTrue();
    }
}
