# Task ID: API-TASK-288
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 05:20  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-284 (npc aisha frost dialogue API), API-TASK-287 (npc james iron reed dialogue API), API-TASK-283 (quest branching database API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-jose-tiger-ramirez-dialogue.yaml`, описывающую многостадийный диалог лидера Valentinos: состояния (`base`, `familia`, `mistrust`, `turf-war`, `fiesta`, `memorial`), проверки, выдачу заказов и реакции на мировые события. Диалог должен работать в narrative-service и синхронизироваться с репутацией Valentinos, квестами Heywood, world events и экономикой.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры для получения состояния NPC, доступа к узлам/опциям, проведения проверок (Intimidation, StreetSense, Deception, CombatLeadership и др.) и применения результатов (контракты, события, флаги, награды)
- Управление флагами (`flag.valentinos.oath`, `flag.valentinos.maelstrom_contact`, `flag.valentinos.ncpd_informer`, `flag.valentinos.exiled`, `flag.valentinos.memorial`) и мировыми событиями (`heywood_turf_war`, `dia_de_los_muertos`, `metro_shutdown`)
- Интеграцию с quest branching (`valentinos-family-rescue`, `valentinos-scout`, `valentinos-double-blind`, `valentinos-trial`), world-service (turf counterstrike, memorial), social-service (репутации ганг фракций) и economy-service (скидки/активности)
- Телеметрию: клятва, контракты, кризисные операции, пасхальные мероприятия (AR-офренда)
- UI поддержку `modules/narrative/quests`, `modules/social/informants`, а также Specter/Neon HUD при turf-war

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-jose-tiger-ramirez.md` — состояния, узлы, проверки, события  
- Дополнительно:
  - `.BRAIN/04-narrative/npc-lore/important/jose-tiger-ramirez.md`
  - `.BRAIN/04-narrative/quests/side/heywood-valentinos-chain.md`
  - `.BRAIN/04-narrative/dialogues/npc-rita-moreno.md`, `.BRAIN/04-narrative/dialogues/npc-royce.md`
  - `.BRAIN/02-gameplay/world/events/world-events-framework.md`
  - `.BRAIN/02-gameplay/social/reputation-formulas.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-jose-tiger-ramirez.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** social-service (репутация Valentinos/Maelstrom), world-service (turf war, memorial, fiesta), gameplay-service (квесты/активности), analytics-service, notification-service, economy-service (скидки, активы)  
**Frontend:** `modules/narrative/quests`, `modules/social/informants`, уличные HUD’ы Heywood

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/jose-tiger-ramirez` — текущие состояния, репутация, активные события, доступные узлы  
2. `POST /api/v1/narrative/dialogues/jose-tiger-ramirez/state/resolve` — вычисление состояния (`base`, `familia`, `mistrust`, `turf-war`, `fiesta`, `memorial`)  
3. `POST /api/v1/narrative/dialogues/jose-tiger-ramirez/state/override` — GM инструменты (lock/reset, включение fiesta/memorial)  
4. `GET /api/v1/narrative/dialogues/jose-tiger-ramirez/nodes/{nodeId}` — описание узла и опций  
5. `POST /api/v1/narrative/dialogues/jose-tiger-ramirez/nodes/{nodeId}/options/{optionId}` — выполнение опции; учитывать проверки, модификаторы и критические исходы (oath, familia-brief, mistrust-interrogation, turf-command и др.)
6. `POST /api/v1/narrative/dialogues/jose-tiger-ramirez/contracts` — управление заказами и активностями (`valentinos-trial`, `valentinos-family-rescue`, `valentinos-scout`, `valentinos-double-blind`, street race, AR-офренда)
7. `POST /api/v1/narrative/dialogues/jose-tiger-ramirez/events/apply` — интеграция с world events (`heywood_turf_war`, `dia_de_los_muertos`, `metro_shutdown`, `valentinos-turf-counterstrike`) и последствия (бафы, alert уровни)
8. `POST /api/v1/narrative/dialogues/jose-tiger-ramirez/audit` — фиксация изгнаний, контрибуций, memorial статусов
9. WebSocket `/ws/narrative/dialogues/jose-tiger-ramirez` — `StateChanged`, `OptionExecuted`, `CheckResult`, `ContractIssued`, `EventTriggered`, `FlagUpdated`, `MemorialStarted`, `FiestaActivated`
10. Схемы: `JoseDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `ContractPayload`, `EventPayload`, `FlagPayload`, `TelemetryRecord`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/jose-tiger-ramirez` используется у всех игровых REST маршрутов.  
2. Состояния и триггеры соответствуют документу (репутация, флаги, мировые события).  
3. Проверки поддерживают DC, модификаторы (tattoo, faction allies, items) и критические исходы; ошибки используют `shared/common/responses.yaml#/components/schemas/Error`.  
4. Контракты/активности и world events синхронизированы с квестами Heywood, turf подписками, fiesta/memorial режимами.  
5. Флаги (`flag.valentinos.oath`, `flag.valentinos.maelstrom_contact`, `flag.valentinos.ncpd_informer`, `flag.valentinos.exiled`, `flag.valentinos.memorial`) корректно обновляются.  
6. WebSocket payload включает state, nodeId, optionId, checkResult, contractId, eventKey, flagKey.  
7. Target Architecture описывает narrative ↔ social/world/gameplay/economy/analytics взаимодействия и UI `modules/narrative/quests`.  
8. Прописаны cooldown’ы (trial, tribute), условия возврата после изгнания, политика GM override.  
9. Телеметрия учитывает oath completion, turf-war успехи, fiesta участие, memorial посещаемость.  
10. FAQ: работа во время fiesta/war concurrently, снятие подозрений после double blind, как возвращается изгнанный игрок.

---



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

