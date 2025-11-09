# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-112  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 20:45  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-100 (inventory-core), API-TASK-102 (economy-contracts), API-TASK-108..111 (auction & trade subsystems)  

## Summary
Подготовить спецификацию `api/v1/economy/trading/trading.yaml`, покрывающую экосистему торговли: player market (ордерная система), auction house интеграции, P2P сделки, влияние социальных структур, торговые маршруты и фракционные ограничения.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/economy-trading.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 04:28) |

**Key points:** комбинированные типы торговли (player market, auction house, P2P); репутационные бонусы/ограничения; торговые маршруты между регионами и фракциями; TODO по балансам не блокирует API; связь с социальными механиками.  
**Related docs:** `.BRAIN/02-gameplay/economy/economy-currencies-resources.md`, `.BRAIN/02-gameplay/economy/auction-house/`, `.BRAIN/05-technical/backend/trade-system.md`, `.BRAIN/02-gameplay/social/social-mechanics-overview.md`, `.BRAIN/02-gameplay/world/world-state/player-impact-systems.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/trading  
- **API directory:** `api/v1/economy/trading/trading.yaml`  
- **base-path:** `/api/v1/economy/trading`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/trading`  
- **Shared UI/Form components:** `@shared/ui/TradingMarketBoard`, `@shared/ui/TradeRouteMap`, `@shared/ui/ReputationBadge`, `@shared/ui/TradingOfferCard`, `@shared/forms/CreateOrderForm`, `@shared/forms/RouteLogisticsForm`, `@shared/forms/FactionPermissionForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для player market (ордеры), доступа к рынкам фракций, торговых маршрутов, социальных модификаторов и логистики доставки.
2. Смоделировать `MarketOrder`, `MarketOrderRequest`, `MarketOrderBook`, `FactionMarketAccess`, `TradeRoute`, `RouteSegment`, `ReputationModifier`, `TradeEvent`.
3. Зафиксировать правила влияния репутации, фракций, торговых войн, региональных цен и маршрутов; предусмотреть аналитику и логистику.
4. Интегрировать данные из аукциона, P2P и торговых маршрутов, обеспечить связи с социальными сервисами.
5. Добавить требования к фронтенду: доска ордеров, карта маршрутов, индикаторы репутации, уведомления о торговых событиях.

## Endpoints
- `POST /market/orders` — разместить ордер (buy/sell) на player market, с валидацией репутации и лимитов.
- `GET /market/orders` — получить ордербук (фильтры по товару, региону, фракции, сортировка).
- `DELETE /market/orders/{orderId}` — отменить ордер.
- `POST /market/orders/{orderId}/fill` — исполнить ордер полностью/частично.
- `GET /factions/markets` — доступные фракционные рынки, требования по репутации.
- `POST /factions/markets/{marketId}/request-access` — запрос доступа (обработка заявок/стоимость).
- `GET /routes` — торговые маршруты между регионами (прибыль, риски, события).
- `POST /routes/{routeId}/schedule` — планирование перевозки (товар, объём, охрана, время).
- `GET /social/modifiers` — социальные модификаторы торговли (скидки, налоги, блокировки).
- `GET /events/trading` — торговые войны, embargo, бонусы.
- `POST /trading/logistics/telemetry` — отчёт о выполнении маршрута (используется для аналитики).
- WebSocket `ws://.../streams/trading` — обновления рынков, маршрутов, социальных событий.

## Data Models
- `MarketOrder` — `id`, `type` (buy/sell), `itemId`, `quantity`, `price`, `currency`, `region`, `faction`, `ownerId`, `status`, `expiresAt`, `createdAt`.
- `MarketOrderRequest` — `itemId`, `quantity`, `price`, `currency`, `type`, `region`, `faction`.
- `OrderFillRequest` — `quantity`, `buyerId`, `deliveryMethod`.
- `MarketOrderBook` — `itemId`, `bids[]`, `asks[]`, `spread`, `lastTrade`.
- `FactionMarketAccess` — `marketId`, `requiredReputation`, `taxRate`, `discount`, `restriction`.
- `TradeRoute` — `id`, `origin`, `destination`, `distance`, `riskLevel`, `recommendedCommodity`, `baseProfit`, `events`.
- `RouteScheduleRequest` — `routeId`, `commodity`, `quantity`, `escortLevel`, `departureTime`.
- `ReputationModifier` — `faction`, `relation`, `priceModifier`, `access`.
- `TradeEvent` — `eventType` (embargo, war, bonus), `factions`, `regions`, `effect`, `duration`.
- `TradingTelemetry` — `routeId`, `completionStatus`, `losses`, `profit`, `timeSpent`.
- Ошибки: `OrderLimitExceededError`, `ReputationInsufficientError`, `MarketAccessDeniedError`, `RouteUnavailableError`, `TradeEventConflictError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Inventory-service: обработка поставок, резерв товаров для ордеров.
- Auction/P2P: ссылки на существующие задачи (перекрёстные уведомления, обмен).
- Social-service: репутация, фракции, социальные бонусы/ограничения.
- World-service: маршруты, расстояния, события на карте.
- Logistics-service: расписания перевозок.
- Economy-events: влияние глобальных событий на торговлю.
- Anti-fraud: проверка ордеров и перевозок.
- Notification-service: торговые алерты, изменения рынков.
- Analytics-service: отчёты, heatmaps, спекуляции.
- Rate limits: ордера ≤ N/день, маршруты ≤ M/день, фракционные заявки.
- WebSocket pushes: обновления ордербука, маршрутов, событий.

## Acceptance Criteria
1. Создан файл `api/v1/economy/trading/trading.yaml` ≤ 500 строк с `info.x-microservice` economy-service.
2. Описаны все перечисленные endpoints и WebSocket поток с запросами, ответами, примерами, ошибками.
3. Модели ордеров, маршрутов, репутационных модификаторов, событий и телеметрии задокументированы.
4. Интеграции с `inventory`, `social`, `world`, `logistics`, `events`, `anti-fraud`, `notification`, `analytics` описаны.
5. Фронтенд требования указаны (доска ордеров, карта маршрутов, репутация, уведомления).
6. `tasks/config/brain-mapping.yaml` дополнен записью `API-TASK-112`, статус `queued`, приоритет `high`.
7. `tasks/queues/queued.md` обновлён записью.
8. `.BRAIN/02-gameplay/economy/economy-trading.md` содержат блок статуса (выполнено).
9. После реализации запуск `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\trading\`.

## FAQ / Notes
- **Балансировка?** Числовые значения остаются TODO, отметить в примечаниях.
- **Связь с Player Market/Auction?** Показать различия и точки интеграции.
- **Маршруты/фракции?** Указать поля для управления рисками, налогами, преимуществами союзов.

## Change Log
- 2025-11-09 20:45 — Задание создано (API Task Creator Agent)


