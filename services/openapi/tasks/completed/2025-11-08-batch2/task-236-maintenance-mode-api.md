# Task ID: API-TASK-236
**Тип:** API Generation
**Приоритет:** критический
**Статус:** completed
**Создано:** 2025-11-08 06:35
**Завершено:** 2025-11-08 21:25
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-230, API-TASK-223, API-TASK-224

## 📦 Результат

- Добавлены спецификации `maintenance-mode.yaml`, `maintenance-components.yaml`, `maintenance-examples.yaml` (REST + WS, <400 строк каждая).
- Описаны процессы планового/экстренного обслуживания, уведомления, graceful shutdown, интеграции с DevOps/incident/status-page.
- Перенесено задание в completed, обновлены `brain-mapping.yaml` и `.BRAIN/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Разработать OpenAPI спецификацию системы режима обслуживания: планирование и управление maintenance окнами, уведомления игроков, graceful shutdown, мониторинг статуса и интеграция с инфраструктурой.

**Что нужно сделать:** Создать `api/v1/system/maintenance/maintenance-mode.yaml`, описав REST/WS контракты по `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md`.

---

## 🎯 Цель задания

Обеспечить централизованный сервис управления обслуживанием, минимизирующий простои и информирующий игроков и администраторов.

**Зачем это нужно:**
- Планировать и объявлять окна обслуживания
- Выполнять плавное выключение сервисов и обработку сессий
- Уведомлять игроков, гильдии и администрацию
- Отслеживать статус и прогресс maintenance в реальном времени
- Предоставлять API для DevOps, GM и внешних статусов

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md`
**Версия:** v1.0.0 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Scheduled vs Emergency maintenance, workflow, approvals
- Graceful shutdown: уведомления, session draining, queue blocking
- Player-facing status page, notifications, countdowns
- Admin control panel, audit, rollback план
- Integration с DevOps инструментами, health checks

### Дополнительные источники

- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/session-management/README.md`
- `.BRAIN/05-technical/backend/clan-war/clan-war-system.md`
- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-230-notification-system-api.md`
- `API-SWAGGER/tasks/active/queue/task-223-clan-war-system-api.md`
- `API-SWAGGER/tasks/active/queue/task-224-progression-backend-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/system/maintenance/maintenance-mode.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/system/maintenance/
 ├── maintenance-mode.yaml        ← создать/обновить
 ├── maintenance-components.yaml
 └── maintenance-examples.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** system-service (maintenance module) или ops-service
- **Порт:** 8098
- **API Base Path:** `/api/v1/system/maintenance`
- **Зависимости:**
  - notification-service – уведомления и статусные рассылки
  - session-service – управление игроками (kick, grace period)
  - realtime-service – блокировка подключений, оповещения
  - auth-service – валидация ролей (DevOps, GM)
  - analytics-service – статистика по простою, SLA
  - incident-service – фиксация аварийных работ
  - deployment-service/CI – триггеры rollout (если имеется)
  - status-page-service – публичный статус (если отдельно)

### Frontend
- **Модуль:** `modules/system/maintenance`
- **State Store:** `useMaintenanceStore`
- **State:** `upcomingWindows`, `activeMaintenance`, `status`, `notifications`, `auditLogs`
- **UI компоненты:** `MaintenanceDashboard`, `MaintenanceScheduleTable`, `MaintenanceCountdown`, `PlayerNotificationBanner`, `MaintenanceStatusCard`, `MaintenanceAuditLog`
- **Формы:** `ScheduleMaintenanceForm`, `EmergencyMaintenanceForm`, `MaintenanceNotificationForm`, `MaintenanceRollbackForm`
- **Хуки:** `useMaintenance`, `useMaintenanceStatus`, `useMaintenanceNotifications`, `useMaintenanceAudit`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: system-service (maintenance module, port 8098)
# - API Base: /api/v1/system/maintenance
# - Dependencies: notification, session, realtime, auth, analytics, incident, deployment/status-page
# - Frontend Module: modules/system/maintenance (useMaintenanceStore)
# - UI: MaintenanceDashboard, MaintenanceScheduleTable, MaintenanceCountdown, PlayerNotificationBanner, MaintenanceStatusCard, MaintenanceAuditLog
# - Forms: ScheduleMaintenanceForm, EmergencyMaintenanceForm, MaintenanceNotificationForm, MaintenanceRollbackForm
# - Hooks: useMaintenance, useMaintenanceStatus, useMaintenanceNotifications, useMaintenanceAudit
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать модели maintenance windows, статуса, уведомлений, прогресса, аудита.
2. Реализовать API планирования, запуска, обновления и завершения maintenance.
3. Добавить контроль graceful shutdown: player drain, queue lock, service states.
4. Описать уведомления игроков (email/push/in-game), кланов, админов.
5. Реализовать интеграцию с status-page, DevOps hooks, deployment pipelines.
6. Добавить мониторинг активного maintenance: прогресс, остаток времени, SLA.
7. Поддержать emergency flow, rollback, post-mortem attachments.
8. Настроить WebSocket события для UI и внешних сервисов.
9. Подготовить примеры, тест-кейсы, чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/system/maintenance/windows`** – список запланированных окон (фильтры по статусу, среде, зоне).
2. **POST `/api/v1/system/maintenance/windows`** – создание нового окна (schedule, scope, уведомления).
3. **GET `/api/v1/system/maintenance/windows/{windowId}`** – детали окна, затронутые сервисы, прогресс.
4. **PATCH `/api/v1/system/maintenance/windows/{windowId}`** – обновление времени, комментариев, уведомлений.
5. **POST `/api/v1/system/maintenance/windows/{windowId}/activate`** – запуск maintenance (переход в статус `IN_PROGRESS`).
6. **POST `/api/v1/system/maintenance/windows/{windowId}/complete`** – завершение, публикация отчёта, рассылка.
7. **POST `/api/v1/system/maintenance/windows/{windowId}/cancel`** – отмена с уведомлениями.
8. **POST `/api/v1/system/maintenance/windows/{windowId}/rollback`** – откат и восстановление сервиса.
9. **POST `/api/v1/system/maintenance/windows/{windowId}/notifications`** – ручной запуск уведомлений (внутриигровых, email, push).
10. **GET `/api/v1/system/maintenance/active`** – текущее состояние (если maintenance активен) с прогрессом.
11. **POST `/api/v1/system/maintenance/active/pause`** – пауза maintenance (если поддерживается).
12. **POST `/api/v1/system/maintenance/active/resume`** – возобновление.
13. **POST `/api/v1/system/maintenance/active/escalate`** – перевод в emergency режим.
14. **GET `/api/v1/system/maintenance/audit`** – аудит действий, изменения расписания, результаты, SLA.
15. **POST `/api/v1/system/maintenance/audit`** – добавление пост-мортема, отчётов, приложений.
16. **GET `/api/v1/system/maintenance/status`** – статусы сервисов, доступные для публичного статус-пейджа.
17. **POST `/api/v1/system/maintenance/status`** – обновление ручного статуса (override).
18. **POST `/api/v1/system/maintenance/hooks/deployment`** – интеграция с CI/CD (триггер deployment, freeze).
19. **POST `/api/v1/system/maintenance/hooks/incident`** – связь с incident-service (create incident, update severity).
20. **WS `/api/v1/system/maintenance/stream`** – события: `maintenance-scheduled`, `maintenance-started`, `maintenance-progress`, `maintenance-completed`, `maintenance-cancelled`, `maintenance-escalated`, `maintenance-notification`.

