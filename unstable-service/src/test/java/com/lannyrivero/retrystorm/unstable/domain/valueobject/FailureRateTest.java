package com.lannyrivero.retrystorm.unstable.domain.valueobject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.lannyrivero.retrystorm.unstable.domain.exception.InvalidFailureRate;

class FailureRateTest {

    @Test
    void acceptsBoundaryValues() {
        assertThat(FailureRate.NEVER.value()).isZero();
        assertThat(FailureRate.ALWAYS.value()).isOne();
    }

    @Test
    void rejectsRatesBelowZero() {
        assertThatThrownBy(() -> new FailureRate(-0.1))
                .isInstanceOf(InvalidFailureRate.class)
                .hasMessage("failureRate must be between 0 and 1");
    }

    @Test
    void rejectsRatesAboveOne() {
        assertThatThrownBy(() -> new FailureRate(1.1))
                .isInstanceOf(InvalidFailureRate.class)
                .hasMessage("failureRate must be between 0 and 1");
    }

    @Test
    void rejectsNaN() {
        assertThatThrownBy(() -> new FailureRate(Double.NaN))
                .isInstanceOf(InvalidFailureRate.class)
                .hasMessage("failureRate must be between 0 and 1");
    }

    @Test
    void rejectsInfinity() {
        assertThatThrownBy(() -> new FailureRate(Double.POSITIVE_INFINITY))
                .isInstanceOf(InvalidFailureRate.class)
                .hasMessage("failureRate must be between 0 and 1");
    }
}
