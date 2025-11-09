# Task ID: API-TASK-341
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 18:55  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-340 (shared schemas и события)

---

## 📋 Краткое описание

Разработать OpenAPI спецификацию для `Trust Contracts API`, описывающую создание, управление и аудит договоров доверия между игроками, кланами и фракциями.  
**Целевой файл:** `api/v1/social/trust/contracts.yaml`

---

## 🎯 Цель задания

Обеспечить social-service контрактом, который:
- создаёт договоры доверия (trade/combat/strategic) с параметрами доступа к ресурсам, лимитами и сроками;
- управляет статусами договора (draft, active, suspended, revoked, completed);
- фиксирует escrow, доли прибыли, условия выхода и штрафы;
- обеспечивает аудит (история изменений, жалобы, арбитраж) и интеграцию с economy-service и notification-service;
- синхронизируется с рейтингами и отношениями, описанными в `relationships/status`.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:40  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**
- Раздел 4 — доверие (уровни, набор, визуализация, арбитраж).  
- Раздел 5 — союзы и договоры, процесс согласования, нарушения.  
- Раздел 7 — история взаимодействий и арбитраж.  
- Раздел 10 — REST черновики (`POST /social/trust/contracts`, `GET /social/trust/contracts/{contractId}`).  
- Раздел 11 — Kafka событие `social.trust.contract.created`.

### Дополнительные документы

- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` — escrow и экономические ограничения.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — влияние договоров на рейтинги, санкции и жалобы.  
- `.BRAIN/02-gameplay/economy/escrow-system.md` — механика эскроу.  
- `.BRAIN/03-lore/_03-lore/diplomacy/faction-treaties-детально.md` — типы стратегических договоров.  
- `.BRAIN/05-technical/content-generation/contracts-validation-service.md` — бизнес правила валидации.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/trust/contracts.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── trust/
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
- **Интеграции:** economy-service (escrow, финансы), world-service (альянсы), notification-service (alerts), analytics-service (TrustStabilityIndex).  
- **Kafka:** `social.trust.contract.created`, `social.trust.contract.updated`, `social.relationships.changed`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/relationships  
- **State Store:** `useSocialStore(trustContracts)`  
- **UI:** `TrustContractWizard`, `TrustContractList`, `TrustContractDetails`, `TrustContractTimeline`, `TrustViolationBanner`  
- **Формы:** `TrustContractForm`, `TrustClauseEditor`, `TrustContractTerminationForm`  
- **Layout:** `TrustContractsLayout`  
- **Hooks:** `useTrustContracts`, `useTrustContract`, `useCreateTrustContract`, `useTerminateTrustContract`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/relationships
# - State Store: useSocialStore(trustContracts)
# - UI: TrustContractWizard, TrustContractList, TrustContractDetails, TrustContractTimeline, TrustViolationBanner
# - Forms: TrustContractForm, TrustClauseEditor, TrustContractTerminationForm
# - Layout: TrustContractsLayout
# - Hooks: useTrustContracts, useTrustContract, useCreateTrustContract, useTerminateTrustContract
# - Events: social.trust.contract.created, social.trust.contract.updated, social.relationships.changed
# - API Base: /api/v1/social/trust/contracts/*
```

---

## ✅ Детальный план

1. **Собрать требования** по контрактам: тип, участники, роли, лимиты, escrow, санкции, доверие.  
2. **Спроектировать схемы:** `TrustContract`, `TrustClause`, `TrustEscrow`, `TrustParticipant`, `TrustContractCreateRequest`, `TrustContractUpdateRequest`, `TrustContractTerminationRequest`, `TrustContractHistoryEntry`, `TrustBreachReport`.  
3. **Определить жизненный цикл:** draft → active → suspended → revoked → completed.  
4. **Разработать эндпоинты:** создание, чтение, поиск/фильтры, обновление, termination, жалобы, история.  
5. **Учесть бизнес-правила:** проверки доверия, лимиты, приоритет арбитража, привязка к relationships.  
6. **Интеграции с economy-service:** escrowId, payouts; с notification-service — alerts.  
7. **Описать Kafka события и схемы сообщений.**  
8. **Подключить shared security/responses/pagination; вынести схемы/примеры в компоненты.**  
9. **Подготовить примеры договоров:** торговый, боевой, стратегический, breach case.  
10. **Финально — валидация скриптом и обновление README.**

