# Session Management - Part 1: Lifecycle & Heartbeat

---

- **Status:** queued
- **Last Updated:** 2025-11-07 21:05
---

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:03  
**Приоритет:** КРИТИЧЕСКИЙ

**Микрофича:** Session lifecycle & heartbeat mechanism

[Навигация](./README.md) | [Part 2 →](./part2-reconnection-monitoring.md)

---

## Краткое описание

**Session Management System** - критически важная система для управления игровыми сессиями игроков в MMORPG NECPGAME.

**Ключевые возможности:**
- ✅ Создание/закрытие сессий
- ✅ Heartbeat механизм
- ✅ AFK detection
- ✅ Reconnection handling
- ✅ Concurrent sessions control
- ✅ Session state management

---

## Архитектура системы

### High-Level Overview

```
┌───────────────┐
│   CLIENT      │
└───────┬───────┘
        │ 1. Login
        ↓
┌───────────────┐
│ API Gateway   │
└───────┬───────┘
        │ 2. Authenticate
        ↓
┌────────────────────────┐
│  Session Manager       │
│  - Create session      │
│  - Generate token      │
│  - Store in cache      │
└───────┬────────────────┘
        │
        ├──→ Redis (Session Store)
        └──→ PostgreSQL (Audit Log)
        
        │ 3. Heartbeat every 30s
        ↓
┌────────────────────────┐
│  Session Manager       │
│  - Update last_seen    │
│  - Check expiration    │
└────────────────────────┘

        │ 4. Logout or Timeout
        ↓
┌────────────────────────┐
│  Session Manager       │
│  - Close session       │
│  - Cleanup state       │
│  - Log event           │
└────────────────────────┘
```

---

## Session Lifecycle (Жизненный цикл сессии)

### Этапы сессии

```
1. CREATED     - Сессия создана при login
   ↓
2. ACTIVE      - Игрок активен, отправляет heartbeat
   ↓
3. IDLE        - Игрок неактивен (нет действий 5 минут)
   ↓
4. AFK         - Игрок AFK (нет heartbeat 10 минут)
   ↓
5. DISCONNECTED - Соединение потеряно (reconnect возможен 5 минут)
   ↓
6. EXPIRED     - Сессия истекла (timeout)
   ↓
7. CLOSED      - Сессия закрыта (logout или cleanup)
```

### State Transitions

```
CREATED → ACTIVE (первый heartbeat)
ACTIVE → IDLE (no actions 5 min)
IDLE → ACTIVE (action performed)
IDLE → AFK (no heartbeat 10 min)
AFK → DISCONNECTED (connection lost)
DISCONNECTED → ACTIVE (reconnect successful)
DISCONNECTED → EXPIRED (reconnect timeout)
AFK → EXPIRED (AFK timeout 30 min)
ACTIVE/IDLE/AFK → CLOSED (logout)
EXPIRED → CLOSED (cleanup)
```

---

## Database Schema

### Таблица `player_sessions`

```sql
CREATE TABLE player_sessions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_token VARCHAR(512) UNIQUE NOT NULL,
    
    player_id UUID NOT NULL,
    character_id UUID,
    account_id UUID NOT NULL,
    
    server_id VARCHAR(100) NOT NULL,
    zone_id VARCHAR(100),
    instance_id VARCHAR(100),
    
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED',
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_heartbeat_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_action_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    closed_at TIMESTAMP,
    
    ip_address VARCHAR(45) NOT NULL,
    user_agent TEXT,
    client_version VARCHAR(20),
    
    session_data JSONB,
    
    can_reconnect BOOLEAN DEFAULT TRUE,
    reconnect_token VARCHAR(512),
    reconnect_expires_at TIMESTAMP,
    
    total_heartbeats INTEGER DEFAULT 0,
    total_actions INTEGER DEFAULT 0,
    afk_count INTEGER DEFAULT 0,
    disconnections_count INTEGER DEFAULT 0,
    
    close_reason VARCHAR(100),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_session_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_session_character FOREIGN KEY (character_id) 
        REFERENCES characters(id) ON DELETE SET NULL,
    CONSTRAINT fk_session_account FOREIGN KEY (account_id) 
        REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE INDEX idx_sessions_player ON player_sessions(player_id);
CREATE INDEX idx_sessions_token ON player_sessions(session_token) 
    WHERE status IN ('ACTIVE', 'IDLE', 'AFK');
CREATE INDEX idx_sessions_status ON player_sessions(status);
CREATE INDEX idx_sessions_server ON player_sessions(server_id);
CREATE INDEX idx_sessions_expires ON player_sessions(expires_at) 
    WHERE status NOT IN ('CLOSED', 'EXPIRED');
CREATE INDEX idx_sessions_last_heartbeat ON player_sessions(last_heartbeat_at DESC);

CREATE UNIQUE INDEX idx_sessions_one_per_player 
    ON player_sessions(player_id) 
    WHERE status IN ('ACTIVE', 'IDLE', 'AFK', 'DISCONNECTED');
```

