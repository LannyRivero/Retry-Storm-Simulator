package com.lannyrivero.retrystorm.simulator.application.port.out;

import java.time.Duration;

/**
 * Observed result of one downstream attempt.
 * <p> The simulator uses this result to build experiment measurements such as success rate, retry amplification factor, and latency. * DependencyCallResult
 * @param successful
 * @param latency
 */

public record DependencyCallResult(boolean successful, Duration latency) {

    public DependencyCallResult {
        if (latency == null || latency.isNegative()) {
            throw new IllegalArgumentException("latency must be zero or positive");
        }
    }
}
