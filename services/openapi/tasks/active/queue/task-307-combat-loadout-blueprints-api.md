# Task ID: API-TASK-307
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:42
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-301], [API-TASK-302], [API-TASK-304], [API-TASK-149], [API-TASK-242]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию подсистемы обмена боевыми лодаутами (Loadout Blueprint Exchange) для `economy-service`: генерация, торговля и управление blueprint-токенами, лицензиями и авторскими правами.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для выпуска, передачи, импорта и монетизации лодаутов между персонажами/аккаунтами с учётом ограничений и комиссий.

---

## 🎯 Цель задания

Обеспечить контролируемый рынок обмена лодаутами, позволяя игрокам делиться конфигурациями, монетизировать их и при этом соблюдать ограничения фракций, брендов и лицензий.

**Зачем это нужно:**
- Формализовать экспорт/импорт через `blueprintToken`, контролировать права и количество активаций.
- Управлять публичным рынком чертежей (экономика, комиссии, аудиты).
- Синхронизировать обмен с валидациями ролей, мастерства и доступности предметов.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Раздел «Обмен лодаутами между персонажами» — `blueprintToken`, привязка к `accountId`, повторная валидация ролей/мастерства, замены несовместимых элементов.
- Раздел «Комплекты и переиспользование» — фракционные/корпоративные ограничения, команды, рынок.
- Раздел «Политики фракционных комплектов» — лимиты `maxFactionKits`, `factionPermitLevel`, аудит.
- Раздел «Управление недоступными предметами» — проверка наличия предметов при импорте.
- Раздел «Метрики и телеметрия» — слежение за передачами, деградациями, предупреждениями.

### Дополнительные источники

- `.BRAIN/02-gameplay/economy/equipment-matrix.md` — бренды, бонусы, стоимость.
- `.BRAIN/02-gameplay/economy/blueprint-market.md` — правила рынка чертежей.
- `.BRAIN/02-gameplay/economy/trade-regulations.md` — комиссии, налоги, лицензии.
- `.BRAIN/02-gameplay/combat/combat-roles-detailed.md`, `progression-skills-mapping.md` — совместимость по ролям и мастерству.
- `.BRAIN/_05-technical/backend/notification-system.md` — уведомления об обменах.

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-301-combat-loadout-kits-api.md`
- `API-SWAGGER/tasks/active/queue/task-304-combat-loadout-availability-api.md`
- `API-SWAGGER/tasks/active/queue/task-149-currency-exchange-api.md`
- `API-SWAGGER/tasks/active/queue/task-242-market-stabilizer-api.md`
- `API-SWAGGER/tasks/active/queue/task-256-stock-exchange-dividends-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/gameplay/economy/loadout-blueprints.yaml`  
**Формат:** OpenAPI 3.0.3 (при необходимости вынести схемы/события)

```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── economy/
                ├── loadout-blueprints.yaml           ← создать
                ├── loadout-blueprints-components.yaml
                └── loadout-blueprints-events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** economy-service
- **Порт:** 8085
- **API Base:** `/api/v1/gameplay/economy/loadout-blueprints*`
- **Интеграции:** gameplay-service (валидация лодаутов), social-service (гильдейский доступ), notification-service (события обмена), analytics-service (метрики рынка), auth-service (scopes), billing-service (комиссии, выплаты).
- **Очереди/события:** Kafka/RabbitMQ `economy.blueprints.*`, подписки на `combat.loadouts.availability-warning` и `loadout.maintenance.patch-applied`.

