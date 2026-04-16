package com.pommala.arka.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Arka security.
 *
 * <pre>{@code
 * arka:
 *   security:
 *     api-key:
 *       enabled: true
 *       header-name: X-Arka-Api-Key
 *       keys:
 *         - "key1"
 *         - "key2"
 * }</pre>
 */
@ConfigurationProperties(prefix = "arka.security")
public class ArkaSecurityProperties {

    private ApiKey apiKey = new ApiKey();

    public ApiKey getApiKey() { return apiKey; }
    public void setApiKey(ApiKey apiKey) { this.apiKey = apiKey; }

    public static class ApiKey {
        private boolean enabled = false;
        private String headerName = "X-Arka-Api-Key";
        private java.util.List<String> keys = java.util.List.of();

        public boolean isEnabled()              { return enabled; }
        public String getHeaderName()           { return headerName; }
        public java.util.List<String> getKeys() { return keys; }

        public void setEnabled(boolean v)                  { this.enabled = v; }
        public void setHeaderName(String v)                { this.headerName = v; }
        public void setKeys(java.util.List<String> v)      { this.keys = v; }
    }
}
