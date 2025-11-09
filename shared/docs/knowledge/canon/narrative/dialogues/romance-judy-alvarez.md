# Романтические сцены — Judy Alvarez (Этапы 1-2)

**ID диалога:** `dialogue-romance-judy`  
**Тип:** romance  
**Статус:** approved  
**Версия:** 1.3.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 21:35  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/judy-alvarez.md`, `../dialogues/npc-viktor-vektor.md`, `../../02-gameplay/social/romance-system.md`, `../../02-gameplay/social/reputation-formulas.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/romance  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 21:35
**api-readiness-notes:** «Версия 1.3.0: добавлен этап 3 с Braindance синхронизацией, пасхалками и обновлённым YAML/REST/телеметрией.»

---

---

## 1. Контекст и цели

- **Этап 1:** Ночная студия брейндансов Lizzie's Bar после совместной защиты Мокси.
- **Этап 2:** AR-прогулка по затопленной Laguna Bend с живым аудиомиксом от Джуди и трансляцией через social-service.
- **Этап 3:** Подземный VR-архив в старом техцентре Мокси (бывшая станция hyperloop) — глубинная брайнданс-синхронизация и проект нового safehouse.
- **Цели:** Укрепить доверие, показать уязвимость Джуди, дать игроку выбор между личными чувствами, активизмом Мокси и демо-революцией брейндансов.
- **Интеграции:** `rep.romance.judy`, `flag.romance.judy.stage*`, `flag.moxx.support`, `world.event.maelstrom_underlink_raid`, `world.event.corporate_war_escalation`.

## 2. Состояния и условия

| Этап | Состояние | Описание | Триггеры | Флаги |
|------|-----------|----------|----------|-------|
| Stage1 | studio-intro | Приглашение в студию | `rep.moxx ≥ 15`, `flag.moxx.support == true` | `flag.romance.judy.stage0` |
| Stage1 | trust-share | Разговор о справедливости | После `studio-intro` | `flag.romance.judy.stage0` |
| Stage1 | bd-demo | Совместный брейнданс | После `trust-share` | `flag.romance.judy.bd` |
| Stage1 | branch-decision | Выбор пути | Завершение сцены | `flag.romance.judy.choice1` |
| Stage2 | town-entry | Подготовка к погружению | `flag.romance.judy.path_*` | `flag.romance.judy.stage1-complete` |
| Stage2 | memory-sync | Синхронизация воспоминаний | После `town-entry` | `flag.romance.judy.sync` |
| Stage2 | rooftop-epilogue | Решение после погружения | После `memory-sync` | `flag.romance.judy.choice2` |
| Stage3 | underground-lab | Спуск в VR-лабораторию Мокси | `flag.romance.judy.path_*` и `flag.romance.judy.stage1-complete` | `flag.romance.judy.stage2-complete` |
| Stage3 | sync-handoff | Braindance-синхронизация активистов | После `underground-lab` | `flag.romance.judy.bd_sync` |
| Stage3 | future-blueprint | План нового safehouse и брайнданс-революции | После `sync-handoff` | `flag.romance.judy.stage3-decision` |

- **Проверки:** Stage1 — Empathy, Deception, Technical; Stage2 — Empathy, Performance, Hacking, Insight; Stage3 — Hacking, Empathy, Performance, Negotiation, Willpower.
- **Пасхалки:** Отсылки к Laguna Bend из 2020-х, мем про «Hyperloop karaoke 2072», архив BrainDance Idol.

## 3. Структура диалога

### 3.1 Этап 1 — Студия Lizzie's Bar

