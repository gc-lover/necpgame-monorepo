# Task ID: API-TASK-160
**Тип:** API Generation | **Приоритет:** средний | **Статус:** queued
**Создано:** 2025-11-07 11:24 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для мировых событий по эпохам (5 документов). DC-скейлинг, AI-слайдеры, D&D генераторы.

---

## 📚 Источники (5 документов)

- `.BRAIN/02-gameplay/world/events/world-events-1990-2000.md` (v0.1.0)
- `.BRAIN/02-gameplay/world/events/world-events-2000-2020.md` (v0.1.0)
- `.BRAIN/02-gameplay/world/events/world-events-2077.md` (v0.1.0)
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` (v0.1.0)
- `.BRAIN/02-gameplay/world/events/world-events-travel-2020-2093.md` (v1.0.0)

**Ключевые механики:**
- DC-скейлинг по эпохам
- AI-слайдеры фракций
- D&D генераторы событий (d100)
- Экономические множители
- Технологические доступы
- Квестовые хуки
- События перемещения

---

## 📁 Целевая структура

```
api/v1/world/events/
├── events-1990-2000.yaml
├── events-2000-2020.yaml
├── events-2077.yaml
├── events-framework.yaml
└── events-travel-all.yaml
```

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
**Путь:** modules/world/events  
**State Store:** useWorldStore (worldEvents, eventsByEpoch, activeEvents)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- EventCard, TimelineView, EpochFilter, EventModal

**Готовые формы (@shared/forms):**
- EventChoiceForm

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useRealtime (для world event triggers)
- useDebounce (для фильтров по эпохам)

---

## ✅ Задача

Создать API для мировых событий по всем эпохам с унифицированной логикой.

**Models:** WorldEvent, EventEpoch, TravelEvent, EventFramework

---

**Источники:** 5 world events документов

