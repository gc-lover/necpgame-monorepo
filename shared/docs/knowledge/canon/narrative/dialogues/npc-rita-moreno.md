# Диалоги — Рита Морено

**ID диалога:** `dialogue-npc-rita-moreno`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.3.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 21:25  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/common/traders/rita-moreno.md`, `../quests/side/maelstrom-double-cross.md`, `../../02-gameplay/social/reputation-formulas.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/social/informants  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 21:25
**api-readiness-notes:** «Версия 1.3.0: расширены состояния (market/fiesta/blackwall), пасхалки, YAML/REST и телеметрия street-информанта.»

---

---

## 1. Контекст и цели

- **Локация:** Watson Market и соседние аллеи, мобильная лавочка Риты между street food и подпольным рынком имплантов.
- **Образ:** Смесь уличного барда и информатора. Воспоминания о WallStreetBets 2021, стримах TikTok 2040-х, lowrider-парадах Valentinos, сетевых мемах про Ever Given.
- **Фазы:** знакомство, инсайдерский доступ, Valentinos льготы, Maelstrom тревога, праздничные фиесты, Blackwall слухи.
- **Отсылки:** протесты Heywood 2077, шоу NUSA Idol, крипто-бумы 2033, «stonks» мемы, легенды о Hidden City Radio.
- **Цели игрока:** получить товары, открыть уникальные слухи, участвовать в доставках, решать double-cross с Maelstrom и NCPD, влиять на social feed.
- **Интеграции:** `npc-jose-tiger-ramirez`, `quest-side-maelstrom-double-cross`, `world.event.heywood_turf_war`, `world.event.blackwall_breach`, economy-service (торговля), social-service (стримы и мемы).

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| street-entry | Первое знакомство, базовая торговля | `rep.traders.rita` 0–24 | `flag.rita.met`, `rep.traders.rita` |
| insider-loop | Доступ к скрытым слухам | `rep.traders.rita ≥ 25` и `flag.rita.met` | `flag.rita.insider`, `rep.traders.rita` |
| valentinos-favor | Особые услуги для друзей Valentinos | `rep.gang.valentinos ≥ 20` | `flag.rita.valentinos`, `rep.gang.valentinos` |
| maelstrom-alert | ЧП с Maelstrom, двойная игра | `flag.maelstrom.double_cross == true` или `world.event.maelstrom_pipeline == true` | `flag.rita.alert`, `world.event.maelstrom_pipeline` |
| fiesta-mode | Праздничное состояние (Día de los Muertos, NUSA Idol live) | `world.event.dia_de_los_muertos == true` или `world.event.nusa_idol_live == true` | `flag.rita.fiesta`, `world.event.dia_de_los_muertos` |
| blackout-rumor | Слухи о Blackwall и Netwatch | `world.event.blackwall_breach == true` или `flag.rita.blackwall_tip == true` | `flag.rita.blackwall_tip` |

- **Репутация:** `rep.traders.rita`, `rep.gang.valentinos`, вторичные `rep.maelstrom`.
- **Проверки:** Streetwise, Deception, Empathy, Hacking, Perception.
- **Активности:** сбор слухов, доставка подпольных медикаментов, сплит-квест с Maelstrom.

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| street-entry | «Watson гудит, чомбата. Я Рита. Держу руку на пульсе улиц с тех пор, как WallStreetBets уронил корпорации.» | default | `["Хочу товар", "Есть слухи?", "Осматриваюсь"]` |
| insider-loop | «Возвращаешься быстрее, чем выходит новый выпуск NUSA Idol? Тогда для тебя есть свежий инсайд.» | `rep.traders.rita ≥ 25` | `["Дай сведения", "Нужна скидка", "Где достать медикаменты?"]` |
| valentinos-favor | «Valentinos сегодня в настроении. Если ты их любимчик — подберём что-то золотое.» | `flag.rita.valentinos` | `["Хочу золото", "Есть дела для Valentinos?", "Не выдавай меня"]` |
| maelstrom-alert | «Maelstrom шумит, как в ночь, когда они сорвали кабель-пайплайн. Готов устроить двойной кроссовер?» | `flag.rita.alert` | `["Даём им ложный след", "Мне нужны координаты", "Это слишком"]` |

### 3.2. Узлы диалога

