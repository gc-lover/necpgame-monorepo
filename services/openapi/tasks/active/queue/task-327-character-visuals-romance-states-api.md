# Task ID: API-TASK-327
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:59  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** [API-TASK-325], [API-TASK-323], [API-TASK-324]

---

## 📋 Краткое описание

Создать спецификацию `api/v1/character/visuals/romance-states.yaml`, описывающую визуальные состояния романтических NPC: палитры, эмоции, аксессуары, ambient эффекты и синхронизацию с социальными хабами.

**Что нужно сделать:** Сформировать OpenAPI 3.0.3 документ для character-service (совместно с social-service), покрывающий выдачу `VisualRomanceState`, управление сценарием и экспорт медиа-пакетов.

---

## 🎯 Цель задания

Обеспечить narrative и social команды единым API для визуальных состояний романтических линий, чтобы события, кат-сцены и маркетинговые кампании использовали согласованные данные.

**Зачем это нужно:**
- Поддержать фронтенд `modules/social/romance` и narrative UI.  
- Синхронизировать визуальные изменения с социальными хабами и мировыми событиями.  
- Позволить маркетингу генерировать медиа-пакеты для романов.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 11:18  
**Статус документа:** approved (api-readiness: ready)

**Что важно из документа:**
- Разделы «Импланты и моды», «Предметы…», «Образы для карточек» и метрики романтических состояний.  
*-* Ссылки на Kafka `character.visuals.romance.state.changed` и метрику `RomanceVisualResonance`.  
- Требования к JSON схеме `VisualRomanceState`.

### Дополнительные источники

- `.BRAIN/04-narrative/dialogues/quest-main-001-first-steps.md` — примеры романтических сцен.  
- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — влияние социального контента на романтические настройки.  
- `API-SWAGGER/api/v1/social/visuals/hubs-detailed.yaml` (API-TASK-323) — ambient контракты.

### Связанные документы

- `API-SWAGGER/api/v1/narrative/quests/*.yaml` — если есть, уточнить связи.  
- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — сцены и локации.  
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — реакция NPC.

---

## 📁 Целевая структура API

