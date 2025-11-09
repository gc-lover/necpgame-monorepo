# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-109  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 20:15  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-108 (auction-database)  

## Summary
Создать спецификацию `api/v1/economy/auction-house/auction-mechanics.yaml`, описывающую прикладные механики аукционов: создание лотов, правила ставок, buyout, авто-продление, завершение, комиссионные и уведомления.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-07 06:15) |

**Key points:** сравнение Auction House vs Player Market; механика создания лота (валидаторы, блокировка предмета, сроки); ставки (+5% минимум, автопродление 5 мин, возврат средств предыдущему участнику); buyout; завершение (cron, комиссия 5%, notification).  
**Related docs:** `.BRAIN/02-gameplay/economy/auction-house/auction-database.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md`, `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`, `.BRAIN/02-gameplay/economy/economy-contracts.md`, `.BRAIN/05-technical/backend/payment-wallets.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/auction-house  
- **API directory:** `api/v1/economy/auction-house/auction-mechanics.yaml`  
- **base-path:** `/api/v1/economy/auction-house`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/auction-house`  
- **Shared UI/Form components:** `@shared/ui/AuctionCreator`, `@shared/ui/BidPanel`, `@shared/ui/BuyoutModal`, `@shared/ui/AuctionStatusBadge`, `@shared/forms/CreateAuctionForm`, `@shared/forms/PlaceBidForm`, `@shared/forms/BuyoutConfirmForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints отражающие бизнес-логику: валидации лота, ставки с минимальным повышением, buyout, отмену, продление, вычисление комиссий.
2. Смоделировать `AuctionCreationRules`, `BidRules`, `BuyoutRules`, `AuctionStatusTransition`, `CommissionConfig`, `NotificationPayload`.
3. Зафиксировать процессы автопродления, возврата средств, cron обработки завершений, взаимодействие с payment и notification сервисами.
4. Документировать API различий `Auction House` vs `Player Market`, включая комиссии, доступные действия, роли и ограничения.
5. Добавить требования к фронтенду (валидация форм, отображение таймеров, уведомления об outbid, buyout, автопродление) и telemetry.

## Endpoints
- `POST /auction-house/rules/validate-create` — проверка параметров создания лота (лимиты, цены, buyout>start).
- `POST /auction-house/auctions/{auctionId}/place-bid` — логика ставки с проверкой минимальной надбавки, резервирования средств, возврата предыдущему участнику.
- `POST /auction-house/auctions/{auctionId}/buyout` — buyout с проверкой доступности, списанием и завершением.
- `POST /auction-house/auctions/{auctionId}/cancel` — отмена продавцом (условия, штрафы, возврат предмета).
- `POST /auction-house/auctions/{auctionId}/extend` — ручное/автоматическое продление (возвращает новый `expiresAt`).
- `POST /auction-house/scheduler/process-expired` — endpoint для cron обработки истекших лотов.
- `GET /auction-house/config` — конфигурация комиссий, минимальных ставок, таймеров, автопродления.
- `GET /auction-house/notifications/sample` — payload уведомлений (`outbid`, `won`, `buyout`, `expired`).
- `POST /auction-house/compare` — возвращает различия Auction House и Player Market (комиссии, скорости, действия).

## Data Models
- `AuctionCreationRules` — `minStartingBid`, `maxDurationHours`, `allowedDurations`, `buyoutMinRatio`, `commissionPercent`, `listingLimit`.
- `BidRules` — `minIncrementPercent`, `autoExtendThreshold`, `autoExtendMinutes`, `maxActiveBids`, `currency`.
- `BuyoutRules` — `enabled`, `minPrice`, `maxPrice`, `cooldown`.
- `AuctionStatusTransition` — `from`, `to`, `trigger`, `conditions`, `notifications`.
- `CommissionConfig` — `listingFee`, `saleCommission`, `buyoutCommission`, `refundPolicy`.
- `NotificationPayload` — `event`, `templateId`, `channels`, `data`.
- `SchedulerConfig` — `frequency`, `batchSize`, `timeWindow`, `gracePeriod`.
- `PlayerMarketComparison` — `feature`, `auctionHouse`, `playerMarket`.
- Ошибки: `AuctionValidationError`, `BidValidationError`, `BuyoutNotAllowedError`, `AuctionCancelForbiddenError`, `SchedulerLockError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `security.yaml`.

## Integrations & Events
- Payment/wallet service: `POST /wallets/reserve`, `POST /wallets/refund`, `POST /wallets/transfer` (ставки, buyout, комиссии).
- Inventory-service: `POST /inventory/lock`, `POST /inventory/unlock`, `POST /inventory/transfer`.
- Notification-service: `POST /notifications/auction/outbid`, `.../won`, `.../buyout`, `.../expired`.
- Scheduler/Cron: hook для `process-expired`.
- Analytics/telemetry: `POST /analytics/auctions/bid-event`, `.../buyout-event`, `.../cancel-event`.
- Anti-fraud: `POST /anti-fraud/auction/bid-check`, `.../create-check`.
- Rate limits: создание ≤ 50 лотов/день, ставки ≤ 100/час/игрок, buyout rate ограничен комиссией, отмена лота ≤ 10/день.
- WebSocket events: `auction.house.outbid`, `auction.house.buyout`, `auction.house.timer-extended`.

## Acceptance Criteria
1. Спецификация `api/v1/economy/auction-house/auction-mechanics.yaml` создана, ≤ 500 строк, с корректным `info.x-microservice`.
2. Описаны все endpoints, связанные модели и бизнес-правила (валидации, автопродление, комиссии, уведомления) с примерами.
3. Задокументированы интеграции payment/inventory/notification/scheduler/analytics/anti-fraud; указаны события WebSocket/Kafka.
4. Прописаны различия Auction House vs Player Market, включая комиссии и сценарии.
5. Фронтенд требования: формы, UI компоненты, таймеры, уведомления в `modules/economy/auction-house`.
6. `tasks/config/brain-mapping.yaml` содержит запись `API-TASK-109`, статус `queued`, приоритет `high`.
7. `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md` содержит блок `API Tasks Status`.
8. `tasks/queues/queued.md` дополнен записью.
9. После реализации запускается `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\auction-house\`.

## FAQ / Notes
- **Поддерживать auto-bid?** Предусмотреть поля `maxAutoBid` и описать алгоритм.
- **Как учитывать Player Market?** Включить сравнение/конфиг `auction vs market` для фронтенда.
- **Комиссии?** Документировать поля под комиссионные и политики возврата; конкретные проценты балансируются отдельно.

## Change Log
- 2025-11-09 20:15 — Задание создано (API Task Creator Agent)


