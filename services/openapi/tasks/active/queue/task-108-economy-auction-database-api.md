# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-108  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 20:05  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-100 (inventory-core), API-TASK-102 (economy-contracts)  

## Summary
Подготовить спецификацию `api/v1/economy/auction-house/auction-database.yaml`, описывающую REST/WS интерфейсы аукционного дома: управление лотами, ставками, buyout, мониторинг, уведомления и отчётность по таблицам `auctions` и `auction_bids`.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/auction-house/auction-database.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-07 06:15) |

**Key points:** подробные схемы БД `auctions` и `auction_bids`, статусы `active/sold/expired/cancelled`, buyout, duration/expiry, индексы по статусу/истечению, UI потоки (browse, my auctions, my bids).  
**Related docs:** `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md`, `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`, `.BRAIN/05-technical/backend/economy-telemetry.md`, `.BRAIN/02-gameplay/economy/economy-contracts.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/auction-house  
- **API directory:** `api/v1/economy/auction-house/auction-database.yaml`  
- **base-path:** `/api/v1/economy/auction-house`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/auction-house`  
- **Shared UI/Form components:** `@shared/ui/AuctionList`, `@shared/ui/AuctionCard`, `@shared/ui/BidHistory`, `@shared/ui/AuctionTimer`, `@shared/forms/CreateAuctionForm`, `@shared/forms/PlaceBidForm`, `@shared/forms/BuyoutConfirmForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для управления лотами, ставок, buyout, отмены, просмотра активности и историй.
2. Смоделировать `Auction`, `AuctionBid`, `AuctionCreateRequest`, `BidRequest`, `BuyoutRequest`, `AuctionSummary`, `AuctionFilters`, `AuctionMetrics`.
3. Зафиксировать жизненный цикл лота (`active`, `sold`, `expired`, `cancelled`) и обработку таймеров, buyout, определения победителя.
4. Документировать интеграции с inventory (резерв/возврат предметов), economy-contracts (эскроу если нужно), notification-service, telemetry/analytics.
5. Добавить требования к фронтенду: страницы каталога, мои аукционы, мои ставки, real-time обновления, push-уведомления.

## Endpoints
- `GET /auctions` — список активных лотов (фильтры по категории, цене, оставшемуся времени, статусу).
- `GET /auctions/{auctionId}` — подробности лота, текущая ставка, buyout, история.
- `POST /auctions` — выставить лот (товар, стартовая ставка, buyout, длительность, ограничения).
- `POST /auctions/{auctionId}/bid` — сделать ставку, пересчитать `current_bid`, обновить `current_bidder`.
- `POST /auctions/{auctionId}/buyout` — моментальная покупка, перевод средств, завершение.
- `POST /auctions/{auctionId}/cancel` — отменить активный лот (условия и штрафы).
- `GET /auctions/{auctionId}/bids` — история ставок (пагинация, сортировка).
- `GET /auctions/my` — лоты текущего пользователя (продажи, активные/архив).
- `GET /auctions/my-bids` — активные ставки игрока, статус.
- `GET /auctions/summary` — агрегаты (кол-во активных, оборот, средний buyout).
- `POST /auctions/schedulers/expire` — обработка истёкших лотов (cron/webhook).
- WebSocket `ws://.../streams/auctions` — real-time обновления ставок, buyout, завершения.
- WebSocket `ws://.../streams/auction/{auctionId}` — канал конкретного лота (ставки, таймер).

## Data Models
- `Auction` — `id`, `itemId`, `sellerId`, `quantity`, `startingBid`, `currentBid`, `buyoutPrice`, `currentBidderId`, `bidCount`, `durationHours`, `expiresAt`, `status`, `soldPrice`, `soldMethod`, `soldAt`, `createdAt`.
- `AuctionCreateRequest` — `itemId`, `quantity`, `startingBid`, `buyoutPrice`, `durationHours`, `currency`, `visibility`, `autoRelist`.
- `BidRequest` — `bidAmount`, `currency`, `maxAutoBid`, `playerId`.
- `BuyoutRequest` — `playerId`, `currency`, `confirmation`.
- `AuctionBid` — `id`, `auctionId`, `bidderId`, `bidAmount`, `createdAt`.
- `AuctionFilters` — `category`, `rarity`, `minPrice`, `maxPrice`, `timeRemaining`, `search`.
- `AuctionSummary` — `totalActive`, `totalCompleted`, `turnover`, `averageBid`, `largestSale`, `expiredCount`.
- `AuctionMetrics` — `bidVelocity`, `conversionRate`, `buyoutUsage`, `cancelRate`.
- Ошибки: `AuctionNotFoundError`, `AuctionExpiredError`, `BidTooLowError`, `BuyoutNotAvailableError`, `AuctionAlreadySoldError`, `InventoryReservationError`, `CurrencyInsufficientError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.auction.created`, `economy.auction.bid-placed`, `economy.auction.buyout`, `economy.auction.expired`, `economy.auction.cancelled`, `economy.auction.sold`.
- Inventory-service: `POST /inventory/reserve` (при выставлении), `POST /inventory/release` (при завершении/отмене), `POST /inventory/transfer` (передача покупателю).
- Economy-contracts: опциональный эскроу для крупных лотов (REST hook).
- Payment/wallet service: списание/пополнение валюты (ставки, buyout, комиссионные).
- Notification-service: push/email `auction.outbid`, `auction.buyout`, `auction.expired`.
- Telemetry/analytics: `POST /analytics/auctions/events`, `POST /analytics/auctions/metrics`.
- Anti-fraud-service: `POST /anti-fraud/auction/check` для подозрительных ставок или buyout.
- Scheduler (Quartz): обработка `expiresAt`.
- Rate limits: создание ≤ 50 лотов/день, ставки ≤ 100/час, buyout контролируется комиссией.
- Security: OAuth2 PlayerSession; выставление/отмена требует проверки владельца; админ операции (ручное завершение) — роль `economy_admin`.

## Acceptance Criteria
1. Файл `api/v1/economy/auction-house/auction-database.yaml` создан (≤ 500 строк) с корректным `info.x-microservice`.
2. Задокументированы все перечисленные endpoints и WebSocket потоки, параметры, ответы, примеры и коды ошибок.
3. Схемы `Auction`, `AuctionBid`, `AuctionCreateRequest`, `BidRequest`, `BuyoutRequest`, `AuctionSummary`, `AuctionMetrics` описаны с обязательными полями и ссылками на общие компоненты.
4. Kafka события и интеграции (inventory, wallet, notification, telemetry, anti-fraud, scheduler) перечислены с payload, ключами и retry-политиками.
5. Учтены требования к UI: `modules/economy/auction-house`, real-time обновления, страницы `My Auctions`, `My Bids`, уведомления, таймеры.
6. `tasks/config/brain-mapping.yaml` содержит запись `API-TASK-108`, статус `queued`, приоритет `high`.
7. `.BRAIN/02-gameplay/economy/auction-house/auction-database.md` включает блок `API Tasks Status`.
8. `tasks/queues/queued.md` дополнен записью.
9. После реализации спецификации запустить `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\auction-house\`.

## FAQ / Notes
- **Нужны ли дополнительные таблицы?** Базовые таблицы описаны; указать возможность расширения (fees, escrow) через metadata.
- **Поддерживать ли автоставки?** В `BidRequest` предусмотреть `maxAutoBid`; реализация может быть расширена позже.
- **Как обрабатывать комиссии?** Документировать поля для комиссий (сервис, налог); сами значения балансируются отдельно (TODO).

## Change Log
- 2025-11-09 20:05 — Задание создано (API Task Creator Agent)


