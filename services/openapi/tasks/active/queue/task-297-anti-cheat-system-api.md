# Task ID: API-TASK-297
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 22:40
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-197], [API-TASK-223], [API-TASK-247]

---

## 📋 Краткое описание

Подготовить спецификацию admin-service для античит-сервиса: обработка телеметрии, расследование нарушений, санкции, интеграция с realtime/геймплей сервисами.

**Что нужно сделать:** На основе `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` описать REST и событийные контракты anti-cheat системы, учитывая валидацию скорости/позиции, лимит действий и reconciliation из realtime-сервера.

---

## 🎯 Цель задания

Создать полный контракт античит-подсистемы, чтобы юридически корректно фиксировать нарушения, быстро реагировать на подозрительные действия и синхронизировать результаты с другими сервисами.

**Зачем это нужно:**
- Централизованное хранение отчётов о подозрительном поведении и автоматических триггеров.
- Возможность для модераторов оперативно подтверждать/отклонять нарушения и применять санкции.
- Синхронизация античит статусов с игровыми событиями (арены, лут-хант, рейды) и аналитикой.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-07 02:18  
**Статус документа:** approved / api-readiness: ready

**Что важно из этого документа:**
- Ядро античита: speed hack detection, position validation, action rate limiting, client-server reconciliation.
- Требование опираться на `realtime-server/part2-protocol-optimization.md` для lag compensation и валидации.
- Указание, что реализация строится на Cron/плановых проверках и непрерывной телеметрии.

### Дополнительные источники

- `.BRAIN/05-technical/backend/realtime-server/part2-protocol-optimization.md` — детали reconciliation и проверки скорости/позиции.
- `.BRAIN/05-technical/backend/party-system.md`, `.BRAIN/02-gameplay/combat/arena-system.md` — геймплейные сценарии, требующие anti-cheat интеграции.
- `.BRAIN/05-technical/backend/notification-system.md` — алерты для админов/гильдий.
- `.BRAIN/05-technical/backend/session/` и `session-management/` — heartbeat и reconnect сценарии для античита.

### Связанные документы

- `.BRAIN/05-technical/infrastructure/anti-cheat-system.md` — высокоуровневая инфраструктура (task-161).
- `.BRAIN/06-tasks/active/CURRENT-WORK/archive/2025-11-07-hybrid-media-references-expansion.md` — интеграция античита с эвентами (используется в зависимых тасках 238–240).

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевые файлы:**
- `api/v1/admin/anti-cheat/anti-cheat-core.yaml` — основной REST контракт (≤400 строк).
- `api/v1/admin/anti-cheat/schemas/anti-cheat-components.yaml` — вынести сложные схемы/enum.
- `api/v1/admin/anti-cheat/events/anti-cheat-events.yaml` — realtime события (WebSocket/SSE/Queue).

