# Task ID: API-TASK-357
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 20:10  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-355, API-TASK-356 (использует данные дерева и событий)

---

## 📋 Краткое описание

Подготовить спецификацию `Families Heritage API`, рассчитывающую наследство, распределение активов, налоги и экономические последствия семейных событий.  
**Целевой файл:** `api/v1/economy/families/heritage.yaml`

---

## 🎯 Цель задания

Обеспечить economy-service API, которое:
- агрегирует имущество семей (активы, компании, доли, ресурсы) и рассчитывает наследство по сценарию (завещания, споры, налоги);  
- поддерживает workflow утверждения, споров, арбитража, выплат, субсидий и резервов;  
- синхронизируется с social-service (tree/events), factions-service (династии), finance-service (бухгалтерия) и notification-service (alerts);  
- публикует Kafka события (`economy.family.heritage`, `economy.family.risk`, `economy.family.tax.update`).

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/family-relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:53  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §7: наследование, завещания, споры, экономические эффекты.  
- §8: влияние на фракции, мир и экономику.  
- §12: REST макет `POST /economy/families/heritage/calculate`.  
- §13: Kafka `economy.family.heritage`.

### Дополнительные источники

- `.BRAIN/02-gameplay/economy/taxation-system-детально.md` — налоговые правила.  
- `.BRAIN/02-gameplay/economy/economic-influence-system.md` — региональные индексы.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — влияние репутации.  
- `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md` — семейный бизнес, контракты.  
- `.BRAIN/05-technical/telemetry/economy-analytics-pipeline.md` — аналитика и алерты.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/economy/families/heritage.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── economy/
            └── families/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── heritage.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** economy-service (port 8089)  
- **Интеграции:** social-service (tree/events), finance-service (учёт, отчёты), taxation-service, factions-service, notification-service, analytics-service, compliance-service.  
- **Kafka:** `economy.family.heritage`, `economy.family.risk`, `economy.family.tax.update`, `social.family.event`, `world.family.crisis`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/economy/families  
- **State Store:** `useEconomyStore(familyHeritage)`  
- **UI:** `FamilyHeritageDashboard`, `HeritageCalculationWizard`, `HeritageDisputePanel`, `FamilyAssetBreakdown`, `HeritageRiskWidget`  
- **Формы:** `HeritageCalculationForm`, `HeritageDistributionForm`, `HeritageDisputeForm`, `HeritageApprovalForm`  
- **Layouts:** `FamilyHeritageLayout`, `EconomyHeritageLayout`  
- **Hooks:** `useFamilyHeritage`, `useHeritageRuns`, `useHeritageDisputes`, `useHeritageForecast`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: economy-service (port 8089)
# - Frontend Module: modules/economy/families
# - State Store: useEconomyStore(familyHeritage)
# - UI: FamilyHeritageDashboard, HeritageCalculationWizard, HeritageDisputePanel, FamilyAssetBreakdown, HeritageRiskWidget
# - Forms: HeritageCalculationForm, HeritageDistributionForm, HeritageDisputeForm, HeritageApprovalForm
# - Layouts: FamilyHeritageLayout, EconomyHeritageLayout
# - Hooks: useFamilyHeritage, useHeritageRuns, useHeritageDisputes, useHeritageForecast
# - Events: economy.family.heritage, economy.family.risk, economy.family.tax.update, social.family.event, world.family.crisis
# - API Base: /api/v1/economy/families/*
```

---

## ✅ Детальный план

1. **Определить модель наследства:** активы, доли, условия завещаний, обязательства, налоги, риски.  
2. **Спроектировать схемы:** `FamilyHeritageRun`, `FamilyAsset`, `HeritageDistribution`, `HeritageTax`, `HeritageDispute`, `HeritageRisk`, `HeritageForecast`, `HeritageReport`, `HeritageSettings`.  
3. **Реализовать эндпоинты:** запуск расчёта, просмотр результатов, управление распределением, споры, налоги, риски, отчёты, экспорт.  
4. **Интеграции:** ссылки на `families/tree`, `families/events`, npc-hiring, economy indices, taxation, factions.  
5. **Документировать workflow одобрения, арбитража и SLA (`family-heritage-validation`).**  
6. **Примеры:** наследство корпорации, спор о завещании, налоговая корректировка, прогноз, риск.  
7. **Shared components:** security/responses/pagination; вынести схемы/примеры, соблюдать лимит 400 строк.  
8. **Коды ошибок:** лимиты, конфликты, блокировка, несогласованные данные.  
9. **Kafka:** описать события и очереди, интеграции с analytics/finance.  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/economy/families/heritage/calculate`** — запуск расчёта наследства (familyId, сценарий).  
2. **GET `/economy/families/heritage/runs/{runId}`** — детали расчёта (активы, распределение, налоги, риски).  
3. **GET `/economy/families/heritage/runs`** — список запусков (фильтры по семье, периоду, статусу).  
4. **POST `/economy/families/heritage/runs/{runId}/approve`** — утверждение распределения.  
5. **POST `/economy/families/heritage/runs/{runId}/dispute`** — инициирование спора/арбитража.  
6. **GET `/economy/families/heritage/disputes`** — активные споры, статусы, дедлайны.  
7. **POST `/economy/families/heritage/runs/{runId}/adjust`** — корректировки (перераспределение, налоги, штрафы).  
8. **GET `/economy/families/heritage/runs/{runId}/report`** — отчёты (PDF/JSON).  
9. **GET `/economy/families/heritage/summary`** — агрегаты (HeritageDisputeRate, FamilyWealthIndex).  
10. **GET `/economy/families/heritage/forecast`** — прогнозы по наследству и налоговым нагрузкам.  
11. **GET `/economy/families/heritage/risks`** — активные риски (недостаток ликвидности, споры, налоги).  
12. **POST `/economy/families/heritage/risks/{riskId}/ack`** — подтверждение обработки риска.  
13. **GET `/economy/families/heritage/settings`** — конфигурация налогов, субсидий, правил распределения.  
14. **PATCH `/economy/families/heritage/settings`** — обновление настроек (аудит).

