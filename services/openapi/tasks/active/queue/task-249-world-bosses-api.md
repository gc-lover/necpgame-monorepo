# Task ID: API-TASK-249
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 22:35
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-246 (live events API), API-TASK-244 (arena system API), API-TASK-248 (dungeon bosses API)

---

## 📋 Краткое описание

Подготовить OpenAPI спецификацию world-service для мировых боссов: открытые события на 20–60 игроков, циклы спавна, механики фаз, последствия для глобального состояния и наград.

**Что нужно сделать:** Создать `world-bosses.yaml`, включающий каталоги, расписание спавна, управление состоянием боя, телеметрию, награды, триггеры live events и мировые последствия.

---

## 🎯 Цель задания

Дать единый REST/WebSocket контракт для глобальных PvE событий, чтобы фронтенд, аналитика и интеграции (economy, social, clan) имели согласованный интерфейс.

**Зачем это нужно:**
- Управлять lifecycle мировых боссов (анонс → спавн → бой → aftermath).
- Сбалансировать награды и последствия с учётом лиги, live events и фракций.
- Собирать телеметрию open-world боя для анти-чита и аналитики.

---

## 📚 Источники информации

### Основной источник

**Путь:** `.BRAIN/02-gameplay/world/world-bosses-catalog.md`
**Версия:** v1.1.0 (2025-11-07 20:37)
**Статус:** approved, api-readiness: ready

**Ключевые элементы:**
- Каталог боссов (`wb-neon-titan`, `wb-blackwall-wraith`, `wb-valentinos-saint`, `wb-nomad-leviathan`, `wb-netwatch-sphinx`, `wb-eclipse-seraph`, `wb-hivemind-behemoth`).
- Фазовые сценарии, уникальные навыки, D&D проверки, loot, world flags.
- REST контуры `/world/bosses` и WebSocket/analytics требования.
- Таблицы данных и последствия (reputation, live-event/hooks, economy effects).
- Ротации, сезонные модификаторы, интеграция с league system.

### Дополнительные источники

- `.BRAIN/02-gameplay/world/dungeons/dungeon-bosses-catalog.md` — перекрёстные механики, loot, shared telemetries.
- `.BRAIN/02-gameplay/world/events/live-events-system.md` — календарь эвентов, модификаторы live events.
- `.BRAIN/02-gameplay/combat/arena-system.md` — рейтинговая шкала и Apex модификаторы.
- `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md` — глобальные рейтинги для open-world.
- `.BRAIN/05-technical/backend/economy-system.md` — распределение наград и рынки.
- `.BRAIN/05-technical/backend/global-state/global-state-management.md` (если доступен) — world flags.

### Связанные документы

- `.BRAIN/03-lore/activities/activities-lore-compendium.md` — сюжетные триггеры.
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` — потоковая синхронизация событий.
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` — анти-чит телеметрии.

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/world/world-bosses.yaml`
**Версия API:** v1
**Тип:** OpenAPI 3.0.3

**Дерево:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── world/
                └── world-bosses.yaml
```

**Содержимое:**
- Paths `/api/v1/world/bosses/*` (REST) + `x-websocket` для live feed.
- Components с сущностями WorldBoss, Phase, Ability, SpawnWindow, Impact, Reward, Telemetry, Participation.
- Ссылки на общие компоненты (`ErrorResponse`, `bearerAuth`).

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** world-service (порт 8086)
- **Base Path:** `/api/v1/world/*`
- **Интеграции:** live-events-service, economy-service, social-service, leaderboard-service, clan-service, progression-service, announcement-service, anti-cheat-service.
- **Event Streams:** Kafka `world.boss.spawn`, `world.boss.state`, `world.boss.telemetry`, `world.boss.aftermath`.
- **Storage:** Postgres (`world_bosses`, `world_boss_phases`, `world_boss_spawn_schedule`, `world_boss_outcomes`).

### Frontend
- **Модуль:** `modules/world/events`
- **State Store:** `useWorldStore` (`worldBossCatalog`, `activeBossStates`, `spawnTimers`, `aftermathHistory`).
- **UI компоненты:** WorldBossCard, SpawnCountdown, PhaseStatusPanel, AbilityAlertFeed, RewardShowcase, AftermathTimeline.
- **Формы:** WorldBossParticipationForm (опционально), WorldBossAdminSpawnForm.
- **Layouts:** `@shared/layouts/GameLayout`, `@shared/layouts/EventHubLayout`.
- **Hooks:** `@shared/hooks/useRealtime`, `@shared/hooks/useCountdown`, `@shared/hooks/useWorldFlags`, `@shared/hooks/useHeatmap`.

### Комментарии
- В описании указать league tiers (Bronze → Diamond → Mythic) и влияние глобального рейтинга.
- Добавить `x-map` для геокоординат отображения босса на карте.
- Указать безопасность для админских операций (scope `world.boss.manage`).

---

## 🔧 Детальный план

