# Task ID: API-TASK-344
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:10  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-343 (shared схемы программ, расписаний)

---

## 📋 Краткое описание

Разработать спецификацию `Mentorship Contracts API`, охватывающую создание, управление и арбитраж договоров наставничества между игроками, NPC и академиями.  
**Целевой файл:** `api/v1/social/mentorship/contracts.yaml`

---

## 🎯 Цель задания

Предоставить social-service API для:
- заключения договоров наставничества с параметрами (тип, цели, длительность, оплата, требования доверия);
- управления жизненным циклом контрактов (draft, active, suspended, completed, terminated);
- отслеживания прогресса, выплат и нарушений, интеграции с economy-service (escrow, гранты) и relationships/trust;
- обработки жалоб, штрафов и отзывов, публикации Kafka событий;
- предоставления фронтенду полных данных для модулей договоров и арбитража.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/mentorship-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:20  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**
- Раздел 3 (пайплайн) и 3.1 — структура договоров, арбитраж, штрафы.  
- Разделы 6–7 — репутация наставника, связь с отношениями/довериями.  
- Раздел 11 — монетизация и экономика, гранты, платежи.  
- Раздел 13 — REST макеты (`POST /social/mentorship/contracts`).  
- Раздел 14 — Kafka события `social.mentorship.contract.signed`, `social.mentorship.contract.terminated`.  
- Раздел 15 — метрики `MentorSatisfactionScore`, `LessonCompletionRate`.

### Дополнительные документы

- `.BRAIN/02-gameplay/social/mentorship-world-impact-детально.md` — последствия контрактов для мира и экономики.  
- `.BRAIN/02-gameplay/social/relationships-system-детально.md` — влияние наставничества на trust и reputation.  
- `.BRAIN/02-gameplay/economy/escrow-system.md` — механики эскроу и штрафов.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — общий подход к отзывам, штрафам и рейтингам.  
- `.BRAIN/05-technical/compliance/mentorship-arbitration-service.md` — регуляторные требования и SLA.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/mentorship/contracts.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── mentorship/
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
- **Интеграции:** economy-service (escrow, выплаты), world-service (академии), notification-service (уведомления), analytics-service (MentorSatisfactionScore), compliance-service (арбитраж).  
- **Kafka:** `social.mentorship.contract.signed`, `social.mentorship.contract.terminated`, `social.mentorship.lesson.completed`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/mentorship  
- **State Store:** `useSocialStore(mentorshipContracts)`  
- **UI:** `MentorshipContractWizard`, `MentorshipContractOverview`, `MentorshipContractTimeline`, `MentorshipArbitrationPanel`, `MentorshipPenaltyBanner`  
- **Формы:** `ContractCreateForm`, `ContractUpdateForm`, `ContractTerminationForm`, `ContractComplaintForm`  
- **Layouts:** `MentorshipContractsLayout`, `MentorshipArbitrationLayout`  
- **Hooks:** `useMentorshipContracts`, `useMentorshipContract`, `useCreateMentorshipContract`, `useMentorshipComplaints`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/mentorship
# - State Store: useSocialStore(mentorshipContracts)
# - UI: MentorshipContractWizard, MentorshipContractOverview, MentorshipContractTimeline, MentorshipArbitrationPanel, MentorshipPenaltyBanner
# - Forms: ContractCreateForm, ContractUpdateForm, ContractTerminationForm, ContractComplaintForm
# - Layouts: MentorshipContractsLayout, MentorshipArbitrationLayout
# - Hooks: useMentorshipContracts, useMentorshipContract, useCreateMentorshipContract, useMentorshipComplaints
# - Events: social.mentorship.contract.signed, social.mentorship.contract.terminated, social.mentorship.lesson.completed
# - API Base: /api/v1/social/mentorship/*
```

---

## ✅ Детальный план

1. **Определить модель контракта:** тип (базовый/премиальный/корпоративный/академический), роли, требования по репутации/довериям, сроки, компенсации.  
2. **Описать жизненный цикл:** `draft → active → suspended → completed/terminated`, условия переходов, SLA арбитража.  
3. **Спроектировать схемы:** `MentorshipContract`, `MentorshipContractCreateRequest`, `MentorshipContractUpdateRequest`, `MentorshipContractTerminationRequest`, `MentorshipContractBreachReport`, `MentorshipContractPayment`, `MentorshipContractHistoryEntry`.  
4. **Определить эндпоинты:** создание, чтение, фильтры, обновление, termination, жалобы, история, выплаты, статистика.  
5. **Интеграции:** economy-service (escrowId, payouts), relationships/trust (требования, эффекты), analytics (метрики).  
6. **Документировать Kafka события и очереди (`mentorship-content-review`).**  
7. **Добавить примеры:** индивидуальный контракт, корпоративный договор академии, контракт с NPC-учителем, жалоба с штрафом.  
8. **Использовать shared security/responses/pagination, вынести схемы/примеры в компоненты.**  
9. **Добавить коды ошибок и бизнес-правила (`BIZ_MENTORSHIP_TRUST_TOO_LOW`, `BIZ_MENTORSHIP_CONTRACT_LOCKED`).**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/social/mentorship/contracts`** — создание договора.  
2. **GET `/social/mentorship/contracts/{contractId}`** — детальная карточка (условия, статус, прогресс).  
3. **GET `/social/mentorship/contracts`** — список/фильтры (тип, статус, mentorId, menteeId, academyId, дата).  
4. **PATCH `/social/mentorship/contracts/{contractId}`** — изменение условий (при необходимости двойного подтверждения).  
5. **POST `/social/mentorship/contracts/{contractId}/terminate`** — завершение/расторжение (указать reason, settlement).  
6. **POST `/social/mentorship/contracts/{contractId}/breach`** — жалоба на нарушение, запуск арбитража.  
7. **GET `/social/mentorship/contracts/{contractId}/history`** — история статусов, выплат, жалоб.  
8. **GET `/social/mentorship/contracts/{contractId}/payments`** — финансовые события (escrow, выплаты, штрафы).  
9. **GET `/social/mentorship/contracts/stats`** — агрегаты (active, breach rate, satisfaction score).  
10. **POST `/social/mentorship/contracts/{contractId}/ack`** — подтверждение условий учениками/академией.

