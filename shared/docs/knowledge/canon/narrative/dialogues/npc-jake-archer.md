# Диалоги — Джейк Арчер

**ID диалога:** `dialogue-npc-jake-archer`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 20:43  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/common/traders/jake-archer.md`, `../quests/main/001-first-steps-dnd-nodes.md`, `../../02-gameplay/economy/economy-trading.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/economy/trade  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 20:43
**api-readiness-notes:** «Файл диалогов Джейка Арчера содержит состояния, проверки, YAML-экспорт и интеграцию с торговыми активностями. Готов к постановке API-задачи.»

---

---

## 1. Контекст и цели

- **NPC:** Джейк Арчер — независимый торговец в Downtown Market.
- **Стадии:** знакомство, доверие постоянного клиента, корпоративный контракт, кризис цепочки поставок.
- **Особенности:** торговые скидки, скрытые поставки, пасхалки на реальный мир (намёки на кризис логистики 2020 и мем о «микросхемах из Остина»).
- **Интеграции:** `rep.traders.jake`, `economy.contracts.delivery`, события `world.event.logistics_strike`, `world.event.blackwall_breach`.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| market-entry | Первое знакомство, нейтральная скидка | `rep.traders.jake` от 0 до 29 | `rep.traders.jake`, `flag.jake.met` |
| preferred-client | Постоянный клиент, секретные предложения | `rep.traders.jake ≥ 30` и `flag.jake.met` | `flag.jake.preferred`, `rep.traders.jake` |
| corporate-sponsor | Джейк обсуждает поставки для корпораций | `rep.corp.arasaka ≥ 15` или `rep.corp.militech ≥ 15` | `flag.jake.corporate` |
| supply-chain-crisis | Логистический кризис или Blackwall | `world.event.logistics_strike == true` или `world.event.blackwall_breach == true` | `flag.jake.crisis`, `world.event.logistics_strike`, `world.event.blackwall_breach` |

- **Репутация:** использует шкалу `rep.traders.jake` из `02-gameplay/social/reputation-formulas.md`.
- **Проверки:** Negotiation, Insight, Technical, Streetwise, Hacking.
- **Активности:** `economy-logistics.md` (мини-квесты снабжения), `economy-trading.md` (скидки и аукционы).

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| market-entry | «Эй, чомбата. Джейк Арчер. Подбираю товар так, чтобы ты пережил даже новую блокаду Суэцкого канала.» | default | `["Мне нужна экипировка", "Что случилось с каналом?", "Просто смотрю"]` |
| preferred-client | «О, это же мой клиент, который знает толк в скидках быстрее, чем чипы уходят из Остина.» | `rep.traders.jake ≥ 30` | `["Покажи, что под полкой", "Есть что-то эксклюзивное?", "Как бизнес?"]` |
| corporate-sponsor | «Корпы снова звонят. Если ты притащил пропуск Arasaka — у меня есть кейс с их печатью.» | `flag.jake.corporate` | `["Готов подписать контракт", "Милитех ждёт поставки", "Мне нужно что-то нейтральное"]` |
| supply-chain-crisis | «Линии стоят, Blackwall визжит, а я всё ещё доставляю. Времена хуже, чем в 2020, когда мир охотился за туалетной бумагой.» | `flag.jake.crisis` | `["Помогу с доставкой", "Есть ли путь через Badlands?", "Лучше вернусь позже"]` |

### 3.2. Узлы диалога

