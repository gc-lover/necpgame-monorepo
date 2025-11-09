# Task ID: API-TASK-308
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:00
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** [API-TASK-299], [API-TASK-304], [API-TASK-306], [API-TASK-190], [API-TASK-131]

---

## 📋 Краткое описание

Спроектировать OpenAPI/AsyncAPI спецификацию подсистемы уведомлений о лодаутах (Combat Loadout Notifications) для `notification-service`: генерация предупреждений, обновлений и рассылок, связанных с доступностью, патчами, обменами и аналитикой лодаутов.

**Что нужно сделать:** На основе `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` описать REST/Async контракты для подписок, отправки, шлюзов (in-game, email, push) и управления шаблонами уведомлений, связанных с лодаутами.

---

## 🎯 Цель задания

Оповещать игроков, гильдии и администраторов о критических событиях лодаутов (деградация, патч, обмен, аналитика), обеспечивая оперативное реагирование и прозрачность.

**Зачем это нужно:**
- Предупреждать игроков о недоступных предметах, режиме `degraded`, обновлениях или важных событиях.
- Автоматически рассылать итоги патчей, новые фракционные комплекты, результаты аналитики.
- Обеспечить администраторов и клан-лидеров инструментами подписок и шаблонов.

---

## 📚 Источники информации

### Основной источник

**Репозиторий:** `.BRAIN`  
**Документ:** `.BRAIN/02-gameplay/combat/combat-loadouts-system.md`  
**Версия:** 0.3.0  
**Дата последнего обновления:** 2025-11-08 00:14  
**Статус документа:** review, `api-readiness: ready`

**Что важно:**
- Разделы «Управление недоступными предметами», «Очереди обновлений», «Политики фракционных комплектов», «Обмен лодаутами», «Метрики и телеметрия» — все упоминают события и необходимость уведомлений.
- События `combat.loadouts.availability-warning`, `combat.loadouts.degraded`, `loadout.maintenance.*`, `blueprint.*`, аналитические предупреждения.
- Потребность в сегментации (игрок, отряд, гильдия, админ), каналах и шаблонах.

### Дополнительные источники

- `.BRAIN/05-technical/backend/notification-system.md` — архитектура уведомлений.
- `.BRAIN/02-gameplay/combat/arena-system.md`, `loot-hunt-system.md` — специфические уведомления для режимов.
- `.BRAIN/02-gameplay/economy/blueprint-market.md` — уведомления о сделках.
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` — мероприятия, влияющие на лодауты.

### Связанные документы/таски

- `API-SWAGGER/tasks/active/queue/task-299-combat-loadouts-api.md`
- `API-SWAGGER/tasks/active/queue/task-304-combat-loadout-availability-api.md`
- `API-SWAGGER/tasks/active/queue/task-306-combat-loadout-maintenance-api.md`
- `API-SWAGGER/tasks/active/queue/task-307-combat-loadout-blueprints-api.md`
- `API-SWAGGER/tasks/active/queue/task-190-analytics-reporting-api.md`
- `API-SWAGGER/tasks/active/queue/task-131-mail-system-api.md`

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Целевой файл:** `api/v1/notification/combat/loadout-notifications.yaml`  
**Формат:** OpenAPI 3.0.3 + AsyncAPI (при необходимости)

```
API-SWAGGER/
└── api/
    └── v1/
        └── notification/
            └── combat/
                ├── loadout-notifications.yaml           ← создать
                ├── loadout-notifications-components.yaml
                └── loadout-notifications-events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** notification-service
- **Порт:** 8090
- **API Base:** `/api/v1/notification/combat/loadouts*`
- **Источники событий:** gameplay-service, admin-service, economy-service, analytics-service.
- **Каналы:** in-game UI (websocket/SSE), email, push, SMS (при необходимости), гильдейские панели.
- **Очереди:** Kafka/RabbitMQ `notification.loadouts.*`, подписка на `combat.loadouts.*`, `loadout.maintenance.*`, `blueprint.*`, `analytics.loadouts.*`.

