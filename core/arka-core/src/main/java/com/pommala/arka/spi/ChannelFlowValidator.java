package com.pommala.arka.spi;

import java.util.List;

/**
 * Additive SPI for channel-specific startup flow validation.
 *
 * <p>Contract type: {@code spi} — all registered beans are called by the
 * shared validation engine. Multiple beans are supported.
 *
 * <h3>Implementation rules:</h3>
 * <ul>
 *   <li>Thread safety: called once at startup from a single thread.</li>
 *   <li>Must collect ALL validation errors before returning — never throw on first error.</li>
 *   <li>Returns an empty list when all flows are valid.</li>
 * </ul>
 */
public interface ChannelFlowValidator {

    /** Returns the channel identifier this validator serves. */
    String channelId();

    /**
     * Validates all channel-specific flow configuration.
     * Collects and returns all violations — never throws.
     *
     * @return list of validation error messages, empty if valid
     */
    List<String> validate();
}
