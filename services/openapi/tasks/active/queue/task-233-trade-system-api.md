# Task ID: API-TASK-233
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 05:55
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-215, API-TASK-227, API-TASK-230

---

## 📋 Краткое описание

Спроектировать OpenAPI спецификацию P2P trade системы: обмен предметами/валютой, двойное подтверждение, история сделок, антифрод, ограничение предметов.

**Что нужно сделать:** Создать `api/v1/trade/trade-system.yaml`, основываясь на `.BRAIN/05-technical/backend/trade-system.md`.

---

## 🎯 Цель задания

Обеспечить безопасный и прозрачный обмен между игроками, интегрированный с экономикой, инвентарём и модерацией.

**Зачем это нужно:**
- Поддержать прямой обмен предметами, валютой, токенами
- Обеспечить защиту от мошенничества (двойное подтверждение, антиспам)
- Вести историю сделок для аналитики, расследований, налогов
- Интегрировать P2P торговлю с guild/clan экономикой и событиями

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/trade-system.md`
**Версия:** v1.0.0 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Trade session lifecycle (initiate → offer → confirm → finalize)
- Лимиты, ограничения предметов, binding rules
- Currency transfer, taxes, fees
- Trade history, audit, dispute resolution
- Anti-scam механики, proximity checks
- GM/Admin инструменты, журнал расследований

### Дополнительные источники

- `.BRAIN/05-technical/backend/inventory-system/part1-core-system.md`
- `.BRAIN/05-technical/backend/loot-system/part2-advanced-loot.md`
- `.BRAIN/05-technical/backend/economy-system.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/support/support-ticket-system.md`
- `.BRAIN/05-technical/backend/admin/admin-tools-core.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-215-loot-advanced-api.md`
- `API-SWAGGER/tasks/active/queue/task-227-combat-session-api.md`
- `API-SWAGGER/tasks/active/queue/task-230-notification-system-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/trade/trade-system.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/trade/
 ├── trade-system.yaml       ← создать/обновить
 ├── trade-components.yaml
 └── trade-examples.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** trade-service
- **Порт:** 8095
- **API Base Path:** `/api/v1/trade`
- **Зависимости:**
  - auth-service – идентификация игроков
  - inventory-service – проверка и перемещение предметов, lock/unlock
  - economy-service – перевод валют, комиссии
  - notification-service – уведомления о trade запросах, завершении
  - analytics-service – статистика сделок, подозрительные активности
  - moderation-service – антифрод, жалобы, санкции
  - support-service – расследование споров
  - realtime-service – live обновления окна торговли

### Frontend
- **Модуль:** `modules/economy/trade`
- **State Store:** `useTradeStore`
- **State:** `currentTrade`, `offers`, `items`, `currency`, `timer`, `history`
- **UI компоненты:** `TradeWindow`, `TradeOfferPanel`, `TradeConfirmation`, `TradeHistoryList`, `TradeWarningBanner`, `TradeRestrictionsModal`
- **Формы:** `TradeInviteForm`, `TradeItemForm`, `TradeCurrencyForm`, `TradeDisputeForm`
- **Хуки:** `useTradeSession`, `useTradeHistory`, `useTradeValidation`, `useTradeNotifications`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: trade-service (port 8095)
# - API Base: /api/v1/trade
# - Dependencies: auth, inventory, economy, notification, analytics, moderation, support, realtime
# - Frontend Module: modules/economy/trade (useTradeStore)
# - UI: TradeWindow, TradeOfferPanel, TradeConfirmation, TradeHistoryList, TradeWarningBanner, TradeRestrictionsModal
# - Forms: TradeInviteForm, TradeItemForm, TradeCurrencyForm, TradeDisputeForm
# - Hooks: useTradeSession, useTradeHistory, useTradeValidation, useTradeNotifications
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать модели trade session, offers, items, currency, confirmations, history.
2. Реализовать эндпоинты создания торговли, обновления предложений, подтверждения, отмены.
3. Добавить проверку ограничений (binding, soulbound, level requirements).
4. Описать перераспределение предметов/валюты после finalize, обработку COD-like фич.
5. Настроить уведомления, realtime события, таймеры session timeout.
6. Вести журнал сделок, audit trail, dispute workflow.
7. Поддержать GM/Admin endpoints для вмешательства, возвратов, блокировок.
8. Предусмотреть антиспам, rate limits, blacklists.
9. Подготовить примеры, тест-кейсы, чеклист.

---

## 🔀 Endpoints

