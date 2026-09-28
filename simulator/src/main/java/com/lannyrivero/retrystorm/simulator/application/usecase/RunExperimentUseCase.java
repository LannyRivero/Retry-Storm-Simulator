package com.lannyrivero.retrystorm.simulator.application.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.lannyrivero.retrystorm.simulator.application.command.RunExperimentCommand;
import com.lannyrivero.retrystorm.simulator.application.exception.ExperimentExecutionFailed;
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
        List<Future<DependencyCallResult>> callResults = dispatchNoRetryCalls(scenario);

        for (Future<DependencyCallResult> callResult : callResults) {
            DependencyCallResult result = await(callResult);
            measurements.recordDownstreamCall(result.successful(), result.latency());
        }

        return new ExperimentResult(scenario, command.strategy(), measurements.toMeasurements());
    }

    private List<Future<DependencyCallResult>> dispatchNoRetryCalls(ExperimentScenario scenario) {
        ExecutorService executor = Executors.newFixedThreadPool(scenario.concurrency().value());
        List<Future<DependencyCallResult>> callResults = new ArrayList<>();

        try {
            for (int requestNumber = 1; requestNumber <= scenario.logicalRequests().value(); requestNumber++) {
                DependencyCall call = new DependencyCall(
                        scenario.seed(),
                        new LogicalRequestId(requestNumber),
                        FIRST_ATTEMPT,
                        scenario.failureRate(),
                        scenario.latency());

                callResults.add(executor.submit(() -> dependencyGateway.call(call)));
            }

            return callResults;
        } finally {
            executor.shutdown();
        }
    }

    private DependencyCallResult await(Future<DependencyCallResult> callResult) {
        try {
            return callResult.get();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new ExperimentExecutionFailed("experiment execution was interrupted", exception);
        } catch (ExecutionException exception) {
            throw new ExperimentExecutionFailed("dependency call failed", exception.getCause());
        }
    }
}
