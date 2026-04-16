package com.pommala.arka.async.internal;

import com.pommala.arka.spi.ExecutionContextPropagator;
import java.util.Map;
import java.util.concurrent.Callable;
import org.slf4j.MDC;

/**
 * Propagates SLF4J MDC context across virtual thread boundaries.
 *
 * <p>Captures the full MDC map on the calling thread and restores it
 * on the worker thread. Always clears in a {@code finally} block.
 */
public class MdcContextPropagator implements ExecutionContextPropagator {

    @Override
    public Runnable wrap(Runnable task) {
        // Capture on calling thread
        var capturedMdc = MDC.getCopyOfContextMap();
        return () -> {
            // Restore on worker thread
            var previous = MDC.getCopyOfContextMap();
            if (capturedMdc != null) {
                MDC.setContextMap(capturedMdc);
            } else {
                MDC.clear();
            }
            try {
                task.run();
            } finally {
                // Always clean up — never leave MDC on the worker thread
                MDC.clear();
                if (previous != null) {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    @Override
    public <T> Callable<T> wrap(Callable<T> task) {
        // Capture on calling thread
        var capturedMdc = MDC.getCopyOfContextMap();
        return () -> {
            var previous = MDC.getCopyOfContextMap();
            if (capturedMdc != null) {
                MDC.setContextMap(capturedMdc);
            } else {
                MDC.clear();
            }
            try {
                return task.call();
            } finally {
                MDC.clear();
                if (previous != null) {
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}
