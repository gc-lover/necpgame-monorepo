# API Integration Guide

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:01

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 00:01

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Краткое описание

Гайд по интеграции системы квестового графа и world state с API.

---

## API Endpoints (рекомендуемые)

### Quest System

```yaml
# Get available quests
GET /api/v1/narrative/quests/available
Query params:
  - characterId: UUID (required)
  - era: string (optional)
  - type: string (optional) # main, side, faction
Response:
  - quests: Array<QuestSummary>
  - total: number

# Get quest details
GET /api/v1/narrative/quests/{questId}
Query params:
  - characterId: UUID (required)
Response:
  - quest: QuestDetail
  - branches: Array<QuestBranch>
  - dialogue_tree: DialogueTree
  - available: boolean
  - blocked_by: Array<string> (if not available)

# Make quest choice
POST /api/v1/narrative/quests/{questId}/choice
Body:
  - characterId: UUID
  - choiceId: string
  - nodeId: number (dialogue node)
Response:
  - success: boolean
  - consequences: Object
  - unlocked_quests: Array<string>
  - blocked_quests: Array<string>
  - reputation_changes: Object
  - flags_set: Array<string>
```

### World State System

```yaml
# Get combined world state
GET /api/v1/narrative/world-state
Query params:
  - characterId: UUID (required)
  - serverId: string (required)
Response:
  - personal_state: Object
  - server_state: Object
  - faction_state: Object (if in faction)
  - combined_view: Object

# Cast vote for server state
POST /api/v1/narrative/world-state/vote
Body:
  - characterId: UUID
  - serverId: string
  - stateKey: string
  - voteValue: any
Response:
  - vote_recorded: boolean
  - current_votes: number
  - threshold_required: number
  - status: "pending" | "active"

# Get territory control
GET /api/v1/narrative/territory-control
Query params:
  - serverId: string (required)
  - territoryId: string (optional)
Response:
  - territories: Array<TerritoryControl>
```

---

## Data Models

### Quest

```typescript
interface Quest {
  id: string;
  name: string;
  description: string;
  type: QuestType;
  era: string;
  minLevel: number;
  requiredQuests: string[];
  requiredFlags: Flag[];
  hasBranches: boolean;
  dialogueTreeRoot: number | null;
  rewards: Rewards;
}

enum QuestType {
  MAIN = "MAIN",
  SIDE = "SIDE",
  FACTION = "FACTION",
  DAILY = "DAILY",
  WEEKLY = "WEEKLY",
  EVENT = "EVENT",
  DYNAMIC = "DYNAMIC"
}
```

### World State

```typescript
interface WorldState {
  personal: Map<string, any>;
  server: Map<string, any>;
  faction: Map<string, any> | null;
}

interface ServerWorldState {
  serverId: string;
  stateKey: string;
  stateValue: any;
  playerVotes: number;
  thresholdRequired: number;
  status: "pending" | "active";
}
```

### Player Flag

```typescript
interface PlayerFlag {
  characterId: UUID;
  flagKey: string;
  flagValue: any;
  setByQuest: string | null;
  createdAt: Date;
}
```

---

## WebSocket Events

### Real-time updates

```yaml
# World state changed
Event: world_state_changed
Payload:
  serverId: string
  stateKey: string
  oldValue: any
  newValue: any
  timestamp: Date

# Quest unlocked
Event: quest_unlocked
Payload:
  characterId: UUID
  questId: string
  unlockedBy: string (quest or event)
  timestamp: Date

# Territory control changed
Event: territory_control_changed
Payload:
  serverId: string
  territoryId: string
  oldFaction: string | null
  newFaction: string
  controlPercentage: number
```

---

## Error Handling

### Common Errors

```typescript
// Quest not available
{
  "error": "QUEST_NOT_AVAILABLE",
  "message": "Quest MQ-2020-002 not available",
  "reasons": [
    "Prerequisites not met: MQ-2020-001",
    "Level too low: required 5, current 3"
  ]
}

// Quest blocked
{
  "error": "QUEST_BLOCKED",
  "message": "Quest SQ-2030-VB-001 permanently blocked",
  "blocked_by": "MQ-2020-005 (choice: NetWatch)",
  "recovery_possible": false
}

// Choice invalid
{
  "error": "CHOICE_INVALID",
  "message": "Choice A3 not available",
  "required_skill": "Hacking 16 (current: 12)"
}
```

---

## Linked Documents

- [Developer Guide](./developer-guide.md)
- [Quest Tables](../../phase4-database/tables/quest-tables-extended.sql)
- [Architecture](../../phase3-event-matrix/architecture.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:01) - API integration guide