---

## 🔌 Эндпоинты

1. **POST `/social/trust/contracts`** — создание договора.  
2. **GET `/social/trust/contracts/{contractId}`** — детальная карточка.  
3. **GET `/social/trust/contracts`** — поиск/фильтры (по типу, статусу, участнику, фракции).  
4. **PATCH `/social/trust/contracts/{contractId}`** — обновление условий (clause update).  
5. **POST `/social/trust/contracts/{contractId}/terminate`** — завершение/расторжение.  
6. **POST `/social/trust/contracts/{contractId}/breach`** — жалоба на нарушение.  
7. **GET `/social/trust/contracts/{contractId}/history`** — история изменений.  
8. **GET `/social/trust/contracts/stats`** — агрегаты (active, suspended, breach rate).

---

## 🧱 Модели данных

- **TrustContract** — id, type (`trade`, `combat`, `strategic`), status, participants[], trustLevelRequired, escrow, clauses[], privileges, penalties, metadata.  
- **TrustParticipant** — entityId, entityType (player, clan, faction), role, trustLevelContribution.  
- **TrustClause** — условия, лимиты, ресурсы, breachPolicy.  
- **TrustEscrow** — currency, amount, releaseRules, escrowProvider.  
- **TrustContractCreateRequest** — payload для создания.  
- **TrustContractUpdateRequest** — изменения (clause adjustments, privilege updates).  
- **TrustContractTerminationRequest** — причина, initiator, settlement.  
- **TrustBreachReport** — описание нарушения, severity, evidence.  
- **TrustContractHistoryEntry** — изменение статуса/условий.  
- **TrustContractStats** — агрегаты по статусам и типам.  
- **PaginatedTrustContracts** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; вынести схемы в components, соблюдать лимит 400 строк.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_TRUST_CONTRACT_INVALID`, `BIZ_TRUST_LEVEL_TOO_LOW`, `BIZ_TRUST_CONTRACT_LOCKED`, `BIZ_TRUST_CONTRACT_BREACH_PENDING`, `INT_TRUST_ESCROW_FAILURE`.  
- Указать связи с `relationships/status` (trust levels) и economy-service (escrow).  
- Добавить `tags`: `Trust Contracts`, `Relationships`, `Escrow`, `Arbitration`.  
- info.description — ссылаться на `.BRAIN` источники, отметить модуль фронтенда и UX прототипы.

---

## ✅ Критерии приемки

1. `api/v1/social/trust/contracts.yaml` создан/обновлён, проходит `scripts/validate-swagger.ps1`.  
2. Добавлен `Target Architecture` блок в начале.  
3. Описаны все эндпоинты и схемы, перечисленные выше.  
4. Подключены shared security/responses/pagination.  
5. Задокументированы события Kafka и интеграции с economy-service.  
6. Добавлены примеры (trade contract, combat pact, strategic treaty, breach case).  
7. README в `social/trust` обновлён (в рамках реализации).  
8. Добавлена запись в `brain-mapping.yaml`.  
9. Обозначены ссылки на `relationships/status` (зависимости).  
10. Учтены UX требования (wizard, timeline, alerts).

---

## ❓ FAQ

**Q:** Можно ли обновлять условия активного договора?  
A: Да, `PATCH` с проверкой доверия и уведомлением участников; при конфликте статус `pending-approval`.  

**Q:** Как работают escrow-платежи?  
A: Экономический сервис резервирует сумму; при breach — удержание штрафов. Нужно поле `escrowId` и ссылки на economy API.  

**Q:** Требуется ли поддержка автопродления?  
A: Да, включить флаги `autoRenew`, `renewalPeriod`, `renewalConditions`.  

**Q:** Что если доверие падает ниже порога?  
A: API возвращает `status: suspended`, инициирует уведомление и запись в историю; решение — через отдельный workflow.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, обновить README, прогнать проверку и оформить MR.

