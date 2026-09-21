package com.phonebook.security;

import com.phonebook.exception.AuthenticationFailedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Convenience accessor for the authenticated principal.
 *
 * <p>
 * Controllers use this to obtain the owner id; the value is derived from the
 * verified JWT and can therefore be trusted, unlike a client supplied id.
 * </p>
 */
@Component
public class CurrentUserProvider {

    public AppUserPrincipal requirePrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof AppUserPrincipal principal)) {
            throw new AuthenticationFailedException("Authentication is required to access this resource");
        }
        return principal;
    }

    public Long requireUserId() {
        return requirePrincipal().getId();
    }
}
