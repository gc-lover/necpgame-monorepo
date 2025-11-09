# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-402  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 22:50  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-113 (combat session backend), API-TASK-401 (combat implants catalog), API-TASK-382 (combat ballistics telemetry)

## Summary
Сформировать OpenAPI спецификацию `api/v1/gameplay/combat/combos/combos-synergies.yaml`, описывающую боевые комбо и синергии: одиночные комбо способностей, командные сочетания, взаимосвязи с экипировкой/имплантами, тайминговые бонусы и системные модификаторы. Спецификация должна позволять gameplay-service рассчитывать эффекты, проверять требования, хранить пресеты и отдавать данные фронтенду `modules/combat/combos`.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/combat/combat-combos-synergies.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 02:48) |

**Key points:** категории синергий (ability, team, equipment, implant, timing); описанные примеры (Solo Combo #1-6, Team Combos, Netrunner/Techie/Support связки); бонусы, требования, тайминги, difficulty,视觉 эффекты; зависимость от имплантов/экипировки/классов.  
**Related docs:** `combat-abilities.md`, `combat-session-backend.md`, `combat-implants-types.md`, `combat-shooting.md`, `combat-freerun.md`, `economy/equipment-matrix.md`, `progression/classes-overview.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay.combat  
- **API directory:** `api/v1/gameplay/combat/combos/combos-synergies.yaml`  
- **base-path:** `/api/v1/gameplay/combat/combos`  
- **Java package:** `com.necpgame.gameplay.combat.combos`  
- **Frontend module:** `modules/combat/combos`  
- **Shared clients:** `@api/gameplay/combat`, `@shared/state/useCombosStore`, `@shared/ui/ComboPlanner`

> Все значения сверяй с таблицей микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для каталога комбо, фильтрации по типам/ролям/уровню сложности, получения детальных эффектов и требований.
2. Смоделировать схемы: `CombatCombo`, `ComboStep`, `ComboBonus`, `ComboRequirement`, `ComboTimingWindow`, `TeamSynergy`, `ComboPreset`.
3. Добавить endpoint для симуляции комбо (`POST /simulate`) — расчёт итоговых бонусов, времени, потребления ресурсов и проверка условий.
4. Описать управление пресетами игрока (`GET/POST/DELETE /presets`) с привязкой к loadout/abilities.
5. Документировать синергии с имплантами/экипировкой (references на каталоги), включая проверку конфликтов.
6. Учесть командные комбинации: входная модель участников, их роли и способности (до 4 игроков).
7. Зафиксировать интеграции: combat session (активация), abilities сервис, implants каталог, telemetry (анализ эффективности), social-service (гильдейские бонусы).
8. Включить Kafka события `combat.combos.executed`, `combat.combos.preset.saved`, `combat.combos.balance.flagged` с payload и consumers.
9. Описать безопасность: `bearerAuth`, пермишены `combat:combos:*`, rate limit для симуляций, античит подпись.
10. Прописать метрики (`combat_combo_success_rate`, `combat_combo_execution_time_ms`, `combat_combo_team_sync_score`), аудит и наблюдаемость.

## Endpoints
- `GET /catalog` — список комбо/синергий с фильтрами (тип, класс, роль, сложность, наличие имплантов).
- `GET /catalog/{comboId}` — детальная информация о комбо: шаги, бонусы, требования, тайминги, визуальные эффекты.
- `POST /simulate` — расчёт исхода комбо для конкретной конфигурации (abilities, экипировка, импланты, участники).
- `GET /team-synergies` — каталог командных синергий, роли участников, окна синхронизации.
- `GET /presets` — сохранённые пресеты комбо игрока.
- `POST /presets` — создание/обновление пресета (`ComboPreset`).
- `DELETE /presets/{presetId}` — удаление пресета.
- `POST /analytics/refresh` — запуск пересчёта телеметрии, балансовых коэффициентов и рекомендаций.

## Data Models
- `CombatCombo` — id, name, type, difficulty, description, role, tags, baseCooldown.
- `ComboStep` — order, abilityId, actionType, timingWindow, resourceCost.
- `ComboBonus` — bonusType, value, duration, stackingRules, conditions.
- `ComboRequirement` — classRestrictions, abilityRank, gearRequirements, implantsNeeded, reputation/faction gates.
- `ComboTimingWindow` — startMs, endMs, successModifier, failurePenalty.
- `TeamSynergy` — participants array (role, abilityId), coordinationWindow, combinedBonus.
- `ComboPreset` — presetId, ownerId, comboId, loadoutRefs, macros, lastUsedAt.
- Общие компоненты: `StandardError`, `ValidationError`, `Pagination`, `bearerAuth` из shared каталогов.

## Integrations & Events
- REST зависимости:  
  - `abilities-service` (внутренний каталог способностей и рангов)  
  - `implants-service` (из каталога API-TASK-401)  
  - `equipment-service` (`equipment-matrix`)  
  - `telemetry-service` (`POST /internal/telemetry/combos`)  
  - `anti-cheat-service` (верификация макросов)  
- Kafka topics: `gameplay.combat.combos.executed` (консьюмеры: telemetry, balance, anti-cheat), `gameplay.combat.combos.preset.saved` (консьюмеры: social achievements, notification), `gameplay.combat.combos.balance.flagged` (консьюмеры: balance-team).  
- WebSocket (`x-streams`): `/ws/gameplay/combat/combos/{sessionId}` — live обновления о статусе комбо во время боя (для UI combat HUD).

## Acceptance Criteria
1. Файл `api/v1/gameplay/combat/combos/combos-synergies.yaml` (≤ 500 строк) создан и проходит валидацию OpenAPI 3.0.3.
2. `info.x-microservice` описывает gameplay-service, порт 8083, base-path `/api/v1/gameplay/combat/combos`.
3. Все перечисленные endpoints задокументированы: параметры, запросы, ответы, коды ошибок, примеры, ссылки на общие компоненты.
4. Схемы `CombatCombo`, `ComboStep`, `ComboBonus`, `ComboRequirement`, `ComboTimingWindow`, `TeamSynergy`, `ComboPreset` определены с обязательными полями и примерами.
5. Учтены типы синергий (solo, team, equipment, implant, timing) и возможности фильтрации.
6. Kafka события и WebSocket канал описаны в `x-events` и `x-streams` с payload и метаданными.
7. Безопасность: `bearerAuth`, `X-AntiCheat-Signature`, rate limit на симуляции, аудит изменений.
8. Метрики, трассировки и логи указаны в `x-observability`.
9. Обновлены `tasks/config/brain-mapping.yaml`, `tasks/queues/queued.md`, `.BRAIN` документ содержит актуальный блок статуса.
10. Команда прошла `validate-swagger.ps1`, все ссылки на общие компоненты корректны, зависимости задокументированы в `x-integrations`.

## FAQ / Notes
- Комбо могут иметь макросы; предусмотреть поле `macroSupport` и интеграцию с античит (dry-run проверки).
+- Поддержать разные уровни сложности (easy/medium/hard/expert) и шкалу `skillCeiling`.
- Тайминговые окна критичны: описывать в миллисекундах, предусмотреть `latencyTolerance` для сетевой игры.
- Для командных синергий добавить флаг `requiresVoiceCoordination` (используется UI подсказками).
- Комбо влияет на progression (`combo mastery`); предусмотреть `x-progression` с рекомендациями по EXP наградам.

