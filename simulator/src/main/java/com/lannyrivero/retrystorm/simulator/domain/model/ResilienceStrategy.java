package com.lannyrivero.retrystorm.simulator.domain.model;

public enum ResilienceStrategy {
    NO_RETRY,
    IMMEDIATE_RETRY,
    EXPONENTIAL_BACKOFF,
    BACKOFF_WITH_JITTER,
    CIRCUIT_BREAKER
}
