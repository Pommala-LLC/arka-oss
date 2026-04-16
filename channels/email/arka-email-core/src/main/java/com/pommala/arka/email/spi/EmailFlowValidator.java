package com.pommala.arka.email.spi;

import com.pommala.arka.spi.ChannelFlowValidator;

/**
 * Email-specific channel flow validator.
 *
 * <p>Implements {@link ChannelFlowValidator} for the {@code email} channel.
 * Registered as an additive bean — collected by the shared validation engine.
 *
 * <h3>Rules to enforce:</h3>
 * <ul>
 *   <li>Every flow must declare a template, subject, and sender-ref.</li>
 *   <li>Sender-ref must resolve to a defined sender.</li>
 *   <li>All errors collected before returning — never throw on first error.</li>
 * </ul>
 */
public interface EmailFlowValidator extends ChannelFlowValidator {}
