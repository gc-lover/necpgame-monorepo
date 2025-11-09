# Task ID: API-TASK-248
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 22:20
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-245 (dungeon scenarios API), API-TASK-246 (live events API), API-TASK-247 (loot hunt API)

---

## 📋 Краткое описание

Разработать OpenAPI спецификацию каталога боссов подземелий: REST и WebSocket контракты для world-service, описывающие фазы, сложности, награды, события и аналитические метрики.

**Что нужно сделать:** Создать `dungeon-bosses.yaml`, включающий пути для чтения каталога, фиксации прогресса, управления сложностями, применения последствий и стриминга фаз в реальном времени.

---

## 🎯 Цель задания

Обеспечить gameplay/world сервис полноценным контрактом управления инстансовыми боссами, синхронизированным с каталогом сценариев, экономикой и аналитикой.

**Зачем это нужно:**
- Упростить оркестрацию фаз и D&D проверок в командном PvE.
- Согласовать выдачу наград, ключей Hard/Apex и мировых флагов.
- Собрать телеметрию для балансировки, анти-чита и лайв-ивентов.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/02-gameplay/world/dungeons/dungeon-bosses-catalog.md`
**Версия:** v1.1.0
**Обновлено:** 2025-11-07 20:37
**Статус:** approved (api-readiness: ready)

**Ключевые элементы:**
- Список боссов (`db-echo-guardian`, `db-void-maestro`, `db-bio-harvester`, `db-specter-warden`, `db-rail-tyrant`, `db-glass-reaper`, `db-cinder-archon`).
- Фазы, уникальные навыки, D&D проверки, Apex/Apex+ модификаторы.
- REST-контуры `/world/dungeons/...` и WebSocket `PhaseStart`, `AbilityTrigger`, `SkillChallenge`, `Failure`, `Victory`.
- Таблицы данных `dungeon_bosses`, `dungeon_boss_phases`, `dungeon_boss_difficulties`.
- Влияние на экономику, прогрессию, репутацию и world flags.

### Дополнительные источники

- `.BRAIN/02-gameplay/world/dungeons/dungeon-scenarios-catalog.md` — структура инстансов и идентификаторы.
- `.BRAIN/02-gameplay/world/events/live-events-system.md` — эвенты, модифицирующие боссы.
- `.BRAIN/02-gameplay/combat/arena-system.md` — примеры Apex модификаторов и рейтингов.
- `.BRAIN/02-gameplay/combat/loot-hunt-system.md` — общие механики экстракции и распределения лута.
- `.BRAIN/05-technical/backend/progression-backend.md`, `.BRAIN/05-technical/backend/economy-system.md` — интеграции наград, progression perks, Blueprint Forge.

### Связанные документы

- `.BRAIN/03-lore/activities/activities-lore-compendium.md` — лорные связи.
- `.BRAIN/05-technical/backend/realtime-server/part2-protocol-optimization.md` — требования к стримингу фаз.
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` — контроль телеметрии и проверок.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`
**Целевой файл:** `api/v1/gameplay/world/dungeon-bosses.yaml`
**Версия API:** v1
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── world/
                └── dungeon-bosses.yaml
```

**Файл должен содержать:**
- Paths `/api/v1/dungeons/...` (в одном файле).
- Components для сущностей (Boss, Phase, Ability, Difficulty, Reward, Outcome, SkillChallenge, TelemetryEvent).
- Ссылки на общие компоненты (`#/components/responses/ErrorResponse`, security).
- Секции описания WebSocket событий и Kafka тем (`x-stream`, `x-kafkaTopics`).

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** world-service
- **Порт:** 8086
- **Base Path:** `/api/v1/dungeons/*`
- **Интеграции:** session-service, combat-session-service, economy-service, progression-service, social-service, analytics-service, live-events-service.
- **Event Streams:** Kafka `dungeon.boss.telemetry`, `dungeon.boss.progress`, `dungeon.boss.aftermath`.
- **Storage:** Postgres (`dungeon_bosses`, `dungeon_boss_phases`, `dungeon_boss_difficulties`, `dungeon_boss_loot`).

