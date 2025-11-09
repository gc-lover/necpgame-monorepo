# Task ID: API-TASK-280
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 03:00
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-271 (guild contract board API), API-TASK-276 (faction economy assets API), API-TASK-279 (factions history timeline API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `faction-social-dialogues.yaml`, описывающую социальные и романтические линии лидеров фракций: условия доступа, ветвление диалогов, репутационные пороги, награды и интеграции с контрактами/экономикой.

**Что нужно сделать:** Определить REST/WS контракты social-service для выдачи диалогов, выбора веток, трекинга прогресса, обновления репутаций и синхронизации с другими сервисами.

---

## 🎯 Цель задания

Обеспечить:
- Каталог лидеров и их диалоговых линий с типами связей (романтическая, платоническая, прагматичная)
- Управление условиями доступа (репутации, сюжетные события, контракты, рейды)
- Выбор и фиксацию веток (romance, pact, mentorship), включая последствия в economy/world сервисах
- Телеметрию привязанности, выдачу наград и синхронизацию с Guild Contract Board
- Поддержку фронтенд модуля `modules/social/interactions` и state store `social/factions`

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/faction-social-lines.md` — лидеры фракций, ветки, REST/WS контуры, схемы БД
- Дополнительно:
  - `.BRAIN/02-gameplay/world/factions/faction-economy-integration.md`
  - `.BRAIN/03-lore/factions/factions-original-catalog.md`
  - `.BRAIN/03-lore/factions/factions-timeline-2020-2093.md`
  - `.BRAIN/04-narrative/dialogues/romance-hanako-tanaka.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/social/factions/faction-social-dialogues.yaml`  
**Микросервисы:** social-service (ядро), world-service (flags, события), economy-service (ценовые модификаторы), analytics-service (affinity метрики), notification-service (уведомления)

---

## 🧩 Обязательные секции

1. `GET /api/v1/social/dialogues` — список лидеров и доступных линий (фильтры по фракции, типу связи, статусу).
2. `GET /api/v1/social/dialogues/{leaderId}` — детальные данные: prerequisites, узлы, доступные ветви, награды.
3. `POST /api/v1/social/dialogues/{leaderId}/unlock` — проверка/активация линии после выполнения условий (репутация, события, контракты).
4. `POST /api/v1/social/dialogues/{leaderId}/branch` — выбор ветки (romance/pact/mentorship), фиксация на аккаунте.
5. `POST /api/v1/social/dialogues/{leaderId}/progress` — обновление текущего узла, affinity, выдача наград.
6. `POST /api/v1/social/dialogues/{leaderId}/outcome` — завершение линии, последствия для репутаций, экономики, world flags.
7. WebSocket `/ws/social/dialogues/{leaderId}` — события `NodeAvailable`, `AffinityChanged`, `BranchLocked`, `OutcomeApplied`.
8. Интеграции: economy-service `POST /api/v1/economy/factions/modifier`, world-service `POST /api/v1/world/factions/contracts/update`, guild board `POST /api/v1/world/guilds/contracts/sync`.
9. Схемы: `FactionDialogue`, `DialoguePrerequisite`, `BranchOption`, `ProgressUpdate`, `OutcomePayload`, `AffinityChange`, `NotificationPayload`.
10. Observability: KPI `dialogue_completion_rate`, `affinity_growth_index`, `branch_distribution`, дашборды `faction-dialogues-overview`, `romance-progress`.

---

## ✅ Критерии приемки

1. Все маршруты используют префикс `/api/v1/social/dialogues`.
2. Поддержаны лидеры и условия из документа (Liang Wen, Amira Al-Faris, Sofia Arvidsson, Mother Pyra, Nyla Kalu, Marshal Vega, Lyra Voss, Echo Arbiter Z3N).
3. Валидация учитывает репутацию (`ep.corp.*`, `ep.street.*`, `affinity`, `legacy_rep`) и события (Desert Grid, Mech Rampart, Tribunal).
4. Ветки `romance`, `pact`, `mentorship` и дополнительные ветви отображают последствия для economy/world-service.
5. Ошибки используют `shared/common/responses.yaml#/components/schemas/Error`.
6. Поддержан повторный доступ (cooldown, lockout, branch reset) и откаты (409/423).
7. WebSocket payload включает идентификаторы лидера, текущего узла, ветки и изменение affinity.
8. Target Architecture описывает взаимодействие с UI `modules/social/interactions` и state store `social/dialogues`.
9. Указаны ограничения на количество активных линий и rate limit на переключение веток.
10. Телеметрия отражает ключевые события (`dialogue_started`, `branch_selected`, `affinity_maxed`, `outcome_applied`), интегрирована с analytics-service.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

