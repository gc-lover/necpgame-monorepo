# Task ID: API-TASK-154
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:10 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для экономических событий. Crisis, inflation, recession, boom, торговые войны.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-events.md` (v1.0.0, ready)

**Ключевые механики:**
- 4 основных типа (crisis, inflation, recession, boom)
- Торговые войны и санкции
- Корпоративные события (scandals, M&A, breakthroughs)
- Commodity events (дефицит ресурсов)
- Влияние на цены и доступность
- Триггеры событий

---

## 📁 Целевой файл

`api/v1/economy/economy-events.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/events/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/events  
**State Store:** useEconomyStore (economyEvents, eventImpact)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- EventCard, ImpactChart, PriceChangeIndicator

**Готовые формы (@shared/forms):**
- N/A (только просмотр)

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для event triggers)

---

## ✅ Endpoints

1. **GET /api/v1/economy/events** - Текущие экономические события
2. **GET /api/v1/economy/events/{event_id}** - Детали события
3. **GET /api/v1/economy/events/history** - История событий
4. **POST /api/v1/economy/events/trigger** - Trigger event (admin)

**Models:** EconomyEvent, EventImpact, EventTrigger

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-events.md`

