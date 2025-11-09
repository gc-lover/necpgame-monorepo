# Task ID: API-TASK-255
**Тип:** API Generation
**Приоритет:** критический
**Статус:** completed
**Создано:** 2025-11-08 09:58
**Завершено:** 2025-11-08 23:10
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-253, API-TASK-254, API-TASK-205, API-TASK-188

## 📦 Результат

- Добавлены `chat-moderation.yaml`, `chat-moderation-components.yaml`, `chat-moderation-examples.yaml` (жалобы, баны, фильтры, авто-ban, <400 строк).
- Задокументированы проверки сообщений, правила фильтрации, интеграции с анти-читом/поддержкой, события Kafka и коды ошибок `BIZ_CHAT_MOD_*`, `VAL_CHAT_MOD_*`, `INT_CHAT_MOD_*`.
- Обновлены `brain-mapping.yaml`, `.BRAIN/05-technical/backend/chat/chat-moderation.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Подготовить OpenAPI спецификацию для модерации чата: фильтрация, антиспам, жалобы, баны, аудит и автоматические санкции.

**Что нужно сделать:** Создать файл `chat-moderation.yaml`, описывающий REST API для управления фильтрами, банами, жалобами и интеграцией с анти-читом.

---

## 🎯 Цель задания

Оснастить social-service прозрачным интерфейсом модерации, позволяющим автоматизировать фильтрацию сообщений, реакцию на жалобы и выдачу банов с аудитом.

**Зачем это нужно:**
- Защитить игроков от токсичного поведения и спама
- Синхронизировать работу клиентских фильтров и серверной модерации
- Обеспечить отчётность и контроль прав модераторов

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/05-technical/backend/chat/chat-moderation.md`
**Версия:** v1.0.0
**Дата обновления:** 2025-11-07 05:30
**Статус:** approved

**Содержит:**
- Таблица `chat_bans`, индексы и правила длительности
- Логика `ModerationService` (фильтр слов, URL, CAPS)
- `SpamDetector` с rate-limit и дубликатами
- Endpoints `/chat/report`, `/chat/ban`, `/chat/bans`, `/chat/bans/{id}`
- Auto-ban система и уведомления WebSocket

### Дополнительные источники

- `.BRAIN/05-technical/backend/chat/chat-channels.md` — связь каналов и модерации
- `.BRAIN/05-technical/backend/chat/chat-features.md` — команды `/report`, `/ignore`
- `.BRAIN/05-technical/backend/anti-cheat/anti-cheat-compact.md` — санкции
- `.BRAIN/05-technical/backend/security-audit.md` — логирование

### Связанные документы

- `.BRAIN/05-technical/backend/notification-system.md` — уведомления о банах
- `.BRAIN/05-technical/backend/support/support-ticket-system.md` — эскалации

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/social/chat/chat-moderation.yaml`
**API версия:** v1
**Тип:** OpenAPI 3.0.3

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── chat/
                ├── chat-channels.yaml
                ├── chat-features.yaml
                └── chat-moderation.yaml ← создать
```

**Требования:**
- В Target Architecture указать: social-service, модуль `modules/social/chat/moderation`
- Схемы: `ChatReportRequest`, `ChatBan`, `ModerationRule`, `SpamCheckResult`
- Поддержка ролей (`ROLE_CHAT_MODERATOR`, `ROLE_SUPPORT_AGENT`)

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** social-service
- **Порт:** 8084
- **Base Path:** `/api/v1/chat/moderation/*`
- **Интеграции:**
  - Feign `anti-cheat-service` → `flagPlayer`
  - Feign `support-service` → `createTicket`
  - Feign `security-audit-service` → `recordAudit`
- **Kafka события:** `chat.moderation.reported`, `chat.moderation.ban.issued`, `chat.moderation.ban.expired`

### Frontend
- **Модуль:** `modules/social/chat/moderation`
- **State Store:** `useModerationStore` (`reports`, `activeBans`, `rules`)
- **UI:** `ReportInbox`, `BanList`, `ModerationDashboard`
- **Формы:** `@shared/forms/ChatBanForm`, `@shared/forms/ModerationRuleForm`

### Примечания
- Документировать заголовки `X-Moderator-Id`, `X-Audit-Reason`
- Указать SLA: auto-ban запись ≤ 500 мс от события

---

