---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:59
**api-readiness-notes:** Admin Tools System. Инструменты администрирования - player management, moderation, analytics. ~380 строк.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 03:27
---
# Admin Tools System - Core System
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 01:59  
**Приоритет:** HIGH  
**Автор:** AI Brain Manager

**Микрофича:** Admin tools & moderation  
**Размер:** ~380 строк ✅

---

## Краткое описание

**Admin Tools System** - комплексная система инструментов для администраторов и модераторов.

**Ключевые возможности:**
- ✅ Player management (ban/kick/mute)
- ✅ Item management (grant/remove items)
- ✅ Economy management (adjust currency)
- ✅ World state management (change flags)
- ✅ Event creation (server events)
- ✅ Analytics dashboard (метрики)

---

## Архитектура системы

```
Admin Panel (Web UI)
    ↓
Admin API (protected)
    ↓
Permission Check (@RequiresPermission)
    ↓
Execute Action
    ↓
Audit Log (все действия)
    ↓
Notify affected players
```

---

## Player Management

### Ban/Kick/Mute

```java
@RestController
@RequestMapping("/api/v1/admin/players")
public class AdminPlayerController {
    
    @PostMapping("/{playerId}/ban")
    @RequiresPermission("player.ban")
    public ResponseEntity<Void> banPlayer(
        @PathVariable UUID playerId,
        @RequestBody BanRequest request,
        @AuthenticatedUser Admin admin
    ) {
        // Validate
        validateBanRequest(request);
        
        // Create ban
        banService.banPlayer(
            playerId,
            request.getReason(),
            request.getDuration(),
            request.getType(),
            admin.getId()
        );
        
        // Audit log
        auditLog(admin.getId(), "PLAYER_BANNED", Map.of(
            "targetPlayerId", playerId,
            "reason", request.getReason(),
            "duration", request.getDuration()
        ));
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{playerId}/kick")
    @RequiresPermission("player.kick")
    public ResponseEntity<Void> kickPlayer(
        @PathVariable UUID playerId,
        @RequestBody KickRequest request,
        @AuthenticatedUser Admin admin
    ) {
        sessionManager.kickPlayer(playerId, request.getReason());
        
        auditLog(admin.getId(), "PLAYER_KICKED", Map.of(
            "targetPlayerId", playerId,
            "reason", request.getReason()
        ));
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{playerId}/mute")
    @RequiresPermission("player.mute")
    public ResponseEntity<Void> mutePlayer(
        @PathVariable UUID playerId,
        @RequestBody MuteRequest request,
        @AuthenticatedUser Admin admin
    ) {
        chatService.mutePlayer(playerId, request.getDuration());
        
        auditLog(admin.getId(), "PLAYER_MUTED", Map.of(
            "targetPlayerId", playerId,
            "duration", request.getDuration()
        ));
        
        return ResponseEntity.ok().build();
    }
}
```

---

## Item Management

### Grant/Remove Items

```java
@PostMapping("/admin/items/grant")
@RequiresPermission("items.grant")
public ResponseEntity<Void> grantItem(
    @RequestBody GrantItemRequest request,
    @AuthenticatedUser Admin admin
) {
    // Validate item exists
    Item item = itemRepository.findById(request.getItemId())
        .orElseThrow(() -> new ItemNotFoundException());
    
    // Grant to player
    inventoryService.addItem(
        request.getPlayerId(),
        request.getItemId(),
        request.getQuantity()
    );
    
    // Send notification
    notificationService.send(request.getPlayerId(),
        new ItemGrantedNotification(item, request.getQuantity()));
    
    // Audit
    auditLog(admin.getId(), "ITEM_GRANTED", Map.of(
        "playerId", request.getPlayerId(),
        "itemId", request.getItemId(),
        "quantity", request.getQuantity()
    ));
    
    return ResponseEntity.ok().build();
}
```

---

## Economy Management

### Adjust Currency

```java
@PostMapping("/admin/economy/adjust-currency")
@RequiresPermission("economy.adjust")
public ResponseEntity<Void> adjustCurrency(
    @RequestBody AdjustCurrencyRequest request,
    @AuthenticatedUser Admin admin
) {
    Player player = playerRepository.findById(request.getPlayerId());
    
    // Adjust
    long oldBalance = player.getCurrency();
    long newBalance = oldBalance + request.getAmount();
    
    if (newBalance < 0) {
        throw new InsufficientCurrencyException();
    }
    
    player.setCurrency(newBalance);
    playerRepository.save(player);
    
    // Audit
    auditLog(admin.getId(), "CURRENCY_ADJUSTED", Map.of(
        "playerId", request.getPlayerId(),
        "oldBalance", oldBalance,
        "newBalance", newBalance,
        "change", request.getAmount(),
        "reason", request.getReason()
    ));
    
    return ResponseEntity.ok().build();
}
```

---

## World State Management

### Change World Flags

```java
@PostMapping("/admin/world-state/set-flag")
@RequiresPermission("world.manage")
public ResponseEntity<Void> setWorldFlag(
    @RequestBody SetFlagRequest request,
    @AuthenticatedUser Admin admin
) {
    worldStateService.setFlag(
        request.getRegionId(),
        request.getFlagName(),
        request.getValue()
    );
    
    // Audit
    auditLog(admin.getId(), "WORLD_FLAG_CHANGED", Map.of(
        "regionId", request.getRegionId(),
        "flag", request.getFlagName(),
        "value", request.getValue()
    ));
    
    // Notify all players in region
    broadcastWorldStateChange(request.getRegionId(), request.getFlagName());
    
    return ResponseEntity.ok().build();
}
```

---

## Event Creation

