# Task ID: API-TASK-351
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:40  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-349, API-TASK-350 (контракты и workforce данные)

---

## 📋 Краткое описание

Создать спецификацию `NPC Hiring Payroll API`, обеспечивающую расчёт зарплат, налогов, бонусов и отчётность по найму NPC.  
**Целевой файл:** `api/v1/economy/npc-hiring/payroll.yaml`

---

## 🎯 Цель задания

Обеспечить economy-service API, которое:
- рассчитывает платежи по контрактам (зарплаты, бонусы, штрафы, налоги, страховки);  
- ведёт учёт расходов, бюджетов, лимитов, страховых резервов и субсидий;  
- генерирует отчёты, интегрируется с финучётом, налоговой системой и analytics;  
- публикует события о выплатах и рисках (`economy.npc-hiring.payroll.processed`, `economy.npc-hiring.risk`).

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:27  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §5–6: экономика найма, зарплаты, бонусы, расходы, ROI.  
- §7: ограничения и квоты.  
- §12–13: REST макеты (`/economy/npc-hiring/payroll/calculate`) и Kafka (`economy.npc-hiring.payroll.processed`).  
- §14: метрики (PayrollBurnRate, ContractSuccessRate).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-hiring-economy.md` — детальные формулы, тарифы, бонусы, страховки.  
- `.BRAIN/02-gameplay/economy/taxation-system-детально.md` — налоговые коэффициенты.  
- `.BRAIN/02-gameplay/economy/economic-influence-system.md` — влияние на экономику регионов.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — репутационные модификаторы и штрафы.  
- `.BRAIN/05-technical/telemetry/economy-analytics-pipeline.md` — метрики и алерты.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/economy/npc-hiring/payroll.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
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
                └── payroll.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** economy-service (port 8089)  
- **Интеграции:** social-service (контракты, workforce), taxation-service (налоги), finance-service (бюджеты), notification-service (alerts), analytics-service (экономические dashboards), compliance-service (регуляторные отчёты).  
- **Kafka:** `economy.npc-hiring.payroll.processed`, `economy.npc-hiring.grant.updated`, `economy.npc-hiring.risk`, `social.npc-hiring.contract.updated`, `world.mentorship.impact`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/economy/npc-hiring  
- **State Store:** `useEconomyStore(npcPayroll)`  
- **UI:** `NpcPayrollDashboard`, `NpcPayrollRunWizard`, `NpcPayrollExpenseTable`, `NpcPayrollRiskPanel`, `NpcPayrollForecastChart`  
- **Формы:** `NpcPayrollRunForm`, `NpcPayrollAdjustmentForm`, `NpcPayrollRiskAcknowledgeForm`  
- **Layouts:** `EconomyNpcPayrollLayout`, `EconomyInsightsLayout`  
- **Hooks:** `useNpcPayroll`, `useNpcPayrollRuns`, `useNpcPayrollRisks`, `useNpcPayrollForecast`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: economy-service (port 8089)
# - Frontend Module: modules/economy/npc-hiring
# - State Store: useEconomyStore(npcPayroll)
# - UI: NpcPayrollDashboard, NpcPayrollRunWizard, NpcPayrollExpenseTable, NpcPayrollRiskPanel, NpcPayrollForecastChart
# - Forms: NpcPayrollRunForm, NpcPayrollAdjustmentForm, NpcPayrollRiskAcknowledgeForm
# - Layouts: EconomyNpcPayrollLayout, EconomyInsightsLayout
# - Hooks: useNpcPayroll, useNpcPayrollRuns, useNpcPayrollRisks, useNpcPayrollForecast
# - Events: economy.npc-hiring.payroll.processed, economy.npc-hiring.grant.updated, economy.npc-hiring.risk, social.npc-hiring.contract.updated, world.mentorship.impact
# - API Base: /api/v1/economy/npc-hiring/*
```

---

## ✅ Детальный план

