---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:27
**api-readiness-notes:** Maintenance Mode System. Управление обслуживанием сервера, плановые остановки, уведомления. ~380 строк.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 06:35
---

## Краткое описание

**Maintenance Mode System** - система управления режимом обслуживания для production deployment.

**Ключевые возможности:**
- ✅ Scheduled Maintenance (плановое обслуживание)
- ✅ Emergency Maintenance (экстренное)
- ✅ Graceful Shutdown (плавное отключение)
- ✅ Player Notifications (уведомления игроков)
- ✅ Maintenance Windows (окна обслуживания)
- ✅ Status Page (страница статуса)

---

## Архитектура системы

```
Admin triggers maintenance
    ↓
Update maintenance status
    ↓
Notify all online players
    ↓
Graceful shutdown (5-15 min countdown)
    ↓
Block new connections
    ↓
Wait for active sessions to complete
    ↓
Enter maintenance mode
    ↓
Perform maintenance
    ↓
Exit maintenance mode
    ↓
Allow connections
```

---

## Database Schema

### Таблица `maintenance_windows`

```sql
CREATE TABLE maintenance_windows (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Type
    maintenance_type VARCHAR(20) NOT NULL,
    
    -- Schedule
    scheduled_start TIMESTAMP NOT NULL,
    scheduled_end TIMESTAMP NOT NULL,
    
    -- Actual times
    actual_start TIMESTAMP,
    actual_end TIMESTAMP,
    
    -- Status
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    
    -- Details
    title VARCHAR(200) NOT NULL,
    description TEXT,
    reason VARCHAR(100),
    
    -- Impact
    expected_downtime INTEGER,
    services_affected VARCHAR(200)[],
    
    -- Notifications
    notification_sent BOOLEAN DEFAULT FALSE,
    notification_sent_at TIMESTAMP,
    
    -- Created by
    created_by UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_maintenance_creator FOREIGN KEY (created_by) 
        REFERENCES accounts(id) ON DELETE SET NULL
);

CREATE INDEX idx_maintenance_status ON maintenance_windows(status);
CREATE INDEX idx_maintenance_scheduled ON maintenance_windows(scheduled_start, scheduled_end);
```

### Таблица `maintenance_status`

```sql
CREATE TABLE maintenance_status (
    id INTEGER PRIMARY KEY DEFAULT 1,
    
    -- Current status
    is_maintenance_mode BOOLEAN DEFAULT FALSE,
    maintenance_window_id UUID,
    
    -- Mode details
    maintenance_type VARCHAR(20),
    started_at TIMESTAMP,
    expected_end_at TIMESTAMP,
    
    -- Message
    status_message TEXT,
    
    -- Connection blocking
    block_new_connections BOOLEAN DEFAULT FALSE,
    allow_admin_only BOOLEAN DEFAULT TRUE,
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT single_row CHECK (id = 1),
    CONSTRAINT fk_maintenance_window FOREIGN KEY (maintenance_window_id) 
        REFERENCES maintenance_windows(id) ON DELETE SET NULL
);

-- Insert default row
INSERT INTO maintenance_status (id) VALUES (1);
```

---

## Maintenance Types

```java
public enum MaintenanceType {
    SCHEDULED,     // Плановое (объявлено заранее)
    EMERGENCY,     // Экстренное (немедленное)
    HOT_FIX,       // Хотфикс (быстрое исправление)
    ROLLBACK,      // Откат изменений
    UPGRADE        // Обновление версии
}

public enum MaintenanceStatus {
    SCHEDULED,     // Запланировано
    WARNING,       // Предупреждение отправлено
    IN_PROGRESS,   // В процессе
    COMPLETED,     // Завершено
    CANCELLED      // Отменено
}
```

---

## Scheduled Maintenance Flow

### 1. Schedule Maintenance

```java
@Service
public class MaintenanceService {
    
    public MaintenanceWindow scheduleMaintenance(ScheduleMaintenanceRequest request) {
        // Validate
        validateMaintenanceWindow(request);
        
        // Create window
        MaintenanceWindow window = new MaintenanceWindow();
        window.setMaintenanceType(request.getType());
        window.setScheduledStart(request.getScheduledStart());
        window.setScheduledEnd(request.getScheduledEnd());
        window.setTitle(request.getTitle());
        window.setDescription(request.getDescription());
        window.setExpectedDowntime(calculateDowntime(
            request.getScheduledStart(), 
            request.getScheduledEnd()
        ));
        window.setStatus(MaintenanceStatus.SCHEDULED);
        window.setCreatedBy(getCurrentAdmin().getId());
        
        window = maintenanceRepository.save(window);
        
        // Schedule notifications
        scheduleNotifications(window);
        
        // Schedule auto-start
        scheduleAutoStart(window);
        
        log.info("Maintenance scheduled: {} at {}", 
            window.getId(), window.getScheduledStart());
        
        return window;
    }
    
    private void scheduleNotifications(MaintenanceWindow window) {
        // 24h before
        scheduler.schedule(
            () -> sendNotification(window, "24h"),
            window.getScheduledStart().minus(24, ChronoUnit.HOURS)
        );
        
        // 1h before
        scheduler.schedule(
            () -> sendNotification(window, "1h"),
            window.getScheduledStart().minus(1, ChronoUnit.HOURS)
        );
        
        // 15min before
        scheduler.schedule(
            () -> sendNotification(window, "15min"),
            window.getScheduledStart().minus(15, ChronoUnit.MINUTES)
        );
    }
}
```

