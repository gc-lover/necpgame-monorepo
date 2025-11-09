# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-114  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 22:05  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-098 (quest-engine core), API-TASK-103 (economy analytics feeds)

## Summary
Создать OpenAPI спецификацию `api/v1/narrative/quests/america/vancouver/granville-island.yaml`, описывающую квест VANCOUVER-2029-009 «Granville Island» с полным lifecycle: этапы A1–A6, ветвления (Artisan Revival, Corporate Showcase, Tide Syndicate Takeover), интеграции с economy/social/world компонентами, KPI, WebSocket live-обновлениями и событиями EventBus.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/03-lore/_03-lore/timeline-author/quests/america/vancouver/2020-2029/quest-009-granville-island.md` |
| Version | v1.0.0 |
| Status | review (api-ready) |
| API readiness | ready (2025-11-09 11:09) |

**Key points:** этапная структура A1–A6, навыковые проверки (crafting, performance, stealth, hacking), KPI культуры/экономики, три финальных исхода, REST + WebSocket + EventBus требования, таблицы хранения, связи с economy/social/world сервисами.  
**Related docs:** `quest-engine-backend.md`, `economy/player-market/player-market-core.md`, `social/npc-hiring-world-impact-детально.md`, `world/world-state/world-state.md`, `modules/narrative/quests` UX спецификации.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** narrative-service  
- **Port:** 8087  
- **Domain:** narrative.quests  
- **API directory:** `api/v1/narrative/quests/america/vancouver/granville-island.yaml`  
- **base-path:** `/api/v1/narrative/quests/america/vancouver/granville-island`  
- **Java package:** `com.necpgame.narrative.quests.vancouver.granvilleisland`  
- **Frontend module:** `modules/narrative/quests`  
- **Shared clients:** `@api/narrative/quests`, `@shared/ws/QuestLiveFeed`, `@shared/state/useQuestStore`

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для управления стадиями квеста (инициализация, действия, проверки, закрепление исхода).
2. Смоделировать payload/response схемы для `QuestStage`, `QuestActionRequest`, `SkillCheckResult`, `QuestOutcome`, `KpiSnapshot`.
3. Задокументировать бизнес-правила KPI (culture.score, economy.margin, compliance.score), таймеры и лимиты попыток.
4. Добавить WebSocket канал `/ws/quests/granville-island/live` с событиями лайв-обновлений и fallback SSE.
5. Зафиксировать EventBus события `quest.granville.stage.completed`, `quest.granville.contract.signed`, `quest.granville.outcome.changed` с payload и consumer списком.
6. Учесть интеграции с economy-service (контракты, динамическое ценообразование), social-service (репутация), world-service (глобальные флаги), telemetry (KPI).
7. Добавить разделы безопасности: JWT, permisssion `narrative:quests:granville`, защита от спама (rate limits) и античит проверка десинхронизации.
8. Прописать наблюдаемость: метрики (`quest_granville_stage_duration_ms`, `quest_granville_live_ws_active`), трассировки и аудит завершений.
9. Обновить `tasks/config/brain-mapping.yaml` и очереди задач (queued), связать с фронтендом и QA чеклистами.
10. Проверить спецификацию через `validate-swagger.ps1`, обеспечить использование общих компонентов (`responses.yaml`, `pagination.yaml`, `security.yaml`).

## Endpoints
- `POST /` — старт квеста и выдача первой сцены (учёт репутации и ресурсов игрока).
- `GET /{playerId}` — получение текущего состояния стадий A1–A6, активных целей, таймеров.
- `POST /actions` — выполнение действия на сцене (передаёт `stageId`, `choiceId`, `skillCheck`, `resourceAdjustments`).
- `POST /contracts` — управление соглашениями (Axiom, Artisan Cooperative, Tide Syndicate) с данными экономических параметров.
- `POST /outcome` — фиксация финала, начисление наград, обновление глобальных флагов.
- `GET /kpi` — получение KPI и аналитики прогресса (culture/economy/compliance), используется фронтендом и аналитикой.

## Data Models
- `QuestStage` — идентификаторы сцен, требования, таймеры, доступные выборы, связанные сервисы.
- `QuestActionRequest` — stageId, choiceId, skillCheck, ресурсы, compliance impact.
- `SkillCheckResult` — тип навыка, порог, бросок, успех/провал, бонусы.
- `QuestOutcome` — итоговый путь (Artisan, Corporate, Tide, Fail), награды, глобальные изменения.
- `KpiSnapshot` — cultureScore, economyMargin, complianceScore, timestamp, источники.
- `ContractAgreement` — тип контракта, участники, маржа, условия, penalties.
- Общие компоненты: ссылки на `api/v1/shared/common/responses.yaml`, `security.yaml`, `pagination.yaml`.

## Integrations & Events
- REST зависимости: `economy-service` (dynamic pricing, vendor_contracts), `social-service` (reputation adjustments), `world-service` (world-state flags), `telemetry-service` (KPI ingestion).
- WebSocket: `/ws/quests/granville-island/live` — события `stage.update`, `crowd.reaction`, `timer.tick`.
- EventBus: `narrative.quest.granville.stage.completed` (producer: narrative-service; consumers: telemetry, quest-engine, social-service), `narrative.quest.granville.contract.signed` (consumers: economy-service, compliance-service), `narrative.quest.granville.outcome.changed` (consumers: world-service, notification-service, analytics).
- Kafka topics должны следовать пространству имён `narrative.quest.granville.*` с описанием ключей партиционирования и SLA.

## Acceptance Criteria
1. Создан файл `api/v1/narrative/quests/america/vancouver/granville-island.yaml` (≤ 500 строк) и проходит `OpenAPI 3.0.3` валидацию.
2. Раздел `info.x-microservice` описывает narrative-service (порт 8087, base-path `/api/v1/narrative/quests/america/vancouver/granville-island`).
3. Все перечисленные REST endpoints задокументированы: запросы, ответы, коды ошибок, примеры, ссылки на общие компоненты.
4. Определены схемы `QuestStage`, `QuestActionRequest`, `SkillCheckResult`, `QuestOutcome`, `ContractAgreement`, `KpiSnapshot` с обязательными полями.
5. WebSocket и SSE описаны через `x-streams` (события, payload, типы каналов).
6. Kafka события зафиксированы в `x-events` с payload, producers, consumers, retention и мониторингом.
7. Учтены правила безопасности: `bearerAuth`, пермишены, rate limits, античит токен, проверка десинхронизации.
8. Наблюдаемость: список метрик, логи, трассировки, аудит.
9. Обновлены `brain-mapping.yaml`, `tasks/queues/queued.md`, а `.BRAIN` документ содержит актуальный `API Tasks Status`.
10. Ссылка на фронтенд модуль `modules/narrative/quests` и QA чеклист (`QA-QUEST-GRANVILLE`) добавлена в `x-notes`.

## FAQ / Notes
- Квест рассчитан на гибридный геймплей: социальные проверки + лёгкий стелс, поэтому предусмотреть флаги `actionMode: DIALOGUE | STEALTH | PERFORMANCE`.
- Для корпоративного финала нужно учитывать влияние на налоговую систему — интеграция с `economy.events` для push уведомлений.
- Artisan ветка требует ссылок на крафтовые рецепты (`crafting.recipe.granville-stage`) и разблокировку ивентов; описать в `x-integrations`.
- Tide Syndicate Ending открывает `black-market` доступ, опишите в ответе `outcomeDetails.blackMarketAccess = true`.
- Обязателен fallback сценарий для провала таймера A2 и для повторного запуска после cooldown (72 внутриигровых часа).

