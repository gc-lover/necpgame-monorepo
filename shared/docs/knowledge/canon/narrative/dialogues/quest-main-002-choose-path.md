# Диалоги — Квест 1.2 «Выбор пути»

**ID диалога:** `dialogue-quest-main-002-choose-path`  
**Тип:** quest  
**Статус:** approved  
**Версия:** 1.2.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 20:50  
**Приоритет:** высокий  
**Связанные документы:** `../quests/main/002-choose-path-dnd-nodes.md`, `../dialogues/npc-hiroshi-tanaka.md`, `../dialogues/npc-jose-tiger-ramirez.md`, `../dialogues/npc-sara-miller.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 20:50
**api-readiness-notes:** «Диалог выбора пути разветвлён: сцены для корпораций, улиц, закона и фриланса с проверками, пасхалками и полным экспортом YAML/REST.»

---

---

## 1. Контекст и цели

- **Сцена:** игрок выбирает направление развития: корпорации, бандитские улицы или независимый путь.
- **Цель:** через диалоговые сцены с ключевыми NPC закрепить выбор и активировать соответствующие ветки.
- **Участники:** Марко Санчес (модератор), Хироши Танака (Arasaka), Хосе «Тигр» Рамирес (Valentinos), Сара Миллер (NCPD, независимый гарант), а также представители независимых (диспетчер Nomad Convoy по коммлинку).

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Флаги |
|-----------|----------|----------|-------|
| council-setup | Предварительный круглый стол у Марко | Начало квеста | `flag.main002.council` |
| corp-track | Переговоры с Хироши | Игрок выбирает корпорацию | `flag.main002.corp` |
| gang-track | Встреча с Хосе | Игрок выбирает улицы | `flag.main002.gang` |
| law-track | Проверка с Сарой (для независимых и NCPD) | Игрок выбирает закон/независимость | `flag.main002.law` |
| freelance-brief | Коммлинк с Nomad диспетчером | Игрок подтверждает независимость | `flag.main002.freelance` |

- **Проверки:** Узлы N-10, N-11, N-12 из `002-choose-path-dnd-nodes.md`.
- **Репутация:** `rep.corp.arasaka`, `rep.gang.valentinos`, `rep.law.ncpd`, `rep.freelance.global`.

## 3. Сцены и узлы

### 3.1. YAML-структура

```yaml
nodes:
  - id: council
    label: "Стол Марко"
    speaker-order: ["Marco", "Player", "Crowd"]
    dialogue:
      - speaker: Marco
        text: "Ты прошёл обучение. Теперь решай, кто ты в Night City: офисный киберсамурай, уличное сердце, значок NCPD или свободный конвой."
      - speaker: Crowd
        text: "#WatsonWalk стримит совет в прямом эфире." 
      - speaker: Player
        options:
          - id: choose-corp
            text: "Мне нужен корпоративный лифт"
            response:
              speaker: Marco
              text: "Лифт идёт в Arasaka Tower. Надеюсь, ты не боишься высоты и NDA длиннее Суэцкого канала."
              set-flag: "flag.main002.corp"
          - id: choose-gang
            text: "Улицы — моя семья"
            response:
              speaker: Marco
              text: "Тогда готовься к марьячи и клятвам под неоном Heywood."
              set-flag: "flag.main002.gang"
          - id: choose-law
            text: "Хочу действовать по правилам"
            response:
              speaker: Marco
              text: "NCPD ждет людей с нервами мощнее, чем у дронов NYPD 2023-го."
              set-flag: "flag.main002.law"
          - id: choose-freelance
            text: "Я независимый Nomad"
            response:
              speaker: Marco
              text: "Свобода? Ты подпишешь больше документов, чем TikTok подписчиков за час. Nomad диспетчер уже на частоте."
              set-flag: "flag.main002.freelance"
          - id: choose-hold
            text: "Дай мне минуту словить вайб"
            response:
              speaker: Marco
              text: "Вдох-выдох. Но город не ставит паузу."

  - id: corp-track
    condition: { flag: "flag.main002.corp" }
    label: "Переговоры с Arasaka"
    speaker-order: ["Hiroshi", "Player", "Assistant AI"]
    dialogue:
      - speaker: Hiroshi
        text: "Arasaka приветствует тех, кто приносит результат и не вспоминает про корпоративные мемы 2021 года."
      - speaker: Assistant AI
        text: "Воспроизвожу баржу Ever Given… шутка. Пожалуйста, избегайте мемов." 
      - speaker: Player
        options:
          - id: corp-persuade
            text: "Мои кейсы закрывались быстрее, чем TikTok банит стримы."
            response:
              system-check: { node: "N-10", stat: "Persuasion", dc: 18, modifiers: [{ source: "credential.corporate", value: +2 }, { source: "flag.main001.target_marked", value: +1 }] }
              success-line: "Ваш профиль впечатляет. Clearance A активирован, муст-гос режим."
              failure-line: "Корпоративный аудит требует ещё доказательств."
              critical-success-line: "Совет назначает вас на проект 'Kyoto Skyline' — легендарный слот."
              critical-failure-line: "Система безопасности вносит вас в watch-list вместе с журналистами из 2020-х."
              outcomes:
                success: { set-flag: "flag.arasaka.clearanceA", reputation: { rep.corp.arasaka: 10 }, grant: "contract.arasaka-entry" }
                failure: { grant: ["contract.corp-basic"], reputation: { rep.corp.arasaka: 2 } }
                critical-success: { grant: ["contract.arasaka-serenity", "item.corp.access-pass"], reputation: { rep.corp.arasaka: 14 }, codex: "arasaka.innovation-lab" }
                critical-failure: { set-flag: "flag.arasaka.watchlist", debuff: "corp_clearance_delay:1800", reputation: { rep.corp.arasaka: -8 } }
          - id: corp-hack
            text: "Можно ли ускорить процесс через Blackwall?"
            response:
              speaker: Hiroshi
              text: "Если вы опять скажете слово Blackwall, вам придётся обсудить это с Netwatch."
              outcomes:
                default: { codex: "netwatch.protocols", reputation: { rep.corp.arasaka: -1 } }
          - id: corp-ask
            text: "Какие бонусы у корпоративного пути?"
            response:
              speaker: Hiroshi
              text: "Доступ к ресурсам, страховка класса Platinum и возможность ошибаться только один раз."

  - id: gang-track
    condition: { flag: "flag.main002.gang" }
    label: "Клятва Valentinos"
    speaker-order: ["Jose", "Player", "Choir"]
    dialogue:
      - speaker: Jose
        text: "Valentinos — семья. Мы не забываем ни свадеб, ни похорон, ни мемов про Mariachi NFT."
      - speaker: Choir
        text: "Mariachi.wav воспроизводится в 8D."
      - speaker: Player
        options:
          - id: gang-intimidate
            text: "Я уже пролил кровь за Heywood."
            response:
              system-check: { node: "N-11", stat: "Intimidation", dc: 17, modifiers: [{ source: "tattoo.valentinos", value: +1 }, { source: "flag.main001.stealth_clear", value: +1 }] }
              success-line: "В твоём голосе улицы. Семья тебя примет."
              failure-line: "Слова — пустые креды."
              critical-success-line: "Твоё имя добавляют в список Mariachi 2090."
              critical-failure-line: "Ты привёл за собой дрона NCPD? Что ж, проверим твою честность."
              outcomes:
                success: { set-flag: "flag.valentinos.oath", reputation: { rep.gang.valentinos: 10 }, grant: "activity.valentinos-caper" }
                failure: { contracts: ["contract.valentinos-trial"], reputation: { rep.gang.valentinos: 3 } }
                critical-success: { items: ["item.valentinos-medallion"], reputation: { rep.gang.valentinos: 14 }, codex: "valentinos.history" }
                critical-failure: { set-flag: "flag.valentinos.suspect", spawn: "valentinos.shadow-tail", reputation: { rep.gang.valentinos: -8 } }
          - id: gang-loyalty
            text: "Я ищу семью, а не прибыль."
            response:
              speaker: Jose
              text: "Тогда получи семейный чат и список тех, кто должен креды."
              outcomes:
                default: { grant: "contract.valentinos-family", reputation: { rep.gang.valentinos: 6 } }
          - id: gang-ask
            text: "Каковы правила?"
            response:
              speaker: Jose
              text: "Не предавай, не скрывай, не забудь принести pan dulce на собрание." 

  - id: law-track
    condition: { flag: "flag.main002.law" }
    label: "Комиссия NCPD"
    speaker-order: ["Sara", "Player", "Internal Affairs"]
    dialogue:
      - speaker: Sara
        text: "Добро пожаловать в бюро paperwork. Заполнишь формы быстрее, чем Boston Dynamics выпустит нового пса?"
      - speaker: Internal Affairs
        text: "Напоминание: честность — лучшая броня."
      - speaker: Player
        options:
          - id: law-honesty
            text: "Я хочу защищать людей, даже когда они не видят."
            response:
              system-check: { node: "N-10", stat: "Persuasion", dc: 18, modifiers: [{ source: "flag.main001.tech_open", value: +1 }] }
              success-line: "Ваши мотивы звучат убедительно. Badge ждет на стойке."
              failure-line: "Комиссия сомневается. Вы пойдёте на патруль."
              critical-success-line: "Вас кидают на горячее дело 'Docklands 2.0'."
              critical-failure-line: "Система фиксирует несостыковки — вы под наблюдением Internal Affairs."
              outcomes:
                success: { set-flag: "flag.ncpd.badge", reputation: { rep.law.ncpd: 10 }, grant: "contract.ncpd-intro" }
                failure: { contracts: ["contract.ncpd-patrol"], reputation: { rep.law.ncpd: 3 } }
                critical-success: { contracts: ["contract.ncpd-cybercrime-taskforce"], items: ["item.ncpd.data-key"], reputation: { rep.law.ncpd: 14 } }
                critical-failure: { set-flag: "flag.ncpd.review", reputation: { rep.law.ncpd: -6 }, debuff: "bureaucracy_hold:1200" }
          - id: law-protocols
            text: "Мне нужны протоколы, пока я не подписал."
            response:
              speaker: Sara
              text: "Патрульные камеры пишут всё. Подписывайся — или возвращайся к Марко."
          - id: law-meme
            text: "Есть ли дресс-код для робособак?"
            response:
              speaker: Sara
              text: "Вам выдадут стикеры 'Copilot 2075'. Не разбрасывайтесь."

  - id: freelance-track
    condition: { flag: "flag.main002.freelance" }
    label: "Связь с Nomad Convoy"
    speaker-order: ["Nomad Dispatcher", "Player", "Convoy AI"]
    dialogue:
      - speaker: Nomad Dispatcher
        text: "Говорит Convoy 77. Дорога свободна, пока корпорации спорят за контейнеры."
      - speaker: Convoy AI
        text: "Погода Badlands: солнечно, шанс песчаных бурь 40%." 
      - speaker: Player
        options:
          - id: freelance-commit
            text: "Нет цепей — только дороги"
            response:
              system-check: { node: "N-12", stat: "ClassChoice", dc: 0, class-bonus: { netrunner: +2, techie: +2, solo: +1 } }
              success-line: "Мы добавили тебя в список 'Freeway Friends'. Маршрут и частоты у тебя в импланте."
              failure-line: "Нужны ещё рекомендации. Отправляем тебя на тренировку."
              critical-success-line: "Получаешь 'Nomad Priority Pass' и грузовик с мемператором 2045."
              outcomes:
                success: { set-flag: "flag.freelance.contract", reputation: { rep.freelance.global: 8 }, grant: "activity.nomad-convoy" }
                failure: { contracts: ["contract.freelance-training"], reputation: { rep.freelance.global: 2 } }
                critical-success: { set-flag: "flag.freelance.priority", items: ["item.nomad-pass"], reputation: { rep.freelance.global: 12 } }
          - id: freelance-map
            text: "Нужна карта безопасных стоянок"
            response:
              speaker: Nomad Dispatcher
              text: "Отправляю сводку: кафе 'Vanlife 2045', станция 'Solar Highway', подкаст про кофе."
          - id: freelance-warning
            text: "Что делать, если корп дрон начнут преследовать?"
            response:
              speaker: Convoy AI
              text: "Отправить мем с баржей Ever Given и уйти на частоту 77.7."

  - id: media-flash
    label: "Вспышка медиа"
    condition: { flag: "flag.main002.media_flash" }
    speaker-order: ["News Anchor", "Player"]
    dialogue:
      - speaker: News Anchor
        text: "Breaking News! #ChooseYourPath: игрок сделал выбор — {faction}. Сравнение с реальностью 2020-х прилагается."
      - speaker: Player
        options:
          - id: media-wave
            text: "Поставить реакцию"
            response:
              speaker: News Anchor
              text: "Реакция добавлена. Реклама доставка 15 минут — бесплатно."
              outcomes:
                default: { reputation: { rep.social.media: +2 } }
          - id: media-ignore
            text: "Выключить уведомления"
            response:
              speaker: News Anchor
              text: "Окей. Но #WatsonWalk всё равно найдёт тебя."

  - id: wrap
    label: "Итог совета"
    speaker-order: ["Marco", "Player", "HUD"]
    dialogue:
      - speaker: Marco
        text: "Решение принято. Не пытайся нажать Ctrl+Z."
      - speaker: HUD
        text: "Репутация обновлена, контракты выданы, телеметрия отправлена."
      - speaker: Player
        options:
          - id: wrap-affirm
            text: "Я готов"
            response:
              speaker: Marco
              text: "Добро пожаловать в новую реальность."
              outcomes: { finalize: "main002", update-world: true, set-flag: "flag.main002.media_flash" }
          - id: wrap-joke
            text: "Могу ли я поменять решение?"
            response:
              speaker: Marco
              text: "Конечно. В следующей жизни."
              outcomes: { finalize: "main002", update-world: true, set-flag: "flag.main002.media_flash" }
