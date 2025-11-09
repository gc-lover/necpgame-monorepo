# Task ID: API-TASK-298
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 22:55
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-141], [API-TASK-190], [API-TASK-183]

---

## 📋 Краткое описание

Спроектировать административный REST/Async API для управления ежедневными и еженедельными сбросами (daily/weekly reset jobs), включая расписание Cron, ручные триггеры, мониторинг и уведомления.

**Что нужно сделать:** На основе `.BRAIN/05-technical/backend/daily-reset/daily-reset-compact.md` и расширенного описания в `daily-weekly-reset-system.md` создать спецификацию admin-service, позволяющую DevOps/GM-команде контролировать reset-процессы, просматривать логи выполнения, объявлять окна обслуживания и координировать нотификации.

---

## 🎯 Цель задания

Обеспечить прозрачность и управляемость автоматических сбросов через административный интерфейс.

**Зачем это нужно:**
- “Одно окно” для мониторинга расписания и статуса ежедневных/еженедельных задач.
- Возможность вручную инициировать, откатить или отложить сбросы при инцидентах.
- Интеграция с уведомлениями и аналитикой для SLA, чтобы заранее предупреждать команды и игроков.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/05-technical/backend/daily-reset/daily-reset-compact.md`  
**Версия:** 1.0.0  
**Дата:** 2025-11-07 02:18  
**Статус:** approved / api-readiness: ready  

**Что важно:**
- Список обязанных сбросов: daily quests, weekly raids, shop refresh, reputation decay.
- Время выполнения: daily 00:00, weekly Monday 00:00 (UTC).
- Технический акцент на Cron jobs и периодичности.

### Дополнительные источники

- `.BRAIN/05-technical/backend/daily-weekly-reset-system.md` — детальный процесс, события, связи с другими сервисами.
- `.BRAIN/05-technical/backend/notification-system.md` — рассылка уведомлений после reset.
- `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md` — окна обслуживания (приостановка reset).
- `.BRAIN/05-technical/backend/realtime-server/part2-protocol-optimization.md` — лаг и синхронизация (учёт при ручных триггерах).
- `.BRAIN/05-technical/backend/achievement/achievement-tracking.md` — сброс достижений/лимитов, которые зависят от расписания.

### Связанные задания

- `API-TASK-141` — основная спецификация reset-системы для игровых сервисов.
- `API-TASK-190` — аналитика/отчётность (фиксировать SLA и метрики).
- `API-TASK-183` — стратегия кэширования (обновление кэшей после сброса).

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевые файлы:**
- `api/v1/admin/reset/reset-operations.yaml` — основной REST/Async контракт (≤400 строк).
- `api/v1/admin/reset/schemas/reset-components.yaml` — схемы, enum, общие объекты.
- `api/v1/admin/reset/events/reset-notifications.yaml` — события/уведомления (если требуется вынести).

```
API-SWAGGER/
└── api/
    └── v1/
        └── admin/
            └── reset/
                ├── reset-operations.yaml         ← создать
                ├── schemas/
                │   └── reset-components.yaml     ← создать
                └── events/
                    └── reset-notifications.yaml  ← создать (если файл >400 строк)
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** admin-service
- **Порт:** 8088
- **API Base:** `/api/v1/admin/reset/*`
- **Ответственность:** управление расписанием, ручные триггеры, остановка/возобновление, логи, уведомления.
- **Интеграции:** world-service (основной исполнитель), gameplay-service, economy-service, social-service, notification-service, analytics-service, maintenance-service.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/admin/reset-operations`
- **State Store:** `useResetOpsStore`
- **State:** `schedules`, `nextRuns`, `executionLogs`, `pendingOverrides`, `maintenanceWindows`
- **UI компоненты:** `ResetScheduleTable`, `ResetCountdownCard`, `ExecutionLogTimeline`, `AlertSubscriptionPanel`, `MaintenanceBanner`, `ManualTriggerDialog`
- **Формы:** `ScheduleUpdateForm`, `ManualTriggerForm`, `MaintenanceWindowForm`, `NotificationConfigForm`
- **Хуки:** `useResetTelemetry`, `useResetOverrides`, `useMaintenanceScheduler`, `useNotificationBindings`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: admin-service (port 8088)
# - API Base: /api/v1/admin/reset/*
# - Dependencies: world, gameplay, economy, social, notification, analytics, maintenance
# - Frontend Module: modules/admin/reset-operations (useResetOpsStore)
# - UI: ResetScheduleTable, ResetCountdownCard, ExecutionLogTimeline, AlertSubscriptionPanel, MaintenanceBanner, ManualTriggerDialog
# - Forms: ScheduleUpdateForm, ManualTriggerForm, MaintenanceWindowForm, NotificationConfigForm
# - Hooks: useResetTelemetry, useResetOverrides, useMaintenanceScheduler, useNotificationBindings
```

