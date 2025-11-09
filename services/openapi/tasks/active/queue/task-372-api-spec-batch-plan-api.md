# Task ID: API-TASK-372
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 19:40
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-177, API-TASK-180

---

## 📋 Краткое описание

Спроектировать OpenAPI-спецификацию для управления партиями генерации API (Batch Plan) и синхронизации с очередью ДУАПИТАСК. Задание покрывает формирование данных по пяти партиям, прогресс, лог действий и интеграцию с readiness-трекером.

**Что нужно сделать:** Создать `api/v1/technical/api-generation/batch-plan.yaml` и вспомогательные файлы партии (`batch-01-core.yaml` … `batch-05-competitive.yaml`) на основе `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-api-spec-batch-plan.md`.

---

## 🎯 Цель задания

Обеспечить административный сервис контрактом для запуска и контроля партий генерации API, связанных с документами Ready-статуса, без обращения к `.BRAIN`.

**Зачем это нужно:**
- Централизовать план партий и статус выполнения для операционной команды.
- Автоматизировать постановку задач в очередь исполнителей на основе readiness-трекера.
- Отслеживать историю запусков, блокировки и взаимосвязи документов внутри партий.
- Дать фронтенду админ-панели источник правды для визуализации прогресса и логов.

---

## 📚 Источники информации

### Основной документ

- **Репозиторий:** `.BRAIN`
- **Путь:** `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-api-spec-batch-plan.md`
- **Версия:** 1.2.0
- **Дата последнего обновления:** 2025-11-07 20:07
- **Статус:** approved, `api-readiness: ready`

**Что важно из документа:**
- Таблица партий 01–05 с тематикой, количеством документов и статусом готовности.
- Перечни исходных файлов по каждой партии (core, gameplay, economy, sessions, competitive).
- Инструкция создать YAML файлы `batch-0X-*.yaml` и вести прогресс в `current-status.md`.
- Следующие шаги по последовательному запуску партий и обновлению `brain-mapping.yaml`.

### Дополнительные источники

- `.BRAIN/06-tasks/config/readiness-tracker.yaml` — агрегатор статусов `api-readiness`.
- `.BRAIN/06-tasks/active/CURRENT-WORK/current-status.md` — лог фактического прогресса.
- `API-SWAGGER/tasks/config/task-creation-guide.md` и `tasks/config/checklist.md` — процесс и стандарты.
- `API-SWAGGER/api/v1/technical/mvp-backend-architecture.yaml` (будет создан) и `api/v1/technical/api-technical-summary.yaml` — примеры админских контрактов.

### Связанные документы (по партиям)

- **Batch 01 — Core & Infrastructure (11 docs):** achievement core/tracking/examples, leaderboard core, daily reset, maintenance, support, announcements, voice chat, referral, companion.
- **Batch 02 — Gameplay & Social (10 docs):** matchmaking (algorithm/queue/rating), party system, guild system, clan war, housing, chat (channels/features/moderation).
- **Batch 03 — Economy & Monetization (10 docs):** inventory (part1/part2), loot system (part1/part2), trade system, battle pass (core/rewards), cosmetic system, reset system, mail.
- **Batch 04 — Sessions & World State (9 docs):** player character management (part1/part2), session lifecycle/reconnection, realtime server (part1/part2), quest engine, progression backend, friend system.
- **Batch 05 — Competitive & Events (8 docs):** arena, loot hunt, dungeon scenarios, live events, voice lobby, leaderboard core (повтор), announcement system (повтор), anti-cheat core.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

- **Целевой файл:** `api/v1/technical/api-generation/batch-plan.yaml`
- **Вспомогательные файлы партии:**
  - `api/v1/technical/api-generation/batches/batch-01-core.yaml`
  - `api/v1/technical/api-generation/batches/batch-02-gameplay.yaml`
  - `api/v1/technical/api-generation/batches/batch-03-economy.yaml`
  - `api/v1/technical/api-generation/batches/batch-04-sessions.yaml`
  - `api/v1/technical/api-generation/batches/batch-05-competitive.yaml`
