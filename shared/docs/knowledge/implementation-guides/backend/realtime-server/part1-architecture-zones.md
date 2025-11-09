# Realtime Server - Part 1: Architecture & Zones

---

- **Status:** queued
- **Last Updated:** 2025-11-07 17:05
---

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:12  
**api-readiness:** ready

[Навигация](./README.md) | [Part 2 →](./part2-protocol-optimization.md)

---

## Краткое описание

**Real-Time Server Architecture** - архитектура для real-time геймплея в MMORPG. Синхронизация позиций, обработка действий, зоны/инстансы, оптимизация трафика.

**Ключевые возможности:**
- ✅ Game Server Instances (масштабируемые)
- ✅ Zone/Instance Management
- ✅ Player Position Synchronization
- ✅ Network Protocol (TCP + WebSocket)
- ✅ Lag Compensation
- ✅ Interest Management (Area of Interest)
- ✅ Bandwidth Optimization

---

## Архитектура High-Level

```
┌──────────────────────────────────────────────────────────────┐
│                     CLIENTS (Players)                         │
│  WebSocket connections (TCP) for game state                   │
└──────────────────────────────┬───────────────────────────────┘
                               │
                               ↓
┌──────────────────────────────────────────────────────────────┐
│                    API GATEWAY / LOAD BALANCER               │
│  - Route to appropriate Game Server                          │
│  - WebSocket sticky sessions                                 │
└──────────────────────────────┬───────────────────────────────┘
                               │
        ┌──────────────────────┴──────────────────────┐
        │                   │                   │
        ↓                   ↓                   ↓
┌───────────────┐   ┌───────────────┐   ┌───────────────┐
│ GAME SERVER   │   │ GAME SERVER   │   │ GAME SERVER   │
│   Instance 1  │   │   Instance 2  │   │   Instance 3  │
│               │   │               │   │               │
│ Zones:        │   │ Zones:        │   │ Zones:        │
│ - Watson      │   │ - Westbrook   │   │ - City Center │
│ - Japantown   │   │ - Heywood     │   │ - Pacifica    │
└───────┬───────┘   └───────┬───────┘   └───────┬───────┘
        │                  │                   │
        └──────────────────┴───────────────────┘
                          │
                          ↓
        ┌──────────────────────────────────────────────┐
        │        SHARED SERVICES                       │
        │  - Redis (state cache)                       │
        │  - PostgreSQL (persistent state)             │
        │  - Event Bus (inter-server events)           │
        │  - Global State Manager                      │
        └──────────────────────────────────────────────┘
```

---

## Game Server Instance

### Концепция

**Game Server Instance** - процесс, который обрабатывает gameplay для набора зон и игроков.

**Характеристики:**
- Обрабатывает 1-5 зон (zones)
- Поддерживает 500-2000 concurrent players
- Обновляет game state 20-60 раз в секунду (tick rate)
- Независимый процесс (может падать без влияния на другие)

### Структура

```java
@Component
public class GameServerInstance {
    
    private final String instanceId;
    private final Set<Zone> zones = new ConcurrentHashMap<>();
    private final Map<UUID, PlayerState> activePlayers = new ConcurrentHashMap<>();
    
    private final ScheduledExecutorService gameLoop;
    private final int TICK_RATE = 20; // 20 ticks/sec = 50ms per tick
    
    @PostConstruct
    public void start() {
        // Запустить game loop
        gameLoop.scheduleAtFixedRate(
            this::tick,
            0,
            1000 / TICK_RATE, // 50ms
            TimeUnit.MILLISECONDS
        );
        
        log.info("Game Server Instance {} started with {} zones", 
            instanceId, zones.size());
    }
    
    private void tick() {
        long startTime = System.currentTimeMillis();
        
        // 1. Process input (player actions)
        processPlayerInput();
        
        // 2. Update game state
        updateGameState();
        
        // 3. Run physics/collision
        updatePhysics();
        
        // 4. Update AI (NPCs)
        updateAI();
        
        // 5. Send updates to clients
        broadcastStateUpdates();
        
        // 6. Cleanup
        cleanup();
        
        long elapsed = System.currentTimeMillis() - startTime;
        
        if (elapsed > 50) {
            log.warn("Tick took {}ms (>50ms), performance issue!", elapsed);
        }
    }
}
```

