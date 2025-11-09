# Task ID: API-TASK-147
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 10:56 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для Random Events All Periods. 73 события перемещений 2020-2093.

---

## 📚 Источник

**Документ:** `.BRAIN/04-narrative/quests/side/EVENTS-ALL-PERIODS.md` (v1.0.0, ready)

**Содержит:** 73 события перемещений с триггерами, условиями, исходами.

---

## 📁 Целевой файл

`api/v1/narrative/random-events-all.yaml`

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** world-service  
**Порт:** 8086  
**API пути:** /api/v1/world/random-events/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** world  
**Путь:** modules/world/random-events  
**State Store:** useWorldStore (randomEvents, triggeredEvents)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- EventCard, EventModal, ChoiceButton

**Готовые формы (@shared/forms):**
- EventChoiceForm

**Layouts (@shared/layouts):**
- GameLayout (event popup overlay)

**Хуки (@shared/hooks):**
- useRealtime (для триггера событий)

---

## ✅ Endpoints

1. **GET /api/v1/narrative/random-events** - Список событий
2. **POST /api/v1/narrative/random-events/trigger** - Trigger event (internal)
3. **GET /api/v1/narrative/random-events/{event_id}** - Детали события

**Models:** RandomEvent, EventTrigger, EventOutcome

---

**Источник:** `.BRAIN/04-narrative/quests/side/EVENTS-ALL-PERIODS.md`