```
- node-id: handshake
  label: Первое знакомство
  entry-condition: not flag.jake.met
  player-options:
    - option-id: ask-stock
      text: "Какие товары у тебя в наличии?"
      requirements: []
      npc-response: "От патронов до нейрошунтов. Первая покупка — минус 5% от цены."
      outcomes:
        default: { effect: "unlock_shop", shop-id: "jake-default", set-flags: ["flag.jake.met"], reputation: +3 }
    - option-id: bargain-fast
      text: "Сразу скидку, я в теме."
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 16
      npc-response: "Посмотрим, насколько ты в теме."
      outcomes:
        success: { effect: "apply_discount", value: 12, reputation: +5, set-flags: ["flag.jake.met", "flag.jake.preferred"] }
        failure: { effect: "price_increase", value: 5, reputation: -4, set-flags: ["flag.jake.met"] }

- node-id: preferred-catalog
  label: Секретная витрина
  entry-condition: flag.jake.preferred == true
  player-options:
    - option-id: ask-exotics
      text: "Мне нужны импланты с чёрного рынка"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 15
      npc-response: "Есть парочка. Но они пахнут разборками Tyger Claws."
      outcomes:
        success: { effect: "unlock_shop", shop-id: "jake-exotics", reputation: +4 }
        failure: { effect: "trigger_event", event-id: "tyger-monitor", reputation: -2 }
    - option-id: request-activity
      text: "Дай мне работу, чтобы отбить расходы"
      requirements:
        - type: stat-check
          stat: Streetwise
          dc: 17
      npc-response: "Есть ночная доставка. Клиент — ветеран Бостонских беспорядков 2069-го."
      outcomes:
        success: { effect: "grant_activity", activity-id: "delivery-night-shift", reputation: +6 }
        failure: { effect: "spawn_encounter", encounter-id: "street-ambush", reputation: -3 }

- node-id: corporate-brief
  label: Корпоративный контракт
  entry-condition: flag.jake.corporate == true
  player-options:
    - option-id: arasaka-supply
      text: "Arasaka ждёт контейнеры"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 18
      npc-response: "По спецификации 6.9-B. Проверь, чтобы сканеры не увидели чипы с подписью Tesla Recovery 2040."
      outcomes:
        success: { effect: "grant_contract", contract-id: "arasaka-supply-run", reputation: +8, set-flags: ["flag.jake.corp-track"] }
        failure: { effect: "delay_contract", cooldown: 3600, reputation: -5 }
    - option-id: militech-alt
      text: "Милитех предлагает альтернативу"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 19
      npc-response: "Если вернёшься живым, получишь доступ к дронам-ветеранам с украинского фронта 2037."
      outcomes:
        success: { effect: "unlock_item", item-id: "militech-drone-blueprint", reputation: +7 }
        failure: { effect: "set_flag", flag: "flag.jake.blacklist_militech", reputation: -6 }

- node-id: crisis-routing
  label: Кризис поставок
  entry-condition: flag.jake.crisis == true
  player-options:
    - option-id: logistics-plan
      text: "У меня есть маршрут через Номадов"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 20
          modifiers:
            - source: "faction.aldecaldos.allied"
              value: 2
      npc-response: "Если это сработает, я назову тебя персональным DHL 2090-х."
      outcomes:
        success: { effect: "resolve_event", event-id: "logistics_strike", reputation: +10, rewards: ["crate-highgrade"] }
        failure: { effect: "spawn_encounter", encounter-id: "strike-hijack", reputation: -8 }
    - option-id: hack-blackwall
      text: "Взломаем Blackwall и подменим манифест"
      requirements:
        - type: stat-check
          stat: Hacking
          dc: 22
      npc-response: "Ты серьёзно? Это хуже ночи, когда обрушился NASDAQ-2083."
      outcomes:
        success: { effect: "grant_activity", activity-id: "blackwall-manifest-hack", reputation: +9 }
        failure: { effect: "trigger_alarm", alarm-id: "blackwall-response", reputation: -10 }
```

