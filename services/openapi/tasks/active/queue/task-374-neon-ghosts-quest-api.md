# Task ID: API-TASK-374
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 20:15
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-142, API-TASK-241, API-TASK-243

---

## 📋 Краткое описание

Сформировать OpenAPI-спецификацию `Neon Ghosts Side Quest` для управления стадиями, проверками, наградами и мировыми модификаторами квеста на основе `.BRAIN/04-narrative/quests/side/2025-11-07-quest-neon-ghosts.md`.

**Что нужно сделать:** Создать `api/v1/narrative/quests/side/neon-ghosts.yaml`, описывающий REST API для получения структуры квеста, прогресса, веток, применения репутации, world-state и economy эффектов с учетом кооперативного прохождения.

---

## 🎯 Цель задания

Предоставить narrative-service централизованный контракт, через который игровые клиенты и инструменты смогут управлять квестом «Neon Ghosts», фиксировать решения и синхронизировать последствия с социальными, мировыми и экономическими подсистемами.

**Зачем это нужно:**
- Позволить фронтенду и игровому серверу последовательно вести прогресс узлов, проверок и веток.
- Автоматизировать применение репутации, world modifiers, контрактов и событий без ручной интеграции.
- Отразить кооперативные сценарии (2-4 игрока) и требования к координации.
- Обеспечить единый источник правды для telemetry и аналитики квеста.

---

## 📚 Источники информации

### Основной документ

- **Репозиторий:** `.BRAIN`
- **Путь:** `.BRAIN/04-narrative/quests/side/2025-11-07-quest-neon-ghosts.md`
- **Версия:** 1.0.0
- **Дата обновления:** 2025-11-07
- **Статус:** approved, `api-readiness: ready`

**Что важно:**
- Структура узлов (`intel-briefing`, `underlink-entry`, `ghost-confrontation`, `resolution`) с проверками и исходами.
- Механики репутации, world-state, economy, social resonance.
- API контуры: `/quests/side-neon-ghosts`, `/world/modifiers`, `/social/reputation/batch`, `/economy/contracts/activate`.
- Таблицы наград и веток (ally, corporate, maelstrom).

### Дополнительные источники

- `.BRAIN/04-narrative/quests/raid/2025-11-07-raid-specter-surge.md` — связанные последствия по Underlink.
- `.BRAIN/02-gameplay/world/dungeons/dungeon-scenarios-catalog.md` — Underlink зональные механики.
- `.BRAIN/02-gameplay/social/relationships-system-детально.md` — влияние на социальную репутацию.
- `API-SWAGGER/api/v1/world/world-interaction-suite.yaml` (API-TASK-241) — world modifiers.
- `API-SWAGGER/api/v1/social/resonance.yaml` (API-TASK-243) — resonance интеграция.

### Связанные задания

- `task-113-combat-session-backend-api.md` — combat session lifecycle для шутерных миссий.
- `task-241-world-interaction-suite-api.md` — мировые модификаторы.
- `task-243-social-resonance-api.md` — социальные показатели.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

- **Целевой файл:** `api/v1/narrative/quests/side/neon-ghosts.yaml`
- **Компоненты:** `api/v1/narrative/components/side-quest-schemas.yaml` (если нужно переиспользовать узлы/ветки)
- **API версия:** v1 (semantic 1.0.0)
- **Тип:** OpenAPI 3.0.3

**Структура директорий:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            ├── quests/
            │   └── side/
            │       └── neon-ghosts.yaml
            └── components/
                └── side-quest-schemas.yaml   (при необходимости вынести общие схемы)
