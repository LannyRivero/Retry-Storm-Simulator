package com.lannyrivero.retrystorm.simulator.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lannyrivero.retrystorm.simulator.application.command.RunExperimentCommand;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCall;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCallResult;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyGateway;
import com.lannyrivero.retrystorm.simulator.domain.exception.UnsupportedResilienceStrategy;
import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentResult;
import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentScenario;
import com.lannyrivero.retrystorm.simulator.domain.model.ResilienceStrategy;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ConcurrencyLevel;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.LogicalRequestCount;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ScenarioName;

class RunExperimentUseCaseTest {

    private static final ExperimentScenario SCENARIO = new ExperimentScenario(
            new ScenarioName("baseline"),
            new LogicalRequestCount(4),
            new ConcurrencyLevel(2),
            new FailureRate(0.30),
            new Latency(Duration.ofMillis(50)),
            new ExperimentSeed(481516L));

    private RecordingDependencyGateway dependencyGateway;
    private RunExperimentUseCase useCase;

    @BeforeEach
    void setUp() {
        dependencyGateway = new RecordingDependencyGateway();
        useCase = new RunExperimentUseCase(dependencyGateway);
    }

    @Test
    @DisplayName("Runs a no-retry experiment with one downstream call per logical request")
    void executesNoRetryExperimentWithOneDownstreamCallPerLogicalRequest() {
        dependencyGateway.willReturn(
                new DependencyCallResult(true, Duration.ofMillis(50)),
                new DependencyCallResult(false, Duration.ofMillis(50)),
                new DependencyCallResult(true, Duration.ofMillis(50)),
                new DependencyCallResult(true, Duration.ofMillis(50)));

        ExperimentResult result = useCase.run(new RunExperimentCommand(SCENARIO, ResilienceStrategy.NO_RETRY));

        assertThat(dependencyGateway.recordedCalls()).hasSize(4);
        assertThat(dependencyGateway.recordedCalls())
                .extracting(call -> call.logicalRequestId().value())
                .containsExactlyInAnyOrder(1, 2, 3, 4);
        assertThat(dependencyGateway.recordedCalls())
                .allSatisfy(call -> assertThat(call.attempt().value()).isOne());

        assertThat(result.measurements().logicalRequests()).isEqualTo(4);
        assertThat(result.measurements().downstreamAttempts()).isEqualTo(4);
        assertThat(result.measurements().successfulRequests()).isEqualTo(3);
        assertThat(result.measurements().failedRequests()).isEqualTo(1);
        assertThat(result.measurements().retryAttempts()).isZero();
        assertThat(result.measurements().shortCircuitedRequests()).isZero();
        assertThat(result.measurements().retryAmplificationFactor()).isEqualByComparingTo("1.0000");
        assertThat(result.measurements().successRate()).isEqualByComparingTo("0.7500");
        assertThat(result.measurements().averageLogicalLatency()).isEqualTo(Duration.ofMillis(50));
    }

    @Test
    @DisplayName("Rejects retry strategies until they are implemented explicitly")
    void rejectsRetryStrategiesUntilTheyAreImplementedExplicitly() {
        assertThatThrownBy(() -> useCase.run(new RunExperimentCommand(SCENARIO, ResilienceStrategy.IMMEDIATE_RETRY)))
                .isInstanceOf(UnsupportedResilienceStrategy.class)
                .hasMessage("strategy is not supported yet: IMMEDIATE_RETRY");
    }

    @Test
    @DisplayName("Honors the scenario concurrency level when dispatching dependency calls")
    void honorsScenarioConcurrencyLevel() {
        BlockingDependencyGateway dependencyGateway = new BlockingDependencyGateway(SCENARIO.concurrency().value());
        RunExperimentUseCase useCase = new RunExperimentUseCase(dependencyGateway);

        useCase.run(new RunExperimentCommand(SCENARIO, ResilienceStrategy.NO_RETRY));

        assertThat(dependencyGateway.maxConcurrentCalls()).isEqualTo(SCENARIO.concurrency().value());
    }

    private static class RecordingDependencyGateway implements DependencyGateway {

        private final List<DependencyCallResult> results = new ArrayList<>();
        private final List<DependencyCall> recordedCalls = Collections.synchronizedList(new ArrayList<>());
        private final AtomicInteger currentResultIndex = new AtomicInteger();

        void willReturn(DependencyCallResult... results) {
            this.results.addAll(List.of(results));
        }

        @Override
        public DependencyCallResult call(DependencyCall call) {
            recordedCalls.add(call);
            return results.get(currentResultIndex.getAndIncrement());
        }

        List<DependencyCall> recordedCalls() {
            return List.copyOf(recordedCalls);
        }
    }

    private static class BlockingDependencyGateway implements DependencyGateway {

        private final CountDownLatch firstConcurrentBatchStarted;
        private final AtomicInteger activeCalls = new AtomicInteger();
        private final AtomicInteger maxConcurrentCalls = new AtomicInteger();

        BlockingDependencyGateway(int expectedConcurrentCalls) {
            this.firstConcurrentBatchStarted = new CountDownLatch(expectedConcurrentCalls);
        }

        @Override
        public DependencyCallResult call(DependencyCall call) {
            int currentActiveCalls = activeCalls.incrementAndGet();
            maxConcurrentCalls.accumulateAndGet(currentActiveCalls, Math::max);
            firstConcurrentBatchStarted.countDown();

            try {
                assertThat(firstConcurrentBatchStarted.await(1, TimeUnit.SECONDS)).isTrue();
                return new DependencyCallResult(true, Duration.ofMillis(50));
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("test dependency call was interrupted", exception);
            } finally {
                activeCalls.decrementAndGet();
            }
        }

        int maxConcurrentCalls() {
            return maxConcurrentCalls.get();
        }
    }
}
