# Task ID: API-TASK-334
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 18:01  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-331]

---

## 📋 Краткое описание

Создать спецификацию `Character Visual Romance States API`, описывающую визуальные состояния романтических линий NPC и игроков: эмоции, эффекты, анимации, взаимодействия с окружением и экспорт ассет-пакетов.  
**Целевой файл:** `api/v1/character/visuals/romance-states.yaml`

---

## 🎯 Цель задания

Дать narrative-service (через character-service) contract-first API, который:
- хранит все визуальные состояния романтических персонажей (initial, growing, intimate, conflict, resolution, epilogue);  
- описывает эффекты, анимации, аудио/световые профили и взаимодействия с локациями;  
- связывает визуальные состояния с сюжетными ветками, достижениями, социальными механиками;  
- поддерживает экспорт мультимедийных пакетов для UI, cutscene команд и маркетинга;  
- документирует Kafka события и метрики эмоций/вовлечённости.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:18  
**Статус:** approved (api-readiness: ready)

**Важное из документа:**
- Раздел «Романтические персонажи» и блоки о визуальных состояниях, эмоциях, световых профилях, аксессуарах.  
- JSON схемы: `RomanceVisualState`, `RomanceEffectLayer`, `RomanceTimeline`, `RomanceExportRequest`.  
- Kafka события: `character.visuals.romance.state.updated`, `character.visuals.romance.highlight`.  
- Метрики: `RomanceVisualAffinity`, `EmotionalEngagementIndex`, `SceneReplayRate`.  
- UX/QA подтверждения: списки состояний протестированы, синхронизация с narrative и marketing командами.

### Дополнительные источники