```
> При увеличении файла >380 строк вынести схемы `QuestNode`, `QuestBranch`, `ReputationEffect` и т.п. в компонентный файл.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)

- **Микросервис:** narrative-service
- **Порт:** 8087
- **API Base Path:** `/api/v1/narrative/quests/side/*`
- **Домен:** Нарративные квесты, структура сюжетов
- **Зависимости:**
  - gameplay-service (execution engine, skill checks, combat)
  - social-service (репутация и resonance)
  - economy-service (контракты, награды)
  - world-service (world modifiers)
  - analytics-service (telemetry событий)

### Frontend (модульная архитектура)

- **Модуль:** `modules/quests/neon-ghosts`
- **State Store:** `useNarrativeStore (questState)`
- **Состояние:** questNodes, partyProgress, availableBranches, worldImpacts, rewards
- **UI компоненты (@shared/ui):** QuestStageTimeline, DecisionMatrix, RewardPreviewCard, ReputationImpactPanel
- **Формы (@shared/forms):** QuestProgressForm, BranchResolutionForm
- **Layouts (@shared/layouts):** QuestDetailLayout
- **Hooks (@shared/hooks):** useRealtime, useQuestState, usePartySync

**Комментарий:** В начале YAML указать архитектурный блок (микросервис, модуль, компоненты, state).

### OpenAPI / Security

- `info.x-microservice`: `name: narrative-service`, `port: 8087`, `domain: narrative`, `base-path: /api/v1/narrative/quests/side`, `package: com.necpgame.narrativeservice`.
- `servers`: только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.
- Роли: `quest-view`, `quest-manage`, `quest-analytics`.

---

## 📡 Основные endpoints

- **GET `/narrative/quests/side/neon-ghosts`** — структура квеста (этапы, узлы, проверки, ветки), возвращает `QuestStructure`.
- **POST `/narrative/quests/side/neon-ghosts/progress`** — запись прогресса узла, skill-check результатов, party context, возвращает `QuestProgressResult`.
- **POST `/narrative/quests/side/neon-ghosts/branch`** — фиксация выбранной ветки (ally, corporate, maelstrom), возвращает `BranchResolution`.
- **POST `/narrative/quests/side/neon-ghosts/world-effects`** — применение world modifiers и событий, вызывает downstream world-service через `x-integrations`.
- **POST `/narrative/quests/side/neon-ghosts/reputation`** — batch-обновление репутации (ally/corporate/maelstrom), возвращает `ReputationBatchResult`.
- **POST `/narrative/quests/side/neon-ghosts/rewards`** — выдача наград (xp, предметы, контракты), синхронизация с economy-service.
- **GET `/narrative/quests/side/neon-ghosts/telemetry`** — агрегированная статистика (branch distribution, completion rate).
- **GET `/narrative/quests/side/neon-ghosts/history`** — история решений партии с пагинацией.
- **POST `/narrative/quests/side/neon-ghosts/state/sync`** — синхронизация для кооперативной группы (party session id).

Каждый endpoint должен описывать:
- Параметры (`partyId`, `playerIds`, `difficulty`, `timestamp`).
- Тела запросов (`QuestProgressRequest`, `BranchSelectionRequest`, `WorldEffectRequest`).
- Ответы (`200 OK` + стандартные ошибки 400, 401, 403, 404, 409, 422, 500).
- `x-integrations` указывать соответствующий сервис (social, world, economy).

---

## 🧩 Модели данных

- **QuestStructure** — метаданные квеста (questId, title, synopsis, stages[], nodes[], branches[], requirements).
- **QuestStage** — порядок, описание, локация, связанные сервисы.
- **QuestNode** — nodeId, label, entryCondition, options[], outcomes, checks (stat, dc, resource).
- **QuestOption / QuestOutcome** — требования (resource, stat, flag), последствия (unlock_node, spawn_encounter, reputationChange, worldModifier).
- **QuestBranch** — ally/corporate/maelstrom, описание, эффекты (world modifiers, contracts, events).
- **QuestProgressRequest** — partyId, nodeId, optionId, playerResults[], skillChecks[], timestamp.
- **QuestProgressResult** — nextNodes, flags, reputationDelta, worldEffects, telemetryIds.
- **BranchSelectionRequest / BranchResolution** — выбор ветки, justification, resulting rewards.
- **WorldEffectRequest / WorldEffectResult** — modifiers[], events[], expiration.
- **ReputationBatchRequest / Result** — массив изменений (`target`, `value`, `reason`, `expiresAt`).
- **RewardPackage** — xp, eddies, items[], contracts[], buffs[].
- **QuestTelemetry** — completion rate, branch breakdown, average time, failure points.
- **QuestHistoryEntry** — partyId, nodeId, decision, outcomes, timestamp, playerIds.

Добавить `x-frontend`, `x-analytics`, `x-storage` (PostgreSQL schema, Kafka topics `narrative.quest.events`) для ключевых схем.

---

## ✅ Детальный план

### Шаг 1: Анализ квеста и определение границ
- Расписать этапы, узлы, ветки из `.BRAIN`.
- Классифицировать взаимодействия по сервисам (narrative orchestrates, social/economy/world downstream).
- Решить, какие данные остаются в narrative-service, что отдаём в downstream API (через `x-integrations`).

**Результат:** диаграмма зависимостей и список необходимых схем.

### Шаг 2: Проектирование модели данных
- Описать `QuestStructure`, `QuestNode`, `QuestBranch`, `QuestOption`, `QuestOutcome`.
- Добавить поддержку кооперативных партий (`partyId`, `playerIds`, `role`, `vote`).
- Подготовить общие компоненты (`OutcomeEffect`, `Requirement`, `WorldModifier`, `ReputationChange`).

**Результат:** готовый раздел `components/schemas` (или вынесенный файл).

### Шаг 3: Разметка endpoints
- Для каждого endpoint определить методы, пути, параметры, тела запросов, ответы.
- Подключить `shared/common/pagination.yaml` там, где возвращаются списки (history, telemetry).
- Добавить `operationId`, `tags`, `summary`, `description`, примеры payload'ов.

**Результат:** раздел `paths` с полными спецификациями.

### Шаг 4: Интеграции и расширения
- Для world modifiers, reputation и economy добавить `x-integrations` с target service, endpoint, метод, SLA.
- Добавить Kafka события (`narrative.quest.neonGhosts.completed`, `narrative.quest.neonGhosts.branchSelected`).
- Прописать `x-audit` (кто и когда изменил состояние), `x-telemetry` (metricIds).

**Результат:** спецификация отражает интеграции и telemetry.

### Шаг 5: Безопасность и кооперативные сценарии
- Определить роли (`quest-view`, `quest-manage`), scopes (`quests:read`, `quests:write`, `quests:resolve`).
- Документировать проверку `partyToken`, rate limits, конфликтные ситуации (409 при параллельных решениях).
- Добавить пример кооперативного прогресса (2 игрока, раздельные skill checks).

**Результат:** security и concurrency описаны, предоставлены примеры.

### Шаг 6: Валидация и чеклист
- Прогнать `scripts/validate-swagger.ps1`.
- Проверить `tasks/config/checklist.md` (все блоки, критерии, FAQ).
- Убедиться, что file size в норме (вносить схемы в components при необходимости).

**Результат:** валидная спецификация, готовая к ревью.

---

## 📏 Критерии приёмки (12)

1. `api/v1/narrative/quests/side/neon-ghosts.yaml` создан и проходит `scripts/validate-swagger.ps1` без ошибок.
2. В `info.x-microservice` указан `narrative-service`, порт 8087, базовый путь `/api/v1/narrative/quests/side`.
3. `GET /narrative/quests/side/neon-ghosts` возвращает `QuestStructure` с этапами, узлами, ветками и проверками.
4. `POST /narrative/quests/side/neon-ghosts/progress` принимает `QuestProgressRequest` (partyId, nodeId, optionId, skillChecks[]) и отдаёт `QuestProgressResult`.
5. `POST /narrative/quests/side/neon-ghosts/branch` валидирует ветку (ally/corporate/maelstrom) и фиксирует world/economy/social эффекты.
6. Описаны endpoints для world modifiers, reputation и rewards, каждая операция содержит `x-integrations` с целевым сервисом и SLA.
7. Предусмотрены коды ошибок 400, 401, 403, 404, 409, 422, 500 для всех записывающих операций.
8. Схемы `QuestNode`, `QuestOption`, `QuestOutcome`, `QuestBranch`, `RewardPackage`, `QuestHistoryEntry` структурированы, имеют `required`, `description`, `example`.
9. В спецификации отражены кооперативные сценарии (параметры partyId, playerIds, голосование) и примеры payload'ов.
10. Добавлены Kafka события (`narrative.quest.neonGhosts.*`) через `x-events`, а также telemetry метрики.
11. Файл ≤380 строк или крупные схемы вынесены в `api/v1/narrative/components/side-quest-schemas.yaml`.
12. Включены расширения `x-frontend`, `x-analytics`, `x-audit`, `x-governance` с требуемыми полями (например, обязательный контент-ревью для ветки Maelstrom).

---

## ❓ FAQ

**В: Почему narrative-service, а не gameplay-service?**  
О: narrative-service выступает оркестратором сюжетов, координирует связи между сервисами и хранит структуру квеста. Gameplay-service обрабатывает runtime механики через интеграции.

**В: Как синхронизировать кооперативные решения?**  
О: Использовать `QuestProgressRequest.partyId` и `playerResults[]`. При конфликте возвращать 409 с деталями последнего принятого решения.

**В: Нужно ли хранить историю решений целиком?**  
О: Да, `QuestHistoryEntry` возвращает список событий, используется аналитикой и world-service. Пагинация обязательно.

**В: Что делать с повторными прохождениями?**  
О: Добавить `runId` или `attempt` в `QuestProgressRequest` (опционально) и описать в FAQ, что повторная попытка требует сброса через админский endpoint (будущая задача).

**В: Как учитывать глобальные события при выборе веток?**  
О: В `x-integrations.world` указать требование проверки `city.chaos.level`, `underlink.delivery.boost`. Если блокеры активны, возвращать 409 с рекомендацией.

---

**Примечание:** После реализации обновить `brain-mapping.yaml` и внести запись в `current-status.md` для отслеживания выполнения партии.

