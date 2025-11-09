# Task ID: API-TASK-323
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:22  
**Создатель:** AI Agent (ДУАПИТАСК)  
**Зависимости:** API-TASK-322 (для ссылок на локации)

---

## 📋 Краткое описание

Описание REST API для детализированных социальных хабов, синхронизированных с визуальными профилями мира.

**Что нужно сделать:** Создать `api/v1/social/visuals/hubs-detailed.yaml`, обеспечив выдачу карточек Skyline Agora, Undermarket Bazaar, League Hub Conflux и других хабов, а также мониторинг активности и связей с событиями.

---

## 🎯 Цель задания

Предоставить social-service API, отображающее визуальные и социальные данные по ключевым хабам с учётом трафика, NPC, бизнес-функций и связей с рейдами/маркетингом.

**Зачем это нужно:**
- UI модулю `modules/social/hubs` нужны готовые карточки для интерактивных карт и списка популярных хабов.
- Система рекомендаций и matchmaking должна знать текущую атмосферу и заполненность хабов.
- Маркетинг и ивент-менеджмент планируют активности, опираясь на визуальные состояния и кампании.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 11:06  
**Статус документа:** approved

**Что важно из этого документа:**
- Описания Skyline Agora, Undermarket Bazaar, League Hub Conflux, Synth Faith Sanctum.
- Атмосферные параметры (освещение, музыку, толпы), функции (VR-подиумы, подпольные клиники, стриминг студии).
- Связь с KPI: HubAmbienceRetention, EventVisualImpact.
- Kafka топик `social.visuals.hub.activity`.

### Дополнительные источники

- `.BRAIN/02-gameplay/social/social-overview.md` — общая структура социальных механик.
- `.BRAIN/02-gameplay/social/npc-simulation.md` — данные о плотности NPC и типах деятельности.
- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — зависимость хабов от заказов и рейтингов (для активности).
- `API-SWAGGER/api/v1/social/npc/schedules.yaml` — примеры моделей social-service, пригодных для переиспользования.

### Связанные документы

- `API-SWAGGER/tasks/active/queue/task-322-visual-locations-detailed-api.md` — источник ссылок на локации.
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — данные о потоках и сменах NPC.

---

## 📁 Целевая структура API

### Репозиторий: `API-SWAGGER`

**Целевой файл:** `api/v1/social/visuals/hubs-detailed.yaml`  
**API версия:** v1  
**Тип файла:** OpenAPI 3.0.3 (YAML)

**Структура директории:**
```
API-SWAGGER/
└── api/
    └── v1/
        └── social/
            └── visuals/
                ├── schemas/
                │   ├── visual-hub-detailed.yaml
                │   └── hub-activity.yaml
                └── hubs-detailed.yaml
```

Создать README в `api/v1/social/visuals/` с описанием назначения и ссылкой на world visuals.

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** social-service  
- **Порт:** 8084  
- **API Base:** `/api/v1/social/visuals/*`  
- **Домен:** социальные хабы, активность, атмосфера  
- **Интеграции:** world-service (детали локаций), economy-service (рынки), marketing-service (кампании), realtime-gateway (WebSocket push)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** modules/social/hubs  
- **State Store:** useSocialStore (visualHubs, hubActivity, hubFilters)  
- **UI компоненты (@shared/ui):** HubCard, CrowdMeter, ActivityTimeline, AmbientTagList, VendorList  
- **Формы (@shared/forms):** HubFilterForm, EventSubscriptionForm  
- **Layouts:** SocialLayout, GameLayout  
- **Хуки:** useRealtime, usePolling, useSocialStoreFilters

### Kafka / Streams
- Поставщик: social-service → `social.visuals.hub.activity`  
- Полезная нагрузка: `{ hubId, activityLevel, featuredVendors[], ambienceTags[], updatedAt }`  
- Подписчики: ui-service, notification-service, analytics  
- Дополнительно описать SLA на обновление (каждые 15с для League Hub Conflux).

---

