# Task ID: API-TASK-305
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:12
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-301], [API-TASK-302], [API-TASK-303], [API-TASK-304], [API-TASK-190]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию подсистемы телеметрии и аналитики боевых лодаутов (Loadout Telemetry & Analytics) для `analytics-service`: сбор метрик, агрегирование, отчётность, экспорт данных и интеграция с балансировкой.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для сбора метрик, построения аналитических витрин, расчёта KPI и предоставления отчётов другим сервисам.

---

## 🎯 Цель задания

Дать дизайнерам, аналитикам и игровым системам прозрачный доступ к данным об эффективности лодаутов, их доступности и использования макросов, чтобы управлять балансом и контентом.

**Зачем это нужно:**
- Измерять успешность лодаутов в разных режимах, выявлять дисбаланс.
- Отслеживать предупреждения, деградации, макро-активность и их влияние на матчи.
- Предоставлять данные для экономических и прогрессионных систем, AI-рекомендаций и отчётности.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Раздел «Метрики и телеметрия» — ключевые показатели (`activation_success_rate`, `availability_conflicts`, `event_adjustments`, `macro_usage`, `profile_violations`, `extraction_completion_rate`, `loot_weight_distribution`).
- Разделы «Очереди обновлений», «Управление недоступными предметами», «Макрокоманды» — события и данные, которые нужно собирать.
- Раздел «Интеграция с другими системами» — потребители аналитики: матчмейкинг, социальные, прогрессия, события, экономика.

### Дополнительные источники

- `.BRAIN/05-technical/backend/analytics/analytics-platform.md` — архитектура аналитики (если существует).
- `.BRAIN/05-technical/backend/notification-system.md` — оповещения на основе метрик.
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` — контекст событий.
- `.BRAIN/02-gameplay/combat/arena-system.md`, `loot-hunt-system.md` — режимы для анализа.
- `API-SWAGGER/tasks/active/queue/task-190-analytics-reporting-api.md` — общие правила аналитики.

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-301-combat-loadout-kits-api.md`
- `API-SWAGGER/tasks/active/queue/task-302-combat-loadout-profiles-api.md`
- `API-SWAGGER/tasks/active/queue/task-303-combat-loadout-macros-api.md`
- `API-SWAGGER/tasks/active/queue/task-304-combat-loadout-availability-api.md`
- `API-SWAGGER/tasks/active/queue/task-190-analytics-reporting-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/analytics/gameplay/loadout-telemetry.yaml`  
**Формат:** OpenAPI 3.0.3 + AsyncAPI (при необходимости)

```
API-SWAGGER/
└── api/
    └── v1/
        ├── analytics/
        │   └── gameplay/
        │       ├── loadout-telemetry.yaml          ← создать
        │       ├── loadout-telemetry-components.yaml
        │       └── loadout-telemetry-events.yaml
        └── gameplay/
            └── combat/
                ├── loadouts.yaml
                ├── loadout-kits.yaml
                ├── loadout-profiles.yaml
                ├── loadout-macros.yaml
                └── loadout-availability.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** analytics-service
- **Порт:** 8092
- **API Base:** `/api/v1/analytics/gameplay/loadouts*`
- **Источники данных:** gameplay-service (events, REST), notification-service (alerts), matchmaking-service (результаты матчей), economy-service (стоимость наборов), progression-service (mastery tiers), world-service (event modifiers).
- **Хранилища:** ClickHouse/Parquet lake (`analytics/loadouts-dataset.parquet`), Prometheus, Kafka topics `combat.loadouts.metrics`, `combat.loadouts.events`.

### Frontend
- **Модуль:** `modules/analytics/loadouts`
- **State Store:** `useLoadoutAnalyticsStore`
- **UI компоненты:** `LoadoutPerformanceDashboard`, `LoadoutHeatmap`, `AvailabilityConflictChart`, `MacroUsageTimeline`, `EventImpactMatrix`, `ExtractionCompletionChart`
- **Формы:** `AnalyticsFilterForm`, `ReportScheduleForm`, `AlertThresholdForm`
- **Хуки:** `useLoadoutAnalytics`, `useTelemetryFeed`, `useReportScheduler`, `useAlertThresholds`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: analytics-service (port 8092)
# - API Base: /api/v1/analytics/gameplay/loadouts*
# - Data Sources: gameplay-service, notification, matchmaking, economy, progression, world
# - Data Stores: ClickHouse/Parquet, Prometheus, Kafka combat.loadouts.metrics/events
# - Frontend Module: modules/analytics/loadouts (useLoadoutAnalyticsStore)
# - UI: LoadoutPerformanceDashboard, LoadoutHeatmap, AvailabilityConflictChart, MacroUsageTimeline, EventImpactMatrix
# - Forms: AnalyticsFilterForm, ReportScheduleForm, AlertThresholdForm
# - Hooks: useLoadoutAnalytics, useTelemetryFeed, useReportScheduler, useAlertThresholds
```

