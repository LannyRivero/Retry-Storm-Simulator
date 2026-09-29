package com.lannyrivero.retrystorm.simulator.infrastructure.adapter.out.http;

import java.time.Duration;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCall;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyCallResult;
import com.lannyrivero.retrystorm.simulator.application.port.out.DependencyGateway;

/***
 * HTTP adapter that connects the simualtor to the unstable dependency service.
 * <p> {@code 503 Service Unavailable} response is treated as a valid simulated dependency failure.Other HTTP or 
 * client failure are transport failures and are allowed to propagate to the application use case.
 */

public class HttpDependencyGateway implements DependencyGateway {

    private final RestClient restClient;

    public HttpDependencyGateway(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DependencyCallResult call(DependencyCall call) {

        long startedAt = System.nanoTime();

        ResponseEntity<DependencyHttpResponse> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("api/dependency")
                        .queryParam("seed", call.seed().value())
                        .queryParam("logicalRequestId", call.logicalRequestId().value())
                        .queryParam("attempt", call.attempt().value())
                        .queryParam("failureRate", call.failureRate().value())
                        .queryParam("latencyMs", call.latency().duration().toMillis())
                        .build())
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.SERVICE_UNAVAILABLE.value(),
                        (request, httpResponse) -> {
                            // 503 is a valid dependency failure, not a transport failure

                        })

                .toEntity(DependencyHttpResponse.class);

        Duration latency = Duration.ofNanos(System.nanoTime() - startedAt);

        DependencyHttpResponse body = Objects.requireNonNull(response.getBody(),
                "dependency response body must not be null");

        boolean successful = response.getStatusCode().is2xxSuccessful() && body.successful();

        return new DependencyCallResult(successful, latency);

    }

}