1. **POST `/api/v1/trade/sessions`** – инициировать trade (target player, context, restrictions).
2. **GET `/api/v1/trade/sessions/{sessionId}`** – текущее состояние сессии, предложения сторон, таймер.
3. **POST `/api/v1/trade/sessions/{sessionId}/items`** – добавить/обновить предмет в предложение.
4. **DELETE `/api/v1/trade/sessions/{sessionId}/items/{itemEntryId}`** – убрать предмет из предложения.
5. **POST `/api/v1/trade/sessions/{sessionId}/currency`** – предложить валюту (amount, currencyType).
6. **POST `/api/v1/trade/sessions/{sessionId}/lock`** – игрок подтверждает своё предложение (lock-in).
7. **POST `/api/v1/trade/sessions/{sessionId}/unlock`** – снять lock для изменений.
8. **POST `/api/v1/trade/sessions/{sessionId}/confirm`** – финальное подтверждение сделки.
9. **POST `/api/v1/trade/sessions/{sessionId}/cancel`** – отменить торговлю (добровольно или авто).
10. **POST `/api/v1/trade/sessions/{sessionId}/pause`** – временно приостановить (disconnect, incident).
11. **POST `/api/v1/trade/sessions/{sessionId}/resume`** – возобновить после паузы.
12. **GET `/api/v1/trade/sessions/{sessionId}/history`** – журнал действий внутри сессии.
13. **GET `/api/v1/trade/history`** – история сделок игрока (фильтры: period, partner, item).
14. **POST `/api/v1/trade/sessions/{sessionId}/dispute`** – открыть спор/жалобу (support workflow).
15. **POST `/api/v1/trade/sessions/{sessionId}/gm/intervene`** – GM вмешательство (freeze, rollback, confiscate).
16. **GET `/api/v1/trade/restrictions`** – текущие ограничения (blacklist, cooldowns, trade bans).
17. **POST `/api/v1/trade/restrictions`** – обновление ограничений (GM/anti-cheat).
18. **GET `/api/v1/trade/stats`** – агрегированные метрики (volume, suspicious trades, taxes).
19. **POST `/api/v1/trade/invitations`** – отправить запрос на торговлю (приглашение).
20. **WS `/api/v1/trade/sessions/{sessionId}/stream`** – события: `session-updated`, `item-added`, `item-removed`, `currency-updated`, `player-locked`, `player-confirmed`, `session-cancelled`, `trade-completed`, `dispute-opened`.

---

## 🧱 Модели данных

- **TradeSession** – `sessionId`, `initiatorId`, `targetId`, `status`, `startedAt`, `expiresAt`, `context` (`WORLD|MARKET|GUILD_HALL`), `location`.
- **TradeOffer** – `offerId`, `playerId`, `items[]`, `currency`, `locked`, `confirmed`, `updatedAt`.
- **TradeItem** – `itemEntryId`, `itemId`, `name`, `quantity`, `rarity`, `bindType`, `tradable`, `metadata`.
- **TradeCurrency** – `currencyType`, `amount`, `limit`, `fee`, `tax`.
- **TradeHistoryEntry** – `entryId`, `sessionId`, `playerId`, `action`, `payload`, `timestamp`.
- **TradeRestriction** – `playerId`, `type` (`BAN|LIMIT|COOLDOWN`), `reason`, `expiresAt`.
- **TradeDispute** – `disputeId`, `sessionId`, `reporterId`, `status`, `resolution`, `notes`.
- **RealtimeEventPayload** – `sessionUpdated`, `itemAdded`, `itemRemoved`, `currencyUpdated`, `playerLocked`, `playerConfirmed`, `sessionCancelled`, `tradeCompleted`, `disputeOpened`.
- **Error Schema (`TradeError`)** – codes (`TRADE_NOT_FOUND`, `TRADE_LOCKED`, `ITEM_NOT_TRADABLE`, `CURRENCY_LIMIT`, `CONFIRMATION_PENDING`, `RESTRICTION_ACTIVE`, `SESSION_TIMEOUT`, `DISPUTE_ALREADY_OPEN`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth`; GM/Admin операции требуют отдельного scope (`TradeGM`).
- Инвентарь: предметы блокируются на время trade; при отмене/отклонении снимается блокировка.
- Ограничения: лимиты на количество сделок в день, фильтрация подозрительных шаблонов.
- Безопасность: proximity check, anti-spam, escrow для валюты.
- Audit: все действия логируются; история доступна support и moderation.
- Localization: уведомления и описания через notification-service и templates.
- Scalability: sharding trade sessions по регионам, хранить state в Redis + persistent store.

---

## 🧪 Примеры

- Торговля предметом легендарного качества с двойным подтверждением и налогом.
- Отправка запроса на торговлю через friend list и завершение сделки с валютой.
- Открытие спора после отмены сделки; связь с support.
- GM вмешательство: заморозка сделки и возврат предметов.
- WebSocket обновление `item-added` с синхронизацией UI.

---

## 🔗 Связности и зависимости

- Интегрируется с inventory, economy, loot, notification, analytics, support, moderation.
- Используется UI `TradeWindow`, push/notification об обновлениях.
- События влияют на achievements, quest objectives (trade tasks), economy dashboards.

---

## ✅ Критерии приемки

1. `trade-system.yaml` описывает полный цикл торговли, события и ошибки.
2. Модели покрывают предметы, валюту, историю, ограничения.
3. Прописаны ограничения, anti-fraud, audit, GM операции.
4. Добавлены примеры, тест-кейсы, выполнен чеклист.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, UI модуль, зависимости
- [ ] Эндпоинты и события покрывают все сценарии торговли
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Есть ли поддержка кросс-серверной торговли?**
**A:** В первой версии нет; предусмотреть поле `shard` и описать ограничения. Возможное расширение.

**Q:** Как обрабатывать налоги и комиссии?**
**A:** Экономика задаёт правила; API должен позволять указывать `fee/tax` и логировать списания. Расчёт происходит в economy-service.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

