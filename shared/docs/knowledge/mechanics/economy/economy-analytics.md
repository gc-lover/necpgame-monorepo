# Экономика - Аналитика и графики (Analytics & Charts)

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-06 22:23  
**Приоритет:** средний (расширение)

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:22
**api-readiness-notes:** Перепроверено 2025-11-09 03:22: материалы по аналитике и API остаются полными, готовы для постановки economy-service.

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/analytics

---
**API Tasks Status:**
- Status: completed
- Tasks:
  - API-TASK-103: Economy Analytics API — `api/v1/economy/analytics/analytics.yaml`
    - Создано: 2025-11-09 18:56
    - Завершено: 2025-11-09 21:55
    - Доп. файлы: `analytics-models.yaml`, `analytics-models-operations.yaml`, `README.md`
    - Файл задачи: `API-SWAGGER/tasks/completed/2025-11-09/task-103-economy-analytics-api.md`
- Last Updated: 2025-11-09 21:55
---

---

- **Status:** created
- **Last Updated:** 2025-11-07 06:55
---

---

## Краткое описание

Детальная проработка системы аналитики и графиков для NECPGAME - инструменты для анализа экономических данных, трендов, цен и принятия торговых решений.

**Цель:** Предоставить игрокам профессиональные аналитические инструменты для анализа рынка и принятия обоснованных торговых решений.

---

## Источники вдохновения

### TradingView - Professional Charts
- Технические индикаторы
- Графики свечей
- Drawing tools
- Multiple timeframes

### Bloomberg Terminal
- Real-time market data
- News integration
- Portfolio analytics
- Heat maps

### EVE Online - Market Analysis
- Price history
- Regional comparison
- Volume analysis
- Market manipulation detection

---

## 📋 Основные механики

### 1. Типы графиков

**1.1. Price Charts (Графики цен):**

**Line Chart (Линейный):**
- Простой график цены во времени
- Best for: общий тренд
- Timeframes: 1h, 24h, 7d, 30d, 90d, 1y, all

**Candlestick Chart (Свечной):**
```
每 свеча показывает:
- Open: цена открытия периода
- High: максимальная цена
- Low: минимальная цена
- Close: цена закрытия
- Color: green (рост), red (падение)
```

**OHLC Chart:**
- Open-High-Low-Close
- Детализация движения цены
- Professional trading view

**Area Chart:**
- Заполненный линейный график
- Показывает volume под линией
- Good for visualization

**1.2. Volume Charts:**

**Volume Histogram:**
- Столбцы объёма торгов
- Color-coded (green/red)
- Correlation с ценой

**Cumulative Volume:**
- Накопленный объём
- Показывает total liquidity

---

### 2. Технические индикаторы

**2.1. Moving Averages (Скользящие средние):**

**Simple Moving Average (SMA):**
```
SMA(7) = (Price₁ + Price₂ + ... + Price₇) / 7
```

**Exponential Moving Average (EMA):**
```
EMA = Price × (2/(n+1)) + EMA_prev × (1 - 2/(n+1))
```

**Common periods:**
- MA(7) - краткосрочный тренд
- MA(30) - среднесрочный
- MA(90) - долгосрочный

**Trading signals:**
- MA(7) crosses above MA(30) → BUY signal
- MA(7) crosses below MA(30) → SELL signal

**2.2. RSI (Relative Strength Index):**

**Formula:**
```
RSI = 100 - (100 / (1 + RS))
RS = Average Gain / Average Loss (14 periods)
```

**Interpretation:**
- RSI > 70 → Overbought (вероятность падения)
- RSI < 30 → Oversold (вероятность роста)
- RSI = 50 → Neutral

**2.3. MACD (Moving Average Convergence Divergence):**

**Components:**
```
MACD Line = EMA(12) - EMA(26)
Signal Line = EMA(9) of MACD Line
Histogram = MACD Line - Signal Line
```

**Trading signals:**
- MACD crosses above Signal → BUY
- MACD crosses below Signal → SELL

**2.4. Bollinger Bands:**

**Formula:**
```
Middle Band = SMA(20)
Upper Band = SMA(20) + (2 × StdDev)
Lower Band = SMA(20) - (2 × StdDev)
```

**Interpretation:**
- Price touches upper band → overbought
- Price touches lower band → oversold
- Bands narrow → low volatility (breakout coming)
- Bands widen → high volatility

---

### 3. Market Sentiment Analysis

**3.1. Bull/Bear Indicator:**

**Metrics:**
```
Bull Power = % of stocks above MA(30)
Bear Power = % of stocks below MA(30)

> 70% → Strong Bull Market
50-70% → Bullish
30-50% → Bearish  
< 30% → Strong Bear Market
```

**3.2. Fear & Greed Index:**

**Components:**
- Price momentum (25%)
- Stock price strength (25%)
- Stock price breadth (25%)
- Put/Call ratio (12.5%)
- Market volatility (12.5%)

