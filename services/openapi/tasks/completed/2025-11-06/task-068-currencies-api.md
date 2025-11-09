# Task ID: API-TASK-068
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 01:35
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-067 (trading.yaml)

---

## 📋 Краткое описание

Создать API для детальной системы валют.

**Что нужно сделать:** Создать API для системы валют: Eurodollar (основная), региональные валюты, Faction Scrip, Premium, Crypto с курсами обмена.

---

## 🎯 Цель задания

Создать API для валют:
- Eurodollar (€$) - основная валюта
- Региональные: Yen, Ruble, Rand, Peso (с курсами)
- Faction Scrip: валюты фракций (Arasaka, Militech, NetWatch)
- Premium: монетизация (no P2W)
- Crypto: BitCoin, EuroCoin
- Курсы обмена: динамические (фракционный контроль, события)
- Earning/Sinks: источники и траты

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/economy/economy-currencies-detailed.md`
**Версия:** v2.0.0
**Статус:** Ready for API

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/economy/currencies.yaml`

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/economy/currencies`** - Все валюты
2. **GET `/api/v1/gameplay/economy/currencies/exchange-rates`** - Курсы обмена
3. **POST `/api/v1/gameplay/economy/currencies/exchange`** - Обменять валюту
4. **GET `/api/v1/gameplay/economy/currencies/{character_id}/balance`** - Баланс

---

**История:** 2025-11-07 01:35 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

