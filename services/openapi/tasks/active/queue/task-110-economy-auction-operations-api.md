# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-110  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 20:25  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-108 (auction-database), API-TASK-109 (auction-mechanics)  

## Summary
Сформировать спецификацию `api/v1/economy/auction-house/auction-operations.yaml`, покрывающую прикладные REST/WS endpoints аукционного дома (создание лотов, поиск, ставки, buyout, безопасность, метрики, roadmap).

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-07 06:15) |

**Key points:** API категории (POST /api/v1/auctions, GET /api/v1/auctions, POST /bid, /buyout); безопасность (anti-fraud, limits); метрики (active auctions, bids/day, buyout rate); roadmap (MVP vs Phase 2 features).  
**Related docs:** `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-database.md`, `.BRAIN/02-gameplay/economy/player-market/`, `.BRAIN/05-technical/backend/anti-fraud-service.md`, `.BRAIN/05-technical/backend/notification-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/auction-house  
- **API directory:** `api/v1/economy/auction-house/auction-operations.yaml`  
- **base-path:** `/api/v1/economy/auction-house`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/auction-house`  
- **Shared UI/Form components:** `@shared/ui/AuctionList`, `@shared/ui/AuctionFilters`, `@shared/ui/MyAuctionsTable`, `@shared/ui/MyBidsTable`, `@shared/forms/AuctionCreateForm`, `@shared/forms/BidForm`, `@shared/forms/BuyoutForm`, `@shared/forms/AuctionCancelForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать все REST endpoints из документа: создание, поиск, ставки, buyout, cancel, модули лимитов/включений, ответные payloadы.
2. Смоделировать `CreateAuctionRequest/Response`, `GetAuctionsResponse`, `PlaceBidRequest/Response`, `BuyoutRequest/Response`, `AuctionFilter`, `AuctionMetrics`.
3. Зафиксировать security/anti-fraud требования, лимиты (10 активных лотов, 5% increment, max duration 7 дней) и метрики.
4. Отразить roadmap: какие функции входят в MVP и Phase 2 (reserve price, watch list, bid notifications).
5. Документировать интеграции с inventory, wallet, notifications, anti-fraud, telemetry/analytics, а также сравнение с Player Market.

## Endpoints
- `POST /auctions` — создание аукциона (валидация, блокировка предмета, возврат `auctionId`, `expiresAt`).
- `GET /auctions` — поиск и фильтрация лотов (search, price range, sorting, pagination).
- `POST /auctions/{id}/bid` — размещение ставки (вернуть `currentBid`, флаг `leading`).
- `POST /auctions/{id}/buyout` — мгновенная покупка (вернуть `paidAmount`, `item`).
- `POST /auctions/{id}/cancel` — отмена продавцом (перечислить условия, штрафы).
- `GET /auctions/{id}` — детали аукциона (информация из MVP/Phase2).
- `GET /auctions/my` — лоты продавца (active, sold, expired).
- `GET /auctions/my-bids` — активные ставки игрока.
- `GET /auctions/metrics` — метрики (active count, bids/day, sold/day, buyout rate).
- `GET /auctions/config` — конфигурация лимитов/комиссий.
- `POST /auctions/watchlist` (Phase 2 placeholder) — добавить в отслеживание.
- WebSocket `ws://.../streams/auctions` (Phase 2) — уведомления outbid, ending soon.

## Data Models
- `CreateAuctionRequest` — `characterId`, `itemId`, `quantity`, `startingBid`, `buyoutPrice`, `durationHours`.
- `CreateAuctionResponse` — `success`, `auctionId`, `expiresAt`.
- `AuctionListItem` — `id`, `item`, `currentBid`, `buyoutPrice`, `bidCount`, `expiresAt`.
- `PlaceBidRequest` — `characterId`, `bidAmount`.
- `PlaceBidResponse` — `success`, `currentBid`, `leading`, `expiresAt`.
- `BuyoutRequest` — `characterId`.
- `BuyoutResponse` — `success`, `paidAmount`, `item`.
- `AuctionFilter` — `search`, `minBid`, `maxBid`, `sortBy`, `page`, `pageSize`.
- `AuctionMetrics` — `active`, `bidsDaily`, `soldDaily`, `buyoutRate`.
- `AuctionConfig` — `maxActiveAuctions`, `minBidIncrementPercent`, `maxDurationHours`, `commissionPercent`.
- `RoadmapFeature` — `name`, `phase`, `status`.
- Ошибки: `AuctionLimitExceededError`, `AuctionValidationError`, `BidTooLowError`, `BuyoutUnavailableError`, `AntiFraudFlaggedError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Inventory-service: `lockItem`, `unlockItem`, `transferItem`.
- Wallet/payment-service: `reserveFunds`, `releaseFunds`, `transferFunds`, `applyCommission`.
- Anti-fraud-service: checks на создание/ставку.
- Notification-service: `auction.outbid`, `auction.won`, `auction.buyout`, `auction.expired`.
- Telemetry/analytics: метрики/ивенты.
- Player Market API: compare endpoint/ссылки.
- Rate limits: 10 активных аукционов, 100 ставок в час, max duration 7 дней.
- Roadmap: Phase 2 features flagged (reserve price, history, watch list, notifications).

## Acceptance Criteria
1. Спецификация `api/v1/economy/auction-house/auction-operations.yaml` создана, ≤ 500 строк, с корректным `info.x-microservice`.
2. Все endpoints из документа описаны: запросы, ответы, параметры, ошибки, примеры.
3. Модели и конфигурации (лимиты, комиссии, метрики, roadmap) отражены в `components`.
4. Интеграции с системами (inventory, wallet, anti-fraud, notification, analytics) задокументированы.
5. Указаны требования к фронтенду: формы, таблицы, фильтры, watch list, уведомления.
6. `tasks/config/brain-mapping.yaml` содержит запись `API-TASK-110`, статус `queued`, приоритет `high`.
7. `tasks/queues/queued.md` дополнен записью.
8. `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md` обновлен блоком статуса (выполнено).
9. После реализации запустить `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\auction-house\`.

## FAQ / Notes
- **Reserve price/Watch list?** Планы Phase 2 — описать placeholders и feature flags.
- **Bid notifications?** Прописать интеграцию с notification-service и WS endpoints.
- **Player Market?** Указать ссылку на различия и возможные интеграции.

## Change Log
- 2025-11-09 20:25 — Задание создано (API Task Creator Agent)


