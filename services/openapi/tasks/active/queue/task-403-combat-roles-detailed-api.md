# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-403  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 23:05  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-382 (combat ballistics), API-TASK-401 (combat implants catalog), API-TASK-402 (combat combos & synergies), API-TASK-113 (combat session backend)

## Summary
Создать OpenAPI спецификацию `api/v1/gameplay/combat/roles/roles.yaml`, описывающую боевые роли (танк, DPS, саппорт, гибриды, специалистические подтипы) с атрибутами, обязательными имплантами, рекомендованными способностями, синергиями и рекомендациями по экипировке. Спецификация должна поддерживать сервис `gameplay-service` в управлении билд-конфигурациями и предоставлении данных фронтенду `modules/combat/roles`.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/combat/combat-roles-detailed.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 03:14) |

**Key points:** философия ролей (гибридность, специализация, адаптация, синергия); подробные профили Tank/DPS/Support/Hybrid/Commander; обязательные импланты и способности, тактики, синергии, контр-пики, рекомендации по экипировке; поддержка нескольких специализаций (Burst DPS, Sustained DPS, Crowd Control, Field Medic, etc.).  
**Related docs:** `combat-implants-types.md`, `combat-combos-synergies.md`, `combat-session-backend.md`, `combat-abilities.md`, `progression/classes-overview.md`, `economy/equipment-matrix.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay.combat  
- **API directory:** `api/v1/gameplay/combat/roles/roles.yaml`  
- **base-path:** `/api/v1/gameplay/combat/roles`  
- **Java package:** `com.necpgame.gameplay.combat.roles`  
- **Frontend module:** `modules/combat/roles`  
- **Shared clients:** `@api/gameplay/combat`, `@shared/state/useRolePlanner`, `@shared/ui/RoleDashboard`

> Все значения сверяй с таблицей микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Определить REST endpoints для получения каталога ролей, детальных профилей, рекомендованных билдов и актуальных метаданных (атрибуты, навыки, импланты, синергии).
2. Смоделировать схемы: `CombatRole`, `RoleAttributeProfile`, `RoleAbilityLoadout`, `RoleImplantSet`, `RoleEquipmentRecommendation`, `RoleSynergy`, `RoleCounter`.
3. Добавить endpoint для генерации персональных рекомендаций (`POST /recommendations`) на основе текущих характеристик игрока (abilities, implants, preferred playstyle).
4. Описать управление пользовательскими пресетами ролей (`GET/POST/DELETE /presets`) с привязкой к loadouts и progression.
5. Включить эндпоинт для сравнения ролей (`POST /compare`) — возвращает различия по статам, синергиям и требованиям.
6. Зафиксировать интеграции с сервисами имплантов, способностей, экипировки, progression (классы, уровни) и telemetry (статистика win-rate/usage).
7. Документировать связь с combat combos: возможность запросить рекомендуемые комбинации для выбранной роли (`GET /{roleId}/combos`).
8. Описать Kafka события (`combat.roles.preset.saved`, `combat.roles.meta.updated`) и обновление аналитики (`POST /analytics/refresh`).
9. Указать требования безопасности (JWT, пермишены `combat:roles:*`, античит токен для пресетов).
10. Прописать метрики (`combat_role_usage_rate`, `combat_role_winrate`, `combat_role_recommendation_latency_ms`), аудит и observability.

## Endpoints
- `GET /catalog` — список доступных ролей, фильтры по классу, стилю (tank/dps/support/hybrid), сложности освоения.
- `GET /catalog/{roleId}` — подробный профиль роли: атрибуты, импланты, способности, синергии, экипировка, тактики, контр-пики.
- `GET /catalog/{roleId}/combos` — рекомендованные комбо и синергии (ссылки на API-TASK-402).
- `POST /recommendations` — генерация персонального набора ролей и билдов на основе входных характеристик (abilities, implants, desired role).
- `GET /presets` — пользовательские пресеты ролей.
- `POST /presets` — создание/обновление пресета (`RolePreset`).
- `DELETE /presets/{presetId}` — удаление пресета.
- `POST /compare` — сравнение нескольких ролей (до 3) по ключевым параметрам.
- `POST /analytics/refresh` — пересчёт аналитики и статистики использования.

## Data Models
- `CombatRole` — id, name, description, roleType, difficulty, specialization, primaryAttributes, secondaryAttributes, recommendedWeapons.
- `RoleAttributeProfile` — attributeName, baseValueRange, emphasis, synergyNotes.
- `RoleAbilityLoadout` — abilities array (id, cooldown, effects), passives, ultimates.
- `RoleImplantSet` — requiredImplants, optionalImplants, synergyScore.
- `RoleEquipmentRecommendation` — weaponTypes, armorSets, mods, rationale.
- `RoleSynergy` — synergyType (tank+dps, support+team), description, bonusEffects, recommendedCombos.
- `RoleCounter` — strongAgainst, weakAgainst, counterTips.
- `RolePreset` — presetId, ownerId, roleId, abilityConfig, implantConfig, equipmentConfig, createdAt, updatedAt.
- Общие компоненты: `StandardError`, `ValidationError`, `Pagination`, `bearerAuth`, `AntiCheatSignature`.

## Integrations & Events
- REST зависимости:  
  - `abilities-service` (каталог способностей и рангов)  
  - `implants-service` (API-TASK-401)  
  - `equipment-service` (`equipment-matrix`)  
  - `combos-service` (API-TASK-402)  
  - `progression-service` (классы, уровни, unlock conditions)  
  - `telemetry-service` (статистика эффективности)  
- Kafka topics: `gameplay.combat.roles.preset.saved`, `gameplay.combat.roles.meta.updated`, `gameplay.combat.roles.analytics.refreshed`.  
- WebSocket (опциональный `x-streams`): `/ws/gameplay/combat/roles/{playerId}` — live обновления рекомендаций при изменении билдов.

## Acceptance Criteria
1. Создан файл `api/v1/gameplay/combat/roles/roles.yaml` (≤ 500 строк) и проходит валидацию OpenAPI 3.0.3.
2. `info.x-microservice` указывает gameplay-service, порт 8083, base-path `/api/v1/gameplay/combat/roles`.
3. Описаны все указанные endpoints с параметрами, запросами, ответами, кодами ошибок, примерами, ссылками на общие компоненты.
4. Схемы `CombatRole`, `RoleAttributeProfile`, `RoleAbilityLoadout`, `RoleImplantSet`, `RoleEquipmentRecommendation`, `RoleSynergy`, `RoleCounter`, `RolePreset` определены с required полями и примерами.
5. Учтены зависимости от имплантов, способностей, комбов, progression; добавлены ссылки на соответствующие API (через `x-integrations`).
6. Kafka события и WebSocket канал описаны в `x-events` / `x-streams` с payload, producers/consumers, SLA.
7. Безопасность: `bearerAuth`, `X-AntiCheat-Signature`, rate limit на рекомендательные эндпоинты, аудит.
8. Метрики (`combat_role_usage_rate`, `combat_role_winrate`, `combat_role_build_conflicts_total`) и наблюдаемость описаны в `x-observability`.
9. Обновлены `tasks/config/brain-mapping.yaml`, `tasks/queues/queued.md`, `.BRAIN` документ содержит актуальный статус.
10. `validate-swagger.ps1` проходит без ошибок, все ссылки на shared компоненты корректны, документация соответствует чеклисту.

## FAQ / Notes
- Роли должны поддерживать гибридные билды: предусмотреть поле `hybridOptions` с описанием альтернативных конфигураций.
- Важен `apmDifficulty` и `coordinationNeed` — использовать для UI подсказок.
- Для командных бонусов указать синергию с конкретными ролями (Tank + Burst DPS, Support + Commander).
- Рекомендации по контр-пикам нужны для матчмейкинга и обучения игроков (описать в `RoleCounter`).
- Добавить `x-progression` с требованиями к уровням/квестам для разблокировки ролей и их улучшений.

