# Task ID: API-TASK-237
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 06:45
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-225, API-TASK-224, API-TASK-219

---

## 📋 Краткое описание

Сформировать OpenAPI спецификацию базовой системы рейтингов: расчёт и хранение MMR, глобальные/региональные/категориальные таблицы, сезонность, награды, аудит и антифрод.

**Что нужно сделать:** Создать `api/v1/leaderboards/leaderboard-core.yaml`, описав REST/WS контракты из `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md`.

---

## 🎯 Цель задания

Обеспечить масштабируемый сервис рейтингов, который предоставляет точные позиции игроков, сезонные циклы и инструменты для LiveOps.

**Зачем это нужно:**
- Обрабатывать обновления рейтингов в реальном времени
- Поддерживать сезонные лиги, награды и reset логики
- Предоставить API для UI, аналитики и клановых систем
- Интегрировать рейтинги с progression, achievements и матчмейкингом

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/leaderboard/leaderboard-core.md`
**Версия:** v1.0.0 (2025-11-07)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Типы рейтингов (global, regional, category, seasonal)
- Score calculation, MMR подсчёт, decay, boosting
- Redis sorted sets, PostgreSQL архив, кэширование
- Reward система, сезонные награды, титулы
- Антифрод: detection smurfing, ограничения обновлений

### Дополнительные источники

- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/quest-engine-backend.md`
- `.BRAIN/05-technical/backend/achievement/achievement-tracking.md`
- `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md`
- `.BRAIN/05-technical/backend/notification-system.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-225-leaderboard-system-api.md`
- `API-SWAGGER/tasks/active/queue/task-224-progression-backend-api.md`
- `API-SWAGGER/tasks/active/queue/task-219-achievement-tracking-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/leaderboards/leaderboard-core.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/leaderboards/
 ├── leaderboard-core.yaml          ← создать/обновить
 ├── leaderboard-components.yaml
 └── leaderboard-examples.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** leaderboard-service (модуль world-service)
- **Порт:** 8099
- **API Base Path:** `/api/v1/leaderboards/core`
- **Зависимости:**
  - progression-service – XP, уровень, power score
  - achievement-service – очки достижений
  - combat-service – PvP результаты
  - party/guild-service – клановые рейтинги
  - matchmaking-service – MMR расчёты
  - notification-service – уведомления о rank-up
  - analytics-service – отчёты, live dashboards

### Frontend
- **Модуль:** `modules/social/leaderboards`
- **State Store:** `useLeaderboardsStore`
- **State:** `boards`, `entries`, `playerScore`, `seasonInfo`, `rewards`, `filters`
- **UI компоненты:** `LeaderboardSwitcher`, `LeaderboardBoardView`, `PlayerRankCard`, `SeasonProgressPanel`, `RewardPreview`, `SmurfFlagBadge`
- **Формы:** `LeaderboardFilterForm`, `SeasonRewardsClaimForm`, `MMRAdjustmentForm` (ops)
- **Хуки:** `useLeaderboardCore`, `useLeaderboardSeasons`, `usePlayerRank`, `useLeaderboardRewards`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: leaderboard-service (port 8099)
# - API Base: /api/v1/leaderboards/core
# - Dependencies: progression, achievement, combat, guild/party, matchmaking, notification, analytics
# - Frontend Module: modules/social/leaderboards (useLeaderboardsStore)
# - UI: LeaderboardSwitcher, LeaderboardBoardView, PlayerRankCard, SeasonProgressPanel, RewardPreview, SmurfFlagBadge
# - Forms: LeaderboardFilterForm, SeasonRewardsClaimForm, MMRAdjustmentForm
# - Hooks: useLeaderboardCore, useLeaderboardSeasons, usePlayerRank, useLeaderboardRewards
```

---

## ✅ Что нужно сделать (детальный план)

1. Определить модели leaderboard board, entries, seasons, tiers, rewards, audit.
2. Реализовать API получения рейтингов, позиции игрока, nearby entries, фильтры.
3. Добавить управление сезонными циклами: старт, завершение, награждение, reset.
4. Описать обновление счёта (ingest events) и антифрод проверки.
5. Реализовать систему наград и claim процесса (manual/auto).
6. Настроить WebSocket события (rank change, season status, reward unlocked).
7. Добавить админские эндпоинты для корректировок рейтинга, блокировок.
8. Подготовить историю изменений и audit log.
9. Подготовить примеры запросов, чеклист, тестовые сценарии.

---

## 🔀 Endpoints

