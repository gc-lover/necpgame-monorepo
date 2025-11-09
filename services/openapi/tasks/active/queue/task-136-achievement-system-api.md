# Task ID: API-TASK-136
**Тип:** API Generation  
**Приоритет:** средний  
**Статус:** queued  
**Создано:** 2025-11-07 10:32  
**Создатель:** AI Agent  
**Зависимости:** none

---

## 📋 Краткое описание
Спроектировать OpenAPI для системы достижений: определения целей, прогресс, награды и уведомления.

**Что нужно сделать:** подготовить спецификацию world-service для `.BRAIN/05-technical/backend/achievement-system.md`.

---

## 🎯 Цель задания
Дать контракт, который обрабатывает события прогресса, рассчитывает очки и выдаёт награды/титулы, синхронизируя UI и аналитические системы.

**Зачем это нужно:**
- Мотивация игроков и удержание через цели и награды.  
- Поддержка кросс-сервисных достижений (combat, social, economy).  
- Интеграция с прогрессией, уведомлениями и монетизацией.

---

## 📚 Источники информации

### Основной источник
**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/05-technical/backend/achievement-system.md`  
**Версия:** v1.0.0  
**Обновлено:** 2025-11-07  
**Статус:** ready  

**Ключевые детали:**
- Категории (combat, social, exploration, economy, quests).  
- Редкости (common → legendary), очки, титулы, косметические награды.  
- Событийная модель (подписка на combat, quests, trade и т.д.).

### Дополнительные источники
- `.BRAIN/05-technical/backend/progression-backend.md` — начисление опыта.  
- `.BRAIN/05-technical/backend/notification-system.md` — уведомления об ачивках.  
- `.BRAIN/05-technical/backend/analytics-data-lake.md` — аналитика достижений.

### Связанные документы
- `.BRAIN/02-gameplay/progression/achievement-categories.md` — структура категорий.  
- `.BRAIN/02-gameplay/social/player-reputation.md` — влияние достижений.  
- `.BRAIN/05-technical/backend/event-bus-overview.md` — список событий.

---

## 📁 Целевая структура API
### Репозиторий: `API-SWAGGER`
**Целевой файл:** `api/v1/world/achievements/achievement-system.yaml`  
> ⚠️ Серверы: `https://api.necp.game/v1/world` и `http://localhost:8080/api/v1/world`.

**Тип:** OpenAPI 3.0.3  
**Версия:** v1

```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── achievements/
                └── achievement-system.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** world-service  
- **Порт:** 8086  
- **API Base:** `/api/v1/world/achievements`  
- **Интеграции:** gameplay-service (combat/quests), social-service (friend achievements), economy-service (wealth), notification-service, analytics-service.  
- **Комментарий в спецификации:**
  ```yaml
  # Target Architecture:
  # - Microservice: world-service (port 8086)
  # - API Base: /api/v1/world/achievements
  # - Dependencies: gameplay-service, social-service, economy-service, notification-service, analytics-service
  # - Frontend Module: modules/progression/achievements
  # - UI: AchievementGrid, AchievementCard, RewardModal
  # - Hooks: useProgressionStore, useRealtime, useAnalytics
  ```

### OpenAPI требования
- `info.x-microservice`:
  ```yaml
  x-microservice:
    name: world-service
    port: 8086
    domain: world
    base-path: /api/v1/world/achievements
    directory: api/v1/world/achievements
    package: com.necpgame.worldservice
  ```
- `servers` как указано выше.  
- `x-websocket`: `wss://api.necp.game/v1/world/achievements/stream/{characterId}` (уведомления, прогресс).

### Frontend
- **Модуль:** `modules/progression/achievements`.  
- **State Store:** `useProgressionStore` (`achievements`, `progress`, `recentUnlocks`).  
- **UI:** AchievementGrid, AchievementCard, RewardModal, CategoryTabs, PointsCounter.  
- **Формы:** AchievementFilterForm, RewardClaimForm (если требуется подтверждение).  
- **Хуки:** useRealtime, useAnalytics, useFilters.  
- **Layouts:** GameLayout (прогрессия/профиль игрока).

