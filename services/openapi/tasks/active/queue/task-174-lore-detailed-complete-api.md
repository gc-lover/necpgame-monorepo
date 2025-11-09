# Task ID: API-TASK-174
**Тип:** API Generation | **Приоритет:** низкий | **Статус:** queued
**Создано:** 2025-11-07 12:52 | **Создатель:** AI Agent | **Зависимости:** API-TASK-162

---

## 📋 Описание

Создать API для детального лора (51 документ). Города, фракции, технологии, timeline, события, культура - полная lore database.

---

## 📚 Источники (51 lore документ)

**Cities (5):**
- night-city/NIGHT-CITY-DISTRICTS-MASTER-INDEX.md
- night-city/westbrook-detailed-2020-2093.md
- night-city/watson-detailed-2020-2093.md
- night-city/pacifica-detailed-2020-2093.md
- world-cities/tokyo-detailed-2020-2093.md, WORLD-CITIES-MASTER-INDEX.md

**Factions (30):**
- Gangs: 6th Street, Maelstrom, Tyger Claws, Valentinos, Voodoo Boys
- Global Gangs: Data Jackals, Fog Razors, Narco Kings, Neon Ronin, Red Winters
- Unique: Bio Purists, Body Modders, Chrome Liberation, Church of Digital God, и др.
- MASTER-INDEX: GANGS-MASTER-INDEX, UNIQUE-FACTIONS-MASTER-INDEX, CORPORATE-POLITICS-MASTER-INDEX

**Technology (3):**
- NET-AND-BLACKWALL-MASTER-INDEX.md
- net-architecture-detailed-2020-2093.md
- blackwall-detailed-2023-2093.md

**Timeline (6):**
- MASTER-TIMELINE-INDEX.md
- detailed-events-2020-2030.md, 2030-2040, 2040-2060, 2060-2077, 2077-2093

**Events (3):**
- fifth-corporate-war-2085-2088.md
- fifth-war-battles-detailed.md
- fifth-war-heroes-and-victims.md

**Culture (1):**
- CYBERPUNK-CULTURE-MASTER-INDEX.md

**Combat abilities (4):**
- combat/abilities/active-abilities.md, passive-abilities.md
- combat/ai/enemy-ai-basic.md, enemy-ai-advanced.md

---

## 📁 Целевая структура

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
**State Store:** useWorldStore (cities, factions, timeline, technology, events, culture)

### Frontend (библиотеки):

**UI компоненты (@shared/ui):**
- Card, CityDistrictCard, FactionCard, Timeline, TechTree, EventCard, CultureCard

**Готовые формы (@shared/forms):**
- N/A (только просмотр детального лора)

**Layouts (@shared/layouts):**
- GameLayout

**Хуки (@shared/hooks):**
- useDebounce (для поиска по лору)

---

```
api/v1/lore/detailed/
├── cities/
│   ├── night-city.yaml
│   └── world-cities.yaml
├── factions/
│   ├── gangs.yaml
│   ├── unique-factions.yaml
│   └── corpo-politics.yaml
├── technology/
│   └── net-blackwall.yaml
├── timeline/
│   └── detailed-timeline.yaml
└── events/
    └── fifth-war.yaml
```

---

## ✅ Endpoints

1. **GET /api/v1/lore/detailed/cities/{id}** - Детали города
2. **GET /api/v1/lore/detailed/factions/{id}** - История фракции
3. **GET /api/v1/lore/detailed/timeline** - Детальная timeline
4. **GET /api/v1/lore/detailed/technology/{id}** - История технологий

**Models:** DetailedCity, FactionHistory, TimelineEvent, TechnologyHistory

---

**Источников:** 51 lore detailed документ

