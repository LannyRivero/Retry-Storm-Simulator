package com.lannyrivero.retrystorm.simulator.domain.exception;

import com.lannyrivero.retrystorm.simulator.domain.model.ResilienceStrategy;

public class UnsupportedResilienceStrategy extends DomainException {

    public UnsupportedResilienceStrategy(ResilienceStrategy strategy) {
        super("strategy is not supported yet: " + strategy);
    }
}
