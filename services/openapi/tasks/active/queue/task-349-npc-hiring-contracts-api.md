# Task ID: API-TASK-349
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:40  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Реализовать спецификацию `NPC Hiring Contracts API`, описывающую создание, управление и арбитраж контрактов найма NPC.  
**Целевой файл:** `api/v1/social/npc-hiring/contracts.yaml`

---

## 🎯 Цель задания

Дать social-service единый REST контракт, который:
- оформляет найм NPC (fixed-term, mission, internship, indefinite) с гибкими условиями, бонусами, штрафами и лицензиями;  
- отслеживает жизненный цикл контрактов (draft → active → suspended → completed/terminated) и статусы арбитража;  
- синхронизируется с economy-service (зарплаты, налоги, бонусы) и npc-service (производительность, навыки, лояльность);  
- интегрируется с notification-service (alerts), world-service (доступность NPC) и analytics (ContractSuccessRate, HiringLeadTime).

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-hiring-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:27  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §2–3: процесс найма, типы контрактов, переговоры, параметры.  
- §5–8: экономика, управление, ограничения, продвинутые механики.  
- §12–13: REST макеты, JSON схемы и Kafka события (`social.npc-hiring.contract.created`, `social.npc-hiring.alert`).  
- §14: метрики (ContractSuccessRate, PayrollBurnRate, LoyaltyTrend).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-hiring-economy.md` — формулы зарплат, бонусов, налогов.  
- `.BRAIN/02-gameplay/social/npc-hiring-limits.md` — квоты, лицензии, ограничения.  
- `.BRAIN/02-gameplay/social/relationships-system-детально.md` — влияние доверия/репутации.  
- `.BRAIN/05-technical/compliance/npc-contract-auditor.md` — регуляторные требования и проверки.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md` — визуализация и UX.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/npc-hiring/contracts.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── npc-hiring/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── contracts.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** economy-service (зарплаты, налоги), npc-service (skills, loyalty), world-service (availability), notification-service (alerts), analytics-service (KPI).  
- **Kafka:** `social.npc-hiring.contract.created`, `social.npc-hiring.contract.updated`, `social.npc-hiring.alert`, `economy.npc-hiring.payroll.processed`, `npc.hiring.performance.changed`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/npc-hiring  
- **State Store:** `useSocialStore(npcHiring)`  
- **UI:** `NpcHiringExchange`, `NpcContractWizard`, `NpcContractDashboard`, `NpcHiringAlertsPanel`, `NpcContractTimeline`  
- **Формы:** `NpcContractCreateForm`, `NpcContractUpdateForm`, `NpcContractTerminateForm`, `NpcHiringLicenseForm`  
- **Layouts:** `NpcHiringLayout`, `NpcContractManagementLayout`  
- **Hooks:** `useNpcContracts`, `useNpcContract`, `useCreateNpcContract`, `useNpcHiringAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/npc-hiring
# - State Store: useSocialStore(npcHiring)
# - UI: NpcHiringExchange, NpcContractWizard, NpcContractDashboard, NpcHiringAlertsPanel, NpcContractTimeline
# - Forms: NpcContractCreateForm, NpcContractUpdateForm, NpcContractTerminateForm, NpcHiringLicenseForm
# - Layouts: NpcHiringLayout, NpcContractManagementLayout
# - Hooks: useNpcContracts, useNpcContract, useCreateNpcContract, useNpcHiringAlerts
# - Events: social.npc-hiring.contract.created, social.npc-hiring.contract.updated, social.npc-hiring.alert, economy.npc-hiring.payroll.processed, npc.hiring.performance.changed
# - API Base: /api/v1/social/npc-hiring/*
```

---

## ✅ Детальный план

1. **Собрать требования к контракту:** тип, статус, сроки, compensation, штрафы, бонусы, условия лицензий.  
2. **Спроектировать схемы:** `NpcContract`, `NpcContractParty`, `NpcContractTerm`, `NpcContractCompensation`, `NpcContractPenalty`, `NpcContractBenefit`, `NpcContractLifecycle`, `NpcContractArbitration`.  
3. **Описать жизненный цикл и статусы:** draft, pending-approval, active, suspended, completed, terminated, disputed.  
4. **Эндпоинты для CRUD, поиска, жалоб, арбитража и подтверждений (acknowledgements).**  
5. **Интеграции с economy-service:** ссылки на payroll, налоги, бонусы, escrow (при необходимости).  
6. **Учесть зависимость от доверия/репутации и лицензий (validation rules).**  
7. **Документировать Kafka события и очередь `npc-hiring-contract-review`.**  
8. **Добавить примеры:** контракт телохранителя, стажировки, группового найма, нарушение и штраф.  
9. **Подключить shared security/responses/pagination, вынести схемы/примеры, соблюдать лимит 400 строк.**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README в `npc-hiring`.**

---

## 🔌 Эндпоинты

