# Task ID: API-TASK-314
**Тип:** API Generation  
**Приоритет:** критический  
**Статус:** queued  
**Создано:** 2025-11-08 09:50  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Создать спецификацию `City Population Pipeline API`, описывающую расчёт, пересчёт и публикацию конфигурации NPC в городах.  
**Целевой файл:** `api/v1/world/cities/population.yaml`

---

## 🎯 Цель задания

Сформировать contract-first API для world-service, который:
- принимает параметры города/района и возвращает рассчитанные показатели плотности и профили NPC;
- запускает пересчёт сегментов при событиях (захваты, экономические всплески, перемещения игроков);
- выдаёт состояние генератора (jobs, очереди, SLA) и предоставляет diff для фронтенда;
- синхронизируется с social/economy/gameplay сервисами через события.

---

## 📚 Источники

- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` (v1.0.0, 2025-11-08)
- `.BRAIN/02-gameplay/world/world-state/player-impact-mechanics.md`
- `.BRAIN/03-lore/locations/locations-overview.md`
- `.BRAIN/05-technical/backend/progression-backend.md` (telemetry hooks)
- `.BRAIN/05-technical/backend/session/session-lifecycle-heartbeat.md` (player presence)
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md`

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** world-service (port 8086)
- **Интеграции:** social-service (NPC профили), economy-service (инфраструктура), gameplay-service (player impact), realtime-service (zones)
- **Event bus topics:** `world.population.updated`, `world.population.job.*`

### Frontend
- **Модуль:** `modules/world/cities`
- **State Store:** `useWorldStore(cities)`
- **UI:** `@shared/ui` { `CityHeatmap`, `DistrictTable`, `CapacityGauge`, `StatusPill`, `Timeline` }
- **Forms:** `@shared/forms` { `PopulationRecalcForm`, `FilterForm`, `ThresholdForm` }
- **Layouts:** `@shared/layouts` { `OperationsSplitView`, `GameLayout` }
- **Hooks:** `useRealtime`, `useWorldFilters`, `useDebounce`

Комментарий добавить в начало YAML:
```
# Target Architecture:
# - Microservice: world-service (8086)
# - Frontend Module: modules/world/cities
# - State: useWorldStore(cities)
# - UI: CityHeatmap, DistrictTable, CapacityGauge, StatusPill, Timeline
# - Forms: PopulationRecalcForm, FilterForm, ThresholdForm
# - Layouts: OperationsSplitView, GameLayout
# - Hooks: useRealtime, useWorldFilters, useDebounce
# - Events: world.population.updated, world.population.job.*
# - API Base: /api/v1/world/cities/*
```

---

## ✅ План работ

1. **Сбор требований**: состояния pipeline, входные данные (blueprints, archetypes, events, player impact).  
2. **Проектирование моделей**:
   - `CityPopulationProfile`
   - `DistrictPopulationState`
   - `PopulationRecalcRequest`
   - `PopulationRecalcJob`
   - `PopulationDiff`
   - `PopulationMetrics`
3. **Эндпоинты** (минимум):
   - `GET /population` — агрегированные данные города.
   - `GET /population/{cityId}/districts` — детализация по районам (пагинация).
   - `POST /population/recalculate` — запуск пересчёта (sync/async с jobId).
   - `GET /population/jobs/{jobId}` — статус пересчёта.
   - `GET /population/{cityId}/diff` — изменения vs baseline.
4. **Ошибки**: использовать `api/v1/shared/common/responses.yaml`.
5. **Примеры**: базовый город, район «Watson», job в статусе `running`, diff после события.
6. **Валидация**: OpenAPI 3.0.3, линтер, ≤400 строк (вынести компоненты при необходимости).

---

## 🧱 Модели

- `CityPopulationProfile`  
  - `cityId`, `timestamp`, `populationTotal`, `capacityUsage`, `densityScore`, `segments[]`
- `DistrictPopulationState`  
  - `districtId`, `segment`, `npcCount`, `capacity`, `growthRate`, `alerts[]`
- `PopulationRecalcRequest`  
  - `cityId`, `districtIds[]`, `trigger` (enum: event, manual, player-impact), `priority`, `dryRun`
- `PopulationRecalcJob`  
  - `jobId`, `status` (queued/running/completed/failed), `submittedAt`, `startedAt`, `finishedAt`, `progress`, `logs[]`
- `PopulationMetrics`  
  - `metricId`, `value`, `threshold`, `trend`, `unit`
- `PopulationDiff`  
  - `cityId`, `baselineTimestamp`, `currentTimestamp`, `districtChanges[]`, `npcDelta`, `capacityDelta`
- `DistrictChange`  
  - `districtId`, `oldState`, `newState`, `alerts`, `playerImpact`
- `EventImpact`  
  - `eventId`, `type`, `severity`, `duration`, `applied`

---

## 📊 Критерии приемки

1. Файл `api/v1/world/cities/population.yaml` создан и содержит минимум 5 эндпоинтов.
2. В начале файла есть комментарий с целевой архитектурой.
3. Используются общие ошибки (400/401/403/404/409/422/500) через `$ref`.
4. Предусмотрена асинхронная работа через job-объекты.
5. Пагинация подключена через `shared/common/pagination.yaml` для списков районов/логов.
6. Примеры охватывают город Watson + событие `world.event.metro_shutdown`.
7. Схемы `CityPopulationProfile`, `DistrictPopulationState`, `PopulationRecalcRequest`, `PopulationRecalcJob`, `PopulationDiff` задокументированы.
8. Линтер проходит без ошибок; соблюден лимит строк (при необходимости вынести компоненты).
9. Обновлены `brain-mapping.yaml` и `.BRAIN` документ.

---

## ❓ FAQ

- **Поддерживать WebSocket?** — Нет, только REST + события.
- **Можно ли запускать пересчёт на dry-run?** — Да, предусмотреть `dryRun` в запросе и отдельный статус.
- **Нужны ли таймслоты?** — Да, `PopulationMetrics` должны учитывать сутки/неделю.
- **Как учитывать player impact?** — Передавать `trigger=player-impact` и `playerImpactContext` (optional).
- **Нужен ли экспорт CSV?** — Нет, ответ JSON достаточно; экспорт реализует другой сервис.


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

