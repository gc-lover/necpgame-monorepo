# Task ID: API-TASK-376
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 20:55
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-374, API-TASK-375, API-TASK-373

---

## 📋 Краткое описание

Нужно описать цепочку «Helios Countermesh Conspiracy» в виде OpenAPI контракта для narrative-service, включая все фазы, ветки, проверки, награды и интеграции с world/social/economy сервисами.

**Что нужно сделать:** Создать `api/v1/narrative/quests/helios-countermesh-conspiracy.yaml` (и при необходимости вынести модели в `api/v1/narrative/components/helios-countermesh-schemas.yaml`), чтобы оркестровать квест после рейда Specter Surge.

---

## 🎯 Цель задания

Обеспечить narrative-service API, который позволит серверу и фронтенду управлять сложной ветвящейся цепочкой выбора между Helios и Specter, сохраняя синхронизацию с мировыми событиями и социальными последствиями.

**Зачем это нужно:**
- Вести сюжетную линию продолжения рейда (`Specter Surge`) с выбором фракций и двойных агентов.
- Координировать world-state (City Unrest), социальные репутации и экономические награды.
- Поддержать telemetry и аналитические KPI для проверки баланса цепочки.
- Предоставить UI квестов, world overlays и социальным каналам полноценный источник данных.

---

## 📚 Источники информации

### Основной документ

- **Репозиторий:** `.BRAIN`
- **Путь:** `.BRAIN/04-narrative/quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`
- **Версия:** 1.3.0
- **Дата обновления:** 2025-11-07 22:00
- **Статус:** approved, `api-readiness: ready`

**Что важно:**
- Фазы (Signal Echo → Conspiracy Finale) с проверками, условиями, ветками.
- Псевдо-YAML узлов с детальными skill checks, флагами, репутацией.
- D&D таблицы, награды, world/social/economy последствия.
- API карта (narrative, world, economy, social) и telemetry.

### Дополнительные источники

- `.BRAIN/04-narrative/quests/raid/2025-11-07-raid-specter-surge.md` — prerequisite рейд.
- `.BRAIN/02-gameplay/world/helios-countermesh-ops.md` — боевые операции Helios.
- `.BRAIN/02-gameplay/world/specter-hq.md` — база Specter.
- `.BRAIN/02-gameplay/world/city-unrest-escalations.md` — мировые эскалации.
- `.BRAIN/04-narrative/npc-lore/important/npc-kaede-ishikawa.md` — NPC двойной агент.
- `API-SWAGGER/api/v1/narrative/raids/specter-surge.yaml` (будет создан задачей API-TASK-375).
- `API-SWAGGER/api/v1/social/romance/events.yaml` (API-TASK-373) — романтические модификаторы Kaori.

### Связанные задания

- `task-374-neon-ghosts-quest-api.md`
- `task-375-specter-surge-raid-api.md`
- `task-243-social-resonance-api.md`
- `task-241-world-interaction-suite-api.md`
- `task-373-romance-events-api.md`

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

- **Целевой файл:** `api/v1/narrative/quests/helios-countermesh-conspiracy.yaml`
- **Компоненты (опционально):** `api/v1/narrative/components/helios-countermesh-schemas.yaml`
- **API версия:** v1 (semantic 1.1.0 — расширенная цепочка после рейда)
- **Тип:** OpenAPI 3.0.3

**Структура директорий:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            ├── quests/
            │   └── helios-countermesh-conspiracy.yaml
            └── components/
                └── helios-countermesh-schemas.yaml   # вынести крупные модели (nodes, branches, rewards, telemetry)
