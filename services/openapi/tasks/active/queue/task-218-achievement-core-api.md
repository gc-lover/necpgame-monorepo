# Task ID: API-TASK-218
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:38
**Создатель:** GPT-5 Codex (API Task Creator)
**Зависимости:** API-TASK-136, API-TASK-217, API-TASK-214

---

## 📋 Краткое описание

Разработать OpenAPI спецификацию ядра системы достижений: типы достижений, категории, награды, значение очков и общие правила.

**Что нужно сделать:** Создать `api/v1/achievements/achievement-core.yaml`, описав модели достижений, категории, награды, rarities и каталог.

---

## 🎯 Цель задания

Сформировать центральный API для управления достижениями, обеспечивающий игровым системам доступ к каталогу и атрибутам достижений.

**Зачем это нужно:**
- Предоставить единый источник правды о достижениях (описания, категории, награды)
- Поддержать UI/analytics/quest системы данными о достижениях
- Связать ядро с трекингом и выдачей наград (задачи Part 2, Part 3)
- Ввести стандарты rarities, очков, коллекций, зависимостей

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/05-technical/backend/achievement/achievement-core.md`
**Версия:** v1.0.0 (2025-11-07 01:59)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- Структура `AchievementDefinition` (id, category, rarity, points, rewards)
- Категории, подкатегории, meta achievements, hidden achievements
- Reward types (titles, cosmetics, currencies, perks)
- Rarity tiers и шкала очков
- Achievement tree (dependencies, series)
- Localization, tags, storytelling

### Дополнительные источники

- `.BRAIN/05-technical/backend/achievement/achievement-tracking.md`
- `.BRAIN/05-technical/backend/achievement/achievement-examples-api.md`
- `.BRAIN/05-technical/backend/progression-backend.md`
- `.BRAIN/05-technical/backend/economy-system.md`
- `.BRAIN/05-technical/backend/notification-system.md`

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-136-achievement-system-api.md`
- `API-SWAGGER/tasks/active/queue/task-209-achievement-ui-api.md`
- `API-SWAGGER/tasks/active/queue/task-210-daily-quests-ui-api.md`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/achievements/achievement-core.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/achievements/
 ├── achievement-core.yaml        ← создать/заполнить
 ├── achievement-tracking.yaml    (будущая задача Part 2)
 └── achievement-rewards.yaml     (будущая задача Part 3)
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend
- **Микросервис:** gameplay-service (achievements module)
- **Порт:** 8083
- **API Base Path:** `/api/v1/achievements`
- **Зависимости:**
  - auth-service – доступ по аккаунту
  - progression-service – влияние на XP/points
  - economy-service – выдача валют/предметов
  - notification-service – уведомления о разблокировках
  - analytics-service – статистика достижений

### Frontend
- **Модуль:** `modules/progression/achievements`
- **State Store:** `useProgressionStore`
- **State:** `achievementCatalog`, `categories`, `rarityTable`, `metaCollections`, `rewards`
- **UI компоненты:** `AchievementCatalogTable`, `CategoryTree`, `RarityLegend`, `RewardPreview`, `AchievementDependencyGraph`
- **Формы:** `AchievementSearchForm`, `FilterPresetForm`
- **Layouts:** `ProgressionHubLayout`
- **Хуки:** `useAchievementCatalog`, `useAchievementFilters`, `useRarityLegend`

### Комментарий для YAML

