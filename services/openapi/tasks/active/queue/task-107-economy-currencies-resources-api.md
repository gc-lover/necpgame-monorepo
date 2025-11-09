# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-107  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 19:55  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-100 (inventory-core), API-TASK-102 (economy-contracts), API-TASK-104 (economy-events)  

## Summary
Описать спецификацию `api/v1/economy/currencies/currencies-resources.yaml`, охватывающую региональные валюты, биржу валют, ресурсы всех типов, методы добычи и обмен, интеграции с экономическими подсистемами и курсы.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/economy-currencies-resources.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 03:34) |

**Key points:** региональные валюты + биржа, типы ресурсов (базовые, редкие, крафт, импланты, фракционные, киберпанк товары), комбинированные способы добычи (экстракт-зоны, open world, производство, данные), TODO по балансу.  
**Related docs:** `.BRAIN/02-gameplay/economy/economy-overview.md`, `.BRAIN/02-gameplay/economy/economy-events.md`, `.BRAIN/02-gameplay/economy/economy-trading.md`, `.BRAIN/02-gameplay/economy/economy-crafting.md`, `.BRAIN/02-gameplay/combat/combat-implants.md`, `.BRAIN/05-technical/backend/anti-fraud-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/currencies  
- **API directory:** `api/v1/economy/currencies/currencies-resources.yaml`  
- **base-path:** `/api/v1/economy/currencies`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/currencies`  
- **Shared UI/Form components:** `@shared/ui/CurrencyExchange`, `@shared/ui/ResourceCatalog`, `@shared/ui/ExchangeRateChart`, `@shared/ui/ResourceAvailabilityMap`, `@shared/forms/CurrencyExchangeForm`, `@shared/forms/ResourceGatherForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для каталога валют, обмена, курсов, ресурсов, методов добычи, распределения по регионам и tracking.
2. Смоделировать `Currency`, `ExchangeRate`, `CurrencyExchangeRequest`, `Resource`, `ResourceSource`, `ResourceDrop`, `ResourceProcessing`, `ResourceInventory`, `ResourceTelemetry`.
3. Учитывать региональные валюты, обменные курсы, биржу, регулирование (anti-fraud), и товары киберпанк лора (данные, лицензии, чипы).
4. Задокументировать интеграции с inventory, trading, events, crafting, quest, faction, analytics, anti-fraud сервисами.
5. Зафиксировать требования к фронтенду, real-time потокам курсов, уведомлениям о смене курсов и доступности ресурсов.

## Endpoints
- `GET /currencies` — список валют (регион, описание, ограничения).
- `GET /currencies/{currencyId}` — детали валюты, привязка к регионам, события, ограничения.
- `GET /exchange/rates` — текущие курсы (base, counter, spread, lastUpdated, volatility).
- `POST /exchange/convert` — обмен валют (проверка лимитов, комиссий, anti-fraud).
- `GET /exchange/history` — история курсов (timeframe, region, events).
- `GET /resources` — каталог ресурсов (тип, редкость, источники, обработка).
- `GET /resources/{resourceId}` — детализация ресурса, источники добычи, регионы, обработка, связанные рецепты.
- `POST /resources/track` — подписка на ресурс (уведомления о событиях, курсах).
- `GET /resources/availability` — доступность ресурсов по регионам (добыча, торговля, события).
- `POST /resources/report` — отчёт о добыче (экстракт-зона, open world, производство).
- `GET /data-market/items` — каталог данных/информации/контрактов как ресурсов (киберпанк товары).
- `POST /data-market/trade` — купить/продать данные, лицензии, контракты (KYC, reputation).
- WebSocket `ws://.../streams/exchange` — real-time курсы, спреды, события.
- WebSocket `ws://.../streams/resources` — онлайн обновления о доступности, событиях, редких спавнах.