### Game Loop Breakdown

**Tick: 50ms (20 ticks/sec)**

```
0ms:  Start tick
  ↓
2ms:  Process player input (movement, actions)
  ↓
10ms: Update game state (positions, health, cooldowns)
  ↓
15ms: Physics/Collision detection
  ↓
20ms: Update AI (NPCs behavior)
  ↓
30ms: Broadcast state to clients (delta updates)
  ↓
35ms: Cleanup (remove disconnected players, etc)
  ↓
40ms: End tick (sleep until next tick)
```

**Если tick > 50ms:**
- Log warning
- Skip некритичные операции (AI updates)
- Уменьшить broadcast frequency

---

## Zone Management

### Зоны (Zones)

**Zone** - область игрового мира (район Night City)

**Примеры зон:**
- `nightCity.watson` (Watson district)
- `nightCity.westbrook` (Westbrook district)
- `nightCity.cityCenter` (City Center)
- `badlands.rockyRidge` (Badlands region)

**Характеристики зоны:**
- Max players: 100-200 per zone
- Size: 1000x1000 meters (1km²)
- Subdivided into cells (100x100m each)

### Таблица `zones`

```sql
CREATE TABLE zones (
    id VARCHAR(100) PRIMARY KEY,
    zone_name VARCHAR(200) NOT NULL,
    zone_type VARCHAR(50) NOT NULL, -- CITY, BADLANDS, DUNGEON, RAID, PVP
    
    -- Game Server assignment
    assigned_server_id VARCHAR(100),
    
    -- Capacity
    max_players INTEGER NOT NULL DEFAULT 100,
    current_players INTEGER DEFAULT 0,
    
    -- Boundaries
    min_x DECIMAL(10,2),
    max_x DECIMAL(10,2),
    min_y DECIMAL(10,2),
    max_y DECIMAL(10,2),
    min_z DECIMAL(10,2),
    max_z DECIMAL(10,2),
    
    -- Settings
    is_pvp_enabled BOOLEAN DEFAULT FALSE,
    is_safe_zone BOOLEAN DEFAULT TRUE,
    weather VARCHAR(50) DEFAULT 'CLEAR',
    time_of_day VARCHAR(20) DEFAULT 'DAY',
    
    -- Status
    status VARCHAR(20) DEFAULT 'ONLINE', -- ONLINE, MAINTENANCE, OFFLINE
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_zone_server FOREIGN KEY (assigned_server_id) 
        REFERENCES game_server_instances(id)
);

CREATE INDEX idx_zones_server ON zones(assigned_server_id);
CREATE INDEX idx_zones_status ON zones(status);
```

### Zone Cell (Area of Interest)

**Проблема:** Не нужно отправлять ALL player positions ALL players (дорого)

**Решение:** Zone Cells (Interest Management)

```
Zone (1000x1000m) разбита на 100 cells (100x100m each)

Cell[0,0]   Cell[1,0]   Cell[2,0]   ...   Cell[9,0]
Cell[0,1]   Cell[1,1]   Cell[2,1]   ...   Cell[9,1]
...
Cell[0,9]   Cell[1,9]   Cell[2,9]   ...   Cell[9,9]
```

**Игрок видит только:**
- Свою cell
- 8 соседних cells (3x3 grid)

**Пример:**
```
Player в Cell[5,5] видит:
Cell[4,4] | Cell[5,4] | Cell[6,4]
Cell[4,5] | Cell[5,5] | Cell[6,5]
Cell[4,6] | Cell[5,6] | Cell[6,6]

= Максимум 9 cells
```

### Реализация

