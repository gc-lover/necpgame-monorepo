# Task ID: API-TASK-345
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:10  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-343, API-TASK-344 (общие сущности программ и контрактов)

---

## 📋 Краткое описание

Подготовить спецификацию `Academies Events API`, описывающую события академий, расписания, метрики и их влияние на мир.  
**Целевой файл:** `api/v1/world/academies/events.yaml`

---

## 🎯 Цель задания

Обеспечить world-service API, которое:
- регистрирует и управляет событиями академий (фестивали, экзамены, чемпионаты, emergency-допуски);
- синхронизируется с social-service и economy-service для обновления наставничества, репутаций и грантов;
- предоставляет аналитике и notification-service данные о заполненности, эффективности и предупреждениях;
- документирует последствия для городов, фракций, курсов и контрактов наставничества.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/mentorship-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:20  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**
- Раздел 9 — академии и образовательные центры, функции и события.  
- Раздел 10 — цепочки наставничества, интеграция с академиями.  
- Раздел 13 — REST макеты (`POST /world/academies/events`).  
- Раздел 14 — Kafka `world.academy.event.published`.  
- Раздел 15 — метрики (`AcademyUtilization`, `ContentModerationLatency`).

### Дополнительные источники

- `.BRAIN/02-gameplay/social/mentorship-world-impact-детально.md` — влияние событий на мир и экономику.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — визуальные элементы академий и событий.  
- `.BRAIN/02-gameplay/world/world-events-system-детально.md` — общий фреймворк мировых событий.  
- `.BRAIN/02-gameplay/economy/economic-influence-system.md` — влияние на налоги, гранты и ресурсы.  
- `.BRAIN/05-technical/telemetry/alliance-analytics-pipeline.md` — подходы к метрикам и алертам.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/world/academies/events.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── academies/
                ├── components/
                │   ├── schemas/
                │   ├── responses/
                │   └── examples/
                └── events.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend:
- **Микросервис:** world-service (port 8092)  
- **Интеграции:** social-service (программы и контракты), economy-service (гранты, субсидии), notification-service, analytics-service (AcademyUtilization), telemetry-service, content-service.  
- **Kafka:** `world.academy.event.published`, `social.mentorship.lesson.completed`, `social.mentorship.contract.signed`, `world.academy.alert`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/world/academies  
- **State Store:** `useWorldStore(academyEvents)`  
- **UI:** `AcademyEventsCalendar`, `AcademyEventDetails`, `AcademyImpactPanel`, `AcademyMetricsWidget`, `AcademyAlertToast`  
- **Формы:** `AcademyEventForm`, `AcademyEventFilterForm`, `AcademyEmergencyForm`  
- **Layouts:** `AcademiesLayout`, `AcademyEventDashboardLayout`  
- **Hooks:** `useAcademyEvents`, `useAcademyEvent`, `useAcademyMetrics`, `useAcademyAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: world-service (port 8092)
# - Frontend Module: modules/world/academies
# - State Store: useWorldStore(academyEvents)
# - UI: AcademyEventsCalendar, AcademyEventDetails, AcademyImpactPanel, AcademyMetricsWidget, AcademyAlertToast
# - Forms: AcademyEventForm, AcademyEventFilterForm, AcademyEmergencyForm
# - Layouts: AcademiesLayout, AcademyEventDashboardLayout
# - Hooks: useAcademyEvents, useAcademyEvent, useAcademyMetrics, useAcademyAlerts
# - Events: world.academy.event.published, world.academy.alert, social.mentorship.lesson.completed, social.mentorship.contract.signed
# - API Base: /api/v1/world/academies/*
```

---

## ✅ Детальный план

