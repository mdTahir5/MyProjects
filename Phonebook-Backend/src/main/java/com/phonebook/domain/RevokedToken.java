package com.phonebook.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.Objects;

/**
 * Access tokens that were explicitly invalidated through {@code POST /api/auth/logout}.
 *
 * <p>JWTs are stateless, so revocation is implemented with a small deny-list
 * keyed by the token's {@code jti} claim. Rows are removed automatically once
 * the token would have expired anyway.</p>
 */
@Entity
@Table(
        name = "revoked_tokens",
        uniqueConstraints = @UniqueConstraint(name = "uk_revoked_tokens_jti", columnNames = "jti"),
        indexes = {
                @Index(name = "idx_revoked_tokens_expires_at", columnList = "expires_at"),
                @Index(name = "idx_revoked_tokens_user_id", columnList = "user_id")
        }
)
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jti", nullable = false, length = 64)
    private String jti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @jakarta.persistence.ForeignKey(name = "fk_revoked_tokens_user"))
    private User user;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at", nullable = false)
    private Instant revokedAt = Instant.now();

    protected RevokedToken() {
        // required by JPA
    }

    public RevokedToken(String jti, User user, Instant expiresAt) {
        this.jti = jti;
        this.user = user;
        this.expiresAt = expiresAt;
        this.revokedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getJti() {
        return jti;
    }

    public User getUser() {
        return user;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RevokedToken other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RevokedToken{jti='" + jti + "', expiresAt=" + expiresAt + '}';
    }
}
