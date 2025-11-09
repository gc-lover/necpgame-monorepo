# Task ID: API-TASK-304
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:58
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-301], [API-TASK-302], [API-TASK-303], [API-TASK-128]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию подсистемы доступности лодаутов (Loadout Availability & Degradation) для `gameplay-service`: управление недоступными предметами, fallback-комплектами, режимом `degraded`, нотификациями и аудитом.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для слежения за доступностью предметов и имплантов, подбора замен, перевода лодаута в деградированный режим, генерации предупреждений и аналитики.

---

## 🎯 Цель задания

Обеспечить устойчивость лодаутов к отсутствию предметов/имплантов и прозрачность для игроков и систем, чтобы избежать блокировок перед матчами и поддерживать баланс.

**Зачем это нужно:**
- Автоматически реагировать на временно недоступные предметы (аренда, таймеры, блокировки).
- Предлагать fallback-комплекты и режим `degraded` с ограничениями.
- Информировать игроков и аналитические системы о проблемах доступности.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Раздел «Управление недоступными предметами и имплантами» — сценарии дефицита, `availabilityService`, fallbackKit, режим `degraded`, события `combat.loadouts.availability-warning`.
- Раздел «Очереди обновлений и масштабирование» — батч обновления после балансовых патчей, live patch hook, `revision`/`jsonb_diff_patch`.
- Раздел «Метрики и телеметрия» — показатели `availability_conflicts`.
- Доменные сущности (`Loadout`, `LoadoutKit`, `LoadoutMacro`) и требования валидации (энергия, человечность).

### Дополнительные источники

- `.BRAIN/02-gameplay/economy/equipment-matrix.md` — бренды, аренда, истекающие предметы.
- `.BRAIN/02-gameplay/combat/combat-implants-limits.md` — лимиты имплантов и человечности.
- `.BRAIN/02-gameplay/combat/combat-roles-detailed.md` — зависимость ролей от предметов.
- `.BRAIN/02-gameplay/combat/arena-system.md` — требования арен к доступности.
- `.BRAIN/02-gameplay/combat/loot-hunt-system.md` — сценарии строгого контроля веса/доступности.
- `.BRAIN/_05-technical/backend/notification-system.md` — интеграция уведомлений.

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-301-combat-loadout-kits-api.md`
- `API-SWAGGER/tasks/active/queue/task-302-combat-loadout-profiles-api.md`
- `API-SWAGGER/tasks/active/queue/task-303-combat-loadout-macros-api.md`
- `API-SWAGGER/tasks/active/queue/task-128-inventory-system-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/gameplay/combat/loadout-availability.yaml`  
**Формат:** OpenAPI 3.0.3 (вынести компоненты/события при необходимости)

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── combat/
                ├── loadouts.yaml
                ├── loadout-kits.yaml
                ├── loadout-profiles.yaml
                ├── loadout-macros.yaml
                ├── loadout-availability.yaml          ← создать
                ├── loadout-availability-components.yaml
                └── loadout-availability-events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base:** `/api/v1/gameplay/combat/loadout-availability*`
- **Зависимости:** inventory-service (статус предметов), economy-service (аренда, блокировки), notification-service (alert), analytics-service (метрики), auth-service (scopes `loadouts:availability.*`), scheduler-service (batch jobs), admin-service (балансовые патчи).
- **Очереди:** Redis Streams/Kafka `combat.loadouts.recalculate`, `combat.loadouts.availability-warning`, `combat.loadouts.degraded`.

### Frontend
- **Модуль:** `modules/combat/loadouts/availability`
- **State Store:** `useLoadoutAvailabilityStore` (issues, replacements, warnings)
- **UI компоненты:** `AvailabilityStatusBadge`, `FallbackSuggestionPanel`, `DegradedModeBanner`, `ItemSuspensionTimeline`, `AvailabilityWarningsTable`, `BatchJobStatusCard`
- **Формы:** `FallbackSelectionForm`, `DegradedModeConsentForm`, `AvailabilityOverrideForm`
- **Хуки:** `useAvailabilityMonitor`, `useFallbackSuggestions`, `useDegradedMode`, `useAvailabilityFeed`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/gameplay/combat/loadout-availability*
# - Dependencies: inventory, economy, notification, analytics, scheduler, auth
# - Queues: combat.loadouts.recalculate, combat.loadouts.availability-warning, combat.loadouts.degraded
# - Frontend Module: modules/combat/loadouts/availability (useLoadoutAvailabilityStore)
# - UI: AvailabilityStatusBadge, FallbackSuggestionPanel, DegradedModeBanner, ItemSuspensionTimeline
# - Forms: FallbackSelectionForm, DegradedModeConsentForm, AvailabilityOverrideForm
# - Hooks: useAvailabilityMonitor, useFallbackSuggestions, useDegradedMode, useAvailabilityFeed
```

