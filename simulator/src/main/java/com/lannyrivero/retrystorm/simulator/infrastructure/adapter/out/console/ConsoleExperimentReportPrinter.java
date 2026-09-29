package com.lannyrivero.retrystorm.simulator.infrastructure.adapter.out.console;

import org.springframework.stereotype.Component;

import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentMeasurements;
import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentResult;

@Component
public class ConsoleExperimentReportPrinter {

    public void print(ExperimentResult result) {

        ExperimentMeasurements measurements = result.measurements();

        System.out.println();
        System.out.println("=== Retry Storm Simulator ===");
        System.out.println("Scenario: " + result.scenario().name().value());
        System.out.println("Strategy: " + result.strategy());
        System.out.println();
        System.out.println("Logical requests: " + measurements.logicalRequests());
        System.out.println("Downstream attempts: " + measurements.downstreamAttempts());
        System.out.println("Successful requests: " + measurements.successfulRequests());
        System.out.println("Failed requests: " + measurements.failedRequests());
        System.out.println("Retry attempts: " + measurements.retryAttempts());
        System.out.println("Short-circuited requests: " + measurements.shortCircuitedRequests());
        System.out.println();
        System.out.println("Retry amplification factor: " + measurements.retryAmplificationFactor());
        System.out.println("Success rate: " + measurements.successRate());
        System.out.println("Average logical latency: " + measurements.averageLogicalLatency().toMillis() + "ms");
        System.out.println("================================");
        System.out.println();

    }

}
