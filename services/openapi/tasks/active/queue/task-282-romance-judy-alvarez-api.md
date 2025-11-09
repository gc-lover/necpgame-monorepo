# Task ID: API-TASK-282
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:40
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-280 (faction social dialogues API), API-TASK-281 (romance hanako tanaka API), API-TASK-271 (guild contract board API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `romance-judy-alvarez.yaml`, описывающую три этапа романтической линии Джуди Альварес: студия брейндансов, AR-тур по Laguna Bend и подземная VR-лаборатория. Учесть ветвление путей, проверки статов, награды, репутации и синхронизации с социальными/экономическими подсистемами.

**Что нужно сделать:** Определить REST/WS контракты narrative-service для выдачи сцен, выборов и прогресса, синхронизировать флаги с social-service, обновлять репутации/награды и интегрировать с Guild Contract Board, world events и analytics.

---

## 🎯 Цель задания

Обеспечить:
- Каталог этапов (`stage1`, `stage2`, `stage3`) с условиями доступа, проверками и флагами (`flag.romance.judy.*`)
- Управление ветками (`path_trust`, `path_comfort`, `path_slow`) и углублёнными решениями (`activism`, `runaway`, `rebuild`)
- Поддержку брейнданс-синхронизации, выдачи бафов, обновления контрактов Мокси и world events
- Телеметрию привязанности, выбора веток и исходов
- UI/Frontend взаимодействие через `modules/narrative/romance`

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/romance-judy-alvarez.md` — структура этапов, YAML-узлы, проверки, флаги
- Дополнительно:
  - `.BRAIN/04-narrative/dialogues/faction-social-lines.md`
  - `.BRAIN/04-narrative/npc-lore/important/judy-alvarez.md`
  - `.BRAIN/02-gameplay/social/romance-system.md`
  - `.BRAIN/02-gameplay/social/reputation-formulas.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/romance/romance-judy-alvarez.yaml`  
**Микросервисы:** narrative-service (ядро), social-service (репутация и аффинити), economy-service (модификаторы и награды), world-service (events/flags), analytics-service (telemetry), notification-service (scene updates)

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/romance/judy` — общее состояние романса (этапы, доступность, текущие флаги, активные ветки).
2. `GET /api/v1/narrative/romance/judy/stage/{stageId}` — данные этапа: узлы, проверки, доступные ветки, награды, требования.
3. `POST /api/v1/narrative/romance/judy/unlock` — активация романса (проверка `rep.moxx`, `flag.moxx.support`, завершения защитной миссии).
4. `POST /api/v1/narrative/romance/judy/branch` — выбор пути (trust/comfort/slow → activism/rebuild/runaway) с проверкой флагов/репутаций.
5. `POST /api/v1/narrative/romance/judy/progress` — фиксация узла, проверка статов (Empathy, Technical, Performance, Hacking, Negotiation, Willpower), обновление флагов и наград.
6. `POST /api/v1/narrative/romance/judy/outcome` — завершение этапов, награды (бафы, чертежи, контрактные права), обновление world flags и social репутаций (`romance_judy`, `moxx_support`).
7. `POST /api/v1/narrative/romance/judy/reset` — служебный endpoint для soft reset/lockout (GM или scripted failover).
8. WebSocket `/ws/narrative/romance/judy` — события `SceneUnlocked`, `CheckResolved`, `BranchChosen`, `StageCompleted`, `RomanceOutcome`.
9. Интеграции: social-service `POST /api/v1/social/reputation/update`, economy-service `POST /api/v1/economy/factions/modifier`, world-service `POST /api/v1/world/events/apply`, guild board `POST /api/v1/world/guilds/contracts/sync`.
10. Схемы: `JudyRomanceState`, `RomanceStage`, `DialogueNode`, `CheckDescriptor`, `BranchChoice`, `OutcomePayload`, `NotificationEvent`, `TelemetryRecord`.

---

## ✅ Критерии приемки

1. Все маршруты используют префикс `/api/v1/narrative/romance/judy`.
2. Поддержаны все этапы (Lizzie's Bar, Laguna Bend AR, Moxxi VR Lab) и ветки (`path_trust`, `path_comfort`, `path_slow`, финальные решения).
3. Проверки статов соответствуют документу, поддерживают модификаторы из предметов/классов/флагов (`class.netrunner`, `flag.romance.judy.humor`).
4. Репутации (`romance_judy`, `moxx_support`) и флаги (`flag.romance.judy.*`) корректно обновляются, поддержана синхронизация с social-service.
5. Брейнданс-синхронизация (Stage2/Stage3) триггерит события для analytics и выдаёт бафы/статусы.
6. Ошибки используют `shared/common/responses.yaml#/components/schemas/Error`, с особыми случаями 409 (branch locked) и 423 (stage locked).
7. WebSocket payload включает stageId, nodeId, branchId, изменения репутации и наград.
8. Target Architecture описывает фронтенд `modules/narrative/romance` и state store `narrative/romance/judy`.
9. Документированы ограничения (cooldown между этапами, минимальный компаньон-ранг, лимиты на reset).
10. Телеметрия включает события (`romance_stage_started`, `bd_sync_success`, `romance_branch_committed`, `romance_outcome_applied`) и метрики (`romance_affinity_score`, `branch_distribution`, `bd_failure_rate`).

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

