package com.phonebook.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Strongly typed binding for the {@code app.*} configuration tree.
 */
@ConfigurationProperties(prefix = "app")
@Validated
public class AppProperties {

    private Security security = new Security();
    private Admin admin = new Admin();

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    // ------------------------------------------------------------------

    public static class Security {

        private Jwt jwt = new Jwt();
        private Cors cors = new Cors();
        private RateLimit rateLimit = new RateLimit();
        private OAuth2 oauth2 = new OAuth2();

        public Jwt getJwt() {
            return jwt;
        }

        public void setJwt(Jwt jwt) {
            this.jwt = jwt;
        }

        public Cors getCors() {
            return cors;
        }

        public void setCors(Cors cors) {
            this.cors = cors;
        }

        public RateLimit getRateLimit() {
            return rateLimit;
        }

        public void setRateLimit(RateLimit rateLimit) {
            this.rateLimit = rateLimit;
        }

        public OAuth2 getOauth2() {
            return oauth2;
        }

        public void setOauth2(OAuth2 oauth2) {
            this.oauth2 = oauth2;
        }
    }

    public static class Jwt {

        /** HMAC secret. Must be at least 32 bytes for HS256. */
        @NotBlank
        private String secret;

        @Min(1)
        private long accessTokenTtlMinutes = 30;

        @NotBlank
        private String issuer = "phonebook-backend";

        @NotBlank
        private String audience = "phonebook-app";

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getAccessTokenTtlMinutes() {
            return accessTokenTtlMinutes;
        }

        public void setAccessTokenTtlMinutes(long accessTokenTtlMinutes) {
            this.accessTokenTtlMinutes = accessTokenTtlMinutes;
        }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }
    }

    public static class Cors {

        /** Comma separated in YAML/properties, bound here as a list. */
        private List<String> allowedOrigins = List.of("http://localhost:5173");

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }
    }

    public static class RateLimit {

        @Min(1)
        private long globalCapacity = 300;

        @Min(1)
        private long globalRefillMinutes = 1;

        @Min(1)
        private long authCapacity = 10;

        @Min(1)
        private long authRefillMinutes = 1;

        public long getGlobalCapacity() {
            return globalCapacity;
        }

        public void setGlobalCapacity(long globalCapacity) {
            this.globalCapacity = globalCapacity;
        }

        public long getGlobalRefillMinutes() {
            return globalRefillMinutes;
        }

        public void setGlobalRefillMinutes(long globalRefillMinutes) {
            this.globalRefillMinutes = globalRefillMinutes;
        }

        public long getAuthCapacity() {
            return authCapacity;
        }

        public void setAuthCapacity(long authCapacity) {
            this.authCapacity = authCapacity;
        }

        public long getAuthRefillMinutes() {
            return authRefillMinutes;
        }

        public void setAuthRefillMinutes(long authRefillMinutes) {
            this.authRefillMinutes = authRefillMinutes;
        }
    }

    public static class OAuth2 {

        private boolean enabled = false;
        private Google google = new Google();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Google getGoogle() {
            return google;
        }

        public void setGoogle(Google google) {
            this.google = google;
        }
    }

    public static class Google {

        private String clientId = "";
        private String clientSecret = "";

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }
    }

    public static class Admin {

        /**
         * Spring cron syntax: second minute hour day-of-month month day-of-week.
         * The default runs daily at 03:00.
         */
        private String tokenCleanupCron = "0 0 3 * * *";

        public String getTokenCleanupCron() {
            return tokenCleanupCron;
        }

        public void setTokenCleanupCron(String tokenCleanupCron) {
            this.tokenCleanupCron = tokenCleanupCron;
        }
    }
}
