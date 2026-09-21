package com.phonebook.security.jwt;

import com.phonebook.domain.User;
import com.phonebook.repository.RevokedTokenRepository;
import com.phonebook.repository.UserRepository;
import com.phonebook.security.AppUserPrincipal;
import com.phonebook.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authenticates requests that carry a valid {@code Authorization: Bearer ...}
 * access token.
 *
 * <p>
 * Validation performed here:
 * </p>
 * <ol>
 * <li>signature, issuer, audience and expiry (JJWT)</li>
 * <li>token id is not on the revoked-token deny-list (logout)</li>
 * <li>the referenced user still exists and is enabled</li>
 * </ol>
 *
 * <p>
 * The filter never fails the request itself; it simply leaves the security
 * context empty so the authorization rules and the authentication entry point
 * produce the correct 401/403 response.
 * </p>
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;
    private final RevokedTokenRepository revokedTokenRepository;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
            RevokedTokenRepository revokedTokenRepository,
            UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.revokedTokenRepository = revokedTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                Claims claims = tokenProvider.parse(token);

                if (revokedTokenRepository.existsByJti(claims.getId())) {
                    log.debug("Rejected revoked token for subject={}", claims.getSubject());
                } else {
                    authenticate(request, claims);
                }
            } catch (JwtException | IllegalArgumentException ex) {
                // Invalid/expired token: stay anonymous, let AuthorizationFilter decide.
                log.debug("JWT validation failed: {}", ex.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private void authenticate(HttpServletRequest request, Claims claims) {
        Long userId = parseUserId(claims);
        if (userId == null) {
            return;
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null || !user.isEnabled()) {
            log.debug("Token references unknown or disabled user id={}", userId);
            return;
        }

        AppUserPrincipal principal = AppUserPrincipal.from(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null,
                principal.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Expose token metadata to downstream components (e.g. logout).
        request.setAttribute(SecurityConstants.ATTR_USER_ID, userId);
        request.setAttribute(SecurityConstants.ATTR_USER_EMAIL, user.getEmail());
        request.setAttribute(SecurityConstants.ATTR_JTI, claims.getId());
        if (claims.getExpiration() != null) {
            request.setAttribute(SecurityConstants.ATTR_TOKEN_EXPIRY, claims.getExpiration().toInstant());
        }
    }

    private Long parseUserId(Claims claims) {
        Object claimValue = claims.get(SecurityConstants.CLAIM_USER_ID);
        if (claimValue instanceof Number number) {
            return number.longValue();
        }
        String subject = claims.getSubject();
        if (StringUtils.hasText(subject)) {
            try {
                return Long.valueOf(subject);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstants.AUTH_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(SecurityConstants.BEARER_PREFIX)) {
            return header.substring(SecurityConstants.BEARER_PREFIX.length()).trim();
        }
        return null;
    }
}