1. Систематизировать данные о боссах: идентификаторы, фазовые навыки, live event триггеры, loot, world flags.
2. Спроектировать REST endpoints: каталог, детали, расписание, состояние боя, участие, награды, последствия, ручные операции (админ).
3. Описать WebSocket `/ws/world/bosses/{bossId}/{instanceId}` для реального времени (фазы, ability alerts, skill challenges, spawn status, aftermath).
4. Добавить эндпоинты аналитики и лидербордов (top damage/heal, participation metrics).
5. Создать схемы данных с ссылками на экономику, прогрессию, репутацию, clan influence.
6. Прописать бизнес-правила: ограничение на одновременные спавны, live event overrides, emergency despawn, retry windows.
7. Указать требования по телеметрии и анти-читу (подписи, частота 30 rps, chunk sequencing).
8. Подготовить примеры JSON и описания ошибок, провести проверку по чеклисту, обновить mapping и `.BRAIN` документ.

---

## 🌐 Endpoints

1. **GET `/api/v1/world/bosses`**
   - Список мировых боссов с базовыми данными, статусом (LOCKED, AVAILABLE, ACTIVE, COOLDOWN), рекомендациями по лиге.
   - Фильтры: `status`, `region`, `leagueTier`, `liveEventId`.
   - Ответ: 200 (`WorldBossCatalogResponse`). Ошибки: 503 (данные недоступны во время обновления).

2. **GET `/api/v1/world/bosses/{bossId}`**
   - Детали босса: фазы, навыки, D&D проверки, loot, world flags, recommended power.
   - Ответ: 200 (`WorldBossDetailResponse`). Ошибки: 404 (неизвестный bossId).

3. **GET `/api/v1/world/bosses/{bossId}/schedule`**
   - Расписание спавнов, временные окна, live event бусты, emergency triggers.
   - Параметры: `rangeStart`, `rangeEnd`, `region`, `leagueTier`.
   - Ответ: 200 (`WorldBossScheduleResponse`).

4. **POST `/api/v1/world/bosses/{bossId}/spawn`** (админ)
   - Форсирует спавн босса с указанными модификаторами (live event, difficulty boost).
   - Тело (`WorldBossSpawnRequest`): spawnWindowId?, modifiers[], liveEventContext?, announcementTemplate.
   - Ответ: 202 (`WorldBossSpawnAccepted`). Ошибки: 403 (нет прав), 409 (босс уже активен).

5. **POST `/api/v1/world/bosses/{bossId}/state`**
   - Обновление состояния боя (PhaseStart, PhaseComplete, Wipe, Victory).
   - Тело (`WorldBossStateUpdate`): instanceId, stateType, phaseIndex?, abilityCode?, timestamp, skillChallenge?, participantsSnapshot.
   - Ответ: 202 (`WorldBossStateAccepted`). Ошибки: 422 (неконсистентная фаза), 409 (параллельное обновление).

6. **POST `/api/v1/world/bosses/{bossId}/telemetry`**
   - Телеметрия open-world боя (до 30 req/min per instance).
   - Заголовки: `X-Telemetry-Chunk`, `X-Telemetry-Signature`, `X-Instance-Id`.
   - Тело (`WorldBossTelemetryChunk`): events[], heatmap, damageMatrix, anomalies.
   - Ответ: 202 Accepted.

7. **POST `/api/v1/world/bosses/{bossId}/participation`**
   - Регистрация/обновление участия игроков, кланов, фракций.
   - Тело (`WorldBossParticipationRequest`): instanceId, playerId, clanId?, role, contributionStats.
   - Ответ: 200 (`WorldBossParticipationReceipt`). Ошибки: 409 (игрок уже зарегистрирован в другом регионе).

8. **POST `/api/v1/world/bosses/{bossId}/rewards`**
   - Распределение наград: loot, league tokens, reputation, battle pass xp, clan influence.
   - Тело (`WorldBossRewardDistribution`): instanceId, outcome, lootRolls[], reputationDeltas[], leaguePoints, economyTransactions[].
   - Ответ: 200 (`WorldBossRewardSummary`). Ошибки: 409 (дубликат), 422 (некорректное распределение).

9. **POST `/api/v1/world/bosses/{bossId}/aftermath`**
   - Применение мировых последствий (world flags, quest unlocks, live event triggers, economy impacts).
   - Тело (`WorldBossAftermathPayload`): outcome, worldFlags[], questsUnlocked[], economyAdjustments[], liveEventHooks[], telemetryRef.
   - Ответ: 200 (`WorldBossAftermathResult`). Ошибки: 409 (уже применено), 500 (ошибка интеграции).

10. **GET `/api/v1/world/bosses/{bossId}/analytics`**
    - Метрики (clear rate, participation by faction, damage distribution, wipe rate, average duration).
    - Параметры: `timeRange`, `leagueTier`, `region`, `liveEventId`, `compositionFilter`.
    - Ответ: 200 (`WorldBossAnalyticsResponse`).

11. **GET `/api/v1/world/bosses/leaderboard`**
    - Глобальный рейтинг по урону, лечению, поддержке, эвентам.
    - Параметры: `bossId?`, `metric`, `seasonId`, `region`.
    - Ответ: 200 (`WorldBossLeaderboardResponse`).

