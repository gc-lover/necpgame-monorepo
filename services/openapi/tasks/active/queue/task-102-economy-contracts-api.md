# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-102  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 18:40  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-100 (inventory-core)  

## Summary
Подготовить спецификацию `api/v1/economy/contracts/contracts.yaml`, описывающую систему контрактов между игроками: создание, переговоры, эскроу, залоги, исполнение, споры и интеграции с инвентарём, кошельками, логистикой и репутацией.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/02-gameplay/economy/economy-contracts.md` |
| Version | v1.1.0 |
| Status | approved |
| API readiness | ready (2025-11-09 03:22) |

**Key points:** типы контрактов (item exchange, delivery, crafting, service); state machine (`DRAFT`→`NEGOTIATION`→`ESCROW_PENDING`→`ACTIVE`→`COMPLETED`/`DISPUTED`); escrow и collateral механики; арбитраж и таймлайны; лимиты на контракты/споры; интеграции с `inventory`, `wallet`, `logistics`, `reputation`, `notification`, антифрод.  
**Related docs:** `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`, `.BRAIN/05-technical/backend/player-character-mgmt/character-management.md`, `.BRAIN/05-technical/backend/economy-wallets.md`, `.BRAIN/02-gameplay/economy/auction-house/auction-mechanics.md`, `.BRAIN/05-technical/backend/notification-service.md`, `.BRAIN/05-technical/backend/anti-fraud-service.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/contracts  
- **API directory:** `api/v1/economy/contracts/contracts.yaml`  
- **base-path:** `/api/v1/economy/contracts`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/contracts`  
- **Shared UI/Form components:** `@shared/ui/ContractCard`, `@shared/ui/ContractTimeline`, `@shared/ui/EscrowStatus`, `@shared/forms/ContractCreateForm`, `@shared/forms/ContractProposalForm`, `@shared/forms/DisputeForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints для lifecycle контрактов: создание, предложения, принятие, эскроу, выполнение, споры, таймлайн, отмены.
2. Смоделировать схемы данных (`Contract`, `ContractTerms`, `EscrowDeposit`, `Collateral`, `Proposal`, `Deliverable`, `DisputeCase`, `TimelineEvent`) с валидациями, лимитами и ссылками на инвентарь/кошельки.
3. Определить механизмы уведомлений, rate limits, eligibility проверки, антифрод параметры и капы залога.
4. Зафиксировать Kafka события `economy.contracts.*`, их payload, потребителей и требования к идемпотентности.
5. Описать интеграции с `inventory`, `wallet`, `logistics`, `reputation`, `notification`, `analytics`, `anti-fraud` сервисами и требования фронтенда (UI/Orval).

## Endpoints
- `POST /` — создать контракт (тип, terms, collateral, invited contractor, escrow requirements).
- `GET /{contractId}` — получить подробности, state machine, escrow balances, историю изменений.
- `POST /{contractId}/proposals` — отправить предложение изменений в ходе переговоров.
- `POST /{contractId}/accept` — принять текущие условия с двухфакторной валидацией.
- `POST /{contractId}/escrow/deposit` — внести эскроу/залог (валюта/предметы) с привязкой к wallet/inventory.
- `POST /{contractId}/escrow/release` — инициировать освобождение эскроу (по завершению или решению арбитража).
- `POST /{contractId}/deliverables` — загрузить подтверждение исполнения (ссылки на локации, инвентарь, логистику).
- `POST /{contractId}/complete` — подтвердить завершение, выставить рейтинг/отзыв.
- `POST /{contractId}/cancel` — отменить по взаимному согласию до `ACTIVE`.
- `POST /{contractId}/dispute` — открыть спор, приложить доказательства.
- `POST /{contractId}/dispute/resolve` — решение арбитража (AI moderator), распределение эскроу.
- `GET /{contractId}/timeline` — аудит действий и документов (пагинация, фильтры).
- `GET /accounts/{accountId}/contracts` — список контрактов аккаунта с фильтрами (status, type, role).
- `GET /analytics/summary` — агрегаты по контрактам (кол-во, успешность, суммы, активные споры).

