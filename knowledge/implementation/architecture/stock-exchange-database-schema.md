<!-- Issue: #140876090 -->

# Stock Exchange System - Database Schema

## Обзор

Схема базы данных для системы фондовой биржи, включающая корпорации, историю цен акций, портфели игроков, дивиденды,
биржевые индексы, влияние событий и алерты о нарушениях.

## ERD Диаграмма

```mermaid
erDiagram
    corporations ||--o{ stock_prices : "has"
    corporations ||--o{ player_portfolios : "owned_by"
    corporations ||--o{ dividend_schedules : "pays"
    corporations ||--o{ index_constituents : "included_in"
    corporations ||--o{ stock_events_impact : "affected_by"
    stock_orders ||--o{ stock_trades : "executed_as"
    character ||--o{ player_portfolios : "owns"
    character ||--o{ stock_orders : "places"
    character ||--o{ dividend_payments : "receives"
    character ||--o{ compliance_alerts : "has"
    dividend_schedules ||--o{ dividend_payments : "pays"
    stock_indices ||--o{ index_constituents : "contains"
    stock_indices ||--o{ index_history : "tracked_in"

    corporations {
        uuid id PK
        varchar symbol UNIQUE
        varchar name
        varchar sector
        stock_type stock_type
        bigint total_shares
        timestamp ipo_date
        timestamp delisted_at
        uuid faction_id
        timestamp created_at
        timestamp updated_at
    }

    stock_prices {
        uuid id PK
        uuid corporation_id FK
        decimal price
        bigint volume
        decimal high
        decimal low
        decimal open
        decimal close
        timestamp timestamp
        uuid event_id
        timestamp created_at
    }

    stock_orders {
        uuid id PK
        uuid character_id FK
        varchar stock_symbol
        varchar order_type
        varchar order_side
        integer quantity
        decimal price
        varchar status
        integer filled_quantity
        timestamp created_at
        timestamp executed_at
        timestamp updated_at
    }

    stock_trades {
        uuid id PK
        uuid buy_order_id FK
        uuid sell_order_id FK
        varchar stock_symbol
        integer quantity
        decimal price
        timestamp executed_at
    }

    player_portfolios {
        uuid id PK
        uuid character_id FK
        uuid corporation_id FK
        bigint quantity
        decimal average_buy_price
        decimal total_invested
        decimal total_dividends_received
        timestamp created_at
        timestamp updated_at
    }

    dividend_schedules {
        uuid id PK
        uuid corporation_id FK
        dividend_type dividend_type
        decimal amount_per_share
        date declaration_date
        date ex_dividend_date
        date record_date
        date payment_date
        dividend_status status
        timestamp created_at
        timestamp updated_at
    }

    dividend_payments {
        uuid id PK
        uuid dividend_schedule_id FK
        uuid character_id FK
        uuid corporation_id FK
        bigint shares_owned
        decimal dividend_amount
        decimal tax_amount
        decimal net_amount
        boolean reinvested
        timestamp paid_at
        timestamp created_at
    }

    stock_indices {
        uuid id PK
        varchar name
        varchar symbol UNIQUE
        index_calculation_method calculation_method
        decimal base_value
        decimal current_value
        timestamp created_at
        timestamp updated_at
    }

    index_constituents {
        uuid id PK
        uuid index_id FK
        uuid corporation_id FK
        decimal weight
        timestamp added_at
        timestamp removed_at
    }

    index_history {
        uuid id PK
        uuid index_id FK
        decimal value
        decimal change
        decimal change_percent
        timestamp timestamp
        timestamp created_at
    }

    stock_events_impact {
        uuid id PK
        uuid event_id
        varchar event_type
        uuid corporation_id FK
        decimal impact_percent
        decimal base_price
        decimal new_price
        integer duration_hours
        timestamp started_at
        timestamp ended_at
        timestamp created_at
    }

    compliance_alerts {
        uuid id PK
        uuid character_id FK
        varchar alert_type
        compliance_severity severity
        text description
        uuid trade_id
        compliance_alert_status status
        timestamp created_at
        timestamp resolved_at
    }
```

## Описание таблиц

### corporations

