# Task ID: API-TASK-347
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:25  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-343, API-TASK-344, API-TASK-346 (использует программы, контракты и индексы влияния)

---

## 📋 Краткое описание

Подготовить спецификацию `Mentorship Economy Index API`, фиксирующую экономические показатели наставничества, субсидии, налоги и финансовые эффекты.  
**Целевой файл:** `api/v1/economy/mentorship/index.yaml`

---

## 🎯 Цель задания

Обеспечить economy-service API, которое:
- рассчитывает и предоставляет экономические индексы наставничества по регионам/фракциям;  
- отражает гранты, субсидии, налоговые ставки, страховые фонды и финансовые риски;  
- синхронизирует данные с world-service (эффекты), social-service (контракты, новости) и analytics-service;  
- предоставляет UI и отчетности для экономических дашбордов.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/mentorship-world-impact-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:33  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §2: влияние на экономику (рынок знаний, субсидии, налоги, инвестиции).  
- §5–6: события, механики и индексы (`MentorshipImpactIndex`, `KnowledgeDiffusionRate`).  
- §7: таблица индикаторов, влияние на economy-service.  
- §9: REST макет `GET /economy/mentorship/index`.  
- §10–11: Kafka события `economy.mentorship.index` и метрики.

### Дополнительные источники

- `.BRAIN/02-gameplay/economy/economic-influence-system.md` — общий экономический фреймворк.  
- `.BRAIN/02-gameplay/economy/taxation-system-детально.md` — налоговые механики.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — влияние репутации на экономику.  
- `.BRAIN/02-gameplay/social/mentorship-system-детально.md` — данные для контрактов и платежей.  
- `.BRAIN/05-technical/telemetry/economy-analytics-pipeline.md` — метрики и события аналитики.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/economy/mentorship/index.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── economy/
            └── mentorship/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── index.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** economy-service (port 8089)  
