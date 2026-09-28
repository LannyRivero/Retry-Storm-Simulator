package com.lannyrivero.retrystorm.simulator.domain.service;

import java.time.Duration;

import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentMeasurements;

public class ExperimentMeasurementAccumulator {

    private int logicalRequests;
    private int downstreamAttempts;
    private int successfulRequests;
    private int failedRequests;
    private Duration totalLatency = Duration.ZERO;

    public void recordDownstreamCall(boolean successful, Duration latency) {
        logicalRequests++;
        downstreamAttempts++;
        totalLatency = totalLatency.plus(latency);

        if (successful) {
            successfulRequests++;
        } else {
            failedRequests++;
        }
    }

    public ExperimentMeasurements toMeasurements() {
        return new ExperimentMeasurements(
                logicalRequests,
                downstreamAttempts,
                successfulRequests,
                failedRequests,
                0,
                0,
                totalLatency);
    }
}
