# Task ID: API-TASK-385
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 22:10  
**Автор:** API Task Creator Agent  
**Зависимости:** API-TASK-300 (living world hybrid API), API-TASK-319 (player-orders world impact API), API-TASK-382 (combat ballistics API), API-TASK-383 (social orders lifecycle API)

---

## Summary

Подготовить спецификацию `world-impact.yaml` для `world-service`, описывающую пересчёт влияния заказов/боёв на города и индексы беспорядков. Контракт должен реализовать REST `/world/impact/simulate`, `/world/unrest/{district}` и связанный поток событий `world.unrest.updates`, обеспечивая требования `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-08-gameplay-backend-sync.md`.

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
- Требуется симуляция влияния заказов/боёв (`WorldImpactRequest`) с выходом `WorldImpactResult` и рекомендациями.
- Необходимо предоставлять показатели беспорядков (`/world/unrest/{district}`) и историю изменений.
- Событие `world.unrest.updates` должно транслировать изменения для UI и аналитики.
- Должны учитываться входные данные из combat ballistics, social orders, economy crafting.
- Нужны ограничения на частоту вызовов, поддержка dry-run (`preview=true`) и фиксация traceId.

**Related docs:**
- `.BRAIN/02-gameplay/world/world-state/living-world-kenshi-hybrid.md`
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md`
- `.BRAIN/02-gameplay/economy/economy-world-impact.md`
- `.BRAIN/05-technical/backend/analytics/telemetry-world.md`

---

## Target Architecture

- **Microservice:** world-service  
- **Port:** 8086  
- **Domain:** world/impact  
- **API directory:** `api/v1/world/impact/world-impact.yaml`  
- **base-path:** `/api/v1/world/impact`  
- **Java package:** `com.necpgame.worldservice.impact`  
- **Frontend module:** `modules/world/state/impact` (store: `useWorldStore`)  
- **UI components:** `ImpactSimulator`, `UnrestHeatmap`, `ImpactTimeline`, `EffectBreakdownPanel`  
- **Shared libs:** `@shared/ui/MapView`, `@shared/ui/TrendChart`, `@shared/forms/ImpactScenarioForm`

---

## Scope of Work

1. Собрать входные параметры симуляции (источник, orderId, combatId, deltaUnrest, modifiers) и выходные показатели.
2. Определить структуру OpenAPI файла, включая security, servers, tags, callbacks.
3. Описать REST endpoints для симуляции, получения показателей беспорядков, истории и рекомендаций.
4. Сформировать схемы данных (WorldImpactRequest/Result, UnrestSummary, ImpactEffect, Recommendation, TrendPoint).
5. Зафиксировать асинхронное событие `world.unrest.updates` и подписчиков.
6. Указать зависимости на combat/social/economy сервисы, shared components, rate limits.
7. Провести валидацию и обновить трекеры (`brain-mapping`, документ .BRAIN).

---

## Endpoints

- **POST `/impact/simulate`** — рассчитывает влияние события; параметры: sourceType (order/combat/crafting), sourceId, districtId, deltaUnrest, effects[], modifiers (weather, factionControl, playerPresence), preview flag; возвращает `WorldImpactResult`.
- **POST `/impact/apply`** — сохраняет результат симуляции (если `preview=false`), обновляет мировые флаги, публикует события.
- **GET `/impact/orders/{orderId}`** — агрегированное влияние заказа (используется social orders).
- **GET `/impact/combat/{combatId}`** — возвращает влияние конкретного боя, в том числе ricochet/curved shot параметры.
- **GET `/unrest/{districtId}`** — текущие показатели беспорядков: уровень, modifiers, активные события, прогноз.
- **GET `/unrest/{districtId}/history`** — временной ряд (последние 72 часа/30 событий) с трендами, причинами, связанными orderId/combatId.
- **GET `/impact/recommendations`** — список рекомендаций (уменьшить unrest, расширить патрули), фильтры по фракции.

Все endpoints используют `BearerAuth`, заголовок `X-Operator-Id` или `X-Player-Id`, rate limit 60 r/min на simulate/apply, ответы через `shared/common/responses.yaml`, поддерживают `traceId`, `preview` режимы.

---

## Data Models

- `WorldImpactRequest` — sourceType, sourceId, districtId, deltaUnrest, reputationDelta, economyDelta, orderPriority, combatMetrics, modifiers (weather, crowdDensity, factionControl, worldFlags[]), preview.
- `WorldImpactResult` — impactId, districtId, newUnrestLevel, projectedTrend, effects[], worldFlagsUpdated[], recommendations[], analyticsTraceId.
- `ImpactEffect` — effectType (economy, security, social, narrative), magnitude, duration, affectedEntities[], followUpActions[].
- `Recommendation` — actionType, description, targetService, urgencyScore, requiredResources[].
- `UnrestSummary` — districtId, currentLevel, safeThreshold, riskLevel, activeEvents[], lastUpdated, influenceSources[].
- `UnrestHistoryEntry` — timestamp, delta, sourceType, sourceId, actorId, notes, worldFlagsSnapshot.
- `TrendPoint` — timestamp, predictedLevel, confidenceInterval.
- `ImpactFilter` — используется для запросов истории/рекомендаций (enum values).
- Enumerations: `ImpactSourceType`, `ImpactEffectType`, `RiskLevel`, `RecommendationAction`.

---

## Integrations & Events

- **Social-service:** получать заказы (`GET /social/orders/{orderId}`), обновлять статус через `POST /social/orders/{orderId}/status` (API-TASK-383).
- **Economy-service:** учитывать `economy.crafting.jobs` (API-TASK-384) — materialsConsumed влияет на `economyDelta`.
- **Gameplay-service:** использовать метрики `combat.ballistics.events` (API-TASK-382) для расчёта боевого влияния.
- **Analytics-service:** REST hook `/analytics/world/impact` для сохранения результатов, callback `analytics.impact.feedback`.
- **Kafka:**  
  - `world.unrest.updates` — payload `{ impactId, districtId, newLevel, delta, sourceType, sourceId, recommendations[], timestamp }`.  
  - `world.impact.applied` — (optional) подтверждение применения симуляции.
- **Security:** администраторские операции (apply) требуют роль `world.operator`; предусмотреть `403` при отсутствии прав.

---

## Acceptance Criteria

1. Файл `api/v1/world/impact/world-impact.yaml` создан (≤ 400 строк) и включает корректный `info.x-microservice`.
2. Описаны все endpoints (simulate, apply, unrest metrics, history, recommendations) с параметрами, примерами и кодами ошибок.
3. Схемы данных охватывают запросы/ответы, эффекты, рекомендации, историю и тренды; поля приведены к PascalCase/ camelCase.
4. Подключены общие ответы/безопасность; указан rate limit и заголовки идентификации.
5. Kafka событие `world.unrest.updates` документировано с обязательными полями и примером payload.
6. Прописаны зависимости на связанные спецификации (combat, social orders, economy crafting, living world).
7. `validate-swagger.ps1 -ApiSpec api/v1/world/impact/world-impact.yaml` выполняется без ошибок.
8. Обновлены `brain-mapping.yaml`, `.BRAIN/06-tasks/.../2025-11-08-gameplay-backend-sync.md`, readiness-трекер.
9. Acceptance checklist выполнен, результаты отражены в change log.
10. FAQ отвечает на вопросы по preview режиму, масштабированию и синхронизации с living world.
11. Примеры данных отражают фракционные изменения и рекомендации (например, «deploy relief convoy»).

---

## FAQ / Notes

- **Как работает режим превью?** Установите `preview=true`; система возвращает `WorldImpactResult` без записи в БД, добавьте поле `applied=false`. При `preview=false` вызывайте `/impact/apply`.
- **Можно ли объединять несколько источников?** Используйте `sourceBundleId` (optional) в запросе; спецификация должна описать массив `additionalSources[]`.
- **Как синхронизировать с living world арками?** Возвращайте `worldFlagsUpdated` и `storyArcTriggers[]`; living world API потребляет эти данные.
- **Что делать при конфликтах мировых флагов?** Возвращайте `409 Conflict` с `conflictingFlags[]` и рекомендацией повторной симуляции.
- **Нужно ли логировать малозначительные изменения?** При `abs(delta) < 2` событие отправляется с флагом `minor=true` (указать в payload), UI может фильтровать.

---

## Change Log

- 2025-11-08 22:10 — Задание создано (API Task Creator Agent)


