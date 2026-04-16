package com.pommala.arka.async.internal;

import com.pommala.arka.spi.ThrottlePolicy;
import java.util.concurrent.Semaphore;

/**
 * Semaphore-based concurrency throttle.
 *
 * <p>Prevents unbounded virtual thread spawning during batch dispatch.
 * Acquired before task submission; released in {@code whenComplete}.
 *
 * @see ThrottlePolicy
 */
public class SemaphoreThrottlePolicy implements ThrottlePolicy {

    private final Semaphore semaphore;

    public SemaphoreThrottlePolicy(int maxConcurrent) {
        this.semaphore = new Semaphore(maxConcurrent);
    }

    @Override
    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    @Override
    public void release() {
        semaphore.release();
    }
}
