# Task ID: API-TASK-210
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 00:45
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-141

---

## 📋 Краткое описание

Подготовить API-слой `daily-quests-ui`, предоставляющий фронтенду агрегированные данные, фильтры и realtime события для ежедневных/еженедельных активностей.

**Что нужно сделать:** Создать `api/v1/gameplay/daily-quests/daily-quests-ui.yaml`, описав REST и WebSocket контракты для вкладок Daily, Weekly, Login Streak, Daily Rewards, а также настройки таймеров, наград, уведомлений.

---

## 🎯 Цель задания

Сделать данные ежедневных активностей доступными в удобном UI-формате, синхронизированном с системами сбросов и прогрессии.

**Зачем это нужно:**
- Вывести все ежедневные активности в едином экране с таймерами и прогрессом
- Поддержать daily/weekly задания, streak, награды и уведомления
- Обеспечить realtime обновления при выполнении задач и наступлении сброса
- Интегрироваться с core задачами (reset system, quest backend) в API-SWAGGER

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/ui/daily-quests/ui-daily-quests.md`
**Версия:** v1.0.0 (2025-11-07 02:18)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Макеты Daily Activities, Weekly Challenges, Login Streak, Daily Rewards
- Описание таймеров, прогресс-баров, карточек заданий
- Фильтры и статусы (completed, in-progress, cooldown)
- События и уведомления (Quest Completed, Streak Bonus, Reward Claimed)
- Интеграция с reset таймерами и пуш-уведомлениями

### Дополнительные источники

- `.BRAIN/05-technical/backend/daily-weekly-reset-system.md` – управление сбросами
- `.BRAIN/05-technical/backend/quest-engine-backend.md` – прогресс квестов
- `.BRAIN/05-technical/backend/progression-backend.md` – XP/награды
- `.BRAIN/05-technical/backend/notification-system.md` – уведомления
- `.BRAIN/05-technical/backend/economy-system.md` – награды (currency/items)

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-141-daily-reset-api.md` – core resets (зависимость)
- `API-SWAGGER/tasks/active/queue/task-138-quest-engine-api.md` – управление квестами
- `API-SWAGGER/tasks/active/queue/task-200-support-ticket-system-api.md` – уведомления (общая логика)

---

## 📁 Целевая структура API

- **Файл:** `api/v1/gameplay/daily-quests/daily-quests-ui.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3 (REST + WebSocket)

```
API-SWAGGER/api/v1/gameplay/daily-quests/
 ├── daily-quests-core.yaml       (файл из API-TASK-141, когда появится)
 └── daily-quests-ui.yaml         ← создать/заполнить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service
- **Порт:** 8083
- **API Base Path:** `/api/v1/gameplay/daily-quests/ui`
- **Зависимости:**
  - auth-service – проверка токенов
  - quest-service – прогресс daily/weekly квестов
  - reset-service (world-service) – расписания сбросов
  - economy-service – награды, валюты
  - notification-service – пуш/звуковые уведомления
  - analytics-service – статистика активности
  - realtime-service – WebSocket/Server-Sent Events