---

## ✅ Что нужно сделать (детальный план)

1. Проанализировать сценарии недоступности предметов и имплантов, деградации и fallback из документа `.BRAIN`.
2. Спроектировать REST endpoints для получения статуса доступности, планирования пересчётов, перевода в `degraded`, выбора fallback-комплектов, ручного override и работы с очередями.
3. Описать схемы `LoadoutAvailability`, `ItemAvailability`, `FallbackOption`, `DegradedMode`, `AvailabilityIssue`, `AvailabilityWarning`, `BatchJobStatus`, `AvailabilityOverride`.
4. Добавить endpoints для запуска batch recalculation, обработки live patch hook, управления очередями и просмотра истории пересчётов.
5. Спроектировать события (`loadout.availability.updated`, `loadout.availability.warning`, `loadout.availability.degraded`, `loadout.availability.recovered`) с payload и retry.
6. Прописать безопасность, аудит, idempotency, лимиты (например, максимальное число fallback-операций), связь с loadouts/kits/profiles/macro спецификациями.
7. Подготовить примеры запросов/ответов (проверка, деградация, fallback, предупреждение, batch job), описать коды ошибок (`409`, `423`, `451`).
8. Интегрировать метрики и аналитическую выгрузку (Prometheus, Parquet датасеты), описать REST/Async точки доставки.
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `GET /api/v1/gameplay/combat/loadout-availability` — агрегированный статус доступности по лодаутам (фильтры по роли, событию, режиму).
2. `GET /api/v1/gameplay/combat/loadout-availability/{loadoutId}` — детальный статус: suspended items, импланты, причины блокировки, предлагаемые fallback.
3. `POST /api/v1/gameplay/combat/loadout-availability/{loadoutId}/fallback` — выбор fallback-комплекта и автоматическая замена.
4. `POST /api/v1/gameplay/combat/loadout-availability/{loadoutId}/degraded/enter` — перевод в режим `degraded` (указать ограничения, доступные зоны).
5. `POST /api/v1/gameplay/combat/loadout-availability/{loadoutId}/degraded/exit` — выход из режима `degraded` после восстановления предметов.
6. `POST /api/v1/gameplay/combat/loadout-availability/{loadoutId}/override` — ручное подтверждение использования недоступного предмета (для админов/GM).
7. `GET /api/v1/gameplay/combat/loadout-availability/{loadoutId}/warnings` — история предупреждений, события, действия.
8. `POST /api/v1/gameplay/combat/loadout-availability/recalculate` — ручной запуск batch пересчёта (админ endpoint, idempotent).
9. `GET /api/v1/gameplay/combat/loadout-availability/batch-jobs` — статус очередей, прогресс пересчётов, ошибки.
10. `POST /api/v1/gameplay/combat/loadout-availability/live-patch` — обработка live patch hook после балансового апдейта (diffPreview, revision).
11. `GET /api/v1/gameplay/combat/loadout-availability/metrics` — агрегированные показатели (conflict rate, degraded sessions, recovery time).
12. `GET /api/v1/gameplay/combat/loadout-availability/feeds` — realtime feed предупреждений (SSE/WebSocket описание / AsyncAPI).

Все мутационные операции требуют `Authorization`, `Idempotency-Key`, `X-Audit-Id`; ответы используют общие `$ref`.

---

## 🧱 Модели данных

