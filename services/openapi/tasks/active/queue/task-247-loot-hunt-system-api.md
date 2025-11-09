# Task ID: API-TASK-247
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 22:05
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-223 (clan-war-system API), API-TASK-154 (economy-events API), API-TASK-141 (daily-reset API), API-TASK-161 (anti-cheat infrastructure API)

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию сервиса Loot Hunt: генерация контрактов, управление инстансами, телеметрия, экстракция и распределение наград с учётом PvPvE рисков и экономики.

**Что нужно сделать:** Создать файл `loot-hunt.yaml` с полным описанием REST API для лутингшутер цикла, покрывающим matchmaking, события, телеметрию и интеграции.

---

## 🎯 Цель задания

Дать gameplay-service стандартизированный контракт для динамических миссий Loot Hunt, чтобы фронтенд, экономика и анти-чит работали в едином контуре.

**Зачем это нужно:**
- Запуск процедурных контрактов и отслеживание риск/награда петли
- Синхронизация с экономикой (аукционы, страховка, крафт) и клановыми экспедициями
- Сбор телеметрии для анти-чита, балансировки и реплеев

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/02-gameplay/combat/loot-hunt-system.md`
**Версия документа:** v1.0.0
**Дата последнего обновления:** 2025-11-07 20:39
**Статус документа:** approved

**Что важно из этого документа:**
- Структура миссии: контракт → инфильтрация → loot phase → экстракция
- Типы зон, уровни угрозы, метрики Heat и Exposure
- Эвенты: Black Market Drop, System Overload, Corporate Sweep, Fixer Chain
- Таблицы данных (`loot_contracts`, `loot_instances`, `loot_telemetry`)
- Интеграции с экономикой, клановыми системами, анти-читом и Daily Reset

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/combat-extract.md` — логика экстракции
- `.BRAIN/02-gameplay/economy/economy-trading.md` — рынки и страхование
- `.BRAIN/02-gameplay/social/guild-expeditions.md` (если доступен) — клановые миссии
- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — групповые лобби
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-core.md` — требования телеметрии

### Связанные документы

- `.BRAIN/02-gameplay/world/events/live-events-system.md` — эвенты, влияющие на лут
- `.BRAIN/03-lore/activities/activities-lore-compendium.md` — лор зон и фиксеров

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/combat/loot-hunt.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                └── loot-hunt.yaml
```

**Требования:**
- В header добавить блок Target Architecture
- Paths держать в файле, схемы можно вынести в `components`
- Использовать общие ответы/безопасность (`#/components/responses/ErrorResponse`, `bearerAuth`)

---

## 🏗️ Целевая архитектура

### Backend
- Микросервис: gameplay-service (порт 8083)
- API Base Path: `/api/v1/loot-hunt/*`
- Интеграции: session-service, anti-cheat-service, economy-service, clan-service, replay-service
- Kafka топики: `loot.hunt.queue`, `loot.hunt.telemetry`, `loot.hunt.events`

### Frontend
- Модуль: `modules/combat/loot-hunt`
- State Store: `useCombatStore` (состояния `lootContracts`, `heatLevel`, `activeInstance`)
- UI компоненты: `@shared/ui` (LootContractCard, HeatMeter, ExtractionTimer, RewardPanel)
- Формы: `@shared/forms` (LootContractRequestForm, LoadoutInsuranceForm)
- Layouts: `@shared/layouts/GameLayout`, `@shared/layouts/ExtractionLayout`
- Хуки: `@shared/hooks/useRealtime`, `@shared/hooks/useCountdown`, `@shared/hooks/useRiskMeter`

### Комментарии
- Документировать работу с voice lobby идентификаторами
- Указать требования к анти-чит подписи телеметрии (`X-Telemetry-Signature`)

---

## 🔧 Детальный план выполнения

