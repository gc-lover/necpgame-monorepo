# Task ID: API-TASK-211
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 00:59
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-137

---

## 📋 Краткое описание

Разработать UI-ориентированный API `leaderboards-ui`, объединяющий данные глобальных, сезонных, дружественных рейтингов и позиционирование игрока.

**Что нужно сделать:** Создать `api/v1/world/leaderboards/leaderboards-ui.yaml`, описав REST и realtime контракты, поддерживающие экраны глобальных/категорийных таблиц, сезонные лиги, соседей игрока и фильтры.

---

## 🎯 Цель задания

Предоставить фронтенду агрегированные рейтинговые данные с минимальными запросами и быстрым обновлением позиции игрока.

**Зачем это нужно:**
- Покрыть UI требования из `.BRAIN` документа (глобальные, сезонные, друзья, лиги)
- Снабдить фронт готовыми DTO для карточек, панелей фильтров, позиционирования
- Включить realtime обновления (rank change, сезонные события)
- Согласовать поведение с core задачей `API-TASK-137` (leaderboard backend)

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/ui/leaderboards/ui-leaderboards.md`
**Версия:** v1.0.0 (2025-11-07 02:18)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Макеты главного экрана, сезонных лиг, таблиц и виджетов игрока
- Требования к фильтрам (season, category, region, platform, friends)
- Секции Nearby Players, Player Spotlight, Seasonal Rewards
- Индикаторы ранга (promotion/demotion), исторические графики
- Realtime события (rank change, league transition)

### Дополнительные источники

- `.BRAIN/05-technical/backend/leaderboard-system.md` – доменная логика рейтингов
- `.BRAIN/05-technical/backend/progression-backend.md` – очки/XP
- `.BRAIN/05-technical/backend/social/friend-system.md` – данные друзей (если есть)
- `.BRAIN/05-technical/backend/notification-system.md` – уведомления
- `.BRAIN/05-technical/backend/analytics/analytics-events.md` – метрики (если доступно)

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-137-leaderboard-system-api.md` – ядро рейтингов (обязательная зависимость)
- `API-SWAGGER/tasks/active/queue/task-159-progression-detailed-api.md` – очки прогрессии
- `API-SWAGGER/tasks/active/queue/task-158-social-mechanics-detailed-api.md` – данные друзей/кланов

---

## 📁 Целевая структура API

- **Файл:** `api/v1/world/leaderboards/leaderboards-ui.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 (REST + WebSocket)

```
API-SWAGGER/api/v1/world/leaderboards/
 ├── leaderboards.yaml          (API-TASK-137)
 └── leaderboards-ui.yaml       ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** world-service
- **Порт:** 8086
- **API Base Path:** `/api/v1/world/leaderboards/ui`
- **Зависимости:**
  - auth-service – аутентификация игроков
  - leaderboard-core (world-service) – получение рейтингов, позиции, статистики
  - social-service – друзья/кланы, приватность
  - progression-service – очки/уровни
  - notification-service – уведомления о повышении
  - analytics-service – метрики использования
  - realtime-service – push rank updates

