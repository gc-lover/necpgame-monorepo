<!-- Issue: #140890164 -->
# Economy Analytics System - Database Schema

## Обзор

Схема базы данных для системы экономической аналитики, включающая данные для графиков, технические индикаторы, анализ настроений, снимки портфеля, оповещения и настройки игроков.

## ERD Диаграмма

```mermaid
erDiagram
    analytics_chart_data ||--o{ analytics_indicators : "calculated_from"
    analytics_chart_data ||--o{ analytics_sentiment : "analyzed_from"
    character ||--o{ analytics_portfolio_snapshots : "has"
    character ||--o{ analytics_alerts : "creates"
    character ||--o{ analytics_settings : "configures"

    analytics_chart_data {
        uuid id PK
        varchar symbol
        chart_type chart_type
        varchar time_frame
        timestamp timestamp
        decimal open
        decimal high
        decimal low
        decimal close
        decimal volume
        jsonb data
        timestamp created_at
    }

    analytics_indicators {
        uuid id PK
        varchar symbol
        varchar indicator_type
        jsonb parameters
        timestamp timestamp
        decimal value
        indicator_signal signal
        timestamp created_at
    }

    analytics_sentiment {
        uuid id PK
        varchar symbol
        timestamp timestamp
        integer bullish_signals
        integer bearish_signals
        decimal fear_greed_index
        decimal sentiment_score
        timestamp created_at
    }

    analytics_portfolio_snapshots {
        uuid id PK
        uuid player_id FK
        timestamp timestamp
        decimal total_value
        decimal total_cost
        decimal total_return
        decimal total_return_percentage
        decimal sharpe_ratio
        decimal win_rate
        decimal profit_factor
        timestamp created_at
    }

    analytics_alerts {
        uuid id PK
        uuid player_id FK
        alert_type alert_type
        varchar symbol
        varchar event_type
        alert_condition condition
        decimal target_value
        boolean triggered
        timestamp triggered_at
        boolean active
        timestamp created_at
        timestamp updated_at
    }

    analytics_settings {
        uuid id PK
        uuid player_id FK UNIQUE
        jsonb chart_preferences
        jsonb indicator_preferences
        jsonb alert_preferences
        timestamp updated_at
    }
```

## Описание таблиц

### analytics_chart_data

Таблица данных для графиков. Хранит исторические данные цен для различных типов графиков.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `symbol`: Символ актива (VARCHAR(50), NOT NULL)
- `chart_type`: Тип графика (chart_type ENUM, NOT NULL)
- `time_frame`: Таймфрейм (VARCHAR(20), NOT NULL: '1m', '5m', '15m', '1h', '4h', '1d', '1w')
- `timestamp`: Временная метка данных (TIMESTAMP, NOT NULL)
- `open`: Цена открытия (DECIMAL(20,8), nullable)
- `high`: Максимальная цена (DECIMAL(20,8), nullable)
- `low`: Минимальная цена (DECIMAL(20,8), nullable)
- `close`: Цена закрытия (DECIMAL(20,8), NOT NULL)
- `volume`: Объем торгов (DECIMAL(20,2), nullable)
- `data`: Дополнительные данные (JSONB, default: {})
- `created_at`: Время создания записи

**Индексы:**
- По `(symbol, chart_type, time_frame, timestamp DESC)` для быстрого поиска данных графика
- По `timestamp DESC` для временных запросов
- По `(symbol, timestamp DESC)` для данных по символу

### analytics_indicators

Таблица технических индикаторов. Хранит рассчитанные технические индикаторы.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `symbol`: Символ актива (VARCHAR(50), NOT NULL)
- `indicator_type`: Тип индикатора (VARCHAR(50), NOT NULL: 'MA', 'RSI', 'MACD', 'BollingerBands' и др.)
- `parameters`: Параметры индикатора (JSONB, default: {})
- `timestamp`: Временная метка расчета (TIMESTAMP, NOT NULL)
- `value`: Значение индикатора (DECIMAL(20,8), NOT NULL)
- `signal`: Сигнал индикатора (indicator_signal ENUM, nullable: 'buy', 'sell', 'neutral')
- `created_at`: Время создания записи

