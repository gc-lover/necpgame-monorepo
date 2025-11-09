# Task ID: API-TASK-306
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:28
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-301], [API-TASK-304], [API-TASK-128], [API-TASK-190]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию административной подсистемы обслуживания боевых лодаутов (Combat Loadout Maintenance) для `admin-service`: управление batch очередями, live патчами, конфликтами версий и аудитом ре-балансировки.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для запуска пересчётов, наблюдения за очередями, предпросмотра диффов, разрешения конфликтов и уведомления заинтересованных сервисов.

---

## 🎯 Цель задания

Обеспечить геймдизайнерам и администраторам инструменты для безопасного и прозрачного обновления лодаутов после балансных патчей, минимизируя простои и конфликты.

**Зачем это нужно:**
- Централизованно запускать batch обновления веса комплектов и проверок совместимости.
- Управлять live патчами с предпросмотром (`diffPreview`) и контролем версий (`revision`).
- Мониторить очереди, ошибки и автоматически уведомлять игроков/аналитику.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Раздел «Очереди обновлений и масштабирование» — batch очередь `combat.loadouts.recalculate`, live patch hook, `diffPreview`, `revision`, `jsonb_diff_patch`, shard по `characterId`.
- Раздел «Управление недоступными предметами» — связанный репортинг и `combat.loadouts.availability-warning`.
- Раздел «Метрики и телеметрия» — показатели для мониторинга последствий патчей.
- Упоминание Kafka/Redis Streams, audit trail и интеграции с нотификациями.

### Дополнительные источники

- `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md` — общие правила обслуживания.
- `.BRAIN/05-technical/backend/notification-system.md` — уведомления администраторов и игроков.
- `.BRAIN/05-technical/backend/daily-weekly-reset-system.md` — синхронизация с расписанием.
- `.BRAIN/02-gameplay/combat/arena-system.md`, `loot-hunt-system.md` — проверка последствий патчей в режимах.

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-301-combat-loadout-kits-api.md`
- `API-SWAGGER/tasks/active/queue/task-304-combat-loadout-availability-api.md`
- `API-SWAGGER/tasks/active/queue/task-190-analytics-reporting-api.md`
- `API-SWAGGER/tasks/active/queue/task-298-daily-reset-operations-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/admin/combat/loadout-maintenance.yaml`  
**Формат:** OpenAPI 3.0.3 (с выносом компонентов/событий при необходимости)

```
API-SWAGGER/
└── api/
    └── v1/
        └── admin/
            └── combat/
                ├── loadout-maintenance.yaml           ← создать
                ├── loadout-maintenance-components.yaml
                └── loadout-maintenance-events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** admin-service
- **Порт:** 8088
- **API Base:** `/api/v1/admin/combat/loadouts*`
- **Интеграции:** gameplay-service (пересчёт и валидация), scheduler-service (ночные задания), notification-service (уведомления), analytics-service (метрики последствий), auth-service (RBAC, scopes `admin:loadouts.*`).
- **Очереди:** Redis Streams/Kafka `combat.loadouts.recalculate`, `combat.loadouts.patch`, `combat.loadouts.conflict`.

