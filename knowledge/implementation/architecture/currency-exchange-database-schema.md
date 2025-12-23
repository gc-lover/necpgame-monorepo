<!-- Issue: #140890193 -->

# Currency Exchange System - Database Schema

## Обзор

Схема базы данных для системы валютной биржи, включающая курсы валютных пар, историю курсов, ордера на обмен,
исполненные сделки и лимиты рисков для игроков.

## ERD Диаграмма

```mermaid
erDiagram
    currency_exchange_rates ||--o{ currency_exchange_rate_history : "has_history"
    currency_exchange_orders ||--o{ currency_exchange_trades : "buy_order"
    currency_exchange_orders ||--o{ currency_exchange_trades : "sell_order"
    character ||--o{ currency_exchange_orders : "places"
    character ||--o{ currency_exchange_risk_limits : "has"

    currency_exchange_rates {
        uuid id PK
        varchar currency_pair UNIQUE
        varchar base_currency
        varchar quote_currency
        decimal rate
        decimal bid_rate
        decimal ask_rate
        decimal spread
        decimal daily_volume
        decimal volatility
        timestamp last_updated
        timestamp created_at
    }

    currency_exchange_rate_history {
        uuid id PK
        varchar currency_pair
        decimal rate
        decimal bid_rate
        decimal ask_rate
        decimal spread
        decimal volume
        timestamp recorded_at
    }

    currency_exchange_orders {
        uuid id PK
        uuid player_id FK
        varchar currency_pair
        currency_order_type order_type
        currency_order_side side
        varchar base_currency
        varchar quote_currency
        decimal amount
        decimal limit_rate
        decimal executed_amount
        currency_order_status status
        decimal fee
        decimal fee_discount
        integer ttl_seconds
        timestamp expires_at
        timestamp created_at
        timestamp updated_at
        timestamp executed_at
    }

    currency_exchange_trades {
        uuid id PK
        uuid buy_order_id FK
        uuid sell_order_id FK
        varchar currency_pair
        varchar base_currency
        varchar quote_currency
        decimal amount
        decimal rate
        decimal total
        decimal fee
        timestamp executed_at
    }

    currency_exchange_risk_limits {
        uuid id PK
        uuid player_id FK
        currency_risk_limit_type limit_type
        decimal limit_value
        decimal current_value
        timestamp period_start
        timestamp period_end
        timestamp reset_at
        timestamp created_at
        timestamp updated_at
    }
```

## Описание таблиц

### currency_exchange_rates

Таблица курсов валютных пар. Хранит текущие курсы валютных пар.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `currency_pair`: Валютная пара (VARCHAR(10), NOT NULL, UNIQUE, например "EDDY/USD", "USD/EUR")
- `base_currency`: Базовая валюта (VARCHAR(10), NOT NULL)
- `quote_currency`: Котируемая валюта (VARCHAR(10), NOT NULL)
- `rate`: Средний курс валютной пары (DECIMAL(20,8), NOT NULL)
- `bid_rate`: Курс покупки (DECIMAL(20,8), NOT NULL)
- `ask_rate`: Курс продажи (DECIMAL(20,8), NOT NULL)
- `spread`: Спред (DECIMAL(10,4), NOT NULL, default: 0.0000)
- `daily_volume`: Дневной объем торговли (DECIMAL(20,2), NOT NULL, default: 0.00)
- `volatility`: Волатильность курса (DECIMAL(5,2), NOT NULL, default: 0.00)
- `last_updated`: Время последнего обновления
- `created_at`: Время создания

**Индексы:**

- По `(currency_pair, last_updated DESC)` для быстрого поиска курса пары
- По `(base_currency, quote_currency)` для поиска по валютам
- По `last_updated DESC` для последних обновлений

**Constraints:**

- CHECK (base_currency != quote_currency): Базовая и котируемая валюты должны быть разными
- CHECK (bid_rate <= ask_rate): Курс покупки должен быть <= курса продажи
- CHECK (spread >= 0.0000): Спред должен быть >= 0

### currency_exchange_rate_history

Таблица истории курсов валютных пар. Хранит исторические данные курсов для графиков и аналитики.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `currency_pair`: Валютная пара (VARCHAR(10), NOT NULL)
- `rate`: Средний курс (DECIMAL(20,8), NOT NULL)
- `bid_rate`: Курс покупки (DECIMAL(20,8), NOT NULL)
- `ask_rate`: Курс продажи (DECIMAL(20,8), NOT NULL)
- `spread`: Спред (DECIMAL(10,4), NOT NULL, default: 0.0000)
- `volume`: Объем торговли (DECIMAL(20,2), NOT NULL, default: 0.00)
- `recorded_at`: Время записи