### 3.3. Таблица проверок D&D

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| handshake.bargain-fast | Negotiation | 16 | — | Скидка 12%, флаг preferred | +5% к цене | +18% скидка | Блокировка скидок на 24 ч |
| preferred-catalog.ask-exotics | Insight | 15 | `+2` при `flag.loot.scentchip` | Доступ к экзотикам | Агро Tyger Claws | Скидка 20% на экзотику | Засада Tyger Claws |
| preferred-catalog.request-activity | Streetwise | 17 | `+2` при `rep.street ≥ 25` | Активность «delivery-night-shift» | Засада на улице | Секретная активность «moonshiner-run» | — |
| corporate-brief.arasaka-supply | Technical | 18 | `+1` при `class.techie` | Контракт Arasaka | Кулдаун 1 ч | +Blueprint «Tesla Recovery 2.0» | Черный список Arasaka |
| corporate-brief.militech-alt | Persuasion | 19 | `+1` при `rep.corp.militech ≥ 20` | Доступ к чертежу дрона | Флаг черного списка | Контракт «militech-salvage» | Засада аудиторов |
| crisis-routing.logistics-plan | Negotiation | 20 | `+2` при `faction.aldecaldos.allied` | Сброс эвента, награда | Засада стачки | Бесплатный склад на 7 дней | Потеря склада, −12 репутации |
| crisis-routing.hack-blackwall | Hacking | 22 | `+2` при `item.netwatch-spoof` | Активность hack | Срабатывает аларм | Авто-запуск «quantum-cache» | Отключение торговли на 12 ч |

### 3.4. Реакции на события

- **Событие:** `world.event.logistics_strike`
  - **Условие:** глобальная блокировка поставок.
  - **Реплика:** «Склад в Гудзоне закрыт, чомбата. Нам нужна караванная охрана, будто мы снова в 2077.»
  - **Последствия:** активируется узел `crisis-routing`, торговля даёт +15% прибыли при успехе активности.
- **Событие:** `world.event.blackwall_breach`
  - **Условие:** ИИ-штурм сетей.
  - **Реплика:** «Blackwall вибрирует сильнее, чем сервера Еврокомиссии в 2024. Девять из десяти поставщиков офлайн.»
  - **Последствия:** +2 DC к проверке `hack-blackwall`, временный доступ к редким чипам `blackwall-shard`.

## 4. Экспорт данных (YAML)

