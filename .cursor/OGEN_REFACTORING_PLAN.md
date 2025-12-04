# ogen Refactoring Plan - Массовая миграция всех сервисов

**Date:** 2025-12-04  
**Status:** 🚧 IN PROGRESS  
**Progress:** 1/86 fully complete (1.2%), 69/86 code generated (80.2%)

---

## 📊 Current Status

**Actual situation:**
- ✅ **1 service** fully migrated with MIGRATION_SUMMARY.md
- 🚧 **69 services** have ogen code generated (pkg/api/oas_*_gen.go)
- ❌ **16 services** not started

**This means:**
- Code generation: **DONE for 80%** of services
- Handler implementation: **NEEDED for 69 services**
- Full migration: **1.2% complete**

---

## 🎯 Strategy

### Phase 1: Complete In-Progress Services (69)

**These services already have:**
- ✅ Makefile updated for ogen
- ✅ pkg/api/oas_*_gen.go generated (19 files each)
- ✅ openapi-bundled.yaml

**Missing:**
- ❌ Handlers updated to ogen interfaces
- ❌ HTTP server setup for ogen
- ❌ Benchmarks
- ❌ MIGRATION_SUMMARY.md

**Effort per service:** 1-2 hours (handlers + testing)

### Phase 2: Start Remaining Services (16)

**These need full migration:**
1. Generate ogen code
2. Update handlers
3. Test & benchmark
4. Document

**Effort per service:** 2-3 hours

---

## 🔥 Priority Order

### 🔴 HIGH PRIORITY - Real-time Critical (23)

**Issue #1595 - Combat Services (18):**
- [x] combat-combos-service-ogen-go ✅ (reference)
- [🚧] combat-actions-service-go
- [🚧] combat-ai-service-go
- [🚧] combat-damage-service-go
- [🚧] combat-extended-mechanics-service-go
- [🚧] combat-hacking-service-go
- [🚧] combat-sessions-service-go
- [🚧] combat-turns-service-go
- [🚧] combat-implants-core-service-go
- [🚧] combat-implants-maintenance-service-go
- [🚧] combat-implants-stats-service-go
- [🚧] combat-sandevistan-service-go
- [🚧] projectile-core-service-go
- [🚧] hacking-core-service-go
- [🚧] gameplay-weapon-special-mechanics-service-go
- [🚧] weapon-progression-service-go
- [🚧] weapon-resource-service-go

**Expected gains:** 25ms → 8ms P99, CPU -60%, Memory -50%

**Issue #1596 - Movement & World (5):**
- [🚧] movement-service-go
- [🚧] world-service-go
- [🚧] world-events-analytics-service-go
- [🚧] world-events-core-service-go
- [🚧] world-events-scheduler-service-go

**Expected gains:** 50ms → 15ms P99 @ 2000 RPS

---

### 🟡 MEDIUM PRIORITY - Active Users (28)

**Issue #1597 - Quest Services (5):**
- [🚧] quest-core-service-go
- [🚧] quest-rewards-events-service-go
- [🚧] quest-skill-checks-conditions-service-go
- [🚧] quest-state-dialogue-service-go
- [🚧] gameplay-progression-core-service-go

**Issue #1598 - Chat & Social (9):**
- [🚧] chat-service-go
- [🚧] social-chat-channels-service-go
- [🚧] social-chat-commands-service-go
- [🚧] social-chat-format-service-go
- [🚧] social-chat-history-service-go
- [🚧] social-chat-messages-service-go
- [🚧] social-chat-moderation-service-go
- [🚧] social-player-orders-service-go
- [🚧] social-reputation-core-service-go

**Issue #1599 - Core Gameplay (14):**
- [🚧] achievement-service-go
- [🚧] leaderboard-service-go
- [🚧] league-service-go
- [🚧] loot-service-go
- [🚧] gameplay-service-go
- [🚧] progression-experience-service-go
- [🚧] progression-paragon-service-go
- [🚧] battle-pass-service-go
- [🚧] seasonal-challenges-service-go
- [🚧] companion-service-go
- [🚧] cosmetic-service-go
- [🚧] housing-service-go
- [🚧] mail-service-go
- [🚧] referral-service-go

---

### 🟢 LOW PRIORITY - Cold Path (35)

