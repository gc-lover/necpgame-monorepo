# Task ID: API-TASK-329
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:32  
**Создатель:** AI Task Creator Agent  
**Зависимости:** none

---

## 📋 Краткое описание

Разработать спецификацию `Gameplay Visual Equipment API`, описывающую визуальные профили оружия, брони, имплантов и аксессуаров, их материалы, эффекты и состояния использования.  
**Целевой файл:** `api/v1/gameplay/visuals/equipment.yaml`

---

## 🎯 Цель задания

Обеспечить gameplay-service контрактом, который:
- предоставляет каталог визуальных профилей экипировки (оружие дальнего/ближнего боя, импланты, броня, аксессуары);
- позволяет фильтровать по классу оружия, происхождению (корпорация/банда), редкости и визуальному состоянию;
- описывает эффекты (свет, частицы, гличи), материалы и анимации для каждого элемента;
- интегрирует экспорт визуальных пресетов для UI (loadouts), маркетинга и аналитики.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:12  
**Статус:** approved (api-readiness: ready)

**Ключевые данные:**
- Классы оружия (пистолеты, винтовки, снайперские системы, энергетическое оружие, дробовики, тяжёлые платформы, метательные).  
- Ближний бой и имплант-артефакты (моноблейды, электрохлысты, интегрированные импланты).  
- Бронесистемы, шлемы, рюкзаки, имплантируемые аксессуары, транспорт/техника.  
- Kafka события `gameplay.visuals.equipment.updated`, метрики `EquipmentVisualEngagement`.  
- UX/QA подтверждения (`ART-VIS-ASSET-006`, `FW-VIS-ASSETS-002`).

### Дополнительные источники

- `.BRAIN/02-gameplay/combat/weapon-archetypes.md` — боевые классы.  
- `.BRAIN/02-gameplay/progression/progression-skills-mapping.md` — навыки и связи с экипировкой.  
- `.BRAIN/05-technical/backend/realtime-server/part1-architecture-zones.md` — эффекты для realtime.  
- `API-SWAGGER/api/v1/character/visuals/archetypes.yaml` (из текущей задачи 328) — связь с персонажами.

### Связанные документы

- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — контекст локаций.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — влияние на социальные рейтинги.  
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — генерация NPC и трафика.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Файл:** `api/v1/gameplay/visuals/equipment.yaml`  
**API версия:** v1  
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
                └── equipment.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис):
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API пути:** `/api/v1/gameplay/visuals/*`
- **Интеграции:** character-service (архетипы), economy-service (marketplace), realtime-service (боевые эффекты), analytics-service (метрики).
- **Kafka:** `gameplay.visuals.equipment.updated`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):
- **Модуль:** modules/gameplay/loadouts
- **State Store:** `useGameplayStore(loadouts)`
- **UI компоненты:** `EquipmentPreviewCard`, `EffectTimeline`, `VariantSelector`, `MaterialSwatchList`, `MetricBadge`
- **Формы:** `LoadoutFilterForm`, `EquipmentExportForm`
- **Layouts:** `LoadoutConfiguratorLayout`, `GameLayout`
- **Хуки:** `useLoadoutFilters`, `useEffectPreview`, `useRealtime`

