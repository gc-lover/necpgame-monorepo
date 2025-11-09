# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-105  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 19:27  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-102 (economy-contracts), API-TASK-103 (economy-analytics), API-TASK-104 (economy-events)  

## Summary
Подготовить спецификацию `api/v1/economy/investments/investments.yaml`, описывающую каталог инвестиционных продуктов, управление позициями, портфели, риск-контроль, дивиденды и интеграции с рынками, событиями и аналитикой.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/economy-investments.md` |
| Version | v1.1.0 |
| Status | approved |
| API readiness | ready (2025-11-09 03:25) |

**Key points:** типы инвестиций (акции, облигации, недвижимость, производственные цепочки, товары), portfolio management, risk levels, margin control, KYC gating, analytics (MPT, VaR), интеграции с stock-exchange, economy-events, housing, guild-system, analytics-service.  
**Related docs:** `.BRAIN/02-gameplay/economy/stock-exchange/`, `.BRAIN/02-gameplay/economy/economy-analytics.md`, `.BRAIN/02-gameplay/economy/economy-events.md`, `.BRAIN/05-technical/backend/pricing-engine.md`, `.BRAIN/05-technical/backend/anti-fraud-service.md`, `.BRAIN/05-technical/backend/notification-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/investments  
- **API directory:** `api/v1/economy/investments/investments.yaml`  
- **base-path:** `/api/v1/economy/investments`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/investments`  
- **Shared UI/Form components:** `@shared/ui/InvestmentCatalog`, `@shared/ui/PortfolioDashboard`, `@shared/ui/RiskGauge`, `@shared/ui/PositionTable`, `@shared/forms/InvestmentPurchaseForm`, `@shared/forms/PortfolioRebalanceForm`, `@shared/forms/MarginCallResolutionForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для каталога продуктов, операций с позициями, портфеля, отчётов дивидендов/купонов, рекомендаций и риск-контроля.
2. Смоделировать схемы `InvestmentProduct`, `InvestmentPosition`, `InvestmentTransaction`, `PortfolioSnapshot`, `RiskProfile`, `RebalanceSuggestion`, `MarginCall`, `DividendReport`.
3. Зафиксировать lifecycle (discovery → subscribed → active → rebalancing → exit) и ограничения (KYC, margin, suitability checks).
4. Документировать Kafka события `economy.investments.*`, интеграции с stock-exchange, economy-events, housing-system, guild-system, analytics-service, notification-service.
5. Добавить требования к фронтенду, аналитике (MPT, VaR), real-time обновлениям и уведомлениям о margin call и выплатах.

## Endpoints
- `GET /products` — каталог продуктов (фильтры: risk, sector, region, type).
- `GET /products/{productId}` — подробности (доходность, прогноз, риски, интеграции).
- `POST /positions` — открытие позиции (покупка доли/актива, проверка KYC/risk).
- `GET /positions/{positionId}` — статус позиции, начисленные выплаты, leverage.
- `POST /positions/{positionId}/close` — закрыть позицию, расчёт прибыли/убытка, налоги.
- `GET /positions/{positionId}/transactions` — история операций (BUY/SELL/DIVIDEND/INTEREST/RENTAL).
- `GET /portfolio` — общие метрики портфеля (total value, cash, risk score, diversification).
- `POST /portfolio/rebalance` — применить рекомендованную пересборку (список target allocations).
- `GET /reports/dividends` — отчёт по дивидендам/купонам (период, налоговые удержания).
- `GET /alerts/margin` — активные margin calls.
- `POST /alerts/margin/{positionId}/resolve` — пополнить залог/закрыть позицию.
- `GET /recommendations` — рекомендации (MPT, VaR, события влияют).
- `POST /watchlist` — подписка на продукты/сектора.
- WebSocket `ws://.../streams/portfolio` — real-time обновление портфеля и margin предупреждений.

