# Task ID: API-TASK-278
**Тип:** API Generation
**Приоритет:** высокий
**Статус:** queued
**Создано:** 2025-11-08 02:30
**Создатель:** AI Agent (GPT-5 Codex)
**Зависимости:** API-TASK-271 (guild contract board API), API-TASK-272 (faction quest chains API), API-TASK-276 (faction economy assets API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `factions-original-catalog.yaml`, описывающую авторские корпорации, банды и гильдии: их историю, события, репутации и интеграции.

**Что нужно сделать:** Определить REST/WS контракты для world/social сервисов, обеспечивающих каталогизацию фракций, выдачу событий, управление репутацией и контрактами.

---

## 🎯 Цель задания

Обеспечить:
- Каталог фракций с историей, лидерами, механиками и связями
- Управление событиями и контрактами, привязанными к конкретным фракциям
- Синхронизацию репутаций, world flags и экономических модификаторов
- Поддержку фронтенда `modules/world/factions` и сопутствующих UI-панелей
- Телеметрию по участию игроков в событиях и изменению репутаций

---

## 📚 Источники информации

- `.BRAIN/03-lore/factions/factions-original-catalog.md` — детализированный каталог фракций, механики и API контуры
- Дополнительно:
  - `.BRAIN/02-gameplay/world/faction-cult-defenders.md`
  - `.BRAIN/02-gameplay/world/world-bosses-catalog.md`
  - `.BRAIN/02-gameplay/world/factions/faction-economy-integration.md`
  - `.BRAIN/04-narrative/dialogues/faction-social-lines.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/world/factions/original-catalog.yaml`  
**Микросервисы:** world-service (основной), social-service (репутации), economy-service (торговля), analytics-service (телеметрия), notification-service (ивенты)

---

## 🧩 Обязательные секции

1. `GET /api/v1/world/factions` — список доступных авторских фракций, фильтры по типу и региону.
2. `GET /api/v1/world/factions/{factionId}` — детальная информация: история, лидеры, механики, активные события.
3. `GET /api/v1/world/factions/{factionId}/events` — расписание и статус world/social событий, связанные world flags.
4. `POST /api/v1/world/factions/{factionId}/contracts` — создание контрактов/квестов, валидация репутации и ресурсов.
5. `POST /api/v1/world/factions/{factionId}/reputation` — обновление репутации, синхронизация с social-service и экономическими модификаторами.
6. `POST /api/v1/world/factions/{factionId}/aftermath` — фиксация исходов событий, изменение world_state, выдача наград.
7. WebSocket `/ws/world/factions/{factionId}` — события `EventStarted`, `ContractUpdated`, `ReputationChanged`, `AftermathApplied`.
8. Интеграции: economy-service `POST /api/v1/economy/factions/modifier`, social-service `POST /api/v1/social/factions/dialogue-hook`, analytics-service `POST /api/v1/analytics/factions/track`.
9. Схемы: `FactionSummary`, `FactionDetail`, `FactionEvent`, `ContractRequest`, `ReputationPatch`, `AftermathPayload`, `TelemetryEvent`.
10. Observability: KPI `faction_engagement_score`, `contract_completion_rate`, `reputation_shift_index`, дашборды `faction-world-map`, `faction-economy-impact`.

---

## ✅ Критерии приемки

1. Все маршруты используют префикс `/api/v1/world/factions`.
2. Набор фракций соответствует каталогу (Aeon Dynasty, Crescent Energy, Mnemosyne Archives, Ember Saints, Void Sirens, Basilisk Sons, Quantum Fable).
3. Репутационные ветки и события синхронизируются с социальными линиями и рейдами.
4. Ошибки оформлены через `shared/common/responses.yaml#/components/schemas/Error`.
5. Поддержаны связи с world-bosses и Defender NPC (related_sources).
6. Контракты и события могут ссылаться на Guild Contract Board и Raid Scenarios (Cross-service hooks).
7. WebSocket события включают идентификаторы фракции и контекста (eventId, contractId, reputationDelta).
8. Target Architecture описывает взаимодействие с фронтендом `modules/world/factions` и state store `world/factions`.
9. Указаны ограничения по rate limit для контрактов и обновлений репутации.
10. Телеметрия охватывает участие игроков, изменение репутаций и экономическое влияние (`economy_modifier_applied`).

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