**Индексы:**

- По `(currency_pair, recorded_at DESC)` для истории пары
- По `recorded_at DESC` для временных запросов

### currency_exchange_orders

Таблица ордеров на обмен валют. Хранит instant и limit ордера игроков.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `player_id`: ID игрока (FK к characters, NOT NULL)
- `currency_pair`: Валютная пара (VARCHAR(10), NOT NULL)
- `order_type`: Тип ордера (currency_order_type ENUM, NOT NULL: 'instant', 'limit')
- `side`: Сторона ордера (currency_order_side ENUM, NOT NULL: 'buy', 'sell')
- `base_currency`: Базовая валюта (VARCHAR(10), NOT NULL)
- `quote_currency`: Котируемая валюта (VARCHAR(10), NOT NULL)
- `amount`: Сумма обмена в базовой валюте (DECIMAL(20,2), NOT NULL, CHECK: > 0)
- `limit_rate`: Лимитный курс (DECIMAL(20,8), nullable - для limit ордеров)
- `executed_amount`: Исполненная сумма (DECIMAL(20,2), NOT NULL, default: 0.00, CHECK: >= 0)
- `status`: Статус ордера (currency_order_status ENUM, NOT NULL, default: 'pending')
- `fee`: Комиссия за обмен (DECIMAL(10,2), NOT NULL, default: 0.00, CHECK: >= 0)
- `fee_discount`: Скидка на комиссию (DECIMAL(5,2), NOT NULL, default: 0.00, CHECK: 0-100.00)
- `ttl_seconds`: Время жизни ордера в секундах (INTEGER, nullable, CHECK: > 0)
- `expires_at`: Время истечения ордера (TIMESTAMP, nullable)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления
- `executed_at`: Время исполнения (TIMESTAMP, nullable)

**Индексы:**

- По `(player_id, status)` для ордеров игрока
- По `(currency_pair, status, created_at DESC)` для ордеров по паре
- По `(status, expires_at)` для истекающих ордеров (WHERE expires_at IS NOT NULL)
- По `(currency_pair, order_type, side, status)` для matching engine (WHERE status IN ('active', 'pending'))

**Constraints:**

- CHECK (executed_amount <= amount): Исполненная сумма не может превышать общую сумму
- CHECK: (order_type = 'limit' AND limit_rate IS NOT NULL) OR (order_type = 'instant' AND limit_rate IS NULL)

### currency_exchange_trades

Таблица исполненных сделок. Хранит историю исполненных обменов.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `buy_order_id`: ID ордера на покупку (FK к currency_exchange_orders, NOT NULL)
- `sell_order_id`: ID ордера на продажу (FK к currency_exchange_orders, NOT NULL)
- `currency_pair`: Валютная пара (VARCHAR(10), NOT NULL)
- `base_currency`: Базовая валюта (VARCHAR(10), NOT NULL)
- `quote_currency`: Котируемая валюта (VARCHAR(10), NOT NULL)
- `amount`: Сумма сделки в базовой валюте (DECIMAL(20,2), NOT NULL, CHECK: > 0)
- `rate`: Курс исполнения сделки (DECIMAL(20,8), NOT NULL, CHECK: > 0)
- `total`: Общая сумма сделки в котируемой валюте (DECIMAL(20,2), NOT NULL, CHECK: > 0)
- `fee`: Комиссия за сделку (DECIMAL(10,2), NOT NULL, default: 0.00, CHECK: >= 0)
- `executed_at`: Время исполнения

**Индексы:**

- По `buy_order_id` для сделок по ордеру на покупку
- По `sell_order_id` для сделок по ордеру на продажу
- По `(currency_pair, executed_at DESC)` для сделок по паре
- По `executed_at DESC` для временных запросов

**Constraints:**

- CHECK (buy_order_id != sell_order_id): Ордера на покупку и продажу должны быть разными

### currency_exchange_risk_limits

