# Task ID: API-TASK-325
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:55  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** [API-TASK-322], [API-TASK-323], [API-TASK-324]

---

## 📋 Краткое описание

Создать спецификацию `api/v1/character/visuals/archetypes-detailed.yaml`, описывающую детальные визуальные профили игровых архетипов и ключевых NPC с поддержкой палитр, аксессуаров и динамических эффектов.

**Что нужно сделать:** Сформировать OpenAPI 3.0.3 документ для character-service, обеспечивающий выдачу `VisualArchetypeDetailedProfile`, управление пресетами и экспорт ассет-пакетов по данным детального визуального гида.

---

## 🎯 Цель задания

Дать командам персонажей, UX и маркетинга единый источник правды по визуалу архетипов, чтобы синхронизировать арт-ассеты, динамические эффекты и реакции сервисов.

**Зачем это нужно:**
- Обеспечить фронтенд модули и маркетинговые витрины актуальными описаниями архетипов.  
- Согласовать палитры, материалы и эффектные слои между character-service и smarthub UI.  
- Упростить генерацию ассет-пакетов и контроль качества визуалов.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 11:18  
**Статус документа:** approved (api-readiness: ready)

**Что важно из документа:**
- Разделы «Методика детализации» и «Архетипы игроков и ключевых NPC».  
- Палитры, материалы, микродинамика, фракционные мотивы и аксессуары.  
- Требования к JSON схемам `VisualArchetypeDetailedProfile`, Kafka потоку `character.visuals.archetype.detailed.updated`, метрикам `ArchetypeVisualFidelity`.

### Дополнительные источники

- `.BRAIN/03-lore/visual-guides/visual-style-assets.md` — базовые профили для согласования терминов.  
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — влияние архетипов на насыщенность NPC.  
- `API-SWAGGER/api/v1/character/visuals/archetypes.yaml` (если существует) — проверить совместимость и версионирование.

### Связанные документы

- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — социальные заказы меняют визуал NPC.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — соответствие архетипов локациям.  
- `API-SWAGGER/api/v1/world/visuals/locations-detailed.yaml` (API-TASK-322) — кросс-ссылки для ассетов.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/character/visuals/archetypes-detailed.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── character/
            └── visuals/
                └── archetypes-detailed.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** character-service  
- **Порт:** 8091  
- **API Base:** `/api/v1/character/visuals/*`  
- **Зависимости:** auth-service (JWT), world-service (локационные ссылки), social-service (социальные роли), marketing-service (ассет-пакеты)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/characters/encyclopedia`  
- **State Store:** `useCharacterStore` (visualArchetypes, featuredNpcProfiles)  
- **UI компоненты (@shared/ui):** CharacterProfileCard, ArchetypePaletteStrip, EquipmentSlotGrid, AnimationPreview, MetricChip  
- **Формы (@shared/forms):** ArchetypeFilterForm, AssetExportForm  
- **Layouts:** CharacterCodexLayout (`@shared/layouts`)  
- **Hooks:** useArchetypeFilters, useAssetExport

### Комментарий
Добавить в верх документа YAML-блок:
```yaml
# Target Architecture:
# - Microservice: character-service (port 8091)
# - Frontend Module: modules/characters/encyclopedia
# - UI Components: @shared/ui (CharacterProfileCard, ArchetypePaletteStrip, EquipmentSlotGrid, AnimationPreview, MetricChip)
# - Forms: @shared/forms (ArchetypeFilterForm, AssetExportForm)
# - State: useCharacterStore (visualArchetypes, featuredNpcProfiles)
# - API Base: /api/v1/character/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Извлечь требования из `.BRAIN`** — зафиксировать обязательные поля (силуэт, материалы, палитры, динамика, фракции, аксессуары, микродетали).  
2. **Проектировать эндпоинты** — чтение списка архетипов, получение детального профиля, управление пресетами, экспорт ассетов, предпросмотр.  
3. **Определить модели** — `VisualArchetypeDetailedProfile`, `VisualLayer`, `DynamicEffect`, `AccessoryDescriptor`, `FactionSignature`, `AssetExportRequest`, `AssetExportBundle`.  
4. **Настроить безопасность и ошибки** — BearerAuth, ErrorResponse из shared/common, статусы 400/404/409/503.  
5. **Документировать Kafka событие** — `character.visuals.archetype.detailed.updated` с payload, связи с маркетингом.  
6. **Обозначить метрики и зависимость** — `ArchetypeVisualFidelity`, взаимодействие с population алгоритмом.  
7. **Проверить размер файла** — при необходимости вынести схемы в `api/v1/character/visuals/components/archetypes-detailed.yaml` с README.  
8. **Прогнать `scripts/validate-swagger.ps1`** на готовом YAML.

