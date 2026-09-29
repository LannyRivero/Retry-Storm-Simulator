package com.lannyrivero.retrystorm.simulator.infrastructure.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyGateway;
import com.lannyrivero.retrystorm.simulator.infrastructure.adapter.out.http.HttpDependencyGateway;

public class SimulatorDependencyConfigurationTest {

    private  static final String DEPENDENCY_BASE_URL_PROPERTY = "simulator.dependency.base.url=htttp://unstable-service";

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(SimulatorDependencyConfiguration.class)
            .withPropertyValues(DEPENDENCY_BASE_URL_PROPERTY);

    @Test 
    @DisplayName ("Creates HTTP dependency gateway from  configured base URL")
    void createdHttpDependencyGatewayFromConfiguredBaseUrl() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(DependencyGateway.class);
            assertThat(context.getBean(DependencyGateway.class)).isInstanceOf(HttpDependencyGateway.class);
        });
    }
}
