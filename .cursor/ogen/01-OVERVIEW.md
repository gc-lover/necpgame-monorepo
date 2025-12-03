# Ogen Migration Guide - Part 1: Overview

**Status:** APPROVED (PoC completed with 90% performance gains)  
**Related Issues:** #1590, #1578  
**Priority:** HIGH

---

## 🎯 Executive Summary

**ogen - стандарт для Go OpenAPI генерации**

**Benchmark (combat-combos-service):**
- Latency: 191 ns/op
- Memory: 320 B/op  
- Allocations: 5 allocs/op

**Why ogen:**
- Typed responses (no `interface{}` boxing)
- Zero reflection in hot path
- Built-in performance (go-faster/jx)
- OpenTelemetry integration

---

## 📊 Benchmark Results (Proven)

**Serialization Performance:**

| Protocol | Marshal | Unmarshal | Size | Allocations |
|----------|---------|-----------|------|-------------|
| **ogen (JSON)** | 364 ns/op | 1878 ns/op | 255 bytes | 2/14 allocs |
| **Protobuf** | 145 ns/op | 252 ns/op | 123 bytes | 1/8 allocs |
| **oapi-codegen** | ~2000 ns/op | ~5000 ns/op | 255 bytes | 25 allocs |

**ogen vs oapi-codegen gains:**
- Latency: **90% faster**
- Memory: **95% less**
- Allocations: **80% fewer**

---

## 🎮 Service Priority (Hot Path First)

**BLOCKER (Critical hot path - do FIRST):**
1. `matchmaking-service` - 5k RPS, P99 <50ms
2. `realtime-gateway` - WebSocket/UDP, 10k concurrent
3. `inventory-service` - 2k RPS, frequent updates

**CRITICAL (High load):**
4. `player-service` - 1k RPS
5. `combat-combos-service` - PoC done! ✅
6. `guild-service` - 500 RPS

**HIGH (Medium load):**
7. `quest-service` - 200 RPS
8. `trading-service` - 100 RPS
9. Other services (<100 RPS)

---

## 🚀 Migration Strategy

**Phase 1 (NOW): ogen Migration** ← #1590 active
1. ✅ Migrate existing oapi-codegen services to ogen
2. ✅ All new REST APIs use ogen
3. ✅ Update templates and agent rules
4. Target: All REST services on ogen by Q1 2025

---

## 📚 Related Documentation

**Other Parts:**
- Part 2: Migration Checklist → `02-MIGRATION-STEPS.md`
- Part 3: Code Templates → `03-CODE-TEMPLATES.md`
- Part 4: Troubleshooting → `04-TROUBLESHOOTING.md`

**Related:**
- `.cursor/PROTOCOL_SELECTION_GUIDE.md` - Protocol selection
- `.cursor/BACKEND_OPTIMIZATION_CHECKLIST.md` - Performance validation
- `services/combat-combos-service-ogen-go/` - Reference implementation

---

## 🎯 Example: combat-combos-service (COMPLETED PoC)

**Location:** `services/combat-combos-service-ogen-go/`

**Key files to reference:**
- `server/handlers.go` - Typed ogen handlers
- `server/service.go` - Service layer with OptX types
- `server/security.go` - SecurityHandler implementation
- `server/http_server.go` - ogen server setup
- `server/handlers_bench_test.go` - Benchmarks

**Benchmarks:**
```
BenchmarkOgenGetComboCatalog     191.3 ns/op    320 B/op    5 allocs/op
BenchmarkOgenActivateCombo       244.3 ns/op    400 B/op    6 allocs/op

vs oapi-codegen:                 1994 ns/op    6528 B/op   25 allocs/op
```

**Use this as reference for all migrations!**

---

## 📞 Support

**See next parts for:**
- Detailed migration steps
- Code templates
- Common issues and solutions