```

### 3.2. Логика переходов

- Активируется только одна основная ветка (corp/gang/law/freelance) согласно выбору.
- После завершения выбранной ветки устанавливается соответствующий флаг и квест завершает сценой `wrap`.
- Репутационные изменения передаются в системы социального сервиса.

---

## 4. Экспорт данных

```yaml
conversation:
  id: dialogue-quest-main-002-choose-path
  entryNodes: [council]
  states:
    council:
      requirements:
        quest.main.001: "completed"
    corp-track:
      requirements:
        flag.main002.corp: true
    gang-track:
      requirements:
        flag.main002.gang: true
    law-track:
      requirements:
        flag.main002.law: true
    freelance-track:
      requirements:
        flag.main002.freelance: true
    media-flash:
      requirements:
        flag.main002.media_flash: true
    wrap:
      requirements:
        anyOf:
          - { flag.main002.corp: true }
          - { flag.main002.gang: true }
          - { flag.main002.law: true }
          - { flag.main002.freelance: true }
  nodes:
    council:
      options:
        - id: choose-corp
          success:
            setFlags: [flag.main002.corp]
        - id: choose-gang
          success:
            setFlags: [flag.main002.gang]
        - id: choose-law
          success:
            setFlags: [flag.main002.law]
        - id: choose-freelance
          success:
            setFlags: [flag.main002.freelance]
        - id: choose-hold
          success:
            tutorial: council.take-a-breath
    corp-track:
      options:
        - id: corp-persuade
          checks:
            - stat: Persuasion
              dc: 18
              modifiers:
                - source: credential.corporate
                  value: 2
                - source: flag.main001.target_marked
                  value: 1
          success:
            setFlags: [flag.arasaka.clearanceA]
            reputation:
              rep.corp.arasaka: 10
            contracts: [contract.arasaka-entry]
          failure:
            contracts: [contract.corp-basic]
            reputation:
              rep.corp.arasaka: 2
          critSuccess:
            contracts: [contract.arasaka-serenity]
            reputation:
              rep.corp.arasaka: 14
            items: [item.corp.access-pass]
          critFailure:
            setFlags: [flag.arasaka.watchlist]
            debuffs: [corp_clearance_delay:1800]
            reputation:
              rep.corp.arasaka: -8
        - id: corp-hack
          success:
            reputation:
              rep.corp.arasaka: -1
        - id: corp-ask
          success:
            codex: arasaka.benefits
    gang-track:
      options:
        - id: gang-intimidate
          checks:
            - stat: Intimidation
              dc: 17
              modifiers:
                - source: tattoo.valentinos
                  value: 1
                - source: flag.main001.stealth_clear
                  value: 1
          success:
            setFlags: [flag.valentinos.oath]
            reputation:
              rep.gang.valentinos: 10
            activities: [activity.valentinos-caper]
          failure:
            contracts: [contract.valentinos-trial]
            reputation:
              rep.gang.valentinos: 3
          critSuccess:
            items: [item.valentinos-medallion]
            reputation:
              rep.gang.valentinos: 14
          critFailure:
            setFlags: [flag.valentinos.suspect]
            reputation:
              rep.gang.valentinos: -8
            spawns: [valentinos.shadow-tail]
        - id: gang-loyalty
          success:
            contracts: [contract.valentinos-family]
            reputation:
              rep.gang.valentinos: 6
        - id: gang-ask
          success:
            codex: valentinos.rules
    law-track:
      options:
        - id: law-honesty
          checks:
            - stat: Persuasion
              dc: 18
              modifiers:
                - source: flag.main001.tech_open
                  value: 1
          success:
            setFlags: [flag.ncpd.badge]
            reputation:
              rep.law.ncpd: 10
            contracts: [contract.ncpd-intro]
          failure:
            contracts: [contract.ncpd-patrol]
            reputation:
              rep.law.ncpd: 3
          critSuccess:
            contracts: [contract.ncpd-cybercrime-taskforce]
            items: [item.ncpd.data-key]
            reputation:
              rep.law.ncpd: 14
          critFailure:
            setFlags: [flag.ncpd.review]
            debuffs: [bureaucracy_hold:1200]
            reputation:
              rep.law.ncpd: -6
        - id: law-protocols
          success:
            codex: ncpd.protocols
        - id: law-meme
          success:
            items: [item.ncpd.copilot-sticker]
    freelance-track:
      options:
        - id: freelance-commit
          checks:
            - stat: ClassChoice
              dc: 0
              class-bonus:
                netrunner: 2
                techie: 2
                solo: 1
          success:
            setFlags: [flag.freelance.contract]
            reputation:
              rep.freelance.global: 8
            activities: [activity.nomad-convoy]
          failure:
            contracts: [contract.freelance-training]
            reputation:
              rep.freelance.global: 2
          critSuccess:
            setFlags: [flag.freelance.priority]
            items: [item.nomad-pass]
            reputation:
              rep.freelance.global: 12
        - id: freelance-map
          success:
            codex: nomad.safe-stops
        - id: freelance-warning
          success:
            hint: convoy.drone-counter
    media-flash:
      options:
        - id: media-wave
          success:
            reputation:
              rep.social.media: 2
        - id: media-ignore
          success:
            hint: media.notifications-muted
    wrap:
      options:
        - id: wrap-affirm
          success:
            finalize: main002
            updateWorld: true
            setFlags: [flag.main002.media_flash]
        - id: wrap-joke
          success:
            finalize: main002
            updateWorld: true
            setFlags: [flag.main002.media_flash]
