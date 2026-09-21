package com.phonebook.dto.contact;

import com.phonebook.domain.Contact;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Payload for creating or updating a contact.
 *
 * <p>
 * {@code userId} is intentionally absent: ownership always comes from the
 * authenticated principal, never from the client.
 * </p>
 */
public record ContactRequest(

        @NotBlank(message = "Contact name is required") @Size(min = 1, max = 100, message = "Contact name must be between 1 and 100 characters") String name,

        @Size(max = 255, message = "Email must not exceed 255 characters") @Email(message = "Email must be a valid email address") String email,

        @NotBlank(message = "Phone number is required") @Size(min = 3, max = 30, message = "Phone number must be between 3 and 30 characters") @Pattern(regexp = "^[+()\\d\\s.-]+$", message = "Phone number may only contain digits, spaces and the characters + ( ) - .") String phone) {
    public Contact toEntity() {
        return new Contact(null, name, email, phone);
    }
}
