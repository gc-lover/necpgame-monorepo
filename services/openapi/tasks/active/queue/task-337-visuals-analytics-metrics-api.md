# Task ID: API-TASK-337
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 18:38  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-331], [API-TASK-332], [API-TASK-333], [API-TASK-334], [API-TASK-335], [API-TASK-336]

---

## 📋 Краткое описание

Создать спецификацию `Visual Analytics Metrics API`, которая агрегирует и предоставляет метрики визуальных ассетов (архетипы, романтические состояния, сцены, экипировка, marketplace) для дашбордов и аналитики.  
**Целевой файл:** `api/v1/analytics/visuals/metrics.yaml`

---

## 🎯 Цель задания

Сформировать контракт для analytics-service, чтобы:
- собирать и хранить метрики из телеметрии и событий (visual fidelity, romance resonance, equipment preference, marketplace conversion);  
- предоставлять API для дашбордов (character, gameplay, economy, marketing), включая фильтры по времени, источникам и каналам;  
- поддерживать расчёт агрегатов, трендов, сегментов (по фракциям, состояниям, сценам, товарам);  
- синхронизировать метрики с marketing-service, narrative-service и telemetry pipelines.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:18  
**Статус:** approved

**Раздел 11. Метрики и аналитика:**
- `ArchetypeVisualFidelity`, `RomanceVisualResonance`, `EquipmentVisualPreference`, `MarketplaceAssetConversion`.  
- Метрики поступают в dashboards и telemetry.  
- Требования к сегментации (по сервисам и каналам) и к визуализации трендов.

### Дополнительные источники

- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` (источники данных).  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` (соц. влияние).  
- `API-SWAGGER/api/v1/character/visuals/romance-export.yaml` — экспорт событий.  
- `API-SWAGGER/api/v1/world/visuals/locations-detailed.yaml` — связь с локациями.  
- Telemetry события из `visual-style-assets-детально.md` (раздел Kafka).

---

## 📁 Целевая структура API

