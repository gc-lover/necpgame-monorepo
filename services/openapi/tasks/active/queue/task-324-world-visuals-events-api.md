# Task ID: API-TASK-324
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:34  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** [API-TASK-241], [API-TASK-300], [API-TASK-322], [API-TASK-323]

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `api/v1/world/visuals/events.yaml`, описывающую визуальные погодные и атмосферные события мира, их стадии, интенсивность и интеграцию с социальными/геймплейными системами.

**Что нужно сделать:** Задокументировать выдачу и планирование `VisualEventProfile`, предпросмотр сценариев и оповещение связанных сервисов на основе детального визуального гида.

---

## 🎯 Цель задания

Обеспечить world-service единым API для визуальных событий (погодных штормов, Blackwall Breach, Titan Freight Orbital, Sable Reactor Ruins и т.д.), чтобы синхронизировать атмосферу в PvE/PvP и социальных активностях.

**Зачем это нужно:**
- Управлять визуальными событиями, связанными с рейдами, погодой и глобальными сценариями.  
- Передавать в realtime UI (world atlas, raid planner, marketing) параметры освещения/эффектов.  
- Поддерживать метрики вовлечения и маркетинговые кампании.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 11:06  
**Статус документа:** approved (api-readiness: ready)

**Что важно из документа:**
- Сценарии рейдовых зон (Blackwall Breach Site, Titan Freight Orbital, Sable Reactor Ruins).  
- Природные зоны (Badlands Storm Belt, Floating Mangrove Farms, Red Wastes Crevasse).  
- Описание `world.visuals.event.triggered` и метрик `EventVisualImpact`.

### Дополнительные источники

- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — влияние событий на плотность NPC.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — мировые эффекты, требующие синхронизации.  
- `.BRAIN/03-lore/visual-guides/visual-style-assets-детально.md` — ассеты и эффекты (гличи, освещение) для рейдов.

### Связанные документы

- `API-SWAGGER/api/v1/world/visuals/locations-detailed.yaml` (API-TASK-322) — источники locationId/assetId.  
- `API-SWAGGER/api/v1/gameplay/raids/scenarios.yaml` (если отсутствует — сослаться на API-TASK-244/245/247).  
- `.BRAIN/04-narrative/quests/quest-main-001-first-steps.md` — пример использования событий в квестах.

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
                └── events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** world-service  
- **Порт:** 8092  
- **API Base:** `/api/v1/world/visuals/events/*`  
- **Зависимости:** gameplay-service (raid triggers), social-service (hub ambience), economy-service (рынки), marketing-service (кампании), auth-service

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/world/events`  
- **State Store:** `useWorldStore` (visualEvents, activeEvent, previewTicket)  
- **UI компоненты (@shared/ui):** EventTimeline, HazardBadge, WeatherBadge, RaidStageCard, MetricChip  
- **Формы (@shared/forms):** EventVisualPlannerForm, EventBroadcastForm  
- **Layouts:** WorldEventsLayout (`@shared/layouts`)  
- **Hooks:** useEventStream, useEventPreview

### Комментарий
Добавить в YAML блок:
```yaml
# Target Architecture:
# - Microservice: world-service (port 8092)
# - Frontend Module: modules/world/events
# - UI Components: @shared/ui (EventTimeline, HazardBadge, WeatherBadge, RaidStageCard, MetricChip)
# - Forms: @shared/forms (EventVisualPlannerForm, EventBroadcastForm)
# - State: useWorldStore (visualEvents, activeEvent, previewTicket)
# - API Base: /api/v1/world/visuals/events/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Классификация событий** — разделить на погодные, рейдовые, социальные, природные; определить обязательные поля (intensity, stages, hazards).  
2. **Спроектировать эндпоинты** — список, карточка события, планирование, предпросмотр, отмена/перезапуск.  
3. **Определить модели** — `VisualEventProfile`, `EventStage`, `EventTrigger`, `EventHazard`, `EventPreviewRequest`, `EventScheduleRequest`, `EventPreviewTicket`.  
4. **Задокументировать связи** — Rx с social-service (ambience), gameplay-service (raid state), marketing-service (кампании).  
5. **Kafka** — описать публикацию `world.visuals.event.triggered`, подписку соц/геймплей.  
6. **Метрики** — `EventVisualImpact`, `VisualFidelityScore`, telemetry pipeline.  
7. **Безопасность и ошибки** — BearerAuth, общие ответы, коды 409 (конфликт расписания), 423 (событие в lockdown), 503.  
8. **Валидация** — проверить линтером, уложиться в 400 строк, вынести components при необходимости.

