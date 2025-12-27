<!-- Issue: #140875135 -->
# Анализ Backend Архитектуры Системы Клановых Войн

## Обзор

Анализ архитектуры системы клановых войн с точки зрения backend реализации. Оценка технической реализуемости, производительности, масштабируемости и интеграций.

## Текущая Архитектура (из clan-war-system-architecture.yaml)

### Компоненты Системы

**1. War Manager** (world-service-go модуль clan-war)
- **Backend Responsibility:** Управление жизненным циклом войн, валидация требований, ACID транзакции
- **Technical Stack:** Go, PostgreSQL, Redis для кэширования
- **Performance:** Event-based operations, strong consistency

**2. War Phase Controller** (world-service-go модуль clan-war-phases)
- **Backend Responsibility:** Таймеры фаз, автоматические переходы, event publishing
- **Technical Stack:** Go timers, cron-like scheduling, Event Bus
- **Performance:** Background processing, minimal latency impact

**3. Territory Manager** (world-service-go модуль clan-war-territory)
- **Backend Responsibility:** Управление владением территориями, ресурсами, сложностью осад
- **Technical Stack:** Spatial queries, PostgreSQL PostGIS, Redis caching
- **Performance:** Read-heavy workload, cached territory states

**4. Battle Manager** (world-service-go модуль clan-war-battle)
- **Backend Responsibility:** Создание PvP зон, трекинг битв, интеграция с combat system
- **Technical Stack:** Go channels, WebSocket integration, Event Bus
- **Performance:** Real-time operations, 20-60 Hz updates

**5. Score Calculator** (world-service-go модуль clan-war-scoring)
- **Backend Responsibility:** Агрегация очков, взвешивание, определение победителя
- **Technical Stack:** PostgreSQL aggregations, Redis counters, atomic operations
- **Performance:** Write-heavy during battles, read-heavy for queries

**6. Reward Distributor** (world-service-go модуль clan-war-rewards)
- **Backend Responsibility:** Распределение наград, интеграция с economy service
- **Technical Stack:** Distributed transactions, Saga pattern, Event Bus
- **Performance:** Batch operations, eventual consistency

**7. Alliance Manager** (world-service-go модуль clan-war-alliance)
- **Backend Responsibility:** Управление союзами, приглашения, ограничения
- **Technical Stack:** Relationship modeling, validation logic, notifications
- **Performance:** Social operations, moderate load

**8. War Event Handler** (world-service-go модуль clan-war-events)
- **Backend Responsibility:** Event publishing/consuming, notifications, real-time updates
- **Technical Stack:** Event Bus (NATS/Kafka), WebSocket push, pub/sub patterns
- **Performance:** High-throughput event processing

## Backend Реализуемость

### ✅ Положительные Аспекты

**1. Четкое Разделение Ответственностей**
- Каждый компонент имеет single responsibility
- SOLID принципы соблюдены
- Легко тестировать и поддерживать

**2. Event-Driven Architecture**
- Outbox Pattern для гарантированной доставки
- Saga Pattern для распределенных транзакций
- CQRS для разделения команд и запросов

**3. Performance Optimizations**
- Struct alignment hints для оптимизации памяти
- Context timeouts на всех операциях
- Object pooling для снижения GC нагрузки

**4. Data Consistency Strategy**
- Strong consistency для критичных операций
- Eventual consistency для уведомлений
- ACID транзакции для финансовых операций

### ⚠️ Технические Вызовы

**1. Сложность Распределенных Транзакций**
```go
// Пример Saga Pattern для объявления войны
type DeclareWarSaga struct {
    steps []SagaStep
}

func (s *DeclareWarSaga) Execute(ctx context.Context, war *ClanWar) error {
    // Step 1: Validate clan requirements (Guild Service)
    // Step 2: Create war record (World Service)
    // Step 3: Schedule phases (War Phase Controller)
    // Step 4: Notify participants (Notification Service)
    // Compensation: Rollback on failure
}
```
**Решение:** Реализовать Saga orchestrator с compensation actions

**2. Real-time Battle Synchronization**
```go
// Пример проблемы concurrent updates
func (bm *BattleManager) UpdateBattleScore(battleID string, playerID string, points int) error {
    // Race condition при одновременных обновлениях от разных серверов
    // Решение: Optimistic locking + conflict resolution
}
```
**Решение:** Version fields, conflict resolution strategies

**3. Spatial Queries Performance**
```go
// Territory proximity queries могут быть тяжелыми
func (tm *TerritoryManager) GetNearbyTerritories(pos Vector3, radius float32) ([]*Territory, error) {
    // PostGIS queries могут быть медленными при большом количестве территорий
    // Решение: Spatial indexing + Redis caching
}
```
**Решение:** Redis geospatial indexes для hot territories

## Производительность и Масштабируемость

### Load Estimation (MMORPG 10k+ игроков)

