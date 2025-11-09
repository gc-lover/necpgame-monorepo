# Task ID: API-TASK-333
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:50  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-330]

---

## 📋 Краткое описание

Разработать спецификацию `Economy Visual Items Detailed API`, описывающую расширенные визуальные карточки предметов: витринные состояния, промо-ассеты, региональные вариации, динамические эффекты и экспорт мультимедийных пакетов.  
**Целевой файл:** `api/v1/economy/visuals/items-detailed.yaml`

---

## 🎯 Цель задания

Предоставить economy-service детальный контракт, который:
- отображает разные состояния предметов (featured, seasonal, limited, retired, corporate exclusive);  
- описывает мультимедийные ассеты (изображения, видео, голограммы, аудио, интерактивные сцены) и их локализации;  
- позволяет управлять витринными расписаниями, регионами и кампаниями;  
- обеспечивает экспорт промо-пакетов для маркетинга, storefront UI и аналитики;  
- документирует Kafka события и метрики конверсии.

---

## 📚 Источники

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:18  
**Статус:** approved (api-readiness: ready)

**Ключевые моменты:**
- Детализированные описания предметов (оружие, импланты, косметика, артефакты, дроны, транспорт).  
- JSON схемы: `VisualItemDetailedProfile`, `PromoAssetExtended`, `DisplayScenario`, `LocalizationVariant`, `ExportJob`.  
- Kafka события: `economy.visuals.item.highlighted`, `marketing.visuals.package.generated`.  
- Метрики: `MarketplaceConversionVisual`, `VisualFidelityScore`, `PromoClickThrough`.  
- UX/QA подтверждения: `ART-VIS-DET-004`, `FW-VISUAL-DETAIL-003`.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — влияние на социальные рейтинги.  
- `.BRAIN/02-gameplay/economy/economy-auction-house.md` и `economy-marketplace.md` — бизнес-логика экономики.  
- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — соответствие локациям.  
- `API-SWAGGER/api/v1/economy/visuals/items.yaml` — базовая спецификация (задача 330).

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/economy/visuals/items-detailed.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── economy/
            └── visuals/
                ├── README.md
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── items-detailed.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** economy-service  
- **Порт:** 8085  
- **API Base:** `/api/v1/economy/visuals/*`  
- **Интеграции:** gameplay-service (экипировка), character-service (архетипы), marketing-service (кампании), analytics-service, social-service (события), localization-service.  
- **Kafka:** `economy.visuals.item.highlighted`, `marketing.visuals.package.generated`, `economy.visuals.export.completed`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/economy/marketplace-pro  
- **State Store:** `useEconomyStore(detailedVisuals)`  
- **UI:** `MarketplaceDetailedCard`, `PromoTimeline`, `RegionAvailabilityMap`, `ConversionMetricDashboard`, `ExportStatusTracker`  
- **Формы:** `PromoCampaignForm`, `LocalizationVariantForm`  
- **Layouts:** `MarketplaceProLayout`, `GameLayout`  
- **Хуки:** `usePromoScheduler`, `useLocalizationPreview`, `useExportStatus`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: economy-service (port 8085)
# - Frontend Module: modules/economy/marketplace-pro
# - State Store: useEconomyStore(detailedVisuals)
# - UI: MarketplaceDetailedCard, PromoTimeline, RegionAvailabilityMap, ConversionMetricDashboard, ExportStatusTracker
# - Forms: PromoCampaignForm, LocalizationVariantForm
# - Layouts: MarketplaceProLayout, GameLayout
# - Hooks: usePromoScheduler, useLocalizationPreview, useExportStatus
# - Events: economy.visuals.item.highlighted, marketing.visuals.package.generated, economy.visuals.export.completed
# - API Base: /api/v1/economy/visuals/*
```

---

## ✅ План

1. **Анализ:** выписать все детализированные параметры предметов (состояния витрины, ассеты, локализации, динамика).  
2. **Схемы:** `VisualItemDetailedProfile`, `DisplayScenario`, `PromoAssetExtended`, `LocalizationVariant`, `MarketplaceCampaign`, `VisualExportJob`.  
3. **Endpoints:** список, детализация, управление сценариями, локализациями, экспорт/статус.  
4. **Kafka/метрики:** документация payload-ов и метрик.  
5. **Ошибки/безопасность:** shared security/responses/pagination.  
6. **Примеры:** Weapon bundle cinematic, Trauma Team premium kit, Neon graffiti capsule interactive, Corporate prestige card VIP, Nomad memory charm tactile, Quantum dice animated.  
7. **Валидация:** структура ≤400 строк, вынести компоненты, `scripts/validate-swagger.ps1`.

---

## 🔌 Эндпоинты

1. **GET `/economy/visuals/items/detailed`**  
   - Параметры: `itemId`, `category`, `displayState`, `campaign`, `region`, `promotion`, `page`, `pageSize`.  
   - Ответ: `200 OK` (`PaginatedVisualItemDetailedProfile`), `400`, `401/403`, `500`.

2. **GET `/economy/visuals/items/{itemId}/detailed`**  
   - Полная карточка с ассетами, локализациями, метриками.  
   - Ответы: `200 OK`, `404`, `409`, `500`.

3. **GET `/economy/visuals/items/{itemId}/display-scenarios`**  
   - Список `DisplayScenario[]`.  
   - Ответы: `200 OK`, `404`, `500`.

4. **POST `/economy/visuals/items/{itemId}/display-scenarios`**  
   - Создание/обновление сценария витрины.  
   - Ответы: `201 Created`, `400`, `404`, `409`, `422`, `500`.

5. **GET `/economy/visuals/items/{itemId}/localizations`**  
   - Локализованные варианты визуалов.  
   - Ответы: `200 OK`, `404`, `500`.

6. **POST `/economy/visuals/items/export`**  
   - Тело: `VisualItemDetailedExportRequest`.  
   - Ответы: `202 Accepted` (`VisualExportJobStatus`), `400`, `409`, `503`.

7. **GET `/economy/visuals/items/export/{jobId}`**  
   - Статус/результаты экспорта.  
   - Ответы: `200 OK`, `404`, `500`.

---

## 🧱 Модели

- **VisualItemDetailedProfile** — базовый профиль + `displayScenarios[]`, `promoAssets[]`, `localizationVariants[]`, `metrics`, `campaigns[]`.  
- **DisplayScenario** — `scenarioId`, `state`, `schedule`, `regions`, `pricing`, `dependencies`, `priority`.  
- **PromoAssetExtended** — `assetId`, `type`, `url`, `format`, `duration`, `interactive`, `preview`, `locales[]`.  
- **LocalizationVariant** — `locale`, `title`, `description`, `visualOverrides`, `voiceover`, `subtitle`, `regulatoryNotes`.  
- **MarketplaceCampaign** — `campaignId`, `name`, `channels`, `startAt`, `endAt`, `budget`, `targetAudience`.  
- **VisualItemDetailedExportRequest/Response**, **VisualExportJobStatus** — управление экспортом.  
- **PaginatedVisualItemDetailedProfile** — пагинация.

Добавить `x-sources`, `x-related-apis`, `x-events` в components.

---

## 📏 Принципы

- OpenAPI 3.0.3, ≤400 строк, компоненты вынести.  
- `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_FILTER`, `BIZ_ITEM_NOT_FOUND`, `BIZ_DISPLAY_SCENARIO_CONFLICT`, `VAL_INVALID_LOCALE`, `INT_VISUAL_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`.  
- В `info.description` указать `.BRAIN` источники, даты, UX/QA подтверждение.  
- Ссылки на базовый API (330) и другие визуальные спецификации.

---

## ✅ Критерии приемки

1. `api/v1/economy/visuals/items-detailed.yaml` валиден (`scripts/validate-swagger.ps1`).  
2. Комментарий `Target Architecture` присутствует.  
3. `GET /economy/visuals/items/detailed` поддерживает фильтры и пагинацию.  
4. Схемы `VisualItemDetailedProfile`, `DisplayScenario`, `PromoAssetExtended`, `LocalizationVariant`, `MarketplaceCampaign`, `VisualExportJobStatus` описаны.  
5. Управление витринными сценариями `POST` + ответы документировано.  
6. Экспорт (`POST` + `GET jobId`) реализован.  
7. Kafka события и метрики отражены.  
8. Ошибки используют shared responses + `x-error-code`.  
9. Примеры для шести типов предметов включены.  
10. README обновлён (после реализации).  
11. Зависимость от базового API (330) указана.

---

## ❓ FAQ

**Q:** Нужно ли хранить видеоконтент в API?  
A: Возвращаем ссылки (URL + метаданные), файлы находятся в CDN/asset pipeline.

**Q:** Как обрабатывать различные регионы и регулирования?  
A: Используйте `LocalizationVariant` и `regulatoryNotes`; обрабатывать конфликты через `422` (`VAL_INVALID_LOCALE`).

**Q:** Что делать при перекрытии промо кампаний?  
A: Возвращать `409` (`BIZ_DISPLAY_SCENARIO_CONFLICT`), задокументировать правила разрешения.

**Q:** Как учитывать социальные события?  
A: Сценарии и кампании могут ссылаться на social-service через `relatedEvents` и Kafka события.

---

**Следующие действия исполнителя:** реализовать спецификацию, вынести схемы/примеры, обновить README, прогнать валидацию.

