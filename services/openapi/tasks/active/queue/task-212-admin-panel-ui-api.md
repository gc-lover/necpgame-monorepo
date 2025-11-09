# Task ID: API-TASK-212
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:12
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-182, API-TASK-200

---

## 📋 Краткое описание

Создать UI-ориентированный API `admin-panel-ui` для модераторов/администраторов, агрегирующий метрики, управление игроками и инструменты модерации.

**Что нужно сделать:** Подготовить `api/v1/admin/panel/admin-panel-ui.yaml`, описав REST и WebSocket контракты для дашборда, управления игроками, модерации, real-time метрик и команд управления миром.

---

## 🎯 Цель задания

Предоставить фронтенду админ-панели готовые DTO и realtime события, минимизируя количество запросов к множеству сервисов и обеспечивая безопасный доступ.

**Зачем это нужно:**
- Реализовать UI, описанный в `.BRAIN` документе (dashboard, player management, moderation tools)
- Объединить данные из auth, gameplay, economy, social и incident сервисов
- Обеспечить аудит и безопасное выполнение админ-команд
- Согласовать поведение с core задачами gateway/notification/support (API-TASK-182, API-TASK-200)

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/ui/admin-panel/part1-dashboard-moderation.md`
**Версия:** v1.0.1 (2025-11-07 02:31)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Макеты главного дашборда, вкладок Players, Moderation, World Control
- Виджеты real-time метрик (онлайн, отчёты, экономические показатели)
- UI компонентов поиска игроков, карточек банов, отчётов
- Требования к live-логам, фильтрации, подтверждениям действий
- Упоминание ролей (SUPER_ADMIN, MODERATOR) и аудита

### Дополнительные источники

- `.BRAIN/05-technical/backend/admin/admin-tools-core.md` – административные операции
- `.BRAIN/05-technical/backend/support/support-ticket-system.md` – отчёты пользователей
- `.BRAIN/05-technical/backend/incident-response/incident-response.md` – инциденты и расследования
- `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md` – управление техработами
- `.BRAIN/05-technical/backend/notification-system.md` – уведомления админов

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-182-api-gateway-management-api.md`
- `API-SWAGGER/tasks/active/queue/task-200-support-ticket-system-api.md`
- `API-SWAGGER/tasks/active/queue/task-205-announcement-system-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/admin/panel/admin-panel-ui.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 (REST + WebSocket)

```
API-SWAGGER/api/v1/admin/panel/
 └── admin-panel-ui.yaml  ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** admin-service
- **Порт:** 8087 (или текущее значение для admin-службы)
- **API Base Path:** `/api/v1/admin/panel`
- **Зависимости:** auth-service, gameplay-service, economy-service, social-service, support-service, incident-service, notification-service, analytics-service, maintenance-service
- **Безопасность:** role-based access (`SUPER_ADMIN`, `MODERATOR`, `SUPPORT`), обязательная MFA, аудит

