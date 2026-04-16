package com.pommala.arka.email.recipient.internal;

import com.pommala.arka.email.model.NormalizedRecipients;
import com.pommala.arka.email.spi.RecipientPolicyHandler;
import com.pommala.arka.email.support.code.EmailRecipientCode;
import com.pommala.arka.email.support.exception.InvalidRecipientException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.SequencedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default recipient normalization, validation, and deduplication pipeline.
 *
 * <h3>Pipeline steps:</h3>
 * <ol>
 *   <li>Parse and normalise each address (trim, lowercase domain, extract from display name).</li>
 *   <li>Validate RFC syntax — apply {@link RecipientAction} per failure.</li>
 *   <li>Deduplicate across lists: to &gt; cc &gt; bcc precedence.</li>
 * </ol>
 *
 * @see RecipientPolicyHandler
 */
public class DefaultRecipientNormalizer implements RecipientPolicyHandler {

    private static final String LOG_PREFIX = "[arka-email-recipient]";
    private static final Logger log = LoggerFactory.getLogger(DefaultRecipientNormalizer.class);

    private final RecipientPolicy policy;

    public DefaultRecipientNormalizer(RecipientPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    @Override
    public NormalizedRecipients normalize(List<String> to, List<String> cc, List<String> bcc) {
        Objects.requireNonNull(to,  "to must not be null");
        Objects.requireNonNull(cc,  "cc must not be null");
        Objects.requireNonNull(bcc, "bcc must not be null");

        // Process each list through normalize → validate pipeline
        var normalizedTo  = processAddresses(to,  true);
        var normalizedCc  = processAddresses(cc,  false);
        var normalizedBcc = processAddresses(bcc, false);

        // Business rule: dedup precedence is to > cc > bcc.
        // An address in a higher-priority list wins.
        var seen = new LinkedHashSet<String>();
        var dedupedTo  = deduplicate(normalizedTo,  seen, "to");
        var dedupedCc  = deduplicate(normalizedCc,  seen, "cc");
        var dedupedBcc = deduplicate(normalizedBcc, seen, "bcc");

        return new NormalizedRecipients(dedupedTo, dedupedCc, dedupedBcc);
    }

    private List<String> processAddresses(List<String> raw, boolean isPrimary) {
        var result = new ArrayList<String>(raw.size());
        for (var address : raw) {
            var normalized = normalizeAddress(address);
            if (normalized == null) {
                handleInvalid(address, isPrimary);
                continue;
            }
            result.add(normalized);
        }
        return result;
    }

    private String normalizeAddress(String raw) {
        if (raw == null || raw.isBlank()) return null;
        try {
            var parsed = InternetAddress.parse(raw.trim(), true);
            if (parsed.length == 0) return null;
            var addr = parsed[0];
            // Normalise: preserve local part, lowercase domain only (RFC 5321)
            var email = addr.getAddress();
            if (email == null) return null;
            int atIdx = email.indexOf('@');
            if (atIdx < 0) return null;
            return email.substring(0, atIdx + 1) + email.substring(atIdx + 1).toLowerCase();
        } catch (AddressException e) {
            return null;
        }
    }

    private void handleInvalid(String address, boolean isPrimary) {
        if (isPrimary) {
            // Policy-driven: invalid primary recipient → FAIL (abort the operation)
            throw new InvalidRecipientException(EmailRecipientCode.INVALID_ADDRESS_FORMAT,
                    "Invalid primary recipient address: " + mask(address));
        }
        // Policy-driven: invalid secondary recipient → DROP_WARN (log and drop)
        log.warn("{} Dropping invalid secondary address: {}", LOG_PREFIX, mask(address));
    }

    private List<String> deduplicate(List<String> addresses,
                                     SequencedSet<String> seen,
                                     String listName) {
        var result = new ArrayList<String>(addresses.size());
        for (var addr : addresses) {
            var key = addr.toLowerCase();
            if (seen.contains(key)) {
                // Business rule: dedup collapse — log WARN per collapsed address.
                log.warn("{} Duplicate recipient collapsed from '{}' list: {}",
                        LOG_PREFIX, listName, mask(addr));
                continue;
            }
            seen.add(key);
            result.add(addr);
        }
        return result;
    }

    private static String mask(String value) {
        if (value == null || value.length() <= 2) return "***";
        return value.charAt(0) + "***" + value.substring(value.length() - 1);
    }
}
