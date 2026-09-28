package com.lannyrivero.retrystorm.unstable.domain.exception;

public class InvalidAttemptNumber extends DomainException {

    public InvalidAttemptNumber() {
        super("attempt must be positive");
    }
}
