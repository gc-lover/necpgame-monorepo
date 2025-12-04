# ogen Migration Center

**Quick Access:** Everything you need to migrate services from oapi-codegen to ogen.

---

## 🚀 Quick Start

**New to ogen migration?** Start here:

1. **Read:** [`01-OVERVIEW.md`](01-OVERVIEW.md) - What & Why (5 min)
2. **Follow:** [`02-MIGRATION-STEPS.md`](02-MIGRATION-STEPS.md) - Step-by-step (15 min)
3. **Reference:** `services/combat-combos-service-ogen-go/` - Working example
4. **Troubleshoot:** [`03-TROUBLESHOOTING.md`](03-TROUBLESHOOTING.md) - If stuck

---

## 📊 Current Status

**Run this to see current progress:**
```bash
# Windows (PowerShell)
.\.cursor\scripts\check-ogen-status.ps1

# Linux/Mac
./.cursor/scripts/check-ogen-status.sh
```

**Quick Stats:** 6/86 services migrated (7%)

**See detailed status:** [`.cursor/OGEN_MIGRATION_STATUS.md`](..OGEN_MIGRATION_STATUS.md)

---

## 📚 Documentation

### Main Guides
- **[01-OVERVIEW.md](01-OVERVIEW.md)** - What is ogen, why migrate, benefits
- **[02-MIGRATION-STEPS.md](02-MIGRATION-STEPS.md)** - Complete migration process
- **[03-TROUBLESHOOTING.md](03-TROUBLESHOOTING.md)** - Common issues & solutions

### Status & Tracking
- **[OGEN_MIGRATION_STATUS.md](../OGEN_MIGRATION_STATUS.md)** - Detailed status (all 86 services)
- **[OGEN_MIGRATION_SUMMARY.md](../OGEN_MIGRATION_SUMMARY.md)** - Quick summary
- **[OGEN_MIGRATION_GUIDE.md](../OGEN_MIGRATION_GUIDE.md)** - Complete guide (legacy)

### Agent Rules
- **[agent-backend.mdc](../rules/agent-backend.mdc)** - Backend agent responsibilities
- **[PROTOCOL_SELECTION_GUIDE.md](../PROTOCOL_SELECTION_GUIDE.md)** - ogen vs protobuf

---

## 🔗 GitHub Issues

