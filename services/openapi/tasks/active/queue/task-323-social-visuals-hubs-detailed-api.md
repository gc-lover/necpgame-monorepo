# Task ID: API-TASK-323
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 16:32  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** [API-TASK-241], [API-TASK-300], [API-TASK-321]

---

## 📋 Краткое описание

Подготовить OpenAPI спецификацию `api/v1/social/visuals/hubs-detailed.yaml` для social-service: карточки социальных хабов, уровни атмосферы, активности NPC и интеграция с маркетинговыми витринами.

**Что нужно сделать:** описать выдачу `HubVisualProfile`, операции управления амбиентом и подписку на активности хабов, используя данные из детализированного визуального гида.

---

## 🎯 Цель задания

Синхронизировать визуальное оформление социальных хабов (Skyline Agora, Undermarket Bazaar, League Hub Conflux, Synth Faith Sanctum) между social-service, маркетингом и фронтендом.

**Зачем это нужно:**
- Обеспечить единый источник правды для визуальных настроек хабов и их динамики.  
- Поддержать персонализацию и аналитические сценарии (активность, retention).  
- Подготовить данные для UI модулей `modules/social/hubs` и маркетинговых витрин.

---

## 📚 Источники информации

### Основной источник концепции

**Репозиторий:** `.BRAIN`  
**Путь к документу:** `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`  
**Версия документа:** 1.0.0  
**Дата последнего обновления:** 2025-11-08 11:06  
**Статус документа:** approved (api-readiness: ready)

**Что важно из документа:**
- Расширенные описания Skyline Agora, Undermarket Bazaar, League Hub Conflux, Synth Faith Sanctum.  
- Требования к NPC моделям, световым уровням, функциям и социальным объектам.  
- Kafka топик `social.visuals.hub.activity` и метрика `HubAmbienceRetention`.

### Дополнительные источники

- `.BRAIN/03-lore/visual-guides/visual-style-locations-детально.md` — asset mapping для хабов (ASSET-HUB-*).  
- `.BRAIN/02-gameplay/social/player-orders-creation-детально.md` — влияние заказов игроков на декорации хабов.  
- `.BRAIN/02-gameplay/social/player-orders-world-impact-детально.md` — мировые эффекты, требующие синхронизации визуальных режимов.

### Связанные документы

- `API-SWAGGER/api/v1/social/player-orders.yaml` (задание API-TASK-317) — взаимодействие с социальными событиями.  
- `API-SWAGGER/api/v1/world/visuals/locations-detailed.yaml` (API-TASK-322) — базовые профили, от которых наследуются хабы.  
- `.BRAIN/05-technical/content-generation/city-life-population-algorithm.md` — плотность NPC и аудиослои.

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
                └── hubs-detailed.yaml
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

### Backend (микросервис)
- **Микросервис:** social-service  
- **Порт:** 8084  
- **API Base:** `/api/v1/social/visuals/*`  
- **Зависимости:** world-service (macro профили), economy-service (рынки), marketing-service (шоукейсы), auth-service (JWT)

### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

### Frontend (модуль)
- **Модуль:** `modules/social/hubs`  
- **State Store:** `useSocialStore` (visualHubs, hubActivity, ambiencePresets)  
- **UI компоненты (@shared/ui):** HubCard, AmbientPreview, ActivityTimeline, NPCSpotlight, MetricChip  
- **Формы (@shared/forms):** HubAmbienceForm, HubShowcaseRequestForm  
- **Layouts:** SocialHubLayout (`@shared/layouts`)  
- **Hooks:** useHubFilters, useHubActivityFeed

### Комментарий
Добавить в начало YAML-файла блок:
```yaml
# Target Architecture:
# - Microservice: social-service (port 8084)
# - Frontend Module: modules/social/hubs
# - UI Components: @shared/ui (HubCard, AmbientPreview, ActivityTimeline, NPCSpotlight, MetricChip)
# - Forms: @shared/forms (HubAmbienceForm, HubShowcaseRequestForm)
# - State: useSocialStore (visualHubs, hubActivity, ambiencePresets)
# - API Base: /api/v1/social/visuals/*
```

