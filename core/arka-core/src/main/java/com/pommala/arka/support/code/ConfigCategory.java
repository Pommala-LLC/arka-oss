package com.pommala.arka.support.code;

/**
 * Marker interface for channel-specific configuration category enums.
 *
 * <p>Each channel defines its own {@code ConfigCategory} enum that implements
 * this interface. The shared exception handler references this type; channel
 * exception handlers do exhaustive switches on the concrete channel enum.</p>
 *
 * <p>Example: {@code EmailConfigCategory.SENDER} → HTTP 500 with code
 * {@code EMAIL-HTTP-5002}.</p>
 */
public interface ConfigCategory {

    /** Display name for this category, used in diagnostic messages. */
    String displayName();
}