1. **Определить типы событий:** фестивали, экзамены, чемпионаты, мастер-классы, emergency-допуски, VR-стримы, корпоративные наборы.  
2. **Сформировать payload:** `academyId`, `eventType`, `schedule`, `capacity`, `requirements`, `impacts`, `rewards`.  
3. **Спроектировать схемы:** `AcademyEvent`, `AcademyEventCreateRequest`, `AcademyEventUpdateRequest`, `AcademyEventImpact`, `AcademyEventMetrics`, `AcademyEventAlert`, `AcademyEventExportRequest`.  
4. **Продумать эндпоинты:** создание, изменение, получение, фильтры, метрики, алерты, экспорт, подтверждения.  
5. **Документировать интеграции:** связи с программами/контрактами наставничества, экономикой (гранты), relationships (репутация).  
6. **Задокументировать Kafka события и очередь модерации контента.**  
7. **Добавить примеры:** корпоративный чемпионат, экзамен академии, emergency-набор, фестиваль наставников, алерт о переполнении.  
8. **Использовать shared components, вынести схемы, соблюдать лимит 400 строк.**  
9. **Обозначить метрики (`AcademyUtilization`, `ContentModerationLatency`, `MentorSatisfactionScore`).**  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/world/academies/events`** — регистрация события (world-service продюсер).  
2. **GET `/world/academies/events/{eventId}`** — детальное представление (программа, расписание, impacts).  
3. **GET `/world/academies/events`** — список с фильтрами (academy, фракция, тип, период, severity).  
4. **PATCH `/world/academies/events/{eventId}`** — обновление расписания, квот, влияния.  
5. **DELETE `/world/academies/events/{eventId}`** — отмена/архивация (с audit).  
6. **POST `/world/academies/events/{eventId}/ack`** — подтверждение обработки другими сервисами.  
7. **GET `/world/academies/events/metrics`** — метрики и агрегаты (utilization, satisfaction, content latency).  
8. **GET `/world/academies/events/alerts`** — активные алерты (переполнения, нарушения).  
9. **GET `/world/academies/events/export`** — экспорт для аналитики (CSV/JSON).  
10. **POST `/world/academies/events/{eventId}/impact`** — фиксация фактических результатов события (score, репутационные изменения).

---

## 🧱 Модели данных

- **AcademyEvent** — `eventId`, `academyId`, `title`, `eventType`, `status`, `schedule`, `location`, `capacity`, `requirements[]`, `rewards[]`, `linkedPrograms[]`, `linkedContracts[]`, `impacts[]`, `createdBy`, `metadata`.  
- **AcademyEventCreateRequest** — `academyId`, `eventType`, `schedule`, `capacity`, `contentRefs[]`, `sponsorships`, `description`, `impactForecast`.  
- **AcademyEventUpdateRequest** — изменения расписания, квот, контента, статуса.  
- **AcademyEventImpact** — `impactType` (world/economy/social/gameplay), `description`, `value`, `affectedEntities[]`, `trustDelta`, `reputationDelta`.  
- **AcademyEventMetrics** — `academyId`, `utilization`, `attendance`, `completionRate`, `sponsorSatisfaction`, `contentLatency`.  
- **AcademyEventAlert** — `alertId`, `eventId`, `severity`, `message`, `actionRequired`, `createdAt`.  
- **AcademyEventExportRequest** — параметры выгрузки (формат, период, фильтры).  
- **AcademyEventAcknowledgement** — `consumerId`, `status`, `notes`, `ackAt`.  
- **PaginatedAcademyEvents** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; соблюдать лимит 400 строк (схемы/примеры в `components`).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_ACADEMY_EVENT_INVALID`, `BIZ_ACADEMY_EVENT_CONFLICT`, `BIZ_ACADEMY_EVENT_LOCKED`, `BIZ_ACADEMY_EVENT_OVERBOOKED`, `INT_ACADEMY_EVENT_STREAM_FAILURE`.  
- `info.description` — ссылки на `.BRAIN` документы, UX подтверждения и связанные сервисы.  
- Теги: `Academies`, `Mentorship`, `World Events`, `Analytics`, `Alerts`.  
- Подробно описать Kafka события и очередь модерации (`mentorship-content-review`).  
- Указать зависимости от `mentorship/programs.yaml` и `mentorship/contracts.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/world/academies/events.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. В начале файла есть `Target Architecture` блок.  
3. Описаны массивы эндпоинтов, модели и примеры.  
4. Подключены общие компоненты безопасности/ответов/пагинации.  
5. Добавлены примеры событий (чемпионат, экзамен, emergency, экспорт, алерт).  
6. Документированы метрики и Kafka события.  
7. README в `world/academies` обновлён (в рамках реализации).  
8. Task отражён в `brain-mapping.yaml`.  
9. В `.BRAIN` документе обновлён статус задач.  
10. Отмечены зависимости на `mentorship/programs.yaml`, `mentorship/contracts.yaml`, `relationships/status.yaml`, `economy/player-orders/risk.yaml`.  
11. Обозначены требования к визуализации (Night City, корпоративные академии).

---

## ❓ FAQ

**Q:** Кто инициирует события академий?  
A: world-service (администраторы академий) с `scope:world.academies.write`; события могут создаваться автоматически по расписанию.  

**Q:** Как связать событие с программами и контрактами?  
A: Через `linkedPrograms[]` и `linkedContracts[]`; social-service обновляет участников и прогресс.  

**Q:** Нужны ли алерты при переполнении?  
A: Да, описать генерацию `world.academy.alert` и эндпоинт `/alerts`.  

**Q:** Поддерживается ли экспорт?  
A: Да, предусмотреть `/export` с фильтрами и форматами (`csv`, `json`), для аналитики и внешних отчётов.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, описать интеграции, подготовить примеры, прогнать проверки и оформить MR.