---

## 🔀 Endpoints

1. **GET `/api/v1/world/visuals/events`**  
   - Фильтры: `type` (weather, raid, social, natural), `intensity`, `macroZone`, `active` (bool), `from`, `to`, `limit`, `offset`.  
   - Ответ 200: список `VisualEventProfile` (сокращённый вид).  
   - Поддержать сортировку по `startTime`, `intensity`.

2. **GET `/api/v1/world/visuals/events/{eventId}`**  
   - Path: `eventId` (`EVT-[A-Z0-9-]+`).  
   - Ответ 200: полный профиль (стадии, триггеры, визуальные эффекты, связанные локации, recommendedUI).  
   - Ошибки: 404, 423 (не раскрывается публично).

3. **POST `/api/v1/world/visuals/events/preview`**  
   - Тело: `EventPreviewRequest` (eventType, locationId, stageOverrides, lightingTest, channels).  
   - Ответ 202: `EventPreviewTicket` (id, status, estimatedReadyAt).  
   - Ошибки: 400, 409 (уже есть активный preview), 503.

4. **GET `/api/v1/world/visuals/events/preview/{ticketId}`**  
   - Возвращает `EventPreviewResult` (links, palette, shaderParams, audioMix).  
   - Ошибки: 404, 410, 423.

5. **POST `/api/v1/world/visuals/events/schedule`**  
   - Тело: `EventScheduleRequest` (eventId или template, startTime, duration, intensity, linkedHubs[], broadcastChannels[]).  
   - Ответ 201: `VisualEventSchedule` (id, scheduleState).  
   - Ошибки: 409 (конфликт с существующим событием), 412 (нет подтверждения QA), 503.

6. **DELETE `/api/v1/world/visuals/events/{eventId}/schedule`**  
   - Назначение: отмена запланированного события.  
   - Ответ 204. Ошибки: 404, 423 (уже активен).

Все ошибки подключать через `shared/common/responses.yaml`. Security — BearerAuth.

---

## 🧱 Модели данных

- **VisualEventProfile**  
  Поля: `eventId`, `name`, `type`, `macroZone`, `locationId`, `intensity`, `durationMinutes`, `stages[]`, `hazards[]`, `lighting`, `weather`, `audio`, `dynamicEffects`, `linkedHubs[]`, `marketingTags[]`, `metrics`, `kafkaTopics`, `lastUpdated`.

- **EventStage** (`stageId`, `name`, `sequence`, `visualCue`, `hazardLevel`, `npcBehavior`, `recommendedUI`).  
- **EventHazard** (`hazardId`, `description`, `impact`, `safetyGuideline`, `visuals`).  
- **EventTrigger** (`triggerType`, `source`, `conditions`, `cooldown`).  
- **EventPreviewRequest** (`eventType`, `locationId`, `intensity`, `stageOverrides[]`, `includeAudio`, `channels`).  
- **EventPreviewTicket** (`ticketId`, `status`, `expiresAt`, `estimatedReadyAt`).  
- **EventPreviewResult** (`ticketId`, `status`, `cdnLinks[]`, `palette`, `shaderParams`, `audioMix`, `generatedAt`).  
- **EventScheduleRequest** (`eventId` or `templateId`, `startTime`, `durationMinutes`, `intensity`, `linkedHubs[]`, `broadcastChannels[]`, `requestedBy`).  
- **VisualEventSchedule** (`scheduleId`, `eventId`, `state`, `startTime`, `endTime`, `createdBy`).  
- Метрики: `EventVisualImpact`, `PlayerEngagementDelta`, `IncidentCount`.  
- Использовать `DateTime` формат ISO 8601, enums для `type`, `intensity`.