---

### Таблица `session_audit_log`

```sql
CREATE TABLE session_audit_log (
    id BIGSERIAL PRIMARY KEY,
    session_id UUID NOT NULL,
    player_id UUID NOT NULL,
    
    event_type VARCHAR(50) NOT NULL,
    details JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_audit_session FOREIGN KEY (session_id) 
        REFERENCES player_sessions(id) ON DELETE CASCADE
);

CREATE INDEX idx_audit_session ON session_audit_log(session_id);
CREATE INDEX idx_audit_player ON session_audit_log(player_id);
CREATE INDEX idx_audit_created ON session_audit_log(created_at DESC);
```

---

## Redis Cache Structure

### Session Cache

**Key:** `session:{sessionToken}`

**Value:**
```json
{
  "sessionId": "uuid",
  "playerId": "uuid",
  "characterId": "uuid",
  "accountId": "uuid",
  "serverId": "server-01",
  "zoneId": "nightCity.watson",
  "status": "ACTIVE",
  "createdAt": "2025-11-06T21:00:00Z",
  "lastHeartbeat": "2025-11-06T21:30:00Z",
  "expiresAt": "2025-11-07T21:00:00Z",
  "ipAddress": "192.168.1.1",
  "clientVersion": "1.0.0"
}
```

**TTL:** 24 hours

### Active Players Set

**Key:** `active_players:{serverId}`  
**Type:** Redis Set  
**Values:** Player IDs

---

## Session Creation

### При Login

```java
@Service
public class SessionManager {
    
    @Transactional
    public SessionResponse createSession(
        UUID accountId,
        UUID playerId,
        String ipAddress,
        String userAgent,
        String clientVersion
    ) {
        // Check for existing sessions
        List<PlayerSession> existingSessions = sessionRepository
            .findActiveByPlayer(playerId);
        
        if (!existingSessions.isEmpty()) {
            handleConcurrentSessions(playerId, existingSessions);
        }
        
        // Create new session
        PlayerSession session = new PlayerSession();
        session.setSessionToken(generateSessionToken(accountId, playerId));
        session.setPlayerId(playerId);
        session.setAccountId(accountId);
        session.setServerId(getOptimalServer());
        session.setStatus(SessionStatus.CREATED);
        session.setIpAddress(ipAddress);
        session.setUserAgent(userAgent);
        session.setClientVersion(clientVersion);
        session.setExpiresAt(Instant.now().plus(Duration.ofHours(24)));
        session.setCanReconnect(true);
        session.setReconnectToken(generateReconnectToken());
        session.setReconnectExpiresAt(Instant.now().plus(Duration.ofMinutes(5)));
        
        session = sessionRepository.save(session);
        
        // Cache in Redis
        String cacheKey = "session:" + session.getSessionToken();
        redis.opsForValue().set(cacheKey, session, 24, TimeUnit.HOURS);
        
        redis.opsForValue().set(
            "player_session:" + playerId,
            session.getSessionToken(),
            24,
            TimeUnit.HOURS
        );
        
        redis.opsForSet().add("active_players:" + session.getServerId(), playerId);
        
        // Log event
        logSessionEvent(session.getId(), playerId, "SESSION_CREATED", Map.of(
            "ipAddress", ipAddress,
            "clientVersion", clientVersion
        ));
        
        // Publish event
        eventBus.publish(new SessionCreatedEvent(
            session.getId(),
            playerId,
            session.getServerId()
        ));
        
        return new SessionResponse(
            session.getSessionToken(),
            session.getReconnectToken(),
            session.getExpiresAt(),
            session.getServerId()
        );
    }
}
```

---

## Heartbeat Mechanism

### Концепция

**Проблема:** Как определить, что игрок онлайн?

**Решение:** Heartbeat каждые 30 секунд

```
Client → Server: POST /api/v1/session/heartbeat
Server → Client: 200 OK

Если heartbeat не пришел 3 минуты → AFK
Если heartbeat не пришел 10 минут → DISCONNECTED
```

### Heartbeat Endpoint

