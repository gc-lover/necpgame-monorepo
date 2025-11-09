# Task ID: API-TASK-336
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 18:25  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-334], [API-TASK-335]

---

## 📋 Краткое описание

Создать спецификацию `Character Visual Romance Export API`, которая описывает управление экспортом ассет-пакетов романтических состояний и сцен: конфигурации, очереди, прогресс, результаты и восстановление.  
**Целевой файл:** `api/v1/character/visuals/romance-export.yaml`

---

## 🎯 Цель задания

Предоставить character-service и marketing-service контракт-first API для:
- запуска экспорта ассетов (видео, анимации, аудио, скрипты, материалы) по романтическим состояниям и сценам;  
- контроля очереди экспортов, отслеживания прогресса, получения результатов и уведомления потребителей;  
- конфигурирования каналов доставки (UI, cutscene studio, маркетинг, analytics, localization);  
- логирования ошибок/предупреждений и обеспечения повторного запуска экспортов.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:18  
**Статус:** approved (api-readiness: ready)

**Из документа:**
- Блоки об экспорте ассетов, JSON схемы `RomanceExportRequest`, `RomanceExportJob`, `RomanceExportResult`.  
- Каналы: marketing, UI, cutscene, localization, analytics.  
- Требования к ассет-пакетам: видео, анимации, аудио, текстовые скрипты, метаданные, превью.  
- Kafka события: `character.visuals.romance.export.requested`, `character.visuals.romance.export.completed`, `character.visuals.romance.export.failed`.  
- Метрики: `ExportSuccessRate`, `AverageExportTime`, `ExportQueueDepth`.

### Дополнительные источники

- `API-SWAGGER/api/v1/character/visuals/romance-states.yaml` (API-TASK-334).  
- `API-SWAGGER/api/v1/character/visuals/romance-scenes.yaml` (API-TASK-335).  
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` (потоки ассетов).  
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` (интеграция с realtime).  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` (социальный импакт).

---

## 📁 Целевая структура API

**Файл:** `api/v1/character/visuals/romance-export.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── character/
            └── visuals/
                ├── README.md
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── romance-export.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервисы:** character-service (8091), marketing-service (8110), narrative-service (8087)  
- **API Base:** `/api/v1/character/visuals/*`  
- **Интеграции:** asset-pipeline-service, notification-service, analytics-service, localization-service.  
- **Kafka:**  
  - `character.visuals.romance.export.requested`  
  - `character.visuals.romance.export.completed`  
  - `character.visuals.romance.export.failed`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/marketing/romance-assets  
