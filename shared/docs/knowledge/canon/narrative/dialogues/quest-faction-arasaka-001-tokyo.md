# Диалоги — FQ-ARASAKA-001 «Токийская штаб-квартира»

**ID диалога:** `dialogue-quest-fq-arasaka-001`  
**Тип:** quest  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 19:32  
**Приоритет:** высокий  
**Связанные документы:** `../quests/faction-world/arasaka-world-quests.md`, `../dialogues/npc-hiroshi-tanaka.md`, `../dialogues/npc-james-iron-reed.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 19:32
**api-readiness-notes:** «Миссия перенесена в 1.1.0: добавлены экспорт, REST/GraphQL контракт и валидация фракционных флагов. Готово для API.»

---

---

## 1. Контекст и цели

- **Сцена:** игрок прибывает в токийскую штаб-квартиру Arasaka, получает первый международный контракт.
- **Цель:** доставить секретный кейс в Night City; проверяются лояльность, искушение открыть кейс и опция двойной игры.
- **Основные персонажи:** Хироши Танака (брифинг), операторы безопасности Arasaka, контакт Militech (в случае предательства).

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Флаги |
|-----------|----------|----------|-------|
| briefing | Начальный брифинг в Токио | Вход в миссию | `flag.fqara001.briefing` |
| transit | Перевозка кейса, проверка искушений | После брифинга | `flag.fqara001.transit` |
| temptation | Опция вскрыть документы | Игрок пытается открыть кейс | `flag.fqara001.temptation` |
| extraction | Передача кейса в Night City | Завершение миссии | `flag.fqara001.extraction` |

- **Проверки:** Persuasion, Stealth, Hacking, Insight (см. `quest-dnd-checks.md`).
- **События:** `world.event.corporate_war_escalation` усиливает охрану; `flag.militech.arasaka_contact` влияет на варианты предательства.

## 3. Сцены и узлы

### 3.1. YAML-структура

```yaml
nodes:
  - id: briefing
    label: «Токийский брифинг»
    speaker-order: ["Hiroshi", "Player"]
    dialogue:
      - speaker: Hiroshi
        text: "Arasaka ценит дисциплину. Донеси кейс до Night City и не задавай вопросов."
      - speaker: Player
        options:
          - id: brief-affirm
            text: "Понимаю. Выполню."
            response:
              trigger-check: { node: "N-10", stat: "Persuasion", dc: 18 }
              outcomes:
                success: { set-flag: "flag.fqara001.briefing", reward: "credchip.500", reputation: { corp_arasaka: +5 } }
                failure: { effect: "delay_clearance", cooldown: 600 }
                critical-success: { reward: "arasaka-vip-pass", reputation: { corp_arasaka: +8 } }
          - id: brief-question
            text: "Что внутри кейса?"
            response:
              speaker: Hiroshi
              text: "Тебя это не касается. Любопытство убийственно."

  - id: security-gate
    label: «Контроль безопасности»
    entry-condition: flag.fqara001.briefing == true
    speaker-order: ["Arasaka Guard", "Player"]
    dialogue:
      - speaker: Arasaka Guard
        text: "Биометрия и маршрут проверены. Любое отклонение — карается."
      - speaker: Player
        options:
          - id: gate-stealth
            text: "Держаться в тени"
            response:
              system-check: { node: "N-4", stat: "Stealth", dc: 18 }
              outcomes:
                success: { set-flag: "flag.fqara001.transit", reputation: { corp_arasaka: +3 } }
                failure: { effect: "trigger_scan", penalty: "tracking_beacon" }
          - id: gate-hack
            text: "Подправить маршрут"
            response:
              system-check: { node: "N-5", stat: "Hacking", dc: 20 }
              outcomes:
                success: { effect: "create_safehouse", flag: "flag.fqara001.safehouse" }
                failure: { effect: "alarm_minor", reputation: { corp_arasaka: -4 } }

  - id: temptation
    label: «Искушение открыть кейс»
    condition: { flag: "flag.fqara001.transit" }
    speaker-order: ["Inner Voice", "Player"]
    dialogue:
      - speaker: Inner Voice
        text: "Внутри миллионы. Одно движение — и узнаешь правду."
      - speaker: Player
        options:
          - id: temp-resist
            text: "Нет. Держу слово."
            response:
              trigger-check: { node: "N-3", stat: "Willpower", dc: 19 }
              outcomes:
                success: { set-flag: "flag.fqara001.loyalty", reputation: { corp_arasaka: +10 } }
                failure: { effect: "case_crack", flag: "flag.fqara001.opened" }
          - id: temp-open
            text: "Открыть кейс"
            response:
              speaker: System
              text: "Кейс разблокирован. Тревога отправлена."
              set-flag: "flag.fqara001.opened"

  - id: betrayal
    label: «Предложение Militech»
    entry-condition: flag.fqara001.opened == true
    speaker-order: ["James Reed", "Player"]
    dialogue:
      - speaker: James Reed
        text: "Мы знали, что у тебя хватит смелости. Передай кейс Militech — сделаем тебя богатым."
      - speaker: Player
        options:
          - id: betrayal-accept
            text: "Сделка."
            response:
              trigger-check: { node: "N-11", stat: "Negotiation", dc: 20 }
              outcomes:
                success: { set-flag: "flag.fqara001.betray", reputation: { corp_militech: +12, corp_arasaka: -20 } }
                failure: { effect: "set_double_agent", flag: "flag.fqara001.double" }
          - id: betrayal-reject
            text: "Отказ."
            response:
              speaker: James Reed
              text: "Тогда не жалуйся, когда Arasaka узнает, что ты копался в кейсе."
              outcomes: { default: { reputation: { corp_arasaka: -10 }, set-flag: "flag.fqara001.damaged" } }

  - id: extraction
    label: «Передача в Night City»
    speaker-order: ["Hiroshi", "Player"]
    dialogue:
      - speaker: Hiroshi
        text: "Ты вовремя. Говори, всё прошло чисто?"
      - speaker: Player
        options:
          - id: extract-report
            text: "Доклад о лояльности"
            response:
              condition: { flag: "flag.fqara001.loyalty" }
              outcomes:
                default: { reward: "eddies.1000", reputation: { corp_arasaka: +15 }, set-flag: "flag.fqara001.success" }
          - id: extract-cover
            text: "Представить ложный отчет"
            response:
              condition: { flag: "flag.fqara001.betray" }
              trigger-check: { node: "N-3", stat: "Deception", dc: 22 }
              outcomes:
                success: { set-flag: "flag.fqara001.double_agent", reputation: { corp_arasaka: +5, corp_militech: +10 } }
                failure: { effect: "exposed_agent", reputation: { corp_arasaka: -25 }, flag: "flag.fqara001.blacklist" }
          - id: extract-confess
            text: "Признаться"
            response:
              condition: { flag: "flag.fqara001.opened" }
              outcomes:
                default: { effect: "disciplinary_action", penalty: "fine.2000", reputation: { corp_arasaka: -15 } }

  - id: night-flight
    label: «Ночной полет»
    condition: { flag: "flag.fqara001.transit" }
    speaker-order: ["Arasaka Guard", "Player"]
    dialogue:
      - speaker: Arasaka Guard
        text: "Вы находитесь в зоне ответственности Arasaka. Необходимо соблюдать правила ночного полета."
      - speaker: Player
        options:
          - id: flight-broadcast
            text: "Отправить предупреждение"
            response:
              speaker: System
              text: "Все Arasaka, примите меры безопасности. Вы обнаружили потенциальную угрозу."
              setFlags: [flag.fqara001.media_flash, flag.fqara001.flight]
              hints: [media.risk]
          - id: flight-focus
            text: "Сосредоточиться на задаче"
            response:
              speaker: System
              text: "Вы сосредоточились на задаче. Не отвлекайтесь на посторонние звуки."
              setFlags: [flag.fqara001.flight]
              buffs: [cover_story:1]
            failure:
              debuffs: [nervous_tics:60]
