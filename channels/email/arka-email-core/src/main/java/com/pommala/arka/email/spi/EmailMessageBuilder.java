package com.pommala.arka.email.spi;

import com.pommala.arka.email.api.EmailSendCommand;
import com.pommala.arka.email.model.FinalEmailMessage;
import com.pommala.arka.email.model.NormalizedRecipients;
import com.pommala.arka.email.model.ResolvedEmailFlow;

/**
 * SPI for constructing the send-ready email message.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 * Default backs off via {@code @ConditionalOnMissingBean}.
 *
 * <h3>Responsibilities:</h3>
 * <ul>
 *   <li>Renders the template (if a renderer is present) or uses the body override.</li>
 *   <li>Applies sender display name and reply-to from the resolved flow.</li>
 *   <li>Merges static flow CC/BCC with runtime CC/BCC from the command.</li>
 *   <li>Binds attachment resources.</li>
 * </ul>
 */
public interface EmailMessageBuilder {

    /**
     * Constructs an immutable {@link FinalEmailMessage} ready for transport.
     *
     * @param flow       the resolved email flow, never null
     * @param command    the send command, never null
     * @param recipients the normalised recipients, never null
     * @return the final email message, never null
     */
    FinalEmailMessage build(ResolvedEmailFlow flow, EmailSendCommand command,
                            NormalizedRecipients recipients);
}