```yaml
# Target Architecture:
# - Microservice: gameplay-service (port 8083)
# - API Base: /api/v1/achievements
# - Dependencies: auth, progression, economy, notification, analytics
# - Frontend Module: modules/progression/achievements (useProgressionStore)
# - UI: AchievementCatalogTable, CategoryTree, RarityLegend, RewardPreview, AchievementDependencyGraph
# - Forms: AchievementSearchForm, FilterPresetForm
# - Hooks: useAchievementCatalog, useAchievementFilters, useRarityLegend
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать схемы achievement definitions, categories, rarities, dependencies.
2. Добавить эндпоинты для каталога, поиска, фильтрации, rarity/points таблиц.
3. Подготовить схемы наград (title, cosmetic, currency, perk) и связи.
4. Ввести API meta-achievements и коллекций, hidden achievements.
5. Настроить версии и локализацию (locale, fallback).
6. Описать event bus события для каталога (`achievement:created`, `achievement:updated`).
7. Прописать кэширование, пагинацию, теги, аудит изменений.
8. Подготовить примеры и тестовые сценарии; выполнить чеклист.

---

## 🔀 Endpoints

1. **GET `/api/v1/achievements/catalog`** – полный каталог достижений (пагинированный, фильтры).
2. **GET `/api/v1/achievements/catalog/{achievementId}`** – детальная карточка.
3. **GET `/api/v1/achievements/categories`** – дерево категорий и подкатегорий.
4. **GET `/api/v1/achievements/rarities`** – таблица rarities, пороги очков, цвета, награды.
5. **GET `/api/v1/achievements/meta`** – meta achievements, коллекции, зависимости.
6. **GET `/api/v1/achievements/rewards`** – список типов наград, ссылки на inventory/cosmetic ids.
7. **GET `/api/v1/achievements/search`** – поиск по имени, тегам, категориям, reward типу.
8. **POST `/api/v1/achievements/filter-presets`** – сохранить/обновить пользовательские фильтры/представления.
9. **GET `/api/v1/achievements/filter-presets`** – получить сохранённые пресеты.
10. **POST `/api/v1/achievements/catalog`** – (GM/LiveOps) создать новое достижение.
11. **PUT `/api/v1/achievements/catalog/{achievementId}`** – обновить существующее достижение.
12. **DELETE `/api/v1/achievements/catalog/{achievementId}`** – архивировать достижение (soft delete).
13. **GET `/api/v1/achievements/versions`** – информация о версиях каталога (для кэша, DLC).
14. **POST `/api/v1/achievements/catalog/import`** – импорт/массовое обновление (GM tools, audit).
15. **WS `/api/v1/achievements/catalog/stream`** – события: `achievement-created`, `achievement-updated`, `achievement-archived`, `catalog-version-changed`.

---

## 🧱 Модели данных

- **AchievementDefinition** – `id`, `name`, `slug`, `category`, `rarity`, `points`, `description`, `lore`, `tags[]`, `isHidden`, `requirements`, `rewards[]`, `metaGroupId`, `dependencies[]`, `version`, `createdAt`, `updatedAt`.
- **AchievementCategory** – `id`, `name`, `parentId`, `order`, `icon`, `description`.
- **AchievementReward** – `rewardType` (`TITLE|COSMETIC|CURRENCY|PERK|ITEM`), `payload`, `preview`, `deliveryMethod`.
- **RarityDefinition** – `rarity`, `color`, `scoreMultiplier`, `unlockNotification`, `badgeAsset`.
- **MetaAchievement** – `metaId`, `title`, `requiredAchievements[]`, `reward`, `progressType` (`ALL|COUNT|POINTS`).
- **FilterPreset** – `presetId`, `name`, `filters`, `sort`, `layout`, `isDefault`.
- **CatalogVersion** – `version`, `checksum`, `releasedAt`, `notes`, `compatibleClientVersions`.
- **RealtimeEventPayload** – payload для событий создания/обновления/архивирования.
- **Error Schema (`AchievementCoreError`)** – codes (`ACHIEVEMENT_NOT_FOUND`, `CATEGORY_NOT_FOUND`, `NAME_CONFLICT`, `RARITY_INVALID`, `DEPENDENCY_LOOP`, `VERSION_CONFLICT`).

---

## 🧭 Принципы и правила

- Авторизация: `BearerAuth` для чтения; `ServiceToken/GMAuthorization` для CRUD операций.
- Кэширование: каталог с `Cache-Control: max-age=60`, `ETag`; WebSocket стрим инвалидаций.
- Локализация: поддержка `Accept-Language` и fallback на `en-US`.
- Версионирование: `catalogVersion` в ответах, опциональное `If-None-Match`.
- Audit: CRUD операции требуют `X-Audit-Id`, ведётся журнал.
- Инциденты: циклы зависимостей/невалидные награды → incident-service.
- DRY: использовать общие компоненты `responses.yaml`, `pagination.yaml`, `security.yaml`.

---

## 🧪 Примеры

- Каталог достижений с фильтром по rare+ и категории `exploration`.
- Meta achievement «Master Explorer» с зависимостями и наградой.
- WebSocket сообщение о добавлении нового достижения.
- Создание нового достижения GM-ом через import.
- Версионирование каталога для клиента (checksum).

---

## 🔗 Связности и зависимости

- Используется трекингом (API-TASK-136) и UI (API-TASK-209).
- Интеграция с economy/cosmetic для наград, progression для очков.
- События каталога запускают обновление кешей UI, analytics, notification.

---

## ✅ Критерии приемки

1. Файл `achievement-core.yaml` создан с архитектурным комментарием и полным набором эндпоинтов/событий.
2. Модели данных описывают дефиниции достижений, категории, награды, rarities, мета.
3. Прописаны правила кэширования, версионирования, локализации и аудита.
4. Добавлены примеры, тестовые сценарии и чеклист.

---

## 📎 Checklist

- [ ] Использован шаблон `api-generation-task-template.md`
- [ ] Определены микросервис, модуль, зависимости, UI компоненты
- [ ] Эндпоинты и события покрывают требования документа
- [ ] Добавлены модели, ошибки, примеры, критерии
- [ ] После сохранения обновить `tasks/config/brain-mapping.yaml`

---

## ❓FAQ

**Q:** Как отличить hidden achievements в API?**
**A:** Поле `isHidden=true` возвращается только для GM/ServiceToken; игрокам выдаются ограниченные данные/placeholder до разблокировки.

**Q:** Можно ли изменять награды?**
**A:** Да, через PUT/IMPORT с audit; требуется сверка с economy-service и уведомление analytics.

**Q:** Нужны ли зависимости при создании?**
**A:** Не обязательно, но система проверяет отсутствие циклов и валидность ссылок.



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