## 🔧 Детальный план выполнения

1. Создать разделы: `Reports`, `Bans`, `Filters`, `Rules`, `Audit`.
2. Описать модели `ChatReportRequest`, `ChatBanRequest`, `ModerationRule`, `SpamCheckResult`.
3. Добавить endpoints для подачи жалобы, проверки сообщений, выдачи и снятия банов, обновления словарей.
4. Уточнить интеграции с анти-читом и системой тикетов.
5. Документировать авто-ban pipeline и WebSocket уведомления.
6. Проверить и обновить mapping + `.BRAIN` документ.

---

## 🌐 Endpoints

### 1. POST `/api/v1/chat/report`
- Назначение: отправить жалобу на сообщение или игрока.
- Тело (`ChatReportRequest`): reporterId, messageId?, channelId?, accusedPlayerId, reason (enum), evidenceUrls?, comment.
- Ответы: 202 Accepted (`ReportTicket`), 400 Bad Request, 409 Conflict (дубликат), 422 Unprocessable Entity.

### 2. GET `/api/v1/chat/reports`
- Назначение: список открытых жалоб (модерация).
- Параметры: `status` (OPEN, IN_REVIEW, RESOLVED), `channelType?`, `page`, `pageSize` (≤100).
- Ответ: 200 OK (`ReportPage`).

### 3. POST `/api/v1/chat/reports/{reportId}/resolve`
- Назначение: задать решение по жалобе.
- Тело (`ReportResolutionRequest`): resolution (WARN, BAN, NO_ACTION), notes, appliedBanId?.
- Ответ: 200 OK (`ReportDetail`), 404 Not Found.

### 4. POST `/api/v1/chat/ban`
- Назначение: выдать бан.
- Тело (`ChatBanRequest`): playerId, channelType?, channelId?, reason, durationMinutes?, severity (LOW/MEDIUM/HIGH), evidence.
- Ответы: 201 Created (`ChatBan`), 409 Conflict (есть активный бан).

### 5. GET `/api/v1/chat/bans`
- Назначение: список активных банов.
- Параметры: `playerId?`, `channelType?`, `includeExpired?`, `page`.
- Ответ: 200 OK (`ChatBanPage`).

### 6. DELETE `/api/v1/chat/bans/{banId}`
- Назначение: снять бан досрочно.
- Ответ: 204 No Content, 404 Not Found.

### 7. POST `/api/v1/chat/moderation/filters/check`
- Назначение: проверить сообщение на нарушения (для клиента/серверов).
- Тело (`ModerationCheckRequest`): text, channelType, playerId.
- Ответ: 200 OK (`ModerationCheckResponse`), содержит `filteredText`, `violations`, `spamScore`.

### 8. PUT `/api/v1/chat/moderation/rules`
- Назначение: обновить словари запрещённых слов и правил фильтрации.
- Тело (`ModerationRuleSet`): bannedWords[], severeViolations[], urlWhitelist[], capsThreshold.
- Ответ: 200 OK, 403 Forbidden.

### 9. GET `/api/v1/chat/moderation/rules`
- Назначение: текущие правила (для клиентов/инфраструктуры).
- Ответ: 200 OK (`ModerationRuleSet`).

### 10. POST `/api/v1/chat/moderation/auto-ban`
- Назначение: инициировать автоматический бан по событию (системный вызов).
- Тело (`AutoBanTrigger`): playerId, source (SPAM, PROFANITY, CHEAT_ALERT), confidence.
- Ответ: 202 Accepted, 409 Conflict.

Ошибки: `ErrorResponse` с кодами `BIZ_CHAT_MOD_*`, `VAL_CHAT_MOD_*`, `INT_CHAT_MOD_*`.

---

## 🧱 Модели данных

### ChatReportRequest
- `reporterId` (uuid)
- `messageId?` (uuid)
- `channelId?`
- `accusedPlayerId` (uuid)
- `reason` (enum: ABUSE, SPAM, HATE, SCAM, OTHER)
- `evidenceUrls` (array<string>)
- `comment` (string ≤500)

### ReportTicket
- `reportId`
- `status`
- `createdAt`
- `priority` (enum: NORMAL, HIGH, CRITICAL)
- `assignedModeratorId?`

