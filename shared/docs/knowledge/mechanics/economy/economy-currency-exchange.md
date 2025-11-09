---

- **Status:** queued
- **Last Updated:** 2025-11-07 00:18
---


# Экономика - Валютная биржа (Currency Exchange)

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 16:14  
**Приоритет:** высокий (Post-MVP)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:32
**api-readiness-notes:** Перепроверено 2025-11-09 03:32; валютные пары, matching engine, тарифы, антифрод и события остаются полностью описанными для задач economy-service.

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/currency-exchange

---

## Краткое описание

Валютная биржа (Currency Exchange) для обмена региональных валют в NECPGAME.

**Микрофича:** Обмен валют, курсы, forex trading

---

## 💱 Концепция

### Зачем нужна валютная биржа?

**Проблема:**
- В мире есть региональные валюты (Eurodollars, Yen, Yuan, Rubles)
- Игрок путешествует между регионами
- Нужно обменивать валюты

**Решение:**
- Централизованная биржа валют
- Динамические курсы (спрос/предложение)
- Возможность спекуляции

---

## 💰 Валютные пары

### Major Pairs

**EDDY/YEN** - Eurodollars / Japanese Yen
```
Rate: 1 EDDY = 110 YEN
Spread: 1% (1.1 YEN)
Daily volume: 5M eddies
Volatility: Low
```

**EDDY/YUAN** - Eurodollars / Chinese Yuan
```
Rate: 1 EDDY = 6.5 YUAN
Spread: 0.8%
Daily volume: 3M eddies
Volatility: Low
```

**EDDY/RUB** - Eurodollars / Russian Rubles
```
Rate: 1 EDDY = 75 RUB
Spread: 2%
Daily volume: 1M eddies
Volatility: High (political risk)
```

**EDDY/EUR** - Eurodollars / European Credits
```
Rate: 1 EDDY = 0.9 EUR
Spread: 0.5%
Daily volume: 4M eddies
Volatility: Low
```

### Cross Pairs

**YEN/YUAN**, **EUR/YEN**, **RUB/EUR** и т.д.

---

## 📊 Механика обмена

### Instant Exchange

```
Player in Tokyo, has 10,000 YEN
Wants to exchange to EDDY

Rate: 110 YEN = 1 EDDY
Exchange fee: 1%

Calculation:
10,000 YEN / 110 = 90.91 EDDY
Fee: 0.91 EDDY (1%)
Receives: 90 EDDY

Lost in conversion: 0.91 EDDY (1%)
```

### Limit Exchange Order

```
Player wants better rate:
Current: 110 YEN/EDDY
Wants: 105 YEN/EDDY (save 4.5%)

Places limit order:
Exchange 10,000 YEN when rate ≤ 105

Waits 3 days → rate drops to 105
Auto-executes: 10,000 / 105 = 95.24 EDDY
Fee: 0.95 EDDY
Receives: 94.29 EDDY

Saved: 4.29 EDDY vs instant (4.8%)
```

---

## 💸 Комиссии

**Exchange Fee:**
```
1% от converted amount

Modifiers:
- High volume (>100k/month): -25%
- VIP: -30%
- Trading guild: -20%
Max discount: -50% (0.5% min)
```

**Spread:**
```
Bid/Ask spread включен в курс
Buy rate: 110 YEN/EDDY
Sell rate: 111 YEN/EDDY
Spread: 1 YEN (0.9%)
```

---

## 📈 Курсы валют

### Базовый курс

```
Base rates (set by system):
EDDY/YEN: 110
EDDY/YUAN: 6.5
EDDY/RUB: 75
EDDY/EUR: 0.9

Updated: Daily based on events
```

### Влияние на курсы

**События:**
```
Corporate War: Arasaka wins
→ YEN strengthens +5%
→ New rate: 104.5 YEN/EDDY

Economic crisis in Europe
→ EUR weakens -10%
→ New rate: 0.99 EUR/EDDY
```

---

## 🗄️ Структура БД

