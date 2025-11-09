# Task ID: API-TASK-170
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:52 | **Создатель:** AI Agent | **Зависимости:** API-TASK-160

---

## 📋 Описание

Создать API для дополнительных world events (8 документов). Travel events по периодам и события эпох (дополнение к task-160).

---

## 📚 Источники (8 документов)

**Travel events по периодам (5):**
- world-events-travel-2030-2045.md
- world-events-travel-2045-2060.md
- world-events-travel-2060-2077.md
- world-events-travel-2077.md
- world-events-travel-2078-2093.md

**Epoch events (3):**
- world-events-2020-2040.md
- world-events-2040-2060.md
- world-events-2060-2077.md

Note: Некоторые могут дублировать task-160, нужно проверить и объединить.

---

## 📁 Целевой файл

`api/v1/world/events/additional-travel-events.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** world-service  
**Порт:** 8086  
**API пути:** /api/v1/world/events/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** world  
**Путь:** modules/world/travel-events  
**State Store:** useWorldStore (travelEvents, epochEvents)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- EventCard, TravelEventModal, EpochFilter

**Готовые формы (@shared/forms):**
- EventChoiceForm

**Layouts (@shared/layouts):**
- GameLayout (event overlay при путешествии)

**Хуки (@shared/hooks):**
- useRealtime (для event triggers при перемещении)

---

## ✅ Endpoints

1. **GET /api/v1/world/events/travel** - Travel события по периоду

**Models:** TravelEvent, EpochEvent

---

**Источники:** 8 world events дополнительных