1. **POST `/social/npc-hiring/contracts`** — создание контракта (c лицензиями и проверками).  
2. **GET `/social/npc-hiring/contracts/{contractId}`** — детальная карточка.  
3. **GET `/social/npc-hiring/contracts`** — поиск (по типу, статусу, npcId, employerId, лицензиям, роли).  
4. **PATCH `/social/npc-hiring/contracts/{contractId}`** — обновление условий (двойная подпись).  
5. **POST `/social/npc-hiring/contracts/{contractId}/approve`** — подтверждение контракта NPC/фракцией.  
6. **POST `/social/npc-hiring/contracts/{contractId}/suspend`** — приостановка с указанием причины.  
7. **POST `/social/npc-hiring/contracts/{contractId}/terminate`** — расторжение с расчётом штрафов.  
8. **POST `/social/npc-hiring/contracts/{contractId}/dispute`** — запуск арбитража.  
9. **GET `/social/npc-hiring/contracts/{contractId}/history`** — журнал событий.  
10. **GET `/social/npc-hiring/contracts/stats`** — агрегаты (ContractSuccessRate, PayrollBurnRate).  
11. **GET `/social/npc-hiring/contracts/licenses`** — доступные лицензии/квоты (при необходимости через proxy).  
12. **POST `/social/npc-hiring/contracts/{contractId}/alerts/ack`** — подтверждение предупреждений.

---

## 🧱 Модели данных

- **NpcContract** — `contractId`, `employerId`, `npcId`, `contractType`, `status`, `startAt`, `endAt`, `salary`, `bonus`, `penalties[]`, `loyaltyClauses`, `licenseRefs[]`, `equipment`, `housing`, `insurance`, `arbitration`, `metadata`.  
- **NpcContractParty** — сторона контракта (employer, agent, faction) с ролями и подписями.  
- **NpcContractTerm** — условия (workingHours, missions, restDays, exclusivity).  
- **NpcContractCompensation** — зарплата, премии, комиссии, бонусы за миссии.  
- **NpcContractPenalty** — штрафы (type, trigger, amount, escrowRef).  
- **NpcContractBenefit** — жильё, оборудование, обучение, страховка.  
- **NpcContractLifecycle** — статусы и временные метки, инициаторы, SLA.  
- **NpcContractArbitration** — жалобы, решения, штрафы, компенсации.  
- **NpcContractHistoryEntry** — события (created/approved/suspended/terminated/arbitrated).  
- **NpcContractStats** — агрегаты по типу, статусу, средним KPI.  
- **PaginatedNpcContracts** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк (схемы/примеры вынести в `components`).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_CONTRACT_INVALID`, `BIZ_NPC_LICENSE_REQUIRED`, `BIZ_NPC_CONTRACT_CONFLICT`, `BIZ_NPC_CONTRACT_SUSPENDED`, `INT_NPC_CONTRACT_PIPELINE_FAILURE`.  
- `info.description` — перечислить источники `.BRAIN`, UX подтверждения и смежные сервисы.  
- Теги: `NPC Hiring`, `Contracts`, `Licenses`, `Arbitration`, `Alerts`.  
- Отметить связи с `npc-hiring/workforce.yaml`, `npc-hiring/payroll.yaml`, `npc-relationships/status.yaml`.

---

## ✅ Критерии приемки

1. Создан файл `api/v1/social/npc-hiring/contracts.yaml`, проходит `scripts/validate-swagger.ps1`.  
2. В начале файла добавлен `Target Architecture` блок.  
3. Описаны все эндпоинты, модели и бизнес-правила из задания.  
4. Подключены общие компоненты безопасности/ответов/пагинации.  
5. Добавлены примеры (телохранитель, миссионный контракт, приостановка, арбитраж).  
6. Kafka события, очередь `npc-hiring-contract-review` и SLA описаны.  
7. README в `npc-hiring` каталоге обновлён (в рамках реализации).  
8. Task отражён в `brain-mapping.yaml`.  
9. Блок `API Tasks Status` в `.BRAIN` документе обновлён.  
10. Указаны зависимости на payroll, workforce, relationships, economy.  
11. Учтены требования лицензий и проверок соответствия.

---

## ❓ FAQ

**Q:** Как проверять лицензии и квоты?  
A: Через отдельный сервис лицензирования; API должно возвращать `licenseRefs[]` и ошибки `BIZ_NPC_LICENSE_REQUIRED` при нарушениях.  

**Q:** Можно ли нанимать NPC через агента?  
A: Да, предусмотреть `agentId` и комиссионные; агент подтверждает контракт (endpoint `/approve`).  

**Q:** Как учитывать групповое соглашение?  
A: Контракты могут ссылаться на `workforceId`; подробная модель описана в задании `workforce`.  

**Q:** Нужно ли хранить шаблоны контрактов?  
A: Ссылаться на `templateId` (из content/config). Шаблоны вне scope, но ссылка обязательна.  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести схемы/примеры, описать интеграции, прогнать проверки и подготовить MR.

