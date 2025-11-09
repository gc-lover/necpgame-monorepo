# Task ID: API-TASK-260
**Тип:** API Generation
**Приоритет:** высокий (Post-MVP)
**Статус:** queued
**Создано:** 2025-11-07 23:40
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-122 (stock-exchange core API), API-TASK-259 (stock-exchange protection API)

---

## 📋 Краткое описание

Сформировать административный контракт управления биржей акций NECPGAME: инициализация, конфигурация сервисов, наблюдаемость и синхронизация подсистем (matching engine, pricing, dividends, protection, integration).

**Что нужно сделать:** Создать спецификацию `stock-exchange-management.yaml`, описывающую REST/WS интерфейсы для мониторинга и настройки сервисов биржи, включая статус подсистем, конфигурационные профили, maintenance режимы, инцидентные журналы и SLA.

---

## 🎯 Цель задания

Предоставить единую административную плоскость для экономики, объединяющую:
- Health-check и readiness каждого компонента биржи
- Управление режимами торговли (halt/resume, расписания техобслуживания)
- Настройку коэффициентов риска (fees, leverage, circuit thresholds)
- Связку с observability (метрики, логи, алерты)
- Отчётность по доверенным сервисам (matching engine, dividend-service, compliance-service, analytics-service)

**Зачем это нужно:** обеспечить DevOps/экономистам прозрачность и контроль системы, ускорить реакцию на события и управлять конфигурацией без прямого доступа к микросервисам.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/02-gameplay/economy/stock-exchange/stock-exchange-overview.md`
**Версия:** v1.1.0 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Чему учиться из документа:**
- Общая архитектура биржи и компоненты (`stock-matching-engine`, `dividend-service`, `compliance-service`, `analytics-service`, `index-service`)
- Потоки данных и взаимодействие между сервисами (events → pricing → portfolio → monitoring)
- Контроль рисков (circuit breakers, position limits, insider detection)
- Необходимость мониторинга SLO (`MatchingLatency`, `OrderFailRate`, `PriceStream uptime`)
- Интеграции с другими системами (economy-events, tax, guild-system, notification)
- Требования к документации и структуре директорий (макс 400 строк, Target Architecture блок)

### Дополнительные источники
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-protection.md` — правила защиты и circuit breakers
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-dividends.md` — жизненный цикл дивидендов
- `.BRAIN/05-technical/backend/maintenance/maintenance-mode-system.md` — best practices maintenance режимов
- `API-SWAGGER/api/v1/gameplay/economy/stock-exchange-core.yaml` — существующие публичные REST контракты
- `API-SWAGGER/api/v1/gameplay/economy/economy-events.yaml` — пример шлюза событий

### Связанные документы
- `.BRAIN/05-technical/backend/support/support-ticket-system.md`
- `.BRAIN/05-technical/backend/announcement/announcement-system.md`
- `.BRAIN/05-technical/backend/voice-chat/voice-chat-system.md` (для шаблона health-мониторинга)

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/economy/stock-exchange-management.yaml`
**Версия API:** v1
**Формат:** OpenAPI 3.0.3 (≤400 строк)

**Директория:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── economy/
                ├── stock-exchange-core.yaml
                ├── stock-exchange-trading.yaml
                ├── stock-exchange-indices.yaml
                ├── stock-exchange-dividends.yaml
                ├── stock-exchange-events.yaml
                ├── stock-exchange-analytics.yaml
                ├── stock-exchange-protection.yaml
                └── stock-exchange-management.yaml  ← СОЗДАТЬ
