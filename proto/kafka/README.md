# Kafka Event Schema Specifications

**Issue:** #2216
**Agent:** API Designer
**Purpose:** Enterprise-grade event-driven architecture specifications for NECPGAME MMOFPS RPG

## Overview

This directory contains JSON Schema specifications for Apache Kafka events used in the microservices event-driven architecture. Events enable asynchronous communication between services, real-time analytics, and maintain data consistency across domains.

## Performance Requirements

- **Throughput:** 100k events/sec sustained
- **Latency:** P99 <50ms end-to-end processing
- **Message Size:** ~1KB average (optimized for 256 bytes/event)
- **Retention:** 7 days game events, 30 days analytics, 90 days system events

## Directory Structure

```
proto/kafka/
├── README.md                 # This file
├── schemas/                  # JSON Schema definitions
│   ├── core/                # Common schemas (base event, metadata)
│   ├── combat/              # Combat domain events
│   ├── economy/             # Economy domain events
│   ├── social/              # Social domain events
│   ├── system/              # System/infrastructure events
│   └── domains/             # Cross-domain events
├── topics/                   # Topic definitions and configurations
└── examples/                 # Sample events for testing/validation
```

## Event Schema Design Principles

### 1. Memory Optimization (Level 3)
- **Field Ordering:** Large types first (strings, arrays) → small types last (booleans, integers)
- **Expected Savings:** 30-50% memory reduction in generated Go structs
- **Target Size:** 256 bytes per event average

### 2. Enterprise-Grade Structure
```json
{
  "event_id": "uuid",
  "event_type": "domain.action.subaction",
  "timestamp": "RFC3339",
  "version": "1.0.0",
  "source": "service.name",
  "correlation_id": "uuid",
  "session_id": "uuid",
  "player_id": "uuid",
  "data": {},
  "metadata": {
    "priority": "normal",
    "ttl": "7d",
    "retry_count": 0
  }
}
```

### 3. Event Type Naming Convention
- Format: `{domain}.{entity}.{action}`
- Examples:
  - `combat.session.start`
  - `economy.trade.execute`
  - `social.guild.join`
  - `system.service.health`

## Core Event Categories

### Combat Events (Hot Path - 20k EPS)
- Session management (start/end/join/leave)
- Player actions (attack/defend/ability)
- Damage calculation and application
- Anti-cheat validation events

### Economy Events
- Trade executions and listings
- Currency transactions
- Auction activities
- Market data updates

### Social Events
- Guild management
- Party formations
- Chat messages (filtered)
- Reputation changes

### System Events
- Service health/status
- Infrastructure monitoring
- Audit/logging events
- Performance metrics

## Schema Validation

All schemas are validated using JSON Schema Draft 7 and must pass:
```bash
# Validate all schemas
find proto/kafka/schemas -name "*.json" -exec jsonschema {} \;

# Bundle for distribution
jsonschema-bundle proto/kafka/schemas -o kafka-events-schema.json
```

## Code Generation

### Go Structs (Backend Services)
```bash
# Generate Go structs from JSON schemas
for schema in proto/kafka/schemas/**/*.json; do
  json-schema-codegen $schema --lang go --out services/
done
```

### TypeScript Types (Analytics Services)
```bash
# Generate TypeScript interfaces
json2ts proto/kafka/schemas/**/*.json > types/kafka-events.d.ts
```

## Topic Configuration

Topics are defined in `topics/` directory with partitioning and retention policies:

- **game.combat.events:** 24 partitions, 7d retention, high priority
- **game.economy.events:** 12 partitions, 30d retention, normal priority
- **game.social.events:** 8 partitions, 30d retention, normal priority
- **game.system.events:** 6 partitions, 90d retention, low priority

## Security & Compliance

- **Encryption:** AES-256 at rest, TLS 1.3 in transit
- **Authentication:** mTLS certificate-based
- **Authorization:** Role-based ACLs per topic
- **Audit:** All event access logged for GDPR/CCPA compliance

## Monitoring & Observability

- **Metrics:** Throughput, latency, error rates per topic/service
- **Tracing:** Distributed tracing with correlation IDs
- **Alerts:** Consumer lag > 10k messages, error rate > 5%

## Development Workflow

1. **Define Event:** Create schema in appropriate domain folder
2. **Validate:** Run JSON schema validation
3. **Generate Code:** Update service structs/interfaces
4. **Test:** Publish sample events to staging topics
5. **Deploy:** Roll out schema changes with backward compatibility

## Backward Compatibility

- **Additive Changes Only:** New fields optional, never remove existing
- **Versioning:** Schema version in event metadata
- **Migration:** Gradual rollout with feature flags
- **Breaking Changes:** New topic for incompatible schemas

---

*Kafka Event Schema Specifications by API Designer Agent for Issue #2216*