### Комментарий в YAML:
```
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - Frontend Module: modules/gameplay/loadouts
# - State Store: useGameplayStore(loadouts)
# - UI: EquipmentPreviewCard, EffectTimeline, VariantSelector, MaterialSwatchList, MetricBadge
# - Forms: LoadoutFilterForm, EquipmentExportForm
# - Layouts: LoadoutConfiguratorLayout, GameLayout
# - Hooks: useLoadoutFilters, useEffectPreview, useRealtime
# - Events: gameplay.visuals.equipment.updated
# - API Base: /api/v1/gameplay/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Классифицировать визуальные элементы:** определить общие поля для оружия, брони, имплантов, транспорта.  
   _Результат:_ спецификация полей `VisualEquipmentProfile`.
2. **Спроектировать схемы:** `VisualEquipmentProfile`, `VisualEffect`, `MaterialProfile`, `AttachmentSet`, `EquipmentVariant`, `VisualEquipmentExportRequest`/`Response`, `PaginatedVisualEquipmentProfile`.  
3. **Сформировать endpoints:** список, деталь, фильтры по эффектам, экспорт, возможно справочник материалов.  
4. **Подключить общие компоненты:** ошибки, безопасность, пагинация, теги.  
5. **Документировать Kafka и метрики:** payload события, `EquipmentVisualEngagement`, `VisualEffectUptime`.  
6. **Добавить примеры:** Smart pistol, Assault rifle (Militech), Monoblade, Heavy exosuit, Corporate armor, Tactical backpack.  
7. **Прогнать валидацию и убедиться, что файлы ≤400 строк (компоненты вынести).**

---

## 🔌 Эндпоинты

1. **GET `/gameplay/visuals/equipment`**  
   - Параметры: `category` (weapon/melee/implant/armor/accessory), `origin`, `rarity`, `effect`, `page`, `pageSize`.  
   - Ответы: `200 OK` (`PaginatedVisualEquipmentProfile`), `400`, `401/403`, `500`.

2. **GET `/gameplay/visuals/equipment/{equipmentId}`**  
   - Детализированная карточка, включая эффекты, материалы, варианты и совместимость с архетипами.  
   - Ответы: `200 OK`, `404`, `409`, `500`.

3. **GET `/gameplay/visuals/equipment/{equipmentId}/variants`**  
   - Список визуальных вариантов (корпоративный, уличный, редкий, легендарный).  
   - Ответы: `200 OK` (`EquipmentVariantList`), `404`, `500`.

4. **POST `/gameplay/visuals/equipment/export`**  
   - Тело: `VisualEquipmentExportRequest` (equipmentIds[], channels[], includeEffects, format).  
   - Ответы: `202 Accepted`, `400`, `409`, `503`.

5. **Опционально:** `GET /gameplay/visuals/materials` (если в документе есть справочник материалов) — уточнить при реализации; можно вынести в отдельный компонент.

---

## 🧱 Модели данных

- **VisualEquipmentProfile** — id, name, category, origin, rarity, description, associatedArchetypes[], compatibleRoles[], materialProfile (ref), visualEffects[], attachments[], metrics.  
- **MaterialProfile** — baseMaterial, finish, emissiveLevel, wearLevel, decalSet.  
- **VisualEffect** — effectType (glow, particles, glitch, recoil), intensity, color, triggerCondition.  
- **AttachmentSet** — список модулей/аксессуаров, их визуальные отличия.  
- **EquipmentVariant** — variantId, variantName, description, palette, unlockCondition.  
- **VisualEquipmentExportRequest / Response** — параметры экспорта и ссылка на готовые ассеты.  
- **PaginatedVisualEquipmentProfile** — таблица пагинации.

Каждую схему снабдить `description`, `enum`, `minItems`, `maxLength`, примерами.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3, файлы ≤400 строк, компоненты вынести в `components`.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Указать `tags` (Visual Equipment, Visual Exports).  
- Ошибки с `x-error-code`: `VAL_INVALID_FILTER`, `BIZ_EQUIPMENT_NOT_FOUND`, `INT_VISUAL_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`.  
- В `info.description` перечислить `.BRAIN` источники и дату.  
- Добавить `x-sources`, `x-related-apis` (ссылки на character и economy визуалы).

---

## ✅ Критерии приемки

1. `api/v1/gameplay/visuals/equipment.yaml` создан и валиден (`scripts/validate-swagger.ps1`).  
2. В начале файла прописан блок `Target Architecture`.  
3. `GET /gameplay/visuals/equipment` поддерживает фильтры и пагинацию.  
4. Схемы `VisualEquipmentProfile`, `MaterialProfile`, `VisualEffect`, `EquipmentVariant`, `PaginatedVisualEquipmentProfile` оформлены и переиспользуемы.  
5. Экспорт ассетов реализован через `POST /gameplay/visuals/equipment/export` с кодом `202`.  
6. Kafka событие `gameplay.visuals.equipment.updated` задокументировано.  
7. Метрики (`EquipmentVisualEngagement`, `VisualEffectUptime`) отражены в спецификации.  
8. Ошибки подключены через `$ref` и имеют `x-error-code`.  
9. Примеры для ключевых классов оружия и бронесистем включены.  
10. README в `gameplay/visuals` обновлён ссылкой на API (в рамках реализации).

---

## ❓ FAQ

**Q:** Как связать визуальные профили с реальными шмотками в инвентаре?  
A: Используйте `inventoryItemId` и ссылки на `api/v1/gameplay/equipment` (когда будет реализовано), плюс `attachmentSet`.

**Q:** Нужно ли описывать геймплейные свойства (урон, броня)?  
A: Нет, только визуальные параметры; геймплейные характеристики остаются в других сервисах.

**Q:** Как учесть кросс-доменные эффекты (маркет, социальные события)?  
A: Через экспорт (каналы `economy`, `marketing`, `social`) и `relatedServices` внутри профиля.

**Q:** Требуется ли версия ассетов?  
A: Да, добавьте поле `visualVersion` и используйте семвер; описать в схеме.

---

**Следующие действия исполнителя:** создать спецификацию, вынести схемы в компоненты, обновить README, выполнить валидацию.

