# Task ID: API-TASK-281
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:20
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-280 (faction social dialogues API), API-TASK-271 (guild contract board API), API-TASK-276 (faction economy assets API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `romance-hanako-tanaka.yaml`, описывающую романтическую линию Hanako Tanaka (этапы 1–2): состояния, проверки, выборы, награды и интеграции с репутацией, контрактами и world events.

**Что нужно сделать:** Определить REST/WS контракты narrative-service для выдачи диалоговых узлов, трекинга прогресса, проверок статов, обработки веток (loyal, equal, respect) и синхронизации с другими сервисами.

---

## 🎯 Цель задания

Обеспечить:
- Каталог этапов романса (чайная комната, небесный сад) с условиями доступа и проверками
- Управление флагами (`flag.romance.hanako.*`), репутациями (`rep.romance.hanako`, `rep.corp.arasaka`) и выдачей наград
- Поддержку ветвления (loyal, equal, respect) и последствий для экономики/контрактов
- Интеграцию с Guild Contract Board, Seasonal/World events и analytics-service
- Поддержку UI `modules/narrative/romance` и state store `narrative/romance/hanako`

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/romance-hanako-tanaka.md` — структура этапов, YAML-узлы, проверки, флаги
- Дополнительно:
  - `.BRAIN/04-narrative/dialogues/faction-social-lines.md`
  - `.BRAIN/04-narrative/npc-lore/important/hanako-arasaka.md`
  - `.BRAIN/02-gameplay/social/romance-system.md`
  - `.BRAIN/02-gameplay/social/reputation-formulas.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/romance/romance-hanako-tanaka.yaml`  
**Микросервисы:** narrative-service (ядро), social-service (репутация), economy-service (ценовые модификаторы), world-service (flags/events), analytics-service (telemetry), notification-service (scene updates)

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/romance/hanako` — общее состояние романса (этапы, доступность, текущие флаги).
2. `GET /api/v1/narrative/romance/hanako/stage/{stageId}` — данные узлов этапа (ветви, проверки, награды, требования).
3. `POST /api/v1/narrative/romance/hanako/unlock` — активация романса после выполнения условий (`rep.corp.arasaka`, clearance, контракт).
4. `POST /api/v1/narrative/romance/hanako/branch` — выбор пути (loyal, equal, respect) с проверкой флагов/репутации.
5. `POST /api/v1/narrative/romance/hanako/progress` — фиксация узла, проверка статов, обновление флагов/репутаций.
6. `POST /api/v1/narrative/romance/hanako/outcome` — завершение этапа, награды, обновление world flags и контрактных связей.
7. WebSocket `/ws/narrative/romance/hanako` — события `NodeUnlocked`, `CheckPassed`, `BranchChosen`, `StageCompleted`.
8. Интеграции: social-service `POST /api/v1/social/reputation/update`, economy-service `POST /api/v1/economy/factions/modifier`, world-service `POST /api/v1/world/events/apply`, guild board `POST /api/v1/world/guilds/contracts/sync`.
9. Схемы: `RomanceState`, `StageDescriptor`, `DialogueNode`, `CheckResult`, `BranchPayload`, `OutcomePayload`, `NotificationEvent`.
10. Observability: KPI `romance_progress_rate`, `branch_distribution`, `check_failure_rate`, дашборды `romance-hanako-overview`, `romance-affinity-trend`.

---

## ✅ Критерии приемки

1. Все маршруты используют префикс `/api/v1/narrative/romance/hanako`.
2. Условия соответствуют документу (репутация, clearance, world events, flags).
3. Проверки статов (Persuasion, Willpower, Insight, Etiquette, Strategy, Technical) поддерживают модификаторы из флагов/предметов.
4. Ветки `loyal`, `equal`, `respect` отражают эффекты в economy/world-service.
5. Ошибки используют `shared/common/responses.yaml#/components/schemas/Error`.
6. Поддерживается откат/переигровка (cooldown, reset) с проверкой состояний (409/423).
7. WebSocket payload содержит идентификаторы узла, ветки, изменения репутации/флагов.
8. Target Architecture описывает взаимодействие с UI и state store `narrative/romance`.
9. Логируются ключевые события (`romance_stage_started`, `romance_check_failed`, `romance_branch_committed`, `romance_outcome_applied`) в analytics-service.
10. Документированы интеграции с Guild Contract Board и Seasonal events (например, `world.event.corporate_war_escalation`).

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

