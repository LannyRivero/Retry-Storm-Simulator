package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidLogicalRequestId;

public record LogicalRequestId(int value) {

    public LogicalRequestId {
        if (value <= 0) {
            throw new InvalidLogicalRequestId();
        }
    }
}