---

## 🧱 Модели данных

- **FamilyHeritageRun** — `runId`, `familyId`, `scenario`, `status`, `assets[]`, `distribution`, `taxes`, `reserves`, `subsidies`, `initiatedBy`, `createdAt`, `approvedAt`.  
- **FamilyAsset** — `assetId`, `type`, `value`, `currency`, `ownership`, `liquidity`, `restrictions`.  
- **HeritageDistribution** — `beneficiaryId`, `share`, `conditions`, `escrow`, `schedule`.  
- **HeritageTax** — `taxId`, `authority`, `rate`, `amount`, `dueDate`.  
- **HeritageDispute** — `disputeId`, `initiator`, `reason`, `severity`, `status`, `resolution`.  
- **HeritageRisk** — `riskId`, `type`, `probability`, `impact`, `mitigation`, `status`.  
- **HeritageForecast** — прогноз (trend, variance, confidence).  
- **HeritageReport** — ссылки на отчёты, формат, compliance статус.  
- **HeritageSettings** — налоговые правила, льготы, пороги.  
- **PaginatedHeritageRuns** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, вынести схемы/примеры.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_FAMILY_HERITAGE_INVALID`, `BIZ_FAMILY_HERITAGE_ASSET_LOCKED`, `BIZ_FAMILY_HERITAGE_DISPUTE_ACTIVE`, `BIZ_FAMILY_HERITAGE_BUDGET_EXCEEDED`, `INT_FAMILY_HERITAGE_PIPELINE_FAILURE`.  
- `info.description` — перечислить `.BRAIN` источники, UX, экономические зависимости.  
- Теги: `Families`, `Heritage`, `Economy`, `Taxes`, `Risks`.  
- Указать зависимости на `families/tree.yaml`, `families/events.yaml`, `economy/taxation.yaml`, `economy/mentorship/index.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/economy/families/heritage.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. Добавлен `Target Architecture` блок.  
3. Реализованы эндпоинты, модели, примеры, описанные выше.  
4. Подключены shared security/responses/pagination.  
5. Kafka события, очереди и метрики документированы.  
6. README обновлён (в рамках реализации).  
7. Task отображён в `brain-mapping.yaml`.  
8. `.BRAIN` документ обновлён.  
9. Указаны зависимости на дерево, события, налоги, экономические индексы.  
10. Описаны workflow одобрения, споров, рисков, отчётов.  
11. Зафиксированы метрики (`HeritageDisputeRate`, `FamilyWealthIndex`, `InsuranceReserve`).

---

## ❓ FAQ

**Q:** Как учитывать валюты и курсы?  
A: Поля `currency`, `exchangeRateRef`, `conversionDate`; при отсутствии курса — ошибка `VAL_FAMILY_HERITAGE_INVALID`.  

**Q:** Что делать при активном споре?  
A: Блокировать финальное утверждение (`BIZ_FAMILY_HERITAGE_DISPUTE_ACTIVE`), разрешение через арбитраж.  

**Q:** Нужно ли поддерживать автоматические выплаты?  
A: Да, предусмотреть `schedule` и `autoDisbursement`; интеграция с finance-service.  

**Q:** Как обрабатывать долги и обязательства?  
A: Включить `liabilities[]`, `debtSettlement`, `risk` поля и учитывать в расчётах/рисках.  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