- **Интеграции:** world-service (эффекты), social-service (контракты, новости), analytics-service (финансовые панели), taxation-service, finance-service, notification-service.  
- **Kafka:** `economy.mentorship.index`, `economy.mentorship.grant.updated`, `economy.mentorship.risk`, `world.mentorship.impact`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/economy/mentorship  
- **State Store:** `useEconomyStore(mentorshipIndex)`  
- **UI:** `MentorshipEconomyDashboard`, `GrantDistributionChart`, `MentorshipTaxPanel`, `KnowledgeMarketWidget`, `MentorshipRiskBanner`  
- **Формы:** `GrantAllocationForm`, `SubsidyAdjustmentForm`, `MentorshipTaxRuleForm`  
- **Layouts:** `EconomyMentorshipLayout`, `EconomyInsightsLayout`  
- **Hooks:** `useMentorshipIndex`, `useMentorshipGrants`, `useMentorshipRisks`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: economy-service (port 8089)
# - Frontend Module: modules/economy/mentorship
# - State Store: useEconomyStore(mentorshipIndex)
# - UI: MentorshipEconomyDashboard, GrantDistributionChart, MentorshipTaxPanel, KnowledgeMarketWidget, MentorshipRiskBanner
# - Forms: GrantAllocationForm, SubsidyAdjustmentForm, MentorshipTaxRuleForm
# - Layouts: EconomyMentorshipLayout, EconomyInsightsLayout
# - Hooks: useMentorshipIndex, useMentorshipGrants, useMentorshipRisks
# - Events: economy.mentorship.index, economy.mentorship.grant.updated, economy.mentorship.risk, world.mentorship.impact
# - API Base: /api/v1/economy/mentorship/*
```

---

## ✅ Детальный план

1. **Определить показатели:** Knowledge Diffusion, Grant Pool, Subsidy Level, Tax Adjustment, Insurance Reserve, MentorROI.  
2. **Спроектировать схемы:** `MentorshipEconomyIndex`, `MentorshipEconomyRegion`, `MentorshipGrant`, `MentorshipSubsidy`, `MentorshipTaxRule`, `MentorshipEconomyRisk`, `MentorshipEconomyForecast`.  
3. **Описать жизненный цикл грантов/субсидий:** создание, обновление, отзыв, переоценка риска.  
4. **Разработать эндпоинты:** получение индексов, фильтры по регионам/фракциям, CRUD по грантам и субсидиям, управление налогами, рисками и прогнозами.  
5. **Интеграции:** ссылки на world effects, social contracts, player orders рейтинг.  
6. **Kafka:** документировать топики (index, grants, risk) и payloadы.  
7. **Примеры:** регион с высоким индексом, распределение грантов, субсидия корпорации, предупреждение о риске.  
8. **Shared components:** подключить security/responses/pagination, вынести схемы/примеры.  
9. **Коды ошибок и бизнес-правила (лимиты, конфликт налогов, превышение бюджета).**  
10. **Валидация `scripts/validate-swagger.ps1`, README обновление.**

---

## 🔌 Эндпоинты

1. **GET `/economy/mentorship/index`** — агрегированный индекс по регионам.  
2. **GET `/economy/mentorship/index/{regionId}`** — детализация по региону/фракции.  
3. **GET `/economy/mentorship/index/history`** — временные ряды (KnowledgeDiffusionRate, GrantPool, SubsidyLevel).  
4. **POST `/economy/mentorship/index/recalculate`** — пересчёт экономических показателей.  
5. **GET `/economy/mentorship/grants`** — активные гранты, фильтры (academyId, sponsorId, tier).  
6. **POST `/economy/mentorship/grants`** — создание/распределение гранта.  
7. **PATCH `/economy/mentorship/grants/{grantId}`** — обновление условий.  
8. **POST `/economy/mentorship/grants/{grantId}/revoke`** — отзыв гранта.  
9. **GET `/economy/mentorship/subsidies`** — список субсидий и компенсаций.  
10. **POST `/economy/mentorship/subsidies`** — установка субсидии/льгот.  
11. **GET `/economy/mentorship/taxes`** — текущие налоговые коэффициенты.  
12. **POST `/economy/mentorship/taxes`** — изменение налоговых правил.  
13. **GET `/economy/mentorship/risks`** — активные риски (страховые, дефолты).  
14. **POST `/economy/mentorship/risks/ack`** — обработка риска.  
15. **GET `/economy/mentorship/forecast`** — прогнозы индексов и бюджета.

---

## 🧱 Модели данных

- **MentorshipEconomyIndex** — `regionId`, `factionId`, `knowledgeDiffusionRate`, `grantPool`, `subsidyLevel`, `taxModifier`, `insuranceReserve`, `mentorROI`, `economyPressure`, `lastRecalculatedAt`, `risks[]`.  
- **MentorshipEconomyRegion** — расширенные поля (academies[], contracts[], playerOrdersImpact, revenueStreams).  
- **MentorshipGrant** — `grantId`, `sponsorId`, `beneficiaryId`, `tier`, `amount`, `currency`, `conditions`, `duration`, `status`.  
- **MentorshipSubsidy** — `subsidyId`, `regionId`, `type`, `eligibility`, `amount`, `expiresAt`.  
- **MentorshipTaxRule** — `ruleId`, `regionId`, `baseRate`, `mentorshipModifier`, `effectiveFrom`, `effectiveTo`.  
- **MentorshipEconomyRisk** — `riskId`, `type`, `severity`, `probability`, `affectedEntities`, `recommendedActions`, `status`.  
- **MentorshipEconomyForecast** — прогнозные значения на период (indexTrend, grantForecast, subsidyForecast).  
- **PaginatedMentorshipEconomyHistory** — стандартная пагинация.  
- **MentorshipEconomyEvent** — `eventId`, `eventType`, `payload`, `issuedAt`.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; соблюдать лимит 400 строк (схемы/примеры в components).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_MENTORSHIP_ECONOMY_INVALID`, `BIZ_MENTORSHIP_GRANT_LIMIT`, `BIZ_MENTORSHIP_SUBSIDY_CONFLICT`, `BIZ_MENTORSHIP_TAX_RULE_INVALID`, `INT_MENTORSHIP_ECONOMY_PIPELINE_FAILURE`.  
- `info.description` — ссылки на `.BRAIN` источники и связанные сервисы.  
- Теги: `Mentorship`, `Economy`, `Grants`, `Subsidies`, `Taxes`, `Risks`.  
- Описать Kafka события и интеграцию с telemetry/analytics.

---

## ✅ Критерии приемки

1. Файл `api/v1/economy/mentorship/index.yaml` создан/обновлён, прошёл `scripts/validate-swagger.ps1`.  
2. В начале файла присутствует `Target Architecture` блок.  
3. Реализованы все эндпоинты и модели данных из задания.  
4. Подключены shared компоненты безопасности/ответов/пагинации.  
5. Добавлены примеры (регион, грант, субсидия, налоговое правило, риск).  
6. Kafka события и интеграции с world/social задокументированы.  
7. README в `economy/mentorship` обновлён (в рамках реализации).  
8. Task отражён в `brain-mapping.yaml`.  
9. `.BRAIN` документ обновлён (API Tasks Status).  
10. Указаны зависимости на `mentorship/effects.yaml`, `mentorship/contracts.yaml`, `player-orders/risk.yaml`.

---

## ❓ FAQ

**Q:** Как учитывать валюты и курсы?  
A: Использовать поля `currency` и `exchangeRateRef`; синхронизация с `economy/currency/index.yaml` (вне scope).  

**Q:** Нужно ли предусмотреть бюджетные лимиты?  
A: Да, добавить поля `budgetCap`, `availableBudget`; ошибки при превышении — `BIZ_MENTORSHIP_GRANT_LIMIT`.  

**Q:** Как отражать нелегальные академии?  
A: Отмечать `complianceStatus` и повышенные риски; события передаются в `economy.mentorship.risk`.  

**Q:** Требуется ли экспорт отчётов?  
A: Можно добавить `/export` в будущем; сейчас достаточно REST + Kafka + аналитические пайплайны.  

---

**Следующие шаги исполнителя:** создать спецификацию, вынести компоненты, описать интеграции, подготовить примеры, прогнать проверки и оформить MR.

