package com.phonebook.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Uniform error body returned by the global exception handler.
 *
 * <p>
 * Never leaks stack traces or internal exception messages for 5xx errors.
 * </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        // Field-level validation problems: field -> message.
        Map<String, String> fieldErrors,
        // Optional correlation id, useful when correlating with the server log.
        String traceId,
        List<String> details) {
    public static ApiErrorResponse of(int status,
            String error,
            String message,
            String path,
            String traceId) {
        return new ApiErrorResponse(Instant.now(), status, error, message, path, null, traceId, null);
    }

    public static ApiErrorResponse withFieldErrors(int status,
            String error,
            String message,
            String path,
            Map<String, String> fieldErrors,
            String traceId) {
        return new ApiErrorResponse(Instant.now(), status, error, message, path, fieldErrors, traceId, null);
    }
}
