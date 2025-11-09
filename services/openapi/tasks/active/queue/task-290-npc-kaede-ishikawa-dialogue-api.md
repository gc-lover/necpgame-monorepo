# Task ID: API-TASK-290
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 05:50  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-265 (helios countermesh ops API), API-TASK-266 (specter-helios balance API), API-TASK-283 (quest branching database API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-kaede-ishikawa-dialogue.yaml`, описывающую многоуровневый диалог двойного агента Kaede Ishikawa: ветки `neutral`, `specter`, `helios`, `balanced`, `helios-agent`, `underlink-mediator`, `family_crisis`. Диалог должен поддерживать рейд `Helios Countermesh Conspiracy`, операции Specter, медиаторские маршруты и кризисные события города.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры narrative-service для управления состояниями диалога, выдачи узлов, проверок (Hacking, Persuasion, Insight и др.), активации контрактов и world events
- Синхронизацию флагов (`flag.kaede.loyalty`, `flag.kaede.prove_helios`, `flag.kaede.network_compromise`, `flag.kaede.family-threatened`, `flag.kaede.logs_shared`) с world-service, raid API и Specter/Helios системами
- Интеграцию с city unrest, событиями (`HELIOS_SPECTER_PROXY_WAR`, `BLACKWALL_GLITCH_ALERT`), Specter HQ, Helios Countermesh OPS и Guild контрактами
- Телеметрию по веткам (Specter intel, Helios CM-Viper, Underlink mediator), критическим провалам и последствиям для семьи Каэдэ
- UI поддержку в `modules/narrative/raids` и Specter/Helios overlays

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-kaede-ishikawa.md` — состояния, YAML узлы, проверочные DC, события  
- Дополнительно:
  - `.BRAIN/04-narrative/npc-lore/important/npc-kaede-ishikawa.md`
  - `.BRAIN/04-narrative/quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`
  - `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`
  - `.BRAIN/02-gameplay/world/specter-hq.md`
  - `.BRAIN/02-gameplay/world/global-research-2020-2093.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-kaede-ishikawa.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** world-service (city_unrest, proxy war events), gameplay-service (raid/contract hooks), social-service (репутации Specter/Helios), analytics-service, notification-service, economy-service (бафы, контракты), guild board  
**Frontend:** `modules/narrative/raids`, Specter/Helios dashboards

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/kaede-ishikawa` — состояние, флаги, активные события, доступные узлы  
2. `POST /api/v1/narrative/dialogues/kaede-ishikawa/state/resolve` — вычисление состояния из флагов (`loyalty`, `network_compromise`, `family-threatened`, city unrest, raid прогресс)  
3. `POST /api/v1/narrative/dialogues/kaede-ishikawa/state/override` — GM инструменты (lock/reset, принудительное включение family crisis, переводы между ветками)  
4. `GET /api/v1/narrative/dialogues/kaede-ishikawa/nodes/{nodeId}` — данные узла и опций  
5. `POST /api/v1/narrative/dialogues/kaede-ishikawa/nodes/{nodeId}/options/{optionId}` — выполнение опций, проверки, модификаторы и эффекты:  
   - `setState`, `setFlags`, `grantItems`, `triggerEvents`, `addCityUnrest`, `unlockCodex`, `grantContract`, `grantBuff`, `spawnEncounter`
6. `POST /api/v1/narrative/dialogues/kaede-ishikawa/contracts` — управление контрактами/операциями (`SPECTER_INTEL_CONTRACT`, `CM-Viper`, `BALANCED_CONTRACT`, `family rescue`)  
7. `POST /api/v1/narrative/dialogues/kaede-ishikawa/events/apply` — интеграция с world events (proxy war, balanced mediator, Blackwall alerts)  
8. `POST /api/v1/narrative/dialogues/kaede-ishikawa/audit` — логирование доверия, провалов, спасения семьи, city unrest изменений  
9. WebSocket `/ws/narrative/dialogues/kaede-ishikawa` — `StateChanged`, `OptionExecuted`, `CheckResult`, `EventTriggered`, `ContractIssued`, `CityUnrestChanged`, `FamilyCrisisStarted`, `MediatorActivated`  
10. Схемы: `KaedeDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `ContractPayload`, `EventPayload`, `CityUnrestDelta`, `TelemetryRecord`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/kaede-ishikawa` для игровых REST маршрутов.  
2. Состояния и флаги соответствуют документу (Specter/Helios/Balanced/Family crisis).  
3. Проверки поддерживают DC, модификаторы (gear, репутации, double agent флаг) и критические исходы; ошибки используют общую `Error` схему.  
4. Контракты и события синхронизируются с raid/ops документами, city unrest и proxy war механиками.  
5. Family crisis ветка корректно реагирует на провалы/спасение, возвращает ветку в Specter/Balanced.  
6. WebSocket payload включает state, nodeId, optionId, checkResult, eventKey, contractId, cityUnrestDelta.  
7. Target Architecture описывает взаимодействие narrative ↔ world/gameplay/social/analytics/economy, UI `modules/narrative/raids`.  
8. Документированы cooldown’ы, GM overrides, последствия крит-провалов, условия balanced mediator.  
9. Телеметрия учитывает ключевые события: `specter_intel`, `cm_viper`, `mediator_success`, `family_rescue`, `city_unrest_change`.  
10. FAQ: управление двойной лояльностью, возвращение после провала, взаимодействие с Specter HQ и Helios OPS, влияние на proxy war.

---



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

