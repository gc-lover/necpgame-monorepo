# Task ID: API-TASK-275
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 01:55
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-264 (city unrest escalations API), API-TASK-265 (helios countermesh ops API), API-TASK-273 (seasonal events schedule API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `global-research.yaml`, покрывающую мировые исследования 2020–2093, их ветки, требования, эффекты и синхронизацию с war/economy системами.

**Что нужно сделать:** Описать REST/WS контракты для выдачи состояния дерева исследований, регистрации прогресса и рассылки событий в другие сервисы.

---

## 🎯 Цель задания

Обеспечить:
- Каталог веток исследований с периодами, требованиями и наградами
- Обновление вклада игроков и автоматическое продвижение фаз
- Синхронизацию world flags, war_meter и city_unrest decay
- Применение экономических модификаторов и выдачу наград
- Телеметрию и оповещения фронтенда (Research Dashboards, World Pulse)

---

## 📚 Источники информации

- `.BRAIN/02-gameplay/world/global-research-2020-2093.md` — дерево исследований, требования, эффекты, метрики
- Дополнительно:
  - `.BRAIN/02-gameplay/world/city-unrest-escalations.md`
  - `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`
  - `.BRAIN/02-gameplay/world/seasonal-events-2020-2093.md`
  - `.BRAIN/03-lore/factions/faction-wars-2020-2093.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/gameplay/world/research/global-research.yaml`  
**Микросервисы:** world-service (ядро исследований), gameplay-service (разблокировки активностей), economy-service (модификаторы), social-service (дипломатия), analytics-service (метрики)

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/research/tree` — список веток, статусы, влияния.
2. `GET /api/v1/world/research/nodes/{nodeId}` — детали узла: требования, эффекты, пасхалки.
3. `POST /api/v1/world/research/contribute` — вклад игрока/фракции с валидацией ресурсов и war событий.
4. `POST /api/v1/world/research/nodes/{nodeId}/advance` — продвижение фазы, world flag updates, запуск событий.
5. `POST /api/v1/world/research/events` — публикация ключевых событий в event bus (`RESEARCH_NODE_COMPLETE`, `TECH_UNLOCKED`).
6. `GET /api/v1/world/research/timeline` — агрегированная шкала 2020–2093, фильтры по периодам/фракциям.
7. Интеграции: economy-service `POST /api/v1/economy/research/apply-modifier`, social-service `POST /api/v1/social/research/broadcast`, analytics-service `POST /api/v1/analytics/research/track`.
8. WebSocket `/ws/world/research` — события `NodeProgress`, `NodeUnlocked`, `WarMeterAdjusted`, `ModifierApplied`.
9. Схемы: `ResearchNode`, `ContributionRequest`, `AdvancePayload`, `ResearchEffect`, `ModifierPatch`, `TelemetryEvent`.
10. Observability: KPI (`research_contribution_total`, `war_research_sync`, `tech_unlock_count`), dashboards `global-research-overview`, `research-vs-war-meter`.

---

## ✅ Критерии приемки

1. Все пути используют префикс `/api/v1/world/research`.
2. Требования узлов отражают war_meter, resources, city_unrest пороги из документа.
3. Поддержан вклад как от игроков, так и от кланов/фракций (payload с `actorType`).
4. Возвращаемые payload содержат эффекты на economy-сервис и world flags.
5. Прогресс узла может быть отменён/заморожен (ответы 409 / 423).
6. Telemetry события соответствуют разделу метрик (`RESEARCH_NODE_COMPLETE`, `TECH_UNLOCKED`, `RESEARCH_MEME_SHARED`).
7. По завершении узла автоматически публикуется событие в event bus и в WebSocket.
8. Ошибки используют `shared/common/responses.yaml#/components/schemas/Error`.
9. Target Architecture описывает фронтенд `modules/world/research` и интеграцию с Seasonal Track.
10. Прописаны ограничения по rate limit и cooldown для `contribute` и `advance`.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

