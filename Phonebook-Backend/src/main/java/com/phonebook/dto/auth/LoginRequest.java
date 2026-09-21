package com.phonebook.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Login payload.
 *
 * <p>Deliberately loose on the password rules: only non-blank and a maximum
 * length are enforced, so a wrong password can never be distinguished from a
 * malformed one by the response.</p>
 */
public record LoginRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Size(max = 255, message = "Email must not exceed 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(max = 72, message = "Password must not exceed 72 characters")
        String password
) {
}