### Frontend
- **Модуль:** `modules/notification/loadouts`
- **State Store:** `useLoadoutNotificationStore`
- **UI компоненты:** `NotificationPreferencesPanel`, `AlertFeed`, `LoadoutMessageCenter`, `TemplateEditor`, `GuildBroadcastPanel`, `AcknowledgementTimeline`
- **Формы:** `SubscriptionForm`, `TemplateConfigForm`, `AlertRuleForm`, `AcknowledgementForm`
- **Хуки:** `useLoadoutNotifications`, `useNotificationPreferences`, `useGuildBroadcast`, `useAlertAcknowledge`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: notification-service (port 8090)
# - API Base: /api/v1/notification/combat/loadouts*
# - Event Sources: gameplay, admin, economy, analytics
# - Channels: in-game, email, push, SMS, guild dashboards
# - Frontend Module: modules/notification/loadouts (useLoadoutNotificationStore)
# - UI: NotificationPreferencesPanel, AlertFeed, LoadoutMessageCenter, TemplateEditor, GuildBroadcastPanel
# - Forms: SubscriptionForm, TemplateConfigForm, AlertRuleForm, AcknowledgementForm
# - Hooks: useLoadoutNotifications, useNotificationPreferences, useGuildBroadcast, useAlertAcknowledge
```

---

## ✅ Что нужно сделать (детальный план)

1. Собрать все сценарии уведомлений из `.BRAIN`: доступность, деградация, патчи, обмены, аналитика.
2. Спроектировать REST endpoints для подписок, управления каналами, обновления шаблонов, рассылок, подтверждений и истории уведомлений.
3. Описать схемы `NotificationSubscription`, `NotificationTemplate`, `NotificationEvent`, `DeliveryChannel`, `DeliveryResult`, `AlertRule`, `GuildBroadcast`, `Acknowledgement`.
4. Добавить endpoints для массовых рассылок (патчи, фракционные комплекты), индивидуальных предупреждений и аудита.
5. Спроектировать асинхронные события (`notification.loadout.alert`, `notification.loadout.degraded`, `notification.loadout.patch`, `notification.loadout.blueprint`, `notification.loadout.analytics`) с payload, retry, dead-letter.
6. Прописать безопасность (scopes для игроков, админов, гильдий), предпочтения, rate limits и антиспам.
7. Подготовить примеры запросов/ответов/событий (подписка, отклик, рассылка, подтверждение).
8. Интегрировать с availability, maintenance, blueprint, analytics API (включить ссылки и `$ref`).
9. Сформировать чеклист, критерии приёмки, FAQ, инструкции по обновлению mapping и `.BRAIN`.

---

## 🔀 Требуемые эндпоинты

1. `POST /api/v1/notification/combat/loadouts/subscriptions` — создать подписку на события (игрок, гильдия, админ).
2. `GET /api/v1/notification/combat/loadouts/subscriptions` — список подписок (фильтры по типу, каналу).
3. `PATCH /api/v1/notification/combat/loadouts/subscriptions/{subscriptionId}` — обновление каналов, фильтров, расписаний.
4. `DELETE /api/v1/notification/combat/loadouts/subscriptions/{subscriptionId}` — отмена подписки.
5. `GET /api/v1/notification/combat/loadouts/templates` — управление шаблонами (тип, канал, язык).
6. `POST /api/v1/notification/combat/loadouts/templates` — создание/обновление шаблонов.
7. `POST /api/v1/notification/combat/loadouts/alerts` — ручная отправка/планирование рассылки (патч, предупреждение, новинки).
8. `GET /api/v1/notification/combat/loadouts/history` — журнал отправленных уведомлений и статусов доставки.
9. `POST /api/v1/notification/combat/loadouts/acknowledgements` — подтверждение получения/прочтения.
10. `GET /api/v1/notification/combat/loadouts/channels` — доступные каналы, лимиты, состояния.
11. `POST /api/v1/notification/combat/loadouts/guild-broadcasts` — рассылка внутри гильдии (настройки, подтверждения).
12. `POST /api/v1/notification/combat/loadouts/event-ingest` — приём событий от других сервисов (если они не публикуют напрямую в шину).
13. `GET /api/v1/notification/combat/loadouts/metrics` — метрики уведомлений (доставка, отклики, спам).
14. `POST /api/v1/notification/combat/loadouts/alert-rules` — управление автоматическими триггерами (thresholds, условия).

---

## 🧱 Модели данных

- **NotificationSubscription** — `subscriptionId`, `ownerType` (`PLAYER`, `GUILD`, `ADMIN`), `ownerId`, `eventTypes[]`, `channels[]`, `filters`, `schedule`, `enabled`.
- **NotificationTemplate** — `templateId`, `eventType`, `channel`, `language`, `subject`, `body`, `variables`.
- **NotificationEvent** — `eventId`, `eventType`, `payload`, `source`, `priority`, `createdAt`.
- **DeliveryChannel** — `channel`, `status`, `limitPerHour`, `lastUsage`, `health`.
- **DeliveryResult** — `deliveryId`, `eventId`, `channel`, `recipient`, `status`, `sentAt`, `deliveredAt`, `error`.
- **AlertRule** — `ruleId`, `eventType`, `threshold`, `comparison`, `channels`, `cooldown`, `enabled`.
- **GuildBroadcast** — `broadcastId`, `guildId`, `message`, `targets`, `acknowledgementDeadline`.
- **Acknowledgement** — `ackId`, `deliveryId`, `recipient`, `ackType`, `ackAt`, `notes`.
- **NotificationMetric** — `time`, `sent`, `delivered`, `failed`, `acknowledged`, `spamRate`.
- **Async Events** — payloads `notification.loadout.alert`, `notification.loadout.degraded`, `notification.loadout.patch`, `notification.loadout.blueprint`, `notification.loadout.analytics`.

---

## 🧭 Принципы и правила

- Соблюдать OpenAPI 3.0.3 и AsyncAPI; при превышении 400 строк вынести компоненты.
- Использовать `$ref` на общие компоненты и на контракты loadout availability/maintenance/blueprints/analytics.
- Учитывать локализацию, предпочтения игроков, GDPR/anti-spam требования.
- Ролевая модель: игроки, гильдийные лидеры, админы; соответствующие scopes и ограничения.
- Обеспечить подтверждения (`acknowledgements`) и возможности отключения уведомлений.
- Публиковать события в `notification.loadouts.*`, соблюдая гарантии доставки и повторные попытки.
- Описать rate limits и защиту от спама (на уровне канала и подписки).

---

## ✅ Критерии приемки

1. Все 14 эндпоинтов описаны с параметрами, схемами, примерами.
2. Поддержка подписок, шаблонов, каналов, рассылок, подтверждений документирована.
3. Асинхронные события описаны с payload и гарантиями (retry, dead-letter).
4. Интеграции с availability/maintenance/blueprints/analytics отражены в описаниях.
5. Метрики и аудит уведомлений задокументированы.
6. Security (scopes, роли), privacy (отписка, GDPR), rate limits прописаны.
7. Checklist и FAQ заполнены, указаны шаги обновления mapping и `.BRAIN`.

---

## 📎 Checklist перед сдачей

- [ ] Все блоки шаблона заполнены, ссылки на `.BRAIN` и связанные API корректны.
- [ ] OpenAPI/AsyncAPI проходит lint; при необходимости вынести компоненты.
- [ ] Примеры покрывают ключевые сценарии (подписка, предупреждение, broadcast, ack, метрики).
- [ ] События согласованы с notification-шиной.
- [ ] Архитектурный комментарий корректен.
- [ ] Инструкции по обновлению mapping и `.BRAIN` подготовлены.

---

## ❓ FAQ

**Q:** Как гарантировать, что игроки не получат спам?  
**A:** Использовать rate limits на канал, предпочтения подписок и `AlertRule.cooldown`. Документировать ошибки (`429 TOO_MANY_NOTIFICATIONS`).

**Q:** Можно ли отправить уведомление только офицерам гильдии?  
**A:** Да, через `guild-broadcast` с таргетом `roles[]`. Требуется проверка прав через social-service.

**Q:** Как отслеживаются прочтения?  
**A:** Через endpoint `acknowledgements` и события `notification.loadout.acknowledged`. Статусы отражаются в `DeliveryResult`.

---

## 🔗 Связность и последующие шаги

- Добавить запись в `tasks/config/brain-mapping.yaml` и обновить `.BRAIN/02-gameplay/combat/combat-loadouts-system.md` (API-TASK-308).
- Согласовать спецификацию с loadout availability, maintenance, blueprints, analytics.
- Подготовить дальнейшие задачи для UI центра уведомлений и интеграции push/email каналов.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