Примеры: Blackwall Breach (трёхстадийный, hazard fractal walls), Titan Freight Orbital (низкая гравитация), Badlands Storm Belt (weather extreme).

---

## 📡 Kafka и интеграции

- **Producer:** world-service → `world.visuals.event.triggered` `{ eventId, locationId, stage, intensity, duration, triggeredAt }`.  
- **Consumers:** social-service (ambience), gameplay-service (raid modifiers), telemetry, marketing-service.  
- Дополнительно описать `marketing.visuals.package.generated` как downstream событие после успешного плана (совместно с API-TASK-323).  
- Подписка на `social.visuals.hub.activity` (обратная связь по ambiance) — описать зависимость.

---

## 📊 Метрики и аналитика

- `EventVisualImpact` — изменение вовлечённости, включить в `metrics` блока.  
- `VisualFidelityScore` — соответствие утверждённым профилям.  
- `SafetyIncidentRate` — количество инцидентов при событии (для QA).  
- Все метрики отправляются в telemetry; указать ссылку на dashboards world/art/social.

---

## ⚙️ Правила реализации

- SOLID/DRY/KISS, не дублировать схемы, использовать `$ref`.  
- Не хардкодить события — API описывает структуры, данные хранятся в БД.  
- В description ссылаться на `.BRAIN` документ и workshop 2025-11-08.  
- При превышении 400 строк — вынести `components` в поддиректорию `api/v1/world/visuals/components/events`.  
- Учесть UX/QA чеклист из документа (schema-test, payload review).

---

## ✔️ Критерии приемки

1. Файл `api/v1/world/visuals/events.yaml` создан/обновлён и содержит Target Architecture.  
2. Описаны все 6 эндпоинтов с параметрами, примерами и ошибками.  
3. Модели `VisualEventProfile`, `EventStage`, `EventHazard`, `EventPreviewRequest`, `EventScheduleRequest` включены с примерами.  
4. Kafka событие `world.visuals.event.triggered` документировано.  
5. Указаны зависимости на social-service, gameplay-service, marketing-service.  
6. Метрики `EventVisualImpact` и `SafetyIncidentRate` описаны.  
7. Подключены стандартные ошибки и безопасность.  
8. Файл проходит `scripts/validate-swagger.ps1`.  
9. Размер ≤400 строк или есть вынесение компонентов + README.  
10. В описании присутствуют ссылки на `.BRAIN` документ и workshop 2025-11-08.  
11. Endpoint schedule описывает конфликты и валидацию QA.  
12. Preview flow поддерживает ticket lifecycle (202 → GET → 200/410).

---

## ❓ FAQ

- **Вопрос:** Почему события оформляются отдельным файлом от локаций?  
  **Ответ:** События затрагивают динамику нескольких сервисов (world, social, gameplay), требуют своих моделей и жизненного цикла, поэтому отделены для удобства версияции.

- **Вопрос:** Нужно ли поддерживать PUT/DELETE для профилей?  
  **Ответ:** Нет, профили управляет контент-пайплайн. API предоставляет чтение и планирование событий.

- **Вопрос:** Как учитывать Blackwall Breach?  
  **Ответ:** Описать трёхстадийное событие с глич-эффектами, гравитационными скачками и hazard уровнями (см. документ). Использовать `type: raid`.

- **Вопрос:** Можно ли использовать preview без schedule?  
  **Ответ:** Да, preview предназначен для арт-ревью; укажите, что ticket можно отменить без планирования.

- **Вопрос:** Как связать событие с социальными хабами?  
  **Ответ:** Через `linkedHubs[]` в `EventScheduleRequest` и зависимость на API-TASK-323 (`HubAmbienceUpdate`).

---

## 📌 История выполнения

- 2025-11-08 — Задание создано AI агентом GPT-5 Codex на основе `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`.


