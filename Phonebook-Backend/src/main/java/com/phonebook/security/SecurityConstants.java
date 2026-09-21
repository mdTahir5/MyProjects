package com.phonebook.security;

/**
 * Central place for security-related string constants.
 */
public final class SecurityConstants {

    private SecurityConstants() {
    }

    /** {@code Authorization: Bearer <token>} */
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "Bearer";

    /** Claims added to every access token. */
    public static final String CLAIM_USER_ID = "uid";
    public static final String CLAIM_NAME = "name";
    public static final String CLAIM_PROVIDER = "provider";

    /** Request attribute holding the authenticated principal's user id. */
    public static final String ATTR_USER_ID = "phonebook.userId";
    public static final String ATTR_USER_EMAIL = "phonebook.userEmail";
    public static final String ATTR_JTI = "phonebook.jti";
    public static final String ATTR_TOKEN_EXPIRY = "phonebook.tokenExpiry";

    /** Paths that must stay publicly reachable. */
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/oauth2/**",
            "/api/public/**",
            "/actuator/health",
            "/actuator/health/**",
            "/actuator/info",
            "/error"
    };
}
