package com.lannyrivero.retrystorm.simulator.application.port.out;

import com.lannyrivero.retrystorm.simulator.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.LogicalRequestId;


/**
 * Request sent througt the dependecy gateway for one downstream attempt.
 * 
 * <p> The values are derived from the experiment scenario plus the current logical request and attempt number
 * DependencyCall
 * @param seed
 * @param logicalRequestId
 * @param attempt
 * @param failureRate
 * @param latency
 */

public record DependencyCall(
        ExperimentSeed seed,
        LogicalRequestId logicalRequestId,
        AttemptNumber attempt,
        FailureRate failureRate,
        Latency latency) {
}
