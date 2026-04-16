package com.pommala.arka.email.recipient.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Recipient normalization policy properties.
 *
 * <pre>{@code
 * arka:
 *   email:
 *     policy:
 *       runtime-validation:
 *         invalid-to-action: FAIL
 *         invalid-cc-action: DROP_WARN
 *         invalid-bcc-action: DROP_WARN
 * }</pre>
 */
@ConfigurationProperties(prefix = "arka.email.policy.runtime-validation")
public class RecipientPolicy {

    /** Action when a primary (to) address is invalid. Default FAIL. */
    private String invalidToAction  = "FAIL";

    /** Action when a CC address is invalid. Default DROP_WARN. */
    private String invalidCcAction  = "DROP_WARN";

    /** Action when a BCC address is invalid. Default DROP_WARN. */
    private String invalidBccAction = "DROP_WARN";

    public String getInvalidToAction()  { return invalidToAction; }
    public String getInvalidCcAction()  { return invalidCcAction; }
    public String getInvalidBccAction() { return invalidBccAction; }

    public void setInvalidToAction(String v)  { this.invalidToAction  = v; }
    public void setInvalidCcAction(String v)  { this.invalidCcAction  = v; }
    public void setInvalidBccAction(String v) { this.invalidBccAction = v; }
}
