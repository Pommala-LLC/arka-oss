package com.pommala.arka.retry.autoconfigure;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for fixed retry policy.
 *
 * <pre>{@code
 * arka:
 *   retry:
 *     max-attempts: 3
 *     delay: 1s
 * }</pre>
 */
@ConfigurationProperties(prefix = "arka.retry")
public class ArkaRetryProperties {

    /** Maximum send attempts including the first. Default 3. */
    private int maxAttempts = 3;

    /** Fixed delay between attempts. Default 1s. */
    private Duration delay = Duration.ofSeconds(1);

    public int getMaxAttempts()  { return maxAttempts; }
    public Duration getDelay()   { return delay; }
    public void setMaxAttempts(int v)   { this.maxAttempts = v; }
    public void setDelay(Duration v)    { this.delay = v; }
}
