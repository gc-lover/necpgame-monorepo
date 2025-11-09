# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-103  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 18:56  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-100 (inventory-core), API-TASK-102 (economy-contracts)  

## Summary
Создать спецификацию `api/v1/economy/analytics/analytics.yaml`, обеспечивающую REST/WS поток экономической аналитики: графики цен, объёмов, технические индикаторы, heat map, портфельные метрики, алерты и интеграцию с рынками и контрактами.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/economy-analytics.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 03:22) |

**Key points:** многоформатные графики (line, candlestick, OHLC, area), индикаторы (SMA, EMA, RSI, MACD, Bollinger Bands), sentiment/heat-map, портфельная аналитика (Sharpe, volatility, drawdown), trade history, алерты и уведомления, интеграции с markets, inventory, notification, analytics pipeline.  
**Related docs:** `.BRAIN/02-gameplay/economy/economy-contracts.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-database.md`, `.BRAIN/05-technical/backend/economy-telemetry.md`, `.BRAIN/05-technical/backend/notification-service.md`, `.BRAIN/05-technical/backend/anti-fraud-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/analytics  
- **API directory:** `api/v1/economy/analytics/analytics.yaml`  
- **base-path:** `/api/v1/economy/analytics`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/analytics`  
- **Shared UI/Form components:** `@shared/ui/PriceChart`, `@shared/ui/CandlestickChart`, `@shared/ui/HeatMap`, `@shared/ui/PortfolioMetrics`, `@shared/forms/AlertCreateForm`, `@shared/forms/IndicatorSettingsForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Определить REST endpoints для графиков, портфелей, heat maps, настроек аналитики и алертов; описать WebSocket/streaming каналы.
2. Смоделировать `ChartData`, `IndicatorConfig`, `VolumeData`, `PortfolioAnalytics`, `RiskMetrics`, `TradeStats`, `HeatMap`, `AlertConfig`, `SentimentIndex`.
3. Фиксировать индикаторы и вычисления (SMA, EMA, RSI, MACD, Bollinger Bands) с параметрами и ограничениями.
4. Описать интеграции с auction/market данными, telemetry pipeline, notification-service (alerts) и economy-contracts (данные сделок).
5. Добавить требования к фронтенду, real-time обновлениям, rate limits и caching.

## Endpoints
- `GET /charts/{asset}` — исторические данные (timeframe, indicators, aggregation).
- `GET /charts/{asset}/candles` — свечные данные с поддержкой 1m/5m/15m/1h/1d/1w.
- `GET /volume/{asset}` — объёмы торгов (histogram, cumulative).
- `POST /charts/{asset}/indicators` — расчёт индикаторов по пользовательским настройкам.
- `GET /portfolio/{playerId}` — метрики портфеля (returns, Sharpe, volatility, drawdown, beta).
- `GET /portfolio/{playerId}/history` — динамика портфеля, monthly returns, win-rate.
- `GET /market/heatmap` — heat map рынка по секторам/корпорациям/регионам.
- `GET /portfolio/heatmap/{playerId}` — heat map портфеля.
- `POST /alerts` — создать/обновить алерты (price, volume, event).
- `DELETE /alerts/{alertId}` — удалить алерт.
- `GET /alerts/{playerId}` — список алертов и их статусы.
- `GET /sentiment/index` — индекс Fear & Greed и bull/bear power.
- `GET /analytics/settings/{playerId}` — настройки аналитики (timeframe, indicators, subscriptions).
- `PUT /analytics/settings/{playerId}` — обновить настройки.
- WebSocket `ws://.../streams/market` — real-time обновления цен/объёмов.
- WebSocket `ws://.../streams/alerts` — push алертов и событий.

## Data Models
- `ChartDataPoint`, `Candle`, `VolumeBar`, `IndicatorSeries`.
- `IndicatorConfig` (type, period, params, thresholds).
- `PortfolioAnalytics` (totalReturn, annualizedReturn, sharpeRatio, volatility, maxDrawdown, beta).
- `RiskMetrics`, `TradeStats`, `MonthlyReturn`, `WinLossRatio`.
- `HeatMapCell` (sector, asset, performance, weight).
- `AlertConfig` (type, condition, threshold, expiration, deliveryChannels).
- `AlertStatus` (active, triggered, snoozed, cancelled).
- `SentimentIndex` (fearGreedScore, bullPower, bearPower, components).
- `AnalyticsSettings` (defaultTimeframe, favoriteIndicators, priceAlerts, eventSubscriptions).
- Ошибки: `AssetNotFoundError`, `IndicatorNotSupportedError`, `AlertLimitExceededError`, `PortfolioNotAccessibleError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.analytics.market-updated`, `economy.analytics.alert-triggered`, `economy.analytics.sentiment-updated`.
- Data sources: auction-house, trade-system, economy-contracts, external telemetry (ticks), anti-fraud signals.
- Notification-service: `POST /notifications/alerts` (push/email/discord).
- Telemetry pipeline: `POST /economy/telemetry/ingest` для ingestion данных.
- Caching: Redis для hot datasets, TTL configurable.
- Rate limits: `GET /charts/*` — max 60 req/min, `POST /alerts` — max 20 alerts/player.
- Security: OAuth2 PlayerSession, roles `player`, `analyst`, `admin`.
- Frontend real-time: Orval клиента `@api/economy/analytics`, state hook `useEconomyAnalyticsStore`.

## Acceptance Criteria
1. Файл `api/v1/economy/analytics/analytics.yaml` создан, ≤ 500 строк, корректный `info.x-microservice`.
2. Описаны все REST endpoints, WebSocket каналы, параметры, ответы, ошибки и примеры (charts, portfolio, alerts).
3. Индикаторы и метрики документированы с формулами и параметрами; указаны ограничения на количество серий.
4. Heat map, sentiment index, portfolio analytics и trade stats представлены отдельными схемами.
5. Kafka события, интеграции, rate limits и caching требования задокументированы; есть ссылки на зависимости (inventory, contracts, auction).
6. Указаны UI/Orval требования, real-time обновления, уведомления.
7. `tasks/config/brain-mapping.yaml` дополнился записью `API-TASK-103`, статус `queued`, приоритет `high`.
8. `.BRAIN/02-gameplay/economy/economy-analytics.md` содержит секцию `API Tasks Status`.
9. `tasks/queues/queued.md` обновлён новой записью.
10. После реализации запускается `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\analytics\`.

## FAQ / Notes
- **Нужен ли отдельный файл для алертов?** Если описание превысит лимит, вторичное задание можно вынести; текущее ТЗ должно покрыть core.
- **Как обрабатывать AI-предсказания?** Пометить в разделе расширений (TODO), без реализации в данном пакете.
- **Есть ли ограничения по источникам данных?** Указать, что данные поступают из economy telemetry pipeline; внешний импорт требуется согласования с anti-fraud.

## Change Log
- 2025-11-09 18:56 — Задание создано (API Task Creator Agent)


