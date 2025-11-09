# Task ID: API-TASK-179
**Тип:** API Generation | **Приоритет:** высокий | **Статус:** queued
**Создано:** 2025-11-07 18:00 | **Создатель:** AI Agent ДУАПИТАСК | **Зависимости:** API-TASK-138

---

## 📋 Описание

Создать API для визуальной карты квестов NECPGAME. Полная карта Night City, Badlands, Cyberspace с квестами, фракциями, концовками, ветвлениями.

---

## 📚 Источники (1 документ)

**Visual Quest Map:**
- `04-narrative/quests/VISUAL-QUEST-MAP.md` - Полная визуальная карта (607 строк)
  - Night City quest map (8 районов)
  - Badlands quest map (3 зоны)
  - Cyberspace quest map
  - Faction quests (14 фракций)
  - Romance quests
  - World events

**Split parts (для справки):**
- `04-narrative/quests/visual-quest-map-part1.md`
- `04-narrative/quests/visual-quest-map-part2.md`

---

## 🎯 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/narrative/quest-map.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0 Specification (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── narrative/
            ├── quest-map.yaml  ← Создать этот файл
            └── quest-system.yaml
```

---

## ✅ Что нужно сделать

### Шаг 1: Создание базовой структуры файла

**Действия:**
1. Создать файл `api/v1/narrative/quest-map.yaml`.
2. Добавить базовую информацию OpenAPI (openapi, info, servers, tags).
3. Определить теги: `Quest Map`, `Visual Navigation`, `Quest Discovery`.

**Ожидаемый результат:**
- Файл `quest-map.yaml` с корректной базовой структурой OpenAPI.

### Шаг 2: Реализация Endpoints для карты квестов

**Действия:**
1. Добавить endpoint `GET /narrative/quest-map` для получения полной карты квестов.
   - Query params: `region` (night_city, badlands, cyberspace), `faction_id`, `district_id`
   - Responses: `200 OK` (QuestMapResponse), `404 NotFound` (Error)
2. Добавить endpoint `GET /narrative/quest-map/districts/{district_id}` для квестов района.
   - Path parameter: `district_id`
   - Responses: `200 OK` (DistrictQuestsResponse), `404 NotFound` (Error)
3. Добавить endpoint `GET /narrative/quest-map/factions/{faction_id}` для квестов фракции.
   - Path parameter: `faction_id`
   - Responses: `200 OK` (FactionQuestsResponse), `404 NotFound` (Error)
4. Добавить endpoint `GET /narrative/quest-map/player/{player_id}/available` для доступных квестов игрока.
   - Path parameter: `player_id`
   - Query params: `filter_by_region`, `filter_by_level`
   - Responses: `200 OK` (AvailableQuestsResponse), `400 BadRequest` (Error)

**Ожидаемый результат:**
- Endpoints для навигации по карте квестов.

### Шаг 3: Определение моделей данных

**Действия:**
1. Создать схемы для моделей:
   - `QuestMapResponse` (regions[], districts[], factions_quests[])
   - `RegionMap` (region_name, quest_chains[], connections[])
   - `DistrictQuestsResponse` (district_id, name, main_quests[], side_quests[])
   - `FactionQuestsResponse` (faction_id, faction_name, quest_chain[], endings[])
   - `QuestNode` (quest_id, name, level, prerequis[], location, quest_giver)
   - `QuestConnection` (from_quest_id, to_quest_id, condition_type)
   - `AvailableQuestsResponse` (available_quests[], recommended_quests[])
2. Использовать `PascalCase` для имен моделей.
3. Добавить примеры для каждой модели.

**Ожидаемый результат:**
- Все модели данных определены в секции `components/schemas`.

### Шаг 4: Определение схем безопасности

**Действия:**
1. Использовать `BearerAuth` из `shared/security/security.yaml`.
2. Определить `security` для каждого защищенного эндпоинта.

**Ожидаемый результат:**
- Корректное применение схем безопасности.

### Шаг 5: Валидация и правила

**Действия:**
1. Добавить валидацию для region (enum: night_city, badlands, cyberspace).
2. Указать ограничения для quest prerequisites.
3. Определить бизнес-правила для доступности квестов.

**Ожидаемый результат:**
- Валидация и бизнес-правила отражены в схемах.

---

## 📚 Дополнительная информация

См. дополнительный файл: **[api-generation-task-template-details.md](../../templates/api-generation-task-template-details.md)**

---

**ВНИМАНИЕ:** Это задание для АПИТАСК агента. Выполняй пошагово.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

