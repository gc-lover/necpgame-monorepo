# Task ID: API-TASK-335
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 18:12  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-334]

---

## 📋 Краткое описание

Сформировать спецификацию `Character Visual Romance Scenes API`, описывающую визуальные сцены романтических сюжетов: локации, постановку камер, световые профили, анимации, аудио и интеграцию с таймлайном.  
**Целевой файл:** `api/v1/character/visuals/romance-scenes.yaml`

---

## 🎯 Цель задания

Дать narrative-service и character-service контракт, позволяющий:
- хранить и выдавать визуальные сцены для романтических веток (cutscenes, интерактивные сцены, ambient сцены);  
- описывать композицию (камеры, свет, эффекты, фон), эмоциональный градус и связку с состояниями (`API-TASK-334`);  
- управлять расписаниями сцен, зависимостями, альтернативами и триггерами;  
- экспортировать мультимедиа пакеты (видео, анимации, аудио, скрипты) для UI, cutscene и маркетинг команд;  
- публиковать события и метрики вовлечённости.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 11:18  
**Статус:** approved (api-readiness: ready)

**Ключевые разделы:**
- Подробные описания романтических сцен (локации, композиция, эффекты, аудио, взаимодействия).  
- JSON схемы: `RomanceSceneProfile`, `CameraSetup`, `LightingRig`, `AmbientEffect`, `SceneScript`, `SceneExportRequest`.  
- Метрики: `SceneReplayRate`, `EmotionalEngagementIndex`.  
- Kafka события: `narrative.romance.scene.triggered`, `character.visuals.scene.exported`.  
- UX/QA подтверждения: workshop world/art/social/marketing 2025-11-08 10:55.

### Дополнительные источники

