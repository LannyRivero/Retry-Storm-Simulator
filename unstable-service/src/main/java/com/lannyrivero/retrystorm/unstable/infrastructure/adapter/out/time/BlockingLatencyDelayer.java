package com.lannyrivero.retrystorm.unstable.infrastructure.adapter.out.time;

import org.springframework.stereotype.Component;

import com.lannyrivero.retrystorm.unstable.application.port.out.LatencyDelayer;

@Component
public class BlockingLatencyDelayer implements LatencyDelayer {

    @Override
    public void delay(long latencyMs) {
        try {
            Thread.sleep(latencyMs);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Latency simulation was interrupted", exception);
        }
    }
}
