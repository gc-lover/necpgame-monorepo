# ogen Migration Summary

**Quick Overview:** 6/88 services migrated (6.8%)

---

## 📊 Quick Stats

| Category | Count | % |
|----------|-------|---|
| ✅ Migrated | 6 | 6.8% |
| 🔴 High Priority | 23 | 26.1% |
| 🟡 Medium Priority | 28 | 31.8% |
| 🟢 Low Priority | 31 | 35.2% |
| **Total** | **88** | **100%** |

---

## ✅ Already Done (6)

1. character-service-go
2. economy-player-market-service-go
3. inventory-service-go
4. matchmaking-service-go
5. party-service-go
6. social-service-go

**Reference:** `services/combat-combos-service-ogen-go/`

---

## 🔗 GitHub Issues

| Priority | Issue | Services | Effort |
|----------|-------|----------|--------|
| 🔴 HIGH | [#1595](https://github.com/gc-lover/necpgame-monorepo/issues/1595) | Combat (18) | 2 days |
| 🔴 HIGH | [#1596](https://github.com/gc-lover/necpgame-monorepo/issues/1596) | Movement & World (5) | 4 hours |
| 🟡 MED | [#1597](https://github.com/gc-lover/necpgame-monorepo/issues/1597) | Quest (5) | 4 hours |
| 🟡 MED | [#1598](https://github.com/gc-lover/necpgame-monorepo/issues/1598) | Chat & Social (9) | 6 hours |
| 🟡 MED | [#1599](https://github.com/gc-lover/necpgame-monorepo/issues/1599) | Core Gameplay (14) | 1 day |
| 🟢 LOW | [#1600](https://github.com/gc-lover/necpgame-monorepo/issues/1600) | Character Engram (5) | 4 hours |
| 🟢 LOW | [#1601](https://github.com/gc-lover/necpgame-monorepo/issues/1601) | Stock/Economy (12) | 1 day |
| 🟢 LOW | [#1602](https://github.com/gc-lover/necpgame-monorepo/issues/1602) | Admin & Support (12) | 1 day |

**Main Tracker:** [#1603](https://github.com/gc-lover/necpgame-monorepo/issues/1603)

**Total Effort:** ~9 days (82 services × ~2h each)

---

## ⚡ Why Migrate?

**Performance Gains:**
```
oapi-codegen: 450ns/op, 320B/op, 8 allocs/op
ogen:          45ns/op,   0B/op, 0 allocs/op
```

**Real-world Impact (hot path @ 5000 RPS):**
- ⚡ Latency: 25ms → 8ms P99 (3x faster)
- 💾 Memory: -50%
- 🖥️ CPU: -60%
- 📦 Type Safety: Full (no `interface{}`)
- 👥 Concurrent users: 2x per pod

---

## 🚀 Quick Start

### Check Status
```bash
# Windows (PowerShell)
.\.cursor\scripts\check-ogen-status.ps1

# Linux/Mac
./.cursor/scripts/check-ogen-status.sh
```

### Migrate One Service
```bash
cd services/{service}-go/

# 1. Read guide
cat ../../.cursor/OGEN_MIGRATION_GUIDE.md

# 2. Update Makefile (see reference: combat-combos-service-ogen-go)
# 3. Generate
make generate-api

# 4. Update handlers
# 5. Build & Test
go build ./...
go test ./...
go test -bench=. -benchmem

# 6. Commit
git commit -m "[backend] feat: migrate {service} to ogen"
```

---

## 📚 Resources

**Main Docs:**
- `.cursor/OGEN_MIGRATION_GUIDE.md` - Complete guide
- `.cursor/OGEN_MIGRATION_STATUS.md` - Detailed status
- `.cursor/ogen/01-OVERVIEW.md` - What & Why
- `.cursor/ogen/02-MIGRATION-STEPS.md` - Step-by-step
- `.cursor/ogen/03-TROUBLESHOOTING.md` - Common issues

**Agent Rules:**
- `.cursor/rules/agent-backend.mdc` - Backend responsibilities
- `.cursor/PROTOCOL_SELECTION_GUIDE.md` - ogen vs protobuf

**Reference Code:**
- `services/combat-combos-service-ogen-go/` - Perfect example

---

## 🎯 Rollout Plan

**Week 1 (High Priority):**
- Combat services (18)
- Movement & World (5)
- **Total:** 23 services

**Week 2 (Medium Priority):**
- Quest (5)
- Chat & Social (9)
- Core Gameplay (14)
- **Total:** 28 services

**Week 3 (Low Priority):**
- Character Engram (5)
- Stock/Economy (12)
- Admin & Support (12)
- **Total:** 31 services

---

## 📝 Per-Service Checklist

- [ ] Read `.cursor/OGEN_MIGRATION_GUIDE.md`
- [ ] Update Makefile (ogen instead of oapi-codegen)
- [ ] Run `make generate-api`
- [ ] Update handlers (implement ogen interfaces)
- [ ] Build: `go build ./...`
- [ ] Test: `go test ./...`
- [ ] Benchmark: `go test -bench=. -benchmem`
- [ ] Validate: P99 <10ms, 0 allocs/op
- [ ] Update Issue checklist
- [ ] Commit with proper format

---

## 🔴 Priority Ranking

**High Priority (Start Here!):**
1. combat-* services (real-time critical, >1000 RPS)
2. movement-service-go (>2000 RPS)
3. world-* services (event processing)

**Medium Priority:**
1. quest-* services (>100 RPS)
2. chat-* services (>500 RPS peak)
3. Core gameplay (achievements, leaderboards, etc.)

**Low Priority:**
1. Admin panel, support tools
2. Stock market, economy analytics
3. Legacy services

---

## 💡 Quick Tips

**DO:**
- ✅ Use `combat-combos-service-ogen-go/` as reference
- ✅ Test with benchmarks (`-bench=. -benchmem`)
- ✅ Validate performance gains
- ✅ Update Issue checklist

**DON'T:**
- ❌ Migrate without reading guide
- ❌ Skip benchmarks
- ❌ Forget to check file sizes (<500 lines)
- ❌ Use ogen for real-time game state (use protobuf)

**When NOT ogen:**
- Real-time game state >1000 updates/sec → protobuf + UDP
- Voice chat metadata → protobuf
- Internal microservices → gRPC + protobuf

See `.cursor/PROTOCOL_SELECTION_GUIDE.md`

---

**Last Updated:** 2025-12-03  
**Status:** 🚧 IN PROGRESS (6/88)