12. **WebSocket `/ws/world/bosses/{bossId}/{instanceId}`**
    - События: `SpawnScheduled`, `SpawnStarted`, `PhaseStart`, `AbilityBroadcast`, `SkillChallengeTriggered`, `SkillChallengeResolved`, `Victory`, `Defeat`, `AftermathApplied`, `LiveEventModifier`, `EmergencyDespawn`.
    - Документировать payload и подписи.

---

## 🧱 Модели данных

- `WorldBoss` — bossId, name, location, region, era, baseDifficulty, recommendedLeague, loreHook, liveEventHooks, lootTags.
- `WorldBossPhase` — phaseIndex, title, description, abilityRefs[], skillChallenges[], objectives, failureConditions, duration.
- `WorldBossAbility` — abilityCode, name, description, damageType, aoe, cooldown, challengeRequirement (stat, difficulty, penalty).
- `SpawnWindow` — windowId, startAt, endAt, region, liveEventModifier, difficultyBoost, announcementTemplate.
- `WorldBossStateUpdate` — stateType (SPAWNED, PHASE_START, PHASE_END, WIPE, VICTORY, DESPAWN), phaseIndex, abilityCode, skillChallenge, timestamp, triggeredBy.
- `WorldBossTelemetryChunk` — chunkIndex, instanceId, timestamp, events[], heatmapGrid[], damageBreakdown, anomalies, signature.
- `WorldBossParticipation` — playerId, clanId?, faction, role, contribution (damage, healing, support, objectives), rewardsPreview.
- `WorldBossRewardDistribution` — participants[], lootRolls[], reputationDeltas[], leaguePoints, battlePassXp, clanInfluence, economyTransactions.
- `WorldBossAftermath` — outcome, worldFlags[], economyAdjustments[], questUnlocks[], liveEventTriggers[], socialReputation.
- `WorldBossAnalyticsMetric` — metricCode (CLEAR_RATE, AVG_DURATION, DAMAGE_TOP, WIPE_RATE, PARTICIPATION_RATE, FACTION_SHARE), value, delta, sampleSize, breakdown.
- `WorldBossLeaderboardEntry` — rank, playerId, clanId?, metricValue, bossId, seasonId, rewardsGranted.

Все схемы без `additionalProperties`, строки ≤256 символов, массивы ≤200 элементов, числовые значения неотрицательные. UUID/ULID для идентификаторов, RFC 3339 timestamps. Ошибки `BIZ_WORLD_BOSS_*`, `VAL_WORLD_BOSS_*`, `INT_WORLD_BOSS_*`.

---

## 📐 Принципы и правила

- Использовать `bearerAuth` и scopes `world.boss.read`, `world.boss.manage`, `world.boss.telemetry`.
- Идемпотентность для `rewards`, `aftermath`, `spawn` — `Idempotency-Key`.
- Ограничить телеметрию до 30 req/min per instance, state updates до 20 req/min.
- Отражать live event overrides (`x-liveEvent`) и league scaling (`x-leagueTier`).
- Поддерживать ссылки на `world-events.yaml`, `dungeon-bosses.yaml`, `leaderboard-core.yaml`.
- Указать требования анти-чита (подписи, последовательность chunkIndex, hash участников).
- Документировать push-уведомления через announcement сервис (response содержит `announcementId`).

---

## ✅ Критерии приемки

- `world-bosses.yaml` содержит все перечисленные REST пути и WebSocket описание, связанные компоненты и примеры.
- В `info.description` отражены каталоги, расписания, league-tier, live event интеграции и мировые последствия.
- Присутствуют схемы для фаз, способностей, расписания, участия, наград, aftermath, аналитики, лидерборда.
- Добавлены примеры JSON для каталога, расписания, state update, telemetry, rewards, aftermath, analytics, leaderboard.
- Ошибки используют префиксы `BIZ_WORLD_BOSS_*`, `VAL_WORLD_BOSS_*`, `INT_WORLD_BOSS_*`.
- Security и rate limits задокументированы, Idempotency-Key и Telemetry Signature обязательны.
- Файл проходит линтеры OpenAPI без критических ошибок.
- `brain-mapping.yaml` и `.BRAIN/02-gameplay/world/world-bosses-catalog.md` обновлены с задачей API-TASK-249.

---

## ❓ FAQ

**В:** Как обработать пересечение двух боссов в одной зоне?  
**О:** Сервис должен запрещать одновременный активный статус в одном регионе. Endpoint `/spawn` возвращает 409 с кодом `BIZ_WORLD_BOSS_REGION_LOCKED`.

**В:** Что если live event требует нестандартных фаз?  
**О:** `WorldBossStateUpdate` допускает `phaseIndex = "EVENT_OVERRIDE"` и `abilityCode` с префиксом `LIVE_EVENT_`. Документируйте условие и включите соответствующий пример.

**В:** Как учитывать участие стримеров и медиапартнёров?  
**О:** В `WorldBossParticipation` добавить поле `mediaTag`. Лидерборд поддерживает фильтр `