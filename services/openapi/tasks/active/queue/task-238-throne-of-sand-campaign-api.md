# Task ID: API-TASK-238
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-07 16:30
**Создатель:** GPT-5 Codex (Brain Manager)
**Зависимости:** API-TASK-143, API-TASK-160, API-TASK-178

---

## 📋 Краткое описание

Разработать OpenAPI спецификацию для кампании «Throne of Sand», описывающую ветвящуюся структуру квестов, решения игроков, влияние на климатический контроль и связанные микросервисы (`world-service`, `social-service`, `economy-service`).

**Что нужно сделать:** Создать `api/v1/narrative/throne-of-sand-campaign.yaml`, охватывающий маршруты управления кампанией, фиксацию решений, расчёт репутации и выдачу уникальных наград.

---

## 🎯 Цель задания

Предоставить единую точку API для управления сюжетной аркой «Throne of Sand», чтобы бэкенд и фронтенд могли синхронизировать состояние кампании, последствия решений и награды.

**Зачем это нужно:**
- Формализовать ветки «Helios Throne», «Free Peoples», «Balance Charter».
- Поддержать три связанных микросервиса (климат, репутация, экономика) через единый сценарный контроллер.
- Сохранить и отдавать состояние кампании для UI (`world`, `social`, `combat`).
- Выдать награды (сет, имплант, дрон) и бафы согласно выбору.

---

## 📚 Источники информации

### Основной документ

**Путь:** `.BRAIN/06-tasks/active/CURRENT-WORK/archive/2025-11-07-hybrid-media-references-expansion.md`
**Версия:** v1.1.0 (2025-11-07 16:14)
**Статус:** approved, api-readiness: ready

**Ключевые разделы:**
- «Ветки решений и последствия» (таблица трёх направлений).
- «Профили NPC» (Лиора Халид, Мэйсон Варр, Нира Сел).
- «Визуальные референсы» (UI варианты `sand-faction/rebel/neutral`).
- «Операционная модель» (season cadence, monitoring).

### Дополнительные источники

- `.BRAIN/06-tasks/ideas/2025-11-07-IDEA-hybrid-media-references.md`
- `.BRAIN/02-gameplay/world/events/world-events-framework.md`
- `.BRAIN/02-gameplay/social/reputation-tiers-detailed.md`
- `.BRAIN/02-gameplay/economy/economy-contracts.md`

### Связанные документы

- `API-SWAGGER/api/v1/narrative/quest-branching.yaml`
- `API-SWAGGER/api/v1/world/events/world-events-framework.yaml`
- `API-SWAGGER/api/v1/social/reputation-system.yaml`

---

## 📁 Целевая структура API

- **Файл:** `api/v1/narrative/throne-of-sand-campaign.yaml`
- **Версия API:** v1
- **Формат:** OpenAPI 3.0.3

```
API-SWAGGER/api/v1/narrative/
 ├── main-story-core.yaml
 ├── quest-branching.yaml
 ├── raids/
 │    ├── raid-blackwall.yaml
 │    └── raid-corpo-tower.yaml
 ├── throne-of-sand-campaign.yaml   ← создать
 └── ...
```

---

## 🏗️ Целевая архитектура (⚠️ ОБЯЗАТЕЛЬНО)

```yaml
# Target Architecture:
# - Microservices: world-service (8086), social-service (8084), economy-service (8085)
# - API Base: /api/v1/narrative/campaigns/throne-of-sand
# - Event Bus: campaign-decision.events (Kafka topic) → world-service, social-service, economy-service
# - Cache: Redis key throne_of_sand:state:{shard}
# - Frontend Modules: modules/world/campaigns, modules/social/reputation, modules/economy/contracts
# - UI Components: CampaignDecisionBoard, ClimateProtocolPanel, FactionReputationCard, BalanceCharterTimeline
# - Hooks/Stores: useCampaignState, useFactionReputation, useClimateProtocols
# - Data Lake Sync: nightly export to analytics-service for telemetry
```

---

## ✅ Что нужно сделать (детальный план)

1. Описать модель кампании (`CampaignState`) с текущим актом, выбранной веткой и прогрессом.
2. Смоделировать решения (`DecisionNode`) и входные данные (фракция, аргументы, skill-checks).
3. Реализовать расчёт репутации (`FactionReputationDelta`) и климатических последствий (`ClimateProtocol`).
4. Добавить контракт выдачи наград (сет «Helios Regalia», имплант «Dustwalk Cortex», дрон «Charter Warden»).
5. Поддержать романтические ветки NPC (открытие диалогов, affinity ≥75).
6. Настроить webhook событий для world-service (штормы, дебаты) и economy-service (futures, charter auctions).
7. Подготовить WebSocket поток обновления кампании (UI таблица решений, таймлайн).
8. Описать SLA и мониторинг (Grafana dashboards, metrics).

---

## 🔀 Endpoints (минимальный набор)

