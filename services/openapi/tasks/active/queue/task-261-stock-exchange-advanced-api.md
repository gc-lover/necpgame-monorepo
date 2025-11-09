# Task ID: API-TASK-261
**Тип:** API Generation
**Приоритет:** средний (Expansion)
**Статус:** queued
**Создано:** 2025-11-07 23:50
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-260 (stock-exchange management API), API-TASK-259 (stock-exchange protection API)

---

## 📋 Краткое описание

Нужно спроектировать спецификацию продвинутых биржевых инструментов: short selling, margin trading, опционные и фьючерсные контракты, а также управление рисками и мониторинг позиций.

**Что нужно сделать:** Создать/обновить файл `stock-exchange-advanced.yaml`, охватывающий REST/WebSocket API для получения и управления производными инструментами, маржинальными счетами и короткими позициями.

---

## 🎯 Цель задания

Дать игрокам-экспертам доступ к расширенным инструментам и обеспечить прозрачный контроль рисков:
- Управление маржинальными счетами, займами и процентами
- Создание, исполнение и клиринг short позиций
- Маржинальные требования, мониторинг equity и margin calls
- Операции с опционами (call/put) и фьючерсами (индексы, товары)
- Интеграция с risk engine, analytics и protection (анти-манипуляции)

**Зачем это нужно:** дополнить базовую биржу профессиональными инструментами и обеспечить игровую глубину при соблюдении балансовых и комплаенс ограничений.

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/02-gameplay/economy/stock-exchange/stock-advanced.md`
**Версия:** v1.1.0 (2025-11-07)
**Статус:** approved, api-readiness ready

**Ключевые детали:**
- Short selling: механика займа, collateral 150%, fees, unlimited loss
- Margin trading: уровни кредитного плеча (2x/5x/10x), margin call при equity <30%
- Options: контракт размер 100 акций, strike ±5/10/20%, expirations weekly, Black-Scholes pricing
- Futures: контракты на индексы/commodities, initial/maintenance margin, settlement
- SQL структуры `margin_accounts`, `derivatives_contracts`, `derivative_positions`
- Требования уровней игроков, лимиты, риск-параметры

### Дополнительные источники
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-protection.md` — контроль манипуляций и ограничения short/margin
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-exchange-overview.md` — архитектура сервисов (matching, compliance, risk)
- `.BRAIN/02-gameplay/economy/stock-exchange/stock-analytics.md` — необходимые индикаторы и аналитика контрактов
- `API-SWAGGER/api/v1/gameplay/economy/stock-exchange-core.yaml` — основные сущности тикеров и портфелей
- `API-SWAGGER/api/v1/gameplay/economy/economy-events.yaml` — события, влияющие на derivatives

### Связанные документы
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-core.md`
- `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md` (рейтинги трейдеров)
- `.BRAIN/05-technical/backend/risk/risk-engine.md` (если доступно)

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/economy/stock-exchange-advanced.yaml`

**Размещение:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── economy/
                ├── stock-exchange-core.yaml
                ├── stock-exchange-trading.yaml
                ├── stock-exchange-dividends.yaml
                ├── stock-exchange-events.yaml
                ├── stock-exchange-analytics.yaml
                ├── stock-exchange-protection.yaml
                ├── stock-exchange-management.yaml
                └── stock-exchange-advanced.yaml  ← создать
```

Файл отсутствует — создать с нуля. Сразу добавить `Target Architecture` блок.

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** economy-service (derivatives module)
- **Порт:** 8085
- **Base path:** `/api/v1/gameplay/economy/stocks/advanced/*`
- **Сервисы:**
  - `risk-engine` — расчет маржи, stress tests
  - `pricing-engine` — опционы (Black-Scholes), фьючерсы (mark-to-market)
  - `collateral-service` — управление залогом, escrow
  - `compliance-service` — проверки на злоупотребления
  - `notification-service` — margin call alerts

### Frontend
- **Модуль:** `modules/economy/stocks`
- **Feature:** `modules/economy/stocks/derivatives`
- **State Store:** `useEconomyStore` (`marginAccounts`, `derivativePositions`, `shortPositions`, `alerts`)
- **UI (@shared/ui):** `DerivativePositionCard`, `MarginDashboard`, `ShortPositionTable`, `OptionChain`, `FuturesCurve`
- **Forms (@shared/forms):** `MarginTopUpForm`, `ShortBorrowForm`, `OptionOrderForm`, `FuturesOrderForm`
- **Layouts:** `@shared/layouts/GameLayout`
- **Hooks:** `@shared/hooks/useRealtime`, `@shared/hooks/useRiskMeter`, `@shared/hooks/useFormStepper`

### API Gateway
```yaml
- id: economy-advanced
  uri: lb://ECONOMY-SERVICE
  predicates:
    - Path=/api/v1/gameplay/economy/stocks/advanced/**
  filters:
    - name: PlayerAuth
```

### Events
- Kafka: `economy.advanced.margin_call`, `economy.advanced.short_liquidated`, `economy.advanced.option_exercised`, `economy.advanced.future_settled`
- WebSocket: `/ws/economy/stocks/advanced`

---

## 🧩 План реализации

1. **Маржинальные счета:** CRUD `margin_accounts`, проверка лимитов, interest accrual.
2. **Short selling:** эндпоинты для открытия/закрытия short, расчет collateral, дневные отчёты.
3. **Margin monitoring:** `/margin/calls`, `/margin/top-up`, webhook/notification интеграция.
4. **Options:** каталог опционов (chains), размещение ордеров (market/limit), exercise/expire, greeks.
5. **Futures:** список контрактов, позиционирование (long/short), mark-to-market, settlement.
6. **Risk checks:** pre-trade проверки, стресс-тесты, ограничения (level requirements, trading volume).
7. **Analytics:** отдавать P&L, realized/unrealized, leverage ratio, risk score.
8. **Observability:** лимиты на открытые позиции, alerts при превышении, audit trail.
9. **Security:** доп. авторизация (`X-Player-TwoFactor`), cooldown после margin call.