---

### 2. Enter Maintenance Mode

```java
public void enterMaintenanceMode(UUID windowId) {
    MaintenanceWindow window = maintenanceRepository.findById(windowId)
        .orElseThrow();
    
    // Update window status
    window.setStatus(MaintenanceStatus.IN_PROGRESS);
    window.setActualStart(Instant.now());
    maintenanceRepository.save(window);
    
    // Update global status
    MaintenanceStatus status = getMaintenanceStatus();
    status.setIsMaintenanceMode(true);
    status.setMaintenanceWindowId(windowId);
    status.setMaintenanceType(window.getMaintenanceType());
    status.setStartedAt(Instant.now());
    status.setExpectedEndAt(window.getScheduledEnd());
    status.setStatusMessage(window.getDescription());
    status.setBlockNewConnections(true);
    status.setAllowAdminOnly(true);
    
    maintenanceStatusRepository.save(status);
    
    // Notify all online players
    notifyAllPlayers(new MaintenanceStartNotification(window));
    
    // Start graceful shutdown
    startGracefulShutdown(15); // 15 minutes
    
    // Publish event
    eventBus.publish(new MaintenanceModeStartedEvent(windowId));
    
    log.warn("Entered maintenance mode: {}", windowId);
}
```

---

### 3. Graceful Shutdown

```java
public void startGracefulShutdown(int gracePeriodMinutes) {
    log.info("Starting graceful shutdown. Grace period: {} minutes", 
        gracePeriodMinutes);
    
    // Countdown notifications
    for (int remaining : List.of(15, 10, 5, 3, 1)) {
        if (remaining <= gracePeriodMinutes) {
            scheduler.schedule(
                () -> notifyShutdown(remaining),
                gracePeriodMinutes - remaining, 
                TimeUnit.MINUTES
            );
        }
    }
    
    // Block new connections
    scheduler.schedule(
        this::blockNewConnections,
        gracePeriodMinutes,
        TimeUnit.MINUTES
    );
    
    // Wait for active sessions
    scheduler.schedule(
        this::waitForActiveSessions,
        gracePeriodMinutes + 5,
        TimeUnit.MINUTES
    );
}

private void blockNewConnections() {
    MaintenanceStatus status = getMaintenanceStatus();
    status.setBlockNewConnections(true);
    maintenanceStatusRepository.save(status);
    
    log.warn("New connections blocked");
}

private void waitForActiveSessions() {
    int maxWaitMinutes = 10;
    int waited = 0;
    
    while (sessionManager.getActiveSessionCount() > 0 && waited < maxWaitMinutes) {
        log.info("Waiting for {} active sessions to complete...", 
            sessionManager.getActiveSessionCount());
        
        Thread.sleep(60000); // Wait 1 minute
        waited++;
    }
    
    if (sessionManager.getActiveSessionCount() > 0) {
        log.warn("Force disconnecting {} remaining sessions", 
            sessionManager.getActiveSessionCount());
        sessionManager.disconnectAll("Server maintenance");
    }
    
    log.info("All sessions closed. Ready for maintenance.");
}
```

---

### 4. Exit Maintenance Mode

```java
public void exitMaintenanceMode(UUID windowId) {
    MaintenanceWindow window = maintenanceRepository.findById(windowId)
        .orElseThrow();
    
    // Update window
    window.setStatus(MaintenanceStatus.COMPLETED);
    window.setActualEnd(Instant.now());
    maintenanceRepository.save(window);
    
    // Update global status
    MaintenanceStatus status = getMaintenanceStatus();
    status.setIsMaintenanceMode(false);
    status.setMaintenanceWindowId(null);
    status.setBlockNewConnections(false);
    maintenanceStatusRepository.save(status);
    
    // Notify completion
    announceMaintenanceComplete(window);
    
    // Publish event
    eventBus.publish(new MaintenanceModeEndedEvent(windowId));
    
    log.info("Exited maintenance mode: {}", windowId);
}
```

---

## Connection Blocking

### Middleware/Interceptor

