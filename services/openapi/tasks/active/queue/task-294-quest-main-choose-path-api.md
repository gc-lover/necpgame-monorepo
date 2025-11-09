# Task ID: API-TASK-294
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 06:55  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-283 (quest branching database API), API-TASK-286 (npc jake archer dialogue API), API-TASK-292 (npc royce dialogue API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `quest-main-002-choose-path.yaml`, описывающую выбор основного пути (corp / gang / law / freelance) после вступительного квеста. Диалог объединяет NPC Марко Санчес, Хироши Танака, Хосе «Тигр» Рамирес, Сару Миллер и Nomad диспетчера, активирует соответствующие контракты и флаги.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры narrative-service для последовательности совета (council), веток `corp-track`, `gang-track`, `law-track`, `freelance-track`
- Управление флагами (`flag.main002.*`, `flag.arasaka.clearanceA`, `flag.valentinos.oath`, `flag.ncpd.badge`, `flag.freelance.convoy`) и репутациями
- Интеграцию с quest branching (`quests/main/001`, `quests/main/002`), NPC диалогами и контрактами (Arasaka, Valentinos, NCPD, Nomad)
- Телеметрию выбора пути, критических проверок, настроек city unrest и выдаваемых бафов
- Поддержку UI `modules/narrative/quests`, guild contract board и economy модулей (скидки/доступы по выбору)

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/quest-main-002-choose-path.md` — основная схема диалогов и проверок
- Дополнительно:
  - `.BRAIN/04-narrative/dialogues/npc-hiroshi-tanaka.md`
  - `.BRAIN/04-narrative/dialogues/npc-jose-tiger-ramirez.md`
  - `.BRAIN/04-narrative/dialogues/npc-sara-miller.md`
  - `.BRAIN/04-narrative/dialogues/npc-marco-fix-sanchez.md`
  - `.BRAIN/06-tasks/active/CURRENT-WORK/active/quest-system-tech-questions-compact.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/quests/main/quest-main-002-choose-path.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** gameplay-service (branch activation, contracts), social-service (репутации), world-service (city unrest adjustments), analytics-service (telemetry), notification-service, economy-service (скидки/доступы по выбору)  
**Frontend:** `modules/narrative/quests`, onboarding dashboard

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/quests/main/002/decision` — текущее состояние совета, доступные ветки, активные флаги  
2. `POST /api/v1/narrative/quests/main/002/decision/choose` — фиксирует выбор (corp/gang/law/freelance), устанавливает флаги и запускает ветку
3. `POST /api/v1/narrative/quests/main/002/decision/{track}/resolve` — обрабатывает проверку конкретной ветки (Persuasion/Intimidation/Logic/Insight и т.п.), возвращает контракты/бафы  
4. `GET /api/v1/narrative/quests/main/002/decision/{track}/nodes` — выдаёт YAML-узлы/опции для выбранной ветки
5. `POST /api/v1/narrative/quests/main/002/decision/reset` — GM/override (откат выбора, повторная попытка)  
6. `POST /api/v1/narrative/quests/main/002/decision/events` — интеграция с world events (city emergency, Blackwall alerts) влияющих на проверки  
7. WebSocket `/ws/narrative/quests/main/002` — `StateChanged`, `TrackChosen`, `CheckResult`, `ContractGranted`, `FlagUpdated`, `TelemetryRecorded`
8. Схемы: `ChoosePathState`, `DecisionRequest`, `TrackResolution`, `OutcomePayload`, `ContractGrant`, `FlagUpdate`, `TelemetryEvent`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/quests/main/002` соблюдён.  
2. Флаги и репутации обновляются согласно документу (Arasaka clearance, Valentinos oath, NCPD badge, freelance convoy).  
3. Проверки отражают DC/модификаторы (корп костюм, тату, предыдущие флаги).  
4. Контракты выдаются по результатам ветки, интеграция с quest branching и contract board описана.  
5. Поддержан GM reset с корректной телеметрией.  
6. WebSocket payload содержит track, node, check result, присвоенные контракты.  
7. Target Architecture описывает взаимодействия narrative ↔ gameplay/world/social/analytics/economy.  
8. Документированы cooldown’ы, повторные попытки, последствия критических провалов.  
9. Телеметрия учитывает распределение путей, критические исходы, влияние на city unrest.  
10. FAQ: смена пути, восстановление после провала, что происходит при одновременных world events.

---



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