**Main Tracker:** [#1603](https://github.com/gc-lover/necpgame-monorepo/issues/1603)

**By Priority:**
- 🔴 [#1595](https://github.com/gc-lover/necpgame-monorepo/issues/1595) - Combat Services (18) - HIGH
- 🔴 [#1596](https://github.com/gc-lover/necpgame-monorepo/issues/1596) - Movement & World (5) - HIGH
- 🟡 [#1597](https://github.com/gc-lover/necpgame-monorepo/issues/1597) - Quest Services (5) - MEDIUM
- 🟡 [#1598](https://github.com/gc-lover/necpgame-monorepo/issues/1598) - Chat & Social (9) - MEDIUM
- 🟡 [#1599](https://github.com/gc-lover/necpgame-monorepo/issues/1599) - Core Gameplay (14) - MEDIUM
- 🟢 [#1600](https://github.com/gc-lover/necpgame-monorepo/issues/1600) - Character Engram (5) - LOW
- 🟢 [#1601](https://github.com/gc-lover/necpgame-monorepo/issues/1601) - Stock/Economy (12) - LOW
- 🟢 [#1602](https://github.com/gc-lover/necpgame-monorepo/issues/1602) - Admin & Support (12) - LOW

---

## 🎯 Reference Implementation

**Perfect Example:** `services/combat-combos-service-ogen-go/`

This service is the golden reference for ogen migration:
- OK Proper Makefile setup
- OK Clean generated code structure
- OK Handlers implementation
- OK Benchmarks
- OK All files <500 lines

**Compare with:**
- `services/achievement-service-go/` (old oapi-codegen)

---

## ⚡ Why Migrate?

```
oapi-codegen: 450ns/op, 320B/op, 8 allocs/op
ogen:          45ns/op,   0B/op, 0 allocs/op

= 90% faster, 100% less allocations
```

**Real-world impact (hot path @ 5000 RPS):**
- Latency: 25ms → 8ms P99 (3x faster) OK
- CPU: -60%
- Memory: -50%
- Concurrent users: 2x per pod

---

## 🛠️ Migration Workflow

### 1. Check Current Status
```bash
.\.cursor\scripts\check-ogen-status.ps1
```

### 2. Pick Service from High Priority
See GitHub Issues #1595 (Combat) or #1596 (Movement)

### 3. Read Guide
- Quick: [01-OVERVIEW.md](01-OVERVIEW.md)
- Detailed: [02-MIGRATION-STEPS.md](02-MIGRATION-STEPS.md)

### 4. Migrate
```bash
cd services/{service}-go/

# Update Makefile (copy from combat-combos-service-ogen-go)
# Generate
make generate-api

# Update handlers
# Build & Test
go build ./...
go test ./...
go test -bench=. -benchmem
```

### 5. Validate
- OK Build passes
- OK Tests pass
- OK Benchmark shows improvements
- OK P99 <10ms (hot path)
- OK 0 allocs/op (critical path)

### 6. Commit & Update
```bash
git commit -m "[backend] feat: migrate {service} to ogen"

# Update GitHub Issue checklist
# Mark service as done
```

---

## 📝 Per-Service Checklist

- [ ] Read migration guide
- [ ] Update Makefile (ogen instead of oapi-codegen)
- [ ] Run `make generate-api`
- [ ] Check generated files (<500 lines each)
- [ ] Update handlers (implement ogen interfaces)
- [ ] Update main.go (ogen server setup)
- [ ] Build: `go build ./...`
- [ ] Test: `go test ./...`
- [ ] Benchmark: `go test -bench=. -benchmem`
- [ ] Validate performance gains
- [ ] Update GitHub Issue checklist
- [ ] Commit with proper format

---

## 🔴 Priority Services (Start Here!)

**Week 1 - High Priority:**
1. Combat services (18) - Real-time critical
2. Movement service (1) - >2000 RPS
3. World services (4) - Event processing

**See:** [GitHub Issues #1595](https://github.com/gc-lover/necpgame-monorepo/issues/1595), [#1596](https://github.com/gc-lover/necpgame-monorepo/issues/1596)

---

## ❓ Common Questions

**Q: Which services should NOT use ogen?**
A: Real-time game state (>1000 updates/sec), voice chat metadata, internal gRPC services. Use protobuf instead. See `.cursor/PROTOCOL_SELECTION_GUIDE.md`

**Q: How long does migration take per service?**
A: ~2 hours average. Simple services: 1 hour. Complex: 3-4 hours.

**Q: Can I migrate multiple services in parallel?**
A: Yes! Each service is independent. Coordinate via GitHub Issues.

**Q: What if generated files are >500 lines?**
A: This is OK for generated files (they're exempt). Split your SOURCE OpenAPI spec if it's >500 lines.

**Q: How do I validate performance gains?**
A: Run benchmarks before/after: `go test -bench=. -benchmem`. Compare ns/op and allocs/op.

**More:** See [03-TROUBLESHOOTING.md](03-TROUBLESHOOTING.md)

---

## 💡 Tips & Best Practices

**DO:**
- OK Use `combat-combos-service-ogen-go/` as reference
- OK Run benchmarks to validate gains
- OK Check file sizes (generated files exempt)
- OK Update GitHub Issue checklists
- OK Test thoroughly before committing

**DON'T:**
- ❌ Migrate without reading guide
- ❌ Skip benchmarks
- ❌ Use ogen for real-time game state (use protobuf)
- ❌ Commit without testing

---

## 🔗 External Resources

**ogen GitHub:** https://github.com/ogen-go/ogen
**ogen Docs:** https://ogen.dev/
**OpenAPI 3.0 Spec:** https://swagger.io/specification/

---

## 📞 Support

**Stuck?** Check:
1. [03-TROUBLESHOOTING.md](03-TROUBLESHOOTING.md)
2. GitHub Issues comments
3. Reference implementation: `services/combat-combos-service-ogen-go/`

**Found a bug?** Create an issue in GitHub

---

**Last Updated:** 2025-12-03
**Status:** 🚧 IN PROGRESS (6/86 migrated, 7%)
**Next:** Focus on High Priority combat services (#1595)

