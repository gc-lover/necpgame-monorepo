# Task ID: API-TASK-296
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 22:05
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-238], [API-TASK-239], [API-TASK-240]

---

## 📋 Краткое описание

Спроектировать набор REST/async спецификаций admin-service для ведения юридического пакета гибридных отсылок (Throne of Sand, Neon Popularity Reset, Quantum Reef Siege).

**Что нужно сделать:** На основе `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-hybrid-media-legal-package.md` описать API управления legal-пакетами, рисками, коммуникациями и статусами согласования, обеспечив трассировку материалов moodboard/audio и связь с игровыми кампаниями.

---

## 🎯 Цель задания

Создать единый контракт для юридической команды: сбор источников вдохновения, уникальных элементов проекта, рисков, коммуникаций и SLA по ответам.

**Зачем это нужно:**
- Централизовать legal approval для гибридных отсылок и контролировать трансформацию оригиналов.
- Обеспечить прозрачную историю рисков/мер и коммуникаций с юридическим отделом и владельцами направлений.
- Связать правовые статусы с игровыми кампаниями и API тасками 238–240 для синхронного запуска контента.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-hybrid-media-legal-package.md`  
**Версия документа:** 1.0.1  
**Дата последнего обновления:** 2025-11-07 16:14  
**Статус документа:** approved / api-readiness: ready

**Что важно из документа:**
- Раздел 1 — таблица вдохновений с уровнем трансформации (источники, тип отсылки).
- Раздел 2 — уникальные элементы проекта, требующие фиксации как доказательства оригинальности.
- Раздел 3 — матрица рисков и мер (Risk, Probability, Consequence, Mitigation, Owner).
- Раздел 6 — план коммуникации, SLA, сопроводительное письмо и лог отправок.
- Раздел 4/5 — чеклисты материалов и процесс согласования.

### Дополнительные источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/archive/2025-11-07-hybrid-media-references-expansion.md` — детальные ветки кампаний, moodboard/audio ID.
- `.BRAIN/06-tasks/config/legal-approvals-template.md` — формат хранения шаблона согласований.
- `.BRAIN/06-tasks/active/CURRENT-WORK/current-status.md` — приоритеты партии и SLA.
- `API-SWAGGER/tasks/active/queue/task-238-throne-of-sand-campaign-api.md` — боевой контур кампании.
- `API-SWAGGER/tasks/active/queue/task-239-neon-popularity-reset-api.md` — сезонный эвент.
- `API-SWAGGER/tasks/active/queue/task-240-quantum-reef-siege-raid-api.md` — рейдовый сценарий.

### Связанные документы

- `.BRAIN/06-tasks/ideas/2025-11-07-IDEA-hybrid-media-references.md` — исходная идея.
- `.BRAIN/05-technical/ui/main-game/ui-features.md` — ссылки на UI компоненты (Toast/Modal и др.).
- `.BRAIN/05-technical/backend/notification-system.md` — рассылка уведомлений о статусах.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевые файлы:**
- `api/v1/admin/legal/hybrid-media-references.yaml` — основной REST контракт.
- `api/v1/admin/legal/schemas/hybrid-legal-components.yaml` — вынести сложные схемы/enum (≤400 строк).
- `api/v1/admin/legal/events/hybrid-legal-events.yaml` — описать async события (при необходимости отделить для лимита).

