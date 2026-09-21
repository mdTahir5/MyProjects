package com.phonebook.exception;

import org.springframework.http.HttpStatus;

/**
 * Authentication failed (bad credentials, disabled account, invalid token...).
 */
public class AuthenticationFailedException extends AppException {

    private static final long serialVersionUID = 1L;

    public AuthenticationFailedException(String message) {
        super(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", message);
    }

    public AuthenticationFailedException(String message, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", message, cause);
    }
}
