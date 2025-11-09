# Task ID: API-TASK-287
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 05:05  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-285 (npc hiroshi tanaka dialogue API), API-TASK-280 (faction social dialogues API), API-TASK-283 (quest branching database API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-james-iron-reed-dialogue.yaml`, описывающую корпоративный диалог Militech: состояния (`base`, `loyal`, `rival-suspect`, `war-alert`), проверки, контракты и мировые события. Диалог должен работать в narrative-service и синхронизироваться с репутациями, флагами корпоративной войны, квестами и событиями.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры для получения состояния NPC, списка узлов/опций, проверки статы (Persuasion, Strategy, Negotiation, Deception, Intimidation, Technical) и применения последствий (контракты, штрафы, флаги, world events)
- Синхронизацию с quest branching (`Operation Iron Dawn`, shadow missions, net defence), репутацией `rep.corp.militech`, флагами (`flag.militech.clearanceA`, `flag.militech.arasaka_contact`, `flag.militech.scrutiny`, `flag.militech.blacklist`)
- Интеграцию с world-service (`corporate_war_escalation`, `blackwall_breach`), analytics-service (telemetry) и social-service (репутации)
- UI поддержку `modules/narrative/quests` и state store `narrative/militech`

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-james-iron-reed.md` — состояния, узлы, проверки, события
- Дополнительно:
  - `.BRAIN/04-narrative/npc-lore/important/james-iron-reed.md`
  - `.BRAIN/04-narrative/quests/main/002-choose-path.md`
  - `.BRAIN/04-narrative/quests/faction-world/arasaka-world-quests.md`
  - `.BRAIN/02-gameplay/world/events/world-events-framework.md`
  - `.BRAIN/02-gameplay/social/reputation-formulas.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-james-iron-reed.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** social-service (репутации, штрафы), world-service (events, war escalations), gameplay-service (контракты, shadow missions), analytics-service, notification-service, economy-service (при наградах/экипировке)  
**Frontend:** `modules/narrative/quests`, корпоративные панели Militech

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/james-iron-reed` — текущее состояние, активные флаги, доступные узлы  
2. `POST /api/v1/narrative/dialogues/james-iron-reed/state/resolve` — вычисление состояния (`base`, `loyal`, `rival-suspect`, `war-alert`) по репутациям/флагам  
3. `POST /api/v1/narrative/dialogues/james-iron-reed/state/override` — GM управление (reset, lock, выдать clearance, снять подозрения)  
4. `GET /api/v1/narrative/dialogues/james-iron-reed/nodes/{nodeId}` — описание узла и опций  
5. `POST /api/v1/narrative/dialogues/james-iron-reed/nodes/{nodeId}/options/{optionId}` — выполнение опции (stat-check, модификаторы, крит. исходы) и эффекты:
   - `grant_clearance`, `grant_contract`, `grant_asset`, `grant_gear`
   - `apply_penalty`, `call_review`, `flag_blacklist`, `increase_surveillance`
   - `unlock_event`, `grant_brief`, `assign_support`, `assign_shadow_mission`
6. `POST /api/v1/narrative/dialogues/james-iron-reed/contracts` — запуск/обновление контрактов (`militech-iron-dawn`, `militech-support-wing`, `militech-counterintel`, `militech-defense-grid`) и shadow миссий  
7. `POST /api/v1/narrative/dialogues/james-iron-reed/events/apply` — обработка world events (`corporate_war_escalation`, `blackwall_breach`) и изменение сложностей  
8. `POST /api/v1/narrative/dialogues/james-iron-reed/audit` — логирование штрафов, наблюдения, blacklist  
9. WebSocket `/ws/narrative/dialogues/james-iron-reed` — `StateChanged`, `OptionExecuted`, `CheckResult`, `ContractIssued`, `PenaltyApplied`, `WarEventTriggered`, `BlacklistApplied`  
10. Схемы: `JamesDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `ContractPayload`, `PenaltyPayload`, `WarEventPayload`, `TelemetryRecord`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/james-iron-reed` для игровых REST маршрутов.  
2. Состояния соответствуют документу, включая репутации и глобальные события (`corporate_war_escalation`).  
3. Все проверки поддерживают DC, модификаторы и критические исходы; ответ использует стандартную `Error` схему при провале валидации.  
4. Clearance, контракты и shadow missions синхронизируются с репутационной системой Militech и quest branching.  
5. Подозрения/blacklist (`flag.militech.arasaka_contact`, `flag.militech.blacklist`, `flag.militech.scrutiny`) обновляются корректно.  
6. Интеграция с world events (`militech-warfront-berlin`, `militech-war-analysis`) документирована и покрыта.  
7. WebSocket payload включает `state`, `nodeId`, `optionId`, `checkResult`, `contractId`, `penaltyId`, `eventId`.  
8. Target Architecture описывает narrative ↔ social/world/analytics/gameplay/notification взаимодействия.  
9. Прописаны cooldown’ы (1800 сек) и ограничения на повторные попытки, очищение подозрительных флагов.  
10. FAQ: восстановление после blacklist, конфликт Militech vs Arasaka (двойные агенты), действия во время war-alert.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

