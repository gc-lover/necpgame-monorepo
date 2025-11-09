# Realtime Server - Part 2: Protocol & Optimization

---

- **Status:** queued
- **Last Updated:** 2025-11-07 20:35
---

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:13  
**api-readiness:** ready

[← Part 1](./part1-architecture-zones.md) | [Навигация](./README.md)

---

## Network Protocol

### WebSocket vs UDP

**Выбор: WebSocket (TCP)**

**Почему НЕ UDP:**
- Web browsers не поддерживают UDP (WebRTC только для voice/video)
- Проблемы с firewall/NAT
- Сложность реализации reliable messages

**Почему WebSocket (TCP):**
- Поддержка во всех браузерах
- Reliable delivery (не нужно реализовывать acknowledgments)
- Проще для web-based клиента
- Trade-off: немного больше latency (но приемлемо для MMORPG)

### Message Types

**Client → Server:**
```
PLAYER_INPUT       - Движение, действия игрока
CHAT_MESSAGE       - Сообщение в чат
ACTION_USE_SKILL   - Использование способности
ACTION_ATTACK      - Атака
ACTION_INTERACT    - Взаимодействие с объектом
HEARTBEAT          - Keepalive
```

**Server → Client:**
```
STATE_UPDATE       - Обновление состояния мира
COMBAT_EVENT       - События боя (урон, хилы)
CHAT_MESSAGE       - Сообщения в чат
SYSTEM_NOTIFICATION - Системные уведомления
ZONE_CHANGED       - Смена зоны
PLAYER_DIED        - Смерть игрока
```

### Message Serialization

**Binary Protocol (MessagePack)**

**Преимущества:**
- Меньший размер сообщений (50-80% reduction vs JSON)
- Быстрая сериализация/десериализация
- Typed schema

**Пример:**
```java
@Service
public class MessageCodec {
    
    private ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
    
    public byte[] encode(Message message) {
        return mapper.writeValueAsBytes(message);
    }
    
    public Message decode(byte[] data) {
        return mapper.readValue(data, Message.class);
    }
}
```

---

## Lag Compensation

### Client-Side Prediction

**Проблема:** Latency 50-100ms → движение чувствуется laggy

**Решение:** Client Prediction

```
Player presses "W" (move forward)
  ↓
CLIENT: Immediately move forward (predict)
  ↓
Send input to server
  ↓
SERVER: Process input, update authoritative state
  ↓
Send state update back to client
  ↓
CLIENT: Reconcile (если prediction правильный - ничего не делать, 
                   если неправильный - корректировать)
```

**Алгоритм (JavaScript):**
```javascript
class ClientPrediction {
    constructor() {
        this.pendingInputs = [];
        this.lastServerTick = 0;
    }
    
    onInput(input) {
        // 1. Сохранить input
        input.sequence = this.nextSequence++;
        this.pendingInputs.push(input);
        
        // 2. Немедленно применить локально (prediction)
        this.applyInput(input);
        
        // 3. Отправить на сервер
        this.sendToServer(input);
    }
    
    onServerUpdate(update) {
        // 1. Удалить обработанные inputs
        this.pendingInputs = this.pendingInputs.filter(
            input => input.sequence > update.lastProcessedSequence
        );
        
        // 2. Установить authoritative state
        this.position = update.position;
        
        // 3. Replay pending inputs (reconciliation)
        for (const input of this.pendingInputs) {
            this.applyInput(input);
        }
    }
}
```

### Server Reconciliation

```java
@Service
public class ServerReconciliation {
    
    // История состояний игрока (последние 1 секунда)
    private final Map<UUID, Deque<PlayerStateSnapshot>> stateHistory = 
        new ConcurrentHashMap<>();
    
    public void processPlayerInput(UUID playerId, PlayerInput input) {
        // 1. Получить текущее состояние игрока
        PlayerState state = getPlayerState(playerId);
        
        // 2. Применить input
        applyInput(state, input);
        
        // 3. Validate (anti-cheat)
        if (!isValidMovement(state, input)) {
            log.warn("Invalid movement from player {}, resetting position", playerId);
            resetToLastValidState(playerId);
            return;
        }
        
        // 4. Сохранить snapshot в историю
        saveStateSnapshot(playerId, state, input.getSequence());
        
        // 5. Broadcast to other players
        broadcastPlayerUpdate(playerId, state);
    }
    
    private boolean isValidMovement(PlayerState state, PlayerInput input) {
        // Проверки:
        // - Скорость не превышает max (anti-speedhack)
        // - Позиция в допустимых границах (не вышел за карту)
        // - Нет teleportation (moved too far in one tick)
        
        double maxSpeed = state.getMaxSpeed();
        double actualSpeed = input.getVelocity().length();
        
        if (actualSpeed > maxSpeed * 1.2) { // 20% tolerance
            return false;
        }
        
        return true;
    }
}
```

### Lag Compensation для Combat

**Проблема:** Игрок стреляет, но цель уже переместилась (из-за latency)

**Решение:** Rewind time на сервере

