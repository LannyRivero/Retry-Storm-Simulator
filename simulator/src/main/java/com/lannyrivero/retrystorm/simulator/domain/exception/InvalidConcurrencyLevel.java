package com.lannyrivero.retrystorm.simulator.domain.exception;

public class InvalidConcurrencyLevel extends DomainException {

    public InvalidConcurrencyLevel() {
        super("concurrency must be positive");
    }
}
