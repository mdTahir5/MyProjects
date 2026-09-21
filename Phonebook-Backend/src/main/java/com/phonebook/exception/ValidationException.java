package com.phonebook.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * Request body failed business validation that cannot be expressed with
 * bean-validation annotations (for example password confirmation mismatch).
 */
public class ValidationException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Field-level problems. Transient because the map is only ever inspected
     * in-process; the exception is never serialized across a network boundary.
     */
    private final transient Map<String, String> fieldErrors;

    public ValidationException(Map<String, String> fieldErrors) {
        this("Validation failed", fieldErrors);
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
        this.fieldErrors = Map.copyOf(fieldErrors);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
