# Task ID: API-TASK-338
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** completed  
**Создано:** 2025-11-08 18:50  
**Создатель:** AI Task Creator Agent  
**Зависимости:** [API-TASK-318], [API-TASK-317], [API-TASK-319]

---

## 📋 Краткое описание

Подготовить спецификацию `Player Orders Reviews API`, которая описывает создание, хранение и выдачу отзывов/рейтинг-пояснений для системы заказов, включая модерацию, флаги и выборки для UI.  
**Целевой файл:** `api/v1/social/player-orders/reviews.yaml`

---

## 🎯 Цель задания

Обеспечить social-service API, позволяющий:
- создавать и модерать отзывы между заказчиками и исполнителями после завершения заказа;
- выдавать отзывные выборки для профилей игроков, заказов, категорий и аналитики;
- управлять флагами (позитив/нейтрал/негатив), жалобами и арбитражем;
- синхронизировать отзывы с рейтингами (`ratings.yaml`) и новостными/социальными блоками.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 09:55  
**Статус:** approved (api-readiness: ready)

**Что важно из документа:**
- Разделы 2–7: метрики исполнителей/заказчиков, флаги, жалобы, санкции, decay/boost.  
- Раздел 4: отзывы, шкалы оценок, флаги, модерация, арбитраж.  
- Раздел 12: REST макет (endpoints для отзывов, рейтингов, санкций).  
- JSON схемы: `PlayerOrderReview`, `PlayerOrderPenalty`, `PlayerOrderCategoryThresholds`.  
- Kafka события: `social.player-orders.review.created`, `social.player-orders.penalty.applied`, `social.player-orders.rating.updated`.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — цикл заказа, публикация.  
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` — бизнес-процесс, статусная модель.  
- `.BRAIN/02-gameplay/social/relationships-system-детально.md` — социальное доверие.  
- `.BRAIN/05-technical/backend/matchmaking/matchmaking-rating.md` — интеграция с матчмейкинг-рейтинго м.  
- `API-SWAGGER/api/v1/social/player-orders/ratings.yaml` — базовая спецификация рейтингов.

---

## 📁 Целевая структура API

**Файл:** `api/v1/social/player-orders/reviews.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── player-orders/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── reviews.yaml  ← создать/обновить
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис):
- **Микросервис:** social-service (порт 8084)  
- **Интеграции:** economy-service (вознаграждения/штрафы), narrative-service (социальные события), analytics-service (метрики), notification-service (уведомления).  
- **Kafka:** `social.player-orders.review.created`, `social.player-orders.review.flagged`, `social.player-orders.penalty.applied`, `social.player-orders.rating.updated`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль):
- **Модуль:** modules/social/player-orders/reviews  
- **State Store:** `useSocialStore(playerOrders)`  
- **UI:** `ReviewList`, `ReviewSummaryCard`, `FlagBadge`, `DisputeBanner`, `PaginationControls`  
- **Формы:** `ReviewSubmitForm`, `ReviewFilterForm`, `FlagReviewForm`  
- **Layouts:** `PlayerOrdersLayout`, `PlayerProfileLayout`  
- **Хуки:** `useReviewsQuery`, `useReviewSubmission`, `useReviewModeration`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/player-orders/reviews
# - State Store: useSocialStore(playerOrders)
# - UI: ReviewList, ReviewSummaryCard, FlagBadge, DisputeBanner, PaginationControls
# - Forms: ReviewSubmitForm, ReviewFilterForm, FlagReviewForm
# - Layouts: PlayerOrdersLayout, PlayerProfileLayout
# - Hooks: useReviewsQuery, useReviewSubmission, useReviewModeration
# - Events: social.player-orders.review.created, social.player-orders.review.flagged, social.player-orders.penalty.applied
# - API Base: /api/v1/social/player-orders/*
```

---

## ✅ План работ

1. **Извлечь требования из документа:** структуры отзывов, шкалы оценок, флаги, арбитраж, жалобы.  
2. **Проектирование моделей:** `PlayerOrderReview`, `ReviewScore`, `ReviewFlag`, `ReviewSummary`, `ReviewFilter`, `ReviewModerationAction`.  
3. **Сформировать endpoints:**  
   - Создание/обновление/удаление (soft-delete) отзывов.  
   - Получение отзывов по игроку, заказу, категории, диапазону дат.  
   - Получение агрегатов (средние оценки, количество флагов).  
   - Управление флагами и жалобами.  
   - Интеграция с рейтингами (trigger recalculation).  
4. **Подключить общие компоненты:** `shared/common/security`, `shared/common/responses`, `shared/common/pagination`.  
5. **Документировать Kafka события и связи с рейтингами.**  
6. **Подготовить примеры:** позитивный отзыв, негативный с жалобой, выборка для профиля, список по заказу, модерирование.  
7. **Прогнать `scripts/validate-swagger.ps1`, убедиться, что файл ≤400 строк (компоненты вынести).**

---

## 🔌 Эндпоинты

1. **POST `/social/player-orders/reviews`** — создание/обновление отзыва (идемпотентно по `orderId` + `authorId`).  
2. **GET `/social/player-orders/reviews`** — выборка с фильтрами (`playerId`, `role`, `rating`, `flag`, `period`, `category`, `page`, `pageSize`).  
3. **GET `/social/player-orders/reviews/{reviewId}`** — получение конкретного отзыва.  
4. **POST `/social/player-orders/reviews/{reviewId}/flags`** — установка/обновление флага, жалоб.  
5. **DELETE `/social/player-orders/reviews/{reviewId}`** — soft-delete (архивирование).  
6. **GET `/social/player-orders/reviews/summary/{playerId}`** — агрегаты (средние оценки, количество отзывов, флаги).  
7. **POST `/social/player-orders/reviews/moderation`** — массовое модерирование (approve/reject/ban).  
8. **POST `/social/player-orders/reviews/recalculate`** — ручной триггер пересчёта резюме/рейтингов (async job).

---

## 🧱 Модели данных

- **PlayerOrderReview** — `reviewId`, `orderId`, `authorId`, `targetId`, `role`, `scores`, `comment`, `flags`, `createdAt`, `updatedAt`, `status`.  
- **ReviewScore** — `communication`, `quality`, `timeliness`, `professionalism`, `overall` (1–5).  
- **ReviewFlag** — `type` (positive/neutral/negative), `reason`, `notes`, `moderationStatus`.  
- **ReviewSummary** — средние показатели, количество отзывов, распределение по категориям.  
- **ReviewFilter** — фильтры для выборки (role, category, minScore, flaggedOnly).  
- **ReviewModerationAction** — `reviewId`, `action` (approve/reject/escalate), `moderatorId`, `comment`.  
- **ReviewRecalculateJob** — `jobId`, `status`, `progress`, `startedAt`, `finishedAt`.  
- **PaginatedReviewList** — стандартная пагинация (использовать `shared/common/pagination`).

Каждая схема должна иметь описания, валидацию и примеры.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; файл ≤400 строк (компоненты вынести).  
- Использовать shared security/responses/pagination.  
- Ошибки с `x-error-code`: `VAL_INVALID_REVIEW`, `BIZ_REVIEW_DUPLICATE`, `BIZ_REVIEW_NOT_FOUND`, `BIZ_REVIEW_FLAG_CONFLICT`, `INT_MODERATION_PIPELINE_FAILURE`.  
- `info.description` ссылается на `.BRAIN` документы и дату.  
- Добавить `tags` (Reviews, Moderation).  
- Зависимости от `ratings.yaml` и `economy/player-orders` указать в `x-related-apis`.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/player-orders/reviews.yaml` создан и проходит `scripts/validate-swagger.ps1`.  
2. В начале файла указан блок `Target Architecture`.  
3. Реализованы endpoints для CRUD отзывов, флагов, агрегатов и пересчёта.  
4. Схемы `PlayerOrderReview`, `ReviewScore`, `ReviewFlag`, `ReviewSummary`, `ReviewModerationAction`, `ReviewRecalculateJob` описаны.  
5. Подключены общие компоненты безопасности и ошибок.  
6. Kafka события и взаимосвязь с рейтингами задокументированы.  
7. Примеры включают позитивный/негативный отзыв, жалобу, summary.  
8. README в `social/player-orders` дополнен ссылкой (в рамках реализации).  
9. Task отражён в `brain-mapping.yaml` (выполнено текущим таском).  
10. Документация подчёркивает интеграцию с арбитражом и санкциями.

