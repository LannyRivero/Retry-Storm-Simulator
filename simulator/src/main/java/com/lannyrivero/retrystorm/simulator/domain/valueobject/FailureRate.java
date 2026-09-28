package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidFailureRate;

public record FailureRate(double value) {

    public FailureRate {
        if (!Double.isFinite(value) || value < 0 || value > 1) {
            throw new InvalidFailureRate();
        }
    }
}
