---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:00
**api-readiness-notes:** Global State Operations микрофича. Query service, projections, monitoring, performance, testing, security, scalability, API. ~480 строк.
---

# Global State Operations - Операции и мониторинг

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 06:00  
**Приоритет:** КРИТИЧЕСКИЙ  
**Автор:** AI Brain Manager

**Микрофича:** Operations, monitoring, optimization  
**Размер:** ~480 строк ✅  

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Краткое описание

**Global State Operations** - операционные аспекты системы (запросы, мониторинг, производительность).

**Ключевые возможности:**
- ✅ Query Service (5 типов запросов)
- ✅ Projections (read models)
- ✅ Monitoring (метрики, health checks)
- ✅ Performance Optimization
- ✅ Testing (replay, time travel)
- ✅ Security (validation, anti-cheat)
- ✅ Scalability (horizontal/vertical)
- ✅ API Endpoints

---

## State Query Service

### Query Types

**1. Point Query:**
```java
Object value = stateService.get(serverId, stateKey);
Integer level = stateService.get("server-01", "player.uuid.level");
```

**2. Range Query:**
```java
Map<String, Object> states = stateService.getRange(serverId, "player.uuid.*");
```

**3. Aggregate Query:**
```java
Map<String, Integer> factionPowers = stateService.aggregate(
    serverId,
    "world.faction.*.power",
    AggregateFunction.SUM
);
```

**4. Time-Travel Query:**
```java
Object pastValue = stateService.getAtTime(
    serverId,
    stateKey,
    Instant.parse("2025-11-06T20:00:00Z")
);
```

**5. Projection Query:**
```java
PlayerStateProjection projection = stateService.getProjection(
    serverId,
    "player.uuid",
    PlayerStateProjection.class,
    Arrays.asList("level", "class", "reputation.arasaka")
);
```

---

## Projections (Read Models)

### Player Profile Projection

```sql
CREATE TABLE player_profile_projection (
    player_id UUID PRIMARY KEY,
    
    level INTEGER NOT NULL,
    experience BIGINT NOT NULL,
    class VARCHAR(50),
    
    total_quests_completed INTEGER DEFAULT 0,
    total_enemies_killed INTEGER DEFAULT 0,
    total_items_crafted INTEGER DEFAULT 0,
    
    reputation_arasaka INTEGER DEFAULT 0,
    reputation_militech INTEGER DEFAULT 0,
    
    total_eurodollars_earned BIGINT DEFAULT 0,
    
    world_impact_score INTEGER DEFAULT 0,
    territories_captured INTEGER DEFAULT 0,
    
    last_event_id UUID,
    last_updated_at TIMESTAMP NOT NULL,
    projection_version INTEGER DEFAULT 1
);
```

### Projector

```java
@Service
public class PlayerProfileProjector {
    
    @EventListener
    public void on(PlayerLeveledUpEvent event) {
        PlayerProfileProjection profile = projectionRepository.findById(event.getPlayerId());
        profile.setLevel(event.getNewLevel());
        profile.setExperience(event.getTotalExperience());
        profile.setLastEventId(event.getEventId());
        projectionRepository.save(profile);
    }
    
    @EventListener
    public void on(QuestCompletedEvent event) {
        PlayerProfileProjection profile = projectionRepository.findById(event.getPlayerId());
        profile.setTotalQuestsCompleted(profile.getTotalQuestsCompleted() + 1);
        profile.setWorldImpactScore(profile.getWorldImpactScore() + event.getImpactScore());
        projectionRepository.save(profile);
    }
}
```

---

## Monitoring

### Metrics

**Event Store:**
```
events.written.total - всего записано событий
events.written.rate - скорость записи событий/сек
events.processing.time - время обработки события
events.processing.errors - ошибки при обработке
events.backlog.size - размер необработанных событий
```

**State Store:**
```
state.keys.total - всего ключей состояния
state.updates.rate - скорость обновлений/сек
state.cache.hit.rate - hit rate кэша
state.cache.miss.rate - miss rate кэша
state.query.time - время запроса состояния
```

