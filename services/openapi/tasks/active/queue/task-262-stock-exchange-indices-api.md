# Task ID: API-TASK-262
**Тип:** API Generation
**Приоритет:** средний (Post-MVP)
**Статус:** queued
**Создано:** 2025-11-08 00:00
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-260 (stock-exchange management), API-TASK-258 (stock-exchange analytics)

---

## 📋 Краткое описание

Нужно обновить/расширить спецификацию биржевых индексов: расчет CORP100, NC50, ASIA25, EURO30, секторальных индексов, ETF и их взаимодействие с аналитикой и событиями.

**Что нужно сделать:** Пересоздать файл `stock-exchange-indices.yaml` версии 1.1.0, чтобы он покрывал расчёт, публикацию и управление индексами, ETF и корзинами, включая REST и WebSocket интерфейсы.

---

## 🎯 Цель задания

Сделать индексы полноценной частью биржи:
- Рассчитывать и хранить состав индексов (веса, ребаланс)
- Публиковать значения индексов в реальном времени и исторические ряды
- Управлять ETF (подписка, выкуп, корзины)
- Интегрировать индексы с событиями, аналитикой и фьючерсами
- Обеспечить админ-инструменты для ребалансировки и конфигурирования

**Зачем это нужно:** игрокам нужны индикаторы рынка, ETF продукты и прозрачность расчёта, а дизайнерам — инструменты управления индексами.

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/02-gameplay/economy/stock-exchange/stock-indices.md`
**Версия:** v1.1.0 (2025-11-07)
**Статус:** approved, api-ready

**Основные данные:**
- Описание индексов CORP100, NC50, ASIA25, EURO30, sector indices (defense, tech, energy, medical, cyber)
- Формула расчёта (market-cap weighted, divisor)
- Ребаланс (quarterly), критерии включения, пример значений
- Использование индексов для market sentiment, рост/падение, ETF
- Метрики, мониторинг, интеграции с аналитикой

### Дополнительные источники
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-analytics.md` — heatmap, sentiment
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-events.md` — влияние событий на индексы
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-exchange-overview.md` — index-service компонента
- `.BRAIN/05-technical/backend/index/index-service.md` (если есть)
- `API-SWAGGER/api/v1/gameplay/economy/stock-exchange-core.yaml` — базовые сущности корпораций

### Связанные документы
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-advanced.md` — фьючерсы и ETF
- `.BRAIN/05-technical/backend/announcement/announcement-system.md` — анонсы ребалансов

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/economy/stock-exchange-indices.yaml`

Файл существует (v1.0.0) → обновить до v1.1.0 (refactor). При необходимости разбить на ≤400 строк, использовать общие компоненты.

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** index-service (часть economy-service или отдельный)
- **Порт:** 8085 (через economy-service gateway)
- **Base path:** `/api/v1/gameplay/economy/stocks/indices/*`
- **Интеграции:**
  - `analytics-service` — исторические ряды
  - `pricing-engine` — realtime updates
  - `economy-events` — корректировки при событиях
  - `announcement-service` — уведомление о ребалансе

### Frontend
- **Модуль:** `modules/economy/stocks`
- **Feature:** `modules/economy/stocks/indices`
- **State Store:** `useEconomyStore` (`indices`, `etfHoldings`, `rebalanceSchedule`)
- **UI:** `IndexCard`, `PerformanceChart`, `SectorHeatmap`, `ETFSubscriptionForm`
- **Forms:** `ETFSubscriptionForm`, `RebalancePreviewForm`
- **Layouts:** `@shared/layouts/GameLayout`
- **Hooks:** `@shared/hooks/useRealtime`, `@shared/hooks/usePagination`, `@shared/hooks/useChartZoom`

### Gateway маршрут
```yaml
- id: economy-indices
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/gameplay/economy/stocks/indices/**
```

### Events
- Kafka: `economy.indices.rebalanced`, `economy.indices.divisor_updated`, `economy.indices.weight_changed`
- WebSocket: `/ws/economy/stocks/indices`

---

## 🧩 План

1. Обновить `info` (версия 1.1.0, ссылки на .BRAIN).
2. Добавить Target Architecture комментарий.
3. Расширить разделы:
   - Получение списка индексов и деталей
   - Состав индекса (tickers, weights)
   - Исторические ряды, агрегации
   - Ребаланс расписание, симуляции
   - ETF операции (подписка/выкуп, NAV)
4. Документировать WebSocket для realtime индекса.
5. Добавить админ endpoints (`/admin/indices`, `/admin/rebalance`) с безопасностью.
6. Прописать интеграцию с events (автоматические корректировки).
7. Добавить схемы данных (Index, IndexConstituent, ETF, RebalancePlan).
8. Обновить acceptance criteria.

