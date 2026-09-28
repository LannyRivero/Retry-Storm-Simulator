package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidAttemptNumber;

public record AttemptNumber(int value) {

    public AttemptNumber {
        if (value <= 0) {
            throw new InvalidAttemptNumber();
        }
    }
}
