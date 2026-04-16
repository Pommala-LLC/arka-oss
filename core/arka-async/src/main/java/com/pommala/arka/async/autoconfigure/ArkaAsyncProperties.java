package com.pommala.arka.async.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

/**
 * Configuration properties for the shared async execution infrastructure.
 *
 * <h3>Configuration:</h3>
 * <pre>{@code
 * arka:
 *   async:
 *     max-concurrent-sends: 20
 *     batch-timeout: 60s
 *     recipient-timeout: 30s
 * }</pre>
 */
@ConfigurationProperties(prefix = "arka.async")
public class ArkaAsyncProperties {

    /** Maximum concurrent sends in a batch. Default 20. */
    private int maxConcurrentSends = 20;

    /** Maximum total time for a batch operation. Default 60s. */
    private Duration batchTimeout = Duration.ofSeconds(60);

    /** Maximum time per recipient in a batch. Default 30s. */
    private Duration recipientTimeout = Duration.ofSeconds(30);

    public int getMaxConcurrentSends()      { return maxConcurrentSends; }
    public Duration getBatchTimeout()       { return batchTimeout; }
    public Duration getRecipientTimeout()   { return recipientTimeout; }

    public void setMaxConcurrentSends(int v)       { this.maxConcurrentSends = v; }
    public void setBatchTimeout(Duration v)        { this.batchTimeout = v; }
    public void setRecipientTimeout(Duration v)    { this.recipientTimeout = v; }
}