---

## 🧪 API Endpoints (минимум)

- `GET /indices` — список индексов, фильтрация по региону/сектору
- `GET /indices/{indexId}` — детальная информация, текущая стоимость
- `GET /indices/{indexId}/history` — исторические значения (interval, range)
- `GET /indices/{indexId}/constituents` — состав (ticker, weight, contribution)
- `GET /indices/{indexId}/rebalance/schedule` — предстоящие ребалансировки
- `POST /indices/{indexId}/subscribe` — подписка на ETF (player)
- `POST /indices/{indexId}/redeem` — выкуп долей ETF
- `GET /indices/{indexId}/etf/holdings` — портфель ETF
- `POST /admin/indices` — создание/обновление индекса (admin-only)
- `POST /admin/indices/{indexId}/rebalance` — запуск ребаланса (с превью)
- `GET /analytics/indices/top-movers` — топ рост/падение
- `GET /analytics/indices/heatmap` — heatmap по индексам
- WebSocket `/ws/economy/stocks/indices` — realtime цены, изменения веса

Ошибки: `400` (невалидные фильтры), `403` (нет доступа), `404` (индекс не найден), `409` (ребаланс уже идёт), `429` (rate limit для ETF), `500`.

---

## 🗄️ Схемы

- **IndexSummary** — id, code, name, region, sector, currentValue, change24h, constituentsCount, etfTicker.
- **IndexDetails** — summary + divisor, methodology, lastRebalanceAt, nextRebalanceAt, performance (YTD, 1Y, 5Y).
- **IndexConstituent** — ticker, weightPercent, marketCap, contribution, sector.
- **IndexHistoryPoint** — timestamp, value, changePercent.
- **RebalanceSchedule** — scheduleId, indexId, plannedAt, status (PLANNED/LOCKED/EXECUTED/CANCELLED), notes.
- **RebalancePreview** — proposedWeights, turnover, impactEstimate.
- **ETFPosition** — playerId, shares, navPerShare, costBasis, unrealizedPnl.
- **ETFRequest** — requestId, type (SUBSCRIBE/REDEEM), amount, status.

---

## 🔄 Интеграции

- `analytics-service`: `POST /analytics/indices/refresh`
- `pricing-engine`: realtime feed для индексов
- `economy-events`: `POST /indices/apply-event` (коррекция весов)
- `announcement-service`: публикация изменений составов

---

## 📊 Observability

- Метрики: `index_rebalance_total`, `index_calc_latency_ms`, `etf_subscriptions_total`, `indices_data_staleness_seconds`
- Алерты: задержка обновления >5 сек, divisor drift, ETF NAV divergence > 1%
- Логи: audit create/update index, rebalance decisions
- Spans: `index-calc`, `rebalance-simulate`, `etf-subscribe`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/gameplay/economy/stocks/indices` соблюдён.
2. В info.description ссылаться на `.BRAIN/02-gameplay/economy/stock-exchange/stock-indices.md` v1.1.0.
3. Указан Target Architecture.
4. Индексы поддерживают фильтр `scope` (global/region/sector/custom).
5. История поддерживает `interval` (1m, 5m, 1h, 1d) и `range` (7d, 30d, YTD, 1y).
6. Constituents endpoint возвращает `weightPercent` и `contribution24h`.
7. Rebalance API требует `X-Admin-Role` и возвращает `rebalanceId`.
8. ETF операции проверяют лимиты игрока (min subscription, cooldown).
9. WebSocket payload содержит `indexId`, `value`, `changePercent`, `timestamp`, `eventType`.
10. Добавлен раздел FAQ (ребаланс во время кризиса, divisor adjustments, ETF liquidity).

---

## ❓ FAQ

**Q:** Как обрабатывать внеплановый ребаланс?

**A:** Через `POST /admin/indices/{id}/rebalance` с флагом `emergency=true`, требуются две подписи (multi-admin). Документируй статус `EMERGENCY_PENDING`.

**Q:** Что делать, если отсутствуют данные по корпорации (halt)?

**A:** Использовать последнее значение и пометить constituent как `halted=true`; API должно возвращать признак и уменьшать вес при следующем ребалансе.

**Q:** Можно ли создавать пользовательские индексы?

**A:** Да, предусмотреть endpoint `/admin/indices/custom` с ограничениями (max constituents, approval required). Отрази в схемах.

**Q:** Как синхронизировать ETF NAV с индексом?

**A:** NAV пересчитывается после каждого `index_update`; описать событие `economy.indices.nav_updated`.

**Q:** Что если divisor нужно изменить?

**A:** Добавить endpoint `/admin/indices/{id}/divisor` (PATCH) с audit и событием `economy.indices.divisor_updated`.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