---

## 🧱 Модели данных

- **MentorshipContract** — `contractId`, `programId`, `mentorId`, `menteeId`, `academyId`, `contractType`, `status`, `startAt`, `endAt`, `trustRequired`, `reputationRequired`, `payment`, `rewards`, `clauses[]`, `penalties[]`, `metadata`.  
- **MentorshipContractCreateRequest** — `programId`, `participants[]`, `type`, `goals`, `duration`, `paymentConfig`, `trustThresholds`, `resources`.  
- **MentorshipContractUpdateRequest** — изменения условий (уроки, награды, штрафы).  
- **MentorshipContractTerminationRequest** — причина, инициатор, компенсации, feedback.  
- **MentorshipContractBreachReport** — `reportId`, `contractId`, `reportedBy`, `reason`, `evidence`, `severity`.  
- **MentorshipContractPayment** — `paymentId`, `contractId`, `amount`, `currency`, `escrowId`, `status`, `releasedAt`.  
- **MentorshipContractHistoryEntry** — `eventType`, `timestamp`, `actor`, `details`.  
- **MentorshipContractStats** — агрегаты (`activeCount`, `suspendedCount`, `breachRate`, `avgSatisfaction`, `avgLessonCompletion`).  
- **MentorshipComplaintResolution** — результат арбитража (`decision`, `penalties`, `reputationImpact`).  
- **PaginatedMentorshipContracts** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; соблюсти лимит 400 строк (схемы/примеры вынести в `components`).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_MENTORSHIP_CONTRACT_INVALID`, `BIZ_MENTORSHIP_TRUST_TOO_LOW`, `BIZ_MENTORSHIP_CONTRACT_LOCKED`, `BIZ_MENTORSHIP_CONTRACT_BREACH_PENDING`, `INT_MENTORSHIP_ESCROW_FAILURE`.  
- В `info.description` указать `.BRAIN` источники и UX подтверждения.  
- Теги: `Mentorship`, `Contracts`, `Arbitration`, `Escrow`, `Trust`.  
- Обязательно описать Kafka события и связь с очередью `mentorship-content-review`.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/mentorship/contracts.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. Добавлен `Target Architecture` блок.  
3. Реализованы все заявленные эндпоинты и модели.  
4. Подключены shared security/responses/pagination.  
5. Документированы жизненный цикл контракта, штрафы, арбитраж.  
6. Добавлены примеры (индивидуальный контракт, корпоративный договор, контракт с NPC).  
7. Kafka события и очередь `mentorship-content-review` описаны.  
8. README в каталоге обновлён (в рамках реализации).  
9. Task отражён в `brain-mapping.yaml`.  
10. В `.BRAIN` документе обновлён блок `API Tasks Status`.  
11. Указаны зависимости на `mentorship/programs.yaml`, `relationships/status.yaml`, `player-orders/ratings.yaml`, `economy/player-orders/risk.yaml`.

---

## ❓ FAQ

**Q:** Можно ли привязать несколько учеников к одному контракту?  
A: Да, допускается `participants[]` с ролью `mentee`; контракт должен хранить квоты и распределение наград.  

**Q:** Как учитываются корпоративные/академические договоры?  
A: Добавить поля `sponsorId`, `academyPolicy`, `corporateTier`; при breach — уведомление world-service и санкции.  

**Q:** Нужно ли хранить шаблоны договоров?  
A: Можно ссылаться на `templateId` (из content-service/конфигураций), детали шаблонов вне scope.  

**Q:** Требуется поддержка подписания договора?  
A: Да, предусмотреть `acknowledgements[]` со статусами, подписью и временной меткой; отдельный эндпоинт `/ack`.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, описать интеграции, прогнать проверки и подготовить MR.

