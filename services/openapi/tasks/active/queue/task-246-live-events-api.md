# Task ID: API-TASK-246
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 21:50
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-205 (announcement-system API), API-TASK-154 (economy-events API), API-TASK-137 (leaderboard-system API), API-TASK-141 (daily-reset API)

---

## 📋 Краткое описание

Разработать спецификацию REST API для системы live events NECPGAME: планирование, публикация, активные модификаторы, аналитика и архив событий.

**Что нужно сделать:** Создать файл `live-events.yaml` с описанием всех endpoints, моделей и интеграций, необходимых для world-service и фронтенда.

---

## 🎯 Цель задания

Дать централизованный API, позволяющий планировать глобальные и локальные события, синхронизировать их с системой объявлений, экономикой, аренами и социальными активностями.

**Зачем это нужно:**
- Управлять календарём событий и транспортировать данные в UI и внешние сервисы
- Контролировать влияние эвентов на экономику, боевые режимы и мир
- Собирать аналитику участия для балансировки и маркетинга

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/02-gameplay/world/events/live-events-system.md`
**Версия документа:** v1.0.0
**Дата последнего обновления:** 2025-11-07 20:33
**Статус документа:** approved

**Что важно из этого документа:**
- Категории событий (City-Wide Crisis, Corporate Ops, Social Festivals, Underground Alerts, Environmental Shifts, Season Specials)
- Жизненный цикл: planning → announcement → active phase → resolution → archive
- Модификаторы систем: Arena, Loot Hunt, Dungeons, Economy, Social
- Интеграции: Announcement System, Support Ticket, Maintenance Mode, Battle Pass, Achievement System
- Метрики аналитики: Participation Rate, Retention Lift, Economy Impact, Voice Lobby Activity

### Дополнительные источники

- `.BRAIN/03-lore/activities/activities-lore-compendium.md` — лор, связанный с эвентами
- `.BRAIN/02-gameplay/combat/arena-system.md` — влияние на арены
- `.BRAIN/02-gameplay/world/dungeons/dungeon-scenarios-catalog.md` — влияние на подземелья
- `.BRAIN/05-technical/backend/announcement/announcement-system.md` — каналы уведомлений

### Связанные документы

- `.BRAIN/05-technical/backend/maintenance-mode/maintenance-mode.md` — режим обслуживания
- `.BRAIN/05-technical/backend/support-ticket/support-ticket-system.md` — обработка жалоб и обращений
- `.BRAIN/02-gameplay/economy/economy-events.md` — экономические эффекты

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/gameplay/world/live-events.yaml`
**API версия:** v1
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── world/
                └── live-events.yaml
