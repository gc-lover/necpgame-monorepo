# Task ID: API-TASK-292
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 06:25  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-288 (npc jose tiger ramirez dialogue API), API-TASK-283 (quest branching database API), API-TASK-265 (helios countermesh ops API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-royce-dialogue.yaml`, описывающую диалог лидера Maelstrom Ройса: ветки `intake`, `trusted`, `paranoid`, `raid-mode`, реакции на корпоративные войны и Blackwall, выдачу имплантов, рейдов и double-cross миссий.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры narrative-service для управления состояниями и узлами, обработки проверок (Intimidation, Technical, Hacking, Deception, Insight), выдачи контрактов и world events
- Синхронизацию флагов (`flag.maelstrom.intake`, `flag.maelstrom.implant_sync`, `flag.maelstrom.corp_contact`, `flag.maelstrom.blacklist`) и репутации `rep.gang.maelstrom`
- Интеграцию с raid API (`maelstrom-underlink-raid`), Helios Countermesh, корпоративной войной, Blackwall событиями и quest документацией
- Телеметрию: инициация Maelstrom, импланты, рейды, двойная игра
- UI поддержку в `modules/narrative/quests` и рейдовых интерфейсах Maelstrom

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-royce.md` — состояния, узлы, проверки, события  
- Дополнительно:
  - `.BRAIN/04-narrative/npc-lore/important/royce.md`
  - `.BRAIN/04-narrative/quests/side/SQ-maelstrom-double-cross.md`
  - `.BRAIN/02-gameplay/world/helios-countermesh-ops.md`
  - `.BRAIN/02-gameplay/world/events/world-events-framework.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-royce.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** social-service (репутации), gameplay-service (рейды/контракты), world-service (corp wars, Blackwall), analytics-service, notification-service, economy-service (трофеи, импланты)  
**Frontend:** `modules/narrative/quests`, Maelstrom raid UI

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/royce` — текущее состояние, флаги, события, узлы  
2. `POST /api/v1/narrative/dialogues/royce/state/resolve` — вычисление состояния (`intake`, `trusted`, `paranoid`, `raid-mode`) по репутации, флагам, world events  
3. `POST /api/v1/narrative/dialogues/royce/state/override` — GM инструменты (lock/reset, переход в raid-mode, снятие blacklist)  
4. `GET /api/v1/narrative/dialogues/royce/nodes/{nodeId}` — описание узла и опций  
5. `POST /api/v1/narrative/dialogues/royce/nodes/{nodeId}/options/{optionId}` — выполнение опций, проверки, критические исходы, эффекты (`set_flag`, `grant_gear`, `unlock_contract`, `clear_flag`, `trigger_event`, `apply_control_chip`, `apply_implant_pain`)  
6. `POST /api/v1/narrative/dialogues/royce/contracts` — управление контрактами/активностями (`maelstrom-gun-heist`, `maelstrom-scrap-run`, `maelstrom-double-cross`)  
7. `POST /api/v1/narrative/dialogues/royce/events/apply` — интеграция с raid events, corp wars, Blackwall alerts  
8. `POST /api/v1/narrative/dialogues/royce/audit` — фиксация подозрений, blacklist, имплантов и контрольных чипов  
9. WebSocket `/ws/narrative/dialogues/royce` — `StateChanged`, `OptionExecuted`, `CheckResult`, `ContractIssued`, `EventTriggered`, `BlacklistApplied`, `ImplantGranted`, `ControlChipApplied`  
10. Схемы: `RoyceDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `ContractPayload`, `EventPayload`, `FlagPayload`, `TelemetryRecord`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/royce` используется для всех игровых REST маршрутов.  
2. Состояния и флаги соответствуют документу (intake/trusted/paranoid/raid-mode).  
3. Проверки поддерживают DC, модификаторы и критические исходы; ошибки используют общий `Error` компонент.  
4. Контракты и события синхронизируются с raid API и корпоративной войной.  
5. Paranoid ветка управляет blacklist/критическими последствиями, обеспечивает восстановление репутации при успехе.  
6. Интеграция с raid-mode (`maelstrom-underlink-raid`) документирована, WebSocket уведомляет об участии.  
7. WebSocket payload включает state, nodeId, optionId, checkResult, contractId, eventKey, flag updates.  
8. Target Architecture описывает narrative ↔ world/social/gameplay/economy/analytics взаимодействия и UI `modules/narrative/quests`.  
9. Прописаны cooldown’ы, GM overrides, контроль прослушки/implants.  
10. Телеметрия учитывает линии (`maelstrom_intake`, `implant_granted`, `raid_joined`, `double_cross_outcome`, `blacklist_applied`).

---



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

