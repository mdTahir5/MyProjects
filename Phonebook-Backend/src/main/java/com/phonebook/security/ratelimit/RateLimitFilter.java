package com.phonebook.security.ratelimit;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonebook.dto.common.ApiErrorResponse;
import com.phonebook.security.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Applies the coarse per-IP request budget and a tighter budget on the
 * authentication endpoints.
 *
 * <p>
 * Ordered to run before Spring Security's filter chain.
 * </p>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);

    /** Endpoints with the strict budget. */
    private static final Set<String> SENSITIVE_PATHS = Set.of(
            "/api/auth/login",
            "/api/auth/register",
            "/api/auth/oauth2/google");

    private final RateLimiterService rateLimiterService;
    private final ObjectMapper objectMapper;

    RateLimitFilter(RateLimiterService rateLimiterService, ObjectMapper objectMapper) {
        this.rateLimiterService = rateLimiterService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Health checks must never be throttled.
        return path.startsWith("/actuator/health");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String clientKey = clientKey(request);

        Long globalWait = rateLimiterService.tryConsumeGlobal(clientKey);
        if (globalWait != null) {
            reject(request, response, clientKey, globalWait,
                    "Too many requests. Please slow down and try again shortly.");
            return;
        }

        if (SENSITIVE_PATHS.contains(request.getRequestURI())) {
            Long authWait = rateLimiterService.tryConsumeAuth(clientKey);
            if (authWait != null) {
                log.warn("Rate limit exceeded for {} on {}", clientKey, request.getRequestURI());
                reject(request, response, clientKey, authWait,
                        "Too many authentication attempts. Please try again later.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Writes the {@code 429 Too Many Requests} response.
     *
     * <p>
     * The body is written here instead of throwing
     * {@link com.phonebook.exception.RateLimitExceededException}: a servlet
     * filter runs <em>before</em> {@code DispatcherServlet}, so an exception
     * thrown here never reaches {@code @RestControllerAdvice} and would surface
     * as a generic {@code 500} from the container - losing both the status and
     * the {@code Retry-After} header the SPA relies on. The payload matches
     * {@link ApiErrorResponse}, exactly like the 401/403 bodies emitted by
     * {@code RestAuthenticationEntryPoint}.
     * </p>
     */
    private void reject(HttpServletRequest request,
            HttpServletResponse response,
            String clientKey,
            long retryAfterSeconds,
            String message) throws IOException {

        if (response.isCommitted()) {
            return;
        }

        String traceId = UUID.randomUUID().toString().substring(0, 8);
        log.warn("[{}] Rate limit exceeded for {} on {} {} (retry after {}s)",
                traceId, clientKey, request.getMethod(), request.getRequestURI(), retryAfterSeconds);

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfterSeconds));

        objectMapper.writeValue(response.getOutputStream(), ApiErrorResponse.of(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "RATE_LIMIT_EXCEED",
                message,
                request.getRequestURI(),
                traceId));
    }

    /**
     * Prefers the authenticated user id (multi-user NAT friendly) and falls back
     * to the transport source address.
     */
    private String clientKey(HttpServletRequest request) {
        Object userId = request.getAttribute(SecurityConstants.ATTR_USER_ID);
        if (userId != null) {
            return "user:" + userId;
        }
        return "ip:" + resolveClientIp(request);
    }

    private String resolveClientIp(HttpServletRequest request) {
        // Behind a reverse proxy, configure the container's remote-ip valve /
        // forwarded-header strategy instead of trusting these blindly.
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardedFor)) {
            int comma = forwardedFor.indexOf(',');
            return comma > 0 ? forwardedFor.substring(0, comma).trim() : forwardedFor.trim();
        }
        return request.getRemoteAddr();
    }
}
