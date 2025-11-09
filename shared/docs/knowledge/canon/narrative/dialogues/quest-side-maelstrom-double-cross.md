# Диалоги — Side Quest «Maelstrom Double-Cross»

**ID диалога:** `dialogue-quest-side-maelstrom-double-cross`  
**Тип:** quest  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 19:46  
**Приоритет:** высокий  
**Связанные документы:** `../quests/side/SQ-maelstrom-double-cross.md`, `../dialogues/npc-royce.md`, `../dialogues/npc-james-iron-reed.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 19:46
**api-readiness-notes:** «Квест синхронизирован с фракционными сервисами, добавлены экспорт, REST/GraphQL и валидация двойной игры. Готов для API.»

---

## 1. Контекст и цели

- **Сюжет:** игрок должен провести сделку с корпоратами от лица Maelstrom, решая, предать ли банду, сыграть двойную игру или укрепить доверие Ройса.
- **Цель:** добыть чертёж имплантов и определить, кому он достанется (Maelstrom, Militech или игрок/НПД).
- **Интеграции:** репутация `rep.gang.maelstrom`, `rep.corp.militech`, флаги двуличности (`flag.maelstrom.double_agent`), события `maelstrom-underlink-raid`.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Флаги |
|-----------|----------|----------|-------|
| briefing | Ройс поручает операцию | Начало квеста | `flag.sqmdl.briefing` |
| meet-corp | Встреча с контактами Militech | После брифинга | `flag.sqmdl.meet_corp` |
| betrayal | Решение о предательстве | Игрок принимает предложение корпораций | `flag.sqmdl.betrayal` |
| double-agent | Двойная игра | Игрок информирует Maelstrom о сделке | `flag.sqmdl.double_agent` |
| fallout | Развязка | Возврат к Ройсу/NCPD | `flag.sqmdl.fallout` |

- **Проверки:** Intimidation, Deception, Hacking, Technical, Insight.
- **События:** `world.event.corporate_war_escalation` повышает DC корпоративных сцен; `world.event.maelstrom_underlink_raid` добавляет опции для усиления банды.

## 3. Сцены и узлы

### 3.1. YAML-структура

```yaml
nodes:
  - id: briefing
    label: «Поручение Ройса»
    speaker-order: ["Royce", "Player"]
    dialogue:
      - speaker: Royce
        text: "Корпы хотят чип. Мы хотим их деньги и их головы. Ты идёшь." 
      - speaker: Player
        options:
          - id: brief-accept
            text: "Давай детали"
            response:
              trigger-check: { node: "N-2", stat: "Intimidation", dc: 17 }
              outcomes:
                success: { set-flag: "flag.sqmdl.briefing", reward: "credchip.400", reputation: { gang_maelstrom: +5 } }
                failure: { effect: "implant_surge", penalty: "hp_damage", reputation: { gang_maelstrom: -3 } }
          - id: brief-question
            text: "Что за чип?"
            response:
              speaker: Royce
              text: "Импульсный протез. Станет нашим, если не свернёшь."

  - id: meet-corp
    label: «Встреча в грузовом ангаре»
    entry-condition: flag.sqmdl.briefing == true
    speaker-order: ["Militech Broker", "Player"]
    dialogue:
      - speaker: Militech Broker
        text: "Мы заплатим двойную цену. Maelstrom не узнает."
      - speaker: Player
        options:
          - id: corp-intimidate
            text: "Вы платите тройную"
            response:
              trigger-check: { node: "N-11", stat: "Negotiation", dc: 19 }
              outcomes:
                success: { set-flag: "flag.sqmdl.meet_corp", reward: "eddies.1200", reputation: { corp_militech: +8 } }
                failure: { effect: "raise_alert", reputation: { corp_militech: -5 } }
          - id: corp-double
            text: "Я работаю и на Маэлстрём"
            response:
              speaker: Militech Broker
              text: "Тогда сделай вид, что нас не видел."
              outcomes: { default: { set-flag: "flag.sqmdl.double_agent" } }

  - id: temptation
    label: «Рассмотреть чертёж»
    condition: { flag: "flag.sqmdl.meet_corp" }
    speaker-order: ["Inner Voice", "Player"]
    dialogue:
      - speaker: Inner Voice
        text: "Продай и себе, и им. Никто не узнает."
      - speaker: Player
        options:
          - id: temp-open
            text: "Копировать данные"
            response:
              trigger-check: { node: "N-5", stat: "Hacking", dc: 20 }
              outcomes:
                success: { set-flag: "flag.sqmdl.steal", reward: "blueprint.copy", reputation: { corp_militech: +5 } }
                failure: { effect: "data_spike", penalty: "stun", reputation: { corp_militech: -6 } }
          - id: temp-resist
            text: "Не трогать"
            response:
              outcomes: { default: { reputation: { gang_maelstrom: +5 } } }

  - id: betrayal
    label: «Решение о предательстве»
    condition: { flag: "flag.sqmdl.meet_corp" }
    speaker-order: ["Player", "Royce"]
    dialogue:
      - speaker: Player
        options:
          - id: betray-corp
            text: "Сдать Militech»"
            response:
              trigger-check: { node: "N-3", stat: "Deception", dc: 21 }
              outcomes:
                success: { set-flag: "flag.sqmdl.betrayal", reputation: { gang_maelstrom: +10, corp_militech: -20 } }
                failure: { effect: "double_cross_fail", flag: "flag.sqmdl.blacklist", reputation: { gang_maelstrom: -15 } }
          - id: betray-maelstrom
            text: "Отдать чип Militech"
            response:
              trigger-check: { node: "N-3", stat: "Deception", dc: 20 }
              outcomes:
                success: { set-flag: "flag.sqmdl.corp_win", reputation: { corp_militech: +12, gang_maelstrom: -20 } }
                failure: { effect: "caught", reputation: { gang_maelstrom: -12 } }

  - id: fallout
    label: «Последствия»
    speaker-order: ["Royce", "Player", "NCPD Officer"]
    dialogue:
      - speaker: Royce
        text: "Итак, где наш чип?"
      - speaker: Player
        options:
          - id: fallout-loyal
            text: "Чип у Maelstrom"
            response:
              condition: { flag: "flag.sqmdl.betrayal" }
              outcomes:
                default: { reward: "maelstrom-ripper-chip", reputation: { gang_maelstrom: +15 }, set-flag: "flag.sqmdl.success" }
          - id: fallout-corp
            text: "Militech доволен"
            response:
              condition: { flag: "flag.sqmdl.corp_win" }
              outcomes:
                default: { reward: "eddies.2000", reputation: { corp_militech: +15, gang_maelstrom: -25 }, set-flag: "flag.sqmdl.exiled" }
          - id: fallout-double
            text: "Есть кое-что для NCPD"
            response:
              condition: { flag: "flag.sqmdl.double_agent" }
              trigger-check: { node: "N-10", stat: "Insight", dc: 20 }
              outcomes:
                success: { set-flag: "flag.sqmdl.triple", reputation: { law_ncpd: +10, gang_maelstrom: +5, corp_militech: +5 } }
                failure: { effect: "ncpd_investigation", reputation: { law_ncpd: -8 } }