---

## 🧪 API Endpoints (минимум)

- `GET /margin/accounts` / `POST /margin/accounts` / `PATCH /margin/accounts/{id}`
- `POST /margin/accounts/{id}/top-up` — пополнение залога
- `GET /margin/calls` — активные margin calls, фильтры severity/time
- `POST /margin/calls/{callId}/acknowledge` — принятие условий
- `GET /short/positions` / `POST /short/positions` / `POST /short/positions/{id}/close`
- `GET /short/quotes` — borrow rates, available shares
- `GET /options/chains` — список опционов по тикеру/expiry
- `POST /options/orders` — размещение ордера (buy/sell call/put)
- `POST /options/{contractId}/exercise` — исполнение
- `GET /futures/contracts` — список фьючерсов (индекс/commodity)
- `POST /futures/orders` — открытие позиции (long/short)
- `POST /futures/settlements/{positionId}` — ручное закрытие
- `GET /analytics/positions` — сводка P&L, greeks, leverage
- `GET /risk/check` — предварительная проверка (simulate)
- WebSocket `/ws/economy/stocks/advanced` — margin call alerts, position updates

Все ошибки через `shared/common/responses.yaml`. Добавить `422` для нарушений правил (например, недостаточный collateral).

---

## 🗄️ Схемы данных

- **MarginAccount** — accountId, playerId, leverageLevel, creditLimit, maintenanceMarginPercent, currentDebt, collateralValue, status.
- **MarginCall** — callId, accountId, equityPercent, requiredTopUp, deadline, status.
- **ShortPosition** — positionId, ticker, sharesBorrowed, borrowRate, collateral, entryPrice, currentPrice, pnl, openedAt, dueDate.
- **OptionContract** — contractId, ticker, type (CALL/PUT), strikePrice, expiration, premium, greeks (delta/gamma/theta/vega), openInterest.
- **OptionOrder** — orderId, side (BUY/SELL), quantity, price, status.
- **FutureContract** — contractId, underlying (index/commodity), deliveryDate, tickSize, marginInitial, marginMaintenance.
- **FuturePosition** — positionId, side (LONG/SHORT), contracts, entryPrice, settlementPrice, pnl.
- **AdvancedAnalytics** — totalExposure, leverageRatio, riskScore, unrealizedPnl, realizedPnl.

Согласовать с таблицами `margin_accounts`, `derivatives_contracts`, `derivative_positions`, добавить `derivative_orders` при необходимости.

---

## 🔄 Интеграции

- **risk-engine:** `POST /risk/check` (stress tests), `POST /risk/margin-evaluate`
- **analytics-service:** `POST /analytics/derivatives/report`
- **notification-service:** `POST /notifications/margin-call`
- **compliance-service:** `POST /compliance/review` при подозрительных операциях
- **economy-events:** получение сигналов о волатильности для корректировки маржи

---

## 📊 Observability

- Метрики: `margin_calls_total`, `margin_liquidations_total`, `short_positions_total`, `derivatives_open_interest`, `options_exercised_total`
- Alerts: equity < 15%, leverage > допустимого, borrow rate spikes, expiry clusters
- Логи: audit для каждого margin call, short borrow, option exercise
- Spans: `derivatives-order`, `margin-top-up`, `short-liquidation`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/gameplay/economy/stocks/advanced` соблюдён.
2. В шапке файла указан `Target Architecture`.
3. Все advanced операции проверяют уровень игрока и trading volume (описать ошибку 403 с кодом `BIZ_LEVEL_TOO_LOW`).
4. Margin account PATCH реализует optimistic locking (поле `version`).
5. Short positions требуют collateral ≥ 150%, нарушения → 422 ошибка.
6. Margin calls имеют обязательное поле `deadline` и событие на шину при создании.
7. Option chain выдаёт greeks и поддерживает фильтр по `expiration` и `moneyness`.
8. Futures mark-to-market выполняется ежедневно; описать событие `economy.advanced.future_marked`.
9. Analytics endpoint возвращает как aggregate, так и список позиций с paginated detail.
10. WebSocket описывает payload `margin_call`, `short_liquidated`, `derivative_update`, heartbeat 30 сек.
11. FAQ охватывает edge cases (double leverage, partial exercise, negative prices).

---

## ❓ FAQ

**Q:** Что происходит, если игрок не пополняет margin call вовремя?

**A:** После дедлайна генерируется событие `economy.advanced.margin_liquidated`, позиции ликвидируются автоматически. API должен возвращать финальный отчёт и сумму списанного collateral.

**Q:** Можно ли частично закрывать short?

**A:** Да — предусмотреть параметр `quantity` в `POST /short/positions/{id}/close`, валидировать остаток, обновлять collateral.

**Q:** Как рассчитываются проценты по марже?

**A:** Через фоновые процессы risk-engine; API должно отдавать `interestAccrued` и `nextInterestAt` в `MarginAccount`.

**Q:** Что делать при резких гэпах (gap up) ночью?

**A:** В management API (TASK-260) включить emergency halt; в advanced API документировать экстренное событие `economy.advanced.gap_event` и форсированную ликвидацию.

**Q:** Как логируются опциональные контрактные изменения?

**A:** Все операции (создание ордера, исполнение) пишутся в audit trail с `orderId`, `playerId`, `performedBy`, `ip`, `deviceFingerprint`.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