### Create Server Event

```java
@PostMapping("/admin/events/create")
@RequiresPermission("event.create")
public ResponseEntity<ServerEvent> createEvent(
    @RequestBody CreateEventRequest request,
    @AuthenticatedUser Admin admin
) {
    ServerEvent event = new ServerEvent();
    event.setTitle(request.getTitle());
    event.setDescription(request.getDescription());
    event.setType(request.getType());
    event.setStartTime(request.getStartTime());
    event.setEndTime(request.getEndTime());
    event.setRewards(request.getRewards());
    event.setCreatedBy(admin.getId());
    
    eventRepository.save(event);
    
    // Schedule event
    eventScheduler.schedule(event);
    
    // Announce to all players
    announcementService.broadcast(new EventAnnouncementNotification(event));
    
    // Audit
    auditLog(admin.getId(), "EVENT_CREATED", Map.of(
        "eventId", event.getId(),
        "type", event.getType()
    ));
    
    return ResponseEntity.ok(event);
}
```

---

## Analytics Dashboard

### Real-Time Metrics

```java
@GetMapping("/admin/analytics/realtime")
@RequiresPermission("analytics.view")
public ResponseEntity<RealtimeMetrics> getRealtimeMetrics() {
    return ResponseEntity.ok(new RealtimeMetrics(
        // Players
        sessionManager.getActivePlayerCount(),
        sessionManager.getAfkPlayerCount(),
        
        // Economy
        economyService.getTotalCurrency(),
        economyService.getAverageCurrency(),
        
        // Combat
        combatService.getActiveCombatSessions(),
        
        // Quests
        questService.getActiveQuests(),
        questService.getCompletionsToday(),
        
        // Performance
        getAverageResponseTime(),
        getDatabaseConnectionCount()
    ));
}
```

### Player Search

```java
@GetMapping("/admin/players/search")
@RequiresPermission("player.search")
public ResponseEntity<List<PlayerSummary>> searchPlayers(
    @RequestParam(required = false) String username,
    @RequestParam(required = false) String email,
    @RequestParam(required = false) UUID playerId
) {
    List<Player> results = playerRepository.search(username, email, playerId);
    
    return ResponseEntity.ok(
        results.stream()
            .map(this::toSummary)
            .collect(Collectors.toList())
    );
}
```

---

## Audit Log System

### Таблица `admin_audit_log`

```sql
CREATE TABLE admin_audit_log (
    id BIGSERIAL PRIMARY KEY,
    admin_id UUID NOT NULL,
    
    -- Action
    action_type VARCHAR(50) NOT NULL,
    target_type VARCHAR(50),
    target_id UUID,
    
    -- Details
    details JSONB NOT NULL,
    
    -- Context
    ip_address VARCHAR(45),
    user_agent TEXT,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_audit_admin FOREIGN KEY (admin_id) 
        REFERENCES accounts(id) ON DELETE SET NULL
);

CREATE INDEX idx_audit_admin ON admin_audit_log(admin_id, created_at DESC);
CREATE INDEX idx_audit_action ON admin_audit_log(action_type);
CREATE INDEX idx_audit_target ON admin_audit_log(target_id);
```

### Log All Actions

```java
private void auditLog(UUID adminId, String actionType, Map<String, Object> details) {
    AdminAuditLog log = new AdminAuditLog();
    log.setAdminId(adminId);
    log.setActionType(actionType);
    log.setDetails(details);
    log.setIpAddress(getClientIp());
    log.setUserAgent(getUserAgent());
    
    auditLogRepository.save(log);
}
```

---

## API Endpoints

### Player Management

**GET `/api/v1/admin/players/search`** - поиск игроков  
**GET `/api/v1/admin/players/{id}`** - детали игрока  
**POST `/api/v1/admin/players/{id}/ban`** - забанить  
**POST `/api/v1/admin/players/{id}/kick`** - кикнуть  
**POST `/api/v1/admin/players/{id}/mute`** - замутить  
**DELETE `/api/v1/admin/players/{id}`** - удалить аккаунт

### Item Management

**POST `/api/v1/admin/items/grant`** - выдать предмет  
**DELETE `/api/v1/admin/items/{id}/remove`** - удалить предмет

### Economy

**POST `/api/v1/admin/economy/adjust-currency`** - изменить валюту  
**GET `/api/v1/admin/economy/stats`** - статистика экономики

### World State

**POST `/api/v1/admin/world-state/set-flag`** - изменить флаг  
**GET `/api/v1/admin/world-state/{regionId}`** - состояние региона

### Events

**POST `/api/v1/admin/events/create`** - создать событие  
**PUT `/api/v1/admin/events/{id}`** - редактировать  
**DELETE `/api/v1/admin/events/{id}`** - отменить

### Analytics

**GET `/api/v1/admin/analytics/realtime`** - метрики реального времени  
**GET `/api/v1/admin/analytics/reports`** - отчёты  
**GET `/api/v1/admin/audit-log`** - audit log

---

## Security

### Permission-Based Access

```java
// Только ADMIN и SUPER_ADMIN
@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")

// Specific permissions
@RequiresPermission("player.ban")
@RequiresPermission("economy.adjust")
@RequiresPermission("world.manage")
```

### Audit Everything

```
ALL admin actions logged:
- Who (admin_id)
- What (action_type)
- When (timestamp)
- Target (player/item/etc)
- Details (full context)
- IP + User Agent
```

---

## Связанные документы

- [Anti-Cheat System](../anti-cheat/anti-cheat-core.md)
- [Authentication System](../auth/auth-authorization-security.md)
- [Analytics System](../analytics/analytics-core.md)
