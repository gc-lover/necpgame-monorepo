# Task ID: API-TASK-269
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:05
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-248 (dungeon bosses API), API-TASK-249 (world bosses API)

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию `faction-cult-defenders.yaml`, которая описывает легендарных защитников фракций: список, способности, триггеры появления, награды, мировые последствия и события WebSocket.

**Что нужно сделать:** Создать контракт для world-service (с интеграциями economy-, social- и analytics-сервисов) на основе документа `faction-cult-defenders.md`.

---

## 🎯 Цель задания

Обеспечить API для:
- Каталога культовых защитников (`GET /factions/defenders`)
- Управления спавнами/исходами и мировыми эффектами
- Расписаний и триггеров (faction events, blackwall breaches, raids)
- Телеметрии и аналитики (clearTime, counterUsage)
- Связи с социальными/экономическими системами (репутации, лут, ответные события)

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/faction-cult-defenders.md` — список защитников, навыки, REST/WS контуры, SQL схемы, интеграции.
- Дополнительно:
  - `.BRAIN/02-gameplay/world/world-bosses-catalog.md`
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md`
  - `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/factions/defenders.yaml`  
**Микросервис:** world-service  
**Порт:** 8086

```
API-SWAGGER/api/v1/gameplay/world/factions/
└── defenders.yaml
```

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/factions/defenders` — список (фильтры: faction, difficulty, status).
2. `GET /api/v1/world/factions/defenders/{defenderId}` — детальная информация (abilities, counters, rewards).
3. `GET /api/v1/world/factions/defenders/{defenderId}/schedule` — таймеры появления.
4. `POST /api/v1/world/factions/defenders/{defenderId}/spawn` — принудительный запуск (админ).
5. `POST /api/v1/world/factions/defenders/{defenderId}/outcome` — фиксация результата, world-state updates.
6. WebSocket `/ws/world/factions/defenders/{defenderId}` — `Spawn`, `Phase`, `AbilityCast`, `CounterUsed`, `Outcome`, `Aftermath`.
7. Схемы: `Defender`, `DefenderAbility`, `AbilityChallenge`, `Counter`, `SpawnTrigger`, `OutcomePayload`, `ReputationChange`.
8. Аналитика: `defenderClearTime`, `defenderAbilityFailRate`, `counterUsage`.
9. Observability: метрики и PagerDuty (AbilityOverlap, DefenderTimeout).
10. FAQ — параллельные спавны, отмены, взаимодействия с сюжетом.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/world/factions/defenders` соблюдён.
2. Target Architecture комментарий с frontend модулем (`modules/world/events`).
3. Ответы возвращают `role`, `uniqueSkill`, shooter-based challenge requirements и контры.
4. Outcomes обновляют world-state (`faction_influence`, `district_security`, `blackwall_stability`) и репутации.
5. Rewards включают валюты, лут, титулы и ссылки на economy-service.
6. Сценарии связаны с city unrest/helios ops (описать интеграцию).
7. WebSocket payload содержит `phase`, `abilityCode`, `telemetry`.
8. Ошибки через `shared/common/responses.yaml`.
9. Схемы соответствуют SQL структурам из документа.
10. FAQ описывает edge cases (спавн нескольких защитников, комбинированные события, manual rollback).

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

