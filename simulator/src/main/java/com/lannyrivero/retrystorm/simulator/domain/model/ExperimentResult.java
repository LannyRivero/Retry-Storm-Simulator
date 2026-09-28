package com.lannyrivero.retrystorm.simulator.domain.model;

public record ExperimentResult(
        ExperimentScenario scenario,
        ResilienceStrategy strategy,
        ExperimentMeasurements measurements) {
}
