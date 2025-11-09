# Task ID: API-TASK-137
**Тип:** API Generation  
**Приоритет:** средний  
**Статус:** queued  
**Создано:** 2025-11-07 10:34  
**Создатель:** AI Agent  
**Зависимости:** none

---

## 📋 Краткое описание
Специфицировать систему рейтингов: глобальные/сезонные таблицы, фильтры по друзьям и гильдиям, выдача позиций.

**Что нужно сделать:** подготовить OpenAPI world-service по документу `.BRAIN/05-technical/backend/leaderboard-system.md`.

---

## 🎯 Цель задания
Обеспечить централизованный API для чтения и обновления лидеров, поддерживающий real-time обновления и разные срезы данных.

**Зачем это нужно:**
- Повысить вовлечённость игроков через рейтинги и соревнования.  
- Синхронизировать UI, уведомления и награды по позициям.  
- Служить источником для аналитики и сезонных сбросов.

---

## 📚 Источники информации

### Основной источник
**Путь:** `.BRAIN/05-technical/backend/leaderboard-system.md`  
**Версия:** v1.0.0 · **Статус:** ready · **Дата:** 2025-11-07  

**Ключевые моменты:**
- Типы рейтингов (global, seasonal, friend, guild, category-based).  
- Поддержка Redis sorted sets, кэширование, pagination (`top 100`, `around me`).  
- Событийная модель (`leaderboard:updated`, `rank-changed`).

### Дополнительные источники
- `.BRAIN/05-technical/backend/progression-backend.md` — уровень/опыт.  
- `.BRAIN/05-technical/backend/quest-engine-backend.md` — PvE достижения.  
- `.BRAIN/05-technical/backend/pvp-rating-system.md` — PvP рейтинг.  
- `.BRAIN/05-technical/backend/notification-system.md` — уведомления о смене ранга.

### Связанные документы
- `.BRAIN/02-gameplay/social/competitive-features.md` — UX рейтингов.  
- `.BRAIN/05-technical/backend/event-bus-overview.md` — список событий источников.  
- `.BRAIN/05-technical/backend/analytics-data-lake.md` — выгрузка данных.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/world/leaderboards/leaderboard-system.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/world` и `http://localhost:8080/api/v1/world`.

**Тип:** OpenAPI 3.0.3 · **Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── leaderboards/
                └── leaderboard-system.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** world-service  
- **Порт:** 8086  
- **API Base:** `/api/v1/world/leaderboards`  
- **Интеграции:** gameplay-service (PvE/PvP события), social-service (друзья/гильдии), economy-service (богатство), analytics-service, notification-service.  
- **Комментарий для спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: world-service (port 8086)
  # - API Base: /api/v1/world/leaderboards
  # - Dependencies: gameplay-service, social-service, economy-service, notification-service, analytics-service
  # - Frontend Module: modules/progression/leaderboards
  # - UI: LeaderboardTable, AroundMeCard, RankBadge
  # - Hooks: useProgressionStore, useFilters, useRealtime
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: world-service
    port: 8086
    domain: world
    base-path: /api/v1/world/leaderboards
    directory: api/v1/world/leaderboards
    package: com.necpgame.worldservice
  ```
- `servers` как выше.  
- `x-websocket`: `wss://api.necp.game/v1/world/leaderboards/{category}/stream` — realtime изменения.

### Frontend
- **Модуль:** `modules/progression/leaderboards`.  
- **State Store:** `useProgressionStore` (`leaderboards`, `myRank`, `filters`, `friendsRank`, `guildRank`).  
- **UI:** LeaderboardTable, AroundMeCard, RankBadge, SeasonSwitcher, CategoryFilter.  
- **Формы:** LeaderboardFilterForm, SeasonSelectionForm.  
- **Хуки:** useRealtime, useDebounce, useSocialStore (friends/guild).  
- **Layouts:** GameLayout, CompetitiveLayout.

---

## ✅ Что нужно сделать

