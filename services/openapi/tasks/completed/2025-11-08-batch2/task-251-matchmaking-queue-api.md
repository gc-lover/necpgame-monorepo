# Task ID: API-TASK-251
**Тип:** API Generation
**Приоритет:** критический
**Статус:** completed
**Создано:** 2025-11-08 09:47
**Завершено:** 2025-11-08 22:05
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-133, API-TASK-134, API-TASK-250

## 📦 Результат

- Созданы `matchmaking-queue.yaml`, `matchmaking-queue-components.yaml`, `matchmaking-queue-examples.yaml` (лимиты <400 строк, REST + SSE).
- Задокументированы операции очереди, расширения, приоритеты, heartbeat, аналитика; определены коды `BIZ_QUEUE_*`, `VAL_QUEUE_*`, `INT_QUEUE_*`.
- Обновлены `brain-mapping.yaml`, `.BRAIN/05-technical/backend/matchmaking/matchmaking-queue.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Разработать OpenAPI спецификацию сервиса очередей матчмейкинга, охватывающего регистрацию игроков/групп, расширение диапазонов поиска, приоритеты ожидания и синхронизацию с алгоритмом подбора.

**Что нужно сделать:** Создать файл `matchmaking-queue.yaml` с REST-контрактом для управления очередями, приоритетами и статусами ожидания.

---

## 🎯 Цель задания

Сформировать единый API для операций с очередями, чтобы gameplay-service и фронтенд могли отслеживать состояние ожидания, получать прогнозы времени и управлять приоритетами.

**Зачем это нужно:**
- Гарантировать честное распределение ожидания между соло и party игроками
- Обеспечить расширяемость (разные режимы, рейды, эвенты)
- Дать аналитике телеметрию ожидания и расширения диапазонов

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/05-technical/backend/matchmaking/matchmaking-queue.md`
**Версия документа:** v1.0.0
**Дата последнего обновления:** 2025-11-07 05:30
**Статус документа:** approved

**Что важно из этого документа:**
- Структура таблиц `matchmaking_queues`, индексы и Redis-ключи
- Логика `enterQueue`, расширение диапазонов (search range expansion), priority boost
- Поддержка party, различных activity types, cooldown и TTL записей
- Endpoint-ы `/queue`, `/queue/status`, правила валидации

### Дополнительные источники

- `.BRAIN/05-technical/backend/matchmaking/matchmaking-algorithm.md` — потребитель очередей
- `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md` — начальный рейтинг и диапазон
- `.BRAIN/05-technical/backend/party-system.md` — party-size и роли
- `.BRAIN/05-technical/backend/session/session-lifecycle-heartbeat.md` — статус онлайна игрока

### Связанные документы

- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — уведомления о готовности команды
- `.BRAIN/05-technical/backend/notification-system.md` — push-уведомления при найденном матче

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/matchmaking/matchmaking-queue.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── matchmaking/
            ├── matchmaking-algorithm.yaml
            └── matchmaking-queue.yaml ← добавить этот файл
