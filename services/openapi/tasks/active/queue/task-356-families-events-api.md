# Task ID: API-TASK-356
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 20:10  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-355 (для ссылок на дерево)

---

## 📋 Краткое описание

Подготовить спецификацию `Families Events API`, которая управляет жизненными событиями семей (свадьбы, кризисы, праздники, трагедии) и синхронизирует их влияние с другими системами.  
**Целевой файл:** `api/v1/social/families/events.yaml`

---

## 🎯 Цель задания

Обеспечить social-service API, которое:
- регистрирует и обновляет семейные события с параметрами, триггерами, последствиями и связями с квестами;  
- распространяет эффекты на отношения, экономику, world events, уведомления и телеметрию;  
- поддерживает модерацию, жалобы, подтверждения, календарь и экспорт;  
- интегрируется с `families/tree`, npc-relationships, mentorship, player orders и world-service.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/family-relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:53  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §4–5: типы событий (свадьбы, кризисы, праздники), семейные квесты.  
- §9: UX календаря и журналов.  
- §12: REST макеты (`GET /social/families/{familyId}/events`, `POST /social/families/events`).  
- §13: Kafka `social.family.event`, `world.family.crisis`.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/npc-relationships-system-детально.md` — эмоции, реакции NPC.  
- `.BRAIN/02-gameplay/social/player-orders-system-детально.md` — семейные поручения.  
- `.BRAIN/02-gameplay/world/world-events-system-детально.md` — мировые события.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — визуальные элементы событий.  
- `.BRAIN/05-technical/telemetry/family-events-analytics.md` — аналитика событий.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/social/families/events.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── families/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** social-service (port 8084)  
- **Интеграции:** world-service (кризисы, публичные события), economy-service (наследство, расходы), notification-service (alerts), gameplay-service (квесты, миссии), analytics-service (FamilyEventImpact), content-service (VR-архив), moderation-service.  
- **Kafka:** `social.family.event`, `world.family.crisis`, `social.family.status.changed`, `economy.family.heritage`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/social/families  
- **State Store:** `useSocialStore(familyEvents)`  
- **UI:** `FamilyEventsCalendar`, `FamilyEventTimeline`, `FamilyCrisisBoard`, `FamilyEventDetailModal`, `FamilyEventAnalytics`  
- **Формы:** `FamilyEventCreateForm`, `FamilyCrisisResolutionForm`, `FamilyEventModerationForm`  
- **Layouts:** `FamilyEventsLayout`, `FamilyCalendarLayout`  
- **Hooks:** `useFamilyEvents`, `useFamilyEvent`, `useFamilyCrisis`, `useFamilyEventAnalytics`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/families
# - State Store: useSocialStore(familyEvents)
# - UI: FamilyEventsCalendar, FamilyEventTimeline, FamilyCrisisBoard, FamilyEventDetailModal, FamilyEventAnalytics
# - Forms: FamilyEventCreateForm, FamilyCrisisResolutionForm, FamilyEventModerationForm
# - Layouts: FamilyEventsLayout, FamilyCalendarLayout
# - Hooks: useFamilyEvents, useFamilyEvent, useFamilyCrisis, useFamilyEventAnalytics
# - Events: social.family.event, world.family.crisis, social.family.status.changed, economy.family.heritage
# - API Base: /api/v1/social/families/*
```

---

## ✅ Детальный план

