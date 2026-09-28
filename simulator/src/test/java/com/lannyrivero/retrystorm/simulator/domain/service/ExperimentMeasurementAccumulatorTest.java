package com.lannyrivero.retrystorm.simulator.domain.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentMeasurements;

class ExperimentMeasurementAccumulatorTest {

    private static final Duration SUCCESSFUL_REQUEST_LATENCY = Duration.ofMillis(100);
    private static final Duration FAILED_REQUEST_LATENCY = Duration.ofMillis(200);
    private static final Duration SECOND_SUCCESSFUL_REQUEST_LATENCY = Duration.ofMillis(300);
    private static final int EXPECTED_LOGICAL_REQUESTS = 3;
    private static final int EXPECTED_DOWNSTREAM_ATTEMPTS = 3;
    private static final int EXPECTED_SUCCESSFUL_REQUESTS = 2;
    private static final int EXPECTED_FAILED_REQUESTS = 1;
    private static final Duration EXPECTED_TOTAL_LATENCY = Duration.ofMillis(600);

    private ExperimentMeasurementAccumulator accumulator;

    @BeforeEach
    void setUp() {
        accumulator = new ExperimentMeasurementAccumulator();
    }

    @Test
    @DisplayName("Accumulates no-retry downstream calls into experiment measurements")
    void accumulatesNoRetryDownstreamCalls() {
        accumulator.recordDownstreamCall(true, SUCCESSFUL_REQUEST_LATENCY);
        accumulator.recordDownstreamCall(false, FAILED_REQUEST_LATENCY);
        accumulator.recordDownstreamCall(true, SECOND_SUCCESSFUL_REQUEST_LATENCY);

        ExperimentMeasurements measurements = accumulator.toMeasurements();

        assertThat(measurements.logicalRequests()).isEqualTo(EXPECTED_LOGICAL_REQUESTS);
        assertThat(measurements.downstreamAttempts()).isEqualTo(EXPECTED_DOWNSTREAM_ATTEMPTS);
        assertThat(measurements.successfulRequests()).isEqualTo(EXPECTED_SUCCESSFUL_REQUESTS);
        assertThat(measurements.failedRequests()).isEqualTo(EXPECTED_FAILED_REQUESTS);
        assertThat(measurements.retryAttempts()).isZero();
        assertThat(measurements.shortCircuitedRequests()).isZero();
        assertThat(measurements.totalLogicalLatency()).isEqualTo(EXPECTED_TOTAL_LATENCY);
    }
}
