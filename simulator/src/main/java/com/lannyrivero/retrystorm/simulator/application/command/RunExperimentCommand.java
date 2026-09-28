package com.lannyrivero.retrystorm.simulator.application.command;

import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentScenario;
import com.lannyrivero.retrystorm.simulator.domain.model.ResilienceStrategy;

public record RunExperimentCommand(ExperimentScenario scenario, ResilienceStrategy strategy) {
}