### Frontend
- **Модуль:** `modules/economy/loadout-blueprints`
- **State Store:** `useLoadoutBlueprintStore`
- **UI компоненты:** `BlueprintMarketplace`, `BlueprintTokenBanner`, `LicenseStatusBadge`, `BlueprintPreviewModal`, `BlueprintHistoryTimeline`, `RoyaltyPayoutCard`
- **Формы:** `BlueprintMintForm`, `BlueprintListingForm`, `BlueprintRedeemForm`, `DisputeResolutionForm`
- **Хуки:** `useBlueprintValidation`, `useBlueprintMarketplace`, `useBlueprintRoyalties`, `useBlueprintNotifications`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: economy-service (port 8085)
# - API Base: /api/v1/gameplay/economy/loadout-blueprints*
# - Dependencies: gameplay, social, notification, analytics, billing, auth
# - Events: economy.blueprints.*, consume combat.loadouts.availability-warning / maintenance.patch-applied
# - Frontend Module: modules/economy/loadout-blueprints (useLoadoutBlueprintStore)
# - UI: BlueprintMarketplace, BlueprintTokenBanner, LicenseStatusBadge, BlueprintPreviewModal
# - Forms: BlueprintMintForm, BlueprintListingForm, BlueprintRedeemForm, DisputeResolutionForm
# - Hooks: useBlueprintValidation, useBlueprintMarketplace, useBlueprintRoyalties, useBlueprintNotifications
```

---

## ✅ Что нужно сделать (детальный план)

1. Проанализировать процесс экспорта/импорта из документа `.BRAIN`: генерация токена, валидация ролей, подписок, fallback.
2. Спроектировать REST endpoints для выпуска, перечисления, покупки, передачи, отзыва blueprint-токенов и управления лицензиями/комиссиями.
3. Описать схемы `LoadoutBlueprint`, `BlueprintToken`, `BlueprintLicense`, `BlueprintListing`, `TradeOffer`, `BlueprintRedemption`, `BlueprintAudit`, `RoyaltyPayout`, `DisputeTicket`.
4. Добавить endpoints для marketplace (листинги, поиск, фильтры), действий в гильдии, отчётности и доверенного обмена между аккаунтами.
5. Определить события (`blueprint.minted`, `blueprint.listed`, `blueprint.sold`, `blueprint.redeemed`, `blueprint.revoked`, `blueprint.dispute-opened`, `blueprint.dispute-resolved`) с payload и гарантиями.
6. Прописать безопасность, ограничения (лимиты активаций, фракционные политики, лицензии), проверки availability и progression.
7. Подготовить примеры запросов/ответов/событий (mint, list, buy, redeem, revoke, dispute).
8. Интегрировать с analytics/telemetry (сбор метрик), notification-service и maintenance (отзыв токенов после патча).
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `POST /api/v1/gameplay/economy/loadout-blueprints` — выпуск blueprint (указание лодаута, прав, лимитов, цены).
2. `GET /api/v1/gameplay/economy/loadout-blueprints/{blueprintId}` — просмотр информации (описание, лицензии, продажи, ограничения).
3. `POST /api/v1/gameplay/economy/loadout-blueprints/{blueprintId}/tokens` — генерация токена для конкретного получателя/скоупа.
4. `POST /api/v1/gameplay/economy/loadout-blueprints/tokens/{tokenId}/redeem` — импорт лодаута по токену (валидация ролей, предметов, мастерства).
5. `POST /api/v1/gameplay/economy/loadout-blueprints/{blueprintId}/listings` — размещение на рынке (цены, комиссии, количества).
6. `GET /api/v1/gameplay/economy/loadout-blueprints/listings` — поиск и фильтрация листингов (роль, фракция, рейтинг).
7. `POST /api/v1/gameplay/economy/loadout-blueprints/listings/{listingId}/buy` — покупка/подписка (учёт комиссий, платежей).
8. `DELETE /api/v1/gameplay/economy/loadout-blueprints/listings/{listingId}` — отзыв листинга.
9. `POST /api/v1/gameplay/economy/loadout-blueprints/{blueprintId}/royalties/payout` — инициировать выплату роялти автору.
10. `GET /api/v1/gameplay/economy/loadout-blueprints/{blueprintId}/audit` — журнал операций, compliance.
11. `POST /api/v1/gameplay/economy/loadout-blueprints/{blueprintId}/revoke` — отзыв blueprint (например, после баланса/нарушений).
12. `POST /api/v1/gameplay/economy/loadout-blueprints/disputes` — открыть спор (нарушение лицензии, мошенничество).
13. `POST /api/v1/gameplay/economy/loadout-blueprints/disputes/{disputeId}/resolve` — решение спора (refund, ban, компенсация).
14. `GET /api/v1/gameplay/economy/loadout-blueprints/metrics` — статистика рынка (продажи, популярность, нарушения).

Все мутационные операции требуют `Authorization`, `Idempotency-Key`, `X-Audit-Id`; ответы используют общие `$ref`.

---

## 🧱 Модели данных

- **LoadoutBlueprint** — `blueprintId`, `ownerId`, `loadoutId`, `roleTags`, `allowedFactions`, `maxActivations`, `licenseType`, `royaltyRate`, `status`, `createdAt`, `updatedAt`.
- **BlueprintToken** — `tokenId`, `blueprintId`, `issuedBy`, `issuedTo`, `scope` (`ACCOUNT`, `SQUAD`, `CLAN`, `PUBLIC`), `activationLimit`, `expiresAt`, `status`.
- **BlueprintLicense** — `licenseId`, `terms`, `allowedUses`, `prohibitedUses`, `expiry`, `revocationConditions`.
- **BlueprintListing** — `listingId`, `blueprintId`, `price`, `currency`, `quantity`, `remaining`, `marketType` (`PUBLIC`, `GUILD`, `PREMIUM`), `visibility`.
- **TradeOffer** — `offerId`, `buyerId`, `sellerId`, `price`, `status`, `createdAt`, `expiresAt`.
- **BlueprintRedemption** — `redemptionId`, `tokenId`, `characterId`, `validationReport`, `fallbackApplied`, `timestamp`.
- **BlueprintAudit** — `auditId`, `action`, `performedBy`, `context`, `result`, `timestamp`.
- **RoyaltyPayout** — `payoutId`, `blueprintId`, `amount`, `currency`, `paidTo`, `paidAt`, `txId`.
- **DisputeTicket** — `disputeId`, `blueprintId`, `complainant`, `reason`, `evidence`, `status`, `resolution`.
- **BlueprintMetric** — `time`, `sales`, `royalties`, `activationRate`, `refundRate`, `violationRate`.
- **Async Events** — payloads для `blueprint.minted`, `blueprint.listed`, `blueprint.sold`, `blueprint.redeemed`, `blueprint.revoked`, `blueprint.royalty-paid`, `blueprint.dispute-opened`, `blueprint.dispute-resolved`.

---

## 🧭 Принципы и правила

- Соблюдать OpenAPI 3.0.3 и ограничения 400 строк (выносить схемы/события).
- Использовать `$ref` на общие компоненты и на контракты loadouts/kits/availability/profiles.
- Учитывать комиссии, налоги, роялти — документировать расчёты и порядок выплат.
- Прописать всевозможные проверки: роли, мастерство, доступность предметов, фракционные лимиты, лицензии.
- Обеспечить безопасность: scopes `blueprints:read`, `blueprints:write`, `blueprints:market`, `blueprints:admin`.
- Публиковать события в `economy.blueprints.*`, подписываться на `loadout.maintenance.patch-applied` для отзывов.
- Обрабатывать ошибки (`409`, `410`, `412`, `423`, `451`), предусмотреть idempotency, аудит, rate limits.

---

## ✅ Критерии приемки

1. Все 14 эндпоинтов описаны с параметрами, схемами и примерами.
2. Процессы mint/list/buy/redeem/revoke документированы с проверками и событиями.
3. Лицензии и ограничения (фракции, активации, сроки) описаны в схемах и правилах.
4. Рынок учитывает комиссии, роялти, налоги; описаны расчёты и выплаты.
5. Интеграция с availability/maintenance/telemetry отражена через события и ссылки.
6. Dispute flow документирован (создание, обработка, результат, события).
7. Метрики рынка и аналитика (продажи, нарушения) описаны.
8. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Все секции шаблона заполнены, ссылки на `.BRAIN` и связанные API корректны.
- [ ] OpenAPI/AsyncAPI проходит lint, длина ≤400 строк (или вынести части).
- [ ] Примеры покрывают сценарии: выпуск, листинг, покупка, импорт, отзыв, спор.
- [ ] События синхронизированы с notification и analytics.
- [ ] Архитектурный комментарий корректен.
- [ ] Инструкции по обновлению mapping и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Что происходит, если токен истёк или превысил лимит активаций?  
**A:** Возвращается ошибка `410 TOKEN_EXPIRED` или `409 ACTIVATION_LIMIT_REACHED`. Событие `blueprint.revoked` уведомляет владельца и подписчиков.

**Q:** Можно ли продавать blueprint только внутри гильдии?  
**A:** Да, рыночный тип `GUILD` ограничивает доступ членами гильдии. Проверка выполняется через social-service.

**Q:** Как защищаемся от мошенничества?  
**A:** Каждый обмен логируется, подозрительные операции генерируют `blueprint.dispute-opened`. Экономическая служба рассматривает спор через dedicated endpoints.

---

## 🔗 Связность и последующие шаги

- Добавить запись в `tasks/config/brain-mapping.yaml`, обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (API-TASK-307).
- Согласовать спецификацию с economy-marketplace и notification системами.
- После генерации спецификаций инициировать задачи для UI marketplace и аналитики продаж.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

