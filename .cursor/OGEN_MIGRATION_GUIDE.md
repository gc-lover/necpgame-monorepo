# Ogen Migration Guide - Index

**Status:** APPROVED (PoC completed with 90% performance gains)  
**Related Issues:** #1590, #1578  
**Priority:** HIGH

---

## 📚 Guide Structure

This guide has been split into multiple files for better maintainability:

### [Part 1: Overview](ogen/01-OVERVIEW.md)
- Executive summary
- Benchmark results
- Service priority list
- Migration strategy
- Reference implementation

### [Part 2: Migration Steps](ogen/02-MIGRATION-STEPS.md)
- Complete migration checklist (7 phases)
- Code generation setup
- Handler migration guide
- Service layer updates
- Testing and deployment
- Performance validation

### [Part 3: Troubleshooting](ogen/03-TROUBLESHOOTING.md)
- Breaking changes overview
- Common issues and solutions
- Mistakes to avoid
- Pro tips
- Expected results

---

## ⚡ Quick Start

### For First-Time Migrators:

1. **Read** Part 1 (Overview) - understand why and what
2. **Follow** Part 2 (Steps) - step-by-step migration
3. **Reference** Part 3 (Troubleshooting) - when you hit issues
4. **Copy** Reference implementation: `services/combat-combos-service-ogen-go/`

### Time Estimate:
- **Simple service:** 4-6 hours
- **Complex service:** 8-12 hours
- **First migration:** Add 2-4 hours for learning

---

## 📊 Key Stats

**Performance vs oapi-codegen:**
- Latency: **90% faster** (191 ns/op vs 1994 ns/op)
- Memory: **95% less** (320 B/op vs 6528 B/op)
- Allocations: **80% fewer** (5 allocs/op vs 25 allocs/op)

**For entire backend (10 services × 10k RPS):**
- **620 MB/sec memory savings**
- **2M allocs/sec reduction**
- **Massive GC pressure relief**

---

## 🎯 Reference Implementation

**Location:** `services/combat-combos-service-ogen-go/`

**Key files:**
- `server/handlers.go` - Typed ogen handlers
- `server/service.go` - Service layer with OptX types
- `server/security.go` - SecurityHandler implementation
- `server/http_server.go` - ogen server setup
- `server/handlers_bench_test.go` - Benchmarks

**Use this as your template!**

---

## OK Success Criteria

**Service ready when:**
- [ ] Build passes (`go build ./...`)
- [ ] Tests pass (`go test ./...`)
- [ ] Benchmarks show >70% improvement
- [ ] All handlers use typed responses
- [ ] SecurityHandler implemented
- [ ] No `interface{}` in hot path
- [ ] PR created with benchmark results

---

## 📞 Related Documentation

- `.cursor/PROTOCOL_SELECTION_GUIDE.md` - When to use ogen vs protobuf
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - Performance validation
- `.cursor/CODE_GENERATION_TEMPLATE.md` - Makefile templates
- `.cursor/GO_BACKEND_PERFORMANCE_BIBLE.md` - Full optimization guide

---

**Start here:** [Part 1: Overview →](ogen/01-OVERVIEW.md)
