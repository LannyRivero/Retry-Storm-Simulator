package com.lannyrivero.retrystorm.simulator.infrastructure.adapter.in.console;

import java.time.Duration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.lannyrivero.retrystorm.simulator.application.command.RunExperimentCommand;
import com.lannyrivero.retrystorm.simulator.application.usecase.RunExperimentUseCase;
import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentResult;
import com.lannyrivero.retrystorm.simulator.domain.model.ExperimentScenario;
import com.lannyrivero.retrystorm.simulator.domain.model.ResilienceStrategy;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ConcurrencyLevel;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ExperimentSeed;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.FailureRate;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.Latency;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.LogicalRequestCount;
import com.lannyrivero.retrystorm.simulator.domain.valueobject.ScenarioName;
import com.lannyrivero.retrystorm.simulator.infrastructure.adapter.out.console.ConsoleExperimentReportPrinter;

@Component
public class NoRetryBaselineRunner implements CommandLineRunner {

    private static final ExperimentScenario SCENARIO = new ExperimentScenario(
            new ScenarioName("no-retry-baseline"),
            new LogicalRequestCount(100),
            new ConcurrencyLevel(10),
            new FailureRate(0.30),
            new Latency(Duration.ofMillis(50)),
            new ExperimentSeed(481516L));

    private final RunExperimentUseCase runExperimentUseCase;
    private final ConsoleExperimentReportPrinter reportPrinter;

    public NoRetryBaselineRunner(
            RunExperimentUseCase runExperimentUseCase,
            ConsoleExperimentReportPrinter reportPrinter) {
        this.runExperimentUseCase = runExperimentUseCase;
        this.reportPrinter = reportPrinter;
    }

    @Override
    public void run(String... args) throws Exception {
        ExperimentResult result = runExperimentUseCase.run(
                new RunExperimentCommand(SCENARIO, ResilienceStrategy.NO_RETRY));

        reportPrinter.print(result);
    }

}
