# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-404  
- **Type:** API Generation  
- **Priority:** critical  
- **Status:** queued  
- **Created:** 2025-11-09 23:15  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-401 (combat implants), API-TASK-402 (combos & synergies), API-TASK-403 (combat roles), API-TASK-382 (combat ballistics telemetry), API-TASK-113 (combat session backend)

## Summary
Разработать OpenAPI спецификацию `api/v1/gameplay/combat/abilities/abilities.yaml`, описывающую систему боевых способностей NECPGAME: источники (экипировка, импланты, прокачка, кибердека), типы слотов (Q/E/R/Passive/Cyberdeck), ограничения (кулдауны, ресурсы, перегрев, энергия), комбинирование и взаимодействие с ролями, имплантами и комбо. Спецификация должна обеспечивать gameplay-service полнофункциональным API управления способностями и их билд-конфигурациями.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/combat/combat-abilities.md` |
| Version | v1.2.0 |
| Status | approved |
| API readiness | ready (2025-11-09 02:49) |

**Key points:** подтверждённый вариант G (способности приходят из экипировки, имплантов, прокачки, кибердеки); структура слотов Q/E/R/Passive/Deck; комбинированные ограничения (кулдауны+ресурсы+перегрев); гибридные билды, сетовые/брендовые синергии, киберпсихоз влияние; поддержка хакерских способностей, energy budget, конфликтов.  
**Related docs:** `combat-implants-types.md`, `combat-combos-synergies.md`, `combat-roles-detailed.md`, `combat-session-backend.md`, `economy/equipment-matrix.md`, `progression/classes-overview.md`, `combat-psycho/…`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay.combat  
- **API directory:** `api/v1/gameplay/combat/abilities/abilities.yaml`  
- **base-path:** `/api/v1/gameplay/combat/abilities`  
- **Java package:** `com.necpgame.gameplay.combat.abilities`  
- **Frontend module:** `modules/combat/abilities`  
- **Shared clients:** `@api/gameplay/combat`, `@shared/state/useAbilityLoadout`, `@shared/ui/AbilityPlanner`

> Все параметры сверяй с таблицей микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Определить REST endpoints для каталога способностей, фильтров по источникам/типам/слотам, получения деталей и их ограничений.
2. Смоделировать схемы: `CombatAbility`, `AbilitySlot`, `AbilitySource`, `AbilityRestriction`, `AbilityComboTag`, `AbilityUpgradePath`, `AbilityConflict`.
3. Реализовать эндпоинты для управления loadout-сетами (`GET/POST/DELETE /loadouts`) и проверки энергобюджета, перегрева и совместимости.
4. Добавить симуляцию использования (`POST /simulate`) — вычисляет потребление ресурсов, кулдауны, риск киберпсихоза, взаимодействие с имплантами/комбо.
5. Документировать интеграцию с имплантами (обязательные/опциональные импланты), экипировкой (equipment-matrix), progression (unlock requirements).
6. Описать хакерские способности: отдельный слот кибердеки, требования доступа к сети, противодействие, NetWatch trace.
7. Зафиксировать метрики и аналитические эндпоинты (`POST /analytics/refresh`, `GET /analytics/usage`) для балансировки.
8. Описать Kafka события `combat.abilities.loadout.saved`, `combat.abilities.used`, `combat.abilities.balance.flagged`, `combat.abilities.overheat`.
9. Обеспечить безопасность: `bearerAuth`, `X-AntiCheat-Signature`, rate limits на симуляции, аудит изменений лоадаутов.
10. Обновить `brain-mapping.yaml`, `tasks/queues/queued.md`, выполнить `validate-swagger.ps1` после генерации спецификации.

## Endpoints
- `GET /catalog` — список способностей с фильтрами по источнику (equipment/implant/progression/deck), типу (active/passive/hacking), роли, бренду.
- `GET /catalog/{abilityId}` — подробная информация: эффекты, требования, слоты, кулдауны, ресурсы, перегрев, киберпсихоз влияние.
- `POST /loadouts/validate` — проверка сборки способностей (энергия, перегрев, конфликты, слоты).
- `GET /loadouts` — сохранённые конфигурации игрока.
- `POST /loadouts` — создание/обновление конфигурации (abilitySlotMap, импланты, экипировка).
- `DELETE /loadouts/{loadoutId}` — удаление конфигурации.
- `POST /simulate` — имитация последовательности способностей (возвращает timeline потребления ресурсов, эффектов, перегрева).
- `GET /sources` — список источников (equipment, implants, progression nodes, deck programs) с ссылками на внешний API.
- `POST /analytics/refresh` — запуск пересчёта метрик использования, win-rate, перегрева.

## Data Models
- `CombatAbility` — id, name, description, slotType (Q/E/R/Passive/Deck), sourceType, rarity, energyCost, cooldown, resourceCost, overheatValue, psychoRisk, tags.
- `AbilitySource` — sourceType, sourceId, requirements (level, quest, faction, brand), unlockConditions.
- `AbilityRestriction` — cooldown, resourceTypes (energy/health/ammo), energyBudgetCost, overheatThreshold, conflictIds.
- `AbilityComboTag` — references to combos (API-TASK-402), synergy bonuses, required partners.
- `AbilityUpgradePath` — stages (required progression nodes, implants, equipment upgrades).
- `AbilityLoadout` — slot assignments, energyBudget, overheatRisk, conflictWarnings, synergyScore.
- `AbilityConflict` — conflictType, conflictingAbilityIds, mitigation, notes.
- Общие компоненты: `StandardError`, `ValidationError`, `Pagination`, `bearerAuth`, `AntiCheatSignature`, `OperationResult`.

## Integrations & Events
- REST зависимости:  
  - `implants-service` (API-TASK-401)  
  - `combos-service` (API-TASK-402)  
  - `roles-service` (API-TASK-403)  
  - `equipment-service` (`equipment-matrix`)  
  - `progression-service` (unlock tree)  
  - `combat-session-service` (ability usage telemetry)  
- Kafka topics:  
  - `gameplay.combat.abilities.loadout.saved` (consumers: telemetry, social, anti-cheat)  
  - `gameplay.combat.abilities.used` (combat session analytics)  
  - `gameplay.combat.abilities.balance.flagged` (balance team)  
  - `gameplay.combat.abilities.overheat` (anti-cheat, notification)  
- WebSocket: `/ws/gameplay/combat/abilities/{sessionId}` — live обновления cooldowns/overheat для HUD (описать в `x-streams`).

## Acceptance Criteria
1. Создан файл `api/v1/gameplay/combat/abilities/abilities.yaml` (≤ 500 строк) и проходит `OpenAPI 3.0.3` валидацию.
2. `info.x-microservice` указывает gameplay-service, порт 8083, base-path `/api/v1/gameplay/combat/abilities`.
3. Все перечисленные endpoints оформлены с параметрами, запросами, ответами, кодами ошибок, примерами, ссылками на shared компоненты.
4. Схемы `CombatAbility`, `AbilitySource`, `AbilityRestriction`, `AbilityComboTag`, `AbilityUpgradePath`, `AbilityLoadout`, `AbilityConflict` определены с обязательными полями.
5. Отражены ограничения (кулдауны, ресурсы, перегрев, energy budget) и риск киберпсихоза.
6. Интеграции (implants, gear, combos, roles, progression) задокументированы в `x-integrations` и через ссылки.
7. Kafka события и WebSocket канал описаны в `x-events` и `x-streams` с payload, producers, consumers.
8. Безопасность: `bearerAuth`, `X-AntiCheat-Signature`, rate limit на симуляции, аудит запросов.
9. Метрики и observability (`combat_ability_usage_rate`, `combat_ability_overheat_total`, `combat_loadout_validation_latency_ms`) перечислены.
10. Обновлены `tasks/config/brain-mapping.yaml`, `tasks/queues/queued.md`, `.BRAIN` документ содержит актуальный блок статуса; `validate-swagger.ps1` проходит без ошибок.

## FAQ / Notes
- Способности могут иметь брендовую привязку — предусмотреть поле `brandSignature`, ссылку на equipment-matrix.
- Важно хранить влияние на киберпсихоз (`psychoRisk`) и механики перегрева — интеграция с combat-cyberpsychosis.
- Поддержать `macroSupport` и античит проверки (что разрешено, что нет).
- Важен `APM difficulty` и `coordinationNeed` для UI подсказок — включить в модели.
- Хакерские способности требуют инфраструктуры сетей (world-service); укажи зависимость в `x-integrations`.