---

## ✅ Что нужно сделать (детальный план)

1. **Разбор контента `.BRAIN`** — выделить характеристики каждого хаба (зонирование, свет, функции, NPC модели, безопасности).  
2. **Определить набор эндпоинтов** — чтение профилей, управление амбиентом, получение активности, настройка витрин.  
3. **Спроектировать модели** — `HubVisualProfile`, `AmbiencePreset`, `VendorHighlight`, `ActivityPulse`, `ShowcaseRequest`.  
4. **Настроить безопасность и ошибки** — подключить BearerAuth, ErrorResponse, описать 409 (конфликт расписания) и 423 (хаб заблокирован событием).  
5. **Документировать Kafka события** — `social.visuals.hub.activity` (producer), привязка к маркетинговым нотификациям.  
6. **Метрики и аналитика** — описать `HubAmbienceRetention`, передаваемую в telemetry, и связь с `EventVisualImpact`.  
7. **Валидация** — проверить OpenAPI, убедиться что файл ≤400 строк, компоненты вынести при необходимости, пройти `scripts/validate-swagger.ps1`.

---

## 🔀 Endpoints

1. **GET `/api/v1/social/visuals/hubs/detailed`**  
   - Назначение: список хабов с визуальными профилями.  
   - Query: `cityId`, `category` (market, esports, spiritual), `activityLevel`, `faction`, `dayPhase`, `limit`, `offset`.  
   - Ответ 200: страница `HubVisualProfile`. Пагинация через shared компонент.

2. **GET `/api/v1/social/visuals/hubs/{hubId}`**  
   - Path: `hubId` (`HUB-[A-Z0-9-]+`).  
   - Возвращает полный профиль, включая ambient слои, NPC композиции, рекомендации по UI.  
   - Ошибки: 404 (нет хаба), 423 (на ремонте).

3. **PATCH `/api/v1/social/visuals/hubs/{hubId}/ambience`**  
   - Тело: `HubAmbienceUpdate` (presetId, overrides, effectiveFrom, duration, triggerEvent).  
   - Возвращает обновлённый `HubVisualProfile`.  
   - Ошибки: 400 (невалидные слои), 409 (пересечение с активным событием), 412 (нет согласования UX).

4. **GET `/api/v1/social/visuals/hubs/{hubId}/activity`**  
   - Назначение: выдача ряда `ActivityPulse` (retention, footfall, ambience feedback).  
   - Query: `from`, `to`, `interval`.  
   - Ответ 200: объект с массивом `ActivityPulse[]` и метриками.

5. **POST `/api/v1/social/visuals/hubs/{hubId}/showcase`**  
   - Тело: `HubShowcaseRequest` (channels, assetScope, marketingCampaignId, requestedBy).  
   - Ответ 202: `ShowcaseTicket` (id, status, eta).  
   - Ошибки: 409 (уже запущена витрина), 503 (marketing-service недоступен).

Все ошибки ссылаться на `shared/common/responses.yaml#/components/responses/ErrorResponse`. Безопасность — `security: - BearerAuth: []`.

---

## 🧱 Модели данных

- **HubVisualProfile**  
  Поля: `hubId`, `name`, `cityId`, `locationRef`, `assetId`, `palette`, `lightingPreset`, `ambientAudio`, `npcProfiles[]`, `services[]`, `interactionHotspots[]`, `safetyLevel`, `activityLevel`, `romanceSuitability`, `marketingTags[]`, `supportedModules[]`, `lastUpdated`.

- **AmbiencePreset** (`presetId`, `name`, `description`, `lighting`, `audio`, `crowdDensity`, `durationMinutes`, `recommendedUse`).  
- **HubAmbienceUpdate** (для PATCH).  
- **VendorHighlight** (`vendorId`, `focus`, `uiComponent`, `sponsorTier`).  
- **ActivityPulse** (`timestamp`, `footfall`, `engagement`, `retention`, `ambienceFeedback`, `source`).  
- **HubShowcaseRequest** (`channels[]`, `assetScope`, `campaignId`, `requestedBy`, `notes`).  
- **ShowcaseTicket** (`ticketId`, `status`, `eta`, `approvedBy`).  
- **NpcProfile** (`archetype`, `outfitTheme`, `behaviorTags`, `affiliation`).  
- Использовать `Pagination` из shared.

