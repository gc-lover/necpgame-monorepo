# Task ID: API-TASK-272
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** completed
**Создано:** 2025-11-08 01:25
**Завершено:** 2025-11-08 23:40
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-269 (faction cult defenders API), API-TASK-267 (specter HQ suite API)

## 📦 Результат

- Добавлены `quest-chains.yaml`, `quest-chains-components.yaml`, `quest-chains-examples.yaml` (каталог контрактов, ветвления, прогресс, завершение, WebSocket).
- Задокументированы интеграции с social-service, economy-service, analytics-service и KPI `contractSuccessRate`, `branchPreferenceIndex`.
- Обновлены `brain-mapping.yaml`, `.BRAIN/02-gameplay/world/factions/faction-quest-chains.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `faction-quest-chains.yaml`, описывающую многоступенчатые фракционные квесты, ветвления, репутационные развилки и интеграцию с world/social сервисами.

**Что нужно сделать:** Реализовать REST/WS контракт для выдачи, трекинга и завершения цепочек фракций (Aeon, Crescent, Mnemosyne, Ember, Basilisk, Quantum Fable, Echo Dominion и др.).

---

## 🎯 Цель задания

Обеспечить:
- Каталог цепочек с требованиями вступления и контактными NPC
- Управление ветками (escort/sabotage/archive/tribunal и т.д.)
- Телеметрию прогресса и world flag updates
- Репутационные изменения и социальные эффекты
- Интеграцию с контрактной доской гильдий и экономическими активами

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/factions/faction-quest-chains.md` — этапы, ветви, API карта, SQL.
- Дополнительно:
  - `.BRAIN/02-gameplay/world/faction-cult-defenders.md`
  - `.BRAIN/02-gameplay/world/specter-hq.md`
  - `.BRAIN/02-gameplay/world/economy-specter-helios-balance.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/factions/quest-chains.yaml`  
**Микросервисы:** world-service (основа), social-service (репутации), economy-service (награды)

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/factions/contracts` — список доступных цепочек (фильтры по репутации/фракции).
2. `GET /api/v1/world/factions/contracts/{chainId}` — детали (этапы, ветви, награды).
3. `POST /api/v1/world/factions/contracts/{chainId}/accept` — старт цепочки (валидация требований).
4. `POST /api/v1/world/factions/contracts/{chainId}/choose` — выбор ветви (escort vs sabotage etc.).
5. `POST /api/v1/world/factions/contracts/{chainId}/progress` — обновление этапа (с world flag updates).
6. `POST /api/v1/world/factions/contracts/{chainId}/outcome` — финал, награды, репутации, последующие события.
7. WebSocket `/ws/world/factions/contracts/{chainId}` — `StageStart`, `StageUpdate`, `ChoiceLocked`, `OutcomeApplied`.
8. Схемы данных: `FactionChain`, `ChainStage`, `BranchOption`, `ProgressUpdate`, `OutcomePayload`, `ReputationChange`, `RewardPayload`.
9. Интеграции: social-service (`POST /api/v1/social/factions/reputation`), economy-service (`POST /api/v1/economy/factions/reward`), analytics-service (`/analytics/factions/contracts`).
10. Observability: KPI (contractSuccessRate, branchPreferenceIndex).

---

## ✅ Критерии приемки

1. Префикс `/api/v1/world/factions/contracts` соблюдён.
2. Target Architecture (комментарий) описывает world/social/economy + frontend `modules/world/contracts`.
3. Требования вступления валидируются и возвращают понятные ошибки.
4. Rewards включают активы из `faction-economy-integration`.
5. Репутации обновляются для нескольких фракций (positive/negative).
6. Telemetry события соответствуют документу (`contract_viewed`, `contract_completed`).
7. Возможность rollback/abort (описать 409/410 случаи).
8. Ограничения по cooldown/lockout реализованы.
9. WebSocket payload включает stageId, branchId, progressState.
10. FAQ: повторный старт, смена ветки, оffline прогресс, взаимодействие с Guild Contract Board.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

