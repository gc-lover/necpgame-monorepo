# Task ID: API-TASK-377
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 21:10
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-376, API-TASK-375

---

## 📋 Краткое описание

Необходимо подготовить OpenAPI спецификацию для кат-сцен и диалогов, связанных с цепочкой Helios Countermesh Conspiracy, включая ветки Specter/Helios/Double Agent.

**Что нужно сделать:** Создать `api/v1/narrative/cutscenes/helios-countermesh.yaml` (с компонентами в `api/v1/narrative/components/cutscene-schemas.yaml`, если нужно) на базе `.BRAIN/04-narrative/cutscenes/2025-11-07-helios-conspiracy-cutscenes.md`.

---

## 🎯 Цель задания

Предоставить narrative-service контракт для запуска кат-сцен, диалоговых веток и связанных событий, синхронизированных с квестами и world-state.

**Зачем это нужно:**
- Управлять кат-сценами `Blackwall Warning`, `Helios Allegiance` и пост-квестовыми диалогами.
- Поддерживать динамические флаги (Specter loyalty, Helios support, double agent).
- Интегрировать narrative события с world-service (city unrest), social feeds и telemetry.
- Обеспечить UI доступ к сценам, диалоговым вариантам и визуальным элементам.

---

## 📚 Источники информации

### Основной документ

- **Репозиторий:** `.BRAIN`
- **Путь:** `.BRAIN/04-narrative/cutscenes/2025-11-07-helios-conspiracy-cutscenes.md`
- **Версия:** 1.0.0
- **Дата обновления:** 2025-11-07 23:45
- **Статус:** approved, `api-readiness: ready`

**Что важно:**
- Кат-сцены `Blackwall Warning`, `Helios Allegiance`, их YAML-блоки, триггеры.
- Пост-квестовые диалоги Kaori и Dr. Lysander, условия и эффекты.
- События, telemetry, SLA.

### Дополнительные источники

- `.BRAIN/04-narrative/quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`
- `.BRAIN/04-narrative/quests/raid/2025-11-07-raid-specter-surge.md`
- `.BRAIN/02-gameplay/world/specter-hq.md`
- `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`
- `API-SWAGGER/api/v1/narrative/quests/helios-countermesh-conspiracy.yaml` (будет создан)
- `API-SWAGGER/api/v1/narrative/raids/specter-surge.yaml`

### Связанные задания

- API-TASK-376 (quest chain)
- API-TASK-375 (raid)
- API-TASK-243 (social resonance)
- API-TASK-241 (world interaction)

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

- **Целевой файл:** `api/v1/narrative/cutscenes/helios-countermesh.yaml`
- **Компоненты (опционально):** `api/v1/narrative/components/cutscene-schemas.yaml`
- **API версия:** v1 (semantic 1.0.0)
- **Тип:** OpenAPI 3.0.3

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            ├── cutscenes/
            │   └── helios-countermesh.yaml
            └── components/
                └── cutscene-schemas.yaml   # если нужно вынести структуры сцен/диалогов