```

### 3.2. Примечания

- Линейка: `briefing → prep-lair → meet-corp → (undernet-heist) → temptation → betrayal → (double-agent) → fallout → media-flash`.
- `undernet-heist` активируется при успешном взломе подготовки; добавляет бонус к копированию и отношениям с Rita.
- `double-agent` требует успешного прохода ветки Insight; без него игрок не получает доступ к NCPD.
- `media-flash` запускается, если игрок просматривает социальные сети в ангаре или проваливает обман Militech/NCPD.
- `world.event.corporate_war_escalation` повышает DC всех сцен с Militech и NCPD на +2, добавляет охранные дроны и новые реплики.

## 4. Таблица проверок

| Узел | Проверка | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|----------|----|--------------|-------|--------|-------------|--------------|
| briefing.brief-accept | Intimidation | 18 | `+1` gear.cyberware-brutal | Допуск, кредчип | Урон импланта, -репутация | Имплант-бустер, +10 репутация | — |
| prep-lair.prep-scan | Perception | 16 | `+1` gear.smart-goggles | Карта ангара | Потеря внимательности | — | — |
| prep-lair.prep-hack | Hacking | 18 | `+2` класс Netrunner | Доступ к undernet | Шок, -репутация | — | — |
| meet-corp.corp-negotiation | Negotiation | 19 | `+1` buff.streetwise_boost | +1500, репутация | Подозрение Militech | — | — |
| meet-corp.corp-threat | Intimidation | 18 | `+1` rep.gang.maelstrom ≥ 40 | Репутация Maelstrom | Слежка | — | — |
| undernet-heist.undernet-siphon | Hacking | 20 | `+1` program.black-ice | Payload данные | Brain burn | — | — |
| temptation.temp-open | Hacking | 20 | `+1` flag.sqmdl.tunnel_favor | Копия Kraken | Перегрев | — | — |
| betrayal.betray-corp | Deception | 21 | `+1` flag.sqmdl.prep | Maelstrom +12 | Blacklist | — | — |
| betrayal.betray-maelstrom | Deception | 20 | `+1` flag.sqmdl.copy | Militech +14 | Hitlist | — | — |
| betrayal.betray-double | Insight | 19 | `+1` rep.law.ncpd ≥ 20 | Тройной путь | Проба NCPD | — | — |
| double-agent.ncpd-deal | Hacking | 18 | `+1` netrunner class | Статус triple | Расследование | — | — |
| fallout.fallout-triple | Insight | 20 | `+2` buff.cover_story | Тройной агент | Аудит | — | — |

---

## 5. Экспорт данных

```yaml
conversation:
  id: dialogue-quest-side-maelstrom-double-cross
  entryNodes: [briefing]
  states:
    briefing:
      requirements:
        quest.side.maelstrom.double: "started"
    prep-lair:
      requirements:
        flag.sqmdl.briefing: true
    meet-corp:
      requirements:
        flag.sqmdl.prep: true
    undernet-heist:
      requirements:
        flag.sqmdl.undernet: true
    temptation:
      requirements:
        flag.sqmdl.meet_corp: true
    betrayal:
      requirements:
        flag.sqmdl.meet_corp: true
    double-agent:
      requirements:
        flag.sqmdl.triple_path: true
    fallout:
      requirements:
        flag.sqmdl.fallout: true
    media-flash:
      requirements:
        flag.sqmdl.media_flash: true
  nodes:
    briefing:
      options:
        - id: brief-accept
          checks:
            - stat: Intimidation
              dc: 18
              modifiers:
                - source: gear.cyberware-brutal
                  value: 1
          success:
            setFlags: [flag.sqmdl.briefing]
            rewards: [credchip.600]
            reputation:
              rep.gang.maelstrom: 6
          failure:
            penalties: [hp_damage]
            reputation:
              rep.gang.maelstrom: -4
          critSuccess:
            rewards: [maelstrom.implant-booster]
            reputation:
              rep.gang.maelstrom: 10
    prep-lair:
      options:
        - id: prep-scan
          checks:
            - stat: Perception
              dc: 16
          success:
            setFlags: [flag.sqmdl.prep]
            rewards: [intel.angarmap]
          failure:
            debuffs: [awareness_drop:90]
        - id: prep-hack
          checks:
            - stat: Hacking
              dc: 18
          success:
            setFlags: [flag.sqmdl.prep, flag.sqmdl.undernet]
            rewards: [program.black-ice]
          failure:
            penalties: [shock]
            reputation:
              rep.gang.maelstrom: -2
        - id: prep-boost
          success:
            setFlags: [flag.sqmdl.prep]
            buffs: [streetwise_boost:1]
    meet-corp:
      options:
        - id: corp-negotiation
          checks:
            - stat: Negotiation
              dc: 19
              modifiers:
                - source: buff.streetwise_boost
                  value: 1
          success:
            setFlags: [flag.sqmdl.meet_corp]
            rewards: [eddies.1500]
            reputation:
              rep.corp.militech: 9
          failure:
            setFlags: [flag.sqmdl.meet_corp]
            penalties: [militech.suspicion]
            reputation:
              rep.corp.militech: -6
        - id: corp-threat
          checks:
            - stat: Intimidation
              dc: 18
          success:
            setFlags: [flag.sqmdl.meet_corp]
            reputation:
              rep.gang.maelstrom: 4
          failure:
            debuffs: [targeted_tracking]
        - id: corp-double
          success:
            setFlags: [flag.sqmdl.double_agent]
    undernet-heist:
      options:
        - id: undernet-siphon
          checks:
            - stat: Hacking
              dc: 20
          success:
            setFlags: [flag.sqmdl.temptation]
            rewards: [data.payload]
            reputation:
              rep.corp.militech: 5
          failure:
            penalties: [brain_burn:120]
        - id: undernet-reroute
          success:
            setFlags: [flag.sqmdl.tunnel_favor]
            reputation:
              rep.informants.rita: 4
    temptation:
      options:
        - id: temp-open
          checks:
            - stat: Hacking
              dc: 20
              modifiers:
                - source: flag.sqmdl.tunnel_favor
                  value: 1
          success:
            setFlags: [flag.sqmdl.copy]
            rewards: [blueprint.copy]
            reputation:
              rep.corp.militech: 6
          failure:
            penalties: [stun]
            triggers: [militech.overheat]
            reputation:
              rep.corp.militech: -8
        - id: temp-resist
          success:
            reputation:
              rep.gang.maelstrom: 6
    betrayal:
      options:
        - id: betray-corp
          checks:
            - stat: Deception
              dc: 21
              modifiers:
                - source: flag.sqmdl.prep
                  value: 1
          success:
            setFlags: [flag.sqmdl.betrayal, flag.sqmdl.fallout]
            grants: [activity.maelstrom-heist]
            reputation:
              rep.gang.maelstrom: 12
              rep.corp.militech: -18
          failure:
            setFlags: [flag.sqmdl.blacklist, flag.sqmdl.fallout]
            reputation:
              rep.gang.maelstrom: -14
        - id: betray-maelstrom
          checks:
            - stat: Deception
              dc: 20
              modifiers:
                - source: flag.sqmdl.copy
                  value: 1
          success:
            setFlags: [flag.sqmdl.corp_win, flag.sqmdl.fallout]
            grants: [contract.militech-blackops]
            reputation:
              rep.corp.militech: 14
              rep.gang.maelstrom: -22
          failure:
            penalties: [maelstrom.hitlist]
            reputation:
              rep.gang.maelstrom: -12
        - id: betray-double
          checks:
            - stat: Insight
              dc: 19
          success:
            setFlags: [flag.sqmdl.triple_path, flag.sqmdl.fallout]
            reputation:
              rep.law.ncpd: 6
              rep.gang.maelstrom: 4
              rep.corp.militech: 4
          failure:
            triggers: [ncpd.probe]
            reputation:
              rep.law.ncpd: -6
    double-agent:
      options:
        - id: ncpd-deal
          checks:
            - stat: Hacking
              dc: 18
          success:
            setFlags: [flag.sqmdl.triple]
            rewards: [eddies.800]
            reputation:
              rep.law.ncpd: 12
          failure:
            penalties: [ncpd.investigation]
            reputation:
              rep.law.ncpd: -10
        - id: ncpd-bounce
          success:
            reputation:
              rep.law.ncpd: -4
    fallout:
      options:
        - id: fallout-loyal
          conditions:
            - flag.sqmdl.betrayal: true
          success:
            setFlags: [flag.sqmdl.success]
            rewards: [maelstrom-ripper-chip]
            reputation:
              rep.gang.maelstrom: 18
        - id: fallout-corp
          conditions:
            - flag.sqmdl.corp_win: true
          success:
            setFlags: [flag.sqmdl.exiled]
            rewards: [eddies.2500]
            reputation:
              rep.corp.militech: 16
              rep.gang.maelstrom: -25
        - id: fallout-triple
          conditions:
            - flag.sqmdl.triple: true
          checks:
            - stat: Insight
              dc: 20
              modifiers:
                - source: buff.cover_story
                  value: 2
          success:
            setFlags: [flag.sqmdl.triple_agent]
            rewards: [activity.triple-weave]
            reputation:
              rep.law.ncpd: 12
              rep.gang.maelstrom: 6
              rep.corp.militech: 6
          failure:
            triggers: [ncpd.audit]
            reputation:
              rep.law.ncpd: -9
        - id: fallout-personal
          conditions:
            - flag.sqmdl.copy: true
          success:
            setFlags: [flag.sqmdl.personal]
            rewards: [blueprint.black-market]
    media-flash:
      options:
        - id: media-loyal
          success:
            reputation:
              rep.gang.maelstrom: 2
        - id: media-corp
          success:
            reputation:
              rep.corp.militech: 2
              rep.social.media: 1
        - id: media-meme
          success:
            reputation:
              rep.corp.militech: -3
              rep.social.media: 4
            codex: social.evergiven-meme
  activities:
    - id: activity.maelstrom-heist
      unlockedBy: flag.sqmdl.betrayal
      description: "Серия налётов Maelstrom на склады Militech"
    - id: contract.militech-blackops
      unlockedBy: flag.sqmdl.corp_win
      description: "Тайные операции Militech против Maelstrom"
    - id: activity.triple-weave
      unlockedBy: flag.sqmdl.triple_agent
      description: "Задачи NCPD по манипуляции корпов и банд"
