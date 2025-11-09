# API Generation Task Template

> Обязательно сверяйся с `GLOBAL-RULES.md`, `API-SWAGGER/CORE.md`, `tasks/config/task-creation-guide.md` и `tasks/config/checklist.md`.  
> Размер задания ≤ 500 строк. При превышении создавай продолжения (`task-XXX-description_0001.md`).

## Metadata
- **Task ID:** API-TASK-111  
- **Type:** API Generation  
- **Priority:** high  
- **Status:** queued  
- **Created:** 2025-11-09 20:35  
- **Author:** API Task Creator Agent  
- **Dependencies:** API-TASK-100 (inventory-core), API-TASK-109 (auction-mechanics), API-TASK-110 (auction-operations)  

## Summary
Сформировать спецификацию `api/v1/economy/trade/trade-system.yaml`, описывающую P2P обмен: инициирование трейда, управление офферами, двойное подтверждение, антифрод и интеграции с inventory/world/session сервисами.

## Source Documents
| Поле | Значение |
| --- | --- |
| Repository | `.BRAIN` |
| Path | `.BRAIN/05-technical/backend/trade-system.md` |
| Version | v1.0.0 |
| Status | approved |
| API readiness | ready (2025-11-09 01:30) |

**Key points:** trade_sessions/trade_history схемы; 5-минутный таймаут; distance check (≤10 м); двойное подтверждение; блокировка предметов; запрет на bound items; события `trade:started/completed/cancelled`; интеграция с session/location/inventory/payment services; антифрод.  
**Related docs:** `.BRAIN/02-gameplay/economy/economy-contracts.md`, `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`, `.BRAIN/05-technical/backend/payment-wallets.md`, `.BRAIN/05-technical/backend/anti-fraud-service.md`, `.BRAIN/05-technical/backend/session-management-system.md`.

## Target Architecture (⚠️ Обязательно)
- **Microservice:** economy-service  
- **Port:** 8085  
- **Domain:** economy/trade  
- **API directory:** `api/v1/economy/trade/trade-system.yaml`  
- **base-path:** `/api/v1/economy/trade`  
- **Java package:** `com.necpgame.economy`  
- **Frontend module:** `modules/economy/trade`  
- **Shared UI/Form components:** `@shared/ui/TradeWindow`, `@shared/ui/TradeOfferSummary`, `@shared/ui/TradeTimer`, `@shared/ui/TradeStatusBadge`, `@shared/forms/TradeInviteForm`, `@shared/forms/TradeOfferForm`, `@shared/forms/TradeConfirmForm`, `@shared/layouts/GameLayout`, `@shared/state/useEconomyStore`.

> Все значения должны соответствовать таблице микросервисов в `GLOBAL-RULES.md`.

## Scope of Work
1. Описать REST endpoints: инициирование, добавление/удаление предметов и золота, подтверждение, отмена, завершение, история, антифрод.
2. Смоделировать `TradeSession`, `TradeOffer`, `TradeItem`, `TradeInitiateRequest/Response`, `TradeOfferUpdate`, `TradeConfirmRequest`, `TradeHistoryEntry`.
3. Учесть проверки: дистанция, онлайн статус, активные трейды, bound items, quantity, timeout, двустороннее подтверждение.
4. Задокументировать интеграции с session/location/inventory/payment/anti-fraud/notification/event bus.
5. Добавить UI/UX требования: окно трейда, уведомления, авто-обновления через WebSocket, таймеры, история.

## Endpoints
- `POST /sessions` — инициировать трейд (проверки, создание `trade_session`, отправка приглашения).
- `POST /sessions/{sessionId}/offers/items` — добавить/удалить предмет в оффер (валидаторы, reset confirmation).
- `POST /sessions/{sessionId}/offers/gold` — добавить золото.
- `POST /sessions/{sessionId}/confirm` — подтвердить оффер (lock → execute, если оба подтвердили).
- `POST /sessions/{sessionId}/cancel` — отменить трейд (указать причину).
- `GET /sessions/{sessionId}` — текущее состояние (offers, confirmed, locked, time left).
- `GET /sessions/active` — активные трейды игрока.
- `GET /history` — история трейдов (paginate, фильтр по дате/участнику).
- `POST /sessions/{sessionId}/heartbeat` — продлить сессию/проверить актуальность (при модальных окнах).
- WebSocket `ws://.../streams/trade/{sessionId}` — обновления офферов, подтверждений, таймера.
- Event hook `POST /sessions/expire` — cron для истекших сделок.

