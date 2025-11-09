# Task ID: API-TASK-364
**Тип:** API Generation
**Приоритет:** критический
**Статус:** queued
**Создано:** 2025-11-08 18:25
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-331, API-TASK-332, API-TASK-333, API-TASK-337, API-TASK-362

---

## 📋 Краткое описание

Подготовить OpenAPI-спецификацию `World Visual Assets Detailed`, объединяющую детальные визуальные профили персонажей, оружия, имплантов, предметов и дронов с многоуровневыми эффектами и анимациями.

**Что нужно сделать:** Создать `api/v1/world/visuals/assets-detailed.yaml` (и при необходимости компоненты) на основе `.BRAIN/03-lore/visual-guides/visual-style-assets-детально.md`, описав REST API, справочники, bulk-синхронизацию и Kafka события для детализированных визуальных ассетов.

---

## 🎯 Цель задания

Обеспечить world-service единым детализированным каталогом визуальных ассетов, который потребляется character-, gameplay-, economy-, social- и marketing-сервисами, а также фронтендом.

**Зачем это нужно:**
- Свести к единому контракту слоистые визуальные данные (`gearLayers`, `ambientAnimations`, `recoilFx`).
- Синхронизировать REST API с существующими детализированными спецификациями (аркетипы, экипировка, предметы) и предоставить агрегирующие endpoints.
- Упростить экспорт и валидацию ассетов для маркетинга, UI и аналитики.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`
**Путь к документу:** `.BRAIN/03-lore/visual-guides/visual-style-assets-детально.md`
**Версия документа:** v1.0.0 (2025-11-08 09:45)
**Статус документа:** approved, api-readiness: ready

**Что важно из этого документа:**
- Детализированные профили архетипов, оружия, имплантов, экипировки, предметов и артефактов.
- Asset registry с разбиением слоёв (base/gear/fx), JSON источники и Kafka темы.
- JSON схемы `CharacterVisualProfileDetailed`, `WeaponVisualProfileDetailed`, `ImplantVisualProfile`, `ItemPreviewPayload`.
- Требования по интеграции с character-, gameplay-, economy-, social-, marketing-сервисами.

### Дополнительные источники

- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md` — расширенные сценарии и связанные задания.
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — требования к NPC визуалам.
- `.BRAIN/02-gameplay/combat/combat-shooting-advanced.md` — эффекты оружия, heat stages, alt fire.
- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — визуальные реакции социальных заказов.

### Связанные задания

- `task-331-character-visuals-archetypes-detailed-api.md`
- `task-332-gameplay-visuals-equipment-detailed-api.md`
- `task-333-economy-visuals-items-detailed-api.md`
- `task-337-visuals-analytics-metrics-api.md`
- `task-362-world-visuals-assets-api.md`

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/world/visuals/assets-detailed.yaml`
> ⚠️ Ограничить файл ≤400 строк. Вынести схемы в `api/v1/world/visuals/components/visual-assets-detailed-schemas.yaml`, если объём превышает лимит.
**API версия:** v1 (semantic version 1.0.0)
**Тип файла:** OpenAPI 3.0.3 YAML

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── visuals/
                ├── assets.yaml              # базовая версия (см. API-TASK-362)
                ├── assets-detailed.yaml     # создать в этом задании
                └── components/
                    ├── visual-assets-schemas.yaml
                    └── visual-assets-detailed-schemas.yaml (по необходимости)
```

**Если файл уже существует:**
- Обновить и синхронизировать с существующими компонентами, соблюдая совместимость с задачами 331-333.
- Использовать `$ref` на общие модели из `shared/common/` и `visual-assets-schemas.yaml`.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)

- **Микросервис:** world-service
- **Порт:** 8086
- **API Base Path:** `/api/v1/world/visuals/*`
- **Домен:** детализированные визуальные ассеты, мультимедийные слои, FX-анимации.
- **Зависимости:**
  - character-service (NPC/PC визуал)
  - gameplay-service (оружие, импланты, способности)
  - economy-service (товары, магазины)
  - social-service (романтические и социальные сценарии)
  - marketing-service (экспорт витрин)
  - analytics-service (метрики использования ассетов)
  - auth-service (валидация ролей `art-admin`, `world-admin`, `marketing-admin`)

