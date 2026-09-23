# ADR-0001: Use a Maven Multi-Module Repository

## Status

Accepted

## Context

Retry Storm Simulator has two distinct runtime responsibilities: the experiment simulator and the unstable downstream dependency. They belong to the same engineering lab and should evolve together, but they should not be collapsed into one runtime boundary.

Using separate repositories would add coordination and release overhead that does not help answer the experiment's core question.

## Decision

Use a single Git repository with a Maven parent project and two child modules:

- `simulator`
- `unstable-service`

The parent project owns shared build configuration such as the Java version and module aggregation.

## Alternatives Considered

### Separate repositories

Rejected for the MVP because it adds repository, versioning, and coordination overhead without improving the experiment.

### Single Spring Boot module

Rejected because it would weaken the runtime separation between the simulator and the unstable dependency.

## Consequences

### Positive

- One repository contains the complete reproducible experiment.
- Shared build configuration remains centralized.
- The two runtime components retain clear boundaries.
- Local development and CI remain simple.

### Negative

- The repository contains more than one deployable application.
- Module boundaries must be kept explicit to avoid accidental coupling.
