# Task ID: API-TASK-245
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 21:40
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-160 (world-events API), API-TASK-155 (crafting-recipes API), API-TASK-223 (clan-war-system API), API-TASK-141 (daily-reset API)

---

## 📋 Краткое описание

Подготовить OpenAPI спецификацию каталога подземелий NECPGAME: сценарии, модификаторы, матчмейкинг, отслеживание прогресса и наград.

**Что нужно сделать:** Создать файл `dungeons.yaml` с полным описанием REST API для управления подземельями, интеграциями и экономикой наград.

---

## 🎯 Цель задания

Сформировать контракт world-service для инстансовых активностей, чтобы игроки, кланы и системные сервисы могли согласованно создавать, проходить и вознаграждать подземелья.

**Зачем это нужно:**
- Обеспечить прозрачное управление сценариями, модификаторами и уровнями сложности
- Связать награды с экономикой, системой прогрессии и лайв-эвентами
- Дать фронтенду и аналитике консистентные данные о прохождении подземелий

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/02-gameplay/world/dungeons/dungeon-scenarios-catalog.md`
**Версия документа:** v1.0.0
**Дата последнего обновления:** 2025-11-07 20:33
**Статус документа:** approved

**Что важно из этого документа:**
- Типология сценариев (Heist, Ritual, Overrun, Gauntlet, Escort) и их игровые акценты
- Структура этапов: briefing → infiltration → core encounter → extraction → debrief
- Модификаторы и уровни сложности (affixes, Apex)
- Экономика наград: Dungeon Tokens, Blueprint Unlocks, Guild Progress
- Интеграции: Loot Hunt ключи, Voice Lobby каналы, Clan Wars, Quest System
- Перечень таблиц данных (`dungeon_catalog`, `dungeon_instances`, `dungeon_rewards`)

### Дополнительные источники

- `.BRAIN/03-lore/activities/activities-lore-compendium.md` — лор сценариев и NPC
- `.BRAIN/03-lore/characters/activity-npc-roster.md` — кураторы подземелий
- `.BRAIN/02-gameplay/world/events/live-events-system.md` — эвенты, модифицирующие подземелья
- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — групповые каналы

### Связанные документы

- `.BRAIN/02-gameplay/combat/arena-system.md` — общие модификаторы соревнований
- `.BRAIN/02-gameplay/combat/combat-roles-detailed.md` — требования к ролям в группах
- `.BRAIN/02-gameplay/progression/progression-attributes-matrix.md` — требования к атрибутам для модификаторов

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/world/dungeons.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── world/
                └── dungeons.yaml
```

**Требования:**
- Сохранить paths внутри файла, схемы можно частично вынести в `api/v1/shared/world/`
- В описаниях указать связь с live-events и clan wars
- Обеспечить совместимость с существующими `world-events` API (namespace `/api/v1/dungeons/*`)

---

## 🏗️ Целевая архитектура

### Backend
- Микросервис: world-service
- Порт: 8086
- API Base Path: `/api/v1/dungeons/*`
- Интеграции: quest-service, loot-service, clan-service, voice-lobby-service, analytics-service
- Событийная система: Kafka топики `dungeon.instance.lifecycle`, `dungeon.modifier.activated`

### Frontend
- Модуль: `modules/world/dungeons`
- State Store: `useWorldStore` (`dungeonCatalog`, `activeInstance`, `modifierRotation`)
- UI компоненты: `@shared/ui` (DungeonScenarioCard, ModifierBadge, PhaseTimeline, RewardBreakdown), `@shared/forms` (DungeonQueueForm, DungeonModifierSelectForm)
- Layouts: `@shared/layouts/GameLayout`, `@shared/layouts/ActivityLayout`
- Хуки: `@shared/hooks/useRealtime`, `@shared/hooks/useCountdown`, `@shared/hooks/useClan`

### Комментарии к спецификации
- Документировать обязательность `X-Party-Id` и `X-Voice-Lobby-Id` для кооперативных операций
- Указать, что данные прогресса реплицируются в ClickHouse для аналитики

---

## 🔧 Детальный план выполнения

