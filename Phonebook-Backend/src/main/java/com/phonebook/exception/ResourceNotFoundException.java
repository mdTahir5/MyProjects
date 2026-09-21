package com.phonebook.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a resource does not exist, or when it exists but does not belong
 * to the authenticated user.
 *
 * <p>
 * Both cases intentionally produce the same 404 so an attacker cannot probe
 * for the existence of other users' records (IDOR protection).
 * </p>
 */
public class ResourceNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }

    public static ResourceNotFoundException contact(Long id) {
        return new ResourceNotFoundException("Contact not found with id " + id);
    }
}
