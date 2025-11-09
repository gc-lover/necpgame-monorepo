# Task ID: API-TASK-252
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** completed
**Создано:** 2025-11-08 09:50
**Завершено:** 2025-11-08 22:25
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-250, API-TASK-251, API-TASK-237, API-TASK-140

## 📦 Результат

- Созданы `matchmaking-rating.yaml`, `matchmaking-rating-components.yaml`, `matchmaking-rating-examples.yaml` (REST + Kafka, <400 строк).
- Описаны операции рейтинга, лидербордов, сезонов, smurf detection; определены схемы `RatingProfile`, `RatingDeltaResult`, `SeasonSummary`, `SmurfFlag`.
- Обновлены `brain-mapping.yaml`, `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Определить OpenAPI спецификацию рейтинговой подсистемы матчмейкинга: расчёт MMR/ELO, управление сезонами, анти-smurf сигналы и выдача лидербордов по режимам.

**Что нужно сделать:** Создать файл `matchmaking-rating.yaml` с REST-контрактом для чтения и обновления рейтингов, расчёта MMR, сезонной статистики и отчётов анти-smurf.

---

## 🎯 Цель задания

Предоставить единый интерфейс для хранения рейтингов, расчёта MMR и интеграции с лидербордами, чтобы матчмейкинг оставался честным и прозрачным.

**Зачем это нужно:**
- Синхронизировать рейтинги между очередью, алгоритмом и лидербордами
- Обеспечить анти-smurf механики и контроль сезонных переходов
- Дать фронтенду доступ к профилям рейтингов и истории

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md`
**Версия:** v1.0.0
**Дата обновления:** 2025-11-07 05:30
**Статус:** approved

**Ключевые моменты:**
- Структура таблицы `player_ratings`, индексы, уникальные ключи по сезонам
- Формула ELO с K-факторами, win rate, streak
- Рейтинговые уровни (tiers/divisions) и анти-smurf проверки
- Endpoints `/ratings/{activityType}`, `/leaderboard/{activityType}`

### Дополнительные источники

- `.BRAIN/05-technical/backend/matchmaking/matchmaking-algorithm.md` — использование рейтинга при подборе
- `.BRAIN/05-technical/backend/progression-backend.md` — награды за рейтинги
- `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md` — глобальные таблицы лидеров
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` — проверки подозрительной активности

### Связанные документы

- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — влияние рейтинга на приоритет лобби
- `.BRAIN/05-technical/backend/economy-system.md` — сезонные награды

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/matchmaking/matchmaking-rating.yaml`
**Версия API:** v1
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── matchmaking/
            ├── matchmaking-algorithm.yaml
            ├── matchmaking-queue.yaml
            └── matchmaking-rating.yaml ← создать