### Frontend
- **Модуль:** `modules/world/dungeons`
- **State Store:** `useWorldStore` (`dungeonBossCatalog`, `activeBoss`, `phaseTelemetry`, `difficultyRotation`).
- **UI компоненты (@shared/ui):** DungeonBossCard, PhaseTimeline, AbilityTooltip, DifficultyBadge, RewardBreakdownModal.
- **Формы (@shared/forms):** DungeonDifficultyForm, BossCheckpointForm.
- **Layout:** `@shared/layouts/GameLayout`, `@shared/layouts/ActivityLayout`.
- **Hooks:** `@shared/hooks/useRealtime`, `@shared/hooks/useCountdown`, `@shared/hooks/useSkillChallenge`, `@shared/hooks/useHeatmap`.

### Комментарии к спецификации
- В разделе `info.description` описать связь с `dungeon-scenarios`, Live Events и экономикой.
- Указать поддержку WebSocket подписки для HUD (раздел `x-realtime`).
- Детализировать зависимости от анти-чита (подпись `X-Telemetry-Signature`).

---

## 🔧 Детальный план

1. Проанализировать каталог боссов, фазы, навыки, D&D проверки; сформировать перечень сущностей.
2. Спроектировать endpoints: GET каталога, GET деталей, POST checkpoint, PUT сложности, POST rewards, POST aftermath, GET rotation schedule, GET analytics.
3. Определить схемы: `DungeonBoss`, `DungeonBossPhase`, `DungeonAbility`, `SkillChallenge`, `DifficultyModifier`, `RewardBundle`, `AftermathPayload`, `CheckpointRequest`, `TelemetryEvent`, `BossAnalytics`.
4. Описать WebSocket (`/ws/dungeons/{instanceId}/boss`) и события, а также связи с Kafka (метаданные в `x-stream`).
5. Добавить бизнес-правила: уникальность `bossId`, ограничения таймингов фаз, Apex/Apex+ модификаторы, валидация D&D DC.
6. Прописать безопасность: `bearerAuth`, scopes `dungeons.boss.read`, `dungeons.boss.manage`, idempotency для checkpoint/rewards/aftermath.
7. Подготовить примеры JSON для ключевых операций (каталог, детали, checkpoint, rewards, aftermath, telemetry chunk).
8. Проверить по чеклисту, сохранить файл, обновить `brain-mapping.yaml`, внести статус в `.BRAIN` документ.

---

## 🌐 Endpoints

1. **GET `/api/v1/dungeons/{dungeonId}/bosses`**
   - Возвращает список боссов подземелья с метаданными (тип, сложности, ключи, текущая ротация).
   - Параметры: `difficulty?`, `rotationWeek?`, `liveEventId?`.
   - Ответ: 200 (`DungeonBossListResponse`). Ошибки: 404 (неизвестное подземелье), 409 (данные блокированы при обновлении).

2. **GET `/api/v1/dungeons/{dungeonId}/bosses/{bossId}`**
   - Полные данные по боссу: фазы с навыками, D&D проверками, Apex модами, лутом.
   - Ответ: 200 (`DungeonBossDetailResponse`). Ошибки: 404 (босс отсутствует), 423 (контент заблокирован). Добавить ссылки на связанные сценарии и world flags.

3. **POST `/api/v1/dungeons/bosses/{bossId}/checkpoint`**
   - Фиксация прогресса фазы и выдача промежуточных наград.
   - Заголовки: `Idempotency-Key`, `X-Instance-Id`, `X-Phase-Index`.
   - Тело (`BossCheckpointRequest`): status (COMPLETED/FAILED), skillChallenges[], telemetryRef, participants[].
   - Ответ: 202 (`BossCheckpointAccepted`). Ошибки: 409 (фаза уже закрыта), 422 (проверки не совпадают).

4. **PUT `/api/v1/dungeons/bosses/{bossId}/difficulty`**
   - Переключение режима (Normal/Hard/Apex/Apex+), обновление модификаторов и лута.
   - Требует scope `dungeons.boss.manage`.
   - Тело (`BossDifficultyUpdate`): targetMode, effectiveFrom, modifiersOverride?.
   - Ответ: 200 (`BossDifficultyState`). Ошибки: 409 (модификатор конфликтует), 403 (нет прав).

