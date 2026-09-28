package com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web.dto;

public record DependencyResponse(int logicalRequestId, int attempt, boolean successful) {
}