**Synchronization:**
```
websocket.connections.active - активные WebSocket соединения
websocket.messages.sent.rate - скорость отправки сообщений
websocket.latency - задержка доставки
state.sync.lag - задержка синхронизации
```

### Health Checks

```java
@Component
public class GlobalStateHealthCheck implements HealthIndicator {
    
    @Override
    public Health health() {
        boolean eventStoreHealthy = checkEventStore();
        boolean stateStoreHealthy = checkStateStore();
        boolean eventBusHealthy = checkEventBus();
        long syncLag = checkSyncLag();
        
        if (!eventStoreHealthy || !stateStoreHealthy || !eventBusHealthy) {
            return Health.down()
                .withDetail("eventStore", eventStoreHealthy)
                .withDetail("stateStore", stateStoreHealthy)
                .withDetail("eventBus", eventBusHealthy)
                .build();
        }
        
        if (syncLag > 60000) {
            return Health.degraded()
                .withDetail("syncLag", syncLag + "ms")
                .build();
        }
        
        return Health.up().build();
    }
}
```

---

## Disaster Recovery

### Сценарии

**1. State Store corrupted:**
```
1. Stop writes
2. Replay events from Event Store
3. Reconstruct state
4. Verify integrity
5. Resume writes

Время: 5-60 минут (зависит от snapshots)
```

**2. Event Store unavailable:**
```
1. Enable write buffering (Redis)
2. Buffer events (до 1000)
3. When available → flush buffer
4. If buffer full → reject writes
```

**3. Event Bus failure:**
```
1. Events still in Event Store
2. Processing delayed
3. Catch-up processing
4. Gradual processing
```

**4. Full system failure:**
```
1. Event Store replicated (3+ copies)
2. State Store replicated (2+ copies)
3. Point-in-time recovery (backup каждые 6h)
4. Geographic distribution

Recovery:
1. Restore Event Store from replica
2. Restore latest State Snapshot
3. Replay events from snapshot
4. Verify integrity
5. Resume service
```

---

## Performance Optimization

### Write Optimization

**Batch Writes:**
```java
// Batch write (1 запрос)
eventStore.appendBatch(events);
```

**Async Writes:**
```java
@Async
public CompletableFuture<Void> appendEventAsync(GameEvent event) {
    return CompletableFuture.runAsync(() -> eventStore.append(event));
}
```

### Read Optimization

**Caching:**
```
Level 1: Local Cache (in-memory)
Level 2: Redis (distributed)
Level 3: Materialized Views
```

**Redis TTL:**
```
Некритические: 5 min
Критические: 1 min
Динамические: 30 sec
```

**Materialized Views:**
```sql
CREATE MATERIALIZED VIEW faction_power_view AS
SELECT 
    server_id,
    faction_id,
    SUM(economic_power) as total_economic_power
FROM faction_power_components
GROUP BY server_id, faction_id;

REFRESH MATERIALIZED VIEW CONCURRENTLY faction_power_view;
```

### Query Optimization

**Indexes:**
```sql
CREATE INDEX idx_events_player_time ON game_events(player_id, event_timestamp DESC);
CREATE INDEX idx_events_unprocessed ON game_events(event_timestamp) 
WHERE is_processed = FALSE;
```

**Partitioning:**
```sql
CREATE TABLE game_events_2025_11 PARTITION OF game_events
FOR VALUES FROM ('2025-11-01') TO ('2025-12-01');
```

---

## Testing

```java
@Test
public void testQuestBranching() {
    // 1. Записать события
    eventStore.append(new QuestStartedEvent(playerId, questId));
    eventStore.append(new QuestChoiceMadeEvent(playerId, questId, "A2"));
    eventStore.append(new QuestCompletedEvent(playerId, questId, "pathTruth"));
    
    // 2. Replay
    replayEngine.replay(playerId, Instant.EPOCH, Instant.now());
    
    // 3. Проверить состояние
    String questStatus = stateService.get(serverId, "player.uuid.quest.questId.status");
    assertEquals("COMPLETED", questStatus);
}
```

