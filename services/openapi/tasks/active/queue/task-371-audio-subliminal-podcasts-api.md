# Task ID: API-TASK-371
**Тип:** API Generation  
**Приоритет:** средний  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-369 (subliminal network API), API-TASK-365 (social anomalies participants API), API-TASK-337 (visuals analytics metrics API)

---

## 📋 Краткое описание

Создать OpenAPI `subliminal-podcasts.yaml` для audio-service, описывающий библиотеку подкастов «Resonance Under»: управление выпусками, маркерами, выдачу координат, расчёт прогресса мини-квеста `Follow the Resonance`.

---

## 🎯 Цель

Предоставить аудио-сервису API для:
- публикации и обновления подкастов, содержащих скрытые маркеры;
- выдачи метаданных и сегментов фронтенду и gameplay-service;
- отслеживания прослушивания, подписок и прогресса мини-квеста;
- синхронизации с notification-service и analytics.

---

## 📚 Источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-subliminal-easter-network.md` (раздел «Коллекция аудио-подкастов»).
- `.BRAIN/05-technical/backend/audio/audio-service.md` (если имеется; иначе использовать общие гайды audio-service).
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — решение по маркерам `RES-MK-01..05`.
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` — координаты событий.

---

## 📁 Целевая структура

- **Файл:** `api/v1/audio/subliminal/podcasts.yaml`
- **Формат:** OpenAPI 3.0.3
- **Версия:** 1.0.0

```
api/
  v1/
    audio/
      subliminal/
        podcasts.yaml
```

`info.x-microservice`:
```yaml
info:
  title: Resonance Under Podcasts API
  version: 1.0.0
  description: Публикация и сопровождение скрытых подкастов подпольной сети
  x-microservice:
    name: audio-service
    port: 8088
    domain: audio
    basePath: /api/v1/audio
    package: com.necp.audio.subliminal.podcasts
```

---

## 🏗️ Архитектура

- **Backend:** audio-service (8088) — хранение аудио, маркеров, интеграция с content delivery и analytics.
- **Интеграции:** world-service (координаты), social-service (мини-квест прогресс), gameplay-service (quest triggers), notification-service.
- **Kafka:** `audio.subliminal.marker`, `audio.subliminal.podcast`.
- **Frontend:** `modules/audio/player`, `modules/world/events`, `modules/gameplay/quests`.
  - UI: `@shared/ui/PodcastPlayer`, `@shared/ui/MarkerTimeline`, `@shared/forms/SubscriptionToggle`.

---

## 🔧 План

1. Смоделировать сущности выпуска подкаста и маркеров (ID RES-MK-01..05) из `.BRAIN`.
2. Спроектировать эндпоинты публикации, обновления, получения списка, прогресса прослушивания, событий маркеров.
3. Добавить контроль качества (битрейт, длительность, поддерживаемые форматы).
4. Описать выдачу координат и связку с мини-квестом (questId, reward).
5. Зафиксировать Kafka payload и мониторинг.
6. Обновить mapping и документ .BRAIN.

---

## 🌐 Endpoints

1. `POST /api/v1/audio/subliminal/podcasts`
   - Создание выпуска: `title`, `episodeCode`, `description`, `duration`, `audioUrl`, `markers[]`.

2. `GET /api/v1/audio/subliminal/podcasts`
   - Список выпусков (фильтр по статусу, сезон, сортировка).

3. `GET /api/v1/audio/subliminal/podcasts/{podcastId}`
   - Детали выпуска: маркеры, координаты, доступность, связанные квесты.

4. `PATCH /api/v1/audio/subliminal/podcasts/{podcastId}`
   - Обновление метаданных (описание, маркеры, ссылки).

5. `POST /api/v1/audio/subliminal/podcasts/{podcastId}/listen`
   - Регистрация прослушивания (playerId, progress, markerReached[]).

6. `GET /api/v1/audio/subliminal/podcasts/{podcastId}/progress`
   - Прогресс игрока: завершённые маркеры, награды, status mini quest.

7. `GET /api/v1/audio/subliminal/podcasts/analytics`
   - Метрики: `streams`, `completionRate`, `markerEngagement`, `questUnlocks`.

---

## 🧱 Модели

- `PodcastEpisode`: `podcastId`, `episodeCode`, `title`, `description`, `season`, `duration`, `audioUrl`, `markers[]`, `status`.
- `Marker`: `markerId`, `timestamp`, `coordinate`, `hint`, `questTrigger`.
- `ListenRequest`: `playerId`, `progressSeconds`, `markerReached[]`, `clientTimestamp`.
- `ProgressResponse`: `playerId`, `markersCompleted`, `questStatus`, `rewards`.
- `PodcastAnalytics`: `streams`, `uniqueListeners`, `averageCompletion`, `markerConversion`.

---

## 📊 Правила

- Поддерживаемые форматы аудио: `audio/mpeg`, `audio/ogg`; максимальная длительность 15 минут.
- Маркеры должны иметь координаты и подсказки, выдающие квест `Follow the Resonance`.
- Прослушивание фиксируется только при прогрессе >30 секунд.
- Награды: после завершения всех выпусков активируется мини-квест и выдаётся бафф.
- Мониторинг: `podcast_stream_total`, `podcast_marker_trigger_total`, `podcast_completion_rate`.

---

## ✅ Acceptance Criteria

1. Файл `api/v1/audio/subliminal/podcasts.yaml` валиден, использует shared компоненты.
2. `info.x-microservice` заполнен (audio-service, 8088).
3. Описаны все endpoints с примерами и кодами ошибок `PODCAST_*`, `MARKER_*`.
4. Модели учитывают маркеры и прогресс слушателя.
5. Kafka события `audio.subliminal.podcast` и `audio.subliminal.marker` документированы.
6. Включены ограничения по формату аудио и длительности.
7. Добавлены `x-examples` (создание выпуска, регистрация прослушивания).
8. `brain-mapping.yaml` содержит запись для API-TASK-371.
9. `.BRAIN/2025-11-07-subliminal-easter-network.md` обновлен блоком `API Tasks Status`.
10. Указана интеграция с gameplay-service (quest trigger) и social-service (маркерные уведомления).

---

## ❓FAQ

- **Где хранится аудио?** Во внешнем CDN; API должен возвращать подписанные URL.
- **Нужно ли стримить?** Нет, спецификация предоставляет ссылки и маркеры; стрим реализуется клиентом.
- **Как учитывать оркестр событий?** В payload маркеров предоставить `questTrigger` и координаты для world-service.

---

После завершения обновить mapping, документ .BRAIN и запустить генерацию клиентов.