1. Проанализировать `.BRAIN/02-gameplay/combat/loot-hunt-system.md`, определить сущности: LootContract, LootInstance, HeatMeter, ExtractionPoint, RewardBundle.
2. Спроектировать схему каталогов: контрактные endpoints, управление инстансом, телеметрия, эвенты, награды.
3. Описать эндпоинты с запросами/ответами, кодами ошибок и примерами JSON; учесть андетerming для PvPvE (Exposure, RiskOptIn).
4. Добавить модели данных (ContractRequest, ContractTicket, InstanceState, TelemetryChunk, ExtractionResult, RewardDistribution).
5. Проверить спецификацию по чеклисту, сохранить файл, обновить `brain-mapping.yaml` и документ `.BRAIN`.

---

## 🌐 Endpoints

### 1. POST `/api/v1/loot-hunt/contracts/request`
- Назначение: сформировать контракт по предпочтениям игрока/группы
- Авторизация: Bearer JWT (scope `loot-hunt.contracts`)
- Тело (`LootContractRequest`): playerId, partyId?, preferredZone, riskLevel, objectives[], insurancePlan, voiceLobbyPreference, desiredModifiers[]
- Ответы:
  - 201 Created (`LootContractTicket`) — ticketId, contractId, heatForecast, eta
  - 409 Conflict — уже есть активный контракт (`BIZ_LOOT_CONTRACT_EXISTS`)
  - 422 Unprocessable Entity — неверный уровень риска или отсутствуют роли (`VAL_LOOT_INVALID_REQUEST`)
- Событие: публикует `loot.hunt.queue.created`

### 2. GET `/api/v1/loot-hunt/contracts/active`
- Назначение: получить активные контракты пользователя/группы
- Параметры: query `playerId?`, `partyId?`, `status` (OPEN, IN_PROGRESS, COMPLETED)
- Ответ: 200 OK (`LootContractList`)
- Кэширование: max-age 30 секунд, поддержка ETag

### 3. POST `/api/v1/loot-hunt/instances`
- Назначение: запустить новый инстанс миссии на основе контракта
- Тело (`LootInstanceStartRequest`): contractId, seed?, difficultyMod, eventContext?, voiceLobbyId
- Ответы: 201 Created (`LootInstanceState`), 404 Not Found (нет контракта), 409 Conflict (команда не готова)
- Интеграции: Voice Lobby автоконфигурация, анти-чит handshake

### 4. POST `/api/v1/loot-hunt/instance/{instanceId}/progress`
- Назначение: отправлять прогресс миссии и состояние Heat/Exposure
- Заголовки: `X-Heat-Level`, `X-Exposure-Level`
- Тело (`LootProgressUpdate`): phaseStatus, objectivesCompleted[], lootCollected[], anomalies[], playerStats[]
- Ответы: 200 OK (aggregated state), 409 Conflict (инстанс завершён), 422 Unprocessable Entity (порядок фаз нарушен)

### 5. POST `/api/v1/loot-hunt/instance/{instanceId}/telemetry`
- Назначение: потоковая телеметрия боевых событий
- Заголовки: `X-Telemetry-Chunk` (int), `X-Telemetry-Signature` (sha256)
- Тело (`LootTelemetryChunk`): timestamp, playerEvents[], anomalyFlags[], exposureSnapshot
- Ответы: 202 Accepted, 400 Bad Request (повреждён chunk), 413 Payload Too Large (лимит 256KB)

### 6. POST `/api/v1/loot-hunt/instance/{instanceId}/events`
- Назначение: регистрировать внутриигровые эвенты (Black Market Drop, Corporate Sweep)
- Тело (`LootDynamicEvent`): eventType, triggerTime, payload, handledBy
- Ответы: 200 OK, 404 Not Found, 409 Conflict (эвент уже активен)

### 7. POST `/api/v1/loot-hunt/instance/{instanceId}/extraction`
- Назначение: инициировать экстракцию команды
- Тело (`LootExtractionRequest`): extractionPoint, evacMode (STANDARD, EMERGENCY), cargoSummary, insuranceClaim
- Ответы: 202 Accepted (`ExtractionTicket`), 409 Conflict (экстракция уже идёт), 403 Forbidden (не лидер группы)
- Событие: `loot.hunt.extraction.started`

