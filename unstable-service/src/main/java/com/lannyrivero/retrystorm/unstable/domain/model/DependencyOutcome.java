package com.lannyrivero.retrystorm.unstable.domain.model;

import com.lannyrivero.retrystorm.unstable.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.unstable.domain.valueobject.LogicalRequestId;

public record DependencyOutcome(LogicalRequestId logicalRequestId, AttemptNumber attempt, boolean successful) {
}