### Time Travel Debugging

```java
Instant debugTime = Instant.parse("2025-11-06T20:00:00Z");

Object stateAt20 = stateService.getStateAtTime(serverId, stateKey, debugTime);
List<GameEvent> eventsAfter = eventStore.getEventsAfter("player.uuid", debugTime);
```

---

## Security

```java
@Component
public class EventValidator {
    
    public void validate(GameEvent event) {
        // 1. Структура
        if (event.getEventType() == null || event.getEventData() == null) {
            throw new InvalidEventException();
        }
        
        // 2. Авторизация
        if (!authService.isAuthenticated(event.getPlayerId())) {
            throw new UnauthorizedException();
        }
        
        // 3. Бизнес-правила
        validateBusinessRules(event);
        
        // 4. Rate limiting
        if (isRateLimitExceeded(event.getPlayerId(), event.getEventType())) {
            throw new RateLimitException();
        }
        
        // 5. Anti-cheat
        if (isCheatDetected(event)) {
            logCheatAttempt(event);
            throw new CheatDetectedException();
        }
    }
}
```

---

## Scalability

### Horizontal Scaling

**Event Store:**
```
Partition by aggregate_id (hash)
→ События игрока на одном узле
→ Load balancing
```

**State Store:**
```
Shard by server_id + state_category
→ Каждый сервер на отдельном шарде
→ Уменьшение contention
```

**Event Bus:**
```
Kafka partitions:
- player events → partition by playerId
- world events → partition by serverId
- economy events → partition by region
```

### Vertical Scaling

**PostgreSQL:**
```
shared_buffers = 8GB
wal_buffers = 16MB
effective_cache_size = 24GB
max_wal_size = 4GB

HikariCP: maximumPoolSize = 50
```

**Redis:**
```
maxmemory = 16GB
maxmemory-policy = allkeys-lru
Replication: 2+ replicas
```

---

## API Endpoints

### Event Management

**POST `/api/v1/events`** - записать событие

**GET `/api/v1/events/{aggregateId}`** - события сущности

**GET `/api/v1/events/history`** - история с фильтрами

### State Management

**GET `/api/v1/state`** - полное состояние сервера

**GET `/api/v1/state/{category}`** - состояние по категории

**GET `/api/v1/state/key/{stateKey}`** - конкретный ключ

**GET `/api/v1/state/history/{stateKey}`** - история изменений

**POST `/api/v1/state/replay`** - replay (admin)

---

## Связанные документы

- `.BRAIN/05-technical/global-state/global-state-core.md` - Core (микрофича 1/5)
- `.BRAIN/05-technical/global-state/global-state-events.md` - Events (микрофича 2/5)
- `.BRAIN/05-technical/global-state/global-state-management.md` - Management (микрофича 3/5)
- `.BRAIN/05-technical/global-state/global-state-sync.md` - Sync (микрофича 4/5)

**Системы:**
- `.BRAIN/02-gameplay/world/world-state-player-impact.md` - влияние на мир
- `.BRAIN/05-technical/api-specs/api-integration-map.md` - интеграция API

---

## TODO

**Высокий приоритет:**
- [ ] Стратегия партиционирования Event Store
- [ ] Частота создания snapshots
- [ ] Retention policy для старых событий
- [ ] Интеграция с БД схемой квестов

**Средний приоритет:**
- [ ] Phasing system для квестов
- [ ] Conflict resolution edge cases
- [ ] Disaster recovery procedures
- [ ] Мониторинг и алертинг

---

## История изменений

- **v1.0.0 (2025-11-07 06:00)** - Микрофича 5/5: Global State Operations (split from global-state-system.md)

---

## Заключение

Global State System - критическая система NECPGAME:

✅ Регистрирует ВСЕ события
✅ Хранит полную историю
✅ Управляет мировым состоянием
✅ Синхронизирует игроков (MMORPG)
✅ Обеспечивает восстановление
✅ Масштабируется
✅ Защищает от сбоев

**Готовность:** READY для API! ✅