**Индексы:**
- По `(symbol, indicator_type, timestamp DESC)` для быстрого поиска индикатора
- По `timestamp DESC` для временных запросов
- По `(symbol, timestamp DESC)` для индикаторов по символу

### analytics_sentiment

Таблица анализа настроений. Хранит анализ настроений рынка.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `symbol`: Символ актива (VARCHAR(50), nullable - NULL для общего рынка)
- `timestamp`: Временная метка анализа (TIMESTAMP, NOT NULL)
- `bullish_signals`: Количество бычьих сигналов (INTEGER, NOT NULL, default: 0)
- `bearish_signals`: Количество медвежьих сигналов (INTEGER, NOT NULL, default: 0)
- `fear_greed_index`: Индекс страха и жадности (DECIMAL(5,2), NOT NULL, default: 0.00, CHECK: 0.00-100.00)
- `sentiment_score`: Общий индекс настроений (DECIMAL(5,2), NOT NULL, default: 0.00, CHECK: -100.00 to 100.00)
- `created_at`: Время создания записи

**Индексы:**
- По `(symbol, timestamp DESC)` для настроений по символу (WHERE symbol IS NOT NULL)
- По `timestamp DESC` для временных запросов
- По `timestamp DESC` для общего рынка (WHERE symbol IS NULL)

### analytics_portfolio_snapshots

Таблица снимков портфеля. Хранит исторические снимки портфеля для аналитики.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `player_id`: ID игрока (FK к characters, NOT NULL)
- `timestamp`: Временная метка снимка (TIMESTAMP, NOT NULL)
- `total_value`: Общая стоимость портфеля (DECIMAL(20,2), NOT NULL, default: 0.00)
- `total_cost`: Общая стоимость покупки портфеля (DECIMAL(20,2), NOT NULL, default: 0.00)
- `total_return`: Общая доходность портфеля (DECIMAL(20,2), NOT NULL, default: 0.00)
- `total_return_percentage`: Процентная доходность портфеля (DECIMAL(5,2), NOT NULL, default: 0.00)
- `sharpe_ratio`: Коэффициент Шарпа (DECIMAL(5,2), nullable)
- `win_rate`: Процент выигрышных сделок (DECIMAL(5,2), nullable, CHECK: 0.00-100.00)
- `profit_factor`: Фактор прибыли (DECIMAL(5,2), nullable)
- `created_at`: Время создания записи

**Индексы:**
- По `(player_id, timestamp DESC)` для снимков портфеля игрока
- По `timestamp DESC` для временных запросов

### analytics_alerts

Таблица оповещений игроков. Хранит оповещения о ценах и событиях.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `player_id`: ID игрока (FK к characters, NOT NULL)
- `alert_type`: Тип оповещения (alert_type ENUM, NOT NULL: 'price', 'event')
- `symbol`: Символ актива (VARCHAR(50), nullable - для ценовых оповещений)
- `event_type`: Тип события (VARCHAR(50), nullable - для событийных оповещений)
- `condition`: Условие оповещения (alert_condition ENUM, NOT NULL: 'above', 'below', 'equals', 'change_percentage')
- `target_value`: Целевое значение для условия (DECIMAL(20,8), nullable)
- `triggered`: Сработало ли оповещение (BOOLEAN, NOT NULL, default: false)
- `triggered_at`: Время срабатывания (TIMESTAMP, nullable)
- `active`: Активно ли оповещение (BOOLEAN, NOT NULL, default: true)
- `created_at`: Время создания
- `updated_at`: Время последнего обновления

