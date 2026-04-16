package com.pommala.arka.email.model.builder;

import com.pommala.arka.email.model.FinalEmailMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fluent builder for {@link FinalEmailMessage}.
 *
 * <p>Used exclusively by message construction code — not for application consumers.
 *
 * @see FinalEmailMessage
 */
public final class FinalEmailMessageBuilder {

    private String flowKey;
    private String from;
    private String replyTo;
    private final List<String> to  = new ArrayList<>();
    private final List<String> cc  = new ArrayList<>();
    private final List<String> bcc = new ArrayList<>();
    private String subject;
    private String htmlBody;
    private final Map<String, String> attachmentPaths = new HashMap<>();
    private String correlationId;

    public FinalEmailMessageBuilder flowKey(String v)       { this.flowKey = v; return this; }
    public FinalEmailMessageBuilder from(String v)          { this.from = v; return this; }
    public FinalEmailMessageBuilder replyTo(String v)       { this.replyTo = v; return this; }
    public FinalEmailMessageBuilder to(List<String> v)      { this.to.addAll(v); return this; }
    public FinalEmailMessageBuilder cc(List<String> v)      { this.cc.addAll(v); return this; }
    public FinalEmailMessageBuilder bcc(List<String> v)     { this.bcc.addAll(v); return this; }
    public FinalEmailMessageBuilder subject(String v)       { this.subject = v; return this; }
    public FinalEmailMessageBuilder htmlBody(String v)      { this.htmlBody = v; return this; }
    public FinalEmailMessageBuilder attachments(Map<String, String> v) {
        this.attachmentPaths.putAll(v); return this;
    }
    public FinalEmailMessageBuilder correlationId(String v) { this.correlationId = v; return this; }

    /** Constructs the immutable {@link FinalEmailMessage}. */
    public FinalEmailMessage build() {
        return new FinalEmailMessage(flowKey, from, replyTo,
                List.copyOf(to), List.copyOf(cc), List.copyOf(bcc),
                subject, htmlBody, Map.copyOf(attachmentPaths), correlationId);
    }
}
