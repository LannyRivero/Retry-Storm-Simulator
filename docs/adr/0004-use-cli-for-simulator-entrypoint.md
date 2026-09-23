# ADR-0004: Use a CLI as the Simulator Entry Point

## Status

Accepted

## Context

The simulator is an engineering experiment runner, not a user-facing web application. Its primary responsibility is to execute reproducible scenarios with explicit configuration and report results.

Adding a REST API to trigger experiments would introduce controllers, transport DTOs, endpoint lifecycle concerns, and API design that do not contribute to the MVP's core question.

## Decision

Expose the simulator through a command-line entry point using Spring Boot in non-web mode.

The CLI adapter will invoke an application use case. Domain and application code must remain independent from Spring Boot's runner interfaces.

## Alternatives Considered

### REST API

Rejected for the MVP because it adds an unnecessary inbound transport layer for a reproducible local engineering experiment.

### Interactive terminal UI

Rejected because interaction is not required; deterministic invocation is more valuable.

## Consequences

### Positive

- Experiments can be executed with explicit, reproducible arguments.
- No unnecessary web server is started by the simulator.
- The inbound adapter remains thin.

### Negative

- Remote execution is not provided by the MVP.
- A future API would require a new inbound adapter.