```
nodes:
  - id: studio-intro
    label: «Ночная студия»
    speaker-order: ["Judy", "Player"]
    dialogue:
      - speaker: Judy
        text: "Раз уж мы спасли девчонок — у меня есть запись, которую никто не видел. Хочешь?"
      - speaker: Player
        options:
          - id: accept
            text: "Я с тобой"
            response:
              set-flag: "flag.romance.judy.stage0"
              outcomes: { default: { reputation: { romance_judy: +4 } } }
          - id: refuse
            text: "Не сейчас"
            response:
              speaker: Judy
              text: "Ладно. Но ночи такие редкие."
              outcomes: { default: { reputation: { romance_judy: -4 } } }

  - id: trust-share
    label: «Правда о справедливости»
    entry-condition: flag.romance.judy.stage0 == true
    speaker-order: ["Judy", "Player"]
    dialogue:
      - speaker: Judy
        text: "Скажи честно: зачем ты защищаешь тех, кого никто не слушает?"
      - speaker: Player
        options:
          - id: honest
            text: "Потому что так правильно"
            response:
              trigger-check: { node: "N-3", stat: "Empathy", dc: 18, modifiers: [{ source: "item.romance-judy-tip", value: +2 }] }
              outcomes:
                success: { set-flag: "flag.romance.judy.truth", reputation: { romance_judy: +6 } }
                failure: { effect: "doubt", reputation: { romance_judy: -3 } }
          - id: deflect
            text: "Это выгодно"
            response:
              trigger-check: { node: "N-3", stat: "Deception", dc: 20 }
              outcomes:
                success: { set-flag: "flag.romance.judy.hide", reputation: { romance_judy: +2 } }
                failure: { effect: "trust_drop", reputation: { romance_judy: -6 } }

  - id: bd-demo
    label: «Совместный брейнданс»
    entry-condition: flag.romance.judy.stage0 == true
    speaker-order: ["Judy", "Player"]
    dialogue:
      - speaker: Judy
        text: "Это старые воспоминания. Они могут задеть."
      - speaker: Player
        options:
          - id: bd-experience
            text: "Я готов"
            response:
              trigger-check: { node: "N-2", stat: "Technical", dc: 17 }
              outcomes:
                success: { set-flag: "flag.romance.judy.bd", reputation: { romance_judy: +5 } }
                failure: { effect: "motion_sickness", penalty: "stun", reputation: { romance_judy: -2 } }
          - id: bd-skip
            text: "Лучше словами"
            response:
              speaker: Judy
              text: "Слова — тоже правда."
              outcomes: { default: { reputation: { romance_judy: +3 } } }

  - id: branch-decision
    label: «Что дальше?»
    entry-condition: flag.romance.judy.stage0 == true
    speaker-order: ["Judy", "Player"]
    dialogue:
      - speaker: Judy
        text: "Ночи меняют жизнь. Что для тебя эта ночь?"
      - speaker: Player
        options:
          - id: stay
            text: "Остаться и помочь"
            response:
              condition: { flag.romance.judy.truth: true }
              outcomes:
                default: { set-flag: "flag.romance.judy.path_trust", reputation: { romance_judy: +7 } }
          - id: comfort
            text: "Просто быть рядом"
            response:
              condition: { flag.romance.judy.bd: true }
              outcomes:
                default: { set-flag: "flag.romance.judy.path_comfort", reputation: { romance_judy: +6 } }
          - id: retreat
            text: "Работа громче чувств"
            response:
              outcomes:
                default: { set-flag: "flag.romance.judy.path_slow", reputation: { romance_judy: +3 } }
```

### 3.2 Этап 2 — Laguna Bend AR

