package com.phonebook.security.jwt;

import com.phonebook.config.AppProperties;
import com.phonebook.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.phonebook.security.SecurityConstants.CLAIM_NAME;
import static com.phonebook.security.SecurityConstants.CLAIM_PROVIDER;
import static com.phonebook.security.SecurityConstants.CLAIM_USER_ID;

/**
 * Creates and verifies HS256 access tokens.
 *
 * <p>
 * Secrets shorter than 32 bytes are rejected by JJWT; the configured value
 * is therefore used as-is when it is long enough, otherwise it is hashed with
 * SHA-256 so local development setups still work deterministically.
 * </p>
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final SecretKey signingKey;
    private final long accessTokenTtlSeconds;
    private final String issuer;
    private final String audience;

    public JwtTokenProvider(AppProperties properties) {
        AppProperties.Jwt jwt = properties.getSecurity().getJwt();
        this.signingKey = buildKey(jwt.getSecret());
        this.accessTokenTtlSeconds = jwt.getAccessTokenTtlMinutes() * 60L;
        this.issuer = jwt.getIssuer();
        this.audience = jwt.getAudience();
        log.info("JWT provider initialised (issuer={}, audience={}, ttl={}s)",
                issuer, audience, accessTokenTtlSeconds);
    }

    private static SecretKey buildKey(String secret) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("app.security.jwt.secret must be configured");
        }
        byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
        if (raw.length >= 32) {
            return Keys.hmacShaKeyFor(raw);
        }
        log.warn("JWT secret is shorter than 32 bytes; deriving a 256-bit key via SHA-256. "
                + "Configure a strong secret for real deployments.");
        try {
            // SHA-256 always yields exactly 32 bytes, which is a valid HS256 key.
            byte[] derived = MessageDigest.getInstance("SHA-256").digest(raw);
            return Keys.hmacShaKeyFor(derived);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /**
     * Issues a signed access token for the given user.
     */
    public IssuedToken issue(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(accessTokenTtlSeconds);
        String jti = UUID.randomUUID().toString();

        String token = Jwts.builder()
                .id(jti)
                .subject(String.valueOf(user.getId()))
                .issuer(issuer)
                .audience().add(audience).and()
                .issuedAt(Date.from(now))
                .notBefore(Date.from(now))
                .expiration(Date.from(expiresAt))
                .claim(CLAIM_USER_ID, user.getId())
                .claim(CLAIM_NAME, user.getName())
                .claim(CLAIM_PROVIDER, user.getProvider().name())
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();

        return new IssuedToken(token, jti, expiresAt, accessTokenTtlSeconds);
    }

    /**
     * Parses and verifies a token, returning its claims.
     *
     * <p>
     * Signature, issuer, <em>audience</em> and expiry are all enforced here, so
     * a token minted for a different audience (or a different issuer) is
     * rejected even when it is correctly signed with the shared secret.
     * </p>
     *
     * @throws JwtException when the signature, issuer, audience or expiry is
     *                      invalid
     */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(issuer)
                .requireAudience(audience)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getAccessTokenTtlSeconds() {
        return accessTokenTtlSeconds;
    }

    /**
     * Minted token plus the metadata needed for the logout deny-list.
     */
    public record IssuedToken(String token, String jti, Instant expiresAt, long expiresInSeconds) {
    }
}