5. **POST `/api/v1/dungeons/bosses/{bossId}/rewards`**
   - Распределение наград после победы/провала.
   - Тело (`BossRewardDistribution`): outcome, lootRolls[], blueprintUnlocks[], reputationDeltas[], battlePassXp, clanInfluence.
   - Ответ: 200 (`BossRewardSummary`). Ошибки: 409 (наград уже выдан), 422 (пустой список участников).

6. **POST `/api/v1/dungeons/bosses/{bossId}/aftermath`**
   - Применение мировых последствий (world flags, live events, economy modifiers).
   - Тело (`BossAftermathPayload`): outcome, worldFlags[], liveEventTriggers[], economyImpacts[], telemetryLink.
   - Ответ: 200 (`BossAftermathResult`). Ошибки: 409 (последствия уже применены), 500 (ошибка интеграции).

7. **GET `/api/v1/dungeons/bosses/rotation`**
   - Возвращает расписание недельной ротации, бонусные модификаторы и live event бусты.
   - Ответ: 200 (`BossRotationSchedule`). Поддержать query `seasonId`.

8. **GET `/api/v1/dungeons/bosses/{bossId}/analytics`**
   - Возвращает метрики (clear rate, challenge fail rate, time to kill) с фильтрами по сложности, составу группы, live event.
   - Параметры: `mode`, `timeRange`, `partySize`, `liveEventId?`.
   - Ответ: 200 (`BossAnalyticsResponse`).

9. **POST `/api/v1/dungeons/bosses/{bossId}/telemetry`**
   - Прием агрегированных телеметрических данных из инстанса.
   - Заголовки: `X-Telemetry-Chunk`, `X-Telemetry-Signature`, `X-Instance-Id`.
   - Тело (`BossTelemetryChunk`): timestamp, phase, events[], anomalies[], heatmap, participants[].
   - Ответ: 202 Accepted. Ошибки: 400, 401 (подпись невалидна), 413 (payload > 256KB).

10. **WebSocket `/ws/dungeons/{instanceId}/boss`** (описать через `x-websocket`): события `PhaseStart`, `PhaseComplete`, `AbilityTrigger`, `SkillChallengeRequest`, `SkillChallengeResult`, `Failure`, `Victory`, `AftermathApplied`.

---

## 🧱 Модели данных

- `DungeonBoss` — bossId, dungeonId, name, bossType, baseDifficulty, loreHook, lootTags, apexAvailable.
- `DungeonBossPhase` — phaseIndex, title, description, abilityRefs[], skillChallenges[], loot, timers.
- `DungeonAbility` — abilityCode, name, description, damageType, checkType (REF/INT/TECH/etc), checkDifficulty, cooldown, visuals.
- `SkillChallenge` — stat (AGI/REF/TECH/COOL), difficulty, failureEffect, successEffect, retryable.
- `DifficultyModifier` — mode, persistentDebuffs[], addSpawnRules[], timerValues, abilityOverrides.
- `BossCheckpointRequest` — phaseIndex, status, participants[], challengeFailures[], lootGranted[], timestamp.
- `BossRewardDistribution` — outcome, participantsRewards[], clanInfluence, reputation, battlePassXp, lootRolls (table, rarity, quantity).
- `BossAftermathPayload` — outcome, worldFlags[], economyAdjustments[], liveEventHooks[], socialReputation, telemetryRef.
- `BossRotationSchedule` — weekNumber, dungeonId, bossId, bonusModifier, startAt, endAt.
- `BossAnalyticsMetric` — metricCode (CLEAR_RATE, CHALLENGE_FAIL_RATE, TIME_TO_KILL, DAMAGE_TAKEN, WIPE_RATE), value, delta, sampleSize, breakdown (by mode, partySize, composition).
- `BossTelemetryChunk` — chunkIndex, timestamp, phase, playerEvents[], anomalyFlags[], abilityTriggers[], heatmapGrid[], signature.
- `BossTelemetryEvent` — type (ABILITY, CHECK, DAMAGE, WIPE, SUCCESS), payload, actorId, targetId, value.