### Frontend
- **Модуль:** `modules/admin/panel`
- **State Store:** `useAdminPanelStore`
- **State:** `dashboardMetrics`, `playerProfiles`, `reports`, `moderationActions`, `worldControls`, `activityLogs`, `filters`
- **UI компоненты:** `AdminDashboard`, `MetricWidget`, `PlayerSearchPanel`, `ModerationQueue`, `ActionDrawer`, `WorldControlPanel`, `LiveLogStream`
- **Формы:** `PlayerActionForm`, `BanAppealResponseForm`, `MaintenanceToggleForm`, `AnnouncementQuickForm`
- **Layouts:** `AdminLayout`
- **Хуки:** `useAdminMetrics`, `useLiveModeration`, `usePlayerLookup`, `useAdminPermissions`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: admin-service (port 8087)
# - API Base: /api/v1/admin/panel
# - Dependencies: auth, gameplay, economy, social, support, incident, notification, analytics, maintenance
# - Frontend Module: modules/admin/panel (useAdminPanelStore)
# - UI: AdminDashboard, MetricWidget, PlayerSearchPanel, ModerationQueue, ActionDrawer, WorldControlPanel, LiveLogStream
# - Forms: PlayerActionForm, BanAppealResponseForm, MaintenanceToggleForm, AnnouncementQuickForm
# - Layout: AdminLayout
# - Hooks: useAdminMetrics, useLiveModeration, usePlayerLookup, useAdminPermissions
```

---

## ✅ Что нужно сделать (детальный план)

1. Сформировать агрегированные DTO для дашборда (метрики онлайн, отчёты, инциденты, экономика).
2. Описать эндпоинты поиска игроков, просмотра профиля, истории наказаний, состояния аккаунта.
3. Реализовать операции модерации: предупреждение, бан, мьют, снятие санкций, ручной rollback.
4. Добавить инструменты world control: включение maintenance mode, запуск эвентов, broadcast объявлений.
5. Настроить WebSocket stream для live-логов, новых отчётов, изменений статусов, подтверждений команд.
6. Прописать систему ролей/разрешений и обязательный аудит (`auditId`, `reason`, `ticketRef`).
7. Определить ошибки, лимиты (rate limiting админ-команд), flow подтверждений (two-step confirm).
8. Предусмотреть интеграцию с incident/support системами (ссылки на тикеты, escalation).
9. Подготовить примеры, сценарии тестирования и пройти чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/admin/panel/dashboard`** – агрегированные метрики (онлайн, отчёты, инциденты, экономика, боевые активности).
2. **GET `/api/v1/admin/panel/dashboard/logs`** – последние события (moderation, incidents, world changes) с пагинацией.
3. **GET `/api/v1/admin/panel/players/search`** – поиск игроков по никнейму/ID, фильтры по статусу, банам, роли.
4. **GET `/api/v1/admin/panel/players/{playerId}`** – профиль игрока (статус аккаунта, punishments, inventory summary, progression, последние активности).
5. **POST `/api/v1/admin/panel/players/{playerId}/actions`** – выполнение модерационных действий (`WARN|MUTE|BAN|KICK|ROLLBACK|FLAG`), требует подтверждения.
6. **POST `/api/v1/admin/panel/players/{playerId}/actions/{actionId}/confirm`** – подтверждение/отмена двухэтапной операции.
7. **GET `/api/v1/admin/panel/moderation/queue`** – список активных репортов/тикетов с приоритетами.
8. **POST `/api/v1/admin/panel/moderation/{reportId}/resolve`** – закрытие репорта (результат, коммент, решение, ссылка на лог).
9. **GET `/api/v1/admin/panel/world/state`** – состояние мировых сервисов (shards, events, maintenance, realtime load).
10. **POST `/api/v1/admin/panel/world/maintenance`** – включение/выключение maintenance mode (параметры, сообщения, расписание).
11. **POST `/api/v1/admin/panel/world/events`** – запуск/остановка игрового события (ID, зона, параметры).
12. **POST `/api/v1/admin/panel/announcements`** – отправка срочного объявления (каналы, шаблон, TTL, подтверждение).
13. **GET `/api/v1/admin/panel/audit`** – история админ-действий (фильтрация по оператору, типу, времени).
14. **GET `/api/v1/admin/panel/settings`** – роли, разрешения, конфиги панелей.
15. **WS `/api/v1/admin/panel/stream`** – WebSocket события: `report-created`, `player-action`, `world-alert`, `maintenance-warning`, `command-result`, `escalation-needed`.

---

## 🧱 Модели данных

