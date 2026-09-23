# ADR-0003: Use Spring RestClient for Downstream HTTP Calls

## Status

Accepted

## Context

The simulator needs a synchronous HTTP client to call the unstable dependency. The experiment focuses on retry timing, downstream call amplification, latency, and circuit breaking rather than reactive programming.

The chosen client should integrate naturally with Spring, remain easy to test, and avoid unnecessary reactive complexity.

## Decision

Use Spring Framework `RestClient` as the simulator's HTTP client abstraction at the infrastructure boundary.

The domain and application layers must depend on an outbound port rather than on `RestClient` directly.

## Alternatives Considered

### WebClient

Rejected for the MVP because reactive execution would introduce concurrency and scheduling semantics that are not required to answer the experiment's core questions.

### RestTemplate

Rejected because `RestClient` is the modern synchronous Spring HTTP client API.

### OpenFeign

Rejected because declarative client generation adds abstraction without providing value for this small experimental boundary.

## Consequences

### Positive

- Simple synchronous call model.
- Clear mapping between one physical attempt and one HTTP invocation.
- Easy integration with Spring Boot and tests.
- HTTP concerns remain isolated in an outbound adapter.

### Negative

- The client is blocking by design.
- A future reactive experiment would require a separate design decision rather than reusing this execution model unchanged.
