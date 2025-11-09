# Task ID: API-TASK-332
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:48  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-329]

---

## 📋 Краткое описание

Создать спецификацию `Gameplay Visual Equipment Detailed API`, описывающую расширенные визуальные профили оружия, брони, имплантов и аксессуаров: эффекты, динамические состояния, анимации и экспорт ассетов.  
**Целевой файл:** `api/v1/gameplay/visuals/equipment-detailed.yaml`

---

## 🎯 Цель задания

Обеспечить gameplay-service контрактом, который:
- раскрывает многоуровневые состояния (стандарт/боевой/stealth/романтический/легендарный) для каждого типа экипировки;  
- документирует эффекты (частицы, свет, глич), анимации и взаимодействия с окружением;  
- поддерживает интеграцию с loadout UI, маркетингом и аналитикой через экспорт ассетов;  
- синхронизирует Kafka события и метрики визуальной эффективности.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:18  
**Статус:** approved (api-readiness: ready)

**Содержимое:**
- Детальные профили оружия (smart, tech, энергетическое, тяжёлые платформы), ближнего боя, имплантов, бронесистем, аксессуаров.  
- JSON схемы для `VisualEquipmentDetailedProfile`, `EffectScenario`, `MaterialDynamicProfile`, `VariantState`, `ExportJob`.  
- Kafka события: `gameplay.visuals.effect.triggered`, `gameplay.visuals.variant.updated`.  
- Метрики: `EquipmentVisualEngagement`, `VisualEffectUptime`, UX/QA подтверждения (ART-VIS-DET-004 и др.).

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/combat-session-effects.md`, `.BRAIN/02-gameplay/combat/weapon-archetypes.md` — боевые эффекты и классы.  
- `.BRAIN/02-gameplay/progression/progression-skills-mapping.md` — навыки и взаимосвязи.  
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` — realtime взаимодействия.  
- `API-SWAGGER/api/v1/gameplay/visuals/equipment.yaml` — базовая спецификация (задача 329).

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/gameplay/visuals/equipment-detailed.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── gameplay/
            └── visuals/
                ├── README.md
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── equipment-detailed.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** gameplay-service  
- **Порт:** 8083  
- **Base Path:** `/api/v1/gameplay/visuals/*`  
- **Интеграции:** character-service (архетипы), economy-service (витрины), realtime-service (боевые эффекты), analytics-service, marketing-service.  
- **Kafka:** `gameplay.visuals.effect.triggered`, `gameplay.visuals.variant.updated`, `gameplay.visuals.export.completed`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/gameplay/loadouts-lab  
- **State Store:** `useGameplayStore(detailedVisuals)`  
- **UI:** `EquipmentDetailedViewer`, `EffectScenarioTimeline`, `VariantMatrix`, `MaterialDynamicPreview`, `ExportQueueStatus`  
- **Формы:** `EffectScenarioFilterForm`, `EquipmentExportConfigForm`  
- **Layouts:** `LoadoutLabLayout`, `GameLayout`  
- **Хуки:** `useEffectScenarios`, `useVariantPreview`, `useExportQueue`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - Frontend Module: modules/gameplay/loadouts-lab
# - State Store: useGameplayStore(detailedVisuals)
# - UI: EquipmentDetailedViewer, EffectScenarioTimeline, VariantMatrix, MaterialDynamicPreview, ExportQueueStatus
# - Forms: EffectScenarioFilterForm, EquipmentExportConfigForm
# - Layouts: LoadoutLabLayout, GameLayout
# - Hooks: useEffectScenarios, useVariantPreview, useExportQueue
# - Events: gameplay.visuals.effect.triggered, gameplay.visuals.variant.updated, gameplay.visuals.export.completed
# - API Base: /api/v1/gameplay/visuals/*
```

---

## ✅ План

1. **Собрать требования:** выделить расширенные поля (варианты, состояния, эффекты, материалы, совместимость, зависимости).  
2. **Схемы:** `VisualEquipmentDetailedProfile`, `EffectScenario`, `VariantState`, `MaterialDynamicProfile`, `EquipmentExportJob`.  
3. **Endpoints:** список детализированных профилей, детали, эффекты, варианты, экспорт/статус.  
4. **Kafka/метрики:** документировать события, описание метрик.  
5. **Ошибки/безопасность:** подключить shared security/responses/pagination.  
6. **Примеры:** Smart pistol легендарный, Monoblade романтический, Heavy exosuit BoS variant, Corporate armor cinematic, Tactical backpack nomad.  
7. **Валидация:** файл ≤400 строк, компоненты вынести, проверить `scripts/validate-swagger.ps1`.

---

## 🔌 Эндпоинты

1. **GET `/gameplay/visuals/equipment/detailed`**  
   - Параметры: `equipmentId`, `category`, `variant`, `state`, `effect`, `page`, `pageSize`.  
   - Ответ: `200 OK` (`PaginatedVisualEquipmentDetailedProfile`), ошибки `400/401/403/500`.

2. **GET `/gameplay/visuals/equipment/{equipmentId}/detailed`**  
   - Возвращает полное описание с эффектами, вариантами, материалами.  
   - Ответы: `200 OK`, `404`, `409`, `500`.

3. **GET `/gameplay/visuals/equipment/{equipmentId}/effects`**  
   - Список `EffectScenario`.  
   - Ответы: `200 OK`, `404`, `500`.

4. **GET `/gameplay/visuals/equipment/{equipmentId}/variants`**  
   - Возвращает `VariantState[]`.  
   - Ответы: `200 OK`, `404`, `500`.

5. **POST `/gameplay/visuals/equipment/export`**  
   - Тело: `VisualEquipmentDetailedExportRequest`.  
   - Ответы: `202 Accepted` (`EquipmentExportJobStatus`), `400`, `409`, `503`.

6. **GET `/gameplay/visuals/equipment/export/{jobId}`**  
   - Статус экспорта.  
   - Ответы: `200 OK`, `404`, `500`.

---

## 🧱 Модели

- **VisualEquipmentDetailedProfile** — базовый профиль + `visualStates[]`, `effectScenarios[]`, `materialDynamics[]`, `animationStacks[]`, `compatibility`, `metrics`.  
- **EffectScenario** — `scenarioId`, `description`, `trigger`, `intensityCurve`, `audioCue`, `cooldown`.  
- **VariantState** — `variantId`, `variantName`, `visualDiff`, `unlockRequirements`, `affinity`.  
- **MaterialDynamicProfile** — `layeredMaterials`, `emissiveSettings`, `wearPatterns`, `environmentInteraction`.  
- **VisualEquipmentDetailedExportRequest/Response**, **EquipmentExportJobStatus** — управление экспортом.  
- **PaginatedVisualEquipmentDetailedProfile** — пагинация.

Добавить `x-sources`, `x-related-apis`, `x-events`.

---

## 📏 Принципы

- OpenAPI 3.0.3, ≤400 строк; компоненты вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_FILTER`, `BIZ_EQUIPMENT_NOT_FOUND`, `BIZ_VARIANT_CONFLICT`, `INT_VISUAL_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`.  
- В `info.description` перечислить источники `.BRAIN`, дату, UX/QA подтверждение.  
- Указать связь с базовой спецификацией API-TASK-329.

