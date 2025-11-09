# Task ID: API-TASK-326
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:57  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** [API-TASK-299], [API-TASK-300], [API-TASK-325]

---

## 📋 Краткое описание

Подготовить спецификацию `api/v1/gameplay/visuals/equipment-detailed.yaml`, описывающую визуальные профили оружия, брони, имплантов и предметов с палитрами, динамикой, фракционными отметками и экспортом ассетов.

**Что нужно сделать:** Создать OpenAPI 3.0.3 документ для gameplay-service, включающий выдачу `VisualEquipmentDetailedProfile`, управление вариантами и экспорт мультимедийных паков для loadout UI.

---

## 🎯 Цель задания

Стандартизировать визуальные данные боевой экипировки, чтобы команды геймплея, экономики и маркетинга использовали единые схемы для отображения и апгрейдов.

**Зачем это нужно:**
- Обеспечить UI loadouts, marketplace и аналитике точные визуальные описания предметов.  
- Упростить синхронизацию эффектов (свет, звук, частицы) между gameplay-service и фронтендом.  
- Поддержать экспорт ассетов для маркетинговых кампаний и PvE событий.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 11:18  
**Статус документа:** approved (api-readiness: ready)

**Что важно из документа:**
- Разделы «Оружие — глубокая детализация», «Импланты и моды», «Экипировка и броня», «Предметы, артефакты и гаджеты».  
- Палитры, материалы, динамические эффекты, брендовые мотивы и JSON схемы.  
- Kafka событие `gameplay.visuals.equipment.variant`, метрики `EquipmentVisualFidelity`, требования к экспортным пакетам.

### Дополнительные источники

- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — влияние экипировки на NPC трафик.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — социальные эффекты, меняющие визуал предметов.  
- `API-SWAGGER/api/v1/gameplay/combat/loadouts/*.yaml` — проверить совместимость с существующими API.

### Связанные документы

- `API-SWAGGER/api/v1/character/visuals/archetypes-detailed.yaml` (API-TASK-325) — соответствие персонажей и экипировки.  
- `API-SWAGGER/api/v1/economy/player-orders/index.yaml` (API-TASK-320) — экономические эффекты на предметы.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — связь предметов с зонами.

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/gameplay/visuals/equipment-detailed.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── visuals/
                └── equipment-detailed.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** gameplay-service  
- **Порт:** 8083  
- **API Base:** `/api/v1/gameplay/visuals/*`  
- **Зависимости:** character-service (архетипы), economy-service (цены, редкости), world-service (зоны), marketing-service (ассет-паки)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/gameplay/loadouts`  
- **State Store:** `useCombatStore` (visualEquipment, variantCatalog, exportTickets)  
- **UI компоненты (@shared/ui):** WeaponCard, EquipmentPanel, VariantSwitcher, EffectTimeline, MetricChip  
- **Формы (@shared/forms):** EquipmentFilterForm, VariantOverrideForm, EquipmentExportForm  
- **Layouts:** LoadoutManagerLayout (`@shared/layouts`)  
- **Hooks:** useEquipmentFilters, useVariantPreview, useEquipmentExport

### Комментарий
В начало спецификации добавить:
```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - Frontend Module: modules/gameplay/loadouts
# - UI Components: @shared/ui (WeaponCard, EquipmentPanel, VariantSwitcher, EffectTimeline, MetricChip)
# - Forms: @shared/forms (EquipmentFilterForm, VariantOverrideForm, EquipmentExportForm)
# - State: useCombatStore (visualEquipment, variantCatalog, exportTickets)
# - API Base: /api/v1/gameplay/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Зафиксировать категории из `.BRAIN`** — оружие, броня, импланты, гаджеты; обязательные атрибуты (материалы, палитры, динамика).  
2. **Спроектировать эндпоинты** — получение списка, карточки предмета, управление вариантами, предпросмотр эффектов, экспорт ассетов.  
3. **Определить модели** — `VisualEquipmentDetailedProfile`, `VariantVisual`, `EffectCue`, `MaterialDescriptor`, `LoadoutCompatibility`, `EquipmentExportRequest`, `EquipmentExportBundle`.  
4. **Добавить безопасность и ошибки** — BearerAuth, ErrorResponse, коды 400/404/409/412/503.  
5. **Документировать Kafka** — `gameplay.visuals.equipment.variant`, связи с economy и marketing.  
6. **Отразить метрики** — `EquipmentVisualFidelity`, `VariantAdoptionRate`, `HazardCompliance`.  
7. **Проверить размер файла**, при необходимости вынести `components` в `api/v1/gameplay/visuals/components/equipment-detailed.yaml`.  
8. **Прогнать `scripts/validate-swagger.ps1`** после генерации.