1. **GET `/api/v1/leaderboards/core`** – список доступных рейтингов (типы, сезоны, статусы).
2. **GET `/api/v1/leaderboards/core/{boardId}`** – детали рейтинга (правила, метрики, награды).
3. **GET `/api/v1/leaderboards/core/{boardId}/entries`** – entries с фильтрами (`GLOBAL|REGION|CATEGORY|SEASON`, `tier`, `guild`).
4. **GET `/api/v1/leaderboards/core/{boardId}/player/{playerId}`** – позиция игрока, nearby, тренды.
5. **GET `/api/v1/leaderboards/core/{boardId}/tiers`** – информация о лигах, порогах, наградах.
6. **GET `/api/v1/leaderboards/core/{boardId}/season`** – статус текущего сезона, таймеры, milestones.
7. **POST `/api/v1/leaderboards/core/{boardId}/season/start`** – запуск сезона (ops/GM).
8. **POST `/api/v1/leaderboards/core/{boardId}/season/end`** – завершение и подготовка наград.
9. **POST `/api/v1/leaderboards/core/{boardId}/season/reset`** – reset и архивирование.
10. **POST `/api/v1/leaderboards/core/{boardId}/rewards/claim`** – получение сезонных наград.
11. **POST `/api/v1/leaderboards/core/{boardId}/rewards/grant`** – принудительная выдача (ops/GM).
12. **POST `/api/v1/leaderboards/core/ingest`** – ingest событий (score updates, MMR changes) с idempotency и антифрод метаданными.
13. **GET `/api/v1/leaderboards/core/history`** – история изменений рейтинга, suspensions, penalty.
14. **GET `/api/v1/leaderboards/core/stats`** – метрики (active players, top trends, decay triggers).
15. **POST `/api/v1/leaderboards/core/{boardId}/adjust`** – ручная корректировка очков (audit required).
16. **POST `/api/v1/leaderboards/core/{boardId}/suspend`** – блокировка игрока/клана в рейтинге (smurf, cheating).
17. **GET `/api/v1/leaderboards/core/mmr/{playerId}`** – детализация MMR (base, confidence, decay, modifiers).
18. **POST `/api/v1/leaderboards/core/mmr/{playerId}`** – корректировка/ресет MMR (matchmaking integration).
19. **GET `/api/v1/leaderboards/core/rewards`** – список наград, условий, claim status.
20. **WS `/api/v1/leaderboards/core/stream`** – события: `leaderboard-updated`, `rank-changed`, `season-started`, `season-ended`, `reward-unlocked`, `player-flagged`, `mmr-updated`.

---

## 🧱 Модели данных

- **Leaderboard** – `boardId`, `name`, `type`, `scope`, `season`, `metrics`, `rules`, `status`.
- **LeaderboardEntry** – `playerId`, `guildId`, `score`, `rank`, `tier`, `region`, `trend`, `flags`.
- **LeaderboardTier** – `tierId`, `name`, `minScore`, `maxScore`, `rewards`, `decayRules`.
- **SeasonInfo** – `seasonId`, `title`, `phase`, `startAt`, `endAt`, `resetAt`, `rewardSchedule`.
- **Reward** – `rewardId`, `type`, `payload`, `deliveryMethod`, `conditions`, `expiry`.
- **ScoreIngestRequest** – `eventId`, `playerId`, `boardId`, `delta`, `source`, `matchContext`, `confidence`, `idempotencyKey`.
- **LeaderboardHistoryEntry** – `entryId`, `boardId`, `playerId`, `action`, `valueBefore`, `valueAfter`, `actor`, `timestamp`.
- **MMRProfile** – `playerId`, `mmr`, `sigma`, `decay`, `streak`, `calibration`.
- **RealtimeEventPayload** – `leaderboardUpdated`, `rankChanged`, `seasonStarted`, `seasonEnded`, `rewardUnlocked`, `playerFlagged`, `mmrUpdated`.
- **Error Schema (`LeaderboardCoreError`)** – codes (`BOARD_LOCKED`, `SEASON_ACTIVE`, `INGEST_RATE_LIMIT`, `PLAYER_SUSPENDED`, `CLAIM_DENIED`, `MMR_OUT_OF_RANGE`, `ADJUSTMENT_REQUIRES_AUDIT`, `REWARD_ALREADY_CLAIMED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` для игроков; `OpsToken`/`GMToken` для админ операций; read-only публичные endpoints для статус-страниц.
- Consistency: использовать event sourcing + Redis sorted set; все изменения логируются и доступны для replay.
- Rate limiting: ограничение ingest вызовов и manual adjustments.
- Anti-fraud: проверять delta, confidence; смаркетинг смурфов; flag suspicious records.
- Seasons: поддержка автоматической смены сезона, архивирование, миграция наград.
- Localization: названия досок и наград через shared localization.
- Observability: метрики (Prometheus), аудиты, webhooks для live dashboards.

---

## 🧪 Примеры

- Получение позиции игрока с динамическими nearby entries.
- Запуск нового сезона с очисткой рейтинга и протоколами наград.
- Ingest события победы в рейтинговом матче и обновление MMR.
- Ручная корректировка рейтинга после расследования античита.
- WebSocket событие `rank-changed` и уведомление о награде.

---

## 🔗 Связности и зависимости

- Интегрируется с progression, achievements, matchmaking, guild/party, notification, analytics, maintenance.
- Используется UI `Leaderboard` (Task 211) и панель админов.
- События влияют на battle pass, daily quests, сезонные награды.

---

## ✅ Критерии приемки

1. `leaderboard-core.yaml` описывает все типы досок, сезонность, награды.
2. Определены модели, события, антифрод, audit, интеграции.
3. Прописаны примеры, чеклист, тестовые сценарии.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, UI модуль, зависимости
- [ ] Эндпоинты и события покрывают все сценарии рейтингов
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] Обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как обрабатывать временные события (Weekend Tournaments)?**
**A:** Добавить `_event` доски с временными окнами; описать в `SeasonInfo` расширение `eventWindow`; поддержать автоочистку.

**Q:** Нужна ли поддержка скрытия игроков?**
**A:** Да, предусмотреть `visibility` флаг и фильтрацию записей по privacy настройкам; описать в `LeaderboardEntry.flags`.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