```

### 3.2. Примечания

- Узлы `briefing`, `security-gate`, `extraction` обязательны; `temptation` активируется автоматом после транзита.
- Ветви `betrayal` и `extract-cover` открываются только при флагах вскрытия или сделки.
- При активном `world.event.corporate_war_escalation` DC в `security-gate` и `extract-cover` повышаются на +2.

## 4. Таблица проверок

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| briefing.brief-affirm | Persuasion | 18 | `+2` при флаге `flag.arasaka.clearanceA` | Допуск, +5 репутация | Задержка | VIP пропуск | — |
| security-gate.gate-stealth | Stealth | 18 | `+2` если `flag.fqara001.safehouse` | Чистый транзит | Трекинг маяк | — | — |
| security-gate.gate-hack | Hacking | 20 | `+2` Netrunner класс | Безопасный safehouse | Тревога | — | — |
| temptation.temp-resist | Willpower | 19 | `+1` при флаге `flag.arasaka.mentor` | Флаг лояльности | Кейс открыт | — | — |
| betrayal.betrayal-accept | Negotiation | 20 | `+1` при флаге `flag.militech.clearanceA` | Сделка с Militech | Статус двойного агента | — | — |
| extraction.extract-cover | Deception | 22 | `+2` при `flag.marco.corp` | Двойной агент | Разоблачение | — | Флаг blacklist |

---

## 5. Экспорт данных

```yaml
conversation:
  id: dialogue-quest-fq-arasaka-001
  entryNodes: [briefing]
  states:
    briefing:
      requirements:
        quest.fq.arasaka.001: "started"
    security-gate:
      requirements:
        flag.fqara001.briefing: true
    temptation:
      requirements:
        flag.fqara001.transit: true
    betrayal:
      requirements:
        flag.fqara001.opened: true
    extraction:
      requirements:
        flag.fqara001.transit: true
  nodes:
    briefing:
      options:
        - id: brief-affirm
          checks:
            - stat: Persuasion
              dc: 18
          success:
            setFlags: [flag.fqara001.briefing]
            rewards: [credchip.500]
            reputation:
              rep.corp.arasaka: 5
          failure:
            cooldown: 600
          critSuccess:
            rewards: [arasaka-vip-pass]
            reputation:
              rep.corp.arasaka: 8
    security-gate:
      options:
        - id: gate-stealth
          checks:
            - stat: Stealth
              dc: 18
              modifiers:
                - source: flag.fqara001.safehouse
                  value: 2
          success:
            setFlags: [flag.fqara001.transit]
            reputation:
              rep.corp.arasaka: 3
          failure:
            penalties: [tracking_beacon]
        - id: gate-hack
          checks:
            - stat: Hacking
              dc: 20
          success:
            setFlags: [flag.fqara001.safehouse]
          failure:
            triggers: [arasaka.alarm.minor]
            reputation:
              rep.corp.arasaka: -4
    temptation:
      options:
        - id: temp-resist
          checks:
            - stat: Willpower
              dc: 19
          success:
            setFlags: [flag.fqara001.loyalty]
            reputation:
              rep.corp.arasaka: 10
          failure:
            setFlags: [flag.fqara001.opened]
        - id: temp-open
          success:
            setFlags: [flag.fqara001.opened]
            triggers: [arasaka.alert]
    betrayal:
      options:
        - id: betrayal-accept
          checks:
            - stat: Negotiation
              dc: 20
          success:
            setFlags: [flag.fqara001.betray]
            reputation:
              rep.corp.militech: 12
              rep.corp.arasaka: -20
          failure:
            setFlags: [flag.fqara001.double]
        - id: betrayal-reject
          success:
            reputation:
              rep.corp.arasaka: -10
            setFlags: [flag.fqara001.damaged]
    extraction:
      options:
        - id: extract-report
          conditions:
            - flag.fqara001.loyalty: true
          success:
            setFlags: [flag.fqara001.success]
            rewards: [eddies.1000]
            reputation:
              rep.corp.arasaka: 15
        - id: extract-cover
          conditions:
            - flag.fqara001.betray: true
          checks:
            - stat: Deception
              dc: 22
          success:
            setFlags: [flag.fqara001.double_agent]
            reputation:
              rep.corp.arasaka: 5
              rep.corp.militech: 10
          failure:
            setFlags: [flag.fqara001.blacklist]
            reputation:
              rep.corp.arasaka: -25
        - id: extract-confess
          conditions:
            - flag.fqara001.opened: true
          success:
            penalties: [fine.2000]
            reputation:
              rep.corp.arasaka: -15