```
nodes:
  - id: town-entry
    label: «Переезд в Laguna Bend»
    entry-condition: flag.romance.judy.path_trust or flag.romance.judy.path_comfort or flag.romance.judy.path_slow
    speaker-order: ["Judy", "Player"]
    dialogue:
      - speaker: Judy
        text: "Я собрала AR-слой. Laguna Bend снова 2023-й, пока мы держим сигнал."
      - speaker: Player
        options:
          - id: promise-support
            text: "Я здесь, чтобы удержать тебя в настоящем"
            response:
              trigger-check: { node: "N-7", stat: "Empathy", dc: 19, modifiers: [{ source: "flag.romance.judy.path_trust", value: +1 }] }
              outcomes:
                success: { set-flag: "flag.romance.judy.stage1-complete", reputation: { romance_judy: +5 } }
                failure: { effect: "emotional_distance", reputation: { romance_judy: -4 } }
          - id: lighten-mood
            text: "А Hyperloop караоке ещё играет под водой?"
            response:
              trigger-check: { node: "N-7", stat: "Performance", dc: 17 }
              outcomes:
                success: { set-flag: "flag.romance.judy.humor", reputation: { romance_judy: +4 } }
                failure: { effect: "awkward", reputation: { romance_judy: -2 } }

  - id: memory-sync
    label: «Синхронизация воспоминаний»
    entry-condition: flag.romance.judy.stage1-complete == true
    speaker-order: ["Judy", "Player"]
    dialogue:
      - speaker: Judy
        text: "Хочу, чтобы ты увидел лагуну моими глазами."
      - speaker: Player
        options:
          - id: dive-deep
            text: "Веди. Я держу канал"
            response:
              trigger-check: { node: "N-8", stat: "Hacking", dc: 20, modifiers: [{ source: "class.netrunner", value: +1 }, { source: "flag.romance.judy.humor", value: +1 }] }
              outcomes:
                success: { set-flag: "flag.romance.judy.sync", reputation: { romance_judy: +7 }, rewards: { buff: "sync-awareness", duration: 600 } }
                failure: { effect: "blackwall_noise", reputation: { romance_judy: -5 } }
          - id: focus-emotions
            text: "Расскажи голосом, без сети"
            response:
              trigger-check: { node: "N-8", stat: "Insight", dc: 18 }
              outcomes:
                success: { set-flag: "flag.romance.judy.sync", reputation: { romance_judy: +6 } }
                failure: { effect: "miss_connection", reputation: { romance_judy: -3 } }

  - id: rooftop-epilogue
    label: «Крыша Lizzie’s»
    entry-condition: flag.romance.judy.sync == true
    speaker-order: ["Judy", "Player"]
    dialogue:
      - speaker: Judy
        text: "Стоит ли делиться лагуной с Мокси или оставить только нам?"
      - speaker: Player
        options:
          - id: share-with-moxx
            text: "Пусть у Мокси будет надежда"
            response:
              outcomes:
                default: { set-flag: "flag.romance.judy.path_public", reputation: { romance_judy: +8 }, reputationBonus: { rep.moxx: +6 } }
          - id: keep-private
            text: "Это наш секрет"
            response:
              outcomes:
                default: { set-flag: "flag.romance.judy.path_private", reputation: { romance_judy: +7 } }
          - id: plan-future
            text: "Построим новый safehouse"
            response:
              outcomes:
                default: { set-flag: "flag.romance.judy.path_future", reputation: { romance_judy: +9 }, grant_contract: "moxx-safehouse-upgrade" }
```

### 3.3 Этап 3 — Подземная VR лаборатория Мокси

