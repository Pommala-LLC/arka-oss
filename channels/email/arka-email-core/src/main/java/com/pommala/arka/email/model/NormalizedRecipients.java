package com.pommala.arka.email.model;

import java.util.List;
import java.util.Objects;

/**
 * Immutable result of recipient normalization, validation, and deduplication.
 *
 * <p>All lists are non-null and immutable after construction. Empty lists
 * are used in place of null collections. Dedup precedence: to &gt; cc &gt; bcc.
 *
 * @param to  normalised primary recipients, never null
 * @param cc  normalised CC recipients, never null
 * @param bcc normalised BCC recipients, never null
 */
public record NormalizedRecipients(List<String> to, List<String> cc, List<String> bcc) {

    public NormalizedRecipients {
        Objects.requireNonNull(to,  "to must not be null");
        Objects.requireNonNull(cc,  "cc must not be null");
        Objects.requireNonNull(bcc, "bcc must not be null");
        // Immutability rule: all collection fields defensively copied.
        to  = List.copyOf(to);
        cc  = List.copyOf(cc);
        bcc = List.copyOf(bcc);
    }

    /** Returns true if there are no primary recipients. */
    public boolean hasPrimaryRecipients() { return !to.isEmpty(); }
}
