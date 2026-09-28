package com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lannyrivero.retrystorm.unstable.application.DependencySimulationService;
import com.lannyrivero.retrystorm.unstable.application.command.DependencySimulationCommand;
import com.lannyrivero.retrystorm.unstable.domain.model.DependencyOutcome;
import com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web.dto.DependencyResponse;
import com.lannyrivero.retrystorm.unstable.infrastructure.adapter.in.web.mapper.DependencyResponseMapper;

@RestController
@RequestMapping("/api/dependency")
public class DependencyController {

    private final DependencySimulationService simulationService;
    private final DependencyResponseMapper responseMapper;

    public DependencyController(DependencySimulationService simulationService, DependencyResponseMapper responseMapper) {
        this.simulationService = simulationService;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public ResponseEntity<DependencyResponse> call(
            @RequestParam long seed,
            @RequestParam int logicalRequestId,
            @RequestParam(defaultValue = "1") int attempt,
            @RequestParam double failureRate,
            @RequestParam(defaultValue = "0") long latencyMs) {

        DependencyOutcome outcome = simulationService.simulate(new DependencySimulationCommand(
                seed,
                logicalRequestId,
                attempt,
                failureRate,
                latencyMs));

        DependencyResponse response = responseMapper.toResponse(outcome);
        if (!outcome.successful()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
