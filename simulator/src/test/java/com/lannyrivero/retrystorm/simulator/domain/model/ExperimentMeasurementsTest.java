package com.lannyrivero.retrystorm.simulator.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidExperimentMeasurements;

class ExperimentMeasurementsTest {

    private static final int LOGICAL_REQUESTS = 100;
    private static final int DOWNSTREAM_ATTEMPTS_WITH_RETRIES = 218;
    private static final int SUCCESSFUL_REQUESTS = 70;
    private static final int FAILED_REQUESTS = 30;
    private static final int RETRY_ATTEMPTS = 118;
    private static final int SHORT_CIRCUITED_REQUESTS = 0;
    private static final Duration TOTAL_LOGICAL_LATENCY = Duration.ofSeconds(50);
    private static final BigDecimal EXPECTED_RETRY_AMPLIFICATION_FACTOR = new BigDecimal("2.1800");
    private static final BigDecimal EXPECTED_SUCCESS_RATE = new BigDecimal("0.7000");

    private static final int NO_RETRY_LOGICAL_REQUESTS = 4;
    private static final int NO_RETRY_DOWNSTREAM_ATTEMPTS = 4;
    private static final int NO_RETRY_SUCCESSFUL_REQUESTS = 3;
    private static final int NO_RETRY_FAILED_REQUESTS = 1;
    private static final Duration TOTAL_LATENCY_FOR_AVERAGE_TEST = Duration.ofMillis(1_000);
    private static final Duration EXPECTED_AVERAGE_LATENCY = Duration.ofMillis(250);

    @Nested
    @DisplayName("Derived metrics")
    class DerivedMetrics {

        private final ExperimentMeasurements measurements = measurementsWithRetries();

        @Test
        @DisplayName("Calculates retry amplification factor from downstream attempts")
        void calculatesRetryAmplificationFactor() {
            assertThat(measurements.retryAmplificationFactor())
                    .isEqualByComparingTo(EXPECTED_RETRY_AMPLIFICATION_FACTOR);
        }

        @Test
        @DisplayName("Calculates success rate over logical requests")
        void calculatesSuccessRateOverLogicalRequests() {
            assertThat(measurements.successRate()).isEqualByComparingTo(EXPECTED_SUCCESS_RATE);
        }
    }

    @Nested
    @DisplayName("Latency")
    class LatencyMetrics {

        @Test
        @DisplayName("Calculates average logical latency")
        void calculatesAverageLogicalLatency() {
            ExperimentMeasurements measurements = noRetryMeasurements(TOTAL_LATENCY_FOR_AVERAGE_TEST);

            assertThat(measurements.averageLogicalLatency()).isEqualTo(EXPECTED_AVERAGE_LATENCY);
        }
    }

    @Nested
    @DisplayName("Invariants")
    class Invariants {

        @Test
        @DisplayName("Rejects inconsistent logical outcome counts")
        void rejectsInconsistentLogicalOutcomeCounts() {
            assertThatThrownBy(() -> new ExperimentMeasurements(
                    10,
                    10,
                    6,
                    3,
                    0,
                    0,
                    Duration.ZERO))
                .isInstanceOf(InvalidExperimentMeasurements.class)
                .hasMessage("successfulRequests + failedRequests must equal logicalRequests");
        }
    }

    private static ExperimentMeasurements measurementsWithRetries() {
        return new ExperimentMeasurements(
                LOGICAL_REQUESTS,
                DOWNSTREAM_ATTEMPTS_WITH_RETRIES,
                SUCCESSFUL_REQUESTS,
                FAILED_REQUESTS,
                RETRY_ATTEMPTS,
                SHORT_CIRCUITED_REQUESTS,
                TOTAL_LOGICAL_LATENCY);
    }

    private static ExperimentMeasurements noRetryMeasurements(Duration totalLogicalLatency) {
        return new ExperimentMeasurements(
                NO_RETRY_LOGICAL_REQUESTS,
                NO_RETRY_DOWNSTREAM_ATTEMPTS,
                NO_RETRY_SUCCESSFUL_REQUESTS,
                NO_RETRY_FAILED_REQUESTS,
                0,
                SHORT_CIRCUITED_REQUESTS,
                totalLogicalLatency);
    }
}