## Data Models
- `InvestmentProduct` — `id`, `type`, `name`, `riskLevel`, `baseReturnPercent`, `currency`, `metadata`, `integrationRefs`.
- `InvestmentPosition` — `id`, `productId`, `playerId`, `purchaseAmount`, `quantity`, `avgPrice`, `leverage`, `openedAt`, `status`, `marginThreshold`.
- `InvestmentTransaction` — `id`, `positionId`, `type`, `amount`, `currency`, `occurredAt`, `metadata`.
- `PortfolioSnapshot` — `totalValue`, `cashBalance`, `riskScore`, `diversificationIndex`, `allocation`.
- `RiskProfile` — `riskTolerance`, `investorType`, `kycStatus`.
- `RebalanceSuggestion` — `targetAllocations`, `rationale`, `expectedReturn`, `riskChange`.
- `MarginCall` — `positionId`, `requiredAmount`, `deadline`, `status`.
- `DividendReport` — `period`, `totalDividends`, `taxWithheld`, `netAmount`.
- `Recommendation` — `productId`, `confidence`, `expectedReturn`, `risk`.
- Ошибки: `KycRequiredError`, `RiskSuitabilityError`, `MarginExceededError`, `ProductUnavailableError`, `RebalanceConflictError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.investments.product-updated`, `economy.investments.position-opened`, `economy.investments.position-closed`, `economy.investments.dividend-paid`, `economy.investments.portfolio-rebalanced`, `economy.investments.margin-call`.
- Stock exchange API: использование `api/v1/economy/stock-exchange/*` для цен/дивидендов.
- Economy-events: получение коэффициентов влияния на прогнозы (REST или Kafka).
- Housing-system: реестр недвижимости (`GET /housing/assets`).
- Guild-system: shared positions, фонды (`POST /guilds/investments`).
- Analytics-service: отчёты, рекомендации (MPT, VaR) через `POST /analytics/investments`.
- Notification-service: push/email для dividend, margin, rebalance (`POST /notifications/investments`).
- Anti-fraud: проверка крупных транзакций (`POST /anti-fraud/investments/check`).
- Rate limits: открытие позиций ≤ 50/день, rebalance ≤ 10/день, margin API доступно только при активных вызовах.
- Security: OAuth2 PlayerSession, роли `player`, `vip`, `economy_admin`; KYC gating.

## Acceptance Criteria
1. Создан файл `api/v1/economy/investments/investments.yaml` (≤ 500 строк) с `info.x-microservice` для economy-service.
2. Все endpoints описаны с параметрами, запросами/ответами, примерами, ошибками (включая risk checks, KYC, margin).
3. Модели продуктов, позиций, транзакций, портфеля, риск-профиля, margin call и отчётов представлены с ограничениями и ссылками на общие компоненты.
4. Kafka события и интеграции (stock, events, housing, guild, analytics, notification) задокументированы; указаны payload и ретраи.
5. Отражены требования к аналитике (MPT, VaR), уведомлениям и real-time потокам, а также зависимости на предыдущие задачи.
6. Фронтенд секция описывает UI компоненты, формы, Orval клиент `@api/economy/investments`, state hook `useEconomyStore`.
7. `tasks/config/brain-mapping.yaml` пополнен записью `API-TASK-105`, статус `queued`, приоритет `high`.
8. `.BRAIN/02-gameplay/economy/economy-investments.md` обновлён секцией `API Tasks Status`.
9. `tasks/queues/queued.md` дополнен записью.
10. После реализации запуск `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\investments\`.

## FAQ / Notes
- **Нужно ли выделять недвижимости отдельно?** Можно в рамках одного файла, пока не превышаем лимит; при расширении возможно добавить `investments-real-estate.yaml`.
- **Как учитывать события?** Указать, что economy-events влияет на прогнозы и рекомендации через coefficients.
- **Что с guild фондами?** Включить поля `guildId`, `sharePercent`; реализация shared фондов описана в отдельных документах, но API должен поддержать.

## Change Log
- 2025-11-09 19:27 — Задание создано (API Task Creator Agent)