```
- node-id: intro
  label: Знакомство
  entry-condition: not flag.rita.met
  player-options:
    - option-id: buy-basic
      text: "Покажи базовый ассортимент"
      requirements: []
      npc-response: "Импланты, патчи, пару вещиц из Пайнвуда."
      outcomes:
        default: { effect: "unlock_shop", shop-id: "rita-default", set-flags: ["flag.rita.met"], reputation: +2 }
    - option-id: drop-wsb
      text: "Слышал про тех парней с Reddit?"
      requirements:
        - type: stat-check
          stat: Streetwise
          dc: 14
      npc-response: "Хаха, старый мем, но я была там. Держи скидку за ностальгию." 
      outcomes:
        success: { effect: "apply_discount", value: 10, set-flags: ["flag.rita.met", "flag.rita.insider"] }
        failure: { effect: "price_increase", value: 3, set-flags: ["flag.rita.met"] }

- node-id: insider
  label: Скрытые слухи
  entry-condition: flag.rita.insider == true
  player-options:
    - option-id: ask-rumors
      text: "Слухи про Maelstrom"
      requirements:
        - type: stat-check
          stat: Perception
          dc: 16
      npc-response: "Они закопали дрона с тегом Militech у туннеля."
      outcomes:
        success: { effect: "unlock_codex", codex-id: "maelstrom-tunnel", reputation: +3 }
        failure: { effect: "spawn_encounter", encounter-id: "maelstrom-spotters", reputation: -3 }
    - option-id: request-delivery
      text: "Мне нужна подпольная доставка"
      requirements:
        - type: stat-check
          stat: Empathy
          dc: 17
      npc-response: "Есть семья из Heywood, им нужно лекарство."
      outcomes:
        success: { effect: "grant_activity", activity-id: "heywood-meds-run", reputation: +5 }
        failure: { effect: "mission_fail", penalty: "reputation_drop", reputation: -4 }

- node-id: valentinos
  label: Сделка для Valentinos
  entry-condition: flag.rita.valentinos == true
  player-options:
    - option-id: gold-request
      text: "Нужны золотые импланты"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 18
      npc-response: "Ладно, но если Padre узнает — скажи, что это идея Керри Евродина."
      outcomes:
        success: { effect: "unlock_shop", shop-id: "rita-valentinos", reputation: +6 }
        failure: { effect: "set_flag", flag: "flag.rita.suspicious", reputation: -5 }
    - option-id: valentinos-mission
      text: "Есть работа для Valentinos?"
      requirements:
        - type: stat-check
          stat: Streetwise
          dc: 18
      npc-response: "Есть гала-конвой Arasaka. Нужно подкинуть туда карнавальные чипы."
      outcomes:
        success: { effect: "grant_activity", activity-id: "valentinos-carnival-hack", reputation: +7 }
        failure: { effect: "trigger_alarm", alarm-id: "arasaka-counterintel", reputation: -6 }

- node-id: maelstrom
  label: Двойная игра
  entry-condition: flag.rita.alert == true
  player-options:
    - option-id: double-cross
      text: "Сдаём их Militech"
      requirements:
        - type: stat-check
          stat: Deception
          dc: 21
      npc-response: "Ты играешь опасно. Проверим, кто пошлёт дрон первым."
      outcomes:
        success: { effect: "resolve_event", event-id: "maelstrom-double-cross", reputation: +9, rewards: ["chip-militech-leverage"] }
        failure: { effect: "spawn_encounter", encounter-id: "maelstrom-hunters", reputation: -9 }
    - option-id: support-maelstrom
      text: "Предупредим Maelstrom"
      requirements:
        - type: stat-check
          stat: Hacking
          dc: 20
      npc-response: "Ладно, качаем фальшивые логи в их сеть."
      outcomes:
        success: { effect: "grant_activity", activity-id: "maelstrom-fake-logs", reputation: +8 }
        failure: { effect: "trigger_alarm", alarm-id: "militech-sniffers", reputation: -8 }
```

### 3.3. Таблица проверок D&D

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| intro.drop-wsb | Streetwise | 14 | `+2` при `flag.street.memelord` | Скидка 10% | Наценка 3% | Получение пасхалки «stonks-up» | Потеря доступа на 30 мин |
| insider.ask-rumors | Perception | 16 | `+1` при `cyberware.ocular.mk3` | Кодекс, +3 репутации | Засада Maelstrom | Скрытая карта сейфа | Подконтрольный патруль в зоне |
| insider.request-delivery | Empathy | 17 | `+2` при `flag.quest.first-steps.completed` | Активность Heywood | Потеря репутации | Разблокировка storyline «family-arc» | Угрозы банды |
| valentinos.gold-request | Negotiation | 18 | `+1` при `rep.gang.valentinos ≥ 30` | Магазин Valentinos | Флаг подозрения | Бесплатный золотой кейс | Padre закрывает скидки |
| valentinos.valentinos-mission | Streetwise | 18 | `+1` при `flag.rita.insider` | Активность карнавала | Аларм Arasaka | Доступ к сцене «Afterlife cameo» | Корпоративная охота |
| maelstrom.double-cross | Deception | 21 | `+2` при `item.militech-badge` | Решение эвента, награда | Засада охотников | Разблокировка легендарного модема | Немедленный рейд |
| maelstrom.support-maelstrom | Hacking | 20 | `+1` при `class.netrunner` | Активность фальшлогов | Аларм Militech | Секрет защиты Blackwall | Blackwall-контрудар |

### 3.4. Реакции на события

- **Событие:** `world.event.nusa_idol_live`
  - **Реплика:** «Идол NUSA снова поёт в прямом эфире. Корпы отвлеклись — можно провести тихую сделку.»
  - **Последствия:** скидка 15% на редкие предметы, снижение DC `valentinos-mission` на 1.
- **Событие:** `world.event.maelstrom_pipeline`
  - **Реплика:** «Maelstrom ломает трубу под Charter Hill. Пора решать, на чьей ты стороне.»
  - **Последствия:** активируется узел `maelstrom`, повышает награды за успехи на 20%.