```java
@Service
public class ZoneManager {
    
    private static final int CELL_SIZE = 100; // 100 meters
    
    public Set<UUID> getPlayersInInterestArea(Zone zone, Vector3 position) {
        // Определить текущую cell
        int cellX = (int) (position.x / CELL_SIZE);
        int cellY = (int) (position.y / CELL_SIZE);
        
        Set<UUID> players = new HashSet<>();
        
        // 3x3 grid вокруг игрока
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int neighborX = cellX + dx;
                int neighborY = cellY + dy;
                
                String cellKey = zone.getId() + ":" + neighborX + ":" + neighborY;
                
                // Получить игроков в этой cell из Redis
                Set<String> cellPlayers = redis.opsForSet().members(
                    "zone_cell:" + cellKey
                );
                
                if (cellPlayers != null) {
                    cellPlayers.forEach(p -> players.add(UUID.fromString(p)));
                }
            }
        }
        
        return players;
    }
    
    public void updatePlayerCell(UUID playerId, Zone zone, Vector3 position) {
        // Удалить из старой cell
        String oldCellKey = getPlayerCellKey(playerId);
        if (oldCellKey != null) {
            redis.opsForSet().remove("zone_cell:" + oldCellKey, playerId.toString());
        }
        
        // Добавить в новую cell
        int cellX = (int) (position.x / CELL_SIZE);
        int cellY = (int) (position.y / CELL_SIZE);
        String newCellKey = zone.getId() + ":" + cellX + ":" + cellY;
        
        redis.opsForSet().add("zone_cell:" + newCellKey, playerId.toString());
        redis.opsForValue().set("player_cell:" + playerId, newCellKey);
    }
}
```

---

## Player State Synchronization

### Player State

**Что синхронизируется:**
```json
{
  "playerId": "uuid",
  "position": {"x": 1234.56, "y": 5678.90, "z": 10.5},
  "rotation": {"yaw": 45.0, "pitch": 0.0, "roll": 0.0},
  "velocity": {"x": 5.0, "y": 0.0, "z": 0.0},
  "animation": "RUNNING",
  "health": 850,
  "maxHealth": 1000,
  "status": ["BUFF_SPEED", "DEBUFF_POISON"],
  "equipment": {
    "weapon": "mantis_blades",
    "armor": "corpo_suit"
  },
  "currentAction": "ATTACKING",
  "targetId": "npc-uuid",
  "timestamp": 1699296000000
}
```

### Synchronization Protocol

**Client → Server: Input Messages**
```json
{
  "type": "PLAYER_INPUT",
  "sequence": 12345,
  "input": {
    "move": {"forward": 1.0, "right": 0.0},
    "rotation": {"yaw": 45.0},
    "action": "SHOOT",
    "actionTarget": "npc-uuid"
  },
  "timestamp": 1699296000000
}
```

**Server → Client: State Updates**
```json
{
  "type": "STATE_UPDATE",
  "tick": 54321,
  "players": [
    {
      "id": "uuid-1",
      "p": [1234.5, 5678.9, 10.5],
      "r": [45.0, 0.0],
      "a": "RUN"
    },
    {
      "id": "uuid-2",
      "p": [1200.0, 5700.0, 10.0],
      "r": [90.0, 0.0],
      "a": "IDLE"
    }
  ],
  "npcs": [...],
  "timestamp": 1699296001000
}
```

### Update Frequency

**Зависит от типа action:**
```
Combat (в бою):          60 updates/sec (16ms)
Movement (движение):     20 updates/sec (50ms)
Idle (стоит на месте):   5 updates/sec (200ms)
AFK:                     1 update/sec (1000ms)
```

**Реализация:**
```java
private int getUpdateFrequency(PlayerState state) {
    if (state.isInCombat()) {
        return 60; // High frequency
    } else if (state.isMoving()) {
        return 20; // Normal
    } else if (state.isIdle()) {
        return 5; // Low
    } else {
        return 1; // Minimal
    }
}
```

---

[Part 2: Protocol & Optimization →](./part2-protocol-optimization.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:12) - Создан с полным Java кодом (архитектура, zones, sync)
- v1.0.0 (2025-11-06) - Создан