Таблица корпораций на бирже. Хранит информацию о корпорациях, чьи акции торгуются на бирже.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `symbol`: Символ корпорации на бирже (VARCHAR(10), UNIQUE, NOT NULL)
- `name`: Название корпорации (VARCHAR(255), NOT NULL)
- `sector`: Сектор экономики (VARCHAR(100), nullable)
- `stock_type`: Тип акций (ENUM: Common, Preferred, NOT NULL, default: Common)
- `total_shares`: Общее количество акций (BIGINT, NOT NULL, default: 0, >= 0)
- `ipo_date`: Дата IPO (TIMESTAMP, nullable)
- `delisted_at`: Дата исключения из листинга (TIMESTAMP, nullable)
- `faction_id`: ID фракции (UUID, nullable)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `symbol` для поиска по символу
- По `sector` для фильтрации по сектору
- По `faction_id` для связи с фракциями
- По `delisted_at` для активных корпораций

### stock_prices

Таблица истории цен акций. Хранит историю цен акций корпораций.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `corporation_id`: ID корпорации (FK к corporations, NOT NULL)
- `price`: Цена акции (DECIMAL(20,4), NOT NULL, > 0)
- `volume`: Объем торгов (BIGINT, NOT NULL, default: 0, >= 0)
- `high`: Максимальная цена (DECIMAL(20,4), NOT NULL, > 0)
- `low`: Минимальная цена (DECIMAL(20,4), NOT NULL, > 0)
- `open`: Цена открытия (DECIMAL(20,4), NOT NULL, > 0)
- `close`: Цена закрытия (DECIMAL(20,4), NOT NULL, > 0)
- `timestamp`: Время записи (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `event_id`: ID события, повлиявшего на цену (UUID, nullable)
- `created_at`: Время создания

**Индексы:**

- По `(corporation_id, timestamp DESC)` для истории цен корпорации
- По `timestamp DESC` для последних цен
- По `event_id` для связи с событиями

### player_portfolios

Таблица портфелей игроков. Хранит информацию об акциях, принадлежащих игрокам.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `character_id`: ID персонажа (FK к characters, NOT NULL)
- `corporation_id`: ID корпорации (FK к corporations, NOT NULL)
- `quantity`: Количество акций (BIGINT, NOT NULL, default: 0, >= 0)
- `average_buy_price`: Средняя цена покупки (DECIMAL(20,4), NOT NULL, default: 0.0, >= 0)
- `total_invested`: Общая сумма инвестиций (DECIMAL(20,4), NOT NULL, default: 0.0, >= 0)
- `total_dividends_received`: Общая сумма полученных дивидендов (DECIMAL(20,4), NOT NULL, default: 0.0, >= 0)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `character_id` для портфеля персонажа
- По `corporation_id` для владельцев акций корпорации

**Constraints:**

- UNIQUE(character_id, corporation_id): Один портфель на персонажа и корпорацию

### dividend_schedules

Таблица расписания дивидендов. Хранит информацию о запланированных и выплаченных дивидендах.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `corporation_id`: ID корпорации (FK к corporations, NOT NULL)
- `dividend_type`: Тип дивидендов (ENUM: QUARTERLY, ANNUAL, NOT NULL)
- `amount_per_share`: Сумма дивидендов на акцию (DECIMAL(20,4), NOT NULL, > 0)
- `declaration_date`: Дата объявления (DATE, NOT NULL)
- `ex_dividend_date`: Дата ex-dividend (DATE, NOT NULL)
- `record_date`: Дата записи (DATE, NOT NULL)
- `payment_date`: Дата выплаты (DATE, NOT NULL)
- `status`: Статус дивидендов (ENUM: SCHEDULED, DECLARED, PAID, NOT NULL, default: SCHEDULED)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `(corporation_id, status)` для дивидендов корпорации
- По `(payment_date, status)` для предстоящих выплат
- По `status` для фильтрации по статусу

### dividend_payments

Таблица выплат дивидендов. Хранит информацию о выплатах дивидендов игрокам.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `dividend_schedule_id`: ID расписания дивидендов (FK к dividend_schedules, NOT NULL)
- `character_id`: ID персонажа (FK к characters, NOT NULL)
- `corporation_id`: ID корпорации (FK к corporations, NOT NULL)
- `shares_owned`: Количество акций в собственности (BIGINT, NOT NULL, > 0)
- `dividend_amount`: Сумма дивидендов (DECIMAL(20,4), NOT NULL, > 0)
- `tax_amount`: Сумма налога (DECIMAL(20,4), NOT NULL, default: 0.0, >= 0)
- `net_amount`: Чистая сумма после налогов (DECIMAL(20,4), NOT NULL, > 0)
- `reinvested`: Флаг реинвестирования (BOOLEAN, NOT NULL, default: false)
- `paid_at`: Время выплаты (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `created_at`: Время создания

**Индексы:**

- По `dividend_schedule_id` для выплат по расписанию
- По `(character_id, paid_at DESC)` для истории выплат персонажа
- По `corporation_id` для выплат корпорации

### stock_indices

Таблица биржевых индексов. Хранит информацию о биржевых индексах.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `name`: Название индекса (VARCHAR(255), NOT NULL)
- `symbol`: Символ индекса (VARCHAR(20), UNIQUE, NOT NULL)
- `calculation_method`: Метод расчета (ENUM: PRICE_WEIGHTED, MARKET_CAP_WEIGHTED, NOT NULL)
- `base_value`: Базовое значение (DECIMAL(20,4), NOT NULL, default: 1000.0, > 0)
- `current_value`: Текущее значение (DECIMAL(20,4), NOT NULL, default: 1000.0, > 0)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**

- По `symbol` для поиска по символу

### index_constituents

Таблица состава индексов. Хранит информацию о корпорациях, входящих в индексы.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `index_id`: ID индекса (FK к stock_indices, NOT NULL)
- `corporation_id`: ID корпорации (FK к corporations, NOT NULL)
- `weight`: Вес корпорации в индексе (DECIMAL(10,6), NOT NULL, 0-1)
- `added_at`: Время добавления в индекс (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `removed_at`: Время удаления из индекса (TIMESTAMP, nullable)

**Индексы:**

- По `(index_id, removed_at)` для активных корпораций в индексе
- По `corporation_id` для индексов, содержащих корпорацию

**Constraints:**

- UNIQUE(index_id, corporation_id, removed_at): Одна запись на комбинацию

### index_history

Таблица истории индексов. Хранит историю значений индексов.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `index_id`: ID индекса (FK к stock_indices, NOT NULL)
- `value`: Значение индекса (DECIMAL(20,4), NOT NULL, > 0)
- `change`: Изменение значения (DECIMAL(20,4), NOT NULL)
- `change_percent`: Изменение в процентах (DECIMAL(10,4), NOT NULL)
- `timestamp`: Время записи (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `created_at`: Время создания

**Индексы:**

- По `(index_id, timestamp DESC)` для истории индекса
- По `timestamp DESC` для последних значений

### stock_events_impact

Таблица влияния событий на акции. Хранит информацию о влиянии событий на цены акций.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `event_id`: ID события (UUID, NOT NULL)
- `event_type`: Тип события (VARCHAR(50), NOT NULL)
- `corporation_id`: ID корпорации (FK к corporations, NOT NULL)
- `impact_percent`: Процент влияния (DECIMAL(10,4), NOT NULL)
- `base_price`: Базовая цена (DECIMAL(20,4), NOT NULL, > 0)
- `new_price`: Новая цена (DECIMAL(20,4), NOT NULL, > 0)
- `duration_hours`: Длительность влияния в часах (INTEGER, NOT NULL, > 0)
- `started_at`: Время начала влияния (TIMESTAMP, NOT NULL, default: CURRENT_TIMESTAMP)
- `ended_at`: Время окончания влияния (TIMESTAMP, nullable)
- `created_at`: Время создания

**Индексы:**

- По `event_id` для связи с событиями
- По `(corporation_id, started_at DESC)` для влияний на корпорацию
- По `ended_at` для активных влияний

### compliance_alerts

Таблица алертов о нарушениях. Хранит информацию о нарушениях правил торговли.

**Ключевые поля:**

- `id`: UUID первичный ключ
- `character_id`: ID персонажа (FK к characters, NOT NULL)
- `alert_type`: Тип алерта (VARCHAR(50), NOT NULL)
- `severity`: Серьезность нарушения (ENUM: LOW, MEDIUM, HIGH, CRITICAL, NOT NULL)
- `description`: Описание нарушения (TEXT, nullable)
- `trade_id`: ID сделки (UUID, nullable)
- `status`: Статус алерта (ENUM: OPEN, INVESTIGATING, RESOLVED, NOT NULL, default: OPEN)
- `created_at`: Время создания
- `resolved_at`: Время разрешения (TIMESTAMP, nullable)

**Индексы:**

- По `(character_id, status)` для алертов персонажа
- По `(status, created_at DESC)` для открытых алертов
- По `(severity, status)` для критичных нарушений

## ENUM типы

### stock_type

- `Common`: Обычные акции
- `Preferred`: Привилегированные акции

### dividend_type

- `QUARTERLY`: Квартальные дивиденды
- `ANNUAL`: Годовые дивиденды

### dividend_status

- `SCHEDULED`: Запланировано
- `DECLARED`: Объявлено
- `PAID`: Выплачено

### index_calculation_method

- `PRICE_WEIGHTED`: Взвешенный по цене
- `MARKET_CAP_WEIGHTED`: Взвешенный по рыночной капитализации

### compliance_severity

- `LOW`: Низкая
- `MEDIUM`: Средняя
- `HIGH`: Высокая
- `CRITICAL`: Критическая

### compliance_alert_status

- `OPEN`: Открыт
- `INVESTIGATING`: Расследуется
- `RESOLVED`: Разрешен

## Constraints и валидация

### CHECK Constraints

- `corporations.total_shares`: Должно быть >= 0
- `stock_prices.price`: Должно быть > 0
- `stock_prices.volume`: Должно быть >= 0
- `stock_prices.high`: Должно быть > 0
- `stock_prices.low`: Должно быть > 0
- `stock_prices.open`: Должно быть > 0
- `stock_prices.close`: Должно быть > 0
- `player_portfolios.quantity`: Должно быть >= 0
- `player_portfolios.average_buy_price`: Должно быть >= 0
- `player_portfolios.total_invested`: Должно быть >= 0
- `player_portfolios.total_dividends_received`: Должно быть >= 0
- `dividend_schedules.amount_per_share`: Должно быть > 0
- `dividend_payments.shares_owned`: Должно быть > 0
- `dividend_payments.dividend_amount`: Должно быть > 0
- `dividend_payments.tax_amount`: Должно быть >= 0
- `dividend_payments.net_amount`: Должно быть > 0
- `stock_indices.base_value`: Должно быть > 0
- `stock_indices.current_value`: Должно быть > 0
- `index_constituents.weight`: Должно быть >= 0 и <= 1
- `index_history.value`: Должно быть > 0
- `stock_events_impact.base_price`: Должно быть > 0
- `stock_events_impact.new_price`: Должно быть > 0
- `stock_events_impact.duration_hours`: Должно быть > 0

### Foreign Keys

- `stock_prices.corporation_id` → `economy.corporations.id` (ON DELETE CASCADE)
- `player_portfolios.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `player_portfolios.corporation_id` → `economy.corporations.id` (ON DELETE CASCADE)
- `dividend_schedules.corporation_id` → `economy.corporations.id` (ON DELETE CASCADE)
- `dividend_payments.dividend_schedule_id` → `economy.dividend_schedules.id` (ON DELETE CASCADE)
- `dividend_payments.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `dividend_payments.corporation_id` → `economy.corporations.id` (ON DELETE CASCADE)
- `index_constituents.index_id` → `economy.stock_indices.id` (ON DELETE CASCADE)
- `index_constituents.corporation_id` → `economy.corporations.id` (ON DELETE CASCADE)
- `index_history.index_id` → `economy.stock_indices.id` (ON DELETE CASCADE)
- `stock_events_impact.corporation_id` → `economy.corporations.id` (ON DELETE CASCADE)
- `compliance_alerts.character_id` → `mvp_core.character.id` (ON DELETE CASCADE)

### Unique Constraints

- `corporations(symbol)`: Один символ на корпорацию
- `player_portfolios(character_id, corporation_id)`: Один портфель на персонажа и корпорацию
- `stock_indices(symbol)`: Один символ на индекс
- `index_constituents(index_id, corporation_id, removed_at)`: Одна запись на комбинацию

## Оптимизация запросов

### Частые запросы

1. **Получение текущей цены акции:**
   ```sql
   SELECT * FROM economy.stock_prices 
   WHERE corporation_id = $1 
   ORDER BY timestamp DESC 
   LIMIT 1;
   ```
   Использует индекс `(corporation_id, timestamp DESC)`.

2. **Получение портфеля персонажа:**
   ```sql
   SELECT * FROM economy.player_portfolios 
   WHERE character_id = $1;
   ```
   Использует индекс `character_id`.

3. **Получение предстоящих дивидендов:**
   ```sql
   SELECT * FROM economy.dividend_schedules 
   WHERE payment_date >= CURRENT_DATE 
   AND status = 'SCHEDULED' 
   ORDER BY payment_date;
   ```
   Использует индекс `(payment_date, status)`.

4. **Получение истории индекса:**
   ```sql
   SELECT * FROM economy.index_history 
   WHERE index_id = $1 
   ORDER BY timestamp DESC 
   LIMIT 100;
   ```
   Использует индекс `(index_id, timestamp DESC)`.

5. **Получение активных влияний событий:**
   ```sql
   SELECT * FROM economy.stock_events_impact 
   WHERE corporation_id = $1 
   AND ended_at IS NULL;
   ```
   Использует индекс `(corporation_id, started_at DESC)`.

## Миграции

### Существующие миграции:

- `V1_51__economy_trading_markets_auctions_tables.sql` - базовые таблицы (stock_orders, stock_trades)
- `V1_61__stock_exchange_system_tables.sql` - полная схема Stock Exchange

### Применение миграций:

```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/implementation/architecture/stock-exchange-database.yaml`:

- [OK] Все таблицы из архитектуры созданы
- [OK] Все поля соответствуют описанию
- [OK] ENUM типы созданы для типов акций, дивидендов, индексов и алертов
- [OK] Индексы оптимизированы для частых запросов
- [OK] Constraints обеспечивают целостность данных
- [OK] Foreign Keys настроены с CASCADE для автоматической очистки
- [OK] Интеграция с существующими таблицами (characters, stock_orders, stock_trades)

## Особенности реализации

### Корпорации

Система корпораций включает:

- **Символы**: уникальные символы на бирже
- **Типы акций**: Common и Preferred
- **Листинг**: отслеживание IPO и исключения из листинга
- **Связь с фракциями**: faction_id для игрового мира

### История цен

Система истории цен включает:

- **OHLC данные**: open, high, low, close
- **Объем торгов**: volume для анализа
- **Связь с событиями**: event_id для отслеживания влияния

### Портфели игроков

Система портфелей включает:

- **Количество акций**: quantity для отслеживания владения
- **Средняя цена покупки**: average_buy_price для расчета прибыли
- **Инвестиции**: total_invested для учета вложений
- **Дивиденды**: total_dividends_received для учета доходов

### Дивиденды

Система дивидендов включает:

- **Расписание**: declaration_date, ex_dividend_date, record_date, payment_date
- **Типы**: QUARTERLY и ANNUAL
- **Выплаты**: автоматический расчет на основе количества акций
- **Налоги**: tax_amount для учета налогов
- **Реинвестирование**: флаг reinvested для автоматического реинвестирования

### Биржевые индексы

Система индексов включает:

- **Методы расчета**: PRICE_WEIGHTED и MARKET_CAP_WEIGHTED
- **Состав**: index_constituents с весами корпораций
- **История**: index_history для отслеживания изменений
- **Базовое значение**: base_value для расчета изменений

### Влияние событий

Система влияния событий включает:

- **Процент влияния**: impact_percent для расчета изменения цены
- **Длительность**: duration_hours для временного влияния
- **Активность**: ended_at для отслеживания активных влияний

### Алерты о нарушениях

Система алертов включает:

- **Серьезность**: LOW, MEDIUM, HIGH, CRITICAL
- **Статус**: OPEN, INVESTIGATING, RESOLVED
- **Связь со сделками**: trade_id для отслеживания нарушений

### Интеграция с другими системами

Система фондовой биржи интегрируется с:

- **Stock Orders/Trades**: через stock_orders и stock_trades (V1_51)
- **Characters**: через character_id для портфелей и алертов
- **World Events**: через event_id для влияния на цены
- **Factions**: через faction_id для связи корпораций с фракциями

