# Task ID: API-TASK-279
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:45
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-278 (factions original catalog API), API-TASK-273 (seasonal events schedule API), API-TASK-275 (global research API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `factions-history-timeline.yaml`, предоставляющую хронику влияния авторских фракций 2020–2093, их события, механики и региональные ветви.

**Что нужно сделать:** Определить REST/WS контракты для world-service и social-service, позволяющие фронтенду и игровым системам запрашивать историю фракций, активировать сезонные события и синхронизировать world flags.

---

## 🎯 Цель задания

Обеспечить:
- Доступ к хронологической ленте фракций по периодам и регионам
- Управление историческими событиями, сезонными волнами и world-state изменениями
- Привязку репутаций, экономических эффектов и narrative-квестов к конкретным эпохам
- Поддержку аналитики и телеметрии для исторических активностей
- Интеграцию с Seasonal Events, Global Research и Raid Scenarios

---

## 📚 Источники информации

- `.BRAIN/03-lore/factions/factions-timeline-2020-2093.md` — основная хронология и API контуры
- Дополнительно:
  - `.BRAIN/02-gameplay/world/seasonal-events-2020-2093.md`
  - `.BRAIN/02-gameplay/world/global-research-2020-2093.md`
  - `.BRAIN/02-gameplay/world/raids/faction-raid-scenarios.md`
  - `.BRAIN/03-lore/factions/factions-original-catalog.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/world/factions/history-timeline.yaml`  
**Микросервисы:** world-service (основа), social-service (репутации), economy-service (исторические модификаторы), analytics-service (телеметрия), narrative-service (квестовые привязки)

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/factions/history` — сводная лента эпох, фильтры по периоду, типу фракции, региону.
2. `GET /api/v1/world/factions/history/{eraId}` — детальные данные об эпохе: события, влияния, механики, API hooks.
3. `GET /api/v1/world/factions/history/{eraId}/events` — расписание ключевых событий и их текущий статус (active, scheduled, archived).
4. `POST /api/v1/world/factions/history/{eraId}/activate` — активация эпохи или сезонной волны, обновление world flags и связанных сервисов.
5. `POST /api/v1/world/factions/history/{eraId}/record` — фиксация результатов исторического события (репутации, экономика, narrative флаги).
6. `GET /api/v1/world/factions/history/{eraId}/branches` — региональные ветви и их уникальные механики.
7. WebSocket `/ws/world/factions/history/{eraId}` — события `EraActivated`, `EventTriggered`, `BranchUnlocked`, `OutcomeRecorded`.
8. Интеграции: seasonal-service `POST /api/v1/world/seasons/apply`, analytics-service `POST /api/v1/analytics/history/track`, narrative-service `POST /api/v1/narrative/history/hooks`.
9. Схемы: `EraSummary`, `EraDetail`, `HistoricalEvent`, `ActivationRequest`, `OutcomePayload`, `RegionalBranch`, `TelemetryRecord`.
10. Observability: KPI `era_engagement_score`, `historical_event_completion`, `branch_participation_rate`, дашборды `historical-influence`, `era-economy-impact`.

---

## ✅ Критерии приемки

1. Все маршруты используют префикс `/api/v1/world/factions/history`.
2. Эпохи и подфракции соответствуют документу (Urban Scribes… Echo Dominion плюс региональные ветви).
3. Поддержаны статусы событий (planned/active/completed/archived) и их влияния на world-state.
4. Ошибки оформлены через `shared/common/responses.yaml#/components/schemas/Error`.
5. Активированные эпохи синхронизируются с Seasonal Events и Global Research.
6. WebSocket payload включает идентификаторы эпохи, события, региона и изменённых показателей.
7. Target Architecture описывает фронтенд `modules/world/history` и state store `world/history`.
8. Указаны ограничения по активации эпох (cooldown, максимум активных одновременно).
9. Телеметрия охватывает KPI из документа (`bioTideScore`, `war_research_sync`, `Metanet Tribunals`).
10. Документированы связи с квестами и рейдами (Quest DB, Raid Scenarios, Guild Contract Board).

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

