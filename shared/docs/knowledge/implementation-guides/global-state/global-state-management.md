---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 06:00
**api-readiness-notes:** Global State Management микрофича. Event processing pipeline, State Manager implementation, Time Travel. ~430 строк.
---

# Global State Management - Управление состоянием

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 06:00  
**Приоритет:** КРИТИЧЕСКИЙ  
**Автор:** AI Brain Manager

**Микрофича:** State Management и Event Processing  
**Размер:** ~430 строк ✅  

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Краткое описание

**Global State Management** - обработка событий и управление состоянием мира.

**Ключевые возможности:**
- ✅ Event Processing Pipeline (10 этапов)
- ✅ Global State Manager (GET/UPDATE)
- ✅ State Reconstruction
- ✅ Time Travel
- ✅ Snapshots

---

## Event Processing Pipeline

### 10 этапов обработки

```
1. EVENT RECEIVED
   ↓
2. VALIDATION
   ↓
3. AUTHORIZATION
   ↓
4. ENRICHMENT
   ↓
5. PERSISTENCE (Event Store)
   ↓
6. PUBLICATION (Event Bus)
   ↓
7. SUBSCRIBERS PROCESSING
   ↓
8. STATE UPDATE
   ↓
9. NOTIFICATION
   ↓
10. ANALYTICS
```

### Event Handler

```java
@Service
public class QuestEventHandler {
    
    @EventListener
    public void handleQuestChoiceMade(QuestChoiceMadeEvent event) {
        // 1. Обновить quest progress
        questProgressRepository.updateBranch(
            event.getPlayerId(),
            event.getQuestId(),
            event.getBranchId()
        );
        
        // 2. Применить последствия выбора
        Choice choice = choiceRepository.findById(event.getChoiceId());
        
        // Репутация
        if (choice.getReputationChanges() != null) {
            reputationService.applyChanges(
                event.getPlayerId(),
                choice.getReputationChanges()
            );
        }
        
        // Флаги
        if (choice.getSetsFlags() != null) {
            flagService.setFlags(
                event.getPlayerId(),
                choice.getSetsFlags(),
                event.getQuestId()
            );
        }
        
        // Предметы
        if (choice.getGivesItems() != null) {
            inventoryService.giveItems(
                event.getPlayerId(),
                choice.getGivesItems()
            );
        }
        
        // 3. Проверить мировое влияние
        if (choice.getWorldStateChanges() != null) {
            globalStateService.applyWorldChanges(
                choice.getWorldStateChanges(),
                event
            );
        }
        
        // 4. Создать follow-up события
        if (choice.getNextNodeId() != null) {
            eventBus.publish(new QuestNodeEnteredEvent(
                event.getPlayerId(),
                event.getQuestId(),
                choice.getNextNodeId()
            ));
        }
        
        // 5. Уведомить игрока
        notificationService.send(
            event.getPlayerId(),
            "Choice recorded: " + choice.getChoiceText()
        );
        
        // 6. Логировать для аналитики
        analyticsService.recordChoice(event);
    }
}
```

---

## Global State Manager