- **API версия:** v1 (semantic 1.0.0)
- **Тип:** OpenAPI 3.0.3 (основной файл) + reuse shared компонентов

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── technical/
            └── api-generation/
                ├── batch-plan.yaml
                └── batches/
                    ├── batch-01-core.yaml
                    ├── batch-02-gameplay.yaml
                    ├── batch-03-economy.yaml
                    ├── batch-04-sessions.yaml
                    └── batch-05-competitive.yaml
```
> Основной файл описывает endpoints, модели, security и ссылки на компоненты партий. Каждая партия может содержать переработанные components/examples и специфические схемы документов (≤200 строк на файл, выносить общие схемы в `api/v1/technical/components/api-generation-batch-shared.yaml` при необходимости).

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)

- **Микросервис:** admin-service
- **Порт:** 8088
- **API Base Path:** `/api/v1/technical/api-generation/*`
- **Домен:** Управление API задачами, прогрессом партий и административными операциями
- **Зависимости:**
  - readiness-tracker storage (PostgreSQL `ops_planning`)
  - task orchestration (Kafka topics `duapitas.batch.triggered`, `duapitas.batch.completed`)
  - auth-service для RBAC (`architecture-admin`, `api-ops`)
  - analytics-service для SLA и метрик выполнения

### Frontend (модульная архитектура)

- **Модуль:** `modules/admin/api-readiness`
- **State Store:** `useAdminStore (apiReadiness)`
- **Состояние:** batches, documents, actionsQueue, blockers, metrics
- **UI компоненты (@shared/ui):** BatchStatusBoard, BatchTimeline, DocumentReadinessTable, ActionLogPanel
- **Формы (@shared/forms):** BatchTriggerForm, BatchStatusPatchForm
- **Layouts (@shared/layouts):** AdminDashboardLayout
- **Hooks (@shared/hooks):** usePolling, useRealtime, useDebounce

**Комментарий для OpenAPI:** В начало каждого YAML добавить блок с архитектурой (микросервис, модуль, UI компоненты, state store, base-path) по примеру шаблона.

### OpenAPI / Shared требования

- Указать `info.x-microservice` (`name: admin-service`, `port: 8088`, `domain: admin`, `base-path: /api/v1/technical/api-generation`, `package: com.necpgame.adminservice`).
- В `servers` оставить только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`, а также `shared/common/sorting.yaml` при описании фильтров.

---

## 📡 Архитектура API и endpoints

### Основные методы (главный файл)

- **GET `/technical/api-generation/batches`** — агрегированный список партий (id, theme, docsCount, readinessStatus, queuedTasks, progressPercent). Поддержать фильтры `status`, `theme`, `containsDoc`, пагинацию и сортировку.
- **GET `/technical/api-generation/batches/summary`** — агрегированные метрики (готово, в очереди, blocked, totalDocs, завершённые задания).
- **GET `/technical/api-generation/batches/{batchId}`** — детальное описание партии, включая связанные документы, ожидаемые API файлы, риски, зависимые задачи.
- **GET `/technical/api-generation/batches/{batchId}/documents`** — список документов и ожидаемых API целей (path, microservice, plannedTaskId, blockers, dependencies).
- **POST `/technical/api-generation/batches/{batchId}/tasks:queue`** — постановка партии в очередь исполнения; вход: `QueueTrigger` (initiator, priority, comment, dryRun). Ответ `202 Accepted` с объектом `BatchAction`.
- **PATCH `/technical/api-generation/batches/{batchId}/status`** — обновление статуса партии (`queued`, `in_progress`, `completed`, `blocked`, `archived`) и фиксация причины/артефактов.
- **GET `/technical/api-generation/batches/{batchId}/log`** — лента событий (statusChanged, triggerQueued, documentSkipped, dependencyResolved) с пагинацией и фильтрами по типу события.
- **GET `/technical/api-generation/batches/{batchId}/metrics`** — SLA, количество созданных API файлов, среднее время генерации задачи, успехи/ошибки.

### Файлы партий (batches/*.yaml)

- Определяют статические компоненты `BatchDocument`, `BatchDependency`, `BatchChecklist`, `BatchRisk`, `BatchTaskPlan` для конкретной партии.
- Дают примеры (`examples`) и детальные перечисления документов, включая предполагаемый microservice и целевой API путь.
- Включают чек-листы по запуску (pre-flight, post-flight) и блокеры (например, документы in review).

---

## 🧩 Модели данных

- **BatchSummary:** `batchId`, `name`, `theme`, `plannedDocs`, `readyDocs`, `status`, `queuedTasks`, `progressPercent`, `priority`.
- **BatchDetail:** расширяет Summary полями `description`, `documents`, `dependencies`, `riskLevel`, `slaTarget`, `nextActionDue`.
- **BatchDocument:** `brainPath`, `documentVersion`, `readiness`, `targetApiFile`, `microservice`, `frontendModule`, `estimatedTaskId`, `blockers`, `notes`.
- **BatchTaskPlan:** `taskId`, `targetFile`, `expectedDuration`, `dependsOn`, `ownerGuild`, `status`.
- **QueueTrigger:** `initiator`, `priority`, `mode` (`auto`, `manual`), `dryRun`, `notes`.
- **BatchAction / QueueResponse:** `actionId`, `batchId`, `actionType`, `timestamp`, `performedBy`, `details`.
- **StatusUpdateRequest:** `status`, `reasonCode`, `comment`, `evidenceLinks`, `expectedResumeAt`.
- **BatchLogEntry:** `logId`, `batchId`, `eventType`, `severity`, `message`, `payload`, `createdAt`, `createdBy`.
- **BatchMetrics:** `generatedTasks`, `completedTasks`, `failedTasks`, `avgExecutionMinutes`, `lastRunAt`, `slaCompliance`.
- **BatchChecklistItem:** `itemId`, `title`, `type` (`preflight`, `postflight`), `required`, `status`, `evidence`.

Каждая схема должна содержать `required`, детальные описания, форматирование (ISO 8601 для дат), enum значений (`status`, `eventType`, `priority`). Добавить `x-frontend` с указанием, какие UI компоненты потребляют модель.

---

## ✅ Детальный план

### Шаг 1: Анализ документа и классификация партий
- Выписать из `.BRAIN` перечни документов, статусы и тематические группы.
- Сопоставить документы с целевыми микросервисами и фронтенд-модулями по таблицам доменов.
- Определить приоритет и риск (повторы, пересекающиеся документы).

**Результат:** матрица партий (id, theme, docs, microservice сеgments, blockers, готовность).

### Шаг 2: Проектирование моделей и компонентов
- Сформировать схемы `BatchSummary`, `BatchDetail`, `BatchDocument`, `BatchTaskPlan`, `BatchLogEntry`, `BatchMetrics`.
- Вынести общие схемы в `components` и переиспользовать их во всех endpoints и файлах партий.
- Определить enum значений `BatchStatus`, `BatchTheme`, `EventType`, `Priority`.

**Результат:** завершённый раздел `components/schemas` с повторным использованием.

### Шаг 3: Проработка endpoints главного файла
- Описать `paths` для операций списка, деталей, документов, постановки в очередь, обновления статуса, логов и метрик.
- Добавить параметры фильтрации (query, path), описать элементы пагинации, примеры запросов/ответов.
- Настроить `security` (Bearer JWT, роли `api-ops`, `architecture-admin`), предусмотреть ошибки 401/403/409/422.

**Результат:** полный раздел `paths` с примерами, ссылками на компоненты и стандартные ответы из `shared/common/responses.yaml`.

### Шаг 4: Наполнение файлов партий
- Для каждой партии перечислить документы с ключевыми полями (`brainPath`, `targetApi`, `microservice`, `frontendModule`, `plannedTaskId`).
- Добавить `checklists`, `risks`, `dependencies` (например, повторяющиеся документы между партиями 01 и 05).
- Включить `examples` ответов `BatchDetail` с реальными данными из таблицы `.BRAIN`.

**Результат:** 5 файлов `batch-0X-*.yaml`, подключаемых через `$ref` из основного файла.

### Шаг 5: Интеграции и события
- Задокументировать Kafka события (`duapitas.batch.triggered`, `duapitas.batch.completed`, `duapitas.batch.blocked`) через `x-events` или раздел `components/messages`.
- Описать, как API синхронизируется с readiness-трекером (polling + manual sync).
- Зафиксировать `x-monitoring` (SLO >= 99.5%, alert thresholds) и `x-governance` (обязательное ревью `API Program Board`).

**Результат:** разделы расширений в `info`, `paths` или `components` с чёткими ссылками.

### Шаг 6: Валидация и контроль качества
- Прогнать `scripts/validate-swagger.ps1 api/v1/technical/api-generation/batch-plan.yaml`.
- Убедиться, что каждый файл ≤400 строк (при необходимости вынести части в `components`).
- Проверить чеклист `tasks/config/checklist.md`, задокументировать результаты в PR описании.

**Результат:** валидный пакет OpenAPI, готовый к использованию фронтендом и backend-оркестратором.

---

## 📏 Критерии приёмки (12)

1. Создан `api/v1/technical/api-generation/batch-plan.yaml`, проходит `scripts/validate-swagger.ps1` без ошибок.
2. В `info.x-microservice` указан `admin-service` с портом 8088 и base-path `/api/v1/technical/api-generation`.
3. `servers` содержит только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`.
4. Описан endpoint `GET /technical/api-generation/batches` с фильтрами, сортировкой и пагинацией.
5. `GET /technical/api-generation/batches/{batchId}` возвращает структуру `BatchDetail`, включающую документы, риски и ожидаемые задачи.
6. `POST /technical/api-generation/batches/{batchId}/tasks:queue` защищён ролью `api-ops`, возвращает `BatchAction` и стандартные ошибки 401/403/409.
7. `PATCH /technical/api-generation/batches/{batchId}/status` принимает `StatusUpdateRequest` с валидацией enum статусов.
8. `GET /technical/api-generation/batches/{batchId}/log` использует `BatchLogEntry` и поддерживает фильтры по `eventType` и `severity`.
9. Созданы файлы партий `batch-01-core.yaml` … `batch-05-competitive.yaml`, подключенные через `$ref` в основном файле.
10. Каждая партия содержит детальный список документов (`brainPath`, `targetApiFile`, `microservice`, `frontendModule`, `plannedTaskId`) и чек-лист запуска.
11. Схемы содержат `x-frontend` аннотации и ссылки на UI компоненты (`BatchStatusBoard`, `BatchTimeline`, `DocumentReadinessTable`).
12. В спецификации описаны Kafka события и SLA через `x-events`, `x-monitoring`, `x-governance`, а также связь с `readiness-tracker.yaml`.

---

## ❓ FAQ

**В: Нужно ли создавать отдельный AsyncAPI файл для Kafka событий?**  
О: Нет, события фиксируются через `x-events` в OpenAPI. При расширении ассинхронных каналов можно вынести в AsyncAPI, но в текущей версии достаточно ссылок и описаний в спецификации.

**В: Что делать с документами, повторяющимися между партиями (например, leaderboard core)?**  
О: Зафиксировать в `BatchDocument.blockers` тип `duplicate`, указав партию-источник. При постановке задачи исполнитель должен объединить работу или удалить дубликат из очереди.

**В: Как учитывать документы, помеченные ready, но отсутствующие в файловой системе?**  
О: Маркировать такие записи в `BatchDocument` как `status: missing`, а в `BatchChecklist` добавить пункт проверки наличия файла. API должен возвращать `warning` для UI.

**В: Нужно ли поддерживать редактирование состава партий через API?**  
О: Нет, редактирование выполняется через `.BRAIN`. API read-only, кроме endpoints `tasks:queue` и `status`, которые фиксируют процесс выполнения.

**В: Как синхронизировать показатели с фактическими задачами API-SWAGGER?**  
О: Использовать `BatchMetrics` и указать, что данные подтягиваются через ETL из `brain-mapping.yaml` и task queue. Добавить `x-integrations.mappings` со ссылкой на `tasks/config/brain-mapping.yaml`.

---

**Примечание:** После реализации спецификаций обновить `brain-mapping.yaml` и `current-status.md`, чтобы поддерживать актуальный прогресс по партиям.