**Индексы:**
- По `(player_id, active)` для активных оповещений игрока (WHERE active = true)
- По `(symbol, active, triggered)` для оповещений по символу (WHERE symbol IS NOT NULL AND active = true)
- По `triggered_at DESC` для сработавших оповещений (WHERE triggered = true)

**Constraints:**
- CHECK: (alert_type = 'price' AND symbol IS NOT NULL AND event_type IS NULL) OR (alert_type = 'event' AND event_type IS NOT NULL)

### analytics_settings

Таблица настроек аналитики игроков. Хранит настройки графиков, индикаторов и оповещений.

**Ключевые поля:**
- `id`: UUID первичный ключ
- `player_id`: ID игрока (FK к characters, NOT NULL, UNIQUE)
- `chart_preferences`: Настройки графиков (JSONB, default: {})
- `indicator_preferences`: Выбранные индикаторы (JSONB, default: {})
- `alert_preferences`: Настройки оповещений (JSONB, default: {})
- `updated_at`: Время последнего обновления

**Индексы:**
- По `player_id` для быстрого поиска настроек игрока

**Constraints:**
- UNIQUE(player_id): Один набор настроек на игрока

## ENUM типы

### chart_type
- `line`: Линейный график
- `candlestick`: Свечной график
- `ohlc`: График OHLC (Open-High-Low-Close)
- `area`: График области
- `volume`: Гистограмма объема

### indicator_signal
- `buy`: Сигнал на покупку
- `sell`: Сигнал на продажу
- `neutral`: Нейтральный сигнал

### alert_type
- `price`: Ценовое оповещение
- `event`: Событийное оповещение

### alert_condition
- `above`: Выше целевого значения
- `below`: Ниже целевого значения
- `equals`: Равно целевому значению
- `change_percentage`: Изменение в процентах

## Constraints и валидация

### CHECK Constraints

- `analytics_chart_data.time_frame`: Должно быть одним из: '1m', '5m', '15m', '1h', '4h', '1d', '1w'
- `analytics_sentiment.fear_greed_index`: Должно быть в диапазоне 0.00-100.00
- `analytics_sentiment.sentiment_score`: Должно быть в диапазоне -100.00 to 100.00
- `analytics_portfolio_snapshots.win_rate`: Должно быть в диапазоне 0.00-100.00 (если не NULL)
- `analytics_alerts`: Проверка соответствия alert_type и полей (symbol/event_type)

### Foreign Keys

