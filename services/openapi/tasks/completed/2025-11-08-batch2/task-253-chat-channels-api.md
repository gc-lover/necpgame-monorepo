# Task ID: API-TASK-253
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** completed
**Создано:** 2025-11-08 09:53
**Завершено:** 2025-11-08 22:40
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-135, API-TASK-193, API-TASK-254, API-TASK-255

## 📦 Результат

- Добавлены `chat-channels.yaml`, `chat-channels-components.yaml`, `chat-channels-examples.yaml` (REST + Kafka, <400 строк).
- Описаны каталог каналов, вступление/выход, настройки, модерация, suspend; определены модели `ChannelDefinition`, `ChannelSettings`, `ChannelMembership`.
- Обновлены `brain-mapping.yaml`, `.BRAIN/05-technical/backend/chat/chat-channels.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию для управления чат-каналами: создание, присоединение, правила доступа, cooldown, лимиты сообщений и типы каналов (global, party, guild, whisper, combat).

**Что нужно сделать:** Создать файл `chat-channels.yaml` с детальным описанием REST API для каналов, их настроек и структуры.

---

## 🎯 Цель задания

Предоставить social-service прозрачный контракт для управления каналами, чтобы фронтенд и игровые сервисы могли работать с единой моделью чатов.

**Зачем это нужно:**
- Обеспечить согласованную конфигурацию каналов между клиентами и сервером
- Позволить модерации и системам эвентов расширять каналы
- Встроить контроль доступа, cooldown и ограничения на уровне API

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/05-technical/backend/chat/chat-channels.md`
**Версия:** v1.0.0
**Дата обновления:** 2025-11-07 05:30
**Статус:** approved

**Ключевые элементы:**
- Таблица `chat_channels`, поля для типов, permissions, cooldown, members
- Перечень типов каналов (GLOBAL, LOCAL, PARTY, RAID, GUILD, WHISPER, TRADE, SYSTEM)
- ALGORITHMS получения получателей и ограничения по длине сообщений
- Endpoints `GET /channels`, `POST /channels/join`, `POST /channels/leave`, `GET /channels/{type}/members`

### Дополнительные источники

- `.BRAIN/05-technical/backend/chat/chat-moderation.md` — ограничения, фильтрация
- `.BRAIN/05-technical/backend/chat/chat-features.md` — команды и форматирование
- `.BRAIN/05-technical/backend/session-management/part1-lifecycle-heartbeat.md` — управление сессиями игроков
- `.BRAIN/05-technical/backend/guild-system-backend.md` — доступ офицеров и клановые каналы

### Связанные документы

- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — привязка голосовых каналов к чатам
- `.BRAIN/05-technical/backend/notification-system.md` — системные уведомления

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/social/chat/chat-channels.yaml`
**API версия:** v1
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── chat/
                ├── README.md (краткое описание модулей чата)
                ├── chat-channels.yaml ← добавить
                ├── chat-features.yaml (будет создан отдельно)
                └── chat-moderation.yaml (будет создан отдельно)
```

**Требования:**
- Включить Target Architecture и ссылки на общие компоненты (`bearerAuth`, `ErrorResponse`)
- Выделить схемы ChannelDefinition, ChannelMembership, ChannelPermissions
- Описать rate-limit и TTL для локальных каналов

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** social-service
- **Порт:** 8084
- **Base Path:** `/api/v1/chat/channels/*`
- **Интеграции:**
  - Feign `guild-service` → `getGuildMembers`
  - Feign `party-service` → `getPartyMembers`
  - Redis для membership cache
- **Kafka события:**
  - Publishes: `chat.channel.created`, `chat.channel.updated`, `chat.channel.closed`
  - Subscribes: `guild.member.joined`, `party.updated`

### Frontend
- **Модуль:** `modules/social/chat`
- **State Store:** `useChatStore` (`channels`, `activeChannel`, `members`)
- **UI компоненты:** `ChannelPicker`, `ChannelSettingsModal`, `ChannelBadge`
- **Формы:** `@shared/forms/ChannelCreateForm`
- **Layouts:** `@shared/layouts/SocialLayout`

### Примечания
- Document `X-Channel-Scope` header (GLOBAL, LOCAL, GROUP, PRIVATE)
- Указать максимальный размер сообщения (по типу канала)

