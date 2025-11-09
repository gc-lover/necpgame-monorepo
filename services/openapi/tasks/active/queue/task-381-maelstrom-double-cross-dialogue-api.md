# Task ID: API-TASK-381
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 22:25
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-379, API-TASK-243, API-TASK-375

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию для диалоговой цепочки побочного квеста «Maelstrom Double-Cross», описывающей многовариантные сцены, двойную игру и фракционные последствия на основе `.BRAIN/04-narrative/dialogues/quest-side-maelstrom-double-cross.md`.

**Что нужно сделать:** Создать `api/v1/narrative/dialogues/quests/maelstrom-double-cross.yaml` (при необходимости вынести схемы в `api/v1/narrative/components/dialogue-quests-schemas.yaml`) для narrative-service, обеспечив управление состояниями, проверками, наградами и телеметрией.

---

## 🎯 Цель задания

Предоставить narrative-service контракт, позволяющий игровым системам вести двойную игру между Maelstrom, Militech и NCPD, отслеживать флаги предательства, выдавать награды и контролировать world events и репутации.

**Зачем это нужно:**
- Синхронизировать квестовую цепочку с другими сюжетными ветками (Valentinos, Underlink raids).
- Управлять сложными состояниями (betrayal, double-agent, triple agent, exile).
- Поддерживать D&D проверки (Intimidation, Deception, Hacking, Insight, Technical).
- Собирать телеметрию по выбору игроков и балансировать экономику/репутацию.

---

## 📚 Источники информации

### Основной документ

- **Репозиторий:** `.BRAIN`
- **Путь:** `.BRAIN/04-narrative/dialogues/quest-side-maelstrom-double-cross.md`
- **Версия:** 1.1.0
- **Дата обновления:** 2025-11-07 19:46
- **Статус:** approved, `api-readiness: ready`

**Что важно:**
- Состояния: `briefing`, `meet-corp`, `betrayal`, `double-agent`, `fallout`, `media-flash`.
- YAML узлы, опции, проверки, последствия и награды.
- Таблица проверок с модификаторами, событиями и флагами.
- REST/GraphQL контуры, валидация, телеметрия.

### Дополнительные источники

- `.BRAIN/04-narrative/dialogues/npc-royce.md`
- `.BRAIN/04-narrative/dialogues/npc-james-iron-reed.md`
- `.BRAIN/04-narrative/quests/side/SQ-maelstrom-double-cross.md`
- `.BRAIN/02-gameplay/social/reputation-formulas.md`
- `.BRAIN/02-gameplay/world/events/world-events-framework.md`
- `.BRAIN/04-narrative/dialogues/DIALOGUE-TEMPLATE.md`
- `.BRAIN/04-narrative/quests/side/heywood-valentinos-chain.md` (пересечение по double-agent логике)

### Связанные задания

- API-TASK-379 (Dialogue Templates API)
- API-TASK-380 (Heywood Valentinos Chain API)
- API-TASK-243 (Social Resonance API)
- API-TASK-241 (World Interaction Suite API)

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

- **Целевой файл:** `api/v1/narrative/dialogues/quests/maelstrom-double-cross.yaml`
- **Компоненты (опционально):** `api/v1/narrative/components/dialogue-quests-schemas.yaml`
- **API версия:** v1 (semantic 1.0.0)
- **Тип:** OpenAPI 3.0.3

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            ├── dialogues/
            │   └── quests/
            │       ├── heywood-valentinos-chain.yaml
            │       └── maelstrom-double-cross.yaml
            └── components/
                └── dialogue-quests-schemas.yaml
