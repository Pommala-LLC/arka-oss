package com.pommala.arka.sms.spi;

import com.pommala.arka.spi.ChannelFlowValidator;

/**
 * SMS-specific channel flow validator.
 *
 * <h3>Rules to enforce:</h3>
 * <ul>
 *   <li>Every flow must declare a sender-ref resolving to a from-number.</li>
 *   <li>Every flow must declare a template or body.</li>
 *   <li>Message type (transactional/promotional) must be set.</li>
 * </ul>
 */
public interface SmsFlowValidator extends ChannelFlowValidator {}
