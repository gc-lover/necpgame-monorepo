# Backend Systems - Обзор бэкенд систем

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-07
**api-readiness-notes:** Навигационный файл, описывает распределение систем по микросервисам

**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07

---

## Описание

Обзор всех backend систем и их распределение по микросервисам в рамках микросервисной архитектуры NECPGAME.

---

## Распределение систем по микросервисам

### 🔐 auth-service (Port 8081)

**API маршруты:** `/api/v1/auth/*`  
**Статус:** ✅ Реализовано

**Системы:**
1. **Authentication & Authorization** - `auth/`
   - Регистрация, login, JWT tokens
   - OAuth integration
   - Password recovery
   - Roles & permissions

2. **Session Management** - `session-management/`
   - Session tracking
   - Heartbeat
   - AFK detection

---

### 👤 character-service (Port 8082)

**API маршруты:** `/api/v1/characters/*`, `/api/v1/players/*`  
**Статус:** 📋 В планах (Фаза 2)

**Системы:**
3. **Player & Character Management** - `player-character-management.md`
   - Character CRUD
   - Character slots
   - Player profiles
   - Attributes и stats

---

### 🎮 gameplay-service (Port 8083)

**API маршруты:** `/api/v1/gameplay/*`  
**Статус:** 📋 В планах (Фаза 2)

**Системы:**
4. **Combat Session** - `combat-session-backend.md`
   - Combat mechanics
   - Damage calculation
   - Turn-based logic

5. **Matchmaking** - `matchmaking/`
   - Queue management
   - Rating calculation
   - Match creation

6. **Progression** - `progression-backend.md`
   - Experience и leveling
   - Skill progression
   - Attribute upgrades

7. **Quest Engine** - `quest-engine-backend.md`
   - Quest state machine
   - Quest progress tracking
   - Rewards distribution

---

### 👥 social-service (Port 8084)

**API маршруты:** `/api/v1/social/*`  
**Статус:** 📋 В планах (Фаза 3)

**Системы:**
8. **Guild System** - `guild-system-backend.md`
   - Guild management
   - Guild wars
   - Guild progression

9. **Party System** - `party-system.md`
   - Party formation
   - Loot distribution
   - Party chat

10. **Friend System** - `friend-system.md`
    - Friend list
    - Friend requests
    - Online status

11. **Chat System** - `chat/`
    - Channels
    - Private messages
    - Moderation

12. **Mail System** - `mail-system.md`
    - Inbox/Outbox
    - Attachments
    - Item transfers

13. **Notification System** - `notification-system.md`
    - Real-time notifications
    - System messages
    - Achievement notifications

---

### 💰 economy-service (Port 8085)

**API маршруты:** `/api/v1/economy/*`  
**Статус:** 📋 В планах (Фаза 3)

**Системы:**
14. **Inventory System** - `inventory-system/`
    - Item storage
    - Equipment management
    - Bank/stash

15. **Loot System** - `loot-system/`
    - Loot generation
    - Loot tables
    - Drop rates

16. **Trade System** - `trade-system.md`
    - P2P trading
    - Trade windows
    - Trade verification

---

### 🌍 world-service (Port 8086)

**API маршруты:** `/api/v1/world/*`  
**Статус:** 📋 В планах (Фаза 3)

**Системы:**
17. **Real-Time Server** - `realtime-server/`
    - Player positions
    - Zone management
    - Event synchronization

18. **Achievement System** - `achievement-system.md`
    - Achievement tracking
    - Progress calculation
    - Rewards

19. **Leaderboard System** - `leaderboard-system.md`
    - Rankings
    - Seasons
    - Statistics

20. **Daily/Weekly Reset** - `daily-weekly-reset-system.md`
    - Scheduled jobs
    - Quest resets
    - Reward distribution

---

## Межсервисное взаимодействие

### REST (Feign Client)

Синхронные запросы между сервисами:

```java
// character-service вызывает auth-service
@FeignClient(name = "AUTH-SERVICE")
public interface AuthServiceClient {
    @PostMapping("/validate-token")
    TokenValidationResponse validateToken(@RequestBody String token);
}
```

### Event Bus (Kafka/RabbitMQ)

Асинхронная коммуникация через события:

```java
// auth-service публикует
eventPublisher.publish("account.created", accountId);

// character-service подписывается
@KafkaListener(topics = "account.created")
public void handleAccountCreated(String accountId) {
    createCharacterSlots(accountId);
}
```

### Circuit Breaker

Устойчивость к отказам сервисов:

```java
@CircuitBreaker(name = "authService", fallbackMethod = "validateTokenFallback")
public TokenValidationResponse validateToken(String token) {
    return authClient.validateToken(token);
}

public TokenValidationResponse validateTokenFallback(String token, Exception e) {
    // Fallback: локальная валидация JWT без вызова auth-service
    return jwtTokenValidator.validateLocally(token);
}
```

---

## Связанные документы

- [backend-architecture-overview.md](../backend-architecture-overview.md) - общий обзор бэкенд архитектуры
- [microservices-overview.md](../microservices-overview.md) - детали микросервисной архитектуры
- [БЭКТАСК-MICROSERVICES.md](../../../BACK-GO/docs/БЭКТАСК-MICROSERVICES.md) - руководство Backend Agent

---

## История изменений

- v1.0.0 (2025-11-07) - Создан обзор backend систем с распределением по микросервисам