```

> Экспорт формируется `scripts/export-dialogues.ps1` и сохраняется в `api/v1/narrative/dialogues/quest-main-002.yaml`.

---

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/quest-main-002` | `GET` | Вернуть структуру выбора пути и активные ветки |
| `/narrative/dialogues/quest-main-002/state` | `POST` | Сохранить прогресс (`flag.main002.*`, репутации, выданные контракты) |
| `/narrative/dialogues/quest-main-002/run-check` | `POST` | Выполнить проверку (Persuasion/Intimidation/ClassChoice) и вернуть исход |
| `/narrative/dialogues/quest-main-002/media` | `POST` | Записать реакцию игрока в HUD-истории (`flag.main002.media_flash`, `rep.social.media`) |
| `/narrative/dialogues/quest-main-002/telemetry` | `POST` | Отправить телеметрию распределения фракций, критических провалов и медиапокрытия |

GraphQL-поле `questDialogue(id: ID!)` возвращает `QuestDialogueNode` с `factionContext` (репутации, активные флаги, доступные миссии) и сообщает рекомендованные квесты следующей цепочки.

---

## 6. Валидация и телеметрия

- `validate-faction-choice.ps1` сверяет флаги `flag.main002.*`, фракционные бонусы, контракты и наличие HUD-сцены `media-flash`.
- `dialogue-simulator.ps1 -Scenario choose-path` прогоняет ветки corp/gang/law/freelance, проверяет выдачу предметов, штрафов и регистрацию watch-list.
- Метрики: `faction-choice-distribution`, `faction-check-success`, `faction-watchlist-rate`, `media-flash-engagement` (ожидаем ≥60% реакции). При превышении watchlist >10% формируется тикет для corp-service и law-service.
- Интеграция с social-service — сохраняет контент AR-историй для дальнейших ивентов, активирует push при критических провалах.