```

> Экспорт генерируется `

## 7. Валидация и телеметрия

- `validate-maelstrom-double.ps1` сверяет поток флагов (`briefing → prep → meet → temptation → betrayal → fallout`), активность undernet, статусы blacklist/exiled и выдачу активностей.
- `dialogue-simulator.ps1 -Scenario maelstrom-double-cross` прогоняет ветки loyal/corp/double/triple/personal, проверяет выдачу предметов, штрафов и медийных эффектов.
- Метрики: `maelstrom-loyalty-rate`, `militech-deal-rate`, `maelstrom-triple-agent-rate`, `maelstrom-personal-hoard-rate`, `maelstrom-meme-rate`, `blacklist-rate`. При meme-rate >40% social-service запускает контент-фильтр, при blacklist-rate >20% создаётся тикет балансировки.
- Интеграции: social-service сохраняет `media-flash` истории, economy-service отслеживает продажу `blueprint.black-market`, law-service реагирует на `ncpd.audit` события.

## 8. Реакции и последствия

- **Maelstrom Loyalty:** `flag.sqmdl.success` → `rep.gang.maelstrom +18`, активируется `activity.maelstrom-heist`, повышается шанс участия в `maelstrom_underlink_raid`.
- **Militech Deal:** `flag.sqmdl.corp_win` → `rep.corp.militech +16`, Maelstrom заносит игрока в `flag.sqmdl.exiled`, появляется цепочка Militech Black Ops.
- **Triple Agent:** `flag.sqmdl.triple_agent` даёт доступ к `activity.triple-weave`, открывает совместные операции NCPD/Militech с пасхалками на расследования 2020-х.
- **Personal Hoard:** `flag.sqmdl.personal` активирует чёрный рынок имплантов, повышает риск `ncpd.audit`, но даёт уникальные моды для имплантов.
- **Blacklist:** `flag.sqmdl.blacklist` и `flag.sqmdl.exiled` блокируют Maelstrom-квесты до прохождения реставрационной миссии; social-service снижает `rep.social.media` при частых мемах против Militech.