package com.phonebook.security.ratelimit;

import com.phonebook.config.AppProperties;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory token-bucket rate limiter.
 *
 * <p>
 * Two independent buckets per client key:
 * </p>
 * <ul>
 * <li><b>global</b> - coarse protection for every API request</li>
 * <li><b>auth</b> - tighter budget for login/register to slow down
 * credential-stuffing and brute-force attempts</li>
 * </ul>
 *
 * <p>
 * For a single local instance this is sufficient. A clustered deployment
 * would swap the map for a shared store (Redis) without touching call sites.
 * </p>
 */
@Service
public class RateLimiterService {

    private final Map<String, Bucket> globalBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> authBuckets = new ConcurrentHashMap<>();
    private final AppProperties.RateLimit config;

    public RateLimiterService(AppProperties properties) {
        this.config = properties.getSecurity().getRateLimit();
    }

    /**
     * @return {@code null} when the request is allowed, otherwise the number of
     *         seconds the client should wait before retrying.
     */
    public Long tryConsumeGlobal(String clientKey) {
        return tryConsume(globalBuckets, clientKey, config.getGlobalCapacity(), config.getGlobalRefillMinutes());
    }

    /**
     * @return {@code null} when the request is allowed, otherwise the number of
     *         seconds the client should wait before retrying.
     */
    public Long tryConsumeAuth(String clientKey) {
        return tryConsume(authBuckets, clientKey, config.getAuthCapacity(), config.getAuthRefillMinutes());
    }

    private Long tryConsume(Map<String, Bucket> store, String key, long capacity, long refillMinutes) {
        Bucket bucket = store.computeIfAbsent(key, k -> Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(capacity)
                        .refillGreedy(capacity, Duration.ofMinutes(refillMinutes))
                        .build())
                .build());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            return null;
        }
        long waitSeconds = Math.max(1, Duration.ofNanos(probe.getNanosToWaitForRefill()).toSeconds());
        return waitSeconds;
    }

    /** Test/ops helper - drops all counters. */
    public void reset() {
        globalBuckets.clear();
        authBuckets.clear();
    }
}