---

## 🧱 Модели данных

- **MaintenanceWindow** – `windowId`, `title`, `description`, `type` (`SCHEDULED|EMERGENCY`), `environment`, `zones`, `startAt`, `endAt`, `expectedDuration`, `status`, `createdBy`.
- **MaintenanceStatus** – `status`, `progressPercent`, `affectedServices`, `playerCount`, `sessionDrain`, `updatedAt`.
- **NotificationPlan** – `channels` (`IN_GAME|EMAIL|PUSH|STATUS_PAGE`), `templates`, `targets` (player segments, guilds), `schedule`.
- **ShutdownPlan** – `gracePeriod`, `drainSteps`, `queueLock`, `forceKickAt`, `checks`.
- **MaintenanceAuditEntry** – `entryId`, `windowId`, `actor`, `action`, `details`, `timestamp`, `attachments`.
- **IntegrationHook** – `hookId`, `type` (`DEPLOYMENT|INCIDENT|STATUS_PAGE`), `url`, `secret`, `enabled`.
- **RealtimeEventPayload** – `maintenanceScheduled`, `maintenanceStarted`, `maintenanceProgress`, `maintenanceCompleted`, `maintenanceCancelled`, `maintenanceEscalated`, `maintenanceNotification`.
- **Error Schema (`MaintenanceError`)** – codes (`WINDOW_NOT_FOUND`, `INVALID_SCHEDULE`, `CONFLICTING_WINDOW`, `NOT_AUTHORIZED`, `NOT_ACTIVE`, `HOOK_FAILED`, `ROLLBACK_FAILED`, `AUDIT_REQUIRED`).

---

## 🧭 Принципы и правила

- Авторизация: только DevOps/GM с нужными ролями; игроки получают read-only статус.
- Безопасность: защищать от несанкционированного выключения; логировать каждое действие.
- Graceful Shutdown: обеспечить корректный drain сессий, лимиты на force kick.
- Уведомления: многоуровневая рассылка (T-24h, T-1h, T-5m, start, complete); поддерживать локализацию.
- SLA & Audit: хранить историю, расчёт простоя, экспорт в аналитические сервисы.
- Rollback: предусматривать автоматический и ручной сценарий; фиксировать метрики.

---

## 🧪 Примеры

- Плановое обслуживание на 2 часа с уведомлениями (in-game + email) и отслеживанием прогресса.
- Экстренное отключение с переводом окна в emergency и отправкой сообщения гильд-лидерам.
- Подключение status-page и публикация публичного статуса.
- Завершение maintenance с генерацией отчёта и экспортом в incident-service.
- WebSocket событие `maintenance-progress` для live dashboard.

---

## 🔗 Связности и зависимости

- Интеграция с notification, session management, realtime, clan war (заморозка событий), progression (отложенные награды).
- Используется admin UI (`MaintenanceDashboard`) и status-page.
- Взаимодействует с DevOps (deployment hooks) и incident management.

---

## ✅ Критерии приемки

1. `maintenance-mode.yaml` описывает планирование, управление, уведомления, audit.
2. Модели покрывают окна, статус, уведомления, интеграции.
3. Прописаны события, авторизация, rollback/emergency сценарии.
4. Примеры и тест-кейсы подготовлены, чеклист выполнен.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, UI модуль, зависимости
- [ ] Эндпоинты и события покрывают scheduling, execution, rollback
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как обрабатывать перекрывающиеся окна обслуживания?**
**A:** Проверять конфликты по сервисам/зонам; возвращать `CONFLICTING_WINDOW`; предусмотреть merge/override через PATCH.

**Q:** Нужен ли публичный read-only API?**
**A:** Да, endpoints `/status` и WebSocket stream должны поддерживать публичные токены/ограниченные ключи для status-page.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