```

Если файл отсутствует — создать; если кто-то начал, обновить до v1.1.0, сохранив обратную совместимость (version bump, changelog в info.description).

---

## 🏗️ Целевая архитектура (⚠️ обязательна)

### Backend (микросервис)
- **Микросервис:** economy-service (admin facade)
- **Порт:** 8085
- **Base path:** `/api/v1/gameplay/economy/stocks/management/*`
- **Контекст:** административные операции, недоступны игрокам
- **Интеграции:**
  - `service-discovery` (Eureka/Consul) — статусы сервисов
  - `config-server` — профили конфигураций (matching-engine, pricing-engine)
  - `notification-service` — рассылка алертов администраторам
  - `tax-service` — зависимость для maintenance (halt налоговых операций)
  - `analytics-service` — сбор SLO и публикация dashboards
  - `support-ticket-system` — реестр инцидентов

### Frontend (админ-консоль)
- **Модуль:** `modules/economy/admin`
- **Feature:** `modules/economy/admin/stock-exchange`
- **State Store:** `useAdminConsoleStore` (`serviceStatus`, `maintenanceWindows`, `riskProfiles`, `slaMetrics`)
- **UI компоненты (@shared/ui):** `ServiceStatusCard`, `MetricChart`, `MaintenanceScheduler`, `RiskConfigForm`, `IncidentTimeline`
- **Формы (@shared/forms):** `MaintenanceWindowForm`, `RiskProfileForm`, `AlertRoutingForm`
- **Layouts:** `@shared/layouts/AdminConsole`
- **Hooks:** `@shared/hooks/usePolling`, `@shared/hooks/useFeatureToggle`

### API Gateway маршрут
```yaml
- id: economy-admin
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/gameplay/economy/stocks/management/**
  filters:
    - name: AdminAuth
```

### Service Communication
- **Feign:**
  - `matching-engine` → `GET /internal/status`
  - `dividend-service` → `POST /internal/halt`
  - `compliance-service` → `POST /internal/refresh-policy`
  - `index-service` → `GET /internal/rebalance/schedule`
- **Event Bus:** `economy.management.*` (`maintenance_scheduled`, `maintenance_started`, `maintenance_completed`, `risk_profile_updated`, `service_degraded`)

---

## 🧩 Детальный план

1. **Документировать целевые подсистемы:** matching, pricing, dividends, compliance, analytics, index, integration gateway.
2. **Определить health эндпоинты:** `/status`, `/status/{service}`, `/status/summary` с SLO (latency, uptime, backlog).
3. **Сконструировать раздел maintenance:** CRUD окон обслуживания (`POST/GET/PATCH/DELETE /maintenance/windows`) с валидацией расписаний и связью с announcements.
4. **Добавить управление торговыми режимами:** `/trading/halt`, `/trading/resume`, `/trading/mode` (normal, restricted, closed).
5. **Описать конфигурацию риска:** `/risk-profiles` (fees, leverage caps, position limits) + аудит изменений.
6. **Прописать incident/journal:** `/incidents` список, `/incidents/{id}` details, `/incidents/{id}/resolve`.
7. **Интегрировать observability:** `/metrics` агрегаты, `/metrics/timeseries` (ссылки на Prometheus), `/alerts/routes` настройка маршрутизации алертов.
8. **Включить WebSocket канал:** `/ws/economy/stocks/management` — живой статусы сервисов/алерты.
9. **Указать безопасность:** OAuth scope `economy.admin`, требуемые заголовки (`X-Admin-Role`, `X-Trace-Id`).
10. **Проверить чеклист:** Target Architecture header, <400 строк, ссылки на shared responses, 10+ acceptance criteria.

---

## 🧪 API Endpoints (минимум)

1. `GET /status` — общая сводка сервисов (up/down, latency, incidents open).
2. `GET /status/{service}` — детальный статус, версии, зависимости, active alerts.
3. `GET /maintenance/windows` — список окон обслуживания (filters: service, status, from/to).
4. `POST /maintenance/windows` — создание окна (service, start, end, scope, announcementId).
5. `PATCH /maintenance/windows/{windowId}` — изменение статуса/времени.
6. `POST /trading/halt` — перевести биржу в halt, указать причину, TTL, связанные сервисы.
7. `POST /trading/resume` — возобновить торговлю (валидация активных инцидентов).
8. `GET /risk-profiles` — текущие коэффициенты риска.
9. `PUT /risk-profiles/{profileId}` — обновление fees, leverage, circuit thresholds.
10. `GET /incidents` — активные/исторические инциденты (pagination, severity).
11. `POST /incidents/{incidentId}/resolve` — закрыть инцидент с отчётом.
12. `GET /metrics` — агрегированные SLO (matchingLatency, orderFailRate, uptime).
13. `GET /alerts/routes` / `PUT /alerts/routes` — настройка каналов (PagerDuty, Slack, in-game announcements).
14. `GET /config/profiles` — версии конфигурации (blue/green, staging/prod).
15. WebSocket `/ws/economy/stocks/management` — push `service_status`, `maintenance_update`, `risk_profile_changed`.

Использовать стандартные ошибки из `shared/common/responses.yaml` (`BadRequest`, `Unauthorized`, `Forbidden`, `NotFound`, `Conflict`, `InternalError`).

---

## 🗄️ Модели и схемы

- **ServiceStatus** — serviceId, name, version, status (UP/DEGRADED/DOWN), latencyMs, incidentsOpen, lastHeartbeat.
- **MaintenanceWindow** — id, serviceId, startAt, endAt, status (SCHEDULED/IN_PROGRESS/COMPLETED/CANCELLED), scope, announcementId.
- **TradingMode** — mode (NORMAL/RESTRICTED/HALT), reason, requestedBy, effectiveAt.
- **RiskProfile** — profileId, leverageCaps, marginRequirements, shortCollateralRatio, circuitBreakers, updatedBy, updatedAt.
- **Incident** — id, serviceId, severity, summary, description, createdAt, updatedAt, status, tickets[] (support references).
- **MetricSnapshot** — metricId, value, target, window, status (OK/WARN/ALERT).
- **AlertRoute** — channel, endpoint, severityFilter, enabled.

Все схемы согласовать с данными, описанными в документе (SLO, circuit breakers, position limits).

---

## 🔄 Интеграции и события

- **Event Bus:** `economy.management.*` (описать payload для `maintenance_scheduled`, `service_halting`, `service_resumed`, `risk_profile_updated`).
- **Outbound calls:**
  - `POST /announcements/global` (notification-service)
  - `POST /support/tickets` (support)
  - `POST /tax/suspension` (tax-service при halt)
- **Inbound webhooks:** `POST /hooks/service-alert` (получение алертов из monitoring stack)

---

## 📊 Observability

- Метрики: `service_status_up_total`, `trading_halt_total`, `maintenance_overlap_detected`, `risk_profile_changes_total`.
- Логи: audit trail каждой админ операции (userId, changes, IP).
- OpenTelemetry spans: `management-halt`, `management-maintenance`, `management-risk-update`.
- Alerts: latency >50 мс, uptime < 99.5%, OrderFailRate >0.5% (по документу), PriceStream uptime < 99.5%.

---

## ✅ Критерии приемки

1. В Info.description указаны источники (.BRAIN docs) и версия API.
2. В заголовке файла присутствует блок `Target Architecture` (microservice, frontend module, UI components, forms, state store, API base).
3. Все endpoints используют префикс `/api/v1/gameplay/economy/stocks/management`.
4. Поддержан `X-Admin-Role` и OAuth scope `economy.admin`; ошибки 401/403 описаны через shared responses.
5. Maintenance окна проверяют пересечения и возвращают `409 Conflict` при конфликте.
6. Trading halt/resume логируют инициатора и причину, возвращают текущий режим.
7. Risk profile update требует поля `version` для optimistic locking.
8. Incident resolve обязательно содержит `resolutionSummary` и список follow-up tasks.
9. Metric snapshots включают таргеты и статус (`OK/WARN/ALERT`).
10. WebSocket документирован: типы событий, payload, heartbeat interval.
11. Все модели снабжены примерами, демонстрирующими реальные значения из документа.
12. Добавлены FAQ/edge cases (halt во время payout, конфликт maintenance).

---

## ❓ FAQ

**Q:** Что делать, если maintenance окно пересекается с активным trading halt?

**A:** Возвращать `409 Conflict` с подсказкой объединить/перенести. Предусмотреть ручное объединение и обновление announcement.

**Q:** Можно ли частично ограничить торговлю (только margin/derivatives)?

**A:** Да — `/trading/mode` принимает список сегментов (`cash`, `margin`, `derivatives`). Документируй enum и поведение.

**Q:** Как отслеживать дубли health-check алертов?

**A:** В `ServiceStatus` включи `alertCount` и `lastAlertAt`, а также маршрутизацию в observability.

**Q:** Что если matching engine обновляется и требует подготовить staging конфигурацию?

**A:** Использовать `/config/profiles` для управления версиями, описать blue/green раскатку, проверки совместимости.

**Q:** Как связать management API с публичными REST файлами?

**A:** Добавить ссылки в components/links или description на соответствующие публичные спецификации (core, trading, dividends).

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

