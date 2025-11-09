# Task ID: API-TASK-359
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 20:22  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-358 (данные для прогнозов)

---

## 📋 Краткое описание

Разработать спецификацию `NPC Hiring Economy Index API`, отвечающую за экономические показатели рынка найма NPC, прогнозы и риски.  
**Целевой файл:** `api/v1/economy/npc-hiring/index.yaml`

---

## 🎯 Цель задания

Обеспечить economy-service API, которое:
- рассчитывает индексы рынка труда (EmploymentStabilityIndex, LaborDemandIndex, WagePressureIndex, TalentCompetitionScore);  
- фиксирует расходы/доходы работодателей, влияние на налоги, субсидии, страховки;  
- предоставляет прогнозы, риски и отчёты для `modules/economy/npc-hiring` и связанных пайплайнов;  
- синхронизируется с world-service (crisis impact) и social-service (alerts, workforce данные).

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-hiring-world-impact-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:12  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §2: экономическое влияние (рынки ресурсов, бухгалтерия, инвестиции, конкуренция, макроиндикаторы).  
- §3: фракционные лицензии, территориальное влияние.  
- §5: кризисы и их экономические последствия.  
- §9: REST макет `GET /economy/npc-hiring/index`.  
- §10–11: Kafka `economy.npc-hiring.index`, метрики и аналитика.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md` — данные контрактов/зарплат.  
- `.BRAIN/02-gameplay/economy/economic-influence-system.md` — региональные индексы.  
- `.BRAIN/02-gameplay/economy/taxation-system-детально.md` — налоги.  
- `.BRAIN/05-technical/telemetry/economy-analytics-pipeline.md` — аналитика и отчёты.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — совместный эффект контрактов/заказов.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/economy/npc-hiring/index.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── economy/
            └── npc-hiring/
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
- **Интеграции:** world-service (impact), social-service (workforce alerts), finance-service (учёт), taxation-service, notification-service, analytics-service, compliance-service.  
- **Kafka:** `economy.npc-hiring.index`, `economy.npc-hiring.risk`, `economy.npc-hiring.subsidy`, `world.npc-hiring.impact`, `social.npc-hiring.alert`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/economy/npc-hiring  
- **State Store:** `useEconomyStore(npcHiringIndex)`  
- **UI:** `NpcHiringEconomyDashboard`, `NpcHiringGrantPanel`, `NpcHiringTaxWidget`, `NpcHiringRiskBoard`, `NpcHiringForecastChart`  
- **Формы:** `NpcHiringIndexRecalculateForm`, `NpcHiringGrantAllocationForm`, `NpcHiringTaxAdjustmentForm`, `NpcHiringRiskAcknowledgeForm`  
- **Layouts:** `EconomyNpcHiringLayout`, `EconomyInsightsLayout`  
- **Hooks:** `useNpcHiringIndex`, `useNpcHiringGrants`, `useNpcHiringRisks`, `useNpcHiringForecast`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: economy-service (port 8089)
# - Frontend Module: modules/economy/npc-hiring
# - State Store: useEconomyStore(npcHiringIndex)
# - UI: NpcHiringEconomyDashboard, NpcHiringGrantPanel, NpcHiringTaxWidget, NpcHiringRiskBoard, NpcHiringForecastChart
# - Forms: NpcHiringIndexRecalculateForm, NpcHiringGrantAllocationForm, NpcHiringTaxAdjustmentForm, NpcHiringRiskAcknowledgeForm
# - Layouts: EconomyNpcHiringLayout, EconomyInsightsLayout
# - Hooks: useNpcHiringIndex, useNpcHiringGrants, useNpcHiringRisks, useNpcHiringForecast
# - Events: economy.npc-hiring.index, economy.npc-hiring.risk, economy.npc-hiring.subsidy, world.npc-hiring.impact, social.npc-hiring.alert
# - API Base: /api/v1/economy/npc-hiring/*
```

---

## ✅ Детальный план

