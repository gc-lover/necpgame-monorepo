# Task ID: API-TASK-286
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 04:50  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-276 (faction economy assets API), API-TASK-283 (quest branching database API), API-TASK-284 (npc aisha frost dialogue API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-jake-archer-dialogue.yaml`, описывающую торговый диалог с Джейком Арчером: состояния (market-entry, preferred-client, corporate-sponsor, supply-chain-crisis), торговые проверки, выдачу скидок/активностей, интеграцию с экономикой и мировыми событиями. Диалог работает в narrative-service и взаимодействует с economy-service, world-service, gameplay-service и analytics-service.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры для получения состояния NPC, списка узлов/опций, выполнения стат-проверок и применения торговых outcomes (скидки, контракты, активити, события)
- Синхронизацию репутации `rep.traders.jake`, флагов клиентов (`flag.jake.met`, `flag.jake.preferred`, `flag.jake.corporate`, `flag.jake.crisis`) и мировых событий (`logistics_strike`, `blackwall_breach`)
- Поддержку выдачи контрактов и активностей для economy-service (`delivery-night-shift`, `arasaka-supply-run`, `blackwall-manifest-hack`)
- Телеметрию для торговых операций: скидки, провал/успех проверок, новые контакты, кризисные маршруты
- UI взаимодействие с `modules/economy/trade`, state store `economy/vendors/jake`

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-jake-archer.md` — состояния, узлы, YAML, проверки, события
- Дополнительно:
  - `.BRAIN/04-narrative/npc-lore/common/traders/jake-archer.md`
  - `.BRAIN/04-narrative/quests/main/001-first-steps.md`
  - `.BRAIN/02-gameplay/economy/economy-trading.md`
  - `.BRAIN/02-gameplay/economy/economy-logistics.md`
  - `.BRAIN/02-gameplay/world/events/world-events-framework.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-jake-archer.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** economy-service (скидки, магазины, контракты), world-service (события/alert), social-service (репутации), gameplay-service (активности/контракты), analytics-service, notification-service  
**Frontend:** `modules/economy/trade`, торговые UI панели, Specter/Neon Ghosts оверлеи

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/jake-archer` — текущие состояния, репутация, активные события, доступные узлы  
2. `POST /api/v1/narrative/dialogues/jake-archer/state/resolve` — вычисление состояния (`market-entry`, `preferred-client`, `corporate-sponsor`, `supply-chain-crisis`)  
3. `POST /api/v1/narrative/dialogues/jake-archer/state/override` — GM управление (lock/reset, установка кризиса, corporate flag)  
4. `GET /api/v1/narrative/dialogues/jake-archer/nodes/{nodeId}` — описание узла и опций  
5. `POST /api/v1/narrative/dialogues/jake-archer/nodes/{nodeId}/options/{optionId}` — выполнение опции: проверка Negotiation, Insight, Streetwise, Technical, Persuasion, Hacking; применение outcomes (`apply_discount`, `unlock_shop`, `grant_activity`, `grant_contract`, `trigger_event`, `spawn_encounter`, `set_flag`, `resolve_event`, `unlock_item`, `price_increase`, `trigger_alarm`)  
6. `POST /api/v1/narrative/dialogues/jake-archer/shops` — выдача списков товаров/скидок (интеграция с economy-service)  
7. `POST /api/v1/narrative/dialogues/jake-archer/contracts` — синхронизация контрактов (`arasaka-supply-run`, `militech-drone-blueprint`) и активностей (`delivery-night-shift`, `blackwall-manifest-hack`)  
8. `POST /api/v1/narrative/dialogues/jake-archer/events/apply` — обработка логистических/Blackwall событий, изменение DC/доступности  
9. WebSocket `/ws/narrative/dialogues/jake-archer` — `StateChanged`, `OptionExecuted`, `CheckResult`, `ShopUpdated`, `ContractGranted`, `EventTriggered`, `AlertRaised`  
10. Схемы: `JakeDialogueState`, `DialogueNode`, `DialogueOption`, `Requirement`, `Outcome`, `ShopPayload`, `ContractPayload`, `EventPayload`, `TelemetryRecord`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/jake-archer` используется для игровых REST маршрутов.  
2. Состояния отражают условия из документа, включая репутации и мировые события (`logistics_strike`, `blackwall_breach`).  
3. Проверки учитывают DC, модификаторы (Aldecaldos, items, класса/репутации) и критические исходы.  
4. Флаги `flag.jake.met`, `flag.jake.preferred`, `flag.jake.corporate`, `flag.jake.crisis`, `flag.jake.corp-track`, `flag.jake.blacklist_militech` корректно обновляются.  
5. Экономические функции (`apply_discount`, `price_increase`, `unlock_shop`, `grant_activity`) интегрированы с economy-service.  
6. Кризисные события (logistics strike, blackwall breach) меняют доступность/сложность, как описано.  
7. WebSocket payload включает state, nodeId, optionId, checkResult, discount/contract/item identifiers, event keys.  
8. Target Architecture показывает narrative ↔ economy/world/social/gameplay взаимодействия и UI `modules/economy/trade`.  
9. Документированы ограничения: cooldown’ы, лимиты скидок, последствия blacklists.  
10. FAQ: борьба с черным списком Militech, восстановление скидок после крит-провала, работа во время кризиса.

---


### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

