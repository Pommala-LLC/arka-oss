package com.pommala.arka.security.filter;

import com.pommala.arka.security.properties.ArkaSecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * API key authentication filter for direct Arka REST API access.
 *
 * <p>Validates the {@code X-Arka-Api-Key} header (configurable) against
 * the configured set of allowed keys. Rejects with 401 when absent or invalid.
 *
 * <p>CORS preflight {@code OPTIONS} requests are always passed through so the
 * browser can complete the preflight handshake before sending the real request.
 *
 * <p>Security scope: direct API auth when Arka is called without Conduvia.
 * RBAC/PBAC/ABAC belongs to Conduvia — not this filter.
 */
public class ArkaApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String LOG_PREFIX = "[arka-security]";
    private static final Logger log = LoggerFactory.getLogger(ArkaApiKeyAuthFilter.class);

    private final String headerName;
    private final Set<String> allowedKeys;

    public ArkaApiKeyAuthFilter(ArkaSecurityProperties props) {
        this.headerName  = props.getApiKey().getHeaderName();
        this.allowedKeys = Set.copyOf(props.getApiKey().getKeys());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // CORS preflight rule: OPTIONS requests must pass through so the browser
        // can complete the preflight handshake before the real authenticated request.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        var key = request.getHeader(headerName);

        if (key == null || !allowedKeys.contains(key)) {
            log.warn("{} API key authentication failed [path={}, method={}]",
                    LOG_PREFIX, request.getRequestURI(), request.getMethod());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"success\":false,\"code\":\"ARKA-SEC-4010\",\"message\":\"Unauthorized\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}