```java
@Service
public class CombatLagCompensation {
    
    public void processShot(UUID shooterId, UUID targetId, long clientTimestamp) {
        // 1. Определить latency
        long serverTime = System.currentTimeMillis();
        long latency = serverTime - clientTimestamp;
        
        // 2. Rewind target position на moment of shot
        PlayerStateSnapshot targetState = getStateAt(
            targetId,
            clientTimestamp - latency
        );
        
        // 3. Проверить hit detection на rewound state
        boolean isHit = checkHitDetection(shooterId, targetState);
        
        // 4. Если hit, применить урон
        if (isHit) {
            applyDamage(targetId, getDamage(shooterId));
            
            // 5. Broadcast hit event
            broadcastCombatEvent(new HitEvent(
                shooterId,
                targetId,
                getDamage(shooterId)
            ));
        }
    }
}
```

---

## Bandwidth Optimization

### Delta Compression

**Проблема:** Отправлять полный state каждый tick - дорого

**Решение:** Delta updates (только изменения)

```java
@Service
public class DeltaCompression {
    
    // Последнее отправленное состояние для каждого игрока
    private final Map<UUID, PlayerState> lastSentState = new ConcurrentHashMap<>();
    
    public PlayerStateDelta createDelta(UUID playerId, PlayerState currentState) {
        PlayerState lastState = lastSentState.get(playerId);
        
        if (lastState == null) {
            // Первый update - отправить все
            lastSentState.put(playerId, currentState.copy());
            return PlayerStateDelta.full(currentState);
        }
        
        // Создать delta (только изменения)
        PlayerStateDelta delta = new PlayerStateDelta(playerId);
        
        if (!currentState.getPosition().equals(lastState.getPosition())) {
            delta.setPosition(currentState.getPosition());
        }
        
        if (!currentState.getRotation().equals(lastState.getRotation())) {
            delta.setRotation(currentState.getRotation());
        }
        
        if (currentState.getHealth() != lastState.getHealth()) {
            delta.setHealth(currentState.getHealth());
        }
        
        // ... и т.д. для остальных полей
        
        // Обновить last sent state
        lastSentState.put(playerId, currentState.copy());
        
        return delta;
    }
}
```

**Экономия:**
```
Full state: ~500 bytes
Delta state: ~50-100 bytes (если только position изменился)
= 80-90% reduction
```

### Priority-Based Updates

**Проблема:** Bandwidth ограничен, не можем отправить все updates

**Решение:** Приоритизация

```java
private List<PlayerState> prioritizeUpdates(
    UUID recipientId,
    List<PlayerState> allPlayers
) {
    // Сортировать по приоритету:
    // 1. Игроки в combat (highest)
    // 2. Игроки близко (по distance)
    // 3. Игроки в party/guild (medium)
    // 4. Остальные (lowest)
    
    return allPlayers.stream()
        .map(state -> {
            int priority = calculatePriority(recipientId, state);
            return new PrioritizedState(state, priority);
        })
        .sorted(Comparator.comparing(PrioritizedState::getPriority).reversed())
        .limit(50) // Максимум 50 updates per tick
        .map(PrioritizedState::getState)
        .collect(Collectors.toList());
}

private int calculatePriority(UUID recipientId, PlayerState state) {
    int priority = 0;
    
    // Combat bonus
    if (state.isInCombat()) {
        priority += 100;
    }
    
    // Distance (closer = higher priority)
    double distance = getDistance(recipientId, state.getPlayerId());
    priority += (int) (100 - distance); // Max 100m
    
    // Party/Guild bonus
    if (isInSameParty(recipientId, state.getPlayerId())) {
        priority += 50;
    }
    
    return priority;
}
```

---

## API Endpoints

### Real-Time WebSocket

**Endpoint:** `ws://localhost:8080/ws/game`

**Topics:**
- `/topic/zone/{zoneId}/players` - игроки в зоне
- `/topic/zone/{zoneId}/chat` - zone chat
- `/topic/character/{characterId}/combat` - combat события
- `/topic/world/events` - мировые события

### REST API (для управления)

**GET** `/api/v1/zones` - список зон  
**GET** `/api/v1/zones/{zoneId}/players` - игроки в зоне  
**POST** `/api/v1/zones/{zoneId}/join` - войти в зону  
**POST** `/api/v1/zones/{zoneId}/leave` - покинуть зону

**POST** `/api/v1/instances/create` - создать instance  
**GET** `/api/v1/instances/{instanceId}` - инфо об instance  
**POST** `/api/v1/instances/{instanceId}/join` - войти в instance  
**DELETE** `/api/v1/instances/{instanceId}` - удалить instance

---

## Performance Metrics

**Target:**
- Latency: < 50ms (p95)
- Tick rate: 20 ticks/sec
- Players per server: 1000-2000
- Bandwidth per player: < 50 KB/s

**Monitoring:**
```
realtime.tick.duration        - время обработки tick
realtime.players.active       - активных игроков
realtime.bandwidth.sent       - отправлено bytes/sec
realtime.messages.rate        - сообщений/sec
realtime.lag.compensation     - срабатываний lag compensation
```

---

## Связанные документы

- [Session Management](../session-management/README.md)
- [Global State System](../../global-state-system/README.md)
- [Combat System](../../../02-gameplay/combat/)

---

[← Назад к навигации](./README.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:13) - Создан с полным Java кодом (protocol, lag compensation, optimization)
- v1.0.0 (2025-11-06) - Создан
