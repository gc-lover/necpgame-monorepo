# Диалоги — Квест 1.1 «Первые шаги»

**ID диалога:** `dialogue-quest-main-001-first-steps`  
**Тип:** quest  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-08 09:26  
**Приоритет:** высокий  
**Связанные документы:** `../quests/main/001-first-steps-dnd-nodes.md`, `../dialogues/npc-marco-fix-sanchez.md`, `../npc-lore/important/marco-fix-sanchez.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 09:26
**api-readiness-notes:** «Диалог квеста 1.1 подтверждён: ветки обучения, пасхалки, экспорт YAML, телеметрия и интеграции narrative-service/ui усвоены без блокеров.»

---

---

## 1. Контекст и цели

- **Уроки старта:** знакомство с Watson через призму Марко Санчеса, baseline для веток корпорации/улиц/фриланса.
- **Игровые системы:** пошаговое обучение Perception → Parkour → Communication → Stealth → Tech с логами бросков и UI-подсказками.
- **Эмоциональный тон:** «город не спит» — сочетание саркастичного наставничества и намёков на реальный мир (поставки 2020, TikTok-стримы протеста).
- **Отсылки:** упоминания дефицита чипов 2020-х, трендов «#WatsonWalk» и «Suez 2071», а также локальных мемов про туалетную бумагу.
- **Интеграции:** `npc-marco-fix-sanchez`, `npc-jake-archer`, `npc-rita-moreno`, события `world.event.metro_shutdown`, `world.event.blackwall_breach`, обучающие подсказки из `modules/narrative/quests`.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Флаги |
|-----------|----------|----------|-------|
| arrival | Первое появление в Watson, приветствие Марко | Старт квеста | `flag.main001.arrival` |
| market-run | Паркур и визуальное сканирование рынка | `flag.main001.arrival` | `flag.main001.market_run` |
| tutorial-hud | Интерактивные подсказки HUD (лог бросков, обучение тактовой панели) | Завершён `market-run` | `flag.main001.hud_shown` |
| fixer-brief | Разговор о первых подработках | `flag.main001.market_run` | `flag.main001.fixer_brief` |
| stealth-route | Опциональный стелс вдоль учебного кордона NCPD | Выбор «Пробую» в `market-run` | `flag.main001.stealth` |
| tech-door | Взлом двери и проверка Tech + cameo Джейка | `flag.main001.fixer_brief` | `flag.main001.tech_door` |
| wrap-up | Финальный выбор ветки и подсуммирование опыта | Успешно/неуспешно пройдены проверки | `flag.main001.choice_*` |

- **Связь с D&D узлами:** `N-1` (Perception), `N-2` (Parkour), `N-3` (Communication), `N-4` (Stealth), `N-5` (Tech). HUD-состояние выводит броски в лог и объясняет DC.
- **UI подсказки:** при первом броске выводится «Shift+Tab — журнал бросков», при провале — покадровый реплей в окне помощи.
- **Репутация:** начальные модификаторы `rep.fixers.marco`, `rep.street`, `rep.law.ncpd`, дальнейшая маршрутизация в `002-choose-path`.

## 3. Сцены и узлы диалога

### 3.1 Основные сцены (псевдокод YAML)

```yaml
nodes:
  - id: arrival
    label: "Час города"
    speaker-order: ["Marco", "Player", "Crowd"]
    dialogue:
      - speaker: Marco
        text: "Привет, чомбата. Watson не терпит jet lag. Смотри по сторонам — тут быстрее, чем очереди за туалетной бумагой 2020-го."
      - speaker: Player
        options:
          - id: arrival-curious
            text: "Что тут кипит?"
            response:
              speaker: Marco
              text: "Корпы хотят закрыть метро, банды стримят рейды, Blackwall снова дрожит. Сначала — глаза."
              trigger-check: { node: "N-1", stat: "Perception", dc: 10, tutorial: true }
              outcomes:
                success: { set-flag: "flag.main001.target_marked", hud: "highlight-cache", reward: "xp.micro" }
                failure: { hud: "log.fail.perception", debuff: "perception_noise:60" }
                critical-success: { reward: "loot.cache01", codex: "nightcity.hidden-caches" }
                critical-failure: { spawn: "ncpd.mocking-drone", note: "Журнал: Марко шутит про Suez" }
          - id: arrival-skeptical
            text: "Мне обещали глянцевые небоскрёбы."
            response:
              speaker: Marco
              text: "Это глянец. Просто покрыт копотью. Двигайся."
              set-flag: "flag.main001.market_run"
          - id: arrival-ready
            text: "Готов учиться"
            response:
              speaker: Marco
              text: "Готов — беги. И запомни: всё, что выше колен, — гангам, всё ниже — корпам."
              set-flag: "flag.main001.market_run"

  - id: market-run
    label: "Паркур через баррикаду"
    speaker-order: ["Marco", "Player"]
    dialogue:
      - speaker: Marco
        text: "Барьеры временные, как очередная блокада Суэцкого канала. Прыгай."
      - speaker: Player
        options:
          - id: parkour-commit
            text: "Прыгаю"
            response:
              system-check: { node: "N-2", stat: "Parkour", dc: 10, hud: "show-dice" }
              success-line: "Ты перепрыгиваешь, камеры из #WatsonWalk добавляют тебя в клип."
              failure-line: "Колено протестует, но Марко подхватывает."
              critical-success-line: "Тройное сальто, лайки растут. Марко: \"Тебе бы в шоу Сильверхенда\"."
              critical-failure-line: "Влетаешь в коробки с очередной партией 3D-печатных мем-уток." 
              outcomes:
                success: { set-flag: "flag.main001.parkour_done", reward: "xp.small" }
                failure: { debuff: "movement_slow:30", hint: "Используй имплант JumpMod" }
                critical-success: { reward: "chip.motion-capture", codex: "city.viral-trends" }
                critical-failure: { spawn: "crowd.laugh-track", reputation: { street: -1 } }
          - id: parkour-stealth
            text: "Есть обход?"
            response:
              speaker: Marco
              text: "Есть вентиляция. Но там учебный дрон NCPD."
              outcomes:
                default: { set-flag: "flag.main001.stealth", next-node: "stealth-route" }
          - id: parkour-decline
            text: "Не прыгну."
            response:
              speaker: Marco
              text: "Ладно. Получишь симулятор падения."
              outcomes:
                default: { tutorial: "parkour.auto", set-flag: "flag.main001.holo_run" }

  - id: tutorial-hud
    label: "HUD-пояснения"
    condition: { flag: "flag.main001.market_run" }
    speaker-order: ["System", "Marco"]
    dialogue:
      - speaker: System
        text: "HUD: бросок d20 + модификаторы. Shift+Tab — журнал."
      - speaker: Marco
        text: "Видишь лог? Это твой лучший друг. Второй лучший — я."
      - speaker: Player
        options:
          - id: hud-ack
            text: "Лог вижу"
            response:
              outcomes:
                default: { set-flag: "flag.main001.hud_shown" }
          - id: hud-ignore
            text: "Спрячь HUD"
            response:
              speaker: System
              text: "HUD можно отключить в настройках, но мы предупредили."

  - id: fixer-brief
    label: "Первый контракт"
    speaker-order: ["Marco", "Player", "Jake"]
    dialogue:
      - speaker: Marco
        text: "У каждого новичка выбор: продаться корпам, улицам или себе."
      - speaker: Player
        options:
          - id: talk-job
            text: "Что по работе?"
            response:
              speaker: Jake
              text: "У меня доставка — свежие микрочипы из Остина. Надо успеть до Blackwall ping."
              trigger-check: { node: "N-3", stat: "Communication", dc: 10, modifiers: [{ source: "flag.main001.target_marked", value: +1 }] }
              outcomes:
                success: { grant: "discount.02", reputation: { fixer: +5 }, set-flag: "flag.main001.jake_discount" }
                failure: { note: "Jake сомневается", reputation: { fixer: -1 } }
                critical-success: { grant: "activity.logistics-mini", codex: "economy.supply-chain" }
                critical-failure: { trigger: "newsfeed:JakeMeme", reputation: { street: -2 } }
          - id: talk-city
            text: "Что с Watson?"
            response:
              speaker: Marco
              text: "Город в режиме Tokyo Drift. Metro в ремонте, Uber-глайдеры бастуют, трамвай TikTokит."
          - id: talk-pascal
            text: "Слышал про #SuezReboot?"
            response:
              speaker: Marco
              text: "Да. Корпы пытаются снова застрять там баржу, чтобы удержать цены на биочипы."

  - id: stealth-route
    label: "Тень рынка"
    condition: { flag: "flag.main001.stealth" }
    speaker-order: ["Marco", "Player", "NCPD Drone"]
    dialogue:
      - speaker: Marco
        text: "Видишь дрон? Он настроен на хэштеги. Если услышит #Revolution, подаст сигнал."
      - speaker: Player
        options:
          - id: stealth-accept
            text: "Пробую"
            response:
              system-check: { node: "N-4", stat: "Stealth", dc: 10, modifiers: [{ source: "gear.cloak", value: +2 }] }
              success-line: "Ты скользишь в тени. Дрон стримит котиков и не замечает."
              failure-line: "Дрон ловит движение, включает мигалки TikTok."
              critical-failure-line: "Учебный патруль включает озвучку Сильверхенда: \"Wake up, samurai!\" и запускает учебный бой."
              outcomes:
                success: { set-flag: "flag.main001.stealth_clear", reputation: { street: +2 }, reward: "xp.micro" }
                failure: { spawn: "ncpd.training-patrol", reputation: { law: -1 }, hud: "stealth.tip" }
                critical-failure: { trigger: "event.training-lockdown", debuff: "stealth_cooldown:90" }
          - id: stealth-bypass
            text: "Взламываю дрона"
            response:
              system-check: { node: "N-5", stat: "Tech", dc: 14 }
              outcomes:
                success: { set-flag: "flag.main001.drone_hacked", reward: "component.micro" }
                failure: { alarm: true, reputation: { law: -2 } }

  - id: tech-door
    label: "Вскрытие и финал"
    speaker-order: ["Jake", "Player", "Marco"]
    dialogue:
      - speaker: Jake
        text: "Этот шлюз застрял сильнее, чем баржа Ever Given. Поможешь — получишь скидку."
      - speaker: Player
        options:
          - id: tech-attempt
            text: "Попробую вскрыть"
            response:
              system-check: { node: "N-5", stat: "Tech", dc: 12, timer: 30, hud: "progress-wheel" }
              success-line: "Контакты щёлкают, дверь открывается, Джейк отмечает тебя в логистическом чате."
              failure-line: "Контакты искрят, Марко ругается, Джейк бурчит про supply chain."
              critical-failure-line: "Система поднимает учебную тревогу, включается архив CNN 2020."
              outcomes:
                success: { reward: "loot.micro", reputation: { fixer: +5, law: +2 }, set-flag: "flag.main001.tech_open" }
                failure: { consume: "battery-pack", reputation: { fixer: -1 }, hud: "tech.tip" }
                critical-failure: { trigger: "ncpd.training-alert", debuff: "lockpick_cooldown:180" }
          - id: tech-skip
            text: "Это не моё"
            response:
              speaker: Marco
              text: "Учись, чомбата. Следующий контракт не подождёт."
          - id: tech-handover
            text: "Попросить Марко"
            response:
              speaker: Marco
              text: "Я делаю скидку один раз. Смотри и учись."
              outcomes:
                default: { hint: "macro.tutorial" }

  - id: wrap-up
    label: "Новый путь"
    speaker-order: ["Marco", "Player", "HUD"]
    dialogue:
      - speaker: Marco
        text: "Неплохо для свежей крови. Дальше — выбор."
      - speaker: HUD
        text: "Выбор влияет на ветки `002-choose-path`."
      - speaker: Player
        options:
          - id: wrap-corp
            text: "Хочу в корпорации"
            response:
              speaker: Marco
              text: "Подготовь костюм и стеклянную улыбку. Хироши проверит твои сложения."
              outcomes:
                default: { set-flag: "flag.main001.choice_corp", reputation: { corp: +3 } }
          - id: wrap-gang
            text: "Улицы зовут"
            response:
              speaker: Marco
              text: "Тогда Тигр покажет, где спрятаны креды. Не умереть бы тебе."
              outcomes:
                default: { set-flag: "flag.main001.choice_gang", reputation: { street: +3 } }
          - id: wrap-freelance
            text: "Останусь свободным"
            response:
              speaker: Marco
              text: "Свобода — дорогой сервис. Но ты уже оплатил предоплату."
              outcomes:
                default: { set-flag: "flag.main001.choice_free", grant: "activity.freelance-sprint" }