---

## 4. Награды и последствия

- **Репутация:**
  - Корпоративный путь: `rep.corp.arasaka +10` (до +14 при критическом успехе), `rep.street -2`, `rep.freelance -1`.
  - Уличный путь: `rep.gang.valentinos +10` (+14 при медальоне), `rep.corp.arasaka -3`, `rep.law.ncpd -1`.
  - Закон и порядок: `rep.law.ncpd +10` (+14 при taskforce), `rep.gang.valentinos -2`, `rep.freelance -1`.
  - Фриланс: `rep.freelance.global +8` (+12 при Priority Pass), небольшие штрафы к корп/ганг (-1).
- **Предметы/контракты:** корпоративный пропуск, медальон Valentinos, значок NCPD, Priority Pass Nomad, а также тематические активности (`activity.valentinos-caper`, `activity.nomad-convoy`, `contract.arasaka-entry`, `contract.ncpd-intro`).
- **HUD/соц-метрики:** `media-flash` повышает `rep.social.media`, открывает AR-историю в social-service.
- **Флаги:** `flag.main002.{corp|gang|law|freelance}`, а также `flag.arasaka.clearanceA`, `flag.valentinos.oath`, `flag.ncpd.badge`, `flag.freelance.contract`, `flag.main002.media_flash`.
- **World-state:** активируются цепочки `arasaka-world-quests`, `heywood-valentinos-chain`, `ncpd-patrol-chain`, `freelance-network`; обновляется карта влияния в `world-service`.

