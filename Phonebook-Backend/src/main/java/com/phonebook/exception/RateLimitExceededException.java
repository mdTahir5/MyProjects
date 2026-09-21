package com.phonebook.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a client exceeds its request budget.
 */
public class RateLimitExceededException extends AppException {

    private static final long serialVersionUID = 1L;

    private final long retryAfterSeconds;

    public RateLimitExceededException(String message, long retryAfterSeconds) {
        super(HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMIT_EXCEED", message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
