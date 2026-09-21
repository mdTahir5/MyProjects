package com.phonebook.exception;

import org.springframework.http.HttpStatus;

/**
 * Base class for all application (business) exceptions.
 *
 * <p>
 * Carries the HTTP status the global handler should map it to, so the
 * service layer can express intent without depending on web types.
 * </p>
 */
public abstract class AppException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;
    private final String errorCode;

    protected AppException(HttpStatus status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    protected AppException(HttpStatus status, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
