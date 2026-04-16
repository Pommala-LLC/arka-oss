package com.pommala.arka.whatsapp.spi;

import com.pommala.arka.spi.ChannelFlowValidator;

/**
 * WhatsApp-specific channel flow validator.
 *
 * <h3>Rules to enforce:</h3>
 * <ul>
 *   <li>Every flow must declare a delivery mode (session or template).</li>
 *   <li>Template flows must declare a Meta template name and language code.</li>
 *   <li>Every flow must declare a sender-ref resolving to a phone-number-id.</li>
 *   <li>All errors collected before returning — never throw on first error.</li>
 * </ul>
 */
public interface WhatsAppFlowValidator extends ChannelFlowValidator {}
