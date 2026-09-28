package com.lannyrivero.retrystorm.unstable.domain.valueobject;

import com.lannyrivero.retrystorm.unstable.domain.exception.InvalidLatency;

public record Latency(long milliseconds) {

    public static final Latency NONE = new Latency(0);

    public Latency {
        if (milliseconds < 0) {
            throw new InvalidLatency();
        }
    }
}
