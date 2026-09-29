package com.lannyrivero.retrystorm.simulator.infrastructure.adapter.out.http;

public record DependencyHttpResponse(
    int logicalRequestId,
    int attempt,
    boolean successful
) {
    

}
