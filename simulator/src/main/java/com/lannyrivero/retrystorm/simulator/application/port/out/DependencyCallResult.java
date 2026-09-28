package com.lannyrivero.retrystorm.simulator.application.port.out;

import java.time.Duration;

public record DependencyCallResult(boolean successful, Duration latency) {

    public DependencyCallResult {
        if (latency == null || latency.isNegative()) {
            throw new IllegalArgumentException("latency must be zero or positive");
        }
    }
}
