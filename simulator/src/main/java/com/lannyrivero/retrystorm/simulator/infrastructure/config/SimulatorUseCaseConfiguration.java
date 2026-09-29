package com.lannyrivero.retrystorm.simulator.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyGateway;
import com.lannyrivero.retrystorm.simulator.application.usecase.RunExperimentUseCase;

@Configuration 
public class SimulatorUseCaseConfiguration {

    @Bean 
    RunExperimentUseCase runExperimentUseCase(DependencyGateway dependencyGateway) {
        return new RunExperimentUseCase(dependencyGateway);
    }

}