Таблица лимитов рисков для игроков. Хранит AML лимиты, дневные лимиты и лимиты на количество транзакций.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `player_id`: ID игрока (FK к characters, NOT NULL)
- `limit_type`: Тип лимита (currency_risk_limit_type ENUM, NOT NULL)
- `limit_value`: Максимальное значение лимита (DECIMAL(20,2), NOT NULL, CHECK: >= 0)
- `current_value`: Текущее значение лимита (DECIMAL(20,2), NOT NULL, default: 0.00, CHECK: >= 0)
- `period_start`: Начало периода лимита (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `period_end`: Конец периода лимита (TIMESTAMP, NOT NULL)
- `reset_at`: Время сброса лимита (TIMESTAMP, NOT NULL)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `(player_id, limit_type, period_start DESC)` для лимитов игрока
- По `reset_at` для лимитов, требующих сброса (WHERE reset_at > CURRENT_TIMESTAMP)

**Constraints:**

- CHECK (current_value <= limit_value): Текущее значение не может превышать лимит
- CHECK (period_start < period_end): Начало периода должно быть раньше конца
- CHECK (reset_at >= period_end): Время сброса должно быть >= конца периода

## ENUM типы

### currency_order_type

- `instant`: Мгновенный обмен
- `limit`: Лимитный ордер

### currency_order_side

- `buy`: Покупка
- `sell`: Продажа

### currency_order_status

- `pending`: Ожидает активации
- `active`: Активен
- `partially_filled`: Частично исполнен
- `filled`: Полностью исполнен
- `cancelled`: Отменен
- `expired`: Истек
- `blocked`: Заблокирован (риск-контроль)

### currency_risk_limit_type

- `daily_volume`: Дневной объем торговли
- `transaction_count`: Количество транзакций
- `aml_limit`: AML лимит (Anti-Money Laundering)

## Constraints и валидация

### CHECK Constraints

- `currency_exchange_rates.base_currency != quote_currency`: Базовая и котируемая валюты должны быть разными
- `currency_exchange_rates.bid_rate <= ask_rate`: Курс покупки должен быть <= курса продажи
- `currency_exchange_rates.spread >= 0.0000`: Спред должен быть >= 0
- `currency_exchange_orders.amount > 0`: Сумма обмена должна быть > 0
- `currency_exchange_orders.executed_amount >= 0`: Исполненная сумма должна быть >= 0
- `currency_exchange_orders.executed_amount <= amount`: Исполненная сумма не может превышать общую сумму
- `currency_exchange_orders.fee >= 0`: Комиссия должна быть >= 0
- `currency_exchange_orders.fee_discount >= 0 AND fee_discount <= 100.00`: Скидка должна быть в диапазоне 0-100%
- `currency_exchange_orders.ttl_seconds > 0`: Время жизни должно быть > 0 (если указано)
- `currency_exchange_orders`: (order_type = 'limit' AND limit_rate IS NOT NULL) OR (order_type = 'instant' AND
  limit_rate IS NULL)
- `currency_exchange_trades.amount > 0`: Сумма сделки должна быть > 0
- `currency_exchange_trades.rate > 0`: Курс должен быть > 0
- `currency_exchange_trades.total > 0`: Общая сумма должна быть > 0
- `currency_exchange_trades.fee >= 0`: Комиссия должна быть >= 0
- `currency_exchange_trades.buy_order_id != sell_order_id`: Ордера должны быть разными
- `currency_exchange_risk_limits.limit_value >= 0`: Лимит должен быть >= 0
- `currency_exchange_risk_limits.current_value >= 0`: Текущее значение должно быть >= 0
- `currency_exchange_risk_limits.current_value <= limit_value`: Текущее значение не может превышать лимит
- `currency_exchange_risk_limits.period_start < period_end`: Начало периода должно быть раньше конца
- `currency_exchange_risk_limits.reset_at >= period_end`: Время сброса должно быть >= конца периода

### Foreign Keys

- `currency_exchange_orders.player_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `currency_exchange_trades.buy_order_id` → `economy.currency_exchange_orders.id` (ON DELETE CASCADE)
- `currency_exchange_trades.sell_order_id` → `economy.currency_exchange_orders.id` (ON DELETE CASCADE)
- `currency_exchange_risk_limits.player_id` → `mvp_core.character.id` (ON DELETE CASCADE)

### Unique Constraints

- `currency_exchange_rates(currency_pair)`: Одна валютная пара на запись

## Оптимизация запросов

### Частые запросы

1. **Получение текущего курса валютной пары:**
   ```sql
   SELECT * FROM economy.currency_exchange_rates 
   WHERE currency_pair = $1;
   ```
   Использует индекс `currency_pair` (UNIQUE).

2. **Получение истории курсов:**
   ```sql
   SELECT * FROM economy.currency_exchange_rate_history 
   WHERE currency_pair = $1 AND recorded_at >= $2 AND recorded_at <= $3 
   ORDER BY recorded_at ASC;
   ```
   Использует индекс `(currency_pair, recorded_at DESC)`.

3. **Получение активных ордеров игрока:**
   ```sql
   SELECT * FROM economy.currency_exchange_orders 
   WHERE player_id = $1 AND status IN ('active', 'pending') 
   ORDER BY created_at DESC;
   ```
   Использует индекс `(player_id, status)`.

4. **Получение ордеров для matching:**
   ```sql
   SELECT * FROM economy.currency_exchange_orders 
   WHERE currency_pair = $1 AND order_type = $2 AND side = $3 
   AND status IN ('active', 'pending') 
   ORDER BY created_at ASC;
   ```
   Использует индекс `(currency_pair, order_type, side, status)`.

5. **Получение истекающих ордеров:**
   ```sql
   SELECT * FROM economy.currency_exchange_orders 
   WHERE status IN ('active', 'pending') AND expires_at <= CURRENT_TIMESTAMP;
   ```
   Использует индекс `(status, expires_at)`.

6. **Получение сделок игрока:**
   ```sql
   SELECT t.* FROM economy.currency_exchange_trades t
   JOIN economy.currency_exchange_orders o ON (t.buy_order_id = o.id OR t.sell_order_id = o.id)
   WHERE o.player_id = $1 
   ORDER BY t.executed_at DESC;
   ```
   Использует индексы `buy_order_id` и `sell_order_id`.

7. **Получение лимитов игрока:**
   ```sql
   SELECT * FROM economy.currency_exchange_risk_limits 
   WHERE player_id = $1 AND limit_type = $2 
   AND period_start <= CURRENT_TIMESTAMP AND period_end >= CURRENT_TIMESTAMP;
   ```
   Использует индекс `(player_id, limit_type, period_start DESC)`.

## Миграции

### Применение миграций:

```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из
`knowledge/implementation/architecture/economy-currency-exchange-system-architecture.yaml`:

- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE для автоматической очистки
- [OK] Интеграция с существующими таблицами (characters)

## Особенности реализации

### Курсы валют

Система курсов включает:

- **Валютные пары**: currency_pair (например, "EDDY/USD", "USD/EUR")
- **Bid/Ask**: bid_rate (курс покупки) и ask_rate (курс продажи)
- **Спред**: spread (разница между ask и bid)
- **Волатильность**: volatility для отслеживания изменений курса
- **Дневной объем**: daily_volume для отслеживания активности

### История курсов

Система истории включает:

- **Исторические данные**: rate, bid_rate, ask_rate, spread, volume
- **Временные метки**: recorded_at для отслеживания времени
- **Оптимизация**: индексы для быстрого поиска по паре и времени

### Ордера

Система ордеров включает:

- **Типы ордеров**: instant (мгновенный) и limit (лимитный)
- **Стороны**: buy (покупка) и sell (продажа)
- **Жизненный цикл**: pending → active → partially_filled/filled/cancelled/expired/blocked
- **Частичное исполнение**: executed_amount для отслеживания частичного исполнения
- **TTL**: ttl_seconds и expires_at для лимитных ордеров
- **Комиссии**: fee и fee_discount для расчета комиссий

### Сделки

Система сделок включает:

- **Связь ордеров**: buy_order_id и sell_order_id для связи с ордерами
- **Курс исполнения**: rate для курса, по которому была исполнена сделка
- **Суммы**: amount (в базовой валюте) и total (в котируемой валюте)
- **Комиссии**: fee для комиссии за сделку

### Лимиты рисков

Система лимитов включает:

- **Типы лимитов**: daily_volume, transaction_count, aml_limit
- **Периоды**: period_start и period_end для определения периода лимита
- **Сброс**: reset_at для автоматического сброса лимитов
- **Текущее значение**: current_value для отслеживания использования лимита

### Интеграция с другими системами

Система валютной биржи интегрируется с:

- **Characters**: через player_id для ордеров и лимитов
- **Wallet Service**: через блокировку средств при создании ордеров
- **Tax Service**: через расчет налогов с обменов
- **Economy Events**: через влияние событий на курсы валют
- **Analytics Service**: через метрики обменов и волатильности
- **Notification Service**: через уведомления об исполнении ордеров

