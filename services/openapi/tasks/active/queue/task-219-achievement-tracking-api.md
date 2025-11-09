# Task ID: API-TASK-219
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:52
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-136, API-TASK-218, API-TASK-210

---

## 📋 Краткое описание

Разработать API для трекинга достижений: получение текущего прогресса, запись событий, обработка batch, выдача наград и уведомления.

**Что нужно сделать:** Создать `api/v1/achievements/achievement-tracking.yaml`, описав REST и event контракты для систем и клиента.

---

## 🎯 Цель задания

Обеспечить надежное обновление прогресса достижений, поддержку realtime уведомлений и отчётности.

**Зачем это нужно:**
- Фиксировать события из различных систем (quests, combat, crafting)
- Поддерживать прогресс-полосы, частичное выполнение, hidden achievements
- Выдавать награды и уведомлять игрока и UI/analytics
- Связать ядро (`API-TASK-218`) с UI (`API-TASK-209`) и live-ops инструментами

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/achievement/achievement-tracking.md`
**Версия:** v1.0.0 (2025-11-07 01:59)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Event tracking pipeline, batch updates, retries
- Progress calculation (threshold, percentage, multi-step)
- Notification flow (`AchievementUnlockedNotification`)
- Redis caching, rate limiting, concurrency control
- WebSocket topics, delayed achievements, weekly resets

### Дополнительные источники

- `.BRAIN/05-technical/backend/achievement/achievement-core.md`
- `.BRAIN/05-technical/backend/achievement/achievement-examples-api.md`
- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/analytics/analytics-reporting.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-218-achievement-core-api.md`
- `API-SWAGGER/tasks/active/queue/task-209-achievement-ui-api.md`
- `API-SWAGGER/tasks/active/queue/task-210-daily-quests-ui-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/achievements/achievement-tracking.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 + event contracts

```
API-SWAGGER/api/v1/achievements/
 ├── achievement-core.yaml
 ├── achievement-tracking.yaml   ← создать/заполнить
 └── achievement-rewards.yaml    (будущая задача)
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service (achievement tracking module)
- **Порт:** 8083
- **API Base Path:** `/api/v1/achievements`
- **Зависимости:**
  - auth-service – проверка игрока
  - event bus (Kafka/RabbitMQ) – получение игровых событий
  - inventory-service – выдача наград
  - economy-service – валюты/перки
  - notification-service – уведомления и push
  - analytics-service – логирование прогресса

