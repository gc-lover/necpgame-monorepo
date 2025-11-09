# Task ID: API-TASK-291
**Тип:** API Generation  
**Приоритет:** высокий  
**Статус:** queued  
**Создано:** 2025-11-08 06:05  
**Создатель:** AI Agent (GPT-5 Codex)  
**Зависимости:** API-TASK-288 (npc jose tiger ramirez dialogue API), API-TASK-289 (npc marco fix sanchez dialogue API), API-TASK-283 (quest branching database API)

---

## 📋 Краткое описание

Создать OpenAPI спецификацию `npc-rita-moreno-dialogue.yaml`, описывающую диалог уличного информатора Риты Морено: состояния (`street-entry`, `insider-loop`, `valentinos-favor`, `maelstrom-alert`, `fiesta-mode`, `blackout-rumor`), проверки и события. Диалог должен предоставлять торговые скидки, слухи, активности Valentinos/Maelstrom и праздничные режимы.

---

## 🎯 Цель задания

Обеспечить:
- REST/WS контуры narrative-service для управления состояниями, узлами, проверками (Streetwise, Perception, Empathy, Negotiation, Deception, Hacking) и выдачи контента (магазины, слухи, активности, world events)
- Поддержку флагов (`flag.rita.met`, `flag.rita.insider`, `flag.rita.valentinos`, `flag.rita.alert`, `flag.rita.fiesta`, `flag.rita.blackwall_tip`), репутаций (`rep.traders.rita`, `rep.gang.valentinos`, `rep.maelstrom`) и city events (`heywood_turf_war`, `blackwall_breach`, `dia_de_los_muertos`, `nusa_idol_live`)
- Интеграцию с quest branching (`maelstrom-double-cross`, `heywood-meds-run`, `valentinos-carnival-hack`) и economy-service (магазины, скидки)
- Телеметрию: успехи двойной игры, участие в fiesta, Blackwall слухи, deliveries
- UI поддержку `modules/social/informants`, торговых панелей и street feed

---

## 📚 Источники информации

- `.BRAIN/04-narrative/dialogues/npc-rita-moreno.md` — состояния, узлы, проверки, события  
- Дополнительно:
  - `.BRAIN/04-narrative/dialogues/npc-jose-tiger-ramirez.md`
  - `.BRAIN/04-narrative/dialogues/npc-marco-fix-sanchez.md`
  - `.BRAIN/04-narrative/quests/side/maelstrom-double-cross.md`
  - `.BRAIN/02-gameplay/world/events/world-events-framework.md`
  - `.BRAIN/02-gameplay/social/reputation-formulas.md`

---

## 📁 Целевая структура API

**Файл:** `api/v1/narrative/dialogues/npc-rita-moreno.yaml`  
**Микросервис:** narrative-service  
**Интеграции:** social-service (репутации, стримы), economy-service (магазины, скидки), gameplay-service (активности/контракты), world-service (fiesta/war/Blackwall события), analytics-service, notification-service  
**Frontend:** `modules/social/informants`, street market UI

---

## 🧩 Обязательные секции

1. `GET /api/v1/narrative/dialogues/rita-moreno` — состояние, репутации, активные события, доступные узлы  
2. `POST /api/v1/narrative/dialogues/rita-moreno/state/resolve` — расчёт состояния (`street-entry`, `insider-loop`, `valentinos-favor`, `maelstrom-alert`, `fiesta-mode`, `blackout-rumor`) по флагам и событиям  
3. `POST /api/v1/narrative/dialogues/rita-moreno/state/override` — GM инструменты (lock/reset, активация fiesta/alert режимов)  
4. `GET /api/v1/narrative/dialogues/rita-moreno/nodes/{nodeId}` — описание узла и опций  
5. `POST /api/v1/narrative/dialogues/rita-moreno/nodes/{nodeId}/options/{optionId}` — выполнение опций, проверки и эффекты (`unlock_shop`, `apply_discount`, `unlock_codex`, `grant_activity`, `resolve_event`, `spawn_encounter`, `trigger_alarm`, `grant_activity`, `apply_fee`)  
6. `POST /api/v1/narrative/dialogues/rita-moreno/shops` — управление торговыми витринами (`rita-default`, `rita-valentinos`, скидки, ценовые модификаторы)  
7. `POST /api/v1/narrative/dialogues/rita-moreno/activities` — выдача и обновление активностей/контрактов (Valentinos, Maelstrom, Heywood deliveries)  
8. `POST /api/v1/narrative/dialogues/rita-moreno/events/apply` — обработка world events (`dia_de_los_muertos`, `nusa_idol_live`, `maelstrom_pipeline`, `blackwall_breach`)  
9. `POST /api/v1/narrative/dialogues/rita-moreno/audit` — логирование двойной игры, tribute выплат, fiesta/Blackwall участий  
10. WebSocket `/ws/narrative/dialogues/rita-moreno` — `StateChanged`, `OptionExecuted`, `CheckResult`, `ShopUpdated`, `ActivityGranted`, `EventTriggered`, `DiscountChanged`, `AlertRaised`

---

## ✅ Критерии приемки

1. Префикс `/api/v1/narrative/dialogues/rita-moreno` для игровых маршрутов.  
2. Состояния соответствуют документу, включая fiesta и Blackwall режимы.  
3. Проверки учитывают DC, модификаторы (street memelord, Valentinos tattoo, ocular cyberware, Netrunner class) и критические исходы; ошибки используют `Error` компонент.  
4. Магазины и скидки интегрированы с economy-service; активные события изменяют цены/DC, как описано.  
5. Двойная игра Maelstrom ↔ Militech корректно обрабатывается, включая награды и последствия.  
6. WebSocket payload включает state, nodeId, optionId, checkResult, shopId, activityId, eventKey.  
7. Target Architecture описывает взаимодействия narrative ↔ social/world/economy/gameplay/analytics.  
8. Прописаны cooldown’ы, ограничения по активностям и GM overrides.  
9. Телеметрия учитывает олимп (discount usage, rumors delivered, double-cross outcomes, fiesta participation).  
10. FAQ: восстановление репутации после провала, управление fiesta скидками, сочетание с Valentinos/Neon Ghosts и Blackwall кризисами.

---



### OpenAPI (обязательно)

- Заполни `info.x-microservice` (name, port, domain, base-path, package) по данным целевого микросервиса.
- В секции `servers` оставь Production gateway `https://api.necp.game/v1` и пример локальной разработки `http://localhost:8080/api/v1`.
- WebSocket маршруты публикуй только через `wss://api.necp.game/v1/...`.

