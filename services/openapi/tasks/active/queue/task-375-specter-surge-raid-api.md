# Task ID: API-TASK-375
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 20:35
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-241, API-TASK-243, API-TASK-351, API-TASK-353

---

## 📋 Краткое описание

Спроектировать производственную OpenAPI спецификацию рейда `Specter Surge`, объединяющего world-, combat-, social- и economy-модули, на основе `.BRAIN/04-narrative/quests/raid/2025-11-07-raid-specter-surge.md`.

**Что нужно сделать:** Создать `api/v1/narrative/raids/specter-surge.yaml` (при необходимости — дополнительные компоненты в `api/v1/narrative/components/raid-specter-surge-schemas.yaml`) для orchestration API narrative-service, описав фазы рейда, проверки, интеграции с зависимыми микросервисами и telemetry.

---

## 🎯 Цель задания

Дать narrative-service контракт, через который рейдовая команда и автоматизация смогут управлять прогрессом Specter Surge, синхронизировать мех/пилот взаимодействие, применять мировые модификаторы и распределять награды.

**Зачем это нужно:**
- Централизовать управление фазами рейда с поддержкой D&D проверок, кооперативных ролей и таймеров.
- Автоматизировать взаимодействия с world-service, combat-service, social-service и economy-service.
- Поставлять фронтенду данные для UI гильдий, world dashboards и HUD рейда.
- Обеспечить телеметрию и SLA-контроль (sync latency, success rate) для оперативного мониторинга.

---

## 📚 Источники информации

### Основной документ

- **Репозиторий:** `.BRAIN`
- **Путь:** `.BRAIN/04-narrative/quests/raid/2025-11-07-raid-specter-surge.md`
- **Версия:** 1.0.0
- **Дата:** 2025-11-07 20:55
- **Статус:** approved, `api-readiness: ready`

**Что важно:**
- Фазы рейда I–V с условиями успеха/провала и их влиянием.
- D&D проверки (Arcana, Hacking, Tactics, Athletics, Leadership) с модификаторами.
- Механики Specter Sync Loop, Dual-Control Combat, City Unrest Feedback, Ghost Logistics.
- Карта API (world/combat/social/economy/narrative) и SLA/observability.
- Награды, флаги, world-state эффекты и гильдейские разблокировки.

### Дополнительные источники

- `.BRAIN/04-narrative/quests/side/2025-11-07-quest-neon-ghosts.md` — prerequisite квест, влияет на флаги.
- `.BRAIN/05-technical/global-state/global-state-management.md` — управление world-state и stability.
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` — контекст мировых событий.
- `API-SWAGGER/api/v1/social/resonance.yaml` (задача API-TASK-243) — social resonance интеграция.
- `API-SWAGGER/api/v1/world/world-interaction-suite.yaml` — world modifiers.
- `API-SWAGGER/api/v1/social/npc-relationships/status.yaml` — social репутации (Ghosts/Helios).
- `API-SWAGGER/api/v1/economy/contracts/activate.yaml` (будущее) — economy эффекты.

### Связанные задания

- `task-241-world-interaction-suite-api.md`
- `task-243-social-resonance-api.md`
- `task-351-npc-hiring-payroll-api.md` (economy распределение наград)
- `task-353-npc-relationships-interactions-api.md`
- `task-180-api-technical-summary-api.md`

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

- **Целевой файл:** `api/v1/narrative/raids/specter-surge.yaml`
- **Компоненты (опционально):** `api/v1/narrative/components/raid-specter-surge-schemas.yaml`
- **API версия:** v1 (semantic версию указать 1.0.0)
- **Тип:** OpenAPI 3.0.3

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            ├── raids/
            │   └── specter-surge.yaml
            └── components/
                └── raid-specter-surge-schemas.yaml   # если длина >380 строк
```
> Ограничить основной файл ≤380 строк, выносить громоздкие схемы (фазы, проверки, награды, telemetry) в компоненты.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервисная архитектура)

- **Микросервис:** narrative-service
- **Порт:** 8087
- **API Base Path:** `/api/v1/narrative/raids/*`
- **Домен:** Нарративные рейды и сюжетные операции
- **Зависимости:**
  - world-service (sync state, city unrest, events, world modifiers)
  - combat-service (mech encounters, boss states)
  - social-service (reputation, flags, resonance)
  - economy-service (rewards, contracts, consumables)
  - analytics-service (telemetry dashboards)

### Frontend (модульная архитектура)

- **Модуль:** `modules/guild/raids/specter-surge`
- **State Store:** `useNarrativeStore (raidState)` + координаторы `useGuildStore (raidOperations)`
- **Состояние:** raidPhases, partyRoles, syncTimers, worldModifiers, rewards, telemetry
- **UI компоненты (@shared/ui):** RaidPhaseTimeline, SyncStatusGauge, MechControlDashboard, ReputationImpactPanel, RewardLootTable
- **Формы (@shared/forms):** RaidProgressForm, PhaseOutcomeForm, RewardDistributionForm
- **Layouts (@shared/layouts):** GuildRaidLayout
- **Hooks (@shared/hooks):** useRealtime, useTelemetry, usePartySync, useCountdown

