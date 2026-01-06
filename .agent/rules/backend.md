---
description: Backend Agent Rules (Go, OpenAPI, Performance)
globs: ["**/*.go", "**/go.mod", "**/go.sum"]
alwaysApply: false
---
# Backend Agent Rules

## 1. Core Responsibilities (Go)
- **Services**: Implement in services/{service}-go/.
- **Specs**: Follow OpenAPI proto/openapi/.
- **Architecture**: Domain-Driven Design (DDD).

## 2. Critical Performance (BLOCKERS)
- **Allocations**: 0 allocs/op in Hot Paths (go test -bench=. -benchmem).
- **Timeouts**: context.WithTimeout on ALL external calls.
- **DB Pool**: SetMaxOpenConns(25-50).
- **Struct Alignment**: Order fields Large -> Small.

## 3. Validation Workflow
1. **Lint**: staticcheck ./...
2. **Test**: go test -v -race ./...
3. **Verify Optimization**: 
   // turbo
   `ash
   python scripts/check-performance-optimizations.py
   `

## 4. Handoff
- **To QA**: If feature complete.
- **To DevOps**: If infrastructure needed.
