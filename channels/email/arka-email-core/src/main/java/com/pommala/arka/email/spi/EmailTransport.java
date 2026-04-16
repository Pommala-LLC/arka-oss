package com.pommala.arka.email.spi;

import com.pommala.arka.email.model.FinalEmailMessage;
import com.pommala.arka.email.support.exception.EmailTransportException;

/**
 * SPI for physical email delivery.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 * Default (JavaMail) backs off via {@code @ConditionalOnMissingBean}.
 *
 * <h3>Implementation rules:</h3>
 * <ul>
 *   <li>Thread safety: must be thread-safe — called concurrently from batch fan-out.</li>
 *   <li>Every exit must return normally or throw a typed {@link EmailTransportException}.
 *       Raw framework exceptions must never escape.</li>
 *   <li>Timeout configuration is mandatory — a missing timeout is a production bug.</li>
 * </ul>
 */
public interface EmailTransport {

    /**
     * Sends the given email message via the underlying transport mechanism.
     *
     * @param message the fully constructed email message, never null
     * @throws EmailTransportException on any transport failure
     */
    void send(FinalEmailMessage message);
}