---

## 🔧 Детальный план выполнения

1. Сформировать разделы: `Channel Catalog`, `Membership`, `Permissions`, `Administration`.
2. Создать схемы `ChannelDefinition`, `ChannelSettings`, `ChannelMember`, `JoinChannelRequest`.
3. Описать endpoints для листинга, присоединения, выхода, обновления настроек и получения участников.
4. Зафиксировать классификацию каналов (global/local/group/private/combat) и ограничения.
5. Добавить описание Redis/TTL и событий Kafka в соответствующих секциях.
6. Проверить спецификацию чеклистом, обновить mapping и `.BRAIN` файл.

---

## 🌐 Endpoints

### 1. GET `/api/v1/chat/channels`
- Назначение: список каналов, доступных игроку.
- Параметры: `scope?` (GLOBAL, LOCAL, GROUP, PRIVATE), `includeSystem` (bool), `zoneId?`.
- Ответ: 200 OK (`ChannelList`), 401 Unauthorized.

### 2. POST `/api/v1/chat/channels/join`
- Назначение: присоединиться к каналу (party, guild, custom).
- Тело (`JoinChannelRequest`): channelId?, channelType, inviteCode?, partyId?, guildId?.
- Ответы: 200 OK (`ChannelMembership`), 403 Forbidden (нет доступа), 404 Not Found.

### 3. POST `/api/v1/chat/channels/leave`
- Назначение: покинуть канал.
- Тело (`LeaveChannelRequest`): channelId, channelType.
- Ответы: 204 No Content, 404 Not Found.

### 4. POST `/api/v1/chat/channels`
- Назначение: создать кастомный канал (private, event).
- Тело (`CreateChannelRequest`): channelName, channelType (CUSTOM, EVENT), settings (cooldown, maxMembers, permissions).
- Ответы: 201 Created (`ChannelDefinition`), 409 Conflict (имя занято), 422 Unprocessable Entity.

### 5. PATCH `/api/v1/chat/channels/{channelId}`
- Назначение: обновить настройки канала (админ/владелец).
- Тело (`UpdateChannelSettingsRequest`): messageCooldown, maxMessageLength, permissions, moderators.
- Ответы: 200 OK (`ChannelDefinition`), 403 Forbidden, 404 Not Found.

### 6. GET `/api/v1/chat/channels/{channelType}/members`
- Назначение: получить участников канала.
- Параметры: `channelId` (uuid/string), `onlineOnly?`, `limit` (≤500).
- Ответ: 200 OK (`ChannelMembersPage`).

### 7. GET `/api/v1/chat/channels/catalog`
- Назначение: метаданные типов каналов (cooldown, длина, scope).
- Ответ: 200 OK (`ChannelCatalog`).

### 8. POST `/api/v1/chat/channels/{channelId}/moderators`
- Назначение: назначить модераторов.
- Тело (`ChannelModeratorsRequest`): add[], remove[].
- Ответы: 200 OK, 403 Forbidden, 404 Not Found.

### 9. POST `/api/v1/chat/channels/{channelId}/suspend`
- Назначение: временно отключить канал (для эвентов или нарушений).
- Тело (`ChannelSuspendRequest`): reason, durationMinutes.
- Ответ: 202 Accepted, 403 Forbidden, 404 Not Found.

### 10. GET `/api/v1/chat/channels/{channelId}/settings`
- Назначение: получить текущие настройки канала (для UI).
- Ответ: 200 OK (`ChannelSettings`), 404 Not Found.

Ошибки: использовать `ErrorResponse` с кодами `BIZ_CHAT_CHANNEL_*`, `VAL_CHAT_CHANNEL_*`, `INT_CHAT_CHANNEL_*`.

---

## 🧱 Модели данных

### ChannelDefinition
- `channelId` (string/uuid)
- `channelType` (enum: GLOBAL, LOCAL, ZONE, PARTY, RAID, GUILD, GUILD_OFFICER, WHISPER, TRADE, SYSTEM, CUSTOM)
- `channelName` (string)
- `scope` (enum: SERVER, ZONE, PARTY, PRIVATE)
- `settings` (ChannelSettings)
- `ownerId?` (uuid)
- `createdAt` (date-time)