1. Извлечь из `.BRAIN` ключевые сущности и сценарии, определить обязательные поля для каталога, инстансов и наград.
2. Спроектировать схемы данных: DungeonScenario, DungeonInstance, DungeonModifier, DungeonRewardBundle, ClanContribution, DungeonPhaseProgress.
3. Описать endpoints для каталога, создания/вступления в инстанс, обновления прогресса, выдачи наград и управления модификаторами.
4. Задокументировать валидацию ролей, уровень сложности, синхронизацию с live-events и clan wars (query параметры, заголовки).
5. Проверить задание по чеклисту, убедиться в наличии примеров, ссылок на ошибки и обновить `brain-mapping.yaml` и `.BRAIN` документ.

---

## 🌐 Endpoints

### 1. GET `/api/v1/dungeons/catalog`
- Назначение: получить список доступных сценариев с описанием фаз, требований и наград.
- Параметры: `difficulty` (enum: NORMAL, HARD, APEX), `type` (enum: HEIST, RITUAL, OVERRUN, GAUNTLET, ESCORT), `eventId` (string, optional)
- Ответ: 200 OK (`DungeonCatalogResponse`) — содержит массив сценарием, модификаторов доступных в ротации, рекомендуемые роли
- Кэширование: ETag + max-age 300 секунд, но учитывать `eventId`

### 2. POST `/api/v1/dungeons/matchmaking/join`
- Назначение: создать новый инстанс или присоединиться к существующему матчмейкингу подземелья
- Авторизация: Bearer JWT (scope `dungeons.queue`)
- Тело (`DungeonQueueRequest`): scenarioCode, difficulty, partyId, preferredModifiers[], voiceLobbyPreference, clanId?
- Ответы:
  - 201 Created (`DungeonQueueTicket`) — ticketId, instancePreview, estimatedStart
  - 409 Conflict — превышено число активных инстансов на игрока
  - 422 Unprocessable Entity — не удовлетворены требования по ролям/атрибутам
- Интеграция: создаёт запись в `voice-lobby-service` при необходимости

### 3. POST `/api/v1/dungeons/instance/{instanceId}/ready`
- Назначение: подтвердить готовность группы перед запуском сценария
- Тело: partySnapshot, loadoutSummary, modifierSelections
- Ответы: 200 OK (updated readiness roster), 409 Conflict (таймер истёк), 403 Forbidden (не в группе)
- События: `dungeon.instance.readyCheck`

### 4. POST `/api/v1/dungeons/instance/{instanceId}/progress`
- Назначение: отправить прогресс прохождения фаз и ключевых действий
- Заголовки: `X-Phase-Id`, `X-Checkpoint-Id`
- Тело (`DungeonProgressUpdate`): phaseStatus, objectivesCompleted, anomalies[], lootAcquired, clanContribution
- Ответы: 200 OK (aggregate progress), 409 Conflict (фаза завершена), 422 Unprocessable Entity (ошибка порядка фаз)
- Интеграции: обновляет Quest System, Clan Wars, Loot Hunt

### 5. POST `/api/v1/dungeons/instance/{instanceId}/checkpoint`
- Назначение: зафиксировать чекпоинт и разрешить рестарт на выбранной фазе
- Тело: checkpointId, description, snapshotHash, unlockCost (DungeonTokens)
- Ответы: 201 Created (checkpoint resource), 409 Conflict (уже зафиксирован), 403 Forbidden (нет прав лидера)

### 6. POST `/api/v1/dungeons/instance/{instanceId}/modifiers`
- Назначение: активировать временные модификаторы (affixes) во время прохождения
- Тело (`DungeonModifierActivation`): modifierCode, triggerPhase, appliedBy, cost
- Ответы: 200 OK (активный список модификаторов), 409 Conflict (несовместимый модификатор), 402 Payment Required (не хватает ресурсов)
- Бизнес-правила: проверять лимиты сложности и совместимость модификаторов

