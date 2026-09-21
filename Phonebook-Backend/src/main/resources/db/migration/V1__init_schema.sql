-- =====================================================================
-- V1: initial schema for the Phonebook application
-- MySQL 8.x
-- =====================================================================

-- ---------------------------------------------------------------------
-- Users
-- ---------------------------------------------------------------------
CREATE TABLE users (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(255) NOT NULL,
    password_hash  VARCHAR(255) NULL,          -- NULL for OAuth2-only accounts
    provider       VARCHAR(20)  NOT NULL DEFAULT 'LOCAL', -- LOCAL | GOOGLE
    provider_id    VARCHAR(255) NULL,          -- subject/id from the OAuth2 provider
    email_verified BIT(1)       NOT NULL DEFAULT b'0',
    enabled        BIT(1)       NOT NULL DEFAULT b'1',
    created_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT chk_users_provider CHECK (provider IN ('LOCAL', 'GOOGLE')),
    CONSTRAINT chk_users_password_present
        CHECK (provider <> 'LOCAL' OR password_hash IS NOT NULL)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Email lookups happen on every login -> covered by uk_users_email.
CREATE INDEX idx_users_provider_provider_id ON users (provider, provider_id);
CREATE INDEX idx_users_created_at ON users (created_at);

-- ---------------------------------------------------------------------
-- Contacts
-- Every contact belongs to exactly one user (user_id FK, cascade delete)
-- ---------------------------------------------------------------------
CREATE TABLE contacts (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NULL,
    phone      VARCHAR(30)  NOT NULL,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    version    BIGINT       NOT NULL DEFAULT 0,   -- optimistic locking
    PRIMARY KEY (id),
    CONSTRAINT fk_contacts_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    -- A single user cannot store the same phone number twice.
    CONSTRAINT uk_contacts_user_phone UNIQUE (user_id, phone)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Primary access pattern: "all contacts of the authenticated user".
CREATE INDEX idx_contacts_user_id ON contacts (user_id);
-- Listing sorted by name for one user.
CREATE INDEX idx_contacts_user_name ON contacts (user_id, name);
-- Search by email / phone inside a user's phonebook.
CREATE INDEX idx_contacts_user_email ON contacts (user_id, email);
CREATE INDEX idx_contacts_created_at ON contacts (created_at);

-- ---------------------------------------------------------------------
-- Revoked JWT access tokens (logout / explicit revocation)
-- ---------------------------------------------------------------------
CREATE TABLE revoked_tokens (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    jti        VARCHAR(64)  NOT NULL,          -- JWT id claim
    user_id    BIGINT       NULL,
    expires_at DATETIME(6)  NOT NULL,
    revoked_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_revoked_tokens_jti UNIQUE (jti),
    CONSTRAINT fk_revoked_tokens_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_revoked_tokens_expires_at ON revoked_tokens (expires_at);
CREATE INDEX idx_revoked_tokens_user_id ON revoked_tokens (user_id);
