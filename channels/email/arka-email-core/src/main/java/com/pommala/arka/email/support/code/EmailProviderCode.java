package com.pommala.arka.email.support.code;

import com.pommala.arka.support.code.ApplicationCode;

/**
 * Email provider resolution codes. Used by the YAML and DB providers.
 * {@code HYBRID_FALLBACK} lives in the PaaS hybrid provider module, not here.
 * Prefix: {@code EMAIL-PROV-*}
 */
public enum EmailProviderCode implements ApplicationCode {

    FLOW_NOT_FOUND_ANYWHERE      ("EMAIL-PROV-4002", "Flow not found in any provider"),
    SENDER_REF_MISSING           ("EMAIL-PROV-4003", "Sender reference missing in flow"),
    SENDER_REF_UNRESOLVABLE      ("EMAIL-PROV-4004", "Sender reference could not be resolved"),
    ATTACHMENT_REF_UNRESOLVABLE  ("EMAIL-PROV-4005", "Attachment reference could not be resolved");

    private final String code;
    private final String defaultMessage;

    EmailProviderCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override public String code()           { return code; }
    @Override public String defaultMessage() { return defaultMessage; }
}