- **AdminDashboard** – `metrics` (onlinePlayers, reportsPending, bansToday, economyVolume, incidentsOpen), `alerts[]`, `servicesStatus[]`.
- **DashboardLogEntry** – `timestamp`, `type`, `summary`, `severity`, `actor`, `link`.
- **PlayerProfile** – `playerId`, `nickname`, `accountStatus`, `roles`, `playtime`, `characters[]`, `progression`, `economy`, `violations[]`.
- **ModerationActionRequest** – `actionType`, `reason`, `duration`, `evidenceUrls[]`, `ticketId`, `requiresConfirmation`, `metadata`.
- **ModerationActionResponse** – `actionId`, `status`, `confirmationRequired`, `expiresAt`.
- **ReportItem** – `reportId`, `type`, `priority`, `reportedPlayer`, `reporter`, `status`, `createdAt`, `attachments[]`.
- **WorldState** – `shards[]`, `playersOnline`, `eventsRunning[]`, `servers[]`, `maintenanceStatus`, `alerts[]`.
- **MaintenanceRequest** – `active`, `message`, `scheduledFrom`, `scheduledTo`, `affectedServices[]`.
- **AnnouncementRequest** – `channels[]`, `title`, `message`, `severity`, `expiresAt`, `confirmationsRequired`.
- **AuditEntry** – `auditId`, `actor`, `action`, `target`, `payload`, `result`, `timestamp`, `ticketRef`.
- **RealtimeEvent** – union (`reportCreated`, `playerAction`, `worldAlert`, `maintenanceWarning`, `commandResult`, `escalationNeeded`).
- **Error Schema (`AdminPanelUiError`)** – codes (`PERMISSION_DENIED`, `CONFIRMATION_REQUIRED`, `ACTION_FAILED`, `REPORT_NOT_FOUND`, `MAINTENANCE_LOCK`, `ANNOUNCEMENT_BLOCKED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` + обязательный `X-Admin-Role`; поддержка MFA и session pinning.
- Aудит: все POST операции требуют `X-Audit-Id` и записываются в лог.
- Rate limiting: защитить от массовых действий (например, 10 банов/мин без подтверждения).
- Безопасность: двухэтапные подтверждения, проверка разрешений на каждую операцию.
- Кэширование: минимальное, большинство данных realtime; использовать short-lived (≤5s) ETag для dashboard.
- Локализация: поддержка `locale` для сообщений и объявлений.
- Инциденты: все ошибки `ACTION_FAILED` с критическим уровнем автоматически отправляются в incident-service.
- DRY: использовать общие схемы безопасности/ответов из `api/v1/shared/common/`.

---

## 🧪 Примеры

- Дашборд с онлайн-метриками, тикетами и предупреждениями.
- Выполнение действия `BAN` с последующим подтверждением и событием stream.
- Включение maintenance mode с расписанием и уведомлением игроков.
- Быстрый broadcast объявления о срочном обновлении.
- Поток live-логов: новый репорт → действие модератора → результат.

---

## 🔗 Связности и зависимости

- Работает поверх admin-tools core API и интеграций (API-TASK-182, API-TASK-200, API-TASK-205).
- Использует поддержку incident/support систем (tickets, escalation).
- Взаимодействует с world/maintenance сервисами для состояния серверов и технических работ.

---

## ✅ Критерии приемки

1. Создан файл `admin-panel-ui.yaml` с архитектурным комментарием и всеми REST/WS контрактами.
2. Эндпоинты покрывают dashboard, поиск игроков, модерацию, world control, announcements, audit.
3. Описаны модели DTO, ошибки, правила безопасности (роли, подтверждения, аудит).
4. Настроены realtime события и интеграция с инцидентами/поддержкой.
5. Подготовлены примеры, тест-кейсы и выполнен чеклист.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, фронтенд модуль, зависимости, UI компоненты
- [ ] Эндпоинты + WebSocket покрывают сценарии UI Admin Panel
- [ ] Прописаны модели, ошибки, безопасность, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml` после сохранения

---

## ❓FAQ

**Q:** Можно ли объединить с core admin API?
**A:** Лучше разделять: core — бизнес-операции, UI — агрегированные данные, подтверждения, realtime и представление.

**Q:** Как обрабатывать массовые действия?
**A:** Через пакетные запросы с обязательным подтверждением и лимитами; предусмотреть `ACTION_FAILED` и оповещение incident-service.

**Q:** Кто получает доступ к WebSocket стриму?
**A:** Только админы/модераторы с активной сессией; требуется токен + проверка роли на каждом сообщении (ping).



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

