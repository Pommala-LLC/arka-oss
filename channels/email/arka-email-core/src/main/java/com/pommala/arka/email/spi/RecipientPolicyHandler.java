package com.pommala.arka.email.spi;

import com.pommala.arka.email.model.NormalizedRecipients;
import java.util.List;

/**
 * SPI for recipient normalization, validation, and deduplication.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean at injection time.
 * Default backs off via {@code @ConditionalOnMissingBean}.
 *
 * <h3>Deduplication precedence (locked):</h3>
 * to &gt; cc &gt; bcc — an address in a higher-priority list wins.
 */
public interface RecipientPolicyHandler {

    /**
     * Normalises, validates, and deduplicates the given recipient address lists.
     *
     * @param to  primary recipients, never null
     * @param cc  CC recipients, never null
     * @param bcc BCC recipients, never null
     * @return normalised recipients with guarantees: no nulls, no duplicates
     */
    NormalizedRecipients normalize(List<String> to, List<String> cc, List<String> bcc);
}
