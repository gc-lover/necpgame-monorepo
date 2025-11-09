---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:00
**api-readiness-notes:** Global State Events микрофича. Типы событий (10 категорий), структура событий, примеры. ~390 строк.
---

# Global State Events - Типы и структура событий

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 06:00  
**Приоритет:** КРИТИЧЕСКИЙ  
**Автор:** AI Brain Manager

**Микрофича:** Event types and structure  
**Размер:** ~390 строк ✅  

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Краткое описание

**Global State Events** - каталог всех типов событий и их структура.

**10 категорий событий:**
- ✅ PLAYER (50+ типов)
- ✅ QUEST (15 типов)
- ✅ COMBAT (10 типов)
- ✅ ECONOMY (15 типов)
- ✅ SOCIAL (10 типов)
- ✅ WORLD (15 типов)
- ✅ NPC (7 типов)
- ✅ TECHNOLOGY (5 типов)
- ✅ POLITICAL (7 типов)
- ✅ LEAGUE (5 типов)

---

## Типы событий (Event Types)

### 1. PLAYER EVENTS

```typescript
// Прогресс
PLAYER_CREATED
PLAYER_LEVELED_UP
PLAYER_ATTRIBUTE_INCREASED
PLAYER_SKILL_INCREASED
PLAYER_PERK_ACQUIRED
PLAYER_REBIRTH

// Экипировка
PLAYER_ITEM_EQUIPPED
PLAYER_ITEM_UNEQUIPPED
PLAYER_IMPLANT_INSTALLED
PLAYER_IMPLANT_REMOVED

// Перемещение
PLAYER_LOCATION_CHANGED
PLAYER_ZONE_ENTERED
PLAYER_ZONE_EXITED

// Статус
PLAYER_DIED
PLAYER_RESPAWNED
PLAYER_STATUS_EFFECT_APPLIED
PLAYER_CYBERPSYCHOSIS_STAGE_CHANGED
```

### 2. QUEST EVENTS

```typescript
QUEST_STARTED
QUEST_OBJECTIVE_UPDATED
QUEST_OBJECTIVE_COMPLETED
QUEST_DIALOGUE_NODE_ENTERED
QUEST_CHOICE_MADE
QUEST_SKILL_CHECK_PERFORMED
QUEST_BRANCH_ENTERED
QUEST_COMPLETED
QUEST_FAILED
QUEST_ABANDONED

// World Impact
QUEST_WORLD_STATE_CHANGED
QUEST_NPC_FATE_CHANGED
QUEST_TERRITORY_CHANGED
QUEST_FACTION_REPUTATION_CHANGED
```

### 3. COMBAT EVENTS

```typescript
COMBAT_SESSION_STARTED
COMBAT_DAMAGE_DEALT
COMBAT_DAMAGE_RECEIVED
COMBAT_ABILITY_USED
COMBAT_COMBO_EXECUTED
COMBAT_ENEMY_KILLED
COMBAT_PLAYER_KILLED
COMBAT_SESSION_ENDED

// Extract shooter
EXTRACT_ZONE_ENTERED
EXTRACT_LOOT_ACQUIRED
EXTRACT_EXTRACTED
EXTRACT_DIED_IN_ZONE
```

### 4. ECONOMY EVENTS

```typescript
ITEM_CRAFTED
ITEM_PURCHASED
ITEM_SOLD
ITEM_TRADED
CURRENCY_EXCHANGED
RESOURCE_GATHERED
MARKET_LISTING_CREATED
MARKET_LISTING_SOLD
AUCTION_BID_PLACED
AUCTION_WON

// Производство
PRODUCTION_CHAIN_STARTED
PRODUCTION_CHAIN_COMPLETED
RECIPE_DISCOVERED
```

### 5. SOCIAL EVENTS

```typescript
REPUTATION_CHANGED
RELATIONSHIP_CHANGED
NPC_HIRED
NPC_DISMISSED
NPC_RELATIONSHIP_STAGE_CHANGED
PLAYER_ORDER_CREATED
PLAYER_ORDER_ACCEPTED
PLAYER_ORDER_COMPLETED
MENTORSHIP_STARTED
MENTORSHIP_COMPLETED

// Романтика
ROMANCE_STAGE_ADVANCED
ROMANCE_QUEST_COMPLETED
ROMANCE_PARTNER_ABILITY_UNLOCKED
```

### 6. WORLD EVENTS

```typescript
WORLD_EVENT_TRIGGERED
WORLD_EVENT_STARTED
WORLD_EVENT_PLAYER_PARTICIPATED
WORLD_EVENT_COMPLETED
WORLD_EVENT_FAILED

// Территории
TERRITORY_ATTACKED
TERRITORY_DEFENDED
TERRITORY_CAPTURED
TERRITORY_CONTROL_CHANGED

// Фракции
FACTION_WAR_STARTED
FACTION_WAR_STAGE_COMPLETED
FACTION_WAR_ENDED
FACTION_POWER_CHANGED
FACTION_ALLIANCE_FORMED
FACTION_ALLIANCE_BROKEN
```

### 7. NPC EVENTS

```typescript
NPC_SPAWNED
NPC_DIED
NPC_FATE_CHANGED
NPC_LOCATION_CHANGED
NPC_STATE_CHANGED
NPC_DIALOGUE_TRIGGERED
NPC_VENDOR_INVENTORY_UPDATED
```

### 8. TECHNOLOGY EVENTS

```typescript
TECHNOLOGY_UNLOCKED
IMPLANT_TIER_UNLOCKED
BLACKWALL_STABILITY_CHANGED
CYBERSPACE_EXPANDED
RESEARCH_COMPLETED
```

### 9. POLITICAL EVENTS

```typescript
ELECTION_STARTED
ELECTION_VOTE_CAST
ELECTION_COMPLETED
LAW_CHANGED
MAYOR_ELECTED
ALLIANCE_FORMED
WAR_DECLARED
```

### 10. LEAGUE EVENTS

```typescript
LEAGUE_STARTED
LEAGUE_STAGE_CHANGED
LEAGUE_ENDED
ERA_CHANGED
SEASON_RESET
```

---

## Структура события

```json
{
  "eventId": "uuid",
  "eventType": "QUEST_CHOICE_MADE",
  "aggregateType": "QUEST",
  "aggregateId": "quest-id",
  "eventVersion": 1,
  "correlationId": "uuid",
  "causationId": "previous-event-uuid",
  
  "eventData": { /* специфичные данные */ },
  "metadata": { /* версии, IP, User-Agent */ },
  
  "serverId": "server-01",
  "playerId": "player-uuid",
  "sessionId": "session-uuid",
  "eventTimestamp": "2025-11-07T06:00:00Z",
  
  "stateChanges": { /* изменения состояния */ },
  "affectedPlayers": ["player-uuid"]
}
```

---

## Связанные документы

- `.BRAIN/05-technical/global-state/global-state-core.md` - Архитектура (микрофича 1/5)
- `.BRAIN/05-technical/global-state/global-state-management.md` - State Management (микрофича 3/5)
- `.BRAIN/05-technical/global-state/global-state-sync.md` - Синхронизация (микрофича 4/5)
- `.BRAIN/05-technical/global-state/global-state-operations.md` - Operations (микрофича 5/5)

---

## История изменений

- **v1.0.0 (2025-11-07 06:00)** - Микрофича 2/5: Global State Events (split from global-state-system.md)
