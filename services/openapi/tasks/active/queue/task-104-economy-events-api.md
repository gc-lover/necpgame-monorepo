# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-104  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 19:12  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-102 (economy-contracts), API-TASK-103 (economy-analytics)  

## Summary
Сформировать спецификацию `api/v1/economy/events/economic-events.yaml`, описывающую управление экономическими событиями: планирование, анонсы, активацию, откат эффектов, мониторинг и real-time поток для модулей экономики.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/economy-events.md` |
| Version | v1.1.0 |
| Status | approved |
| API readiness | ready (2025-11-09 03:32) |

**Key points:** типы событий (кризис, бум, инфляция, эмбарго, санкции, тарифы), state machine (`PLANNED`, `ANNOUNCED`, `ACTIVE`, `COOLDOWN`, `ARCHIVED`), scheduler и ограничения, REST/WS API (`/economy/events`, `/announce`, `/cancel`, `feed`), EventBus `economy.events.*`, мониторинг (`PriceDeviation%`, PagerDuty), интеграции с pricing, stock-exchange, currency, quest, analytics.  
**Related docs:** `.BRAIN/02-gameplay/economy/economy-analytics.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md`, `.BRAIN/02-gameplay/economy/currency-exchange.md`, `.BRAIN/05-technical/backend/pricing-engine.md`, `.BRAIN/05-technical/backend/quest-engine-backend.md`, `.BRAIN/05-technical/backend/notification-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/events  
- **API directory:** `api/v1/economy/events/economic-events.yaml`  
- **base-path:** `/api/v1/economy/events`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/events`  
- **Shared UI/Form components:** `@shared/ui/EventTimeline`, `@shared/ui/EventBanner`, `@shared/ui/EventImpactChart`, `@shared/forms/EventCreateForm`, `@shared/forms/EventAnnounceForm`, `@shared/forms/EventCancelForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для CRUD событий, анонсов, активации, отмены, аудита и аналитики; зафиксировать WebSocket поток `feed`.
2. Смоделировать `EconomicEvent`, `EventEffect`, `RegionImpact`, `SectorImpact`, `Schedule`, `Announcement`, `Cancellation`, `EventAuditEntry`, `EventMetrics`.
3. Определить правила планировщика, лимиты (≤3 глобальных, ≤5 региональных, cooldown ≥7 дней) и анти-фрод проверки.
4. Задокументировать события EventBus `economy.events.*`, интеграции с pricing, stock-exchange, currency, quest, analytics сервисами, уведомлениями.
5. Включить мониторинг и метрики, PagerDuty/alerting, требования фронтенда (UI, push-уведомления), Dependency на контракт/аналитику для отображения последствий.

## Endpoints
- `GET /` — список событий (фильтры `status`, `type`, `region`, `severity`).
- `POST /` — создание события (админ/геймдизайн) с указанием эффектов, расписания, каналов коммуникации.
- `GET /{eventId}` — подробности события, эффекты, интеграции, текущий статус.
- `PATCH /{eventId}` — обновление дат, эффектов, статуса (подписи, аудит).
- `POST /{eventId}/announce` — публикация анонса, запуск уведомлений, `warning_time`.
- `POST /{eventId}/activate` — принудительный старт (если нужно обойти scheduler).
- `POST /{eventId}/cancel` — аварийный откат (rolling back effects, audit).
- `POST /{eventId}/archive` — завершение, перевод в архив, генерация отчётов.
- `GET /{eventId}/timeline` — история изменений, подписи, уведомления.
- `GET /metrics/summary` — агрегаты (кол-во активных, price deviation, player sentiment).
- `GET /feed` — REST/WS endpoint для real-time обновлений (long polling или ws).
- `GET /planner/schedule` — планировщик событий (предстоящие слоты, конфликты).

## Data Models
- `EconomicEvent` — базовые поля (`id`, `type`, `name`, `description`, `status`, `severity`, `regions`, `sectors`, `startDate`, `endDate`, `effects`, `stackable`, `cooldown`).
- `EventEffect` — изменение цен, валют, налогов, trade restrictions.
- `EventSchedule` — планирование (start, end, warning, cooldown).
- `EventAnnouncement` — сообщения, каналы (UI, push, email), audiences.
- `EventCancellation` — причину, rollbackAction, компенсации.
- `EventTimelineEntry` — аудит (timestamp, actor, action, payload).
- `EventMetrics` — priceDeviation, transactionVolume, playerSentiment, uptime, rollbackStatus.
- `EventAlert` — PagerDuty/monitoring конфигурации.
- Ошибки: `EventLimitExceededError`, `EventOverlapError`, `EventStateConflictError`, `SchedulerUnavailableError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.events.created`, `economy.events.announced`, `economy.events.activated`, `economy.events.effect-applied`, `economy.events.effect-rolled-back`, `economy.events.archived`.
- Scheduler service: Quartz-based `economic-event-scheduler` (`POST /scheduler/events`, `DELETE /scheduler/events/{id}`).
- Pricing engine: `POST /pricing/adjustments` для применения ценовых модификаторов.
- Stock exchange: `POST /stock/exchange/events` для изменения индексов.
- Currency exchange: `POST /currency/events` для корректировки курсов.
- Quest-service: `POST /quests/events` — триггеры из сюжетов.
- Notification-service: `POST /notifications/events`.
- Analytics-service: `POST /analytics/events/report`.
- Telemetry: `POST /economy/telemetry/events` (сбор метрик).
- Rate limits: создание событий ≤ 20/день, анонсы ≤ 5/час, cancel/activate доступно только `economy_admin`.
- Security: OAuth2 AdminSession, роли `economy_admin`, `economy_analyst`.

## Acceptance Criteria
1. Создан файл `api/v1/economy/events/economic-events.yaml` (≤ 500 строк) с `info.x-microservice` → economy-service.
2. Все REST endpoints и WebSocket потоки задокументированы с параметрами, телами запросов/ответов, примерами и кодами ошибок.
3. Модели событий, эффектов, расписаний, анонсов, отмен и метрик описаны с ограничениями и ссылками на общие компоненты.
4. Kafka события и интеграции с pricing/stock/currency/quest/analytics/notification сервисами перечислены, указаны payload и правила ретрая.
5. Отражены ограничения scheduler (stackable, cooldown, conflict detection) и мониторинг (PagerDuty, PriceDeviation).
6. Frontend секция описывает UI компоненты, push уведомления, Orval клиент `@api/economy/events`, state `useEconomyStore`.
7. Обновлён `tasks/config/brain-mapping.yaml` (API-TASK-104, статус `queued`, приоритет `high`).
8. `.BRAIN/02-gameplay/economy/economy-events.md` содержит секцию `API Tasks Status`.
9. `tasks/queues/queued.md` дополнен записью.
10. После реализации запуск `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\events\`.

## FAQ / Notes
- **Нужно ли делить на глобальные/региональные события?** Описать в схемах поле `scope` (`global`, `regional`, `sector`); дробление возможно позже, если файл превысит лимит.
- **Как обрабатывать stackable эффекты?** Указать, что эффекты складываются до лимита, scheduler предотвращает конфликты; документация должна содержать сетку приоритетов.
- **Что с компенсациями игрокам?** Указать в `EventCancellation`/`COOLDOWN`, что economy-service взаимодействует с wallet/compensation сервисом (link через TODO).

## Change Log
- 2025-11-09 19:12 — Задание создано (API Task Creator Agent)