**Целевой файл:** `api/v1/character/visuals/romance-states.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── character/
            └── visuals/
                └── romance-states.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервисы)
- **Основной микросервис:** character-service (port 8091)  
- **Вторичный микросервис:** social-service (port 8084) — ambient sync  
- **API Base:** `/api/v1/character/visuals/romance/*`  
- **Зависимости:** auth-service, narrative-service (сюжеты), marketing-service, world-service (локации)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/social/romance`  
- **State Store:** `useSocialStore` (romanceStates, romanceScenes, exportTickets)  
- **UI компоненты (@shared/ui):** RomanceStateCard, EmotionTimeline, AmbientBadge, CharacterPortrait, MetricChip  
- **Формы (@shared/forms):** RomanceStateFilterForm, SceneOverrideForm, RomanceMediaExportForm  
- **Layouts:** RomanceStoryLayout (`@shared/layouts`)  
- **Hooks:** useRomanceFilters, useScenePreview, useRomanceExport

### Комментарий
Добавить в YAML:
```yaml
# Target Architecture:
# - Microservice: character-service (port 8091)
# - Secondary Microservice: social-service (port 8084)
# - Frontend Module: modules/social/romance
# - UI Components: @shared/ui (RomanceStateCard, EmotionTimeline, AmbientBadge, CharacterPortrait, MetricChip)
# - Forms: @shared/forms (RomanceStateFilterForm, SceneOverrideForm, RomanceMediaExportForm)
# - State: useSocialStore (romanceStates, romanceScenes, exportTickets)
# - API Base: /api/v1/character/visuals/romance/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Выделить состояния из `.BRAIN`** — эмоции, палитры, аксессуары, ambient, связанные предметы и локации.  
2. **Определить эндпоинты** — список состояний, карточка NPC, управление сценарием, предпросмотр сцен, экспорт медиа.  
3. **Спроектировать модели** — `VisualRomanceState`, `EmotionCue`, `AmbientProfile`, `SceneTrigger`, `RomanceExportRequest`, `RomanceExportBundle`.  
4. **Документировать безопасность** — BearerAuth, ErrorResponse, коды 400/404/409/412/423/503.  
5. **Kafka** — `character.visuals.romance.state.changed`, зависимость на `social.visuals.hub.activity`, `marketing.visuals.package.generated`.  
6. **Метрики** — `RomanceVisualResonance`, `SceneEngagementRate`, `AmbientHarmonyScore`.  
7. **Лимит 400 строк** — при необходимости вынести компоненты.  
8. **Валидация** — `scripts/validate-swagger.ps1`.

---

## 🔀 Endpoints

1. **GET `/api/v1/character/visuals/romance/states`**  
   - Фильтры: `npcId`, `storyArc`, `emotion`, `ambientTag`, `macroZone`, `limit`, `offset`.  
   - Ответ 200: `Page<VisualRomanceState>`.  
   - Ошибки: 400, 401/403, 503.

2. **GET `/api/v1/character/visuals/romance/states/{stateId}`**  
   - Path: `stateId` (`ROM-[A-Z0-9-]+`).  
   - Ответ 200: полный профиль (emotion timeline, palette, accessories, ambient, linked scenes, marketing tags).  
   - Ошибки: 404, 410 (архив), 423 (locked by narrative).

3. **PATCH `/api/v1/character/visuals/romance/states/{stateId}/scene`**  
   - Тело: `SceneOverrideRequest` (emotionOverride, ambientOverride, accessoryOverride, effectiveFrom, effectiveTo, narrativeChecklistId).  
   - Ответ 200: обновлённый `VisualRomanceState`.  
   - Ошибки: 400, 409, 412 (QA/narrative не подтверждено).

4. **GET `/api/v1/character/visuals/romance/states/{stateId}/preview`**  
   - Query: `emotion`, `ambient`, `intensity`.  
   - Ответ 200: `RomanceScenePreview` (video, audio, shaderParams, narrativeNotes).  
   - Ошибки: 400, 404, 423, 503.

5. **POST `/api/v1/character/visuals/romance/export`**  
   - Тело: `RomanceExportRequest` (stateIds[], includeAudio, includeNarrativePrompts, channels).  
   - Ответ 202: `RomanceExportTicket`.  
   - Ошибки: 400, 409, 503.

6. **GET `/api/v1/character/visuals/romance/export/{ticketId}`**  
   - Возвращает `RomanceExportBundle` (cdnLinks, palette, ambientAudio, narrativePrompts, marketingAssets).  
   - Ошибки: 404, 410, 423.

Ошибки — через `shared/common/responses.yaml`. Пагинация — общие компоненты.

---

## 🧱 Модели данных

- **VisualRomanceState**  
  Поля: `stateId`, `npcId`, `npcName`, `storyArc`, `emotion`, `palette`, `lighting`, `ambientProfile`, `accessories[]`, `visualLayers[]`, `emotionTimeline[]`, `linkedItems[]`, `linkedLocations[]`, `marketingTags[]`, `safetyConsiderations`, `lastUpdated`.  
  Примеры: романтический ветеран, Synth-культовый оракул, корпоративный исполнитель.

- **EmotionCue** (`emotion`, `intensity`, `visualCue`, `audioCue`, `gesture`, `animationRef`).  
- **AmbientProfile** (`ambientTag`, `lightingPreset`, `soundscape`, `hubReference`).  
- **SceneTrigger** (`triggerId`, `triggerType`, `conditions`, `branchingOptions`).  
- **SceneOverrideRequest** (описать поля с `required`).  
- **RomanceExportRequest**, **RomanceExportTicket**, **RomanceExportBundle**, **RomanceScenePreview** — с примерами и `required`.

---

## 📡 Kafka и интеграции

- **Producer:** character-service → `character.visuals.romance.state.changed` `{ npcId, stateId, emotion, ambientTag, palette, updatedAt }`.  
- **Consumers:** narrative-service, social-service, marketing-automation, telemetry, ui-service.  
- Указать подписки на `social.visuals.hub.activity` (ambient), `world.visuals.event.triggered` (ивенты) и `marketing.visuals.package.generated` (экспорт).  
- Отразить зависимость на `API-TASK-323` и `API-TASK-324`.

---

## 📊 Метрики и аналитика

- `RomanceVisualResonance` — реакция игроков на визуальные состояния (анализ ссылок).  
- `SceneEngagementRate` — вовлечённость в сцены.  
- `AmbientHarmonyScore` — синхронизация с хабами.  
- Метрики публикуются в telemetry; описать структуру передачи.

---

## ⚙️ Правила реализации

- Соблюдать SOLID/DRY/KISS, `$ref` для повторяемых структур.  
- Не хранить статические данные в спецификации — только структуры и ссылки.  
- Info.description должен ссылаться на `.BRAIN` и workshop 2025-11-08.  
- Размер ≤400 строк; при превышении вынести `components` и добавить README.  
- Учитывать локализацию (`nameLocalized`, `emotionLocalized`) в схемах.

---

## ✔️ Критерии приемки

1. Файл `api/v1/character/visuals/romance-states.yaml` создан, содержит Target Architecture блок.  
2. Все 6 эндпоинтов описаны с параметрами, примерами, ошибками.  
3. Пагинация и ошибки используют общие компоненты.  
4. Модели `VisualRomanceState`, `EmotionCue`, `AmbientProfile`, `RomanceExportRequest` описаны с `required` и примерами.  
5. Kafka событие и подписки задокументированы.  
6. Метрики `RomanceVisualResonance`, `SceneEngagementRate`, `AmbientHarmonyScore` отражены.  
7. Файл проходит `scripts/validate-swagger.ps1`.  
8. Размер ≤400 строк или есть план вынесения компонентов.  
9. Связь с `useSocialStore` и фронтенд модулем зафиксирована.  
10. PATCH endpoint учитывает narrative QA и блокировки.  
11. Экспорт поддерживает каналы marketing/narrative/ui.  
12. Info.description содержит ссылку на `.BRAIN` и связанные задания (API-TASK-325, API-TASK-323, API-TASK-324).

---

## ❓ FAQ

- **Вопрос:** Нужен ли API для создания новых романтических состояний?  
  **Ответ:** Нет, состояния создаёт narrative-контент. API предоставляет чтение, обновление и экспорт утверждённых состояний.

- **Вопрос:** Как синхронизировать с социальными хабами?  
  **Ответ:** Через поле `ambientProfile.hubReference` и подписку на `social.visuals.hub.activity`.

- **Вопрос:** Нужно ли хранить аудио внутри API?  
  **Ответ:** Нет, в ответах должны быть ссылки на CDN и идентификаторы аудио, а не бинарные данные.

- **Вопрос:** Поддерживается ли локализация?  
  **Ответ:** Да, добавить `nameLocalized`, `emotionLocalized`, `descriptionLocalized` (map locale → string) в модели.

- **Вопрос:** Как обрабатывать конфликтующие сцены?  
  **Ответ:** PATCH должен возвращать 409 и ссылку на активный schedule если state заблокирован другим событием.

---

## 📌 История выполнения

- 2025-11-08 — Задание создано AI агентом GPT-5 Codex на основе `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-assets-детально.md`.



