package com.lannyrivero.retrystorm.simulator.application.port.out;

import com.lannyrivero.retrystorm.simulator.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.LogicalRequestId;

public record DependencyCall(
        ExperimentSeed seed,
        LogicalRequestId logicalRequestId,
        AttemptNumber attempt,
        FailureRate failureRate,
        Latency latency) {
}
