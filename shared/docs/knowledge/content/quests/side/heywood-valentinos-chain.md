# Диалоги — Цепочка побочных квестов «Heywood Valentinos»

**ID диалога:** `dialogue-quest-side-heywood-valentinos-chain`  
**Тип:** quest  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 21:21  
**Приоритет:** высокий  
**Связанные документы:** `../dialogues/npc-jose-tiger-ramirez.md`, `../dialogues/npc-rita-moreno.md`, `../dialogues/npc-marco-fix-sanchez.md`, `../../dialogues/npc-royce.md`, `../../02-gameplay/social/reputation-formulas.md`, `../../02-gameplay/world/events/world-events-framework.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 21:21
**api-readiness-notes:** «Цепочка Valentinos против Maelstrom: сцены turf-war, переговоры, пасхалки и экспорт YAML/REST. Готово для API.»

---

## 1. Контекст и цели

- **Арка:** серия миссий Valentinos в Vista del Rey (Heywood), где семья защищает баррио от давления Maelstrom и Militech.
- **Тон:** смесь семейных ритуалов, уличных войн и ярких пасхалок — AR-офренды Día de los Muertos, мемы Ever Given, lowrider TikTok стримы, отсылки к GameStop 2021 как «легенде рейдеров».
- **Ключевые NPC:** Хосе «Тигр» Рамирес, Рита Морено, полевой координатор Militech, агенты NCPD, стрит-арт коллектив «Vista Vibes».
- **Цели игрока:** пройти инициацию, отработать infiltration, вести street race-диверсию, решить престрелку в соборе, договориться с НCPD или Militech (double-cross) и провести мемориал павших.
- **Интеграции:** `npc-jose-tiger-ramirez`, `npc-rita-moreno`, событие `world.event.heywood_turf_war`, квест `quest-side-maelstrom-double-cross`, модули social-service и economy-service (черный рынок имплантов, lowrider гонки).

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Флаги |
|-----------|----------|----------|-------|
| setup | Ритуал клятвы и подготовка баррио | Старт цепочки | `flag.valchain.setup` |
| infiltration | Проникновение на склад Militech | Взят контакт у Риты | `flag.valchain.infiltration` |
| street-race | Lowrider диверсия на трассе | Получена миссия Хосе | `flag.valchain.street_race` |
| turf-war | Открытый бой за квартал | `world.event.heywood_turf_war == true` | `flag.valchain.turf_war` |
| double-cross | Выбор: NCPD, Maelstrom или Valentinos | Завершён turf-war | `flag.valchain.double_cross` |
| memorial | Финальная офренда, AR-мемориал | Установлен исход double-cross | `flag.valchain.memorial` |

- **Проверки:** Streetwise, Intimidation, Performance, Hacking, Strategy, Empathy, Willpower, Technical.
- **Репутация:** `rep.gang.valentinos`, `rep.gang.maelstrom`, `rep.law.ncpd`, `rep.social.media`. Расчёты берутся из `social/reputation-formulas.md`.

## 3. Сцены и узлы (псевдо-YAML)

```yaml
- node-id: oath-setup
  label: «Баррио держится»
  entry-condition: state == "setup"
  speaker-order: ["Jose", "Player", "Choir"]
  player-options:
    - option-id: swear
      text: "Готов(а) держать улицу"
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 18
          modifiers:
            - source: tattoo.valentinos
              value: +1
      npc-response: "Vista del Rey помнит всех. Даже Ever Given не заткнула наш порт." 
      outcomes:
        success: { set-flag: "flag.valchain.setup", reputation: { rep.gang.valentinos: +8 }, reward: "token.valentinos-rosary" }
        failure: { grant-contract: "valentinos-proving-run", reputation: { rep.gang.valentinos: +3 } }
        critical-success: { reward: "item.valentinos-medallion", reputation: { rep.gang.valentinos: +12 }, unlock: "activity.valentinos-ceremony" }
    - option-id: observe
      text: "Я просто наблюдаю"
      outcomes: { default: { hint: "Raise_rep", reputation: { rep.gang.valentinos: -2 } } }

