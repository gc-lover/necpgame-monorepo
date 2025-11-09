# Task ID: API-TASK-315
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** completed  
**Создано:** 2025-11-08 09:50  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-314 (population)

---

## 📋 Краткое описание

Разработать спецификацию `NPC Schedule Service API`, выдающую расписания и профили NPC по городам/районам.  
**Файл:** `api/v1/social/npc/schedules.yaml`

---

## 🎯 Цель задания

Социальный сервис должен:
- хранить и отдавать расписания NPC (день/ночь/события/чрезвычайка);
- генерировать FSM состояний и маршрутные цепочки;
- поддерживать фильтрацию по архетипам, профессиям, активности, редкости;
- синхронизировать изменения с world-service (population) и gameplay (player impact);
- предоставлять инструменты для ручного override и диагностики.

---

## 📚 Источники

- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md`
- `.BRAIN/02-gameplay/social/social-overview.md`
- `.BRAIN/02-gameplay/social/npc-simulation.md`
- `.BRAIN/03-lore/characters/characters-overview.md`
- `.BRAIN/05-technical/backend/realtime-server/part2-protocol-optimization.md`
- `.BRAIN/04-narrative/dialogues/npc-*` (архетипы и роли)

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** social-service (port 8084)
- **Event topics:** `social.npc.schedule.updated`, `social.npc.spawned`, `social.npc.override`
- **Интеграции:** world-service (population diff), economy-service (infrastructure load), gameplay-service (player events), narrative-service (story NPC)

### Frontend
- **Модуль:** `modules/social/npc-schedules`
- **State Store:** `useSocialStore(npcSchedules)`
- **UI:** `NPCScheduleGrid`, `Timeline`, `RouteMap`, `StatusPill`, `Badge`
- **Forms:** `ScheduleFilterForm`, `OverrideForm`, `EventPlannerForm`
- **Layouts:** `OperationsSplitView`, `GameLayout`
- **Hooks:** `useRealtime`, `useScheduleFilters`, `useDebounce`

Комментарий в YAML:
```
# Target Architecture:
# - Microservice: social-service (8084)
# - Frontend Module: modules/social/npc-schedules
# - State: useSocialStore(npcSchedules)
# - UI: NPCScheduleGrid, Timeline, RouteMap, StatusPill, Badge
# - Forms: ScheduleFilterForm, OverrideForm, EventPlannerForm
# - Layouts: OperationsSplitView, GameLayout
# - Hooks: useRealtime, useScheduleFilters, useDebounce
# - Events: social.npc.schedule.updated, social.npc.spawned, social.npc.override
# - API Base: /api/v1/social/npc/*
```

---

## ✅ План

1. Определить входные данные (archetypes, schedule templates, events, overrides).  
2. Схемы:
   - `NpcSchedule`
   - `ScheduleSlot`
   - `RouteNode`
   - `NpcProfile`
   - `ScheduleQueryParams`
   - `OverrideRequest`
   - `ScheduleDiff`
3. Эндпоинты (минимально):
   - `GET /schedules` (пагинация, фильтры: city, district, archetype, rarity, activity, timeRange).
   - `GET /schedules/{npcId}` — детализация, маршруты, FSM.
   - `POST /schedules/rebuild` — пересборка для города/района/фракции.
   - `POST /schedules/override` — ручное изменение (включая SLA).
   - `GET /schedules/diff` — различия после событий.
4. Подключить `shared/common/responses.yaml`, `shared/common/pagination.yaml`.
5. Примеры: NPC «Vendor_Heywood», Archetype «CorporateGuard», событие «Festival».
6. Проверка: OpenAPI 3.0.3, линтер, ≤400 строк.

---

## 🧱 Модели

- `NpcSchedule`
  - `npcId`, `cityId`, `districtId`, `archetype`, `rarity`, `active`, `slots[]`, `routes[]`, `flags[]`
- `ScheduleSlot`
  - `slotId`, `start`, `end`, `activity`, `location`, `state`, `probability`, `conditions`
- `RouteNode`
  - `nodeId`, `order`, `location`, `mode`, `travelTime`, `constraints`
- `OverrideRequest`
  - `npcId`, `slotId`, `newActivity`, `newLocation`, `timeOverride`, `reason`, `expireAt`
- `ScheduleDiff`
  - `cityId`, `timestamp`, `changes[]` (added/updated/removed slots)
- `ScheduleRebuildRequest`
  - `cityId`, `districtIds[]`, `archetypes[]`, `trigger`, `priority`
- `ScheduleRebuildJob`
  - `jobId`, `status`, `progress`, `submittedAt`, `logs[]`
- `NpcProfile`
  - `npcId`, `name`, `faction`, `profession`, `behaviour`, `affinities`

---

## 📊 Критерии

1. Файл `api/v1/social/npc/schedules.yaml` создан с описанными 5 эндпоинтами.
2. Комментарий об архитектуре присутствует.
3. Схемы `NpcSchedule`, `ScheduleSlot`, `RouteNode`, `OverrideRequest`, `ScheduleDiff`, `ScheduleRebuildJob` задокументированы.
4. Пагинация и ошибки подключены через общие компоненты.
5. Примеры охватывают дневной/ночной режимы, событие фестиваля, override (курьер).
6. Поддерживается rebuild job (синхрон/асинхрон).
7. Линтер проходит без замечаний; лимит строк соблюдён.
8. `brain-mapping.yaml` и `.BRAIN` обновлены.

---

## ❓ FAQ

- **Можно ли выдавать маршруты в реальном времени?** — API возвращает план; realtime обновления идут через события.
- **Поддерживаются ли групповые override?** — `ScheduleRebuildRequest` может принимать списки archetypes/districts.
- **Как учитывать фракционные события?** — Передавать `trigger=faction-event`; указывать `eventId` в запросах.
- **Нужно ли хранить историю?** — Да, `ScheduleDiff` и job logs должны содержать хронологию изменений.
- **А что с сюжетными NPC?** — Флаг `storyCritical` и связанные ссылки (narrative-service), но управление остаётся здесь.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

