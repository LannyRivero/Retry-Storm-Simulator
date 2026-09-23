# Retry Storm Simulator

> Engineering lab to study retry amplification, backoff, jitter, and circuit breaker behavior in degraded distributed systems.

**Status:** 🚧 In development — project foundation

## Purpose

Retry Storm Simulator is a reproducible engineering experiment built with Java and Spring Boot. Its goal is to measure when retry strategies improve resilience and when they amplify load against a degraded downstream dependency.

The project focuses on observable behavior rather than framework annotations. The MVP will compare strategies such as no retry, immediate retry, exponential backoff, jitter, and circuit breaker using reproducible scenarios and measurable outcomes.

## Repository structure

```text
retry-storm-simulator/
├── simulator/          # Experiment runner and resilience client
├── unstable-service/   # Configurable degraded HTTP dependency
├── docs/
│   └── adr/            # Architecture Decision Records
└── pom.xml              # Maven multi-module parent
```

## Technical baseline

- Java 21
- Spring Boot 4.1.1
- Maven multi-module build
- Hexagonal Architecture
- DDD applied pragmatically to the experiment domain
- Real HTTP boundary between simulator and unstable dependency

## Current phase

The project is currently establishing its technical foundation. No retry or circuit-breaker behavior is implemented yet.

Initial architecture decisions are documented under [`docs/adr`](docs/adr).

## Planned MVP

1. Baseline experiment with no retry
2. Immediate retry and Retry Amplification Factor (RAF)
3. Exponential backoff
4. Backoff with jitter
5. Circuit breaker
6. Recovery scenario
7. Reproducible comparison and analysis

## Scope discipline

The MVP intentionally excludes unrelated infrastructure such as databases, messaging systems, authentication, frontend applications, Kubernetes, and full observability stacks. New features must contribute directly to understanding retry amplification or failure control.

## License

This project is licensed under the MIT License.