---

## 🔀 Endpoints

1. **GET `/api/v1/character/visuals/archetypes/detailed`**  
   - Фильтры: `faction`, `role` (player, npc), `rarity`, `styleTag`, `macroZone`, `limit`, `offset`.  
   - Ответ 200: пагинированный список `VisualArchetypeDetailedProfile` (сокращённый вид).  
   - Ошибки: 400 (невалидные фильтры), 401/403, 503.

2. **GET `/api/v1/character/visuals/archetypes/{archetypeId}`**  
   - Path: `archetypeId` (`ARCH-[A-Z0-9-]+`).  
   - Ответ 200: полный профиль со слоями, эффектами, оборудованием, микроанимациями, связанными локациями и хабами.  
   - Ошибки: 404, 410 (архетип архивирован), 503.

3. **PATCH `/api/v1/character/visuals/archetypes/{archetypeId}/presets/{presetId}`**  
   - Тело: `ArchetypePresetUpdate` (palette overrides, accessory toggles, animation set, availability windows).  
   - Ответ 200: обновлённый `VisualArchetypeDetailedProfile`.  
   - Ошибки: 400, 409 (конфликт ревизий), 412 (QA не подтверждено).

4. **POST `/api/v1/character/visuals/archetypes/export`**  
   - Тело: `AssetExportRequest` (archetypeIds[], includeAnimations, includeAudio, channels).  
   - Ответ 202: `AssetExportTicket` (id, status, eta).  
   - Ошибки: 400, 409 (активный экспорт), 503.

5. **GET `/api/v1/character/visuals/archetypes/export/{ticketId}`**  
   - Возвращает `AssetExportBundle` (cdnLinks[], palette, audio tracks, metadata).  
   - Ошибки: 404, 410 (истёк), 423 (в обработке).

Все ошибки должны ссылаться на `shared/common/responses.yaml#/components/responses/ErrorResponse`. Пагинация — через `shared/common/pagination.yaml`.

---

## 🧱 Модели данных

- **VisualArchetypeDetailedProfile**  
  Поля: `archetypeId`, `name`, `role`, `faction`, `silhouette`, `materials[]`, `palette` (primary/secondary/accent), `lighting`, `dynamicEffects[]`, `microAnimations[]`, `accessories[]`, `equipmentLoadout`, `socialAttributes`, `safetyConsiderations`, `marketingTags[]`, `relatedLocations[]`, `lastUpdated`.  
  Пример: Corpo Operative, Street Ronin, Nomad Mechanist, Synth Mystic, Data Broker.

- **VisualLayer** (`layerId`, `layerType`, `description`, `intensity`, `visibilityRules`).  
- **DynamicEffect** (`effectId`, `trigger`, `visualCue`, `audioCue`, `duration`, `hazardLevel`).  
- **AccessoryDescriptor** (`accessoryId`, `category`, `material`, `brand`, `animationRef`).  
- **FactionSignature** (`factionId`, `logo`, `patterns`, `colorCodes`, `affinityScore`).  
- **ArchetypePresetUpdate** (`presetId`, `paletteOverride`, `materialsOverride`, `effectOverrides`, `effectiveFrom`, `effectiveTo`, `qaChecklistId`).  
- **AssetExportRequest** (`archetypeIds[]`, `includeAnimations`, `includeAudio`, `channels[]`, `requestedBy`).  
- **AssetExportTicket** (`ticketId`, `status`, `expiresAt`, `estimatedReadyAt`).  
- **AssetExportBundle** (`ticketId`, `cdnLinks[]`, `palette`, `animationRefs[]`, `audioRefs[]`, `generatedAt`).