### Frontend
- **Модуль:** `modules/progression/daily-quests`
- **State Store:** `useDailyQuestsStore`
- **State:** `dailyQuests`, `weeklyQuests`, `loginStreak`, `dailyRewards`, `timers`, `notifications`, `filters`
- **UI компоненты:** `DailyQuestList`, `WeeklyChallengeBoard`, `LoginStreakTracker`, `DailyRewardCalendar`, `QuestTimerBar`, `RewardClaimModal`
- **Формы:** `ClaimRewardForm`, `QuestFilterForm`, `NotificationPreferencesForm`
- **Layouts:** `ProgressionHubLayout`
- **Хуки:** `useDailyQuestTimers`, `useQuestRealtime`, `useLoginStreak`, `useDailyRewardClaim`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/gameplay/daily-quests/ui
# - Dependencies: auth, quest, reset, economy, notification, analytics, realtime
# - Frontend Module: modules/progression/daily-quests (useDailyQuestsStore)
# - UI: DailyQuestList, WeeklyChallengeBoard, LoginStreakTracker, DailyRewardCalendar, QuestTimerBar, RewardClaimModal
# - Forms: ClaimRewardForm, QuestFilterForm, NotificationPreferencesForm
# - Layout: ProgressionHubLayout
# - Hooks: useDailyQuestTimers, useQuestRealtime, useLoginStreak, useDailyRewardClaim
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать агрегированные DTO для вкладок Daily, Weekly, Login Streak, Rewards.
2. Добавить REST эндпоинты для получения списков заданий, streak состояния, календаря наград.
3. Реализовать операции claim rewards, reroll daily quest (если доступно), toggle notifications.
4. Определить WebSocket канал для событий: «quest progress», «quest completed», «streak updated», «reset timer tick».
5. Спроектировать фильтры и сортировки (по типу, сложности, наградам).
6. Обеспечить кэширование и таймеры (ETag, `Cache-Control`, `Retry-After` для сбросов).
7. Описать интеграцию с reset-service (получение расписаний), quest-service (прогресс), economy (награды).
8. Указать правила безопасности, лимиты, обработку ошибок.
9. Приложить примеры ответов, сценарии тестирования, выполнить чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/gameplay/daily-quests/ui/dashboard`** – сводка активностей (таймеры, counters, streak state).
2. **GET `/api/v1/gameplay/daily-quests/ui/daily`** – список ежедневных заданий: статус, прогресс, награды, время до сброса.
3. **GET `/api/v1/gameplay/daily-quests/ui/weekly`** – список еженедельных челленджей с прогрессом и наградами.
4. **POST `/api/v1/gameplay/daily-quests/ui/daily/{questId}/claim`** – получение награды за выполненный daily quest.
5. **POST `/api/v1/gameplay/daily-quests/ui/daily/{questId}/reroll`** – смена задания (при наличии токена/условий).
6. **GET `/api/v1/gameplay/daily-quests/ui/login-streak`** – информация о streak (current day, rewards, защитные механики).
7. **POST `/api/v1/gameplay/daily-quests/ui/login-streak/claim`** – получение награды за streak.
8. **GET `/api/v1/gameplay/daily-quests/ui/daily-reward`** – календарь наград (daily reward calendar, текущая награда).
9. **POST `/api/v1/gameplay/daily-quests/ui/daily-reward/claim`** – получение награды за вход.
10. **GET `/api/v1/gameplay/daily-quests/ui/notifications`** – настройки уведомлений по daily/weekly активности.
11. **POST `/api/v1/gameplay/daily-quests/ui/notifications`** – обновление предпочтений (channels, reminders).
12. **GET `/api/v1/gameplay/daily-quests/ui/history`** – история выполненных задач и полученных наград (пагинация).
13. **GET `/api/v1/gameplay/daily-quests/ui/recommendations`** – рекомендации по заданиям (смотри документ: «Suggested next quest»).
14. **GET `/api/v1/gameplay/daily-quests/ui/timers`** – детальные таймеры сбросов (daily, weekly, special events).
15. **WS `/api/v1/gameplay/daily-quests/ui/stream`** – WebSocket события: `quest-progress`, `quest-completed`, `reward-available`, `reset-countdown`, `streak-updated`.

---

## 🧱 Модели данных

- **DailyQuestSummary** – `id`, `title`, `description`, `type`, `difficulty`, `progress`, `goal`, `reward`, `status` (`AVAILABLE|COMPLETED|CLAIMED|LOCKED`), `remainingTime`.
- **WeeklyChallenge** – `id`, `category`, `objectives[]`, `progress`, `reward`, `expiresAt`, `bonusMultiplier`.
- **StreakInfo** – `currentDay`, `maxDay`, `isProtected`, `protectionCharges`, `nextReward`, `multiplier`, `lostAt`.
- **DailyRewardCalendar** – `days[]` (day, reward, claimed, bonus), `currentDay`, `nextResetAt`.
- **Reward** – `type`, `amount`, `itemId`, `currency`, `xp`, `boost`, `cosmeticId`.
- **NotificationPreferences** – `channels[]`, `reminders[]`, `quietHours`, `pushEnabled`.
- **HistoryEntry** – `questId`, `type`, `reward`, `completedAt`, `claimedAt`, `source` (`DAILY|WEEKLY|STREAK|LOGIN_REWARD`).
- **Recommendation** – `questId`, `reason`, `progress`, `rewardHighlight`.
- **ResetTimer** – `type` (`DAILY|WEEKLY|EVENT`), `resetsAt`, `secondsLeft`, `status` (`RUNNING|COMPLETED`).
- **RealtimeEvent** – union типы (progress, completed, rewardAvailable, streakUpdated, resetCountdown).
- **Error Schema (`DailyQuestUiError`)** – код (`QUEST_NOT_FOUND`, `QUEST_NOT_COMPLETED`, `REWARD_ALREADY_CLAIMED`, `REROLL_LIMIT`, `STREAK_BROKEN`, `NOTIFICATION_DISABLED`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth`; `ServiceToken` – для сервисов, отправляющих прогресс.
- Ограничения: лимит на reroll (день/неделя), защита streak (покупка за валюту), один claim в день.
- Таймеры: синхронизация с reset-service; использовать `Retry-After` в ответах при ожидании.
- Кэширование: ETag для списков, `Cache-Control: max-age=60` для таймеров, invalidate on reset event.
- Локализация: поддерживать `Accept-Language` / `locale` параметр.
- Доступность: возвращать `ariaLabels`, `descriptions` для карточек.
- Инциденты: подозрительные повторные claim отправлять в incident-service.
- Использовать общие компоненты (`responses.yaml`, `pagination.yaml`, `security.yaml`).

