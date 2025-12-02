# Protocol Selection Guide: ogen vs Protobuf

**Version:** 1.0  
**Date:** 2025-12-02  
**Status:** Active Policy

---

## 📊 Benchmark Results (Proven)

**Serialization Performance:**

| Protocol | Marshal | Unmarshal | Size | Allocations |
|----------|---------|-----------|------|-------------|
| **ogen (JSON)** | 364 ns/op | 1878 ns/op | 255 bytes | 2/14 allocs |
| **Protobuf** | 145 ns/op | 252 ns/op | 123 bytes | 1/8 allocs |
| **Difference** | **2.5x slower** | **7.5x slower** | **2x larger** | **2x more** |

**Key Findings:**
- Protobuf: 51.8% smaller payloads
- Protobuf: 2.5x faster encoding, 7.5x faster decoding
- ogen vs oapi-codegen: 90% faster (already proven in #1590)

---

## 🎯 Service Classification

### ✅ USE OGEN (JSON REST) - 95% of services

**Criteria:**
- Request rate: <1000 RPS
- Latency tolerance: >10ms P99
- Need debugging/monitoring
- External clients (web, mobile)
- Admin interfaces

**Services:**

**Matchmaking & Social:**
- `matchmaking-service` ← 100-500 RPS, needs debugging
- `friends-service` ← Low frequency, REST standard
- `clan-service` ← Low frequency, REST standard
- `party-service` ← Medium frequency, WebSocket already fast
- `leaderboard-service` ← Read-heavy, caching helps more

**Economy & Progression:**
- `inventory-service` ← 200-500 RPS, caching critical
- `marketplace-service` ← Low frequency, complex queries
- `achievement-service` ← Event-driven, not latency critical
- `progression-service` ← Infrequent updates, REST is fine
- `reward-service` ← Batch processing, not real-time

**Content & Configuration:**
- `quest-service` ← Config loading, not frequent
- `loot-service` ← Drop calculation, async is fine
- `crafting-service` ← UI-driven, REST standard
- `skill-tree-service` ← Config loading, infrequent

**Infrastructure:**
- `auth-service` ← JWT validation fast enough
- `profile-service` ← Low frequency, needs flexibility
- `analytics-service` ← Batch processing, not real-time
- `notification-service` ← WebSocket payload, JSON fine
- `admin-panel-api` ← Low traffic, debugging critical

**Combat (Non-Real-Time):**
- `combat-combos-service` ← Config loading only
- `loadout-service` ← Pre-match setup, not real-time
- `weapon-stats-service` ← Static data, REST standard

---

### ⚡ USE PROTOBUF (Binary) - 5% of services

**Criteria:**
- Request rate: >1000 RPS
- Latency critical: <5ms P99
- Bandwidth critical: >10 MB/sec
- Real-time game state
- Internal microservices only

**Services:**

**Game State (CRITICAL):**
- `realtime-gateway-service` ← **UDP + Protobuf**
  - Player position updates (60-128 Hz tick rate)
  - Rotation, velocity updates
  - Shooting events, hit detection
  - **Payload:** 50-100 bytes/update, 1000+ updates/sec per server
  - **Why:** 2.5x faster encoding critical for tick rate

**Voice/Video (CRITICAL):**
- `voice-chat-service` ← **WebRTC + Protobuf metadata**
  - Room state, participant list
  - Audio routing metadata
  - **Payload:** 20-50 bytes/event, 100+ events/sec
  - **Why:** Low overhead for signaling

**Zone Sync (HIGH LOAD):**
- `zone-sync-service` ← **gRPC + Protobuf**
  - Server-to-server world state sync
  - NPC position updates (for shared zones)
  - **Payload:** 500-1000 bytes/batch, 100+ batches/sec
  - **Why:** Internal service, binary efficient

---

## 🔄 Hybrid Services (Both Protocols)

**Services that use BOTH:**

### `game-server-service`
- **REST/ogen:** Match setup, loadouts, pre-game config
- **UDP/Protobuf:** In-game position, shooting, damage events
- **Why:** Different endpoints, different requirements

### `housing-service`
- **REST/ogen:** Furniture catalog, purchase, permissions
- **UDP/Protobuf:** Real-time furniture placement (multiplayer editing)
- **Why:** Config via REST, real-time sync via binary

---

## 📋 Decision Matrix

**Use ogen when:**
- ✅ REST API (public or internal)
- ✅ Web/mobile clients
- ✅ Debugging important
- ✅ Latency <50ms acceptable
- ✅ Rate <1000 RPS
- ✅ Need Swagger docs

**Use Protobuf when:**
- ⚡ Latency <5ms required
- ⚡ Rate >1000 RPS sustained
- ⚡ Bandwidth >10 MB/sec
- ⚡ Real-time game state
- ⚡ UDP protocol
- ⚡ Internal-only service

**RED FLAGS (don't use protobuf):**
- ❌ Admin panel (debugging nightmare)
- ❌ Public API (integration barrier)
- ❌ Web browser clients (need protobuf.js, slow)
- ❌ Low traffic (<100 RPS)
- ❌ Rapid prototyping (complexity overhead)

---

## 🚀 Migration Strategy

**Phase 1 (NOW): ogen Migration** ← #1590 active
1. ✅ Migrate existing oapi-codegen services to ogen
2. ✅ All new REST APIs use ogen
3. ✅ Update templates and agent rules
4. Target: All REST services on ogen by Q1 2025

**Phase 2 (FUTURE): Protobuf for Hot Path**
1. `realtime-gateway-service` UDP protocol
2. `voice-chat-service` WebRTC signaling
3. `zone-sync-service` gRPC internal
4. Only IF production metrics show bottleneck

**DO NOT:**
- ❌ Convert existing REST APIs to protobuf
- ❌ Use protobuf for low-traffic services
- ❌ Mix protobuf in ogen services (confusing)

---

## 📝 Agent Instructions

**API Designer:**
- Default: OpenAPI 3.0 for ogen
- Exception: Hot path services marked in Issue (architect decision)
- Document protocol choice in Issue description

**Backend Developer:**
- Default: ogen for REST
- Protobuf only for services in "USE PROTOBUF" list above
- Both: Separate handlers for REST vs binary endpoints

**Network Engineer:**
- UDP + Protobuf: Real-time game state only
- WebSocket + JSON: Everything else (chat, lobby, notifications)
- gRPC + Protobuf: Internal microservice communication (if needed)

---

## 📊 Production Monitoring

**When to consider Protobuf:**
1. Service consistently >1000 RPS
2. P99 latency >10ms despite optimizations
3. Bandwidth >10 MB/sec sustained
4. CPU >70% on serialization (profiling confirms)

**Before switching:**
1. Optimize ogen code first (pooling, batch, cache)
2. Profile to confirm serialization is bottleneck
3. Estimate dev cost vs performance gain
4. Create migration Issue with benchmarks

---

## ✅ Summary

**Default Standard: ogen (JSON REST)**
- Fast (90% improvement vs old code)
- Easy debugging
- Wide compatibility
- Perfect for 95% of services

**Rare Exception: Protobuf (Binary)**
- Real-time game state (position, shooting)
- Voice chat metadata
- Internal high-load sync
- Only when ogen insufficient

**Rule of Thumb:**
- If it's a REST API → ogen
- If it's game state >60Hz → protobuf
- When in doubt → ogen

