# Task ID: API-TASK-156
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:14 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для системы ценообразования. Формулы, multipliers, dynamic pricing, modifiers.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-pricing-detailed.md` (v2.0.0, ready)

**Ключевые механики:**
- Pricing формулы
- Multipliers (quality, rarity, level)
- Dynamic pricing (supply/demand)
- Regional/faction modifiers
- Auction House mechanics
- Trade routes pricing
- Vendor prices

---

## 📁 Целевой файл

`api/v1/economy/pricing.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/pricing/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/pricing  
**State Store:** useEconomyStore (itemPrices, marketData)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- PriceDisplay, PriceChart, ModifiersList

**Готовые формы (@shared/forms):**
- N/A (динамический расчёт)

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для dynamic pricing)
- useDebounce

---

## ✅ Endpoints

1. **GET /api/v1/economy/pricing/item/{item_id}** - Цена предмета
2. **GET /api/v1/economy/pricing/vendor/{vendor_id}** - Цены у vendor
3. **GET /api/v1/economy/pricing/market-data** - Рыночные данные
4. **POST /api/v1/economy/pricing/calculate** - Рассчитать цену

**Models:** ItemPrice, PriceModifiers, MarketData, VendorPricing

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-pricing-detailed.md`

