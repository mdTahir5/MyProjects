package com.phonebook.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Registration payload.
 *
 * <p>
 * Password confirmation is validated in {@code AuthService} (and mirrored on
 * the client) so the API can return a precise, field-level error.
 * </p>
 */
public record RegisterRequest(

                @NotBlank(message = "Name is required") @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters") @Pattern(regexp = "^[\\p{L}][\\p{L}\\p{M}' .-]*$", message = "Name may only contain letters, spaces, apostrophes, dots and hyphens") String name,

                @NotBlank(message = "Email is required") @Email(message = "Email must be a valid email address") @Size(max = 255, message = "Email must not exceed 255 characters") String email,

                @NotBlank(message = "Password is required") @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).+$", message = "Password must contain at least one uppercase letter, one lowercase letter, "
                                + "one digit and one special character") String password,

                @NotBlank(message = "Confirm password is required") String confirmPassword) {
        public boolean passwordsMatch() {
                return password != null && password.equals(confirmPassword);
        }
}