```
- node-id: underground-lab
  label: «Техцентр под Lizzie’s»
  entry-condition: flag.romance.judy.stage2-complete == true
  speaker-order: ["Judy", "Player", "Moxx Tech"]
  player-options:
    - option-id: stabilize-sync
      text: "Стабилизировать сетевой канал"
      requirements:
        - type: stat-check
          stat: Hacking
          dc: 21
          modifiers:
            - source: class.netrunner
              value: +2
      outcomes:
        success: { set-flag: "flag.romance.judy.bd_sync", reputation: { romance_judy: +7 }, reward: "buff.sync-harmony" }
        failure: { effect: "signal_glitch", penalty: "stun:5", reputation: { romance_judy: -4 } }
    - option-id: comfort-judy
      text: "Поддержать её голосом"
      requirements:
        - type: stat-check
          stat: Empathy
          dc: 19
      outcomes:
        success: { set-flag: "flag.romance.judy.bd_sync", reputation: { romance_judy: +6 } }
        failure: { effect: "emotional_miss", reputation: { romance_judy: -3 } }

- node-id: sync-handoff
  label: «Передача манифеста»
  entry-condition: flag.romance.judy.bd_sync == true
  speaker-order: ["Judy", "Player", "Moxx Recorder"]
  player-options:
    - option-id: record-bd
      text: "Записать новую брайнданс сессию"
      requirements:
        - type: stat-check
          stat: Performance
          dc: 18
      outcomes:
        success: { set-flag: "flag.romance.judy.bd_manifest", reputation: { romance_judy: +6 }, grantActivity: "moxx-bd-revolution" }
        failure: { effect: "bd_overload", reputation: { romance_judy: -4 } }
    - option-id: negotiate-release
      text: "Договориться о выпуске"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 19
      outcomes:
        success: { set-flag: "flag.romance.judy.bd_release", reputation: { romance_judy: +5 }, reputationBonus: { rep.moxx: +4 } }
        failure: { effect: "producer_pushback", reputation: { romance_judy: -3 } }

- node-id: future-blueprint
  label: «Схема будущего»
  entry-condition: flag.romance.judy.bd_sync == true
  speaker-order: ["Judy", "Player"]
  player-options:
    - option-id: vow-public
      text: "Пусть эта сессия вдохновит всех"
      requirements:
        - type: stat-check
          stat: Performance
          dc: 19
      outcomes:
        success: { set-flag: "flag.romance.judy.stage3-public", reputation: { romance_judy: +9 }, reputationBonus: { rep.moxx: +6 }, add-flags: ["flag.romance.judy.stage3-decision"] }
        failure: { effect: "public-backlash", reputation: { romance_judy: -5 } }
    - option-id: vow-private
      text: "Сохраним это только для нас"
      requirements:
        - type: stat-check
          stat: Willpower
          dc: 18
      outcomes:
        success: { set-flag: "flag.romance.judy.stage3-private", reputation: { romance_judy: +8 }, add-flags: ["flag.romance.judy.stage3-decision"] }
        failure: { effect: "doubt_private", reputation: { romance_judy: -4 } }
    - option-id: vow-rebuild
      text: "Поможем Мокси построить новый safehouse"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 20
      outcomes:
        success: { set-flag: "flag.romance.judy.stage3-rebuild", grantContract: "moxx-archive-hub", add-flags: ["flag.romance.judy.stage3-decision"], reputation: { romance_judy: +10 }, reputationBonus: { rep.moxx: +5 } }
        failure: { effect: "construction_delays", reputation: { romance_judy: -5 } }
```

### 3.4 Реакции на события

| Этап | Триггер | Статистика | DC | Модификаторы | Бафф/Эффект | Штраф/Пенальти | Сцена | Описание |
|------|----------|-----------|----|--------------|--------------|---------------|--------|----------|
| Stage1 | studio-intro | Empathy | 15 | `+1` `flag.moxx.support` | Флаг stage0 | −4 | Сцена «Studio intro» | Эмоциональный откат |
| Stage1 | trust-share | Empathy | 18 | `+2` `item.romance-judy-tip` | Флаг truth | +6 | Сцена «Truth or Dare» | Усиление доверия |
| Stage1 | bd-demo | Technical | 17 | `+1` класс Netrunner | Флаг bd | +5 | Сцена «BD demo» | Успех в брайндансе |
| Stage1 | branch-decision | — | — | — | — | — | Сцена «Branch decision» | Выбор пути |
| Stage2 | town-entry | Empathy | 19 | `+1` `flag.romance.judy.path_trust` | Флаг stage1-complete | +5 | Сцена «Laguna sunrise» | Успех в погружении |
| Stage2 | memory-sync | Hacking | 20 | `+1` класс Netrunner, `+1` `flag.romance.judy.humor` | Флаг sync, баф sync-awareness | +7 | Сцена «Laguna sunrise» | Успех в синхронизации |
| Stage2 | memory-sync.focus-emotions | Insight | 18 | `+1` при `path_slow` | Флаг sync | −3 | Сцена «Laguna sunrise» | Эмоциональный откат |
| Stage3 | underground-lab.stabilize-sync | Hacking | 21 | `+2` класс Netrunner | Баф sync-harmony | Статус glitch | Доп. баф «Deep Sync» | Netwatch маяк |
| Stage3 | underground-lab.comfort-judy | Empathy | 19 | `+1` `flag.romance.judy.path_comfort` | Флаг bd_sync | -3 | Сцена «Heartbeat Duo» | Разрыв связи |
| Stage3 | sync-handoff.record-bd | Performance | 18 | `+1` `flag.romance.judy.humor` | Активность BD | -4 | Пасхалка «BrainDance Idol» | — |
| Stage3 | sync-handoff.negotiate-release | Negotiation | 19 | `+1` `rep.moxx ≥ 25` | Репутация Мокси | -3 | Контракт «BD Liberation» | Отказ продюсеров |
| Stage3 | future-blueprint.vow-public | Performance | 19 | `+1` `flag.romance.judy.bd_manifest` | Публичный путь | -5 | Запись «Public Anthem» | Засада хейтеров |
| Stage3 | future-blueprint.vow-private | Willpower | 18 | `+1` `flag.romance.judy.path_private` | Приватный путь | -4 | — | — |
| Stage3 | future-blueprint.vow-rebuild | Negotiation | 20 | `+1` `flag.romance.judy.bd_release` | Контракт safehouse | -5 | Ускоренный билд | Арест подрядчика |

