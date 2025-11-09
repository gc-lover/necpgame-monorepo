# Task ID: API-TASK-323
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:15  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-162], [API-TASK-322]

---

## 📋 Краткое описание

Подготовить спецификацию `Social Visual Hubs Detailed API`, описывающую визуальные карточки социальных хабов, их активность и интеграцию с метриками удержания.  
**Что нужно сделать:** Создать OpenAPI-файл `api/v1/social/visuals/hubs-detailed.yaml` на основе `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`.

---

## 🎯 Цель задания

Дать social-service точный контракт для:
- публикации визуальных профилей Skyline Agora, Undermarket Bazaar, League Hub Conflux, Synth Faith Sanctum;
- передачи активности хабов (NPC трафик, featured vendors, ambience tags);
- синхронизации с world-service по микрозонам и маркетинговым кампаниям;
- мониторинга метрик HubAmbienceRetention и EventVisualImpact.

**Зачем это нужно:**
- Фронтенд модуль `modules/social/hubs` сможет отрисовывать «киберпанк» лаунжи с актуальными параметрами.
- Экономика и маркетинг получат данные о доступных витринах и аудиовизуальных пактах.
- Позволяет планировать социальные события, турниры и кооперативные активности.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`  
**Версия документа:** v1.0.0  
**Дата последнего обновления:** 2025-11-08 11:06  
**Статус документа:** approved

**Что важно:**
- Раздел «Социальные хабы — расширенная спецификация» с зонированием, NPC, светом и функциями.
- Связь с метриками удержания и маркетинговыми пакетами.
- Kafka топик `social.visuals.hub.activity`.
- Перекрестные ссылки на события в League Hub Conflux и Synth Faith Sanctum.

### Дополнительные источники

- `.BRAIN/03-lore/visual-guides/visual-style-locations.md` — сводные атрибуты.
- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — социальные экономики (пресеты для vendors).
- `API-SWAGGER/api/v1/social/player-orders/news.yaml` — примеры social-service спецификаций.
- `API-SWAGGER/api/v1/world/visuals/locations-detailed.yaml` (из задачи 001) — для синхронизации атрибутов.

### Связанные документы

- `.BRAIN/04-narrative/dialogues/quest-main-001-first-steps.md` (League Hub onboarding).
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` (NPC распределение).

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/social/visuals/hubs-detailed.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 Specification (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── visuals/
                ├── README.md
                └── hubs-detailed.yaml  ← создать/обновить
```

Компоненты вынести в `api/v1/social/visuals/components/` при превышении 400 строк.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис):
- **Микросервис:** social-service
- **Порт:** 8084
- **API пути:** `/api/v1/social/visuals/*`
- **Интеграции:** world-service (локации), economy-service (рынки, контракты), marketing-service (кампании), notification-service (ивенты)
- **Kafka:** `social.visuals.hub.activity`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):
- **Модуль:** modules/social/hubs
- **Путь:** modules/social/hubs/detailed
- **State Store:** `useSocialStore(hubs)`
- **UI компоненты:** `HubVisualPanel`, `AmbienceMeter`, `VendorCarousel`, `EventTicker`
- **Формы:** `HubHighlightForm`, `HubFilterForm`
- **Layouts:** `SocialHubLayout`, `GameLayout`
- **Хуки:** `useSocialRealtime`, `useHubFilters`, `useMarketingBundles`

### Комментарий в YAML:
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/hubs/detailed
# - State Store: useSocialStore(hubs)
# - UI: HubVisualPanel, AmbienceMeter, VendorCarousel, EventTicker
# - Forms: HubHighlightForm, HubFilterForm
# - Layouts: SocialHubLayout, GameLayout
# - Hooks: useSocialRealtime, useHubFilters, useMarketingBundles
# - Events: social.visuals.hub.activity
# - API Base: /api/v1/social/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Собрать атрибуты хабов:** Извлечь характеристики Skyline Agora, Undermarket Bazaar, League Hub Conflux, Synth Faith Sanctum и определить обязательные поля.  
   _Результат:_ таблица атрибутов и enum-значения для типов хабов.
2. **Определить модели:** Спроектировать `VisualHubDetailedProfile`, `HubZone`, `HubAmbience`, `HubActivitySample`, `HubVendorHighlight`, `HubMetricSnapshot`.  
   _Результат:_ schemas в components с описаниями и примерами.
3. **Описать endpoints:** Реализовать список, детализацию, realtime-срез активности и ссылку на маркетинговые пакеты.  
   _Результат:_ секция `paths` (минимум три эндпоинта) с параметрами/ресурсами.
4. **Интеграция с Kafka:** Добавить раздел о доставке `social.visuals.hub.activity`, payload ссылку на схему `HubActivityEvent`.  
   _Результат:_ документация расширений + ссылки на события.
5. **Ошибки и безопасность:** Подключить shared security/responses, определить `BIZ_HUB_LOCKED`, `VAL_FILTER_UNSUPPORTED`, `INT_HUB_SYNC_FAILED`.  
   _Результат:_ унифицированные ответы и коды ошибок.
6. **Примеры и метрики:** Подготовить примеры для Skyline Agora (day/night), League Hub Conflux (турнир), Undermarket Bazaar (контрабанда).  
   _Результат:_ components/examples и описание метрик в `info`.

---

## 🔌 Эндпоинты

1. **GET `/social/visuals/hubs/detailed`**  
   - **Назначение:** Возвращает страницу визуальных профилей хабов.  
   - **Параметры:** `locationId`, `hubType` (enum: agora, bazaar, league, sanctum, custom), `ambienceTag`, `minActivityScore`, `page`, `pageSize`.  
   - **Ответы:**  
     - `200 OK` — `PaginatedVisualHubDetailedProfile`.  
     - `400 Bad Request` — `VAL_FILTER_UNSUPPORTED`.  
     - `401/403` — shared security.  
     - `500` — `INT_HUB_SYNC_FAILED`.

2. **GET `/social/visuals/hubs/detailed/{hubId}`**  
   - **Назначение:** Выдаёт расширенную карточку хаба с зонами, NPC ролями, event hooks.  
   - **Ответы:**  
     - `200 OK` — `VisualHubDetailedProfile`.  
     - `404 Not Found` — `BIZ_HUB_NOT_FOUND`.  
     - `409 Conflict` — `BIZ_HUB_LOCKED` (идёт событие/рестрим).  
     - `500` — `INT_HUB_SYNC_FAILED`.

3. **GET `/social/visuals/hubs/detailed/{hubId}/activity`**  
   - **Назначение:** Возвращает последнюю агрегацию активности (traffic, ambience, featured vendors).  
   - **Параметры:** `lookbackMinutes` (query, default 60, max 720).  
   - **Ответы:**  
     - `200 OK` — `HubActivitySnapshot`.  
     - `400 Bad Request` — `VAL_INVALID_LOOKBACK`.  
     - `404 Not Found` — `BIZ_HUB_NOT_FOUND`.  
     - `429 Too Many Requests` — `VAL_RATE_LIMIT`.  
     - `500` — `INT_HUB_SYNC_FAILED`.

---

## 🧱 Модели данных

- **VisualHubDetailedProfile**  
  - `hubId`, `name`, `locationId`, `hubType`, `description`, `lightingProfile`, `ambience` (ref), `npcRoles[]`, `services[]`, `securityLevel`, `marketingHooks[]`, `openHours`, `linkedEvents[]`, `lastUpdated`.
- **HubAmbience**  
  - `soundscape`, `lightPalette`, `crowdDensity`, `moodTags[]`, `retentionScore`.
- **HubZone**  
  - `zoneId`, `purpose` (trade, performance, negotiation, ritual), `visualCue`, `accessLevel`, `featuredVendors[]`.
- **HubActivitySnapshot**  
  - `hubId`, `generatedAt`, `activityScore`, `trafficBreakdown` (NPC/players), `featuredVendors[]`, `ambienceTrend`, `alerts[]`.
- **HubVendorHighlight**  
  - `vendorId`, `category`, `rarity`, `promotionEndsAt`, `visualStyle`.
- **HubMetricSnapshot**  
  - `visualFidelityScore`, `hubAmbienceRetention`, `marketingAssetUtilization`, `playerPresence`, `alerts`.
- **HubActivityEvent** (Kafka payload)  
  - `hubId`, `timestamp`, `activityLevel`, `featuredVendors[]`, `ambienceTags[]`, `eventHook`.
- **PaginatedVisualHubDetailedProfile**  
  - `items[]`, `page`, `pageSize`, `totalItems`, `totalPages`.

Все модели снабдить `description`, ограничениями, примерами (Skyline Agora, Undermarket Bazaar, League Hub Conflux).

---

## 📏 Принципы и правила

- OpenAPI 3.0.3, ≤400 строк, компоненты выносить.  
- Использовать `shared/common/security.yaml` и `shared/common/responses.yaml`.  
- Подключить `shared/common/pagination.yaml` для списков.  
- Добавить `x-sources` со списком документов `.BRAIN`.  
- Метрики и события описывать как расширения (`x-metrics`, `x-events`) с payload ссылками.  
- Не фиксировать константы активности — только структуры и валидацию.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/visuals/hubs-detailed.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. Комментарий `Target Architecture` добавлен в начало файла.  
3. Все эндпоинты используют Bearer security и ссылку на общие ошибки.  
4. `GET /social/visuals/hubs/detailed` поддерживает фильтры и пагинацию через общие компоненты.  
5. Схемы описывают минимум четыре канонических хаба с примерами.  
6. Kafka событие `social.visuals.hub.activity` документировано с payload.  
7. Метрики `HubAmbienceRetention`, `VisualFidelityScore`, `MarketingAssetUtilization` описаны в info или `x-metrics`.  
8. `HubActivitySnapshot` содержит ограничения (например, `lookbackMinutes` ≤720).  
9. Ошибки используют `VAL_*`, `BIZ_*`, `INT_*` коды и `$ref` на shared ответы.  
10. Примеры включают Skyline Agora (business), Undermarket Bazaar (контрабанда), League Hub Conflux (турнир).  
11. README для директории `social/visuals` обновлён ссылкой на новый файл (можно поручить исполнителю в этой задаче).

---

## ❓ FAQ

**Q:** Нужно ли включать маркетинговые пакеты в этот API?  
**A:** Нет, они остаются в world/marketing. Здесь лишь ссылки (`marketingHooks`) на доступные пакеты и `marketing-service` топики.

**Q:** Как синхронизировать данные с world-service?  
**A:** Использовать `locationId` и `microHubId` из `VisualLocationDetailedProfile`, описать это в схемах и примерах.

**Q:** Что делать при высокой активности хаба?  
**A:** Возвращать `alerts[]` и код `429` при превышении rate limits; аналитика читает Kafka событие.

**Q:** Нужен ли отдельный POST для обновления активности?  
**A:** Обновления происходят автоматически через event pipeline; ручные обновления вне текущего scope.

---

**Следующие действия исполнителя:** подготовить структуру директории, описать схемы, задокументировать Kafka события, обновить README и прогнать валидацию спецификации.


