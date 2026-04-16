package com.pommala.arka.spi;

import com.pommala.arka.support.exception.ArkaApplicationException;
import java.time.Duration;

/**
 * SPI for retry classification and policy configuration.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 * Default backs off via {@code @ConditionalOnMissingBean}.
 *
 * <h3>Retriability rules (locked):</h3>
 * <ul>
 *   <li>Timeout, connection refused, stale connection → retriable.</li>
 *   <li>Auth failure, TLS failure, config error → never retriable.</li>
 *   <li>Business-rule violation, validation failure → never retriable.</li>
 *   <li>Generic transport failure → configurable, default false.</li>
 * </ul>
 */
public interface RetryPolicy {

    /**
     * Returns whether the given exception is eligible for retry.
     *
     * @param ex the exception that caused the failure
     * @return true if the operation should be retried
     */
    boolean isRetriable(ArkaApplicationException ex);

    /** Maximum number of attempts (including the first). */
    int maxAttempts();

    /** Fixed delay between attempts. */
    Duration delay();
}