### Frontend
- **Модуль:** `modules/admin/combat/loadouts-maintenance`
- **State Store:** `useLoadoutMaintenanceStore`
- **UI компоненты:** `MaintenanceQueueDashboard`, `PatchDiffViewer`, `ConflictResolutionPanel`, `JobProgressTimeline`, `ShardHealthStatus`, `NotificationRuleForm`
- **Формы:** `RecalculateRequestForm`, `LivePatchForm`, `ConflictOverrideForm`
- **Хуки:** `useMaintenanceQueue`, `usePatchPreview`, `useConflictResolver`, `useMaintenanceNotifications`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: admin-service (port 8088)
# - API Base: /api/v1/admin/combat/loadouts*
# - Dependencies: gameplay, scheduler, notification, analytics, auth
# - Queues: combat.loadouts.recalculate, combat.loadouts.patch, combat.loadouts.conflict
# - Frontend Module: modules/admin/combat/loadouts-maintenance (useLoadoutMaintenanceStore)
# - UI: MaintenanceQueueDashboard, PatchDiffViewer, ConflictResolutionPanel, JobProgressTimeline
# - Forms: RecalculateRequestForm, LivePatchForm, ConflictOverrideForm
# - Hooks: useMaintenanceQueue, usePatchPreview, useConflictResolver, useMaintenanceNotifications
```

---

## ✅ Что нужно сделать (детальный план)

1. Извлечь из документа сценарии batch пересчёта, live патча и разрешения конфликтов; определить необходимые данные и шаги.
2. Спроектировать REST endpoints для запуска/остановки заданий, просмотра статуса очередей, предпросмотра диффов, применения патчей и управления конфликтами (`revision` / `ETag`).
3. Описать схемы `MaintenanceJob`, `PatchPreview`, `LoadoutDiff`, `ShardStatus`, `ConflictTicket`, `ResolutionAction`, `NotificationRule`, `QueueLag`.
4. Добавить endpoints для настройки расписаний, перезапуска шардов, экспорта логов и интеграции с analytics (отправка результатов).
5. Определить события (`loadout.maintenance.job-started`, `job-completed`, `job-failed`, `patch-applied`, `conflict-detected`, `conflict-resolved`) с payload и гарантией доставки.
6. Прописать безопасность (RBAC: `admin`, `designer`), аудит, idempotency и rate limits (чтобы не спамить патчами).
7. Подготовить примеры запросов/ответов/событий (batch запуск, live patch preview, конфликт, override).
8. Связать спецификацию с loadout availability/telemetry для автоматических уведомлений и метрик.
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `POST /api/v1/admin/combat/loadouts/jobs/recalculate` — запуск batch пересчёта (параметры: scope, приоритет, dryRun).
2. `GET /api/v1/admin/combat/loadouts/jobs` — мониторинг запущенных/исторических заданий (пагинация, фильтры).
3. `GET /api/v1/admin/combat/loadouts/jobs/{jobId}` — подробный статус, shard прогресс, ошибки.
4. `POST /api/v1/admin/combat/loadouts/jobs/{jobId}/cancel` — отмена задания (idempotent).
5. `POST /api/v1/admin/combat/loadouts/live-patch/preview` — генерация `diffPreview` на основе входного патча.
6. `POST /api/v1/admin/combat/loadouts/live-patch/apply` — применение live патча с проверкой `revision` и `If-Match`.
7. `GET /api/v1/admin/combat/loadouts/conflicts` — список конфликтов (`revision`, `jsonb_diff_patch` результаты).
8. `POST /api/v1/admin/combat/loadouts/conflicts/{conflictId}/resolve` — принятие решения (merge, rollback, override).
9. `GET /api/v1/admin/combat/loadouts/shards` — состояние шардов, лаги, перезапуски.
10. `POST /api/v1/admin/combat/loadouts/shards/{shardId}/restart` — мягкий перезапуск воркера.
11. `GET /api/v1/admin/combat/loadouts/logs/export` — экспорт логов обслуживания (фильтры, формат).
12. `POST /api/v1/admin/combat/loadouts/notifications` — настройка уведомлений (правила, каналы).
13. `GET /api/v1/admin/combat/loadouts/notifications` — список действующих правил и подписчиков.
14. `GET /api/v1/admin/combat/loadouts/metrics` — метрики обслуживания (среднее время, ошибки, лаги).

Все мутационные операции требуют `Authorization`, `Idempotency-Key`, `X-Audit-Id`, и используют общие `$ref` для ответов/ошибок.

---

## 🧱 Модели данных

- **MaintenanceJob** — `jobId`, `type` (`RECALCULATE`, `LIVE_PATCH`), `status`, `initiator`, `createdAt`, `startedAt`, `finishedAt`, `scope`, `dryRun`, `retryCount`.
- **ShardStatus** — `shardId`, `state`, `lastHeartbeat`, `processed`, `lag`, `errors`, `restartCount`.
- **PatchPreview** — `patchId`, `diff`, `affectedLoadouts`, `riskScore`, `estimatedDuration`.
- **LoadoutDiff** — `before`, `after`, `breakingChanges`, `fallbackImpact`, `macroImpact`.
- **ConflictTicket** — `conflictId`, `loadoutId`, `revision`, `conflictType`, `detectedAt`, `assignedTo`, `status`, `resolution`.
- **ResolutionAction** — `action`, `appliedBy`, `appliedAt`, `notes`, `rollbackToken`.
- **NotificationRule** — `ruleId`, `event`, `threshold`, `channels[]`, `recipients[]`, `enabled`.
- **QueueLag** — `queue`, `lagMs`, `depth`, `oldestEvent`, `shardBreakdown`.
- **MaintenanceMetric** — `time`, `jobsRun`, `avgDuration`, `failureRate`, `conflictsResolved`, `notificationsSent`.
- **Async Events** — payloads для `loadout.maintenance.job-started`, `job-updated`, `job-completed`, `job-failed`, `patch-applied`, `conflict-detected`, `conflict-resolved`.

---

## 🧭 Принципы и правила

- Использовать OpenAPI 3.0.3 и при необходимости AsyncAPI, соблюдать лимит 400 строк (вынести компоненты/события).
- Поддерживать строгий RBAC: роли `admin`, `designer`, `observer`; прописать scopes и audit trail.
- Все операции должны быть идемпотентными; повторные запросы с тем же `Idempotency-Key` возвращают одинаковый результат.
- Учитывать шардирование (ключ `characterId`) и предоставлять диагностику.
- Публиковать события в `combat.loadouts.maintenance.*` с `correlationId`, `causationId`.
- Интегрироваться с notification/analytics/availability, использовать `$ref` на соответствующие схемы.
- Документировать обработку ошибок (`409`, `423`, `425`, `500`) и механизмы rollback через `rollbackToken`.

---

## ✅ Критерии приемки

1. Все 14 эндпоинтов описаны с параметрами, схемами и примерами запросов/ответов.
2. Live patch workflow включает preview, apply, rollback с проверкой `revision`.
3. Поддержка очередей и шардов задокументирована (мониторинг, перезапуск, лаги).
4. Конфликты описаны с типами, шагами разрешения, событиями и аудитом.
5. Интеграция с notification/analytics/availability отражена в схемах и событиях.
6. Метрики обслуживания предоставлены (REST + события) с примерами.
7. Security/RBAC, idempotency, audit и rate limiting описаны и согласованы.
8. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Все разделы шаблона заполнены и согласованы с `.BRAIN`.
- [ ] OpenAPI/AsyncAPI проходит lint; при превышении 400 строк вынесены компоненты.
- [ ] Примеры покрывают batch запуск, live patch, конфликт, уведомление, метрики.
- [ ] События синхронизированы с notification/analytics.
- [ ] Архитектурный комментарий корректен.
- [ ] Инструкции по обновлению mapping и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Как безопасно откатить неудачный патч?  
**A:** Через `rollbackToken`, возвращаемый при `PATCH apply`. Endpoint `conflicts/{id}/resolve` с действием `ROLLBACK` восстанавливает предыдущую версию и публикует событие `loadout.maintenance.patch-rolled-back`.

**Q:** Можно ли запускать несколько batch заданий одновременно?  
**A:** Да, но сервис должен управлять приоритетами и шардированием. Указать ограничения и возвращать `409 JOB_ALREADY_RUNNING`, если конфликт по scope.

**Q:** Как информируются игроки об изменениях?  
**A:** Notification-service подписывается на события `loadout.maintenance.job-completed` и `loadout.maintenance.patch-applied`, после чего рассылает информацию игрокам/кланам.

---

## 🔗 Связность и последующие шаги

- Добавить запись в `tasks/config/brain-mapping.yaml`, обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (API-TASK-306).
- Согласовать спецификацию с availability, telemetry и notification API.
- Далее можно инициировать задачи для UI панели обслуживания и автоматизированных тестов шардов.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

