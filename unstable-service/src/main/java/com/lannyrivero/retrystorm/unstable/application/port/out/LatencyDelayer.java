package com.lannyrivero.retrystorm.unstable.application.port.out;

public interface LatencyDelayer {

    void delay(long latencyMs);
}
