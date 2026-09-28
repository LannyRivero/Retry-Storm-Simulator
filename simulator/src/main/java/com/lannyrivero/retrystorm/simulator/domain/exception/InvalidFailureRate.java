package com.lannyrivero.retrystorm.simulator.domain.exception;

public class InvalidFailureRate extends DomainException {

    public InvalidFailureRate() {
        super("failureRate must be between 0 and 1");
    }
}