```

**Требования:**
- В info.description описать связь с Announcement, Arena, Economic и Social системами
- Использовать shared security (`bearerAuth`) и общие ответы
- Учесть версионирование для будущих сезонных обновлений (в тегах указать `LiveEvents v1`)

---

## 🏗️ Целевая архитектура

### Backend
- Микросервис: world-service
- Порт: 8086
- API Base Path: `/api/v1/live-events/*`
- Интеграции: announcement-service, arena-service, economy-service, social-service, analytics-service, maintenance-service
- События: Kafka топики `live.event.scheduled`, `live.event.activated`, `live.event.resolved`

### Frontend
- Модуль: `modules/world/events`
- State Store: `useWorldStore` (`eventCalendar`, `activeEvents`, `eventAnalytics`)
- UI компоненты: `@shared/ui` (LiveEventCard, CalendarTimeline, ImpactBadge, AlertBanner), `@shared/forms` (LiveEventCreationForm, EventModifierForm)
- Layouts: `@shared/layouts/GameLayout`, `@shared/layouts/DashboardLayout`
- Хуки: `@shared/hooks/useCountdown`, `@shared/hooks/useRealtime`, `@shared/hooks/useFilters`

### Комментарии
- Указать необходимость поддержки push-уведомлений (topic keys) и синхронизации с голосовыми объявлениями
- В описаниях подчеркнуть требования к SLA (анонс ≥30 минут до старта, если не Emergency)

---

## 🔧 Детальный план выполнения

1. Собрать из `.BRAIN` ключевые сущности: LiveEventDefinition, LiveEventPhase, LiveEventEffect, модификаторы систем.
2. Спроектировать endpoints для планирования, публикации, активации, мониторинга и архивирования live events.
3. Описать аналитику и webhook-интеграции (например, `eventImpact`) с примерами метрик.
4. Задокументировать взаимодействие с Announcement и Maintenance Mode (заголовки каналов, ограничения по одновременности).
5. Проверить структуру по чеклисту, обновить `brain-mapping.yaml`, а также статус документа `.BRAIN/02-gameplay/world/events/live-events-system.md`.

---

## 🌐 Endpoints

### 1. POST `/api/v1/live-events`
- Назначение: создать новое событие и запланировать его в календаре
- Авторизация: Bearer JWT (scope `live-events.manage`)
- Тело (`LiveEventCreateRequest`): eventCode, title, category, description, startAt, endAt, phases[], impactMatrix, audienceTargets, announcementChannels[], telemetryTargets
- Ответы: 201 Created (`LiveEventSummary`), 409 Conflict (коллизия расписания), 422 Unprocessable Entity (даты некорректны)
- Интеграции: при создании публикуется `live.event.scheduled`

### 2. PATCH `/api/v1/live-events/{eventId}`
- Назначение: обновить параметры события до активации
- Тело: изменяемые поля (title, description, schedule, modifiers)
- Ответы: 200 OK (обновлённое событие), 409 Conflict (уже активно), 404 Not Found
- Требование: фиксировать `updatedBy`, `updateReason`

### 3. POST `/api/v1/live-events/{eventId}/activate`
- Назначение: перевести событие в активное состояние и запустить эффекты
- Тело (`LiveEventActivationRequest`): activationMode (SCHEDULED, EMERGENCY), overrides (arenaRules, economyModifiers, dungeonAffixes)
- Ответы: 200 OK (`LiveEventState`), 412 Precondition Failed (подготовка не завершена), 409 Conflict (уже активно)
- Интеграции: запускает объявления через announcement-service, активирует модификаторы (arena, loot, economy)

### 4. POST `/api/v1/live-events/{eventId}/resolve`
- Назначение: завершить событие, рассчитать награды и архивировать
- Тело (`LiveEventResolutionRequest`): resolutionType (SUCCESS, FAILURE, FORCE_CLOSE), rewardsSummary, analyticsSnapshot
- Ответы: 200 OK (`LiveEventArchiveRecord`), 409 Conflict (событие не активно), 422 Unprocessable Entity (аналитика не собрана)
- Публикует `live.event.resolved`

### 5. GET `/api/v1/live-events/calendar`
- Назначение: вернуть расписание событий (активные, будущие, архив)
- Параметры: `rangeStart`, `rangeEnd`, `status` (PLANNED, ACTIVE, RESOLVED), `category`
- Ответ: 200 OK (`LiveEventCalendarResponse`)
- Кэширование: max-age 120 секунд, поддержка `If-Modified-Since`

### 6. GET `/api/v1/live-events/{eventId}`
- Назначение: получить подробную информацию об эвенте
- Ответ: 200 OK (`LiveEventDetail`), 404 Not Found
- Включает фазы, текущие эффекты, задействованные системы, связанные эвенты

### 7. GET `/api/v1/live-events/{eventId}/effects`
- Назначение: получить текущие модификаторы (arena, economy, dungeons, social)
- Ответ: 200 OK (`LiveEventEffectList`)
- Параметры: `system` (ARENA, ECONOMY, DUNGEON, SOCIAL)

### 8. GET `/api/v1/live-events/{eventId}/analytics`
- Назначение: вернуть метрики участия и влияния
- Параметры: `metric` (PARTICIPATION, RETENTION, ECONOMY_IMPACT, VOICE_ACTIVITY)
- Ответ: 200 OK (`LiveEventAnalyticsResponse`)
- Дополнительно: указать источники данных (ClickHouse, BigQuery)

### 9. POST `/api/v1/live-events/{eventId}/notifications`
- Назначение: отправить дополнительные уведомления в каналы (push, email, voice)
- Тело (`LiveEventNotificationRequest`): channels[], templateId, variables, targetSegments
- Ответы: 202 Accepted, 404 Not Found (эвент), 409 Conflict (канал недоступен)

### 10. GET `/api/v1/live-events/impacts`
- Назначение: агрегировать текущие эффекты для всех активных событий
- Ответ: 200 OK (`LiveEventImpactMatrix`)
- Используется фронтендом для отображения глобальных модификаторов

Все endpoints обязаны ссылаться на общую схему ошибок и описывать коды `BIZ_EVENT_*`, `VAL_EVENT_*`, `INT_EVENT_*`.

---

## 🧱 Модели данных

### LiveEventCreateRequest
- `eventCode` (string, kebab-case, unique)
- `title` (string, i18n поддержка через `localizedTitle`)
- `category` (enum: CITY_CRISIS, CORPORATE_OPS, SOCIAL_FESTIVAL, UNDERGROUND_ALERT, ENVIRONMENTAL_SHIFT, SEASON_SPECIAL)
- `description` (string, markdown)
- `startAt` (datetime, UTC)
- `endAt` (datetime, UTC)
- `phases` (array[LiveEventPhaseDefinition])
- `impactMatrix` (LiveEventImpactMatrix)
- `audienceTargets` (array[AudienceTarget])
- `announcementChannels` (array[string], enum: PUSH, EMAIL, VOICE, BILLBOARD)
- `telemetryTargets` (array[string], например `arena.match.state`)

### LiveEventPhaseDefinition
- `phaseCode` (string)
- `name` (string)
- `startOffsetMinutes` (integer)
- `durationMinutes` (integer)
- `objectives` (array[string])
- `modifiers` (array[LiveEventModifier])

### LiveEventModifier
- `system` (enum: ARENA, LOOT_HUNT, DUNGEON, ECONOMY, SOCIAL)
- `modifierCode` (string)
- `value` (number or object) — описать тип union через oneOf
- `durationMinutes` (integer, optional)
- `notes` (string)

### LiveEventSummary
- `eventId` (uuid)
- `eventCode` (string)
- `title` (string)
- `category` (enum)
- `status` (enum: PLANNED, ACTIVE, RESOLVED)
- `startAt` (datetime)
- `endAt` (datetime)
- `createdBy` (uuid)
- `createdAt` (datetime)

### LiveEventState
- `eventId` (uuid)
- `status` (enum)
- `activatedAt` (datetime)
- `activePhases` (array[LiveEventPhaseStatus])
- `activeEffects` (LiveEventEffectList)
- `linkedSystems` (array[string])

### LiveEventEffectList
- `effects` (array[LiveEventEffect])
- `generatedAt` (datetime)

### LiveEventEffect
- `system` (enum)
- `scope` (string, например `arena.queueType=RANKED`)
- `impactType` (enum: MULTIPLIER, RESTRICTION, BONUS, CONTENT_UNLOCK)
- `value` (number or object)
- `expiresAt` (datetime)

### LiveEventAnalyticsResponse
- `eventId` (uuid)
- `metrics` (array[LiveEventMetric])
- `generatedAt` (datetime)

### LiveEventMetric
- `metricCode` (enum: PARTICIPATION, RETENTION, ECONOMY_IMPACT, VOICE_ACTIVITY, ARENA_QUEUE_TIME)
- `value` (number)
- `delta` (number)
- `sampleSize` (integer)
- `dimensionBreakdown` (array[MetricBreakdown])

### LiveEventImpactMatrix
- `system` (enum)
- `impacts` (array[LiveEventImpact])

### LiveEventImpact
- `target` (string)
- `modifier` (string)
- `value` (number or object)
- `priority` (integer)

### AudienceTarget
- `segment` (enum: GLOBAL, REGION, CLAN, FACTION, ROLE)
- `identifier` (string)
- `deliveryChannels` (array[string])

Все текстовые поля ограничить 512 символами, массивы — максимум 50 элементов. Использовать `additionalProperties: false`. Для числовых метрик указать `format: double` или `integer`. Для union-типов (`value`) описать `oneOf` с вариантами (number, object с полями `min`, `max`, `multiplier`).

---

## 📐 Принципы и правила

- Применять общие компоненты безопасности и ошибок; авторизация обязательна для всех операций кроме публичного календаря
- Отразить SLA: Emergency события допускают активацию <30 минут, добавить бизнес-правило в описании
- Указать лимиты частоты — не более 20 активаций в сутки, 5 уведомлений в минуту на канал
- Все timestamps в UTC, формат RFC 3339, хранить timezone offset
- Использовать теги `LiveEvents` и `LiveEventsAdmin` для разделения публичных и админских endpoints
- Документировать webhooks или push topics в info.description и/или разделе FAQ
- Соблюдать SOLID/DRY/KISS: повторяющиеся структуры вынести в components

---

## ✅ Критерии приемки

- В файле описаны все указанные endpoints с параметрами, телами, ответами и кодами ошибок
- Info-блок содержит контекст категорий, жизненный цикл и ключевые интеграции
- Схемы данных включают определения событий, фаз, эффектов, аналитики и аудиторий
- Для операций create/activate/resolve предусмотрено логирование `createdBy`, `activatedBy`, `resolvedBy`
- Ошибки используют префиксы `BIZ_EVENT_*`, `VAL_EVENT_*`, `INT_EVENT_*`
- Предусмотрены примеры (example) для запросов create, activate, analytics, calendar
- Указан блок Target Architecture с modулем `modules/world/events`
- В разделе security определены scopes `live-events.read`, `live-events.manage`
- API поддерживает пагинацию календаря через `limit`, `cursor` и это отражено в описании
- Файл проходит OpenAPI валидацию (spectral/openapi-generator)
- Обновлён `brain-mapping.yaml` и `.BRAIN` документ после создания

---

## ❓ FAQ

**Вопрос:** Как обрабатывать пересечение двух глобальных событий?  
**Ответ:** Сервис должен запретить одновременную активацию двух событий категории CITY_CRISIS. Endpoint `/live-events` возвращает 409 с кодом `BIZ_EVENT_CONFLICT`, если временной интервал пересекается.

**Вопрос:** Можно ли запускать локальные emergency события?  
**Ответ:** Да, при `activationMode = EMERGENCY` требуются дополнительные поля: `emergencyReason`, `approvalId`. Документируйте их как обязательные в `LiveEventActivationRequest` при emergency.

**Вопрос:** Как фронтенд узнаёт о мгновенных изменениях модификаторов?  
**Ответ:** Через WebSocket канал и polling endpoint `/live-events/impacts`. В спецификации указать поле `pollingIntervalSeconds` в ответе impacts.

**Вопрос:** Какие ограничения на уведомления?  
**Ответ:** Ограничение 5 уведомлений в минуту на канал, дублирующие уведомления агрегируются. Укажите в описании `/notifications` заголовок `X-Idempotency-Key`.

**Вопрос:** Где хранится архив событий?  
**Ответ:** Архив в Postgres таблице `live_event_archive`. Endpoint `/live-events/calendar` с `status=RESOLVED` должен поддерживать пагинацию и фильтр `seasonId`.

---

## 📦 Результат

- Файл `api/v1/gameplay/world/live-events.yaml` с полным описанием live events API
- Новая запись в `brain-mapping.yaml` для `.BRAIN/02-gameplay/world/events/live-events-system.md`
- Обновлённый статус `.BRAIN/02-gameplay/world/events/live-events-system.md` с задачей API-TASK-246








### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.


