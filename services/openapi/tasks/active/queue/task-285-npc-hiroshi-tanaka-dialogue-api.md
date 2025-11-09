# Task ID: API-TASK-285
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 04:35  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-273 (seasonal events schedule API), API-TASK-283 (quest branching database API), API-TASK-280 (faction social dialogues API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-hiroshi-tanaka-dialogue.yaml`, описывающую корпоративный диалог Arasaka: состояния (`base`, `loyal`, `suspicious`, `lockdown`), проверки D&D, выдачу контрактов и реакции на мировые события. Диалог работает в narrative-service с глубокой интеграцией в социальные, квестовые и world-state системы.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры для запросов состояния NPC, выдачи узлов/опций, проведения стат-проверок и применения последствий (флаги, репутации, контракты, события)
- Синхронизацию с квестами Arasaka (`Operation Serenity`, double agent ветки), world events (`arasaka_lockdown`), Specter/Helios флагами
- Управление clearance уровня A, корпоративными штрафами, GM overrides и логированием
- Поддержку UI `modules/narrative/quests` и state store `narrative/arasaka`
- Телеметрию по корпоративным проверкам, выдаче контрактов и реакции Arasaka на игрока

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-hiroshi-tanaka.md` — состояния, узлы, проверки, последствия
- Дополнительно:
  - `.BRAIN/04-narrative/npc-lore/important/hiroshi-tanaka.md`
  - `.BRAIN/04-narrative/quests/main/002-choose-path.md`
  - `.BRAIN/04-narrative/quests/faction-world/arasaka-world-quests.md`
  - `.BRAIN/02-gameplay/social/reputation-formulas.md`
  - `.BRAIN/02-gameplay/world/events/world-events-framework.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-hiroshi-tanaka.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** social-service, world-service, gameplay-service (quests/contracts), analytics-service, notification-service, economy-service  
**Frontend:** `modules/narrative/quests`, корпоративный интерфейс Arasaka

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/hiroshi-tanaka` — текущие состояния, активные флаги, доступные узлы, cooldown’ы  
2. `POST /api/v1/narrative/dialogues/hiroshi-tanaka/state/resolve` — вычисление состояния (`base`, `loyal`, `suspicious`, `lockdown`) по репутации и флагам  
3. `POST /api/v1/narrative/dialogues/hiroshi-tanaka/state/override` — GM/LiveOps (lock/reset, выдать clearance)  
4. `GET /api/v1/narrative/dialogues/hiroshi-tanaka/nodes/{nodeId}` — описание опций, требований, исходов  
5. `POST /api/v1/narrative/dialogues/hiroshi-tanaka/nodes/{nodeId}/options/{optionId}` — выполнение опции, проверка статов (Persuasion, Strategy, Deception, Composure), применение outcomes:
   - `grant_clearance`, `unlock_contract`, `issue_penalty`, `trigger_lockdown`, `deliver_brief`, `apply_flag`, `grant_asset`, `call_supervisor`
6. `POST /api/v1/narrative/dialogues/hiroshi-tanaka/contracts` — выдача/обновление контрактов (`arasaka-serenity`, shadow tasks) и их статусов  
7. `POST /api/v1/narrative/dialogues/hiroshi-tanaka/events/apply` — интеграция с world events (`arasaka_lockdown`, `arasaka-lockdown-response`) и city unrest  
8. `POST /api/v1/narrative/dialogues/hiroshi-tanaka/audit` — запись инцидентов (blacklist, silence note, salary cut)  
9. WebSocket `/ws/narrative/dialogues/hiroshi-tanaka` — `StateChanged`, `OptionExecuted`, `CheckResult`, `ContractIssued`, `PenaltyApplied`, `LockdownTriggered`  
10. Схемы: `HiroshiDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `ContractPayload`, `PenaltyPayload`, `LockdownEvent`, `TelemetryRecord`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/hiroshi-tanaka` используется для игровых REST маршрутов.  
2. Состояния соответствуют документу (`base`, `loyal`, `suspicious`, `lockdown`) с нужными условиями и флагами.  
3. Стат-проверки учитывают DC, модификаторы и критические исходы из документа; ошибки возвращают общую `Error` схему.  
4. Clearance A, контракты и shadow tasks синхронизируются с quest branching и social репутацией.  
5. Логика подозрений (`flag.arasaka.militech_contact`) и blacklist обрабатывается корректно, с записью в аудит.  
6. Интеграция с `world.event.arasaka_lockdown` и city unrest (через world-service) документирована и покрыта.  
7. WebSocket payload включает `state`, `nodeId`, `optionId`, `checkResult`, `contractId`, `penaltyId`, `eventId`.  
8. Target Architecture описывает взаимодействия narrative-service ↔ social/world/analytics/gameplay и UI `modules/narrative/quests`.  
9. Прописаны ограничения/cooldowns (1800, 7200 секунд), политика GM override и обработка повторных попыток.  
10. FAQ: восстановление репутации после blacklist, возврат clearance, работа во время lockdown, совместимость с double agent ветками.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

