# Task ID: API-TASK-244
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 21:30
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-137 (leaderboard-system API), API-TASK-195 (voice-chat-system API), API-TASK-141 (daily-reset API), API-TASK-161 (anti-cheat infrastructure API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию для аренного геймплейного цикла NECPGAME. В спецификации должны быть матчмейкинг, карты, загрузка лодаутов, телеметрия, награды и профили игроков с учётом интеграций.

**Что нужно сделать:** Сконструировать файл `arena-system.yaml`, описывающий полный REST API для арен, включая схемы, примеры и связь с общими компонентами.

---

## 🎯 Цель задания

Сформировать единый контракт аренного сервиса, который позволит gameplay-service, voice lobby, лидербордам и социальным системам синхронно работать с матчами, наградами и рейтингами.

**Зачем это нужно:**
- Обеспечить честный и отслеживаемый матчмейкинг для киберспортивных арен
- Синхронизировать награды, лидерборды и сезонные активности
- Дать фронтенду и внешним сервисам единый интерфейс для управления матчами и телеметрией

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/02-gameplay/combat/arena-system.md`
**Версия документа:** v1.0.0
**Дата последнего обновления:** 2025-11-07 20:28
**Статус документа:** approved

**Что важно из этого документа:**
- Каталог арен и режимов, включая Neon Circuit, Orbital Pit, Underbelly Gauntlet, HoloGrid Rumble
- Жизненный цикл матча (pre-match lobby, draft, core loop, post-match)
- Экономика наград: ARENA_CHIPS, кейсы, контракты
- Интеграции с Voice Lobby, Leaderboard, Guild Wars, Anti-Cheat, Daily Reset, Replay
- Перечень данных телеметрии и таблиц (`arena_matches`, `arena_rewards`, `arena_leaderboard`)

### Дополнительные источники

- `.BRAIN/03-lore/activities/activities-lore-compendium.md` — лор и контекст арен
- `.BRAIN/03-lore/characters/activity-npc-roster.md` — ведущие NPC и репутации
- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — требования к голосовым каналам
- `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md` — модель рейтингов

### Связанные документы

- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-core.md` — правила телеметрии и анти-чита
- `.BRAIN/05-technical/backend/replay-system/replay-service.md` — интеграция повтора матчей
- `.BRAIN/02-gameplay/world/events/live-events-system.md` — эвенты, влияющие на арены

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/combat/arena-system.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                └── arena-system.yaml
```

**Требования:**
- В начале файла указать блок `Target Architecture` по образцу из шаблона
- Paths держать в одном файле, схемы можно вынести в `api/v1/shared` при необходимости
- Обязательно использовать `#/components/responses/ErrorResponse` из `shared/common/responses.yaml`

---

## 🏗️ Целевая архитектура

### Backend
- Микросервис: gameplay-service
- Порт: 8083
- API Base Path: `/api/v1/arena/*`
- Домены данных: матчмейкинг, телеметрия, награды, рейтинги
- Интеграции: session-service, anti-cheat-service, leaderboard-service, voice-lobby-service

### Frontend
- Модуль: `modules/combat/arenas`
- State Store: `useCombatStore` (состояния `arenaQueue`, `currentMatch`, `arenaRewards`)
- UI компоненты: `@shared/ui` (ArenaMatchCard, MapPreviewCarousel, TeamRosterPanel, RewardPopup), `@shared/forms` (ArenaQueueForm, LoadoutSubmissionForm)
- Layouts: `@shared/layouts/GameLayout`, `@shared/layouts/CompetitiveLayout`
- Хуки: `@shared/hooks/useRealtime`, `@shared/hooks/useCountdown`, `@shared/hooks/useMatchmaking`

### Комментарии для спецификации
- Включить информацию о требуемых Kafka топиках (`arena.match.state`, `arena.telemetry.ingest`) в описаниях моделей
- Подчеркнуть необходимость audit trail для анти-чита в ответах телеметрии

---

## 🔧 Детальный план выполнения

1. Проанализировать документ `.BRAIN/02-gameplay/combat/arena-system.md`, выделить все пользовательские сценарии и интеграции, согласовать названия сущностей (ArenaMatch, ArenaLoadout, ArenaReward).
2. Спроектировать структуру файлов: paths внутри `arena-system.yaml`, схемы данных (ArenaMatchmakingRequest, ArenaMatchSnapshot, ArenaTelemetryPayload, ArenaRewardSummary) с использованием `components`.
3. Описать каждый endpoint: метод, параметры, заголовки, тела запросов, ответы с примерами JSON и ссылками на общие ошибки.
4. Учесть интеграции: добавить запросы к Leaderboard, Daily Reset, Voice Lobby в разделах описаний и зависимостей; задокументировать события и webhooks, если требуются.
5. Провести проверку по чеклисту: целостность ссылок `$ref`, ограничения полей, коды ошибок, безопасность (OAuth2 / bearer), критерии приёмки; приложить файл в `tasks/active/queue`. Обновить `brain-mapping.yaml` и `.BRAIN` документ.

---

## 🌐 Endpoints

### 1. POST `/api/v1/arena/matchmaking/search`
- Назначение: поставить игрока или группу в очередь арены с учётом рейтинга, роли и предпочтений.
- Авторизация: Bearer JWT (scope `arena.queue`)
- Параметры: query `region` (string, optional), `queueType` (enum: RANKED, CASUAL, EVENT)
- Тело запроса (`ArenaMatchmakingRequest`): playerId, partyId?, preferredRoles[], voiceLobbyPreference, loadoutSummary, riskAcceptance (boolean), telemetryHash
- Ответы:
  - 202 Accepted (`ArenaMatchmakingTicket`) — возвращает ticketId, estimatedWait, queuePosition
  - 409 Conflict — игрок уже в очереди (ErrorResponse `BIZ_ARENA_ALREADY_QUEUED`)
  - 422 Unprocessable Entity — нарушены ограничения по имплантам (ErrorResponse `VAL_ARENA_INVALID_LOADOUT`)
- Интеграции: при успешной постановке публикуется событие `arena.matchmaking.started` в Kafka

### 2. POST `/api/v1/arena/matchmaking/cancel`
- Назначение: отменить очередь по ticketId
- Авторизация: Bearer JWT
- Тело: ticketId (uuid), reason (enum: USER_REQUEST, TIMEOUT, PARTY_CHANGED)
- Ответы: 204 No Content, 404 Not Found (ticket отсутствует), 409 Conflict (матч уже назначен)
- Дополнительно: если ticket связан с voice lobby, инициировать удаление комнаты через voice-lobby-service

### 3. GET `/api/v1/arena/maps`
- Назначение: вернуть доступные карты и модификаторы для фильтров и UI
- Авторизация: Optional (public), но с персонализацией при наличии токена
- Параметры: query `queueType`, `seasonId`
- Ответы: 200 OK (`ArenaMapList`) — содержит mapCode, mode, recommendedRoles, rotationWindow, loreHighlights
- Кэширование: ETag + max-age 300 секунд

### 4. POST `/api/v1/arena/match/{matchId}/ready`
- Назначение: подтвердить готовность игрока к старту, синхронизируется с ready-check UI
- Авторизация: Bearer JWT
- Параметры пути: matchId (uuid)
- Тело: playerId, readyState (enum READY/NOT_READY), deviceSignature
- Ответы: 200 OK (текущее состояние readyCheck), 409 Conflict (таймер истёк), 403 Forbidden (не участник матча)
- События: публиковать `arena.match.readyStatusChanged`

### 5. POST `/api/v1/arena/match/{matchId}/loadout`
- Назначение: передать окончательный набор имплантов и гаджетов после драфта
- Тело (`ArenaLoadoutSubmission`): loadoutId, implants[], gadgets[], tacticalMods[], confirmationHash
- Ответы: 200 OK (подтверждение и ограничения), 422 Unprocessable Entity (нарушены лимиты команды), 409 Conflict (драфт закрыт)
- Бизнес-правила: проверять уникальность имплантов в команде, синхронизация с anti-cheat (hash)

### 6. POST `/api/v1/arena/match/{matchId}/telemetry`
- Назначение: потоковая телеметрия боя (kills, assists, damage, styleEvents)
- Заголовки: `X-Telemetry-Chunk` (int), `X-Signature` (HMAC)
- Тело (`ArenaTelemetryPayload`): timestamp, playerMetrics[], eventStream[], anomalyFlags[]
- Ответы: 202 Accepted (данные поставлены в очередь), 400 Bad Request (сломанная последовательность chunk), 413 Payload Too Large (размер > 256KB)
- Интеграции: отправка в ClickHouse, проверка анти-читом, опциональный дубликат в replay-service

### 7. GET `/api/v1/arena/rewards/{seasonId}`
- Назначение: вернуть награды сезона, прогресс и активные контракты спонсоров
- Авторизация: Bearer JWT (scope `arena.rewards`)
- Ответы 200 OK (`ArenaRewardSummary`) — содержит currencies, lootCases, sponsorContracts, streakBonuses
- Критерии: учитывать decay, streak buff, cross-progression

### 8. GET `/api/v1/arena/profile/{playerId}`
- Назначение: профиль игрока в арене (MMR диапазон, статистика, истории наград)
- Авторизация: Bearer JWT (playerId must match or admin scope)
- Ответы: 200 OK (`ArenaProfile`), 404 Not Found (нет данных), 403 Forbidden (нет доступа)
- В ответ включить ссылки на реплей, активные штрафы, историю сезонных рангов

### Дополнительно
- Заложить возможность webhooks: `POST /api/v1/arena/webhooks/events` для подписок спонсоров (опционально, зафиксировать в FAQ)
- Стандартизировать ошибки через `ErrorResponse` (`BIZ_ARENA_...`, `VAL_ARENA_...`, `INT_ARENA_...`)

---

## 🧱 Модели данных

### ArenaMatchmakingRequest
- `playerId` (uuid, required) — идентификатор игрока
- `partyId` (uuid, optional) — группа
- `preferredRoles` (array[string], max 3, enum: ASSAULT, SUPPORT, CONTROL, SCOUT)
- `voiceLobbyPreference` (enum: AUTO, FRIENDS_ONLY, SOLO)
- `loadoutSummary` (object) — топ-level идентификаторы имплантов
- `riskAcceptance` (boolean) — участвует ли в high risk матчах
- `telemetryHash` (string, sha256) — контроль целостности setup

### ArenaMatchmakingTicket
- `ticketId` (uuid)
- `status` (enum: QUEUED, MATCHED, CANCELLED)
- `estimatedWaitSeconds` (integer)
- `queuePosition` (integer)
- `requiredReadyCheck` (boolean)
- `expiresAt` (datetime)

### ArenaLoadoutSubmission
- `playerId` (uuid)
- `teamId` (uuid)
- `loadoutId` (uuid)
- `implants` (array[ArenaImplantRef], min 1, max 5)
- `gadgets` (array[ArenaGadgetRef], max 4)
- `tacticalMods` (array[string], max 3)
- `confirmationHash` (string, sha256)

### ArenaTelemetryPayload
- `matchId` (uuid)
- `chunkIndex` (integer, sequential)
- `timestamp` (datetime, UTC)
- `playerMetrics` (array[ArenaPlayerMetric])
- `eventStream` (array[ArenaEvent])
- `anomalyFlags` (array[string], optional)

### ArenaRewardSummary
- `seasonId` (string)
- `arenaChips` (integer)
- `lootCases` (array[ArenaLootCase])
- `sponsorContracts` (array[SponsorContractProgress])
- `streakBonuses` (array[ArenaStreakBonus])
- `battlePassXp` (integer)

### ArenaProfile
- `playerId` (uuid)
- `currentTier` (enum: BRONZE, NEON, CHROME, BLACKWALL)
- `mmr` (integer)
- `winRate` (number, format float, 0-1)
- `streak` (integer)
- `recentMatches` (array[ArenaMatchSummary], limit 10)
- `reputation` (array[ArenaReputationTrack])
- `penalties` (array[ArenaPenalty], optional)
- `replayLinks` (array[string], optional)

### Дополнительные определения
- ArenaImplantRef: code, slot, rarity
- ArenaGadgetRef: code, cooldown
- ArenaPlayerMetric: playerId, damage, assists, objectives, styleScore
- ArenaEvent: type (enum: KILL, OBJECTIVE, STREAK, ANOMALY), payload, timestamp
- ArenaPenalty: type (enum: LEAVER, TOXICITY, CHEAT), issuedAt, expiresAt, issuer

Все типы дат использовать в формате RFC 3339. Денежные значения в integer (минимум 0). Ограничить массивы 100 элементами.

---

## 📐 Принципы и правила

- Соблюдать SOLID/DRY/KISS при описании схем и путей, исключить дублирование структур
- Использовать общие компоненты (`ErrorResponse`, `Pagination`) через `$ref` из `api/v1/shared/common`
- Все ошибки кодировать через `errorCode` (`BIZ_ARENA_*`, `VAL_ARENA_*`, `INT_ARENA_*`)
- Авторизация — `bearerAuth` из `shared/security/security.yaml`
- Валидация нагрузок: указать максимальные размеры тел (256KB для телеметрии)
- Обязательно документировать rate limits и idempotency ключи в описаниях
- Поддержать расширяемость: schemas предусмотреть `additionalProperties: false`

---

## ✅ Критерии приемки

- В файле присутствуют все перечисленные endpoints с детальными описаниями параметров и ответов
- Все схемы данных описаны, включают обязательные и опциональные поля, типы и ограничения
- Ошибки используют общую модель ErrorResponse с корректными кодами `BIZ_`, `VAL_`, `INT_`
- В разделе `info.description` упомянуты интеграции: Leaderboard, Voice Lobby, Daily Reset, Anti-Cheat, Replay
- В каждом endpoint указаны требования авторизации и безопасность
- Примеры запросов и ответов приведены минимум для 4 endpoints (matchmaking, ready, telemetry, rewards)
- Документация описывает реакцию системы на таймаут ready-check и отмену очереди
- Схемы телеметрии включают ограничения chunkIndex и подпись `X-Signature`
- В описаниях rewards отражена логика streak buff и decay
- Файл проходит валидацию OpenAPI 3.0.3 (spectral/openapi-generator)
- В `Target Architecture` указаны микросервис, модуль, UI компоненты, формы, state store

---

## ❓ FAQ

**Вопрос:** Что происходит, если игрок переходит в другую очередь во время драфта?  
**Ответ:** Endpoint `/matchmaking/cancel` должен возвращать 409, а сервис обязан завершить текущий матч с меткой LEAVER и штрафом в `ArenaPenalty`.

**Вопрос:** Как обрабатывать асинхронные обновления матчей?  
**Ответ:** Использовать Kafka события `arena.match.*` и документировать payload в схемах `ArenaEvent`. Реальные-time обновления фронтенда идут через WebSocket, но REST API обеспечивает идемпотентную запись.

**Вопрос:** Нужно ли хранить реплеи внутри этого сервиса?  
**Ответ:** Нет, только ссылаться на replay-service. В `ArenaProfile.replayLinks` хранится массив URL, формируемый другим сервисом.

**Вопрос:** Как связать арены с лайв-эвентами?  
**Ответ:** Добавить query параметр `eventId` в `GET /arena/maps` и `GET /arena/rewards/{seasonId}`, описать влияние эвента в документации.

**Вопрос:** Какие ограничения по отправке телеметрии?  
**Ответ:** Не более 60 запросов в минуту на игрока, chunkIndex должен возрастать без пропусков. При превышении лимита возвращается 429 и логируется `ANOMALY_RATE_LIMIT`.

---

## 📦 Результат

- Файл `api/v1/gameplay/combat/arena-system.yaml` с полным описанием API аренного сервиса
- Обновлённый `brain-mapping.yaml` с записью о документе `.BRAIN/02-gameplay/combat/arena-system.md`
- Статус в `.BRAIN/02-gameplay/combat/arena-system.md` обновлён на `queued` с ссылкой на задачу API-TASK-244








### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.