## 🧭 Подробный план реализации

1. **Оформить директорию `social/visuals`** — создать README и подпапку `schemas`.
2. **Спроектировать модели** — вынести `VisualHubDetailedProfile`, `HubActivitySnapshot`, `VendorSpotlight`, `EventHighlight`.
3. **Описать endpoints** — включить список хабов, получение детали, поток активности и фильтры.
4. **Интегрировать world visuals** — добавить ссылки на `locationId`, `worldVisualId`, описать связи в schema.
5. **Документировать событийность** — добавить описание Kafka топика и webhook уведомлений для маркетинга.
6. **Проверить чеклист** — убедиться в использовании общих компонентов, правильном versioning и security.

---

## 🔀 Endpoints

### 1. GET `/api/v1/social/visuals/hubs/detailed`
- **Назначение:** получить коллекцию социальных хабов с фильтрами по городу, типу, уровню активности.
- **Параметры:** `cityId`, `hubType` (enum: commerce, nightlife, religious, esports, blackmarket), `activityLevel` (enum: calm, moderate, peak), `featured` (boolean), `page`, `size`.
- **Ответ 200:** `VisualHubDetailedCollection` (данные + пагинация).
- **Голые состояния:** указать, что пустой результат возвращает `data: []`.

### 2. GET `/api/v1/social/visuals/hubs/detailed/{hubId}`
- **Назначение:** вернуть полную карточку хаба, включая расписание, визуальные особенности, NPC роли, безопасность.
- **Ответ 200:** `VisualHubDetailedProfile`.
- **Доп. поля:** `linkedLocationId`, `currentEvents`, `liveActivity`.
- **Ошибки:** 404 (не найден), 423 (хаб временно закрыт — использовать Error с кодом `BIZ_HUB_LOCKED`).

### 3. GET `/api/v1/social/visuals/hubs/detailed/{hubId}/activity`
- **Назначение:** стриминг или периодический снимок активности.
- **Query:** `from`, `to`, `interval` (enum: realtime, 5m, 15m, 1h).
- **Ответ 200:** `HubActivityTimeline` (time series).
- **Заголовки:** `X-Realtime-Token` для подписки через WebSocket.

### 4. POST `/api/v1/social/visuals/hubs/subscriptions`
- **Назначение:** оформить подписку на изменения активности/атмосферы для выбранных хабов.
- **Body:** `HubSubscriptionRequest` (упомянуть ограничение до 10 хабов).
- **Ответ 202:** `HubSubscriptionStatus` с `subscriptionId`, `status`, `callbackUrl`.
- **Ошибки:** 400 (валидация), 409 (дубликат подписки), 503 (realtime hub недоступен).

---

## 🧱 Модели данных

- **VisualHubDetailedProfile**
  - `hubId` (string) — уникальный идентификатор (например, `skyline-agora`).
  - `name` (string) — локализуемое имя.
  - `locationId` (string) — ссылка на world visual.
  - `hubType` (string, enum: commerce, nightlife, esports, religious, blackmarket, civic).
  - `ambienceTags` (array[string]) — визуальные и чувственные метки (neon, ambient, clandestine).
  - `lightingProfile` (`HubLightingProfile`) — описания света, голограмм.
  - `audioPalette` (`AudioLayerReference`) — шумы, музыка, спецэффекты.
  - `npcRoles` (array[`NpcRoleProfile`]) — ключевые категории NPC.
  - `services` (array[`HubServiceOffering`]) — доступные функции (Shadow Clinic, VR Pods).
  - `safetyLevel` (`HubSecurityProfile`) — охрана, риск событий.
  - `activityLevel` (string, enum: calm, moderate, peak).
  - `featuredVendors` (array[`VendorSpotlight`]).
  - `currentEvents` (array[`EventHighlight`]).
  - `metrics` (`HubMetricSnapshot`) — HubAmbienceRetention, EventVisualImpact.
  - `updatedAt` (date-time).

