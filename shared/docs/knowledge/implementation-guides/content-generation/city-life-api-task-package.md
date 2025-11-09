---
**Статус:** draft  
**Версия:** 0.1.1  
**Дата:** 2025-11-07 21:03  
**Автор:** AI Brain Manager
---

# City Life API Task Package

- **Status:** ready-to-create
- **Target tasks:**
- **Last Updated:** 2025-11-08 09:37

## Финальные baseline и культурные рамки

| Район | Население (лор) | Baseline `populationTarget` | Ключевые фракции | Культурные запреты |
|-------|------------------|-----------------------------|------------------|---------------------|
| Watson (Night City) | 800k | 800000 | Maelstrom 45%, Tyger Claws 20%, Arasaka 12%, NCPD 5% | Нет корпоративных VIP-ивентов, уважение мемориалов (Jackie Welles) |
| Westbrook (Night City) | 300k | 300000 | Arasaka 45%, Tyger Claws 30%, Kang Tao 12%, City Elites 8% | Запрет на уличные протесты в North Oak |
| Shinjuku (Tokyo) | 550k (верхние уровни) | 550000 | Megacorp Syndicates 30%, Local Yakuza 22%, Nightlife Collective 15%, Metro Authority 10% | Запрет хоррор-перформансов в Asakusa (уважение храмов) |
| Kreuzberg (Berlin) | 260k (оценка из лора) | 260000 | Flux Kollektiv 30%, Urban Phantoms 20%, EuroDyne 18%, Stahlfist 12% | Запрет корпоративных парадов, сохранение автономии коммун |

Лор согласован по документам:
- `03-lore/locations/night-city/watson-detailed-2020-2093.md`
- `03-lore/locations/night-city/westbrook-detailed-2020-2093.md`
- `03-lore/locations/world-cities/tokyo-detailed-2020-2093.md`
- `03-lore/locations/world-cities/berlin-detailed-2020-2093.md`

## ER-схемы и ключевые сущности

- `city_districts` (world-service): `city_id`, `district_id`, `metrics`, `population_target`, `faction_dominance`, `version`.
- `infrastructure_instances` (economy-service): `instance_id`, `district_id`, `category`, `capacity`, `state`, `owner_faction`.
- `npc_profiles` (social-service): `npc_id`, `archetype_id`, `rarity`, `district_id`, `faction_id`, `priority_score`.
- `npc_schedules` (social-service): `schedule_id`, `npc_id`, `mode`, `fsm`, `slots`, `version`.
- `world_events_bindings` (world-service): `event_id`, `district_id`, `demand_modifiers`, `npc_modifiers`, `duration`, `severity`.
- `player_impact_log` (gameplay-service): `impact_id`, `player_id`, `delta`, `affected_districts`, `timestamp`, `processed`.

## REST эндпоинты для swagger-задач

### World-service
1. `POST /world/cities/{cityId}/recompute`
   - Request: `{ "scope": "district", "targets": ["watson"], "reason": "player_impact", "triggerId": "impact-123" }`
   - Response 202: `{ "jobId": "recompute-456", "eta": "PT3M" }`
   - Kafka emit: `world.city.lifecycle.requested`
2. `GET /world/cities/{cityId}/snapshot`
   - Query: `include=metrics,infrastructure,npcOverview`
   - Response: см. payload ниже
3. `GET /world/cities/{cityId}/districts/{districtId}` → `district_profile`, версии инфраструктуры, живость

**Snapshot payload:**
```
{
  "cityId": "night-city",
  "version": 27,
  "generatedAt": "2025-11-07T19:55:00Z",
  "livingMetrics": {
    "npcDensity": { "target": 0.72, "current": 0.70 },
    "infrastructureLoad": 0.78,
    "diversityIndex": 1.92
  },
  "districts": [
    {
      "districtId": "watson",
      "profile": "industrial",
      "metrics": {"density": 0.72, "crime": 0.82},
      "infrastructureVersion": 14,
      "npcOverview": {"total": 450000, "dynamic": 0.31}
    }
  ]
}
```