**Issue #1600 - Character Engram (5):**
- [🚧] character-engram-compatibility-service-go
- [🚧] character-engram-core-service-go
- [🚧] character-engram-cyberpsychosis-service-go
- [🚧] character-engram-historical-service-go
- [🚧] character-engram-security-service-go

**Issue #1601 - Stock/Economy (12):**
- [🚧] stock-analytics-charts-service-go
- [🚧] stock-analytics-tools-service-go
- [🚧] stock-dividends-service-go
- [🚧] stock-events-service-go
- [🚧] stock-futures-service-go
- [🚧] stock-indices-service-go
- [🚧] stock-integration-service-go
- [🚧] stock-margin-service-go
- [🚧] stock-options-service-go
- [🚧] stock-protection-service-go
- [🚧] economy-service-go
- [🚧] trade-service-go

**Issue #1602 - Admin & Support (18):**
- [🚧] admin-service-go
- [🚧] support-service-go
- [🚧] maintenance-service-go
- [🚧] feedback-service-go
- [🚧] clan-war-service-go
- [🚧] faction-core-service-go
- [🚧] reset-service-go
- [🚧] client-service-go
- [🚧] realtime-gateway-go ⚠️ (check protocol)
- [🚧] ws-lobby-go ⚠️ (check protocol)
- [🚧] voice-chat-service-go ⚠️ (check protocol)

⚠️ **Note:** Some services may need protobuf instead of ogen (check `.cursor/PROTOCOL_SELECTION_GUIDE.md`)

---

## 📋 Completion Checklist (Per Service)

### 1. Verify Generated Code ✅ (Already done for 69)
- [x] pkg/api/oas_*_gen.go exists (19 files)
- [x] openapi-bundled.yaml exists
- [x] Makefile uses ogen

### 2. Update Handlers (NEEDED)
```go
// OLD (oapi-codegen)
func (h *Handlers) GetPlayer(w http.ResponseWriter, r *http.Request, id string) {
    respondJSON(w, 200, player)  // ← interface{} boxing!
}

// NEW (ogen)
func (h *Handlers) GetPlayer(ctx context.Context, params api.GetPlayerParams) (api.GetPlayerRes, error) {
    return player, nil  // ← Typed! No interface{}!
}
```

**Tasks:**
- [ ] Update ALL handler signatures
- [ ] Return typed responses (no respondJSON helpers)
- [ ] Handle errors with typed error responses
- [ ] Remove helper functions (respondJSON, respondError)

### 3. Update Service Layer (NEEDED)
```go
// Use ogen optional types
player := &api.Player{
    ID:   uuid.MustParse(id),
    Name: api.NewOptString("Player"),  // ← ogen optional
}
```

### 4. Update Server Setup (NEEDED)
```go
// Create SecurityHandler
type SecurityHandler struct{}

func (s *SecurityHandler) HandleBearerAuth(ctx context.Context, operationName string, t api.BearerAuth) (context.Context, error) {
    // Validate JWT
    return ctx, nil
}

// Setup ogen server
handlers := NewHandlers(service)
secHandler := &SecurityHandler{}
ogenServer, err := api.NewServer(handlers, secHandler)
```

### 5. Create Benchmarks (NEEDED)
```go
func BenchmarkOgenGetPlayer(b *testing.B) {
    handlers := NewHandlers(service)
    ctx := context.Background()
    params := api.GetPlayerParams{ID: uuid.New()}

    b.ReportAllocs()
    b.ResetTimer()

    for i := 0; i < b.N; i++ {
        _, _ = handlers.GetPlayer(ctx, params)
    }
}
```

### 6. Test & Validate (CRITICAL)
```bash
# Build
go build ./...

# Test
go test ./...

# Benchmark
go test -bench=. -benchmem ./server

# Expected gains:
# - Latency: >70% faster
# - Allocations: >70% fewer
# - Memory: >80% less
```

### 7. Document (FINAL STEP)
- [ ] Create MIGRATION_SUMMARY.md (like combat-actions-service-go)
- [ ] Update GitHub Issue checklist
- [ ] Git commit: `[backend] perf: migrate {service} to ogen`

---

## 🚀 Execution Plan

### Week 1: HIGH PRIORITY (23 services)

