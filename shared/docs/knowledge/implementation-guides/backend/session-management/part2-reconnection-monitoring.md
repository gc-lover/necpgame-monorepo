# Session Management - Part 2: Reconnection & Monitoring

---

- **Status:** queued
- **Last Updated:** 2025-11-07 21:35
---

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:04  
**Приоритет:** КРИТИЧЕСКИЙ

[← Part 1](./part1-lifecycle-heartbeat.md) | [Навигация](./README.md)

---

## Reconnection Handling (Переподключение)

### Fast Reconnect

**Сценарий:** Игрок потерял соединение

**Механизм:**
```
1. Client detects disconnect
2. Client tries to reconnect (auto or manual)
3. Client sends reconnect_token
4. Server validates token (5 min window)
5. Server restores session (same state)
6. Client continues from where left off
```

**Endpoint:**
```java
@PostMapping("/reconnect")
public ResponseEntity<SessionResponse> reconnect(
    @RequestBody ReconnectRequest request
) {
    String reconnectToken = request.getReconnectToken();
    
    // Find session
    PlayerSession session = sessionRepository
        .findByReconnectToken(reconnectToken);
    
    if (session == null) {
        return ResponseEntity.status(404).body(
            new ErrorResponse("Session not found")
        );
    }
    
    // Check reconnect window
    if (Instant.now().isAfter(session.getReconnectExpiresAt())) {
        return ResponseEntity.status(410).body(
            new ErrorResponse("Reconnect window expired")
        );
    }
    
    // Verify status
    if (session.getStatus() != SessionStatus.DISCONNECTED) {
        return ResponseEntity.status(409).body(
            new ErrorResponse("Session is not disconnected")
        );
    }
    
    // Restore session
    session.setStatus(SessionStatus.ACTIVE);
    session.setLastHeartbeatAt(Instant.now());
    session.setDisconnectionsCount(session.getDisconnectionsCount() + 1);
    
    sessionRepository.save(session);
    
    // Update cache
    String cacheKey = "session:" + session.getSessionToken();
    redis.opsForValue().set(cacheKey, session, 24, TimeUnit.HOURS);
    
    // Log reconnection
    logSessionEvent(session.getId(), session.getPlayerId(), "RECONNECTED", Map.of(
        "disconnectionDuration", Duration.between(
            session.getClosedAt(), Instant.now()
        ).getSeconds() + "s"
    ));
    
    // Publish event
    eventBus.publish(new PlayerReconnectedEvent(
        session.getPlayerId(),
        session.getServerId(),
        session.getZoneId()
    ));
    
    return ResponseEntity.ok(new SessionResponse(
        session.getSessionToken(),
        session.getReconnectToken(),
        session.getExpiresAt(),
        session.getServerId(),
        getSessionState(session) // Restore game state
    ));
}
```

### Reconnect Window

```
Connection Lost
    ↓
5 minutes window (can reconnect)
    ↓
Session EXPIRED (cannot reconnect, must login again)
```

---

## Concurrent Sessions (Множественные сессии)

### Стратегии

**Стратегия 1: One Session Per Player (рекомендуется для MMORPG)**
```
Player tries to login
→ Check for existing active session
→ If found: Close old session, create new one
→ Result: Only one session allowed
```

**Стратегия 2: Multiple Sessions Allowed**
```
Player can login from multiple devices
→ Each device has own session
→ State synchronized between sessions
→ Actions from any session update global state
```

**Стратегия 3: Kick Old Session**
```
Player tries to login
→ Check for existing session
→ If found: Send disconnect to old session, create new
→ Old device receives "You have been logged in elsewhere"
```

### Реализация (Strategy 1)

```java
private void handleConcurrentSessions(
    UUID playerId,
    List<PlayerSession> existingSessions
) {
    log.warn("Player {} has {} existing sessions, closing them", 
        playerId, existingSessions.size());
    
    for (PlayerSession oldSession : existingSessions) {
        // Send notification to old session
        webSocketService.sendToSession(
            oldSession.getSessionToken(),
            new SessionKickedMessage(
                "You have been logged in from another location"
            )
        );
        
        // Close old session
        closeSession(oldSession.getId(), "CONCURRENT_LOGIN");
        
        // Small delay to ensure message delivered
        Thread.sleep(2000);
    }
}
```

---

## Session Timeout

### Типы timeout

**1. Inactivity Timeout:**
```
No heartbeat for 10 minutes → AFK
AFK for 30 minutes → Session EXPIRED
```

**2. Absolute Timeout:**
```
Session created 24 hours ago → Session EXPIRED
```

**3. Idle Timeout:**
```
No actions for 5 minutes → IDLE status
IDLE for 60 minutes → Session EXPIRED
```

### Cleanup Job

```java
@Service
public class SessionCleanupService {
    
    @Scheduled(cron = "0 */5 * * * *") // Every 5 minutes
    public void cleanupExpiredSessions() {
        Instant now = Instant.now();
        
        // Find expired sessions
        List<PlayerSession> expiredSessions = sessionRepository
            .findByExpiresAtBefore(now);
        
        log.info("Found {} expired sessions to cleanup", expiredSessions.size());
        
        for (PlayerSession session : expiredSessions) {
            closeSession(session.getId(), "TIMEOUT");
        }
        
        // Delete old closed sessions (older than 7 days)
        Instant oldThreshold = now.minus(Duration.ofDays(7));
        int deleted = sessionRepository.deleteClosedBefore(oldThreshold);
        
        log.info("Deleted {} old closed sessions", deleted);
    }
}
```

---

## Session Closing

### Logout

