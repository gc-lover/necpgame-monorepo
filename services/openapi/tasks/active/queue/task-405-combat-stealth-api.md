# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-405  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 23:25  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-401 (combat implants), API-TASK-402 (combos & synergies), API-TASK-403 (roles), API-TASK-404 (abilities), API-TASK-382 (combat ballistics telemetry), API-TASK-113 (combat session backend)

## Summary
Подготовить OpenAPI спецификацию `api/v1/gameplay/combat/stealth/stealth.yaml`, которая описывает систему скрытности: уровни обнаружения, типы сенсоров, имплантные и экипировочные бонусы, взаимодействие с киберпространством, социальную инженерии и механики стелс-боёв. Спецификация должна предоставить gameplay-service API для расчёта скрытности, обновления состояний обнаружения, управления стелс-перками и наградами.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/combat/combat-stealth.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 02:49) |

**Key points:** комбинация стелс-элементов (укрытия, тени, маскировка, засадные удары, отвлечения, толпа, киберпространство); каналы обнаружения (визуальные, аудио, технологические, сетевые) и профили врагов; влияние имплантов, навыков, экипировки; система наград и ачивок за стелс; интеграция с хакерством, паркуром, боем.  
**Related docs:** `combat-implants-types.md`, `combat-hacking.md`, `combat-combos-synergies.md`, `combat-roles-detailed.md`, `economy/equipment-matrix.md`, `progression/classes-overview.md`, `combat-psycho/...`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay.combat  
- **API directory:** `api/v1/gameplay/combat/stealth/stealth.yaml`  
- **base-path:** `/api/v1/gameplay/combat/stealth`  
- **Java package:** `com.necpgame.gameplay.combat.stealth`  
- **Frontend module:** `modules/combat/stealth`  
- **Shared clients:** `@api/gameplay/combat`, `@shared/state/useStealthStore`, `@shared/ui/StealthHUD`