**Event Streams:** `world.visual.assets.detailed.updated`, `world.visual.assets.detailed.bulk-sync`, `marketing.visual.showcase.updated`

### Frontend (модули)

- **Основной модуль:** `modules/world/visual-guides`
- **Дополнительные:** `modules/characters/encyclopedia`, `modules/combat/armory`, `modules/social/romance`, `modules/marketing/showcase`
- **State Stores:** `useWorldStore` (`visualAssetsDetailed`), `useCombatStore` (`armoryAssetsDetailed`), `useSocialStore` (`romanceVisuals`)
- **UI компоненты (@shared/ui):** CharacterCardDetailed, WeaponPreviewDetailed, ImplantOverlay, ItemTileDetailed, DroneCardFX
- **Формы (@shared/forms):** VisualAssetAdvancedFilterForm, VisualAssetBulkUploadForm, ShowcaseConfigForm
- **Hooks (@shared/hooks):** usePalettePreview, useFxTimeline, useDebounce, useAudioPreview
- **Layouts:** GameLayout, ArmoryLayout, ShowcaseLayout

**Комментарий:** В начале OpenAPI файла включить архитектурный блок (см. шаблон) с перечислением микросервиса, модулей, компонентов UI и state.

### OpenAPI

- Заполнить `info.x-microservice`: `name: world-service`, `port: 8086`, `domain: world`, `base-path: /api/v1/world/visuals`, `package: com.necpgame.worldservice`.
- Секция `servers`: только `https://api.necp.game/v1` и `http://localhost:8080/api/v1`.
- Подключить `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.

---

## ✅ Что нужно сделать (детальный план)

### Шаг 1: Моделирование данных

**Действия:**
1. Сформировать основной `DetailedVisualAssetProfile` с полями: `assetId`, `category`, `subCategory`, `gearLayers`, `ambientAnimations`, `particleFx`, `recoilFx`, `heatStages`, `altFireModes`, `localizedDescriptions`.
2. Определить подмодели через `oneOf`: `CharacterVisualDetailed`, `WeaponVisualDetailed`, `ImplantVisualDetailed`, `ItemVisualDetailed`, `DroneVisualDetailed`.
3. Добавить `VisualFxPreset`, `AnimationSet`, `AudioCueSet`, `BrandingAttributes`, `MarketingShowcaseConfig`.

**Ожидаемый результат:** Полный набор схем в `components` с валидацией assetId (regex), примерами и ссылками на JSON источники.

### Шаг 2: REST endpoints

**Обязательные endpoints:**
1. `GET /world/visuals/assets/detailed` — список ассетов с фильтрами `category`, `brand`, `faction`, `fxType`, `supportsRomance`, `supportsMarketing`, пагинация.
2. `GET /world/visuals/assets/detailed/{assetId}` — детальный профиль, включая слои, эффекты, аудио, ссылки на DTO.
3. `GET /world/visuals/assets/detailed/{assetId}/showcase` — конфигурация витрины (marketing-service) с `lightingScenes`, `cameraPaths`.
4. `POST /world/visuals/assets/detailed:bulk-sync` — обновление ассетов (до 200 объектов, требуется роль `art-admin`/`world-admin`).
5. `POST /world/visuals/assets/detailed/{assetId}/publish` — публикация ассета в маркетинговую витрину (`marketing-admin`).

**Дополнительно:** предусмотреть query `version`, `status`, `include=fx,animations,audio`, заголовки `X-Trace-Id`, `X-Request-Source`, `If-Match`.

**Ожидаемый результат:** секция `paths` с детальными описаниями параметров, кодами ответов (200, 202, 204, 400, 401, 403, 404, 409, 422, 500) через общие компоненты.

### Шаг 3: Kafka события

**Действия:**
1. Задокументировать `world.visual.assets.detailed.updated`, `world.visual.assets.detailed.bulk-sync`, `marketing.visual.showcase.updated`, `analytics.visual.assets.metric` в `components.messages`.
2. Для каждого payload указать `assetId`, `category`, `version`, `fxSummary`, `updatedBy`, `timestamp`.
3. Добавить `x-integrations` с ссылками на потребителей (character-, gameplay-, economy-, marketing-, analytics-сервисы).

### Шаг 4: Безопасность и роли

**Действия:**
1. Подключить `securitySchemes` из `shared/common/security.yaml`.
2. Определить `x-roles` для каждого эндпоинта (просмотр — `player`, `designer`, `gm`, `marketing-view`; bulk/publish — `art-admin`, `world-admin`, `marketing-admin`).
3. Прописать требования к аудиту (`createdBy`, `updatedBy`, `publishedBy`, `approvedBy`).

### Шаг 5: Примеры и расширения

**Действия:**
1. Добавить `examples` и `x-codeSamples` (curl, TypeScript) для ключевых endpoint.
2. Вставить `x-frontend` с перечислением модулей, DTO путей (`world/visual/detailed/*.json`, `marketing/showcase/*.json`).
3. Добавить `x-monitoring` (метрики latency, cacheHitRatio, payloadSize) и `x-governance` (версионность, review board).

### Шаг 6: Валидация

**Действия:**
1. Прогнать `scripts/validate-swagger.ps1`.
2. Пройти чеклист `tasks/config/checklist.md` (особенно блоки 1-12, 15 Governance).
3. Убедиться, что файл ≤400 строк, схемы вынесены, ссылки на общие компоненты корректные.

---

## 📏 Критерии приемки (13 пунктов)

1. Создан `api/v1/world/visuals/assets-detailed.yaml` и валидирован `scripts/validate-swagger.ps1`.
2. Заполнен `info.x-microservice` (world-service, порт 8086, base-path `/api/v1/world/visuals`).
3. `servers` содержит только gateway URL (`https://api.necp.game/v1`, `http://localhost:8080/api/v1`).
4. `GET /world/visuals/assets/detailed` использует пагинацию из `shared/common/pagination.yaml` и фильтры из документа.
5. Модель `DetailedVisualAssetProfile` включает обязательные поля `assetId`, `category`, `gearLayers`, `ambientAnimations`, `particleFx`, `recoilFx`.
6. Регулярные выражения для assetId соответствуют форматам (`ASSET-CHAR-...`, `ASSET-WEAPON-...`, `ASSET-ITEM-...`, `ASSET-DRONE-...`).
7. `bulk-sync` ограничивает запрос 200 объектами, документирует `409` (дубликаты), `422` (валидация), `202` (асинхронная обработка).
8. `publish` endpoint доступен только ролям `marketing-admin`, возвращает `202 Accepted` и публикует Kafka событие.
9. Kafka сообщения описаны с payload, включая `fxSummary` и `version`.
10. Добавлены `x-frontend`, `x-integrations`, `x-monitoring`, `x-governance` расширения.
11. Все ошибки используют ответы из `shared/common/responses.yaml`.
12. В файле нет дублирования схем; общие модели вынесены в `visual-assets-detailed-schemas.yaml` (если >400 строк).
13. Проверка чеклистом `tasks/config/checklist.md` выполнена и отражена в задании.

---

## ❓ FAQ

**В: Чем отличается от API-TASK-362?**  
О: API-TASK-362 покрывает базовый каталог ассетов. Текущее задание агрегирует детальные данные (слои, FX, анимации, маркетинг) и замыкает их в одном REST API.

**В: Нужно ли дублировать схемы из задач 331-333?**  
О: Нет, использовать `$ref` на их компоненты или вынесенные общие схемы; расширять через `allOf`/`oneOf`.

**В: Как обрабатывать архивные ассеты?**  
О: Добавить поле `status` (`active`, `deprecated`, `archived`) и фильтр `status` в списке.

**В: Требуется ли поддерживать мультиязычные описания?**  
О: Да, `localizedDescriptions` (минимум `en`, `ru`, `ja`).

**В: Нужно ли интегрировать с маркетингом?**  
О: Да, `publish` endpoint формирует showcase конфигурацию и публикует Kafka `marketing.visual.showcase.updated`.
