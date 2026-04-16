package com.pommala.arka.async.internal;

import com.pommala.arka.spi.ExecutionContextPropagator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Applies all registered {@link ExecutionContextPropagator} beans in sequence.
 *
 * <p>Each propagator wraps the task independently.
 * Order of application does not affect correctness.
 */
public class ExecutionContextPropagatorChain {

    private final List<ExecutionContextPropagator> propagators;

    public ExecutionContextPropagatorChain(List<ExecutionContextPropagator> propagators) {
        Objects.requireNonNull(propagators, "propagators must not be null");
        this.propagators = List.copyOf(propagators);
    }

    /**
     * Wraps a {@link Runnable} through all registered propagators.
     *
     * @param task the task to wrap, never null
     * @return a task that restores all propagated contexts on the worker thread
     */
    public Runnable wrap(Runnable task) {
        var wrapped = task;
        for (var propagator : propagators) {
            wrapped = propagator.wrap(wrapped);
        }
        return wrapped;
    }

    /**
     * Wraps a {@link Callable} through all registered propagators.
     *
     * @param task the task to wrap, never null
     * @return a task that restores all propagated contexts on the worker thread
     */
    public <T> Callable<T> wrap(Callable<T> task) {
        var wrapped = task;
        for (var propagator : propagators) {
            wrapped = propagator.wrap(wrapped);
        }
        return wrapped;
    }
}