## 8. Связанные материалы

- `../quests/main/002-choose-path-dnd-nodes.md`
- `../dialogues/npc-hiroshi-tanaka.md`
- `../dialogues/npc-jose-tiger-ramirez.md`
- `../dialogues/npc-sara-miller.md`
- `../../02-gameplay/social/reputation-formulas.md`

## 9. История изменений

- 2025-11-07 20:50 — Расширен выбор пути: добавлены пасхалки, HUD media-flash, экспорт YAML и телеметрия; статус `ready`, версия 1.2.0.
- 2025-11-07 19:18 — Добавлены экспорт, REST/GraphQL блок и метрики. Статус `ready`, версия 1.1.0.
- 2025-11-07 16:42 — добавлен диалоговый сценарий для квеста 1.2.

### 3.2 Таблица проверок D&D

| Узел | Проверка | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|----------|----|--------------|-------|--------|-------------|--------------|
| council.arrival-curious | — | — | — | — (выбор без проверки) | — | — | — |
| corp-track.corp-persuade | Persuasion | 18 | `+2` credential, `+1` флаг обзора `target_marked` | Clearance A, контракт | Базовый контракт | Проект «Serenity», +14 репутации | Watch-list, задержка clearance |
| gang-track.gang-intimidate | Intimidation | 17 | `+1` тату, `+1` `stealth_clear` | Клятва, активность | Трилл-миссия | Медальон, +14 репутации | Флаг подозрения, минус репутация |
| law-track.law-honesty | Persuasion | 18 | `+1` `tech_open` | Значок NCPD, контракт | Патруль, +3 репутации | Задача «Cybercrime Taskforce» | Флаг ревью, бюрократический дебафф |
| freelance-track.freelance-commit | ClassChoice | 0 | `+2` netrunner/techie, `+1` solo | Контракт Convoy, +8 | Тренировка, +2 | Priority Pass, +12 | — |
| media-flash.media-wave | — | — | — | Социальный бонус | — | — | — |