---

## ✅ Что нужно сделать (детальный план)

1. Собрать список метрик и телеметрии из `.BRAIN` документа, определить схемы данных и источники.
2. Спроектировать REST endpoints для получения агрегированных метрик, временных рядов, сравнений, экспорта и управления конфигурацией аналитики.
3. Описать схемы `LoadoutMetric`, `LoadoutPerformanceSlice`, `AvailabilityConflictMetric`, `MacroUsageMetric`, `EventImpactMetric`, `ExtractionStats`, `ReportConfig`, `AlertRule`, `TelemetryIngestRequest`.
4. Спроектировать ingestion endpoints/события для приёма данных из gameplay-service и других источников.
5. Добавить endpoints для настройки алертов, планирования отчётов, вызова ручных расчётов и выгрузки данных (CSV/Parquet).
6. Описать асинхронные события (`loadout.telemetry.ingested`, `loadout.analytics.alert-triggered`, `loadout.analytics.report-generated`) с payload и гарантиями доставки.
7. Прописать безопасность (scopes `analytics:loadouts.read`, `analytics:loadouts.write`, `analytics:loadouts.alerts`, `analytics:loadouts.reports`), аудит и лимиты.
8. Подготовить примеры запросов/ответов, расписать формулы KPI и связь с loadouts/availability/macro API.
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `POST /api/v1/analytics/gameplay/loadouts/telemetry` — ingestion endpoint (batch/stream) для сырых событий.
2. `GET /api/v1/analytics/gameplay/loadouts/metrics/summary` — сводные KPI (win rate, usage, conflicts, macro impact).
3. `GET /api/v1/analytics/gameplay/loadouts/metrics/trends` — временные ряды с фильтрами по режиму, роли, событию.
4. `GET /api/v1/analytics/gameplay/loadouts/metrics/availability` — статистика по предупреждениям, деградациям, времени восстановления.
5. `GET /api/v1/analytics/gameplay/loadouts/metrics/macro` — влияние макросов на успех, конфликты, бан-рейты арен.
6. `GET /api/v1/analytics/gameplay/loadouts/metrics/event-impact` — влияние live events на метрики лодаутов.
7. `GET /api/v1/analytics/gameplay/loadouts/metrics/extraction` — показатели PvE экспедиций (completion rate, loot weight).
8. `POST /api/v1/analytics/gameplay/loadouts/reports` — генерация отчёта (запрос параметров, формат).
9. `GET /api/v1/analytics/gameplay/loadouts/reports/{reportId}` — получение статуса и скачивание готового отчёта.
10. `POST /api/v1/analytics/gameplay/loadouts/alerts` — создание/обновление правил оповещений (thresholds, канал).
11. `GET /api/v1/analytics/gameplay/loadouts/alerts` — список правил и их состояние.
12. `POST /api/v1/analytics/gameplay/loadouts/recalculate` — ручной пересчёт аналитики (idempotent).
13. `GET /api/v1/analytics/gameplay/loadouts/export` — экспорт данных (CSV/Parquet) с фильтрами.
14. `GET /api/v1/analytics/gameplay/loadouts/ingest-status` — статус ingestion pipeline, лаги, ошибки.

Все мутационные операции требуют `Authorization`, `Idempotency-Key`, `X-Audit-Id`. Использовать общие `$ref` для ответов и ошибок.

---

## 🧱 Модели данных

