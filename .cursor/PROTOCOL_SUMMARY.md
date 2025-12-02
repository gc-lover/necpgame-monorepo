# Protocol Selection - Quick Reference

**Version:** 1.0  
**Date:** 2025-12-02

---

## 🎯 Quick Decision Tree

```
Is it a REST API?
├─ YES → OpenAPI 3.0 + ogen (API Designer)
│         Examples: matchmaking, inventory, profiles, admin
│
└─ NO → Is it real-time game state (>1000 updates/sec)?
    ├─ YES → Protobuf + UDP (Network Engineer creates .proto)
    │         Examples: position, shooting, voice metadata
    │
    └─ NO → WebSocket + JSON (Backend)
              Examples: chat, lobby, notifications
```

---

## 📋 Agent Responsibilities

| Protocol | Who Creates | Who Implements | Examples |
|----------|-------------|----------------|----------|
| **OpenAPI 3.0** | API Designer | Backend (ogen) | matchmaking, inventory, profiles |
| **Protobuf** | Network Engineer | Backend (protoc) | game state, voice metadata |
| **WebSocket+JSON** | Backend | Backend | chat, lobby, notifications |

---

## 📊 Service List (Quick Reference)

### ✅ OpenAPI 3.0 + ogen (95% сервисов)

**Matchmaking & Social:**
- matchmaking-service, friends-service, clan-service, party-service, leaderboard-service

**Economy:**
- inventory-service, marketplace-service, achievement-service, progression-service, reward-service

**Content:**
- quest-service, loot-service, crafting-service, skill-tree-service

**Infrastructure:**
- auth-service, profile-service, analytics-service, notification-service, admin-panel-api

**Combat (Config):**
- combat-combos-service, loadout-service, weapon-stats-service

---

### ⚡ Protobuf + UDP/gRPC (5% сервисов)

**Real-Time Game State:**
- realtime-gateway-service (position, shooting, 60-128 Hz)
- voice-chat-service (WebRTC metadata)
- zone-sync-service (server-to-server, gRPC)

---

## 🚀 Migration Status

**Phase 1 (Current): ogen migration** ← #1590
- [x] Proven: 90% faster vs oapi-codegen
- [x] Benchmarks: 191 ns/op vs 1994 ns/op
- [ ] Migrate all existing oapi-codegen services
- [ ] Update all templates

**Phase 2 (Future): Protobuf for hot path**
- [ ] realtime-gateway-service (when needed)
- [ ] voice-chat-service (when needed)
- [ ] Only IF production metrics show bottleneck

---

## 📚 Documentation

**Detailed Guide:** `.cursor/PROTOCOL_SELECTION_GUIDE.md`  
**ogen Migration:** `.cursor/OGEN_MIGRATION_GUIDE.md`  
**Agent Rules:** `.cursor/rules/agent-*.mdc`

---

**Default Rule: When in doubt → OpenAPI 3.0 + ogen**

