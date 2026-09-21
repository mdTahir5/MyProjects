package com.phonebook.service;

import com.phonebook.domain.AuthProvider;
import com.phonebook.domain.User;
import com.phonebook.dto.auth.AuthResponse;
import com.phonebook.dto.auth.LoginRequest;
import com.phonebook.dto.auth.RegisterRequest;
import com.phonebook.dto.user.UserResponse;
import com.phonebook.exception.AuthenticationFailedException;
import com.phonebook.exception.DuplicateResourceException;
import com.phonebook.exception.ResourceNotFoundException;
import com.phonebook.exception.ValidationException;
import com.phonebook.repository.ContactRepository;
import com.phonebook.repository.RevokedTokenRepository;
import com.phonebook.repository.UserRepository;
import com.phonebook.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;
import java.util.Map;

/**
 * Registration, login and logout.
 *
 * <p>
 * Security notes:
 * </p>
 * <ul>
 * <li>Passwords are BCrypt-hashed before they ever reach the database.</li>
 * <li>A failed login always returns the same generic message so the endpoint
 * cannot be used to enumerate registered emails.</li>
 * <li>Logout records the token's {@code jti} in a deny-list, which is consulted
 * on every subsequent request.</li>
 * </ul>
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    /** The exact word a client must supply to confirm account deletion. */
    public static final String DELETE_CONFIRMATION = "DELETE";

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final RevokedTokenRepository revokedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthService(UserRepository userRepository,
            ContactRepository contactRepository,
            RevokedTokenRepository revokedTokenRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
        this.revokedTokenRepository = revokedTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    // ------------------------------------------------------------------
    // Register
    // ------------------------------------------------------------------

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = normaliseEmail(request.email());

        if (!request.passwordsMatch()) {
            throw new ValidationException("Passwords do not match",
                    Map.of("confirmPassword", "Password and confirm password must match"));
        }
        if (userRepository.existsByEmail(email)) {
            // Generic enough to be useful, and the field-level error makes the UI precise.
            throw new DuplicateResourceException("An account with this email already exists", "email");
        }

        User user = new User(
                request.name().trim(),
                email,
                passwordEncoder.encode(request.password()),
                AuthProvider.LOCAL);
        user.setEmailVerified(false);

        try {
            user = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            // Defence in depth: the unique index is the source of truth, so a
            // concurrent registration cannot create duplicates.
            log.warn("Concurrent duplicate registration attempt for {}", email);
            throw new DuplicateResourceException("An account with this email already exists", "email");
        }

        log.info("Registered new user id={} provider={}", user.getId(), user.getProvider());
        return buildAuthResponse(user);
    }

    // ------------------------------------------------------------------
    // Login
    // ------------------------------------------------------------------

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = normaliseEmail(request.email());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password()));
        } catch (AuthenticationException ex) {
            // Do not distinguish "unknown email" from "wrong password".
            log.warn("Failed login attempt for email={}", maskEmail(email));
            throw new AuthenticationFailedException("Invalid email or password");
        }

        Object principal = authentication.getPrincipal();
        Long userId = principal instanceof com.phonebook.security.AppUserPrincipal appUser
                ? appUser.getId()
                : null;

        User user = userRepository.findById(userId == null ? -1L : userId)
                .orElseThrow(() -> new AuthenticationFailedException("Invalid email or password"));

        log.info("Successful login for user id={}", user.getId());
        return buildAuthResponse(user);
    }

    // ------------------------------------------------------------------
    // Logout
    // ------------------------------------------------------------------

    /**
     * Revokes the presented access token.
     *
     * @param jti       token id claim
     * @param expiresAt token expiry (the row is kept until then)
     * @param userId    owning user, may be {@code null} when unknown
     */
    @Transactional
    public void logout(String jti, Instant expiresAt, Long userId) {
        if (jti == null || jti.isBlank()) {
            return;
        }
        if (revokedTokenRepository.existsByJti(jti)) {
            return;
        }
        User user = userId == null
                ? null
                : userRepository.findById(userId).orElse(null);

        revokedTokenRepository.save(
                new com.phonebook.domain.RevokedToken(
                        jti,
                        user,
                        expiresAt == null ? Instant.now() : expiresAt));

        log.info("Access token revoked (jti={}, userId={})", jti, userId);
    }

    // ------------------------------------------------------------------
    // Delete account
    // ------------------------------------------------------------------

    /**
     * Permanently removes the authenticated user and everything they own.
     *
     * <p>The caller must echo the literal word {@code DELETE}, which mirrors the
     * browser-side confirmation. The check is repeated here on purpose: the UI is
     * a convenience, not a security boundary.</p>
     *
     * <p>Ordering matters. {@code contacts} and {@code revoked_tokens} both hold a
     * foreign key to {@code users}, so they are cleared first; otherwise the
     * delete would fail with a constraint violation. The whole method runs in one
     * transaction, so a failure at any step leaves the account fully intact.</p>
     *
     * @param userId       the authenticated user's id
     * @param confirmation must equal {@value #DELETE_CONFIRMATION} (case-sensitive)
     */
    @Transactional
    public void deleteAccount(Long userId, String confirmation) {
        if (confirmation == null || !DELETE_CONFIRMATION.equals(confirmation.trim())) {
            throw new ValidationException("Type DELETE to confirm account deletion",
                    Map.of("confirmation", "Type DELETE exactly as shown to confirm"));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found"));

        int contactsRemoved = contactRepository.deleteAllByUserId(user.getId());
        int tokensRemoved = revokedTokenRepository.deleteAllByUserId(user.getId());

        userRepository.delete(user);
        userRepository.flush();

        log.info("Deleted account id={} (contacts removed: {}, revoked tokens removed: {})",
                user.getId(), contactsRemoved, tokensRemoved);
    }

    // ------------------------------------------------------------------
    // Current user
    // ------------------------------------------------------------------

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User account not found"));
        return UserResponse.from(user);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private AuthResponse buildAuthResponse(User user) {
        JwtTokenProvider.IssuedToken issued = tokenProvider.issue(user);
        return AuthResponse.of(issued.token(), issued.expiresInSeconds(), UserResponse.from(user));
    }

    private static String normaliseEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    /** Keeps logs privacy-friendly: {@code j***@example.com}. */
    private static String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 1) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(at);
    }
}
