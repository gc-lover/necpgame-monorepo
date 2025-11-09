# Task ID: API-TASK-258
**Тип:** API Generation
**Приоритет:** высокий (Post-MVP)
**Статус:** queued
**Создано:** 2025-11-07 23:15
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-122 (stock-exchange core API), API-TASK-121 (economy analytics API)

---

## 📋 Краткое описание

Нужно разработать контракт для аналитической подсистемы биржи: исторические данные, технические индикаторы, тепловые карты, алерты и realtime стримы.

**Что нужно сделать:** Создать файл `stock-exchange-analytics.yaml`, описывающий REST/WebSocket API для аналитики акций, включая модели данных, фильтры и интеграции с уведомлениями.

---

## 🎯 Цель задания

Предоставить инвесторам и администраторам инструменты анализа рынка:
- История цен и объёмов (OHLC, candlesticks)
- Показатели (MA, RSI, MACD, Bollinger, sentiment indexes)
- Heatmap рынка и портфеля
- Настройка алертов по цене/индикаторам
- Order book depth, realtime stream обновлений

**Зачем это нужно:** повысить вовлечённость игроков, дать им данные для осознанных решений и обеспечить синхронизацию с аналитикой фронтенда.

---

## 📚 Источники информации

### Основной источник

**Путь:** `.BRAIN/02-gameplay/economy/stock-exchange/stock-analytics.md`
**Версия:** v1.1.0
**Дата обновления:** 2025-11-07
**Статус:** approved

**Ключевые данные:**
- Типы графиков (line, candlestick, volume, area)
- Индикаторы: SMA/EMA, RSI, MACD, Bollinger, Bull/Bear index
- Heat maps (market, portfolio), sentiment indices
- Портфельная аналитика: Sharpe, beta, volatility, drawdown
- Таблицы `stock_ohlc`, `stock_indicator_cache`, `player_analytics_settings`
- Endpoints `/stocks/analytics/ohlc`, `/indicators`, `/heatmap`, `/orderbook`, `/alerts`
- WebSocket `/stocks/analytics/stream`
- Интеграции с notification-service, analytics, economy-events

### Дополнительные источники
- `.BRAIN/02-gameplay/economy/economy-analytics.md` — общие требования интерфейсов аналитики
- `.BRAIN/02-gameplay/economy/player-market-analytics.md` — reuse подходов к графикам
- `API-SWAGGER/api/v1/gameplay/economy/analytics.yaml` — стилистика общих аналитик
- `API-SWAGGER/api/v1/gameplay/economy/stock-exchange-core.yaml` — базовые структуры тикеров

### Связанные документы
- `.BRAIN/05-technical/backend/analytics/analytics-service.md`
- `.BRAIN/05-technical/backend/notification/notification-system.md`
- `.BRAIN/02-gameplay/economy/economy-events.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/economy/stock-exchange-analytics.yaml`

**Размещение:**
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
                └── stock-exchange-analytics.yaml  ← создать
