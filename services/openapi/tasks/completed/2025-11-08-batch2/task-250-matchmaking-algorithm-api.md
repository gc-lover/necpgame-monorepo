# Task ID: API-TASK-250
**Тип:** API Generation
**Приоритет:** критический
**Статус:** completed
**Создано:** 2025-11-08 09:45
**Завершено:** 2025-11-08 21:45
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-251, API-TASK-252, API-TASK-133, API-TASK-134, API-TASK-237

## 📦 Результат

- Добавлены `matchmaking-algorithm.yaml`, `matchmaking-algorithm-components.yaml`, `matchmaking-algorithm-examples.yaml` с REST/WS контрактами и событиями Kafka.
- Описаны алгоритмы поиска, ready-check, quality analytics, телеметрия ожидания, обязательные заголовки и SLA.
- Обновлены `brain-mapping.yaml`, `.BRAIN/05-technical/backend/matchmaking/matchmaking-algorithm.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Спроектировать OpenAPI спецификацию алгоритмического слоя матчмейкинга, отвечающего за подбор команд на основе рейтингов, ролей, ожидания и сетевой латентности.

**Что нужно сделать:** Создать файл `matchmaking-algorithm.yaml` с полным REST-контрактом для сервисов подбора матчей (PvP и PvE), подтверждения матчей и публикации качества матчей.

---

## 🎯 Цель задания

Обеспечить gameplay-service стандартизированным API, который сочетает очередь, рейтинг и алгоритмы балансировки, чтобы разные режимы (PvP, PvE, рейды) использовали общую логику подбора.

**Зачем это нужно:**
- Снизить время ожидания и исключить несбалансированные матчи
- Дать фронтенду и аналитике доступ к прозрачным метрикам качества матчей
- Интегрировать анти-чит, рейтинг и голосовые лобби в единый процесс подготовки матча

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/05-technical/backend/matchmaking/matchmaking-algorithm.md`
**Версия документа:** v1.0.0
**Дата последнего обновления:** 2025-11-07 05:30
**Статус документа:** approved

**Что важно из этого документа:**
- PvP и PvE алгоритмы, snake draft распределение ролей и оценка качества матча
- Формулы Match Quality Score и требования к сбору телеметрии ожидания
- Потоки подтверждения матчей (accept/decline) и требования к повторному подбору
- Сценарии для role-based PvE, балансировки рандомных и party-заявок

### Дополнительные источники

- `.BRAIN/05-technical/backend/matchmaking/matchmaking-queue.md` — данные очередей, расширение диапазонов
- `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md` — рейтинги и анти-smurf логика
- `.BRAIN/05-technical/backend/party-system.md` — групповые очереди и ready-check
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` — сетевые зоны и latency
- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — голосовые лобби для готовых матчей

### Связанные документы

- `.BRAIN/05-technical/backend/clan-war/clan-war-system.md` — соревновательные матчи кланов
- `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md` — интеграция с глобальными таблицами лидеров

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/matchmaking/matchmaking-algorithm.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── matchmaking/
            ├── README.md (добавить обзор при необходимости)
            └── matchmaking-algorithm.yaml ← создать этот файл
```

**Требования:**
- В шапке описания указать Target Architecture (микросервис, порт, модуль фронтенда)
- Все paths в основном файле, повторно используемые схемы выносить в `components/schemas`
- Подключить `#/components/securitySchemes/bearerAuth` и общие ответы из `api/v1/shared/common/responses.yaml`

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервисная архитектура)

- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base Path:** `/api/v1/matchmaking/*`
- **Домен:** подбор матчей, балансировка команд, расчёт качества
- **Интеграции:**
  - Feign `party-service` → `getPartyMembers(partyId)` для групповых очередей
  - Feign `voice-lobby-service` → `allocateLobby(matchId)` для авто-создания голосовых каналов
  - Feign `leaderboard-service` → `recordMatch(matchId, ratings)` при завершении
  - Feign `session-service` → `fetchLatencyProfile(playerId)` для учёта пинга
- **Event Bus (Kafka):**
  - Публикация: `matchmaking.match.created`, `matchmaking.match.cancelled`, `matchmaking.match.timeout`
  - Подписка: `matchmaking.queue.ready`, `matchmaking.queue.expanded`

### Frontend (модульная архитектура)

