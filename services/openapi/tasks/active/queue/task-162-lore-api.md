# Task ID: API-TASK-162
**Тип:** API Generation | **Приоритет:** низкий | **Статус:** queued
**Создано:** 2025-11-07 11:28 | **Создатель:** AI Agent | **Зависимости:** none

---

## 📋 Описание

Создать API для лора (4 документа). Universe, factions, locations, characters.

---

## 📚 Источники (4 документа)

- `.BRAIN/03-lore/universe.md` (v1.1.0)
- `.BRAIN/03-lore/factions/factions-overview.md` (v1.1.0)
- `.BRAIN/03-lore/locations/locations-overview.md` (v1.2.0)
- `.BRAIN/03-lore/characters/characters-overview.md` (v1.2.0)

**Содержит:**
- Universe: временная шкала 2020-2093, лор симуляции
- Factions: 28 корпораций, 27 банд, 29 организаций
- Locations: 27 городов по регионам
- Characters: 30+ категорий NPC

---

## 📁 Целевая структура

```
api/v1/lore/
├── universe.yaml
├── factions.yaml
├── locations.yaml
└── characters.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** world-service  
**Порт:** 8086  
**API пути:** /api/v1/lore/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** world  
**Путь:** modules/world/lore  
**State Store:** useWorldStore (factions, locations, timeline)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, FactionCard, LocationCard, Timeline, CharacterCard

**Готовые формы (@shared/forms):**
- N/A (только просмотр лора)

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce (для поиска по лору)

---

## ✅ Endpoints

1. **GET /api/v1/lore/universe/timeline** - Временная шкала
2. **GET /api/v1/lore/factions** - Список фракций
3. **GET /api/v1/lore/locations** - Список локаций
4. **GET /api/v1/lore/characters** - Категории NPC

**Models:** UniverseTimeline, Faction, Location, CharacterCategory

---

**Источники:** 4 lore документа