### 8. POST `/api/v1/loot-hunt/instance/{instanceId}/rewards`
- Назначение: распределить награды после завершения миссии
- Тело (`LootRewardDistribution`): contractId, outcome (SUCCESS, FAILURE, EXTRACTED), lootRolls[], sponsorContracts[], streakBonus
- Ответы: 200 OK (`LootRewardSummary`), 409 Conflict (наград уже выданы), 422 Unprocessable Entity (несогласованные участники)

### 9. GET `/api/v1/loot-hunt/instances/{instanceId}`
- Назначение: получить состояние инстанса, активные модификаторы, таймеры
- Ответ: 200 OK (`LootInstanceState`), 404 Not Found

### 10. GET `/api/v1/loot-hunt/analytics`
- Назначение: выдавать агрегированные метрики (heat distribution, pvp encounter rate)
- Параметры: `metric`, `rangeStart`, `rangeEnd`, `eventId?`
- Ответ: 200 OK (`LootAnalyticsResponse`)

Все ошибки описывать через `ErrorResponse` с кодами `BIZ_LOOT_*`, `VAL_LOOT_*`, `INT_LOOT_*`.

---

## 🧱 Модели данных

### LootContractRequest
- `playerId` (uuid)
- `partyId` (uuid, optional)
- `preferredZone` (enum: ABANDONED_MEGASTRUCTURE, BLACKWALL_BREACH, URBAN_RUINS, OFFSHORE_VAULT)
- `riskLevel` (enum: LOW, MEDIUM, HIGH, EXTREME)
- `objectives` (array[ContractObjective], 1-5)
- `insurancePlan` (enum: NONE, STANDARD, PREMIUM)
- `voiceLobbyPreference` (enum: AUTO, FRIENDS_ONLY, SOLO)
- `desiredModifiers` (array[string], max 3)

### LootContractTicket
- `ticketId` (uuid)
- `contractId` (uuid)
- `status` (enum: QUEUED, READY, CANCELLED)
- `heatForecast` (number, format float, 0-10)
- `exposureForecast` (number, format float, 0-10)
- `estimatedStart` (datetime)

### LootInstanceState
- `instanceId` (uuid)
- `contractId` (uuid)
- `zone` (string)
- `difficultyMod` (number)
- `heatLevel` (number)
- `exposureLevel` (number)
- `phase` (enum: PREP, INFILTRATION, LOOT, EXTRACTION, COMPLETE)
- `participants` (array[LootParticipant])
- `activeEvents` (array[LootDynamicEventState])
- `lootSummary` (LootCollectedSummary)
- `createdAt` (datetime)
- `expiresAt` (datetime)

### LootProgressUpdate
- `phaseStatus` (enum: IN_PROGRESS, COMPLETED, FAILED)
- `objectivesCompleted` (array[string])
- `lootCollected` (array[LootItem])
- `anomalies` (array[LootAnomaly])
- `playerStats` (array[LootParticipantStat])
- `heatDelta` (number)
- `exposureDelta` (number)
- `timestamp` (datetime)

### LootTelemetryChunk
- `instanceId` (uuid)
- `chunkIndex` (integer)
- `timestamp` (datetime)
- `playerEvents` (array[LootPlayerEvent])
- `anomalyFlags` (array[string])
- `exposureSnapshot` (number)

### LootExtractionRequest
- `extractionPoint` (string)
- `evacMode` (enum: STANDARD, EMERGENCY)
- `cargoSummary` (array[LootItem])
- `insuranceClaim` (InsuranceClaim)
- `initiatedBy` (uuid)

### LootRewardDistribution
- `contractId` (uuid)
- `outcome` (enum: SUCCESS, FAILURE, EXTRACTED)
- `lootRolls` (array[LootRewardRoll])
- `sponsorContracts` (array[SponsorContractResult])
- `streakBonus` (integer)
- `clanInfluence` (integer)