- **LoadoutAvailability** — `loadoutId`, `status` (`OK`, `WARNING`, `DEGRADED`, `BLOCKED`), `unavailableItems[]`, `unavailableImplants[]`, `fallbackSuggestions[]`, `degradedMode`, `lastCheck`, `nextCheck`, `conflictScore`.
- **ItemAvailability** — `itemId`, `type`, `reason` (`RENT_EXPIRED`, `SUSPENDED`, `BROKEN`, `UNAPPROVED`), `suspendedUntil`, `replacementOptions[]`.
- **FallbackOption** — `kitId`, `score`, `tradeoffs`, `requiresApproval`, `estimatedCost`.
- **DegradedMode** — `active`, `allowedZones[]`, `restrictions`, `expiresAt`, `initiatedBy`.
- **AvailabilityIssue** — `issueId`, `loadoutId`, `severity`, `category`, `detectedAt`, `resolvedAt`, `resolution`.
- **AvailabilityWarning** — `warningId`, `loadoutId`, `message`, `context`, `notificationChannels[]`, `acknowledgedBy`, `acknowledgedAt`.
- **BatchJobStatus** — `jobId`, `type` (`RECALCULATE`, `LIVE_PATCH`), `startedAt`, `finishedAt`, `progress`, `affectedLoadouts`, `errors[]`.
- **AvailabilityOverride** — `overrideId`, `loadoutId`, `itemId`, `approvedBy`, `reason`, `expiresAt`, `auditRef`.
- **AvailabilityMetric** — `date`, `conflictRate`, `avgRecoveryTime`, `degradedSessions`, `fallbackUsage`, `warningRate`.
- **Async Events** — payloads для `loadout.availability.updated`, `loadout.availability.warning`, `loadout.availability.degraded`, `loadout.availability.recovered`, `loadout.availability.override-applied`.

---

## 🧭 Принципы и правила

- Соблюдать OpenAPI 3.0.3, лимит 400 строк; повторяющиеся схемы вынести.
- Использовать `$ref` на общие компоненты и контракты loadouts/kits/profiles/macro/inventory.
- Учесть требования безопасности: scopes `loadouts:availability.read`, `loadouts:availability.write`, `loadouts:availability.override`, `loadouts:availability.admin`.
- Описать аудит и idempotency, ошибки `409`, `410`, `423`, `451`.
- Batch и live patch операции — idempotent, с поддержкой `revision`, `If-Match`, `jsonb_diff_patch`.
- События публикуются в `combat.loadouts.availability.*` с `correlationId`.
- Метрики и выгрузки описать через REST и Async (Prometheus scrape, parquet export).

---

## ✅ Критерии приемки

1. Все 12 эндпоинтов описаны с параметрами, схемами, примерами и кодами ошибок.
2. Режим `degraded` документирован (вход/выход, ограничения, взаимодействие с матчами).
3. Fallback-механика описана: подбор комплектов, ограничения, стоимость, аудит.
4. Batch пересчёт и live patch hook описаны с очередями, статусами и idempotency.
5. События `availability.*` описаны с payload, каналами, retry.
6. Метрики и выгрузки задокументированы (REST + Async).
7. Прописаны требования к безопасности, ролям, audit trail, `Idempotency-Key`.
8. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Все разделы шаблона заполнены, ссылки на `.BRAIN` и связанные API корректны.
- [ ] OpenAPI/AsyncAPI проходят lint, длина файла ≤400 строк (или части вынесены).
- [ ] Примеры покрывают сценарии: выпуск предупреждения, переход в `degraded`, fallback, batch пересчёт, live patch.
- [ ] События синхронизированы с notification и analytics сервисами.
- [ ] Архитектурный комментарий корректен.
- [ ] Инструкции по обновлению `brain-mapping.yaml` и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Что происходит, если игрок игнорирует предупреждение?  
**A:** После истечения `gracePeriod` система переводит лодаут в `degraded` и отправляет событие `loadout.availability.degraded`. Заблокированные зоны закрываются до устранения проблемы.

**Q:** Можно ли принудительно разрешить использование недоступного предмета?  
**A:** Только через `override` с `GM` разрешением. Операция журналируется, событие `loadout.availability.override-applied` уведомляет аналитику и службу безопасности.

**Q:** Как часто выполняется пересчёт доступности?  
**A:** Плановый пересчёт запускается nightly через batch job, возможно ручное триггерирование. Результаты доступны в `batch-jobs` и метриках.

---

## 🔗 Связность и последующие шаги

- Добавить запись в `tasks/config/brain-mapping.yaml` и обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (добавить API-TASK-304).
- Согласовать спецификацию с заданиями loadouts/kits/profiles/macro и inventory.
- При необходимости подготовить задачи для backend обслуживания очередей и frontend UI уведомлений.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

