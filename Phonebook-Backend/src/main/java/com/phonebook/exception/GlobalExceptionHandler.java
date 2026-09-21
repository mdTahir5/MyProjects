package com.phonebook.exception;

import com.phonebook.dto.common.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Single place where exceptions become HTTP responses.
 *
 * <p>
 * Rules:
 * </p>
 * <ul>
 * <li>Client errors (4xx) get an actionable message.</li>
 * <li>Server errors (5xx) get a generic message; the details go to the log
 * together with a short trace id that is also returned to the client.</li>
 * <li>Nothing internal (stack traces, SQL, class names) is ever leaked.</li>
 * </ul>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        // ------------------------------------------------------------------
        // Application exceptions
        // ------------------------------------------------------------------

        @ExceptionHandler(AppException.class)
        public ResponseEntity<ApiErrorResponse> handleAppException(AppException ex, HttpServletRequest request) {
                String traceId = newTraceId();
                log.warn("[{}] {} -> {} ({})", traceId, request.getMethod(), request.getRequestURI(),
                                ex.getStatus().value(), ex.getMessage());

                Map<String, String> fieldErrors = null;
                HttpHeaders headers = new HttpHeaders();

                if (ex instanceof ValidationException validationException) {
                        fieldErrors = validationException.getFieldErrors();
                }
                if (ex instanceof DuplicateResourceException duplicate) {
                        fieldErrors = Map.of(duplicate.getField(), duplicate.getMessage());
                }
                if (ex instanceof RateLimitExceededException rateLimited) {
                        headers.set(HttpHeaders.RETRY_AFTER, String.valueOf(rateLimited.getRetryAfterSeconds()));
                }

                ApiErrorResponse body = fieldErrors == null
                                ? ApiErrorResponse.of(ex.getStatus().value(), ex.getErrorCode(), ex.getMessage(),
                                                request.getRequestURI(), traceId)
                                : ApiErrorResponse.withFieldErrors(ex.getStatus().value(), ex.getErrorCode(),
                                                ex.getMessage(), request.getRequestURI(), fieldErrors, traceId);

                return new ResponseEntity<>(body, headers, ex.getStatus());
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                return handleAppException(ex, request);
        }

        // ------------------------------------------------------------------
        // Validation
        // ------------------------------------------------------------------

        /** {@code @Valid} failures on request bodies. */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleBodyValidation(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                Map<String, String> fieldErrors = new LinkedHashMap<>();
                for (FieldError error : ex.getBindingResult().getFieldErrors()) {
                        // First message wins - keeps the response focused for the UI.
                        fieldErrors.putIfAbsent(error.getField(), error.getDefaultMessage());
                }
                String traceId = newTraceId();
                log.debug("[{}] Validation failed for {}: {}", traceId, request.getMethod(),
                                request.getRequestURI(), fieldErrors);

                ApiErrorResponse body = ApiErrorResponse.withFieldErrors(
                                HttpStatus.BAD_REQUEST.value(), "VALIDATION_ERROR",
                                "The request contains invalid fields.", request.getRequestURI(), fieldErrors, traceId);
                return ResponseEntity.badRequest().body(body);
        }

        /** {@code @Validated} failures on path variables / query params. */
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex,
                        HttpServletRequest request) {
                Map<String, String> fieldErrors = new LinkedHashMap<>();
                for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                        String path = violation.getPropertyPath().toString();
                        String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
                        fieldErrors.putIfAbsent(field, violation.getMessage());
                }
                String traceId = newTraceId();
                ApiErrorResponse body = ApiErrorResponse.withFieldErrors(
                                HttpStatus.BAD_REQUEST.value(), "VALIDATION_ERROR",
                                "The request contains invalid parameters.", request.getRequestURI(), fieldErrors,
                                traceId);
                return ResponseEntity.badRequest().body(body);
        }

        @ExceptionHandler({ HttpMessageNotReadableException.class,
                        MissingServletRequestParameterException.class,
                        MethodArgumentTypeMismatchException.class })
        public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
                String traceId = newTraceId();
                log.debug("[{}] Malformed request to {}: {}", traceId, request.getMethod(),
                                request.getRequestURI(), ex.getMessage());
                return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST",
                                "The request could not be understood. Check the request format and parameters.",
                                request, traceId);
        }

        // ------------------------------------------------------------------
        // Database
        // ------------------------------------------------------------------

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                        HttpServletRequest request) {
                String traceId = newTraceId();
                String root = rootMessage(ex);
                log.warn("[{}] Data integrity violation on {}: {}", traceId, request.getMethod(),
                                request.getRequestURI(), root);

                // Translate the most common constraint failures into actionable messages.
                if (root.contains("uk_users_email")) {
                        return build(HttpStatus.CONFLICT, "DUPLICATE_EMAIL",
                                        "An account with this email already exists.", request, traceId);
                }
                if (root.contains("uk_contacts_user_phone")) {
                        return build(HttpStatus.CONFLICT, "DUPLICATE_PHONE",
                                        "You already have a contact with this phone number.", request, traceId);
                }
                return build(HttpStatus.CONFLICT, "CONFLICT",
                                "The request conflicts with existing data.", request, traceId);
        }

        @ExceptionHandler(OptimisticLockingFailureException.class)
        public ResponseEntity<ApiErrorResponse> handleOptimisticLock(OptimisticLockingFailureException ex,
                        HttpServletRequest request) {
                String traceId = newTraceId();
                log.warn("[{}] Optimistic locking conflict on {}: {}", traceId, request.getMethod(),
                                request.getRequestURI(), ex.getMessage());
                return build(HttpStatus.CONFLICT, "STALE_RESOURCE",
                                "This record was modified by another request. Reload it and try again.", request,
                                traceId);
        }

        // ------------------------------------------------------------------
        // Security (defensive - Spring Security normally handles these first)
        // ------------------------------------------------------------------

        @ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class })
        public ResponseEntity<ApiErrorResponse> handleAuthentication(AuthenticationException ex,
                        HttpServletRequest request) {
                String traceId = newTraceId();
                log.warn("[{}] Authentication failure on {}: {}", traceId, request.getMethod(),
                                request.getRequestURI(), ex.getClass().getSimpleName());
                return build(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED",
                                "Invalid email or password.", request, traceId);
        }

        @ExceptionHandler({ DisabledException.class, LockedException.class })
        public ResponseEntity<ApiErrorResponse> handleDisabled(AuthenticationException ex,
                        HttpServletRequest request) {
                String traceId = newTraceId();
                return build(HttpStatus.FORBIDDEN, "ACCOUNT_DISABLED",
                                "This account is disabled. Please contact support.", request, traceId);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex,
                        HttpServletRequest request) {
                String traceId = newTraceId();
                log.warn("[{}] Access denied on {} {}", traceId, request.getMethod(), request.getRequestURI());
                return build(HttpStatus.FORBIDDEN, "FORBIDDEN",
                                "You do not have permission to perform this action.", request, traceId);
        }

        // ------------------------------------------------------------------
        // Routing / protocol
        // ------------------------------------------------------------------

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                        HttpServletRequest request) {
                return build(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED",
                                "HTTP method " + ex.getMethod() + " is not supported for this endpoint.", request,
                                newTraceId());
        }

        @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
        public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                        HttpServletRequest request) {
                return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE",
                                "Content type is not supported. Use application/json.", request, newTraceId());
        }

        /**
         * Unknown paths.
         *
         * <p>
         * Spring 6.1 raises {@link NoResourceFoundException} when the static
         * resource handler is the last handler left (the default setup).
         * {@code NoHandlerFoundException} only appears when
         * {@code throw-exception-if-no-handler-found} is enabled, so both are
         * mapped here to guarantee a 404 rather than a 500.
         * </p>
         */
        @ExceptionHandler({ NoHandlerFoundException.class, NoResourceFoundException.class })
        public ResponseEntity<ApiErrorResponse> handleNoHandler(Exception ex,
                        HttpServletRequest request) {
                return build(HttpStatus.NOT_FOUND, "NOT_FOUND", "Endpoint not found.", request, newTraceId());
        }

        // ------------------------------------------------------------------
        // Fallback
        // ------------------------------------------------------------------

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
                String traceId = newTraceId();
                // Full detail goes to the log only.
                log.error("[{}] Unhandled exception on {} {}", traceId, request.getMethod(),
                                request.getRequestURI(), ex);
                return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                                "Something went wrong on our side. Please try again later. Reference: " + traceId,
                                request, traceId);
        }

        // ------------------------------------------------------------------
        // Helpers
        // ------------------------------------------------------------------

        private ResponseEntity<ApiErrorResponse> build(HttpStatus status,
                        String error,
                        String message,
                        HttpServletRequest request,
                        String traceId) {
                return ResponseEntity.status(status)
                                .body(ApiErrorResponse.of(status.value(), error, message, request.getRequestURI(),
                                                traceId));
        }

        private static String newTraceId() {
                return UUID.randomUUID().toString().substring(0, 8);
        }

        private static String rootMessage(Throwable throwable) {
                Throwable current = throwable;
                while (current.getCause() != null && current.getCause() != current) {
                        current = current.getCause();
                }
                return current.getMessage() == null ? "" : current.getMessage();
        }
}
