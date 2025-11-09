# Task ID: API-TASK-209
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 00:28
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-136

---

## 📋 Краткое описание

Настроить фронтовой API-слой `achievements-ui`, обеспечивающий агрегированные данные и realtime обновления для интерфейса достижений.

**Что нужно сделать:** Подготовить `api/v1/gameplay/achievements/achievements-ui.yaml`, описав REST и WebSocket каналы для списков достижений, фильтров, прогресса и уведомлений, включая структуры, необходимые UI макетам.

---

## 🎯 Цель задания

Обеспечить UI Achievements данными в удобном формате, минимизирующим количество запросов и преобразований на клиенте.

**Зачем это нужно:**
- Предоставить готовые DTO для основных экранов (список, категория, прогресс, история)
- Поддержать фильтры и сортировку, соответствующие UX требованиям
- Включить realtime уведомления о новых достижениях и почти завершённых прогрессах
- Синхронизировать UI с backend задачей `API-TASK-136` (core achievements)

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/ui/achievements/ui-achievements-main.md`
**Версия:** v1.0.0 (2025-11-07 02:18)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Макеты главного экрана, просмотра категории, карточек достижений
- Требования к фильтрам (rarity, category, completion state, search)
- Блоки Recent Unlocks, Near Completion, Leaderboard
- Интерактивные элементы (favorite toggle, share, pin)
- Требования к realtime уведомлениям и прогресс-бару

### Дополнительные источники

- `.BRAIN/05-technical/backend/achievement/achievement-core.md` – определения достижений
- `.BRAIN/05-technical/backend/achievement/achievement-tracking.md` – события прогресса
- `.BRAIN/05-technical/backend/achievement/achievement-examples-api.md` – схемы API
- `.BRAIN/05-technical/backend/notification-system.md` – пуши/уведомления
- `.BRAIN/05-technical/backend/progression-backend.md` – общая прогрессия

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-136-achievement-system-api.md` – базовые CRUD и события достижений (зависимость)
- `API-SWAGGER/tasks/active/queue/task-159-progression-detailed-api.md` – расширенные данные прогрессии

---

## 📁 Целевая структура API

- **Файл:** `api/v1/gameplay/achievements/achievements-ui.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 (REST + WebSocket схемы)

```
API-SWAGGER/api/v1/gameplay/achievements/
 ├── achievements.yaml        (API-TASK-136)
 └── achievements-ui.yaml     ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base Path:** `/api/v1/gameplay/achievements/ui`
- **Зависимости:**
  - auth-service – авторизация игроков
  - gameplay-service (achievement core) – агрегировать данные, проверять прогресс
  - social-service – данные друзей для сравнений
  - notification-service – отправка/подписка на уведомления
  - analytics-service – сбор метрик по использованию UI
  - realtime-service – трансляция событий (WebSocket)

