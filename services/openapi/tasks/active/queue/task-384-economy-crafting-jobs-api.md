# Task ID: API-TASK-384
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 22:05  
**Автор:** API Task Creator Agent  
**Зависимости:** API-TASK-320 (player-orders economy index API), API-TASK-333 (economy visuals items detailed API), API-TASK-382 (combat ballistics API)

---

## Summary

Сформировать спецификацию `crafting-weapon-jobs.yaml` для `economy-service`, описывающую управление очередями крафта оружейных модов и чертежей. Контракт должен обеспечить создание, отслеживание и отмену крафтовых заданий, синхронизацию с заказами игроков и публикацию событий `economy.crafting.jobs`.

---

## Source Documents

| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md` |
| Version | v1.0.0 |
| Status | ready |
| API readiness | ready (2025-11-08) |

**Key points:**
- Требуется REST интерфейс `/crafting/blueprints/{id}` и `/crafting/jobs` для интеграции с боевой системой и заказами.
- Нужно поддержать привязку к social orders (`linkedOrderId`), мастерским и требуемым навыкам.
- Событие `economy.crafting.jobs` должно содержать статус, ETA, потреблённые материалы.
- Проверки: наличие материалов, разрешённых мастерских, навыков, лимитов очереди.
- Нужна аналитика по длительности крафта и энергозатратам (возвращать KPI для telemetry-service).

**Related docs:**
- `.BRAIN/02-gameplay/combat/combat-shooting-advanced.md` (weapon mods)
- `.BRAIN/02-gameplay/economy/economy-crafting.md`
- `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md` (блокировки станций)
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` (link to orders)

---

## Target Architecture

- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/crafting  
- **API directory:** `api/v1/economy/crafting/crafting-weapon-jobs.yaml`  
- **base-path:** `/api/v1/gameplay/economy/crafting`  
- **Java package:** `com.necpgame.economyservice.crafting.jobs`  
- **Frontend module:** `modules/economy/crafting/jobs` (store: `useEconomyStore`)  
- **UI components:** `CraftingQueueTable`, `BlueprintDetailsDrawer`, `WorkshopSelector`, `MaterialChecklist`  
- **Shared libs:** `@shared/forms/StepperForm`, `@shared/ui/StatusBadge`, `@shared/hooks/usePolling`

---

## Scope of Work

1. Выделить сущности: Blueprint, CraftingJob, MaterialRequirement, Workshop, SkillRequirement, KPI.
2. Разработать структуру OpenAPI файла с info, security, servers, tags, paths.
3. Описать endpoints для CRUD по чертежам и заданиям, включая фильтры и пагинацию.
4. Подготовить схемы данных (запрос/ответ), перечисления статусов, ограничения по мастерским.
5. Описать интеграцию с social orders, combat ballistics и maintenance mode.
6. Добавить документацию по Kafka событию `economy.crafting.jobs` и метрикам.
7. Прогнать валидацию, обновить brain-mapping и .BRAIN документ.

---

## Endpoints

- **GET `/crafting/blueprints`** — список чертежей с фильтрами по типу (barrel, scope, chip), rarity, требуемым навыкам; поддерживает пагинацию.
- **GET `/crafting/blueprints/{blueprintId}`** — детальная информация: материалы, навыки, мастерские, время производства, совместимость с оружием.
- **POST `/crafting/jobs`** — создание задания; поля: blueprintId, workshopId, materials[], linkedOrderId, priority, requestedBy; возвращает `CraftingJob`.
- **GET `/crafting/jobs`** — список активных/завершённых заданий; фильтры по статусу, workshopId, linkedOrderId, priority.
- **GET `/crafting/jobs/{jobId}`** — статус задания (progress %, eta, worker NPC, материал/энергия).
- **POST `/crafting/jobs/{jobId}/cancel`** — отмена задания; проверяет статус, возвращает штрафы и возврат материалов.
- **POST `/crafting/jobs/{jobId}/pause`** — пауза/возобновление для maintenance режимов (optional).
- **GET `/crafting/workshops`** — (reference) список мастерских, режимов работы, текущих блокировок.

Все endpoints используют `BearerAuth`, заголовок `X-Player-Id` или `X-Operator-Id`, применяют ответы из `shared/common/responses.yaml`, ошибки 409/422 для конфликтов и нехватки материалов.

---

## Data Models