1. **GET `/campaigns/throne-of-sand/state`** – получить текущее состояние (акт, ветка, прогресс, активные эффекты).
2. **POST `/campaigns/throne-of-sand/start`** – начало кампании (создать state, зарегистировать участников).
3. **POST `/campaigns/throne-of-sand/decisions`** – зафиксировать выбор игрока/кланов (тело содержит `decisionId`, аргументы, skill-checks).
4. **GET `/campaigns/throne-of-sand/decisions`** – список доступных решений с требованиями.
5. **POST `/campaigns/throne-of-sand/romance`** – прогресс романтических линий (Лиора, Мэйсон) при успешных affinity-checks.
6. **POST `/campaigns/throne-of-sand/effects/climate`** – применить климатический протокол (`climate.override`, `Sand Parliament`, `Balance Charter`).
7. **GET `/campaigns/throne-of-sand/factions`** – агрегированная репутация по ключевым фракциям.
8. **POST `/campaigns/throne-of-sand/rewards`** – выдача наград в зависимости от ветки (сет, имплант, дрон, бафы).
9. **POST `/campaigns/throne-of-sand/webhooks`** – регистрация/обновление подписчиков (world-service, social-service, economy-service).
10. **GET `/campaigns/throne-of-sand/logs`** – журнал решений, аргументов, skill-check результатов.
11. **WS `/campaigns/throne-of-sand/stream`** – события `campaign-started`, `decision-available`, `decision-applied`, `effect-triggered`, `reward-issued`.

---

## 🧱 Модели данных

- **CampaignState**: `campaignId`, `chapter`, `act`, `branch`, `progressPercent`, `activeEffects`, `startAt`, `updatedAt`.
- **DecisionNode**: `decisionId`, `chapter`, `type` (`POLITICS|SABOTAGE|DIPLOMACY`), `requirements` (skill checks, items, NPC affinity), `consequences` (climate, reputation, economy).
- **FactionReputationDelta**: `factionId`, `delta`, `source`, `expiresAt`.
- **ClimateProtocol**: `protocolId`, `scope`, `weatherPattern`, `duration`, `cooldown`, `affectedZones`.
- **RewardGrant**: `rewardId`, `tier`, `items`, `perks`, `buffs`, `autoClaim`, `eligibility`.
- **RomanceProgress**: `npcId`, `affinity`, `milestones`, `unlockedScenes`.
- **WebhookRegistration**: `service`, `endpoint`, `events`, `auth`, `status`.
- **CampaignLogEntry**: `entryId`, `timestamp`, `actor`, `action`, `payload`, `result`.

---

## 🧭 Принципы и правила

- **Branch exclusivity:** каждая сессия может выбрать только одну ветку; возврат невозможен после акта II.
- **Affinity gates:** романтические сцены открываются при `affinity >= 75` и прохождении соответствующих skill-checks.
- **Climate safety:** климатические протоколы имеют cooldown, который должен применяться в API (защита от спама).
- **Economy sync:** выдача контрактов `Climate Charter` требует проверки текущих аукционов (`economy-service`).
- **Auditability:** каждое решение логируется (actor, аргументы, результат) и доступно для админов.
- **Localization:** responses должны поддерживать локализованные строки (id + fallback).

---

## 🧪 Примеры

- Игроки поддерживают Helios Throne → активируется `climate.override`, повышается репутация Helios, выдаётся сет «Helios Regalia».
- Повстанческая ветка Free Peoples → создаётся эвент `Sandstorm Liberation` и выдаётся имплант «Dustwalk Cortex».
- Нейтральная ветка Balance Charter → регистрируется сезонный дебат `Sand Parliament`, выдается дрон «Charter Warden».
- Romance progress с Лиорой при успешном `Persuasion DC 15` → открытие сцены «Crimson Node Soirée».

---

## 🔗 Связности и зависимости

- `world-service`: генерация климатических событий и глобальных эффектов.
- `social-service`: управление репутацией фракций и романтическими линиями.
- `economy-service`: биржа `Spice/Data Futures`, аукционы `Climate Charter`.
- `analytics-service`: сбор телеметрии по выбору веток и вовлечению.
- `quest-engine`: интеграция с основным состоянием D&D проверок (вопросы к API-TASK-143, API-TASK-178).

---

## ✅ Критерии приемки

1. `throne-of-sand-campaign.yaml` содержит полное описание REST и WS маршрутов, схем и примеров.
2. Описаны все три ветки, награды и климатические эффекты из документа .BRAIN.
3. Реализованы модели репутации, протоколов, романтических веток и логов.
4. Определены WebSocket события и формат сообщений для UI.
5. Добавлены комментарии по архитектуре (микросервисы, модули фронтенда, интеграции).
6. Подготовлены примеры запросов/ответов для каждой ветки и основных действий.
7. Все требования согласованы с мировыми событиями, репутацией и экономикой (зависимости закрыты).


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

