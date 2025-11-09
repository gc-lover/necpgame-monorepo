# Task ID: API-TASK-225
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 04:00
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-211, API-TASK-140, API-TASK-196

---

## 📋 Краткое описание

Разработать API глобальной системы рейтингов: глобальные, сезонные, дружественные и клановые таблицы с realtime обновлениями и аналитикой.

**Что нужно сделать:** Создать `api/v1/world/leaderboards/leaderboards.yaml`, описав REST and WS контракты на основе `.BRAIN/05-technical/backend/leaderboard-system.md`.

---

## 🎯 Цель задания

Обеспечить масштабируемую систему рейтингов, интегрированную с progression, кланами и аналитикой.

**Зачем это нужно:**
- Поддержать PvE/PvP таблицы, сезонные циклы, награды
- Предоставить UI данные для dashboard, friends, nearby players
- Интегрировать с клановыми войнами, progression и achievements
- Собирать аналитику для live-ops и экономики

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/leaderboard-system.md`
**Версия:** v1.0.0 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Типы leaderboards: global, seasonal, friends, guild, PvE/PvP
- Redis sorted sets, caching, diff updates
- Reward distribution, seasons, reset logic
- WebSocket topics, live updates
- Admin/GM tools for maintenance

### Дополнительные источники

- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/clan-war/clan-war-system.md`
- `.BRAIN/05-technical/backend/notification-system.md`
- `.BRAIN/05-technical/backend/analytics/analytics-reporting.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-211-leaderboards-ui-api.md`
- `API-SWAGGER/tasks/active/queue/task-140-progression-backend-api.md`
- `API-SWAGGER/tasks/active/queue/task-223-clan-war-system-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/world/leaderboards/leaderboards.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/world/leaderboards/
 ├── leaderboards.yaml  ← создать/обновить
 ├── leaderboards-components.yaml
 └── leaderboards-examples.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** world-service (leaderboards module)
- **Порт:** 8086
- **API Base Path:** `/api/v1/world/leaderboards`
- **Зависимости:**
  - progression-service – уровни, XP
  - clan-service – клановые рейтинги
  - social-service – friends/guild relations
  - analytics-service – метрики, историю
  - notification-service – уведомления о rank change
  - realtime-service – live updates (WebSocket)

### Frontend
- **Модуль:** `modules/social/leaderboards`
- **State Store:** `useLeaderboardsStore`
- **State:** `boardList`, `entries`, `playerRank`, `filters`, `seasonInfo`
- **UI компоненты:** `LeaderboardTable`, `LeaderboardFilters`, `PlayerSpotlight`, `NearbyPlayers`, `SeasonalRewards`, `RankTimeline`
- **Формы:** `BoardFilterForm`, `ShareRankForm`
- **Хуки:** `useLeaderboardFilters`, `usePlayerRank`, `useSeasonSwitch`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: world-service (port 8086)
# - API Base: /api/v1/world/leaderboards
# - Dependencies: progression, clan, social, analytics, notification, realtime
# - Frontend Module: modules/social/leaderboards (useLeaderboardsStore)
# - UI: LeaderboardTable, LeaderboardFilters, PlayerSpotlight, NearbyPlayers, SeasonalRewards, RankTimeline
# - Forms: BoardFilterForm, ShareRankForm
# - Hooks: useLeaderboardFilters, usePlayerRank, useSeasonSwitch
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить модели таблиц, участников, сезонной информации, rewards.
2. Реализовать эндпоинты списка рейтингов, детализации, entries, player rank, friends.
3. Описать сезонный цикл: season info, history, rewards, resets.
4. Поддержать realtime события rank change, leaderboard refresh.
5. Добавить аналитические отчёты, фильтры, presets.
6. Настроить кэширование (Redis), pagination, ограничение запросов.
7. Подготовить примеры JSON, UI сценарии, тест-план.

---

## 🔀 Endpoints

1. **GET `/api/v1/world/leaderboards`** – список доступных рейтингов (filters: type, season, region).
2. **GET `/api/v1/world/leaderboards/{boardId}`** – информация о конкретной таблице (rules, scoring, rewards).
3. **GET `/api/v1/world/leaderboards/{boardId}/entries`** – entries с пагинацией и scopes (`GLOBAL|FRIENDS|CLAN`).
4. **GET `/api/v1/world/leaderboards/{boardId}/rank`** – позиция игрока, nearby players.
5. **GET `/api/v1/world/leaderboards/{boardId}/season`** – сезонные данные, таймеры, rewards.
6. **GET `/api/v1/world/leaderboards/{boardId}/history`** – история сезонных позиций, графики.
7. **POST `/api/v1/world/leaderboards/{boardId}/share`** – генерация share payload.
8. **GET `/api/v1/world/leaderboards/{boardId}/friends`** – рейтинг друзей/кланов.
9. **GET `/api/v1/world/leaderboards/{boardId}/analytics`** – метрики (activity, rank changes, churn).
10. **POST `/api/v1/world/leaderboards/{boardId}/refresh`** – ручное обновление (admin/GM, audit).
11. **POST `/api/v1/world/leaderboards/{boardId}/season/reset`** – завершение сезона и старт нового.
12. **POST `/api/v1/world/leaderboards/{boardId}/entries`** – ingest сервисных обновлений (idempotency key).
13. **GET `/api/v1/world/leaderboards/leaderboard-map`** – карта регионов/территорий (для интеграции с clan wars).
14. **GET `/api/v1/world/leaderboards/leaderboard-config`** – конфигурация scoring, multipliers.
15. **WS `/api/v1/world/leaderboards/stream`** – события: `leaderboard-updated`, `rank-changed`, `season-started`, `season-ended`, `reward-unlocked`.

---

## 🧱 Модели данных

- **Leaderboard** – `boardId`, `name`, `type`, `season`, `rules`, `scoring`, `rewards`, `status`.
- **LeaderboardEntry** – `playerId`, `nickname`, `rank`, `score`, `delta`, `isFriend`, `clan`, `region`, `lastUpdated`.
- **PlayerRank** – `rank`, `score`, `nextPromotionAt`, `previousBest`, `streak`.
- **SeasonInfo** – `seasonId`, `title`, `status`, `startAt`, `endAt`, `rewards[]`, `bonuses`.
- **SeasonHistoryEntry** – `timestamp`, `rank`, `score`, `event` (`PROMOTION|DEMOTION|REWARD`).
- **LeaderboardReward** – `rewardType` (`COSMETIC|CURRENCY|TITLE`), `payload`, `distributionMethod`.
- **RealtimeEventPayload** – `leaderboardUpdated`, `rankChanged`, `seasonStarted`, `seasonEnded`, `rewardUnlocked`.
- **Error Schema (`LeaderboardError`)** – codes (`BOARD_NOT_FOUND`, `SEASON_LOCKED`, `SCOPE_NOT_ALLOWED`, `INGEST_CONFLICT`, `REFRESH_LIMIT`, `REWARD_PENDING`).

---

## 🧭 Принципы и правила

- Авторизация: публичные GET; `BearerAuth` для персональных данных; `ServiceToken` для ingest.
- Кэширование: Redis/ETag; refresh через invalidate или scheduled updates.
- Rate limiting: защита от спама `refresh`, `share`.
- Seasons: автоматический cron (отдельный service), manual override с audit.
- Events: publish на realtime-service и notification (push).
- Analytics: каждое обновление → analytics-service.

---

## 🧪 Примеры

- Получение глобального рейтинга с фильтром по региону.
- Player rank и nearby players с friend-first логикой.
- Сезонный reset и выдача наград.
- WebSocket событие `rank-changed` для уведомления игрока.
- Ingest сервисное обновление результата матча.

---

## 🔗 Связности и зависимости

- Интегрируется с clan wars, progression, achievements, daily quests.
- Использует UI `Leaderboards` (Task 211) для отображения.
- Взаимодействует с notification/analytics для уведомлений/метрик.

---

## ✅ Критерии приемки

1. `leaderboards.yaml` описывает все типы таблиц, данные и события.
2. Прописаны сезонные механики, награды, аналитика, безопасность.
3. Добавлены примеры, тест-план и чеклист.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, UI модуль, зависимости
- [ ] Эндпоинты и события покрывают все сценарии leaderboards
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как обрабатывать tied ranks?**
**A:** Документировать tie-breaking (score timestamp, playerId). API должен возвращать `tieBreakInfo`.

**Q:** Поддерживаются ли private leaderboards?**
**A:** Можно расширить scope `CUSTOM`, требующий авторизации/ограниченной видимости; выделить в будущем.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