```yaml
conversation:
  id: dialogue-npc-jake-archer
  entryNodes:
    - handshake
  states:
    market-entry:
      requirements:
        rep.traders.jake: "0-29"
    preferred-client:
      requirements:
        rep.traders.jake: ">=30"
        flag.jake.met: true
    corporate-sponsor:
      requirements:
        flag.jake.corporate: true
    supply-chain-crisis:
      requirements:
        world.event.logistics_strike: true
  nodes:
    handshake:
      onEnter: dialogue.handshake()
      options:
        - id: ask-stock
          text: "Какие товары у тебя в наличии?"
          success:
            unlockShop: jake-default
            setFlags: [flag.jake.met]
            reputation:
              rep.traders.jake: 3
        - id: bargain-fast
          text: "Сразу скидку, я в теме."
          checks:
            - stat: Negotiation
              dc: 16
          success:
            applyDiscount: 12
            setFlags: [flag.jake.met, flag.jake.preferred]
            reputation:
              rep.traders.jake: 5
          failure:
            priceIncrease: 5
            setFlags: [flag.jake.met]
            reputation:
              rep.traders.jake: -4
    preferred-catalog:
      onEnter: dialogue.preferred()
      options:
        - id: ask-exotics
          checks:
            - stat: Insight
              dc: 15
          success:
            unlockShop: jake-exotics
            reputation:
              rep.traders.jake: 4
          failure:
            triggerEvent: tyger-monitor
            reputation:
              rep.traders.jake: -2
        - id: request-activity
          checks:
            - stat: Streetwise
              dc: 17
          success:
            grantActivity: delivery-night-shift
            reputation:
              rep.traders.jake: 6
          failure:
            spawnEncounter: street-ambush
            reputation:
              rep.traders.jake: -3
    corporate-brief:
      options:
        - id: arasaka-supply
          checks:
            - stat: Technical
              dc: 18
          success:
            grantContract: arasaka-supply-run
            setFlags: [flag.jake.corp-track]
            reputation:
              rep.traders.jake: 8
          failure:
            cooldown: 3600
            reputation:
              rep.traders.jake: -5
        - id: militech-alt
          checks:
            - stat: Persuasion
              dc: 19
          success:
            unlockItem: militech-drone-blueprint
            reputation:
              rep.traders.jake: 7
          failure:
            setFlags: [flag.jake.blacklist_militech]
            reputation:
              rep.traders.jake: -6
    crisis-routing:
      options:
        - id: logistics-plan
          checks:
            - stat: Negotiation
              dc: 20
          success:
            resolveEvent: logistics_strike
            rewards:
              - crate-highgrade
            reputation:
              rep.traders.jake: 10
          failure:
            spawnEncounter: strike-hijack
            reputation:
              rep.traders.jake: -8
        - id: hack-blackwall
          checks:
            - stat: Hacking
              dc: 22
          success:
            grantActivity: blackwall-manifest-hack
            reputation:
              rep.traders.jake: 9
          failure:
            triggerAlarm: blackwall-response
            reputation:
              rep.traders.jake: -10
```

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/jake-archer` | `GET` | Вернуть актуальные узлы с учётом репутации, событий и скидок |
| `/narrative/dialogues/jake-archer/run-check` | `POST` | Прогнать проверки Negotiation/Insight/Streetwise/Technical/Hacking |
| `/narrative/dialogues/jake-archer/state` | `POST` | Сохранить флаги (`flag.jake.*`), репутацию, применённые скидки |
| `/narrative/dialogues/jake-archer/activities` | `POST` | Подтвердить запуск активностей `delivery-night-shift`, `blackwall-manifest-hack` |

GraphQL поле `traderDialogue(id: ID!)` возвращает `TraderDialogueNode` с `economyHooks` для экономических сервисов.

## 6. Валидация и телеметрия

- `scripts/validate-dialogue-flags.ps1` проверяет `flag.jake.*`, `rep.traders.jake` в `reputation-formulas.md`, события в `world/events/global-events-2020-2093.md`.
- `scripts/dialogue-simulator.ps1 -Scenario trader-jake` прогоняет ветки `market-entry`, `preferred-client`, `corporate-sponsor`, `supply-chain-crisis`.
- Метрики: `trader-jake-discount-rate`, `trader-jake-activity-uptake`, `trader-jake-crisis-resolution` (цель ≥60%).

## 7. Награды и последствия

- **Репутация:** `rep.traders.jake` от -20 до +40, косвенные изменения `rep.corp.arasaka`, `rep.corp.militech`, `rep.street`.
- **Предметы:** `militech-drone-blueprint`, `crate-highgrade`, редкие импланты из `jake-exotics`.
- **Активности:** логистические миссии, взлом манифеста, доставка ветерану 2069.
- **Флаги:** `flag.jake.met`, `flag.jake.preferred`, `flag.jake.corporate`, `flag.jake.crisis`, `flag.jake.corp-track`, `flag.jake.blacklist_militech`.

## 8. Связанные материалы

- `../npc-lore/common/traders/jake-archer.md`
- `../../02-gameplay/economy/economy-trading.md`
- `../../02-gameplay/economy/economy-logistics.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/global-events-2020-2093.md`

## 9. История изменений

- 2025-11-07 20:24 — Создан диалог Джейка Арчера (ветки, проверки, активности, экспорт, API). Статус `ready`.
# Диалоги — Джейк Арчер

**ID диалога:** `dialogue-npc-jake-archer`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 20:43  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/common/traders/jake-archer.md`, `../quests/main/001-first-steps-dnd-nodes.md`, `../../02-gameplay/economy/economy-trading.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/economy/trade  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 20:43
**api-readiness-notes:** «Файл диалогов Джейка Арчера содержит состояния, проверки, YAML-экспорт и интеграцию с торговыми активностями. Готов к постановке API-задачи.»

---

## 1. Контекст и цели

- **NPC:** Джейк Арчер — независимый торговец в Downtown Market.
- **Стадии:** знакомство, доверие постоянного клиента, корпоративный контракт, кризис цепочки поставок.
- **Особенности:** торговые скидки, скрытые поставки, пасхалки на реальный мир (намёки на кризис логистики 2020 и мем о «микросхемах из Остина»).
- **Интеграции:** `rep.traders.jake`, `economy.contracts.delivery`, события `world.event.logistics_strike`, `world.event.blackwall_breach`.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| market-entry | Первое знакомство, нейтральная скидка | `rep.traders.jake` от 0 до 29 | `rep.traders.jake`, `flag.jake.met` |
| preferred-client | Постоянный клиент, секретные предложения | `rep.traders.jake ≥ 30` и `flag.jake.met` | `flag.jake.preferred`, `rep.traders.jake` |
| corporate-sponsor | Джейк обсуждает поставки для корпораций | `rep.corp.arasaka ≥ 15` или `rep.corp.militech ≥ 15` | `flag.jake.corporate` |
| supply-chain-crisis | Логистический кризис или Blackwall | `world.event.logistics_strike == true` или `world.event.blackwall_breach == true` | `flag.jake.crisis`, `world.event.logistics_strike`, `world.event.blackwall_breach` |

- **Репутация:** использует шкалу `rep.traders.jake` из `02-gameplay/social/reputation-formulas.md`.
- **Проверки:** Negotiation, Insight, Technical, Streetwise, Hacking (для эксплойтов поставок).
- **Активности:** `economy-logistics.md` (мини-квесты снабжения), `economy-trading.md` (скидки и аукционы).

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| market-entry | «Эй, чомбата. Джейк Арчер. Подбираю товар так, чтобы ты пережил даже новую блокаду Суэцкого канала.» | default | `["Мне нужна экипировка", "Что случилось с каналом?", "Просто смотрю"]` |
| preferred-client | «О, это же мой клиент, который знает толк в скидках быстрее, чем чипы уходят из Остина.» | `rep.traders.jake ≥ 30` | `["Покажи, что под полкой", "Есть что-то эксклюзивное?", "Как бизнес?"]` |
| corporate-sponsor | «Корпы снова звонят. Если ты притащил пропуск Arasaka — у меня есть кейс с их печатью.» | `flag.jake.corporate` | `["Готов подписать контракт", "Милитех ждёт поставки", "Мне нужно что-то нейтральное"]` |
| supply-chain-crisis | «Линии стоят, Blackwall визжит, а я всё ещё доставляю. Времена хуже, чем в 2020, когда мир охотился за туалетной бумагой.» | `flag.jake.crisis` | `["Помогу с доставкой", "Есть ли путь через Badlands?", "Лучше вернусь позже"]` |

### 3.2. Узлы диалога

```
- node-id: handshake
  label: Первое знакомство
  entry-condition: not flag.jake.met
  player-options:
    - option-id: ask-stock
      text: "Какие товары у тебя в наличии?"
      requirements: []
      npc-response: "От патронов до нейрошунтов. Первая покупка — минус 5% от цены."
      outcomes:
        default: { effect: "unlock_shop", shop-id: "jake-default", set-flags: ["flag.jake.met"], reputation: +3 }
    - option-id: bargain-fast
      text: "Сразу скидку, я в теме."
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 16
      npc-response: "Посмотрим, насколько ты в теме."
      outcomes:
        success: { effect: "apply_discount", value: 12, reputation: +5, set-flags: ["flag.jake.met", "flag.jake.preferred"] }
        failure: { effect: "price_increase", value: 5, reputation: -4, set-flags: ["flag.jake.met"] }

