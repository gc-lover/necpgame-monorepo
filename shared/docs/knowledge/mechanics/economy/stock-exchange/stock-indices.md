# Биржа акций - Фондовые индексы

**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-07 16:19  
**Приоритет:** средний (Post-MVP)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: расчёты индексов, ребаланс и REST/событийные интерфейсы в силе, блокеров нет.

---

## Краткое описание

Фондовые индексы для отслеживания общей динамики рынка.

**Микрофича:** Stock market indices (Corporate Index, Regional Indices)

---

## 📊 Индексы в NECPGAME

### 1. Corporate Index (CORP100)

**Описание:** Главный индекс рынка (топ-100 корпораций)

**Состав:**
- 100 крупнейших корпораций по market cap
- Weighted by market cap
- Rebalanced quarterly

**Calculation:**
```
CORP100 = Σ(price_i × shares_i × weight_i) / divisor

weight_i = market_cap_i / total_market_cap

Пример:
Arasaka: market cap 2.5T / total 10T = 25% weight
Militech: market cap 1.8T / total 10T = 18% weight
Etc.
```

**Current value:**
```
CORP100: 15,234.56 points
24h: +1.2% (↗)
YTD: +18.5%
All-time high: 16,500 (last month)
```

### 2. NC50 (Night City 50)

**Описание:** Индекс корпораций Night City

**Состав:**
- 50 корпораций с HQ в Night City
- Weighted by market cap
- Фокус на локальную экономику

**Value:**
```
NC50: 8,456.23 points
24h: +0.8%
YTD: +22.3% (outperforming CORP100!)
```

### 3. ASIA25 (Asian Corporations)

**Описание:** Азиатские корпорации

**Состав:**
- Arasaka, Kang Tao, и другие азиатские
- 25 корпораций

**Value:**
```
ASIA25: 12,345.67 points
24h: +1.5%
YTD: +16.2%
```

### 4. EURO30 (European Corporations)

**Описание:** Европейские корпорации

**Состав:**
- EBM, Biotechnica, и другие европейские
- 30 корпораций

**Value:**
```
EURO30: 9,876.54 points
24h: +0.3%
YTD: +12.8%
```

### 5. Sector Indices (Секторальные)

**DEFENSE** - оборонные корпорации  
**TECH** - технологические  
**ENERGY** - энергетические  
**MEDICAL** - медицинские  
**CYBER** - cybernetics  

---

## 📈 Использование индексов

### Tracking Market Sentiment

**Bull Market (Растущий):**
```
CORP100: +20% YTD
Sentiment: BULLISH 🟢
Most stocks rising
```

**Bear Market (Падающий):**
```
CORP100: -15% YTD
Sentiment: BEARISH 🔴
Most stocks falling
```

### Index Funds (опционально, Post-MVP)

**Концепция:** Покупать весь индекс одним кликом

**Механика:**
```
Buy CORP100 Index Fund:
- Automatically buys all 100 stocks in proportion
- Instant diversification
- Lower fee (0.5% vs 1%)
- Tracks index performance

Example:
Invest 100,000 eddies in CORP100 Fund
→ Owns small portions of all 100 stocks
→ Returns match CORP100 performance (+18.5% YTD)
```

---

## 🗄️ Структура данных

```sql
CREATE TABLE stock_indices (
    id UUID PRIMARY KEY,
    code VARCHAR(16) UNIQUE NOT NULL, -- CORP100, NC50, ASIA25
    name VARCHAR(100) NOT NULL,
    description TEXT,
    weighting_method VARCHAR(32) NOT NULL, -- MARKET_CAP, EQUAL, SECTOR
    divisor DECIMAL(18,8) NOT NULL,
    last_rebalanced_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_index_constituents (
    index_id UUID NOT NULL REFERENCES stock_indices(id) ON DELETE CASCADE,
    corporation_id VARCHAR(100) NOT NULL REFERENCES stock_corporations(id) ON DELETE CASCADE,
    weight_percent DECIMAL(6,4) NOT NULL,
    shares_included DECIMAL(18,4) NOT NULL,
    PRIMARY KEY (index_id, corporation_id)
);

CREATE TABLE stock_index_history (
    id SERIAL PRIMARY KEY,
    index_id UUID NOT NULL REFERENCES stock_indices(id) ON DELETE CASCADE,
    value DECIMAL(18,4) NOT NULL,
    change_percent DECIMAL(7,4) NOT NULL,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🔁 Ребалансировка

- **Расписание:** квартально (`cron: 0 0 3 1 */3`), экстренно при IPO/delisting.
- **Алгоритм:** пересчёт веса = `market_cap / total_market_cap`; ограничение веса ≤ 10%.
- **Divisor adjustment:** поддержание непрерывности индекса при сплитах/замене компаний.
- **Репорты:** сохраняются в `stock_index_history`, публикуется changelog.

---

## 🌐 API индексов

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/stocks/indices` | `GET` | Список индексов, текущие значения |
| `/stocks/indices/{code}` | `GET` | Структура, веса, история |
| `/stocks/indices/{code}/history` | `GET` | Свечные данные, интервал (1d/1h/5m) |
| `/stocks/indices/{code}/constituents` | `GET` | Состав с весами |
| `/stocks/admin/indices/rebalance` | `POST` | Форсировать ребаланс (админ) |
| `/stocks/admin/indices/{code}/constituents` | `PATCH` | Обновить состав (IPO/delisting) |

**Event bus (`economy.indices.*`):** `rebalance_started`, `rebalance_completed`, `constituent_added`, `constituent_removed`, `divisor_adjusted`.

---

## 🔄 Интеграции

- `stock-analytics`: графики индексов, сравнения.
- `economy-events`: массовые события меняют веса/значения.
- `economy-investments`: индексные фонды используют этот сервис.
- `guild-system`: гильдейские отчёты по секторам.

---

## 🔗 Связанные документы

- `stock-exchange-overview.md`
- `stock-corporations.md`

---

## История изменений

- v1.1.0 (2025-11-07 16:19) - Добавлены БД, ребаланс, REST API и интеграции
- v1.0.0 (2025-11-06 21:45) - Создание документа об индексах

