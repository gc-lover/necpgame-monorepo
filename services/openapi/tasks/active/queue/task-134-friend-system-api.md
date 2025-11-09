# Task ID: API-TASK-134
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-07 10:28  
**Создатель:** AI Agent  
**Зависимости:** none

---

## 📋 Краткое описание
Подготовить OpenAPI-спецификацию для социальной системы друзей: заявки, онлайн-статусы, блок-лист и недавние игроки.

**Что нужно сделать:** оформить API `social-service` по документу `.BRAIN/05-technical/backend/friend-system.md`.

---

## 🎯 Цель задания
Дать фронтенду и другим сервисам контракт управления дружескими связями, уведомлениями и блокировками, включая realtime обновления статусов.

**Зачем это нужно:**
- Оперативное взаимодействие игроков (добавление, совместные активности).  
- Рассылка уведомлений о запросах и активности друзей.  
- Сбор аналитики по социальным связям и рекомендациям.

---

## 📚 Источники информации

### Основной источник
**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/05-technical/backend/friend-system.md`  
**Версия:** v1.0.0  
**Обновлено:** 2025-11-07  
**Статус:** ready  

**Важно из документа:**
- Жизненный цикл дружбы (request → accept/decline → remove).  
- Лимиты (рекомендуемые 500 друзей), хранение недавних игроков.  
- Интеграция с online presence и уведомлениями.  
- События event bus (`friend:*`).

### Дополнительные источники
- `.BRAIN/05-technical/backend/notification-system.md` — уведомления о запросах.  
- `.BRAIN/05-technical/backend/presence-service.md` — статус онлайн/оффлайн.  
- `API-SWAGGER/api/v1/social/notifications/notifications.yaml`.

### Связанные документы
- `.BRAIN/02-gameplay/social/friends-ui.md` — UX ожидания.  
- `.BRAIN/02-gameplay/social/reputation-system.md` — влияние дружеских связей.  
- `.BRAIN/05-technical/backend/block-system.md` — политика блокировок.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/social/friends/friend-system.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/social` и `http://localhost:8080/api/v1/social`.

**Тип:** OpenAPI 3.0.3  
**Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── friends/
                └── friend-system.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** social-service  
- **Порт:** 8084  
- **API Base:** `/api/v1/social/friends`  
- **Интеграции:** auth-service/session-service (presence), character-service (профили), notification-service, block-system.  
- **Комментарий в спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: social-service (port 8084)
  # - API Base: /api/v1/social/friends
  # - Dependencies: auth-service, character-service, notification-service, block-system
  # - Frontend Module: modules/social/friends
  # - UI: FriendsList, FriendCard, RequestsDrawer
  # - Forms: FriendRequestForm, BlockPlayerForm
  # - Hooks: useSocialStore, useRealtime, useSearch
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: social-service
    port: 8084
    domain: social
    base-path: /api/v1/social/friends
    directory: api/v1/social/friends
    package: com.necpgame.socialservice
  ```
- `servers` с путями, WebSocket `x-websocket`: `wss://api.necp.game/v1/social/friends/presence/{accountId}`.

### Frontend
- **Модуль:** `modules/social/friends`.  
- **State Store:** `useSocialStore` (`friends`, `requests`, `blocked`, `recentPlayers`).  
- **UI:** FriendsList, FriendCard, RequestsDrawer, OnlineBadge, BlockListPanel.  
- **Формы:** FriendRequestForm, BlockPlayerForm, SearchFriendsForm.  
- **Хуки:** useRealtime (presence), useDebounce (поиск), useNotificationStore.  
- **Layouts:** GameLayout (social hub), Overlay (requests).

---

## ✅ Что нужно сделать

### Шаг 1. Анализ
- Выявить статусы дружбы: pending, accepted, blocked.  
- Зафиксировать лимиты по друзьям/блок-листу, правила рекомендаций и хранения recent players.  
- Определить payload статусов онлайна (timestamp, platform, activity).

### Шаг 2. Endpoints
1. **GET `/api/v1/social/friends`** — список друзей с метаданными.  
2. **POST `/api/v1/social/friends/request`** — отправка заявки.  
3. **POST `/api/v1/social/friends/request/respond`** — принять/отклонить.  
4. **DELETE `/api/v1/social/friends/{friendId}`** — удалить/отписаться.  
5. **GET `/api/v1/social/friends/requests`** — входящие/исходящие заявки.  
6. **GET `/api/v1/social/friends/presence`** — онлайн статусы (batched).  
7. **GET `/api/v1/social/friends/recent`** — недавние игроки.  
8. **POST `/api/v1/social/friends/block`**, **DELETE `/api/v1/social/friends/block/{characterId}`** — блок-лист.  
9. **GET `/api/v1/social/friends/block`** — список блокировок.  
10. **POST `/api/v1/social/friends/notes`** — заметки на друзей (опционально).  
11. **GET `/api/v1/social/friends/suggestions`** — рекомендации (по интересам/гильдиям).

### Шаг 3. Модели
- `Friend`, `FriendRequest`, `Presence`, `BlockedPlayer`, `RecentPlayer`, `FriendSuggestion`, `FriendNote`.  
- Ошибки: `FriendError` (`VAL_ALREADY_FRIENDS`, `VAL_MAX_LIMIT`, `BIZ_REQUEST_NOT_FOUND`, `BIZ_BLOCKED`).  
- WebSocket события: `friendOnline`, `friendOffline`, `requestReceived`, `requestAccepted`.

### Шаг 4. OpenAPI
- Прописать схемы, re-use components; ссылки на `shared/common`.  
- `security`: `BearerAuth`.  
- Примеры запросов/ответов (добавление друга, блокировка).  
- Дополнить `x-websocket` описанием каналов и payload.

### Шаг 5. Валидация
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/social/friends/`.  
- Убедиться, что покрыты требования по блок-листу, recent players, notifications.  
- Обновить brain-mapping и `.BRAIN` документ, README для раздела `friends`.

---

## 🔍 Критерии приемки
1. Правильный `info.x-microservice` (`social-service`, 8084, `social`).  
2. Все маршруты под `/api/v1/social/friends` (для блоков — `/api/v1/social/friends/block`).  
3. Реализованы заявки, список друзей, блок-лист, недавние игроки, presence.  
4. Возврат `unreadRequestsCount` и `onlineCount` там, где нужно.  
5. WebSocket события описаны, включая payload статуса.  
6. Ошибки используют общую модель `Error`.  
7. Примеры для основных операций (request, accept, block).  
8. Валидация проходит без ошибок.  
9. Обновлены brain-mapping и `.BRAIN` (новый путь `api/v1/social/friends/friend-system.yaml`).  
10. README в каталоге `friends` описывает ключевые endpoints.  
11. Указаны ограничения (лимиты, rate limit на запросы).

---

## FAQ
- **Существуют ли двусторонние блокировки?** Блок — односторонний, но запрет на заявки реализуется.  
- **Как хранить заметки?** Внутри friend связи (опциональное поле).  
- **Как обновляются статусы?** Через presence service и WebSocket стрим.  
- **Можно ли скрыть присутствие?** Настройка в preferences, описать в API.  
- **Как обрабатывать спам?** Rate limiting + `ErrorCode VAL_RATE_LIMIT`.

---

**Источник:** `.BRAIN/05-technical/backend/friend-system.md` (v1.0.0, ready)