```

**Требования:**
- Подключить `bearerAuth`, общие компоненты ошибок
- Предусмотреть `components/schemas` для RatingProfile, SeasonalStats, SmurfFlag
- Учесть версионирование сезонов через параметры `leagueId`

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **Base Path:** `/api/v1/matchmaking/ratings/*`
- **Интеграции:**
  - Feign `leaderboard-service` → `pushSeasonRanking`
  - Feign `analytics-service` → `storeRatingDelta`
- **События Kafka:**
  - Publishes: `matchmaking.rating.updated`, `matchmaking.rating.season.reset`
  - Subscribes: `matchmaking.match.finalized`
- **Batch jobs:** сезонный сброс, перерасчёт placement matches

### Frontend
- **Модуль:** `modules/gameplay/matchmaking`
- **State Store:** `useMatchmakingStore` (`ratingProfile`, `seasonStats`, `smurfAlerts`)
- **UI:** `RatingBadge`, `TierProgressBar`, `PlacementProgress`
- **Формы:** `@shared/forms/RatingAppealForm` (админ)
- **Хуки:** `@shared/hooks/useInfiniteQuery` для лидерборда

### Примечания
- Указать, что доступ к обновлению рейтингов ограничен scope `matchmaking.ratings.write`
- Документировать SLA для выдачи рейтинга ≤ 50 ms

---

## 🔧 Детальный план выполнения

1. Определить секции API: `Player Ratings`, `Leaderboard`, `Seasons`, `Smurf Detection`.
2. Создать схемы `RatingProfile`, `RatingUpdateRequest`, `SeasonSummary`, `SmurfInvestigation`.
3. Описать endpoints для чтения рейтинга, обновления после матча, выдачи лидерборда и анти-smurf отчётов.
4. Добавить секцию `Service Communication` с Kafka событиями и Feign вызовами.
5. Задокументировать сезонные операции (reset, archive) и ограничения.
6. Проверить файл чеклистом, обновить mapping и документ `.BRAIN`.

---

## 🌐 Endpoints

### 1. GET `/api/v1/matchmaking/ratings/{activityType}`
- Назначение: получить рейтинг текущего игрока.
- Параметры: `activityType`, `leagueId?` (default текущий сезон).
- Ответ: 200 OK (`RatingProfile`), 404 Not Found (нет данных).
- Заголовки: `X-Rating-Tier`, `X-Rating-Division`.

### 2. POST `/api/v1/matchmaking/ratings/{activityType}/delta`
- Назначение: применить изменение рейтинга по результатам матча.
- Тело (`RatingDeltaRequest`): matchId, playerId, opponentRating, result (WIN/LOSS/DRAW), bonusAdjustments, placementFlag.
- Ответы: 202 Accepted (`RatingDeltaResult`), 409 Conflict (дубликат), 422 Unprocessable Entity.
- Событие: `matchmaking.rating.updated`.

### 3. GET `/api/v1/matchmaking/ratings/{activityType}/history`
- Назначение: история изменений рейтинга.
- Параметры: `limit` (≤100), `cursor?`, `leagueId?`.
- Ответ: 200 OK (`RatingHistoryPage`).

### 4. GET `/api/v1/matchmaking/leaderboard/{activityType}`
- Назначение: выдача лидерборда по режиму/региону.
- Параметры: `leagueId`, `region?`, `tier?`, `page`, `pageSize` (≤100).
- Ответ: 200 OK (`LeaderboardPage`).

### 5. POST `/api/v1/matchmaking/ratings/{activityType}/placement`
- Назначение: начать или завершить placement-серию.
- Тело (`PlacementRequest`): playerId, totalGames, wins, losses.
- Ответы: 200 OK (`PlacementStatus`), 409 Conflict (уже завершена).

### 6. POST `/api/v1/matchmaking/ratings/{activityType}/seasons/reset`
- Назначение: инициировать сезонный сброс (админ операция).
- Тело (`SeasonResetRequest`): leagueId, carryOverPercent, softCapRating, tiersMapping.
- Ответы: 202 Accepted, 403 Forbidden (нет прав), 409 Conflict (процесс уже идёт).

### 7. GET `/api/v1/matchmaking/ratings/{activityType}/smurf-flags`
- Назначение: список подозрительных аккаунтов.
- Параметры: `threshold` (default 0.75), `limit` (≤200).
- Ответ: 200 OK (`SmurfFlagList`).

### 8. POST `/api/v1/matchmaking/ratings/{activityType}/smurf-review`
- Назначение: зафиксировать решение по smurf-проверке.
- Тело (`SmurfReviewRequest`): playerId, verdict (CLEAN, WARN, BAN_RECOMMENDED), notes, reviewerId.
- Ответы: 200 OK, 404 Not Found.

### 9. GET `/api/v1/matchmaking/ratings/{activityType}/tiers`
- Назначение: получить конфигурацию рангов (tiers/divisions) и требования.
- Ответ: 200 OK (`TierConfig`), 503 Service Unavailable (конфигурация отсутствует).

### 10. GET `/api/v1/matchmaking/ratings/{activityType}/summary`
- Назначение: агрегированная статистика сезона (avg rating, distribution, winrate).
- Параметры: `leagueId`, `region?`.
- Ответ: 200 OK (`SeasonSummary`).

Ошибки: `ErrorResponse` с кодами `BIZ_RATING_*`, `VAL_RATING_*`, `INT_RATING_*`.

---

## 🧱 Модели данных

### RatingProfile
- `playerId` (uuid)
- `activityType` (enum)
- `leagueId` (string)
- `rating` (integer)
- `peakRating` (integer)
- `tier` (enum: BRONZE…GRANDMASTER)
- `division` (integer 1-5)
- `gamesPlayed` (integer)
- `wins` / `losses`
- `winRate` (number, format float, 0-100)
- `streak` (integer)
- `lastGameAt` (date-time)

### RatingDeltaRequest
- `matchId` (uuid)
- `playerId` (uuid)
- `opponentRating` (integer)
- `result` (enum: WIN, LOSS, DRAW)
- `bonusAdjustments` (array<RatingBonus>)
- `placementFlag` (boolean)

### RatingDeltaResult
- `oldRating`
- `newRating`
- `delta`
- `tierChange` (TierChange?)
- `smurfTriggered` (boolean)

### SmurfFlag
- `playerId`
- `score` (float 0-1)
- `reason` (array: HIGH_WINRATE, FAST_GROWTH, NEW_ACCOUNT_HIGH_RATING)
- `gamesPlayed`
- `flaggedAt`

### SeasonSummary
- `leagueId`
- `seasonName`
- `startedAt` / `endsAt`
- `averageRating`
- `medianRating`
- `distribution` (map<tier, percentage>)
- `topPlayers` (array<LeaderboardEntry>)

### TierConfig
- `tiers` (array<TierDefinition>)
- `placementGames` (integer)
- `decayRules` (DecayRule)

---

## 🔄 Service Communication

### Feign Clients
- `leaderboard-service`: `POST /internal/leaderboards/matchmaking` — синхронизация топов
- `analytics-service`: `POST /internal/analytics/matchmaking/rating-delta`
- `anti-cheat-service`: `POST /internal/anti-cheat/flags` при высоком smurf score

### Events (Kafka)
- **Publishes:** `matchmaking.rating.updated`, `matchmaking.rating.smurf.flagged`, `matchmaking.rating.season.reset`
- **Subscribes:** `matchmaking.match.finalized`, `clan-war.match.completed`

### Scheduler Hooks
- Сезонный reset запускает event `matchmaking.rating.season.pending`

---

## 🗄️ Database

- **Schema:** `matchmaking`
- **Tables:**
  - `player_ratings` — основная таблица (уникальный индекс `(player_id, activity_type, league_id)`)
  - `player_rating_history` — события (партиционирование по месяцу)
  - `player_smurf_flags` — активные флаги
  - `rating_tiers` — конфигурация рангов
- **Materialized Views:** `leaderboard_top100_{activity}` для быстрого чтения

---

## 🧩 Frontend Usage

- **Компоненты:** `RatingBadge`, `RatingProgressChart`
- **API:** `useGetMatchmakingRatingsActivityType`, `useGetMatchmakingLeaderboardActivityType`
- **State:** `useMatchmakingStore` обновляет `ratingProfile` и `leaderboard`
- **Пример:**
```typescript
const { data: profile } = useGetMatchmakingRatingsActivityType({ activityType: 'ARENA' });

return <RatingBadge rating={profile?.rating} tier={profile?.tier} />;
```

---

## 📝 Implementation Notes

- Реализовать rate-limit: максимум 5 delta-запросов на матч, проверить идемпотентность по `matchId`.
- Документировать формулу ELO и примеры вычислений.
- Предусмотреть enum `SeasonStatus` (ACTIVE, PRESEASON, ARCHIVED).
- Указать, что leaderboards кэшируются 60 секунд, поддержка ETag.
- Смurf score >0.8 должен инициировать webhook в модерацию (`POST /moderation/smurf-alert`).

---

## ✅ Acceptance Criteria

1. Создан файл `matchmaking-rating.yaml` с корректной структурой OpenAPI.
2. Описаны операции чтения, обновления рейтингов, лидерборды и сезоны.
3. Все схемы и примеры соответствуют .BRAIN документу.
4. Коды ошибок используют префиксы `BIZ_RATING_*`, `VAL_RATING_*`, `INT_RATING_*`.
5. Задокументированы smurf-метрики и review endpoint.
6. Указаны события Kafka и интеграции с leaderboards/analytics.
7. Файл проходит чеклист без ошибок.
8. `brain-mapping.yaml` дополнен задачей `API-TASK-252`.
9. `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md` содержит статус `queued` с новым task ID.
10. Пример фронтенда использует Orval-клиент.

---

## ❓ FAQ

**В:** Как обрабатывать сезонные переходы?

**О:** Использовать endpoint `/seasons/reset` — в описании пояснить, что он создаёт записи в истории и сбрасывает рейтинги до softCap.

**В:** Нужно ли хранить рейтинги для PvE?

**О:** Да, `activityType` включает PvE режимы (например, `DUNGEON`), документация должна описать разные K-факторы.

**В:** Как идентифицировать smurf без матчей?

**О:** Через `placementFlag` и анализ первых 10 игр; в спецификации указать, что endpoint `/smurf-flags` возвращает пустой список при недостатке данных.

**В:** Можно ли вручную корректировать рейтинг?

**О:** Да, описать админский scope `matchmaking.ratings.manage` и предусмотреть `bonusAdjustments` с audit trail.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

