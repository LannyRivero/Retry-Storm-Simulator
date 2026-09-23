# ADR-0006: Use Programmatic Resilience Policies

## Status

Accepted

## Context

The purpose of Retry Storm Simulator is to make resilience behavior observable and explainable. Annotation-driven resilience can hide decorator ordering and attempt boundaries behind framework aspects.

The experiment needs explicit control over how retry and circuit breaker policies wrap downstream calls and how events are measured.

## Decision

Use Resilience4j programmatically at the application/infrastructure boundary instead of relying on Spring annotations for the MVP.

Domain types must not depend on Resilience4j classes.

## Alternatives Considered

### Spring annotations such as `@Retry` and `@CircuitBreaker`

Rejected for the MVP because they obscure composition order and make the experiment less explicit.

### Custom resilience implementation

Rejected because the goal is to study resilience behavior, not reimplement production-grade retry and circuit breaker libraries.

## Consequences

### Positive

- Retry and circuit-breaker composition remains explicit in code.
- Metrics and events can be tied to known execution boundaries.
- Domain code stays independent from the resilience library.

### Negative

- More configuration code is required than with annotations.
- Decorator composition must be designed and tested deliberately.
