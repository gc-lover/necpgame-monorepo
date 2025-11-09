# Task ID: API-TASK-348
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:25  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-343, API-TASK-344, API-TASK-346 (использует программы, контракты, эффекты)

---

## 📋 Краткое описание

Разработать спецификацию `Mentorship News & Highlights API`, публикующую новости, истории успеха, кризисы и уведомления наставничества.  
**Целевой файл:** `api/v1/social/mentorship/news.yaml`

---

## 🎯 Цель задания

Обеспечить social-service API, которое:
- предоставляет ленту новостей наставничества (выпускники, события, гранты, кризисы, медиа-стримы);  
- публикует хайлайты и уведомления для игроков, фракций и медиа;  
- синхронизируется с world-service (events, effects), economy-service (grants) и notification-service;  
- поддерживает фильтры, подписки, аналитику и интеграцию с фронтенд-модулями.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/mentorship-world-impact-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:33  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §1, §5: социальные экосистемы, события, медиа-покрытие, истории успеха.  
- §8: UX и визуализация (журнал событий, VR-галерея, уведомления).  
- §9: REST макеты (`GET /social/mentorship/news`, `POST /social/mentorship/highlights`).  
- §10: Kafka события `social.mentorship.news`.  
- §11: метрики (MentorSatisfactionScore, ContentModerationLatency).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/mentorship-system-детально.md` — программы, контракты, рейтинги наставников.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — визуальные элементы и стилистика.  
- `.BRAIN/05-technical/content-generation/mentorship-content-pipeline.md` — модерация и публикация контента.  
- `.BRAIN/02-gameplay/social/player-orders-reputation-детально.md` — система отзывов и рейтингов.  
- `.BRAIN/05-technical/telemetry/social-analytics-pipeline.md` — трекинг новостей и реакций.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/mentorship/news.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── mentorship/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── news.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** world-service (events/effects), economy-service (grants), notification-service (push/alerts), content-service (VR-материалы), analytics-service (engagement), moderation-service.  
- **Kafka:** `social.mentorship.news`, `social.mentorship.highlight.published`, `world.mentorship.crisis`, `economy.mentorship.index`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/mentorship  
- **State Store:** `useSocialStore(mentorshipNews)`  
- **UI:** `MentorshipNewsFeed`, `MentorshipHighlightCarousel`, `MentorshipCrisisTicker`, `MentorSpotlightCard`, `MentorshipMediaGallery`  
- **Формы:** `MentorshipStorySubmissionForm`, `MentorshipHighlightPublishForm`, `MentorshipSubscriptionForm`  
- **Layouts:** `MentorshipNewsroomLayout`, `MentorshipHighlightsLayout`  
- **Hooks:** `useMentorshipNews`, `useMentorshipHighlights`, `useMentorshipSubscriptions`, `useMentorshipMedia`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/mentorship
# - State Store: useSocialStore(mentorshipNews)
# - UI: MentorshipNewsFeed, MentorshipHighlightCarousel, MentorshipCrisisTicker, MentorSpotlightCard, MentorshipMediaGallery
# - Forms: MentorshipStorySubmissionForm, MentorshipHighlightPublishForm, MentorshipSubscriptionForm
# - Layouts: MentorshipNewsroomLayout, MentorshipHighlightsLayout
# - Hooks: useMentorshipNews, useMentorshipHighlights, useMentorshipSubscriptions, useMentorshipMedia
# - Events: social.mentorship.news, social.mentorship.highlight.published, world.mentorship.crisis, economy.mentorship.index
# - API Base: /api/v1/social/mentorship/*
```

---

## ✅ Детальный план

1. **Определить сущности контента:** новости, хайлайты, кризисы, интервью, медиа-материалы.  
2. **Спроектировать схемы:** `MentorshipNewsItem`, `MentorshipHighlight`, `MentorshipCrisis`, `MentorshipMediaAsset`, `MentorshipNewsFilter`, `MentorshipSubscription`, `MentorshipNewsAnalytics`.  
3. **Разработать эндпоинты:** получение ленты, публикация, модерация, подписки, аналитика, поиск, загрузка медиа ссылок.  
4. **Модерирование:** предусмотреть статусы контента (`pending`, `approved`, `rejected`, `archived`).  
5. **Интеграции:** ссылки на программы, контракты, события академий, экономические метрики.  
6. **Kafka:** описать новостной поток, хайлайты, кризисы, модерацию (`mentorship-story-review`).  
7. **Примеры:** история успеха выпускника, VR-трансляция, кризис академии, дайджест недели, уведомление.  
8. **Shared components:** security/responses/pagination; вынести схемы/примеры.  
9. **Ошибки и бизнес-правила:** лимиты публикаций, нарушения модерации, права доступа.  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **GET `/social/mentorship/news`** — лента новостей с фильтрами (тип, теги, регион, академия, период, криза).  
2. **GET `/social/mentorship/news/{newsId}`** — детальная карточка (контент, источники, метрики).  
3. **POST `/social/mentorship/news`** — публикация новости (с модерацией).  
4. **PATCH `/social/mentorship/news/{newsId}`** — обновление/статусы (`approve`, `reject`, `feature`).  
5. **DELETE `/social/mentorship/news/{newsId}`** — архивирование/удаление.  
6. **POST `/social/mentorship/highlights`** — выпуск хайлайта (истории успеха, награды).  
7. **GET `/social/mentorship/highlights`** — каталог хайлайтов.  
8. **GET `/social/mentorship/crises`** — текущие кризисы наставничества (alert feed).  
9. **POST `/social/mentorship/crises/{crisisId}/ack`** — подтверждение обработки.  
10. **GET `/social/mentorship/subscriptions`** — подписки пользователей/фракций.  
11. **POST `/social/mentorship/subscriptions`** — управление подписками.  
12. **GET `/social/mentorship/media`** — медиа-ассеты (VR, видео, галереи).  
13. **POST `/social/mentorship/media`** — добавление медиа ссылок (через content-service).  
14. **GET `/social/mentorship/news/analytics`** — метрики вовлечённости.

