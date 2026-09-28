package com.lannyrivero.retrystorm.simulator.domain.exception;

public class InvalidLogicalRequestId extends DomainException {

    public InvalidLogicalRequestId() {
        super("logicalRequestId must be positive");
    }
}
