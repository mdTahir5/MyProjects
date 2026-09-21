package com.phonebook.exception;

import org.springframework.http.HttpStatus;

/**
 * Conflict with the current state of the resource (duplicate email,
 * duplicate phone number for the same user, ...).
 */
public class DuplicateResourceException extends AppException {

    private static final long serialVersionUID = 1L;

    private final String field;

    public DuplicateResourceException(String message, String field) {
        super(HttpStatus.CONFLICT, "CONFLICT", message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
