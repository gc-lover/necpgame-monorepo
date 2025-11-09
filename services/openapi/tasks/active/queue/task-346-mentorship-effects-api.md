# Task ID: API-TASK-346
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:25  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-343, API-TASK-345 (программы и события академий)

---

## 📋 Краткое описание

Разработать спецификацию `Mentorship Effects API`, агрегирующую влияние наставничества на мир, регионы, фракции и индексы.  
**Целевой файл:** `api/v1/world/player-orders/effects.yaml`

---

## 🎯 Цель задания

Обеспечить world-service API, которое:
- собирает и отдаёт метрики влияния наставничества (MentorshipImpactIndex, AcademyPrestigeScore, KnowledgeDiffusionRate и др.);  
- инициирует пересчёт индексов и публикацию world-events;  
- предоставляет данные аналитике, notification-service и UI дашбордам;  
- синхронизируется с social-service, economy-service и world academies events.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/mentorship-world-impact-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:33  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §1–6: социальные экосистемы, экономика, влияние на фракции/города, геймплей, события.  
- §7: индикаторы и пороги (MentorshipImpactIndex, AcademyPrestigeScore, KnowledgeDiffusionRate).  
- §9: REST макеты (`GET /world/mentorship/effects`, `POST /world/mentorship/effects/recalculate`).  
- §10–11: Kafka события и метрики.  
- §12: использование документа и интеграции.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/mentorship-system-детально.md` — программы/контракты.  
- `.BRAIN/02-gameplay/world/world-events-system-детально.md` — обработка мировых событий.  
- `.BRAIN/02-gameplay/economy/economic-influence-system.md` — индексы экономики.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — визуализация панелей влияния.  
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` — перекрёстные рейтинги и жалобы.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/world/player-orders/effects.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── mentorship/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── effects.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** world-service (port 8092)  
- **Интеграции:** social-service (программы, новости), economy-service (индекс экономики), analytics-service (dashboards), notification-service (alerts), telemetry-service (pipeline).  
- **Kafka:** `world.mentorship.impact`, `world.mentorship.crisis`, `social.mentorship.news`, `economy.mentorship.index`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/world/insights  
- **State Store:** `useWorldStore(mentorshipImpact)`  
- **UI:** `MentorshipImpactMap`, `MentorshipSeasonSummary`, `AcademyPrestigeWidget`, `KnowledgeDiffusionChart`, `MentorshipAlertBanner`  
- **Формы:** `ImpactFilterForm`, `ImpactRecalculateForm`  
- **Layouts:** `WorldInsightsLayout`, `MentorshipImpactDashboardLayout`  
- **Hooks:** `useMentorshipImpact`, `useMentorshipImpactFilters`, `useMentorshipAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: world-service (port 8092)
# - Frontend Module: modules/world/insights
# - State Store: useWorldStore(mentorshipImpact)
# - UI: MentorshipImpactMap, MentorshipSeasonSummary, AcademyPrestigeWidget, KnowledgeDiffusionChart, MentorshipAlertBanner
# - Forms: ImpactFilterForm, ImpactRecalculateForm
# - Layouts: WorldInsightsLayout, MentorshipImpactDashboardLayout
# - Hooks: useMentorshipImpact, useMentorshipImpactFilters, useMentorshipAlerts
# - Events: world.mentorship.impact, world.mentorship.crisis, social.mentorship.news, economy.mentorship.index
# - API Base: /api/v1/world/mentorship/*
```

---

## ✅ Детальный план

1. **Собрать индикаторы:** определить поля для `MentorshipImpactIndex`, `AcademyPrestigeScore`, `KnowledgeDiffusionRate`, `MentorRetentionRate`, `NewMentorPipeline`.  
2. **Спроектировать схемы:** `MentorshipImpact`, `MentorshipImpactRegion`, `MentorshipImpactRecalculateRequest`, `MentorshipImpactAlert`, `MentorshipImpactThreshold`, `MentorshipImpactHistory`.  
3. **Реализовать эндпоинты:** получение текущих значений, пересчёт, история, алерты, конфигурации порогов.  
4. **Документировать интеграции с social/economy:** ссылки на программы, события академий, экономические показатели.  
5. **Описать Kafka события и очереди (`mentorship-event-forecast`).**  
6. **Добавить примеры:** регион с высоким индексом, кризис академии, пересчёт, уведомление.  
7. **Использовать shared security/responses/pagination; вынести схемы/примеры в components.**  
8. **Добавить коды ошибок и бизнес-правила (пороговые проверки, блокировки пересчёта).**  
9. **Указать требования к визуализации и аналитике.**  
10. **Валидация `scripts/validate-swagger.ps1`, README обновление.**

