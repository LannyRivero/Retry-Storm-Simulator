package com.lannyrivero.retrystorm.simulator.application.usecase;

import com.lannyrivero.retrystorm.simulator.application.command.RunExperimentCommand;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCall;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCallResult;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyGateway;
import com.lannyrivero.retrystorm.simulator.domain.exception.UnsupportedResilienceStrategy;
import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentResult;
import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentScenario;
import com.lannyrivero.retrystorm.simulator.domain.model.ResilienceStrategy;
import com.lannyrivero.retrystorm.simulator.domain.service.ExperimentMeasurementAccumulator;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.LogicalRequestId;

public class RunExperimentUseCase {

    private static final AttemptNumber FIRST_ATTEMPT = new AttemptNumber(1);

    private final DependencyGateway dependencyGateway;

    public RunExperimentUseCase(DependencyGateway dependencyGateway) {
        this.dependencyGateway = dependencyGateway;
    }

    public ExperimentResult run(RunExperimentCommand command) {
        if (command.strategy() != ResilienceStrategy.NO_RETRY) {
            throw new UnsupportedResilienceStrategy(command.strategy());
        }

        ExperimentScenario scenario = command.scenario();
        ExperimentMeasurementAccumulator measurements = new ExperimentMeasurementAccumulator();

        for (int requestNumber = 1; requestNumber <= scenario.logicalRequests().value(); requestNumber++) {
            DependencyCallResult callResult = dependencyGateway.call(new DependencyCall(
                    scenario.seed(),
                    new LogicalRequestId(requestNumber),
                    FIRST_ATTEMPT,
                    scenario.failureRate(),
                    scenario.latency()));

            measurements.recordDownstreamCall(callResult.successful(), callResult.latency());
        }

        return new ExperimentResult(scenario, command.strategy(), measurements.toMeasurements());
    }
}
