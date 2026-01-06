---
description: Workflow for validating backend optimizations before handoff in NECPGAME.
---
# Backend Optimization Validation Workflow

This workflow automates the validation process described in `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md`.

## 1. Context & Setup

1. **Identify Target Service**: Determine which service directory (e.g. `services/combat-service-go`) you are optimizing.
2. **Review Checklist**: Read `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` if not familiar.

## 2. Automated Validation

Run the provided validation scripts. These commands are safe to auto-run as they are read-only checks.

### 2.1 Domain & OpenAPI Validation

// turbo

```bash
python scripts/openapi/validate-domains-openapi.py
```

### 2.2 Migration Validation

// turbo

```bash
python scripts/migrations/validate-all-migrations.py
```

### 2.3 Code Analysis (Go)

Use `golangci-lint` or standard Go tools to check for issues.
// turbo

```bash
go vet ./...
```

// turbo

```bash
staticcheck ./...
```

## 3. Manual Checks (Performed by Agent)

You must manually verify the following critical optimizations in the code:

1. **Struct Field Alignment**: Check if fields are ordered by size.
2. **Context Deadlines**: Verify `context.WithTimeout` is used in external calls.
3. **DB Connection Pool**: Verify `SetMaxOpenConns` is set (usually 25-50).
4. **Structured Logging**: Ensure `zap` or similar JSON logger is used, NO `fmt.Println`.
5. **Memory Pooling**: For strict hot paths, check `sync.Pool`.

## 4. Performance Metrics (If applicable)

If benchmarks exist, run them:
// turbo

```bash
go test -bench=. -benchmem ./...
```

## 5. Report & Handoff

If all validations pass, proceed with handoff using the `necpgame_task_flow` workflow.
Ensure your handoff comment includes the optimization checklist status:

```markdown
[OK] Backend ready. Handed off to {NextAgent}

**Optimizations applied:**
- [x] Memory pooling
- [x] Batch DB queries
- [x] Context timeouts
...
```