---

## 🧱 Модели данных

- **MentorshipNewsItem** — `newsId`, `title`, `summary`, `content`, `tags[]`, `regionId`, `academyId`, `eventRefs[]`, `publishStatus`, `publishedAt`, `author`, `media[]`, `engagementMetrics`.  
- **MentorshipHighlight** — `highlightId`, `title`, `story`, `participants`, `awards`, `linkedPrograms[]`, `featuredUntil`.  
- **MentorshipCrisis** — `crisisId`, `severity`, `regionId`, `academyId`, `description`, `recommendedActions`, `status`, `issuedAt`.  
- **MentorshipMediaAsset** — `mediaId`, `type`, `contentId`, `thumbnail`, `duration`, `format`, `accessLevel`.  
- **MentorshipSubscription** — `subscriptionId`, `subscriberId`, `subscriberType`, `preferences`, `channels`, `status`.  
- **MentorshipNewsFilter** — фильтры API (search, tags, roles, severity, timeframe).  
- **MentorshipNewsAnalytics** — `period`, `views`, `shares`, `reactions`, `topTags`, `mentorSpotlight`, `crisisCount`.  
- **PaginatedMentorshipNews** — стандартная пагинация; аналогично для highlights/media.  
- **MentorshipNewsModerationAction** — `actionId`, `newsId`, `moderatorId`, `result`, `comment`, `timestamp`.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, компоненты вынести.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_MENTORSHIP_NEWS_INVALID`, `BIZ_MENTORSHIP_NEWS_MODERATION_REQUIRED`, `BIZ_MENTORSHIP_SUBSCRIPTION_CONFLICT`, `BIZ_MENTORSHIP_MEDIA_UNAVAILABLE`, `INT_MENTORSHIP_NEWS_PIPELINE_FAILURE`.  
- `info.description` — указать `.BRAIN` источники и UX подтверждения.  
- Теги: `Mentorship`, `News`, `Highlights`, `Crises`, `Media`.  
- Документировать Kafka (`social.mentorship.news`, `social.mentorship.highlight.published`, `world.mentorship.crisis`) и очереди `mentorship-story-review`.  
- Указать интеграцию с notification-service (push канал).

---

## ✅ Критерии приемки

1. Файл `api/v1/social/mentorship/news.yaml` создан/обновлён, проходит `scripts/validate-swagger.ps1`.  
2. Целевой YAML содержит `Target Architecture` блок.  
3. Реализованы все эндпоинты, схемы и примеры.  
4. Подключены shared security/responses/pagination.  
5. Документированы модерация, подписки, кризисы и интеграции.  
6. Добавлены примеры (история успеха, кризис, уведомление, подписка).  
7. Kafka события и очередь модерации описаны.  
8. README в каталоге обновлён (в рамках реализации).  
9. Task отражён в `brain-mapping.yaml`.  
10. `.BRAIN` документ обновлён (API Tasks Status).  
11. Указаны зависимости на `mentorship/programs.yaml`, `mentorship/contracts.yaml`, `mentorship/effects.yaml`, `economy/mentorship/index.yaml`.

---

## ❓ FAQ

**Q:** Кто может публиковать новости?  
A: Наставники (при наличии прав), редакторы social-service, автоматические пайплайны; требуется модерация.  

**Q:** Поддерживаем ли пользовательские истории?  
A: Да, через `MentorshipStorySubmissionForm`; контент проходит модерацию (`pending` → `approved/rejected`).  

**Q:** Как отправляются уведомления?  
A: Через notification-service (web, mobile, email). API должно возвращать `notificationRefs[]`.  

**Q:** Требуются ли мультиязычные версии?  
A: Да, предусмотреть `localization` поле (список поддерживаемых языков и контента).  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, описать модерацию и подписки, подготовить примеры и прогнать проверки.

