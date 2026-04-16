package com.pommala.arka.email.transport.javamail.autoconfigure;

import com.pommala.arka.email.spi.EmailTransport;
import com.pommala.arka.email.transport.javamail.internal.JavaMailEmailTransport;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

/**
 * Auto-configuration for the JavaMail SMTP transport.
 *
 * <p>Wires timeout properties into {@link JavaMailSenderImpl} at startup.
 * A missing timeout configuration is a production bug — defaults are explicit.
 */
@AutoConfiguration
@ConditionalOnClass(JavaMailSender.class)
@EnableConfigurationProperties(JavaMailTransportProperties.class)
public class EmailJavaMailAutoConfiguration {

    /**
     * JavaMail email transport.
     * Pluggability rule: backs off when consumer provides own {@link EmailTransport}.
     */
    @Bean
    @ConditionalOnMissingBean(EmailTransport.class)
    public JavaMailEmailTransport javaMailEmailTransport(
            JavaMailSender mailSender,
            JavaMailTransportProperties props) {

        // Timeout properties wired at autoconfiguration time.
        // Missing timeout is a production bug — always set explicitly.
        if (mailSender instanceof JavaMailSenderImpl impl) {
            var javaMailProps = impl.getJavaMailProperties();
            javaMailProps.setProperty("mail.smtp.connectiontimeout",
                    String.valueOf(props.getConnectionTimeout().toMillis()));
            javaMailProps.setProperty("mail.smtp.timeout",
                    String.valueOf(props.getReadTimeout().toMillis()));
            javaMailProps.setProperty("mail.smtp.writetimeout",
                    String.valueOf(props.getWriteTimeout().toMillis()));
        }

        return new JavaMailEmailTransport(mailSender);
    }
}
