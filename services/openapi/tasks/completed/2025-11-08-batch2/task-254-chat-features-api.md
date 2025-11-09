# Task ID: API-TASK-254
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** completed
**Создано:** 2025-11-08 09:56
**Завершено:** 2025-11-08 22:55
**Исполнитель:** GPT-5 Codex (API Executor)
**Зависимости:** API-TASK-253, API-TASK-193, API-TASK-205, API-TASK-255

## 📦 Результат

- Добавлены `chat-features.yaml`, `chat-features-components.yaml`, `chat-features-examples.yaml` (команды, голос, перевод, история, <400 строк).
- Описаны slash-команды, форматирование, WebRTC join/leave/mute, перевод и история; определены коды `BIZ_CHAT_FEATURE_*`, `VAL_CHAT_FEATURE_*`, `INT_CHAT_FEATURE_*`.
- Обновлены `brain-mapping.yaml`, `.BRAIN/05-technical/backend/chat/chat-features.md`, `.BRAIN/06-tasks/config/implementation-tracker.yaml`.

---

## 📋 Краткое описание

Проработать OpenAPI спецификацию функциональности чата: slash-команды, rich formatting, voice chat (WebRTC), автоперевод и история сообщений.

**Что нужно сделать:** Создать файл `chat-features.yaml`, покрывающий REST-API для обработки команд, выдачи настроек форматирования, управления голосовыми каналами и истории сообщений.

---

## 🎯 Цель задания

Расширить social-service API, чтобы клиенты могли использовать расширенные возможности чата без дублирования логики и с учётом модульности (голос, переводы, история).

**Зачем это нужно:**
- Обеспечить единый интерфейс для slash-команд и форматирования
- Интегрировать голосовой чат с системами party/raid
- Реализовать сервис перевода и историю сообщений с кэшированием

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`
**Путь:** `.BRAIN/05-technical/backend/chat/chat-features.md`
**Версия:** v1.0.0
**Дата обновления:** 2025-11-07 05:30
**Статус:** approved

**Ключевые элементы:**
- Списки slash-команд (/help, /whisper, /party, /wave, /dance)
- Алгоритмы форматирования (bold, italic, links, mentions, emoji)
- Voice chat endpoints `/chat/voice/join`, `/leave`, `/participants`, `/mute`
- TranslationService и auto-translation примеры
- История сообщений с Redis кэшем и пагинацией

### Дополнительные источники

- `.BRAIN/05-technical/backend/chat/chat-channels.md` — привязка команд к каналам
- `.BRAIN/05-technical/backend/chat/chat-moderation.md` — фильтрация и антиспам
- `.BRAIN/05-technical/backend/voice-lobby/voice-lobby-system.md` — голосовые комнаты
- `.BRAIN/05-technical/backend/translation-service.md` (если существует) — словари

### Связанные документы

- `.BRAIN/05-technical/backend/notification-system.md` — уведомления о командах
- `.BRAIN/05-technical/backend/session-management/part2-reconnection-monitoring.md` — восстановление голосовых сессий

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/social/chat/chat-features.yaml`
**API версия:** v1
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── chat/
                ├── chat-channels.yaml
                ├── chat-features.yaml ← создать
                └── chat-moderation.yaml (будет создан)