## Data Models
- `Currency` — `id`, `name`, `symbol`, `region`, `type` (base, pvp, pve, faction), `description`, `regulations`, `events`.
- `ExchangeRate` — `pair`, `buyRate`, `sellRate`, `spread`, `volatility`, `lastUpdated`, `eventImpact`.
- `CurrencyExchangeRequest` — `fromCurrency`, `toCurrency`, `amount`, `playerId`, `region`, `channel`.
- `CurrencyExchangeResponse` — итоговая сумма, комиссии, расчетные данные.
- `Resource` — `id`, `name`, `category`, `rarity`, `description`, `usage`, `metadata` (cyberpunk items, data).
- `ResourceSource` — `type` (extract zone, open world, production, data market), `details`, `riskLevel`, `requirements`.
- `ResourceDrop` — `location`, `dropRate`, `conditions`, `eventModifiers`.
- `ResourceProcessing` — `input`, `output`, `time`, `station`, `skills`.
- `ResourceInventory` — `playerId`, `resourceId`, `quantity`, `quality`, `region`, `storage`.
- `ResourceTelemetry` — добыча, потребление, продажи, баланс по регионам.
- Ошибки: `CurrencyNotFoundError`, `ExchangeRateUnavailableError`, `ExchangeLimitExceededError`, `ResourceNotFoundError`, `InsufficientCurrencyError`, `AntiFraudFlaggedError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.currencies.rate-updated`, `economy.currencies.exchange-processed`, `economy.resources.gathered`, `economy.resources.depleted`, `economy.resources.data-traded`.
- Inventory-service: операции с ресурсами (`POST /inventory/resources/add`, `POST /inventory/resources/remove`).
- Trading-service: использование валют в торговле (`POST /trading/orders`).
- Economy-events: влияние на курсы/доступность (REST/Kafka).
- Crafting-service: потребление/производство ресурсов (`POST /crafting/resources/consume`).
- Quest-service: награды ресурсами/валютами (`POST /quests/rewards`).
- Faction-service: фракционные курсы и ресурсы (`POST /factions/resources`).
- Analytics-service: отчёты, heatmaps (`POST /analytics/currencies`).
- Anti-fraud-service: проверка крупного обмена (`POST /anti-fraud/currency/check`).
- Notification-service: уведомления об изменениях курсов/доступности.
- Rate limits: обмен ≤ 100 операций/день, data-market операции ≤ 20/день.
- Security: OAuth2 PlayerSession, KYC для крупных обменов, role `economy_admin` для управления курсов/ресурсов.

## Acceptance Criteria
1. Создан файл `api/v1/economy/currencies/currencies-resources.yaml` (≤ 500 строк) с `info.x-microservice` → economy-service.
2. Задокументированы все endpoints и WebSocket потоки, параметры, ответы, примеры, ошибки.
3. Схемы валют, курсов, обменов, ресурсов, источников, данных и телеметрии описаны с обязательными полями и ссылками на общие компоненты.
4. Kafka события и интеграции (inventory, trading, events, crafting, quest, faction, analytics, anti-fraud, notification) задокументированы.
5. Отражены требования к фронтенду, real-time курсам, уведомлениям, TODO по балансу (как примечания).
6. `tasks/config/brain-mapping.yaml` пополнен записью `API-TASK-107`, статус `queued`, приоритет `high`.
7. `.BRAIN/02-gameplay/economy/economy-currencies-resources.md` содержит блок `API Tasks Status`.
8. `tasks/queues/queued.md` дополнен записью.
9. После реализации запустить `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\currencies\`.

## FAQ / Notes
- **Нужно ли поддерживать региональные ограничения?** Включить поля `region`, `regulationLevel`, `exchangeLimits`.
- **Как описать данные Cyberpunk?** Отразить в каталоге ресурсов как отдельные категории `data`, `license`, `contract`.
- **Учитывать ли динамические события?** Да, указать, что economy-events влияет на курсы и доступность; предусмотреть поля для modifiers.

## Change Log
- 2025-11-09 19:55 — Задание создано (API Task Creator Agent)