---

## 🔀 Endpoints

1. **GET `/api/v1/gameplay/visuals/equipment/detailed`**  
   - Фильтры: `category` (weapon, armor, implant, gadget), `manufacturer`, `rarity`, `slot`, `macroZone`, `hazardLevel`, `limit`, `offset`.  
   - Ответ 200: `Page<VisualEquipmentDetailedProfile>`.  
   - Ошибки: 400, 401/403, 503.

2. **GET `/api/v1/gameplay/visuals/equipment/{equipmentId}`**  
   - Path: `equipmentId` (`EQP-[A-Z0-9-]+`).  
   - Ответ 200: полный профиль с вариантами, эффектами, материалами, доступными архетипами.  
   - Ошибки: 404, 410 (выведен из rotation).

3. **PATCH `/api/v1/gameplay/visuals/equipment/{equipmentId}/variants/{variantId}`**  
   - Тело: `VariantOverrideRequest` (paletteOverride, effectOverride, audioOverride, availabilityWindow, qaChecklistId).  
   - Ответ 200: обновлённый `VariantVisual`.  
   - Ошибки: 400, 409 (конфликт расписаний), 412 (QA не завершено).

4. **POST `/api/v1/gameplay/visuals/equipment/export`**  
   - Тело: `EquipmentExportRequest` (equipmentIds[], includeAnimations, includeParticles, channels).  
   - Ответ 202: `EquipmentExportTicket`.  
   - Ошибки: 400, 409, 503.

5. **GET `/api/v1/gameplay/visuals/equipment/export/{ticketId}`**  
   - Возвращает `EquipmentExportBundle` (cdnLinks, particleConfigs, audio, marketingAssets).  
   - Ошибки: 404, 410, 423 (в обработке).

6. **GET `/api/v1/gameplay/visuals/equipment/{equipmentId}/effects/preview`**  
   - Query: `effectId`, `intensity`, `environment`.  
   - Ответ 200: `EffectPreview` (shaderParams, video, audio).  
   - Ошибки: 400, 404, 503.

Ошибки — через `shared/common/responses.yaml`. Пагинация — общие компоненты.

---

## 🧱 Модели данных

- **VisualEquipmentDetailedProfile**  
  Поля: `equipmentId`, `name`, `category`, `slot`, `manufacturer`, `rarity`, `materials[]`, `palette`, `lighting`, `dynamicEffects[]`, `effectCues[]`, `variants[]`, `loadoutCompatibility`, `hazardCompliance`, `marketingTags[]`, `relatedArchetypes[]`, `linkedLocations[]`, `lastUpdated`.  
  Примеры: Smart Pistols, Militech Assault Rifle, Titan Freight Armor, Nomad Mechanist toolkit.

- **VariantVisual** (`variantId`, `name`, `description`, `paletteOverride`, `materialOverride`, `effectOverrides`, `availability`, `marketingNotes`).  
- **EffectCue** (`effectId`, `trigger`, `visualCue`, `audioCue`, `particlePreset`, `safetyLevel`).  
- **MaterialDescriptor** (`materialId`, `type`, `finish`, `emissiveLevel`, `shaderProfile`).  
- **LoadoutCompatibility** (`supportedRoles[]`, `recommendedArchetypes[]`, `conflicts[]`).  
- **VariantOverrideRequest**, **EquipmentExportRequest**, **EquipmentExportTicket**, **EquipmentExportBundle**, **EffectPreview** — описать поля, `required`, примеры.