1. **Определить сущность семейного события:** тип, severity, участники, триггеры, последствия, связанные квесты и ресурсы.  
2. **Спроектировать схемы:** `FamilyEvent`, `FamilyEventCreateRequest`, `FamilyCrisis`, `FamilyEventImpact`, `FamilyEventModeration`, `FamilyEventAnalytics`, `FamilyEventFilter`.  
3. **Эндпоинты:** CRUD событий, кризисы, календарь, аналитика, модерация, экспорт, уведомления.  
4. **Интеграции:** ссылки на `families/tree`, npc-relationships, mentorship, economy heritage, world events.  
5. **Документировать модерацию, SLA и жалобы (`family-event-review`).**  
6. **Примеры:** свадьба, кризис болезни, семейный праздник, конфликт наследства, модерация.  
7. **Shared components:** security/responses/pagination, вынести схемы/примеры, соблюдать лимит 400 строк.  
8. **Коды ошибок:** ограничения, конфликты, закрытые события.  
9. **Kafka:** описать события и очереди, связь с world/economy.  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/social/families/events`** — создание события (согласование, триггеры).  
2. **GET `/social/families/events/{eventId}`** — детальная карточка.  
3. **GET `/social/families/events`** — список (фильтры: familyId, type, severity, timeframe, status, region).  
4. **PATCH `/social/families/events/{eventId}`** — обновление параметров (организатор, дата, участники, последствия).  
5. **POST `/social/families/events/{eventId}/cancel`** — отмена события (audit).  
6. **GET `/social/families/events/calendar`** — агрегированный календарь (персонализированный, фракционный).  
7. **GET `/social/families/events/alerts`** — активные кризисы/приглашения.  
8. **POST `/social/families/events/{eventId}/ack`** — подтверждение участия/обработки.  
9. **POST `/social/families/events/{eventId}/moderation`** — решение модератора.  
10. **GET `/social/families/events/analytics`** — метрики (FamilyEventImpact, CrisisResolutionRate).  
11. **GET `/social/families/events/export`** — экспорт для отчётов.  
12. **POST `/social/families/events/{eventId}/notify`** — ручной запуск уведомлений (опционально).

---

## 🧱 Модели данных

- **FamilyEvent** — `eventId`, `familyId`, `eventType`, `severity`, `status`, `startAt`, `endAt`, `location`, `participants[]`, `npcIds[]`, `playerIds[]`, `impact`, `linkedQuests[]`, `resources`, `media[]`, `metadata`.  
- **FamilyEventCreateRequest** — входные данные (type, triggers, approvals).  
- **FamilyCrisis** — кризисы (type, severity, requiredActions, deadline, resolutionStatus).  
- **FamilyEventImpact** — `relationshipDelta`, `economyDelta`, `worldDelta`, `factionModifier`.  
- **FamilyEventModeration** — `moderationId`, `moderatorId`, `decision`, `notes`, `timestamp`.  
- **FamilyEventAnalytics** — агрегаты (FamilyEventImpact, CrisisResolutionRate, AdoptionSuccessRate).  
- **FamilyEventFilter** — параметры поиска.  
- **PaginatedFamilyEvents** — стандартная пагинация.  
- **FamilyEventNotification** — каналы и шаблоны уведомлений.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк, вынести схемы/примеры.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_FAMILY_EVENT_INVALID`, `BIZ_FAMILY_EVENT_CONFLICT`, `BIZ_FAMILY_EVENT_LOCKED`, `BIZ_FAMILY_EVENT_CRISIS_ACTIVE`, `INT_FAMILY_EVENT_PIPELINE_FAILURE`.  
- `info.description` — указать `.BRAIN` источники, UX, интеграции.  
- Теги: `Families`, `Events`, `Crisis`, `Calendar`, `Analytics`.  
- Обозначить зависимости на `families/tree.yaml`, `npc-relationships/interactions.yaml`, `economy/families/heritage.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/social/families/events.yaml` создан/обновлён, проходит `scripts/validate-swagger.ps1`.  
2. Добавлен `Target Architecture` блок.  
3. Реализованы эндпоинты, модели, примеры из задания.  
4. Подключены shared security/responses/pagination.  
5. Kafka события, очереди и метрики документированы.  
6. README обновлён (в рамках реализации).  
7. Task отражён в `brain-mapping.yaml`.  
8. `.BRAIN` документ обновлён (API Tasks Status).  
9. Указаны зависимости на другие API (tree, heritage, world events).  
10. Учтены модерация, кризисы, уведомления, календарь.  
11. Обозначены метрики (`FamilyEventImpact`, `CrisisResolutionRate`, `AdoptionSuccessRate`).

---

## ❓ FAQ

**Q:** Кто может создавать события?  
A: Члены семьи, фракционные менеджеры, world-service; требуется проверка ролей и лицензий.  

**Q:** Как управлять пригласительными?  
A: Использовать поля `participants[]`, `invitationStatus`, `notifications`; acknowledgements через `/ack`.  

**Q:** Нужно ли хранить медиа?  
A: Да, через `media[]` с ссылками на content-service; модерация в очереди `family-event-review`.  

**Q:** Как синхронизировать с world events?  
A: Возвращать `worldEventRefs[]` и публиковать `world.family.crisis`/`social.family.event`.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