**War Operations:**
- Объявление войны: 1-5/hour (низкая нагрузка)
- Phase transitions: 10-50/day (планируемые события)
- Territory captures: 10-100/hour (PvP активность)

**Battle Operations:**
- Создание битв: 5-20/minute
- Score updates: 100-500/second (во время активных битв)
- State sync: 20-60 Hz per battle (высокая нагрузка)

**Query Operations:**
- War status: 100-1000/second
- Territory info: 50-200/second
- Player scores: 200-500/second

### Database Performance Strategy

**Partitioning Strategy:**
```sql
-- Time-based partitioning для исторических данных
CREATE TABLE clan_war_scores_y2024m12 PARTITION OF clan_war_scores
    FOR VALUES FROM ('2024-12-01') TO ('2025-01-01');

-- Hash partitioning по war_id для распределения нагрузки
CREATE TABLE clan_war_participants PARTITION BY HASH (war_id);
```

**Indexing Strategy:**
```sql
-- Composite indexes для hot queries
CREATE INDEX idx_war_scores_war_player ON clan_war_scores (war_id, player_id);
CREATE INDEX idx_territory_owner_active ON territories (owner_clan_id) WHERE is_active = true;

-- Spatial indexes для территорий
CREATE INDEX idx_territories_location ON territories USING gist (location);
```

### Caching Strategy

**Redis Multi-layer Caching:**
```go
type WarCache struct {
    // Hot data: Active wars, current scores
    activeWars   *redis.Client // TTL: 5 min
    // Warm data: Territory states, alliances
    territoryStates *redis.Client // TTL: 15 min
    // Cold data: Historical data, leaderboards
    historicalData *redis.Client // TTL: 1 hour
}
```

**Cache Invalidation:**
```go
func (wc *WarCache) InvalidateWar(warID string) {
    // Pub/Sub pattern для distributed invalidation
    wc.redis.Publish("war:invalidate", warID)
}
```

## Рекомендации по Реализации

### Phase 1: Core Infrastructure (3 недели)

**1. Database Schema & Migrations**
```sql
-- Основные таблицы
CREATE TABLE clan_wars (
    id UUID PRIMARY KEY,
    attacker_clan_id UUID NOT NULL REFERENCES guilds(id),
    defender_clan_id UUID NOT NULL REFERENCES guilds(id),
    status VARCHAR(20) NOT NULL, -- DECLARED, PREPARATION, ACTIVE, ENDING, ENDED
    phase_started_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT NOW(),
    winner_clan_id UUID REFERENCES guilds(id)
);

CREATE TABLE clan_war_territories (
    id UUID PRIMARY KEY,
    war_id UUID NOT NULL REFERENCES clan_wars(id),
    territory_id UUID NOT NULL, -- FK to world.territories
    owner_clan_id UUID REFERENCES guilds(id),
    captured_at TIMESTAMP,
    defense_level INTEGER DEFAULT 1
);
```

**2. Event Schemas**
```go
type WarDeclaredEvent struct {
    WarID           string    `json:"war_id"`
    AttackerClanID  string    `json:"attacker_clan_id"`
    DefenderClanID  string    `json:"defender_clan_id"`
    Territories     []string  `json:"territories"`
    DeclaredAt      time.Time `json:"declared_at"`
}

type TerritoryCapturedEvent struct {
    WarID       string    `json:"war_id"`
    TerritoryID string    `json:"territory_id"`
    ClanID      string    `json:"clan_id"`
    CapturedAt  time.Time `json:"captured_at"`
    Points      int       `json:"points"`
}
```

### Phase 2: Service Implementation (4 недели)

**War Manager Implementation:**
```go
type WarManager struct {
    db      *sql.DB
    redis   *redis.Client
    eventBus EventPublisher
    logger  *zap.Logger
}

func (wm *WarManager) DeclareWar(ctx context.Context, req *DeclareWarRequest) (*ClanWar, error) {
    // 1. Validate requirements (clan size, cooldown)
    // 2. Create war record in transaction
    // 3. Schedule phases
    // 4. Publish WarDeclaredEvent
    // 5. Return war details
}
```

**Score Calculator with Optimizations:**
```go
type ScoreCalculator struct {
    db         *sql.DB
    redis      *redis.Client
    batchSize  int
    workerPool *WorkerPool
}

func (sc *ScoreCalculator) AddScore(ctx context.Context, warID, playerID string, points int) error {
    // Atomic increment in Redis for performance
    key := fmt.Sprintf("war:%s:scores", warID)
    return sc.redis.HIncrBy(ctx, key, playerID, points).Err()
}

func (sc *ScoreCalculator) FlushScores(ctx context.Context, warID string) error {
    // Batch flush to PostgreSQL
    scores := sc.redis.HGetAll(ctx, fmt.Sprintf("war:%s:scores", warID))
    // Bulk insert/update in transaction
}
```

