# Task ID: API-TASK-121
**Тип:** API Generation
**Приоритет:** средний (Post-MVP)
**Статус:** queued
**Создано:** 2025-11-07 06:55
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-095 (player-market.yaml), API-TASK-094 (auction-house.yaml)

---

## 📋 Краткое описание

Создать API для системы экономической аналитики и графиков.

**Что нужно сделать:** Создать API для аналитики рынка (графики, технические индикаторы, market sentiment, heat maps, portfolio analytics, alerts).

---

## 🎯 Цель задания

Создать API для Economy Analytics:
- **Типы графиков:**
  - Line Charts (линейные)
  - Candlestick Charts (свечи)
  - OHLC Charts (открытие/максимум/минимум/закрытие)
  - Volume Charts (объемы)
- **Технические индикаторы:**
  - Moving Averages (MA, EMA)
  - RSI (Relative Strength Index)
  - MACD (Moving Average Convergence Divergence)
  - Bollinger Bands
- **Market Sentiment:** Bull/Bear indicators, Volume trends
- **Heat Maps:** Price changes visualization
- **Portfolio Analytics:** Profit/Loss, ROI, diversification
- **Trade History:** Анализ сделок
- **Alerts:** Price alerts, volume alerts
- **Вдохновение:** TradingView, Bloomberg Terminal, EVE Online

**КРИТИЧЕСКИ ВАЖНО:** Профессиональные инструменты для трейдеров!

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/economy/economy-analytics.md`
**Версия:** v1.0.0
**Статус:** approved (ready)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/economy/analytics.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/economy/analytics/price-chart`** - График цен
2. **GET `/api/v1/gameplay/economy/analytics/indicators`** - Технические индикаторы
3. **GET `/api/v1/gameplay/economy/analytics/portfolio`** - Портфолио
4. **POST `/api/v1/gameplay/economy/analytics/alerts/create`** - Создать alert

---

**История:** 2025-11-07 06:55 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

