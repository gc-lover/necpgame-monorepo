# Task ID: API-TASK-342
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 18:55  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-340 (relationship context), API-TASK-341 (trust contracts)

---

## 📋 Краткое описание

Подготовить OpenAPI спецификацию `Alliance Events API`, описывающую публикацию и управление событиями союзов и дипломатии (world-service ↔ social-service).  
**Целевой файл:** `api/v1/world/alliances/events.yaml`

---

## 🎯 Цель задания

Обеспечить world-service контрактом, который:
- регистрирует события союзов (заключение, продление, нарушение, клятва, война, перемирие);
- синхронизируется с social-service и economy-service для эффекта на репутацию, налоги, доступы;
- предоставляет подписку и фильтры по регионам, фракциям, severity, типам событий;
- логирует последствия для городов, квестов, PvP-зон, торговых маршрутов;
- поддерживает analytics-service (AllianceSuccessRate) и notification-service.

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:40  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- 5.1–5.2 — типы союзов и процесс создания/нарушения.  
- 8 — взаимодействие с фракциями и городами, ежедневные обновления.  
- 10 — REST макет `POST /world/alliances/events`.  
- 11 — Kafka `world.alliance.event`.  
- 12 — метрики `AllianceSuccessRate`.

### Дополнительные документы

- `.BRAIN/03-lore/_03-lore/factions/factions-overview-детально.md` — иерархия фракций, дипломатические статусы.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — визуализация событий в городах.  
- `.BRAIN/02-gameplay/world/world-events-system-детально.md` — общий фреймворк мировых событий.  
- `.BRAIN/02-gameplay/economy/economic-influence-system.md` — влияние на налоги/рынки.  
- `.BRAIN/05-technical/telemetry/alliance-analytics-pipeline.md` — метрики и алерты.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/world/alliances/events.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Директория:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── alliances/
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
- **Интеграции:** social-service (relationships, trust), economy-service (налоги), analytics-service (AllianceSuccessRate), notification-service, telemetry-service.  
- **Kafka:** `world.alliance.event`, `world.alliance.alert`, `world.alliance.metrics`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/world/diplomacy  
- **State Store:** `useWorldStore(allianceEvents)`  
- **UI:** `AllianceTimeline`, `AllianceEventFeed`, `AllianceImpactPanel`, `AllianceMetricsWidget`, `DiplomacyHeatmap`  
- **Формы:** `AllianceEventFilterForm`, `AllianceEventRegistrationForm`  
- **Layouts:** `WorldDiplomacyLayout`, `AllianceDashboardLayout`  
- **Hooks:** `useAllianceEvents`, `useAllianceEvent`, `useAllianceMetrics`, `useAllianceAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: world-service (port 8092)
# - Frontend Module: modules/world/diplomacy
# - State Store: useWorldStore(allianceEvents)
# - UI: AllianceTimeline, AllianceEventFeed, AllianceImpactPanel, AllianceMetricsWidget, DiplomacyHeatmap
# - Forms: AllianceEventFilterForm, AllianceEventRegistrationForm
# - Layouts: WorldDiplomacyLayout, AllianceDashboardLayout
# - Hooks: useAllianceEvents, useAllianceEvent, useAllianceMetrics, useAllianceAlerts
# - Events: world.alliance.event, world.alliance.alert, world.alliance.metrics
# - API Base: /api/v1/world/alliances/*
```

---

## ✅ Детальный план

1. **Определить типы событий:** formation, renewal, upgrade, breach, termination, war, truce, pact, trade-route-opened, city-revolt, emergency-aid.  
2. **Продумать payload:** сущности (allianceId, factions[], regions[], severity, impacts[], trustDelta, reputationDelta, economyModifiers).  
3. **Эндпоинты:** публикация, чтение, фильтрация, acknowledgement, аналитика, экспорт.  
4. **Связь с trust/relationships:** ссылаться на договоры (contractId), уровни доверия, причины изменения.  
5. **Схемы:** `AllianceEvent`, `AllianceImpact`, `AllianceEventCreateRequest`, `AllianceEventFilter`, `AllianceEventAcknowledgeRequest`, `AllianceAlert`, `AllianceMetrics`, `AllianceEventExportRequest`.  
6. **Указать подключения к Kafka и telemetry.**  
7. **Добавить shared security/responses/pagination.**  
8. **Примеры:** создание боевого союза, нарушение с санкциями, мировое событие (война), трэд-альянс, уведомление о восстании города.  
9. **Учесть лимит 400 строк — вынести компоненты.**  
10. **Валидация скриптом и обновление README.**