**Day 1-3: Combat Services (18)**
- Parallel work: 3-4 services/day
- Reference: combat-combos-service-ogen-go
- Focus: handlers + benchmarks
- Target: All combat services ready

**Day 4-5: Movement & World (5)**
- Complete remaining high-priority
- Load testing
- Performance validation

**Deliverable:** All real-time critical services migrated

---

### Week 2: MEDIUM PRIORITY (28 services)

**Day 6-8: Quest (5) + Chat (9)**
- Quest services: sequential dependencies
- Chat services: parallel work possible

**Day 9-10: Core Gameplay (14)**
- Parallel work: 4-5 services/day
- Focus: quality over speed

**Deliverable:** All active user services migrated

---

### Week 3: LOW PRIORITY (35 services)

**Day 11-13: Character Engram (5) + Stock (12)**
- Lower complexity
- Faster migration
- 5-6 services/day

**Day 14-15: Admin & Support (18)**
- Final batch
- Protocol validation (realtime-gateway, ws-lobby, voice-chat)
- Cleanup & documentation

**Deliverable:** 100% migration complete

---

## 📊 Expected Global Impact

**Current state (oapi-codegen):**
- Memory: ~65 MB/sec per 10k RPS service
- Allocations: ~250k allocs/sec
- GC pauses: frequent

**After migration (ogen):**
- Memory: ~3 MB/sec (62 MB saved per service)
- Allocations: ~50k allocs/sec (200k saved)
- GC pauses: minimal

**For entire backend (86 services @ avg 1k RPS):**
- **Memory savings: ~5.3 GB/sec** 🚀
- **Allocation reduction: ~17M allocs/sec** 🚀
- **Massive GC pressure relief** 🚀

---

## 🛠️ Tools & Scripts

**Created:**
- `.cursor/scripts/migrate-service-to-ogen.ps1` - Single service migration
- `.cursor/scripts/batch-migrate-combat-services.ps1` - Batch migration
- `.cursor/scripts/check-migration-progress-simple.ps1` - Progress tracking

**Usage:**
```powershell
# Check progress
.\cursor\scripts\check-migration-progress-simple.ps1

# Migrate single service
.\cursor\scripts\migrate-service-to-ogen.ps1 -ServiceName combat-ai-service-go

# Batch migrate combat services
.\cursor\scripts\batch-migrate-combat-services.ps1
```

---

## 📚 Documentation

**Migration guides:**
- `.cursor/ogen/01-OVERVIEW.md` - What & Why
- `.cursor/ogen/02-MIGRATION-STEPS.md` - Step-by-step guide
- `.cursor/ogen/03-TROUBLESHOOTING.md` - Common issues

**Reference implementation:**
- `services/combat-combos-service-ogen-go/` - Perfect example
- All handlers, benchmarks, structure

**Agent rules:**
- `.cursor/rules/agent-backend.mdc` - Updated for ogen
- `.cursor/PROTOCOL_SELECTION_GUIDE.md` - ogen vs protobuf

---

## ✅ Success Criteria

**Per service:**
- [ ] Build passes: `go build ./...`
- [ ] Tests pass: `go test ./...`
- [ ] Benchmarks show >70% improvement
- [ ] MIGRATION_SUMMARY.md created
- [ ] GitHub Issue updated

**Global:**
- [ ] 100% REST services on ogen (86/86)
- [ ] Performance targets met (P99 <10ms hot path)
- [ ] Zero critical bugs
- [ ] Documentation complete

---

## 🎯 Next Actions

**IMMEDIATE:**
1. Complete handlers for 5 high-priority combat services
2. Test & benchmark each
3. Create MIGRATION_SUMMARY.md template
4. Start parallel work on remaining combat services

**THIS WEEK:**
- Focus on HIGH PRIORITY (23 services)
- Daily progress tracking
- Address blockers immediately

**BLOCKERS to watch:**
- Complex business logic in old handlers
- Missing OpenAPI specs
- Protobuf services misidentified as REST

---

**Status:** 🚀 READY TO EXECUTE  
**Owner:** Backend Agent  
**Timeline:** 3 weeks (15 working days)  
**Confidence:** HIGH (80% already code-generated)

---

**Last Updated:** 2025-12-04  
**Next Update:** Daily during active migration