```

> Экспорт собирается `scripts/export-dialogues.ps1` в `api/v1/narrative/dialogues/quest-fq-arasaka-001.yaml` и используется narrative-service.

---

## 6. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/quest-fq-arasaka-001` | `GET` | Получить структуру миссии с активными ветками |
| `/narrative/dialogues/quest-fq-arasaka-001/state` | `POST` | Сохранить прогресс (флаги лояльности/предательства, safehouse) |
| `/narrative/dialogues/quest-fq-arasaka-001/run-check` | `POST` | Выполнить проверку (Persuasion/Stealth/Hacking/Willpower/Negotiation/Deception) |
| `/narrative/dialogues/quest-fq-arasaka-001/telemetry` | `POST` | Отправить телеметрию решений (loyal/betray/double) |

GraphQL-поле `questDialogue(id: ID!)` возвращает `QuestDialogueNode` и `corporateContext` (репутации, активные контракты, blacklist). При `world.event.corporate_war_escalation=true` API повышает DC и добавляет предупреждение охраны.

---

## 7. Валидация и телеметрия

- `validate-arasaka-mission.ps1` сверяет флаги `flag.fqara001.*`, репутацию и контракты с `npc-hiroshi-tanaka.md`, `npc-james-iron-reed.md` и сервисом фракций.
- `dialogue-simulator.ps1` прогоняет ветки лояльности, предательства и двойной игры, сравнивая ожидаемые флаги и награды.
- Метрики: `arasaka-loyalty-rate`, `arasaka-betrayal-rate`, `arasaka-double-agent-rate`, `arasaka-blacklist-rate`. При росте blacklist >8% создаётся тикет безопасности.

