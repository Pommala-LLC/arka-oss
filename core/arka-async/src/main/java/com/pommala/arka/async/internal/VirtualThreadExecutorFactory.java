package com.pommala.arka.async.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory for virtual thread executor services.
 *
 * <p>Creates named virtual thread executors for use by channel batch dispatchers.
 * Each channel creates its own executor with a descriptive name prefix.
 */
public final class VirtualThreadExecutorFactory {

    private VirtualThreadExecutorFactory() {}

    /**
     * Creates a new virtual thread per-task executor with the given name prefix.
     *
     * @param namePrefix prefix applied to virtual thread names (e.g., "arka-email-")
     * @return a new virtual thread executor, never null
     */
    public static ExecutorService create(String namePrefix) {
        return Executors.newThreadPerTaskExecutor(
                Thread.ofVirtual().name(namePrefix, 0).factory());
    }
}