```sql
CREATE TABLE currency_exchange_rates (
    id SERIAL PRIMARY KEY,
    currency_pair VARCHAR(20) NOT NULL, -- "EDDY/YEN"
    
    rate DECIMAL(12,4) NOT NULL,
    bid_rate DECIMAL(12,4) NOT NULL,
    ask_rate DECIMAL(12,4) NOT NULL,
    spread_percent DECIMAL(5,2) NOT NULL,
    
    daily_volume BIGINT DEFAULT 0,
    
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(currency_pair)
);

CREATE TABLE currency_exchange_orders (
    id UUID PRIMARY KEY,
    player_id UUID NOT NULL,
    
    from_currency VARCHAR(10) NOT NULL,
    to_currency VARCHAR(10) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    
    order_type VARCHAR(10) NOT NULL, -- "MARKET", "LIMIT"
    limit_rate DECIMAL(12,4), -- For limit orders
    
    status VARCHAR(20) DEFAULT 'ACTIVE',
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Order lifecycle

| Stage | Описание | API вызовы |
| --- | --- | --- |
| `PENDING_VALIDATION` | Заявка создана и проходит AML/KYC проверку | `POST /exchange/orders` |
| `ACTIVE` | Доступна для матчинга | автоматический переход после проверки |
| `PARTIALLY_FILLED` | Частичное исполнение, остаток ждёт ликвидность | `economy.exchange.partial_fill` event |
| `FILLED` | Полное исполнение, средства выданы | `economy.exchange.executed` event |
| `CANCELLED` | Отменена игроком или по TTL | `POST /exchange/orders/{id}/cancel` |
| `REJECTED` | Заблокирована антифродом | `economy.exchange.rejected` event |

---

## 🌐 API (economy-service)

| Endpoint | Метод | Назначение | Ключевые поля |
| --- | --- | --- | --- |
| `/exchange/rates` | `GET` | Получить актуальные курсы и спреды | фильтры `base`, `quote`, `region`, `granularity` |
| `/exchange/rates/subscribe` | `WS` | Реал-тайм поток котировок | `pair`, `interval` (1s/5s/1m) |
| `/exchange/orders` | `POST` | Создать ордер (market/limit) | `pair`, `amount`, `side`, `orderType`, `limitRate`, `expiresAt` |
| `/exchange/orders/{id}` | `GET` | Детали ордера | состояние, журнал исполнения |
| `/exchange/orders/{id}/cancel` | `POST` | Отмена ордера | MFA > 50k EDDY |
| `/exchange/orders/history` | `GET` | История сделок игрока | пагинация, фильтры по паре и времени |
| `/exchange/quote` | `POST` | Предварительный расчёт instant обмена | `pair`, `amount`, `side` |

**Event bus (`economy.exchange.*`):** `rate_updated`, `order_created`, `order_partial_fill`, `order_filled`, `order_cancelled`, `order_rejected`, `market_halt`.

Подписчики: `wallet-service`, `tax-service`, `analytics-service`, `notification-service`.

---

## 🔍 Risk & compliance

- AML: сделки > 25k EDDY → ручная проверка и временный freeze до 30 минут.
- Rate throttling: 60 котировок/мин на аккаунт, IP limit 200/мин.
- Market halt: волатильность > ±15% за час → пара в статусе `HALTED`, доступны только отмены.
- Cross-region arbitrage guard: > 3 крупных исполнений за час → динамический лимит на вывод.

---

## ⚙️ Matching engine

- FIFO внутри одинакового курса; tie-breaker — timestamp + guild priority (если активен).
- Liquidity pool: системные маркет-мейкеры поддерживают объём по major парам ≥ 5k EDDY.
- Авторасширение спреда при liquidity < 5k или волатильности > 10%.
- TTL для лимитных ордеров (`expiresAt`), авто-отмена по истечении.

---

## 🔔 UX и уведомления

- HUD: всплывающее уведомление при исполнении/отклонении ордера.
- Push/email: `order_filled`, `order_cancelled`, `market_halt`.
- Frontend: WebSocket поток для графиков (1m/5m/1h) + исторические свечи в UI.

---

## 🔄 Интеграции

- `wallet-service`: блокировка/разблокировка валют.
- `tax-service`: сбор комиссий и отчётность.
- `event-service`: экономические события, влияющие на коэффициенты.
- `analytics-service`: агрегированные объёмы, отчёты для управленцев/гильдий.

---

---

## 🔗 Связанные документы

- `economy-currencies-resources.md` - Валюты
- `economy-overview.md` - Обзор

---

## История изменений

- v1.1.0 (2025-11-07 16:14) - Добавлены API, lifecycle, risk-контроль, matching engine, интеграции, обновлены статусы
- v1.0.0 (2025-11-06 22:00) - Создание документа о валютной бирже