### Дополнительные объекты
- ContractObjective: code, type (PRIMARY, SECONDARY), description, rewardWeight
- LootParticipant: playerId, role, loadoutSummary, insuranceStatus
- LootParticipantStat: playerId, damage, assists, stealthScore, anomaliesTriggered
- LootItem: itemCode, rarity, quantity
- LootAnomaly: type (enum), severity, description
- LootDynamicEvent: eventType, payload, duration
- LootDynamicEventState: eventType, status, startedAt, expiresAt
- LootCollectedSummary: totalValue, rareItems[], heatGenerated
- LootRewardRoll: table, result, rarity
- SponsorContractResult: sponsorId, objectiveCode, reward
- InsuranceClaim: plan, deductible, payout
- LootAnalyticsResponse: metricCode, values[], generatedAt

Все строки ограничить 256 символами, массивы максимум 100 элементов, числа неотрицательные. Использовать `additionalProperties: false`.

---

## 📐 Принципы и правила

- Использовать общие компоненты безопасности/ошибок, описывать scopes `loot-hunt.*`
- Указать лимиты запросов: телеметрия ≤60 req/min на игрока, прогресс ≤30 req/min на инстанс
- Документировать idempotency для `extraction` и `rewards` через заголовок `Idempotency-Key`
- Все timestamps в формате RFC 3339 UTC
- Ошибки кодировать через `errorCode` (`BIZ_LOOT_*`, `VAL_LOOT_*`, `INT_LOOT_*`)
- Соблюдать SOLID/DRY/KISS, вынести повторяющиеся структуры в components
- В info.description описать связь с анти-читом, экономикой и эвентами

---

## ✅ Критерии приемки

- Файл `api/v1/gameplay/combat/loot-hunt.yaml` создан с перечисленными endpoints и схемами
- Для каждого endpoint указаны параметры, ответы, коды ошибок и примеры JSON
- В Target Architecture указан микросервис, модуль, UI компоненты, формы, хуки
- Описаны Heat/Exposure метрики и заголовки, ограничения размера тел телеметрии
- Прописаны интеграции с voice lobby, clan wars, economy, anti-cheat
- В info.description отражены эвенты (Black Market Drop, Corporate Sweep и т.д.)
- Примеры предоставлены минимум для контрактов, прогресса, телеметрии и экстракции
- Ошибки используют модель `ErrorResponse`
- Спецификация проходит OpenAPI линтеры (spectral/openapi-generator)
- `brain-mapping.yaml` и `.BRAIN` документ обновлены, статусы синхронизированы

---

## ❓ FAQ

**Вопрос:** Что делать, если Heat Level достиг максимума до завершения миссии?  
**Ответ:** Сервис должен отправить событие `loot.hunt.heat.maxed` и автоматически выставить Exposure Hard Mode. Endpoint `/instances/{id}` должен отражать флаг `heatLockdown`.

**Вопрос:** Можно ли передать контракт другому клану?  
**Ответ:** Нет. Для передачи нужно завершить текущий контракт и инициировать новый. Endpoint `/contracts/request` возвращает 409 с кодом `BIZ_LOOT_TRANSFER_FORBIDDEN` при попытке смены clanId.

**Вопрос:** Как обрабатывать аварийную экстракцию?  
**Ответ:** Использовать `evacMode = EMERGENCY`. Требуется подтверждение большинства участников, и система начисляет штраф к наградам. Задокументируйте дополнительное поле `penaltyApplied` в `ExtractionTicket`.

**Вопрос:** Как интегрировать динамические эвенты Live Events?  
**Ответ:** Передавать `eventContext` в `LootInstanceStartRequest` и поддерживать фильтр `eventId` в `/analytics` и `/contracts/active`.

**Вопрос:** Нужна ли поддержка оффлайн завершения?  
**Ответ:** Да, предусмотреть повторную отправку `rewards` с тем же `Idempotency-Key`. При успешном завершении сервис возвращает 200 и игнорирует дубликаты.

---

## 📦 Результат

- Файл `api/v1/gameplay/combat/loot-hunt.yaml` с полной спецификацией Loot Hunt
- Запись в `brain-mapping.yaml` для `.BRAIN/02-gameplay/combat/loot-hunt-system.md`
- Обновлённый статус в `.BRAIN/02-gameplay/combat/loot-hunt-system.md` с задачей API-TASK-247








### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.


