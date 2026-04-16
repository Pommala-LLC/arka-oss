package com.pommala.arka.whatsapp.spi;

import com.pommala.arka.whatsapp.model.FinalWhatsAppMessage;
import com.pommala.arka.whatsapp.support.exception.WhatsAppTransportException;

/**
 * SPI for physical WhatsApp message delivery.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 * Default (Meta Cloud API) backs off via {@code @ConditionalOnMissingBean}.
 *
 * <h3>Implementation rules:</h3>
 * <ul>
 *   <li>Thread safety: must be thread-safe — called concurrently from batch fan-out.</li>
 *   <li>Every exit must return normally or throw a typed {@link WhatsAppTransportException}.</li>
 *   <li>Timeout configuration is mandatory.</li>
 * </ul>
 */
public interface WhatsAppTransport {

    void send(FinalWhatsAppMessage message);
}
