package com.lannyrivero.retrystorm.unstable.domain.valueobject;

import com.lannyrivero.retrystorm.unstable.domain.exception.InvalidAttemptNumber;

public record AttemptNumber(int value) {

    public AttemptNumber {
        if (value <= 0) {
            throw new InvalidAttemptNumber();
        }
    }
}
