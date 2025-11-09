# Task ID: API-TASK-380
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 22:05
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-379, API-TASK-243, API-TASK-241

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию для диалоговой цепочки побочных квестов «Heywood Valentinos», включающей многосценовые состояния, проверки и интеграции с репутацией и world events на основе `.BRAIN/04-narrative/quests/side/heywood-valentinos-chain.md`.

**Что нужно сделать:** Создать `api/v1/narrative/dialogues/quests/heywood-valentinos-chain.yaml` (при необходимости вынести схемы в `api/v1/narrative/components/dialogue-quests-schemas.yaml`) для narrative-service, описав получение структуры диалога, выполнение проверок, обновление состояния и обработку телеметрии.

---

## 🎯 Цель задания

Обеспечить narrative-service контрактом, который позволит игровым клиентам управлять цепочкой Valentinos против Maelstrom/Militech, синхронизировать репутацию, выдачу наград, world events и медиасобытия (стримы, мемориалы).

**Зачем это нужно:**
- Поддержать комплексную арку Valentinos с множеством ветвлений и исходов.
- Координировать репутации (Valentinos, Maelstrom, NCPD, social media) и world events (`heywood_turf_war`, `metro_shutdown`, `blackwall_breach`).
- Предоставить API для выполнения D&D проверок и выдачи наград/контрактов.
- Собирать телеметрию выбора путей (loyalty, double-cross, memorial participation) для балансировки.

---

## 📚 Источники информации

### Основной документ

- **Репозиторий:** `.BRAIN`
- **Путь:** `.BRAIN/04-narrative/quests/side/heywood-valentinos-chain.md`
- **Версия:** 1.0.0
- **Дата обновления:** 2025-11-07 21:21
- **Статус:** approved, `api-readiness: ready`

**Что важно:**
- Состояния цепочки (`setup`, `infiltration`, `street-race`, `turf-war`, `double-cross`, `memorial`).
- Узлы, опции, требования, исходы, награды, флаги.
- Таблица D&D проверок и интеграция с events/репутациями.
- REST/GraphQL контуры: state, run-check, media, telemetry.

### Дополнительные источники

- `.BRAIN/04-narrative/dialogues/npc-jose-tiger-ramirez.md`
- `.BRAIN/04-narrative/dialogues/npc-rita-moreno.md`
- `.BRAIN/04-narrative/dialogues/npc-royce.md`
- `.BRAIN/04-narrative/quests/side/quest-side-maelstrom-double-cross.md` (если есть)
- `.BRAIN/02-gameplay/social/reputation-formulas.md`
- `.BRAIN/02-gameplay/world/events/world-events-framework.md`
- `.BRAIN/04-narrative/dialogues/DIALOGUE-TEMPLATE.md` (структурные правила)

### Связанные задания

- API-TASK-379 (Dialogue Templates API)
- API-TASK-243 (Social Resonance API)
- API-TASK-241 (World Interaction Suite API)
- API-TASK-378 (Kaede Ishikawa NPC API) — пересечение по double-cross логике

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

- **Целевой файл:** `api/v1/narrative/dialogues/quests/heywood-valentinos-chain.yaml`
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
            │   ├── templates.yaml
            │   └── quests/
            │       └── heywood-valentinos-chain.yaml
            └── components/
                └── dialogue-quests-schemas.yaml   # при необходимости вынести общие сущности
