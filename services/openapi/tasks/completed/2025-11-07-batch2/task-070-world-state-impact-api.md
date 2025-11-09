# Task ID: API-TASK-070
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-07 01:45
**Создатель:** AI Agent (API Task Creator)
**Зависимости:** API-TASK-060 (relationships.yaml)

---

## 📋 Краткое описание

Создать API для влияния игроков на глобальное состояние мира.

**Что нужно сделать:** Создать централизованную API для системы мирового состояния с уровнями влияния (индивидуальный → групповой → фракционный → региональный → глобальный), категориями (territory control, faction power, economy, technology, social).

---

## 🎯 Цель задания

Создать API для world state:
- 5 уровней влияния: Individual → Group → Faction → Regional → Global
- Категории: TERRITORY_CONTROL, FACTION_POWER, ECONOMIC_STATE, TECHNOLOGY_LEVEL, SOCIAL_STRUCTURE, QUEST_PROGRESS, ENVIRONMENTAL
- Живой мир: KENSHI + RIMWORLD + EVE Online + WOW + Baldur's Gate 3
- Механизмы влияния: квесты, экономика, боевая система, социальные механики
- Агрегация влияний: от индивидуального к глобальному
- Синхронизация: real-time updates, server-wide state

**Критически важно:** Это основа всего MMORPG мира!

---

## 📚 Источники информации

**Путь:** `.BRAIN/02-gameplay/world/world-state-player-impact.md`
**Версия:** v1.0.0
**Статус:** approved (критический)

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/world/world-state.yaml`

**ВАЖНО:** Огромный файл (1500+ строк). ОБЯЗАТЕЛЬНО разбить:
- world-state.yaml - основные endpoints
- world-state-categories.yaml - категории состояния
- world-state-aggregation.yaml - агрегация влияний

---

## ✅ Endpoints

1. **GET `/api/v1/gameplay/world/world-state`** - Текущее глобальное состояние
2. **GET `/api/v1/gameplay/world/world-state/region/{region_id}`** - Состояние региона
3. **POST `/api/v1/gameplay/world/world-state/event`** - Зарегистрировать событие
4. **GET `/api/v1/gameplay/world/world-state/faction-power`** - Могущество фракций
5. **GET `/api/v1/gameplay/world/world-state/territory-control`** - Контроль территорий

---

**История:** 2025-11-07 01:45 - Создано


### OpenAPI (обязательно)

- Заполни `info.x-microservice` с актуальными данными:
  - name: world-service
  - port: 8086
  - domain: world
  - base-path: /api/v1/gameplay/world
  - package: com.necpgame.worldservice
- В секции `servers` используй gateway:
  - https://api.necp.game/v1/gameplay/world
  - http://localhost:8080/api/v1/gameplay/world
- WebSocket маршруты публикуй только через wss://api.necp.game/v1/gameplay/world/...