---

## ✅ Что нужно сделать (детальный план)

1. Собрать из источников список сбросов, расписаний, зависимостей и SLA.
2. Спроектировать REST методы для чтения/изменения расписания, ручного запуска, паузы, управления окнами обслуживания, просмотра логов.
3. Добавить endpoints для управления подписками на уведомления (email/webhook/ingame) и настройки оповещений за N минут до сброса.
4. Определить модели данных (Schedule, ResetJob, ExecutionLog, ManualTriggerRequest, MaintenanceWindow, NotificationBinding, ResetScope).
5. Описать события (AsyncAPI или раздел events): `reset.job.scheduled`, `reset.job.executed`, `reset.job.failed`, `reset.maintenance.updated`.
6. Продумать безопасность: роли `reset:read`, `reset:manage`, `reset:trigger`, `reset:maintenance`.
7. Добавить примеры запросов/ответов (≥70%), указать использование `shared/common` компонентов, `Idempotency-Key` и `X-Audit-Id` для мутаций.
8. Заполнить чеклист, критерии приемки, FAQ, добавить инструкции по обновлению mapping и `.BRAIN` документа.

---

## 🔀 Требуемые эндпоинты

1. `GET /api/v1/admin/reset/schedule` — текущая конфигурация расписания (daily, weekly, monthly), таймзона, cron выражения.
2. `PATCH /api/v1/admin/reset/schedule` — обновление расписания (изменить время, временно отключить job, сменить cron).
3. `POST /api/v1/admin/reset/jobs/{jobType}/trigger` — ручной запуск (`jobType`: `daily`, `weekly`, `monthly`, `custom`), с опциями `dryRun`, `notify`.
4. `POST /api/v1/admin/reset/jobs/{jobType}/replay` — переисполнение последнего job в случае отката.
5. `GET /api/v1/admin/reset/history` — список последних выполнений (status, duration, affected services, anomalies).
6. `GET /api/v1/admin/reset/jobs/{jobId}` — детали исполнения (логи, затронутые сервисы, publish события).
7. `POST /api/v1/admin/reset/overrides` — создание override (отложить/пропустить следующий reset, указать причину, approvedBy).
8. `DELETE /api/v1/admin/reset/overrides/{overrideId}` — отмена override.
9. `GET /api/v1/admin/reset/maintenance` — активные/запланированные окна обслуживания.
10. `POST /api/v1/admin/reset/maintenance` — добавить окно обслуживания (`startAt`, `endAt`, `scope`, `reason`).
11. `PATCH /api/v1/admin/reset/maintenance/{maintenanceId}` — изменить окно (продлить, завершить досрочно).
12. `GET /api/v1/admin/reset/notifications` — подписки на уведомления по сбросам (email/webhook/realtime).
13. `POST /api/v1/admin/reset/notifications` — добавить/обновить подписку (канал, получатель, leadTime, filters).
14. `DELETE /api/v1/admin/reset/notifications/{subscriptionId}` — удалить подписку.
15. `GET /api/v1/admin/reset/metrics` — SLA: время выполнения, задержки, процент успеха, количество ручных триггеров, пропущенные сбросы.

Все методы использовать `shared/common/responses.yaml`, `shared/common/pagination.yaml`. Для POST/PATCH — `Idempotency-Key`, `X-Audit-Id`.

---

## 🧱 Модели данных

