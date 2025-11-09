# Task ID: API-TASK-289
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 05:35  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-283 (quest branching database API), API-TASK-272 (faction quest chains API), API-TASK-280 (faction social dialogues API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-marco-fix-sanchez-dialogue.yaml`, описывающую многоуровневый диалог фикса Марко Санчеса: состояния (`base`, `trusted`, `hostile`, `blackwall-alert`), мост к квестам `main-001`, `main-002`, выбору фракций и событиям Blackwall. Диалог должен работать в narrative-service с интеграцией в репутацию фиксеров, выдачу контрактов и мировые события.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры для получения состояния NPC, выдачи узлов/опций, проверки статов (Perception, Persuasion, Intimidation, Negotiation, NetrunnerFocus и др.) и применения исходов (контракты, события, репутация, дебаффы)
- Управление флагами (`flag.marco.met`, `flag.marco.corp`, `flag.marco.gang`, `flag.marco.freelance`, `flag.marco.betrayal`) и репутацией `rep.fixers.marco`
- Интеграцию с квестами `main-001`/`main-002`, веткой Valentinos/Corpo/Freelance, world events (`blackwall_breach`, `blackwall-containment`)
- Телеметрию: onboarding новичка, выбор пути, кризис Blackwall, восстановление после предательства
- UI поддержку `modules/narrative/quests` и `modules/economy/trade` (контракты фикса)

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-marco-fix-sanchez.md` — состояния, узлы, проверки, события
- Дополнительно:
  - `.BRAIN/04-narrative/npc-lore/important/marco-fix-sanchez.md`
  - `.BRAIN/04-narrative/quests/main/001-first-steps.md`
  - `.BRAIN/04-narrative/quests/main/002-choose-path.md`
  - `.BRAIN/04-narrative/dialogues/npc-jake-archer.md`
  - `.BRAIN/02-gameplay/world/events/world-events-framework.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-marco-fix-sanchez.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** social-service (репутация фиксеров), gameplay-service (квесты/контракты), world-service (Blackwall события), analytics-service, notification-service, economy-service (контракты/скидки)  
**Frontend:** `modules/narrative/quests`, onboarding UI, фиксерский блок в `modules/economy/trade`

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/marco-fix-sanchez` — текущее состояние, репутация, активные события, доступные узлы  
2. `POST /api/v1/narrative/dialogues/marco-fix-sanchez/state/resolve` — вычисление состояния (`base`, `trusted`, `hostile`, `blackwall-alert`)  
3. `POST /api/v1/narrative/dialogues/marco-fix-sanchez/state/override` — GM управление (reset, lock, принудительное доверие/hostile, включение Blackwall режима)  
4. `GET /api/v1/narrative/dialogues/marco-fix-sanchez/nodes/{nodeId}` — описание узла и опций  
5. `POST /api/v1/narrative/dialogues/marco-fix-sanchez/nodes/{nodeId}/options/{optionId}` — выполнение опции, учёт проверок, критических исходов и применение эффектов:
   - `grant_quest`, `unlock_contract`, `unlock_codex`, `bonus_reward`, `spawn_encounter`, `apply_debuff`, `restore_reputation`, `apply_fee`, `unlock_event`
6. `POST /api/v1/narrative/dialogues/marco-fix-sanchez/contracts` — выдача/обновление контрактов (`arasaka-entry`, `valentinos-trial`, `freelance-sprint`, `gang-trust-test`, `corp-runner-basic`)  
7. `POST /api/v1/narrative/dialogues/marco-fix-sanchez/events/apply` — обработка world events (`blackwall_breach`, `blackwall-containment`) и изменение сложности/доступности  
8. `POST /api/v1/narrative/dialogues/marco-fix-sanchez/audit` — фиксация предательства, восстановления репутации, оплат контрибюции  
9. WebSocket `/ws/narrative/dialogues/marco-fix-sanchez` — `StateChanged`, `OptionExecuted`, `CheckResult`, `ContractIssued`, `PenaltyApplied`, `BlackwallEventTriggered`  
10. Схемы: `MarcoDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `ContractPayload`, `EventPayload`, `PenaltyPayload`, `TelemetryRecord`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/marco-fix-sanchez` используется для игровых REST маршрутов.  
2. Состояния и триггеры соответствуют документу (`rep.fixers.marco`, `flag.marco.betrayal`, `world.blackwall_breach`).  
3. Проверки поддерживают DC, модификаторы (корп костюм, Valentinos tattoo, классовые бонусы) и критические исходы; ошибки используют `shared/common/responses.yaml#/components/schemas/Error`.  
4. Контракты на пути corp/gang/freelance синхронизируются с квестами и репутациями.  
5. Hostile ветка корректно управляет возвращением доверия и штрафами.  
6. Интеграция с Blackwall событиями (`blackwall-containment`, `blackwall-surge`) документирована.  
7. WebSocket payload включает state, nodeId, optionId, checkResult, contractId, penaltyId, eventKey.  
8. Target Architecture описывает narrative ↔ social/world/gameplay/economy/analytics взаимодействия и UI onboarding.  
9. Прописаны cooldown’ы (3600/600), условия повторных попыток и GM override.  
10. FAQ: работа с негативной репутацией, влияние двойных путей, последствия Blackwall провалов, выдача обучающих бафов.

---



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