### ChatBan
- `banId`
- `playerId`
- `channelType?`
- `channelId?`
- `reason`
- `issuedBy`
- `issuedAt`
- `expiresAt?`
- `severity`
- `isActive`

### ModerationCheckResponse
- `filteredText`
- `violations` (array<Violation>)
- `spamScore` (0-1)
- `autoBanTriggered` (boolean)

### Violation
- `type` (enum: PROFANITY, URL, CAPS, REPEAT, SEVERE)
- `severity` (LOW/MEDIUM/HIGH)
- `context` (string)

### ModerationRuleSet
- `bannedWords` (array<string>)
- `severeViolations` (array<string>)
- `urlWhitelist` (array<string>)
- `capsThreshold` (integer)
- `repeatCharLimit` (integer)
- `updatedAt`

### AutoBanTrigger
- `playerId`
- `source`
- `confidence` (0-1)
- `metadata` (object)

---

## 🔄 Service Communication

### Feign Clients
- `anti-cheat-service`: `POST /internal/anti-cheat/flags` при severe нарушениях
- `support-service`: `POST /internal/support/tickets` для эскалаций
- `security-audit-service`: `POST /internal/audit/logs`

### Event Bus
- Publishes: `chat.moderation.reported`, `chat.moderation.ban.issued`, `chat.moderation.warning.sent`
- Subscribes: `chat.command.executed` (для /report), `anti-cheat.alert`

### WebSocket
- `/topic/chat/moderation/{playerId}` — уведомления о бане/варнинге

---

## 🗄️ Database

- **Schema:** `chat`
- **Tables:**
  - `chat_reports` — жалобы, индекс по `status`
  - `chat_bans`
  - `chat_moderation_rules`
  - `chat_violation_log`
- **Redis:** rate-limit ключи `chat:spam:{playerId}` и `chat:report:{playerId}`

---

## 🧩 Frontend Usage

- **Компоненты:** `ReportInbox`, `BanTimeline`, `ModerationRuleEditor`
- **API:** `usePostChatReport`, `useGetChatBans`, `usePutChatModerationRules`
- **State Store:** `useModerationStore`
- **Пример:**
```typescript
const { data: bans } = useGetChatBans({ playerId });

return bans?.items.map(ban => (
  <BanTimelineItem key={ban.banId} ban={ban} />
));
```

---

## 📝 Implementation Notes

- В спецификации указать role-based доступ: `ROLE_CHAT_MODERATOR` для чтения/выдачи банов, `ROLE_SUPPORT_AGENT` для эскалаций.
- Rate-limit на жалобы: 5 жалоб в час, описать в ответах (429).
- Фильтрация сообщений должна возвращать `filteredText` и список нарушений.
- Автозавершение банов — cron job каждые 5 минут, документировать.
- Указать, что все действия пишутся в аудит с `X-Audit-Reason`.

---

## ✅ Acceptance Criteria

1. Файл `chat-moderation.yaml` создан и проходит чеклист.
2. Описаны жалобы, баны, фильтры, правила и авто-ban.
3. Схемы ChatReportRequest, ChatBan, ModerationRuleSet, ModerationCheckResponse присутствуют.
4. Коды ошибок используют префиксы `BIZ_CHAT_MOD_*`, `VAL_CHAT_MOD_*`, `INT_CHAT_MOD_*`.
5. Задокументированы event bus и интеграции с анти-читом/поддержкой.
6. Указаны требования по ролям и заголовкам аудита.
7. Обновлён `brain-mapping.yaml` и `.BRAIN/05-technical/backend/chat/chat-moderation.md` с задачей `API-TASK-255`.
8. Пример фронтенда использует Orval-клиент.

---

## ❓ FAQ

**В:** Как обрабатывать массовые жалобы на одного игрока?

**О:** Спецификация должна описать авто-повышение приоритета, если `reportCount > 5` за 10 минут (status → CRITICAL).

**В:** Можно ли частично банить (только торговый канал)?

**О:** Да, `channelType` и `channelId` задают область бана, документировать примеры.

**В:** Как интегрируется с анти-читом?

**О:** При severe нарушения отправляется `anti-cheat-service` flag и создаётся запись в `chat_violation_log`.

**В:** Что делать при ложных срабатываниях фильтра?

**О:** Endpoint `/moderation/rules` поддерживает обновление whitelist; описать workflow отмены бана через `DELETE /bans/{id}`.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

