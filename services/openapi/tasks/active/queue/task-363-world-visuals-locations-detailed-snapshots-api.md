# Task ID: API-TASK-363
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 18:05
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-322, API-TASK-337, API-TASK-361

---

## 📋 Краткое описание

Сформировать OpenAPI-спецификацию `World Visual Location Snapshots`, обеспечивающую выдачу детализированных визуальных профилей локаций с погодой, аудио, NPC-плотностью и динамическими эффектами.

**Что нужно сделать:** Создать `api/v1/world/visuals/locations-detailed-snapshots.yaml` и связанные компоненты, отразив REST API для `DetailedVisualProfile`, таймлайнов погодных сценариев, аудио-саундскейпов и Kafka событий из `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md`.

---

## 🎯 Цель задания

Дать world-service формализованный контракт для детализированных визуальных состояний локаций, используемых арт-командой, социальными сервисами, экономикой и рейдовыми сценариями.

**Зачем это нужно:**
- Предоставить фронтенду и внешним сервисам доступ к погодным пресетам, аудио-профилям и динамическим эффектам без обращения к `.BRAIN`.
- Синхронизировать сценарии `city-life-population` и `player-orders` с визуальными изменениями.
- Поддержать Kafka события `world.visual.detailed.updated` и репликацию снапшотов в analytics-service.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md`
**Версия документа:** v1.0.0 (2025-11-08 09:44)
**Статус документа:** approved, api-readiness: ready

**Что важно из этого документа:**
- Расширенные описания макро-локаций, районов, социальных хабов, рейдовых зон, подземных и природных областей.
- Погодные и световые сценарии, аудиофон, NPC плотность, фракционные мотивы и ключевые объекты.
- Asset mapping с JSON источниками и UI модулями, требования к `DetailedVisualProfile`, Kafka темам и DTO.

### Дополнительные источники

- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — глубинные сценарии и требования к Kafka payload.
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — плотность NPC и алгоритм заселения.
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — влияние социальных заказов на визуальные состояния.
- `.BRAIN/02-gameplay/world/events/live-events-system.md` — глобальные события, влияющие на визуальные пресеты.

### Связанные задания

- `API-SWAGGER/tasks/active/queue/task-322-world-visuals-locations-detailed-api.md`
- `API-SWAGGER/tasks/active/queue/task-337-visuals-analytics-metrics-api.md`
- `API-SWAGGER/tasks/active/queue/task-361-world-visuals-locations-api.md`

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/world/visuals/locations-detailed-snapshots.yaml`
> ⚠️ Файл ≤400 строк. Общие схемы вынести в `api/v1/world/visuals/components/visual-location-detailed-schemas.yaml`, если объём превышает лимит.
**API версия:** v1 (semantic version 1.0.0)
**Тип файла:** OpenAPI 3.0.3 YAML

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── visuals/
                ├── locations-detailed-snapshots.yaml
                └── components/
                    └── visual-location-detailed-schemas.yaml (опционально)