- node-id: preferred-catalog
  label: Секретная витрина
  entry-condition: flag.jake.preferred == true
  player-options:
    - option-id: ask-exotics
      text: "Мне нужны импланты с чёрного рынка"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 15
      npc-response: "Есть парочка. Но они пахнут разборками Tyger Claws."
      outcomes:
        success: { effect: "unlock_shop", shop-id: "jake-exotics", reputation: +4 }
        failure: { effect: "trigger_event", event-id: "tyger-monitor", reputation: -2 }
    - option-id: request-activity
      text: "Дай мне работу, чтобы отбить расходы"
      requirements:
        - type: stat-check
          stat: Streetwise
          dc: 17
      npc-response: "Есть ночная доставка. Клиент — ветеран Бостонских беспорядков 2069-го."
      outcomes:
        success: { effect: "grant_activity", activity-id: "delivery-night-shift", reputation: +6 }
        failure: { effect: "spawn_encounter", encounter-id: "street-ambush", reputation: -3 }

- node-id: corporate-brief
  label: Корпоративный контракт
  entry-condition: flag.jake.corporate == true
  player-options:
    - option-id: arasaka-supply
      text: "Аррасака ждёт контейнеры"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 18
      npc-response: "По спецификации 6.9-B. Проверь, чтобы сканеры не увидели чипы с подписью Tesla Recovery 2040."
      outcomes:
        success: { effect: "grant_contract", contract-id: "arasaka-supply-run", reputation: +8, set-flags: ["flag.jake.corp-track"] }
        failure: { effect: "delay_contract", cooldown: 3600, reputation: -5 }
    - option-id: militech-alt
      text: "Милитех предлагает альтернативу"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 19
      npc-response: "Если вернёшься живым, получишь доступ к дронам-ветеранам с украинского фронта 2037."
      outcomes:
        success: { effect: "unlock_item", item-id: "militech-drone-blueprint", reputation: +7 }
        failure: { effect: "set_flag", flag: "flag.jake.blacklist_militech", reputation: -6 }