---

## 🔌 Эндпоинты

1. **POST `/world/alliances/events`** — регистрация события (producer world-service).  
2. **GET `/world/alliances/events/{eventId}`** — детальная карточка.  
3. **GET `/world/alliances/events`** — список (фильтры по типу, фракции, региону, периоду, severity).  
4. **POST `/world/alliances/events/{eventId}/ack`** — подтверждение обработки другими сервисами.  
5. **GET `/world/alliances/events/metrics`** — метрики и агрегаты (успешность союзов, breach rate).  
6. **GET `/world/alliances/events/export`** — экспорт (CSV/JSON) для аналитики.  
7. **GET `/world/alliances/events/alerts`** — активные алерты высокого приоритета.  
8. **DELETE `/world/alliances/events/{eventId}`** — отзыв события (edge-case, audit log).

---

## 🧱 Модели данных

- **AllianceEvent** — основная запись (`eventId`, `allianceId`, `type`, `status`, `severity`, `timestamp`, `initiator`, `participants[]`, `regions[]`, `factions[]`, `impacts[]`, `trustDelta`, `reputationDelta`, `economyModifiers`, `relatedContracts[]`, `metadata`).  
- **AllianceImpact** — детализированные последствия (world, economy, social, gameplay).  
- **AllianceEventCreateRequest** — payload для публикации (включая references на trust contracts).  
- **AllianceEventFilter** — фильтры (тип, статус, регион, период, severity, factionId).  
- **AllianceEventAcknowledgeRequest** — подтверждение обработки (consumerId, status, notes).  
- **AllianceAlert** — сигнал тревоги (alertId, eventId, severity, message, actionRequired).  
- **AllianceMetrics** — агрегированные показатели (AllianceSuccessRate, BreachRate, ActiveAlliances, PendingTreaties).  
- **AllianceEventExport** — параметры экспорта.  
- **PaginatedAllianceEvents** — пагинация с ссылками.  
- **AllianceEventDeletionRequest** — причина отзыва.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; файл ≤400 строк (схемы/примеры вынести).  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки с `x-error-code`: `VAL_ALLIANCE_EVENT_INVALID`, `BIZ_ALLIANCE_EVENT_CONFLICT`, `BIZ_ALLIANCE_EVENT_LOCKED`, `BIZ_ALLIANCE_EVENT_NOT_FOUND`, `INT_ALLIANCE_EVENT_STREAM_FAILURE`.  
- В `info.description` указать `.BRAIN` источники и взаимодействия с social/economy.  
- Добавить теги: `Alliances`, `Diplomacy`, `World Events`, `Analytics`.  
- Подробно описать Kafka события (`world.alliance.event`, `world.alliance.alert`, `world.alliance.metrics`) и payload.

---

## ✅ Критерии приемки

1. Создан/обновлён файл `api/v1/world/alliances/events.yaml`, проходит `scripts/validate-swagger.ps1`.  
2. Присутствует `Target Architecture` блок.  
3. Описаны все необходимые эндпоинты и схемы.  
4. Подключены общие компоненты безопасности/ответов/пагинации.  
5. Включены описания Kafka событий и влияние на economy/social.  
6. Добавлены примеры (formation, breach, war, alert, export).  
7. README в `world/alliances` дополнен (в рамках реализации).  
8. Task привязан в `brain-mapping.yaml`.  
9. Указаны зависимости на trust contracts и relationships.  
10. Задокументированы метрики для analytics-service.

---

## ❓ FAQ

**Q:** Кто может публиковать события?  
A: world-service (авторитетный источник). API должно требовать `scope:world.events.write`.  

**Q:** Как синхронизировать с social-service?  
A: Возвращать `relatedContracts[]` и `relationshipsEffects[]` — social-service по ID обновляет trust/rep.  

**Q:** Нужна ли поддержка реального времени?  
A: Kafka поток обеспечивает realtime; REST — для регистрации и аналитики.  

**Q:** Какой retention истории?  
A: Минимум сезон (90 дней) в primary storage, архив в cold storage (вне scope).  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, описать схемы, вынести компоненты, прогнать проверки и подготовить MR.

