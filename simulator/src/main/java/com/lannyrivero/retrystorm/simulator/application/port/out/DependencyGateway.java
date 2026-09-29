package com.lannyrivero.retrystorm.simulator.application.port.out;

/**
 * Outbound port used by the simulator to call the dependency under test.
 * 
 * <p> The application layer depends on this abstraction so experiment execution
 * stays independent from the concrete transport, such as HTTP.
 */

public interface DependencyGateway {

    DependencyCallResult call(DependencyCall call);
}