```java
@PostMapping("/logout")
public ResponseEntity<Void> logout(
    @RequestHeader("Authorization") String token
) {
    String sessionToken = extractToken(token);
    PlayerSession session = getSession(sessionToken);
    
    closeSession(session.getId(), "LOGOUT");
    
    return ResponseEntity.ok().build();
}

@Transactional
private void closeSession(UUID sessionId, String reason) {
    PlayerSession session = sessionRepository.findById(sessionId);
    
    // Save character state before closing
    if (session.getCharacterId() != null) {
        characterService.saveCurrentState(session.getCharacterId());
    }
    
    // Update session
    session.setStatus(SessionStatus.CLOSED);
    session.setClosedAt(Instant.now());
    session.setCloseReason(reason);
    session.setCanReconnect(false);
    
    sessionRepository.save(session);
    
    // Remove from cache
    redis.delete("session:" + session.getSessionToken());
    redis.delete("player_session:" + session.getPlayerId());
    
    // Remove from active players
    redis.opsForSet().remove(
        "active_players:" + session.getServerId(),
        session.getPlayerId()
    );
    
    // Log event
    logSessionEvent(session.getId(), session.getPlayerId(), "SESSION_CLOSED", Map.of(
        "reason", reason,
        "duration", Duration.between(
            session.getCreatedAt(), session.getClosedAt()
        ).getSeconds() + "s"
    ));
    
    // Cleanup resources
    cleanupSessionResources(session);
    
    // Publish event
    eventBus.publish(new SessionClosedEvent(
        session.getId(),
        session.getPlayerId(),
        reason
    ));
}

private void cleanupSessionResources(PlayerSession session) {
    // Leave party
    if (session.getSessionData().get("partyId") != null) {
        partyService.leave(session.getPlayerId());
    }
    
    // Exit combat
    if (session.getSessionData().get("combatSessionId") != null) {
        combatService.exitCombat(session.getPlayerId());
    }
    
    // Cancel trade
    if (session.getSessionData().get("tradeSessionId") != null) {
        tradeService.cancelTrade(session.getPlayerId());
    }
}
```

---

## Session Monitoring

### Метрики

```
sessions.active.total         - всего активных сессий
sessions.created.rate          - скорость создания сессий/мин
sessions.closed.rate           - скорость закрытия сессий/мин
sessions.afk.count             - количество AFK игроков
sessions.heartbeat.rate        - heartbeat/сек
sessions.reconnections.count   - количество reconnect
sessions.average.duration      - средняя длительность сессии
```

### Dashboard

```java
@GetMapping("/admin/sessions/stats")
public SessionStats getSessionStats() {
    return new SessionStats(
        sessionRepository.countByStatus(SessionStatus.ACTIVE),
        sessionRepository.countByStatus(SessionStatus.IDLE),
        sessionRepository.countByStatus(SessionStatus.AFK),
        sessionRepository.countByStatus(SessionStatus.DISCONNECTED),
        redis.opsForSet().size("active_players:*"),
        getAverageSessionDuration(),
        getSessionsPerServer()
    );
}
```

---

## API Endpoints Summary

### Session Management
- **POST** `/api/v1/session/create` - создать сессию
- **POST** `/api/v1/session/heartbeat` - heartbeat
- **POST** `/api/v1/session/reconnect` - переподключение
- **POST** `/api/v1/session/logout` - выход
- **GET** `/api/v1/session/info` - информация о сессии
- **PUT** `/api/v1/session/state` - обновить состояние
- **GET** `/api/v1/session/active-players` - список онлайн игроков

### Admin Endpoints
- **GET** `/api/v1/admin/sessions` - все сессии
- **POST** `/api/v1/admin/sessions/{id}/kick` - kick игрока
- **GET** `/api/v1/admin/sessions/stats` - статистика сессий

---

## Security

### Session Hijacking Protection

**1. IP Address Binding:**
```java
if (!session.getIpAddress().equals(request.getRemoteAddr())) {
    log.warn("IP mismatch for session {}", sessionId);
    // Optional: reject request or require re-auth
}
```

**2. User Agent Validation:**
```java
if (!session.getUserAgent().equals(request.getHeader("User-Agent"))) {
    log.warn("User-Agent mismatch for session {}", sessionId);
}
```

**3. Token Rotation:**
```java
if (Duration.between(session.getCreatedAt(), Instant.now()).toHours() > 4) {
    String newToken = generateSessionToken();
    session.setSessionToken(newToken);
    // Send new token to client
}
```

### Brute Force Protection

```
Failed login attempts from IP:
3 attempts → 1 min cooldown
5 attempts → 5 min cooldown
10 attempts → 30 min cooldown
20 attempts → IP block (24 hours)
```

---

## Интеграция с другими системами

### При создании сессии

```
SessionManager.createSession()
  ↓
→ GlobalStateManager: set player.{id}.session = sessionId
→ EventBus: publish SESSION_CREATED
→ AnalyticsService: record login
→ NotificationService: send "Welcome back!"
```

### При logout

```
SessionManager.closeSession()
  ↓
→ GlobalStateManager: remove player.{id}.session
→ PartyService: leave party
→ CombatService: exit combat
→ EventBus: publish SESSION_CLOSED
→ AnalyticsService: record session duration
```

---

## Связанные документы

- [Part 1: Lifecycle & Heartbeat](./part1-lifecycle-heartbeat.md)
- [Authentication System](../authentication-authorization/README.md)
- [Global State System](../../global-state-system/README.md)

---

[← Назад к навигации](./README.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:04) - Восстановлен с полным Java кодом
- v1.0.0 (2025-11-07 01:46) - Создан