### 7. POST `/api/v1/dungeons/instance/{instanceId}/rewards`
- Назначение: выдать награды после завершения сценария
- Тело (`DungeonRewardGrantRequest`): completionRank, lootTableRolls[], blueprintUnlocks[], clanInfluence, battlePassXp
- Ответы: 200 OK (`DungeonRewardGrantResponse`), 409 Conflict (награды уже распределены), 422 Unprocessable Entity (часть участников не подтвердила получение)
- Интеграции: обновляет экономику, progression и achievements, публикует `dungeon.reward.distributed`

### 8. GET `/api/v1/dungeons/instance/{instanceId}`
- Назначение: получить полное состояние инстанса (фазы, участники, активные модификаторы, таймеры)
- Ответ: 200 OK (`DungeonInstanceState`), 404 Not Found, 403 Forbidden (нет доступа)

### 9. GET `/api/v1/dungeons/rewards`
- Назначение: предоставить справочник наград по сценариям и уровням сложности
- Параметры: `scenarioCode`, `difficulty`, `seasonId`
- Ответ: 200 OK (`DungeonRewardCatalog`)

### 10. GET `/api/v1/dungeons/leaderboards`
- Назначение: показать топ кланов и групп по времени и очкам
- Параметры: `scenarioCode`, `seasonId`, `metric` (TIME, SCORE, CLEAN_RUN)
- Ответ: 200 OK (`DungeonLeaderboardResponse`)
- Интеграция: использует leaderboard-service, описать формат `syncToken`

Все endpoints должны ссылаться на `ErrorResponse` и описывать коды `BIZ_DUNGEON_*`, `VAL_DUNGEON_*`, `INT_DUNGEON_*`.

---

## 🧱 Модели данных

### DungeonScenario
- `scenarioCode` (string, kebab-case)
- `name` (string)
- `type` (enum)
- `recommendedRoles` (array[string], min 4, max 10)
- `phaseSequence` (array[DungeonPhaseDescriptor])
- `baseRewards` (DungeonRewardBundle)
- `loreSummary` (string)
- `voiceLobbyTemplate` (string)

### DungeonPhaseDescriptor
- `phaseId` (string)
- `name` (string)
- `objectives` (array[string])
- `timeLimitSeconds` (integer)
- `requiresRoles` (array[string])
- `failureCondition` (string)

### DungeonQueueRequest
- `scenarioCode` (string, required)
- `difficulty` (enum: NORMAL, HARD, APEX)
- `partyId` (uuid)
- `preferredModifiers` (array[string], max 3)
- `voiceLobbyPreference` (enum: AUTO, EXISTING, SILENT)
- `clanId` (uuid, optional)
- `expectedRoles` (array[string], optional)

### DungeonQueueTicket
- `ticketId` (uuid)
- `status` (enum: QUEUED, MATCHED, CANCELLED)
- `instancePreview` (DungeonInstancePreview)
- `estimatedStartUtc` (datetime)

### DungeonInstanceState
- `instanceId` (uuid)
- `scenarioCode` (string)
- `difficulty` (enum)
- `party` (array[DungeonParticipant])
- `activePhase` (DungeonPhaseStatus)
- `modifiers` (array[DungeonModifier])
- `progress` (DungeonProgressMetrics)
- `leaderId` (uuid)
- `createdAt` (datetime)
- `expiresAt` (datetime)

### DungeonProgressUpdate
- `phaseId` (string)
- `status` (enum: IN_PROGRESS, COMPLETED, FAILED)
- `objectivesCompleted` (array[string])
- `lootAcquired` (array[DungeonLootEntry])
- `anomalies` (array[DungeonAnomaly])
- `clanContribution` (ClanContribution)
- `timestamp` (datetime)

### DungeonRewardBundle
- `dungeonTokens` (integer)
- `blueprints` (array[BlueprintUnlock])
- `guildProgress` (integer)
- `battlePassXp` (integer)
- `lootCases` (array[DungeonLootCase])

### DungeonModifier
- `modifierCode` (string)
- `name` (string)
- `description` (string)
- `triggerPhase` (string)
- `cost` (integer)
- `stackingRule` (enum: UNIQUE, STACKABLE_ONCE, STACKABLE_MULTIPLE)

