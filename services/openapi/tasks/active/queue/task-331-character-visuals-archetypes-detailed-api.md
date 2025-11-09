# Task ID: API-TASK-331
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:45  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-328]

---

## 📋 Краткое описание

Сформировать спецификацию `Character Visual Archetypes Detailed API`, описывающую расширенные визуальные профили архетипов, их состояния, эффекты, анимационные слоёв и экспорт ассет-пакетов.  
**Целевой файл:** `api/v1/character/visuals/archetypes-detailed.yaml`

---

## 🎯 Цель задания

Предоставить character-service детализированный контракт, который:
- раскрывает многоуровневые состояния архетипов (стандарт, боевой, романтический, теневой и т.д.);
- описывает сложные эффекты (эмиссия, частицы, глич, аудио сопровождение), фракционные вариации, динамические аксессуары;
- синхронизирует экспорт ассетов для UI, маркетинга, narration и motion-design команд;
- документирует Kafka события, метрики и связи с другими сервисами.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:18  
**Статус:** approved (api-readiness: ready)

**Содержит:**
- Детализация визуальных состояний персонажей: архетипы, романтические ветви, боевые модификации, культовые образы.  
- JSON схемы: `VisualArchetypeDetailedProfile`, `VisualStateExtended`, `AccessoryDynamicSet`, `AnimationStack`.  
- Kafka события: `character.visuals.romance.state.updated`, `character.visuals.effect.triggered`.  
- UX/QA подтверждения, workshop 2025-11-08 10:55, синхронизация команд world/art/social/marketing.  
- Метрики: `VisualFidelityScore`, `RomanceVisualAffinity`, `EffectReliability`.

### Дополнительные источники

- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — связь архетипов с локациями.  
- `.BRAIN/04-narrative/dialogues/quest-main-001-first-steps.md` — романтические состояния NPC.  
- `.BRAIN/02-gameplay/combat/combat-session-effects.md` — боевые эффекты.  
- `API-SWAGGER/api/v1/character/visuals/archetypes.yaml` — базовая спецификация (задача 328).

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/character/visuals/archetypes-detailed.yaml`  
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
                └── archetypes-detailed.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** character-service  
- **Порт:** 8091  
- **Base Path:** `/api/v1/character/visuals/*`  
- **Интеграции:** gameplay-service (боевые моды), narrative-service (сюжетные состояния), social-service (романтика), analytics-service, marketing-service.
- **Kafka:** `character.visuals.romance.state.updated`, `character.visuals.effect.triggered`, `character.visuals.asset.exported`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/characters/visual-lab  
- **State Store:** `useCharacterStore(detailedVisuals)`  
- **UI:** `ArchetypeDetailedViewer`, `StateTimeline`, `EffectIntensityRadar`, `AccessoryPreview`, `ExportJobStatus`  
- **Формы:** `VisualStateFilterForm`, `ExportConfigForm`  
- **Layouts:** `VisualLabLayout`, `GameLayout`  
- **Хуки:** `useVisualStatePreview`, `useEffectControls`, `useExportJobs`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: character-service (port 8091)
# - Frontend Module: modules/characters/visual-lab
# - State Store: useCharacterStore(detailedVisuals)
# - UI: ArchetypeDetailedViewer, StateTimeline, EffectIntensityRadar, AccessoryPreview, ExportJobStatus
# - Forms: VisualStateFilterForm, ExportConfigForm
# - Layouts: VisualLabLayout, GameLayout
# - Hooks: useVisualStatePreview, useEffectControls, useExportJobs
# - Events: character.visuals.romance.state.updated, character.visuals.effect.triggered, character.visuals.asset.exported
# - API Base: /api/v1/character/visuals/*
```

---

## ✅ Что нужно сделать (план)

1. **Анализ документа:** выделить все состояния архетипов, эффекты, динамические аксессуары, связь с локациями и событиями.  
2. **Схемы:** описать `VisualArchetypeDetailedProfile`, `VisualStateExtended`, `EffectLayer`, `AccessoryDynamicSet`, `AnimationStack`, `ExportJob`.  
3. **Endpoints:** список детализированных профилей, загрузка по ID, фильтры по состояния/фракции/эффекту, получение истории эффектов, управление экспортом.  
4. **Интеграция Kafka:** задокументировать payload событий, указать потребителей.  
5. **Ошибки и безопасность:** использовать `shared/common/security.yaml` и `shared/common/responses.yaml`.  
6. **Примеры:** Night City Quantum Plaza hero, Neo Tokyo romance variant, Maelstrom battle variant, Trauma Team cinematic state.  
7. **Валидация:** `scripts/validate-swagger.ps1`, файл ≤400 строк (компоненты вынести).

---

## 🔌 Эндпоинты

1. **GET `/character/visuals/archetypes/detailed`** — страница профилей.  
   - Параметры: `archetypeId`, `faction`, `state`, `effect`, `romanceTier`, `page`, `pageSize`.  
   - Ответы: `200 OK` (`PaginatedVisualArchetypeDetailedProfile`), `400`, `401/403`, `500`.

2. **GET `/character/visuals/archetypes/{archetypeId}/detailed`** — полное детализированное описание.  
   - Ответы: `200 OK` (`VisualArchetypeDetailedProfile`), `404`, `409`, `500`.

3. **GET `/character/visuals/archetypes/{archetypeId}/effects`** — активные эффектные слои, триггеры, зависимости.  
   - Ответы: `200 OK` (`EffectLayerList`), `404`, `500`.

4. **POST `/character/visuals/archetypes/export`** — запуск экспорта ассет-пакетов (детализированный).  
   - Тело: `VisualArchetypeDetailedExportRequest`.  
   - Ответы: `202 Accepted` (`ExportJobStatus`), `400`, `409`, `503`.

5. **GET `/character/visuals/archetypes/export/{jobId}`** — статус и результаты экспорта.  
   - Ответы: `200 OK`, `404`, `500`.

---

## 🧱 Модели

- **VisualArchetypeDetailedProfile** — базовые поля + `visualStates[]`, `animationStack`, `effectLayers[]`, `dynamicAccessories`, `audioCues`, `metrics`.  
- **VisualStateExtended** — `stateId`, `stateType`, `description`, `lightingProfile`, `emissiveSettings`, `npcEmotion`.  
- **EffectLayer** — `layerType`, `intensity`, `trigger`, `colorProfile`, `duration`, `cooldown`.  
- **AccessoryDynamicSet** — `accessoryId`, `attachmentPoint`, `visualModes[]`, `physicsProfile`.  
- **AnimationStack** — `animations[]`, `blendTrees[]`, `transitionRules`.  
- **VisualArchetypeDetailedExportRequest/Response** — параметры экспорта (каналы, форматы, включение анимаций/звука).  
- **ExportJobStatus** — `jobId`, `status`, `progress`, `downloadLinks[]`, `warnings[]`.  
- **PaginatedVisualArchetypeDetailedProfile** — пагинация через shared компоненты.

Добавить `x-sources`, `x-related-apis`, `x-events` в `components`.

---

## 📏 Принципы

- OpenAPI 3.0.3, ≤400 строк; схемы/примеры вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_FILTER`, `BIZ_ARCHETYPE_NOT_FOUND`, `BIZ_EXPORT_CONFLICT`, `INT_VISUAL_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`.  
- В `info.description` перечислить `.BRAIN` источник, дату, UX/QA подтверждение.  
- Соблюдать SOLID/DRY/KISS, не дублировать модели.  
- Добавить ссылки на базовую спецификацию (API-TASK-328) и другие визуальные API.

---

## ✅ Критерии приемки

1. `api/v1/character/visuals/archetypes-detailed.yaml` создан и валиден (`scripts/validate-swagger.ps1`).  
2. Комментарий `Target Architecture` присутствует.  
3. Описаны схемы `VisualArchetypeDetailedProfile`, `VisualStateExtended`, `EffectLayer`, `AccessoryDynamicSet`, `AnimationStack`, `ExportJobStatus`.  
4. `GET /character/visuals/archetypes/detailed` поддерживает фильтры и пагинацию.  
5. Экспорт реализован через `POST /character/visuals/archetypes/export` / `GET /export/{jobId}`.  
6. Kafka события и метрики документированы.  
7. Ошибки используют `$ref` и `x-error-code`.  
8. Примеры включают как минимум четыре детализированных архетипа.  
9. README обновлён (в рамках реализации).  
10. Зависимость от базовой спецификации (API-TASK-328) отражена.

---

## ❓ FAQ

**Q:** Чем отличается от базового API?  
A: Здесь детализированные состояния, эффекты, анимации и экспорт; базовый API (328) — упрощённые профили.

**Q:** Нужно ли описывать анимации?  
A: Да, через `AnimationStack` и ссылки на motion-ассеты; фактические файлы — в asset pipeline.

**Q:** Как обрабатывать конфликт экспортов?  
A: Возвращать `409` с кодом `BIZ_EXPORT_CONFLICT`; описать retry/backoff.

**Q:** Требуется ли realtime?  
A: Документировать события; realtime-сервис использует Kafka payloadы, дополнительных WebSocket описаний не нужно.

---

**Следующие действия исполнителя:** создать спецификацию, вынести схемы/примеры, обновить README, проверить линтером.

