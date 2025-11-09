# Task ID: API-TASK-109
**Тип:** API Generation  
**Приоритет:** high  
**Статус:** completed  
**Создано:** 2025-11-09 20:15 — ДУАПИТАСК  
**Завершено:** 2025-11-09 16:30 — АПИТАСК

---

## 📋 Краткое описание

Спецификация `auction-mechanics.yaml` описывает REST-контракты экономики для аукционного дома: правила создания лота, ставки с автопродлением, buyout, отмену, cron-завершение, конфигурацию комиссий и отличие от Player Market.

---

## ✅ Выполнено

- Создан основной контракт `api/v1/economy/auction-house/auction-mechanics.yaml` (≤ 500 строк) с блоком `info.x-microservice`, servers (gateway + economy segment) и секцией WebSocket событий.
- Реализованы endpoints:
  - `POST /auction-house/rules/validate-create`, `place-bid`, `buyout`, `cancel`, `extend`, `scheduler/process-expired`;
  - `GET /auction-house/config`, `GET /auction-house/notifications/sample`, `POST /auction-house/compare`.
- Описаны ключевые модели: `AuctionCreationRules`, `BidRules`, `BuyoutRules`, `AuctionStatusTransition`, `CommissionConfig`, `NotificationPayload`, `SchedulerConfig`, `PlayerMarketComparison`.
- Задокументированы интеграции с payment/wallet, inventory, notification, anti-fraud, scheduler, analytics.
- Подготовлены схемы запросов/ответов (`AuctionCreateRequest`, `BidRequest`, `BuyoutRequest`, `ProcessExpiredResult` и др.).
- Валидация `..\scripts\validate-swagger.ps1 -ApiDirectory api/v1/economy/auction-house` успешно пройдена.

---

## 🔗 Спецификации

- `api/v1/economy/auction-house/auction-mechanics.yaml`

---

## 🧾 Источники

- `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md`
- `.BRAIN/02-gameplay/economy/auction-house/auction-database.md`
- `.BRAIN/02-gameplay/economy/auction-house/auction-operations.md`
- `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`
- `.BRAIN/02-gameplay/economy/economy-contracts.md`
- `.BRAIN/05-technical/backend/payment-wallets.md`