---

## ✅ Критерии приемки

1. `api/v1/gameplay/visuals/equipment-detailed.yaml` создан и проходит `scripts/validate-swagger.ps1`.  
2. Комментарий `Target Architecture` добавлен.  
3. Схемы `VisualEquipmentDetailedProfile`, `EffectScenario`, `VariantState`, `MaterialDynamicProfile`, `EquipmentExportJobStatus` описаны.  
4. `GET /gameplay/visuals/equipment/detailed` поддерживает фильтры и пагинацию.  
5. Экспорт (`POST` + `GET jobId`) документирован.  
6. Kafka события и метрики отражены.  
7. Ошибки используют shared responses и содержат `x-error-code`.  
8. Примеры включают минимум пять категорий экипировки.  
9. README обновлён (после реализации).  
10. Dependence на базовый API (329) указана.

---

## ❓ FAQ

**Q:** Что включает детализированное API, чего нет в базовом?  
A: Многоуровневые состояния, эффекты, анимации, экспорт, метрики и события.

**Q:** Нужно ли описывать физику/коллизии?  
A: Да, через `MaterialDynamicProfile.environmentInteraction`; конкретные физические параметры остаются в backend.

**Q:** Что делать при конфликте вариантов?  
A: Возвращать `409` (`BIZ_VARIANT_CONFLICT`), описать разрешение (обновление или откат).

**Q:** Требуется ли описание звуков?  
A: Включить `audioCue` в `EffectScenario` и `VariantState`.

---

**Следующие действия исполнителя:** реализовать спецификацию, вынести компоненты, обновить README, прогнать валидацию.

