# Task ID: API-TASK-382
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 21:55  
**Автор:** API Task Creator Agent  
**Зависимости:** API-TASK-319 (player-orders world impact API), API-TASK-333 (economy visuals items API), API-TASK-360 (combat AI enemies API)

---

## Summary

Подготовить OpenAPI спецификацию `combat-ballistics.yaml` для `gameplay-service`, покрывающую симуляцию траекторий, управление модами оружия и мониторинг активных боевых навыков (Curved Shot, Smart Ricochet). Контракт должен предоставить данные для фронтенда, боевой телеметрии и крафтовых очередей, описанных в `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md`.

---

## Source Documents

| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md` |
| Version | v1.0.0 |
| Status | ready |
| API readiness | ready (2025-11-08) |

**Key points:**
- Требуется REST интерфейс для `POST /combat/ballistics/simulate` с расчётом вероятности попадания, урона, рикошетов и статусов.
- Необходимо управление модульными слотами оружия (`PATCH /combat/weapon-mods/{weaponId}`) с валидацией экономики и имплантов.
- UI должен получать состояние активных навыков (`GET /combat/skills/active`) и запускать Curved Shot (`POST /combat/abilities/curved-shot/activate`).
- События `combat.ballistics.events`, `combat.abilities.curved`, `player.skills.cooldowns` должны быть описаны в Async секции.
- Безопасность: `BearerAuth`, заголовок `X-Player-Id`, rate limit 180 r/min на симуляцию.

**Related docs:**
- `.BRAIN/02-gameplay/combat/combat-shooting-advanced.md` (v1.0.0, approved)
- `.BRAIN/02-gameplay/economy/economy-crafting.md`
- `.BRAIN/05-technical/backend/telemetry/combat-telemetry.md`
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` (для связки с крафтом и заказами)

---

## Target Architecture

- **Microservice:** gameplay-service  
- **Port:** 8083  
- **Domain:** gameplay/combat  
- **API directory:** `api/v1/gameplay/combat/combat-ballistics.yaml`  
- **base-path:** `/api/v1/gameplay/combat`  
- **Java package:** `com.necpgame.gameplayservice.combat.ballistics`  
- **Frontend module:** `modules/combat/ballistics` (store: `useCombatStore`)  
- **UI components:** `BallisticsSimulator`, `WeaponCard`, `SkillStatusPanel`, `RicochetPathViewer`, `CurvedShotControl`  
- **Shared libs:** `@shared/ui/MetricCard`, `@shared/forms/ToggleGroup`, `@shared/hooks/useRateLimiter`

---

## Scope of Work

1. Проанализировать исходные документы и выписать сущности: WeaponProfile, BallisticsSimulation, WeaponModSlot, ActiveSkillState.
2. Сформировать структуру OpenAPI файла: info, servers, security, tags, paths, components.
3. Описать REST endpoints (simulate, weapon mods, active skills, Curved Shot) с примерами запросов/ответов и кодами ошибок.
4. Определить схемы данных: запросы, ответы, перечисления модов, параметры урона и рикошетов.
5. Задокументировать асинхронные события и связи с economy-service/social-service.
6. Подключить общие компоненты (`shared/common/responses.yaml`, `shared/common/security.yaml`, `shared/common/pagination.yaml` при необходимости).
7. Прогнать `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiSpec api/v1/gameplay/combat/combat-ballistics.yaml`.
8. Обновить `tasks/config/brain-mapping.yaml`, `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md`, readiness-трекер.

---

## Endpoints

- **POST `/combat/ballistics/simulate`** — рассчитывает вероятность попадания, ожидаемый урон, траекторию Curved Shot, список статусов; валидация комбинаций модов, имплантов, навыков.
- **GET `/combat/ballistics/profiles/{weaponId}`** — возвращает `WeaponProfileDTO` (дистанции, падение урона, допустимые моды, поддержка Curved Shot).
- **PATCH `/combat/weapon-mods/{weaponId}`** — обновляет набор модов; проверяет совместимость, лимиты слотов, блокировки через economy-service; возвращает `WeaponModState`.
- **GET `/combat/weapon-mods/{weaponId}`** — текущая конфигурация модов, привязка к crafting jobs, статус обслуживания.
- **POST `/combat/abilities/curved-shot/activate`** — триггер навыка; валидирует кулдауны, энергозатраты, возвращает `CurvedShotActivationResult` со временем восстановления.
- **GET `/combat/skills/active`** — список активных боевых навыков игрока, кулдауны, модификаторы от имплантов и заказов; поддерживает фильтры по типу навыка.
- **GET `/combat/ballistics/telemetry/{combatId}`** — агрегированная статистика по бою (используется telemetry-service); поддерживает пагинацию и фильтры по режиму (PvE/PvP).

