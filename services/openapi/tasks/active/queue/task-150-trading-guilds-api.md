# Task ID: API-TASK-150
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:02 | **Создатель:** AI Agent | **Зависимости:** API-TASK-135

---

## 📋 Описание

Создать API для торговых гильдий. Создание, структура ролей, общий капитал, распределение прибыли.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-trading-guilds.md` (v1.0.0, ready)

**Ключевые механики:**
- Trading guild creation/types
- Структура ролей (Guild Master, Treasurer, Merchant, Trader)
- Общий капитал (guild treasury)
- Распределение прибыли
- Торговые квоты
- Эксклюзивные маршруты
- Guild Hall
- Уровни гильдий (1-5)

---

## 📁 Целевой файл

`api/v1/economy/trading-guilds.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/trading-guilds/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/trading-guilds  
**State Store:** useEconomyStore (tradingGuild, treasury, routes)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- GuildCard, TreasuryDisplay, RouteCard, ProfitChart

**Готовые формы (@shared/forms):**
- TradingGuildCreationForm, ContributeForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для guild treasury updates)

---

## ✅ Endpoints

1. **POST /api/v1/economy/trading-guilds** - Создать торговую гильдию
2. **GET /api/v1/economy/trading-guilds/{guild_id}** - Информация
3. **GET /api/v1/economy/trading-guilds/{guild_id}/treasury** - Казна
4. **POST /api/v1/economy/trading-guilds/{guild_id}/contribute** - Внести капитал
5. **GET /api/v1/economy/trading-guilds/{guild_id}/routes** - Торговые маршруты

**Models:** TradingGuild, GuildTreasury, TradingRoute, GuildProfit

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-trading-guilds.md`

