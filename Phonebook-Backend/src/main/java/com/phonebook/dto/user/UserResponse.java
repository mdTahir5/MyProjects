package com.phonebook.dto.user;

import com.phonebook.domain.User;

import java.time.Instant;

/**
 * Public representation of a user. Never contains password material.
 */
public record UserResponse(
        Long id,
        String name,
        String email,
        String provider,
        boolean emailVerified,
        Instant createdAt) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProvider().name(),
                user.isEmailVerified(),
                user.getCreatedAt());
    }
}