- **Модуль:** `modules/gameplay/matchmaking`
- **State Store:** `useMatchmakingStore` (состояния `queueEntries`, `activeMatch`, `qualityMetrics`)
- **UI компоненты:** `@shared/ui` (MatchCard, QueueStatusPanel, ReadyCheckDialog, LatencyBadge)
- **Формы:** `@shared/forms` (MatchConfirmForm, MatchDeclineReasonForm)
- **Layouts:** `@shared/layouts/GameLayout`
- **Хуки:** `@shared/hooks/useRealtime`, `@shared/hooks/useCountdown`, `@shared/hooks/usePolling`

### Комментарии для спецификации

- Отдельно документировать требования к заголовкам `X-Matchmaking-Request-Id` и `X-Latency-Bucket`
- Указать SLA: PvP подбор ≤ 120 секунд, PvE подбор ≤ 90 секунд

---

## 🔧 Детальный план выполнения

1. Проанализировать разделы алгоритма в `.BRAIN/05-technical/backend/matchmaking/matchmaking-algorithm.md`, выписать сущности (MatchTicket, TeamDivision, MatchQuality, ReadyCheck).
2. Определить структуру OpenAPI: разделы `matches`, `ready-check`, `quality`, `analytics`; подготовить скелет файла и ссылки на общие компоненты.
3. Описать endpoints с авторизацией, параметрами, кодами ошибок (`BIZ_MATCH_*`, `VAL_MATCH_*`, `INT_MATCH_*`) и примерами JSON.
4. Смоделировать схемы данных (`MatchCandidate`, `MatchTeam`, `LatencyProfile`, `PvERoleRequirement`, `MatchQualitySnapshot`).
5. Задокументировать события Kafka в разделе Service Communication и предусмотреть webhooks для фронтенда (SSE/WS topics).
6. Проверить спецификацию по чеклисту, добавить Target Architecture, сохранить файл и обновить `brain-mapping.yaml`.
7. Обновить документ `.BRAIN/05-technical/backend/matchmaking/matchmaking-algorithm.md` блоком API Tasks Status с новой задачей и временем.

---

## 🌐 Endpoints

### 1. POST `/api/v1/matchmaking/matches/search`
- Назначение: инициировать подбор матча по набору кандидатов из очереди.
- Тело (`MatchSearchRequest`): queueIds[], mode (PVP_RANKED, PVP_CASUAL, PVE_DUNGEON, RAID), requiredRoles[], latencyCapMs, allowCrossRegion.
- Ответы: 202 Accepted (`MatchSearchTicket`), 409 Conflict (указанные заявки уже в подборе), 422 Unprocessable Entity (несоответствие ролям).
- Событие: `matchmaking.match.search.started`.

### 2. GET `/api/v1/matchmaking/matches/pending`
- Назначение: получить список матчей в статусе PENDING для UI и аналитики.
- Параметры: `mode`, `limit` (≤50), `after` (cursor).
- Ответ: 200 OK (`PendingMatchPage`), поддержка пагинации, `Cache-Control: no-store`.

### 3. GET `/api/v1/matchmaking/matches/{matchId}`
- Назначение: вернуть полный состав матча, роли, значения Match Quality Score.
- Ответы: 200 OK (`MatchDetail`), 404 Not Found (`BIZ_MATCH_NOT_FOUND`).
- Заголовки: `X-Match-Quality` (0-100), `X-Match-Latency-Bucket` (LOW/MEDIUM/HIGH).

### 4. POST `/api/v1/matchmaking/matches/{matchId}/accept`
- Назначение: подтвердить участие игрока/party.
- Тело (`MatchAcceptRequest`): playerId, partyId?, clientLatencyMs, readyCheckToken.
- Ответы: 204 No Content, 409 Conflict (`BIZ_MATCH_ALREADY_CONFIRMED`), 403 Forbidden (не автор заявки).
- Веб-событие: `matchmaking.match.ready-check.update`.

### 5. POST `/api/v1/matchmaking/matches/{matchId}/decline`
- Назначение: отклонить матч с указанием причины для последующей аналитики.
- Тело (`MatchDeclineRequest`): playerId, reason (enum: ROLE_MISMATCH, HIGH_LATENCY, TEAMMATE_ISSUE, PERSONAL), comment? (≤200 chars).
- Ответы: 204 No Content, 409 Conflict, 410 Gone (матч уже закрыт).
- Событие: `matchmaking.match.cancelled`.

### 6. POST `/api/v1/matchmaking/matches/{matchId}/ready-check`
- Назначение: инициировать или обновить ready-check для party/raid матчей.
- Тело (`ReadyCheckCommand`): initiatorId, expiresInSeconds (≤45).
- Ответы: 202 Accepted (`ReadyCheckState`), 409 Conflict (ready-check активен), 404 Not Found.