- **`world.event.maelstrom_underlink_raid`:** +1 DC к проверкам Stage2, реплика Джуди «Maelstrom снова тестит наши границы».
- **`world.event.corporate_war_escalation`:** включает дополнительную реплику о давлении корпораций и даёт бонусные награды для выбора `plan-future`.
- **`world.event.metro_shutdown`:** снижает DC Performance/Negotiation на Stage3 на 1 (фокус на восстановлении инфраструктуры) и добавляет пасхалку `hyperloop-relief`.
- **`event.netwatch-survey`:** при провале Stage3 запускает побочный квест `netwatch-cleanup` и снижает `rep.corp.arasaka` на 2.

## 4. Экспорт (YAML)

```yaml
conversation:
  id: romance-judy-stage1
  entryNodes: [studio-intro]
  states:
    studio-intro:
      requirements:
        rep.moxx: ">=15"
        flag.moxx.support: true
    trust-share:
      requirements:
        flag.romance.judy.stage0: true
    bd-demo:
      requirements:
        flag.romance.judy.stage0: true
    branch-decision:
      requirements:
        flag.romance.judy.stage0: true
  nodes:
    studio-intro:
      options:
        - id: accept
          success:
            setFlags: [flag.romance.judy.stage0]
            reputation:
              romance_judy: 4
        - id: refuse
          success:
            reputation:
              romance_judy: -4
    trust-share:
      options:
        - id: honest
          checks:
            - stat: Empathy
              dc: 18
              modifiers:
                - source: item.romance-judy-tip
                  value: 2
          success:
            setFlags: [flag.romance.judy.truth]
            reputation:
              romance_judy: 6
          failure:
            reputation:
              romance_judy: -3
        - id: deflect
          checks:
            - stat: Deception
              dc: 20
          success:
            setFlags: [flag.romance.judy.hide]
            reputation:
              romance_judy: 2
          failure:
            reputation:
              romance_judy: -6
    bd-demo:
      options:
        - id: bd-experience
          checks:
            - stat: Technical
              dc: 17
              modifiers:
                - source: class.netrunner
                  value: 1
          success:
            setFlags: [flag.romance.judy.bd]
            reputation:
              romance_judy: 5
          failure:
            penalties: [stun]
            reputation:
              romance_judy: -2
        - id: bd-skip
          success:
            reputation:
              romance_judy: 3
    branch-decision:
      options:
        - id: stay
          conditions:
            - flag.romance.judy.truth: true
          success:
            setFlags: [flag.romance.judy.path_trust]
            reputation:
              romance_judy: 7
        - id: comfort
          conditions:
            - flag.romance.judy.bd: true
          success:
            setFlags: [flag.romance.judy.path_comfort]
            reputation:
              romance_judy: 6
        - id: retreat
          success:
            setFlags: [flag.romance.judy.path_slow]
            reputation:
              romance_judy: 3
```

