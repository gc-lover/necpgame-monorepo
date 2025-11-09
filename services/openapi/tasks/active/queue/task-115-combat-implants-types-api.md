# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-115  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 22:30  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-113 (combat session telemetry), API-TASK-382 (combat ballistics data), API-TASK-106 (economy crafting core)

## Summary
Подготовить OpenAPI спецификацию `api/v1/gameplay/combat/implants/implants-types.yaml`, охватывающую каталог боевых имплантов (боевые, тактические, защитные, двигательные, OS), их эффекты, требования, бренды, синергии и конфигурации билдов. Спецификация должна обеспечивать сервису `gameplay-service` управление имплантами, расчёт бонусов, интеграцию с крафтом/экономикой и выдачу данных фронтенду `modules/combat/implants`.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/combat/combat-implants-types.md` |
| Version | v1.1.0 |
| Status | approved |
| API readiness | ready (2025-11-09 01:28) |

**Key points:** обязательный и расширяемый набор имплантов; влияние на статы и способности; бренды и сет-бонусы; синергии с экипировкой, свободой передвижения, хакерством; требования по репутации и классам; взаимодействие с экономикой (торговля, крафт, обслуживание).  
**Related docs:** `combat-abilities.md`, `combat-shooting.md`, `combat-freerun.md`, `combat-stealth.md`, `combat-hacking-types.md`, `economy-crafting.md`, `equipment-matrix.md`, `progression/classes-overview.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay.combat  
- **API directory:** `api/v1/gameplay/combat/implants/implants-types.yaml`  
- **base-path:** `/api/v1/gameplay/combat/implants`  
- **Java package:** `com.necpgame.gameplay.combat.implants`  
- **Frontend module:** `modules/combat/implants`  
- **Shared clients:** `@api/gameplay/combat/implants`, `@shared/state/useImplantsStore`, `@shared/forms/ImplantLoadoutForm`

> Все параметры сверяй с матрицей микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для каталогов имплантов, фильтров по брендам/классам, расчёта бонусов и управления сетами.
2. Смоделировать схемы: `CombatImplant`, `ImplantEffect`, `ImplantBrand`, `ImplantSetBonus`, `ImplantRequirement`, `ImplantSynergy`, `ImplantLoadout`.
3. Задокументировать механику обязательных слотов (боевые, тактические, защитные, двигательные, OS) и их лимиты.
4. Добавить endpoint для предварительного расчёта билдов (`POST /loadouts/preview`) с агрегацией статов, синергий и конфликтов.
5. Определить обновление телеметрии/баланса (`POST /analytics/refresh`) с асинхронным подтверждением.
6. Задекларировать интеграции: economy-service (торговля, обслуживание, крафт), progression-service (классовые ограничения), social-service (репутация и доступ), world-service (фракционные лицензии).
7. Прописать Kafka события (`combat.implants.loadout.created`, `combat.implants.modified`, `combat.implants.brand-unlocked`) и очереди на баланс-проверку.
8. Учесть безопасность: JWT, разрешения `combat:implants:*`, античит сигнатуры для подтверждения установок, rate limiting.
9. Описать метрики (`combat_implant_install_time_ms`, `combat_implant_loadout_synergy_score_avg`), аудит и трассировки.
10. Обновить `brain-mapping.yaml`, `tasks/queues/queued.md`, убедиться что спецификация пройдёт `validate-swagger.ps1`.

## Endpoints
- `GET /catalog` — список имплантов с фильтрами по типу, бренду, редкости, требованиям.
- `GET /catalog/{implantId}` — подробное описание импланта, эффекты, слот, синергии, требования обслуживания.
- `GET /brands` — бренды и их линейки (сет-бонусы, совместимость).
- `GET /synergies` — список доступных синергий (имплант + оружие/способность).
- `POST /loadouts/preview` — расчёт итоговых статов и конфликтов выбранного набора имплантов.
- `POST /loadouts/apply` — закрепление билдов (валидация требований, публикация событий).
- `POST /analytics/refresh` — запуск пересчёта метрик использования имплантов (асинхронный job).

## Data Models
- `CombatImplant` — id, name, type, slot, brand, rarity, baseEffects, cooldowns, maintenanceCost.
- `ImplantEffect` — stat, modifierType, value, duration, conditions.
- `ImplantRequirement` — classRestrictions, reputation, factionLicense, level, questFlags.
- `ImplantBrand` — id, name, manufacturer, description, compatibleTypes, setBonuses.
- `ImplantSynergy` — sourceImplantId, targetType (weapon/ability/movement), bonusDescription, requirements.
- `ImplantLoadout` — slots map, synergyScore, conflicts, totalCost, recommendedCombos.
- Общие компоненты: `StandardError` из `responses.yaml`, пагинация, security схемы (`bearerAuth`, `antiCheatToken`), `OperationResult`.

## Integrations & Events
- REST зависимости:  
  - `economy-service` (`GET /internal/equipment/pricing`, `POST /internal/maintenance/schedule`)  
  - `progression-service` (`GET /internal/classes/{id}/implant-access`)  
  - `social-service` (`GET /internal/reputation/licenses`)  
  - `world-service` (`POST /internal/factions/licenses/check`)  
- Kafka: `gameplay.combat.implants.loadout.created`, `gameplay.combat.implants.loadout.applied`, `gameplay.combat.implants.brand.unlocked`, `gameplay.combat.implants.maintenance.required`. Указать payload, producer (gameplay-service), consumers (economy-service, telemetry-service, anti-cheat-service, notification-service).
- WebSocket (опционально, через `x-streams`): `/ws/gameplay/combat/implants/{playerId}` для live-обновлений эффектов (качество-of-life для билд редактора).

## Acceptance Criteria
1. Файл `api/v1/gameplay/combat/implants/implants-types.yaml` (≤ 500 строк) создан и проходит `OpenAPI 3.0.3` валидацию.
2. `info.x-microservice` указывает gameplay-service, порт 8083, base-path `/api/v1/gameplay/combat/implants`.
3. Документированы все перечисленные endpoints с запросами/ответами, кодами ошибок, примерами и ссылками на общие компоненты.
4. Схемы `CombatImplant`, `ImplantEffect`, `ImplantRequirement`, `ImplantBrand`, `ImplantSynergy`, `ImplantLoadout` оформлены в `components.schemas` с обязательными полями.
5. Учтены обязательные слоты и ограничения (основной набор, расширяемый набор, OS).
6. Kafka события описаны в `x-events` с payload, producer/consumer, метриками надёжности.
7. Безопасность: схемы `bearerAuth`, заголовок `X-AntiCheat-Signature`, rate limiting и аудирование изменений.
8. Наблюдаемость: перечислены метрики, audit trail, OpenTelemetry spans.
9. Добавлены `x-integrations` с перечислением зависимых сервисов и внутренних контрактов.
10. Обновлены `brain-mapping.yaml`, `tasks/queues/queued.md`, `.BRAIN` документ содержит актуальный `API Tasks Status`, `validate-swagger.ps1` проходит без ошибок.

## FAQ / Notes
- Импланты могут конфликтовать (например, два OS в одном слоте) — предусмотреть список конфликтов в ответах.
- Требуется учёт обслуживания: поле `maintenanceIntervalHours`, интеграция с economy-service для списаний.
- Сет-бонусы брендов (Arasaka, Militech, etc.) описывать в `ImplantBrand.setBonuses` с условиями.
- Поддержать тэги `combat`, `hacking`, `mobility`, `support` для фильтрации в UI.
- В `POST /loadouts/apply` предусмотреть dry-run режим (`?dryRun=true`) для фронтенда.

