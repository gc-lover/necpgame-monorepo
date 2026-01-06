---
description: Optimized Rules for Backend Agent (Go, OpenAPI, Performance)
---
# Backend Agent Rules

Adapted from `.cursor/rules/agent-backend.mdc`.

## 1. Core Responsibilities

- **Implementation**: Write Go services based on OpenAPI specs.
- **Architecture**: Use Enterprise-Grade Domain structure (SOLID/DRY).
- **Performance**: Enforce strict performance requirements (allocation-free hot paths).

## 2. Code Generation (ogen)

New services MUST use `ogen` (JSON REST).
Existing services using `oapi-codegen` should be migrated.

### Workflow

1. **Validate OpenAPI**:
   // turbo

   ```bash
   python scripts/validate-domains-openapi.py --domain <DOMAIN>
   ```

2. **Optimize Structs**:
   // turbo

   ```bash
   python scripts/batch-optimize-openapi-struct-alignment.py proto/openapi/<DOMAIN>/main.yaml
   ```

3. **Generate**:
   // turbo

   ```bash
   python scripts/generation/enhanced_service_generator.py --spec proto/openapi/<DOMAIN>/main.yaml
   ```

## 3. Critical Performance Rules (BLOCKERS)

- **Context Timeouts**: ALL external calls (DB, API) must have `context.WithTimeout`.
- **DB Pool**: `SetMaxOpenConns` must be 25-50.
- **No Goroutine Leaks**: Verify with `go test -v -run TestMain`.
- **Struct Alignment**: Fields ordered larged-to-small (use `fieldalignment`).

## 4. Handoff Checklist

Before passing to QA/Network:

1. Run validation: `/backend-validate-optimizations #123` (or equivalent script).
2. Ensure 0 allocs/op in hot paths (`go test -bench=. -benchmem`).
3. Comment with optimization stats.
