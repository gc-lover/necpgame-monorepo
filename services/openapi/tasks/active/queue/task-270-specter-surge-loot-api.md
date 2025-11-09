# Task ID: API-TASK-270
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:10
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-260 (stock-exchange management), API-TASK-266 (specter-helios balance)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `specter-surge-loot.yaml`, описывающую лут, прогрессию и экономику рейда Specter Surge (фазы I–V, сложности, недельные ротации, лимиты, награды, репутации).

**Что нужно сделать:** Реализовать контракт для world-, economy- и social-сервисов (≤400 строк, возможно разделить по доменам), включив REST/WS endpoints, модели данных, телеметрию.

---

## 🎯 Цель задания

Сформировать единый источник правды для Specter Surge:
- Таблицы наград по фазам и сложностям
- Недельные ротации/модификаторы и расписания
- Прогрессия игроков/гильдий, лимиты и catch-up
- Экономические операции и магазин Specter HQ
- Репутационные изменения и социальные события
- Телеметрия, KPI, анти-абьюз механики

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/raids/specter-surge-loot.md` — лут-таблицы, ротации, API карта, SQL, телеметрия.
- Дополнительно:
  - `.BRAIN/02-gameplay/world/specter-hq.md`
  - `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md`
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/raids/specter-surge-loot.yaml`  
**Микросервисы:** world-service, economy-service, social-service  
**Порт:** 8086 / 8085 / 8084 via gateway

```
API-SWAGGER/api/v1/gameplay/world/raids/
└── specter-surge-loot.yaml
```

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/raids/specter-surge/loot` — базовые таблицы (фильтры: phase, difficulty, rotation).
2. `GET /api/v1/world/raids/specter-surge/rotations/current` и `/schedule`.
3. `POST /api/v1/world/raids/specter-surge/complete` — завершение рейда, world-state updates.
4. `POST /api/v1/economy/raid/rewards/claim` — распределение наград (base, modifiers).
5. `GET /api/v1/economy/raid/store/catalog` / `POST /purchase` — магазин Specter HQ (raid items).
6. `POST /api/v1/social/reputation/raid` — репутации и события.
7. `GET /api/v1/world/raids/specter-surge/limits` — недельные лимиты и catch-up.
8. WebSocket `/ws/world/raids/specter-surge` — `Phase`, `Rotation`, `Reward`, `Lockout`, `CatchUp`.
9. Схемы: `LootTableEntry`, `Rotation`, `RewardDistribution`, `StoreItem`, `LimitInfo`, `ReputationChange`, `TelemetrySnapshot`.
10. KPI/Observability: latency, queues, anti-abuse; описать PagerDuty.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/world/raids/specter-surge` (+ economy/social endpoints) соблюдён.
2. Target Architecture включает фронтенд модули (`modules/world/raids`, `modules/economy`, `modules/social`).
3. Rewards возвращают структуру: base, modifiers (`unrest`, `prestige`, `rotation`), currency breakdown.
4. Лимиты учитывают weekly cap и catch-up, ошибки 409/429 при превышении.
5. Rotations перечисляют модификаторы и активные бонусы.
6. Магазин Specter HQ интегрирован с economy-service (лимиты, валюты, ledgerId).
7. Репутации обновляются через social-service; предусмотреть negative outcomes.
8. Telemetry покрывает KPI (`avg_raid_duration`, `loot_claim_success_rate`, `store_latency`).
9. Anti-abuse поля (account/device) описаны в ответах и комментариях.
10. FAQ: повторные прохождения, частичное завершение, emergency rollback, взаимодействие с city unrest.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