- **State Store:** `useMarketingStore(romanceExports)`  
- **UI:** `ExportQueueDashboard`, `ExportJobDetails`, `ChannelSelector`, `AssetPreview`, `RetryPanel`  
- **Формы:** `ExportRequestForm`, `ExportChannelForm`, `RetryExportForm`  
- **Layouts:** `MarketingAssetStudioLayout`, `RomanceExportOperationsLayout`  
- **Хуки:** `useExportQueue`, `useExportJob`, `useAssetPreview`, `useExportMetrics`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservices: character-service (8091), marketing-service (8110), narrative-service (8087)
# - Frontend Module: modules/marketing/romance-assets
# - State Store: useMarketingStore(romanceExports)
# - UI: ExportQueueDashboard, ExportJobDetails, ChannelSelector, AssetPreview, RetryPanel
# - Forms: ExportRequestForm, ExportChannelForm, RetryExportForm
# - Layouts: MarketingAssetStudioLayout, RomanceExportOperationsLayout
# - Hooks: useExportQueue, useExportJob, useAssetPreview, useExportMetrics
# - Events: character.visuals.romance.export.requested, character.visuals.romance.export.completed, character.visuals.romance.export.failed
# - API Base: /api/v1/character/visuals/*
```

---

## ✅ План

1. **Разбор требований:** определить поля запросов (sceneIds, stateIds, channels, formats, priorities, localization).  
2. **Схемы:** `RomanceExportRequest`, `RomanceExportJob`, `RomanceExportStatus`, `RomanceExportResult`, `ExportChannelConfig`, `AssetDescriptor`, `RetryPolicy`.  
3. **Endpoints:**  
   - создание запроса экспорта,  
   - просмотр очереди,  
   - статус конкретного job,  
   - скачивание результатов,  
   - управление каналами и ретраями.  
4. **Kafka/метрики:** задокументировать события, описать метрики.  
5. **Ошибки/безопасность:** shared security/responses/pagination.  
6. **Примеры:** экспорт сцен Charismatic Idol, Romantic event bundle, Localization export, Analytics-only export.  
7. **Валидация:** файл ≤400 строк, компоненты вынести.

---

## 🔌 Эндпоинты

1. **POST `/character/visuals/romance-export/jobs`**  
   - Тело: `RomanceExportRequest`.  
   - Ответ: `202 Accepted` (`RomanceExportJobCreated`), ошибки `400`, `409`, `422`, `503`.

2. **GET `/character/visuals/romance-export/jobs`**  
   - Параметры: `status`, `channel`, `priority`, `createdFrom`, `createdTo`, `page`, `pageSize`.  
   - Ответ: `200 OK` (`PaginatedRomanceExportJob`).

3. **GET `/character/visuals/romance-export/jobs/{jobId}`**  
   - Статус job, прогресс, ссылки на результаты/логи.  
   - Ответы: `200 OK`, `404`, `500`.

4. **POST `/character/visuals/romance-export/jobs/{jobId}/retry`**  
   - Тело: `RetryPolicy`.  
   - Ответы: `202 Accepted`, `400`, `404`, `409`, `503`.

5. **GET `/character/visuals/romance-export/jobs/{jobId}/results`**  
   - Возвращает список `RomanceExportResult` (URL, тип, локали, размер, TTL).  
   - Ответы: `200 OK`, `404`, `410` (если TTL истёк), `500`.

6. **GET `/character/visuals/romance-export/channels`**  
   - Каталог доступных каналов и конфигураций.  
   - Ответ: `200 OK` (`ExportChannelConfigList`).

---

## 🧱 Модели

- **RomanceExportRequest** — `requestId`, `stateIds[]`, `sceneIds[]`, `channels[]`, `formats[]`, `includeVideo`, `includeAudio`, `includeScripts`, `locales[]`, `priority`, `callbackUrl`, `metadata`.  
- **RomanceExportJob** — `jobId`, `status` (queued/running/completed/failed/cancelled), `progress`, `createdAt`, `updatedAt`, `requestedBy`, `estimatedCompletion`.  
- **RomanceExportStatus** — `jobId`, `status`, `progress`, `tasks[]`, `warnings[]`, `errors[]`.  
- **RomanceExportResult** — `assetId`, `assetType`, `url`, `expiresAt`, `locale`, `channel`, `checksum`.  
- **ExportChannelConfig** — `channelId`, `name`, `description`, `deliveryMechanism`, `requirements`.  
- **RetryPolicy** — `strategy` (immediate/backoff/manual), `maxAttempts`, `delay`, `notes`.  
- **PaginatedRomanceExportJob** — `items[]`, `page`, `pageSize`, `totalItems`, `totalPages`.

Добавить `x-events`, `x-metrics`, `x-related-apis` (ссылки на states/scenes).

---

## 📏 Принципы

- OpenAPI 3.0.3, ≤400 строк, компоненты вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_REQUEST`, `BIZ_EXPORT_CONFLICT`, `BIZ_JOB_NOT_FOUND`, `INT_EXPORT_PIPELINE_FAILURE`, `INT_CHANNEL_UNAVAILABLE`.  
- `info.description` содержит ссылки на `.BRAIN`, дату, UX/QA подтверждение.  
- Указать зависимости на `romance-states` и `romance-scenes`.

---

## ✅ Критерии приемки

1. `api/v1/character/visuals/romance-export.yaml` создан и проходит `scripts/validate-swagger.ps1`.  
2. `Target Architecture` добавлен в начало файла.  
3. `POST /jobs` и `GET /jobs/{jobId}` реализованы с полной документацией.  
4. Описаны схемы `RomanceExportRequest`, `RomanceExportJob`, `RomanceExportStatus`, `RomanceExportResult`, `ExportChannelConfig`, `RetryPolicy`.  
5. Экспортные результаты возвращают URL, TTL, checksum.  
6. Kafka события и метрики задокументированы.  
7. Ошибки используют shared responses с `x-error-code`.  
8. Примеры включают маркетинговый пакет, UI пакет, локализацию, аналитический экспорт.  
9. README обновлён (в реализации).  
10. Зависимости от `API-TASK-334` и `API-TASK-335` отражены.

---

## ❓ FAQ

**Q:** Как экспорт связан с состояниями и сценами?  
A: Через `stateIds` и `sceneIds`; нужно ссылаться на API `romance-states` и `romance-scenes`.

**Q:** Нужно ли поддерживать мультиязычные ассеты?  
A: Да, используйте поле `locales[]` и указывайте локализованные результаты.

**Q:** Что делать с большими файлами?  
A: Возвращать `url`/`checksum` на CDN; API хранит только метаданные и TTL.

**Q:** Как обрабатывать ошибки экспорта?  
A: Через события `export.failed`, поле `errors[]` в `RomanceExportStatus` и endpoint `retry`.

---

**Следующие действия исполнителя:** реализовать спецификацию, вынести компоненты и примеры, обновить README, прогнать валидацию и линтеры.

