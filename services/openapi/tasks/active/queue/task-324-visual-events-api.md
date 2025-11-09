# Task ID: API-TASK-324
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:24  
**Создатель:** AI Agent (ДУАПИТАСК)  
**Зависимости:** API-TASK-322 (локации), API-TASK-323 (социальные хабы)

---

## 📋 Краткое описание

Подготовить OpenAPI спецификацию `api/v1/world/visuals/events.yaml` для описания атмосферных и визуальных событий, влияющих на зоны и хабы.

**Что нужно сделать:** Описать REST endpoints для получения каталогов событий, расписаний, интенсивности и связи с метриками/маркетингом.

---

## 🎯 Цель задания

Предоставить world-service API для визуальных событий (погодные изменения, рейдовые сценарии, маркетинговые шоу), чтобы синхронизировать атмосферу с локациями и хабами.

**Зачем это нужно:**
- Системы живого мира и маркетинга планируют события (шторма, концерты, Blackwall Breach) и должны транслировать их визуальные параметры.
- Social-service и economy-service корректируют NPC трафик и витрины, опираясь на расписания событий.
- UI/UX команде необходимо отображать и воспроизводить эффекты в интерфейсе и карты.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 11:06  
**Статус документа:** approved

**Что важно из этого документа:**
- Разделы 1, 4, 5, 6, 7 описывают погодные сцены, рейдовые зоны, подземные пространства и природные пограничные области.
- Kafka топик `world.visuals.event.triggered`.
- Метрики `EventVisualImpact`.

### Дополнительные источники

- `.BRAIN/02-gameplay/world/events/live-events-system.md` — логика живых событий.
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — реакция NPC и инфраструктуры.
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — взаимодействие событий с социальными механиками.
- `API-SWAGGER/api/v1/gameplay/actions/actions.yaml` — образец описания событий/активностей.

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-322-visual-locations-detailed-api.md` — базовые данные локаций.
- `API-SWAGGER/tasks/active/queue/task-323-visual-hubs-detailed-api.md` — зависимые хабы.
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` — требования к realtime рассылкам.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/world/visuals/events.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── visuals/
                ├── schemas/
                │   ├── visual-event-profile.yaml
                │   └── visual-event-schedule.yaml
                └── events.yaml