## Data Models
- `Contract` — основные поля (id, type, creator, contractor, status, deadline, collateral, escrow, negotiation snapshot).
- `ContractTerms` — детализация по типам (itemExchange, delivery, crafting, service) с вложенными структурами.
- `EscrowDeposit` — вид залога (currency, items), суммы, источник wallet, статус блокировки.
- `Collateral` — размер, limit, возврат при успехе/штраф при провале.
- `Proposal` — версии, дельта условий, сообщения, подписи.
- `Deliverable` — тип (inventoryRef, logisticsManifest, proofLink), контрольные параметры.
- `DisputeCase` — причина, evidence list, статус (`OPEN`, `IN_REVIEW`, `RESOLVED`), решение.
- `TimelineEvent` — audit trail entries (timestamp, actor, action, payload, attachments).
- `ContractFilter`, `ContractSummary`, `AnalyticsSummary`.
- Ошибки: `EligibilityFailedError`, `EscrowMissingError`, `CollateralExceededError`, `NegotiationExpiredError`, `DisputeLimitReachedError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `pagination.yaml`, `security.yaml`.

## Integrations & Events
- Kafka topics: `economy.contracts.created`, `economy.contracts.proposal-submitted`, `economy.contracts.escrow-locked`, `economy.contracts.deliverable-submitted`, `economy.contracts.completed`, `economy.contracts.cancelled`, `economy.contracts.dispute-opened`, `economy.contracts.dispute-resolved`. Payload включает contractId, type, status, participants, escrow summary.
- REST/gRPC зависимости:
  - `inventory-service` — резерв/освобождение предметов (`POST /inventory/reserve`, `POST /inventory/release`).
  - `wallet-service` — блокировка и возврат валюты (`POST /wallets/hold`, `POST /wallets/release`).
  - `logistics-service` — оформление доставки (`POST /logistics/orders`).
  - `reputation-service` — обновление рейтинга (`POST /reputation/contracts`).
  - `notification-service` — push/email (`POST /notifications/contracts`).
  - `anti-fraud-service` — проверка риска (`POST /anti-fraud/contracts/check`).
  - `analytics-service` — агрегация экономических данных.
- WebSocket канал `contracts.updates` для реального времени.
- Метрики: `AverageContractValue`, `ContractCompletionRate`, `DisputeRate`, `EscrowLockedValue`, `FraudAlerts`.

## Acceptance Criteria
1. Спецификация `api/v1/economy/contracts/contracts.yaml` создана, ≤ 500 строк, с корректным `info.x-microservice`.
2. Все перечисленные endpoints описаны с параметрами, телами запросов/ответов, кодами ошибок и примерами.
3. Модели контрактов, escrow, collateral, предложений, споров и таймлайнов оформлены с обязательными полями и ограничениями.
4. Kafka события, интеграции, rate limits и eligibility правила задокументированы; указаны продюсеры/консьюмеры.
5. Прописаны требования фронтенда: модуль, UI/формы, Orval клиент `@api/economy/contracts`, обработка real-time обновлений.
6. Указана зависимость на `API-TASK-100` (inventory-core) и интеграция с wallet/logistics/reputation сервисами.
7. Обновлён `tasks/config/brain-mapping.yaml` (API-TASK-102, статус `queued`, приоритет `high`).
8. `.BRAIN/02-gameplay/economy/economy-contracts.md` содержит секцию `API Tasks Status`.
9. `tasks/queues/queued.md` обновлён новой записью.
10. После реализации спецификации необходимо запустить `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\contracts\`.

## FAQ / Notes
- **Можно ли разделить контракты по типам?** Если файл приближается к лимиту, после создания core можно выделить delivery/service в отдельные задания; базовый файл должен содержать общую схему.
- **Как описывать эскроу предметов?** Использовать ссылки на inventory items (UUID/slot) и указать, что предметы замораживаются до завершения или спора.
- **Что с арбитражом?** Описать роли AI moderator/GM, тайминги решения (3–5 дней), поля `decision`, `payout`.
- **Нужны ли ставки курьера?** Delivery контракт включает поля логистики и SLA; детали маршрутов/трекера можно вынести позже.

## Change Log
- 2025-11-09 18:40 — Задание создано (API Task Creator Agent)


