# Combat Service - Enterprise-Grade Real-Time Combat System

## Overview

Полная реализация enterprise-grade системы реального времени для боевых механик NECPGAME. Обеспечивает создание сессий боя, управление состоянием, обработку действий игроков и расчет урона с поддержкой до 10,000 одновременных боев.

## Core Features

### Combat Session Management
- **Session Creation**: Создание боевых сессий с настраиваемыми параметрами
- **Participant Management**: Добавление игроков и NPC в бой
- **State Synchronization**: Реал-тайм синхронизация состояния боя
- **Turn-Based Logic**: Управление очередностью ходов

### Action Processing
- **Attack Actions**: Расчет урона с учетом брони и критических ударов
- **Movement Actions**: Перемещение участников в 3D пространстве
- **Ability Usage**: Использование способностей с cooldown механикой
- **Status Effects**: Poison, bleeding, stunned и другие эффекты

### Damage Calculation Engine
- **Armor Reduction**: Реалистичное уменьшение урона броней
- **Critical Hits**: Случайные критические удары (1.5x урон)
- **Damage Types**: Поддержка различных типов урона
- **Status Effects**: Применение эффектов при уроне

## Architecture

### Service Layers

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   HTTP Handlers │────│ Combat Business │────│   In-Memory     │
│   (ogen-gen)    │    │    Logic        │    │   Storage       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
          │                       │                       │
          ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Action        │    │   Damage        │    │   Session       │
│  Processing     │    │  Calculation    │    │  Management     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Data Structures

#### CombatSession
```go
type CombatSession struct {
    ID             uuid.UUID
    GameMode       string
    Status         string // active, paused, completed, cancelled
    MaxParticipants int
    Participants   map[string]*CombatParticipant
    CreatedAt      time.Time
    UpdatedAt      time.Time
    RoundNumber    int
    TurnOrder      []string
    CurrentTurn    string
    Environment    *CombatEnvironment
    CombatLog      []*CombatEvent
    mutex          sync.RWMutex
}
```

#### CombatParticipant
```go
type CombatParticipant struct {
    ID          string
    Name        string
    Type        string // player, npc, enemy
    Health      *HealthStats
    Position    *Position
    Status      []string // poisoned, bleeding, stunned
    Inventory   []*CombatItem
    Abilities   []*CombatAbility
    JoinedAt    time.Time
    LastAction  time.Time
    IsActive    bool
}
```

## API Endpoints

### Combat Session Lifecycle

#### 1. Create Combat Session
```http
POST /combat/sessions
Content-Type: application/json

{
  "game_mode": "deathmatch",
  "max_participants": 8
}
```

**Response (201):**
```json
{
  "id": "uuid",
  "game_mode": "deathmatch",
  "status": "active",
  "max_participants": 8,
  "participants": [],
  "created_at": "2024-01-10T12:00:00Z",
  "round_number": 1,
  "turn_order": [],
  "current_turn": "",
  "environment": {
    "type": "urban",
    "weather": "clear",
    "time_of_day": "day"
  },
  "combat_log": []
}
```

#### 2. Get Combat Session
```http
GET /combat/sessions/{session_id}
```

**Response (200):** Полная информация о сессии

#### 3. Join Combat Session
```http
POST /combat/sessions/{session_id}/join
Content-Type: application/json

{
  "participant_id": "player123",
  "participant_name": "John Doe",
  "participant_type": "player"
}
```

#### 4. Execute Combat Action
```http
POST /combat/sessions/{session_id}/actions
Content-Type: application/json

{
  "participant_id": "player123",
  "action_type": "attack",
  "target_id": "enemy456",
  "position": {"x": 10.5, "y": 5.2, "z": 0}
}
```

**Response (200):**
```json
{
  "id": "event-uuid",
  "type": "attack",
  "timestamp": "2024-01-10T12:01:00Z",
  "participant": "player123",
  "target": "enemy456",
  "action": "attack",
  "damage": 27,
  "description": "John Doe attacked Enemy for 27 damage"
}
```

#### 5. Get Session State
```http
GET /combat/sessions/{session_id}/state
```

**Response (200):** Текущее состояние сессии с позициями всех участников

### Damage Calculation

#### Calculate Damage
```http
POST /combat/calculate-damage
Content-Type: application/json

{
  "base_damage": 50,
  "target_armor": 10,
  "damage_type": "physical",
  "critical_multiplier": 1.0
}
```