### Social-service
1. `GET /social/npc/{npcId}` → `{ profile, schedule, relationships, currentState }`
2. `GET /social/npc` (filters: `districtId`, `rarity`, `status`)
3. `PATCH /social/npc/{npcId}/schedule` → обновление расписаний после пересчёта

**Kafka:**
- `social.npc.schedule.v1` (key `npcId`, payload ниже)
- `social.npc.relationship.updated`

```
{
  "npcId": "npc-8f1c",
  "scheduleVersion": 5,
  "mode": "normal_day",
  "stateMachine": {
    "states": ["home", "transit", "work", "leisure"],
    "transitions": ["home->transit", "transit->work"]
  },
  "slots": [
    {"from": "06:00", "to": "08:00", "location": "home", "activity": "wake_up"},
    {"from": "08:00", "to": "08:45", "location": "metro-12", "activity": "commute", "transport": "metro"}
  ]
}
```

### Economy-service
1. `GET /economy/districts/{districtId}/infrastructure`
2. `PATCH /economy/infrastructure/{instanceId}` → смена `state`
3. `POST /economy/infrastructure/{instanceId}/alerts` → регистрация перегрузок

**Kafka:** `economy.infrastructure.alerts` (key `districtId`, payload `{ objectId, loadFactor, forecastLoad, status }`)

### Gameplay-service
1. `POST /gameplay/player-impact`
   - `{ "playerId": "p-31", "impactType": "quest", "delta": { "faction.maelstrom": 12 }, "affectedDistricts": ["watson"], "severity": "major" }`
2. `GET /gameplay/player-impact/{impactId}`
3. `GET /gameplay/city-activity/{cityId}` → агрегированные события

**Kafka:** `gameplay.player.impact` (key `impactId`)

## Пакеты данных

- Baseline JSON: `content-generation/baseline/night-city-watson.json`, `night-city-westbrook.json`, `tokyo-shinjuku.json`, `berlin-kreuzberg.json`
- Алгоритм: `city-life-population-algorithm.md` (версия 0.3.0)
- Планы симуляций: `city-life-simulation-plan.md`

## Шаблон задачи для API Task Creator

```
title: "City Life API Package — world/social/economy/gameplay"
description:
  1. Реализовать эндпоинты и kafka topics согласно `city-life-api-task-package.md`.
  2. Подготовить OpenAPI схемы и DTO для ER-сущностей.
  3. Настроить зависимые сервисы (world-service, social-service, economy-service, gameplay-service).
  4. Валидация: использовать baseline JSON и payload примеры из пакета.
attachments:
  - path: .BRAIN/05-technical/content-generation/city-life-api-task-package.md
  - path: .BRAIN/05-technical/content-generation/city-life-population-algorithm.md
  - path: .BRAIN/05-technical/content-generation/baseline/
``` 

После создания карточки в `API-SWAGGER/tasks/active/queue` указать ссылку на данный документ и привязать сервисы.
---
**Статус:** draft  
**Версия:** 0.1.0  
**Дата:** 2025-11-07 20:45  
**Автор:** AI Brain Manager
---

# City Life API Task Package

## Цель

Подготовить входные данные для API Task Creator: финальные сущности, события и эндпоинты, необходимые для интеграции алгоритма оживления городов в API-SWAGGER (world-service, social-service, economy-service, gameplay-service).

## Контракты и эндпоинты

### World-service

1. `POST /world/cities/{cityId}/recompute`
   - scope: `city` | `district`
   - payload: `{ "scope": "district", "targets": ["watson"], "reason": "player_impact", "triggerId": "impact-123" }`
   - response: `202 Accepted` → `{ "jobId": "recompute-456", "eta": "PT3M" }`
   - kafka emit: `world.city.recompute.requested`
2. `GET /world/cities/{cityId}/snapshot`
   - query: `include=metrics,infrastructure,npcOverview`
   - response: агрегированное состояние города (см. payload ниже)
3. `GET /world/cities/{cityId}/districts/{districtId}`
   - возвращает `district_profile`, метрики, версии инфраструктуры

