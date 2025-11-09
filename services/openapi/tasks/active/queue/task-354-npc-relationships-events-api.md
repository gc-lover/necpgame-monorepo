# Task ID: API-TASK-354
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 19:55  
**Создатель:** AI Task Creator Agent  
**Зависимости:** API-TASK-352, API-TASK-353 (использует статус и взаимодействия)

---

## 📋 Краткое описание

Создать спецификацию `NPC Relationship Events API`, описывающую мировые события и последствия, возникающие из отношений с NPC (кризисы, спасения, квесты, глобальные реакции).  
**Целевой файл:** `api/v1/world/npc-relationships/events.yaml`

---

## 🎯 Цель задания

Предоставить world-service API, которое:
- регистрирует и распространяет события, связанные с NPC (alliances, betrayal, romance milestones, crises, public scandals);  
- синхронизирует эффекты с social-service (статусы) и economy-service (рынок, скидки), а также с notification-service и analytics;  
- поддерживает подписку, фильтры, acknowledge, экспорт, аналитику влияния на регионы, фракции и игроков;  
- публикует Kafka события (`world.npc-relationships.event`, `world.npc-relationships.alert`).

---

## 📚 Источники информации

### Основной документ

**Репозиторий:** `.BRAIN`  
**Путь:** `.BRAIN/02-gameplay/social/npc-relationships-system-детально.md`  
**Версия:** 1.0.0  
**Дата обновления:** 2025-11-08 10:47  
**Статус документа:** approved (api-readiness: ready)

**Ключевые разделы:**  
- §5, §9: влияние на мир, глобальные события, приглашения, спасения, политические последствия.  
- §11: история и арбитраж (жалобы, публичность).  
- §13–14: REST макет `GET /world/npc-relationships/events`, Kafka `world.npc-relationships.event`.  
- §15: метрики (`NpcRelationshipImpact`, `RomanceHeadlineCount`, `CrisisResolutionRate`).

### Дополнительные источники

- `.BRAIN/03-lore/_03-lore/factions/factions-overview-детально.md` — политические последствия.  
- `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md` — визуализация событий.  
- `.BRAIN/02-gameplay/world/world-events-system-детально.md` — общий фреймворк событий.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — влияние на экономику и контракты.  
- `.BRAIN/05-technical/telemetry/world-relationship-analytics.md` — аналитика и алерты.

---

## 📁 Целевая структура API

**Репозиторий:** `API-SWAGGER`  
**Файл:** `api/v1/world/npc-relationships/events.yaml`  
**Тип:** OpenAPI 3.0.3 (YAML)

**Структура:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── world/
            └── npc-relationships/
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
- **Интеграции:** social-service (status / interactions), economy-service (market modifiers), gameplay-service (quests, raids), notification-service (broadcast), analytics-service (impact dashboards), telemetry-service, content-service (media coverage).  
- **Kafka:** `world.npc-relationships.event`, `world.npc-relationships.alert`, `world.npc-relationships.metrics`, `social.npc-relationships.alert`, `economy.npc-impact.index`.

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend:
- **Модуль:** modules/world/insights  
- **State Store:** `useWorldStore(npcRelationshipEvents)`  
- **UI:** `NpcRelationshipEventFeed`, `NpcRelationshipImpactPanel`, `NpcRelationshipCrisisBoard`, `NpcRelationshipMediaCarousel`, `NpcRelationshipMetricsWidget`  
- **Формы:** `NpcRelationshipEventFilterForm`, `NpcRelationshipEventAckForm`, `NpcRelationshipEventExportForm`  
- **Layouts:** `WorldRelationshipsLayout`, `NpcRelationshipEventDashboardLayout`  
- **Hooks:** `useNpcRelationshipEvents`, `useNpcRelationshipEvent`, `useNpcRelationshipMetrics`, `useNpcRelationshipAlerts`

**Комментарий в YAML:**
```
# Target Architecture:
# - Microservice: world-service (port 8092)
# - Frontend Module: modules/world/insights
# - State Store: useWorldStore(npcRelationshipEvents)
# - UI: NpcRelationshipEventFeed, NpcRelationshipImpactPanel, NpcRelationshipCrisisBoard, NpcRelationshipMediaCarousel, NpcRelationshipMetricsWidget
# - Forms: NpcRelationshipEventFilterForm, NpcRelationshipEventAckForm, NpcRelationshipEventExportForm
# - Layouts: WorldRelationshipsLayout, NpcRelationshipEventDashboardLayout
# - Hooks: useNpcRelationshipEvents, useNpcRelationshipEvent, useNpcRelationshipMetrics, useNpcRelationshipAlerts
# - Events: world.npc-relationships.event, world.npc-relationships.alert, world.npc-relationships.metrics, social.npc-relationships.alert, economy.npc-impact.index
# - API Base: /api/v1/world/npc-relationships/*
```

---

## ✅ Детальный план

1. **Сформировать модель события:** тип (crisis, headline, alliance, betrayal, rescue, celebration, romance milestone), severity, impacted entities, modifiers.  
2. **Спроектировать схемы:** `NpcRelationshipEvent`, `NpcRelationshipImpact`, `NpcRelationshipEventCreateRequest`, `NpcRelationshipEventAck`, `NpcRelationshipAlert`, `NpcRelationshipMetrics`, `NpcRelationshipEventExport`.  
3. **Эндпоинты:** публикация, чтение, фильтрация, acknowledge, метрики, алерты, экспорт, подписки.  
4. **Определить зависимости с `status`/`interactions` API (links to ids) и economy/player orders.**  
5. **Задокументировать Kafka события и очереди (например, `npc-relationship-crisis-response`).**  
6. **Добавить примеры:** скандал корпоративного NPC, романтический headline, спасение игрока, кризис лояльности, экспорт отчёта.  
7. **Shared компоненты:** security/responses/pagination; вынести схемы/примеры, лимит 400 строк.  
8. **Коды ошибок и бизнес-правила (дубликаты событий, блокировка публикаций, SLA).**  
9. **Метрики:** `NpcRelationshipImpact`, `CrisisResolutionRate`, `RomanceHeadlineCount`, `MentorSupportRate`.  
10. **Прогнать `scripts/validate-swagger.ps1`, обновить README.**