### Frontend
- **Модуль:** `modules/progression/achievements`
- **State Store:** `useProgressionStore` (achievements, filters, favorites, leaderboard)
- **State:** `allAchievements`, `categories`, `filters`, `recentUnlocks`, `nearCompletion`, `favorites`, `leaderboard`
- **UI компоненты:** `AchievementGrid`, `AchievementCard`, `AchievementSidebar`, `ProgressSummary`, `RecentUnlocksPanel`, `LeaderboardWidget`
- **Формы:** `FiltersForm`, `ShareAchievementForm`, `PinAchievementForm`
- **Layouts:** `GameLayout`, `ProgressionHubLayout`
- **Хуки:** `useAchievementFilters`, `useAchievementRealtime`, `useFavoriteAchievements`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/gameplay/achievements/ui
# - Dependencies: auth-service, achievement-core (gameplay), social-service, notification-service, analytics-service, realtime-service
# - Frontend Module: modules/progression/achievements (useProgressionStore)
# - UI: AchievementGrid, AchievementCard, AchievementSidebar, ProgressSummary, RecentUnlocksPanel, LeaderboardWidget
# - Forms: FiltersForm, ShareAchievementForm, PinAchievementForm
# - Layouts: GameLayout, ProgressionHubLayout
# - Hooks: useAchievementFilters, useAchievementRealtime, useFavoriteAchievements
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить агрегированные DTO для главного экрана (категории, фильтры, секции).
2. Описать REST эндпоинты для получения списков, категорий, фильтров, истории, избранного.
3. Настроить эндпоинт для обновления избранного/пиннутых достижений и шаринга.
4. Описать WebSocket канал `achievements.stream` для realtime прогресса и уведомлений.
5. Включить поддержку friend comparison и глобальной таблицы лидеров (с пагинацией).
6. Добавить responses с использованием общих компонентов `common/responses.yaml` и `pagination.yaml`.
7. Определить схемы фильтров, сортировок и поисковых запросов (query params).
8. Проработать правила кэширования/ETag, чтобы снизить нагрузку на фронт.
9. Подготовить примеры запросов/ответов, сценарии тестирования, выполнить чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/gameplay/achievements/ui/dashboard`** – агрегированные данные для главного экрана (категории, counters, near completion, recent unlocks).
2. **GET `/api/v1/gameplay/achievements/ui/list`** – список достижений с фильтрами (`category`, `rarity`, `completion`, `search`, `favorites`, `sort`).
3. **GET `/api/v1/gameplay/achievements/ui/{achievementId}`** – детальная карточка с прогрессом, наградами, дружеским сравнением.
4. **POST `/api/v1/gameplay/achievements/ui/{achievementId}/favorite`** – добавить/удалить из избранного (toggle, audit).
5. **POST `/api/v1/gameplay/achievements/ui/{achievementId}/pin`** – закрепить достижение для быстрого доступа.
6. **POST `/api/v1/gameplay/achievements/ui/{achievementId}/share`** – сгенерировать share payload (чат/соцсети).
7. **GET `/api/v1/gameplay/achievements/ui/history`** – история недавних разблокировок (пагинация, фильтры по rarity).
8. **GET `/api/v1/gameplay/achievements/ui/leaderboard`** – лидерборд по очкам достижений (другие параметры, пагинация, friend-first).
9. **GET `/api/v1/gameplay/achievements/ui/friends`** – прогресс друзей по выбранному достижению/категории.
10. **GET `/api/v1/gameplay/achievements/ui/filters`** – доступные фильтры/пресеты (из документа UI).
11. **GET `/api/v1/gameplay/achievements/ui/alerts`** – список текущих бустов/ивентов, влияющих на прогресс.
12. **POST `/api/v1/gameplay/achievements/ui/preferences`** – сохранение пользовательских настроек UI (layout, сортировка).
13. **GET `/api/v1/gameplay/achievements/ui/export`** – выгрузка прогресса для внешнего отображения (CSV/JSON, ограничить доступ).
14. **GET `/api/v1/gameplay/achievements/ui/recommendations`** – рекомендации по достижениями, которые почти выполнены/в тренде.
15. **WS `/api/v1/gameplay/achievements/ui/stream`** – WebSocket канал (`achievement-progress`, `achievement-unlocked`, `favorite-updated`, `leaderboard-update`).

---

## 🧱 Модели данных

- **AchievementDashboard** – `categories[]`, `summary` (completed, total, points), `recentUnlocks[]`, `nearCompletion[]`, `alerts[]`.
- **AchievementListItem** – `id`, `title`, `description`, `category`, `rarity`, `progress`, `isCompleted`, `isFavorite`, `points`, `rewards[]`, `tags[]`.
- **AchievementDetail** – расширенные поля + `friendComparison[]`, `unlockRequirements`, `rewardPreviews`, `loreSnippet`, `relatedAchievements[]`.
- **FavoriteToggleRequest** – `action` (`ADD|REMOVE`), `source` (`UI|SYNC`).
- **PinRequest** – `pin` (bool), `slot` (0-2), `expiresAt`.
- **SharePayload** – `channel` (`CHAT|SOCIAL|CLAN`), `message`, `previewImage`, `deepLink`.
- **LeaderboardEntry** – `playerId`, `nickname`, `points`, `rank`, `trend` (`UP|DOWN|STABLE`), `isFriend`.
- **FriendProgress** – `friendId`, `nickname`, `progress`, `lastUpdated`, `status` (`ONLINE|OFFLINE`).
- **FiltersResponse** – `categories[]`, `rarities[]`, `sortOptions[]`, `presets[]`.
- **AlertsResponse** – `activeBoosts[]`, `events[]`, `expirationTimers`.
- **PreferencesRequest** – `defaultSort`, `cardSize`, `showLocked`, `modules`, `theme`.
- **RecommendationsResponse** – `suggested[]` (achievementId, reason, completion %).
- **RealtimeEvent** – union (`progressUpdate`, `unlock`, `favoriteUpdate`, `leaderboardUpdate`).
- **Error Schema (`AchievementUiError`)** – codes (`ACHIEVEMENT_NOT_FOUND`, `ALREADY_FAVORITE`, `PIN_LIMIT`, `SHARE_FORBIDDEN`, `EXPORT_LIMIT`, `FRIEND_DATA_UNAVAILABLE`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth`; `ServiceToken` только для стрима служебных событий (админ/инфраструктура).
- Ограничения: rate limiting на share/export, максимум 3 pinned achievements, favorites ≤ 20.
- Кэширование: использовать ETag/If-None-Match на `list`, `dashboard`, `filters`.
- Локализация: предусмотреть поле `localizedStrings` или `locale` query для отображения перевода.
- Accessibility: возвращать данные для экранных читалок (altText, longDescription).
- Privacy: friend comparison доступен только при взаимном согласии (social-service проверка).
- Инциденты: подозрительные действия логируются в incident-service.
- DRY: общие ответы/ошибки ссылаться через `$ref` на `api/v1/shared/common/`.