## Data Models
- `TradeSession` — `id`, `initiatorCharacterId`, `recipientCharacterId`, `initiatorOffer`, `recipientOffer`, `initiatorConfirmed`, `recipientConfirmed`, `initiatorLocked`, `recipientLocked`, `status`, `zoneId`, `createdAt`, `expiresAt`, `completedAt`, `completionReason`.
- `TradeOffer` — `items[]`, `gold`, `locked`.
- `TradeItem` — `itemId`, `quantity`, `metadata`.
- `TradeInitiateRequest` — `initiatorCharacterId`, `recipientCharacterId`.
- `TradeInitiateResponse` — `tradeSessionId`, `expiresAt`, `message`.
- `TradeOfferUpdateRequest` — `itemId`, `quantity`, `action` (add/remove), `side`.
- `TradeConfirmRequest` — `characterId`.
- `TradeCancelRequest` — `characterId`, `reason`.
- `TradeHistoryEntry` — `tradeSessionId`, `characterA`, `characterB`, `characterAGave`, `characterBGave`, `zoneId`, `tradedAt`.
- `TradeEventNotification` — payload для WS/notifications.
- Ошибки: `TradeAlreadyActiveError`, `TradePartnerOfflineError`, `TradeDistanceTooFarError`, `TradeLockedError`, `TradeItemNotAllowedError`, `TradeGoldInsufficientError`, `TradeTimeoutError`.
- Использовать `$ref` на `api/v1/shared/common/responses.yaml`, `security.yaml`.

## Integrations & Events
- Session-service: `isOnline`, `session:ended`.
- World/location service: `getDistance`, зоны.
- Inventory-service: `lockItem`, `unlockItem`, `transferItem`.
- Payment-service: `reserveGold`, `releaseGold`, `transferGold`.
- Anti-fraud-service: `trade/check`.
- Notification-service: `trade.requested`, `trade.updated`, `trade.completed`, `trade.cancelled`.
- Event bus: `trade:started`, `trade:completed`, `trade:cancelled`.
- Telemetry/analytics: `trade.sessions.count`, `trade.items.volume`, `trade.gold.volume`.
- Rate limits: 1 активная сделка на игрока, 5 минут таймер, ограничение на размер золота/предметов.
- Security: OAuth2 PlayerSession, проверка принадлежности к trade, circuit breaker для inventory.

## Acceptance Criteria
1. Файл `api/v1/economy/trade/trade-system.yaml` создан, ≤ 500 строк, `info.x-microservice` настроен на economy-service.
2. Все endpoints задокументированы с запросами, ответами, валидациями, ошибками и примерами.
3. Модели сессий, офферов, предметов, истории и уведомлений описаны с обязательными полями.
4. Интеграции (session, location, inventory, payment, anti-fraud, notification, event bus) задокументированы.
5. UI требования и WebSocket поток описаны, учтены таймеры, уведомления, статус.
6. `tasks/config/brain-mapping.yaml` содержит запись `API-TASK-111`, статус `queued`, приоритет `high`.
7. `tasks/queues/queued.md` обновлен записью.
8. `.BRAIN/05-technical/backend/trade-system.md` дополнен блоком статуса (выполнено).
9. После реализации запускается `pwsh -NoProfile -File ..\scripts\validate-swagger.ps1 -ApiDirectory API-SWAGGER\api\v1\economy\trade\`.

## FAQ / Notes
- **Кооперативные trade?** Файл описывает 1-на-1; расширение на group trade вынести как отдельный Task.
- **Автобаланс/антифрод?** Указать поля и endpoints для проверок; детали значений балансируются позже.
- **Cross-region trade?** Указать, что distance check и zoneId обязательны; future updates могут расширить.

## Change Log
- 2025-11-09 20:35 — Задание создано (API Task Creator Agent)


