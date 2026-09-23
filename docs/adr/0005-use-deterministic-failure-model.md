# ADR-0005: Use a Deterministic Failure Model

## Status

Accepted

## Context

The simulator must support reproducible experiments. A single seeded pseudo-random generator is not sufficient when requests execute concurrently because thread scheduling can change the order in which random values are consumed.

Comparing resilience strategies requires the same logical request and attempt to receive the same failure outcome for the same experiment seed, regardless of scheduling order.

## Decision

Derive each failure decision deterministically from stable experiment inputs such as:

- experiment seed
- logical request identifier
- attempt number

The failure decision must not depend on shared mutable random state or thread execution order.

## Alternatives Considered

### Shared seeded Random

Rejected because concurrent execution can consume values in different orders and break reproducibility.

### Non-seeded randomness

Rejected because results could not be reproduced reliably.

### Pre-generated failure sequence

Viable, but rejected for the MVP because it introduces additional storage and coordination that is unnecessary if the outcome can be derived deterministically.

## Consequences

### Positive

- Same inputs produce the same failure outcome.
- Different resilience strategies can be compared against stable failure decisions.
- Tests avoid probabilistic flakiness.

### Negative

- The deterministic algorithm becomes part of the experimental contract and must remain documented.
- Care is required to ensure the mapping preserves the configured failure-rate semantics.
