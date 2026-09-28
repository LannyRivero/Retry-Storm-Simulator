package com.lannyrivero.retrystorm.unstable.domain.valueobject;

import com.lannyrivero.retrystorm.unstable.domain.exception.InvalidFailureRate;

public record FailureRate(double value) {

    public static final FailureRate NEVER = new FailureRate(0);
    public static final FailureRate ALWAYS = new FailureRate(1);

    public FailureRate {
        if (value < 0 || value > 1) {
            throw new InvalidFailureRate();
        }
    }
}
