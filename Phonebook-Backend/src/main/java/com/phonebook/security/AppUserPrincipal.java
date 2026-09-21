package com.phonebook.security;

import com.phonebook.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security principal backed by the application's {@link User} entity.
 *
 * <p>
 * Exposes the immutable user id so services can scope every query to the
 * authenticated owner without ever trusting a client supplied id.
 * </p>
 */
public class AppUserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String name;
    private final String email;
    private final String passwordHash;
    private final boolean enabled;
    /**
     * Spring Security authorities. Transient because {@code GrantedAuthority}
     * does not promise serializability and the principal is never serialized.
     */
    private final transient List<GrantedAuthority> authorities;

    public AppUserPrincipal(Long id,
            String name,
            String email,
            String passwordHash,
            boolean enabled,
            List<GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.enabled = enabled;
        this.authorities = authorities == null ? List.of() : List.copyOf(authorities);
    }

    public static AppUserPrincipal from(User user) {
        return new AppUserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.isEnabled(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