## 4. Экспорт данных (YAML)

```yaml
conversation:
  id: dialogue-npc-rita-moreno
  entryNodes:
    - intro
  states:
    street-entry:
      requirements:
        rep.traders.rita: "0-24"
    insider-loop:
      requirements:
        rep.traders.rita: ">=25"
        flag.rita.met: true
    valentinos-favor:
      requirements:
        rep.gang.valentinos: ">=20"
    maelstrom-alert:
      requirements:
        flag.maelstrom.double_cross: true
  nodes:
    intro:
      options:
        - id: buy-basic
          success:
            unlockShop: rita-default
            setFlags: [flag.rita.met]
            reputation:
              rep.traders.rita: 2
        - id: drop-wsb
          checks:
            - stat: Streetwise
              dc: 14
          success:
            applyDiscount: 10
            setFlags: [flag.rita.met, flag.rita.insider]
          failure:
            priceIncrease: 3
            setFlags: [flag.rita.met]
    insider:
      options:
        - id: ask-rumors
          checks:
            - stat: Perception
              dc: 16
          success:
            unlockCodex: maelstrom-tunnel
            reputation:
              rep.traders.rita: 3
          failure:
            spawnEncounter: maelstrom-spotters
            reputation:
              rep.traders.rita: -3
        - id: request-delivery
          checks:
            - stat: Empathy
              dc: 17
          success:
            grantActivity: heywood-meds-run
            reputation:
              rep.traders.rita: 5
          failure:
            missionFail: heywood-meds-run
            reputation:
              rep.traders.rita: -4
    valentinos:
      options:
        - id: gold-request
          checks:
            - stat: Negotiation
              dc: 18
          success:
            unlockShop: rita-valentinos
            reputation:
              rep.traders.rita: 6
          failure:
            setFlags: [flag.rita.suspicious]
            reputation:
              rep.traders.rita: -5
        - id: valentinos-mission
          checks:
            - stat: Streetwise
              dc: 18
          success:
            grantActivity: valentinos-carnival-hack
            reputation:
              rep.traders.rita: 7
          failure:
            triggerAlarm: arasaka-counterintel
            reputation:
              rep.traders.rita: -6
    maelstrom:
      options:
        - id: double-cross
          checks:
            - stat: Deception
              dc: 21
          success:
            resolveEvent: maelstrom-double-cross
            rewards:
              - chip-militech-leverage
            reputation:
              rep.traders.rita: 9
          failure:
            spawnEncounter: maelstrom-hunters
            reputation:
              rep.traders.rita: -9
        - id: support-maelstrom
          checks:
            - stat: Hacking
              dc: 20
          success:
            grantActivity: maelstrom-fake-logs
            reputation:
              rep.traders.rita: 8
          failure:
            triggerAlarm: militech-sniffers
            reputation:
              rep.traders.rita: -8
```

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/rita-moreno` | `GET` | Вернуть диалог с активными состояниями и скидками |
| `/narrative/dialogues/rita-moreno/run-check` | `POST` | Выполнить проверки Streetwise/Perception/Empathy/Negotiation/Hacking |
| `/narrative/dialogues/rita-moreno/state` | `POST` | Сохранить флаги (`flag.rita.*`, `flag.maelstrom.*`), обновить репутации |
| `/narrative/dialogues/rita-moreno/telemetry` | `POST` | Отправить статистику выбора веток и исходов |

GraphQL поле `informantDialogue(id: ID!)` отдаёт `InformantDialogueNode` с `streetEvents` для связи с social-service.

## 6. Валидация и телеметрия

- `scripts/validate-dialogue-flags.ps1` сверяет `flag.rita.*`, `flag.maelstrom.*` и репутации с `reputation-formulas.md` и `quest-side-maelstrom-double-cross.md`.
- `scripts/dialogue-simulator.ps1 -Scenario rita` проверяет ветки `street-entry`, `insider-loop`, `valentinos-favor`, `maelstrom-alert`.
- Метрики: `rita-rumor-conversion`, `rita-delivery-success`, `rita-double-cross-outcome` (минимальная цель 55% успешных операций).

## 7. Награды и последствия

- **Репутация:** `rep.traders.rita`, `rep.gang.valentinos`, `rep.maelstrom` (динамические изменения).
- **Предметы:** `chip-militech-leverage`, редкие золотые импланты, медикаменты Heywood.
- **Активности:** `heywood-meds-run`, `valentinos-carnival-hack`, `maelstrom-fake-logs`.
- **Флаги:** `flag.rita.met`, `flag.rita.insider`, `flag.rita.valentinos`, `flag.rita.alert`, `flag.rita.suspicious`.

## 8. Связанные материалы

- `../npc-lore/common/traders/rita-moreno.md`
- `../dialogues/quest-side-maelstrom-double-cross.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/economy/economy-trading.md`
- `../../02-gameplay/world/events/world-events-2060-2077.md`

## 9. История изменений

- 2025-11-07 19:18 — Создан диалог Риты Морено (ветки слухов, Valentinos, Maelstrom, YAML, API). Статус `ready`.

