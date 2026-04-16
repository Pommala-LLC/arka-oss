package com.pommala.arka.email.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable resolved email flow configuration.
 *
 * <p>All collection fields use {@code List.copyOf()} and {@code Map.copyOf()}.
 * Resolved by the YAML or DB provider before message construction begins.
 *
 * <h3>Construction rules:</h3>
 * <ul>
 *   <li>{@code flowKey} — required, non-null.</li>
 *   <li>{@code template} — required, non-null, non-blank.</li>
 *   <li>{@code subject} — required, non-null, non-blank.</li>
 *   <li>{@code sender} — required, non-null.</li>
 *   <li>{@code staticCc}, {@code staticBcc} — nullable, normalised to empty list.</li>
 *   <li>{@code attachmentPaths} — nullable, normalised to empty map.</li>
 * </ul>
 *
 * @param flowKey         the flow identifier
 * @param template        the template name for rendering
 * @param subject         the email subject line
 * @param sender          the resolved sender identity
 * @param staticCc        static CC addresses from flow config
 * @param staticBcc       static BCC addresses from flow config
 * @param attachmentPaths map of attachment-ref to classpath/filesystem path
 */
public record ResolvedEmailFlow(
        String flowKey,
        String template,
        String subject,
        ResolvedSender sender,
        List<String> staticCc,
        List<String> staticBcc,
        Map<String, String> attachmentPaths) {

    public ResolvedEmailFlow {
        Objects.requireNonNull(flowKey,  "flowKey must not be null");
        Objects.requireNonNull(template, "template must not be null");
        Objects.requireNonNull(subject,  "subject must not be null");
        Objects.requireNonNull(sender,   "sender must not be null");
        if (template.isBlank()) throw new IllegalArgumentException("template must not be blank");
        if (subject.isBlank())  throw new IllegalArgumentException("subject must not be blank");
        // Immutability rule: collection fields defensively copied.
        staticCc        = staticCc        != null ? List.copyOf(staticCc)        : List.of();
        staticBcc       = staticBcc       != null ? List.copyOf(staticBcc)       : List.of();
        attachmentPaths = attachmentPaths != null ? Map.copyOf(attachmentPaths)  : Map.of();
    }
}
