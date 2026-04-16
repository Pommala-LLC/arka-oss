package com.pommala.arka.email.model;

import com.pommala.arka.email.model.builder.FinalEmailMessageBuilder;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable, send-ready email message handed to {@link com.pommala.arka.email.spi.EmailTransport}.
 *
 * <p>All collection fields use {@code List.copyOf()} and {@code Map.copyOf()}.
 * This record is fully immutable after construction. Constructed exclusively by
 * {@link FinalEmailMessageBuilder}.
 *
 * <h3>Construction rules:</h3>
 * <ul>
 *   <li>{@code flowKey} — required, non-null.</li>
 *   <li>{@code from} — required, non-null, non-blank.</li>
 *   <li>{@code to} — required, non-null, must contain at least one address (enforced by service).</li>
 *   <li>{@code subject} — required, non-null, non-blank.</li>
 *   <li>{@code htmlBody} — required, non-null.</li>
 *   <li>{@code replyTo} — nullable. Null means no Reply-To header.</li>
 *   <li>{@code cc}, {@code bcc} — nullable, normalised to empty list.</li>
 *   <li>{@code attachmentPaths} — nullable, normalised to empty map.</li>
 *   <li>{@code correlationId} — required, non-null. Read from MDC — never generated downstream.</li>
 * </ul>
 *
 * @param flowKey         the flow key this message was built for
 * @param from            the sender address (formatted with display name when present)
 * @param replyTo         reply-to address; null means no Reply-To header
 * @param to              primary recipients, never null
 * @param cc              CC recipients, never null
 * @param bcc             BCC recipients, never null
 * @param subject         the email subject, never null or blank
 * @param htmlBody        the rendered HTML body, never null
 * @param attachmentPaths map of attachment name to file path, never null
 * @param correlationId   tracking ID from MDC, never null
 * @see FinalEmailMessageBuilder
 */
public record FinalEmailMessage(
        String flowKey,
        String from,
        String replyTo,
        List<String> to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String htmlBody,
        Map<String, String> attachmentPaths,
        String correlationId) {

    public FinalEmailMessage {
        Objects.requireNonNull(flowKey,       "flowKey must not be null");
        Objects.requireNonNull(from,          "from must not be null");
        Objects.requireNonNull(to,            "to must not be null");
        Objects.requireNonNull(subject,       "subject must not be null");
        Objects.requireNonNull(htmlBody,      "htmlBody must not be null");
        Objects.requireNonNull(correlationId, "correlationId must not be null");
        if (from.isBlank())    throw new IllegalArgumentException("from must not be blank");
        if (subject.isBlank()) throw new IllegalArgumentException("subject must not be blank");
        // Immutability rule: all collection fields defensively copied.
        to              = List.copyOf(to);
        cc              = cc              != null ? List.copyOf(cc)              : List.of();
        bcc             = bcc             != null ? List.copyOf(bcc)             : List.of();
        attachmentPaths = attachmentPaths != null ? Map.copyOf(attachmentPaths)  : Map.of();
    }

    /** Returns a new builder. */
    public static FinalEmailMessageBuilder builder() {
        return new FinalEmailMessageBuilder();
    }
}
