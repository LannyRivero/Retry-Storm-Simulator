package com.lannyrivero.retrystorm.simulator.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import com.lannyrivero.retrystorm.simulator.domain.exception.InvalidExperimentMeasurements;

public record ExperimentMeasurements(
        int logicalRequests,
        int downstreamAttempts,
        int successfulRequests,
        int failedRequests,
        int retryAttempts,
        int shortCircuitedRequests,
        Duration totalLogicalLatency) {

    private static final int METRIC_SCALE = 4;

    public ExperimentMeasurements {
        if (logicalRequests <= 0) {
            throw new InvalidExperimentMeasurements("logicalRequests must be positive");
        }
        if (downstreamAttempts < 0 || successfulRequests < 0 || failedRequests < 0
                || retryAttempts < 0 || shortCircuitedRequests < 0) {
            throw new InvalidExperimentMeasurements("metrics must not be negative");
        }
        if (successfulRequests + failedRequests != logicalRequests) {
            throw new InvalidExperimentMeasurements("successfulRequests + failedRequests must equal logicalRequests");
        }
        if (totalLogicalLatency == null || totalLogicalLatency.isNegative()) {
            throw new InvalidExperimentMeasurements("totalLogicalLatency must be zero or positive");
        }
    }

    public BigDecimal retryAmplificationFactor() {
        return divide(downstreamAttempts, logicalRequests);
    }

    public BigDecimal successRate() {
        return divide(successfulRequests, logicalRequests);
    }

    public Duration averageLogicalLatency() {
        return totalLogicalLatency.dividedBy(logicalRequests);
    }

    private BigDecimal divide(int numerator, int denominator) {
        return BigDecimal.valueOf(numerator)
                .divide(BigDecimal.valueOf(denominator), METRIC_SCALE, RoundingMode.HALF_UP);
    }
}