```yaml
conversation:
  id: romance-judy-stage2
  entryNodes: [town-entry]
  states:
    town-entry:
      requirements:
        flag.romance.judy.path_trust: true
      fallbackRequirements:
        flag.romance.judy.path_comfort: true
      secondaryFallbackRequirements:
        flag.romance.judy.path_slow: true
    memory-sync:
      requirements:
        flag.romance.judy.stage1-complete: true
    rooftop-epilogue:
      requirements:
        flag.romance.judy.sync: true
  nodes:
    town-entry:
      options:
        - id: promise-support
          checks:
            - stat: Empathy
              dc: 19
          success:
            setFlags: [flag.romance.judy.stage1-complete]
            reputation:
              romance_judy: 5
          failure:
            reputation:
              romance_judy: -4
        - id: lighten-mood
          checks:
            - stat: Performance
              dc: 17
          success:
            setFlags: [flag.romance.judy.humor]
            reputation:
              romance_judy: 4
          failure:
            reputation:
              romance_judy: -2
    memory-sync:
      options:
        - id: dive-deep
          checks:
            - stat: Hacking
              dc: 20
          success:
            setFlags: [flag.romance.judy.sync]
            rewards:
              - buff: sync-awareness
                duration: 600
            reputation:
              romance_judy: 7
          failure:
            penalties:
              - blackwall_noise
            reputation:
              romance_judy: -5
        - id: focus-emotions
          checks:
            - stat: Insight
              dc: 18
          success:
            setFlags: [flag.romance.judy.sync]
            reputation:
              romance_judy: 6
          failure:
            reputation:
              romance_judy: -3
    rooftop-epilogue:
      options:
        - id: share-with-moxx
          success:
            setFlags: [flag.romance.judy.path_public]
            reputation:
              romance_judy: 8
            reputationBonus:
              rep.moxx: 6
        - id: keep-private
          success:
            setFlags: [flag.romance.judy.path_private]
            reputation:
              romance_judy: 7
        - id: plan-future
          success:
            setFlags: [flag.romance.judy.path_future]
            grantContract: moxx-safehouse-upgrade
            reputation:
              romance_judy: 9
```

