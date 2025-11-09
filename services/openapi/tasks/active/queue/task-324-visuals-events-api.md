# Task ID: API-TASK-324
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:20  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-322]

---

## 📋 Краткое описание

Определить спецификацию `World Visual Events API`, описывающую погодные и атмосферные сценарии для локаций, их интенсивность, расписание и публикацию событий.  
**Что нужно сделать:** Создать OpenAPI-файл `api/v1/world/visuals/events.yaml` на основе `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`.

---

## 🎯 Цель задания

Зафиксировать контракт world-service для:
- выдачи каталогов визуальных событий (погодные штормы, глич-сингулярности, Blackwall Breach и др.);
- предоставления расписаний, интенсивностей и зон действия;
- публикации обновлений в Kafka для social, gameplay, marketing и telemetry сервисов;
- измерения влияния через метрики EventVisualImpact и VisualFidelityScore.

**Зачем это нужно:**
- UI и маркетинг смогут синхронно отображать погодные/атмосферные сценарии.
- Gameplay-сценарии и рейды получают детальные данные о стадии события.
- Telemetry фиксирует влияние визуальных событий на игроков и NPC.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`  
**Версия:** v1.0.0  
**Дата обновления:** 2025-11-08 11:06  
**Статус:** approved

**Важные разделы:**
- «Рейдовые зоны и боевые арены», «Подземные пространства», «Природные и пограничные зоны».
- Описания Blackwall Breach Site, Titan Freight Orbital, Sable Reactor Ruins, Badlands Storm Belt.
- Kafka топик `world.visuals.event.triggered`, метрики EventVisualImpact.
- UX/QA подтверждение корректности JSON схем и payloadов.

### Дополнительные источники

- `.BRAIN/03-lore/visual-guides/visual-style-locations.md` — базовые профили.
- `.BRAIN/03-lore/visual-guides/visual-style-assets-детально.md` — ассеты, которые участвуют в событиях.
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — влияние событий на население.
- `API-SWAGGER/api/v1/world/events/world-events.yaml` (если есть) — свериться с форматом.

### Связанные документы

- `.BRAIN/04-narrative/quests/raid/quantum-reef-siege.md` — использование визуальных событий в рейдах.
- `.BRAIN/02-gameplay/combat/raids/raid-archetypes.md` — требования боевых арен.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/world/visuals/events.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 Specification (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── visuals/
                ├── README.md
                └── events.yaml  ← создать/обновить
```

Компоненты при необходимости делить на `api/v1/world/visuals/components/`.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис):
- **Микросервис:** world-service
- **Порт:** 8086
- **API пути:** `/api/v1/world/visuals/*`
- **Интеграции:** gameplay-service (рейды/боевые события), social-service (хабы), economy-service (рынки), marketing-service (кампании), telemetry-service (метрики)
- **Kafka:** `world.visuals.event.triggered`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):
- **Модуль:** modules/world/events
- **Путь:** modules/world/events/visuals
- **State Store:** `useWorldStore(events)`
- **UI компоненты:** `VisualEventTimeline`, `EventIntensityGauge`, `EventZoneMap`, `EventAlertBanner`
- **Формы:** `EventSubscriptionForm`, `EventFilterForm`
- **Layouts:** `EventCommandLayout`, `GameLayout`
- **Хуки:** `useEventFeed`, `useRealtime`, `useEventFilters`