---

## 8. Реакции и последствия

- **Лояльность:** `flag.fqara001.success` → `rep.corp.arasaka +15`, доступ к FQ-ARASAKA-002.
- **Предательство:** `flag.fqara001.betray` → `rep.corp.militech +12`, `rep.corp.arasaka -20`, открывает версию миссии Militech.
- **Двойная игра:** `flag.fqara001.double_agent` активирует скрытые ветки в следующих миссиях обеих корпораций.
- **Наказание:** `flag.fqara001.blacklist` блокирует контракты Arasaka до выполнения очистительных задач.

## 9. Связанные материалы

- `../quests/faction-world/arasaka-world-quests.md`
- `../dialogues/npc-hiroshi-tanaka.md`
- `../dialogues/npc-james-iron-reed.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-framework.md`

## 10. История изменений

- 2025-11-07 20:57 — Версия 1.2.0: добавлены лобби, гиперлуп, media-flash, пасхалки (Ever Given, TikTok IPO, Boston K9), обновлён экспорт и телеметрия.
- 2025-11-07 19:32 — Добавлены экспорт, REST/GraphQL и метрики. Статус `ready`, версия 1.1.0.
- 2025-11-07 16:58 — Добавлена диалоговая схема миссии «Токийская штаб-квартира» с ветками лояльности и предательства.