```
API-SWAGGER/
└── api/
    └── v1/
        └── admin/
            └── anti-cheat/
                ├── anti-cheat-core.yaml            ← создать/дополнить
                ├── schemas/
                │   └── anti-cheat-components.yaml  ← создать
                └── events/
                    └── anti-cheat-events.yaml      ← создать (если core >400 строк)
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** admin-service
- **Порт:** 8088
- **API Base Path:** `/api/v1/admin/anti-cheat/*`
- **Подсистемы:** отчетность, расследования, санкции, интеграция с realtime/notification.
- **Зависимости:** realtime-service (telemetry), gameplay-service (arena/raid hooks), social-service (репутация), economy-service (блокировка торговли), session-service (kick/logout), notification-service (алерты), analytics-service (метрики), storage-service (лог файлы/повторы).

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/admin/anti-cheat`
- **State Store:** `useAntiCheatStore`
- **State:** `telemetryOverview`, `pendingReviews`, `violations`, `sanctions`, `alerts`, `evidence`
- **UI компоненты:** `TelemetryHeatmap`, `ViolationTimeline`, `PlayerTraceViewer`, `SanctionDecisionPanel`, `AlertFeed`, `EvidenceAttachmentCard`
- **Формы:** `ViolationReviewForm`, `SanctionApplyForm`, `ManualFlagForm`, `AppealDecisionForm`
- **Хуки:** `useTelemetryStream`, `useViolationQueue`, `useSanctionWorkflow`, `useAppealTracker`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: admin-service (port 8088)
# - API Base: /api/v1/admin/anti-cheat/*
# - Dependencies: realtime, gameplay, social, economy, session, notification, analytics, storage
# - Frontend Module: modules/admin/anti-cheat (useAntiCheatStore)
# - UI: TelemetryHeatmap, ViolationTimeline, PlayerTraceViewer, SanctionDecisionPanel, AlertFeed, EvidenceAttachmentCard
# - Forms: ViolationReviewForm, SanctionApplyForm, ManualFlagForm, AppealDecisionForm
# - Hooks: useTelemetryStream, useViolationQueue, useSanctionWorkflow, useAppealTracker
```

---

## ✅ Что нужно сделать (детальный план)

1. Проанализировать источники `.BRAIN` и собрать список сценариев: авто-детекция (скорость, позиция, input spam), ручные расследования, санкции, апелляции, интеграция с телеметрией.
2. Спроектировать REST endpoints для получения телеметрии, вручную созданных кейсов, подтверждения нарушений, применения санкций, регистрации апелляций, обмена данными с gameplay/service.
3. Описать webhook/WS/SSE события (`anti-cheat.alert`, `anti-cheat.violation.confirmed`, `anti-cheat.sanction.applied`, `anti-cheat.appeal.updated`) и payload с ссылками на схемы.
4. Вынести повторяемые схемы в `anti-cheat-components.yaml`: `TelemetrySnapshot`, `ViolationCase`, `SanctionRecord`, `EvidenceAttachment`, `AppealRecord`, enum причин/статусов.
5. Добавить требования к безопасности (JWT + роли `anti-cheat.read`, `anti-cheat.review`, `anti-cheat.sanction`), `X-Idempotency-Key` для POST/PATCH, интеграцию с notification-service (webhook) и realtime stream.
6. Разработать примеры запросов/ответов (минимум 70%): создание кейса, подтверждение нарушения, применение санкции, подписка на события.
7. Заполнить критерии приемки, чеклист, FAQ и описать шаги обновления `brain-mapping.yaml` и `.BRAIN` документа после выполнения.

---

## 🔀 Требуемые эндпоинты

1. `GET /api/v1/admin/anti-cheat/telemetry` — агрегация телеметрии и аномалий (фильтры: `type`, `severity`, `zone`, `timeRange`, `playerId`).
2. `GET /api/v1/admin/anti-cheat/telemetry/{telemetryId}` — подробная запись + ссылки на сырой лог/реплей.
3. `POST /api/v1/admin/anti-cheat/violations` — создание кейса (автоматически или вручную); поля: `source`, `playerId`, `suspicion`, `evidence[]`, `detectedAt`.
4. `GET /api/v1/admin/anti-cheat/violations` — очередь расследований (фильтры по статусу, типу нарушения, зоне, активности).
5. `POST /api/v1/admin/anti-cheat/violations/{violationId}/review` — решение ревьюера (`decision`, `notes`, `confidenceScore`, `evidence[]`).
6. `POST /api/v1/admin/anti-cheat/violations/{violationId}/sanction` — применение санкции (`type`, `duration`, `scope`, `notifyPlayer`, `linkedSessions[]`).
7. `POST /api/v1/admin/anti-cheat/sanctions/{sanctionId}/lift` — снятие санкции (с указанием причины/апелляции).
8. `GET /api/v1/admin/anti-cheat/players/{playerId}/history` — история нарушений, санкций, апелляций, статистика.
9. `POST /api/v1/admin/anti-cheat/appeals` — регистрация апелляции (игрок или оффлайн запрос).
10. `PATCH /api/v1/admin/anti-cheat/appeals/{appealId}` — обновление статуса (`accepted`, `rejected`, `needs-info`) и действий.
11. `POST /api/v1/admin/anti-cheat/manual-flags` — добавление ручного флага (`reason`, `expiresAt`, `autoSanctionEnabled`).
12. `POST /api/v1/admin/anti-cheat/telemetry/import` — загрузка батча телеметрии (архив, CSV, JSON) для оффлайн анализа.
13. `GET /api/v1/admin/anti-cheat/realtime/settings` + `PATCH` — конфигурация порогов (speed multiplier, action rate, teleport distance).
14. `POST /api/v1/admin/anti-cheat/notifications/test` — тест уведомлений (webhook/email) для проверки интеграций.
15. `GET /api/v1/admin/anti-cheat/metrics` — SLA по обработке, количество нарушений, подтверждений, ложных срабатываний, среднее время ревью.

Все ответы использовать общие компоненты (`shared/common/responses.yaml`, `shared/common/pagination.yaml`). Ошибки: 400/401/403/404/409/422/500 через `$ref`.

---

## 🧱 Модели данных

- **TelemetrySnapshot** — `telemetryId`, `playerId`, `zoneId`, `timestamp`, `speed`, `position`, `latency`, `inputRate`, `anomalyScore`, `source`.
- **ViolationCase** — `violationId`, `playerId`, `detectedAt`, `type`, `description`, `detectedBy`, `status`, `confidenceScore`, `telemetryLinks[]`, `evidence[]`.
- **EvidenceAttachment** — `attachmentId`, `type` (`log`, `video`, `screenshot`, `replay`), `uri`, `checksum`, `expiresAt`.
- **ReviewDecision** — `reviewerId`, `decision`, `notes`, `confidenceScore`, `decisionAt`, `escalatedTo`.
- **SanctionRecord** — `sanctionId`, `violationId`, `type` (`KICK`, `TEMP_BAN`, `PERMA_BAN`, `ROLLBACK`, `ITEM_REMOVE`), `duration`, `scope`, `appliedBy`, `appliedAt`, `status`.
- **PlayerHistorySummary** — `playerId`, `violationsCount`, `activeSanctions`, `lastViolationAt`, `totalAppeals`, `falsePositiveCount`.
- **AppealRecord** — `appealId`, `playerId`, `violationId`, `submittedBy`, `submittedAt`, `status`, `reviewerId`, `resolutionNotes`.
- **ManualFlag** — `flagId`, `playerId`, `reason`, `createdBy`, `createdAt`, `expiresAt`, `autoSanctionEnabled`.
- **RealtimeSettings** — `speedMultiplier`, `teleportThreshold`, `actionRateLimit`, `latencyTolerance`, `reconciliationWindow`.
- **NotificationTestRequest** — `channel`, `target`, `payload`, `result`.
- **AsyncEventPayloads** — (в events файле) `AntiCheatAlert`, `ViolationReviewed`, `SanctionApplied`, `AppealUpdated`.

---

## 🧭 Принципы и правила

- Использовать security схемы из `shared/common/security.yaml` с ролями `anti-cheat.read`, `anti-cheat.review`, `anti-cheat.sanction`.
- Для всех мутационных операций обязательны `X-Idempotency-Key` и `X-Audit-Id`.
- При подтверждении санкции необходимо синхронизировать с gameplay/economy/social сервисами (указать outbound webhook/commands).
- Реализовать SLA поля: `reviewDeadline`, `responseTime`, `slaState` (`in_time`, `due_soon`, `overdue`).
- Регистрировать ложные срабатывания и апелляции (важно для аналитики и доверия системы).
- Все ссылки на raw логи/реплеи оформлять через `storage-service` URL (подписи, TTL).
- Указывать лимиты на частоту запросов (rate limiting) и защиту от abuse.
- Придерживаться лимита 400 строк на файл — схемы/события выносить во вспомогательные файлы.

---

## ✅ Критерии приемки (минимум 10)

1. Описаны все 15 REST эндпоинтов с параметрами, запросами, ответами и примерами.
2. Схемы вынесены в `anti-cheat-components.yaml` и переиспользуются через `$ref`.
3. Async события документированы (минимум 3) с payload, примерами и каналами подключения.
4. Применены общие компоненты ответов и безопасности (`shared/common`).
5. Каждый POST/PATCH требует `Idempotency-Key` и описывает аудит (`X-Audit-Id`).
6. Поля SLA (`reviewDeadline`, `slaState`) присутствуют в ключевых схемах (`ViolationCase`, `AppealRecord`).
7. Отражены интеграции с realtime/gameplay/social/economy/session сервисами (описаны outbound события/вызовы).
8. Примеры покрывают ≥70% эндпоинтов (включая санкции, апелляции, телеметрию).
9. Размер основного файла ≤400 строк (иначе события/схемы вынесены).
10. `info.description` содержит ссылки на все `.BRAIN` документы и версию.
11. Добавлен `x-target-architecture` блок в начале спецификации.
12. Checklist и FAQ заполнены; указано, что после выполнения обновить `brain-mapping.yaml` и `.BRAIN` документ.

---

## 📎 Checklist перед сдачей

- [ ] Все разделы шаблона заполнены, ссылки на `.BRAIN` указаны.
- [ ] Endpoints + события + схемы покрывают сценарии детекции, расследования, санкций и апелляций.
- [ ] Примеры и ошибки оформлены через общие компоненты.
- [ ] Проверен размер файлов, вынесены повторяющиеся части.
- [ ] Добавлен `x-target-architecture` в YAML.
- [ ] Обновлены `tasks/config/brain-mapping.yaml` и `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` (секция API Tasks Status).

---

## ❓ FAQ

**Q:** Где хранить большие логи и видеозаписи?  
**A:** В `storage-service`; в API хранить только ссылки и метаданные (`EvidenceAttachment`), предусмотреть TTL и проверку доступа.

**Q:** Как учитывать лаг и ложные срабатывания?  
**A:** В `ViolationCase` добавить `confidenceScore`, `latencySnapshot` и SLA; при низком confidence не применять автоматические санкции без ревью.

**Q:** Нужно ли разделять санкции по доменам (PvE/PvP)?  
**A:** Да, добавить поле `scope` (enum: `GLOBAL`, `ARENA`, `LOOT_HUNT`, `RAID`, `SOCIAL`, `ECONOMY`) и описать влияние на соответствующие сервисы.

---

## 🔗 Связность и дальнейшие шаги

- После выпуска спецификации обновить `tasks/config/brain-mapping.yaml` и `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` (секция API Tasks Status).
- Синхронизировать работу с заданиями `API-TASK-197`, `API-TASK-223`, `API-TASK-247` — они используют античит контракты.
- При изменении античит порогов уведомить команды, работающие над аренами, лут-хантом и клановыми войнами.


