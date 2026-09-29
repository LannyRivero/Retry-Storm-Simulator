package com.lannyrivero.retrystorm.simulator.infrastructure.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyGateway;
import com.lannyrivero.retrystorm.simulator.infrastructure.adapter.out.http.HttpDependencyGateway;

@Configuration 
public class SimulatorDependencyConfiguration {
    @Bean 
    RestClient dependencyRestClient(@Value("${simulator.dependency.base-url}") String dependencyBaseUrl) {
        return  RestClient.builder()
                .baseUrl(dependencyBaseUrl)
                .build();
    }

    @Bean 
    DependencyGateway dependencyGateway(RestClient dependencyRestClient){
        return new HttpDependencyGateway(dependencyRestClient);
    }

}