- node-id: infiltration-plan
  label: «Склад Militech»
  entry-condition: state == "infiltration"
  speaker-order: ["Rita", "Player", "Militech Broker"]
  player-options:
    - option-id: stealth-entry
      text: "Подкрасться через вентиляцию"
      requirements:
        - type: stat-check
          stat: Streetwise
          dc: 18
      outcomes:
        success: { set-flag: "flag.valchain.infiltration", reward: "intel.militech-layout", reputation: { rep.gang.valentinos: +6 } }
        failure: { trigger: "militech.alert", reputation: { rep.gang.maelstrom: +2 } }
    - option-id: hack-cameras
      text: "Хакнуть камеры"
      requirements:
        - type: stat-check
          stat: Hacking
          dc: 19
          modifiers:
            - source: class.netrunner
              value: +1
      outcomes:
        success: { reward: "program.camera-scrambler", reputation: { rep.gang.valentinos: +7 } }
        failure: { debuff: "overheat", reputation: { rep.gang.valentinos: -4 } }
    - option-id: negotiate
      text: "Перетереть с брокером"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 20
      outcomes:
        success: { cost: "eddies.1000", grant-contract: "valentinos-militech-kickback", reputation: { rep.gang.valentinos: +5 } }
        failure: { set-flag: "flag.valchain.double_cross", reputation: { rep.corp.militech: +4 } }

- node-id: street-race
  label: «Lowrider диверсия»
  entry-condition: state == "street-race"
  speaker-order: ["Jose", "Player", "Vista DJ"]
  player-options:
    - option-id: race-win
      text: "Выиграть гонку"
      requirements:
        - type: stat-check
          stat: Performance
          dc: 18
          modifiers:
            - source: activity.lowrider-race
              value: +1
      outcomes:
        success: { reward: "vehicle.lowrider-upgrade", reputation: { rep.gang.valentinos: +7 }, trigger: "event.militech-diversion" }
        failure: { reputation: { rep.gang.valentinos: -3 } }
    - option-id: hack-drones
      text: "Поджарить дронов"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 19
      outcomes:
        success: { reward: "component.drone-core", reputation: { rep.gang.valentinos: +6 }, set-flag: "flag.valchain.street_race" }
        failure: { debuff: "shock", reputation: { rep.gang.valentinos: -2 } }

- node-id: turf-war-assault
  label: «Штурм завода»
  entry-condition: state == "turf-war"
  speaker-order: ["Jose", "Player", "Maelstrom Sentry"]
  player-options:
    - option-id: lead-charge
      text: "Вести атаку"
      requirements:
        - type: stat-check
          stat: Strategy
          dc: 20
      outcomes:
        success: { unlock_event: "valentinos-turf-counterstrike", reputation: { rep.gang.valentinos: +10 }, reward: "weapon.valentinos-shotgun" }
        failure: { reputation: { rep.gang.valentinos: -5 }, spawn: "maelstrom.heavy-squad" }
    - option-id: crowd-stream
      text: "Поднять стрим"
      requirements:
        - type: stat-check
          stat: Performance
          dc: 17
      outcomes:
        success: { reputation: { rep.social.media: +6, rep.gang.valentinos: +4 } }
        failure: { reputation: { rep.social.media: -3 } }

- node-id: double-cross-hub
  label: «Договор или предательство»
  entry-condition: state == "double-cross"
  speaker-order: ["Player", "NCPD Officer", "Militech Handler", "Jose"]
  player-options:
    - option-id: side-valentinos
      text: "Остаюсь с Valentinos"
      requirements:
        - type: stat-check
          stat: Empathy
          dc: 18
      outcomes:
        success: { set-flag: "flag.valchain.double_cross", reputation: { rep.gang.valentinos: +12 }, clear-flags: ["flag.valentinos.mistrust"] }
        failure: { reputation: { rep.gang.valentinos: -6 } }
    - option-id: side-ncpd
      text: "Сделка с NCPD"
      requirements:
        - type: stat-check
          stat: Deception
          dc: 20
      outcomes:
        success: { reward: "contract.ncpd-ceasefire", reputation: { rep.law.ncpd: +10, rep.gang.valentinos: +3 } }
        failure: { trigger: "event.ncpd-audit", reputation: { rep.law.ncpd: -6 } }
    - option-id: side-maelstrom
      text: "Продать Maelstrom"
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 19
      outcomes:
        success: { set-flag: "flag.valchain.double_cross", reputation: { rep.gang.maelstrom: +8, rep.gang.valentinos: -15 }, reward: "loot.maelstrom-creditstick" }
        failure: { set-flag: "flag.valentinos.exiled", reputation: { rep.gang.valentinos: -20 } }