- `analytics_portfolio_snapshots.player_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `analytics_alerts.player_id` → `mvp_core.character.id` (ON DELETE CASCADE)
- `analytics_settings.player_id` → `mvp_core.character.id` (ON DELETE CASCADE)

### Unique Constraints

- `analytics_settings(player_id)`: Один набор настроек на игрока

## Оптимизация запросов

### Частые запросы

1. **Получение данных графика:**
   ```sql
   SELECT * FROM analytics.analytics_chart_data 
   WHERE symbol = $1 AND chart_type = $2 AND time_frame = $3 
   AND timestamp >= $4 AND timestamp <= $5 
   ORDER BY timestamp ASC;
   ```
   Использует индекс `(symbol, chart_type, time_frame, timestamp DESC)`.

2. **Получение технических индикаторов:**
   ```sql
   SELECT * FROM analytics.analytics_indicators 
   WHERE symbol = $1 AND indicator_type = $2 
   AND timestamp >= $3 AND timestamp <= $4 
   ORDER BY timestamp ASC;
   ```
   Использует индекс `(symbol, indicator_type, timestamp DESC)`.

3. **Получение настроений:**
   ```sql
   SELECT * FROM analytics.analytics_sentiment 
   WHERE symbol = $1 AND timestamp >= $2 AND timestamp <= $3 
   ORDER BY timestamp ASC;
   ```
   Использует индекс `(symbol, timestamp DESC)`.

4. **Получение снимков портфеля:**
   ```sql
   SELECT * FROM analytics.analytics_portfolio_snapshots 
   WHERE player_id = $1 AND timestamp >= $2 AND timestamp <= $3 
   ORDER BY timestamp ASC;
   ```
   Использует индекс `(player_id, timestamp DESC)`.

5. **Получение активных оповещений:**
   ```sql
   SELECT * FROM analytics.analytics_alerts 
   WHERE player_id = $1 AND active = true;
   ```
   Использует индекс `(player_id, active)`.

6. **Проверка сработавших оповещений:**
   ```sql
   SELECT * FROM analytics.analytics_alerts 
   WHERE symbol = $1 AND active = true AND triggered = false 
   AND condition = $2 AND target_value <= $3;
   ```
   Использует индекс `(symbol, active, triggered)`.

## Миграции

### Применение миграций:
```bash
liquibase update --changelog-file=infrastructure/liquibase/changelog.yaml
```

## Соответствие архитектуре

Схема БД полностью соответствует архитектуре из `knowledge/implementation/architecture/economy-analytics-system-architecture.yaml`:
- OK Все таблицы из архитектуры созданы
- OK Все поля соответствуют описанию
- OK Индексы оптимизированы для частых запросов
- OK Constraints обеспечивают целостность данных
- OK Foreign Keys настроены с CASCADE для автоматической очистки
- OK Интеграция с существующими таблицами (characters)

## Особенности реализации

### Графики

Система графиков включает:
- **Типы графиков**: line, candlestick, ohlc, area, volume
- **Таймфреймы**: 1m, 5m, 15m, 1h, 4h, 1d, 1w
- **OHLC данные**: open, high, low, close для свечных и OHLC графиков
- **Объем**: volume для гистограмм объема
- **Дополнительные данные**: data (JSONB) для расширенных данных

### Технические индикаторы

Система индикаторов включает:
- **Типы индикаторов**: MA, RSI, MACD, BollingerBands и др.
- **Параметры**: parameters (JSONB) для гибкой настройки индикаторов
- **Сигналы**: buy, sell, neutral для торговых сигналов
- **Временные метки**: timestamp для отслеживания времени расчета

### Анализ настроений

Система настроений включает:
- **Бычьи/медвежьи сигналы**: bullish_signals и bearish_signals для баланса сигналов
- **Индекс страха и жадности**: fear_greed_index (0.00-100.00)
- **Общий индекс настроений**: sentiment_score (-100.00 to 100.00)
- **Символы**: symbol (nullable) для настроений по символу или общего рынка

### Портфельная аналитика

Система портфельной аналитики включает:
- **Метрики стоимости**: total_value, total_cost, total_return, total_return_percentage
- **Метрики риска**: sharpe_ratio для риск-скорректированной доходности
- **Метрики торговли**: win_rate, profit_factor для анализа торговли
- **Снимки**: timestamp для исторических снимков портфеля

### Оповещения

Система оповещений включает:
- **Типы оповещений**: price (ценовые) и event (событийные)
- **Условия**: above, below, equals, change_percentage для различных условий
- **Целевые значения**: target_value для условий оповещений
- **Срабатывание**: triggered и triggered_at для отслеживания срабатывания
- **Активность**: active для управления оповещениями

### Настройки

Система настроек включает:
- **Предпочтения графиков**: chart_preferences (JSONB) для настройки графиков
- **Предпочтения индикаторов**: indicator_preferences (JSONB) для выбранных индикаторов
- **Предпочтения оповещений**: alert_preferences (JSONB) для настройки оповещений
- **Уникальность**: один набор настроек на игрока

### Интеграция с другими системами

Система аналитики интегрируется с:
- **Characters**: через player_id для портфельной аналитики, оповещений и настроек
- **Economy Service**: через symbol для данных о ценах
- **Stock Exchange Service**: через symbol для данных об акциях
- **Investment Service**: через player_id для портфельной аналитики
- **Notification Service**: через analytics_alerts для отправки оповещений
- **Event Bus**: через события для обновления данных