```

**Требования:**
- Обязательно ссылаться на общие компоненты ошибок и безопасности (`bearerAuth`)
- Подготовить reusable схемы для QueueEntry, QueueSummary, EstimateWaitResponse
- Зафиксировать лимиты (максимум 10 активных очередей на игрока)

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base Path:** `/api/v1/matchmaking/queue/*`
- **Интеграции:**
  - Feign `rating-service` (часть gameplay) → `getPlayerRating`
  - Feign `party-service` → `getPartyState`
  - Redis для хранения активных очередей и rate-limit
- **События Kafka:**
  - Publishes: `matchmaking.queue.entered`, `matchmaking.queue.left`, `matchmaking.queue.timeout`
  - Subscribes: `matchmaking.match.created` (для очистки очередей)

### Frontend
- **Модуль:** `modules/gameplay/matchmaking`
- **State Store:** `useMatchmakingStore` (`queueTicket`, `estimatedWait`, `priorityBoost`)
- **UI компоненты:** `QueueForm`, `QueueTimer`, `PriorityBadge`, `ExpansionTimeline`
- **Формы:** `@shared/forms/QueueRequestForm`
- **Хуки:** `@shared/hooks/useInterval` для обновления ожидания

### Комментарии
- Указать, что все запросы требуют заголовка `X-Client-Latency` (опционально) для прогноза ожидания
- Документировать ограничения TTL (10 минут) и auto-expire

---

## 🔧 Детальный план выполнения

1. Сформировать разделы API: `Queue`, `Priority`, `Analytics`, `Internal`.
2. Описать модель `QueueRequest` с валидациями (activityType, role, level range, party size).
3. Добавить endpoints для входа/выхода, статусов, расширения диапазона и административных операций.
4. Предусмотреть webhooks/SSE для уведомлений о расширении и найденном матче.
5. Отразить Redis ключи и TTL в описании, указав ограничения.
6. Проверить спецификацию чеклистом, обновить `brain-mapping.yaml` и `.BRAIN` документ.

---

## 🌐 Endpoints

### 1. POST `/api/v1/matchmaking/queue`
- Назначение: добавить игрока или party в очередь.
- Тело (`QueueRequest`): activityType, mode, partyId?, preferredRole, canFill, minLevel, maxLevel, estimatedSkill, expiresAt.
- Ответы: 201 Created (`QueueTicket`), 409 Conflict (уже в очереди), 422 Unprocessable Entity (некорректные уровни/роли).
- Заголовки: `Location: /api/v1/matchmaking/queue/{ticketId}`.

### 2. DELETE `/api/v1/matchmaking/queue/{ticketId}`
- Назначение: покинуть очередь вручную.
- Ответы: 204 No Content, 404 Not Found, 409 Conflict (матч уже найден).

### 3. GET `/api/v1/matchmaking/queue/status`
- Назначение: получить статус всех активных очередей игрока.
- Параметры: `activityType?`, `mode?`.
- Ответ: 200 OK (`QueueStatusList`), включает `estimatedWait`, `currentRatingRange`, `priority`, `expansions`.

### 4. POST `/api/v1/matchmaking/queue/{ticketId}/priority`
- Назначение: применить ручное повышение приоритета (админ или эвент).
- Тело (`PriorityAdjustmentRequest`): priorityDelta, reason, expiresInSeconds.
- Ответы: 202 Accepted (`QueuePriorityState`), 403 Forbidden (нет прав), 404 Not Found.

### 5. POST `/api/v1/matchmaking/queue/{ticketId}/expand`
- Назначение: форсировать расширение диапазона для эвентов/турниров.
- Тело (`RangeExpansionCommand`): newRatingRange, expandLatency (bool), notifyPlayer (bool).
- Ответы: 202 Accepted, 409 Conflict (уже превышает лимит), 422 Unprocessable Entity.

### 6. GET `/api/v1/matchmaking/queue/analytics/wait-time`
- Назначение: агрегированные метрики ожидания.
- Параметры: `activityType`, `mode`, `window` (LAST_5M, LAST_15M, HOURLY, DAILY), `region?`.
- Ответ: 200 OK (`WaitTimeAnalytics`), 400 Bad Request.

### 7. GET `/api/v1/matchmaking/queue/{ticketId}`
- Назначение: получить подробную информацию по конкретному тикету.
- Ответ: 200 OK (`QueueEntryDetail`), 404 Not Found.
- Заголовки: `X-Queue-Priority`, `X-Queue-Range`.

### 8. POST `/api/v1/matchmaking/queue/{ticketId}/heartbeat`
- Назначение: продлить TTL заявки (клиент отправляет раз в минуту).
- Тело: пустое, заголовок `X-Client-Latency`.
- Ответы: 204 No Content, 410 Gone (тикет истёк).

### 9. POST `/api/v1/matchmaking/queue/{ticketId}/snapshot`
- Назначение: сохранить снимок состояния очереди для диагностики.
- Тело (`QueueSnapshotRequest`): reason, includeHistory (bool).
- Ответы: 202 Accepted, 404 Not Found.

### 10. GET `/api/v1/matchmaking/queue/events/stream`
- Назначение: SSE-поток уведомлений (range expanded, priority changed, match found).
- Ответ: `text/event-stream`, события `queue.rangeExpanded`, `queue.priorityBoost`, `queue.matchReady`.

Ошибки: использовать `ErrorResponse` с кодами `BIZ_QUEUE_*`, `VAL_QUEUE_*`, `INT_QUEUE_*`.

---

## 🧱 Модели данных

### QueueRequest
- `activityType` (enum: ARENA, RAID, DUNGEON, LOOT_HUNT, CLAN_WAR)
- `mode` (enum: CASUAL, RANKED, EVENT)
- `partyId` (uuid, optional)
- `partySize` (integer 1-15)
- `preferredRole` (enum: TANK, HEALER, DPS, SUPPORT, FLEX)
- `canFill` (boolean)
- `minLevel` / `maxLevel` (integer)
- `rating` (integer?)
- `ratingRange` (integer default 200)
- `expiresAt` (date-time)

### QueueTicket
- `ticketId` (uuid)
- `playerId` (uuid)
- `partyId?`
- `queuedAt` (date-time)
- `expiresAt` (date-time)
- `priority` (integer)
- `currentRatingRange` (integer)
- `status` (enum: QUEUED, MATCHING, CANCELLED, MATCH_FOUND)

### QueueStatus
- `ticketId`
- `etaSeconds` (integer)
- `waitedSeconds` (integer)
- `priorityBoost` (integer)
- `rangeExpansions` (array<RangeExpansion>)
- `notifications` (array<QueueNotification>)

### RangeExpansion
- `timestamp` (date-time)
- `newRange` (integer)
- `reason` (enum: TIMEOUT, EVENT, ADMIN)
- `latencyCapMs` (integer)

### WaitTimeAnalytics
- `window` (enum)
- `averageWaitSeconds`
- `percentile50` / `percentile90`
- `activeTickets`
- `rangeExpansionsPerTicket`
- `priorityDistribution` (map<int, int>)

### QueueSnapshot
- `ticketId`
- `snapshotTakenAt`
- `queueState` (QueueStatus)
- `rawPayload` (object)

---

## 🔄 Service Communication

### Feign Client calls
- `rating-service`: `GET /internal/ratings/{playerId}?activityType=` — получение актуального MMR
- `party-service`: `GET /internal/parties/{partyId}/composition`
- `session-service`: `GET /internal/sessions/{playerId}/status` — проверка онлайна

### Event Bus
- **Publishes:**
  - `matchmaking.queue.entered`
  - `matchmaking.queue.priority.changed`
  - `matchmaking.queue.range.expanded`
  - `matchmaking.queue.timeout`
- **Subscribes:**
  - `matchmaking.match.locked` — очистка тикета
  - `matchmaking.match.cancelled` — возврат в очередь

### Webhooks/SSE
- `queue.matchReady` → фронтенд и уведомления (payload: ticketId, matchId, expiresIn)

---

## 🗄️ Database

- **Schema:** `matchmaking`
- **Tables:**
  - `matchmaking_queues` (основная таблица, индексы на activity_type/status, rating)
  - `matchmaking_queue_priority` (история повышений, audit)
  - `matchmaking_queue_events` (лог расширений, TTL 7 дней)
- **Redis:**
  - `queue:{activityType}:{mode}` — список тикетов (rightPush)
  - `queue:priority:{ticketId}` — priority score (sorted set)
  - `queue:heartbeat:{ticketId}` — TTL для heartbeat

---

## 🧩 Frontend Usage

- **Feature:** `QueueManagerPanel`
- **API Client:** `useMatchmakingQueue` (Orval генерация)
- **UI:** `QueueForm`, `QueueTimeline`, `PriorityBadge`
- **State:** `useMatchmakingStore` обновляет `queueTicket` и `eta`
- **Пример:**
```typescript
const { mutate: enterQueue } = usePostMatchmakingQueue();

function handleJoin(data: QueueRequest) {
  enterQueue(data, {
    onSuccess: ticket => setQueueTicket(ticket.ticketId),
  });
}
```

---

## 📝 Implementation Notes

- Максимум один активный тикет на игрока в каждом режиме — описать в валидации.
- Для party нужно проверять, что все участники онлайн (через session-service).
- Указать, что priority boost автоматически увеличивается через scheduler (каждые 5 минут) и отражается в SSE.
- Документировать rate-limit: 3 входа в очередь за 30 секунд.
- Для админских операций предусмотреть роль `ROLE_MATCHMAKING_ADMIN`.

---

## ✅ Acceptance Criteria

1. Создан файл `matchmaking-queue.yaml` с корректным OpenAPI.
2. Расписаны все ключевые операции очереди и админские команды.
3. Везде применён `bearerAuth`, описаны нужные scopes (`matchmaking.queue.read`, `matchmaking.queue.write`).
4. Реализованы схемы QueueRequest, QueueTicket, QueueStatus, WaitTimeAnalytics.
5. Документированы SSE события и заголовки.
6. Указаны лимиты по priority и range expansion в описаниях.
7. Задекларированы Feign-вызовы и Kafka события.
8. Проверено чеклистом, ошибок нет.
9. `brain-mapping.yaml` содержит новую запись со статусом `queued`.
10. `.BRAIN/05-technical/backend/matchmaking/matchmaking-queue.md` обновлён с задачей `API-TASK-251` и временной меткой.
11. Frontend пример использует сгенерированный клиент.

---

## ❓ FAQ

**В:** Что если party покидает очередь частично?

**О:** Endpoint DELETE должен поддерживать параметр `partyId` и документировать, что частичное снятие запрещено — возвращает 409.

**В:** Можно ли вручную задавать ratingRange?

**О:** Да, но только в админском endpoint `/expand`; в `QueueRequest` поле доступно лишь для системных клиентов (`matchmaking.queue.manage`).

**В:** Как работает priority при повторном входе?

**О:** Документировать decay: priority сбрасывается при выходе; повторный вход → новый тикет с priority 0.

**В:** Как обрабатывать истечение TTL?

**О:** Endpoint `/heartbeat` описывает, что спустя 10 минут без heartbeat тикет переводится в статус `TIMEOUT`, событие `queue.timeout` публикуется.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

