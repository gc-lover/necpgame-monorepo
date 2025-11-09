# Task ID: API-TASK-157
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:16 | **Создатель:** AI Agent | **Зависимости:** API-TASK-155

---

## 📋 Описание

Создать API для производственных цепочек. От руды до legendary, optimization, profitability.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-production-chains.md` (v2.0.0, ready)

**Ключевые механики:**
- Production chains (от руды до legendary)
- 3 полные цепочки (weapons, armor, implants)
- Optimization strategies
- Bulk production
- Profitability analysis
- Resource management
- Production facilities

---

## 📁 Целевой файл

`api/v1/economy/production-chains.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/production/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/production  
**State Store:** useEconomyStore (productionChains, jobs, profitability)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, ChainDiagram, ProfitabilityChart, ResourceBar

**Готовые формы (@shared/forms):**
- ProductionStartForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce
- useRealtime (для production progress)

---

## ✅ Endpoints

1. **GET /api/v1/economy/production/chains** - Доступные цепочки
2. **GET /api/v1/economy/production/chains/{chain_id}** - Детали цепочки
3. **POST /api/v1/economy/production/start** - Начать производство
4. **GET /api/v1/economy/production/profitability** - Анализ прибыльности

**Models:** ProductionChain, ChainStep, ProductionJob, Profitability

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-production-chains.md`

