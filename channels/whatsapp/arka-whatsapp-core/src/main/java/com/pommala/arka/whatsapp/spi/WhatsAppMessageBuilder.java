package com.pommala.arka.whatsapp.spi;

import com.pommala.arka.whatsapp.api.WhatsAppSendCommand;
import com.pommala.arka.whatsapp.model.FinalWhatsAppMessage;
import com.pommala.arka.whatsapp.model.ResolvedWhatsAppFlow;

/**
 * SPI for constructing the send-ready WhatsApp message.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Resolves delivery mode (session vs template) from flow config.</li>
 *   <li>Binds template variables for template messages.</li>
 *   <li>Constructs session body for session messages.</li>
 *   <li>Attaches media URL if present.</li>
 * </ul>
 */
public interface WhatsAppMessageBuilder {

    FinalWhatsAppMessage build(ResolvedWhatsAppFlow flow, WhatsAppSendCommand command);
}