### Phase 3: Integration & Testing (3 недели)

**Event-Driven Integration:**
```go
type WarEventHandler struct {
    warManager       *WarManager
    battleManager    *BattleManager
    scoreCalculator  *ScoreCalculator
    rewardDistributor *RewardDistributor
}

func (weh *WarEventHandler) HandleCombatKill(event *CombatKillEvent) {
    // Calculate points based on kill
    points := weh.calculateKillPoints(event)
    // Add to war score
    weh.scoreCalculator.AddScore(event.WarID, event.KillerID, points)
}

func (weh *WarEventHandler) HandleTerritoryCapture(event *TerritoryCaptureEvent) {
    // Award territory control points
    points := weh.calculateTerritoryPoints(event)
    weh.scoreCalculator.AddScore(event.WarID, event.CapturingClanID, points)
}
```

### Performance Optimizations

**Struct Alignment (30-50% memory savings):**
```go
// Optimized for struct alignment (large → small)
type ClanWar struct {
    ID               string    `db:"id"`               // string (16 bytes)
    AttackerClanID   string    `db:"attacker_clan_id"` // string (16 bytes)
    DefenderClanID   string    `db:"defender_clan_id"` // string (16 bytes)
    Territories      []string  `db:"territories"`      // slice (24 bytes)
    Status           string    `db:"status"`           // string (16 bytes)
    PhaseStartedAt   time.Time `db:"phase_started_at"` // time.Time (24 bytes)
    CreatedAt        time.Time `db:"created_at"`       // time.Time (24 bytes)
    WinnerClanID     *string   `db:"winner_clan_id"`   // *string (8 bytes)
    // bool поля в конце для alignment
    IsActive         bool      `db:"is_active"`        // bool (1 byte)
}
// Total: ~160 bytes (optimized)
```

**Context Timeouts:**
```go
func (wm *WarManager) DeclareWar(ctx context.Context, req *DeclareWarRequest) (*ClanWar, error) {
    ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
    defer cancel()

    // All database operations use ctx
    tx, err := wm.db.BeginTx(ctx, nil)
    if err != nil {
        return nil, fmt.Errorf("failed to begin transaction: %w", err)
    }
    defer tx.Rollback()
    // ... operations ...
}
```

## Риски и Митigation

### High Risk Issues

**1. Data Consistency in Distributed Battles**
- **Risk:** Race conditions при одновременных score updates
- **Mitigation:** Optimistic locking, conflict resolution, Redis atomic operations

**2. Performance Degradation During Peak Wars**
- **Risk:** System overload при 1000+ concurrent battles
- **Mitigation:** Horizontal scaling, Redis clustering, load shedding

**3. Complex Saga Rollbacks**
- **Risk:** Partial failures в распределенных транзакциях
- **Mitigation:** Comprehensive compensation logic, monitoring, manual intervention procedures

### Monitoring Strategy

**Key Metrics:**
```go
type WarMetrics struct {
    ActiveWars           prometheus.Gauge
    BattlesPerSecond     prometheus.Counter
    ScoreUpdatesLatency  prometheus.Histogram
    TerritoryCaptures    prometheus.Counter
    DatabaseQueryLatency prometheus.Histogram
}
```

**Alerting Rules:**
```yaml
# War declaration failures
- alert: WarDeclarationFailed
  expr: rate(war_declaration_errors_total[5m]) > 0.1
  labels:
    severity: critical

# High latency for score updates
- alert: WarScoreUpdateLatencyHigh
  expr: histogram_quantile(0.95, rate(war_score_update_latency_bucket[5m])) > 0.5
  labels:
    severity: warning
```

## Заключение

Архитектура системы клановых войн технически реализуема и соответствует требованиям MMORPG с 10k+ игроков. Ключевые преимущества:

### ✅ Strengths
- **Scalable Design:** Event-driven architecture, CQRS, partitioning
- **Performance Optimized:** Struct alignment, caching, async processing
- **Reliable:** ACID transactions, Saga pattern, comprehensive error handling
- **Maintainable:** Clean separation of concerns, SOLID principles

### 🎯 Implementation Priority
1. **Phase 1:** Database schema, basic CRUD operations (3 недели)
2. **Phase 2:** Battle system, scoring, territory management (4 недели)
3. **Phase 3:** Event integration, rewards, monitoring (3 недели)
4. **Phase 4:** Performance testing, production deployment (2 недели)

### 📊 Expected Performance
- **War Operations:** P99 <50ms, 1000 RPS
- **Battle Updates:** 20-60 Hz, sub-50ms latency
- **Score Queries:** <30ms, 2000 RPS
- **Scalability:** 10k+ concurrent players, 100+ simultaneous wars

Архитектура готова к реализации с учетом всех современных практик backend разработки для игровых систем.
