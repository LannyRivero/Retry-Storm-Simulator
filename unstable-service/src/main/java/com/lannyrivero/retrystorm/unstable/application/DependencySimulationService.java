package com.lannyrivero.retrystorm.unstable.application;

import org.springframework.stereotype.Service;

import com.lannyrivero.retrystorm.unstable.application.command.DependencySimulationCommand;
import com.lannyrivero.retrystorm.unstable.application.port.out.LatencyDelayer;
import com.lannyrivero.retrystorm.unstable.domain.model.DependencyOutcome;
import com.lannyrivero.retrystorm.unstable.domain.service.DeterministicFailureModel;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.LogicalRequestId;

@Service
public class DependencySimulationService {

    private final DeterministicFailureModel failureModel;
    private final LatencyDelayer latencyDelayer;

    public DependencySimulationService(DeterministicFailureModel failureModel, LatencyDelayer latencyDelayer) {
        this.failureModel = failureModel;
        this.latencyDelayer = latencyDelayer;
    }

    public DependencyOutcome simulate(DependencySimulationCommand command) {
        ExperimentSeed seed = command.toExperimentSeed();
        LogicalRequestId logicalRequestId = command.toLogicalRequestId();
        AttemptNumber attempt = command.toAttemptNumber();
        FailureRate failureRate = command.toFailureRate();
        Latency latency = command.toLatency();

        latencyDelayer.delay(latency.milliseconds());

        boolean failed = failureModel.shouldFail(
                seed,
                logicalRequestId,
                attempt,
                failureRate);

        return new DependencyOutcome(logicalRequestId, attempt, !failed);
    }
}
