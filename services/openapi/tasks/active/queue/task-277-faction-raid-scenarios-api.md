# Task ID: API-TASK-277
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:15
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-269 (faction cult defenders API), API-TASK-270 (specter surge loot API), API-TASK-272 (faction quest chains API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `faction-raid-scenarios.yaml`, описывающую эскалацию фракционных рейдов, сигнальные миссии и экономику наград.

**Что нужно сделать:** Сформировать REST/WS контракты world-service для каталогизации рейдов, отслеживания фаз и распределения лута/облигаций.

---

## 🎯 Цель задания

Обеспечить:
- Каталог рейдов по фракциям с триггерами и сигнальными миссиями
- Запуск, управление и завершение фаз с мировыми эффектами
- Регистрацию сигналов и прогресса в world flags
- Интеграцию с economy-service (Raid Bonds), combat-session, analytics-service
- Рассылку событий для фронтенда `modules/world/raids` и сценариев Specter/Helios

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/raids/faction-raid-scenarios.md` — фазы рейдов, сигналы, экономика
- Дополнительно:
  - `.BRAIN/02-gameplay/world/faction-cult-defenders.md`
  - `.BRAIN/02-gameplay/world/specter-hq.md`
  - `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md`
  - `.BRAIN/02-gameplay/world/dungeons/dungeon-bosses-abilities.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/factions/raid-scenarios.yaml`  
**Микросервисы:** world-service (основной), combat-service (инстансы боёв), economy-service (награды), analytics-service (telemetry), notification-service (broadcast)

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/raids` — список рейдов, статус зарядки, доступность.
2. `GET /api/v1/world/raids/{raidId}` — детали фаз, сигнальных миссий, наград, world flags.
3. `POST /api/v1/world/raids/{raidId}/signal` — регистрация успешной/проваленной сигнальной миссии (`signalCode`, `success`).
4. `POST /api/v1/world/raids/{raidId}/start` — запуск рейда (GM/auto), проверка условий (resources, reputation, defenders).
5. `POST /api/v1/world/raids/{raidId}/phase` — переход между фазами, фиксация механик и combat hooks.
6. `POST /api/v1/world/raids/{raidId}/outcome` — распределение наград, Raid Bonds, обновление economy-service и city_unrest.
7. WebSocket `/ws/world/raids/{raidId}` — события `PhaseStart`, `MechanicTrigger`, `OutcomeApplied`, `LootDistributed`.
8. Интеграции: economy-service `POST /api/v1/economy/rewards/raid`, combat-session `POST /api/v1/combat/instances/{instanceId}/state`, analytics-service `POST /api/v1/analytics/raids/track`.
9. Схемы: `RaidScenario`, `SignalMission`, `PhaseDescriptor`, `RaidState`, `OutcomePayload`, `RewardMatrix`, `TelemetryEvent`.
10. Observability: метрики `raid_completion_rate`, `signal_success_ratio`, `raid_bond_volume`, dashboards `raid-escalation`, `raid-economy`.

---

## ✅ Критерии приемки

1. Все маршруты используют префикс `/api/v1/world/raids`.
2. Фазы и сигнальные миссии соответствуют таблице (orbital-lockdown, solar-surge, purge-litany, mech-rampart, metanet-dominion).
3. Поддерживаются параметры сложности (Bronze → Mythic+) и влияния на outcome.
4. Raid Bonds отражены в API и интегрируются с economy-service и аукционами.
5. Ошибки согласованы с `shared/common/responses.yaml#/components/schemas/Error`.
6. Поддержаны откаты/прерывания рейда (409/410) и повторные сигнальные попытки с cooldown.
7. WebSocket payload включает `phaseId`, `mechanicKey`, `rewardTier`.
8. Target Architecture описывает взаимодействие с `modules/world/raids` и Specter/Helios прогресс панелями.
9. Телеметрия включает события `SIGNAL_REGISTERED`, `PHASE_COMPLETED`, `RAID_OUTCOME_APPLIED`.
10. Документированы ограничения по rate limits и требования для GM override.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