**Файл:** `api/v1/analytics/visuals/metrics.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── analytics/
            └── visuals/
                ├── README.md
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── metrics.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** analytics-service  
- **Порт:** 8090  
- **API Base:** `/api/v1/analytics/visuals/*`  
- **Интеграции:** telemetry-service, character-service, gameplay-service, economy-service, marketing-service, narrative-service.  
- **Kafka / ETL:** потребляет `character.visuals.*`, `gameplay.visuals.*`, `economy.visuals.*`, `marketing.visuals.package.generated`, `telemetry.visuals.metric.*`.  
- **Хранилище:** аналитическое (OLAP) с агрегациями по временным окнам.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/analytics/visuals-dashboard  
- **State Store:** `useAnalyticsStore(visualMetrics)`  
- **UI:** `MetricOverview`, `TrendChart`, `SegmentBreakdown`, `ConversionFunnel`, `AlertPanel`  
- **Формы:** `MetricFilterForm`, `SegmentSelector`, `ExportMetricsForm`  
- **Layouts:** `AnalyticsDashboardLayout`, `MarketingInsightLayout`  
- **Хуки:** `useMetricQuery`, `useTrendExport`, `useAlertSubscription`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: analytics-service (port 8090)
# - Frontend Module: modules/analytics/visuals-dashboard
# - State Store: useAnalyticsStore(visualMetrics)
# - UI: MetricOverview, TrendChart, SegmentBreakdown, ConversionFunnel, AlertPanel
# - Forms: MetricFilterForm, SegmentSelector, ExportMetricsForm
# - Layouts: AnalyticsDashboardLayout, MarketingInsightLayout
# - Hooks: useMetricQuery, useTrendExport, useAlertSubscription
# - Events: character.visuals.archetype.detailed.updated, character.visuals.romance.state.changed, gameplay.visuals.equipment.variant, economy.visuals.item.detailed
# - API Base: /api/v1/analytics/visuals/*
```

---

## ✅ План

1. **Определить модели метрик:** базовые (fidelity, resonance, preference, conversion) и расширяемые (custom).  
2. **Схемы:** `VisualMetricPoint`, `VisualMetricAggregate`, `SegmentBreakdown`, `TrendSeries`, `MetricQueryRequest`, `MetricExportResponse`, `AlertSubscription`.  
3. **Эндпоинты:** запрос метрик по фильтрам, агрегация по периодам, сегментация, экспорт CSV/JSON, управление подписками на алерты.  
4. **Интеграция с export:** выдавать ссылки на экспортированные отчёты.  
5. **Ошибки/безопасность:** shared security/responses/pagination.  
6. **Примеры:** dashboards для архетипов, romance сцены, оружейные вариации, marketplace карты.  
7. **Валидация:** файл ≤400 строк; вынести компоненты.

---

## 🔌 Эндпоинты

1. **POST `/analytics/visuals/metrics/query`**  
   - Тело: `MetricQueryRequest` (metricIds[], dateRange, granularity, segments, filters).  
   - Ответ: `200 OK` (`MetricQueryResponse`), ошибки `400`, `401/403`, `422`, `500`.

2. **GET `/analytics/visuals/metrics/overview`**  
   - Параметры: `dateRange`, `domain`, `channel`.  
   - Ответ: `200 OK` (`VisualMetricOverview`).

3. **POST `/analytics/visuals/metrics/segment`**  
   - Тело: `SegmentQueryRequest`.  
   - Ответ: `200 OK` (`SegmentBreakdownResponse`).

4. **POST `/analytics/visuals/metrics/export`**  
   - Тело: `MetricExportRequest` (format, metrics, dateRange, segments, channel).  
   - Ответ: `202 Accepted` (`MetricExportStatus`), ошибки `400`, `409`, `503`.

5. **GET `/analytics/visuals/metrics/export/{exportId}`**  
   - Статус и результаты экспорта (URL, ttl).  
   - Ответы: `200 OK`, `404`, `410`, `500`.

6. **POST `/analytics/visuals/alerts`**  
   - Создание подписки на алерты (`AlertSubscriptionRequest`).  
   - Ответ: `201 Created`, ошибки `400`, `409`, `422`.

7. **GET `/analytics/visuals/alerts`** / **DELETE `/analytics/visuals/alerts/{subscriptionId}`** — управление подписками.

---

## 🧱 Модели

- **MetricQueryRequest / Response** — параметры запросов (metrics, range, filters).  
- **VisualMetricPoint** — `metricId`, `timestamp`, `value`, `unit`, `context`.  
- **TrendSeries** — `metricId`, `series[]`.  
- **SegmentBreakdown** — `segmentKey`, `values[]`.  
- **MetricExportRequest/Status/Result** — экспорт отчётов.  
- **AlertSubscription** — `subscriptionId`, `metricId`, `threshold`, `comparison`, `channel`, `enabled`.  
- **PaginatedMetricExportJob** — пагинация экспортов.

Добавить `x-metrics`, `x-sources`, `x-related-apis`, `x-alerts` в спецификацию.

---

## 📏 Принципы

- OpenAPI 3.0.3, ≤400 строк; компоненты вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_QUERY`, `BIZ_METRIC_NOT_AVAILABLE`, `INT_ANALYTICS_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`, `INT_ALERT_CHANNEL_UNAVAILABLE`.  
- `info.description` содержит ссылки на `.BRAIN` документ и дата обновления.  
- Зависимость от `API-TASK-331..336` указана (метрики опираются на эти API).

---

## ✅ Критерии приемки

1. Файл `api/v1/analytics/visuals/metrics.yaml` создан и валиден (`scripts/validate-swagger.ps1`).  
2. В начале файла указан `Target Architecture`.  
3. Поддерживаются запросы трендов, сегментов и экспорта.  
4. Описаны схемы `MetricQueryRequest`, `VisualMetricPoint`, `SegmentBreakdown`, `MetricExportStatus`, `AlertSubscription`.  
5. Экспорт отчётов реализован (POST + GET).  
6. Kafka события и источники данных представлены в `x-sources`.  
7. Ошибки используют shared responses с `x-error-code`.  
8. Примеры включают четыре ключевых метрики.  
9. README `analytics/visuals` обновлён (после реализации).  
10. Указана зависимость от `API-TASK-331..336`.

---

## ❓ FAQ

**Q:** Откуда берутся данные?  
A: Через telemetry и события `character.visuals.*`, `gameplay.visuals.*`, `economy.visuals.*`, `marketing.visuals.*`; описать в `x-data-sources`.

**Q:** Можно ли добавлять кастомные метрики?  
A: Поддерживаются через `customMetrics[]` и расширения; потребуется консенсус analytics-team.

**Q:** Нужны ли realtime обновления?  
A: Базовый API — pull; realtime реализуется через websocket/alert сервис (вне текущего scope), но `AlertSubscription` готовит почву.

**Q:** Как обеспечивать безопасность?  
A: Использовать bearer-token, service-token; агрегированные данные — read-only, экспорт требует роли `analytics.export`.

---

**Следующие действия исполнителя:** реализовать спецификацию, вынести схемы/примеры, обновить README, прогнать валидацию.

