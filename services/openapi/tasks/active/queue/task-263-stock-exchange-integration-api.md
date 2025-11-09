# Task ID: API-TASK-263
**Тип:** API Generation
**Приоритет:** высокий (Post-MVP)
**Статус:** queued
**Создано:** 2025-11-08 00:10
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-257 (stock-exchange events API), API-TASK-262 (stock-exchange indices API)

---

## 📋 Краткое описание

Нужно спроектировать интеграционный слой между биржей и игровыми системами: квесты, фракции, мировые события, новости и аналитика.

**Что нужно сделать:** Создать спецификацию `stock-exchange-integration.yaml`, описывающую регистрационные вебхуки источников событий, журнал корреляций, превью воздействия, overrides и мониторинг интеграций.

---

## 🎯 Цель задания

Обеспечить корректное взаимодействие биржи с геймплеем:
- Регистрация и аутентификация источников событий (quest-service, faction-service, world events)
- What-if симуляции перед публикацией событий
- Журнал влияния событий на акции и индексы
- Overrides/whitelist для дизайнеров
- Синхронизация с новостями и внутриигровыми уведомлениями

**Зачем это нужно:** сделать экономику реактивной, но управляемой — контролировать влияние квестов, фракций и мировых событий на цены, предотвращать злоупотребления и документировать связи.

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/02-gameplay/economy/stock-exchange/stock-integration.md`
**Версия:** v1.1.0 (2025-11-07)
**Статус:** approved, api-ready

**Содержимое:**
- Квестовые сценарии, влияющие на акции (пример Corporate Espionage, Sabotage)
- Фракционные войны, репутационные бонусы
- Глобальные события (economic crisis, AI breakthrough)
- Event bus (`economy.integration.events`), таблица `event_stock_mapping`
- API предложения: `/stocks/integration/event-hooks`, `/event-preview`, `/event-override`, `/journal`

### Дополнительные источники
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-events.md` — модель импактов
- `.BRAIN/04-narrative/quests/...` — конкретные сюжеты (Helios, Specter)
- `.BRAIN/02-gameplay/world/events/live-events-system.md` — календарь мировых ивентов
- `.BRAIN/05-technical/backend/quest/quest-service.md` (если существует)
- `API-SWAGGER/api/v1/gameplay/economy/economy-events.yaml`

### Связанные документы
- `.BRAIN/05-technical/backend/news/news-feed.md`
- `.BRAIN/05-technical/backend/faction/faction-service.md`
- `.BRAIN/05-technical/backend/analytics/analytics-service.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/economy/stock-exchange-integration.yaml`

**Размещение:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── economy/
                ├── stock-exchange-core.yaml
                ├── stock-exchange-events.yaml
                ├── stock-exchange-management.yaml
                ├── stock-exchange-advanced.yaml
                ├── stock-exchange-indices.yaml
                └── stock-exchange-integration.yaml  ← создать