---

## ❓ FAQ

**Q:** Как обрабатывать дублирующиеся отзывы?  
A: Использовать идемпотентный ключ (`orderId` + `authorId`); при повторе возвращать `409 BIZ_REVIEW_DUPLICATE`.

**Q:** Что делать с токсичными комментариями?  
A: Включить модерацию (`ReviewFlag`, `moderationStatus`) и интеграцию с антиспам-сервисом (`notification/moderation-service`).  

**Q:** Нужно ли хранить историю правок?  
A: Поддержать `status` (active/archived/removed) и `updatedAt`; историю можно хранить во внутреннем audit log (вне scope API).

**Q:** Как связать отзыв с рейтингом?  
A: При создании/обновлении вызывать асинхронный job пересчёта (`/recalculate`) или публикацию события `social.player-orders.rating.updated`.

---

**Следующие шаги исполнителя:** реализовать спецификацию, вынести схемы/примеры, обновить README, прогнать валидацию и линтеры.

---

## 📌 История выполнения

- 2025-11-08 – создано задание по `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md`, статус `queued`.
- 2025-11-08 – АПИТАСК активировал задачу, старт разработки OpenAPI `api/v1/social/player-orders/reviews.yaml` (`status: in_progress`).
- 2025-11-08 – Подготовлена и провалидирована спецификация `api/v1/social/player-orders/reviews.yaml`, задача выполнена (`status: completed`).