```
> Держать основной файл ≤380 строк; крупные модели (DialogueQuest, DialogueState, DialogueNode, CheckOutcome) вынести в components.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервисная архитектура)

- **Микросервис:** narrative-service
- **Порт:** 8087
- **API Base Path:** `/api/v1/narrative/dialogues/quests/*`
- **Домен:** Нарративные диалоговые квесты
- **Зависимости:**
  - social-service (репутации фракций/соцсетей)
  - world-service (events, world modifiers, flags)
  - economy-service (награды, контракты)
  - gameplay-service (проверки D&D)
  - analytics-service (телеметрия)

### Frontend (модульная архитектура)

- **Модули:** `modules/narrative/quests/heywood-valentinos`, `modules/social/gangs`, `modules/world/events`
- **State Store:** `useNarrativeStore (questDialogueState)` + `useSocialStore (reputationState)` + `useWorldStore (eventState)`
- **Состояние:** currentState, flags, activeNodes, pendingChecks, rewards, telemetry
- **UI компоненты (@shared/ui):** QuestDialogueTree, ReputationGauge, EventImpactBanner, MissionOutcomePanel
- **Формы (@shared/forms):** DialogueDecisionForm, CheckExecutionForm, MemorialEntryForm
- **Layouts (@shared/layouts):** QuestDialogueLayout
- **Hooks (@shared/hooks):** useRealtime, useQuestState, useTelemetry, useAutosave

**Комментарий:** Добавить в начале спецификации архитектурный блок (микросервис, модуль, компоненты, state store, base path).

### OpenAPI / Security

- `info.x-microservice`: `name: narrative-service`, `port: 8087`, `domain: narrative`, `base-path: /api/v1/narrative/dialogues/quests`, `package: com.necpgame.narrativeservice`.
- `servers`: `https://api.necp.game/v1`, `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.
- `security`: `BearerAuth` (roles: `quest-dialogue-view`, `quest-dialogue-manage`, `quest-dialogue-analytics`).
- `x-events`: `narrative.dialogue.valentinos.stateChanged`, `narrative.dialogue.valentinos.checkExecuted`, `narrative.dialogue.valentinos.memorialLogged`.

---

## 📡 Endpoints

- **GET `/narrative/dialogues/quests/heywood-valentinos-chain`** — базовая информация о цепочке (состояния, entry nodes, связанные NPC).
- **GET `/narrative/dialogues/quests/heywood-valentinos-chain/state`** — текущее состояние игрока/партии (флаги, репутация, активные узлы).
- **POST `/narrative/dialogues/quests/heywood-valentinos-chain/state`** — обновление прогресса (установить флаги, награды, world events).
- **POST `/narrative/dialogues/quests/heywood-valentinos-chain/checks`** — выполнение проверок (Intimidation, Streetwise, Hacking, Technical, Performance, Strategy, Empathy, Insight, Willpower).
- **POST `/narrative/dialogues/quests/heywood-valentinos-chain/actions/media`** — логирование стримов/мемориалов, обновление social media репутации.
- **POST `/narrative/dialogues/quests/heywood-valentinos-chain/actions/reputation`** — пакетное изменение репутаций (Valentinos, Maelstrom, NCPD, Social).
- **POST `/narrative/dialogues/quests/heywood-valentinos-chain/events`** — обработка world events влияющих на сцену (`heywood_turf_war`, `metro_shutdown`, `blackwall_breach`).
- **GET `/narrative/dialogues/quests/heywood-valentinos-chain/history`** — история решений (пагинация, фильтры по исходам).
- **GET `/narrative/dialogues/quests/heywood-valentinos-chain/telemetry`** — отчёты: `valentinos-loyalty-rate`, `ncpd-ceasefire-rate`, `maelstrom-betrayal-rate`, `memorial-participation`.
- **POST `/narrative/dialogues/quests/heywood-valentinos-chain/simulate`** — (админ) прогон сценариев для QA (loyal, ncpd, maelstrom пути).
- **POST `/narrative/dialogues/quests/heywood-valentinos-chain/reset`** — сброс состояния (админ) для новой попытки.

Каждый endpoint описать с параметрами (`playerId`, `partyId`, `questRunId`, `stateId`, `checkId`, `repChanges`, `eventId`), телами запросов и подробными ответами, включая стандартные ошибки.

---

## 🧩 Модели данных

- **DialogueQuestOverview** — метаданные, состояния, entry nodes, связанные NPC/quests, награды.
- **DialogueQuestState** — текущие флаги (`flag.valchain.*`), активные узлы, репутация, world events.
- **DialogueNodeDetail** — опции, требования, исходы (success/failure/critical).
- **CheckExecutionRequest/Result** — статистика, модификаторы, последствия.
- **MediaActionRequest/Result** — стримы, мемориалы, social media эффекты.
- **ReputationBatchRequest/Result** — изменения репутации по фракциям.
- **EventImpactRequest/Result** — влияние world events (DC модификаторы, новые ветки).
- **RewardPackage** — предметы, контракты, активности, buffs/debuffs.
- **HistoryEntry** — timestamp, игрок, действие, результат, репутация, награды.
- **TelemetryReport** — показатели выбора (loyalty, ncpd, maelstrom, exile, memorial).
- **SimulationRequest/Result** — параметры QA прогона, outcomes.
- **AuditEntry** — кто и когда вносил изменения.

Добавить `required`, `description`, `example`, `enum`, расширения `x-frontend`, `x-storage`, `x-monitoring`, `x-privacy`, `x-governance`.

---

## ✅ Детальный план

### Шаг 1: Анализ узлов и состояний
- Выписать все состояния, узлы, проверки и награды.
- Сопоставить world events и репутации, указанные в документе.
- Синхронизировать с шаблоном диалогов (API-TASK-379).

**Результат:** матрица состояний → узлы → опции → исходы.

### Шаг 2: Архитектурный каркас
- Настроить заголовок файла, `info.x-microservice`, `servers`, `tags`.
- Подготовить комментарий об архитектуре и front-end модулях.
- Запланировать вынесение общих схем в components.

**Результат:** структура OpenAPI файла.

### Шаг 3: Проработка `paths`
- Описать endpoints для state, checks, actions, events, telemetry, history.
- Добавить `operationId`, `tags`, примеры, `x-integrations` (social/world/economy).
- Подключить стандартные ответы из `shared/common/responses.yaml`.

**Результат:** раздел `paths` закрывает все сценарии.

### Шаг 4: Схемы и расширения
- Создать схемы `DialogueQuestOverview`, `DialogueQuestState`, `DialogueNodeDetail`, `CheckExecutionResult`, `TelemetryReport`.
- Добавить `x-frontend`, `x-storage`, `x-monitoring`, `x-privacy` к ключевым моделям.
- Включить примеры JSON (линии loyal, ncpd, maelstrom).

**Результат:** `components/schemas` готов и переиспользуем.

### Шаг 5: Телеметрия и балансы
- Документировать метрики (`valentinos-loyalty-rate`, `ncpd-ceasefire-rate`, `maelstrom-betrayal-rate`, `memorial-participation`).
- Задать SLA (run-check ≤ 150 мс, state update ≤ 200 мс).
- Подготовить FAQ (double-cross, exile, memorial).

**Результат:** спецификация учитывает эксплуатацию и баланс.

### Шаг 6: Проверка качества
- Прогнать `scripts/validate-swagger.ps1 api/v1/narrative/dialogues/quests/heywood-valentinos-chain.yaml`.
- Проверить чеклист `tasks/config/checklist.md`.
- Убедиться, что основной файл ≤380 строк (при необходимости вынести модели).

**Результат:** валидная спецификация готова к исполнению.

---

## 📏 Критерии приёмки (12)

1. `api/v1/narrative/dialogues/quests/heywood-valentinos-chain.yaml` создан и проходит `scripts/validate-swagger.ps1`.
2. `info.x-microservice` заполнен (`narrative-service`, порт 8087, base-path `/api/v1/narrative/dialogues/quests`).
3. `GET /narrative/dialogues/quests/heywood-valentinos-chain` возвращает `DialogueQuestOverview` с состояниями, entry nodes, связанными NPC и наградами.
4. `GET /.../state` и `POST /.../state` работают с `DialogueQuestState`, поддерживают установку флагов и наград.
5. `POST /.../checks` выполняет проверки для всех статов, возвращает `CheckExecutionResult` с outcome и эффектами.
6. `POST /.../actions/media` и `/actions/reputation` документируют влияние на social media и репутации.
7. `POST /.../events` описывает взаимодействие с `heywood_turf_war`, `metro_shutdown`, `blackwall_breach` и их эффекты.
8. `GET /.../history` и `/telemetry` предоставляют подробные записи и метрики (loyalty, ncpd, maelstrom, memorial).
9. Схемы вынесены в components или основной файл ≤380 строк; все поля имеют `required`, `description`, `example`.
10. Добавлены расширения `x-frontend`, `x-storage`, `x-monitoring`, `x-privacy`, `x-governance`.
11. Указаны зависимости с API-TASK-379 (шаблоны), API-TASK-243 (резонанс), API-TASK-241 (world) и соответствующие источники `.BRAIN`.
12. FAQ описывает пути лояльности, работу с изгнанием, повторные попытки и политику хранения данных.

---

## ❓ FAQ

**В: Можно ли пропустить состояния?**  
О: Нет. `DialogueQuestState` содержит `stateProgression`; попытка перейти к `turf-war` без `street-race` возвращает `409 Conflict`.

**В: Как фиксируются массовые провалы (изгнание)?**  
О: В `CheckExecutionResult` добавить поле `exileTriggered`; при массовых провалах возвращать `warning` и обновлять `TelemetryReport`.

**В: Поддерживается ли кооператив?**  
О: Да, использовать `partyId` и `questRunId`. Endpoint `/simulate` должен учитывать party context. FAQ указать, что итоговое решение double-cross требует согласия большинства (дополнительное поле `votes`).

**В: Как обрабатывается social media мемориал?**  
О: Endpoint `/actions/media` принимает `memorialEntry`, обновляет `rep.social.media` и логирует события в social-service. В FAQ указать лимиты (одна запись в 12 часов).

**В: Как защищаются данные игроков?**  
О: `x-privacy` → retention 60 дней для history/telemetry, персональные данные деперсонифицируются для аналитики.

---

**Примечание:** После создания задания обновить `brain-mapping.yaml`, добавить секцию статуса в `.BRAIN/04-narrative/quests/side/heywood-valentinos-chain.md` и выполнить `scripts/autocommit.ps1`.