- node-id: crisis-routing
  label: Кризис поставок
  entry-condition: flag.jake.crisis == true
  player-options:
    - option-id: logistics-plan
      text: "У меня есть маршрут через Номадов"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 20
          modifiers:
            - source: "faction.aldecaldos.allied"
              value: 2
      npc-response: "Если это сработает, я назову тебя персональным DHL 2090-х."
      outcomes:
        success: { effect: "resolve_event", event-id: "logistics_strike", reputation: +10, rewards: ["crate-highgrade"] }
        failure: { effect: "spawn_encounter", encounter-id: "strike-hijack", reputation: -8 }
    - option-id: hack-blackwall
      text: "Взломаем Blackwall и подменим манифест"
      requirements:
        - type: stat-check
          stat: Hacking
          dc: 22
      npc-response: "Ты серьёзно? Это хуже ночи, когда обрушился NASDAQ-2083."
      outcomes:
        success: { effect: "grant_activity", activity-id: "blackwall-manifest-hack", reputation: +9 }
        failure: { effect: "trigger_alarm", alarm-id: "blackwall-response", reputation: -10 }
```

### 3.3. Таблица проверок D&D

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| handshake.bargain-fast | Negotiation | 16 | — | Скидка 12%, флаг preferred | +5% к цене | +18% скидка | Блокировка скидок на 24 ч |
| preferred-catalog.ask-exotics | Insight | 15 | `+2` при `flag.loot.scentchip` | Доступ к экзотикам | Агро Tyger Claws | Скидка 20% на экзотику | Засада Tyger Claws |
| preferred-catalog.request-activity | Streetwise | 17 | `+2` при `rep.street ≥ 25` | Активность «delivery-night-shift» | Засада на улице | Секретная активность «moonshiner-run» | - |
| corporate-brief.arasaka-supply | Technical | 18 | `+1` при `class.techie` | Контракт Arasaka | Кулдаун 1 ч | +Blueprint «Tesla Recovery 2.0» | Черный список Arasaka |
| corporate-brief.militech-alt | Persuasion | 19 | `+1` при `rep.corp.militech ≥ 20` | Доступ к чертежу дрона | Флаг черного списка | Контракт «militech-salvage» | Засада милитековских аудиторов |
| crisis-routing.logistics-plan | Negotiation | 20 | `+2`