# Task ID: API-TASK-095
**Тип:** API Generation
**Приоритет:** КРИТИЧЕСКИЙ (MVP)
**Статус:** queued
**Создано:** 2025-11-07 04:25
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-067 (trading.yaml), API-TASK-094 (auction-house.yaml)

---

## 📋 Краткое описание

Создать API для рынка игроков с системой ордеров (Player Market).

**Что нужно сделать:** Создать API для продвинутой торговой системы с ордерами (buy/sell orders, order book, market/limit ордера, частичное исполнение).

---

## 🎯 Цель задания

Создать API для Player Market (КРИТИЧЕСКИЙ - MVP):
- **Система ордеров:**
  - Buy orders (заявки на покупку)
  - Sell orders (заявки на продажу)
  - Order book (стакан заявок)
  - Price/time priority
- **Типы ордеров:**
  - Market orders (мгновенное исполнение по лучшей цене)
  - Limit orders (исполнение при достижении цены)
- **Исполнение:** Частичное, полное, автоматическое
- **Комиссии:** Listing fee + exchange fee (0.5-5%)
- **История:** Детальная статистика, графики цен
- **Региональные рынки:** Арбитраж между городами
- **БД структура:** 2 таблицы + 2 materialized views
- **API:** 15+ REST endpoints + 3 WebSocket
- **Торговые стратегии:** Market making, arbitrage, trend following
- **Вдохновение:** EVE Online, GW2, Albion Online

**КРИТИЧЕСКИ ВАЖНО:** Продвинутая торговая система для опытных трейдеров! (1829 строк документа)

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/economy/economy-player-market.md`
**Версия:** v1.0.0
**Статус:** ready (draft, но детально проработан)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/economy/player-market.yaml`

**ВАЖНО:** Огромная система (1829 строк). ОБЯЗАТЕЛЬНО разбить:
- player-market-core.yaml - основные endpoints
- player-market-orders.yaml - система ордеров
- player-market-execution.yaml - исполнение
- player-market-stats.yaml - статистика и графики
- player-market-ws.yaml - WebSocket для real-time

---

## ✅ Endpoints

1. **POST `/api/v1/gameplay/economy/market/create-order`** - Создать ордер
2. **POST `/api/v1/gameplay/economy/market/cancel-order`** - Отменить
3. **GET `/api/v1/gameplay/economy/market/order-book/{item_id}`** - Стакан заявок
4. **GET `/api/v1/gameplay/economy/market/execute-market-order`** - Market order
5. **WebSocket `/ws/market/updates`** - Real-time обновления

---

**История:** 2025-11-07 04:25 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` с актуальными данными:
  - name: economy-service
  - port: 8085
  - domain: economy
  - base-path: /api/v1/gameplay/economy
  - package: com.necpgame.economyservice
- В секции `servers` используй gateway:
  - https://api.necp.game/v1/gameplay/economy
  - http://localhost:8080/api/v1/gameplay/economy
- WebSocket маршруты публикуй только через wss://api.necp.game/v1/gameplay/economy/...

