# Task ID: API-TASK-330
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:34  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Подготовить спецификацию `Economy Visual Items API`, описывающую визуальные карточки предметов для экономики/marketplace: внешний вид, редкость, промо-ассеты, витринные состояния и метрики конверсии.  
**Целевой файл:** `api/v1/economy/visuals/items.yaml`

---

## 🎯 Цель задания

Дать economy-service REST API, который:
- предоставляет визуальные описания товаров (оружие, импланты, косметика, редкие предметы, артефакты, пакеты услуг);
- поддерживает состояния витрины (featured, limited, seasonal), регионы показа, промо-ассеты и анимации;
- синхронизирует маркетинговые кампании и внутренние storefront UI через единый контракт;
- публикует события о выделенных предметах и собирает метрики визуальной эффективности.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:12  
**Статус:** approved (api-readiness: ready)

**Ключевые разделы:**
- Каталоги предметов, дронов, транспортных средств, артефактов, модных аксессуаров и косметики.  
- Правила брендинга, голографические эффекты, промо-версии и витринные оформления.  
- Журналы метрик `MarketplaceConversionVisual`, `VisualFidelityScore`.  
- Kafka события `economy.visuals.item.featured`.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — влияние на социальные заказы.  
- `.BRAIN/02-gameplay/economy/economy-auction-house.md` — правила marketplace.  
- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — соответствие локациям.  
- `API-SWAGGER/api/v1/gameplay/visuals/equipment.yaml` (задача 329) — ссылки на визуалы экипировки.

### Связанные документы

- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — генерация потоков товаров.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md` — детализированные состояния (для будущих задач).

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Файл:** `api/v1/economy/visuals/items.yaml`  
**API версия:** v1  
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
                └── items.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис):
- **Микросервис:** economy-service
- **Порт:** 8085
- **API пути:** `/api/v1/economy/visuals/*`
- **Интеграции:** gameplay-service (экипировка), character-service (архетипы), marketing-service (кампании), analytics-service, social-service (витринные события).
- **Kafka:** `economy.visuals.item.featured`, `marketing.visuals.package.generated`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):
- **Модуль:** modules/economy/marketplace
- **State Store:** `useEconomyStore(marketplaceVisuals)`
- **UI компоненты:** `MarketplaceItemCard`, `PromoBanner`, `VariantCarousel`, `ConversionMetricChip`, `AvailabilityBadge`
- **Формы:** `MarketplaceFilterForm`, `PromoScheduleForm`
- **Layouts:** `MarketplaceLayout`, `GameLayout`
- **Хуки:** `useMarketplaceFilters`, `usePromoScheduler`, `useRealtime`

### Комментарий в YAML:
```
# Target Architecture:
# - Microservice: economy-service (port 8085)
# - Frontend Module: modules/economy/marketplace
# - State Store: useEconomyStore(marketplaceVisuals)
# - UI: MarketplaceItemCard, PromoBanner, VariantCarousel, ConversionMetricChip, AvailabilityBadge
# - Forms: MarketplaceFilterForm, PromoScheduleForm
# - Layouts: MarketplaceLayout, GameLayout
# - Hooks: useMarketplaceFilters, usePromoScheduler, useRealtime
# - Events: economy.visuals.item.featured, marketing.visuals.package.generated
# - API Base: /api/v1/economy/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Определить визуальные атрибуты предметов:** название, категория, происхождение, редкость, визуальные эффекты, витринные состояния, маркетинговые теги.  
2. **Спроектировать схемы:** `VisualItemProfile`, `PromoAsset`, `DisplayState`, `MarketplaceContext`, `VisualItemExportRequest/Response`, `PaginatedVisualItemProfile`.  
3. **Описать endpoints:** список, детализация, управление витринными состояниями, экспорт промо-ассетов.  
4. **Подключить общие компоненты:** безопасность, ошибки, пагинация, ответы.  
5. **Документировать Kafka события и метрики:** payload `economy.visuals.item.featured`, метрики `MarketplaceConversionVisual`, `PromoClickThrough`.  
6. **Добавить примеры:** Weapon bundle, Fashion pack, Trauma Team medical kit, Neon graffiti capsule, Nomad memory charm, Corporate prestige card.  
7. **Прогнать `scripts/validate-swagger.ps1`, убедиться в соблюдении ограничений и ссылок на shared компоненты.**

---

## 🔌 Эндпоинты

1. **GET `/economy/visuals/items`**  
   - Параметры: `category`, `origin`, `rarity`, `displayState`, `promotion`, `page`, `pageSize`.  
   - Ответы: `200 OK` (`PaginatedVisualItemProfile`), `400`, `401/403`, `500`.

2. **GET `/economy/visuals/items/{itemId}`**  
   - Возвращает полную визуальную карточку предмета, вкл. promoAssets, compatibility (archetypes, equipment).  
   - Ответы: `200 OK`, `404`, `409`, `500`.

3. **POST `/economy/visuals/items/export`**  
   - Тело: `VisualItemExportRequest` (itemIds[], channels[], includePromo, targetLocales[], format).  
   - Ответы: `202 Accepted`, `400`, `409`, `503`.

4. **PATCH `/economy/visuals/items/{itemId}/display-state`**  
   - Изменяет витринное состояние (featured, limited, retired).  
   - Тело: `VisualItemDisplayStateUpdate`.  
   - Ответы: `200 OK`, `400`, `404`, `409`, `422`, `500`.

5. **GET `/economy/visuals/items/promotions/schedule`** (опционально) — список активных/запланированных промо (уточнить в реализации; можно вынести в отдельный компонент).

---

## 🧱 Модели данных

- **VisualItemProfile** — id, name, category, origin, rarity, description, visualEffects[], palette, promoAssets[], displayStates[], compatibility (archetypeIds, equipmentIds), metrics.  
- **DisplayState** — `stateType` (featured/limited/seasonal/retired), `startAt`, `endAt`, `regions[]`, `priority`.  
- **PromoAsset** — тип (image, video, hologram, audio, interactive), `url`, `format`, `resolution`, `preview`.  
- **MarketplaceContext** — `priceTier`, `currency`, `bundleId`, `availabilityWindow`.  
- **VisualItemExportRequest / Response** — параметры экспорта (channels: ui, marketing, analytics, ops).  
- **VisualItemDisplayStateUpdate** — состояние, даты, аудитории.  
- **PaginatedVisualItemProfile** — пагинация через общие компоненты.

Каждую схему снабдить `description`, `enum`, `format`, примерами; использовать `$ref` на shared модели при возможности.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3, ≤400 строк; схемы и примеры вынести в `components`.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_FILTER`, `BIZ_ITEM_NOT_FOUND`, `BIZ_DISPLAY_STATE_CONFLICT`, `INT_VISUAL_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`.  
- В `info.description` перечислить `.BRAIN` источники и дату.  
- Добавить `x-sources`, `x-related-apis` (character/gameplay visual APIs, marketing packages).  
- Обеспечить SOLID/DRY/KISS, не дублировать модели (использовать общие компоненты).

---

## ✅ Критерии приемки

1. `api/v1/economy/visuals/items.yaml` создан и валиден (`scripts/validate-swagger.ps1`).  
2. В начале файла присутствует блок `Target Architecture`.  
3. `GET /economy/visuals/items` поддерживает фильтры, пагинацию и возвращает `PaginatedVisualItemProfile`.  
4. Описаны модели `VisualItemProfile`, `DisplayState`, `PromoAsset`, `VisualItemExportRequest/Response`, `PaginatedVisualItemProfile`.  
5. Экспорт ассетов реализован и возвращает `202 Accepted`.  
6. PATCH обновление витринного состояния документировано с валидацией и кодами ошибок.  
7. Kafka событие `economy.visuals.item.featured` и метрики `MarketplaceConversionVisual`, `PromoClickThrough` задокументированы.  
8. Ошибки используют `$ref` на shared responses и содержат `x-error-code`.  
9. Добавлены примеры ключевых предметов (weapon bundle, Trauma Team kit, Neon graffiti capsule, etc.).  
10. README в `economy/visuals` обновлён ссылкой на спецификацию (в рамках реализации).

---

## ❓ FAQ

**Q:** Как синхронизировать визуальные карточки с ценами и стоками?  
A: Через `MarketplaceContext` и ссылки на backend economy API; текущая задача покрывает только визуальную часть.

**Q:** Нужно ли хранить динамический контент (видео, анимации) в API?  
A: Сохраняем ссылки (URL, формат, превью); фактические ассеты находятся в CDN/asset pipeline.

**Q:** Как обновлять витринные состояния без дублирования логики?  
A: Используйте PATCH endpoint, публикуйте событие `economy.visuals.item.featured`, чтобы фронтенд и маркетинг синхронизировались автоматически.

**Q:** Требуется ли поддержка локализации?  
A: Да, добавьте `localization` или `translations` в `VisualItemProfile` (указать массив локалей с визуальными вариациями).

---

**Следующие действия исполнителя:** реализовать спецификацию, вынести компоненты, обновить README, прогнать валидацию.