- **TelemetryIngestRequest** — `events[]` (тип, источник, payload, timestamp, correlationId).
- **LoadoutMetric** — `time`, `loadoutId`, `role`, `mode`, `winRate`, `usageRate`, `avgTTK`, `damageContribution`.
- **AvailabilityConflictMetric** — `time`, `conflictRate`, `avgRecoveryTime`, `warningsSent`, `degradedSessions`.
- **MacroUsageMetric** — `time`, `macroUsage`, `conflictRate`, `banRate`, `successDelta`.
- **EventImpactMetric** — `eventCode`, `impactScore`, `affectedLoadouts[]`, `modifierType`, `duration`.
- **ExtractionStats** — `zone`, `completionRate`, `avgLootWeight`, `extractionTime`, `lossRate`.
- **ReportConfig** — `reportId`, `name`, `filters`, `format`, `schedule`, `recipients`.
- **ReportStatus** — `reportId`, `status`, `generatedAt`, `expiresAt`, `downloadUrl`.
- **AlertRule** — `alertId`, `metric`, `threshold`, `comparison`, `cooldown`, `channels[]`, `enabled`.
- **AlertEvent** — `alertId`, `triggeredAt`, `metricValue`, `context`, `actions`.
- **AnalyticsExport** — `exportId`, `filters`, `format`, `status`, `createdAt`, `readyAt`.
- **Async Events** — payloads для `loadout.telemetry.ingested`, `loadout.analytics.alert-triggered`, `loadout.analytics.report-generated`, `loadout.analytics.export-ready`.

---

## 🧭 Принципы и правила

- Соблюдать OpenAPI 3.0.3 и AsyncAPI, выносить повторяющиеся схемы.
- Поддерживать масштабируемый ingestion (batch/stream) с гарантией `at-least-once`, дедупликацию по `eventId`.
- Использовать `$ref` на общие компоненты и на контракты loadouts/availability/macro.
- Указывать безопасность (scopes analytics), аудит, idempotency и роли (Analyst, Designer, GM).
- Документировать SLA для отчётов, экспорта и алертов.
- Предусмотреть пагинацию, фильтры, агрегации; следить за лимитами размера ответов.
- Интегрировать Prometheus/ClickHouse endpoints через `$ref` или описания.

---

## ✅ Критерии приемки

1. Все 14 эндпоинтов описаны с параметрами, схемами, примерами.
2. Ingestion специфицирован с поддержкой batch/stream, дедупликации и ошибок.
3. Алерты и отчёты документированы (создание, получение, расписание, события).
4. Метрики покрывают доступность, макросы, события, экспедиции, производительность.
5. Экспорт и выгрузки описаны, включая форматы и ограничения.
6. Асинхронные события перечислены с payload, каналами, retry и idempotency.
7. Требования к безопасности, аудит, `Idempotency-Key`, `X-Audit-Id` описаны.
8. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Все секции шаблона заполнены, ссылки на `.BRAIN` и связанные API корректны.
- [ ] OpenAPI/AsyncAPI проходят lint, длина файла ≤400 строк (или части вынесены).
- [ ] Примеры покрывают ingestion, запрос метрик, отчёты, алерты, экспорт.
- [ ] События синхронизированы с analytics/reporting платформой.
- [ ] Архитектурный комментарий корректен.
- [ ] Инструкции по обновлению `brain-mapping.yaml` и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Как обрабатываются задвоенные события?  
**A:** Использовать `eventId` и `deduplicationWindow`; при дубле возвращать `208 Already Reported` и не записывать событие повторно.

**Q:** Можно ли настроить автоматическую рассылку отчётов?  
**A:** Да, endpoint `/reports` поддерживает расписание; события `loadout.analytics.report-generated` уведомляют получателей через notification-service.

**Q:** Как быстро обновляются метрики?  
**A:** Ingestion — near real-time; агрегаты обновляются каждые 5 минут, но можно инициировать ручной пересчёт через `/recalculate`.

---

## 🔗 Связность и последующие шаги

- Добавить запись в `tasks/config/brain-mapping.yaml` и обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (API-TASK-305).
- Согласовать спецификацию с общими аналитическими контрактами (`API-TASK-190`), loadout availability и macro API.
- После создания спецификации инициировать задачи для аналитических дашбордов и ML-рекомендаций.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