- **HubActivityTimeline**
  - `hubId` (string).
  - `timeframe` (`TimeRange`).
  - `granularity` (enum).
  - `points` (array[`HubActivityPoint`]) — `timestamp`, `activityScore`, `crowdDensity`, `queueLength`.

- **HubSubscriptionRequest**
  - `hubIds` (array[string], minItems 1, maxItems 10).
  - `callbackUrl` (string, format uri).
  - `delivery` (enum: webhook, kafka).
  - `filters` (`HubSubscriptionFilters`) — activity thresholds, ambience tags.

- **HubSubscriptionStatus**
  - `subscriptionId` (uuid).
  - `status` (enum: queued, active, paused).
  - `expiresAt` (date-time).

- **Shared components** (`schemas/visual-hub-detailed.yaml`):
  - `HubLightingProfile`
  - `AudioLayerReference` (можно переиспользовать из world schemas)
  - `NpcRoleProfile`
  - `HubServiceOffering`
  - `HubSecurityProfile`
  - `VendorSpotlight`
  - `EventHighlight`
  - `HubMetricSnapshot`
  - `HubActivityPoint`
  - `HubSubscriptionFilters`
  - `TimeRange`

Учесть примеры для Skyline Agora (commerce, peak), Undermarket Bazaar (blackmarket, clandestine), League Hub Conflux (esports, peak).

---

## 📏 Принципы и правила

- RESTful стиль, единая модель ошибок `Error`; коды: `VAL_HUB_FILTER_INVALID`, `BIZ_HUB_LOCKED`, `INT_SOCIAL_STREAM_FAILED`.
- Использовать `shared/common/pagination.yaml` и `shared/common/responses.yaml`.
- Указать безопасность BearerAuth + требуемый scope `social.hubs.read` / `social.hubs.manage`.
- Размер каждого файла ≤400 строк; вынести схемы в отдельный компонентный файл.
- Добавить `x-realtime` блок с указанием WebSocket канала `/ws/social/visuals/hubs`.
- Поддержать заголовок `X-Player-Segment` для таргетированных данных (опционально).

---

## ✅ Критерии приемки

1. Все четыре endpoints задокументированы с параметрами, примерами и ответами.
2. `VisualHubDetailedProfile` покрывает данные из Skyline Agora, Undermarket Bazaar, League Hub Conflux, Synth Faith Sanctum.
3. Пагинация списка хабов использует общий компонент и имеет пример ответа.
4. В ответах указываются ссылки на world visuals (`locationId`), обеспечивая согласованность.
5. Подписка описывает ограничения, пример запроса и ответ 202.
6. Зафиксированы требования к безопасности и scopes.
7. Kafka топик `social.visuals.hub.activity` описан с payload и потребителями.
8. README в директории `social/visuals` обновлён с оглавлением и ссылками.
9. Проверена валидация данных для фильтров (enum, maxItems, pattern).
10. Пример ответа показывает League Hub Conflux на пике активности.
11. Эндпоинт активности поддерживает интервалы и возвращает корректную временную сетку.
12. Линтер `validate-swagger.ps1` проходит без ошибок.

---

## ❓ FAQ

- **Зачем отдельный эндпоинт для активности?**  
  Он позволяет UI и аналитике запрашивать time series без вытягивания полной карточки хаба и использует оптимизированный storage.

- **Как синхронизироваться с world visuals?**  
  Каждая запись содержит `locationId`; при изменениях world-service публикует Kafka событие, social-service обновляет связанные хабы.

- **Нужно ли включать маркетинговые кампании?**  
  Укажите поле `campaigns` внутри `EventHighlight` с ссылкой на marketing-service; полноценные кампании будут оформлены отдельным API.

- **Как фильтровать по дню/ночи?**  
  Использовать параметр `timePhase` (optional) и указать его в schema `HubActivityPoint`.

- **Что делать при недоступном realtime?**  
  Возвращать `503` с кодом `INT_SOCIAL_STREAM_FAILED` и описать fallback: polling endpoint `/activity`.

---