---

## 🧪 Примеры

- Dashboard с таймерами и списком daily/weekly задач.
- Claim награды за ежедневный квест с обновлением WebSocket.
- Обновление streak после пропуска дня с защитой streak.
- Получение календаря наград с отмеченными уже полученными днями.
- Подписка на stream и получение `reset-countdown`.

---

## 🔗 Связности и зависимости

- Зависит от API `API-TASK-141` (reset system) для корректного времени сбросов.
- Интегрируется с quest-engine для прогресса и выдачи наград.
- Использует economy-service для наград, notification-service для напоминаний.
- События стрима публикуются через realtime-service.

---

## ✅ Критерии приемки

1. Файл `daily-quests-ui.yaml` создан, содержит архитектурный комментарий, REST и WS разделы.
2. Эндпоинты покрывают все UI сценарии: daily/weekly список, streak, rewards, notifications, timers.
3. Описаны модели данных, события, ошибки и ограничения (reroll, streak protection).
4. Проработаны правила кэширования, локализации, accessibility.
5. WebSocket канал описывает типы событий и payload.
6. Указаны зависимости от reset/quest/economy/notification сервисов.
7. Добавлены примеры запросов/ответов и сценарии тестирования.
8. Выполнен чеклист `tasks/config/checklist.md`.

---

## 📎 Checklist

- [ ] Заполнен шаблон `api-generation-task-template.md`
- [ ] Прописаны микросервис, frontend модуль, зависимости, компоненты
- [ ] Эндпоинты и WS покрывают функционал документа
- [ ] Есть модели данных, ошибки, ограничения, примеры, критерии
- [ ] После сохранения обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Зачем отдельный UI-файл, если есть core reset API?
**A:** Core описывает систему сбросов, но UI требует агрегированных данных по заданиям, календарю наград, streak и realtime событиям – это отдельный контракт.

**Q:** Где обрабатывается защита streak?
**A:** В core прогрессии; UI API только отображает состояние и позволяет активировать защиту через соответствующие эндпоинты.

**Q:** Можно ли объединить daily и weekly в один эндпоинт?
**A:** Для оптимизации UI потоков данные разделены, но dashboard объединяет ключевые показатели.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

