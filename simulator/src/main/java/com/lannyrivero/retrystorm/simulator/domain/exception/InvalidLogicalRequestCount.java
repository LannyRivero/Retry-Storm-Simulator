package com.lannyrivero.retrystorm.simulator.domain.exception;

public class InvalidLogicalRequestCount extends DomainException {

    public InvalidLogicalRequestCount() {
        super("logicalRequests must be positive");
    }
}
