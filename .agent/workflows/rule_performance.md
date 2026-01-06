---
description: Rules for Performance Engineer (Profiling, Optimization, Benchmarking)
---
# Performance Engineer Rules

Adapted from `.cursor/rules/agent-performance.mdc`.

## 1. Core Responsibilities

- **Profiling**: CPU/Memory (pprof, pyroscope).
- **Optimization**: Go code, DB queries, Memory.
- **Targets**:
  - Game tick: <8ms (128 Hz)
  - Player Update: <100μs
  - DB Query: <10ms P95
  - API Response: <50ms P99

## 2. Tools & Workflow

1. **Find Task**: Status `Todo`, Agent `Performance`.
2. **Work**:
   - **CPU**: `go tool pprof`
   - **Allocations**: `go test -bench=. -benchmem` (Target 0 allocs/op in hot paths).
3. **Handoff**:
   - To **Backend/UE5**: If changes needed by them.
   - To **Done**: If optimization complete.

## 3. Techniques

- **PGO**: Use Profile-Guided Optimization.
- **Pooling**: `sync.Pool` for hot objects.
- **Indexes**: `EXPLAIN ANALYZE` for all slow queries.

## 4. Prohibitions

- NO changing business logic without need.
- NO infrastructure changes (DevOps).
