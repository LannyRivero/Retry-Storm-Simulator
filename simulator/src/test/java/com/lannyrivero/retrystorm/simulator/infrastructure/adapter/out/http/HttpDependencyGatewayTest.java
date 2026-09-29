package com.lannyrivero.retrystorm.simulator.infrastructure.adapter.out.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServiceUnavailable;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCall;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCallResult;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.AttemptNumber;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.LogicalRequestId;

public class HttpDependencyGatewayTest {

    private static final String BASE_URL = "http://unstable-service";
    private static final String DEPENDENCY_URL = BASE_URL
            + "/api/dependency?seed=481516&logicalRequestId=7&attempt=1&failureRate=0.3&latencyMs=50";

    private MockRestServiceServer server;
    private HttpDependencyGateway gateway;

    @BeforeEach
    void setUp() {
        RestClient.Builder restClientBuilder = RestClient.builder().baseUrl(BASE_URL);
        server = MockRestServiceServer.bindTo(restClientBuilder).build();
        gateway = new HttpDependencyGateway(restClientBuilder.build());

    }

    @Test
    @DisplayName("Maps successful dependency responses to successful call results")
    void mapsSuccessfulDependencyResponsesToSuccessfulCallResults() {
        server.expect(once(), requestTo(DEPENDENCY_URL))
                .andExpect(method(GET))
                .andRespond(withSuccess("""
                        {
                            "logicalRequestId": 7,
                            "attempt": 1,
                            "successful": true
                        }
                            """, MediaType.APPLICATION_JSON));
        DependencyCallResult result = gateway.call(dependencyCall());
        assertThat(result.successful()).isTrue();
        assertThat(result.latency()).isPositive();
        server.verify();
    }

    @Test
    @DisplayName("Maps 503 dependency response to failed call result")
    void mapsServiceUnavailableDependencyResponseToFailedCallResult() {
        server.expect(once(), requestTo(DEPENDENCY_URL))
                .andExpect(method(GET))
                .andRespond(withServiceUnavailable()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("""
                                {
                                    "logicalRequestId": 7,
                                    "attempt": 1,
                                    "successful": false
                                }
                                """));
        DependencyCallResult result = gateway.call(dependencyCall());

        assertThat(result.successful()).isFalse();
        assertThat(result.latency()).isPositive();
        server.verify();
    }

    @Test
    @DisplayName("Propagates unexpected HTTP errors as transport failures")
    void propagatesUnexpectedHttpErrorsAsTransportFailures() {
        server.expect(once(), requestTo(DEPENDENCY_URL))
                .andExpect(method(GET))
                .andRespond(withServerError());

        assertThatThrownBy(() -> gateway.call(dependencyCall()))
                .isInstanceOf(RestClientException.class);
        server.verify();

    }

    private static DependencyCall dependencyCall() {
        return new DependencyCall(
                new ExperimentSeed(481516L),
                new LogicalRequestId(7),
                new AttemptNumber(1),
                new FailureRate(0.30),
                new Latency(Duration.ofMillis(50)));
    }
}