**Scale:**
- 0-25: Extreme Fear → BUY opportunity
- 25-45: Fear
- 45-55: Neutral
- 55-75: Greed
- 75-100: Extreme Greed → SELL opportunity

---

### 4. Heat Maps

**4.1. Market Heat Map:**

**Visualization:**
```
╔════════════════════════════════════════╗
║ MARKET HEAT MAP - 2077-12-15          ║
╠════════════════════════════════════════╣
║ Sector        | Performance | Color    ║
╠═══════════════╪═════════════╪══════════╣
║ Technology    | +8.5%       | 🟩 Green ║
║ Defense       | +3.2%       | 🟩 Green ║
║ Finance       | +1.1%       | 🟩 Green ║
║ Healthcare    | -0.5%       | 🟥 Red   ║
║ Manufacturing | -2.3%       | 🟥 Red   ║
║ Energy        | -5.8%       | 🟥 Red   ║
╚═══════════════╧═════════════╧══════════╝
```

**4.2. Portfolio Heat Map:**

**Visualization:**
- Assets по размеру и performance
- Larger boxes = bigger allocation
- Color = performance (green/red)

---

### 5. Portfolio Analytics

**5.1. Performance Metrics:**

**Total Return:**
```
Total Return = ((Current Value - Initial Investment) / Initial Investment) × 100%
```

**Annualized Return:**
```
Annualized Return = ((1 + Total Return)^(365/Days)) - 1) × 100%
```

**Sharpe Ratio (risk-adjusted return):**
```
Sharpe Ratio = (Portfolio Return - Risk-Free Rate) / Portfolio Volatility
> 1.0 = Good
> 2.0 = Excellent
```

**5.2. Risk Metrics:**

**Portfolio Volatility:**
```
Volatility = StdDev of daily returns × √365
```

**Maximum Drawdown:**
```
Max Drawdown = (Trough Value - Peak Value) / Peak Value × 100%
```

**Beta (market correlation):**
```
Beta = Covariance(Portfolio, Market) / Variance(Market)
β = 1.0: moves with market
β > 1.0: more volatile than market
β < 1.0: less volatile
```

---

### 6. Trade History Analysis

**6.1. Trade Statistics:**

**Win Rate:**
```
Win Rate = (Profitable Trades / Total Trades) × 100%
```

**Profit Factor:**
```
Profit Factor = Gross Profit / Gross Loss
> 1.5 = Good
> 2.0 = Excellent
```

**Average Trade:**
```
Avg Win = Total Profit / Number of Wins
Avg Loss = Total Loss / Number of Losses
R:R Ratio = Avg Win / Avg Loss
```

**6.2. Performance over time:**

**Monthly Returns:**
```
Month      | Return | Cumulative
-----------|--------|------------
Jan 2077   | +5%    | +5%
Feb 2077   | +3%    | +8.15%
Mar 2077   | -2%    | +6.00%
...
```

---

### 7. Alerts and Notifications

**7.1. Price Alerts:**

**Types:**
- Price reaches target (e.g., ARSK > 1600)
- Price drops below threshold (e.g., MILT < 1100)
- Percentage change (e.g., BIOT +10% in 24h)

**7.2. Event Alerts:**

**Economic events:**
- New economic crisis started
- Embargo announced
- Corporate scandal breaking

**Portfolio alerts:**
- Portfolio down 10%
- Individual position down 20%
- Margin call warning

---

### 8. Структура БД

**8.1. Таблица: player_analytics_settings**
```sql
CREATE TABLE player_analytics_settings (
    player_id BIGINT PRIMARY KEY,
    default_timeframe VARCHAR(10) DEFAULT '7d',
    favorite_indicators JSONB,
    price_alerts JSONB,
    event_subscriptions JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### 9. API Endpoints

**9.1. REST API:**

**GET /api/v1/analytics/chart/:asset**
- Получить данные для графика
- Params: timeframe, indicators

**GET /api/v1/analytics/portfolio/:playerId**
- Analytics портфеля игрока
- Response: metrics, performance, risks

**POST /api/v1/analytics/alerts/create**
- Создать price alert
- Body: asset, condition, threshold

**GET /api/v1/analytics/market/heatmap**
- Market heat map данные
- Params: sector, timeframe

---

### 10. TODO для дальнейшей проработки

**Расширения:**
- [ ] AI-powered predictions
- [ ] Social sentiment analysis
- [ ] Automated trading signals

---

## История изменений

- v1.0.0 (2025-11-06 22:23) - Создание документа с детальными механиками аналитики
  - 4 типа графиков (line, candlestick, OHLC, area)
  - Технические индикаторы (MA, RSI, MACD, Bollinger Bands)
  - Market sentiment analysis
  - Heat maps (market, portfolio)
  - Portfolio analytics (return, risk metrics)
  - Trade history analysis
  - Alerts and notifications
  - Структура БД (1 таблица)
  - API endpoints (4 REST)
