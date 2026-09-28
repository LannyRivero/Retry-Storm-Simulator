package com.lannyrivero.retrystorm.unstable.domain.valueobject;

import com.lannyrivero.retrystorm.unstable.domain.exception.InvalidLogicalRequestId;

public record LogicalRequestId(int value) {

    public LogicalRequestId {
        if (value <= 0) {
            throw new InvalidLogicalRequestId();
        }
    }
}