```

В README `visuals/` добавить раздел про события и связь с локациями/хабами.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** world-service  
- **Порт:** 8086  
- **API Base:** `/api/v1/world/visuals/events/*`  
- **Интеграции:** social-service (активность хабов), economy-service (рынки), marketing-service (кампании), telemetry-service (метрики)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** modules/world/events  
- **State Store:** useWorldStore (visualEvents, schedules, eventFilters)  
- **UI компоненты (@shared/ui):** EventCard, EventTimeline, ImpactBadge, WeatherPreview, RaidStageMap  
- **Формы (@shared/forms):** EventFilterForm, EventSubscriptionForm  
- **Layouts:** GameLayout  
- **Хуки:** useRealtime, useWorldStoreFilters, useAnalytics

### Messaging
- Kafka: `world.visuals.event.triggered` (producer world-service, consumers social-service, gameplay-service, telemetry).
- Доп. поток: `marketing.visuals.package.generated` (для событий с маркетинговыми пакетами).
- Указать webhook `/webhooks/world/visuals/events` для подписчиков.

---

## 🧭 Подробный план реализации

1. **Описать модели событий** — вынести `VisualEventProfile`, `VisualEventStage`, `WeatherEffect`, `RaidPhase`, `EventMediaPackage`.
2. **Задокументировать catalog endpoint** — фильтры по типу (weather, raid, social, marketing) и регионам.
3. **Добавить расписания** — endpoint для будущих и активных событий с временными окнами.
4. **Определить realtime взаимодействие** — указать Kafka событие, subscription webhook и поля `realtimeChannels`.
5. **Согласовать ошибки** — использовать общую модель `Error`.
6. **Выполнить проверку чеклистом и валидацией.**

---

## 🔀 Endpoints

### 1. GET `/api/v1/world/visuals/events`
- **Назначение:** список визуальных событий с фильтрами по типу, региону, статусу.
- **Параметры:** `eventType` (enum: weather, raid, social, marketing, blackout), `cityId`, `zoneId`, `status` (upcoming, active, completed), `intensity` (1-5), `page`, `size`.
- **Ответ 200:** `VisualEventCollection` (data[], meta).
- **Пример:** Blackwall Breach Site (тип raid, статус active, интенсивность 5).

### 2. GET `/api/v1/world/visuals/events/{eventId}`
- **Назначение:** подробная информация о событии, стадиях, эффектах и связанных локациях/хабах.
- **Ответ 200:** `VisualEventProfile`.
- **Полезные поля:** `stages[]`, `linkedLocations[]`, `linkedHubs[]`, `mediaPackages[]`, `metrics`.
- **Ошибки:** 404 (не найден), 409 (конфликт расписания).

### 3. GET `/api/v1/world/visuals/events/schedule`
- **Назначение:** безопасный доступ к расписанию будущих событий.
- **Параметры:** `from`, `to` (date-time), `cityId`, `includeArchived` (boolean).
- **Ответ 200:** `VisualEventSchedule` (entries[] с временными окнами).
- **Заголовок:** `X-Event-Preview-Key` для маркетинговых превью.

### 4. POST `/api/v1/world/visuals/events/subscriptions`
- **Назначение:** подписка на уведомления о событиях (webhook или Kafka).
- **Body:** `VisualEventSubscriptionRequest` (eventTypes[], cities[], callbackUrl, deliveryMode).
- **Ответ 202:** `VisualEventSubscriptionStatus`.
- **Ошибки:** 400 (валидация), 409 (дубликат), 503 (webhook недоступен).

---

## 🧱 Модели данных

- **VisualEventProfile**
  - `eventId` (string, pattern `^[A-Z0-9_-]{6,32}$`).
  - `name` (string).
  - `description` (string, maxLength 2000).
  - `eventType` (enum: weather, raid, social, marketing, blackout, ritual).
  - `status` (enum: draft, scheduled, active, cooldown, archived).
  - `intensity` (integer, 1-5).
  - `cityId`, `zoneId` (string).
  - `linkedLocations` (array[`EventLocationLink`]).
  - `linkedHubs` (array[`EventHubLink`]).
  - `stages` (array[`VisualEventStage`]) — например, Blackwall Breach: вход, бой с AI, ядро.
  - `weatherEffects` (array[`WeatherEffect`]) — туман, неон, штормы.
  - `audioPalette` (array[`AudioLayerReference`]).
  - `visualCues` (array[`VisualCue`]) — гличи, лазеры, дроны.
  - `mediaPackages` (array[`EventMediaPackage`]) — ссылки для маркетинга.
  - `metrics` (`VisualEventMetricSnapshot`) — EventVisualImpact, MarketingAssetUtilization.
  - `updatedAt` (date-time), `startsAt`, `endsAt`.

- **VisualEventStage**
  - `stageId` (string).
  - `name`, `description`.
  - `durationMinutes` (integer).
  - `visuals` (`VisualCue[]`).
  - `difficulty` (enum: narrative, combat, logistics).

- **VisualEventSchedule**
  - `entries` (array[`VisualEventScheduleEntry`]) — `eventId`, `window`, `expectedIntensity`, `isGlobal`, `previewAvailable`.

- **VisualEventSubscriptionRequest**
  - `eventTypes` (array[string], minItems 1, maxItems 5).
  - `cities` (array[string]).
  - `deliveryMode` (enum: webhook, kafka).
  - `callbackUrl` (uri) — required if webhook.
  - `maxEventsPerMinute` (integer, default 60).
  - `includeMedia` (boolean).

- **VisualEventSubscriptionStatus**
  - `subscriptionId` (uuid).
  - `status` (enum: queued, active, paused, cancelled).
  - `createdAt`, `updatedAt` (date-time).

- **Дополнительные компоненты** (`schemas/visual-event-profile.yaml`, `visual-event-schedule.yaml`):
  - `EventLocationLink`, `EventHubLink`, `WeatherEffect`, `VisualCue`, `VisualEventMetricSnapshot`, `EventMediaPackage`, `SnapshotAssetLink` (reuse), `VisualEventScheduleEntry`, `TimeWindow`.

Учесть метрики и Kafka payload из документа.

---

## 📏 Принципы и правила

- Использовать `shared/common/responses.yaml` и `shared/common/pagination.yaml`.
- Описать безопасность (BearerAuth) и scopes `world.visuals.events.read`, `world.visuals.events.manage`.
- В ответах публиковать `etag` для кэширования (`Etag` header).
- Каждый файл ≤ 400 строк, схемы вынести в отдельные YAML.
- Документировать отношение к realtime: добавить `x-realtime` с каналом `/ws/world/visuals/events`.
- Использовать единый `Error` с кодами `VAL_EVENT_FILTER_INVALID`, `BIZ_EVENT_CONFLICT`, `INT_EVENT_STREAM_FAILED`.

---

## ✅ Критерии приемки

1. Определены четыре endpoints с полными параметрами, примерами и кодами ответов.
2. Схемы событий вынесены в `schemas/visual-event-profile.yaml` и `visual-event-schedule.yaml`.
3. Примеры покрывают Blackwall Breach Site, Titan Freight Orbital, Sable Reactor Ruins и Badlands Storm Belt.
4. Пагинация подключена и описана.
5. Kafka событие `world.visuals.event.triggered` документировано с payload и потребителями.
6. Подписка учитывает ограничения (max events per minute, delivery mode).
7. README в `visuals/` обновлён с разделом Events.
8. Добавлены ссылки на локации и хабы (через `linkedLocations`, `linkedHubs`).
9. Метрики EventVisualImpact и MarketingAssetUtilization включены в схемы и описаны.
10. Файл проходит `validate-swagger.ps1` без ошибок.
11. Обработаны ошибки 409 (конфликт расписания) и 503 (недоступность потоков).
12. Документация указывает требуемые scopes безопасности.

---

## ❓ FAQ

- **Чем отличаются eventType `raid` и `combat`?**  
  Визуальные события `raid` связаны с крупными сценариями (Blackwall Breach, Titan Freight Orbital). Боевым событиям (`combat`) управляет отдельное API gameplay-service; в этом API мы охватываем атмосферную часть рейдов.

- **Нужно ли описывать экономические эффекты?**  
  Да, указать ссылку на economy-service (например, `economy-impact` внутри `VisualEventProfile`) и встроить в примеры.

- **Как обрабатывать глобальные события?**  
  Использовать поле `scope` (local | regional | global) и указывать `isGlobal` в расписании.

- **Что делать при отмене события?**  
  Возвращать `status: cancelled` и публиковать Kafka payload с флагом `cancelled: true`.

- **Можно ли менять интенсивность во время события?**  
  Да, документировать endpoint активности (PATCH) в будущем; в рамках этого задания достаточно описать поле `intensityHistory` в `VisualEventProfile`.

---



