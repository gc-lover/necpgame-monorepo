# Task ID: API-TASK-151
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:04 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для системы логистики. Транспортные средства, маршруты, риски, страхование груза.

---

## 📚 Источник

**Документ:** `.BRAIN/02-gameplay/economy/economy-logistics.md` (v1.0.0, ready)

**Ключевые механики:**
- Транспортные средства (5 типов: пешком, мото, авто, грузовик, аэродин)
- Маршруты (локальные, региональные, глобальные)
- Риски транспортировки (ambush, weather, mechanical, accidents)
- Страхование груза (3 плана)
- Конвои и эскорт
- Скорость доставки
- Cargo management (вес, объем)

---

## 📁 Целевой файл

`api/v1/economy/logistics.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** economy-service  
**Порт:** 8085  
**API пути:** /api/v1/economy/logistics/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** economy  
**Путь:** modules/economy/logistics  
**State Store:** useEconomyStore (shipments, routes, vehicles)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- ShipmentCard, RouteMap, VehicleCard, RiskIndicator, ProgressBar (delivery)

**Готовые формы (@shared/forms):**
- ShipmentCreationForm, InsuranceForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для tracking доставки)
- useDebounce

---

## ✅ Endpoints

1. **POST /api/v1/economy/logistics/shipment** - Создать доставку
2. **GET /api/v1/economy/logistics/shipment/{id}** - Статус доставки
3. **GET /api/v1/economy/logistics/routes** - Доступные маршруты
4. **POST /api/v1/economy/logistics/insurance** - Купить страховку
5. **GET /api/v1/economy/logistics/vehicles** - Транспортные средства

**Models:** Shipment, LogisticsRoute, CargoInsurance, TransportVehicle

---

**Источник:** `.BRAIN/02-gameplay/economy/economy-logistics.md`

