# Task ID: API-TASK-166
**Тип:** API Generation | **Приоритет:** низкий | **Статус:** queued
**Создано:** 2025-11-07 11:44 | **Создатель:** AI Agent | **Зависимости:** API-TASK-162

---

## 📋 Описание

Создать API для детального лора (51 документ). Детальные города, фракции, технологии, timeline, культура.

---

## 📚 Источники (51 документ)

**Города (5):**
- Night City: Westbrook, Watson, Pacifica, DISTRICTS-INDEX
- World: Tokyo, WORLD-CITIES-INDEX

**Фракции (30):**
- Gangs: 6th Street, Maelstrom, Tyger Claws, Valentinos, Voodoo Boys + Global gangs + GANGS-INDEX
- Unique: 10 уникальных фракций + UNIQUE-FACTIONS-INDEX + BATCH-2 + BATCH-3
- Corpo: Arasaka, Militech, Kangtao politics + CORPO-POLITICS-INDEX

**Технологии (3):**
- NET-AND-BLACKWALL-INDEX, net-architecture-detailed, blackwall-detailed

**Timeline (6):**
- MASTER-TIMELINE-INDEX + detailed-events по периодам (2020-2030, 2030-2040, 2040-2060, 2060-2077, 2077-2093)

**События (3):**
- Fifth Corporate War: основной + battles + heroes

**Культура (4):**
- CYBERPUNK-CULTURE-INDEX + культурные аспекты

---

## 📁 Целевая структура

```
api/v1/lore/detailed/
├── cities/
│   ├── night-city-districts.yaml
│   └── world-cities.yaml
├── factions/
│   ├── gangs-detailed.yaml
│   ├── unique-factions.yaml
│   └── corpo-politics.yaml
├── technology/
│   ├── net-architecture.yaml
│   └── blackwall-history.yaml
├── timeline/
│   └── detailed-timeline.yaml
└── events/
    └── fifth-war.yaml
```

---

## 🏗️ Целевая архитектура

### Backend (микросервис):

**Микросервис:** world-service  
**Порт:** 8086  
**API пути:** /api/v1/lore/detailed/*

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):

**Модуль:** world  
**Путь:** modules/world/lore-detailed  
**State Store:** useWorldStore (detailedLore, districts, detailedFactions, timeline)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, CityDistrictCard, FactionCard, Timeline, TechTree

**Готовые формы (@shared/forms):**
- N/A (только просмотр детального лора)

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce (для поиска по лору)

---

## ✅ Endpoints

1. **GET /api/v1/lore/detailed/cities/{city_id}** - Детали города
2. **GET /api/v1/lore/detailed/factions/{faction_id}** - Детали фракции
3. **GET /api/v1/lore/detailed/timeline** - Детальная timeline
4. **GET /api/v1/lore/detailed/technology/{tech_id}** - Технологии

**Models:** DetailedCity, DetailedFaction, TimelineEvent, TechnologyHistory

---

**Источники:** 51 lore detailed документ