> Дополнительные проверки (например, попытка «corp-hack») фиксируются как события без броска, чтобы не нарушать D&D баланс.

### 3.3 Реакции на события

- **`world.event.metro_shutdown`:** во время `council` и `gang-track` толпа реагирует; DC паркур/социальных проверок +1, появляется ambient-реплика о закрытом метро.
- **`world.event.blackwall_breach`:** в `corp-track` добавляется опция "Запросить Blackwall статистику" (повышает DC Persuasion до 20, но даёт дополнительный контракт Netwatch), а во `freelance-track` появляется предупреждение навигатора.
- **`world.event.night_market_boom`:** усиливает награды Valentinos (дополнительный лут `item.valentinos-marketpass`) и уменьшает репутацию корпораций на 1 при выборе ганг-варианта.
- **`world.event.citywide_protest`:** активирует альтернативную фразу Сары Миллер об общественном доверии и добавляет бонус +2 к Persuasion для law-пути.

### 3.4 Пасхалки и активности

- **Ever Given Redux:** упоминание баржи в ветке корпораций; критический успех выдаёт AR-наклейку «Ever Given 2077» и комментирует глобальную логистику.
- **Mariachi NFT 2079:** критический успех Valentinos даёт доступ к AR-концерту и особому скину гитары для social-service.
- **Copilot 2075:** шутка Сары про робособак активирует cosmetic badge для law-пути.
- **Vanlife 2045 Podcast:** фрилансеры получают аудио-лог, который снижает усталость в будущих караванных миссиях.
- **TikTok Drift Feed:** HUD `media-flash` публикует короткий ролик, влияющий на социальную метрику и будущие события в `modules/social`.