### 7. POST `/api/v1/matchmaking/matches/{matchId}/lock`
- Назначение: зафиксировать состав матча и подготовить переход в игровую сессию.
- Тело (`MatchLockRequest`): sessionServerId, voiceLobbyId, lockReason (enum: READY, TIMEOUT, FORCE_START).
- Ответы: 200 OK (`MatchLockResult`), 412 Precondition Failed (не все подтвердили), 503 Service Unavailable (нет сервера).

### 8. GET `/api/v1/matchmaking/matches/{matchId}/quality`
- Назначение: получить расчёт Match Quality Score с разбивкой по факторам.
- Ответ: 200 OK (`MatchQualityReport`), 404 Not Found.
- Параметры: `includeBreakdown` (bool), `refresh` (bool → пересчитать).

### 9. GET `/api/v1/matchmaking/analytics/quality`
- Назначение: агрегированные метрики качества по режимам.
- Параметры: `mode`, `window` (LAST_15M, LAST_HOUR, DAILY), `region?`.
- Ответ: 200 OK (`MatchQualityAnalytics`), 400 Bad Request (неверное окно).

### 10. POST `/api/v1/matchmaking/matches/{matchId}/telemetry`
- Назначение: регистрировать телеметрию ожидания (время поиска, расширения диапазонов).
- Заголовки: `X-Telemetry-Chunk` (int), `X-Telemetry-Signature` (sha256).
- Тело (`MatchTelemetryBatch`): queueId, waitDurationMs, rangeExpansions[], latencySamples[].
- Ответы: 202 Accepted, 413 Payload Too Large (>256KB), 422 Unprocessable Entity.

Все ошибки маппить на `#/components/responses/ErrorResponse` и использовать коды `BIZ_MATCH_*`, `VAL_MATCH_*`, `INT_MATCH_*`.

---

## 🧱 Модели данных

### MatchSearchRequest
- `queueIds` (array<uuid>, 2-30)
- `mode` (enum: `PVP_RANKED`, `PVP_CASUAL`, `PVE_DUNGEON`, `RAID`, `ARENA_EVENT`)
- `requiredRoles` (array<RoleRequirement>)
- `latencyCapMs` (integer, minimum 30, maximum 250)
- `allowCrossRegion` (boolean)

### RoleRequirement
- `role` (enum: `TANK`, `HEALER`, `DPS`, `SUPPORT`, `SCOUT`)
- `minimum` (integer 0-3)
- `maximum` (integer 1-5)

### MatchDetail
- `matchId` (uuid)
- `mode` (enum)
- `status` (enum: `PENDING`, `READY_CHECK`, `LOCKED`, `CANCELLED`)
- `createdAt` (date-time)
- `teams` (array<MatchTeam>)
- `quality` (MatchQualityReport)
- `latencyProfile` (LatencyProfile)
- `readyCheck` (ReadyCheckState)

### MatchTeam
- `teamId` (string)
- `averageRating` (integer)
- `players` (array<MatchParticipant>)
- `roleSummary` (array<RoleSummary>)

### MatchParticipant
- `playerId` (uuid)
- `partyId` (uuid?)
- `rating` (integer)
- `role` (enum)
- `latencyMs` (integer)
- `smurfFlag` (boolean)

### MatchQualityReport
- `score` (number, format float, 0-100)
- `ratingBalance` (number)
- `roleFulfillment` (number)
- `waitTimePenalty` (number)
- `latencyPenalty` (number)
- `factors` (array<QualityFactor>)

### ReadyCheckState
- `status` (enum: `INITIATED`, `IN_PROGRESS`, `SUCCEEDED`, `FAILED`, `EXPIRED`)
- `expiresAt` (date-time)
- `responses` (array<ReadyCheckResponse>)

### MatchTelemetryBatch
- `queueId` (uuid)
- `waitDurationMs` (integer)
- `rangeExpansions` (array<RangeExpansionEvent>)
- `latencySamples` (array<LatencySample>)
- `partySize` (integer)
- `mode` (enum)

---

## 🔄 Service Communication

### Feign Client calls
- **party-service**: `GET /internal/party/{partyId}` — состав группы и роли
- **voice-lobby-service**: `POST /internal/voice-lobbies` — резервирование голосового канала
- **leaderboard-service**: `POST /internal/leaderboards/matches` — фиксация результата
- **session-service**: `GET /internal/sessions/{playerId}/latency` — сбор сетевой телеметрии

### Event Bus
- **Publishes:**
  - `matchmaking.match.created` (payload: MatchDetail)
  - `matchmaking.match.locked`
  - `matchmaking.match.timeout`