**Response (200):**
```json
{
  "actual_damage": 40,
  "critical_hit": false,
  "armor_reduction": 10,
  "damage_type": "physical",
  "status_effects": [],
  "calculated_at": "2024-01-10T12:02:00Z"
}
```

## Combat Mechanics

### Action Types

#### Attack Actions
- **Base Damage**: 20-30 HP (randomized)
- **Critical Chance**: 10% (1.5x multiplier)
- **Armor Reduction**: 10% per armor point
- **Minimum Damage**: Always at least 1 HP

#### Movement Actions
- **3D Positioning**: X, Y, Z coordinates
- **Collision Detection**: Environment-aware movement
- **Cover System**: Tactical positioning

#### Ability Actions
- **Cooldown System**: Time-based ability restrictions
- **Resource Costs**: Mana/stamina consumption
- **Area Effects**: Multi-target abilities

### Status Effects System

#### Supported Effects
- **Poison**: Damage over time
- **Bleeding**: Stackable damage
- **Stunned**: Action prevention
- **Burning**: Fire damage ticks
- **Frozen**: Movement/speed reduction

#### Effect Mechanics
```go
type StatusEffect struct {
    Type     string        // poison, bleed, stun
    Duration time.Duration // Effect duration
    Damage   int          // Damage per tick
    Interval time.Duration // Tick interval
    Stacks   int          // Stack count
}
```

## Performance Characteristics

### Benchmarks (Target)
- **P99 Latency**: <25ms per combat action
- **Memory Usage**: <100KB per active combat session
- **Concurrent Sessions**: 10,000+ simultaneous battles
- **Action Throughput**: 1,000+ actions/second per server

### Optimizations
- **In-Memory Storage**: Fast session state access
- **Mutex-Based Locking**: Thread-safe concurrent access
- **Event-Driven Updates**: Efficient state synchronization
- **Context Timeouts**: Prevents hanging operations

## Real-Time Features

### Combat Events
```go
type CombatEvent struct {
    ID          string
    Type        string    // attack, move, ability, death
    Timestamp   time.Time
    Participant string
    Target      string
    Action      string
    Damage      int
    Description string
}
```

### State Synchronization
- **Participant Positions**: Real-time 3D coordinates
- **Health Updates**: Instant HP/armor changes
- **Status Effects**: Live effect application
- **Turn Management**: Automatic turn progression

## Environment System

### Combat Environments
```go
type CombatEnvironment struct {
    Type        string          // urban, wilderness, building
    Weather     string          // clear, rain, snow, fog
    TimeOfDay   string          // day, night, dawn, dusk
    Cover       []*CoverObject
    Hazards     []*HazardObject
}
```

### Cover Mechanics
- **Hard Cover**: 50% damage reduction
- **Soft Cover**: 25% damage reduction
- **Flanking**: Damage bonuses for side attacks
- **Suppression**: Accuracy penalties

## AI Integration (Future)

### NPC Behavior
- **Aggressive AI**: Priority targeting system
- **Defensive AI**: Cover-seeking behavior
- **Tactical AI**: Flanking and positioning
- **Group AI**: Coordinated attacks

### Dynamic Difficulty
- **Adaptive Scaling**: Real-time difficulty adjustment
- **Player Performance**: Based on combat statistics
- **Group Composition**: Balanced encounters

## Monitoring & Analytics

### Combat Metrics
- **Session Duration**: Average battle length
- **Action Frequency**: Actions per minute per player
- **Damage Statistics**: DPS, healing, mitigation
- **Win/Loss Ratios**: Performance analytics

### Weapon Analytics
- **Usage Statistics**: Most popular weapons
- **Effectiveness**: Damage per shot ratios
- **Balance Metrics**: Weapon power scaling

## Future Enhancements

### Advanced Features
- **Crowd Simulation**: Mass combat scenarios
- **Destructible Environments**: Dynamic cover destruction
- **Vehicle Combat**: Mounted weapon systems
- **Co-op Missions**: Multiplayer objectives

### Performance Improvements
- **Redis Caching**: Session state persistence
- **Database Sharding**: Horizontal scaling support
- **Load Balancing**: Multi-server combat distribution
- **CDN Integration**: Global low-latency access

---

**This combat system provides enterprise-grade real-time battle mechanics with extensible architecture for future MMOFPS features and scaling to millions of concurrent players.**