### Комментарий:
```
# Target Architecture:
# - Microservice: world-service (port 8086)
# - Frontend Module: modules/world/events/visuals
# - State Store: useWorldStore(events)
# - UI: VisualEventTimeline, EventIntensityGauge, EventZoneMap, EventAlertBanner
# - Forms: EventSubscriptionForm, EventFilterForm
# - Layouts: EventCommandLayout, GameLayout
# - Hooks: useEventFeed, useRealtime, useEventFilters
# - Events: world.visuals.event.triggered
# - API Base: /api/v1/world/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Каталогизировать события:** Составить перечень типов событий (weather, raid, glitch, ritual) и их атрибуты (интенсивность, длительность, поражённые локации).  
   _Результат:_ матрица полей для `VisualEventProfile`.
2. **Спроектировать схемы:** Создать `VisualEventProfile`, `VisualEventStage`, `VisualEventIntensity`, `VisualEventSchedule`, `VisualEventImpact`, `VisualEventSubscription`, `VisualEventMetric`.  
   _Результат:_ описанные компоненты с примерами.
3. **Описать endpoints:** Реализовать список событий, детализацию отдельного события, а также endpoint для расписаний/подписок.  
   _Результат:_ минимум три эндпоинта с параметрами фильтрации и пагинацией.
4. **Kafka и analytics:** Документировать, какие поля публикуются в `world.visuals.event.triggered`, и как метрики EventVisualImpact/VisualFidelityScore рассчитываются.  
   _Результат:_ раздел `x-events`/`x-metrics`.
5. **Ошибки/безопасность:** Подключить shared security/responses, определить `VAL_INVALID_EVENT_FILTER`, `BIZ_EVENT_NOT_FOUND`, `INT_EVENT_PIPELINE_FAILURE`.  
   _Результат:_ унифицированные коды ошибок.
6. **Примеры и тесты:** Добавить примеры для Blackwall Breach Site (3 стадии), Badlands Storm Belt (weather), Titan Freight Orbital (low gravity), Netherline Tunnels (underground).  
   _Результат:_ components/examples, проверенные линтером.

---

## 🔌 Эндпоинты

1. **GET `/world/visuals/events`**  
   - **Назначение:** Возвращает список визуальных событий с пагинацией.  
   - **Параметры:** `eventType` (enum: weather, raid, glitch, ritual, festival), `region`, `locationId`, `intensityMin`, `status` (planned, active, coolingDown), `page`, `pageSize`.  
   - **Ответы:**  
     - `200 OK` — `PaginatedVisualEventProfile`.  
     - `400 Bad Request` — `VAL_INVALID_EVENT_FILTER`.  
     - `401/403` — shared security.  
     - `500` — `INT_EVENT_PIPELINE_FAILURE`.

2. **GET `/world/visuals/events/{eventId}`**  
   - **Назначение:** Возвращает полное описание события, фаз, интенсивности, задействованных хабов.  
   - **Ответы:**  
     - `200 OK` — `VisualEventProfile`.  
     - `404 Not Found` — `BIZ_EVENT_NOT_FOUND`.  
     - `409 Conflict` — `BIZ_EVENT_LOCKED` (редактируется).  
     - `500` — `INT_EVENT_PIPELINE_FAILURE`.

3. **GET `/world/visuals/events/{eventId}/schedule`**  
   - **Назначение:** Возвращает расписание запуска/окончания события и связанные подписки.  
   - **Ответы:**  
     - `200 OK` — `VisualEventSchedule`.  
     - `404 Not Found` — `BIZ_EVENT_NOT_FOUND`.  
     - `500` — `INT_EVENT_PIPELINE_FAILURE`.

4. **POST `/world/visuals/events/subscriptions`**  
   - **Назначение:** Регистрирует подписку сервисов или маркетинга на уведомления конкретных визуальных событий.  
   - **Тело:** `VisualEventSubscriptionRequest` (serviceId, eventTypes[], severityThreshold, deliveryChannels[]).  
   - **Ответы:**  
     - `202 Accepted` — `VisualEventSubscriptionResponse` (subscriptionId, status).  
     - `400 Bad Request` — `VAL_INVALID_SUBSCRIPTION`.  
     - `409 Conflict` — `BIZ_SUBSCRIPTION_EXISTS`.  
     - `503 Service Unavailable` — `INT_EVENT_PIPELINE_FAILURE`.

---

## 🧱 Модели данных

- **VisualEventProfile**  
  - `eventId`, `name`, `eventType`, `locationIds[]`, `region`, `stages[]` (ref), `intensity` (ref), `duration`, `effects[]`, `linkedHubs[]`, `kafkaTopic`, `metrics` (ref), `assetsRef`, `lastUpdated`.
- **VisualEventStage**  
  - `stageId`, `name`, `sequence`, `description`, `visualEffects[]`, `gameplayModifiers`, `audioLayers[]`.
- **VisualEventIntensity**  
  - `level` (enum: low, medium, high, extreme), `visualDelta`, `npcImpact`, `playerVisibility`, `weatherSeverity`.
- **VisualEventSchedule**  
  - `eventId`, `plannedStart`, `plannedEnd`, `actualStart`, `actualEnd`, `recurrence` (cron expression/enum), `cooldownMinutes`, `notifications[]`.
- **VisualEventImpact**  
  - `visualFidelityScore`, `eventVisualImpact`, `playerEngagement`, `npcDisplacement`, `economyImpact`.
- **VisualEventSubscriptionRequest**  
  - `serviceId`, `eventTypes[]`, `severityThreshold`, `deliveryChannels[]` (enum: kafka, webhook, email, internalBus), `callbackUrl`, `metadata`.
- **VisualEventSubscriptionResponse**  
  - `subscriptionId`, `status`, `createdAt`, `nextDelivery`, `channels[]`.
- **VisualEventSubscription** (storage object)  
  - `subscriptionId`, `serviceId`, `filters`, `channels`, `lastDeliveredAt`.
- **VisualEventTriggerEvent** (Kafka payload)  
  - `eventId`, `locationId`, `stage`, `intensityLevel`, `duration`, `effects[]`, `timestamp`.
- **PaginatedVisualEventProfile**  
  - `items[]`, `page`, `pageSize`, `totalItems`, `totalPages`.

Добавить примеры для Blackwall Breach Site (3 стадии), Titan Freight Orbital (low gravity), Badlands Storm Belt (weather), Sable Reactor Ruins (hazard).

---

## 📏 Принципы и правила

- OpenAPI 3.0.3, ≤400 строк, компоненты выносить.  
- Использовать общие `security`, `responses`, `pagination`.  
- Инфо-блок содержит ссылку на `.BRAIN` документ и дату.  
- Метрики и Kafka события документируются в `x-metrics`/`x-events`.  
- Не описывать бизнес-логику за пределами контрактов; только структуры и коды.  
- Все enum значения фиксируются в документации и согласуются с `.BRAIN`.

---

## ✅ Критерии приемки

1. `api/v1/world/visuals/events.yaml` создан/обновлён, проходит `scripts/validate-swagger.ps1`.  
2. Добавлен комментарий `Target Architecture`.  
3. `GET /world/visuals/events` поддерживает фильтры, пагинацию и возвращает `PaginatedVisualEventProfile`.  
4. `VisualEventProfile` описывает минимум 3 стадии и ссылку на связанные хабы.  
5. Kafka событие `world.visuals.event.triggered` документировано с payload `VisualEventTriggerEvent`.  
6. Метрики `EventVisualImpact`, `VisualFidelityScore` описаны и связаны с analytics.  
7. POST `/world/visuals/events/subscriptions` описывает asynch flow и коды ошибок (`202`, `409`, `503`).  
8. Все ошибки используют `$ref` из `shared/common/responses.yaml` с `x-error-code`.  
9. Примеры включают Blackwall Breach, Badlands Storm Belt, Titan Freight Orbital, Netherline Tunnels.  
10. README `world/visuals` дополнен ссылкой на файл (часть задания).  
11. Файл ≤400 строк с вынесенными компонентами.

---

## ❓ FAQ

**Q:** Как учитывать события, охватывающие несколько локаций и хабов?  
**A:** Использовать массив `locationIds` и `linkedHubs`; в примерах показать распределение по макро- и микро-зонам.

**Q:** Нужно ли включать события маркетинга?  
**A:** Только если они визуальные; иначе ссылка на маркетинговый API. Указать это в `eventType` и `deliveryChannels`.

**Q:** Как описывать повторяющиеся события?  
**A:** Использовать поле `recurrence` в `VisualEventSchedule` и уточнять cron/enum значения в описании.

**Q:** Что делать с устаревшими событиями?  
**A:** Возвращать статус `coolingDown` и `actualEnd`; архивирование вне рамок текущего API.

---

**Следующие действия исполнителя:** подготовить каталог событий, описать схемы и эндпоинты, задокументировать Kafka, обновить README, прогнать валидацию.


