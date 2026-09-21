package com.phonebook.dto.contact;

import com.phonebook.domain.Contact;

import java.time.Instant;

/**
 * Public representation of a contact.
 */
public record ContactResponse(
        Long id,
        String name,
        String email,
        String phone,
        Instant createdAt,
        Instant updatedAt) {
    public static ContactResponse from(Contact contact) {
        return new ContactResponse(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getPhone(),
                contact.getCreatedAt(),
                contact.getUpdatedAt());
    }
}