- **ResetSchedule** — `dailyCron`, `weeklyCron`, `monthlyCron`, `timezone`, `enabled`, `nextRuns[]`, `lastRunAt`.
- **ResetJobDefinition** — `jobType`, `description`, `scopes[]`, `enabled`, `dependencies[]`.
- **ResetExecutionLog** — `jobId`, `jobType`, `startedAt`, `finishedAt`, `duration`, `status`, `initiator`, `affectedServices[]`, `publishedEvents[]`, `alerts[]`.
- **ManualTriggerRequest** — `jobType`, `reason`, `initiator`, `dryRun`, `notifyScopes[]`.
- **OverrideRequest** — `overrideId`, `jobType`, `action` (`SKIP`, `DELAY`, `CUSTOM_TIME`), `scheduledFor`, `reason`, `createdBy`, `status`.
- **MaintenanceWindow** — `maintenanceId`, `scope`, `startAt`, `endAt`, `status`, `reason`, `createdBy`, `notifyPlayers`.
- **NotificationSubscription** — `subscriptionId`, `channel` (`email`, `webhook`, `ingame`), `target`, `leadTimeMinutes`, `scopes[]`, `createdAt`.
- **ResetMetricSnapshot** — `timestamp`, `jobType`, `avgDuration`, `successRate`, `lateExecutions`, `manualTriggers`, `skippedJobs`.
- **ResetScope** (enum) — `DAILY_QUESTS`, `WEEKLY_RAIDS`, `SHOP_REFRESH`, `REPUTATION`, `LOGIN_REWARDS`, `CUSTOM`.
- **ResetAlert** — `alertId`, `level`, `message`, `createdAt`, `linkedJobId`.
- **Async Events Payloads** — описать в events файле (`ResetJobScheduled`, `ResetJobExecuted`, `ResetJobFailed`, `MaintenanceWindowUpdated`).

---

## 🧭 Принципы и правила

- Роли: `reset:read`, `reset:manage`, `reset:trigger`, `reset:maintenance`, `reset:notify`.
- Каждый мутационный endpoint требует `Idempotency-Key`, `X-Audit-Id`.
- В `ManualTrigger` предусмотреть флаг `dryRun` (только проверить без уведомлений).
- Сервис должен публиковать события в notification-service и analytics-service (описать payload и SLA).
- Учитывать maintenance-mode: если запланировано окно, job не должен стартовать автоматически без override.
- Обязать вести историю override и ручных триггеров (для compliance).
- Обозначить лимиты по rate limiting (например, не более 3 ручных триггеров за час).
- Указывать timezone UTC по умолчанию, но позволять override с явным указанием timezone.
- Разделить схемы/события на вспомогательные файлы, если основной файл приближается к 400 строк.

---

## ✅ Критерии приемки (минимум 10)

1. Все 15 эндпоинтов описаны с параметрами, примерами запросов/ответов.
2. Схемы вынесены в `reset-components.yaml`, повторно используются через `$ref`.
3. Async события задокументированы с payload, каналами и примерами.
4. Используются стандартные компоненты (`responses.yaml`, `pagination.yaml`, `security.yaml`).
5. Для всех POST/PATCH/DELETE указаны `Idempotency-Key` и `X-Audit-Id`.
6. Описаны роли и scopes безопасности (`reset:*`).
7. Примеры покрывают ≥70% эндпоинтов (расписание, override, ручной триггер, уведомления).
8. Указаны SLA и метрики (lead time, execution delay) в description/schema.
9. Учтены maintenance окна и их влияние на job execution.
10. `info.description` содержит ссылки на оба `.BRAIN` документа с версиями.
11. `x-target-architecture` комментарий присутствует в основной спецификации.
12. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN` документа.

---

## 📎 Checklist перед сдачей

- [ ] Пройдены все шаги плана, ссылки на `.BRAIN` добавлены.
- [ ] Эндпоинты/события/схемы оформлены по стандартам, размер файла ≤400 строк.
- [ ] Безопасность, аудит, idempotency описаны.
- [ ] Примеры запросов/ответов добавлены.
- [ ] Обновить `tasks/config/brain-mapping.yaml` и `.BRAIN/05-technical/backend/daily-reset/daily-reset-compact.md` после завершения.

---

## ❓ FAQ

**Q:** Нужно ли поддерживать разные часовые пояса инвесторов/регионов?  
**A:** В API хранить базовое расписание в UTC. Для отдельных регионов использовать overrides с timezone, но все события публикуются в UTC, чтобы сервисы синхронизировались.

**Q:** Что если reset провалился?  
**A:** Через `ExecutionLog` фиксировать статус `FAILED`, автоматически публиковать событие `reset.job.failed` (описать в events) и уведомлять ответственных. Предусмотреть ручной `replay`.

**Q:** Как координировать с maintenance-mode?  
**A:** Использовать endpoints maintenance subsystem: при создании окна обслуживания reset jobs автоматически переходят в `paused` до завершения окна. Override обязателен, чтобы запустить job во время maintenance.

---

## 🔗 Связность и дальнейшие шаги

- После генерации спецификации обновить mapping и документ `.BRAIN/05-technical/backend/daily-reset/daily-reset-compact.md`.
- Координироваться с задачами `API-TASK-141`, `API-TASK-190`, `API-TASK-183` — при изменении расписаний корректировать связанные API.
- Подготовить будущие задания для frontend/ops (dashboard) после утверждения спецификации.