Каждый endpoint должен ссылаться на стандартные ответы (400/401/403/404/409/422/500) через `shared/common/responses.yaml`, использовать `BearerAuth` и проверочный заголовок `X-Player-Id`.

---

## Data Models

- `BallisticsSimulationRequest` — weaponId, ammoType, distanceMeters, angleDegrees, flags, attackerImplants, targetArmor, environmentMaterial, weather, trajectoryOverrides.
- `BallisticsSimulationResult` — hitProbability, expectedDamage, ricochetPath (array `RicochetStep`), statusEffects, cooldown, suggestedMods, warnings.
- `WeaponProfileDTO` — baseDamage, optimalDistance, falloffRate, supportedAmmo, supportedMods, curvedShotSupport, ricochetLimit.
- `WeaponModSlot` / `WeaponModState` — slotType, modId, rarity, synergyBonus, stabilityPenalty, craftingJobId.
- `WeaponModUpdateRequest` — replace strategy (merge/overwrite), slots[], validationFlags, requestedByOrderId.
- `ActiveSkillState` — skillId, name, type (passive/active/channel), cooldownEndsAt, fatigueCost, linkedImplants[], linkedOrders[].
- `CurvedShotActivationRequest` / `CurvedShotActivationResult` — targetVector, bendAngle, energyCost, cooldown, telemetryTraceId.
- `TelemetrySummary` — combatId, modes[], totalShots, curvedShotUsage, ricochetUsage, averageDamage.
- Enumerations: `DamageMode`, `RicochetSurface`, `SkillCategory`, `ModRarity`.

Схемы должны использовать PascalCase, свойства — camelCase; обязательные поля перечислить через `required`.

---

## Integrations & Events

- **REST зависимости:** economy-service (`POST /economy/crafting/weapon-jobs`), social-service (`GET /social/orders/{orderId}` для авторизации), analytics-service (`POST /analytics/combat/ballistics` — указать как внешнюю зависимость).
- **Kafka:**  
  - `combat.ballistics.events` — публикация траекторий (payload из `BallisticsSimulationResult` + combatId, orderId).  
  - `combat.abilities.curved` — событие активации Curved Shot, содержит playerId, weaponId, bendAngle, cooldown.  
  - `player.skills.cooldowns` — обновление кулдаунов после активации способностей (подписываются social-service, UI gateway).
- **Telemetry hooks:** integration с `telemetry-service` для сохранения `TelemetrySummary`.
- **Security & rate limiting:** документировать лимит 180 r/min, заголовок `X-Rate-Limit-Remaining`.

---

## Acceptance Criteria

1. Файл `api/v1/gameplay/combat/combat-ballistics.yaml` создан (≤ 400 строк) с корректным `info.x-microservice`.
2. Все перечисленные endpoints описаны, содержат параметры, валидации, примеры и ссылки на общие ответы.
3. `BearerAuth` и заголовок `X-Player-Id` задокументированы во всех путях.
4. Data models включают все сущности из исходных документов и используют единый стиль именования.
5. Async события `combat.ballistics.events`, `combat.abilities.curved`, `player.skills.cooldowns` задокументированы с payload.
6. Примеры используют данные из `.BRAIN/02-gameplay/combat/combat-shooting-advanced.md` (ricochetPath, curvedShot).
7. Добавлены зависимости на economy-service/social-service в разделе Integrations.
8. Валидатор `validate-swagger.ps1` проходит без ошибок и предупреждений.
9. `tasks/config/brain-mapping.yaml` и `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md` обновлены актуальными данными.
10. Acceptance checklist (`tasks/config/checklist.md`) пройден, результаты отражены в change log.
11. Rate limit и безопасность описаны в спецификации.

---

## FAQ / Notes

- **Что делать, если комбинация модов конфликтует?** Указать `422 UnprocessableEntity` с массивом конфликтующих слотов и рекомендациями заменить моды; использовать ссылку на `WeaponModConflict`.
- **Нужно ли описывать материал окружения?** Да, `environmentMaterial` — enum (metal, concrete, glass, organic, shield), используется для расчёта рикошетов.
- **Как связать симуляцию с заказами игроков?** Поле `linkedOrderId` (UUID) в запросе позволяет привязать симуляцию к social orders; оно опционально, но при заполнении логируется в `combat.ballistics.events`.
- **Поддерживаются ли оффлайн симуляции?** Для batch-режима описать query-параметр `mode=batch`, который снижает лимит до 30 r/min и отключает реального игрока.
- **Где хранить рекомендации по модам?** Возвращаются в `suggestedMods` (array of IDs), UI использует `modules/combat/ballistics` для подсветки.

---

## Change Log

- 2025-11-08 21:55 — Задание создано (API Task Creator Agent)