1. **Определить сущности payroll:** рассчитать выплаты по контрактам, бонусы, штрафы, налоги, страховые резервы.  
2. **Спроектировать схемы:** `NpcPayrollRun`, `NpcPayrollEntry`, `NpcPayrollAdjustment`, `NpcPayrollSummary`, `NpcPayrollForecast`, `NpcPayrollRisk`, `NpcPayrollGrant`, `NpcPayrollReport`, `NpcPayrollSettings`.  
3. **Разработать эндпоинты:** запуск расчёта, получение результатов, история, корректировки, риски, прогнозы, экспорт.  
4. **Интеграции:** ссылки на контракты (API-TASK-349), workforce (API-TASK-350), налоги, субсидии, экономические индексы.  
5. **Документировать Kafka события и очередь аудита (если нужна).**  
6. **Примеры:** еженедельный payroll, перерасчёт бонусов, налоговая корректировка, предупреждение о риске, прогноз бюджета.  
7. **Shared компоненты:** security/responses/pagination, вынести схемы/примеры; соблюдать лимит 400 строк.  
8. **Определить коды ошибок и бизнес-правила (лимиты бюджета, конфликты налогов).**  
9. **Добавить описания метрик: PayrollBurnRate, MentorROI, InsuranceReserve.**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/economy/npc-hiring/payroll/run`** — запуск расчёта payroll (период, регионы, фильтры).  
2. **GET `/economy/npc-hiring/payroll/runs/{runId}`** — детали расчёта (выплаты, налоги, бонусы, отчёты).  
3. **GET `/economy/npc-hiring/payroll/runs`** — список запусков (фильтры по периоду, статусу, региону, работодателю).  
4. **POST `/economy/npc-hiring/payroll/runs/{runId}/recalculate`** — перерасчёт (после обновления контрактов).  
5. **POST `/economy/npc-hiring/payroll/runs/{runId}/approve`** — утверждение выплаты (финансовый менеджер).  
6. **POST `/economy/npc-hiring/payroll/runs/{runId}/adjust`** — корректировки (бонусы, штрафы, перераспределение).  
7. **GET `/economy/npc-hiring/payroll/runs/{runId}/report`** — отчёт (PDF/JSON ссылки, интеграция с finance-service).  
8. **GET `/economy/npc-hiring/payroll/summary`** — агрегаты (PayrollBurnRate, GrantUsage, InsuranceReserve).  
9. **GET `/economy/npc-hiring/payroll/forecast`** — прогноз расходов и бюджета.  
10. **GET `/economy/npc-hiring/payroll/risks`** — активные риски и предупреждения.  
11. **POST `/economy/npc-hiring/payroll/risks/{riskId}/ack`** — подтверждение обработки риска.  
12. **GET `/economy/npc-hiring/payroll/settings`** — конфигурация налогов, субсидий, страховок.  
13. **PATCH `/economy/npc-hiring/payroll/settings`** — обновление конфигурации (c аудиторскими правилами).

---

## 🧱 Модели данных

- **NpcPayrollRun** — `runId`, `period`, `status`, `initiatedBy`, `contracts[]`, `totalCost`, `taxes`, `bonuses`, `penalties`, `subsidies`, `currency`, `createdAt`, `approvedAt`.  
- **NpcPayrollEntry** — `contractId`, `npcId`, `employerId`, `baseSalary`, `hours`, `missionRewards`, `bonuses`, `penalties`, `taxes`, `netPay`, `loyaltyImpact`.  
- **NpcPayrollAdjustment** — `adjustmentId`, `runId`, `type`, `reason`, `amount`, `createdBy`, `status`.  
- **NpcPayrollSummary** — агрегаты по работодателю/региону (`PayrollBurnRate`, `GrantUsage`, `InsuranceReserve`, `MentorROI`).  
- **NpcPayrollForecast** — прогнозные значения на будущие периоды (trend, variance, confidence).  
- **NpcPayrollRisk** — `riskId`, `severity`, `probability`, `description`, `impact`, `recommendedActions`, `status`.  
- **NpcPayrollGrant** — `grantId`, `sponsorId`, `amount`, `conditions`, `usedAmount`, `expiresAt`.  
- **NpcPayrollReport** — ссылки на документы, формат, подписи, compliance статус.  
- **NpcPayrollSettings** — `taxRules[]`, `subsidyPrograms[]`, `insurancePolicies[]`, `currency`, `exchangeRateRef`.  
- **PaginatedNpcPayrollRuns** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, компоненты вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_PAYROLL_INVALID`, `BIZ_NPC_PAYROLL_BUDGET_EXCEEDED`, `BIZ_NPC_PAYROLL_TAX_CONFLICT`, `BIZ_NPC_PAYROLL_RUN_LOCKED`, `INT_NPC_PAYROLL_PIPELINE_FAILURE`.  
- `info.description` — указать `.BRAIN` источники, налоговые документы и UX подтверждения.  
- Теги: `NPC Hiring`, `Payroll`, `Economy`, `Taxes`, `Risks`.  
- Документировать Kafka и отчёты для compliance; упомянуть `economy.npc-hiring.risk`.  
- Указать зависимости на `npc-hiring/contracts.yaml`, `npc-hiring/workforce.yaml`, `economy/mentorship/index.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/economy/npc-hiring/payroll.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. Добавлен `Target Architecture` блок.  
3. Реализованы все эндпоинты и модели, перечисленные в задании.  
4. Подключены shared компоненты безопасности/ответов/пагинации.  
5. Добавлены примеры (еженедельный payroll, корректировка, риск, прогноз).  
6. Kafka события и интеграция с налоговой/финансовой системой описаны.  
7. README для каталога обновлён (в рамках реализации).  
8. Task отражён в `brain-mapping.yaml`.  
9. `.BRAIN` документ обновлён (API Tasks Status).  
10. Указаны зависимости на контракты, workforce, economy индексы.  
11. Учтены бюджетные лимиты, субсидии и валюты.

---

## ❓ FAQ

**Q:** Как учитывать валюту и курсы?  
A: Поля `currency`, `exchangeRateRef`, `conversionDate`; ошибки при отсутствии курса — `VAL_NPC_PAYROLL_INVALID`.  

**Q:** Можно ли частично одобрить выплаты?  
A: Да, предусмотреть `NpcPayrollAdjustment` с статусами и аудитом; API поддерживает частичное утверждение.  

**Q:** Требуется ли экспорт для бухгалтерии?  
A: Да, возвращать `reportLinks[]` с форматами (PDF/JSON) и статусом compliance.  

**Q:** Как реагировать на превышение бюджета?  
A: Возвращать ошибку `BIZ_NPC_PAYROLL_BUDGET_EXCEEDED`, публиковать событие `economy.npc-hiring.risk`, просить подтверждение менеджера.  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры, прогнать проверки и оформить MR.