---

## 🔌 Эндпоинты

1. **GET `/world/mentorship/effects`** — текущие индексы по регионам/фракциям.  
2. **GET `/world/mentorship/effects/{regionId}`** — детализация по региону.  
3. **POST `/world/mentorship/effects/recalculate`** — пересчёт индексов (триггер world-event).  
4. **GET `/world/mentorship/effects/history`** — история индексов с пагинацией.  
5. **GET `/world/mentorship/effects/alerts`** — активные кризисы/пороги.  
6. **POST `/world/mentorship/effects/thresholds`** — настройка порогов и реакций.  
7. **GET `/world/mentorship/effects/config`** — конфигурация моделей и источников данных.  
8. **POST `/world/mentorship/effects/{regionId}/ack`** — подтверждение обработки кризиса.  
9. **GET `/world/mentorship/effects/summary`** — сезонная сводка, тренды, топ академий.

---

## 🧱 Модели данных

- **MentorshipImpact** — агрегированная модель (regionId, factionId, mentorshipImpactIndex, academyPrestigeScore, knowledgeDiffusionRate, mentorRetentionRate, newMentorPipeline, lastRecalculatedAt, alerts[]).  
- **MentorshipImpactRegion** — расширенная детализация (linkedPrograms[], linkedEvents[], economicModifiers, reputationDelta).  
- **MentorshipImpactRecalculateRequest** — параметры пересчёта (regions[], reason, force, forecastHorizon).  
- **MentorshipImpactHistoryEntry** — временной ряд значений, инициатор, triggers.  
- **MentorshipImpactAlert** — `alertId`, `regionId`, `severity`, `threshold`, `actionRequired`, `createdAt`, `expectedResolution`.  
- **MentorshipImpactThreshold** — конфигурация порогов (indicator, value, action, cooldown).  
- **MentorshipImpactSummary** — сезонные агрегаты (topAcademies[], crisesHandled, innovationSurges).  
- **PaginatedMentorshipImpactHistory** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк (схемы/примеры вынести).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_MENTORSHIP_EFFECTS_INVALID`, `BIZ_MENTORSHIP_RECALCULATION_LOCKED`, `BIZ_MENTORSHIP_THRESHOLD_CONFLICT`, `INT_MENTORSHIP_PIPELINE_FAILURE`.  
- `info.description` содержит ссылки на `.BRAIN` источники, UX и интеграции.  
- Теги: `Mentorship`, `World`, `Analytics`, `Alerts`, `Events`.  
- Подробно описать Kafka события, очереди и SLA пересчёта.

---

## ✅ Критерии приемки

1. `api/v1/world/player-orders/effects.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. В файле есть `Target Architecture` блок.  
3. Реализованы эндпоинты, схемы и примеры, описанные выше.  
4. Подключены общие компоненты безопасности/ответов/пагинации.  
5. Документированы индексы, пороги, кризисы и связи с другими сервисами.  
6. Добавлены примеры (высокий индекс, кризис, пересчёт, сводка).  
7. Kafka события и очередь `mentorship-event-forecast` описаны.  
8. README в `world/mentorship` обновлён (в рамках реализации).  
9. Task добавлен в `brain-mapping.yaml`.  
10. Статус синхронизирован в `.BRAIN` документе (API Tasks Status).  
11. Указаны зависимости на `mentorship/programs.yaml`, `mentorship/contracts.yaml`, `academies/events.yaml`, `economy/mentorship/index.yaml`.

---

## ❓ FAQ

**Q:** Как часто запускать пересчёт индексов?  
A: По расписанию (час/день) или вручную через API; предусмотреть `cooldown` и блокировку параллельных пересчётов.  

**Q:** Нужно ли хранить сырые данные?  
A: Вне scope; API оперирует агрегатами и ссылками на источники. Указать `dataSourceRefs[]` при необходимости.  

**Q:** Как реагировать на кризис?  
A: Возвращать алерты с actionRequired; world-service инициирует события, social-service уведомляет игроков.  

**Q:** Поддерживаются ли прогнозы?  
A: Да, предусмотреть поля `forecast` в summary/region, указать потребность в модели (analytics-service).  

---

**Следующие шаги исполнителя:** создать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры, прогнать проверки и оформить MR.