---

## ✅ Что нужно сделать

### Шаг 1. Анализ потребностей
- Зафиксировать структуру achievement definition (условия, награды, редкость).  
- Список событий, запускающих обновление прогресса.  
- Политика начисления очков и синхронизация с прогрессией.

### Шаг 2. Спроектировать endpoints
1. **GET `/api/v1/world/achievements`** — справочник достижений с фильтрами (category, rarity).  
2. **GET `/api/v1/world/achievements/{achievementId}`** — детальная карточка.  
3. **GET `/api/v1/world/achievements/me`** — прогресс и состояние игрока.  
4. **GET `/api/v1/world/achievements/recent`** — последние разблокировки (по аккаунту/глобально).  
5. **POST `/api/v1/world/achievements/progress`** — внутренний endpoint для событий (service token).  
6. **POST `/api/v1/world/achievements/reward/{achievementId}/claim`** — выдача награды (если требуется подтверждение).  
7. **GET `/api/v1/world/achievements/categories`** — категории/статистика.  
8. **GET `/api/v1/world/achievements/stats`** — общие показатели (для UI/аналитики).  
9. **POST `/api/v1/world/achievements/sync`** — синхронизация из оффлайн-очереди (fallback).

### Шаг 3. Модели
- `AchievementDefinition`, `AchievementRequirement`, `AchievementReward`, `AchievementProgress`, `AchievementCategoryStats`, `RecentUnlock`.  
- Ошибки: `AchievementError` (`VAL_ALREADY_COMPLETED`, `VAL_INVALID_EVENT`, `BIZ_REWARD_ALREADY_CLAIMED`).  
- WebSocket события: `achievementProgressed`, `achievementUnlocked`, `achievementRewardClaimed`.

### Шаг 4. OpenAPI оформление
- В `paths` описать все запросы, указать параметры (`achievementId`, фильтры).  
- Использовать `shared/common/responses.yaml` и `shared/common/security.yaml`.  
- Добавить `BearerAuth` и `ServiceToken` (для внутренних событий).  
- Примеры: получение списка с фильтрами, событие прогресса, выдача награды.  
- Схемы и enum вынести в `components`.

### Шаг 5. Проверки
- Запустить `scripts/validate-swagger.ps1 -ApiDirectory API-SWAGGER/api/v1/world/achievements/`.  
- Убедиться, что лимит файла ≤ 400 строк, структура соответствует архитектуре.  
- Обновить `brain-mapping.yaml`, документ `.BRAIN` и README в каталоге `world/achievements`.

---

## 🔍 Критерии приемки
1. `info.x-microservice` заполнен (`world-service`, 8086, `world`).  
2. Все пути начинаются с `/api/v1/world/achievements`.  
3. Поддерживаются категории, редкости, очки, награды и статистика.  
4. Реализован внутренний endpoint для событий прогресса (`ServiceToken`).  
5. Предусмотрены данные для UI (filters, category stats, recent unlocks).  
6. Ошибки используют общую модель `Error`.  
7. Примеры запросов/ответов и WebSocket payload присутствуют.  
8. Валидаторы (`swagger-cli`, проектный скрипт) проходят без ошибок.  
9. Обновлены brain-mapping и `.BRAIN` с новым путём файла.  
10. README каталога `world/achievements` описывает API и основные сценарии.  
11. Учтена аналитика (возврат данных для дэшбордов).

---

## FAQ
- **Как обрабатываются мульти-этапные ачивки?** Через массив требований и частичный прогресс, отражённый в `AchievementProgress`.  
- **Можно ли вручную выдать достижение?** Через internal endpoint с `ServiceToken`.  
- **Как хранить массовые события?** Использовать батчи/очереди, описать в секции интеграции.  
- **Нужно ли кэширование?** Да, добавить примечание про Redis/Materialized views.  
- **Что с локализацией?** Возвращать идентификаторы локализуемых строк (`titleKey`, `descriptionKey`).

---

**Источник:** `.BRAIN/05-technical/backend/achievement-system.md` (v1.0.0, ready)