```

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** economy-service (integration gateway)
- **Порт:** 8085
- **Base path:** `/api/v1/gameplay/economy/stocks/integration/*`
- **Интеграции:**
  - `quest-service` (outcomes, branching decisions)
  - `faction-service` (wars, reputation)
  - `world-service` (global events, territory control)
  - `news-service` (публикации)
  - `notification-service` (информирование игроков)

### Frontend
- **Модуль:** `modules/economy/admin`
- **Feature:** `modules/economy/admin/stock-integration`
- **State Store:** `useAdminConsoleStore` (`eventHooks`, `integrationHealth`, `impactJournal`, `overrides`)
- **UI:** `IntegrationCard`, `EventImpactTable`, `OverrideForm`, `SourceStatusBadge`
- **Forms:** `EventHookRegistrationForm`, `ImpactOverrideForm`
- **Layouts:** `@shared/layouts/AdminConsole`
- **Hooks:** `@shared/hooks/useRealtime`, `@shared/hooks/useScenarioRunner`

### API Gateway
```yaml
- id: economy-integration
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/gameplay/economy/stocks/integration/**
```

### Events
- Kafka: `economy.integration.event_registered`, `economy.integration.impact_previewed`, `economy.integration.override_applied`, `economy.integration.webhook_failed`
- WebSocket: `/ws/economy/stocks/integration`

---

## 🧩 План

1. Описать регистрацию источников событий (`POST /event-hooks`) с валидацией токенов и scope.
2. Реализовать ingest `/event` (internal) → передачу в stock-events (TASK-257).
3. Добавить what-if симуляции `/event-preview` с указанием modifiers и прогнозов.
4. Журнал `/journal` — связь события, тикера, impact, подтверждающие квесты.
5. Overrides `/event-override` и whitelist `/event-whitelist` — ручное управление.
6. Интегрировать новости (`/news/publish`), уведомления (`/notifications/preview`).
7. Поддержать мониторинг источников (`GET /health`) и retry policy.
8. Обновить схемы (EventHook, ImpactPreview, MappingEntry, Override).
9. Документировать security (service tokens, HMAC, idempotency).

---

## 🧪 API Endpoints (минимум)

- `POST /event-hooks` — регистрация источника (quest/faction/world/news)
- `GET /event-hooks` — список, фильтры по типу и статусу
- `PATCH /event-hooks/{hookId}` — изменение secret, callback URL, scopes
- `POST /events/ingest` — ingest события (service token, idempotency key)
- `POST /events/preview` — расчёт ожидаемого impact (без применения)
- `GET /events/journal` — история (filters: ticker, eventType, source, date)
- `GET /events/{eventId}` — подробности влияния
- `POST /events/{eventId}/override` — ручная корректировка (impact, duration)
- `POST /events/{eventId}/whitelist` — добавление в whitelist
- `GET /integration/health` — статус подключений, ошибки
- `GET /integration/stats` — количество событий по источникам, успех/ошибка
- `POST /news/publish` — связка события с новостным сообщением
- `POST /notifications/dispatch` — массовая рассылка инвесторам
- WebSocket `/ws/economy/stocks/integration` — realtime события `event_received`, `impact_applied`, `override_applied`

Ошибки: `400` (невалидные данные), `401` (неверный secret), `403` (нет scope), `404` (event), `409` (дубликат idempotency key), `429` (rate limit), `500`.

---

## 🗄️ Схемы данных

- **EventHook** — hookId, sourceType (`quest`, `faction`, `world`, `news`), callbackUrl, secretHash, scopes, status, lastEventAt.
- **IntegrationEvent** — eventId, sourceType, sourceId, eventType, severity, payload (JSON), occurredAt, ingestStatus.
- **ImpactPreview** — predictedImpact[], modifiers (sector, playerCount, territory), confidence, recommendedAction.
- **ImpactJournalEntry** — eventId, ticker, indexId, baseImpactPercent, appliedImpactPercent, appliedAt, overrides[]
- **OverrideRequest** — overrideId, eventId, appliedBy, appliedAt, reason, newImpact, duration.
- **WhitelistEntry** — entryId, eventType, ticker, expiresAt.
- **IntegrationHealth** — sourceType, status (UP/DEGRADED/DOWN), lastSuccess, failures24h.
- **IntegrationStats** — totalEvents, appliedEvents, rejectedEvents, overridesCount.

---

## 🔄 Интеграции

- **quest-service:** HMAC подпись, `questOutcomeId`, `questBranch`
- **faction-service:** данные о войнах, территории (`territoryId`, `control`) 
- **world-service:** глобальные события (`eventCode`, `severity`)
- **news-service:** создание лент `POST /news/articles`
- **analytics-service:** `POST /analytics/events/annotate`
- **notification-service:** `POST /notifications/broadcast`

---

## 📊 Observability

- Метрики: `integration_events_total`, `integration_failures_total`, `integration_override_total`, `integration_latency_ms`
- Alerting: spike отказов > 5/min, ingest latency > 30 сек, неподтвержденные события > 100
- Logs: audit override, whitelist, manual adjustments
- Spans: `integration-ingest`, `integration-preview`, `integration-override`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/gameplay/economy/stocks/integration` соблюдён.
2. Target Architecture блок присутствует.
3. Event ingest требует заголовков `X-Service-Id`, `X-Signature`, `X-Idempotency-Key`.
4. Preview возвращает рекомендации (например, `suggestedAction: HALT_MARGIN`).
5. Journal поддерживает пагинацию (cursor) и экспорт (link to CSV/JSON).
6. Overrides логируются с `auditId`; возвращается событие `economy.integration.override_applied`.
7. Health endpoint содержит per-source latency, errorRate.
8. News publish связывает событие с `newsId` и поддерживает шаблон.
9. Notifications endpoint поддерживает сегментацию получателей (`investors`, `guild-leaders`, `global`).
10. WebSocket payload включает `eventId`, `source`, `impact`, `status`.
11. FAQ охватывает повторы событий, задержки и ручной откат.

---

## ❓ FAQ

**Q:** Что делать, если событие приходит дважды?

**A:** Использовать `X-Idempotency-Key`; при повторе возвращать `409` с `existingEventId`. Документируй этот процесс.

**Q:** Как откатить ошибочный override?

**A:** Предусмотреть `POST /events/{overrideId}/rollback`; описать audit, уведомление и пересчёт impact.

**Q:** Как обработать события без указания тикера?

**A:** Требовать mapping lookup (по `eventType` + `metadata`). Если не найден, помещать в `pending` и уведомлять админа.

**Q:** Можно ли ограничить влияние события на индекс?

**A:** Да — добавить параметр `maxIndexImpact` в overrides и `IndexImpactPolicy` в журнале.

**Q:** Что если источник недоступен (health DOWN)?

**A:** Обновлять `IntegrationHealth`, генерировать alert, автоматически переводить связанные события в manual review.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

