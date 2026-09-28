package com.lannyrivero.retrystorm.unstable.domain.exception;

public class InvalidLogicalRequestId extends DomainException {

    public InvalidLogicalRequestId() {
        super("logicalRequestId must be positive");
    }
}