1. **Собрать показатели:** EmploymentStabilityIndex, LaborDemandIndex, WagePressureIndex, TalentCompetitionScore, NPCProfitability, SubsidyUsage.  
2. **Спроектировать схемы:** `NpcHiringEconomyIndex`, `NpcHiringEconomyRegion`, `NpcHiringEconomyForecast`, `NpcHiringEconomyRisk`, `NpcHiringGrant`, `NpcHiringTaxRule`, `NpcHiringEconomyReport`, `NpcHiringEconomySettings`.  
3. **Эндпоинты:** получение индекса/деталей, пересчёт, grants/subsidies, налоги, риски, прогнозы, отчёты.  
4. **Интеграции:** ссылки на world impact, social alerts, payroll, taxation, mentorship (обучение).  
5. **Документировать Kafka события и очереди (`npc-hiring-economy-validation`).**  
6. **Примеры:** рост конкуренции, выдача гранта, налоговая регулировка, риск дефицита, прогноз.  
7. **Shared components:** security/responses/pagination; вынести схемы/примеры, соблюдать лимит 400 строк.  
8. **Коды ошибок:** лимиты бюджета, конфликты налогов, блокировки.  
9. **Прописать метрики и их использование в аналитике.**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **GET `/economy/npc-hiring/index`** — агрегированный индекс рынка найма.  
2. **GET `/economy/npc-hiring/index/{regionId}`** — детализация региона/фракции.  
3. **GET `/economy/npc-hiring/index/history`** — временные ряды (пагинация).  
4. **POST `/economy/npc-hiring/index/recalculate`** — пересчёт с учетом новых данных.  
5. **GET `/economy/npc-hiring/grants`** — активные гранты/субсидии.  
6. **POST `/economy/npc-hiring/grants`** — распределение грантов.  
7. **PATCH `/economy/npc-hiring/grants/{grantId}`** — корректировки.  
8. **GET `/economy/npc-hiring/taxes`** — текущие налоговые коэффициенты.  
9. **POST `/economy/npc-hiring/taxes`** — установка/изменение правил.  
10. **GET `/economy/npc-hiring/risks`** — активные риски (страхование, дефицит, субсидии).  
11. **POST `/economy/npc-hiring/risks/{riskId}/ack`** — подтверждение/смягчение.  
12. **GET `/economy/npc-hiring/forecast`** — прогнозы (индексы, расходы, субсидии).  
13. **GET `/economy/npc-hiring/report`** — отчёты и показатели.  
14. **GET `/economy/npc-hiring/settings`** — конфигурация налогов/субсидий.  
15. **PATCH `/economy/npc-hiring/settings`** — обновление настроек (аудит).

---

## 🧱 Модели данных

- **NpcHiringEconomyIndex** — `regionId`, `employmentStabilityIndex`, `laborDemandIndex`, `wagePressureIndex`, `talentCompetitionScore`, `npcProfitability`, `subsidyUsage`, `taxPressure`, `updatedAt`.  
- **NpcHiringEconomyRegion** — расширенная модель (employers[], payrollCosts, grantUsage, riskScore).  
- **NpcHiringEconomyForecast** — прогноз (trend, variance, confidence).  
- **NpcHiringEconomyRisk** — `riskId`, `type`, `severity`, `probability`, `impact`, `mitigation`.  
- **NpcHiringGrant** — `grantId`, `sponsor`, `beneficiary`, `amount`, `currency`, `conditions`, `status`.  
- **NpcHiringTaxRule** — `ruleId`, `authority`, `baseRate`, `mentorshipModifier`, `effectiveFrom`.  
- **NpcHiringEconomyReport** — ссылки на отчёты, формат, compliance статус.  
- **NpcHiringEconomySettings** — глобальные параметры (budgetLimits, subsidies, thresholds).  
- **PaginatedNpcHiringEconomyHistory** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, вынести схемы/примеры.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_HIRING_INDEX_INVALID`, `BIZ_NPC_HIRING_GRANT_LIMIT`, `BIZ_NPC_HIRING_TAX_CONFLICT`, `BIZ_NPC_HIRING_RISK_ACTIVE`, `INT_NPC_HIRING_ECONOMY_PIPELINE_FAILURE`.  
- `info.description` — указать `.BRAIN` источники, UX и интеграции.  
- Теги: `NPC Hiring`, `Economy`, `Index`, `Grants`, `Taxes`, `Risks`.  
- Указать зависимости на `world/npc-hiring/effects.yaml`, `social/npc-hiring/workforce.yaml`, `economy/taxation/rules.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/economy/npc-hiring/index.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. Добавлен `Target Architecture` блок.  
3. Реализованы перечисленные эндпоинты, модели и примеры.  
4. Подключены shared security/responses/pagination.  
5. Kafka события и очереди документированы.  
6. README в каталоге обновлён (в рамках реализации).  
7. Task отражён в `brain-mapping.yaml`.  
8. `.BRAIN` документ обновлён (`API Tasks Status`).  
9. Указаны зависимости на world/social сервисы, налоги, субсидии.  
10. Обозначены метрики (`EmploymentStabilityIndex`, `LaborDemandIndex`, `WagePressureIndex`, `TalentCompetitionScore`).  
11. Добавлены примеры (региональный индекс, grant, налоговое изменение, риск, прогноз).

---

## ❓ FAQ

**Q:** Как учитывать разные валюты?  
A: Использовать `currency` и `exchangeRateRef`; ошибки при отсутствии курса — `VAL_NPC_HIRING_INDEX_INVALID`.  

**Q:** Можно ли автоматически распределять субсидии?  
A: Да, предусмотреть флаги `autoDistribute`, `prioritySectors`; алгоритм описан в economy документе.  

**Q:** Как реагировать на риски?  
A: Возвращать их в `/risks`, поддерживать ack, публиковать `economy.npc-hiring.risk`.  

**Q:** Требуется ли экспорт для бухгалтерии?  
A: Да, `report` эндпоинт возвращает ссылки (PDF/JSON) и статусы compliance.  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

