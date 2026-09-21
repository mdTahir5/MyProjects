package com.phonebook.security.ratelimit;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.phonebook.config.AppProperties;
import com.phonebook.dto.common.ApiErrorResponse;

import jakarta.servlet.FilterChain;

/**
 * Rate limiting behaviour.
 *
 * <p>
 * The filter must answer with {@code 429} + {@code Retry-After} and the
 * project's uniform error body. It has to write that response itself: a servlet
 * filter runs before {@code DispatcherServlet}, so an exception thrown here
 * would bypass {@code @RestControllerAdvice} and reach the client as an opaque
 * {@code 500}.
 * </p>
 */
class RateLimitFilterTest {

    private static final String PROTECTED_PATH = "/api/contacts";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String HEALTH_PATH = "/actuator/health";

    /** Global budget 5, auth budget 2 - see {@link #setUp()}. */
    private static final int GLOBAL_CAPACITY = 5;
    private static final int AUTH_CAPACITY = 2;

    private ObjectMapper objectMapper;
    private RateLimitFilter filter;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        AppProperties properties = new AppProperties();
        AppProperties.RateLimit rateLimit = properties.getSecurity().getRateLimit();
        rateLimit.setGlobalCapacity(GLOBAL_CAPACITY);
        rateLimit.setGlobalRefillMinutes(1);
        rateLimit.setAuthCapacity(AUTH_CAPACITY);
        rateLimit.setAuthRefillMinutes(1);

        filter = new RateLimitFilter(new RateLimiterService(properties), objectMapper);
    }

    @Test
    @DisplayName("requests inside the budget reach the downstream chain")
    void allowsRequestsInsideTheBudget() throws Exception {
        Outcome outcome = hit(PROTECTED_PATH, GLOBAL_CAPACITY);

        assertThat(outcome.chainInvoked()).isTrue();
        assertThat(outcome.response().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(outcome.response().getHeader(HttpHeaders.RETRY_AFTER)).isNull();
    }

    @Test
    @DisplayName("exhausting the global budget answers 429 with the uniform error body")
    void rejectsWhenGlobalBudgetIsExhausted() throws Exception {
        Outcome outcome = hit(PROTECTED_PATH, GLOBAL_CAPACITY + 1);

        assertThat(outcome.chainInvoked())
                .as("the rejected request must not be forwarded to the application")
                .isFalse();
        assertThat(outcome.response().getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
        assertThat(outcome.response().getContentType()).startsWith(MediaType.APPLICATION_JSON_VALUE);
        assertThat(outcome.response().getHeader(HttpHeaders.RETRY_AFTER))
                .isNotNull()
                .matches("\\d+");

        ApiErrorResponse body = body(outcome);
        assertThat(body.status()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
        assertThat(body.error()).isEqualTo("RATE_LIMIT_EXCEED");
        assertThat(body.message()).contains("Too many requests");
        assertThat(body.path()).isEqualTo(PROTECTED_PATH);
        assertThat(body.traceId()).isNotBlank();
    }

    @Test
    @DisplayName("the tighter auth budget rejects credential-stuffing with 429 and Retry-After")
    void rejectsWhenAuthBudgetIsExhausted() throws Exception {
        Outcome outcome = hit(LOGIN_PATH, AUTH_CAPACITY + 1);

        assertThat(outcome.chainInvoked()).isFalse();
        assertThat(outcome.response().getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
        assertThat(Integer.parseInt(outcome.response().getHeader(HttpHeaders.RETRY_AFTER))).isPositive();

        ApiErrorResponse body = body(outcome);
        assertThat(body.error()).isEqualTo("RATE_LIMIT_EXCEED");
        assertThat(body.message()).contains("Too many authentication attempts");
        assertThat(body.path()).isEqualTo(LOGIN_PATH);
    }

    @Test
    @DisplayName("health checks are never throttled")
    void doesNotThrottleHealthChecks() throws Exception {
        Outcome outcome = hit(HEALTH_PATH, GLOBAL_CAPACITY * 4);

        assertThat(outcome.chainInvoked()).isTrue();
        assertThat(outcome.response().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private record Outcome(MockHttpServletResponse response, boolean chainInvoked) {
    }

    /**
     * Sends {@code calls} identical requests through the filter and returns the
     * outcome of the last one.
     */
    private Outcome hit(String path, int calls) throws Exception {
        Outcome outcome = null;
        for (int i = 0; i < calls; i++) {
            MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
            MockHttpServletResponse response = new MockHttpServletResponse();
            AtomicBoolean chainInvoked = new AtomicBoolean(false);
            FilterChain chain = (req, res) -> chainInvoked.set(true);

            filter.doFilter(request, response, chain);
            outcome = new Outcome(response, chainInvoked.get());
        }
        return outcome;
    }

    private ApiErrorResponse body(Outcome outcome) throws Exception {
        return objectMapper.readValue(outcome.response().getContentAsString(), ApiErrorResponse.class);
    }
}