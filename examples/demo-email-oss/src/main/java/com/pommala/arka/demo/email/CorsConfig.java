package com.pommala.arka.demo.email;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for demo-email-oss.
 *
 * <p>Allows the tenant console HTML (opened from the filesystem or any origin)
 * to call Arka's REST endpoints and the actuator health check.
 *
 * <p>This open policy ({@code *}) is appropriate for local development only.
 * Restrict {@code allowedOrigins} to specific domains in production.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // covers /api/**, /actuator/**, etc.
                .allowedOriginPatterns("*") // allowedOriginPatterns supports * with credentials
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);              // preflight cache: 1 hour
    }
}