**Комментарий:** В начало YAML добавить блок с архитектурой (микросервис, модуль, UI компоненты, state store, base path).

### OpenAPI требования

- `info.x-microservice` указать (`name: narrative-service`, `port: 8087`, `domain: narrative`, `base-path: /api/v1/narrative/raids`, `package: com.necpgame.narrativeservice`).
- `servers`: только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`, `shared/common/sorting.yaml`.
- `security`: `BearerAuth` (роли `raid-view`, `raid-manage`, `raid-analytics`).
- Добавить `x-events` (Kafka): `narrative.raid.specterSync`, `narrative.raid.phaseCompleted`, `narrative.raid.failure`.

---

## 📡 Endpoints

### Основные операции

- **GET `/narrative/raids/specter-surge`** — базовая структура рейда (метаданные, фазы, требования, награды). Возвращает `RaidOverview`.
- **GET `/narrative/raids/specter-surge/phases`** — список фаз с состоянием таймеров, условиями успеха/провала, требуемыми проверками. Возвращает `RaidPhaseList`.
- **POST `/narrative/raids/specter-surge/phases/{phaseId}/progress`** — регистрация результатов D&D проверок и событий (успех/провал, крит). Принимает `PhaseProgressRequest`, возвращает `PhaseProgressResult`.
- **POST `/narrative/raids/specter-surge/sync`** — обновление Specter Sync Loop (каждые 30 сек). Интеграция с world/combat, возвращает `SyncStatus`.
- **POST `/narrative/raids/specter-surge/encounters`** — запуск боевой встречи через combat-service (boss states, mech actions). Возвращает `EncounterTicket`.
- **POST `/narrative/raids/specter-surge/world-effects`** — применение мировых эффектов (`underlink.stability`, `city.unrest.level`). Возвращает `WorldEffectResult`.
- **POST `/narrative/raids/specter-surge/reputation`** — пакетное обновление репутации Ghosts/Helios/Maelstrom. Возвращает `ReputationChangeResult`.
- **POST `/narrative/raids/specter-surge/rewards`** — выдача наград (предметы, контракты). Возвращает `RewardDistribution`.
- **POST `/narrative/raids/specter-surge/flags`** — установка флагов (`flag.specter.raid_cleared`, `flag.neon.blacklist`).
- **GET `/narrative/raids/specter-surge/telemetry`** — метрики (успех фаз, время, эвакуация). Использует пагинацию/фильтры.
- **GET `/narrative/raids/specter-surge/history`** — история попыток рейда (partyId, outcomes, timestamps).

### Дополнительно

- **POST `/narrative/raids/specter-surge/party-sync`** — синхронизация данных партии (ролей, readiness).
- **POST `/narrative/raids/specter-surge/alerts/reset`** — ручное сброс/очистка тревог (админские операции, роль `raid-manage`).

Каждая операция должна описывать:
- Параметры (`partyId`, `phaseId`, `attempt`, `timestamp`, `role`, `modifierSources`).
- Ответы `200/202` и ошибки `400`, `401`, `403`, `404`, `409`, `422`, `429`, `500`.
- `x-integrations` с указанием целевого микросервиса, метода, SLA (см. таблицу в `.BRAIN`).

---

## 🧩 Модели данных

- **RaidOverview** — метаданные, требования (level, prerequisites, flags), фазовый список, награды, SLA.
- **RaidPhase** — `phaseId`, `name`, `description`, `successEffects`, `failureEffects`, `requiredChecks[]`, `timer`, `dependencies`.
- **PhaseCheck** — `skill`, `dc`, `modifiers`, `successEffects`, `failureEffects`, `criticalEffects`.
- **PhaseProgressRequest** — `partyId`, `phaseId`, `checkResults[]`, `eventTriggers`, `timestamp`, `attempt`.
- **PhaseProgressResult** — `status`, `nextPhase`, `worldEffects`, `reputationDelta`, `rewards`, `syncState`.
- **SyncStatus** — `syncLevel`, `lagPenalty`, `buffs`, `debuffs`, `nextCheckIn`.
- **EncounterTicket** — `encounterId`, `boss`, `participants`, `mechStates`, `expiresAt`.
- **WorldEffectRequest/Result** — modifier changes (`city.unrest`, `underlink.stability`, events triggered).
- **ReputationChangeRequest/Result** — массив изменений по фракциям.
- **RewardDistribution** — лут, предметы, кредиты, контракты, шансы легендарок.
- **RaidFlagUpdate** — список флагов с состояниями.
- **TelemetryMetrics** — `averageDuration`, `phaseSuccessRate`, `syncLatency`, `evacuationRate`, `alerts`.
- **RaidHistoryEntry** — `attemptId`, `partyId`, `phaseOutcomes[]`, `rewards`, `duration`, `result`.

Все схемы должны содержать `required`, `description`, `example`, `enum` и расширения:
- `x-frontend` (компоненты UI)
- `x-storage` (PostgreSQL/Redis/Kafka topics)
- `x-monitoring` (Grafana dashboards, PagerDuty alerts)
- `x-governance` (обязательный ревью/держатели SLA)

---

## ✅ Детальный план

### Шаг 1: Анализ документа
- Извлечь фазы, проверки, эффекты и зависимости.
- Определить минимальный набор данных для orchestration (party, roles, timers, buffs).
- Составить карту интеграций (world/combat/social/economy).

**Ожидаемый результат:** таблица сущностей и связей, список endpoints.

### Шаг 2: Архитектура и структура файлов
- Подготовить каркас `specter-surge.yaml` с `info`, `servers`, `security`, `tags`.
- Ререфакторить крупные схемы в `components/raid-specter-surge-schemas.yaml`.
- Добавить комментарий об архитектуре в заголовок файла.

**Результат:** базовые секции с корректными `$ref`.

### Шаг 3: Проработка `paths`
- Для каждого endpoint описать параметры, requestBody, responses, примеры.
- Подключить `shared/common` компоненты (pagination, sorting, responses).
- Добавить `operationId`, `tags`, `x-integrations`, `x-events`.

**Результат:** завершённый раздел `paths`.

### Шаг 4: Модели данных
- Реализовать схемы для фаз, проверок, наград, telemetry, истории.
- Включить `x-frontend`, `x-monitoring`, `x-storage`.
- Продумать enum статусов (`phaseStatus`, `encounterState`, `alertSeverity`).

**Результат:** `components/schemas` готов, реиспользуемый и валидный.

### Шаг 5: Безопасность, SLA, события
- Описать `securitySchemes` и требуемые роли.
- Задокументировать SLA, latency targets, PagerDuty alerts в `x-monitoring`.
- Добавить Kafka события и их payload (минимально через `x-events`).

**Результат:** спецификация отражает операционные требования.

### Шаг 6: Примеры, FAQ, валидация
- Добавить примеры для ключевых операций (`PhaseProgressRequest`, `SyncStatus`).
- Подготовить FAQ (например, повторные попытки, fallback при провале).
- Запустить `scripts/validate-swagger.ps1`.
- Проверить чеклист `tasks/config/checklist.md`.

**Результат:** валидная спецификация, полностью описанная для исполнителя.

---

## 📏 Критерии приёмки (12)

1. `api/v1/narrative/raids/specter-surge.yaml` создан и проходит `scripts/validate-swagger.ps1`.
2. `info.x-microservice` заполнен для `narrative-service` (порт 8087, base-path `/api/v1/narrative/raids`).
3. `GET /narrative/raids/specter-surge` возвращает `RaidOverview` с флагами, требованиями, фазами, наградами.
4. `POST /narrative/raids/specter-surge/phases/{phaseId}/progress` принимает `PhaseProgressRequest` и обрабатывает успех/провал, включая critical cases.
5. `POST /narrative/raids/specter-surge/sync` документирует Specter Sync Loop с SLA и latency метриками.
6. `POST /narrative/raids/specter-surge/world-effects` и `/reputation` описывают связь с world-service и social-service, используют стандартные ответы.
7. `POST /narrative/raids/specter-surge/rewards` описывает распределение предметов, контрактов, валюты.
8. Все endpoints включают ошибки 400/401/403/404/409/422/429/500 и примеры ответов.
9. Крупные схемы вынесены в файл компонентов или основной файл ≤380 строк.
10. Схемы содержат `x-frontend`, `x-storage`, `x-monitoring`, `x-governance` расширения.
11. Добавлены Kafka события (`narrative.raid.specterSync`, `...phaseCompleted`, `...failure`) с описанием payload.
12. FAQ описывает кооперативные попытки, восстановление после провала, работу с мех-пилот связью.

---

## ❓ FAQ

**В: Кто отвечает за боевую механику — narrative или combat?**  
О: Narrative-service orchestration вызывает combat-service через `encounters` endpoint; боевые события описаны в `x-integrations`. Combat-service не хранит нарративную структуру.

**В: Как обрабатываются повторные попытки рейда?**  
О: Использовать поле `attempt` в `PhaseProgressRequest`. Сервер открывает новый `attemptId` при рестарте. При попытке перезаписать активную фазу возвращать `409 Conflict`.

**В: Что происходит при рассинхронизации Specter Sync?**  
О: Возвращается `SyncStatus` с `lagPenalty > 0`, UI показывает предупреждение. Можно повторить `sync` запрос для восстановления. Если таймер превышен, активируется fallback события Helios.

**В: Нужно ли документировать WebSocket?**  
О: WebSocket описаны в `.BRAIN` как события. В OpenAPI укажи их в `x-events` + ссылку на AsyncAPI backlog. Реализация realtime — задача для другой команды.

**В: Как логируются награды и world изменения?**  
О: Через `x-audit` расширения указать, что все операции записываются в `analytics-service`, с полями `performedBy`, `partyId`, `timestamp`, `source`.

---

**Примечание:** После публикации спецификации синхронизируй `brain-mapping.yaml` и обнови прогресс в `CURRENT-WORK/current-status.md`.