```
> Длина основного файла ≤380 строк. Вынести крупные схемы (`QuestNode`, `BranchOption`, `WorldEffect`, `TelemetryEntry`) в компонентный файл.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)

- **Микросервис:** narrative-service
- **Порт:** 8087
- **API Base Path:** `/api/v1/narrative/quests/*`
- **Домен:** Нарративные квесты и сюжетные ветки
- **Зависимости:**
  - world-service (City Unrest, world events)
  - combat-service (PvE/PvPvE encounters)
  - economy-service (контракты, награды)
  - social-service (репутация, feeds, resonance)
  - analytics-service (telemetry, dashboards)

### Frontend (модульная архитектура)

- **Модули:** `modules/narrative/raids/helios-countermesh`, `modules/world/state`, `modules/social/feeds`
- **State Store:** `useNarrativeStore (heliosConspiracyState)` + `useWorldStore (cityUnrest)` + `useSocialStore (factionReputation)`
- **Состояние:** questNodes, branches, flags, cityUnrest, reputation, rewards, telemetry
- **UI компоненты (@shared/ui):** ConspiracyTimeline, BranchDecisionMatrix, ReputationImpactPanel, UnrestMeter, RewardPreviewCard
- **Формы (@shared/forms):** QuestDecisionForm, WorldModifierApplyForm, RewardClaimForm
- **Layouts (@shared/layouts):** NarrativeQuestLayout
- **Hooks (@shared/hooks):** useRealtime, useQuestState, useWorldUnrest, useBranchPlanner

**Комментарий:** В начале OpenAPI файла указать блок с архитектурой (микросервис, фронтенд модуль, UI компоненты, state stores).

### OpenAPI требования

- Заполнить `info.x-microservice` (`name: narrative-service`, `port: 8087`, `domain: narrative`, `base-path: /api/v1/narrative/quests`, `package: com.necpgame.narrativeservice`).
- `servers`: только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`, `shared/common/sorting.yaml`.
- `security`: `BearerAuth` с ролями `quest-view`, `quest-manage`, `quest-analytics`.
- `x-events`: `narrative.helios.conspiracy.progress`, `narrative.helios.conspiracy.outcome`, `world.city.unrest.update`.

---

## 📡 Endpoints

- **GET `/narrative/quests/helios-countermesh-conspiracy`** — метаданные, prerequisite flags, фазы, ветки, награды (`QuestOverview`).
- **GET `/narrative/quests/helios-countermesh-conspiracy/nodes`** — список узлов с условиями, проверками, действиями (`QuestNodeList`).
- **GET `/narrative/quests/helios-countermesh-conspiracy/nodes/{nodeId}`** — детальная информация узла, ветвление, сценарии (`QuestNodeDetail`).
- **POST `/narrative/quests/helios-countermesh-conspiracy/progress`** — регистрация прохождения узла, результатов проверок, флагов (`QuestProgressRequest` → `QuestProgressResult`).
- **POST `/narrative/quests/helios-countermesh-conspiracy/branch`** — фиксация ветки (support Helios / expose / double agent) с последствиями (`BranchDecisionRequest` → `BranchDecisionResult`).
- **POST `/narrative/quests/helios-countermesh-conspiracy/world-effects`** — применение world-state изменений (City Unrest, events) (`WorldEffectRequest/Result`).
- **POST `/narrative/quests/helios-countermesh-conspiracy/reputation`** — batch-обновление репутаций фракций (`ReputationBatchRequest/Result`).
- **POST `/narrative/quests/helios-countermesh-conspiracy/rewards`** — выдача наград, контрактов, unlock-активностей (`RewardDistributionRequest/Result`).
- **POST `/narrative/quests/helios-countermesh-conspiracy/feeds`** — публикация social feed событий (`FeedBroadcastRequest/Result`).
- **GET `/narrative/quests/helios-countermesh-conspiracy/history`** — история решений игроков, используется analytics (с пагинацией).
- **GET `/narrative/quests/helios-countermesh-conspiracy/telemetry`** — метрики KPI (phase completion rate, unrest delta, engagement).
- **POST `/narrative/quests/helios-countermesh-conspiracy/reset`** — административный сброс для повторных попыток (роль `quest-manage`).

Каждый endpoint должен:
- Описывать параметры (`partyId`, `playerId`, `attempt`, `flag`, `cityUnrest`, `reputation`).
- Возвращать `2xx` + стандартные ошибки `400/401/403/404/409/422/500`.
- Включать `x-integrations` (куда и какой запрос уходит в world/social/economy).
- Приводить примеры payload’ов (успех и ошибка).

---

## 🧩 Модели данных

- **QuestOverview** — метаданные (questId, version, prerequisites, phases, branches, rewards, telemetryTargets).
- **QuestPhase** — phaseId, описание, условия, связанные узлы, проверочные навыки.
- **QuestNode** — nodeId, type, location, npc, dialogues, checks, success/failure effects, triggers.
- **CheckDefinition** — skill, dc, modifiers, success, failure, criticalSuccess, criticalFailure.
- **BranchOption** — optionId, requirements, effects (flags, cityUnrest, reputation, events, unlock activities).
- **QuestProgressRequest** — questId, nodeId, optionId, checkResults[], partyId, timestamp, attempt.
- **QuestProgressResult** — updatedFlags, nextNodes, cityUnrestDelta, reputationDelta, events, rewards.
- **BranchDecisionRequest/Result** — выбранная ветка, justification, resulting world/social/economy impacts.
- **WorldEffectRequest/Result** — массив модификаторов (`city.unrest`, `underlink.stability`, events).
- **ReputationBatchRequest/Result** — изменения репутации (`rep.corp.helios`, `rep.specter`, `rep.city.gov`).
- **RewardDistribution** — кредиты, предметы, контракты, unlock активности, telemetryId.
- **FeedBroadcastRequest/Result** — social события, мемы, broadcast targets.
- **TelemetryMetrics** — `phaseCompletion`, `branchDistribution`, `unrestDelta`, `engagementTime`, `doubleAgentRate`.
- **QuestHistoryEntry** — попытка, ветка, решения, испытания, награды, длительность.
- **AuditEntry / Governance** — кто инициировал изменения, ссылка на ревью.

Добавить расширения:
- `x-frontend` (компоненты UI/модули)
- `x-storage` (PostgreSQL schema, Kafka topic)
- `x-monitoring` (Grafana панели, PagerDuty алерты)
- `x-privacy` (персональные данные, retention)
- `x-governance` (обязательные ревью, compliance)

---

## ✅ Детальный план

### Шаг 1: Анализ структуры
- Уточнить фазы, условия, флаги из `.BRAIN`.
- Составить карту зависимостей с рейдом Specter Surge и world documents.
- Определить минимальный набор данных для orchestrator (flags, cityUnrest, reputation).

**Результат:** матрица фаз/узлов/веток/эффектов.

### Шаг 2: Архитектура OpenAPI
- Создать каркас файла с `info`, `servers`, `security`, `tags`.
- Добавить архитектурный комментарий (микросервис, модуль, UI компоненты).
- Решить, какие схемы вынести в компоненты.

**Результат:** шаблон с подключенными `shared` компонентами.

### Шаг 3: Реализация endpoints
- Для каждого endpoint описать: метод, путь, параметры, requestBody, responses, примеры.
- Использовать `$ref` на схемы.
- Задокументировать `x-integrations` для world/social/economy и events.

**Результат:** раздел `paths` покрывает весь сценарий квеста.

### Шаг 4: Модели данных и расширения
- Реализовать схемы для узлов, проверок, веток, эффектов, telemetry, history.
- Добавить `x-frontend`, `x-storage`, `x-monitoring`, `x-governance`.
- Обеспечить `enum`, `required`, `description`, `example`.

**Результат:** `components/schemas` полноценно описан и переиспользован.

### Шаг 5: Безопасность и governance
- Настроить `security` (BearerAuth, roles/scopes).
- Добавить FAQ по повторным попыткам, double-agent ветке, пасхалкам.
- Задокументировать SLA, telemetry и compliance.

**Результат:** спецификация учитывает безопасность, мониторинг, управление.

### Шаг 6: Проверка качества
- Прогнать `scripts/validate-swagger.ps1 api/v1/narrative/quests/helios-countermesh-conspiracy.yaml`.
- Пройти чеклист `tasks/config/checklist.md`.
- Убедиться, что файл ≤380 строк (при необходимости вынести компоненты).
- Подготовить примеры success/error payload’ов.

**Результат:** валидный контракт, готовый к реализации.

---

## 📏 Критерии приёмки (12)

1. Создан `api/v1/narrative/quests/helios-countermesh-conspiracy.yaml`, проходит `scripts/validate-swagger.ps1`.
2. Заполнен `info.x-microservice` для `narrative-service` (порт 8087, base-path `/api/v1/narrative/quests`).
3. `GET /narrative/quests/helios-countermesh-conspiracy` возвращает `QuestOverview` с фазами, ветками, наградами и prerequisite флагами.
4. `POST /narrative/quests/helios-countermesh-conspiracy/progress` принимает `QuestProgressRequest` и обрабатывает успех/провал/critical сценарии.
5. `POST /narrative/quests/helios-countermesh-conspiracy/branch` документирует три ветки (Helios, Specter, Double Agent) и их последствия.
6. `POST /narrative/quests/helios-countermesh-conspiracy/world-effects` описывает City Unrest, events, integrates с world-service.
7. `POST /narrative/quests/helios-countermesh-conspiracy/reputation` и `/rewards` используют стандартные ответы и описывают economy/social эффекты.
8. Все endpoints включают ошибки 400/401/403/404/409/422/500 и примеры ответов.
9. Схемы вынесены в компоненты или основной файл ≤380 строк; все модели имеют `required`, `description`, `example`.
10. Добавлены расширения `x-frontend`, `x-storage`, `x-monitoring`, `x-governance`, `x-privacy`.
11. Включены Kafka события (`narrative.helios.conspiracy.*`) и telemetry метрики (`helios_conspiracy_choice`, `city_unrest_delta`).
12. FAQ и примеры описывают повторные попытки, double-agent сценарии, пасхалки и broadcast в social feeds.

---

## ❓ FAQ

**В: Как квест зависит от рейда Specter Surge?**  
О: Через prerequisite флаги (`flag.specter.raid_cleared`) и world state (`underlink.stability`, `city.unrest`). Укажи это в `QuestOverview.prerequisites`.

**В: Как обрабатывать двойных агентов?**  
О: `BranchDecisionRequest` должен поддерживать ветку `double-agent`, возвращать эффекты на обе фракции и повышать City Unrest. Документируй поля `doubleAgentRisk`, `proxyWarTriggered`.

**В: Нужно ли описывать кат-сцены?**  
О: Да, через `x-integrations.narrative` добавь ссылку на cutscene trigger (`trigger_cutscene`) и AsyncAPI backlog для сцен.

**В: Как учитываются social feeds?**  
О: Endpoint `/feeds` публикует мемы и новости; подключи `API-SWAGGER/shared/common/social-feed.yaml` (если существует) и опиши payload `FeedBroadcast`.

**В: Какая ретенция данных?**  
О: Добавь `x-privacy.retention` (например, 30 дней для telemetry, 90 для history) и `x-audit` для контроля доступа.

---

**Примечание:** После реализации спецификации обнови `brain-mapping.yaml` и `current-status.md`, отразив прогресс цепочки Countermesh.