```

### 3.2 Таблица проверок D&D

| Узел | Проверка | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|----------|----|--------------|-------|--------|-------------|--------------|
| arrival.arrival-curious | Perception | 10 | `+1` при `gear.smart-goggles` | Отмечена скрытая тайник-камерa | Шум HUD | +Кодекс и лут | «Drone mockery» событие |
| market-run.parkour-commit | Parkour | 10 | `+2` при импланте `jump_mod` | Флаг `parkour_done`, XP | Замедление 30с | Баф «viral fame» | Падение, -1 street |
| fixer-brief.talk-job | Communication | 10 | `+1` при `flag.main001.target_marked` | Скидка Джейка, mini-activity | Недоверие | Открывает `activity.logistics-mini` | Меме-фид против игрока |
| stealth-route.stealth-accept | Stealth | 10 | `+2` при `gear.cloak` | Репутация улиц +2, лут | Патруль NCPD | Доп. награда `component.nano` | Тренировочная тревога |
| stealth-route.stealth-bypass | Tech | 14 | `+1` при `class.netrunner` | Контроль дрона | Аларм | Баф «drone support» | -2 к закону |
| tech-door.tech-attempt | Tech | 12 | `+1` при `flag.main001.stealth_clear` | Лут, репутация корп/закон | Сгорает баттарея | Контракт бонус | Учебная тревога |
| wrap-up.* | Нет прямой проверки | — | — | Формирует ветку 002 | — | — | — |

### 3.3 Реакции на события

- **`world.event.metro_shutdown`:** во время `market-run` Марко добавляет реплику про забастовку метро; DC Parkour +1 из-за толпы.
- **`world.event.blackwall_breach`:** при взломе двери добавляется опция «Запросить сетевую помощь», но критический провал вызывает мини-задачу `blackwall-containment`.
- **`world.event.solar_flare_2075`:** отключает HUD-подсказки, если событие активно; в `tutorial-hud` выводится предупреждение об отказе интерфейса.

### 3.4 Пасхалки и активности

- **Пасхалка «Ever Given 2077»:** крит. успех тех-взлома выдаёт голо-наклейку с баржей и подписью «я тоже застрял». 
- **Активность `activity.logistics-mini`:** короткая доставка от Джейка — ведёт к `npc-jake-archer` (дополнительный диалог о supply chain).
- **Учебный клип:** крит. успех Parkour — автоматическое добавление клипа в внутриигровую соцсеть `WatsonWalk`, повышает шанс приглашения в клан активистов.

## 4. Награды и последствия

- **Репутация:** `rep.fixers.marco` ±8, `rep.street` ±3, `rep.corp.arasaka` +3 (корп-путь), `rep.law.ncpd` +2 при успешном взломе.
- **Предметы:** `loot.micro`, `chip.motion-capture`, `component.nano`, наклейка «Ever Given 2077», доступ к логистическим активностям.
- **Активности:** разблокировка `activity.freelance-sprint`, `activity.logistics-mini`, потенциальная миссия `blackwall-containment` при критических провалах.
- **Флаги:** `flag.main001.target_marked`, `flag.main001.parkour_done`, `flag.main001.stealth_clear`, `flag.main001.tech_open`, `flag.main001.choice_{corp|gang|free}`.
- **Маршрутизация:** передаёт выбор в `002-choose-path` и обновляет карту заданий social-service.

## 5. Экспорт (API / YAML)

```yaml
conversation:
  id: quest-main-001-first-steps
  entryNodes: [arrival]
  states:
    arrival:
      requirements: { quest.main-001: "started" }
    market-run:
      requirements: { flag.main001.arrival: true }
    tutorial-hud:
      requirements: { flag.main001.market_run: true }
    fixer-brief:
      requirements: { flag.main001.market_run: true }
    stealth-route:
      requirements: { flag.main001.stealth: true }
    tech-door:
      requirements: { flag.main001.fixer_brief: true }
    wrap-up:
      requirements: { flag.main001.tech_door: true }
  nodes:
    arrival:
      options:
        - id: arrival-curious
          checks:
            - stat: Perception
              dc: 10
              modifiers:
                - source: gear.smart-goggles
                  value: 1
          success:
            setFlags: [flag.main001.target_marked]
            rewards:
              - xp.micro
          failure:
            debuffs:
              - perception_noise:60
        - id: arrival-ready
          success:
            setFlags: [flag.main001.market_run]
    market-run:
      options:
        - id: parkour-commit
          checks:
            - stat: Parkour
              dc: 10
          success:
            setFlags: [flag.main001.parkour_done]
            rewards:
              - xp.small
        - id: parkour-stealth
          success:
            setFlags: [flag.main001.stealth]
            nextNode: stealth-route
    fixer-brief:
      options:
        - id: talk-job
          checks:
            - stat: Communication
              dc: 10
          success:
            setFlags: [flag.main001.jake_discount]
            rewards:
              - discount.02
    tech-door:
      options:
        - id: tech-attempt
          checks:
            - stat: Tech
              dc: 12
          success:
            setFlags: [flag.main001.tech_open]
            rewards:
              - loot.micro
              - reputation.fixers: 5
              - reputation.law: 2
          failure:
            costs:
              - battery-pack
          criticalFailure:
            triggers:
              - ncpd.training-alert
    wrap-up:
      options:
        - id: wrap-corp
          success:
            setFlags: [flag.main001.choice_corp]
        - id: wrap-gang
          success:
            setFlags: [flag.main001.choice_gang]
        - id: wrap-freelance
          success:
            setFlags: [flag.main001.choice_free]
            grants:
              - activity.freelance-sprint
```

## 6. Телеметрия и метрики

- **Метрики:** `tutorial-perception-success-rate`, `tutorial-parkour-success-rate`, `tutorial-stealth-alerts`, `tutorial-tech-critical-failures` (цель — не более 15%).
- **Сбор данных:** `/quests/tutorials/first-steps/telemetry` — лог бросков, выбранных опций, причин провалов, таргетируемых подсказок.
- **Мониторинг:** при трёх критических провалах подряд автоматически предлагается режим «assist mode» (маштабирование DC).
- **Валидация:** `scripts/validate-quest-dialogue.ps1` сверяет флаги `flag.main001.*`, проверяет соответствие узлам `001-first-steps-dnd-nodes.md`, наличие подсказок HUD.

## 7. История изменений

- 2025-11-08 09:26 — перепроверка готовности, синхронизация метрик и UI подсказок, статус подтверждён.
- 2025-11-07 20:43 — финализирован диалог, добавлены пасхалки, экспорт и метрики; статус переведён в `ready`.
- 2025-11-07 16:42 — добавлен начальный сценарий квеста 1.1.