```
API-SWAGGER/
└── api/
    └── v1/
        └── admin/
            └── legal/
                ├── hybrid-media-references.yaml      ← создать/обновить
                ├── schemas/
                │   └── hybrid-legal-components.yaml  ← создать
                └── events/
                    └── hybrid-legal-events.yaml      ← создать (если >400 строк)
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** admin-service
- **Порт:** 8088
- **API Base:** `/api/v1/admin/legal/*`
- **Ответственность:** legal/compliance трекинг, approvals, коммуникации, интеграции с notification-service и analytics-service.
- **Внешние зависимости:** auth-service (JWT, роли `legal.reviewer`, `legal.manager`), notification-service (рассылки), world-service / gameplay-service / social-service / economy-service (ссылки на элементы контента), analytics-service (SLA метрики), storage-service (файлы mood/audio), support-service (инциденты).

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/admin/legal/hybrid-packages`
- **State Store:** `useLegalComplianceStore`
- **State:** `packages`, `risks`, `communications`, `approvals`, `attachments`, `slaTimers`
- **UI компоненты:** `LegalPackageTable`, `InspirationDiffViewer`, `RiskMatrix`, `MitigationChecklist`, `CommunicationTimeline`, `ApprovalStatusCard`
- **Формы:** `LegalPackageForm`, `RiskAssessmentForm`, `MitigationUpdateForm`, `CommunicationLogForm`, `ApprovalDecisionForm`
- **Layouts:** `AdminLayout`
- **Хуки:** `useLegalPackages`, `useLegalSlaTimer`, `useLegalAttachments`, `useLegalNotifications`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: admin-service (port 8088)
# - API Base: /api/v1/admin/legal/*
# - Dependencies: auth, notification, analytics, storage, world, gameplay, social, economy, support
# - Frontend Module: modules/admin/legal/hybrid-packages (useLegalComplianceStore)
# - UI: LegalPackageTable, InspirationDiffViewer, RiskMatrix, MitigationChecklist, CommunicationTimeline, ApprovalStatusCard
# - Forms: LegalPackageForm, RiskAssessmentForm, MitigationUpdateForm, CommunicationLogForm, ApprovalDecisionForm
# - Hooks: useLegalPackages, useLegalSlaTimer, useLegalAttachments, useLegalNotifications
```

---

## ✅ Что нужно сделать (детальный план)

1. Извлечь из `.BRAIN` полный перечень источников, уникальных элементов, рисков и коммуникаций; сформировать сущности API.
2. Спроектировать CRUD/workflow endpoints для legal пакета, рисков, мер, коммуникаций, привязки moodboard/audio и отправки письма.
3. Описать интеграцию с notification-service (webhooks/email) и analytics-service (SLA метрики).
4. Определить роли, заголовки (`Authorization`, `X-Idempotency-Key`, `X-Legal-Package-Id`, `X-SLA-Deadline`) и re-use `shared/common/security.yaml`.
5. Подготовить Async события (`legal.package.created`, `legal.risk.updated`, `legal.communication.dispatched`, `legal.approval.status-changed`) с payload примерами.
6. Вынести схемы (`HybridReferencePackage`, `InspirationSource`, `RiskRegisterEntry`, etc.) в отдельный файл, соблюдая лимит 400 строк.
7. Добавить примеры запросов/ответов (минимум 70%), чеклист соответствия, критерии приемки, FAQ.

---

## 🔀 Требуемые эндпоинты

1. **GET `/api/v1/admin/legal/hybrid-packages`** — список пакетов, фильтры: `status`, `direction`, `owner`, `campaignId`, `slaState`, `updatedAfter`.
2. **POST `/api/v1/admin/legal/hybrid-packages`** — создать пакет; body содержит `title`, `directions[]`, `inspirationSources[]`, `uniqueElements[]`, `attachments[]`.
3. **GET `/api/v1/admin/legal/hybrid-packages/{packageId}`** — детальный просмотр (источники, уникальные элементы, риски, коммуникации, approvals, связанный контент).
4. **PUT `/api/v1/admin/legal/hybrid-packages/{packageId}`** — обновить общее описание, версии, ссылки на moodboard/audio, связать с API тасками.
5. **POST `/api/v1/admin/legal/hybrid-packages/{packageId}/links`** — добавить/обновить ссылки на связанные задачи (tasks 238-240), ресурсы storage, UI ID.
6. **POST `/api/v1/admin/legal/hybrid-packages/{packageId}/risks`** — зарегистрировать риск (fields: `riskCode`, `description`, `probability`, `impact`, `mitigationPlan`, `ownerRole`).
7. **PATCH `/api/v1/admin/legal/hybrid-packages/{packageId}/risks/{riskId}`** — изменить статус, скор, добавить доказательства (attachments).
8. **POST `/api/v1/admin/legal/hybrid-packages/{packageId}/mitigations`** — зафиксировать меру/чеклист, связанный с riskId, with `deadline`, `status`.
9. **POST `/api/v1/admin/legal/hybrid-packages/{packageId}/communications`** — лог коммуникации (fields: `channel`, `recipient`, `message`, `attachments`, `slaDueAt`).
10. **POST `/api/v1/admin/legal/hybrid-packages/{packageId}/communications/{communicationId}/dispatch`** — зафиксировать отправку письма (указать канал, `dispatchAt`, `trackingId`, `status`).
11. **POST `/api/v1/admin/legal/hybrid-packages/{packageId}/approvals`** — зафиксировать решение (`pending`, `approved`, `rejected`, `needs-info`) с полями `reviewer`, `decisionAt`, `notes`.
12. **PATCH `/api/v1/admin/legal/hybrid-packages/{packageId}/approvals/{approvalId}`** — обновить статус, добавить attachments, пересчитать SLA.
13. **POST `/api/v1/admin/legal/hybrid-packages/{packageId}/escalations`** — инициировать эскалацию (этап, причина, получатель, связанный incidentId).
14. **GET `/api/v1/admin/legal/hybrid-packages/{packageId}/timeline`** — хронология действий (создание, риски, коммуникации, approvals, эскалации).
15. **GET `/api/v1/admin/legal/hybrid-packages/{packageId}/metrics`** — SLA, количество рисков по статусам, среднее время ответа, блокирующие элементы.

Каждый эндпоинт использовать общие ответы через `$ref` (`shared/common/responses.yaml`, `shared/common/pagination.yaml`). Для POST/PATCH предусмотреть `Idempotency-Key`.

---

## 🧱 Модели данных

- **HybridReferencePackage** — `packageId`, `title`, `docVersion`, `directions[]`, `status`, `owner`, `createdAt`, `updatedAt`, `relatedTasks[]`, `moodboardLinks[]`, `audioLinks[]`, `sla`.
- **InspirationSource** — `direction`, `originalWork`, `referenceType`, `transformationLevel`, `comment`, `evidenceLinks[]`.
- **UniqueProjectElement** — `elementId`, `name`, `description`, `evidence`, `relatedSystems[]`.
- **RiskRegisterEntry** — `riskId`, `riskCode`, `description`, `probability`, `impact`, `severity`, `mitigationPlan`, `ownerRole`, `status`, `linkedApprovals[]`.
- **MitigationAction** — `actionId`, `riskId`, `action`, `owner`, `deadline`, `status`, `evidenceLinks[]`.
- **CommunicationRecord** — `communicationId`, `channel`, `recipient`, `messageSummary`, `attachments[]`, `preparedAt`, `dispatchedAt`, `slaDueAt`, `status`.
- **ApprovalRecord** — `approvalId`, `reviewer`, `role`, `decision`, `decisionAt`, `notes`, `attachments[]`, `nextSteps`.
- **EscalationEntry** — `escalationId`, `trigger`, `initiator`, `timestamp`, `route`, `incidentId`, `status`.
- **MetricsSnapshot** — `packageId`, `slaState`, `pendingRisks`, `overdueMitigations`, `approvalsPending`, `communicationsOpen`, `lastUpdated`.
- **LegalAttachmentLink** — `attachmentId`, `type`, `uri`, `checksum`, `sourceSystem`, `expiresAt`.
- **Async Events Payloads** — (см. events файл) `legal.package.created`, `legal.risk.updated`, `legal.communication.dispatched`, `legal.approval.status-changed`.

Схемы оформить в `schemas/hybrid-legal-components.yaml`, используя PascalCase, enums вынести отдельно.

---

## 🧭 Принципы и правила

- Обязательные scopes: `legal:read`, `legal:write`, `legal:approve`; добавить security из `shared/common/security.yaml`.
- Все мутационные операции требуют `X-Idempotency-Key` и audit trail (`x-audit-log` с payload в описании).
- SLA контроль: `slaDueAt`, `slaState` (enum: `in_time`, `due_soon`, `overdue`), рассчитывать в analytics-service; включить `x-sla` описания.
- Risk severity вычисляется из probability×impact → указать допустимые enum (`low`, `medium`, `high`, `critical`).
- Указывать связи с игровыми системами (world, social, gameplay, economy) через массив `relatedSystems`.
- При отправке письма формировать уведомление через notification-service webhook `legal.package.dispatched`.
- Соблюдать DRY: ошибки (400/401/403/404/409/422/500) использовать из `shared/common/responses.yaml`.
- Разделить файлы, если основной surpass >400 строк; комментарий `Target Architecture` обязателен.

---

## ✅ Критерии приемки (≥10)

1. Описаны все 15 эндпоинтов с параметрами, запросами, ответами и примерами.
2. Схемы вынесены в `schemas/hybrid-legal-components.yaml`, повторяющиеся блоки переиспользованы через `$ref`.
3. async события документированы с payload и примерами (минимум 3 события).
4. Используются стандартные ответы и безопасность через `shared/common` `$ref`.
5. Для всех POST/PATCH указан `Idempotency-Key` и аудит.
6. Определены SLA поля (`slaDueAt`, `slaState`) и описан расчёт в `description`.
7. Присутствует `x-target-architecture` комментарий из блока выше.
8. Для рисков/мер описаны enum состояний (например, `open`, `in_progress`, `mitigated`, `closed`).
9. Добавлены примеры payload: создание пакета, регистрация риска, лог отправки письма, 승인.
10. Указаны зависимости с tasks 238–240 и ссылками на moodboard/audio ID.
11. Документ проходит lint (без ошибок Spectral) и укладывается ≤400 строк (или разбит на несколько файлов).
12. FAQ и Checklist заполнены; указано, что после выполнения обновить brain-mapping и .BRAIN.

---

## 📎 Checklist перед сдачей

- [ ] Все разделы шаблона заполнены, ссылки на `.BRAIN` добавлены в `info.description`.
- [ ] Включены security схемы, роли и заголовки.
- [ ] Примеры запросов/ответов покрывают ≥70% эндпоинтов.
- [ ] async события описаны и связаны с notification-service.
- [ ] Проведена проверка размера файла; при необходимости вынесены схемы/события.
- [ ] Обновлён `tasks/config/brain-mapping.yaml` и `.BRAIN` документ после завершения.

---

## ❓ FAQ

**Q:** Как хранить большие вложения (moodboard/audio)?  
**A:** В API хранить только ссылки/ID и метаданные (`LegalAttachmentLink`), загрузка/хранение остаётся на storage-service. Указать ограничения в `description`.

**Q:** Нужно ли разделять пакеты по направлениям?  
**A:** Да, поле `directions[]` (enum: `throne-of-sand`, `neon-reset`, `quantum-reef`) позволяет вести один пакет с подразделами. При необходимости разрешить фильтрацию и частичное обновление.

**Q:** Как фиксировать само письмо?  
**A:** Использовать endpoint `communications` + `dispatch`: первый шаг — подготовка текста, второй — фиксация отправки с `trackingId`, SLA пересчитывается автоматически.

---

## 🔗 Связность и дальнейшие шаги

- После генерации API нужно обновить `brain-mapping.yaml` и `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-hybrid-media-legal-package.md` (секция API Tasks Status → status `queued`, task `API-TASK-296`).
- Сообщить в `CURRENT-WORK/current-status.md`, если SLA или зависимости изменятся.
- Координировать с tasks 238–240 при изменении уникальных элементов/рисков.


