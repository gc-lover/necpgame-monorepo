# Task ID: API-TASK-360
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 14:45
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-139, API-TASK-227, API-TASK-299, API-TASK-300

---

## 📋 Краткое описание

Подготовить новую OpenAPI-спецификацию "Combat AI Enemies Matrix", описывающую управление профилями врагов, рейдовыми фазами и телеметрией боёв.

**Что нужно сделать:** Создать/обновить `api/v1/gameplay/combat/ai/ai-enemies.yaml` и связанные компоненты, отразив REST, WebSocket и Event Bus контракты для боевого AI.

---

## 🎯 Цель задания

Обновить API боевого AI до версии матрицы слоёв (Street/Tactical/Mythic/Raid), чтобы gameplay-service, analytics и narrative могли координировать поведение, телеметрию и сюжетные флаги.

**Зачем это нужно:**
- Дать gameplay-service общий контракт управления профилями AI и рейдовыми фазами
- Синхронизировать world-service и analytics-service через события `combat.ai.state` и `raid.telemetry`
- Поддержать фронтенд (raid HUD, threat UI) актуальными данными в реальном времени
- Обеспечить narrative-интеграцию (репутация, сюжетные флаги) без обращений к .BRAIN

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/02-gameplay/combat/combat-ai-enemies.md`
**Версия документа:** v1.0.0 (2025-11-08 12:20)
**Статус документа:** approved, api-readiness: ready

**Что важно из этого документа:**
- Матрица слоёв AI (Street/Tactical/Mythic/Raid) с навыками, DC проверок, лором
- Kafka топики `combat.ai.state`, `world.events.trigger`, `raid.telemetry`
- REST/WebSocket контракты `/combat/ai/*`, `/combat/raids/{raidId}/phase`, `wss://api.necp.game/v1/gameplay/raid/{raidId}`
- YAML профиль `aiprofile` и схемы таблиц `enemy_ai_profiles`, `enemy_ai_abilities`, `raid_boss_phases`
- Требования к телеметрии, балансным метрикам и сюжетным флагам

### Дополнительные источники

- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` — синхронизация боёв
- `.BRAIN/02-gameplay/world/world-state/living-world-kenshi-hybrid.md` — мировые флаги/влияния
- `.BRAIN/05-technical/backend/quest-engine-backend.md` — D&D проверки и флаги квестов
- `.BRAIN/05-technical/backend/analytics/telemetry-pipeline.md` — требования к событиям телеметрии
- `.BRAIN/02-gameplay/combat/arena-system.md` и `raid-*` документы — сценарии рейдов и арен

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-139-combat-session-api.md`
- `API-SWAGGER/tasks/active/queue/task-227-combat-session-api.md`
- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-300-living-world-hybrid-api.md`

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевые файлы:**
- `api/v1/gameplay/combat/ai/ai-enemies.yaml`
- `api/v1/gameplay/combat/ai/ai-enemies-components.yaml`
- `api/v1/gameplay/combat/ai/ai-enemies-events.yaml`

> ⚠️ Ограничить каждый файл ≤400 строк, использовать общие компоненты из `api/v1/shared/common/`.

**API версия:** v1 (semantic version 1.1.0 после обновления)
**Тип файлов:** OpenAPI 3.0.3 YAML + Event schema фрагменты

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                └── ai/
                    ├── ai-enemies.yaml
                    ├── ai-enemies-components.yaml
                    └── ai-enemies-events.yaml
```

**Если `ai-enemies.yaml` уже существует:**
- Обновить до версии 1.1.0 (releaseNotes → добавлены слои, телеметрия, WS события)
- Сохранить обратную совместимость (углубить схемы через новые свойства с `nullable`/`enum`)
- Перенести общие схемы в `ai-enemies-components.yaml`

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервисная архитектура)

- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base Path:** `/api/v1/gameplay/combat/*`
- **Домен:** combat AI, raid orchestration
- **Зависимости:**
  - world-service (raid события, мировые флаги)
  - analytics-service (телеметрия и авто-тюнинг)
  - social-service (репутация фракций)
  - economy-service (loot/rewards)
  - notification-service (оповещения о фазах)
  - auth-service (валидировать GM/Admin роли для админных вызовов)

**Event Bus:** Kafka топики `combat.ai.state`, `raid.telemetry`, `world.events.trigger`

### Frontend (модульная архитектура)

- **Модуль:** `modules/combat/ai`
- **State Store:** `useCombatStore`
- **State:** `enemyProfiles`, `raidPhases`, `threatLevels`, `telemetryFeed`

**UI компоненты (@shared/ui):** EnemyProfileCard, RaidPhaseTimeline, ThreatIndicator, AbilityCooldownMeter

**Формы (@shared/forms):** EncounterSetupForm, RaidPhaseOverrideForm

**Layouts (@shared/layouts):** CombatOpsLayout, RaidCommandCenterLayout

**Hooks (@shared/hooks):** useRealtime, useDebounce, useCharacter, useTelemetryStream

### Комментарий для OpenAPI файлов

В начале `ai-enemies.yaml` добавить комментарий:
```
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - Frontend Module: modules/combat/ai
# - UI Components: EnemyProfileCard, RaidPhaseTimeline, ThreatIndicator, AbilityCooldownMeter
# - Forms: EncounterSetupForm, RaidPhaseOverrideForm
# - Layouts: CombatOpsLayout, RaidCommandCenterLayout
# - Hooks: useRealtime, useTelemetryStream
# - API Base: /api/v1/gameplay/combat/*
```

---

## ✅ Что нужно сделать (детальный план)

### Шаг 1: Анализ и актуализация данных
- Перечитать `.BRAIN/02-gameplay/combat/combat-ai-enemies.md`, выделить сущности (profiles, abilities, raid phases, telemetry)
- Сопоставить с текущей спецификацией `ai-enemies.yaml` (если существует)
- Зафиксировать изменения версии (release notes 1.1.0)

**Результат:** чеклист сущностей и различий, таблица полей для обновления схем

### Шаг 2: Обновление архитектуры и серверов
- Проверить `servers` (только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`)
- Добавить/обновить `info.x-microservice` (name, port, domain, base-path, package `com.necpgame.gameplayservice.combat.ai`)
- Указать ссылки на события Kafka в `externalDocs` или `x-event-stream`

**Результат:** Заголовок спецификации соответствует стандартам проекта

### Шаг 3: Описание REST endpoints
- Для каждого из пяти основных endpoint'ов описать параметры, тела, ответы, ошибки, securityScopes
- Использовать компоненты из `api/v1/shared/common/` (`responses.yaml`, `security.yaml`, `pagination.yaml`)
- Добавить query-параметры фильтрации (`layer`, `faction`, `difficulty`, `raidId`, `since`)

**Результат:** Раздел `paths` покрывает все REST операции с кодами 200/400/401/403/404/409/422/500

### Шаг 4: Схемы данных, события и примеры
- Вынести модели (`EnemyAiProfile`, `AiAbility`, `SavingThrow`, `EncounterRequest`, `TelemetryEvent`, `RaidPhaseUpdate`, `MechanicEvent`, `WorldImpact`) в `ai-enemies-components.yaml`
- Добавить `allOf`/`oneOf` для различных слоёв сложности
- В `ai-enemies-events.yaml` описать payload для Kafka (`combat.ai.state`, `raid.telemetry`, `world.events.trigger`) и WebSocket сообщений (`PhaseStart`, `MechanicTrigger`, `PlayerDown`, `CheckRequired`)
- Приложить примеры JSON для request/response и событий

**Результат:** Полный набор схем + примеров, готовый для codegen

### Шаг 5: Валидация и документация
- Проверить lint (`npx swagger-cli validate ai-enemies.yaml`)
- Убедиться, что все ссылки `$ref` валидны и не превышают 400 строк
- Добавить разделы `x-tags` (Combat AI, Raids) и `x-acceptance-criteria`
- Подготовить заметку в `CHANGELOG` (если ведётся) 

**Результат:** Валидная спецификация, готовая к генерации клиентов (Orval/Feign)

---

## 🔀 Endpoints и события

### REST Endpoints (gameplay-service)
1. **GET `/api/v1/gameplay/combat/ai/profiles`**
   - Назначение: получить список профилей AI
   - Query: `layer`, `faction`, `difficulty`, `page`, `size`, `includeAbilities` (boolean)
   - Ответ 200: `AiProfilePage`
   - Ошибки: 400 (неверные фильтры), 401/403 (auth), 500 (internal)

2. **GET `/api/v1/gameplay/combat/ai/profiles/{profileId}`**
   - Назначение: получить полный профиль + лор
   - Path: `profileId` (string, pattern `[a-z0-9\-]+`)
   - Ответ 200: `EnemyAiProfile`
   - Ошибки: 404 (не найден), 410 (deprecated profile), 500

3. **POST `/api/v1/gameplay/combat/ai/profiles/{profileId}/telemetry`**
   - Назначение: клиентская телеметрия удара/контры
   - Body: `AiTelemetryEvent`
   - Ответ 202: принятие события, включает `correlationId`
   - Ошибки: 400 (некорректные значения), 401/403, 409 (конфликт версии профиля), 429 (rate limit), 500

4. **POST `/api/v1/gameplay/combat/raids/{raidId}/phase`**
   - Назначение: зарегистрировать смену фазы рейда, запустить события и обновить world-state
   - Body: `RaidPhaseTransition`
   - Ответ 201: `RaidPhaseAck`
   - Ошибки: 400, 401/403, 404 ( рейд ), 409 (невалидная последовательность фаз), 422 (проверки не выполнены, ссылка на `CheckRequired`), 500

5. **POST `/api/v1/gameplay/combat/ai/encounter`**
   - Назначение: старт встречи — формирует состав врагов на основе слоя и мировых флагов
   - Body: `EncounterStartRequest`
   - Ответ 201: `EncounterStartResponse` (список `EnemySpawn`, `initialPhase`, `worldImpacts`)
   - Ошибки: 400, 401/403, 409 (слой заблокирован событием), 422 (недостаточно данных party), 500

### WebSocket канал
- **URL:** `wss://api.necp.game/v1/gameplay/raid/{raidId}` (и локальный `ws://localhost:8080/api/v1/gameplay/raid/{raidId}`)
- **События:**
  - `PhaseStart`: `RaidPhaseUpdate`
  - `MechanicTrigger`: `MechanicEvent`
  - `PlayerDown`: `PlayerStatusEvent`
  - `CheckRequired`: `SkillCheckPrompt`
- Требования: heartbeat каждые 10s, reconnect token, подпись `raid:observe`

### Kafka / Event Streams
- `combat.ai.state`
  - Payload: `{ enemyId, profileId, state, threatLevel, timestamp, morale, fear, correlationId }`
  - Producers: gameplay-service | Consumers: analytics-service, world-service
- `raid.telemetry`
  - Payload: `{ raidId, phase, bossHp, mechanics, playerDown, checkResults[], worldFlagsChanged[] }`
  - Producers: gameplay-service | Consumers: analytics-service, notification-service, world-service
- `world.events.trigger`
  - Payload: `{ eventId, triggerSource, aiModifier, worldFlag, expiresAt }`
  - Producers: world-service | Consumers: gameplay-service

---

## 🧱 Модели данных

Определить в `ai-enemies-components.yaml`:

- **EnemyAiProfile**: id, name, layer(enum Street/Tactical/Mythic/Raid), faction, difficulty(enum Bronze/Silver/Gold/Platinum/Diamond/Mythic), narrativeContext (era, event, questHook), stats (level, hp, armorClass, morale, fear), abilities[], lootTable, worldImpact
- **AiAbility**: id, title, description, cooldown, effect(enum), counters[], savingThrow (attribute enum {STR, DEX, CON, INT, WIS, CHA}, dc, failureEffect, successEffect), tags[]
- **RaidBossPhase**: bossId, phaseNumber, hpThreshold, mechanics[], skillChallenges[], rewards[], softEnrageTimer
- **EncounterStartRequest**: locationId, raidId?, layer, party(roles, averageLevel, gearScore, composition), worldFlags[], narrativeContext[], desiredDifficulty
- **EncounterStartResponse**: encounterId, expiresAt, enemyProfiles[], initialPhase, modifiersApplied[], worldImpacts[]
- **AiTelemetryEvent**: encounterId, profileId, abilityId, result(enum {hit, miss, countered}), damage, appliedCounterId?, playerId?, timestamp, latencyMs, clientVersion
- **RaidPhaseTransition**: raidId, fromPhase, toPhase, trigger(enum {hpThreshold, script, gmOverride}), checkResults[], initiatedBy, worldFlags[], notes
- **RaidPhaseAck**: raidId, currentPhase, appliedModifiers[], publishedEvents[]
- **MechanicEvent**: mechanicId, description, severity, requiresResponse(boolean), responseWindowMs, suggestedCounters[]
- **SkillCheckPrompt**: checkId, attribute, dc, participants[], consequenceOnFail, countdownMs
- **WorldImpact**: reputationChanges, globalFlags[], lootModifiers, narrativeUnlocks
- **EnemySpawn**: profileId, spawnCount, spawnType(enum), entryDelayMs, initialState
- **PlayerStatusEvent**: playerId, status(enum {down, rescued, eliminated}), reviveWindowMs, requiredAction

Каждая схема должна:
- Использовать `required`/`nullable`
- Содержать `example`
- Указывать `x-tags` (`CombatAI`, `Raid`, `Telemetry`)

---

## 📐 Принципы и правила

- Соблюдать SOLID/DRY/KISS; не дублировать схемы — выносить в компоненты
- Использовать OpenAPI 3.0.3, общие ответы/безопасность через `$ref` (`api/v1/shared/common/security.yaml#/components/securitySchemes/BearerAuth`)
- Серверы: только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`
- Обязательный `info.x-microservice` (name `gameplay-service`, port 8083, domain `gameplay`, base-path `/api/v1/gameplay/combat`, package `com.necpgame.gameplayservice.combat.ai`)
- Security scopes: `combat.ai.read`, `combat.ai.manage`, `raid.manage`
- Response codes стандартные: 200/201/202, ошибки 400/401/403/404/409/410/422/429/500
- Валидация: использовать `pattern`, `minimum/maximum`, `enum`
- Поддерживать версионирование через `x-api-version: 1.1.0`
- WebSocket описать через `x-websocket` блок (как у `task-227`)
- Асинхронные события задокументировать в `ai-enemies-events.yaml` с `x-message-name`

---

## 🔍 Примеры

Примеры добавить в разделах `examples`:

1. **GET /combat/ai/profiles** response:
```json
{
  "data": [
    {
      "id": "max-tac-captain-2091",
      "name": "MaxTac Captain (2091)",
      "layer": "Mythic",
      "faction": "NCPD-MaxTac",
      "difficulty": "Diamond",
      "stats": {
        "level": 55,
        "hp": 3800,
        "armorClass": 24,
        "morale": 95,
        "fear": 10
      },
      "abilities": [
        {
          "id": "zero-strike",
          "cooldown": 45,
          "effect": "singleTargetDisable",
          "savingThrow": {"attribute": "WIS", "dc": 20}
        }
      ],
      "lootTable": {
        "guaranteed": ["max-tac-insignia"],
        "legendaryChance": 0.18
      }
    }
  ],
  "page": 0,
  "size": 20,
  "total": 132
}
```

2. **POST /combat/ai/encounter** request:
```json
{
  "locationId": "pacifica-substructure-77",
  "layer": "Raid",
  "party": {
    "averageLevel": 52,
    "gearScore": 1850,
    "roles": ["tank", "dps", "dps", "support"],
    "composition": {
      "classes": ["netrunner", "solo", "techie", "medic"],
      "implantsScore": 0.82
    }
  },
  "worldFlags": ["world.flag.blackwall_integrity:medium"],
  "desiredDifficulty": "Mythic",
  "narrativeContext": ["quest-main-042-black-barrier-heist"]
}
```

3. **PhaseStart WS событие:**
```json
{
  "type": "PhaseStart",
  "raidId": "blackwall-expedition",
  "phase": 3,
  "bossHp": 0.62,
  "mechanics": ["EntropySpiral"],
  "check": {"attribute": "INT", "dc": 22, "deadlineMs": 15000}
}
```

4. **Kafka combat.ai.state:**
```json
{
  "enemyId": "blackwall-entity-α",
  "profileId": "blackwall-entity",
  "state": "MechanicTrigger",
  "threatLevel": 0.91,
  "morale": 100,
  "fear": 0,
  "timestamp": "2025-11-08T14:40:12Z",
  "correlationId": "enc-90352-evt-18"
}
```

---

## 🔗 Связности и зависимости

- Требуется консистентность с `combat-session` API (API-TASK-139/227) — использовать те же идентификаторы сессий и встреч
- Зависит от `combat-loadouts` (API-TASK-299) и `living-world-hybrid` (API-TASK-300) для modifiers и world flags
- Интеграция с `quest-engine` (API-TASK-226) — ссылки на skill checks и последствия квестов
- Финансовые награды согласовать с `economy/loot-system` (API-TASK-215, API-TASK-247)
- Репутация и социальные эффекты синхронизировать с `npc-relationships` и `player-orders` API (tasks 352-359)

---

## ✅ Критерии приемки (минимум 12)

1. В `ai-enemies.yaml` обновлена версия 1.1.0 и корректно заполнен `info.x-microservice`
2. Раздел `servers` содержит только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`
3. Все пять REST endpoint'ов задокументированы с параметрами, схемами и кодами ошибок
4. WebSocket поток описан с событиями `PhaseStart`, `MechanicTrigger`, `PlayerDown`, `CheckRequired`
5. Kafka топики `combat.ai.state`, `raid.telemetry`, `world.events.trigger` описаны в `ai-enemies-events.yaml`
6. Схемы `EnemyAiProfile`, `AiAbility`, `EncounterStartRequest`, `EncounterStartResponse`, `RaidPhaseTransition`, `AiTelemetryEvent` присутствуют и согласованы с документом
7. Все примеры запросов/ответов/событий добавлены и проходят JSON-валидаторы
8. Использованы общие компоненты (`api/v1/shared/common/responses.yaml`, `security.yaml`, `pagination.yaml`), нет дублирования кодов ошибок
9. Все новые свойства снабжены описаниями (`description`) и ограничениями (`enum`, `minimum`, `pattern`)
10. Линт (`npx swagger-cli validate`) проходит без ошибок и предупреждений
11. Спецификация содержит `x-tags` (`CombatAI`, `Raid`, `Telemetry`) для каждого endpoint'а
12. Документация перечисляет необходимые scopes (`combat.ai.read`, `combat.ai.manage`, `raid.manage`)
13. Добавлено описание интеграции с world flags и narrative (через поля `worldImpacts`, `narrativeContext`)
14. Раздел FAQ в конце задания заполнен и закрывает потенциальные вопросы исполнителя

---

## ❓ FAQ

**Q:** Чем отличается новая версия от API 2025-11-06 (API-TASK-047)?  
**A:** Матрица слоёв и рейдовые механики требуют расширенных схем (world flags, skill checks, телеметрия). Существующий файл необходимо обновить до v1.1.0 с новой структурой.

**Q:** Нужно ли поддерживать старые клиенты?
**A:** Да. Добавляйте новые поля как `nullable` и отмечайте `deprecated` старые атрибуты, чтобы клиенты могли обновиться постепенно.

**Q:** Где хранить payload WebSocket событий?
**A:** В `ai-enemies-components.yaml` создайте схемы `RaidPhaseUpdate`, `MechanicEvent`, `PlayerStatusEvent` и ссылайтесь на них в `x-websocket` разделе основного файла.

**Q:** Как валидировать skill checks (DC)?
**A:** Используйте enum `attribute` и `minimum/maximum` для `dc`. Добавьте правило: `dc` 5–30, иначе ответ 422.

**Q:** Как координировать с analytics-service?
**A:** Обязательно укажите `correlationId` и `latencyMs` в `AiTelemetryEvent`. Analytics подтягивает события через `combat.ai.state`/`raid.telemetry`.

**Q:** Нужно ли документировать GM override?
**A:** Да, в `RaidPhaseTransition.trigger` предусмотрите значения `gmOverride` и документируйте требуемый scope `raid.manage`.

**Q:** Как связать с narrative задачами?
**A:** Поля `narrativeContext` и `worldImpacts` должны ссылаться на slug-и квестов/флагов (см. `quest-engine` и `living-world` API). Приведите примеры.

---

## 📞 Контакты и ссылки

- Gameplay Systems Owner: `@gameplay-architect`
- Narrative Integration: `@narrative-director`
- Analytics Lead: `@data-ops`
- Руководство по пайплайну событий: `api/v1/shared/common/events-guidelines.md`

---

## 📝 История

- 2025-11-08 14:45 — создано задание API-TASK-360 для версии матрицы AI