---

## 📡 Kafka и интеграции

- **Producer:** gameplay-service → `gameplay.visuals.equipment.variant` `{ equipmentId, variantId, palette, effectOverrides[], updatedAt }`.  
- **Consumers:** loadouts-ui, economy-service, marketing-automation, telemetry.  
- Описать зависимость на `world.visuals.event.triggered` (API-TASK-324) для реагирования на события.  
- Подписка на `marketing.visuals.package.generated` (API-TASK-324) и `character.visuals.archetype.detailed.updated` (API-TASK-325).

---

## 📊 Метрики и аналитика

- `EquipmentVisualFidelity` — качество совпадения визуала.  
- `VariantAdoptionRate` — популярность вариантов в PvP/PvE.  
- `HazardCompliance` — соответствие требованиям безопасности и рейтингов.  
- Метрики передаются в telemetry и используются аналитикой.

---

## ⚙️ Правила реализации

- Использовать SOLID/DRY/KISS, `$ref` для повторяемых схем.  
- Не фиксировать статические списки — использовать идентификаторы и ссылки на registry.  
- Максимум 400 строк; при необходимости вынести компоненты с README.  
- Info.description должен содержать ссылки на `.BRAIN` и связанные задания.  
- Проверить совместимость с генерацией SDK и фронтенд-типов.

---

## ✔️ Критерии приемки

1. Файл `api/v1/gameplay/visuals/equipment-detailed.yaml` создан, содержит Target Architecture.  
2. Все 6 эндпоинтов задокументированы с параметрами, примерами, кодами ошибок.  
3. Пагинация и ошибки используют общие компоненты.  
4. Модели `VisualEquipmentDetailedProfile`, `VariantVisual`, `EffectCue`, `EquipmentExportRequest` описаны с `required` и примерами.  
5. Kafka событие и зависимости указаны.  
6. Метрики `EquipmentVisualFidelity`, `VariantAdoptionRate`, `HazardCompliance` отражены.  
7. Файл проходит `scripts/validate-swagger.ps1`.  
8. Размер ≤400 строк или есть план вынесения компонентов.  
9. Связь с `useCombatStore` и фронтенд модулем описана.  
10. PATCH endpoint учитывает QA и конфликты расписаний.  
11. Export flow поддерживает несколько каналов.  
12. Info.description содержит ссылки на `.BRAIN` документ и workshop 2025-11-08.

---

## ❓ FAQ

- **Вопрос:** Нужен ли endpoint для создания новых вариантов?  
  **Ответ:** Нет, варианты создаются арт-пайплайном. API поддерживает чтение и конфигурацию существующих вариантов.

- **Вопрос:** Как описывать частицы и аудио?  
  **Ответ:** Через `EffectCue` и `EquipmentExportBundle`, указывая ссылки на asset registry и параметры shader/audio.

- **Вопрос:** Нужно ли поддерживать локализацию?  
  **Ответ:** Да, добавить `nameLocalized` и `descriptionLocalized` (map locale → string) в модели профилей и вариантов.

- **Вопрос:** Как синхронизировать с economy-service?  
  **Ответ:** Указать `economyReference` в профиле и зависимость на API-TASK-320 (`player-orders`), описание в разделе `relatedTargets`.

- **Вопрос:** Есть ли ограничения по hazard уровню?  
  **Ответ:** Да, поле `hazardCompliance` должно ссылаться на нормативы QA (см. `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md`).

---

## 📌 История выполнения

- 2025-11-08 — Задание создано AI агентом GPT-5 Codex на основе `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`.