- node-id: memorial-fiesta
  label: «AR-офренда»
  entry-condition: state == "memorial"
  speaker-order: ["Jose", "Player", "Vista Choir"]
  player-options:
    - option-id: honor-names
      text: "Добавить имена"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 17
      outcomes:
        success: { reward: "item.memorial-charm", reputation: { rep.gang.valentinos: +8 }, set-flag: "flag.valentinos.memorial" }
        failure: { reputation: { rep.gang.valentinos: -3 } }
    - option-id: blackwall-prayer
      text: "Включить Blackwall-гимн"
      requirements:
        - type: stat-check
          stat: Willpower
          dc: 18
      outcomes:
        success: { buff: "valentinos_resolve", reputation: { rep.social.media: +4 } }
        failure: { debuff: "overload", reputation: { rep.gang.valentinos: -2 } }
```

## 4. Таблица проверок D&D

| Узел | Проверка | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|----------|----|--------------|-------|--------|-------------|--------------|
| oath-setup.swear | Intimidation | 18 | `+1` `tattoo.valentinos` | Клятва, +8 реп | Пробный контракт | Медальон, активность | — |
| infiltration-plan.stealth-entry | Streetwise | 18 | — | План склада | Тревога | — | — |
| infiltration-plan.hack-cameras | Hacking | 19 | `+1` Netrunner | Scrambler | Перегрев | — | — |
| infiltration-plan.negotiate | Negotiation | 20 | — | Контракт kickback | Потеря доверия | — | — |
| street-race.race-win | Performance | 18 | `+1` активность гонки | Апгрейд lowrider | -3 реп | — | — |
| street-race.hack-drones | Technical | 19 | — | Drone core | Шок | — | — |
| turf-war-assault.lead-charge | Strategy | 20 | — | Контратака, +10 реп | Maelstrom подкрепления | — | — |
| turf-war-assault.crowd-stream | Performance | 17 | — | Соц-эффект | -реп соц | — | — |
| double-cross-hub.side-valentinos | Empathy | 18 | — | Лояльность семьи | -реп | — | — |
| double-cross-hub.side-ncpd | Deception | 20 | — | Контракт NCPD | Аудит | — | — |
| double-cross-hub.side-maelstrom | Intimidation | 19 | — | Сделка Maelstrom | Изгнание | — | — |
| memorial-fiesta.honor-names | Insight | 17 | — | Амулет памяти | -реп | — | — |
| memorial-fiesta.blackwall-prayer | Willpower | 18 | — | Баф Resolve | Перегрузка | — | — |

## 5. Реакции на события

- **`world.event.heywood_turf_war`** — автоматически переводит сцену в `turf-war`, повышает реплики NPC, активирует внешний баф `valentinos_fury`, увеличивает DC Strategy на +1.
- **`world.event.metro_shutdown`** — добавляет сцену дефицита ресурсов в `infiltration-plan`, Streetwise DC +1, выводит пасхалку про «новый Ever Given».
- **`world.event.blackwall_breach`** — снижает DC `blackwall-prayer` на 1 и добавляет акапеллу AR-гимна.
- **`flag.sqmdl.triple_path`** — открывает скрытые ответы в `double-cross-hub` (опция координации с NCPD).
- **`flag.valentinos.memorial`** — снижает штрафы за провал Empathy/Insight в дальнейших Valentinos сценах.

## 6. Награды и последствия

- **Репутация:** `rep.gang.valentinos` ±25, `rep.gang.maelstrom` -10…+8, `rep.law.ncpd` ±10, `rep.social.media` ±6.
- **Лут/активности:** `token.valentinos-rosary`, `item.valentinos-medallion`, `vehicle.lowrider-upgrade`, `weapon.valentinos-shotgun`, `contract.ncpd-ceasefire`, `contract.maelstrom-data-warfare`, `activity.valentinos-ceremony`, `activity.valentinos-memorial-defense`.
- **Флаги:** `flag.valchain.setup`, `flag.valchain.infiltration`, `flag.valchain.street_race`, `flag.valchain.turf_war`, `flag.valchain.double_cross`, `flag.valchain.memorial`, `flag.valentinos.exiled`.
- **World-state:** влияет на доступ к `npc-jose-tiger-ramirez` (семейные поручения), запускает `valentinos-turf-counterstrike`, может разблокировать `maelstrom-double-cross` ответвления.

## 7. Экспорт (YAML)

```yaml
conversation:
  id: dialogue-quest-side-heywood-valentinos-chain
  entryNodes: [oath-setup]
  states:
    setup:
      requirements: { quest.side.valentinos-chain: "started" }
    infiltration:
      requirements: { flag.valchain.setup: true }
    street-race:
      requirements: { flag.valchain.infiltration: true }
    turf-war:
      requirements: { flag.valchain.street_race: true, world.event.heywood_turf_war: true }
    double-cross:
      requirements: { flag.valchain.turf_war: true }
    memorial:
      requirements: { flag.valchain.double_cross: true }
  nodes:
    oath-setup:
      options:
        - id: swear
          checks:
            - stat: Intimidation
              dc: 18
          success:
            setFlags: [flag.valchain.setup]
            rewards: [token.valentinos-rosary]
            reputation:
              rep.gang.valentinos: 8
          failure:
            contracts: [valentinos-proving-run]
            reputation:
              rep.gang.valentinos: 3
          critSuccess:
            rewards: [item.valentinos-medallion]
            activities: [activity.valentinos-ceremony]
            reputation:
              rep.gang.valentinos: 12
    infiltration-plan:
      options:
        - id: stealth-entry
          checks:
            - stat: Streetwise
              dc: 18
          success:
            setFlags: [flag.valchain.infiltration]
            rewards: [intel.militech-layout]
            reputation:
              rep.gang.valentinos: 6
          failure:
            triggers: [militech.alert]
            reputation:
              rep.gang.maelstrom: 2
        - id: hack-cameras
          checks:
            - stat: Hacking
              dc: 19
          success:
            rewards: [program.camera-scrambler]
            reputation:
              rep.gang.valentinos: 7
          failure:
            debuffs: [overheat]
            reputation:
              rep.gang.valentinos: -4
        - id: negotiate
          checks:
            - stat: Negotiation
              dc: 20
          success:
            costs: [eddies.1000]
            contracts: [valentinos-militech-kickback]
            reputation:
              rep.gang.valentinos: 5
          failure:
            setFlags: [flag.valchain.double_cross]
            reputation:
              rep.corp.militech: 4
    street-race:
      options:
        - id: race-win
          checks:
            - stat: Performance
              dc: 18
          success:
            rewards: [vehicle.lowrider-upgrade]
            events: [event.militech-diversion]
            reputation:
              rep.gang.valentinos: 7
          failure:
            reputation:
              rep.gang.valentinos: -3
        - id: hack-drones
          checks:
            - stat: Technical
              dc: 19
          success:
            setFlags: [flag.valchain.street_race]
            rewards: [component.drone-core]
            reputation:
              rep.gang.valentinos: 6
          failure:
            debuffs: [shock]
            reputation:
              rep.gang.valentinos: -2
    turf-war-assault:
      options:
        - id: lead-charge
          checks:
            - stat: Strategy
              dc: 20
          success:
            events: [valentinos-turf-counterstrike]
            rewards: [weapon.valentinos-shotgun]
            reputation:
              rep.gang.valentinos: 10
          failure:
            spawns: [maelstrom.heavy-squad]
            reputation:
              rep.gang.valentinos: -5
    double-cross-hub:
      options:
        - id: side-valentinos
          checks:
            - stat: Empathy
              dc: 18
          success:
            setFlags: [flag.valchain.double_cross]
            reputation:
              rep.gang.valentinos: 12
          failure:
            reputation:
              rep.gang.valentinos: -6
        - id: side-ncpd
          checks:
            - stat: Deception
              dc: 20
          success:
            contracts: [ncpd-ceasefire]
            reputation:
              rep.law.ncpd: 10
              rep.gang.valentinos: 3
          failure:
            triggers: [event.ncpd-audit]
            reputation:
              rep.law.ncpd: -6
        - id: side-maelstrom
          checks:
            - stat: Intimidation
              dc: 19
          success:
            reputation:
              rep.gang.maelstrom: 8
              rep.gang.valentinos: -15
            rewards: [loot.maelstrom-creditstick]
          failure:
            setFlags: [flag.valentinos.exiled]
            reputation:
              rep.gang.valentinos: -20
    memorial-fiesta:
      options:
        - id: honor-names
          checks:
            - stat: Insight
              dc: 17
          success:
            setFlags: [flag.valchain.memorial]
            rewards: [item.memorial-charm]
            reputation:
              rep.gang.valentinos: 8
          failure:
            reputation:
              rep.gang.valentinos: -3
        - id: blackwall-prayer
          checks:
            - stat: Willpower
              dc: 18
          success:
            buffs: [valentinos_resolve]
            reputation:
              rep.social.media: 4
          failure:
            debuffs: [overload]
            reputation:
              rep.gang.valentinos: -2
