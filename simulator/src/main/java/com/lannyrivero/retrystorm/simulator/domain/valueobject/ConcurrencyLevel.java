package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidConcurrencyLevel;

public record ConcurrencyLevel(int value) {

    public ConcurrencyLevel {
        if (value <= 0) {
            throw new InvalidConcurrencyLevel();
        }
    }
}