```
> Следить за длиной основного файла (≤320 строк). Объёмные схемы (`Cutscene`, `DialogueState`, `Trigger`, `Segment`) выносить в components.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)

- **Микросервис:** narrative-service
- **Порт:** 8087
- **API Base Path:** `/api/v1/narrative/cutscenes/*`
- **Домен:** Нарративные сцены и диалоги
- **Зависимости:**
  - world-service (city unrest adjustments, world events)
  - social-service (feeds, resonance)
  - economy-service (rewards, unlocks)
  - analytics-service (telemetry, dashboards)

### Frontend (модульная архитектура)

- **Модули:** `modules/narrative/cutscenes`, `modules/world/overlays`, `modules/social/feeds`
- **State Store:** `useNarrativeStore (cutsceneState)` + `useWorldStore (unrestTracking)`
- **UI компоненты (@shared/ui):** CutscenePlayer, DialogueChoicePanel, HologramViewer, ObjectiveBriefingCard
- **Формы (@shared/forms):** DialogueChoiceForm, CutsceneTriggerForm
- **Layouts (@shared/layouts):** NarrativeCinematicLayout
- **Hooks (@shared/hooks):** useDialogueState, useCutscenePlayback, useTelemetry

**Комментарий:** Добавить в начало файла архитектурный блок с указанием микросервиса, модулей, UI компонентов, state store.

### OpenAPI требования

- `info.x-microservice`: `name: narrative-service`, `port: 8087`, `domain: narrative`, `base-path: /api/v1/narrative/cutscenes`, `package: com.necpgame.narrativeservice`.
- `servers`: `https://api.necp.game/v1`, `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`.
- `security`: `BearerAuth`, роли `cutscene-view`, `cutscene-manage`.
- `x-events`: `narrative.cutscene.played`, `narrative.dialogue.choice`, `narrative.cutscene.completed`.

---

## 📡 Endpoints

- **GET `/narrative/cutscenes/helios-countermesh`** — список доступных кат-сцен и диалогов с условиями (`CutsceneOverview`).
- **GET `/narrative/cutscenes/helios-countermesh/{cutsceneId}`** — детали конкретной кат-сцены (segments, dialogue, events) (`CutsceneDetail`).
- **POST `/narrative/cutscenes/helios-countermesh/{cutsceneId}/play`** — запуск кат-сцены с проверкой флагов (`CutscenePlayRequest/Response`).
- **POST `/narrative/cutscenes/helios-countermesh/dialogues/{dialogueId}/choice`** — отправка выбранной реплики (`DialogueChoiceRequest/Result`).
- **POST `/narrative/cutscenes/helios-countermesh/events`** — публикация сопутствующих событий (`CutsceneEventRequest/Result`).
- **GET `/narrative/cutscenes/helios-countermesh/telemetry`** — статистика просмотров, выборов, доверия Kaori (`CutsceneTelemetry`).
- **GET `/narrative/cutscenes/helios-countermesh/logs`** — история воспроизведённых сцен и выборов (с пагинацией).
- **POST `/narrative/cutscenes/helios-countermesh/reset`** — сброс состояния кат-сцены (админ) для тестирования или повторного просмотра.

Каждый endpoint должен:
- Описать параметры (`cutsceneId`, `dialogueId`, `playerId`, `partyId`).
- Возвращать `200/202` + ошибки `400/401/403/404/409/422/429/500`.
- Указывать `x-integrations` (например, world-service для `add_city_unrest`, social-service для feeds).
- Приводить примеры (успешный запуск, ошибка из-за недоступных флагов).

---

## 🧩 Модели данных

- **CutsceneOverview** — список сцен (id, title, trigger conditions, required flags, outcomes).
- **CutsceneDetail** — segments, dialogue, choices, rewards, emitted events.
- **CutsceneSegment** — тип (`scene`, `holo_feed`, `briefing`, `arrival`, `pledge`), location, characters, dialogue.
- **DialogueOption** — id, text, effects (flags, reputation, world modifiers).
- **TriggerCondition** — quest flags, stages, prerequisites.
- **CutscenePlayRequest/Response** — входные данные (playerId, partyId, flags), ответ (status, events emitted, rewards).
- **DialogueChoiceRequest/Result** — выбранная опция, новые флаги, репутация, world-state изменения.
- **CutsceneEventRequest/Result** — emitted events, target services.
- **CutsceneTelemetry** — просмотры, выборы, city unrest delta, Kaori trust delta, Helios pledge count.
- **CutsceneLogEntry** — playerId, cutsceneId, choice, timestamp.
- **AuditEntry** — кто запустил, с какими правами.

Расширения:
- `x-frontend` (компоненты UI)
- `x-storage` (PostgreSQL таблицы, Kafka topics)
- `x-monitoring` (Grafana панели, PagerDuty alerts)
- `x-privacy` (dialogue logs, retention)
- `x-governance` (обязательные approvals, rating)

---

## ✅ Детальный план

### Шаг 1: Анализ сцен и диалогов
- Выписать триггеры, сегменты, выборы, эффекты.
- Определить связи с квестом и world-state.
- Решить, какие поля обязательно нужны в API (music, holograms, events).

**Результат:** список сущностей и связей.

### Шаг 2: Архитектура файла
- Создать каркас OpenAPI с `info`, `servers`, `security`, `tags`.
- Добавить архитектурный комментарий (микросервис, модули, UI компоненты).
- Определить, какие схемы выносить в components.

**Результат:** готовый шаблон.

### Шаг 3: Реализация endpoints
- Заполнить секцию `paths` для всех операций (GET/POST).
- Добавить примеры, `operationId`, `tags`, `x-integrations`.
- Обеспечить стандартные ошибки и ссылки на компоненты.

**Результат:** раздел `paths` полностью описан.

### Шаг 4: Модели данных
- Реализовать схемы `Cutscene`, `Segment`, `DialogueOption`, `Trigger`, `Telemetry`, `LogEntry`.
- Добавить `x-frontend`, `x-monitoring`, `x-storage`, `x-privacy`.
- Подготовить примеры JSON.

**Результат:** `components/schemas` завершён.

### Шаг 5: Безопасность и telemetry
- Настроить `security` (roles, scopes).
- Описать telemetry метрики, SLA, events (`SPECTER_BLACKWALL_WARNING`, `HELIOS_PLEDGE`, `HELIOS_COUNTERMESH_UPGRADE`).
- Добавить FAQ по повторным показам, пропуску, приватности.

**Результат:** спецификация учитывает эксплуатационные требования.

### Шаг 6: Валидация
- Прогнать `scripts/validate-swagger.ps1 api/v1/narrative/cutscenes/helios-countermesh.yaml`.
- Проверить чеклист `tasks/config/checklist.md`.
- Убедиться, что основной файл ≤320 строк (иначе вынести компоненты).

**Результат:** валидный контракт, готовый к реализации.

---

## 📏 Критерии приёмки (12)

1. Создан `api/v1/narrative/cutscenes/helios-countermesh.yaml`, проходит `scripts/validate-swagger.ps1`.
2. `info.x-microservice` заполнен для narrative-service (порт 8087, base-path `/api/v1/narrative/cutscenes`).
3. `GET /narrative/cutscenes/helios-countermesh` возвращает список кат-сцен/диалогов с триггерами и условиями.
4. `GET /narrative/cutscenes/helios-countermesh/{cutsceneId}` описывает сегменты, диалоги, события, награды.
5. `POST /narrative/cutscenes/helios-countermesh/{cutsceneId}/play` проверяет флаги, возвращает эмитированные события, награды и world/social эффекты.
6. `POST /narrative/cutscenes/helios-countermesh/dialogues/{dialogueId}/choice` принимает `DialogueChoiceRequest`, обновляет флаги/репутацию/городское настроение.
7. Спецификация описывает emitted events (`SPECTER_BLACKWALL_WARNING`, `HELIOS_PLEDGE`, `HELIOS_COUNTERMESH_UPGRADE`) и telemetry.
8. Все endpoints включают ошибки 400/401/403/404/409/422/429/500 и примеры ответов.
9. Схемы вынесены в components или основной файл ≤320 строк; все модели имеют `required`, `description`, `example`.
10. Добавлены расширения `x-frontend`, `x-storage`, `x-monitoring`, `x-privacy`, `x-governance`.
11. FAQ объясняет повторные показы, пропуски сцен, работу с Double Agent веткой.
12. Документация описывает интеграции с world-service (city unrest) и social-service (feeds) через `x-integrations`.

---

## ❓ FAQ

**В: Можно ли повторно проигрывать кат-сцены?**  
О: Да, endpoint `/play` должен поддерживать флаг `allowReplay`; в случае отсутствия прав возвращать `403`. Пример включить в спецификацию.

**В: Как обрабатываются эффекты на City Unrest?**  
О: Через `x-integrations.world` указать вызов world-service. В ответах показывать `cityUnrestDelta`.

**В: Нужно ли сохранять выборы игрока?**  
О: Да, использование `CutsceneLogEntry` и `x-privacy.retention`. Минимум 30 дней хранения, доступ только ролям `cutscene-analytics`.

**В: Что делать с музыкой/аудио?**  
О: Зафиксировать в поле `audioCue` и указать ссылку на asset (ID в DAM). Это метаданные, не медиа.

**В: Как интегрировать с social feeds?**  
О: Использовать endpoint `/events`; если событие требует публикации, указать `x-integrations.social` с payload для social-service.

---

**Примечание:** После реализации спецификации обновить `brain-mapping.yaml` и `current-status.md`, чтобы track выработку контента.

