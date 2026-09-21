package com.phonebook.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonebook.dto.common.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Renders JSON (instead of an HTML/empty body) for 401 and 403 responses so the
 * SPA can handle them uniformly.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint, AccessDeniedHandler {

    private static final Logger log = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /** 401 - missing, malformed or expired credentials. */
    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        log.debug("Unauthenticated access to {} {}", request.getMethod(), request.getRequestURI());
        write(request, response, HttpStatus.UNAUTHORIZED, "Unauthorized",
                "Authentication is required to access this resource.");
    }

    /** 403 - authenticated but not allowed. */
    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        log.warn("Forbidden access to {} by principal={}", request.getMethod(), request.getRequestURI(),
                request.getUserPrincipal() == null ? "anonymous" : request.getUserPrincipal().getName());
        write(request, response, HttpStatus.FORBIDDEN, "Forbidden",
                "You do not have permission to perform this action.");
    }

    private void write(HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String error,
            String message) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        ApiErrorResponse body = ApiErrorResponse.of(status.value(), error, message,
                request.getRequestURI(), traceId);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
