package com.phonebook.dto.auth;

import com.phonebook.dto.user.UserResponse;

/**
 * Authentication result returned by register / login.
 *
 * <p>
 * {@code tokenType} is always {@code Bearer}.
 * </p>
 */
public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        UserResponse user) {
    public static AuthResponse of(String accessToken, long expiresInSeconds, UserResponse user) {
        return new AuthResponse(accessToken, "Bearer", expiresInSeconds, user);
    }
}