```

---

## 🏗️ Целевая архитектура (⚠️)

### Backend
- **Микросервис:** economy-service
- **Порт:** 8085
- **Base path:** `/api/v1/gameplay/economy/stocks/analytics/*`
- **Зависимости:**
  - `analytics-service` (вычисление индикаторов и агрегаций)
  - `economy-events` (аннотации на графиках)
  - `notification-service` (алерты)
  - `realtime-server` (стрим котировок)
  - `player-market-service` (shared dataset order book)

### Frontend
- **Модуль:** `modules/economy/stocks`
- **Feature:** `modules/economy/stocks/analytics`
- **State Store:** `useEconomyStore` (`analyticsSeries`, `indicatorCache`, `heatmapData`, `alertRules`)
- **UI (@shared/ui):** `PriceChart`, `CandlestickChart`, `Heatmap`, `IndicatorPanel`, `PortfolioBreakdown`
- **Forms (@shared/forms):** `AlertRuleForm`, `IndicatorConfigForm`
- **Layouts:** `@shared/layouts/GameLayout`
- **Hooks:** `@shared/hooks/useRealtime`, `@shared/hooks/useChartZoom`, `@shared/hooks/useAlertRules`

### API Gateway
```yaml
- id: economy-service
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/gameplay/economy/stocks/analytics/**
```

### Event streaming
- WebSocket `/ws/economy/stocks/analytics`
- Kafka broadcasts `economy.stocks.analytics.updated`

---

## 🧩 План выполнения

1. Смоделировать ресурсы: исторические данные, индикаторы, heatmap, alerts, sentiment.
2. Добавить query параметры: `ticker`, `interval`, `range`, `indicator`, `sector`, `compare`.
3. Описать агрегации и таймфреймы (1m, 5m, 1h, 1d, 1w, 1m, 3m, 1y, max).
4. Настроить WebSocket канал с обновлениями цены/объёма/индикаторов.
5. Реализовать API для управления алертами (создание, перечисление, удаление, toggle состояния).
6. Описать heatmap market/portfolio, включая цветовые ранги и sectors.
7. Добавить модели для аналитики портфеля (Sharpe, beta, volatility, drawdown).
8. Согласовать схемы с таблицами `stock_ohlc`, `stock_indicator_cache`, `player_analytics_settings`.
9. Добавить observability: latency, cache hits, alert triggers.

---

## 🧪 API Endpoints

1. **GET `/api/v1/gameplay/economy/stocks/analytics/ohlc`** — исторические данные; параметры `ticker`, `interval`, `range`, `includeIndicators`.
2. **GET `/api/v1/gameplay/economy/stocks/analytics/indicators`** — MA/EMA, RSI, MACD, Bollinger; поддержка множественных индикаторов.
3. **GET `/api/v1/gameplay/economy/stocks/analytics/heatmap`** — market/sector heatmap, фильтр `scope=market|portfolio`.
4. **GET `/api/v1/gameplay/economy/stocks/analytics/orderbook`** — top-N уровни стакана для тикера.
5. **POST `/api/v1/gameplay/economy/stocks/analytics/alerts`** — создание алерта (price, percentage, indicator crossing).
6. **GET `/api/v1/gameplay/economy/stocks/analytics/alerts`** — список алертов пользователя.
7. **DELETE `/api/v1/gameplay/economy/stocks/analytics/alerts/{alertId}`** — удаление/отключение.
8. **PATCH `/api/v1/gameplay/economy/stocks/analytics/alerts/{alertId}`** — обновление параметров.
9. **GET `/api/v1/gameplay/economy/stocks/analytics/portfolio`** — метрики портфеля: total/annualized return, sharpe, beta, drawdown.
10. **GET `/api/v1/gameplay/economy/stocks/analytics/sentiment`** — Bull/Bear indicators, Fear & Greed index.
11. **WebSocket `/ws/economy/stocks/analytics`** — realtime потоки цен, объёмов, индикаторов, алертов.

Эндпоинты должны использовать error responses из `shared/common/responses.yaml`.

---

## 🗄️ Схемы данных

- **OhlcPoint** — timestamp, open, high, low, close, volume.
- **IndicatorValue** — indicatorType, interval, value, calculatedAt.
- **HeatmapCell** — ticker/sector, performancePercent, volume, sentimentColor.
- **OrderBookLevel** — side (bid/ask), price, quantity, cumulativeQuantity.
- **AlertRule** — alertId, type (`PRICE`, `PERCENT`, `INDICATOR`), comparator, threshold, indicatorConfig, status.
- **PortfolioAnalytics** — totalReturn, annualizedReturn, sharpeRatio, beta, volatility, maxDrawdown, holdings[].
- **SentimentMetrics** — bullPower, bearPower, fearGreedIndex, commentary.
- **RealtimeUpdate** — ticker, price, volume, indicators{}, eventAnnotations[]

Связать с таблицами из документа (`stock_ohlc`, `stock_indicator_cache`, `player_analytics_settings`).

---

## 🔄 Интеграции и события

- **Feign:**
  - `notification-service` (`POST /notifications/alerts`) — отправка алертов
  - `analytics-service` (`POST /analytics/cache/refresh`) — invalidate cache
- **Events:**
  - Kafka `economy.stocks.analytics.alert_triggered`
  - Kafka `economy.stocks.analytics.cache_miss`
- **WS:** push обновлений каждые 1s/5s/1m (конфигurable)

---

## 🗃️ База данных

- `stock_ohlc` — указать поддерживаемые интервалы, индексы (`corporation_id`, `interval`, `recorded_at`)
- `stock_indicator_cache` — primary key (corporation_id, indicator, interval)
- `player_analytics_settings` — пользовательские настройки графиков, алертов
- `analytics_alerts` (если отсутствует) — хранение створенных правил, статусов, каналов уведомлений

---

## 📊 Мониторинг

- Метрики: `analytics_query_latency_ms`, `analytics_ws_clients`, `alert_trigger_rate`, `indicator_cache_hit_ratio`
- Алерты: latency > 500мс, cache hit < 70%, неудачные пуши > 5% за 5 мин
- Logs: audit действий с алертами, изменение пользовательских настроек

---

## ✅ Критерии приемки

1. OpenAPI корректен и содержит блок `Target Architecture`.
2. Все маршруты соответствуют префиксу `/api/v1/gameplay/economy/stocks/analytics`.
3. Поддержана пагинация и агрегации по таймфреймам.
4. Модели покрывают все индикаторы, heatmap и портфельные метрики.
5. WebSocket раздел описывает payload и частоту обновлений.
6. Alert API поддерживает разные типы триггеров и каналы уведомлений.
7. Order book endpoint ограничивает TOP N уровней и поддерживает side filters.
8. Sentiment API возвращает Bull/Bear, Fear & Greed и комментарии.
9. Перечислены интеграции с notification-service и analytics-service.
10. Observability раздел включает метрики latency, cache, alert-задачи.

---

## ❓ FAQ

**Q:** Как ограничить объём данных при запросе OHLC?

**A:** Предусмотреть параметры `range` и `limit`, а также установить верхний предел (например, 10k точек), возвращать 400 при превышении.

**Q:** Где хранить пользовательские настройки графиков?

**A:** В таблице `player_analytics_settings`; API должен позволять сохранять/получать через отдельные endpoints (можно описать future extension).

**Q:** Как уведомлять игрока о срабатывании алертов?

**A:** Через `notification-service` с указанным каналом (HUD, push, mail); логировать алерты в `analytics_alerts`.

**Q:** Что делать при отсутствии данных для выбранного интервала?

**A:** Возвращать пустой массив и 200 с `total=0`, а также добавить предупреждение в поле `warnings`.

**Q:** Как синхронизировать индикаторы с realtime обновлениями?

**A:** WebSocket должен публиковать обновления индикаторов вместе с ценой; описать структуру payload.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

