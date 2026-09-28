package com.lannyrivero.retrystorm.simulator.domain.model;

import com.lannyrivero.retrystorm.simulator.domain.valueobject.ConcurrencyLevel;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.LogicalRequestCount;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ScenarioName;

public record ExperimentScenario(
        ScenarioName name,
        LogicalRequestCount logicalRequests,
        ConcurrencyLevel concurrency,
        FailureRate failureRate,
        Latency latency,
        ExperimentSeed seed) {
}