**Payload snapshot:**
```
{
  "cityId": "night-city",
  "version": 27,
  "generatedAt": "2025-11-07T19:55:00Z",
  "livingMetrics": {
    "npcDensity": { "target": 0.72, "current": 0.70 },
    "infrastructureLoad": 0.78,
    "diversityIndex": 1.92
  },
  "districts": [
    {
      "districtId": "watson",
      "profile": "industrial",
      "metrics": {"density": 0.72, "crime": 0.82},
      "infrastructureVersion": 14,
      "npcOverview": {"total": 450000, "dynamic": 0.31}
    }
  ]
}
```

### Social-service

1. `GET /social/npc/{npcId}` → `{ profile, schedule, relationships, currentState }`
2. `GET /social/npc` (query) → фильтрация по `districtId`, `rarity`, `status`
3. `PATCH /social/npc/{npcId}/schedule` → обновление расписания после пересчёта

**Kafka:**
- `social.npc.schedule.v1` (key `npcId`)
- `social.npc.relationship.updated`

**Schedule payload:**
```
{
  "npcId": "npc-8f1c",
  "scheduleVersion": 5,
  "mode": "normal_day",
  "stateMachine": {
    "states": ["home", "transit", "work", "leisure"],
    "transitions": ["home->transit", "transit->work"]
  },
  "slots": [
    {"from": "06:00", "to": "08:00", "location": "home", "activity": "wake_up"},
    {"from": "08:00", "to": "08:45", "location": "metro-12", "activity": "commute", "transport": "metro"}
  ]
}
```

### Economy-service

1. `GET /economy/districts/{districtId}/infrastructure`
2. `PATCH /economy/infrastructure/{instanceId}` — обновление статуса `planned|active|degraded|offline`
3. `POST /economy/infrastructure/{instanceId}/alerts` — регистрация событий перегрузки

**Kafka:** `economy.infrastructure.alerts` (key `districtId`, payload `{ objectId, loadFactor, forecastLoad, status }`)

### Gameplay-service

1. `POST /gameplay/player-impact`
   - payload: `{ "playerId": "p-31", "impactType": "quest", "delta": { "faction.maelstrom": 12 }, "affectedDistricts": ["watson"], "severity": "major" }`
2. `GET /gameplay/player-impact/{impactId}`
3. `GET /gameplay/city-activity/{cityId}` — агрегированные события игроков

**Kafka:** `gameplay.player.impact`

## ER-схемы (кратко)

- `city_districts`: ключевые поля `city_id`, `district_id`, `metrics`, `population_target`, `version`
- `infrastructure_instances`: `instance_id`, `district_id`, `category`, `capacity`, `state`, `owner_faction`
- `npc_profiles`: `npc_id`, `archetype_id`, `rarity`, `district_id`, `faction_id`, `priority_score`
- `npc_schedules`: `schedule_id`, `npc_id`, `mode`, `fsm`, `slots`, `version`
- `world_events_bindings`: `event_id`, `district_id`, `demand_modifiers`, `npc_modifiers`, `duration`
- `player_impact_log`: `impact_id`, `player_id`, `impact_type`, `delta`, `affected_districts`, `timestamp`, `processed`

## Зависимости и данные

- Baseline JSON: `content-generation/baseline/*.json`
- Лор: Night City (Watson, Westbrook), Tokyo (Shinjuku), Berlin (Kreuzberg)
- Телеметрия: `gameplay-service` события игрока, `economy-service` нагрузка инфраструктуры, `social-service` расписания

## Требования к API задачам

1. Определить схемы запросов/ответов (OpenAPI) для перечисленных эндпоинтов.
2. Описать kafka topics, ключи и payload.
3. Задокументировать схемы DTO для ER-сущностей.
4. Указать связи с существующими swagger API (world-state, social, economy, gameplay).
5. Подготовить тестовые примеры с использованием baseline JSON.

## Пакет для API Task Creator

- Документ: `city-life-api-task-package.md`
- Приложения: baseline файлы, алгоритм 0.3.0, ER-схемы, payload примеры.
- Следующий шаг: создать карточку в API-SWAGGER/tasks с ссылкой на этот пакет и указанием микросервисов world/social/economy/gameplay.

---

## История изменений

- 2025-11-08 — финализирован пакет задач, добавлен статус готовности и список целевых API задач.
- 2025-11-07 — первичный пакет API для City Life.
