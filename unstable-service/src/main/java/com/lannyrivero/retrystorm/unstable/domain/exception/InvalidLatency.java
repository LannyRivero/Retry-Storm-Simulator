package com.lannyrivero.retrystorm.unstable.domain.exception;

public class InvalidLatency extends DomainException {

    public InvalidLatency() {
        super("latencyMs must be greater than or equal to 0");
    }
}