### Шаг 1. Анализ требований
- Список категорий и их источников данных.  
- Сезоны: время старта/окончания, сброс позиций, награды.  
- Поддержка friend/guild фильтров, privacy правил.

### Шаг 2. Проектировать endpoints
1. **GET `/api/v1/world/leaderboards/categories`** — список категорий, сезонность, доступность.  
2. **GET `/api/v1/world/leaderboards/{category}`** — топ N (default 100) с пагинацией и фильтрами.  
3. **GET `/api/v1/world/leaderboards/{category}/me`** — позиция игрока, окружение (`aroundMe`).  
4. **GET `/api/v1/world/leaderboards/{category}/friends`**, **`/guild`** — социальные срезы.  
5. **GET `/api/v1/world/leaderboards/{category}/season/{seasonId}`** — исторические данные.  
6. **POST `/api/v1/world/leaderboards/{category}/submit`** — внутренняя запись результата (service token).  
7. **POST `/api/v1/world/leaderboards/{category}/season/reset`** — старт нового сезона (admin).  
8. **GET `/api/v1/world/leaderboards/{category}/stats`** — aggregate показатели (для UI/analytics).  
9. **GET `/api/v1/world/leaderboards/{category}/rewards`** — награды по позициям.

### Шаг 3. Модели
- `LeaderboardCategory`, `LeaderboardEntry`, `LeaderboardAroundMe`, `SocialLeaderboard`, `SeasonInfo`, `LeaderboardStats`, `LeaderboardReward`.  
- Ошибки: `LeaderboardError` (`VAL_UNKNOWN_CATEGORY`, `BIZ_SEASON_CLOSED`, `BIZ_SUBMIT_DISABLED`).  
- WebSocket payload: `leaderboardUpdated`, `rankChanged`, `seasonReset`.

### Шаг 4. OpenAPI оформление
- Описать query параметры (limit, offset, seasonId, filters, socialMode).  
- Использовать общие компоненты для ответов/безопасности.  
- `security`: `BearerAuth` для чтения, `ServiceToken` / `AdminToken` для submit/reset.  
- Примеры: global топ, friend leaderboard, aroundMe, submit score.  
- В `components` вынести enum категорий/сезонов, схемы ranking entries.

### Шаг 5. Проверки
- Запустить `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/world/leaderboards/`.  
- Проверить лимит 400 строк, наличие README.  
- Обновить `brain-mapping.yaml`, документ `.BRAIN` и сопутствующие README.

---

## 🔍 Критерии приемки
1. `info.x-microservice` указывает `world-service`, порт `8086`, домен `world`.  
2. Все публичные маршруты под `/api/v1/world/leaderboards`.  
3. Реализованы категории, социальные фильтры, сезонность, rewards, stats.  
4. Предусмотрен internal submit/reset с `ServiceToken`/`Admin` защитой.  
5. WebSocket события и payload описаны.  
6. Ошибки используют `shared/common/responses.yaml`.  
7. Примеры покрывают основные сценарии.  
8. Валидаторы проходят без ошибок.  
9. Обновлены brain-mapping и `.BRAIN` документ (новый путь).  
10. README каталога `world/leaderboards` отражает API и сценарии.  
11. Указаны SLA кэширования и ограничения (rate limiting, max entries).

---

## FAQ
- **Как хранится `around me`?** Через Redis sorted sets + вторичный запрос, документировать в `x-notes`.  
- **Можно ли скрыть профиль?** Предусмотреть флаг `privacy`, описать поведение.  
- **Нужны ли гильдейские rewards?** Да, в `rewards` endpoint указать структуру для guild payouts.  
- **Как обрабатывать сезонный сброс?** Endpoint reset запускает batch, возвращает новый `seasonId`.  
- **Что с кроссплатформенностью?** Фильтр по платформе (`platform`) включить в параметры.

---

**Источник:** `.BRAIN/05-technical/backend/leaderboard-system.md` (v1.0.0, ready)

