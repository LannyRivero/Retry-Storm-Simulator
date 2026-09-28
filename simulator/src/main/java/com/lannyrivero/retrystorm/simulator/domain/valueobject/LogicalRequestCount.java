package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidLogicalRequestCount;

public record LogicalRequestCount(int value) {

    public LogicalRequestCount {
        if (value <= 0) {
            throw new InvalidLogicalRequestCount();
        }
    }
}