```

## 8. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/quest-side-heywood-valentinos-chain` | `GET` | Получить структуру цепочки, активные состояния и узлы |
| `/narrative/dialogues/quest-side-heywood-valentinos-chain/state` | `POST` | Сохранить прогресс (`flag.valchain.*`, репутацию, выданные предметы/активности) |
| `/narrative/dialogues/quest-side-heywood-valentinos-chain/run-check` | `POST` | Выполнить проверки (Intimidation/Streetwise/Hacking/Technical/Performance/Strategy/Empathy/Insight/Willpower) |
| `/narrative/dialogues/quest-side-heywood-valentinos-chain/media` | `POST` | Записать участие в стримах, фиестах и мемориалах, обновить `rep.social.media` |
| `/narrative/dialogues/quest-side-heywood-valentinos-chain/telemetry` | `POST` | Отправить метрики (loyalty, NCPD deals, Maelstrom betrayal, memorial attendance) |

GraphQL `questDialogue(id: "quest-side-heywood-valentinos-chain")` возвращает `QuestDialogueNode` с `valentinosContext` (репутации, текущие флаги, активные контракты) и рекомендациями по следующим миссиям (`npc-jose-tiger-ramirez`, `quest-side-maelstrom-double-cross`).

## 9. Валидация и телеметрия

- `validate-valentinos-chain.ps1` проверяет последовательность состояний, наличие обязательных флагов, корректность ссылок на NPC/контракты и синхронизацию с world events.
- `dialogue-simulator.ps1 -Quest valentinos-chain` прогоняет пути loyal/ncpd/maelstrom, проверяет репутационные изменения и выдачу наград.
- Метрики: `valentinos-loyalty-rate`, `ncpd-ceasefire-rate`, `maelstrom-betrayal-rate`, `street-race-success`, `memorial-participation`. При `valentinos-exile-rate > 10%` создаётся тикет балансировки.
- Интеграции: social-service логирует стримы и мемориалы, economy-service отслеживает меховые сделки, law-service — соглашения с NCPD.

## 10. Связанные материалы

- `../dialogues/npc-jose-tiger-ramirez.md`
- `../dialogues/npc-rita-moreno.md`
- `../dialogues/npc-royce.md`
- `../quests/side/SQ-2020-006-padre-debt.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-framework.md`

## 11. История изменений

- 2025-11-07 21:21 — Создана цепочка Heywood Valentinos: сцены turf-war, double-cross, мемориал; подготовлено для API.

