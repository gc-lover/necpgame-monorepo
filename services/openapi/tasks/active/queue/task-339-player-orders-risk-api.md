# Task ID: API-TASK-339
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** completed  
**Создано:** 2025-11-08 18:52  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-317], [API-TASK-318], [API-TASK-320]

---

## 📋 Краткое описание

Подготовить спецификацию `Player Orders Risk API`, которая описывает скоринг рисков и страхование заказов: расчёт коэффициентов, страховые планы, лимиты, уведомления и интеграцию с экономикой.  
**Целевой файл:** `api/v1/economy/player-orders/risk.yaml`

---

## 🎯 Цель задания

Обеспечить economy-service API, позволяющий:
- вычислять риск-коэффициент заказа на основе сложности, вовлечённых фракций, истории исполнителя/заказчика, территорий и текущей ситуации;  
- управлять страховыми продуктами (планы, премии, покрытия, escrow) и лимитами;  
- публиковать данные риска для настроек наград, комиссий и гарантий;  
- интегрировать риск-метрики с социальными рейтингами, новостными системами и аналитикой.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 09:55  
**Статус:** approved (api-readiness: ready)

**Что важно из документа:**
- Разделы 2–6: метрики, категории, decay/boost, санкции.  
- Раздел 12: REST эндпоинты и связанный `riskModifier`, escrow, страховки.  
- Упоминание `RiskModifier` и страховых фондов (`economy-service`, escrow).  
- Kafka события: `social.player-orders.rating.updated`, `economy.player-orders.reward.adjusted`.  
- Интеграции с арбитражем, страховыми фондами и decay.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — формула `BaseReward = ComplexityScore * RiskModifier * MarketIndex * TimeModifier`.  
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` — workflow заказов и гарантий.  
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — региональные риски, события.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — влияние на world/economy.  
- `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md` — рейтинги участников (вход в скоринг).

---

## 📁 Целевая структура API

**Файл:** `api/v1/economy/player-orders/risk.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── economy/
            └── player-orders/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── risk.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис):