```yaml
conversation:
  id: romance-judy-stage3
  entryNodes: [underground-lab]
  states:
    underground-lab:
      requirements:
        flag.romance.judy.stage2-complete: true
    sync-handoff:
      requirements:
        flag.romance.judy.bd_sync: true
    future-blueprint:
      requirements:
        flag.romance.judy.bd_sync: true
  nodes:
    underground-lab:
      options:
        - id: stabilize-sync
          checks:
            - stat: Hacking
              dc: 21
          success:
            setFlags: [flag.romance.judy.bd_sync]
            rewards: [buff.sync-harmony]
            reputation:
              romance_judy: 7
          failure:
            penalties: [signal_glitch]
            reputation:
              romance_judy: -4
        - id: comfort-judy
          checks:
            - stat: Empathy
              dc: 19
          success:
            setFlags: [flag.romance.judy.bd_sync]
            reputation:
              romance_judy: 6
          failure:
            reputation:
              romance_judy: -3
    sync-handoff:
      options:
        - id: record-bd
          checks:
            - stat: Performance
              dc: 18
          success:
            setFlags: [flag.romance.judy.bd_manifest]
            activities: [moxx-bd-revolution]
            reputation:
              romance_judy: 6
          failure:
            penalties: [bd_overload]
            reputation:
              romance_judy: -4
        - id: negotiate-release
          checks:
            - stat: Negotiation
              dc: 19
          success:
            setFlags: [flag.romance.judy.bd_release]
            reputation:
              romance_judy: 5
              rep.moxx: 4
          failure:
            penalties: [producer_pushback]
            reputation:
              romance_judy: -3
    future-blueprint:
      options:
        - id: vow-public
          checks:
            - stat: Performance
              dc: 19
          success:
            setFlags: [flag.romance.judy.stage3-public, flag.romance.judy.stage3-decision]
            reputation:
              romance_judy: 9
              rep.moxx: 6
          failure:
            penalties: [public-backlash]
            reputation:
              romance_judy: -5
        - id: vow-private
          checks:
            - stat: Willpower
              dc: 18
          success:
            setFlags: [flag.romance.judy.stage3-private, flag.romance.judy.stage3-decision]
            reputation:
              romance_judy: 8
          failure:
            penalties: [doubt_private]
            reputation:
              romance_judy: -4
        - id: vow-rebuild
          checks:
            - stat: Negotiation
              dc: 20
          success:
            setFlags: [flag.romance.judy.stage3-rebuild, flag.romance.judy.stage3-decision]
            contracts: [moxx-archive-hub]
            reputation:
              romance_judy: 10
              rep.moxx: 5
          failure:
            penalties: [construction_delays]
            reputation:
              romance_judy: -5
```

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/romance/dialogues/judy/stage1` | `GET` | Вернуть сценарий студии с активными ветками |
| `/romance/dialogues/judy/stage1/run-check` | `POST` | Прогнать проверки Empathy/Deception/Technical |
| `/romance/dialogues/judy/stage1/state` | `POST` | Сохранить флаги `stage0`, `path_*`, репутацию |
| `/romance/dialogues/judy/stage2` | `GET` | Вернуть AR-сцену Laguna Bend |
| `/romance/dialogues/judy/stage2/run-check` | `POST` | Обработать проверки Empathy/Performance/Hacking/Insight |
| `/romance/dialogues/judy/stage2/state` | `POST` | Сохранить флаги `stage1-complete`, `sync`, `path_*`, контракты |
| `/romance/dialogues/judy/stage3` | `GET` | Получить сцену подземной VR лаборатории |
| `/romance/dialogues/judy/stage3/run-check` | `POST` | Проверки Hacking/Empathy/Performance/Negotiation/Willpower |
| `/romance/dialogues/judy/stage3/state` | `POST` | Сохранить флаги `bd_sync`, `stage3-decision`, контракты и активности |
| `/romance/dialogues/judy/telemetry` | `POST` | Сводная телеметрия по обоим этапам |

GraphQL `romanceDialogue(id: ID!, stage: Int)` возвращает `RomanceDialogueNode` с `studioContext`, `lagunaContext`, `undergroundContext`, `stageMetrics`, активными бафами и списком выданных контрактов.

## 6. Валидация и телеметрия

- `scripts/validate-romance-flags.ps1` сверяет `flag.romance.judy.*`, `flag.moxx.*`, контракты и события.
- `scripts/dialogue-simulator.ps1 -Scenario romance-judy` прогоняет пути `path_trust`, `path_comfort`, `path_slow`, а также финальные варианты `path_public`, `path_private`, `path_future`.
- Метрики: `romance-judy-stage1-success-rate` (цель ≥70%), `romance-judy-stage2-sync-rate` (цель ≥60%), `romance-judy-stage3-public-private-rebuild`, `romance-judy-bd-revolution-uptake`. Два провала Stage3 подряд → миссия `support-moxx-2078`.

## 7. Награды и последствия

- **Репутация:** `rep.romance.judy` до +48, бонус к `rep.moxx` при публичных и rebuild решениях.
- **Бафы:** `sync-awareness`, `laguna-overdrive`, `sync-harmony` (Stage3 Stabilize).
- **Контракты/активности:** `moxx-safehouse-upgrade`, `moxx-archive-hub`, `moxx-bd-revolution` (активность).
- **Флаги:** `flag.romance.judy.stage0`, `flag.romance.judy.path_*`, `flag.romance.judy.stage1-complete`, `flag.romance.judy.sync`, `flag.romance.judy.stage2-complete`, `flag.romance.judy.bd_*`, `flag.romance.judy.stage3-{public|private|rebuild}`, `flag.romance.judy.stage3-decision`.

## 8. Связанные материалы

- `../npc-lore/important/judy-alvarez.md`
- `../dialogues/npc-viktor-vektor.md`
- `../../02-gameplay/social/romance-system.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-2060-2077.md`

## 9. История изменений

- 2025-11-07 21:35 — Версия 1.3.0: добавлен этап 3 (подземная VR лаборатория), новые проверки, YAML/REST/телеметрия.
- 2025-11-07 19:26 — Добавлен этап 2, обновлены API и метрики.
- 2025-11-07 18:32 — Расширен экспорт, REST/GraphQL блок и метрики этапа 1.
- 2025-11-07 17:13 — Создана романтическая сцена Judy (этап 1).

