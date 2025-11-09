# Task ID: API-TASK-213
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:26
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-212, API-TASK-190

---

## 📋 Краткое описание

Создать UI-ориентированный API `admin-panel-analytics-ui`, обеспечивающий административный аналитический дашборд, контрольные панели и realtime визуализации.

**Что нужно сделать:** Подготовить `api/v1/admin/panel/admin-panel-analytics-ui.yaml`, описав REST и WebSocket контракты для метрик, графиков, контрольных панелей и исполнительных команд, соответствующих документу Part 2.

---

## 🎯 Цель задания

Предоставить фронтенду админ-панели удобный доступ к аналитике, графикам, мониторингу сервисов и управлению автоматизацией.

**Зачем это нужно:**
- Визуализировать ключевые данные: активность игроков, производительность серверов, экономику, инциденты
- Поддержать контрольные action-панели (server controls, workflow automation, alert tuning)
- Обеспечить realtime обновление графиков и запуск автоматических сценариев
- Согласовать с UI Part 1 и аналитическим API (API-TASK-190) для полного покрытия админ-панели

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/ui/admin-panel/part2-analytics-controls.md`
**Версия:** v1.0.1 (2025-11-07 02:32)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Реализация компонентов `AdminAnalyticsDashboard`, `ServerClusterMap`, `IncidentTimeline`
- Параметры графиков, фильтров временных интервалов, каналов данных
- Control Panels: server restarts, scaling, automation workflows, alert thresholds
- Отчёты и экспорты (PDF/CSV), расписания, интеграция с Ops
- Реалтайм метрики и алертинг

### Дополнительные источники

- `.BRAIN/05-technical/backend/admin/admin-tools-core.md` – административные команды
- `.BRAIN/05-technical/backend/analytics/analytics-reporting.md` – отчёты и сбор метрик
- `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md` – управление кластерами
- `.BRAIN/05-technical/backend/incident-response/incident-response.md` – таймлайны инцидентов
- `.BRAIN/05-technical/backend/performance-monitoring.md` – метрики производительности

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-212-admin-panel-ui-api.md`
- `API-SWAGGER/tasks/active/queue/task-190-analytics-reporting-api.md`
- `API-SWAGGER/tasks/active/queue/task-205-announcement-system-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/admin/panel/admin-panel-analytics-ui.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 (REST + WebSocket)

```
API-SWAGGER/api/v1/admin/panel/
 ├── admin-panel-ui.yaml            (API-TASK-212)
 └── admin-panel-analytics-ui.yaml  ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** admin-service (аналитический модуль)
- **Порт:** 8087
- **API Base Path:** `/api/v1/admin/panel/analytics`
- **Зависимости:** analytics-service, monitoring-service, incident-service, maintenance-service, notification-service, economy-service, auth-service
- **Безопасность:** только роли `SUPER_ADMIN`, `OPS`, `ANALYST`, поддержка SSO и MFA

