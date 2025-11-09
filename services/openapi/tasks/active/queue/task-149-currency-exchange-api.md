# Task ID: API-TASK-149
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:00 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для валютной биржи. 12 региональных валют, обмен, арбитраж, leverage trading.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-currency-exchange.md` (v1.0.0, ready)

**Ключевые механики:**
- 12 региональных валют
- Валютные пары (major/minor/exotic)
- Спреды и комиссии
- Арбитраж (региональный, triangular)
- Hedging (страхование рисков)
- Carry trade
- Leverage trading
- Real-time курсы

---

## 📁 Целевой файл

`api/v1/economy/currency-exchange.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/currency-exchange/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/currency-exchange  
**State Store:** useEconomyStore (exchangeRates, currencies)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, CurrencyPairCard, PriceDisplay, Chart (rate history)

**Готовые формы (@shared/forms):**
- CurrencyExchangeForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для обновления курсов)
- useDebounce

---

## ✅ Endpoints

1. **GET /api/v1/economy/currency-exchange/rates** - Текущие курсы
2. **POST /api/v1/economy/currency-exchange/convert** - Обменять валюту
3. **GET /api/v1/economy/currency-exchange/pairs** - Доступные пары
4. **GET /api/v1/economy/currency-exchange/history** - История курсов

**Models:** CurrencyPair, ExchangeRate, ConversionRequest, ArbitrageOpportunity

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-currency-exchange.md`

