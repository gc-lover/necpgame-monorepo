# Текущий статус активных направлений

**Обновлено:** 2025-11-09 15:20  
**Ответственный:** Brain Manager

---

## Active
- Combat Systems Wave 1 — pivot на shooter: combat-session, combat-ai-enemies, combat-implants-types, combat-shooter-core (в работе), combat-abilities, combat-shooting, combat-combos-synergies, combat-extract, combat-freerun, combat-hacking-networks, combat-hacking-combat-integration, combat-cyberspace и arena-system. D&D документы помечены `deprecated`. Единый пакет обновлён в `2025-11-09-combat-ai-package.md`, `2025-11-09-combat-wave-package.md`, `2025-11-09-combat-shooting-package.md`, `2025-11-09-combat-stealth-package.md`, `2025-11-09-combat-abilities-package.md`, `2025-11-09-combat-combos-brief.md`.
- Economy Core Refresh — trade-system и inventory-system/part1-core-system перепроверены 2025-11-09 01:30; REST и события (`/inventory/*`, `inventory:item-*`, `/trade/sessions`, `trade:completed`) описаны; детали, зависимости и разбиение задач фиксируем в `2025-11-09-economy-core-package.md`; ждём окно ДУАПИТАСК для передачи без затрагивания API-SWAGGER.
- Auth/Characters/Progression Package — перепроверены `.BRAIN/05-technical/backend/auth/README.md`, `player-character-mgmt/character-management.md`, `progression-backend.md` (2025-11-09 02:47); рабочий файл `2025-11-09-auth-characters-package.md` фиксирует REST/Events/Storage для auth-service, character-service и progression (gameplay-service).
- Quest Engine Package — материал `.BRAIN/05-technical/backend/quest-engine-backend.md` (ready) увязан с `quest-system.md` и shooter-механиками; детализация REST/WS/EventBus вынесена в `2025-11-09-quest-engine-package.md`, требуется финальная нарезка задач перед ДУАПИТАСК.
- Quest Branching Liquibase — подготовлен `scripts/migrations/quest-branching/master.xml`, `v1/01-create-core-tables.xml`, `v1/02-create-branching-tables.xml`, `v1/03-create-world-state-tables.xml`, `v1/04-indexes.xml`, `v1/05-shadow-triggers.xml`, `v1/06-materialized-views.xml`, `v1/07-rls-and-roles.xml` и README; план действий зафиксирован в `2025-11-09-quest-branching-liquibase-plan.md`, структурные решения в `2025-11-09-quest-branching-validation.md`; в работе оставшийся changeSet (`08-rollback-scripts.xml`).
- Quest Branching PoC — `2025-11-09-quest-branching-poc-plan.md` дополнен результатами; скрипты (`run_poc.ps1`, SQL) проверены на базе `quest_branching_poc` (Liquibase 5.0.1 + PostgreSQL 15). Update/refresh/rollback выполняются успешно, helper-функция чистит триггеры/RLS/роли.
## Pending
- gameplay-service/arena-system — подтвердить приоритет и окно ДУАПИТАСК для `.BRAIN/02-gameplay/combat/arena-system.md` (ready, рейтинговые циклы, `api/v1/gameplay/combat/arena-system.yaml`).
- gameplay-service/shooter-core — подготовить новый документ `.BRAIN/02-gameplay/combat/combat-shooter-core.md` (v0.1.0 draft) и бриф `combat-shooter-core-brief` для ДУАПИТАСК.
- gameplay-service/abilities — актуализировать бриф `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-09-combat-abilities-package.md` с учётом shooter модификаторов.
- gameplay-service/freerun — черновик брифа готов (`2025-11-09-combat-freerun-brief.md`), ждём окно ДУАПИТАСК.
- gameplay-service/stealth — черновик брифа (`2025-11-09-combat-stealth-brief.md`), требуется согласование со shooter ядром.
- gameplay-service/quests — финализировать REST/WS декомпозицию (обновлена под shooter) перед передачей ДУАПИТАСК.
- lore/quest timelines — `.BRAIN/03-lore/_03-lore/timeline-author/quests/america/vancouver/2020-2029/quest-009-granville-island.md` повышен до ready (2025-11-09 11:09, Brain Readiness Checker); остаётся оформить `.BRAIN/03-lore/_03-lore/timeline-author/quests/america/vancouver/2020-2029/quest-010-most-livable-city.md` и пакет `.BRAIN/03-lore/_03-lore/timeline-author/quests/america/washington-dc/2020-2029/quest-001-white-house.md`–`quest-007-pentagon.md` с привязкой к quest-engine. Дополнительно: `.BRAIN/03-lore/_03-lore/timeline-author/quests/cis/moscow/2061-2077/quest-035-implant-addiction.md` — needs-work, проверено 2025-11-09 09:37 Brain Manager (ждёт привязку к имплантам и экономике).
## Blocked
- *(блокеров нет)*

---

> После добавления задач обновите этот файл, синхронизируйте `TODO.md`, `readiness-tracker.yaml` и уведомления для ДУАПИТАСК.