- `.BRAIN/04-narrative/dialogues/` (романтические ветки, сценари сценариев).  
- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — контекст локаций.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — влияние сцен на репутацию.  
- `API-SWAGGER/api/v1/character/visuals/romance-states.yaml` — состояния (зависимость).

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/character/visuals/romance-scenes.yaml`  
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
                └── romance-scenes.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервисы:** narrative-service (8087), character-service (8091)  
- **API Base:** `/api/v1/character/visuals/*` (с подпространством `/romance-scenes`)  
- **Интеграции:** world-service (локации), social-service (отношения), economy-service (подарки/кампании), analytics-service.  
- **Kafka:** `narrative.romance.scene.triggered`, `character.visuals.scene.exported`, `marketing.visuals.package.generated`

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/narrative/romance-scenes  
- **State Store:** `useNarrativeStore(romanceScenes)`  
- **UI:** `SceneStoryboard`, `CameraTrackPreview`, `LightingControlPanel`, `EmotionOverlay`, `ExportQueueStatus`  
- **Формы:** `SceneFilterForm`, `SceneExportForm`, `ScheduleForm`  
- **Layouts:** `RomanceSceneStudioLayout`, `NarrativeDirectorLayout`  
- **Хуки:** `useSceneTimeline`, `useCameraPreview`, `useExportJobs`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservices: narrative-service (port 8087), character-service (port 8091)
# - Frontend Module: modules/narrative/romance-scenes
# - State Store: useNarrativeStore(romanceScenes)
# - UI: SceneStoryboard, CameraTrackPreview, LightingControlPanel, EmotionOverlay, ExportQueueStatus
# - Forms: SceneFilterForm, SceneExportForm, ScheduleForm
# - Layouts: RomanceSceneStudioLayout, NarrativeDirectorLayout
# - Hooks: useSceneTimeline, useCameraPreview, useExportJobs
# - Events: narrative.romance.scene.triggered, character.visuals.scene.exported, marketing.visuals.package.generated
# - API Base: /api/v1/character/visuals/*
```

---

## ✅ План

1. **Анализ сцены:** выделить поля (идентификатор, состояние, локация, участников, эмоции, камеры, свет, эффекты, аудио).  
2. **Схемы:** `RomanceSceneProfile`, `SceneParticipant`, `CameraSetup`, `LightingRig`, `SceneEffect`, `SceneScript`, `SceneExportRequest/Response`, `SceneSchedule`.  
3. **Endpoints:** список сцен с фильтрами, получение сцены, расписание, управление сценарием, экспорт.  
4. **Kafka/метрики:** документировать payload событий и метрики.  
5. **Ошибки/безопасность:** shared security/responses/pagination.  
6. **Примеры:** для сцен Charismatic Idol (VR stage), Rebel Poet (rooftop), Corporate Diplomat (boardroom), Nomad Veteran (desert sunset).  
7. **Валидация:** ≤400 строк, вынести компоненты, `scripts/validate-swagger.ps1`.

---

## 🔌 Эндпоинты

1. **GET `/character/visuals/romance-scenes`**  
   - Параметры: `sceneType`, `stateId`, `characterId`, `locationId`, `emotion`, `scheduleStatus`, `page`, `pageSize`.  
   - Ответ: `200 OK` (`PaginatedRomanceSceneProfile`), `400`, `401/403`, `500`.

2. **GET `/character/visuals/romance-scenes/{sceneId}`**  
   - Детальная сцена с камерами, светом, эффектами, сценариями, экспозициями.  
   - Ответы: `200 OK`, `404`, `409`, `500`.

3. **GET `/character/visuals/romance-scenes/{sceneId}/schedule`**  
   - Расписание показов, альтернатив, блокировок.  
   - Ответы: `200 OK` (`SceneSchedule`), `404`, `500`.

4. **POST `/character/visuals/romance-scenes/export`**  
   - Тело: `SceneExportRequest` (sceneIds[], channels[], includeVideo, includeAudio, includeScript, format).  
   - Ответы: `202 Accepted` (`SceneExportStatus`), `400`, `409`, `503`.

5. **GET `/character/visuals/romance-scenes/export/{jobId}`**  
   - Статус/результат экспорта.  
   - Ответы: `200 OK`, `404`, `500`.

6. **PATCH `/character/visuals/romance-scenes/{sceneId}/script`** (опционально) — если требуется обновление сценариев; уточнить при реализации (можно вынести в отдельный этап).

---

## 🧱 Модели

- **RomanceSceneProfile** — `sceneId`, `title`, `stateId`, `characters[]`, `location`, `emotionIntensity`, `cameraSetups[]`, `lightingRigs[]`, `effects[]`, `audioSet`, `script`, `assets`, `metrics`.  
- **SceneParticipant** — `characterId`, `role`, `visualProfile`, `emotionArc`.  
- **CameraSetup** — `cameraId`, `type`, `position`, `movement`, `transition`, `focus`.  
- **LightingRig** — `rigId`, `setupType`, `colorPalette`, `intensityCurve`, `reactiveElements`.  
- **SceneEffect** — `effectId`, `type`, `trigger`, `duration`, `linkedState`.  
- **SceneScript** — `dialogueRefs[]`, `stageDirections[]`, `timing`.  
- **SceneSchedule** — `scheduleId`, `startAt`, `endAt`, `cooldown`, `regions[]`, `priority`.  
- **SceneExportRequest/Response**, **SceneExportStatus** — экспорт ассетов.  
- **PaginatedRomanceSceneProfile** — пагинация.

Добавить `x-sources`, `x-related-apis`, `x-events`, `x-metrics`.

---

## 📏 Принципы

- OpenAPI 3.0.3, ≤400 строк; компоненты вынести.  
- Общие `security`, `responses`, `pagination`.  
- Ошибки с `x-error-code`: `VAL_INVALID_FILTER`, `BIZ_SCENE_NOT_FOUND`, `BIZ_SCHEDULE_CONFLICT`, `INT_SCENE_PIPELINE_FAILURE`, `INT_EXPORT_QUEUE_BUSY`.  
- `info.description` — ссылки на `.BRAIN`, дату, подтверждения UX/QA.  
- Указать зависимость от `API-TASK-334`.

---

## ✅ Критерии приемки

1. `api/v1/character/visuals/romance-scenes.yaml` создан и валиден (`scripts/validate-swagger.ps1`).  
2. Комментарий `Target Architecture` присутствует.  
3. Схемы `RomanceSceneProfile`, `SceneParticipant`, `CameraSetup`, `LightingRig`, `SceneSchedule`, `SceneExportStatus` описаны.  
4. `GET /character/visuals/romance-scenes` поддерживает фильтры и пагинацию.  
5. Экспорт реализован (POST + GET jobId).  
6. Kafka события и метрики задокументированы.  
7. Ошибки используют shared responses и `x-error-code`.  
8. Примеры включают минимум четыре сцены.  
9. README обновлён (после реализации).  
10. Связи с `romance-states`, `locations-detailed`, `marketing visuals` указаны.

---

## ❓ FAQ

**Q:** Как сцены привязывать к состояниям?  
A: Через `stateId` и `sceneDescriptors`; указать это в `x-related-apis`.

**Q:** Нужны ли сценарии/диалоги?  
A: Храните ссылки на narrative контент через `SceneScript`; сами тексты в narrative репозитории.

**Q:** Как учитывать локализации?  
A: Используйте `SceneSchedule.regions` и `SceneScript` (поддержка локализованных направлений, аудио). Дополнительные поля можно вынести в отдельную схему.

**Q:** Что с live-сценами?  
A: Отразить в `sceneType` (live, pre-rendered, interactive); описать ограничения и события.

---

**Следующие действия исполнителя:** создать спецификацию, вынести компоненты, обновить README, прогнать валидацию и линтеры.