- `.BRAIN/04-narrative/dialogues/quest-main-001-first-steps.md` и другие романтические сценарии.  
- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — локации для сцен.  
- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — влияние на социальные показатели.  
- `API-SWAGGER/api/v1/character/visuals/archetypes-detailed.yaml` — базовый контекст архетипов.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/character/visuals/romance-states.yaml`  
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
                └── romance-states.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** character-service (порт 8091) совместно с narrative-service (порт 8087)  
- **API Base:** `/api/v1/character/visuals/*`  
- **Интеграции:** narrative-service (сюжеты), social-service (отношения), economy-service (подарки), analytics-service, marketing-service.  
- **Kafka:** `character.visuals.romance.state.updated`, `character.visuals.romance.highlight`, `narrative.romance.scene.triggered`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/romance  
- **State Store:** `useSocialStore(romanceVisuals)`  
- **UI:** `RomanceStateViewer`, `EmotionMeter`, `ScenePreviewCarousel`, `GiftRecommendationPanel`, `TimelineProgress`  
- **Формы:** `RomanceStateFilterForm`, `SceneExportForm`  
- **Layouts:** `RomanceLabLayout`, `GameLayout`  
- **Хуки:** `useRomanceTimeline`, `useEmotionAnalysis`, `useExportJobs`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: character-service (port 8091) with narrative-service (port 8087)
# - Frontend Module: modules/social/romance
# - State Store: useSocialStore(romanceVisuals)
# - UI: RomanceStateViewer, EmotionMeter, ScenePreviewCarousel, GiftRecommendationPanel, TimelineProgress
# - Forms: RomanceStateFilterForm, SceneExportForm
# - Layouts: RomanceLabLayout, GameLayout
# - Hooks: useRomanceTimeline, useEmotionAnalysis, useExportJobs
# - Events: character.visuals.romance.state.updated, character.visuals.romance.highlight, narrative.romance.scene.triggered
# - API Base: /api/v1/character/visuals/*
```

---

## ✅ План

1. **Проанализировать романтические состояния:** извлечь все визуальные уровни, эффекты, аксессуары, локации, сцены.  
2. **Спроектировать схемы:** `RomanceVisualStateProfile`, `EmotionCue`, `SceneDescriptor`, `RomanceGiftSet`, `RomanceExportRequest/Response`, `RomanceTimeline`.  
3. **Эндпоинты:** список и фильтры по персонажу/эмоции/сцене, детализированная карточка, расписание сцен, экспорт.  
4. **Kafka и метрики:** документировать payload событий, описать метрики вовлечённости.  
5. **Ошибки и безопасность:** подключить shared security/responses/pagination.  
6. **Примеры:** Charismatic Idol (initial→intimate), Rebel Poet (conflict), Corporate Diplomat (resolution), Nomad Veteran (epilogue).  
7. **Валидация:** файл ≤400 строк, вынести компоненты, `scripts/validate-swagger.ps1`.

---

## 🔌 Эндпоинты

1. **GET `/character/visuals/romance-states`**  
   - Параметры: `characterId`, `emotion`, `stateType`, `sceneType`, `locationId`, `page`, `pageSize`.  
   - Ответы: `200 OK` (`PaginatedRomanceStateProfile`), `400`, `401/403`, `500`.

2. **GET `/character/visuals/romance-states/{stateId}`**  
   - Полная карточка состояния (эмоции, эффекты, сцены, подарки, метрики).  
   - Ответы: `200 OK`, `404`, `409`, `500`.

3. **GET `/character/visuals/romance-states/{stateId}/timeline`**  
   - Возвращает `RomanceTimeline` (переходы, условия, связанные сцены).  
   - Ответы: `200 OK`, `404`, `500`.

4. **POST `/character/visuals/romance-states/export`**  
   - Тело: `RomanceExportRequest` (stateIds[], channels[], includeScenes, includeAudio, format).  
   - Ответы: `202 Accepted` (`RomanceExportStatus`), `400`, `409`, `503`.

5. **GET `/character/visuals/romance-states/export/{jobId}`**  
   - Статус экспорта.  
   - Ответы: `200 OK`, `404`, `500`.

---

## 🧱 Модели

- **RomanceVisualStateProfile** — `stateId`, `characterId`, `stateType`, `emotion`, `description`, `locationId`, `effects[]`, `audioCues[]`, `sceneDescriptors[]`, `gifts[]`, `metrics`.  
- **EmotionCue** — `emotionType`, `intensity`, `visualCue`, `audioCue`, `transition`.  
- **SceneDescriptor** — `sceneId`, `sceneType`, `locationRef`, `lightingProfile`, `cameraSetup`, `duration`, `unlockConditions`.  
- **RomanceGiftSet** — `giftId`, `category`, `visualProfile`, `impactOnState`.  
- **RomanceTimeline** — `nodes[]`, `edges[]`, `branchingConditions`.  
- **RomanceExportRequest/Response**, **RomanceExportStatus** — экспорт ассетов.  
- **PaginatedRomanceStateProfile** — пагинация через shared компонент.

Добавить `x-sources`, `x-related-apis`, `x-events`, `x-metrics`.

---

## 📏 Принципы

- OpenAPI 3.0.3, ≤400 строк; компоненты и примеры вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_INVALID_FILTER`, `BIZ_ROMANCE_STATE_NOT_FOUND`, `BIZ_TIMELINE_CONFLICT`, `INT_VISUAL_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`.  
- В `info.description` указать `.BRAIN` источники, дату, UX/QA.  
- Ссылки на `archetypes-detailed`, `equipment-detailed`, `items-detailed`, `locations-detailed`, `narrative` API.

---

## ✅ Критерии приемки

1. Файл `api/v1/character/visuals/romance-states.yaml` создан и валиден (`scripts/validate-swagger.ps1`).  
2. В начале файла присутствует `Target Architecture`.  
3. `GET /character/visuals/romance-states` поддерживает фильтры и пагинацию.  
4. Описаны схемы `RomanceVisualStateProfile`, `EmotionCue`, `SceneDescriptor`, `RomanceTimeline`, `RomanceExportStatus`.  
5. Экспорт реализован (POST + GET jobId).  
6. Kafka события и метрики задокументированы.  
7. Ошибки используют shared responses и `x-error-code`.  
8. Примеры включают минимум четыре состояния.  
9. README обновлён (после реализации).  
10. Зависимость от `API-TASK-331` отражена.

---

## ❓ FAQ

**Q:** Как связать визуальные состояния с сюжетами?  
A: Используйте `sceneDescriptors` и `narrativeBranchId`, документируйте в `x-related-apis`.

**Q:** Нужно ли хранить сценарии подарков?  
A: Да, через `RomanceGiftSet`; сами предметы находятся в economy-service.

**Q:** Как обрабатывать конфликты таймлайна?  
A: Возвращать `409` с кодом `BIZ_TIMELINE_CONFLICT`, описать стратегию разрешения.

**Q:** Требуется ли поддержка аудио/видео экспорта?  
A: Да, через поля `includeScenes`, `includeAudio` и соответствующие каналы (marketing, cutscene).

---

**Следующие действия исполнителя:** реализовать спецификацию, вынести компоненты, обновить README, прогнать валидацию и линтеры.

