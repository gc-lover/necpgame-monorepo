# Global State System - Навигация

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07
**api-readiness-notes:** Система глобального состояния мира, реализуется в world-service

**Версия:** 1.0.1  
**Дата:** 2025-11-07  
**Статус:** approved

---

## Микросервисная архитектура

**Ответственный микросервис:** world-service  
**Порт:** 8086  
**API Gateway маршрут:** `/api/v1/world/state/*`  
**Статус:** 📋 В планах (Фаза 3)

**Взаимодействие с другими сервисами:**
- Все сервисы могут обновлять global state через Event Bus
- world-service координирует и хранит состояние
- Синхронизация через Redis для real-time updates

**Event Bus события:**
- Публикует: `world:state-changed`, `world:event-started`, `world:faction-war-update`
- Подписывается: `combat:territory-captured`, `guild:war-declared`, `quest:world-quest-completed`

---

## Распределенное состояние

### Текущая реализация (Shared State)

```
world-service (8086)
  ├─ Управляет global state
  ├─ PostgreSQL: хранилище состояния
  └─ Redis: cache для быстрого доступа
      ↓
Другие сервисы читают через API:
  - character-service: проверка faction ownership
  - gameplay-service: активные world events
  - social-service: guild territory status
```

### Event-driven синхронизация

**Пример: Territory Capture**

```
1. gameplay-service: guild захватила территорию
   eventBus.publish('combat:territory-captured', {
     territoryId: 'night-city-downtown',
     guildId: 'guild-123',
     capturedAt: timestamp
   });

2. world-service подписан на событие:
   @EventListener('combat:territory-captured')
   updateGlobalState(territoryId, guildId);
   
3. world-service публикует обновление:
   eventBus.publish('world:state-changed', {
     type: 'territory-ownership',
     data: {...}
   });

4. Все сервисы получают обновление:
   - character-service: обновить территорию в cache
   - social-service: уведомить guild members
   - notification-service: отправить уведомления
```

---

## Описание

Система управления глобальным состоянием мира: Территории, фракции, экономика, события.

---

## Структура документов

### Part 1: Core
**Файл:** [global-state-core.md](./global-state-core.md)  
**Содержание:** Основы, типы состояний, структуры данных

### Part 2: Management
**Файл:** [global-state-management.md](./global-state-management.md)  
**Содержание:** Управление состоянием, update механизмы

### Part 3: Events
**Файл:** [global-state-events.md](./global-state-events.md)  
**Содержание:** События и их влияние на состояние

### Part 4: Sync
**Файл:** [global-state-sync.md](./global-state-sync.md)  
**Содержание:** Синхронизация между клиентами и сервером

### Part 5: Operations
**Файл:** [global-state-operations.md](./global-state-operations.md)  
**Содержание:** CRUD операции, API

---

## Distributed State Management

### Redis для синхронизации

```typescript
// world-service обновляет state
redis.set('world:state:current', JSON.stringify(globalState));
redis.publish('world:state:changed', JSON.stringify(changes));

// Другие сервисы подписываются
redis.subscribe('world:state:changed', (changes) => {
  updateLocalCache(changes);
});
```

### Event Sourcing (опционально для Production)

**Концепция:** Хранить все изменения состояния как события

```
Event Store:
1. TerritoryCapture(territoryId, guildId, timestamp)
2. FactionsWarDeclared(faction1, faction2, timestamp)
3. EconomyCrashEvent(marketId, severity, timestamp)
...

Current State = replay всех событий
```

**Преимущества:**
- Audit trail
- Time-travel debugging
- Event replay для testing

---

## Связанные документы

- [microservices-overview.md](../microservices-overview.md) - микросервисная архитектура
- [backend/README.md](../backend/README.md) - все backend системы
- [ARCHITECTURE.md](../../ARCHITECTURE.md) - общая архитектура

---

## История изменений

- v1.0.1 (2025-11-07) - Добавлена информация о микросервисной архитектуре и распределенном состоянии
- v1.0.0 (2025-11-07) - Разбит на 5 частей (из 2097 строк)