- **Subscribes:**
  - `matchmaking.queue.ready` — новые кандидаты
  - `matchmaking.queue.cancelled` — отклонённые заявки

### Outbox / Telemetry
- Отправка `matchmaking.quality.snapshot` в analytics-service каждые 5 минут

---

## 🗄️ Database

- **Schema:** `matchmaking`
- **Tables:**
  - `matchmaking_matches` — основная информация о матчах, индекс по `status`
  - `matchmaking_participants` — участники, индекс по `(match_id, player_id)`
  - `matchmaking_ready_checks` — состояние ready-check, TTL на записи
  - `matchmaking_quality_snapshots` — сохранённые метрики качества, партиционирование по дате
- **Redis:**
  - `matchmaking:pending:{mode}` — сортированное множество ожидающих матчей
  - `matchmaking:ready-check:{matchId}` — состояние откликов

---

## 🧩 Frontend Usage

- **Feature/Module:** `modules/gameplay/matchmaking`
- **API Client:** генерируется через Orval → `useMatchmakingApi`
- **UI Components:** `MatchCard`, `ReadyCheckDialog`, `QualityBadge`, `LatencyBadge`
- **State:** `useMatchmakingStore` хранит `activeMatch`, `readyCheck`, `qualityReport`
- **Пример:**
```typescript
import { useGetMatchmakingMatchesPending } from '@/api/generated/matchmaking';
import { MatchCard, ReadyCheckDialog } from '@shared/ui';

export function PendingMatchesPanel() {
  const { data } = useGetMatchmakingMatchesPending({ mode: 'PVP_RANKED' });

  return (
    <section>
      {data?.items.map(match => (
        <MatchCard key={match.matchId} match={match} />
      ))}
      <ReadyCheckDialog />
    </section>
  );
}
```

---

## 📝 Implementation Notes

- Все ответы должны содержать `traceId` для корреляции с telemetry.
- В headers endpoints `/telemetry` требовать `X-Telemetry-Signature` (sha256) и документировать формат.
- Граничные значения: максимум 100 матчей в запросах аналитики, максимум 10 диапазонов расширения.
- SLA: подтверждение ready-check ≤ 45 секунд; документировать поведение при таймауте.
- Для PvE матчей — явно описать требования к ролям и fallback стратегию.

---

## ✅ Acceptance Criteria

1. Файл `api/v1/matchmaking/matchmaking-algorithm.yaml` создан и соответствует OpenAPI 3.0.3.
2. В шапке спецификации присутствует блок Target Architecture.
3. Для каждого endpoint описаны запрос, ответы, коды ошибок и примеры JSON.
4. Все ошибки используют общую схему `ErrorResponse` и коды `BIZ_MATCH_*`, `VAL_MATCH_*`, `INT_MATCH_*`.
5. Описаны события Kafka и Feign-вызовы в разделе Service Communication.
6. Схемы данных включают модели MatchDetail, MatchTeam, ReadyCheckState, MatchQualityReport.
7. Описан механизм телеметрии ожидания (endpoint `/telemetry` и модель `MatchTelemetryBatch`).
8. Прописаны ограничения производительности (лимиты, SLA, latency cap) в описаниях.
9. Спецификация проходит проверку чеклиста `tasks/config/checklist.md` без замечаний.
10. В `brain-mapping.yaml` добавлена запись со статусом `queued`.
11. Документ `.BRAIN/05-technical/backend/matchmaking/matchmaking-algorithm.md` обновлён блоком API Tasks Status с задачей `API-TASK-250`.
12. Тестовый пример фронтенда использует сгенерированный клиент `matchmaking`.

---

## ❓ FAQ

**В:** Что делать, если игрок подтверждает матч после таймаута?

**О:** Endpoint `POST /matches/{matchId}/accept` должен возвращать 409 и предлагать новую очередь, алгоритм инициирует пересоздание матча.

**В:** Как учитывать party с неполным составом?

**О:** Используйте `requiredRoles` и укажите в спецификации, что алгоритм может комбинировать party и соло заявки при соблюдении роли.

**В:** Что если latency-профиль недоступен?

**О:** Документируйте fallback: latency считается HIGH, матч помещается в `matchmaking.match.timeout` после 30 секунд без профиля.

**В:** Нужно ли разделять PvP и PvE payload?

**О:** В `MatchSearchRequest.mode` и `MatchTeam` описать флаги `isPvE`, `roleConstraints`, дать примеры для обоих режимов.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