Примеры: Skyline Agora (деловые встречи), Undermarket Bazaar (подпольные операции), League Hub Conflux (киберспорт), Synth Faith Sanctum (духовные события).

---

## 📡 Kafka и интеграции

- **Producer:** social-service → `social.visuals.hub.activity` `{ hubId, activityLevel, featuredVendors[], ambienceTags[], timestamp }`.  
- **Consumers:** ui-service, notification-service, marketing-automation.  
- Обозначить зависимость на `marketing.visuals.package.generated` (API-TASK-324) для витрин.  
- Подписка на `world.visuals.location.detailed.updated` (API-TASK-322) — описать в разделе dependencies (`x-dependencies`).

---

## 📊 Метрики и аналитика

- `HubAmbienceRetention` — retention игроков благодаря визуалам (отдельное поле в `ActivityPulse`).  
- `AmbienceConversionRate` — конверсия из ambient режимов в заказы (связь с player-orders).  
- `ShowcaseEngagement` — измеряется marketing-service, требуется ссылка на ответ showcase endpoint.

---

## ⚙️ Правила реализации

- Не хранить статические пресеты в коде — только через БД и API.  
- Использовать SOLID/DRY/KISS, выносить повторяющиеся схемы.  
- Все примеры и описания ссылаться на `.BRAIN` документ и asset IDs (`ASSET-HUB-*`).  
- Если файл превысит 400 строк — вынести `components` в `api/v1/social/visuals/components/hubs-detailed.yaml` и описать в README.

---

## ✔️ Критерии приемки

1. Создан файл `api/v1/social/visuals/hubs-detailed.yaml` с блоком Target Architecture.  
2. Все 5 эндпоинтов описаны, включая безопасность, параметры и примеры.  
3. Реализованы модели `HubVisualProfile`, `AmbiencePreset`, `ActivityPulse`, `HubShowcaseRequest`.  
4. Подключены общие ошибки и security схемы.  
5. Описаны Kafka связи и зависимость от world-service.  
6. Пагинация использует общий компонент.  
7. Указаны связи с player-orders и marketing задачами.  
8. Файл проходит `scripts/validate-swagger.ps1`.  
9. Размер ≤400 строк или есть вынесение компонентов.  
10. В описании упомянут `.BRAIN` документ и дата workshop 2025-11-08.  
11. Метрика `HubAmbienceRetention` отражена в моделях и описании.  
12. PATCH endpoint поддерживает ограничения UX/QA (см. документ).

---

## ❓ FAQ

- **Вопрос:** Нужно ли создавать отдельный endpoint для CRUD пресетов?  
  **Ответ:** Нет, пресеты управляются контент-пайплайном. PATCH использует существующие presetId и overrides.

- **Вопрос:** Как синхронизировать изменения с marketing-service?  
  **Ответ:** Через `HubShowcaseRequest` (POST) и Kafka `marketing.visuals.package.generated`, описать зависимость на API-TASK-324.

- **Вопрос:** Можно ли объединить activity и profiles в один ответ?  
  **Ответ:** Нежелательно — activity поток используется для realtime UI, поэтому выделен в отдельный endpoint.

- **Вопрос:** Какие UI модули должны потреблять данные?  
  **Ответ:** `modules/social/hubs`, `modules/marketing/showcase`, `modules/world/atlas` (для cross-link) — перечислить в описании модели.

- **Вопрос:** Требуется ли поддержка локализации?  
  **Ответ:** Да, добавить поля `nameLocalized`/`descriptionLocalized` (map locale → string) с примером для en-US и ru-RU.

---

## 📌 История выполнения

- 2025-11-08 — Задание создано AI агентом GPT-5 Codex на основе `.BRAIN/03-lore/_03-lore/visual-guides/visual-style-locations-детально.md`.


