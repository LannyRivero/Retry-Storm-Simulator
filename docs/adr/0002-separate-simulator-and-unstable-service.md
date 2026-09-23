# ADR-0002: Separate Simulator and Unstable Service Runtime Boundaries

## Status

Accepted

## Context

The experiment studies how a client-side resilience policy affects a degraded downstream dependency. If both sides share the same Spring Boot application, JVM, server resources, and lifecycle, the experiment would blur the client/dependency boundary.

The project does not require independently managed microservices, service discovery, an API gateway, or Kubernetes.

## Decision

Run the simulator and unstable dependency as two separate Spring Boot applications connected through a real HTTP boundary.

Both applications remain in the same repository and Maven reactor.

## Alternatives Considered

### One Spring Boot application

Rejected because it would make the dependency an in-process concern and weaken the realism of downstream HTTP attempts.

### Two independently managed repositories/services

Rejected because the operational complexity does not contribute to the experiment.

## Consequences

### Positive

- HTTP attempts are real network calls between separate application runtimes.
- Metrics such as downstream attempts correspond to actual calls crossing the boundary.
- Failure behavior remains isolated from the simulator runtime.

### Negative

- Local execution requires starting two applications.
- Integration tests need to coordinate two runtime boundaries when full end-to-end coverage is required.
