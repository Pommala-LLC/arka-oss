package com.pommala.arka.spi;

/**
 * SPI for concurrency throttling in batch dispatch.
 *
 * <p>Contract type: {@code spi} — exactly one effective bean.
 * Default (semaphore-based) backs off via {@code @ConditionalOnMissingBean}.
 *
 * <h3>Usage contract:</h3>
 * <pre>{@code
 * throttle.acquire();
 * try { executor.submit(task); }
 * catch (RejectedExecutionException e) { throttle.release(); throw e; }
 * future.whenComplete((r, t) -> throttle.release());
 * }</pre>
 */
public interface ThrottlePolicy {

    /** Acquires a permit, blocking if necessary. */
    void acquire() throws InterruptedException;

    /** Releases a permit. Always called in finally or whenComplete. */
    void release();
}