### Frontend
- **Модуль:** `modules/admin/panel`
- **State Store:** `useAdminAnalyticsStore`
- **State:** `metrics`, `charts`, `incidents`, `serverClusters`, `alerts`, `automation`, `filters`
- **UI компоненты:** `AnalyticsDashboard`, `MetricCardGrid`, `LineChart`, `HeatMap`, `IncidentTimeline`, `AutomationPanel`, `AlertConfigurator`, `ReportScheduler`
- **Формы:** `MetricsFilterForm`, `ServerControlForm`, `AutomationWorkflowForm`, `ReportExportForm`
- **Layouts:** `AdminLayout`
- **Хуки:** `useAdminMetrics`, `useAnalyticsFilters`, `useAutomationActions`, `useRealtimeAnalytics`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: admin-service (port 8087)
# - API Base: /api/v1/admin/panel/analytics
# - Dependencies: analytics, monitoring, incident, maintenance, notification, economy, auth
# - Frontend Module: modules/admin/panel (useAdminAnalyticsStore)
# - UI: AnalyticsDashboard, MetricCardGrid, LineChart, HeatMap, IncidentTimeline, AutomationPanel, AlertConfigurator, ReportScheduler
# - Forms: MetricsFilterForm, ServerControlForm, AutomationWorkflowForm, ReportExportForm
# - Layout: AdminLayout
# - Hooks: useAdminMetrics, useAnalyticsFilters, useAutomationActions, useRealtimeAnalytics
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить DTO для основных виджетов: карточки метрик, графики, тепловые карты, таймлайны.
2. Описать REST эндпоинты загрузки метрик, фильтров, исторических данных, расписаний отчётов.
3. Реализовать endpoints для server control/automation (scaling, restart, script execution) с подтверждениями и аудитом.
4. Настроить WebSocket stream для realtime обновлений: метрики, алерты, инциденты, результаты команд.
5. Прописать возможности экспорта (CSV/PDF) и планировщиков отчётов.
6. Указать поддержку фильтров (time range, region, cluster, category) и сохранение пресетов.
7. Добавить правила безопасности, двухэтапное подтверждение критичных действий, аудит.
8. Предусмотреть интеграцию с notification-service для алертов и escalation.
9. Подготовить примеры, тест-план и пройти чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/admin/panel/analytics/dashboard`** – агрегированные метрики (онлайн, нагрузка, экономика, инциденты, latency).
2. **GET `/api/v1/admin/panel/analytics/metrics`** – подробные метрики с фильтрами (`timeRange`, `region`, `cluster`, `source`).
3. **GET `/api/v1/admin/panel/analytics/charts/{chartId}`** – данные для конкретного графика (line, bar, area, pie).
4. **GET `/api/v1/admin/panel/analytics/heatmaps`** – тепловые карты нагрузки по кластерам/серверам.
5. **GET `/api/v1/admin/panel/analytics/incidents`** – таймлайн инцидентов (фильтр по severity, статусу, владельцу).
6. **GET `/api/v1/admin/panel/analytics/incidents/{incidentId}`** – подробности инцидента, шаги, связанные системы.
7. **POST `/api/v1/admin/panel/analytics/automation`** – запуск автоматизации (workflow id, параметры, подтверждение).
8. **POST `/api/v1/admin/panel/analytics/server-control`** – операции над серверами (restart, scale, drain, allocate).
9. **POST `/api/v1/admin/panel/analytics/alerts`** – настройка/обновление alert thresholds, каналов уведомлений.
10. **GET `/api/v1/admin/panel/analytics/reports`** – список отчётов, статусы, расписания.
11. **POST `/api/v1/admin/panel/analytics/reports`** – создание отчёта (тип, формат, диапазон, auto schedule).
12. **GET `/api/v1/admin/panel/analytics/reports/{reportId}/download`** – получение сформированного отчёта (link, expiry).
13. **POST `/api/v1/admin/panel/analytics/presets`** – сохранение пресета фильтров/виджетов.
14. **GET `/api/v1/admin/panel/analytics/presets`** – список пресетов пользователя.
15. **WS `/api/v1/admin/panel/analytics/stream`** – события: `metric-update`, `alert-triggered`, `incident-updated`, `automation-result`, `server-state-change`, `report-ready`.

---

## 🧱 Модели данных

- **AnalyticsDashboard** – `cards[]`, `chartsSummary[]`, `alerts[]`, `incidentsSummary`, `serverHealth`, `economySnapshot`.
- **MetricCard** – `id`, `title`, `value`, `unit`, `trend`, `threshold`, `status`, `updatedAt`.
- **ChartData** – `chartId`, `type`, `series[]`, `labels[]`, `timeRange`, `annotations[]`.
- **HeatmapData** – `clusters[]` (clusterId, nodes[], load, status, alerts).
- **IncidentTimelineItem** – `timestamp`, `incidentId`, `severity`, `status`, `summary`, `owner`, `links[]`.
- **AutomationRequest** – `workflowId`, `parameters`, `requiresApproval`, `scheduledAt`, `auditId`.
- **ServerControlRequest** – `action`, `target`, `reason`, `confirmation`, `auditId`.
- **AlertConfig** – `metric`, `threshold`, `comparison`, `duration`, `channels[]`, `escalationPolicy`.
- **ReportDefinition** – `reportId`, `name`, `type`, `format`, `schedule`, `lastRun`, `status`, `downloadUrl`.
- **PresetDefinition** – `presetId`, `name`, `filters`, `layout`, `createdAt`, `isDefault`.
- **RealtimeEvent** – union (`metricUpdate`, `alertTriggered`, `incidentUpdated`, `automationResult`, `serverStateChange`, `reportReady`).
- **Error Schema (`AdminAnalyticsUiError`)** – codes (`METRIC_SOURCE_UNAVAILABLE`, `WORKFLOW_APPROVAL_REQUIRED`, `SERVER_ACTION_DENIED`, `REPORT_LIMIT`, `ALERT_INVALID`, `PRESET_CONFLICT`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` + роль `SUPER_ADMIN|OPS|ANALYST`, обязательный `X-Audit-Id` для управляющих операций.
- Безопасность: двухфакторное подтверждение для `server-control` и `automation` (если требует).
- Пагинация: cursor-based для событий/инцидентов; `limit ≤ 200`.
- Кэширование: графики и метрики с `Cache-Control: max-age=5`; realtime поток обновляет изменения.
- Локализация: поддержка локалей и часовых поясов.
- Audit trail: все операции `POST` логируются.
- Инциденты: критические ошибки → incident-service, alert escalation.
- DRY: использовать общие компоненты (`responses`, `pagination`, `security`).

---

## 🧪 Примеры

- Дашборд с карточками метрик и графиком активности.
- Heatmap загрузки серверов по кластерам с realtime обновлением.
- Запуск автоматического workflow для масштабирования shard.
- Скачивание отчёта по экономике за неделю.
- Настройка alert threshold, получение события `alert-triggered`.

---

## 🔗 Связности и зависимости

- Зависит от `API-TASK-212` (общий UI admin panel) и аналитического API `API-TASK-190`.
- Интегрируется с monitoring/incident/maintenance сервисами.
- Использует notification-service для алертинга и escalation.

---

## ✅ Kriterien приемки

1. Создан файл `admin-panel-analytics-ui.yaml` с архитектурным комментарием, REST и WS секциями.
2. Эндпоинты покрывают все аналитические виджеты, фильтры, automation, отчёты и пресеты.
3. Описаны модели данных, ошибки, требования безопасности и аудита.
4. Реализованы механизмы realtime обновлений и экспорта.
5. Приложены примеры и тестовые сценарии; выполнение чеклиста подтверждено.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Прописаны микросервис, фронтенд модуль, зависимости, UI компоненты
- [ ] Эндпоинты и WS покрывают аналитические сценарии
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml` после сохранения

---

## ❓FAQ

**Q:** Чем отличается от задачи Part 1?
**A:** Part 1 (API-TASK-212) покрывает дашборд модерации и player management; Part 2 фокусируется на аналитике, графиках, automation и server controls.

**Q:** Где хранятся отчёты?
**A:** В analytics-service; UI API возвращает ссылки (подписанные URL) и статусы генерации.

**Q:** Требуются ли ограничения на automation?
**A:** Да, через `requiresApproval`, `auditId` и политики; критичные операции требуют подтверждения двух админов.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

