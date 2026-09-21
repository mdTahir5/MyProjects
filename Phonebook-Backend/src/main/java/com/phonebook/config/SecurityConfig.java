package com.phonebook.config;

import com.phonebook.security.RestAuthenticationEntryPoint;
import com.phonebook.security.jwt.JwtAuthenticationFilter;
import com.phonebook.security.ratelimit.RateLimitFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.phonebook.security.SecurityConstants.PUBLIC_ENDPOINTS;

/**
 * Application security configuration.
 *
 * <ul>
 *   <li>Stateless JWT authentication - no HTTP session is ever created.</li>
 *   <li>CSRF disabled because the API is token based (cookies are not used for auth).</li>
 *   <li>Strict CORS allow-list driven by {@code app.security.cors.allowed-origins}.</li>
 *   <li>Security headers: HSTS, frame options, content type options, referrer policy,
 *       a restrictive Content-Security-Policy and Permissions-Policy.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RateLimitFilter rateLimitFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final AppProperties appProperties;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          RateLimitFilter rateLimitFilter,
                          RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                          AppProperties appProperties) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.appProperties = appProperties;
    }

    /**
     * BCrypt with strength 12 - deliberately slow, automatically salted, and the
     * de-facto standard for password hashing in Spring Security.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        // Do not leak "user not found" vs "bad password".
        provider.setHideUserNotFoundExceptions(true);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Stateless token API: CSRF protection is neither needed nor usable.
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable())   // logout is a custom endpoint

                // ---- Authorization rules (deny by default) ----
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().denyAll()
                )

                // ---- Uniform JSON error responses ----
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAuthenticationEntryPoint)
                )

                // ---- Security headers ----
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .contentTypeOptions(Customizer.withDefaults())
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31_536_000)
                                .preload(true))
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'none'; frame-ancestors 'none'; base-uri 'none'; form-action 'none'"))
                        // NOTE: permissionsPolicy() returns a nested configurer, so it must be
                        // the LAST header in the chain.
                        .permissionsPolicy(permissions -> permissions.policy(
                                "geolocation=(), microphone=(), camera=(), payment=()"))
                )

                // Rate limiting runs before authentication so brute force is cheap to reject.
                .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Explicit CORS policy. Wildcards are never combined with credentials; only
     * the configured origins may call the API.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        List<String> allowedOrigins = appProperties.getSecurity().getCors().getAllowedOrigins();

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                "X-Requested-With"
        ));
        configuration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION, "Retry-After"));
        configuration.setAllowCredentials(false);   // tokens travel in the Authorization header
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