### Frontend
- **Модуль:** `modules/progression/achievements`
- **State Store:** `useProgressionStore`
- **State:** `progress`, `recentUnlocks`, `trackingQueue`, `notifications`
- **UI компоненты:** `AchievementProgressList`, `UnlockToast`, `TrackingTimeline`, `BatchProgressModal`
- **Формы:** `AchievementFilterForm`, `TrackingDebugForm`
- **Хуки:** `useAchievementProgress`, `useUnlockNotifications`, `useBatchTracking`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/achievements
# - Dependencies: auth, event bus, inventory, economy, notification, analytics
# - Frontend Module: modules/progression/achievements (useProgressionStore)
# - UI: AchievementProgressList, UnlockToast, TrackingTimeline, BatchProgressModal
# - Forms: AchievementFilterForm, TrackingDebugForm
# - Hooks: useAchievementProgress, useUnlockNotifications, useBatchTracking
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить модели прогресса, статусы, очереди, события.
2. Описать REST эндпоинты для получения прогресса, истории, ручной синхронизации.
3. Добавить event контракты для ingestion (`achievement.progress.update`) и выдачи.
4. Документировать batch API (bulk updates), retries, idempotency.
5. Описать уведомления (WebSocket + push) и throttling.
6. Указать кеширование (Redis), TTL, invalidation стратегии.
7. Добавить примеры и тест-план; пройти чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/achievements/progress`** – текущее состояние достижений игрока (пагинировано, фильтры).
2. **GET `/api/v1/achievements/progress/{achievementId}`** – прогресс конкретного достижения (шаги, проценты).
3. **POST `/api/v1/achievements/progress/sync`** – принудительная синхронизация (для live ops/debug).
4. **POST `/api/v1/achievements/progress/batch`** – batch обновления прогресса (сервисные токены; idempotency key).
5. **GET `/api/v1/achievements/unlocks/recent`** – последние разблокировки, уведомления, pending rewards.
6. **POST `/api/v1/achievements/unlocks/claim`** – подтверждение/получение наград (если требуется manual claim).
7. **GET `/api/v1/achievements/progress/history`** – история прогресс событий (источник, timestamp).
8. **GET `/api/v1/achievements/notifications`** – настройки/предпочтения уведомлений.
9. **PUT `/api/v1/achievements/notifications`** – обновление предпочтений (channels, frequency).
10. **POST `/api/v1/achievements/admin/recalculate`** – перерасчет прогресса (GM/LiveOps, аудит).
11. **POST `/api/v1/achievements/admin/reset`** – сброс прогресса (event reset, weekly).
12. **GET `/api/v1/achievements/progress/summary`** – агрегаты для UI (points earned, rarity breakdown).
13. **GET `/api/v1/achievements/progress/hidden`** – список скрытых достижений (для GM, с маскировкой).
14. **POST `/api/v1/achievements/progress/debug-event`** – отправка тестового события (QA tools).
15. **WS `/api/v1/achievements/progress/stream`** – события: `progress-updated`, `achievement-unlocked`, `rewards-granted`, `batch-processed`, `notification-sent`.

### Event Ingestion Contracts (для сервисов)
- Topic `achievements.progress.update`
- Payload: `playerId`, `achievementId`, `increment`, `source`, `metadata`, `timestamp`
- Batch формат: массив событий + `idempotencyKey`

---

## 🧱 Модели данных

- **AchievementProgress** – `achievementId`, `currentValue`, `targetValue`, `percentage`, `state` (`ACTIVE|COMPLETED|CLAIMED|LOCKED`), `updatedAt`, `source`.
- **ProgressSyncRequest** – `forceRecalculate`, `includeHidden`, `auditId`.
- **BatchProgressRequest** – `events[]`, `idempotencyKey`, `sourceService`.
- **ProgressEvent** – `playerId`, `achievementId`, `value`, `progressType` (`INCREMENT|SET|COMPLETE`), `metadata`.
- **UnlockNotification** – `achievementId`, `name`, `rarity`, `rewards[]`, `unlockedAt`, `displayUntil`.
- **NotificationPreference** – `channel`, `enabled`, `frequency`, `quietHours`.
- **ProgressHistoryEntry** – `timestamp`, `achievementId`, `delta`, `source`, `sessionId`.
- **BatchResult** – `batchId`, `processed`, `failed`, `skipped`, `errors[]`.
- **RealtimeEventPayload** – типизированные события (`progressUpdated`, `achievementUnlocked`, `rewardsGranted`, `batchProcessed`, `notificationSent`).
- **Error Schema (`AchievementTrackingError`)** – codes (`ACHIEVEMENT_LOCKED`, `EVENT_REJECTED`, `IDEMPOTENCY_CONFLICT`, `BATCH_LIMIT`, `NOTIFICATION_DISABLED`, `RESET_DENIED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` для игроков; `ServiceToken` для внутренних событий/Batch.
- Rate limiting: защищать `batch` и `debug` эндпоинты.
- Idempotency: `batch` и event ingestion используют `idempotencyKey`.
- Кэширование: Redis хранилище прогресса; TTL 5 минут; invalidation на обновление.
- Уведомления: throttling, группировка (не более 5 toasts за 10 секунд).
- Инциденты: непредвиденные ошибки `EVENT_REJECTED` → incident-service.
- DRY: ссылки на shared компоненты, reuse моделей из core (через `$ref`).

---

## 🧪 Примеры

- Прогресс достижения «Kill 100 enemies» с increment=5.
- Batch update 3 событий (quest completion, crafting, social).
- WebSocket уведомление `achievement-unlocked`.
- Настройки уведомлений с отключением SMS канала.
- Debug event для QA со `source=QA_TOOL`.

---

## 🔗 Связности и зависимости

- Использует `achievement-core` для метаданных, `achievement-ui` для отображения.
- Интеграция с daily quests, progression, inventory (наград выдача).
- Публикует события для analytics/notification сервисов.

---

## ✅ Критерии приемки

1. Файл `achievement-tracking.yaml` создан и описывает REST/WS + ingestion контракты.
2. Модели прогресса, batch, уведомлений и ошибок задокументированы.
3. Прописаны правила idempotency, кеширования, уведомлений, безопасного доступа.
4. Примеры и тестовые сценарии подготовлены, чеклист выполнен.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Прописаны микросервис, модуль, зависимости, UI компоненты
- [ ] Эндпоинты и события покрывают трекинг и уведомления
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] После сохранения обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как обрабатывать события из оффлайн источников?**
**A:** Через batch API или ingestion topic с отложенной обработкой; события содержат timestamp, система пересчитывает прогресс при необходимости.

**Q:** Что делать с hidden achievements?**
**A:** Прогресс учитывается, но UI получает ограниченную информацию (placeholder). Полные данные доступны через ServiceToken/GM.

**Q:** Нужно ли подтверждение наград?**
**A:** По умолчанию выдача автоматическая; для некоторых достижений допускается manual claim (endpoint `unlocks/claim`).



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

