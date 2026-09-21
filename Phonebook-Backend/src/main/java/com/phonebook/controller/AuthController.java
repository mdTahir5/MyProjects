package com.phonebook.controller;

import com.phonebook.dto.auth.AuthResponse;
import com.phonebook.dto.auth.LoginRequest;
import com.phonebook.dto.auth.RegisterRequest;
import com.phonebook.dto.user.DeleteAccountRequest;
import com.phonebook.dto.user.UserResponse;
import com.phonebook.security.AppUserPrincipal;
import com.phonebook.security.CurrentUserProvider;
import com.phonebook.security.SecurityConstants;
import com.phonebook.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Authentication endpoints.
 *
 * <p>
 * {@code /register} and {@code /login} are public; {@code /me} and
 * {@code /logout} require a valid access token.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final CurrentUserProvider currentUserProvider;

    public AuthController(AuthService authService, CurrentUserProvider currentUserProvider) {
        this.authService = authService;
        this.currentUserProvider = currentUserProvider;
    }

    /** Creates a new account and immediately returns an access token. */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Exchanges credentials for an access token. */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Returns the authenticated user.
     *
     * <p>
     * This is the endpoint the SPA calls on start-up to restore the session
     * from a token kept in local storage.
     * </p>
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> currentUser() {
        AppUserPrincipal principal = currentUserProvider.requirePrincipal();
        return ResponseEntity.ok(authService.getCurrentUser(principal.getId()));
    }

    /**
     * Revokes the presented access token.
     *
     * <p>
     * Always succeeds, even if the token was already revoked, so the client
     * can treat logout as idempotent.
     * </p>
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        String jti = (String) request.getAttribute(SecurityConstants.ATTR_JTI);
        Instant expiresAt = (Instant) request.getAttribute(SecurityConstants.ATTR_TOKEN_EXPIRY);
        Long userId = (Long) request.getAttribute(SecurityConstants.ATTR_USER_ID);

        authService.logout(jti, expiresAt, userId);
        log.debug("User id={} logged out", userId);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    /**
     * Permanently deletes the authenticated user's account and all of their data.
     *
     * <p>The body must echo the literal word {@code DELETE}, which mirrors the
     * "type DELETE to confirm" prompt in the UI. On success the account, its
     * contacts and its token deny-list entries are gone, so the presented token
     * stops resolving to a user on the very next request.</p>
     *
     * <p>Returns {@code 200} with a message rather than {@code 204}: the client
     * shows a success popup and having the server's own wording available keeps
     * that copy in one place.</p>
     */
    @DeleteMapping("/account")
    public ResponseEntity<Map<String, String>> deleteAccount(
            @Valid @RequestBody DeleteAccountRequest request) {

        AppUserPrincipal principal = currentUserProvider.requirePrincipal();
        authService.deleteAccount(principal.getId(), request.confirmation());

        log.debug("User id={} deleted their account", principal.getId());

        return ResponseEntity.ok(Map.of(
                "message", "Your account has been deleted successfully"));
    }
}
