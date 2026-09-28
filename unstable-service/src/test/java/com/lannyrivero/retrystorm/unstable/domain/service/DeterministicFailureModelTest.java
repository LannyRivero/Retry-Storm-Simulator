package com.lannyrivero.retrystorm.unstable.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.lannyrivero.retrystorm.unstable.domain.exception.InvalidFailureRate;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.LogicalRequestId;

class DeterministicFailureModelTest {

    private static final ExperimentSeed SEED = new ExperimentSeed(481516L);
    private static final LogicalRequestId FIRST_LOGICAL_REQUEST = new LogicalRequestId(1);
    private static final LogicalRequestId SAMPLE_LOGICAL_REQUEST = new LogicalRequestId(42);
    private static final AttemptNumber FIRST_ATTEMPT = new AttemptNumber(1);
    private static final AttemptNumber THIRD_ATTEMPT = new AttemptNumber(3);
    private static final FailureRate THIRTY_PERCENT_FAILURE_RATE = new FailureRate(0.30);

    private final DeterministicFailureModel failureModel = new DeterministicFailureModel();

    @Test
    void returnsSameDecisionForSameInputs() {
        boolean firstDecision = failureModel.shouldFail(
                SEED,
                SAMPLE_LOGICAL_REQUEST,
                FIRST_ATTEMPT,
                THIRTY_PERCENT_FAILURE_RATE);
        boolean secondDecision = failureModel.shouldFail(
                SEED,
                SAMPLE_LOGICAL_REQUEST,
                FIRST_ATTEMPT,
                THIRTY_PERCENT_FAILURE_RATE);

        assertThat(secondDecision).isEqualTo(firstDecision);
    }

    @Test
    void neverFailsWhenFailureRateIsZero() {
        assertThat(failureModel.shouldFail(SEED, FIRST_LOGICAL_REQUEST, FIRST_ATTEMPT, FailureRate.NEVER)).isFalse();
    }

    @Test
    void alwaysFailsWhenFailureRateIsOne() {
        assertThat(failureModel.shouldFail(SEED, FIRST_LOGICAL_REQUEST, FIRST_ATTEMPT, FailureRate.ALWAYS)).isTrue();
    }

    @Test
    void samplesRemainInsideUnitInterval() {
        double sample = failureModel.sample(SEED, new LogicalRequestId(100), THIRD_ATTEMPT);

        assertThat(sample).isGreaterThanOrEqualTo(0).isLessThan(1);
    }

    @Test
    void rejectsInvalidFailureRate() {
        assertThatThrownBy(() -> new FailureRate(1.1))
                .isInstanceOf(InvalidFailureRate.class)
                .hasMessage("failureRate must be between 0 and 1");
    }
}
