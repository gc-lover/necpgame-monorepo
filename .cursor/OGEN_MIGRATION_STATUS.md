# ogen Migration Status

**Last Updated:** 2025-12-03

## 📊 Overall Progress: 6/88 (6.8%)

**Migrated:** 6 services OK  
**Remaining:** 82 services ❌  
**Total Services:** 88

---

## OK Already Migrated (6 services)

| Service | Status | Notes |
|---------|--------|-------|
| character-service-go | OK | Migrated |
| economy-player-market-service-go | OK | Migrated |
| inventory-service-go | OK | Migrated |
| matchmaking-service-go | OK | Migrated |
| party-service-go | OK | Migrated |
| social-service-go | OK | Migrated |

**Reference:** `services/combat-combos-service-ogen-go/`

---

## 🔴 High Priority - Hot Path (23 services)

**Issue:** [#1595](https://github.com/gc-lover/necpgame-monorepo/issues/1595) (Combat), [#1596](https://github.com/gc-lover/necpgame-monorepo/issues/1596) (Movement & World)

### Combat Services (18)
- [ ] combat-actions-service-go
- [ ] combat-ai-service-go
- [ ] combat-combos-service-go
- [ ] combat-damage-service-go
- [ ] combat-extended-mechanics-service-go
- [ ] combat-hacking-service-go
- [ ] combat-sessions-service-go
- [ ] combat-turns-service-go
- [ ] combat-implants-core-service-go
- [ ] combat-implants-maintenance-service-go
- [ ] combat-implants-stats-service-go
- [ ] combat-sandevistan-service-go
- [ ] projectile-core-service-go
- [ ] hacking-core-service-go
- [ ] gameplay-weapon-special-mechanics-service-go
- [ ] weapon-progression-service-go
- [ ] weapon-resource-service-go

**Expected Gains:** 25ms → 8ms P99, CPU -60%, Memory -50%

### Movement & World (5)
- [ ] movement-service-go
- [ ] world-service-go
- [ ] world-events-analytics-service-go
- [ ] world-events-core-service-go
- [ ] world-events-scheduler-service-go

**Expected Gains:** 50ms → 15ms P99 @ 2000 RPS

---

## 🟡 Medium Priority - Active Users (28 services)

### Quest Services (5) - [#1597](https://github.com/gc-lover/necpgame-monorepo/issues/1597)
- [ ] quest-core-service-go
- [ ] quest-rewards-events-service-go
- [ ] quest-skill-checks-conditions-service-go
- [ ] quest-state-dialogue-service-go
- [ ] gameplay-progression-core-service-go

**Expected:** 30ms → 10ms P99

### Chat & Social (9) - [#1598](https://github.com/gc-lover/necpgame-monorepo/issues/1598)
- [ ] chat-service-go
- [ ] social-chat-channels-service-go
- [ ] social-chat-commands-service-go
- [ ] social-chat-format-service-go
- [ ] social-chat-history-service-go
- [ ] social-chat-messages-service-go
- [ ] social-chat-moderation-service-go
- [ ] social-player-orders-service-go
- [ ] social-reputation-core-service-go

**Expected:** 40ms → 12ms P99

### Core Gameplay (14) - [#1599](https://github.com/gc-lover/necpgame-monorepo/issues/1599)
- [ ] achievement-service-go
- [ ] leaderboard-service-go
- [ ] league-service-go
- [ ] loot-service-go
- [ ] gameplay-service-go
- [ ] progression-experience-service-go
- [ ] progression-paragon-service-go
- [ ] battle-pass-service-go
- [ ] seasonal-challenges-service-go
- [ ] companion-service-go
- [ ] cosmetic-service-go
- [ ] housing-service-go
- [ ] mail-service-go
- [ ] referral-service-go

**Expected:** 35ms → 12ms P99

---

## 🟢 Low Priority - Cold Path (31 services)

### Character Engram (5) - [#1600](https://github.com/gc-lover/necpgame-monorepo/issues/1600)
- [ ] character-engram-compatibility-service-go
- [ ] character-engram-core-service-go
- [ ] character-engram-cyberpsychosis-service-go
- [ ] character-engram-historical-service-go
- [ ] character-engram-security-service-go

**Expected:** 45ms → 15ms P99

### Stock/Economy (12) - [#1601](https://github.com/gc-lover/necpgame-monorepo/issues/1601)
- [ ] stock-analytics-charts-service-go
- [ ] stock-analytics-tools-service-go
- [ ] stock-dividends-service-go
- [ ] stock-events-service-go
- [ ] stock-futures-service-go
- [ ] stock-indices-service-go
- [ ] stock-integration-service-go
- [ ] stock-margin-service-go
- [ ] stock-options-service-go
- [ ] stock-protection-service-go
- [ ] economy-service-go
- [ ] trade-service-go

**Expected:** 50ms → 18ms P99

### Admin & Support (12) - [#1602](https://github.com/gc-lover/necpgame-monorepo/issues/1602)
- [ ] admin-service-go
- [ ] support-service-go
- [ ] maintenance-service-go
- [ ] feedback-service-go
- [ ] clan-war-service-go
- [ ] faction-core-service-go
- [ ] reset-service-go
- [ ] client-service-go
- [ ] realtime-gateway-go WARNING
- [ ] ws-lobby-go WARNING
- [ ] voice-chat-service-go WARNING
- [ ] matchmaking-go *(check if legacy)*

WARNING **Note:** Network services may need protobuf instead (check `.cursor/PROTOCOL_SELECTION_GUIDE.md`)

### Legacy/Duplicate (2)
- combat-combos-service-go *(has -ogen version)*
- matchmaking-go *(has matchmaking-service-go)*

---

## ⚡ Expected Performance Gains

**Benchmark Comparison:**
```
oapi-codegen: 450ns/op, 320 B/op, 8 allocs/op
ogen:          45ns/op,   0 B/op, 0 allocs/op
```

**Real-world Impact:**
- **Encoding:** 90% faster
- **Decoding:** 85% faster
- **Memory:** 70% less allocations
- **Type Safety:** Full compile-time checks (no `interface{}`)

**Hot Path Services (5000 RPS):**
- Latency: 25ms → 8ms P99 OK
- CPU usage: -60%
- Memory usage: -50%
- Concurrent users: 2x per pod

---

## 📚 Migration Resources

**Guides:**
- `.cursor/OGEN_MIGRATION_GUIDE.md` - Main guide
- `.cursor/ogen/01-OVERVIEW.md` - What & Why
- `.cursor/ogen/02-MIGRATION-STEPS.md` - Step-by-step
- `.cursor/ogen/03-TROUBLESHOOTING.md` - Common issues

**Agent Rules:**
- `.cursor/rules/agent-backend.mdc` - Backend responsibilities
- `.cursor/PROTOCOL_SELECTION_GUIDE.md` - ogen vs protobuf

**Reference:**
- `services/combat-combos-service-ogen-go/` - Perfect example
- `services/matchmaking-service-go/` - Migrated

---

## 🎯 Rollout Plan

**Phase 1 - High Priority (Week 1):**
- Combat services (18) - #1595
- Movement & World (5) - #1596
- **Total:** 23 services, ~3 days

**Phase 2 - Medium Priority (Week 2):**
- Quest (5) - #1597
- Chat & Social (9) - #1598
- Core Gameplay (14) - #1599
- **Total:** 28 services, ~3 days

**Phase 3 - Low Priority (Week 3):**
- Character Engram (5) - #1600
- Stock/Economy (12) - #1601
- Admin & Support (12) - #1602
- **Total:** 31 services, ~3 days

**Estimated Total:** 9 days (~2h per service)

**Parallel work possible:** Multiple services can be migrated simultaneously by different developers.

---

## OK Per-Service Checklist

For each service:
1. [ ] Read `.cursor/OGEN_MIGRATION_GUIDE.md`
2. [ ] Update Makefile (use ogen instead of oapi-codegen)
3. [ ] Run `make generate-api`
4. [ ] Update handlers (implement ogen interfaces)
5. [ ] Run `go build ./...`
6. [ ] Run `go test ./...`
7. [ ] Benchmark: `go test -bench=. -benchmem`
8. [ ] Validate: P99 <10ms (hot), 0 allocs/op
9. [ ] Update service Issue checklist
10. [ ] Commit: `[backend] feat: migrate {service} to ogen`

---

## 🔗 Tracking Issues

**Main Tracker:** [#1603](https://github.com/gc-lover/necpgame-monorepo/issues/1603)

**Category Issues:**
- [#1595](https://github.com/gc-lover/necpgame-monorepo/issues/1595) - Combat Services (High)
- [#1596](https://github.com/gc-lover/necpgame-monorepo/issues/1596) - Movement & World (High)
- [#1597](https://github.com/gc-lover/necpgame-monorepo/issues/1597) - Quest Services (Medium)
- [#1598](https://github.com/gc-lover/necpgame-monorepo/issues/1598) - Chat & Social (Medium)
- [#1599](https://github.com/gc-lover/necpgame-monorepo/issues/1599) - Core Gameplay (Medium)
- [#1600](https://github.com/gc-lover/necpgame-monorepo/issues/1600) - Character Engram (Low)
- [#1601](https://github.com/gc-lover/necpgame-monorepo/issues/1601) - Stock/Economy (Low)
- [#1602](https://github.com/gc-lover/necpgame-monorepo/issues/1602) - Admin & Support (Low)

---

## 📝 Notes

**Why ogen over oapi-codegen?**
1. **90% faster** encoding/decoding
2. **Zero allocations** (hot path)
3. **Full type safety** (no `interface{}`)
4. **Auto SOLID** (~20 files, each <200 lines)
5. **Industry standard** (maintained, active)

**When NOT ogen:**
- Real-time game state >1000 updates/sec → protobuf + UDP
- Voice chat metadata → protobuf
- Internal microservices → gRPC + protobuf

**Status Emoji:**
- OK Migrated
- 🚧 In Progress
- ❌ Not Started
- WARNING Needs Review (protocol selection)

---

**Last Check:** Run `.cursor/scripts/check-ogen-status.sh` to regenerate this file