### Дополнительные объекты
- DungeonParticipant: playerId, role, powerScore, loadoutSummary
- DungeonPhaseStatus: phaseId, status, progressPercent, timerRemaining
- DungeonProgressMetrics: totalTime, deaths, revives, bonusObjectives
- ClanContribution: clanId, influenceGained, reputationTrack
- BlueprintUnlock: itemCode, rarity, craftingTier
- DungeonLootEntry: itemId, rarity, quantity, source
- DungeonAnomaly: type, severity, notes
- DungeonLootCase: caseCode, rarity, guaranteedDrops

Все строки ограничить 256 символами, массивы не более 100 элементов. Числовые значения неотрицательные. Для денежных полей использовать integer. Добавить ссылки на shared enums, если уже существуют.

---

## 📐 Принципы и правила

- Использовать единые компоненты ошибок и безопасности (`bearerAuth`, `ErrorResponse`)
- Указывать идемпотентность для операций checkpoint и rewards (заголовок `Idempotency-Key`)
- Описать rate limits: 30 запросов в минуту на `progress`, 10 на `modifiers`
- Обеспечить расширяемость через `additionalProperties: false`
- Придерживаться SOLID/DRY/KISS — вынести общие схемы (Participant, Modifier) в components
- Документировать обязательные интеграционные события для analytics и clan wars
- Учесть локализацию: возвращать `localizedName` массивом при необходимости

---

## ✅ Критерии приемки

- Все перечисленные endpoints задокументированы с параметрами, телами и ответами
- В info.description отражены цели подземелий и связи с Clan Wars, Loot Hunt, Live Events
- Схемы данных покрывают каталоги, инстансы, модификаторы, награды и прогресс
- Ошибки используют коды `BIZ_DUNGEON_*`, `VAL_DUNGEON_*`, `INT_DUNGEON_*`
- Для matchmaking и progress описаны требования к авторизации и ролям
- Примеры JSON присутствуют минимум для запросов join, progress, rewards
- Документация указывает лимиты количества активных инстансов на игрока и клан
- В описании rewards отражена логика clanContribution и blueprint unlocks
- Указан блок Target Architecture с модулем `modules/world/dungeons`
- Файл проходит проверку OpenAPI линтерами (spectral/openapi-generator)
- В разделе security описан OAuth2 scope `dungeons.*`

---

## ❓ FAQ

**Вопрос:** Как обрабатываются частичные группы меньше минимального размера?  
**Ответ:** Endpoint `/matchmaking/join` задаёт `groupFillStrategy` (AUTO_FILL, FRIENDS_ONLY). Если AUTO_FILL, сервис дополняет группу случайными игроками и уведомляет через `DungeonQueueTicket`.

**Вопрос:** Что делать при разрыве соединения лидера инстанса?  
**Ответ:** Инстанс назначает нового лидера (первый по очереди в party) и отправляет событие `dungeon.instance.leaderChanged`; REST ответ `/instance/{id}` должен отражать новое поле `leaderId`.

**Вопрос:** Можно ли переактивировать модификатор после отмены?  
**Ответ:** Да, если `stackingRule` допускает. Документируйте, что повторная активация требует нового запроса с другим `modifierCode` или `stackId`.

**Вопрос:** Как выдаются награды при неполной группе?  
**Ответ:** Сервис распределяет награды пропорционально участию. Endpoint `/rewards` должен описывать поле `distributionMode` (FULL, PARTIAL, CLAN_POOL).

**Вопрос:** Где хранится прогресс для повторных попыток?  
**Ответ:** В `DungeonCheckpoint` (часть `DungeonInstanceState`). Необходимо описать ретрив endpoint `/instance/{id}/checkpoint` (опционально) или указать, что REST возвращает массив `checkpoints` в state.

---

## 📦 Результат

- Файл `api/v1/gameplay/world/dungeons.yaml` со всеми paths и схемами подземелий
- Добавленная запись в `brain-mapping.yaml` для `.BRAIN/02-gameplay/world/dungeons/dungeon-scenarios-catalog.md`
- Обновлённый статус в `.BRAIN/02-gameplay/world/dungeons/dungeon-scenarios-catalog.md` с ссылкой на API-TASK-245








### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.


