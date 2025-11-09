# Task ID: API-TASK-132
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-07 10:24  
**Создатель:** AI Agent  
**Зависимости:** none

---

## 📋 Краткое описание
Разработать OpenAPI-спецификацию системы уведомлений: realtime push, inbox, email и пользовательские предпочтения.

**Что нужно сделать:** оформить контракт `social-service` для работы с игровыми уведомлениями согласно `.BRAIN/05-technical/backend/notification-system.md`.

---

## 🎯 Цель задания
Согласовать backend и frontend на уровне API: доставка уведомлений, управление статусами, фильтры, email рассылки и WebSocket push.

**Зачем это нужно:**
- Единая точка доставки событий от всех микросервисов игроку.
- Поддержка UI-компонентов (колокольчик, тосты, настройки) и мобильных/email сообщений.
- Хранение истории и аналитика по типам/приоритетам уведомлений.

---

## 📚 Источники информации

### Основной источник
**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/05-technical/backend/notification-system.md`  
**Версия:** v1.0.0  
**Дата обновления:** 2025-11-07  
**Статус:** ready  

**Ключевые моменты:**
- Типы уведомлений (quest, achievement, social, economy, combat, mail).  
- Каналы доставки: in-game toast, WebSocket, email.  
- Настройки предпочтений, тихий режим, приоритеты.  
- SLA хранения (30 дней), журнал действий, массовые рассылки.

### Дополнительные источники
- `.BRAIN/05-technical/backend/security-anti-scam.md` — лимиты на спам и rate limiting.  
+- `.BRAIN/05-technical/backend/mail-system.md` — триггеры «новое письмо».  
- `.BRAIN/05-technical/backend/trade-system.md` — уведомления о торговых заявках.  
- `API-SWAGGER/api/v1/social/notifications/` (существующие компоненты, если есть).  
- `API-SWAGGER/api/v1/shared/common/responses.yaml` — стандартизированные ответы.

### Связанные документы
- `.BRAIN/05-technical/backend/event-bus-overview.md` — список событий, которые нужно обрабатывать.  
- `.BRAIN/02-gameplay/social/social-hubs.md` — UI контекст.  
- `.BRAIN/05-technical/backend/push-email-integration.md` — SMTP и push-шлюзы.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/social/notifications/notifications.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/social` и `http://localhost:8080/api/v1/social`. Структура строго `api/v1/<microservice>/<domain>/`.

**Тип:** OpenAPI 3.0.3  
**Версия:** v1

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── notifications/
                └── notifications.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** social-service  
- **Порт:** 8084  
- **API Base:** `/api/v1/social/notifications`  
- **Интеграции:** auth-service (preferences per account), mail-system, trade-system, guild-system, economy analytics, email gateway.  
- **Комментарий в спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: social-service (port 8084)
  # - API Base: /api/v1/social/notifications
  # - Dependencies: auth-service, mail-system, trade-system, guild-system, push-gateway
  # - Frontend Module: modules/notifications
  # - UI: NotificationBell, NotificationToast, NotificationDrawer
  # - Forms: NotificationPreferencesForm
  # - Hooks: useNotificationStore, useRealtime
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: social-service
    port: 8084
    domain: social
    base-path: /api/v1/social/notifications
    directory: api/v1/social/notifications
    package: com.necpgame.socialservice
  ```
- `servers`:
  ```yaml
  servers:
    - url: https://api.necp.game/v1/social
      description: Production API Gateway
    - url: http://localhost:8080/api/v1/social
      description: Local API Gateway
  ```
- WebSocket endpoint: `wss://api.necp.game/v1/social/notifications/stream/{accountId}` (описать в `x-websocket`).  
- Email отправка оформляется как фоновые задачи (описать в разделе интеграций).

### Frontend
- **Модуль:** `modules/notifications` (shared feature).  
- **State Store:** `useNotificationStore` (items, unread, settings, channelStatus).  
- **UI компоненты:** NotificationBell, NotificationToast, NotificationDrawer, PriorityIndicator, QuietModeToggle.  
- **Формы:** NotificationPreferencesForm, EmailSubscriptionForm.  
- **Hooks:** useRealtime, useDeviceManager (push tokens), useSettings.  
- **Layouts:** подключение к `GameLayout` и `AdminLayout`.

---

## ✅ Что нужно сделать