> Сверь все параметры с `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для:  
   - каталога стелс-способностей/перков;  
   - профилей обнаружения врагов и зон;  
   - управления стелс-состояниями (уровень подозрения, тревога, тревоги сети);  
   - системы наград и ачивок за стелс.
2. Смоделировать схемы: `StealthProfile`, `DetectionChannel`, `EnemyDetectionProfile`, `StealthAbility`, `StealthLoadout`, `StealthRewardTier`, `StealthEvent`, `StealthNetworkNode`.
3. Добавить endpoint для симуляции стелс-сценариев (`POST /simulate`) — учитывает каналы обнаружения, импланты, способности, шум, освещённость, сетевые узлы.
4. Учесть хакерские интеграции: взлом камер/датчиков, отключение узлов, отражать во `x-integrations` зависимости от `combat-hacking`.
5. Описать связь с социальными механиками (маскировка, толпа) и progression (перки, навыки), включая требования и бонусы.
6. Предусмотреть управление личным стелс-лоадаутом (`GET/POST/DELETE /loadouts`) с проверкой энергобюджета имплантов, конфликтов и перегрева.
7. Зафиксировать события: `stealth.session.started`, `stealth.detected`, `stealth.alert.raised`, `stealth.reset`, `stealth.achievement.unlocked`.
8. Добавить аналитические и телеметрические endpoints (`POST /analytics/refresh`, `GET /analytics/stats`) для балансировки.
9. Описать безопасность: `bearerAuth`, `X-AntiCheat-Signature`, ограничения на симуляции, аудит изменений.
10. Прописать метрики и observability (`stealth_detection_rate`, `stealth_time_in_shadow_ms`, `stealth_alert_count_total`).

## Endpoints
- `GET /catalog` — список стелс-способностей, имплантов, перков, модификаторов.
- `GET /detection-profiles` — профили обнаружения (по типу врага/зоны), каналы и пороги.
- `GET /network/nodes` — карта узлов камер/датчиков (для интеграции с хакерством).
- `POST /simulate` — симуляция скрытности (вход: loadout, импланты, зона, враги, действия).
- `GET /loadouts` / `POST /loadouts` / `DELETE /loadouts/{id}` — управление стелс-конфигурациями.
- `POST /events/register` — регистрация события (убийство из засады, отвлечение, взлом).
- `GET /rewards/tiers` — уровни наград/ачивок за стелс-прохождения.
- `POST /analytics/refresh` / `GET /analytics/stats` — аналитика и балансировка.

## Data Models
- `StealthProfile` — id, description, detectionThresholds, noiseModifiers, lightSensitivity.
- `DetectionChannel` — type (visual/audio/tech/network), sensitivity, counterMeasures.
- `EnemyDetectionProfile` — enemyType, channels list, alertEscalationRules, resetTime.
- `StealthAbility` — source (implant/equipment/progression), energyCost, cooldown, effect, synergyTags.
- `StealthLoadout` — abilities, implants, equipment, energyBudget, overheatRisk.
- `StealthRewardTier` — tierId, requirements, rewards (XP, items, reputation modifiers).
- `StealthEvent` — eventType (kill, hack, distraction), detectionImpact, rewardImpact.
- `StealthNetworkNode` — nodeId, zoneId, controlLevel, hackDifficulty, linkedChannels.
- Общие компоненты: `StandardError`, `ValidationError`, `Pagination`, `bearerAuth`, `AntiCheatSignature`, `OperationResult`.

## Integrations & Events
- REST зависимости: `implants-service`, `abilities-service`, `combos-service`, `roles-service`, `hacking-service`, `equipment-service`, `progression-service`, `social-service` (маскировка), `telemetry-service`.
- Kafka topics: `gameplay.combat.stealth.detected`, `gameplay.combat.stealth.alert.raised`, `gameplay.combat.stealth.session.completed`, `gameplay.combat.stealth.achievement.unlocked`.
- WebSocket: `/ws/gameplay/combat/stealth/{sessionId}` — поток обновлений статуса обнаружения и подсказок (документировать в `x-streams`).

## Acceptance Criteria
1. Создан файл `api/v1/gameplay/combat/stealth/stealth.yaml` (≤ 500 строк), валидный по OpenAPI 3.0.3.
2. `info.x-microservice` указывает gameplay-service, порт 8083, base-path `/api/v1/gameplay/combat/stealth`.
3. Все endpoints документированы с параметрами, запросами, ответами, кодами ошибок, примерами и ссылками на общие компоненты.
4. Схемы `StealthProfile`, `DetectionChannel`, `EnemyDetectionProfile`, `StealthAbility`, `StealthLoadout`, `StealthRewardTier`, `StealthEvent`, `StealthNetworkNode` определены и снабжены примерами.
5. Учтены интеграции с имплантами, способностями, хакерством, ролями и progression (через `x-integrations`).
6. Kafka события и WebSocket канал задокументированы в `x-events` / `x-streams` с payload, producers, consumers, SLA.
7. Безопасность: `bearerAuth`, `X-AntiCheat-Signature`, rate limits, аудит trail.
8. Метрики и observability указаны в `x-observability`.
9. Обновлены `tasks/config/brain-mapping.yaml`, `tasks/queues/queued.md`, `.BRAIN` документ содержит актуальный статус.
10. `validate-swagger.ps1` проходит без ошибок; все ссылки на shared компоненты корректны; проверен чеклист.

## FAQ / Notes
- Предусмотреть механику «подозрения» (градусы от 0 до тревоги), хранить в `StealthProfile`.
- Описать взаимодействие с киберпсихозом (длительный стелс/маскировка может повышать стресс).
- Маскировка под NPC требует интеграции с social-service — указать в `x-integrations`.
- Добавить `stealthDifficulty` и `recommendedRoles` для UI подсказок.
- Учитывать влияние погоды/времени суток (связь с world-service) — можно описать как `environmentModifiers`.