---

## 🧪 Примеры

- Получение dashboard для игрока с заполненными секциями и near completion.
- Список достижений с фильтром по категории «Exploration» и сортировкой по прогрессу.
- Триггер favorite toggle с WebSocket событием для синхронизации на других клиентах.
- Сравнение прогресса с друзьями по конкретному достижению.
- Подписка на WebSocket и получение события `achievement-unlocked`.

---

## 🔗 Связности и зависимости

- Основано на данных и событиях, реализуемых в `API-TASK-136` (achievement core).
- Использует social-service для данных друзей, notification-service для push.
- Отправляет аналитические события в analytics-service (UI usage, conversions, favorites).
- Интегрируется с realtime-service / gateway для WebSocket подключений.

---

## ✅ Критерии приемки

1. Файл `achievements-ui.yaml` создан, включает архитектурный комментарий и REST/WS секции.
2. Эндпоинты покрывают все UI сценарии (dashboard, list, detail, favorites, filters, recommendations, leaderboard).
3. Схемы DTO соответствуют макетам: есть секции recent/near completion, friend comparison, alerts.
4. Предусмотрены ошибки, ограничения и политики безопасности (favorites limit, privacy).
5. Описан WebSocket канал с типами событий, примерами payload.
6. Используются общие компоненты (`responses`, `pagination`, `security`).
7. Прописаны требования к кэшированию и локализации.
8. Добавлены примеры запросов/ответов и сценарии тестирования.
9. Указаны зависимости от `API-TASK-136` и других сервисов.
10. Задание проходит чеклист `tasks/config/checklist.md`.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Прописаны микросервис, фронтенд модуль, зависимости, UI компоненты
- [ ] Эндпоинты и WebSocket покрывают сценарии документа
- [ ] Добавлены модели данных, ошибки, события, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml` после сохранения

---

## ❓FAQ

**Q:** Почему нужен отдельный UI-слой, если уже есть core API?
**A:** Core API обеспечивает CRUD, но UI требует агрегированных и закешированных данных, сценариев «recent/near completion», настроек и realtime – всё это оформляется отдельным контрактом.

**Q:** Можно ли объединить с core файлом?
**A:** Нет, чтобы соблюсти лимит 400 строк и разделить ответственность: core отвечает за доменную модель, ui – за потребности фронтенда.

**Q:** Как обрабатывать приватность друзей?
**A:** Через social-service: перед выдачей friend data проверять разрешения, в противном случае возвращать `FRIEND_DATA_UNAVAILABLE`.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

