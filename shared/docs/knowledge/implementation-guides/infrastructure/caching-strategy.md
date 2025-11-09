---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Caching Strategy - Стратегия кэширования

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 05:20  
**Приоритет:** критический (Performance)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 05:20
**api-readiness-notes:** Многоуровневая стратегия кэширования. L1 (CDN static), L2 (Redis hot data), L3 (app-level), TTL policies, invalidation strategy, cache warming. Performance critical!

---

## Краткое описание

Многоуровневая стратегия кэширования для оптимизации производительности.

**Микрофича:** Caching (Redis, application cache, CDN, TTL)

---

## 🚀 Уровни кэширования

### Level 1: CDN Cache (Static Assets)

**Что кэшируется:**
- Images (textures, UI icons)
- 3D models
- Audio files
- Game client (patches)

**TTL:** 30 days  
**Invalidation:** Version-based (asset-v1.2.3.png)

### Level 2: Redis Cache (Application Data)

**Что кэшируется:**
```
Player session data:
- TTL: Session duration (15 min idle)
- Key: session:{sessionId}

Player profile:
- TTL: 1 hour
- Key: player:{playerId}:profile

Market order book:
- TTL: 1 minute (very short, high update frequency)
- Key: market:orderbook:{itemId}

Quest data:
- TTL: 24 hours
- Key: quest:{questId}:data
```

### Level 3: Application Cache (In-Memory)

**Что кэшируется:**
```
Static game data:
- Item templates (weapons, armor)
- NPC data (stats, dialogues)
- Achievement definitions
- Quest templates

TTL: Until server restart or manual invalidation
```

---

## 🔑 Cache Keys

**Pattern:**
```
{service}:{entity}:{id}:{field}

Examples:
player:123e4567:profile
market:orderbook:health-booster
quest:main-001:dialogue-tree
stock:ARSK:price
```

---

## ⏰ TTL Strategy

```
Static data: 24 hours - 7 days
User data: 5 minutes - 1 hour
Real-time data: 10 seconds - 1 minute
Session data: Session duration

Examples:
player:session → 15 minutes (idle timeout)
market:orderbook → 1 minute (fast updates)
quest:definition → 24 hours (rarely changes)
item:template → 7 days (very static)
```

---

## 🔄 Cache Invalidation

**Strategies:**

**1. Time-based (TTL):**
```
Set TTL when caching
Redis auto-expires
```

**2. Event-based:**
```
Player levels up:
→ Invalidate cache: player:{id}:profile
→ Invalidate cache: leaderboard:level:global

Market order filled:
→ Invalidate cache: market:orderbook:{itemId}
```

**3. Manual:**
```
Admin updates quest:
→ Clear cache: quest:{id}:*
```

---

## 📊 Cache Hit Ratio

**Target:**
```
Level 1 (CDN): 95%+ hit rate
Level 2 (Redis): 80%+ hit rate
Level 3 (App): 90%+ hit rate
```

**Monitoring:**
```
Redis INFO:
keyspace_hits: 1,234,567
keyspace_misses: 123,456
hit_rate: 90.9%
```

---

## 🗄️ Redis Structure

```
Redis instances:

redis-session: Session data
- High memory
- Short TTL
- Persistence: AOF

redis-cache: Application cache
- Medium memory
- Variable TTL
- Persistence: RDB

redis-realtime: Real-time data (positions, matchmaking)
- Low memory
- Very short TTL
- No persistence (ephemeral)
```

---

## 🔗 Связанные документы

- `database-architecture.md`
- `api-gateway-architecture.md`

---

## История изменений

- v1.0.0 (2025-11-06 23:00) - Создание caching стратегии
