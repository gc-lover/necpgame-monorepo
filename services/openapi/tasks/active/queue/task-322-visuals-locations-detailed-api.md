# Task ID: API-TASK-322
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:10  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-162], [API-TASK-314]

---

## 📋 Краткое описание

Создать спецификацию `World Visual Locations Detailed API`, которая описывает расширенные визуальные профили локаций, микрозоны и экспорт ассетов.  
**Что нужно сделать:** Сформировать OpenAPI-файл `api/v1/world/visuals/locations-detailed.yaml` по данным из `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`.

---

## 🎯 Цель задания

Зафиксировать каноническую модель визуальных профилей локаций, чтобы world-service мог:
- отдавать последовательные визуальные сценарии для Night City, Neo Tokyo, Badlands и др.;
- поддерживать погодные, световые и аудио режимы на уровне макро- и микро-зон;
- синхронизировать микросервисные данные с social, economy и marketing потоками;
- обеспечивать экспорт ассетов и палитр для UI, маркетинга и внутриигровых энциклопедий.

**Зачем это нужно:**
- Обеспечивает консистентность визуального опыта для всех команд.
- Формирует основание для генерации карт, карточек зон и маркетинговых пакетов.
- Дает world-service единый контракт для управления атмосферой, светом и аудио.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`  
**Версия документа:** v1.0.0  
**Дата последнего обновления:** 2025-11-08 11:06  
**Статус документа:** approved

**Что важно из этого документа:**
- Подробные описания макро-локаций (Night City, Neo Tokyo, Lagos, Amazon Cloud Basin).
- Детализация районов Night City с указанием освещения, фракций, рынков.
- Привязка визуальных элементов к микросервисам world/social/economy/marketing.
- Требования к REST/Kafka контурам, JSON схемам и UX/QA подтверждениям.
- Метрики: VisualFidelityScore, EventVisualImpact, HubAmbienceRetention, MarketingAssetUtilization.

### Дополнительные источники

- `.BRAIN/03-lore/locations/locations-overview.md` — базовая иерархия городов и регионов.
- `.BRAIN/03-lore/visual-guides/visual-style-locations.md` — сводный визуальный гид.
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — интеграция с генерацией NPC/трафика.
- `API-SWAGGER/api/v1/world/cities/population.yaml` — пример world-service спецификации (структура).

### Связанные документы

- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — пересечения с социальными эффектами.
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md` — ассеты и камеры.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/world/visuals/locations-detailed.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 Specification (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── visuals/
                ├── README.md
                └── locations-detailed.yaml  ← создать/обновить
```

Если файл уже существует, обновить его, сохранив версию и совместимость. Компоненты >400 строк вынести в `api/v1/world/visuals/components/`.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис):
- **Микросервис:** world-service
- **Порт:** 8086
- **API пути:** `/api/v1/world/visuals/*`
- **Интеграции:** social-service (hub presence), economy-service (рынки и контракты), marketing-service (ассет-пакеты), analytics-service (метрики визуала)
- **Kafka топики:** `world.visuals.location.detailed.updated`, `world.visuals.event.triggered`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):
- **Модуль:** modules/world/atlas
- **Путь:** modules/world/atlas/detailed
- **State Store:** `useWorldStore(visuals)`
- **UI компоненты:** `VisualLocationCard`, `AtmosphereTimeline`, `EventHeatmap`, `LightCycleBadge`
- **Формы:** `VisualSnapshotRequestForm`, `VisualFilterForm`
- **Layouts:** `AtlasSplitLayout`, `GameLayout`
- **Хуки:** `useWorldFilters`, `useRealtime`, `useAtlasCues`

### Комментарий в начале YAML:
```
# Target Architecture:
# - Microservice: world-service (port 8086)
# - Frontend Module: modules/world/atlas/detailed
# - State Store: useWorldStore(visuals)
# - UI: VisualLocationCard, AtmosphereTimeline, EventHeatmap, LightCycleBadge
# - Forms: VisualSnapshotRequestForm, VisualFilterForm
# - Layouts: AtlasSplitLayout, GameLayout
# - Hooks: useWorldFilters, useRealtime, useAtlasCues
# - Events: world.visuals.location.detailed.updated, world.visuals.event.triggered
# - API Base: /api/v1/world/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Уточнить сценарии:** Сопоставить макро- и микро-локации из `.BRAIN` с параметрами API (погода, свет, трафик, безопасность) и сформировать таблицу атрибутов.  
   _Результат:_ перечень полей для `VisualLocationDetailedProfile`.
2. **Спроектировать схемы:** Описать модели `VisualLocationDetailedProfile`, `AtmosphereSet`, `MicroHubSummary`, `AudioLayer`, `SecurityEnvelope`, `VisualSnapshotRequest`, `VisualSnapshotResponse`.  
   _Результат:_ раздел `components/schemas` ≤400 строк (вынести в отдельные файлы при необходимости).
3. **Проработать endpoints:** Задать контракт для списка профилей, детального просмотра, фильтрации, экспорта снапшотов, используя пагинацию, сортировку и фильтры.  
   _Результат:_ секция `paths` с полной документацией и примерами.
4. **Integraции и события:** Описать связи с Kafka топиками, указать, какие поля публикуются и как обновляются фронтенд и маркетинг.  
   _Результат:_ секция описаний / `x-event-topics` / `x-mq` (кастомные расширения) при необходимости.
5. **Ошибки и безопасность:** Подключить `shared/common/security.yaml` и `shared/common/responses.yaml`, определить коды `VAL_*`, `BIZ_*`, `INT_*`.  
   _Результат:_ единообразные ответы на ошибки и секция безопасности.
6. **Примеры и тесты:** Добавить пример профиля (Night City Quantum Plaza), пример фильтрации по региону, пример снапшота.  
   _Результат:_ раздел `examples` / `components/examples`, проверенный линтером (`scripts/validate-swagger.ps1`).

---

## 🔌 Эндпоинты

1. **GET `/world/visuals/locations/detailed`**  
   - **Назначение:** Возвращает страницу детализированных профилей локаций.  
   - **Параметры:**  
     - `region` (query, string, optional) — регион (Night City, Neo Tokyo, Badlands).  
     - `atmosphere` (query, string, optional) — фильтр по типу атмосферы (neutral, tense, extreme).  
     - `faction` (query, string, optional) — доминирующая фракция.  
     - `timeOfDay` (query, string, optional) — day/night/evening.  
     - `page`, `pageSize` — пагинация через `shared/common/pagination.yaml`.  
   - **Ответы:**  
     - `200 OK` — `PaginatedVisualLocationDetailedProfile`.  
     - `400 Bad Request` — `VAL_INVALID_FILTER` (через `shared/common/responses.yaml#/BadRequest`).  
     - `401/403` — общие ответы безопасности.  
     - `500` — `INT_VISUALS_PIPELINE_FAILURE`.

2. **GET `/world/visuals/locations/detailed/{locationId}`**  
   - **Назначение:** Получение полного профиля конкретной локации с микрозонами и текущими событиями.  
   - **Path параметры:** `locationId` (string, required).  
   - **Ответы:**  
     - `200 OK` — `VisualLocationDetailedProfile`.  
     - `404 Not Found` — `BIZ_LOCATION_NOT_FOUND`.  
     - `409 Conflict` — `BIZ_PROFILE_OUTDATED` (если идёт пересчёт).  
     - `500` — `INT_VISUALS_PIPELINE_FAILURE`.

3. **POST `/world/visuals/locations/snapshots`**  
   - **Назначение:** Запрос на экспорт ассетов/палитр/аудио слоёв для выбранных локаций.  
   - **Тело:** `VisualSnapshotRequest` (локации, каналы, требуемые форматы).  
   - **Ответы:**  
     - `202 Accepted` — `VisualSnapshotResponse` с `snapshotId`, `expiresAt`, `deliveryChannels[]`.  
     - `400 Bad Request` — `VAL_INVALID_REQUEST`.  
     - `422 Unprocessable Entity` — `VAL_ASSET_CHANNEL_UNSUPPORTED`.  
     - `503 Service Unavailable` — `INT_SNAPSHOT_QUEUE_BUSY`.

Для всех эндпоинтов указать `security` по `shared/common/security.yaml` (например, `bearerAuth`).

---

## 🧱 Модели данных

- **VisualLocationDetailedProfile**  
  - `locationId`, `name`, `tier`, `region`, `primaryFaction`, `threatLevel`, `populationBand`,  
    `atmosphereSet` (ref), `lightingCycle` (ref), `audioLayers[]` (ref), `microHubs[]` (ref),  
    `securityEnvelope` (ref), `activeEvents[]` (ref), `metrics` (ref), `lastUpdated`.
- **AtmosphereSet**  
  - `mode` (enum: neutral, tense, extreme), `fogDensity`, `weatherPalette[]`, `npcDensity`, `transportFlow`.
- **LightingCycle**  
  - `timeOfDay`, `primaryColors[]`, `neonPatterns`, `reflectionProfile`.
- **MicroHubSummary**  
  - `hubId`, `hubType`, `description`, `services[]`, `trafficScore`, `factionPresence`, `marketingTags[]`.
- **AudioLayer**  
  - `layerId`, `palette` (enum), `intensity`, `loop`, `source`, `notes`.
- **SecurityEnvelope**  
  - `patrolPresence`, `droneCoverage`, `turretZones[]`, `alertColors`, `safeRoutes[]`.
- **VisualSnapshotRequest**  
  - `locations[]`, `channels[]` (enum: ui, marketing, analytics, archive), `includeAudio`, `includeLighting`, `format` (enum: gltf, png, json), `priority`.  
- **VisualSnapshotResponse**  
  - `snapshotId`, `status` (queued/processing/ready/failed), `expiresAt`, `links[]`, `warnings[]`.
- **PaginatedVisualLocationDetailedProfile**  
  - `items[]`, `page`, `pageSize`, `totalItems`, `totalPages`.

Каждая схема должна содержать примеры и описания, валидаторы (enum, min/max, format) и ссылки на JSON схемы из `.BRAIN` при необходимости.

---

## 📏 Принципы и правила

- Соблюдать SOLID/DRY/KISS и лимит ≤400 строк (выносить компоненты).  
- OpenAPI 3.0.3, использование общих `responses`, `pagination`, `security`.  
- Версионирование info.version → `1.0.0`, при изменениях обновлять семвер.  
- Не хардкодить статические данные, описывать структуры и источники (Kafka, BFF, маркетинг).  
- Добавить ссылки на `.BRAIN` источники в описании `info.description` и `x-sources`.

---

## ✅ Критерии приемки

1. Файл `api/v1/world/visuals/locations-detailed.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. В начале файла присутствует блок комментария `Target Architecture`.  
3. В `info.description` перечислены `.BRAIN` источники и дата актуализации.  
4. Эндпоинты `GET /world/visuals/locations/detailed` и `/detailed/{locationId}` поддерживают пагинацию/фильтры и возвращают схему `PaginatedVisualLocationDetailedProfile`.  
5. `POST /world/visuals/locations/snapshots` описывает асинхронный экспорт с кодами `202`, `422`, `503`.  
6. Все ошибки используют `$ref` из `shared/common/responses.yaml`; собственные коды указаны в `x-error-code`.  
7. Для списков применяется `$ref` на `shared/common/pagination.yaml#/components/schemas/Pagination`.  
8. Kafka события перечислены в разделе `x-events` с payload ссылками на схемы.  
9. Примеры включают Night City Quantum Plaza и Badlands Storm Belt.  
10. Спецификация документирует метрики (`VisualFidelityScore`, `EventVisualImpact`) и их связь с analytics.  
11. Безопасность подключена через `shared/common/security.yaml` (bearer + service token).  
12. Файл ≤400 строк, компоненты вынесены при необходимости.

---

## ❓ FAQ

**Q:** Как связать визуальные профили с существующим `world/cities` API?  
**A:** Использовать общий `locationId` и `districtId`; в описании схем добавить ссылки на `CityPopulationProfile` из `api/v1/world/cities/population.yaml`.

**Q:** Что делать, если ассет-пакеты требуют дополнительных форматов?  
**A:** Добавить в `VisualSnapshotRequest.channels` новое значение и описать его в `.BRAIN` + маркетинг-документации перед обновлением спецификации.

**Q:** Нужно ли добавлять WebSocket/Realtime?  
**A:** Нет, достаточно указать Kafka события; realtime-service использует их для трансляции.

**Q:** Как учитывать обновления локаций без перегенерации всех ассетов?  
**A:** Поддерживать `snapshotId` и флаг `partialUpdate`; при необходимости добавить PATCH в отдельной итерации (не входит в текущую задачу).

---

**Следующие действия исполнителя:** подготовить структуру директории `world/visuals`, создать/обновить YAML файл, добавить ссылки в `README.md` и прогнать валидацию.

