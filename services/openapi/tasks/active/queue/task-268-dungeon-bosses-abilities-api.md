# Task ID: API-TASK-268
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:00
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-248 (dungeon bosses catalog API), API-TASK-037 (combat shooting API)

---

## 📋 Краткое описание

Нужно создать спецификацию `dungeon-bosses-abilities.yaml`, описывающую матрицу боссов подземелий, их навыки, проверки D&D и контр-стратегии, а также REST/WS контуры world-service.

**Что нужно сделать:** Сформировать OpenAPI контракт (≤400 строк) с эндпоинтами для получения абилити боссов, расписаний, аналитики и управления событиями.

---

## 🎯 Цель задания

Обеспечить world-service данными о боссах:
- Каталог способностей и режимов (Normal → Mythic)
- REST API для выдачи информации клиенту и триггера событий
- WebSocket поток фаз/кастов и телеметрии
- Интеграция с combat-session, social-service и economy-service
- Схемы данных и контролируемые последствия (world-state, репутации)

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/dungeons/dungeon-bosses-abilities.md` — каталог боссов, навыков, контров, API карта, SQL схемы.
- Дополнительно:
  - `.BRAIN/02-gameplay/world/dungeons/dungeon-bosses-catalog.md`
  - `.BRAIN/02-gameplay/combat/combat-abilities.md`
  - `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/dungeons/abilities.yaml`  
**Микросервис:** world-service  
**Порт:** 8086

```
API-SWAGGER/api/v1/gameplay/world/
└── dungeons/
    ├── catalog.yaml
    └── abilities.yaml   ← создать
```

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/dungeons/bosses/abilities` — список с фильтрами (bossId, faction, mode).
2. `GET /api/v1/world/dungeons/bosses/{bossId}/abilities` — подробная информация (D&D проверки, counters, rewards).
3. `POST /api/v1/world/dungeons/bosses/{bossId}/spawn` — запуск события (админ).
4. `POST /api/v1/world/dungeons/bosses/{bossId}/outcome` — фиксация результата, world-state обновлений.
5. `GET /api/v1/world/dungeons/bosses/{bossId}/schedule` — расписание и таймеры.
6. WebSocket `/ws/world/dungeons/bosses/{bossId}` — `Spawn`, `Phase`, `AbilityCast`, `CounterUsed`, `Outcome`.
7. Схемы данных: `BossAbility`, `AbilityCheck`, `CounterStrategy`, `SpawnTrigger`, `OutcomePayload`, `TelemetrySnapshot`.
8. Интеграции: combat-session (`encounterId`), economy-service (rewards), social-service (репутации), analytics-service (metrics).
9. Observability: метрики `bossAbilityUsage`, `counterSuccessRate`, алерты `AbilityOverlap`, `ScenarioTimeout`.
10. FAQ с edge cases (спавн нескольких боссов, cancel, escalations).

---

## ✅ Критерии приемки

1. Префикс `/api/v1/world/dungeons/bosses` соблюдён.
2. Target Architecture (комментарий) включает microservice, frontend module, UI-компоненты.
3. Фильтры поддерживают режимы (Normal/Hard/Apex/Mythic) и фракции.
4. Выходные данные содержат D&D проверки (тип, DC, навыки).
5. Контр-стратегии возвращают тип (Quickhack/Gadget/Implant) и требования.
6. Outcome отражает world-state изменения (`faction_influence`, `district_security`).
7. Telemetry секция описывает события для analytics.
8. WebSocket описание содержит payload, heartbeat, reconnect policy.
9. Ошибки используют `shared/common/responses.yaml` (400/401/403/404/409/422/500).
10. Добавлен FAQ о параллельных событиях и ручной отмене.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