- `BlueprintSummary` — blueprintId, name, category, rarity, baseDuration, skillRequirements[], allowedWorkshops[], materialSnapshot.
- `BlueprintDetails` — включает `enhancementOptions`, `compatibleWeapons[]`, `economyIndices`, `maintenanceNeeds`.
- `CraftingJobRequest` — blueprintId, workshopId, linkedOrderId, quantity, priority, autoDistribute, scheduledAt.
- `CraftingJob` — jobId, blueprintId, status (queued, in_progress, paused, completed, failed, cancelled), progressPercent, eta, assignedWorkerId, energyCost, materialsConsumed[], linkedOrderId.
- `CraftingJobListResponse` — массив `CraftingJob` + `PaginationMeta`.
- `CraftingJobStatusUpdate` — stage, progressLog[], warnings[], maintenanceFlags[].
- `CancellationResult` — refundMaterials[], penalty, notes.
- `Workshop` — workshopId, name, location, status (online/offline/maintenance), capacity, specialization.
- Enumerations: `CraftingPriority`, `CraftingStatus`, `WorkshopStatus`, `MaterialQuality`.

---

## Integrations & Events

- **Social-service:** Передавать `linkedOrderId`; при завершении job публиковать webhook `POST /social/orders/{orderId}/attachments` (описать зависимость).
- **Gameplay-service:** Возвращать `suggestedMods` для `combat/weapon-mods`.
- **Maintenance-mode:** Проверять через `maintenance-mode-system` (API-TASK-205) — описать зависимость и error `423 Locked`.
- **Kafka:**  
  - `economy.crafting.jobs` — payload `{ jobId, blueprintId, status, progressPercent, eta, linkedOrderId, materialsConsumed, workshopId }`.  
  - `economy.crafting.jobs.alerts` — (optional) при неудаче или дефиците материалов.
- **Telemetry:** REST hook `/analytics/crafting/jobs` для KPI (duration, failureRate).
- **Rate limiting:** указать лимит 60 r/min для создания job с защитой от спама.

---

## Acceptance Criteria

1. Файл `api/v1/economy/crafting/crafting-weapon-jobs.yaml` создан (≤ 400 строк) и соответствует требованиям `info.x-microservice`.
2. Все endpoints задокументированы с примерами запросов/ответов, кодами ошибок и ссылками на общие компоненты.
3. Схемы данных включают все сущности (Blueprint, CraftingJob, Workshop, CancellationResult) с обязательными полями.
4. Поддерживается пагинация и фильтрация в `/crafting/blueprints` и `/crafting/jobs` (использовать `shared/common/pagination.yaml`).
5. Задокументированы статусы `queued`, `in_progress`, `paused`, `completed`, `failed`, `cancelled`, их переходы и ограничения.
6. Kafka событие `economy.crafting.jobs` описано с ключевыми полями, примерами и связями с social orders/combat ballistics.
7. Прописаны зависимости на maintenance-mode, social orders, combat ballistics.
8. Валидатор `validate-swagger.ps1 -ApiSpec api/v1/economy/crafting/crafting-weapon-jobs.yaml` проходит без ошибок.
9. Обновлены `brain-mapping.yaml`, `.BRAIN/06-tasks/.../2025-11-08-gameplay-backend-sync.md`, readiness-трекер.
10. В FAQ отражены вопросы по материалам, мастерским и отмене.
11. Примеры учитывают rare материалы и linkedOrderId (UUID).

---

## FAQ / Notes

- **Что делать, если материалов недостаточно?** Возвращать `422` с массивом `missingMaterials` (id, required, available); UI предлагает конверсию через economy-service.
- **Можно ли создавать батч из нескольких job?** Нет, текущий контракт поддерживает одиночные задания; для батчей использовать `priority=bulk` и повторяющиеся запросы.
- **Как учитывать мастерские в обслуживании?** Перед созданием job проверять статус; если `maintenance`, отвечать `423 Locked` с указанием расписания.
- **Поддерживаются ли NPC-исполнители?** Поле `assignedWorkerId` может содержать UUID NPC; при пустом значении используется автоприсвоение.
- **Где хранится история прогресса?** `progressLog[]` в `CraftingJobStatusUpdate` (timestamp, stage, comment); UI показывает в `CraftingQueueTable`.

---

## Change Log

- 2025-11-08 22:05 — Задание создано (API Task Creator Agent)


