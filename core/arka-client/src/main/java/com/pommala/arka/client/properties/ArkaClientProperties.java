package com.pommala.arka.client.properties;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Arka typed client.
 *
 * <p>Prefix: {@code arka.client}
 */
@ConfigurationProperties(prefix = "arka.client")
public class ArkaClientProperties {

    /** Base URL of the Arka platform REST API. Required. */
    private String baseUrl;

    /** Connect timeout. Default: 5 seconds. */
    private Duration connectTimeout = Duration.ofSeconds(5);

    /** Read timeout. Default: 30 seconds. */
    private Duration readTimeout = Duration.ofSeconds(30);

    /** API key for authentication (if arka-security is enabled on the server). */
    private String apiKey;

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    public Duration getConnectTimeout() { return connectTimeout; }
    public void setConnectTimeout(Duration connectTimeout) { this.connectTimeout = connectTimeout; }
    public Duration getReadTimeout() { return readTimeout; }
    public void setReadTimeout(Duration readTimeout) { this.readTimeout = readTimeout; }
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
}
