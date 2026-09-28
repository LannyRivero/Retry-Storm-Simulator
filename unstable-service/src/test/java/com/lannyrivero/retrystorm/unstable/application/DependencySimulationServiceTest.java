package com.lannyrivero.retrystorm.unstable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.lannyrivero.retrystorm.unstable.application.command.DependencySimulationCommand;
import com.lannyrivero.retrystorm.unstable.application.exception.InvalidDependencySimulationRequest;
import com.lannyrivero.retrystorm.unstable.application.port.out.LatencyDelayer;
import com.lannyrivero.retrystorm.unstable.domain.model.DependencyOutcome;
import com.lannyrivero.retrystorm.unstable.domain.service.DeterministicFailureModel;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.LogicalRequestId;

class DependencySimulationServiceTest {

    private static final long SEED = 481516L;
    private static final int FIRST_LOGICAL_REQUEST = 1;
    private static final int FIRST_ATTEMPT = 1;
    private static final double ZERO_FAILURE_RATE = 0;
    private static final double TOTAL_FAILURE_RATE = 1;
    private static final Latency SMALL_LATENCY = new Latency(25);

    private final RecordingLatencyDelayer latencyDelayer = new RecordingLatencyDelayer();
    private final DependencySimulationService service = new DependencySimulationService(
            new DeterministicFailureModel(),
            latencyDelayer);

    @Test
    void simulatesSuccessfulDependencyCall() {
        DependencyOutcome outcome = service.simulate(new DependencySimulationCommand(
                SEED,
                FIRST_LOGICAL_REQUEST,
                FIRST_ATTEMPT,
                ZERO_FAILURE_RATE,
                SMALL_LATENCY.milliseconds()));

        assertThat(outcome).isEqualTo(new DependencyOutcome(
                new LogicalRequestId(FIRST_LOGICAL_REQUEST),
                new AttemptNumber(FIRST_ATTEMPT),
                true));
        assertThat(latencyDelayer.recordedLatencies()).containsExactly(SMALL_LATENCY.milliseconds());
    }

    @Test
    void simulatesFailedDependencyCall() {
        DependencyOutcome outcome = service.simulate(new DependencySimulationCommand(
                SEED,
                FIRST_LOGICAL_REQUEST,
                FIRST_ATTEMPT,
                TOTAL_FAILURE_RATE,
                Latency.NONE.milliseconds()));

        assertThat(outcome.successful()).isFalse();
    }

    @Test
    void rejectsInvalidCommandBeforeDelaying() {
        assertThatThrownBy(() -> service.simulate(new DependencySimulationCommand(SEED, 0, FIRST_ATTEMPT, 0.30, 25)))
                .isInstanceOf(InvalidDependencySimulationRequest.class)
                .hasMessage("logicalRequestId must be positive");

        assertThat(latencyDelayer.recordedLatencies()).isEmpty();
    }

    private static class RecordingLatencyDelayer implements LatencyDelayer {

        private final List<Long> recordedLatencies = new ArrayList<>();

        @Override
        public void delay(long latencyMs) {
            recordedLatencies.add(latencyMs);
        }

        List<Long> recordedLatencies() {
            return recordedLatencies;
        }
    }
}