Все структуры должны иметь `additionalProperties: false`, строки ≤256 символов, массивы ≤100 элементов. Использовать UUID для идентификаторов, RFC 3339 для datetimes, decimal с точностью 2 для коэффициентов. Ошибки ссылаться на `BIZ_DUNGEON_*`, `VAL_DUNGEON_*`, `INT_DUNGEON_*`.

---

## 📐 Принципы и правила

- Соблюдать SOLID/DRY/KISS, повторяющиеся блоки (`DungeonBoss`, `SkillChallenge`) вынести в `components/schemas`.
- Использовать `bearerAuth` и scopes `dungeons.boss.read`, `dungeons.boss.manage`, `dungeons.boss.telemetry`.
- Для идемпотентных операций (`checkpoint`, `rewards`, `aftermath`) требовать `Idempotency-Key`.
- Ограничения: телеметрия ≤60 req/min на инстанс, checkpoint ≤20 req/min, payload ≤256KB.
- Документировать интеграцию с Kafka через `x-kafkaTopics`.
- Ссылаться на `dungeon-scenarios.yaml` (API-TASK-245) и `live-events.yaml` (API-TASK-246) для совместимости идентификаторов.
- Указать механизмы анти-чита (hash подпись, проверка последовательности chunkIndex).

---

## ✅ Критерии приемки

- В `dungeon-bosses.yaml` представлены все перечисленные endpoints и WebSocket описание.
- Схемы данных покрывают босса, фазы, проверки, модификаторы, награды, последствия, аналитику и телеметрию.
- В `info.description` отражены ротации, интеграции и влияние на world flags, economy, progression.
- Примеры JSON включены минимум для каталогов, деталей, checkpoint, rewards, aftermath, telemetry.
- Определены коды ошибок с префиксами `BIZ_DUNGEON_*`, `VAL_DUNGEON_*`, `INT_DUNGEON_*`.
- Security схемы и scopes документированы, указаны требования `Idempotency-Key` и `X-Telemetry-Signature`.
- Добавлен блок `Target Architecture` в начале файла (microservice, frontend module, UI компоненты, state store, base path).
- Спецификация проходит линтеры (spectral/openapi-generator) без критических ошибок.
- `brain-mapping.yaml` и `.BRAIN/02-gameplay/world/dungeons/dungeon-bosses-catalog.md` обновлены, статус `queued` отражает API-TASK-248.

---

## ❓ FAQ

**В:** Как обрабатывать перескоки фаз (skip mechanic)?  
**О:** Endpoint `/bosses/{bossId}/checkpoint` должен принимать статус `SKIPPED` с указанием условий. Система регистрирует событие, но не выдает награды, и отмечает необходимость дополнительной проверки D&D.

**В:** Что делать, если live event меняет сложность на лету?  
**О:** В ответе `/bosses/{bossId}` предусмотреть `activeLiveEventModifiers`; `PUT /difficulty` должен принимать источник (`MANUAL`, `LIVE_EVENT`). При live-event смене клиент получает уведомление через WebSocket `DifficultyChanged`.

**В:** Как синхронизировать loot roll с economy-service?  
**О:** В `BossRewardDistribution` добавить ссылку `economyTransactionId`; сервис экономики подтверждает выдачу и публикует событие `economy.loot.issued`. REST ответ должен содержать поле `transactionStatus`.

**В:** Как фиксировать неудавшиеся D&D проверки?  
**О:** Хранить их в `challengeFailures` (статистика для аналитики). Аналитика агрегирует `CHALLENGE_FAIL_RATE`, доступную через `/analytics`.

**В:** Нужно ли поддерживать очки Hard Mode Keycard?  
**О:** Да, `BossRewardDistribution` должно включать `hardModeKeycardGranted` (boolean) и `keycardId`. При `true` экономический сервис записывает предмет в инвентарь.

---

## 📦 Результат

- `api/v1/gameplay/world/dungeon-bosses.yaml` со всеми путями, схемами и real-time описанием.
- Запись в `brain-mapping.yaml` о связи документа с задачей API-TASK-248.
- Обновлённый `.BRAIN/02-gameplay/world/dungeons/dungeon-bosses-catalog.md` со статусом `queued` и ссылкой на задачу.








### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.


