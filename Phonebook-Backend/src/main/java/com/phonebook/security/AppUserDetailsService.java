package com.phonebook.security;

import com.phonebook.domain.User;
import com.phonebook.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Loads users for the authentication manager, normalising the lookup key so
 * that email casing never affects login.
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username == null ? "" : username.trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                // Generic message: never reveal whether an email is registered.
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
        return AppUserPrincipal.from(user);
    }
}