- **Микросервис:** economy-service (порт 8085)  
- **Интеграции:** social-service (рейтинги), world-service (регионы), marketing-service (страховые промо), analytics-service (метрики), notification-service.  
- **Kafka:** `economy.player-orders.risk.calculated`, `economy.player-orders.insurance.updated`, `economy.player-orders.reward.adjusted`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):
- **Модуль:** modules/economy/player-orders/risk  
- **State Store:** `useEconomyStore(playerOrders)`  
- **UI:** `RiskDashboard`, `InsurancePlanCard`, `RiskMatrix`, `EscrowStatus`, `AlertBanner`  
- **Формы:** `RiskEvaluationForm`, `InsurancePlanForm`, `EscrowReleaseForm`  
- **Layouts:** `PlayerOrdersEconomyLayout`, `OperationsConsoleLayout`  
- **Хуки:** `useRiskEvaluation`, `useInsurancePlans`, `useEscrowStatus`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: economy-service (port 8085)
# - Frontend Module: modules/economy/player-orders/risk
# - State Store: useEconomyStore(playerOrders)
# - UI: RiskDashboard, InsurancePlanCard, RiskMatrix, EscrowStatus, AlertBanner
# - Forms: RiskEvaluationForm, InsurancePlanForm, EscrowReleaseForm
# - Layouts: PlayerOrdersEconomyLayout, OperationsConsoleLayout
# - Hooks: useRiskEvaluation, useInsurancePlans, useEscrowStatus
# - Events: economy.player-orders.risk.calculated, economy.player-orders.insurance.updated, economy.player-orders.reward.adjusted
# - API Base: /api/v1/economy/player-orders/*
```

---

## ✅ План работ

1. **Проанализировать метрики риска:** сложности заказа, рейтинг сторон, региональные угрозы, тип заказа (боевой/хакерский/логистический).  
2. **Спроектировать схемы:** `RiskEvaluationRequest`, `RiskScore`, `RiskFactorBreakdown`, `InsurancePlan`, `InsuranceQuote`, `EscrowStatus`, `RiskAlert`.  
3. **Сформировать endpoints:**  
   - Расчёт риска (one-shot, batch).  
   - Получение risk score для заказа/игрока.  
   - Управление страховыми планами и цитатами.  
   - Отслеживание escrow и гарантий.  
   - Подписка на risk alerts для операций.  
4. **Подключить общие компоненты:** shared security/responses/pagination.  
5. **Документировать Kafka события, влияние на награды и decay.**  
6. **Примеры:** высокий риск боевого заказа, низкий риск логистического, страховой план Platinum, escrow release, alert при превышении порога.  
7. **Прогнать `scripts/validate-swagger.ps1`, вынести компоненты, удержать файл ≤400 строк.**

---

## 🔌 Эндпоинты

1. **POST `/economy/player-orders/risk/evaluate`** — расчёт риска для заказа (синхронный).  
2. **POST `/economy/player-orders/risk/jobs`** — batch расчёт (возвращает `jobId`).  
3. **GET `/economy/player-orders/risk/jobs/{jobId}`** — статус batch расчёта.  
4. **GET `/economy/player-orders/risk/{orderId}`** — текущий риск заказа, факторы, страховой план.  
5. **GET `/economy/player-orders/risk/players/{playerId}`** — risk profile участника.  
6. **POST `/economy/player-orders/risk/insurance/quote`** — расчёт страховой премии и условий.  
7. **POST `/economy/player-orders/risk/insurance/plans`** — создание/обновление страхового плана (для экономистов).  
8. **GET `/economy/player-orders/risk/escrow/{orderId}`** — статус escrow, лимиты, триггеры release.  
9. **POST `/economy/player-orders/risk/alerts`** — подписка на алерты/пороговые события.  
10. **DELETE `/economy/player-orders/risk/alerts/{subscriptionId}`** — управление подпиской.

---

## 🧱 Модели данных

- **RiskEvaluationRequest** — `orderId`, `orderType`, `regionId`, `complexityScore`, `participants[]`, `factionTags[]`, `historicalData`, `requestedBy`.  
- **RiskScore** — `orderId`, `overallScore` (0.5–2.0), `category` (low/medium/high/extreme), `factors[]`, `decayTs`.  
- **RiskFactorBreakdown** — массив факторов с весами (complexity, territory, faction hostility, reputation, dispute history).  
- **InsurancePlan** — `planId`, `name`, `coverage`, `premiumRate`, `deductible`, `conditions`.  
- **InsuranceQuote** — результат расчёта премии, `premium`, `coverage`, `expiresAt`.  
- **EscrowStatus** — `escrowId`, `balance`, `lockState`, `releaseConditions`, `history[]`.  
- **RiskAlertSubscription** — `subscriptionId`, `threshold`, `channels[]`, `contact`, `status`.  
- **RiskEvaluationJob** — `jobId`, `status`, `progress`, `createdAt`, `finishedAt`, `errors[]`.  
- **PaginatedRiskScoreList** — для списков history (использовать shared pagination).

Каждую схему снабдить описаниями, валидацией, ссылками на связанные API (`ratings`, `reviews`, `player-orders/economy`).

---

## 📏 Принципы и правила

- OpenAPI 3.0.3, ≤400 строк; компоненты вынести.  
- Использовать shared security/responses/pagination.  
- Ошибки с `x-error-code`: `VAL_INVALID_RISK_REQUEST`, `BIZ_RISK_NOT_FOUND`, `BIZ_INSURANCE_CONFLICT`, `BIZ_ESCROW_LOCKED`, `INT_RISK_ENGINE_FAILURE`.  
- `info.description` перечисляет источники `.BRAIN`, дату, UX/QA.  
- Добавить `tags` (Risk Evaluation, Insurance).  
- Документировать взаимосвязь с rewards (`economy.player-orders.reward.adjusted`) и ratings.

---

## ✅ Критерии приемки

1. Файл `api/v1/economy/player-orders/risk.yaml` создан и валиден (`scripts/validate-swagger.ps1`).  
2. Комментарий `Target Architecture` присутствует.  
3. Реализованы endpoints для расчёта риска, страховых планов, escrow и alerts.  
4. Схемы `RiskEvaluationRequest`, `RiskScore`, `RiskFactorBreakdown`, `InsurancePlan`, `EscrowStatus`, `RiskEvaluationJob`, `RiskAlertSubscription` описаны.  
5. Подключены общие компоненты безопасности и ошибок.  
6. Kafka события и интеграция с наградами/рейтинговым пересчётом задокументированы.  
7. Примеры охватывают разные типы заказов и страховых планов.  
8. README в `economy/player-orders` обновлён (в реализации).  
9. Task добавлен в `brain-mapping.yaml` (текущее задание).  
10. Документация отражает использование риска в формуле наград и decay.

---

## ❓ FAQ

**Q:** Как риск влияет на награды?  
A: Через `RiskModifier` (формула из creation-документа). API должен возвращать `riskModifier`, чтобы `economy-service` корректировал вознаграждение/комиссию.

**Q:** Нужно ли учитывать региональные события?  
A: Да, добавьте поле `regionRisk` и ссылку на `world-events`; учитывайте `factionTags` и `territorySecurity`.

**Q:** Как синхронизируется escrow?  
A: Через `EscrowStatus` и события `economy.player-orders.reward.adjusted`; при release обновляется награда и рейтинги.

**Q:** Нужно ли хранить историю расчётов?  
A: Предусмотреть `RiskEvaluationJob` и endpoints для выборок/экспорта; исторические данные могут храниться в analytics (вне scope).

---

**Следующие действия исполнителя:** реализовать спецификацию, вынести компоненты и примеры, обновить README, прогнать валидацию.

---

## 📌 История выполнения

- 2025-11-08 – создано задание по `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md`, статус `queued`.
- 2025-11-08 – АПИТАСК активировал задачу, старт разработки OpenAPI `api/v1/economy/player-orders/risk.yaml` (`status: in_progress`).
- 2025-11-08 – Подготовлена и провалидирована спецификация `api/v1/economy/player-orders/risk.yaml`, задача завершена (`status: completed`).