---

## 🔌 Эндпоинты

1. **POST `/world/npc-relationships/events`** — регистрация события (world-service producer).  
2. **GET `/world/npc-relationships/events/{eventId}`** — детальная карточка (links на status/interactions).  
3. **GET `/world/npc-relationships/events`** — список (фильтры: тип, регион, фракция, severity, romance, npcType, timeframe).  
4. **POST `/world/npc-relationships/events/{eventId}/ack`** — подтверждение обработки сервисами/фракциями.  
5. **GET `/world/npc-relationships/events/alerts`** — активные кризисы, требующие реакции.  
6. **POST `/world/npc-relationships/events/{eventId}/alerts/ack`** — закрытие алертов.  
7. **GET `/world/npc-relationships/events/metrics`** — агрегаты (impact index, crisis rate, romance headlines).  
8. **GET `/world/npc-relationships/events/export`** — генерация отчётов (CSV/JSON).  
9. **GET `/world/npc-relationships/events/streams`** — SSE/Webhook конфигурация (описать).  
10. **GET `/world/npc-relationships/events/subscriptions`** — подписки фракций/гильдий.  
11. **POST `/world/npc-relationships/events/subscriptions`** — управление подписками (scopes, filters).

---

## 🧱 Модели данных

- **NpcRelationshipEvent** — `eventId`, `eventType`, `severity`, `status`, `regionId`, `factionId`, `npcIds[]`, `playerIds[]`, `summary`, `details`, `links` (status, interactions, contracts), `impact`, `media[]`, `createdAt`, `expiresAt`.  
- **NpcRelationshipImpact** — метрики эффекта (`relationshipDelta`, `economyModifier`, `worldModifier`, `questUnlocks`, `playerReputation`).  
- **NpcRelationshipEventCreateRequest** — входной payload (source, trigger, calculations).  
- **NpcRelationshipAlert** — `alertId`, `eventId`, `severity`, `message`, `actionRequired`, `deadline`, `owners`.  
- **NpcRelationshipEventAck** — `consumerId`, `status`, `notes`, `timestamp`.  
- **NpcRelationshipMetrics** — агрегаты (NpcRelationshipImpactIndex, CrisisResolutionRate, RomanceHeadlineCount, MentorSupportRate).  
- **NpcRelationshipEventExport** — параметры выгрузки (format, range, filters).  
- **NpcRelationshipSubscription** — `subscriptionId`, `subscriber`, `filters`, `channels`, `status`.  
- **PaginatedNpcRelationshipEvents** — стандартная пагинация.

---

## 📏 Принципы и правила

- OpenAPI 3.0.3; ≤400 строк; вынести схемы/примеры в `components`.  
- Использовать `shared/common/security.yaml`, `shared/common/responses.yaml`, `shared/common/pagination.yaml`.  
- Ошибки (`x-error-code`): `VAL_NPC_EVENT_INVALID`, `BIZ_NPC_EVENT_DUPLICATE`, `BIZ_NPC_EVENT_LOCKED`, `BIZ_NPC_EVENT_SUBSCRIPTION_CONFLICT`, `INT_NPC_EVENT_PIPELINE_FAILURE`.  
- `info.description` — перечислить `.BRAIN` источники, UX/analytics подтверждения, связанные сервисы.  
- Теги: `NPC Relationships`, `World Events`, `Alerts`, `Analytics`, `Subscriptions`.  
- Указать зависимости на `npc-relationships/status.yaml`, `npc-relationships/interactions.yaml`, `player-orders/world-impact.yaml`, `factions/events.yaml`.

---

## ✅ Критерии приемки

1. Файл `api/v1/world/npc-relationships/events.yaml` создан/обновлён и проходит `scripts/validate-swagger.ps1`.  
2. Добавлен `Target Architecture` блок.  
3. Реализованы перечисленные эндпоинты, схемы, примеры и бизнес-правила.  
4. Подключены shared security/responses/pagination.  
5. Kafka события, метрики и подписки описаны.  
6. README в каталоге обновлён (в рамках реализации).  
7. Task отражён в `brain-mapping.yaml`.  
8. `.BRAIN` документ обновлён (API Tasks Status).  
9. Указаны зависимости на status/interactions/экономику/фракции.  
10. Отражены метрики (`NpcRelationshipImpact`, `CrisisResolutionRate`, `RomanceHeadlineCount`).  
11. Описаны правила подписок и алертов, включая acknowledge.

---

## ❓ FAQ

**Q:** Кто может публиковать события?  
A: world-service и авторизованные системы; требуются `scope:world.npc-relationships.write`; предусмотреть `sourceSystem` и валидацию подписей.  

**Q:** Как избежать дубликатов?  
A: Включить `idempotencyKey` и проверку конфликтов; ошибка `BIZ_NPC_EVENT_DUPLICATE`.  

**Q:** Нужны ли публичные/приватные события?  
A: Да, добавить `visibility` (public, faction-only, private) и описать влияние на подписки.  

**Q:** Как интегрировать с мировыми квестами?  
A: Возвращать `questRefs[]` и `impact` поле; gameplay-service подписывается на Kafka для запуска квестов/рейдов.  

---

**Следующие шаги исполнителя:** реализовать OpenAPI-файл, вынести компоненты, описать интеграции, подготовить примеры и прогнать проверки.

