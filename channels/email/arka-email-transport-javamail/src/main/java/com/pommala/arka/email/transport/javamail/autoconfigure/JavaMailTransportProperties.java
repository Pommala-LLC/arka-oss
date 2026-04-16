package com.pommala.arka.email.transport.javamail.autoconfigure;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JavaMail transport timeout configuration.
 *
 * <p>A missing or default timeout is a production bug.
 * These values are wired into {@link org.springframework.mail.javamail.JavaMailSenderImpl}
 * at autoconfiguration time.
 *
 * <pre>{@code
 * arka:
 *   email:
 *     transport:
 *       connection-timeout: 5s
 *       read-timeout: 10s
 *       write-timeout: 10s
 * }</pre>
 */
@ConfigurationProperties(prefix = "arka.email.transport")
public class JavaMailTransportProperties {

    /** SMTP connection timeout. Default 5s. */
    private Duration connectionTimeout = Duration.ofSeconds(5);

    /** SMTP read timeout. Default 10s. */
    private Duration readTimeout = Duration.ofSeconds(10);

    /** SMTP write timeout. Default 10s. */
    private Duration writeTimeout = Duration.ofSeconds(10);

    public Duration getConnectionTimeout() { return connectionTimeout; }
    public Duration getReadTimeout()       { return readTimeout; }
    public Duration getWriteTimeout()      { return writeTimeout; }

    public void setConnectionTimeout(Duration v) { this.connectionTimeout = v; }
    public void setReadTimeout(Duration v)       { this.readTimeout = v; }
    public void setWriteTimeout(Duration v)      { this.writeTimeout = v; }
}