### Шаг 1. Анализ требований
- Уточнить типы уведомлений и payload (quest, social, economy, admin).  
- Определить правила rate limiting, quiet mode, snooze, split каналов (in-app, email).  
- Зафиксировать статусы: `unread`, `read`, `archived`, `deleted`.

### Шаг 2. Проектирование endpoints
1. **GET `/api/v1/social/notifications`** — список уведомлений с фильтрами (type, priority, status, period).  
2. **GET `/api/v1/social/notifications/{notificationId}`** — детальный просмотр.  
3. **POST `/api/v1/social/notifications/dispatch`** — отправка уведомления (service token).  
4. **POST `/api/v1/social/notifications/dispatch/bulk`** — пакетная рассылка (батчи).  
5. **POST `/api/v1/social/notifications/{notificationId}/read`** — пометить прочитанным.  
6. **POST `/api/v1/social/notifications/read-all`** — очистка счётчика.  
7. **DELETE `/api/v1/social/notifications/{notificationId}`** — удалить/архивировать.  
8. **GET `/api/v1/social/notifications/preferences`** и **PATCH** для обновления каналов/типов.  
9. **POST `/api/v1/social/notifications/email/subscribe`** — управление email каналом.  
10. **GET `/api/v1/social/notifications/history`** — журнал (для аналитики/admin).  
11. **GET `/api/v1/social/notifications/channels/status`** — состояние WebSocket/email/push.

### Шаг 3. Модели данных
- `Notification` (id, type, title, body, priority, channel, payload, metadata).  
- `NotificationListResponse` (items, pagination, unreadCount).  
- `DispatchRequest`, `BulkDispatchRequest`, `NotificationPreference`.  
- `ChannelStatus` (websocketConnected, emailSubscribed, quietMode).  
- Ошибки: `NotificationError` (`VAL_INVALID_CHANNEL`, `VAL_RATE_LIMIT`, `BIZ_NOTIFICATION_NOT_FOUND`).

### Шаг 4. OpenAPI оформление
- Описать все endpoints в `paths`, добавить параметры (`notificationId`, фильтры).  
- Использовать референсы на `shared/common` для стандартных ответов.  
- Указать `security`: `BearerAuth` для user endpoints, `ServiceToken` для dispatch API.  
- Добавить `x-websocket` блок с событиями `notificationReceived`, `notificationUpdated`.  
- Примеры payloadов для разных типов уведомлений.

### Шаг 5. Валидация и синхронизация
- `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/social/notifications/`.  
- Перепроверить соответствие требуемым сценариям (email, preferences, history).  
- Обновить `brain-mapping.yaml`, `.BRAIN` документ и README для раздела `notifications`.  
- Зафиксировать требования по rate limiting в описании (`x-notes`).

---

## 🔍 Критерии приемки
1. Корректное заполнение `info.x-microservice` (`social-service`, 8084, `/api/v1/social/notifications`).  
2. Поддержка всех ключевых каналов: in-app, WebSocket, email, bulk dispatch.  
3. Реализован `preferences` API с granular toggles и quiet mode.  
4. Поддержан журнал и пагинация, возврат `unreadCount`.  
5. WebSocket описан через `x-websocket`, указаны события и payload.  
6. Все ошибки используют общие ответы (`shared/common/responses.yaml`).  
7. Есть примеры запросов/ответов для dispatch, preferences, списка.  
8. Проходит `swagger-cli validate` и скрипт проверки.  
9. Обновлены `brain-mapping.yaml` и `.BRAIN` статус с новым путём `api/v1/social/notifications/notifications.yaml`.  
10. Задокументирован rate limiting и приоритеты (low/medium/high/critical).  
11. Email endpoint защищён `ServiceToken` и описан как асинхронная задача.

---

## FAQ
- **Как отличить тихий режим от read?** Quiet mode скрывает toast, но уведомление остаётся непрочитанным.  
- **Что делать при недоступности email шлюза?** Возвращать `503` и логировать retry; описание добавить в errors.  
- **Нужна ли push-интеграция?** Да, указать в разделе интеграций (через внешнюю систему).  
- **Сколько хранить уведомления?** 30 дней (через cron); endpoint history должен возвращать метаданные об удалении.  
- **Можно ли отписаться от конкретного типа?** Preferences поддерживает granular toggles по типам.

---

**Источник:** `.BRAIN/05-technical/backend/notification-system.md` (v1.0.0, ready)

