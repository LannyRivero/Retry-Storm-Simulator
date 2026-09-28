package com.lannyrivero.retrystorm.unstable.application.command;

import com.lannyrivero.retrystorm.unstable.application.exception.InvalidDependencySimulationRequest;
import com.lannyrivero.retrystorm.unstable.domain.exception.DomainException;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.LogicalRequestId;

public record DependencySimulationCommand(
        long seed,
        int logicalRequestId,
        int attempt,
        double failureRate,
        long latencyMs) {

    public ExperimentSeed toExperimentSeed() {
        return new ExperimentSeed(seed);
    }

    public LogicalRequestId toLogicalRequestId() {
        return mapInvalidRequest(() -> new LogicalRequestId(logicalRequestId));
    }

    public AttemptNumber toAttemptNumber() {
        return mapInvalidRequest(() -> new AttemptNumber(attempt));
    }

    public FailureRate toFailureRate() {
        return mapInvalidRequest(() -> new FailureRate(failureRate));
    }

    public Latency toLatency() {
        return mapInvalidRequest(() -> new Latency(latencyMs));
    }

    private <T> T mapInvalidRequest(ValueObjectFactory<T> factory) {
        try {
            return factory.create();
        } catch (DomainException exception) {
            throw new InvalidDependencySimulationRequest(exception.getMessage());
        }
    }

    @FunctionalInterface
    private interface ValueObjectFactory<T> {

        T create();
    }
}
