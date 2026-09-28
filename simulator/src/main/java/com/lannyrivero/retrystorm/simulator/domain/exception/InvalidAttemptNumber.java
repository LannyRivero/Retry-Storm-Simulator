package com.lannyrivero.retrystorm.simulator.domain.exception;

public class InvalidAttemptNumber extends DomainException {

    public InvalidAttemptNumber() {
        super("attempt must be positive");
    }
}
