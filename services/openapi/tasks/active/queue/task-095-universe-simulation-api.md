# Metadata
- **Task ID:** API-TASK-095  
- **Type:** API Generation  
- **Priority:** critical  
- **Status:** queued  
- **Created:** 2025-11-09 15:10  
- **Author:** API Task Creator Agent  
- **Dependencies:** none  

## Summary
Создать OpenAPI-спецификацию набора `lore/universe` для управления временной шкалой лиг и механизмом раскрытия симуляции, чтобы гейм-дизайнеры, аналитика и внутриигровые системы могли получать детализированные данные и синхронизировать события мира между сервисами.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/03-lore/universe.md` |
| Version | v1.1.0 |
| Status | review |
| API readiness | ready (2025-11-03 20:35) |

**Key points:**
- Лиги длятся 3–6 месяцев и соответствуют 70 игровым годам (2020–2090) с формулами пересчёта времени.
- Каждая лига делится на пять этапов (начало, ранняя, средняя, поздняя, финал) с различным технологическим уровнем.
- Раскрытие симуляции проходит по пяти стадиям, влияющим на контент и доступность сведений о “чёрном заслоне”.
- События времени определяют появление технологий, глобальные события и зависят от прогресса игроков.
- Документ связывается с каталогами фракций, локаций и персонажей для построения повествовательной связности.

**Related docs:**
- `.BRAIN/03-lore/factions/factions-overview.md`
- `.BRAIN/03-lore/locations/locations-overview.md`
- `.BRAIN/03-lore/characters/characters-overview.md`
- `.BRAIN/04-narrative/quest-system.md`

## Target Architecture (⚠️ Обязательно)
- **Microservice:** world-service  
- **Port:** 8086  
- **Domain:** lore  
- **API directory:** `api/v1/lore/universe/universe.yaml`  
- **base-path:** `/api/v1/lore/universe`  
- **Java package:** `com.necpgame.world`  
- **Frontend module:** `modules/world/universe`  
- **Shared UI/Form components:** `@shared/ui/Timeline`, `@shared/ui/EventCard`, `@shared/ui/StatBlock`, `@shared/forms/FiltersForm`, `@shared/layouts/GameLayout`

## Scope of Work
1. Проанализировать исходный документ и связанные материалы лора.
2. Спроектировать структуру каталогов `api/v1/lore/universe/` и при необходимости выделить отдельные файлы (≤ 400 строк каждый).
3. Определить набор REST endpoints для временной шкалы, этапов лиги и прогресса раскрытия симуляции.
4. Описать модели данных: временные фазы, события, параметры симуляции, состояния раскрытия.
5. Задать зависимости от общих компонентов (`responses.yaml`, `pagination.yaml`, `security.yaml`) и предусмотреть фильтры/пагинацию.
6. Зафиксировать интеграции с world-service (мировые события), narrative-service (квесты) и economy-service (доступность технологий).

## Endpoints
- `GET /timeline` — получить сводную временную шкалу текущей или указанной лиги; параметры: `league_id`, `phase`, пагинация; ответы 200/400/404.
- `GET /timeline/{eventId}` — детальный просмотр события времени, включая связанные технологии и narrative-триггеры.
- `GET /leagues` — список лиг с длительностью, коэффициентами времени, статусами; поддержать фильтр по периоду (2020–2090).
- `GET /leagues/{leagueId}/phases` — этапы конкретной лиги с описанием технологических уровней и разблокированного контента.
- `GET /simulation/stages` — текущая стадия раскрытия симуляции и ожидаемые триггеры перехода.
- `POST /simulation/stages/advance` — инициировать переход к следующей стадии (требует `admin` security); тело: подтверждение, список триггеров; ответы 202/409.
- `GET /simulation/anomalies` — список зафиксированных глитчей и аномалий, влияющих на лор и доступ к данным.
- `POST /simulation/anomalies` — зарегистрировать новое аномальное событие (админ, body содержит тип, локализацию, влияние).
- `GET /blackwall/insights` — агрегировать сведения «за чёрным заслоном», уровень доступа, связанные квесты.
- `GET /metrics/progression` — вернуть статистику раскрытия (скорость, процент вовлечённых игроков, прогноз следующего перехода).

## Data Models
- `League` (id, title, startYear, endYear, realDurationDays, accelerationFactor, resetType, status).
- `LeaguePhase` (id, leagueId, code, displayName, timeframe, technologyLevel, unlocks, narrativeHooks).
- `TimelineEvent` (id, leagueId, phaseCode, eventType, timestampGame, timestampReal, description, relatedEntities, impact).
- `SimulationStage` (id, order, name, description, visibilityLevel, triggers, unlockedSystems, riskLevel).
- `SimulationTrigger` (id, stageId, triggerType, conditionExpression, source, requiredProgress, autoAdvance).
- `Anomaly` (id, detectedAt, location, severity, description, mitigation, linkedEvents).
- `BlackwallInsight` (id, accessLevel, summary, discoveredBy, relatedQuestIds, unlocks).
- `ProgressionMetrics` (leagueId, currentStage, completionPercent, averageRevealRatePerDay, forecastNextStageDate).
- Общие `Error`/`ValidationError` refs из `api/v1/shared/common/responses.yaml`.

## Integrations & Events
- REST подписки world-service на данные глобальных событий (`api/v1/gameplay/world/global-events.yaml`) для синхронизации таймлайна.
- Webhook/Kafka событие `world.universe.stage.advanced` для narrative-service и economy-service.
- REST ссылки на narrative-service (`api/v1/narrative/quest-system.yaml`) для привязки квестовых арок.
- Экспорт данных для economy-service о доступности технологий по фазам (взаимодействие с `api/v1/gameplay/economy/equipment-matrix.yaml`).
- Безопасные админ-операции защищаются схемой `BearerAuth` из `security.yaml`.

## Acceptance Criteria
1. Создана спецификация `api/v1/lore/universe/universe.yaml` (≤ 400 строк). При превышении вынести модели в `universe-models.yaml`.
2. `info.x-microservice` указывает `world-service`, порт 8086, домен `lore`, base-path `/api/v1/lore/universe`.
3. Все endpoints из раздела реализованы с корректными методами, параметрами и ответами.
4. Использованы общие компоненты `$ref` из `api/v1/shared/common/responses.yaml`, `security.yaml`, `pagination.yaml`.
5. Определены схемы всех перечисленных моделей с обязательными полями, enum и примерами.
6. Для POST операций описаны тела запросов с валидацией и примерами.
7. Задокументированы события/интеграции (Kafka/Webhook) и связи с другими спецификациями.
8. Добавлены `servers`: `https://api.necp.game/v1` и `http://localhost:8080/api/v1`.
9. Обновлены `tasks/config/brain-mapping.yaml`, `.BRAIN/03-lore/universe.md` (секция статуса задач) и `tasks/queues/queued.md`.
10. Скрипт `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiSpec api/v1/lore/universe/universe.yaml` выполняется без ошибок.

## FAQ / Notes
- **Вопрос:** Что делать, если объём моделей превышает лимит в 400 строк?  
  **Ответ:** Вынести схемы в `api/v1/lore/universe/universe-models.yaml` и подключить через `$ref`, сохранив лимит основного файла.
- **Вопрос:** Как синхронизировать стадии симуляции с квестами?  
  **Ответ:** Использовать событие `world.universe.stage.advanced` и REST-эндпоинты narrative-service для обновления статуса арок.

## Change Log
- 2025-11-09 15:10 — Задание создано (API Task Creator Agent)