Указать `required`, enum для `role`, `layerType`, `effectId`, `channels`.

---

## 📡 Kafka и интеграции

- **Producer:** character-service публикует `character.visuals.archetype.detailed.updated` `{ archetypeId, revision, palette, accessories[], dynamicEffects[], updatedAt }`.  
- **Consumers:** ui-service, marketing-service, telemetry, social-service (для NPC реакций).  
- Описать зависимость на `marketing.visuals.package.generated` (API-TASK-324) и `world.visuals.location.detailed.updated` (API-TASK-322) в разделе `x-dependencies`.  
- Подписка на `social.visuals.hub.activity` — для синхронизации ambient отображения.

---

## 📊 Метрики и аналитика

- `ArchetypeVisualFidelity` — показатель соответствия арта и профиля.  
- `PresetAdoptionRate` — использование пресетов игроками и NPC.  
- `AccessoryConversion` — частота выбора аксессуаров в маркетплейсе.  
- Метрики публикуются в telemetry и должны быть описаны в ответах или разделе `x-metrics`.

---

## ⚙️ Принципы и правила реализации

- Соблюдать SOLID, DRY, KISS; использовать `$ref` на общие компоненты.  
- Не хардкодить списки архетипов — оперировать ID и links на БД.  
- Поддерживать ссылку на `.BRAIN` документ в info.description.  
- Ограничить размер файла ≤400 строк; при превышении вынести `components` и добавить README.  
- Убедиться, что схемы совместимы с генератором SDK.

---

## ✔️ Критерии приемки

1. `api/v1/character/visuals/archetypes-detailed.yaml` создан и содержит Target Architecture блок.  
2. Описаны все 5 эндпоинтов с параметрами, примерами и кодами ошибок.  
3. Пагинация и ошибки используют общие компоненты.  
4. Модели `VisualArchetypeDetailedProfile`, `DynamicEffect`, `AccessoryDescriptor`, `AssetExportRequest` задокументированы с `required` и примерами.  
5. Kafka событие и зависимости отражены в спецификации.  
6. В info.description указаны ссылки на `.BRAIN` и связанные задачи.  
7. Метрики `ArchetypeVisualFidelity`, `PresetAdoptionRate` описаны.  
8. Файл проходит `scripts/validate-swagger.ps1`.  
9. Размер файла ≤400 строк или есть план выноса компонентов.  
10. Обозначена связь с `useCharacterStore` и фронтенд модулем.  
11. PATCH endpoint учитывает QA согласование.  
12. Экспорт поддерживает многоцелевые каналы (marketing, ui, analytics).

---

## ❓ FAQ

- **Вопрос:** Нужен ли DELETE для пресетов?  
  **Ответ:** Нет, пресеты управляются контент- пайплайном. PATCH позволяет менять активный набор через overrides.

- **Вопрос:** Как учитывать фракционные расцветки?  
  **Ответ:** Через `FactionSignature` и ссылки на `.BRAIN` фракционные документы; enum значений закрепить в спецификации.

- **Вопрос:** Можно ли создавать кастомные архетипы через API?  
  **Ответ:** Нет, только чтение и обновление утверждённых пресетов. Создание остаётся за контент-пайплайном.

- **Вопрос:** Где хранить параметры микроанимаций?  
  **Ответ:** В `microAnimations[]` в профиле с ссылками на asset registry; OpenAPI фиксирует структуру без хардкода.

- **Вопрос:** Нужно ли поддерживать локализацию?  
  **Ответ:** Да, добавить `nameLocalized` и `descriptionLocalized` (map locale → string) в модель.

---

## 📌 История выполнения

- 2025-11-08 — Задание создано AI агентом GPT-5 Codex на основе `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`.



