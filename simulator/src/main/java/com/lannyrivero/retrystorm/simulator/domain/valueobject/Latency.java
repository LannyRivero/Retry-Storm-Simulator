package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import java.time.Duration;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidLatency;

public record Latency(Duration duration) {

    public static final Latency NONE = new Latency(Duration.ZERO);

    public Latency {
        if (duration == null || duration.isNegative()) {
            throw new InvalidLatency();
        }
    }
}
