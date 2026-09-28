package com.lannyrivero.retrystorm.simulator.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidConcurrencyLevel;
import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidFailureRate;
import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidLatency;
import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidLogicalRequestCount;
import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidScenarioName;

class ExperimentScenarioValueObjectTest {

    @Nested
    @DisplayName("Scenario names")
    class ScenarioNames {

        @Test
        @DisplayName("Trims scenario names")
        void trimsScenarioName() {
            assertThat(new ScenarioName(" degraded ").value()).isEqualTo("degraded");
        }

        @Test
        @DisplayName("Rejects blank scenario names")
        void rejectsBlankScenarioName() {
            assertThatThrownBy(() -> new ScenarioName(" "))
                    .isInstanceOf(InvalidScenarioName.class)
                    .hasMessage("scenario name must not be blank");
        }
    }

    @Nested
    @DisplayName("Scenario sizing")
    class ScenarioSizing {

        @Test
        @DisplayName("Rejects invalid logical request counts")
        void rejectsInvalidLogicalRequestCount() {
            assertThatThrownBy(() -> new LogicalRequestCount(0))
                    .isInstanceOf(InvalidLogicalRequestCount.class)
                    .hasMessage("logicalRequests must be positive");
        }

        @Test
        @DisplayName("Rejects invalid concurrency levels")
        void rejectsInvalidConcurrencyLevel() {
            assertThatThrownBy(() -> new ConcurrencyLevel(0))
                    .isInstanceOf(InvalidConcurrencyLevel.class)
                    .hasMessage("concurrency must be positive");
        }
    }

    @Nested
    @DisplayName("Dependency behavior")
    class DependencyBehavior {

        @Test
        @DisplayName("Rejects non-finite failure rates")
        void rejectsNonFiniteFailureRate() {
            assertThatThrownBy(() -> new FailureRate(Double.NaN))
                    .isInstanceOf(InvalidFailureRate.class)
                    .hasMessage("failureRate must be between 0 and 1");
        }

        @Test
        @DisplayName("Rejects negative latencies")
        void rejectsNegativeLatency() {
            assertThatThrownBy(() -> new Latency(Duration.ofMillis(-1)))
                    .isInstanceOf(InvalidLatency.class)
                    .hasMessage("latencyMs must be greater than or equal to 0");
        }
    }
}
