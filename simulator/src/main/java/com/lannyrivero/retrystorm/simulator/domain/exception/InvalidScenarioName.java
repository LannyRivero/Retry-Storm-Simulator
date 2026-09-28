package com.lannyrivero.retrystorm.simulator.domain.exception;

public class InvalidScenarioName extends DomainException {

    public InvalidScenarioName() {
        super("scenario name must not be blank");
    }
}
