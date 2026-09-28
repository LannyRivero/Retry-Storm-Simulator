package com.lannyrivero.retrystorm.unstable.domain.service;

import com.lannyrivero.retrystorm.unstable.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.LogicalRequestId;

public class DeterministicFailureModel {

    private static final long LOGICAL_REQUEST_SALT = 0x9E3779B97F4A7C15L;
    private static final long ATTEMPT_SALT = 0xBF58476D1CE4E5B9L;
    private static final long FIRST_MIX_MULTIPLIER = 0xBF58476D1CE4E5B9L;
    private static final long SECOND_MIX_MULTIPLIER = 0x94D049BB133111EBL;
    private static final int FIRST_MIX_SHIFT = 30;
    private static final int SECOND_MIX_SHIFT = 27;
    private static final int FINAL_MIX_SHIFT = 31;
    private static final int DOUBLE_PRECISION_SHIFT = 11;

    public boolean shouldFail(
            ExperimentSeed seed,
            LogicalRequestId logicalRequestId,
            AttemptNumber attempt,
            FailureRate failureRate) {
        return sample(seed, logicalRequestId, attempt) < failureRate.value();
    }

    public double sample(ExperimentSeed seed, LogicalRequestId logicalRequestId, AttemptNumber attempt) {
        long value = seed.value();
        value ^= ((long) logicalRequestId.value()) * LOGICAL_REQUEST_SALT;
        value ^= ((long) attempt.value()) * ATTEMPT_SALT;

        long mixed = mix(value);
        return (mixed >>> DOUBLE_PRECISION_SHIFT) * 0x1.0p-53;
    }

    private long mix(long value) {
        value = (value ^ (value >>> FIRST_MIX_SHIFT)) * FIRST_MIX_MULTIPLIER;
        value = (value ^ (value >>> SECOND_MIX_SHIFT)) * SECOND_MIX_MULTIPLIER;
        return value ^ (value >>> FINAL_MIX_SHIFT);
    }
}
