# Task ID: API-TASK-276
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:05
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-260 (stock exchange management API), API-TASK-263 (stock exchange integration API), API-TASK-271 (guild contract board API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `faction-economy-assets.yaml`, описывающую управление фракционными активами, налогами, скидками и интеграцией с аукционами/крафтом.

**Что нужно сделать:** Определить REST/WS контракты economy-service для выдачи активов, расчёта модификаторов и обмена репутации на экономические бонусы.

---

## 🎯 Цель задания

Обеспечить:
- Каталог фракционных активов (obligations, catalysts, tokens)
- Процессы выдачи, продажи, выкупа и обмена активов
- Расчёт налогов/скидок в зависимости от репутации и city_unrest
- Интеграцию с аукционами, крафтом, логистикой и контрактной доской
- Потоки данных для аналитики и ивентов (AssetIssued, AuctionFilled, TaxUpdated)

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/factions/faction-economy-integration.md` — активы, потоки экономики, схемы таблиц
- Дополнительно:
  - `.BRAIN/02-gameplay/economy/stock-exchange/stock-exchange-overview.md`
  - `.BRAIN/02-gameplay/world/specter-hq.md`
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md`
  - `.BRAIN/05-technical/ui/guild-contract-board.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/economy/factions/assets.yaml`  
**Микросервисы:** economy-service (ядро), world-service (логистика, world flags), social-service (репутации), analytics-service (метрики), auction-service (market listings)

---

## 🧩 Обязательные секции

1. `GET /api/v1/economy/factions/assets` — каталог активов, фильтры по фракции/типу/редкости.
2. `GET /api/v1/economy/factions/assets/{assetId}` — детальное описание, требования, hooks в крафт/логистику.
3. `POST /api/v1/economy/factions/assets/issue` — выдача актива по событию (контракты, рейды), валидация репутации.
4. `POST /api/v1/economy/factions/assets/{assetId}/redeem` — обмен на бонусы (скидки, доступы, моды).
5. `POST /api/v1/economy/factions/trade-modifiers/calculate` — расчёт налогов/скидок по текущей репутации, city_unrest, сезонным эффектам.
6. `POST /api/v1/economy/factions/assets/{assetId}/listings` — создание листинга на аукционе (проксирование в auction-service).
7. `GET /api/v1/economy/factions/logistics/routes` — связь активов с логистическими бонусами (Nomad транспорт).
8. WebSocket `/ws/economy/factions` — события `AssetIssued`, `ListingFilled`, `TaxUpdated`, `ModifierExpired`.
9. Интеграции: world-service `POST /api/v1/world/logistics/routes/update`, crafting `POST /api/v1/economy/crafting/session/apply`, social-service `POST /api/v1/social/reputation/update`.
10. Схемы: `FactionAsset`, `AssetIssueRequest`, `RedeemPayload`, `TradeModifier`, `AuctionListing`, `LogisticsBonus`, `EventEnvelope`.

---

## ✅ Критерии приемки

1. Префикс `/api/v1/economy/factions` соблюдён у всех REST маршрутов.
2. Поддерживаются типы активов из таблицы (orbital-bond, solar-catalyst, memory-fragment, pyre-mod, mech-armor-plate, narrative-token, metanet-license).
3. Репутационные диапазоны и city_unrest modifiers влияют на расчёты налогов и скидок.
4. Выдача и выкуп активов отражаются в telemetry (`faction_asset_issued`, `faction_asset_redeemed`).
5. События WebSocket синхронизируются с event bus (economy.exchange, world.logistics).
6. Ошибки используют общий `Error` из `shared/common/responses.yaml`.
7. Target Architecture описывает фронтенд `modules/economy/trade` и state store `economy/factions`.
8. Указаны ограничения на количество активов в обращении и rate limits на выдачу/выкуп.
9. Схемы таблиц (`faction_assets`, `faction_trade_modifiers`) отзеркалены в разделах `components/schemas`.
10. Документированы аудиторские события (`tax_modifier_changed`, `asset_liquidity_alert`) для analytics-service.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