```java
@PostMapping("/heartbeat")
public ResponseEntity<HeartbeatResponse> heartbeat(
    @RequestHeader("Authorization") String token
) {
    String sessionToken = extractToken(token);
    
    // Get from cache (fast)
    String cacheKey = "session:" + sessionToken;
    PlayerSession session = (PlayerSession) redis.opsForValue().get(cacheKey);
    
    if (session == null) {
        return ResponseEntity.status(401).build();
    }
    
    // Update heartbeat
    Instant now = Instant.now();
    session.setLastHeartbeatAt(now);
    session.setTotalHeartbeats(session.getTotalHeartbeats() + 1);
    
    // Reactivate if was IDLE/AFK
    if (session.getStatus() == SessionStatus.IDLE || 
        session.getStatus() == SessionStatus.AFK) {
        session.setStatus(SessionStatus.ACTIVE);
    }
    
    // Update cache
    redis.opsForValue().set(cacheKey, session, 24, TimeUnit.HOURS);
    
    // Queue DB update (batch)
    sessionUpdateQueue.add(session.getId(), now);
    
    return ResponseEntity.ok(new HeartbeatResponse(
        true,
        session.getStatus(),
        session.getExpiresAt()
    ));
}
```

### Batch Update

```java
@Service
public class SessionBatchUpdater {
    
    private Map<UUID, Instant> updateQueue = new ConcurrentHashMap<>();
    
    public void queueUpdate(UUID sessionId, Instant lastHeartbeat) {
        updateQueue.put(sessionId, lastHeartbeat);
    }
    
    @Scheduled(fixedDelay = 60000) // Every minute
    public void flushUpdates() {
        if (updateQueue.isEmpty()) {
            return;
        }
        
        List<UUID> sessionIds = new ArrayList<>(updateQueue.keySet());
        sessionRepository.batchUpdateHeartbeat(sessionIds, updateQueue);
        updateQueue.clear();
        
        log.info("Flushed {} session heartbeats to DB", sessionIds.size());
    }
}
```

---

## AFK Detection

### Auto-AFK Механизм

```java
@Service
public class AfkDetector {
    
    @Scheduled(fixedDelay = 60000) // Every minute
    public void detectAfkPlayers() {
        Instant now = Instant.now();
        Instant idleThreshold = now.minus(Duration.ofMinutes(5));
        Instant afkThreshold = now.minus(Duration.ofMinutes(10));
        
        // Mark as IDLE
        List<PlayerSession> activeSessions = sessionRepository
            .findByStatus(SessionStatus.ACTIVE);
        
        for (PlayerSession session : activeSessions) {
            if (session.getLastActionAt().isBefore(idleThreshold)) {
                updateSessionStatus(session, SessionStatus.IDLE);
            }
        }
        
        // Mark as AFK
        List<PlayerSession> idleSessions = sessionRepository
            .findByStatus(SessionStatus.IDLE);
        
        for (PlayerSession session : idleSessions) {
            if (session.getLastHeartbeatAt().isBefore(afkThreshold)) {
                updateSessionStatus(session, SessionStatus.AFK);
                session.setAfkCount(session.getAfkCount() + 1);
                
                // Publish AFK event
                eventBus.publish(new PlayerAfkEvent(
                    session.getPlayerId(),
                    session.getZoneId()
                ));
            }
        }
        
        // Expire AFK sessions
        Instant expireThreshold = now.minus(Duration.ofMinutes(30));
        
        List<PlayerSession> afkSessions = sessionRepository
            .findByStatus(SessionStatus.AFK);
        
        for (PlayerSession session : afkSessions) {
            if (session.getLastHeartbeatAt().isBefore(expireThreshold)) {
                closeSession(session.getId(), "AFK_TIMEOUT");
            }
        }
    }
}
```

### AFK Consequences

**В игре:**
- AFK игрок не может участвовать в боевых сессиях
- AFK игрок не получает loot в группе
- AFK игрок автоматически выходит из группы после 15 минут
- AFK в PvP зоне → уязвим для атак

**Notifications:**
```
IDLE (5 min): "You are now idle"
AFK (10 min): "You are AFK. Activity will be paused."
AFK Warning (25 min): "You will be disconnected in 5 minutes due to inactivity"
EXPIRED (30 min): "You have been disconnected due to inactivity"
```

---

[Part 2: Reconnection & Monitoring →](./part2-reconnection-monitoring.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:03) - Восстановлен с полным Java кодом
- v1.0.0 (2025-11-07 01:46) - Создан
