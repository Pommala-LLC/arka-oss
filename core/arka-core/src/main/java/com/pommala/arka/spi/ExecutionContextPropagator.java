package com.pommala.arka.spi;

import java.util.concurrent.Callable;

/**
 * Additive SPI for propagating thread-local context across virtual thread boundaries.
 *
 * <p>Contract type: {@code spi} — all registered beans are applied at task
 * submission time. Multiple beans are supported; each captures and restores
 * only its own context independently.
 *
 * <h3>Implementation rules:</h3>
 * <ul>
 *   <li>Thread safety: capture happens on the calling thread; restore on the worker thread.</li>
 *   <li>Each propagator is independent — order of wrapping does not matter for correctness.</li>
 *   <li>Always clean up in a {@code finally} block — never leave context on the worker thread.</li>
 * </ul>
 *
 * <h3>Correct pattern:</h3>
 * <pre>{@code
 * public Runnable wrap(Runnable task) {
 *     SomeContext captured = SomeContext.current(); // capture on calling thread
 *     return () -> {
 *         SomeContext.set(captured);                // restore on worker thread
 *         try { task.run(); }
 *         finally { SomeContext.clear(); }          // always clean up
 *     };
 * }
 * }</pre>
 */
public interface ExecutionContextPropagator {

    /**
     * Wraps a {@link Runnable} to propagate context from the current thread.
     *
     * @param task the task to wrap, never null
     * @return a wrapped task that restores context on the worker thread
     */
    Runnable wrap(Runnable task);

    /**
     * Wraps a {@link Callable} to propagate context from the current thread.
     *
     * @param task the task to wrap, never null
     * @return a wrapped task that restores context on the worker thread
     */
    <T> Callable<T> wrap(Callable<T> task);
}
