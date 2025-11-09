# Task ID: API-TASK-366
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-241 (world-interaction-suite API), API-TASK-246 (live-events API), API-TASK-299 (combat-loadouts API), API-TASK-320 (player-orders-economy-index API)

---

## 📋 Краткое описание

Подготовить OpenAPI спецификацию `crossculture-atlas.yaml` для world-service, описывающую сезон Metropolis Threads: расписание недель, активные хабы (граффити, павильон, рынок, экскурсии, фестивали, музей), региональные окна и интеграцию с UI.

---

## 🎯 Цель

Обеспечить world-service API для управления сезонным контентом:
- опубликовать календарь и активные хабы по регионам;
- вызывать запуск/остановку хабов, валидацию расписаний, синхронизацию с AR/Audio и уведомлениями;
- передавать фронтенду агрегированную информацию для `modules/world/events` и `modules/social/seasons`.

---

## 📚 Источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-crossculture-easter-atlas.md` (v1.0.0, 2025-11-08 16:51, ready).
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — решения по low-impact анимациям и аудио (2025-11-08 17:03).
- `.BRAIN/03-lore/activities/activities-lore-compendium.md` — лор активностей.
- `.BRAIN/02-gameplay/world/events/world-events-framework.md`.
- `.BRAIN/05-technical/backend/announcement/announcement-system.md`.

---

## 📁 Целевая структура

- **Файл:** `api/v1/world/events/crossculture-atlas.yaml`
- **Формат:** OpenAPI 3.0.3
- **Версия:** 1.0.0

```
api/
  v1/
    world/
      events/
        crossculture-atlas.yaml
```

`info.x-microservice`:
```yaml
info:
  title: World Crossculture Season API
  version: 1.0.0
  description: Управление сезоном Metropolis Threads (расписания и хабы)
  x-microservice:
    name: world-service
    port: 8086
    domain: world
    basePath: /api/v1/world
    package: com.necp.world.seasons.crossculture
```

---

## 🏗️ Архитектура

- **Backend:** world-service (8086) + интеграции c social-service, economy-service, audio-service, notification-service.
- **Frontend:** `modules/world/events`, `modules/social/seasons`, `modules/ui/hud`.
  - State: `useWorldStore` (`seasonSchedule`, `activeHubs`, `regionalWindows`).
  - UI: `@shared/ui/SeasonCalendar`, `@shared/ui/HubStatusCard`, `@shared/ui/AlertBanner`.
- **Kafka:** `world.season.crossculture.lifecycle`, `world.season.crossculture.hub-state`.
- **Webhooks:** уведомления в voice lobby, push-шаблоны от notification-system.

---

## 🔧 План

1. Моделировать сущность сезона (seasonId, weeks, hubs) и хаба (id, тип, регион, расписание, активность).
2. Спроектировать endpoints для получения расписания, статуса хабов, ручного включения/отключения, обновления атрибутов.
3. Добавить endpoints для аналитики посещаемости и оповещений.
4. Описать бизнес-правила: лимиты активных хабов, региональные окна, fallback расписаний.
5. Задокументировать Kafka события и payload для UI подписок.
6. Проверить использование shared components, подготовить примеры.
7. Обновить `brain-mapping.yaml` и документ .BRAIN.

---

## 🌐 Endpoints (draft)

1. `GET /api/v1/world/seasons/crossculture/schedule`
   - Возвращает список недель (1–14), активные элементы, временные окна по регионам.
   - Параметры: `region` (ASIA/EU/AMERICAS), `includeHistory`.

2. `GET /api/v1/world/seasons/crossculture/hubs`
   - Состояния всех хабов (граффити, павильон, рынок, экскурсии, фестиваль, музей).
   - Фильтры: `hubType`, `status`, `region`.

3. `POST /api/v1/world/seasons/crossculture/hubs/{hubId}/activate`
   - Форсированная активация/деактивация GM.
   - Тело: `action` (ACTIVATE/DEACTIVATE), `region`, `overrideReason`.

4. `PATCH /api/v1/world/seasons/crossculture/hubs/{hubId}`
   - Обновление атрибутов (описание, AR profile, low-impact настройки).

5. `GET /api/v1/world/seasons/crossculture/analytics`
   - Метрики посещаемости: `visits`, `capturedPhotos`, `capsuleSales`, `museumEntries`.

6. `POST /api/v1/world/seasons/crossculture/notifications`
   - Планирование уведомлений в announcement/voice каналы.

7. `GET /api/v1/world/seasons/crossculture/roadmap`
   - Возвращает timeline следующих действий (для UI roadmap).

---

## 🧱 Модели

- `SeasonSchedule`: `seasonId`, `week`, `startDate`, `endDate`, `activeHubs[]`, `regionalWindows`.
- `HubStatus`: `hubId`, `hubType`, `region`, `status`, `activeFrom`, `activeTo`, `settings`, `fallbackProfile`.
- `HubActivationRequest`: `action`, `region`, `overrideReason`, `expiresAt`.
- `SeasonAnalytics`: `totalVisits`, `photosShared`, `capsuleRevenue`, `museumExhibits`, `sentiment`.
- `NotificationPlan`: `channels[]`, `templateId`, `scheduledAt`, `targetSegments`.

---

## 📊 Бизнес-правила

- В неделю активны только хабы, указанные в документе (.BRAIN) — валидировать при PATCH/POST.
- Региональные окна: Asia/EU/Americas; необходимо проверять пересечения.
- Low-impact режим обязателен для игроков с `visual-effects=LOW`.
- Оповещения: шаблоны (`SEASON_START`, `HUB_SWITCH`, `MUSEUM_FEATURE`).
- Метрики должны быть доступны за последние 7/30/90 дней.

---

## ✅ Acceptance Criteria

1. Спецификация `api/v1/world/events/crossculture-atlas.yaml` валидна по OpenAPI.
2. Включены все описанные endpoints c примерами и кодами ошибок `SEASON_*`.
3. `info.x-microservice` заполнен (world-service, порт 8086).
4. Описаны модели расписаний, хабов, аналитики согласно .BRAIN.
5. Kafka события `world.season.crossculture.lifecycle` и `world.season.crossculture.hub-state` перечислены в разделе `x-events`.
6. Используются shared security/response/pagination компоненты.
7. Задокументированы бизнес-правила по региональным окнам, лимитам хабов, low-impact.
8. Добавлены `x-examples` (например, неделя 5–8, запуск павильона).
9. `brain-mapping.yaml` содержит запись для API-TASK-366 (статус `queued`).
10. `.BRAIN/2025-11-07-crossculture-easter-atlas.md` обновлён блоком `API Tasks Status` с ID 366–368.

---

## ❓FAQ

- **Перекрываются ли эти endpoints с live events?** Нет, live events — отдельная система; здесь сезонные хабы и расписания.
- **Нужны ли endpoints для контента капсул или музея?** Нет, они покрываются отдельными спецификациями (API-TASK-367/368).
- **Как учитывать кросс-сервисные связи?** Указать в зависимостях и payload событиях идентификаторы хабов, которые используют economy/social сервисы.

---

Подготовка спецификации должна сопровождаться обновлением маппинга, документа .BRAIN и последующей генерацией клиентов.