### Frontend
- **Модуль:** `modules/social/leaderboards`
- **State Store:** `useLeaderboardsStore`
- **State:** `globalBoards`, `seasonBoards`, `playerSpotlight`, `filters`, `friends`, `history`, `leagueStatus`
- **UI компоненты:** `LeaderboardTable`, `LeaderboardFilters`, `PlayerPositionCard`, `NearbyPlayersList`, `SeasonalRewardsPanel`, `RankChangeTicker`
- **Формы:** `LeaderboardFilterForm`, `ShareRankForm`, `NotificationOptInForm`
- **Layouts:** `SocialHubLayout`, `ProgressionHubLayout`
- **Хуки:** `useLeaderboardFilters`, `useRankRealtime`, `useNearbyPlayers`, `useSeasonSwitch`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: world-service (port 8086)
# - API Base: /api/v1/world/leaderboards/ui
# - Dependencies: auth, leaderboard-core, social, progression, notification, analytics, realtime
# - Frontend Module: modules/social/leaderboards (useLeaderboardsStore)
# - UI: LeaderboardTable, LeaderboardFilters, PlayerPositionCard, NearbyPlayersList, SeasonalRewardsPanel, RankChangeTicker
# - Forms: LeaderboardFilterForm, ShareRankForm, NotificationOptInForm
# - Layouts: SocialHubLayout, ProgressionHubLayout
# - Hooks: useLeaderboardFilters, useRankRealtime, useNearbyPlayers, useSeasonSwitch
```

---

## ✅ Что нужно сделать (детальный план)

1. Сформировать агрегированные DTO для глобальных, сезонных, дружеских таблиц и секции игрока.
2. Добавить REST эндпоинты для списков, фильтров, построения виджетов (nearby, spotlight, rewards, history).
3. Реализовать операции share rank, подписка на уведомления о продвижении, закрепление избранных таблиц.
4. Настроить WebSocket канал `leaderboards.stream` для событий rank change, league updates, leaderboard refresh.
5. Прописать поддержку пагинации (cursor-based) и отсечение больших объёмов данных.
6. Уточнить кэширование, синхронизацию с core leaderboard API и валидацию фильтров.
7. Определить ошибки, лимиты, правила приватности (друзья/кланы, скрытые профили).
8. Подготовить примеры, сценарии тестирования, пройти чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/world/leaderboards/ui/dashboard`** – агрегированная информация (top boards, player rank, season info).
2. **GET `/api/v1/world/leaderboards/ui/global`** – список глобальных рейтингов (категории, режимы), поддержка фильтров.
3. **GET `/api/v1/world/leaderboards/ui/global/{boardId}`** – детали конкретной таблицы (top entries, player position, stats).
4. **GET `/api/v1/world/leaderboards/ui/global/{boardId}/entries`** – страницируемый список записей с параметрами `cursor`, `limit`, `scope` (`GLOBAL|FRIENDS|CLAN`).
5. **GET `/api/v1/world/leaderboards/ui/player`** – позиция игрока, delta, ближайшие соперники, streak продвижения.
6. **GET `/api/v1/world/leaderboards/ui/seasons`** – текущий и прошлые сезоны, таймеры, состояние лиг.
7. **GET `/api/v1/world/leaderboards/ui/seasons/{seasonId}`** – сезонные лиги (дивизионы, пороги, награды).
8. **GET `/api/v1/world/leaderboards/ui/seasons/{seasonId}/history`** – история игрока (ранги по неделям, график).
9. **POST `/api/v1/world/leaderboards/ui/notifications`** – настройки уведомлений (promotion/demotion, friend surpass).
10. **POST `/api/v1/world/leaderboards/ui/share`** – создание шаринга ранга (payload для соц./клан чата).
11. **GET `/api/v1/world/leaderboards/ui/friends`** – рейтинги друзей по выбранной таблице (privacy-aware).
12. **GET `/api/v1/world/leaderboards/ui/search`** – поиск игроков по имени/ID (limit, privacy checks).
13. **GET `/api/v1/world/leaderboards/ui/trends`** – аналитика по категориям, изменения активности.
14. **POST `/api/v1/world/leaderboards/ui/pin`** – закрепление избранных таблиц на главном экране.
15. **WS `/api/v1/world/leaderboards/ui/stream`** – события: `rank-change`, `league-transition`, `board-refresh`, `friend-surpass`, `seasonal-alert`.

---

## 🧱 Модели данных

