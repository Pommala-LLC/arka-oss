package com.pommala.arka.retry.internal;

import com.pommala.arka.spi.RetryPolicy;
import com.pommala.arka.support.exception.ArkaApplicationException;
import java.time.Duration;

/**
 * Fixed-interval retry policy.
 *
 * <p>Classifies failures as retriable based on the application code's
 * {@link com.pommala.arka.support.code.ApplicationCode#retriable()} flag.
 * Uses a fixed delay between attempts with no backoff or jitter.
 *
 * <p>Default: 3 attempts, 1-second fixed delay.
 *
 * @see RetryPolicy
 */
public class FixedRetryPolicy implements RetryPolicy {

    private final int maxAttempts;
    private final Duration delay;

    public FixedRetryPolicy(int maxAttempts, Duration delay) {
        this.maxAttempts = maxAttempts;
        this.delay = delay;
    }

    @Override
    public boolean isRetriable(ArkaApplicationException ex) {
        // Retry classification: delegates to the ApplicationCode's retriable flag.
        // Auth, TLS, configuration, and business-rule failures are never retriable.
        return ex.internalCode().retriable();
    }

    @Override
    public int maxAttempts() { return maxAttempts; }

    @Override
    public Duration delay() { return delay; }
}