### ChannelSettings
- `messageCooldownSeconds` (integer)
- `maxMessageLength` (integer)
- `maxMembers` (integer?)
- `isPublic` (boolean)
- `isModerated` (boolean)
- `permissions` (ChannelPermissions)

### ChannelPermissions
- `canRead` (array<RolePermission>)
- `canWrite` (array<RolePermission>)
- `canModerate` (array<RolePermission>)

### RolePermission
- `type` (enum: ROLE, PLAYER)
- `value` (string/uuid)

### ChannelMembership
- `channelId`
- `playerId`
- `joinedAt`
- `role` (enum: MEMBER, MODERATOR, OWNER)
- `muted` (boolean)

### ChannelCatalog
- `channels` (array<ChannelTypeInfo>)
- `combatChannels` (array<ChannelTypeInfo>)

### ChannelTypeInfo
- `channelType`
- `scope`
- `cooldownSeconds`
- `maxMessageLength`
- `description`

---

## 🔄 Service Communication

### Feign Clients
- `guild-service`: `GET /internal/guilds/{guildId}/members`
- `party-service`: `GET /internal/party/{partyId}/members`
- `notification-service`: `POST /internal/notifications` для системных сообщений

### Events
- **Publishes:** `chat.channel.created`, `chat.channel.member.joined`, `chat.channel.member.left`
- **Subscribes:** `guild.member.kicked`, `party.disbanded`

### Redis / WS
- Channel membership кэш: `chat:channel:{channelId}:members`
- WS topic: `/topic/chat/{channelId}` для live сообщений

---

## 🗄️ Database

- **Schema:** `chat`
- **Tables:**
  - `chat_channels`
  - `chat_channel_members`
  - `chat_channel_permissions`
  - `chat_channel_suspend_log`
- **Indices:** по `channel_type`, `owner_id`, `is_active`
- TTL для временных каналов (EVENT) — поле `expires_at`

---

## 🧩 Frontend Usage

- **Feature:** `ChatSidebar`
- **API Client:** `useGetChatChannels`, `usePostChatChannelsJoin`
- **UI:** `ChannelPicker`, `ChannelBadge`
- **State Store:** `useChatStore` хранит список каналов и текущий
- **Пример:**
```typescript
const { data: channels } = useGetChatChannels({ scope: 'GLOBAL' });

return channels?.items.map(channel => (
  <ChannelBadge key={channel.channelId} channel={channel} />
));
```

---

## 📝 Implementation Notes

- Описать, что глобальные каналы создаются системой и недоступны для удаления.
- Указать лимиты: maxMembers = 5 для party, 15 для raid.
- Для LOCAL каналов — TTL 5 минут после выхода всех участников.
- Документировать header `X-Zone-Id` для локальных каналов.
- Предусмотреть scope `chat.channels.manage` для админских операций.

---

## ✅ Acceptance Criteria

1. Файл `chat-channels.yaml` создан в директории `api/v1/social/chat`.
2. Спецификация описывает все типы каналов и их параметры.
3. Реализованы схемы ChannelDefinition, ChannelSettings, ChannelPermissions, ChannelMembership.
4. Каждый endpoint документирован с примерами и кодами ошибок.
5. События Kafka и интеграции с партиями/гильдиями перечислены.
6. В Target Architecture указан social-service и модуль фронтенда.
7. Файл проходит проектный чеклист без замечаний.
8. `brain-mapping.yaml` включает запись `API-TASK-253` со статусом queued.
9. `.BRAIN/05-technical/backend/chat/chat-channels.md` обновлён блоком API Tasks Status.
10. Frontend пример использует Orval-клиент.

---

## ❓ FAQ

**В:** Как обрабатывать whisper между игроками на разных серверах?

**О:** В спецификации описать, что whisper обходится через routing service и требует `targetShardId`.

**В:** Можно ли создавать временные event-каналы?

**О:** Да, endpoint `POST /channels` поддерживает тип `EVENT` с `expiresAt`, лимиты описать.

**В:** Как ограничить торговый канал от спама?

**О:** В `ChannelCatalog` указать cooldown 30 секунд и требование `ROLE_TRADE_UNLOCKED`.

**В:** Что если зона меняется?

**О:** Клиент вызывает `GET /channels` с новым `zoneId`; локальные каналы пересоздаются автоматически, описать это поведение.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

