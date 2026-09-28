package com.lannyrivero.retrystorm.unstable.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lannyrivero.retrystorm.unstable.domain.service.DeterministicFailureModel;

@Configuration
public class DomainServiceConfiguration {

    @Bean
    DeterministicFailureModel deterministicFailureModel() {
        return new DeterministicFailureModel();
    }
}