```
> Поддерживать основной файл ≤380 строк; схемы (`DialogueQuest`, `DialogueState`, `CheckOutcome`, `RewardPackage`) при необходимости вынести в components.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервисная архитектура)

- **Микросервис:** narrative-service
- **Порт:** 8087
- **API Base Path:** `/api/v1/narrative/dialogues/quests/*`
- **Домен:** Нарративные квестовые диалоги
- **Зависимости:**
  - social-service (репутации Maelstrom/Militech/NCPD)
  - world-service (events: `maelstrom_underlink_raid`, `corporate_war_escalation`)
  - economy-service (награды, чёрный рынок имплантов)
  - analytics-service (телеметрия выбора)
  - law-service (NCPD audits)

### Frontend (модульная архитектура)

- **Модули:** `modules/narrative/quests/maelstrom-double-cross`, `modules/social/gangs`, `modules/world/events`
- **State Store:** `useNarrativeStore (questDialogueState)` + `useSocialStore (factionReputation)` + `useWorldStore (eventState)`
- **Состояние:** questState, flags (`flag.sqmdl.*`), reputation, pendingChecks, rewards, telemetry
- **UI компоненты (@shared/ui):** DialogueDecisionTree, ReputationImpactPanel, EventModifierBadge, OutcomeSummaryModal
- **Формы (@shared/forms):** DialogueActionForm, CheckExecutionForm, RewardSelectionForm
- **Layouts (@shared/layouts):** QuestDialogueLayout
- **Hooks (@shared/hooks):** useRealtime, useTelemetry, useAutosave, useQuestState

**Комментарий:** В начале спецификации включить архитектурный комментарий (микросервис, модуль, UI, state, base path).

### OpenAPI / Security

- `info.x-microservice`: `name: narrative-service`, `port: 8087`, `domain: narrative`, `base-path: /api/v1/narrative/dialogues/quests`, `package: com.necpgame.narrativeservice`.
- `servers`: `https://api.necp.game/v1`, `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.
- `security`: `BearerAuth` с ролями `quest-dialogue-view`, `quest-dialogue-manage`, `quest-dialogue-analytics`.
- `x-events`: `narrative.dialogue.maelstrom.stateChanged`, `narrative.dialogue.maelstrom.checkExecuted`, `narrative.dialogue.maelstrom.auditTriggered`.

---

## 📡 Endpoints

- **GET `/narrative/dialogues/quests/maelstrom-double-cross`** — обзор цепочки (состояния, entry nodes, связанные NPC/активности).
- **GET `/narrative/dialogues/quests/maelstrom-double-cross/state`** — текущее состояние (flags, reputation, active nodes).
- **POST `/narrative/dialogues/quests/maelstrom-double-cross/state`** — обновление прогресса (установка флагов, выдача наград, world events).
- **POST `/narrative/dialogues/quests/maelstrom-double-cross/checks`** — выполнение проверок (Intimidation, Deception, Hacking, Insight, Technical).
- **POST `/narrative/dialogues/quests/maelstrom-double-cross/actions/reputation`** — пакетное изменение репутаций (Maelstrom, Militech, NCPD, social media).
- **POST `/narrative/dialogues/quests/maelstrom-double-cross/actions/economy`** — выдача лута/контрактов/цифровых payload.
- **POST `/narrative/dialogues/quests/maelstrom-double-cross/events`** — обработка world events (escalation, underlink raids) и их влияние на DC/ветки.
- **GET `/narrative/dialogues/quests/maelstrom-double-cross/history`** — журнал решений, наград, audits.
- **GET `/narrative/dialogues/quests/maelstrom-double-cross/telemetry`** — метрики: loyalty, Militech deals, triple agent rate, meme rate, blacklist rate.
- **POST `/narrative/dialogues/quests/maelstrom-double-cross/simulate`** — QA прогон сценариев (loyal, corp, double/triple, personal).
- **POST `/narrative/dialogues/quests/maelstrom-double-cross/reset`** — сброс состояния (админ).

Каждый endpoint описать с параметрами (`playerId`, `partyId`, `questRunId`, `stateId`, `checkId`, `reputationChanges`, `eventId`), телами запросов, ответами, ошибками, `x-integrations`.

---

## 🧩 Модели данных

- **DialogueQuestOverview**
- **DialogueQuestState** (`flag.sqmdl.*`, активные узлы, репутация, world events)
- **DialogueNodeDetail**
- **CheckExecutionRequest/Response**
- **ReputationBatchRequest/Response**
- **EconomyActionRequest/Response** (loot, contracts, penalties)
- **EventImpactRequest/Result**
- **RewardPackage**
- **HistoryEntry**
- **TelemetryReport** (`maelstrom-loyalty-rate`, `militech-deal-rate`, `triple-agent-rate`, `personal-hoard-rate`, `meme-rate`, `blacklist-rate`)
- **SimulationRequest/Result**
- **AuditEntry**

Обязательные поля, `description`, `example`, `enum`. Добавить расширения:
- `x-frontend`
- `x-storage` (PostgreSQL quest tables, Redis cache, Kafka topics)
- `x-monitoring` (Grafana панели, PagerDuty alerts)
- `x-privacy` (retention, anonymization)
- `x-governance` (review board, risk levels)

---

## ✅ Детальный план

### Шаг 1: Извлечь структуру квеста
- Разложить состояния, узлы, опции, проверки, награды.
- Определить зависимости репутаций/событий.
- Сопоставить с шаблоном диалогов (API-TASK-379) и Valentinos цепочкой (API-TASK-380).

**Результат:** детальная схема квеста.

### Шаг 2: Архитектура OpenAPI
- Настроить заголовок, `info.x-microservice`, `servers`, `tags`.
- Добавить архитектурный комментарий.
- Решить, какие модели вынести в components.

**Результат:** каркас файла.

### Шаг 3: Описание endpoints
- Разработать `paths` для state/checks/actions/events/history/telemetry/simulate/reset.
- Добавить `operationId`, `tags`, примеры, `x-integrations`.
- Обеспечить стандартные ответы и ошибки.

**Результат:** раздел `paths` завершён.

### Шаг 4: Схемы и расширения
- Создать схемы `DialogueQuestState`, `CheckExecutionResult`, `ReputationBatchResult`, `TelemetryReport`.
- Добавить `x-frontend`, `x-storage`, `x-monitoring`, `x-privacy`, `x-governance`.
- Подготовить примеры JSON (loyal, corp, triple, personal сценарии).

**Результат:** `components/schemas` готов.

### Шаг 5: Телеметрия и баланс
- Описать метрики, SLA (check execution ≤ 150 мс, state update ≤ 200 мс, telemetry post ≤ 300 мс).
- Подготовить FAQ по выгоду фракций, triple агентам, мем кулдаун.

**Результат:** спецификация учитывает эксплуатацию и управление рисками.

### Шаг 6: Проверка качества
- Прогнать `scripts/validate-swagger.ps1 api/v1/narrative/dialogues/quests/maelstrom-double-cross.yaml`.
- Проверить `tasks/config/checklist.md`.
- Убедиться, что основной файл ≤380 строк (при необходимости вынести схемы).

**Результат:** валидная спецификация готова к передаче исполнительному агенту.

---

## 📏 Критерии приёмки (12)

1. `api/v1/narrative/dialogues/quests/maelstrom-double-cross.yaml` создан и проходит `scripts/validate-swagger.ps1`.
2. `info.x-microservice` заполнен (`narrative-service`, порт 8087, base-path `/api/v1/narrative/dialogues/quests`).
3. `GET /narrative/dialogues/quests/maelstrom-double-cross` возвращает `DialogueQuestOverview` со всеми состояниями, узлами и наградами.
4. `GET`/`POST` `/state` работают с `DialogueQuestState`, поддерживают флаги и награды.
5. `POST /checks` описывает проверки (Intimidation, Deception, Hacking, Insight, Technical) и возвращает `CheckExecutionResult`.
6. `POST /actions/reputation` и `/actions/economy` документируют репутационные изменения и выдачу лута/контрактов.
7. `POST /events` учитывает `corporate_war_escalation`, `maelstrom_underlink_raid` и модификацию DC/веток.
8. `GET /history` и `/telemetry` предоставляют подробный журнал и метрики (`loyalty`, `deal`, `triple`, `personal`, `meme`, `blacklist` rates).
9. Крупные схемы вынесены в components или основной файл ≤380 строк; поля снабжены `required`, `description`, `example`.
10. Добавлены `x-frontend`, `x-storage`, `x-monitoring`, `x-privacy`, `x-governance`.
11. Указаны зависимости с API-TASK-379/380/243/241 и источники `.BRAIN`.
12. FAQ объясняет triple агент-путь, мем кулдауны, exile восстановление, обработку audits и privacy.

---

## ❓ FAQ

**В: Как ограничить мемы против Militech?**  
О: Endpoint `/telemetry` должен отслеживать `meme-rate`. При превышении 40% возвращать `warning`, social-service включает фильтр. Указать кулдаун (1 мем на 6 часов).

**В: Что происходит при blacklist?**  
О: В `DialogueQuestState` добавить поле `isBlacklisted`. При true блокировать дальнейшее взаимодействие до выполнения восстановительной миссии (Return endpoint вернет `403` с `errorCode: BIZ_MAELSTROM_BLACKLIST`).

**В: Поддерживается ли кооператив?**  
О: Да, использовать `partyId` и `questRunId`. FAQ описать правила консенсуса: финальное решение (`fallout`) требует согласия большинства (поле `votes`).

**В: Как обрабатывается личное присвоение чертежа?**  
О: Указать `flag.sqmdl.personal`. Endpoint `/actions/economy` при этом записывает сделку в economy-service и запускает audit таймер (вернуть `x-monitoring.auditDueAt`).

**В: Какие требования к storage?**  
О: Сохранение state в PostgreSQL (`narrative.quest_states`), кеширование в Redis (`quest:maelstrom:{playerId}`), Kafka топики `narrative.quest.maelstrom.events` и `analytics.quest.maelstrom.telemetry`.

---

**Примечание:** После создания задания обновить `brain-mapping.yaml`, добавить секцию статуса в `.BRAIN/04-narrative/dialogues/quest-side-maelstrom-double-cross.md` и выполнить `scripts/autocommit.ps1`.

