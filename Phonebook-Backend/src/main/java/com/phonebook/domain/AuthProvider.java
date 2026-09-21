package com.phonebook.domain;

/**
 * How a user account was created.
 */
public enum AuthProvider {
    /** Email + password registered through the application. */
    LOCAL,
    /** Account created/verified through an OAuth2 provider (Google). */
    GOOGLE
}
