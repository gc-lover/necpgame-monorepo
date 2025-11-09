# Task ID: API-TASK-370
**Тип:** API Generation  
**Приоритет:** средний  
**Статус:** queued  
**Создано:** 2025-11-08 17:26  
**Создатель:** AI Brain Manager (GPT-5 Codex)  
**Зависимости:** API-TASK-369 (subliminal network API), API-TASK-241 (world-interaction-suite API), API-TASK-361 (world-visuals-locations API)

---

## 📋 Краткое описание

Спроектировать OpenAPI `subliminal-billboards.yaml` для world-service, описывающий управление глитч-билбордами «Сдвиг пикселей»: публикация сообщений, расписания, фиксация sightings, opt-in low-impact профили.

---

## 🎯 Цель

Обеспечить world-service API для управления визуальными сигналами:
- планировать и обновлять сообщения на билбордах;
- фиксировать игроками обнаруженные сообщения и выдавать эмоцию `Pixel Snap`;
- синхронизировать визуальные профили с HUD и safety режимами;
- экспортировать данные в analytics/UI.

---

## 📚 Источники

- `.BRAIN/06-tasks/active/CURRENT-WORK/active/2025-11-07-subliminal-easter-network.md` — раздел билбордов и safety режимов.
- `.BRAIN/06-tasks/active/CURRENT-WORK/open-questions.md` — решение по low-impact (2025-11-08 17:03).
- `.BRAIN/03-lore/visual-guides/visual-style-assets-детально.md` — визуальные профили.
- `.BRAIN/02-gameplay/world/events/world-events-framework.md` — хуки для мировых событий.

---

## 📁 Целевая структура

- **Файл:** `api/v1/world/subliminal/billboards.yaml`
- **Формат:** OpenAPI 3.0.3
- **Версия:** 1.0.0

```
api/
  v1/
    world/
      subliminal/
        billboards.yaml
```

`info.x-microservice`:
```yaml
info:
  title: Subliminal Billboards API
  version: 1.0.0
  description: Управление глитч-билбордами подпольной сети
  x-microservice:
    name: world-service
    port: 8086
    domain: world
    basePath: /api/v1/world
    package: com.necp.world.subliminal.billboards
```

---

## 🏗️ Архитектура

- **Backend:** world-service, связанный с social-service (участники), analytics-service, notification-service, UI gateway.
- **Kafka:** `world.subliminal.billboard` (публикация), `world.subliminal.billboard.sighting`.
- **Frontend:** `modules/world/events`, `modules/ui/hud`, `modules/ui/gallery`.
  - UI: `@shared/ui/BillboardFeed`, `@shared/ui/SightingForm`, `@shared/ui/ImpactBadge`.
  - State: `useWorldStore` (`billboardMessages`, `activeBillboards`, `sightingHistory`).

---

## 🔧 План

1. Описать сущности `world.subliminal_billboards` и связанную аналитику из `.BRAIN`.
2. Спроектировать endpoints для управления сообщениями (CRUD), расписаний, sightings.
3. Внедрить поддержку low-impact режима (`visualProfile`, `fallbackProfile`).
4. Добавить интеграцию c `HUDIndicator` (payload для UI gateway).
5. Зафиксировать Kafka payload и мониторинг.
6. Обновить mapping и документ `.BRAIN`.

---

## 🌐 Endpoints

1. `POST /api/v1/world/subliminal/billboards`
   - Создание сообщения (текст, визуальный профиль, регион, расписание).

2. `GET /api/v1/world/subliminal/billboards`
   - Список активных/предстоящих билбордов (фильтры: `region`, `status`, `visualProfile`).

3. `PATCH /api/v1/world/subliminal/billboards/{billboardId}`
   - Обновление контента, расписания, fallback профиля.

4. `DELETE /api/v1/world/subliminal/billboards/{billboardId}`
   - Снятие сообщения (с указанием причины).

5. `POST /api/v1/world/subliminal/billboards/{billboardId}/sightings`
   - Регистрация наблюдения игроком (используется UI и награды).

6. `GET /api/v1/world/subliminal/billboards/{billboardId}/sightings`
   - История наблюдений, статистика (для GM/аналитики).

7. `GET /api/v1/world/subliminal/billboards/analytics`
   - Метрики: кол-во sightings, доля low-impact, вовлечённость регионов.

---

## 🧱 Модели

- `BillboardMessage`: `billboardId`, `title`, `message`, `visualProfile`, `fallbackProfile`, `region`, `activeFrom`, `activeUntil`, `priority`.
- `BillboardSchedule`: `cronExpression`, `triggerType`, `lastDisplayed`, `nextDisplay`.
- `SightingRequest`: `playerId`, `shard`, `screenshotUrl`, `visualIntensity`, `safetyMode`.
- `SightingRecord`: `recordId`, `playerId`, `billboardId`, `recordedAt`, `rewardGranted`.
- `BillboardAnalytics`: `totalSightings`, `uniquePlayers`, `lowImpactUsage`, `emotionUnlocks`.

---

## 📊 Правила

- Low-impact профиль обязателен; если игрок в режиме LOW, возвращать `fallbackProfile`.
- Эмоция `Pixel Snap` выдаётся через inventory-service после подтверждённого sighting.
- Ограничение: максимум 10 активных билбордов на shard.
- Совместимость с world-state: учитывать события аномалий (доп. фильтры).
- Мониторинг: `billboard_active_total`, `billboard_sighting_total`, `billboard_lowimpact_ratio`.

---

## ✅ Acceptance Criteria

1. Файл `api/v1/world/subliminal/billboards.yaml` валиден и использует shared компоненты.
2. `info.x-microservice` указан (world-service, 8086).
3. Все endpoints документированы с примерами и кодами ошибок `BILLBOARD_*`.
4. Описаны модели сообщений, расписаний, наблюдений и аналитики.
5. Kafka события `world.subliminal.billboard` и `world.subliminal.billboard.sighting` описаны.
6. Указаны правила low-impact, лимиты, награды.
7. Добавлены `x-examples` (создание сообщения, sighting).
8. `brain-mapping.yaml` содержит запись для API-TASK-370.
9. `.BRAIN/2025-11-07-subliminal-easter-network.md` обновлён.
10. Предусмотрена интеграция с inventory-service (эмоция `Pixel Snap`).

---

## ❓FAQ

- **Поддерживаются ли видео?** Да, через `visualProfile` (тип `video/mp4`) с fallback изображением.
- **Как учитывать регион?** В модели и фильтрах указать `region` (ASIA/EU/AMERICAS/ALL).
- **Нужен ли realtime?** Состояние распространяется через Kafka → UI gateway; отдельный WebSocket не требуется.

---

После подготовки спецификации обновить mapping, документ .BRAIN и сгенерировать клиентов.