```

**Если `locations-detailed-snapshots.yaml` уже существует:**
- Обновить и синхронизировать с новыми схемами, сохранив backward compatibility.
- Подключить общие компоненты через `$ref` на `shared/common/`.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)

- **Микросервис:** world-service
- **Порт:** 8086
- **API Base Path:** `/api/v1/world/visuals/*`
- **Домен:** детализированные визуальные состояния городов, районов и рейдов.
- **Зависимости:**
  - analytics-service (телеметрия и heatmaps)
  - social-service (хабы и социальные события)
  - economy-service (рынки, торговля)
  - gameplay-service (рейды, боевые арены)
  - auth-service (валидировать роли `art-admin`, `world-admin`)

**Event Streams:** `world.visual.detailed.updated`, `social.hub.visual.updated`, `world.visual.snapshot.exported`

### Frontend (модули)

- **Основной модуль:** `modules/world/visual-guides`
- **Дополнительные:** `modules/world/events`, `modules/social/hubs`, `modules/analytics/heatmaps`
- **State Stores:** `useWorldStore` (`visualSnapshots`, `weatherTimelines`), `useSocialStore` (`hubVisuals`)
- **UI компоненты (@shared/ui):** DetailedLocationCard, WeatherTimelineGraph, SoundscapePanel, NpcDensityChart
- **Формы (@shared/forms):** VisualSnapshotFilterForm, AdminSnapshotPublishForm
- **Hooks (@shared/hooks):** useDebounce, useDynamicTimeline, useAudioPreview
- **Layouts:** WorldAtlasLayout, RaidOperationsLayout

**Комментарий:** В начале OpenAPI файла зафиксировать архитектурный блок с микросервисом, модулями и UI компонентами (см. шаблон).

### OpenAPI

- Заполнить `info.x-microservice`: `name: world-service`, `port: 8086`, `domain: world`, `base-path: /api/v1/world/visuals`, `package: com.necpgame.worldservice`.
- Секция `servers`: `https://api.necp.game/v1` (Production API Gateway) и `http://localhost:8080/api/v1` (Local API Gateway).
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.

---

## ✅ Что нужно сделать (детальный план)

### Шаг 1: Выделить модели данных

**Действия:**
1. На основе документа выделить `DetailedVisualProfile`, `WeatherPreset`, `AudioSoundscape`, `DynamicEffectSet`, `NpcDensityProfile`.
2. Определить перечисления (`weatherType`, `lightingPattern`, `factionTag`, `hazardLevel`).
3. Подготовить `DetailedVisualSnapshot` (результат REST выдачи) и `VisualSnapshotExport` (payload Kafka).

**Ожидаемый результат:** Полный набор схем и валидаций, вынесенный в `components` файл при необходимости.

### Шаг 2: Спроектировать REST endpoints

**Обязательные endpoints:**
1. `GET /world/visuals/locations/{visualId}/detailed` — вернуть актуальный `DetailedVisualSnapshot`.
2. `GET /world/visuals/locations/{visualId}/timeline` — временная линия погодных и световых пресетов (пагинация по временным чекпоинтам).
3. `GET /world/visuals/locations/{visualId}/soundscape` — аудиопрофиль локации с ссылками на наборы звуков.
4. `GET /world/visuals/locations/{visualId}/dynamic-effects` — данные о NPC плотности, трафике и опасностях.
5. `POST /world/visuals/locations/{visualId}/snapshots:publish` — публикация нового снапшота (ограничено ролями `art-admin`, `world-admin`).

**Дополнительно:** поддержать query `atTime` (ISO8601) для просмотра исторического состояния, хедеры `X-Trace-Id`, `X-Request-Source`, `If-Match`.

**Ожидаемый результат:** Полная секция `paths` с описанием параметров, ответов (200, 202, 400, 401, 403, 404, 409, 422, 500) через общие компоненты.

### Шаг 3: Kafka события и интеграции

**Действия:**
1. Описать `components.messages.VisualSnapshotUpdated` и `VisualSnapshotPublished`.
2. Зафиксировать payload (assetId, cityId, version, weatherSet, soundscapeId, updatedBy, publishedAt).
3. Связать события с analytics-service и social-service (`x-integrations`).

### Шаг 4: Безопасность и роли

**Действия:**
1. Подключить `security` (bearerAuth) и указать роли для каждого эндпоинта.
2. Для `publish` добавить requirement `x-roles: [art-admin, world-admin]`.
3. Документировать audit-поля (`createdBy`, `updatedBy`, `publishedBy`).

### Шаг 5: Примеры и расширения

**Действия:**
1. Добавить `examples` и `x-codeSamples` (curl, TypeScript) для ключевых endpoints.
2. Вставить `x-frontend` с модулями/компонентами и DTO путями (`world/visual/*.json`).
3. Согласовать `x-monitoring` блок для `analytics-service` (метрики latency, cache-hit).

### Шаг 6: Валидация

**Действия:**
1. Прогнать `scripts/validate-swagger.ps1`.
2. Пройти чеклист `tasks/config/checklist.md` (блоки 1-12).
3. Убедиться, что файл ≤400 строк, схемы вынесены, ошибок линтера нет.

---

## 📏 Критерии приемки (12 пунктов)

1. Файл `api/v1/world/visuals/locations-detailed-snapshots.yaml` создан и валиден по OpenAPI 3.0.3.
2. Заполнен `info.x-microservice` для world-service (порт 8086, base-path `/api/v1/world/visuals`).
3. `servers` содержит только gateway URL (`https://api.necp.game/v1`, `http://localhost:8080/api/v1`).
4. `GET /world/visuals/locations/{visualId}/detailed` возвращает модель `DetailedVisualSnapshot` с weather, soundscape, dynamicEffects, npcDensity.
5. `timeline` endpoint использует `shared/common/pagination.yaml` и поддерживает фильтр `atTime`.
6. Все ответы на ошибки используют `shared/common/responses.yaml` (400, 401, 403, 404, 409, 422, 500).
7. `POST .../snapshots:publish` ограничен ролями `art-admin`, `world-admin`, возвращает `202 Accepted` и событие Kafka.
8. Определены схемы `WeatherPreset`, `AudioSoundscape`, `DynamicEffectSet`, `NpcDensityProfile` с примерами.
9. Kafka события `world.visual.detailed.updated` и `world.visual.snapshot.exported` документированы с payload.
10. Добавлены `x-frontend` и `x-integrations` с перечислением модулей, DTO путей и зависимостей сервисов.
11. Файл ≤400 строк; общие схемы вынесены в `components/visual-location-detailed-schemas.yaml` (если необходимо).
12. Валидация `scripts/validate-swagger.ps1` проходит без ошибок.

---

## ❓ FAQ

**В: Чем отличается это задание от API-TASK-322?**  
О: API-TASK-322 описывает основную структуру `locations-detailed.yaml`. Текущее задание создаёт расширенный файл с историей, погодой и аудио-снапшотами, дополняя базовую спецификацию.

**В: Нужно ли включать WebSocket?**  
О: Нет, только REST и Kafka. WebSocket каналы будут описаны в задачах для live-дашбордов.

**В: Как обрабатывать архивные снапшоты?**  
О: Добавить поле `status` (`active`, `archived`) в `DetailedVisualSnapshot` и фильтр `status` в списках.

**В: Требуется ли локализация названий?**  
О: Да, включить объект `localizedNames` (минимум ключи `en`, `ru`, `ja`) в `DetailedVisualSnapshot`.

**В: Как связать с analytics?**  
О: Добавить `x-monitoring.metrics` (latency, cacheHitRatio) и ссылку на `API-TASK-337`.
