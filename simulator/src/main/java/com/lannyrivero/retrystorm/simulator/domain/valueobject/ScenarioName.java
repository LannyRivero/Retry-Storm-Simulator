package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidScenarioName;

public record ScenarioName(String value) {

    public ScenarioName {
        if (value == null || value.isBlank()) {
            throw new InvalidScenarioName();
        }
        value = value.trim();
    }
}