- **LeaderboardDashboard** – `featuredBoards[]`, `playerRank`, `seasonInfo`, `alerts[]`.
- **LeaderboardSummary** – `boardId`, `name`, `category`, `season`, `entriesPreview[]`, `playerRank`, `trend`, `isPinned`.
- **LeaderboardEntry** – `playerId`, `nickname`, `value`, `rank`, `delta`, `isFriend`, `clan`, `region`, `platform`.
- **PlayerPosition** – `boardId`, `rank`, `score`, `delta`, `nextPromotionAt`, `previousBest`, `nearbyPlayers[]`.
- **SeasonInfo** – `seasonId`, `name`, `status`, `startAt`, `endAt`, `remaining`, `league`, `tier`, `promotion`, `relegation`.
- **SeasonHistoryPoint** – `timestamp`, `rank`, `score`, `delta`, `event`.
- **NotificationPreferences** – `promotion`, `demotion`, `friendSurpass`, `seasonStart`, `channel`.
- **SharePayload** – `boardId`, `playerRank`, `preview`, `deepLink`, `expiresAt`.
- **FriendLeaderboard** – `boardId`, `entries[]` (friends only), `playerRank`, `privacy`.
- **TrendData** – `boardId`, `metric`, `change`, `period`, `topMovers[]`.
- **PinRequest** – `boardId`, `pinned` (bool), `slot`.
- **RealtimeEvent** – union (`rankChange`, `leagueTransition`, `boardRefresh`, `friendSurpass`, `seasonalAlert`).
- **Error Schema (`LeaderboardUiError`)** – codes (`BOARD_NOT_FOUND`, `PLAYER_NOT_VISIBLE`, `PRIVACY_RESTRICTED`, `PIN_LIMIT`, `NOTIFICATION_FORBIDDEN`, `SEASON_CLOSED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth`; `ServiceToken` для внутреннего пуша обновлений.
- Приватность: уважаем настройки скрытых профилей (social-service проверки).
- Пагинация: cursor-based (`nextCursor`, `prevCursor`), limit ≤ 100.
- Кэширование: ETag/Last-Modified для таблиц, `Cache-Control: max-age=30` для summary.
- Локализация и форматирование: поддержка `locale`, `numberFormat`.
- Доступность: возвращать `ariaLabels`, `highlightReason` для топ-движений.
- Античит: события подозрительных скачков → incident-service.
- Использовать общие компоненты (`responses.yaml`, `pagination.yaml`, `security.yaml`).

---

## 🧪 Примеры

- Dashboard с указанной сезонной информацией и позициями игрока.
- Страница глобального рейтинга с пагинацией, фильтром по региону и секцией nearby players.
- Событие `rank-change` по WebSocket c обновлением UI.
- Шаринг ранга в клановый чат с ссылкой на профиль.
- История сезонного продвижения игрока с графиком.

---

## 🔗 Связности и зависимости

- Зависит от `API-TASK-137` (leaderboard core) для данных таблиц и рангов.
- Интеграция с social-service (friends/clans), progression-service (очки), notification-service (alerts).
- События передаются через realtime-service; аналитика в analytics-service.

---

## ✅ Критерии приемки

1. Файл `leaderboards-ui.yaml` создан, содержит архитектурный комментарий, REST и WS секции.
2. Эндпоинты покрывают dashboard, глобальные/сезонные таблицы, игрока, друзей, поиск, тренды.
3. Модели описывают DTO для UI (entries, player position, seasons, history, alerts).
4. Прописаны правила пагинации, приватности, кэширования и локализации.
5. WebSocket канал описан с типами событий и payload.
6. Указаны зависимости и взаимодействия с core leaderboard API и смежными сервисами.
7. Добавлены примеры и сценарии тестирования; выполнен чеклист.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, модуль фронтенда, зависимости, UI компоненты
- [ ] Эндпоинты + WebSocket отражают все сценарии документа
- [ ] Модели, ошибки, правила, примеры, критерии присутствуют
- [ ] После сохранения обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Чем UI API отличается от core leaderboards?
**A:** Core управляет расчётом рейтингов. UI API агрегирует данные, добавляет секции Nearby/Friends, фильтры, визуальные DTO и realtime обновления.

**Q:** Как обрабатывается приватность игроков?
**A:** Через social-service: запросы фильтруют записи скрытых профилей, возвращая `PRIVACY_RESTRICTED` при отсутствии разрешения.

**Q:** Нужен ли отдельный канал для сезонных уведомлений?
**A:** Они передаются по WebSocket `seasonalAlert` и дублируются через notification-service при необходимости.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