```

**Требования:**
- Указать Target Architecture (social-service, modules/social/chat)
- Документировать WebRTC handshake (SDP, ICE) и перевод
- Выделить схемы ChatCommandRequest, VoiceSession, TranslationSettings, ChatHistoryResponse

---

## 🏗️ Целевая архитектура

### Backend
- **Микросервис:** social-service
- **Порт:** 8084
- **Base Path:** `/api/v1/chat/*`
- **Интеграции:**
  - Feign `voice-lobby-service` → `createVoiceChannel`
  - Feign `translation-service` → `translateBatch`
  - Redis для кэша истории
- **Kafka события:** `chat.command.executed`, `chat.voice.channel.created`

### Frontend
- **Модуль:** `modules/social/chat`
- **State Store:** `useChatStore` (`voiceChannels`, `translationPrefs`, `history`)
- **UI:** `ChatInput`, `CommandPalette`, `VoiceChannelPanel`, `TranslationToggle`
- **Формы:** `@shared/forms/ChatSettingsForm`, `@shared/forms/VoiceChannelJoinForm`
- **Хуки:** `@shared/hooks/useWebRTC`, `@shared/hooks/useInfiniteQuery`

### Примечания
- Описать обязательный header `X-Voice-Session-Id` для переговоров
- Указать лимиты: история — максимум 100 сообщений за запрос

---

## 🔧 Детальный план выполнения

1. Разделить спецификацию на: `Commands`, `Rich Formatting`, `Voice`, `Translation`, `History`.
2. Создать схемы `ChatCommandRequest`, `ChatCommandResult`, `VoiceJoinRequest`, `TranslationPreference`.
3. Задокументировать handshake для WebRTC (SDP/ICE endpoints) и mute/unmute.
4. Описать endpoint истории с учётом кэша и параметров пагинации.
5. Добавить таблицу поддерживаемых команд и форматирование в описания.
6. Проверить файл, добавить в mapping и обновить `.BRAIN` документ.

---

## 🌐 Endpoints

### 1. POST `/api/v1/chat/commands`
- Назначение: выполнить slash-команду.
- Тело (`ChatCommandRequest`): command, arguments[], channelId?, targetPlayer?
- Ответы: 200 OK (`ChatCommandResult`), 400 Bad Request (неизвестная команда), 403 Forbidden (нет прав), 429 Too Many Requests (спам).

### 2. GET `/api/v1/chat/commands/catalog`
- Назначение: получить список доступных команд и помощь.
- Параметры: `scope?` (GENERAL, PARTY, RAID, ADMIN).
- Ответ: 200 OK (`CommandCatalog`).

### 3. POST `/api/v1/chat/format`
- Назначение: отформатировать сообщение (preview).
- Тело (`FormatPreviewRequest`): rawText, channelType.
- Ответ: 200 OK (`FormatPreviewResponse`), описать HTML и безопасные теги.

### 4. POST `/api/v1/chat/voice/join`
- Назначение: подключение к голосовому каналу.
- Тело (`VoiceJoinRequest`): channelType, channelId, sdpOffer, deviceCapabilities.
- Ответы: 200 OK (`VoiceJoinResponse` с sdpAnswer), 403 Forbidden, 404 Not Found.

### 5. POST `/api/v1/chat/voice/leave`
- Назначение: покинуть голосовой канал.
- Тело (`VoiceLeaveRequest`): voiceSessionId.
- Ответ: 204 No Content.

### 6. GET `/api/v1/chat/voice/participants`
- Назначение: список участников голосового канала.
- Параметры: `voiceSessionId`.
- Ответ: 200 OK (`VoiceParticipantsResponse`).

### 7. POST `/api/v1/chat/voice/mute`
- Назначение: включить mute для себя или админом.
- Тело (`VoiceMuteRequest`): voiceSessionId, targetPlayerId?, mode (SELF, FORCE), durationSeconds?.
- Ответ: 200 OK, 403 Forbidden.

### 8. PUT `/api/v1/chat/settings/translation`
- Назначение: обновить настройки автоперевода пользователя.
- Тело (`TranslationSettingsRequest`): enabled, preferredLanguages[], autoDetect.
- Ответ: 200 OK (`TranslationSettings`).

### 9. POST `/api/v1/chat/translate`
- Назначение: выполнить ручной перевод сообщения.
- Тело (`TranslateMessageRequest`): text, targetLanguages[].
- Ответ: 200 OK (`TranslateMessageResponse`), 422 Unprocessable Entity (язык не поддерживается).

### 10. GET `/api/v1/chat/history/{channelType}`
- Назначение: получить историю сообщений.
- Параметры: `channelId`, `limit` (≤100), `beforeMessageId?`, `afterMessageId?`.
- Ответ: 200 OK (`ChatHistoryResponse`), 404 Not Found.

Ошибки: использовать `ErrorResponse` с кодами `BIZ_CHAT_FEATURE_*`, `VAL_CHAT_FEATURE_*`, `INT_CHAT_FEATURE_*`.

---

## 🧱 Модели данных

### ChatCommandRequest
- `command` (string, начинается с `/`)
- `arguments` (array<string>)
- `channelId?`
- `targetPlayerId?`
- `context` (CommandContext)

### ChatCommandResult
- `status` (enum: SUCCESS, INFO, ERROR)
- `message` (string)
- `payload` (object?)
- `cooldownSeconds?`

### FormatPreviewRequest
- `rawText` (string, ≤2000)
- `channelType` (enum)
- `allowLinks` (boolean)

### FormatPreviewResponse
- `html` (string)
- `mentions` (array<PlayerMention>)
- `emotes` (array<string>)

### VoiceJoinRequest
- `channelType` (enum: PARTY, RAID, GUILD, CUSTOM)
- `channelId` (uuid/string)
- `sdpOffer` (string)
- `deviceCapabilities` (array<string>)

### VoiceJoinResponse
- `voiceSessionId`
- `sdpAnswer`
- `iceServers` (array<IceServer>)
- `expiresAt`

### TranslationSettings
- `enabled` (boolean)
- `preferredLanguages` (array<string>)
- `autoDetect` (boolean)
- `lastUpdatedAt`

### ChatHistoryResponse
- `channelId`
- `channelType`
- `messages` (array<ChatMessage>)
- `hasMoreBefore` (boolean)
- `hasMoreAfter` (boolean)
- `nextCursor?`

### ChatMessage
- `messageId` (uuid)
- `senderId` (uuid)
- `displayName`
- `content` (string, HTML безопасное)
- `rawContent`
- `translatedContent?` (map<lang, string>)
- `sentAt` (date-time)
- `metadata` (MessageMetadata)

---

## 🔄 Service Communication

### Feign Clients
- `voice-lobby-service`: `POST /internal/voice-lobbies/{channelType}`
- `translation-service`: `POST /internal/translation/batch`
- `moderation-service`: `POST /internal/moderation/command-log`

### Events
- Publishes: `chat.command.executed`, `chat.voice.participant.joined`, `chat.voice.participant.left`
- Subscribes: `moderation.chat.mute`, `event.schedule.voice`

### WebRTC / WS
- Endpoint `wss://.../voice/{voiceSessionId}` документировать в описании

---

## 🗄️ Database

- **Schema:** `chat`
- **Tables:**
  - `chat_messages` (история, партиционирование по `channel_type`)
  - `chat_voice_sessions`
  - `chat_translation_settings`
- **Redis:**
  - `chat_history:{channelId}` — список последних 100 сообщений
  - `chat_command_cooldown:{playerId}:{command}` — rate-limit

---

## 🧩 Frontend Usage

- **Компоненты:** `ChatInput`, `CommandPalette`, `VoiceOverlay`
- **API:** `usePostChatCommands`, `usePostChatVoiceJoin`, `useGetChatHistoryChannelType`
- **State Store:** `useChatStore` хранит `activeVoiceSession`, `translationSettings`
- **Пример:**
```typescript
const { mutate: runCommand } = usePostChatCommands();

function handleSlash(command: string) {
  runCommand({ command });
}
```

---

## 📝 Implementation Notes

- Описать ограничения команд: максимум 5 команд в 10 секунд (429 при превышении).
- Для форматирования указать whitelist тегов: `<strong>`, `<em>`, `<a>`, `<mention>`.
- Voice endpoints должны требовать `X-Voice-Client-Version`.
- История сообщений кэшируется в Redis 1 час.
- Указать, что переводы поддерживают до 5 языков за запрос.

---

## ✅ Acceptance Criteria

1. Файл `chat-features.yaml` создан и соответствует OpenAPI 3.0.3.
2. Описаны команды, форматирование, голосовой чат, перевод и история.
3. Схемы ChatCommandRequest, VoiceJoinRequest, TranslationSettings, ChatHistoryResponse присутствуют.
4. Зафиксированы rate-limit и ограничения по длине.
5. Отражены интеграции с голосовым лобби и переводом.
6. Документированы события Kafka и WebRTC handshake.
7. Файл проходит чеклист без ошибок.
8. `brain-mapping.yaml` содержит запись `API-TASK-254`.
9. `.BRAIN/05-technical/backend/chat/chat-features.md` обновлён блоком API Tasks Status.
10. Frontend пример использует Orval-клиенты.

---

## ❓ FAQ

**В:** Как обрабатывать неизвестные команды?

**О:** Endpoint возвращает `status: ERROR` и `cooldownSeconds: 0`, документировать пример ответа.

**В:** Нужно ли поддерживать групповой перевод?

**О:** Да, `translate` принимает список языков, описать лимит 5 и поведение, если язык совпадает с исходным.

**В:** Как восстанавливать голосовые сессии после дисконнекта?

**О:** Через повторный `POST /voice/join` с `resumeToken`, добавить поле в `VoiceJoinRequest`.

**В:** Где хранить историю RP-эмотов?

**О:** В тех же `chat_messages` с меткой `messageType: EMOTE`, спецификация должна отразить поле.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