```java
@Component
public class MaintenanceModeInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, 
                            HttpServletResponse response, 
                            Object handler) throws Exception {
        
        MaintenanceStatus status = maintenanceService.getStatus();
        
        if (status.isMaintenanceMode()) {
            // Check if admin
            if (status.isAllowAdminOnly() && isAdmin(request)) {
                return true; // Allow admins
            }
            
            // Block non-admins
            response.setStatus(503); // Service Unavailable
            response.setContentType("application/json");
            response.getWriter().write(buildMaintenanceResponse(status));
            
            return false;
        }
        
        return true;
    }
    
    private String buildMaintenanceResponse(MaintenanceStatus status) {
        return String.format("""
            {
              "error": "SERVICE_UNAVAILABLE",
              "message": "Server is currently in maintenance mode",
              "statusMessage": "%s",
              "expectedEndAt": "%s",
              "retryAfter": %d
            }
            """,
            status.getStatusMessage(),
            status.getExpectedEndAt(),
            calculateRetryAfter(status.getExpectedEndAt())
        );
    }
}
```

---

## WebSocket Handling

```java
@Service
public class MaintenanceWebSocketService {
    
    public void notifyMaintenanceStart(MaintenanceWindow window) {
        // Send to all connected clients
        websocketTemplate.convertAndSend("/topic/maintenance", 
            new MaintenanceNotification(
                "MAINTENANCE_STARTING",
                window.getDescription(),
                window.getScheduledStart(),
                window.getScheduledEnd()
            )
        );
    }
    
    public void sendCountdownNotification(int minutesRemaining) {
        websocketTemplate.convertAndSend("/topic/maintenance",
            new CountdownNotification(
                "MAINTENANCE_COUNTDOWN",
                String.format("Server shutdown in %d minutes. Please finish your activities.", 
                    minutesRemaining),
                minutesRemaining
            )
        );
    }
}
```

---

## Status Page

### Public Endpoint

```java
@RestController
@RequestMapping("/api/v1/status")
public class StatusController {
    
    @GetMapping
    public ResponseEntity<ServerStatus> getStatus() {
        MaintenanceStatus maintenance = maintenanceService.getStatus();
        
        ServerStatus status = new ServerStatus();
        status.setOnline(!maintenance.isMaintenanceMode());
        status.setMaintenanceMode(maintenance.isMaintenanceMode());
        
        if (maintenance.isMaintenanceMode()) {
            status.setStatusMessage(maintenance.getStatusMessage());
            status.setExpectedEndAt(maintenance.getExpectedEndAt());
        }
        
        // Server metrics
        status.setOnlinePlayers(sessionManager.getActivePlayerCount());
        status.setServerTime(Instant.now());
        
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<MaintenanceWindow>> getMaintenanceHistory(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by("scheduledStart").descending());
        
        Page<MaintenanceWindow> windows = maintenanceRepository
            .findByStatusIn(
                List.of(MaintenanceStatus.COMPLETED, MaintenanceStatus.IN_PROGRESS),
                pageable
            );
        
        return ResponseEntity.ok(windows.getContent());
    }
}
```

---

## Emergency Maintenance

```java
public void emergencyMaintenance(String reason) {
    log.error("EMERGENCY MAINTENANCE TRIGGERED: {}", reason);
    
    // Create emergency window
    MaintenanceWindow window = new MaintenanceWindow();
    window.setMaintenanceType(MaintenanceType.EMERGENCY);
    window.setTitle("Emergency Maintenance");
    window.setDescription(reason);
    window.setScheduledStart(Instant.now());
    window.setScheduledEnd(Instant.now().plus(2, ChronoUnit.HOURS)); // Estimate
    window.setStatus(MaintenanceStatus.IN_PROGRESS);
    
    window = maintenanceRepository.save(window);
    
    // Immediate notification
    notifyAllPlayers(new EmergencyMaintenanceNotification(reason));
    
    // Short grace period (5 minutes)
    startGracefulShutdown(5);
    
    // Enter maintenance mode
    enterMaintenanceMode(window.getId());
}
```

---

## API Endpoints

**GET `/api/v1/status`** - статус сервера (public)

```json
Response:
{
  "online": false,
  "maintenanceMode": true,
  "statusMessage": "Scheduled maintenance for database upgrade",
  "expectedEndAt": "2025-11-07T06:00:00Z",
  "onlinePlayers": 0,
  "serverTime": "2025-11-07T04:30:00Z"
}
```

**GET `/api/v1/admin/maintenance/schedule`** - запланировать обслуживание

**POST `/api/v1/admin/maintenance/start`** - начать обслуживание

**POST `/api/v1/admin/maintenance/end`** - завершить обслуживание

**POST `/api/v1/admin/maintenance/emergency`** - экстренное обслуживание

**GET `/api/v1/status/history`** - история обслуживания

---

## Связанные документы

- [Admin Tools](../admin/admin-tools-core.md)
- [Notification System](../notification-system.md)
- [Session Management](../session/session-lifecycle-heartbeat.md)
