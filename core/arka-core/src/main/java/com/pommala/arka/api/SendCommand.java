package com.pommala.arka.api;

/**
 * Marker interface for all channel send commands.
 *
 * <p>Contract type: {@code api} — application code constructs and passes these directly.
 * Every channel defines its own typed command that implements this interface.
 *
 * <h3>Invariants all implementations must enforce:</h3>
 * <ul>
 *   <li>{@code flowKey} — required, non-null, non-blank.</li>
 *   <li>All collection fields defensively copied at construction.</li>
 *   <li>Null collections normalised to empty collections.</li>
 * </ul>
 *
 * @see MessageFlowService
 */
public interface SendCommand {
    /** The flow key identifying the delivery configuration. Never null or blank. */
    String flowKey();
}
