package com.lannyrivero.retrystorm.simulator.application.port.out;

public interface DependencyGateway {

    DependencyCallResult call(DependencyCall call);
}