```java
@Service
public class GlobalStateManager {
    
    @Autowired
    private GlobalStateRepository stateRepository;
    
    @Autowired
    private GameEventRepository eventRepository;
    
    @Autowired
    private RedisTemplate<String, Object> redis;
    
    @Autowired
    private EventBus eventBus;
    
    // ==================== GET STATE ====================
    
    public Object getState(String serverId, String stateKey) {
        // 1. Попытка из кэша
        String cacheKey = "state:" + serverId + ":" + stateKey;
        Object cached = redis.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        // 2. Из БД
        GlobalState state = stateRepository.findByServerAndKey(serverId, stateKey);
        if (state == null) {
            return getDefaultValue(stateKey);
        }
        
        // 3. Кэшировать
        redis.opsForValue().set(cacheKey, state.getValue(), 5, TimeUnit.MINUTES);
        
        return state.getValue();
    }
    
    public Map<String, Object> getStateCategory(String serverId, String category) {
        String cacheKey = "state:" + serverId + ":" + category + ":*";
        
        // Попытка из кэша
        Map<String, Object> cached = redis.opsForHash().entries(cacheKey);
        if (!cached.isEmpty()) {
            return cached;
        }
        
        // Из БД
        List<GlobalState> states = stateRepository.findByServerAndCategory(
            serverId, 
            category
        );
        
        Map<String, Object> result = new HashMap<>();
        for (GlobalState state : states) {
            result.put(state.getStateKey(), state.getValue());
        }
        
        // Кэшировать
        redis.opsForHash().putAll(cacheKey, result);
        redis.expire(cacheKey, 5, TimeUnit.MINUTES);
        
        return result;
    }
    
    // ==================== UPDATE STATE ====================
    
    @Transactional
    public void updateState(
        String serverId,
        String stateKey,
        Object newValue,
        UUID eventId,
        Map<String, Object> metadata
    ) {
        // 1. Получить текущее состояние
        GlobalState currentState = stateRepository.findByServerAndKey(serverId, stateKey);
        Object previousValue = currentState != null ? currentState.getValue() : null;
        
        // 2. Проверить изменение
        if (Objects.equals(previousValue, newValue)) {
            return; // Нет изменений
        }
        
        // 3. Обновить или создать
        if (currentState != null) {
            currentState.setPreviousValue(previousValue);
            currentState.setValue(newValue);
            currentState.setVersion(currentState.getVersion() + 1);
            currentState.setChangedByEventId(eventId);
            currentState.setChangedAt(Instant.now());
            stateRepository.save(currentState);
        } else {
            GlobalState newState = new GlobalState();
            newState.setServerId(serverId);
            newState.setStateKey(stateKey);
            newState.setValue(newValue);
            newState.setChangedByEventId(eventId);
            stateRepository.save(newState);
        }
        
        // 4. Инвалидировать кэш
        String cacheKey = "state:" + serverId + ":" + stateKey;
        redis.delete(cacheKey);
        
        // 5. Опубликовать событие изменения состояния
        eventBus.publish(new StateChangedEvent(
            serverId,
            stateKey,
            previousValue,
            newValue,
            eventId
        ));
        
        // 6. Уведомить игроков (если нужно)
        if (metadata.get("notifyPlayers") == Boolean.TRUE) {
            notifyAffectedPlayers(stateKey, newValue);
        }
    }
    
    @Transactional
    public void updateStateBatch(
        String serverId,
        Map<String, Object> stateChanges,
        UUID eventId
    ) {
        for (Map.Entry<String, Object> entry : stateChanges.entrySet()) {
            updateState(serverId, entry.getKey(), entry.getValue(), eventId, Map.of());
        }
    }
    
    // ==================== TIME TRAVEL ====================
    
    public Object getStateAtTime(
        String serverId,
        String stateKey,
        Instant pointInTime
    ) {
        // 1. Найти последний snapshot до pointInTime
        StateSnapshot snapshot = snapshotRepository.findLastBefore(
            serverId,
            extractCategory(stateKey),
            pointInTime
        );
        
        // 2. Получить начальное состояние из snapshot
        Object state = snapshot != null 
            ? snapshot.getSnapshotData().get(stateKey)
            : getDefaultValue(stateKey);
        
        // 3. Получить события после snapshot до pointInTime
        Instant snapshotTime = snapshot != null 
            ? snapshot.getSnapshotTimestamp()
            : Instant.EPOCH;
            
        List<GameEvent> events = eventRepository.findEventsForKey(
            stateKey,
            snapshotTime,
            pointInTime
        );
        
        // 4. Применить события
        for (GameEvent event : events) {
            state = applyEventToState(state, event);
        }
        
        return state;
    }
}
```

---

## State Reconstruction

### Концепция

```
Current State = Initial State + All Events
```

**Example:**
```
Initial: Player Level = 1
+ Event: PLAYER_LEVELED_UP (1→2)
+ Event: PLAYER_LEVELED_UP (2→3)
...
+ Event: PLAYER_LEVELED_UP (49→50)
= Current: Player Level = 50
```

### Algorithm

```java
public GlobalState reconstructState(String stateKey, Instant pointInTime) {
    List<GameEvent> events = eventStore.getEvents(stateKey, null, pointInTime);
    
    Object currentValue = getInitialState(stateKey);
    
    for (GameEvent event : events) {
        currentValue = applyEvent(currentValue, event);
    }
    
    return new GlobalState(stateKey, currentValue, pointInTime);
}
```

---

## Event Bus

### Technologies

**Options:**
- **Kafka** (production) - масштабируемость
- **RabbitMQ** - гибкость
- **Redis Pub/Sub** (MVP) - простота

### Topics

```
necpgame.events.player.{playerId}
necpgame.events.quest.{questId}
necpgame.events.world.{serverId}
necpgame.events.economy.{region}
necpgame.events.combat.{sessionId}
necpgame.events.faction.{factionId}
necpgame.events.global
```

### Publishers & Subscribers

**Publishers:**
- QuestService → quest events
- CombatService → combat events
- EconomyService → economy events
- SocialService → social events
- WorldService → world events

**Subscribers:**
- GlobalStateManager → обновляет state
- NotificationService → уведомления
- AnalyticsService → аналитика
- WebSocketService → real-time updates
- AuditService → аудит
- CacheInvalidator → кэш

---

## API Endpoints

**GET `/api/v1/state/{category}`** - получить состояние категории
**GET `/api/v1/state/key/{stateKey}`** - получить конкретное состояние
**POST `/api/v1/events`** - создать событие

---

## Связанные документы

- `.BRAIN/05-technical/global-state/global-state-core.md` - Core (микрофича 1/5)
- `.BRAIN/05-technical/global-state/global-state-events.md` - Events (микрофича 2/5)
- `.BRAIN/05-technical/global-state/global-state-sync.md` - Sync (микрофича 4/5)
- `.BRAIN/05-technical/global-state/global-state-operations.md` - Operations (микрофича 5/5)

---

## История изменений

- **v1.0.0 (2025-11-07 06:00)** - Микрофича 3/5: Global State Management (split from global-state-system.md)
