# Диалоги — Виктор Вектор

**ID диалога:** `dialogue-npc-viktor-vektor`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 18:05  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/viktor-vektor.md`, `../quests/side/medical-balance-chain.md`, `../../02-gameplay/social/reputation-formulas.md`, `../../02-gameplay/combat/combat-cyberpsychosis.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 18:05
**api-readiness-notes:** «Диалог риппердока дополнен расширенным YAML-экспортом, REST/GraphQL контрактом и валидацией медицинских флагов. Готов к API задачам.»

---

## 1. Контекст и цели

- **NPC:** Виктор Вектор — независимый риппердок Downtown, наставник по имплантам и профилактике киберпсихоза.
- **Цели:** установка имплантов, медицинская помощь, контроль киберпсихоза, сопровождение романтических веток (советы).
- **Интеграции:** `rep.med.viktor`, параметры здоровья, флаги киберпсихоза (`flag.cyberpsychosis.stage`), события лечебного протокола, советы для Hanako/Judy.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Флаги |
|-----------|----------|----------|-------|
| intake | Первичный осмотр | первое взаимодействие | `flag.viktor.intake` |
| steady | Постоянный пациент | `rep.med.viktor ≥ 30` и `flag.viktor.loyal == true` | `flag.viktor.loyal` |
| emergency | Риск киберпсихоза | `flag.cyberpsychosis.stage ≥ 2` | `flag.cyberpsychosis.stage` |
| romance-consult | Советы по романтике | `flag.romance.active == true` | `flag.romance.active` |

- **Проверки:** Technical, Insight, Willpower, Empathy, Deception.
- **События:** `world.event.med_crisis`, `world.event.blackwall_breach`.

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| intake | «Привет, чомбата. Снимай куртку, покажи, что импланты не дымятся.» | default | `["Нужна установка", "Меня ранили", "Я за советом"]` |
| steady | «Хорош видеть тебя. Как провода? Держал баланс?» | `rep.med.viktor ≥ 30` | `["Имплант апгрейд", "Проверка здоровья", "Поделиться новостями"]` |
| emergency | «Глаза стеклянные, пульс скачет. Сними импульс и дыши.» | `flag.cyberpsychosis.stage ≥ 2` | `["Помоги", "Сам справлюсь", "Мне нужен стабилизатор"]` |
| romance-consult | «Сердце не только железное, да? Рассказывай, кто пленил твой процессор.» | `flag.romance.active` | `["Hanako", "Judy", "Это сложно"]` |

### 3.2. Узлы диалога

```
- node-id: intake-check
  label: Первичная диагностика
  entry-condition: state == "intake" and not flag.viktor.intake
  player-options:
    - option-id: intake-status
      text: "Нужна установка"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 16
      npc-response: "Сначала проверим совместимость. Пульс держится?"
      outcomes:
        success: { effect: "set_flag", flag: "flag.viktor.intake", reputation: { med_viktor: +6 }, reward: "medkit.basic" }
        failure: { effect: "micro_seizure", penalty: "hp_damage", reputation: { med_viktор: -4 } }
        critical-success: { effect: "grant_implant", item: "viktor-basic-reflex", reputation: { med_viktор: +8 } }
        critical-failure: { effect: "implant_rejection", event: "medical_emergency", reputation: { med_viktор: -8 } }
    - option-id: intake-wound
      text: "Меня ранили"
      requirements: []
      npc-response: "Ложись. Спасём твою кожу и кредиты."
      outcomes: { default: { effect: "heal", value: 0.5, reputation: { med_viktор: +4 } } }

- node-id: steady-upgrade
  label: Продвинутый имплант
  entry-condition: state == "steady"
  player-options:
    - option-id: upgrade-request
      text: "Имплант апгрейд"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 19
      npc-response: "Подготовь нервную систему. Это не игрушка."
      outcomes:
        success: { effect: "grant_implant", item: "viktor-kinetic-shield", reputation: { med_viktор: +10 } }
        failure: { effect: "overload", penalty: "energy_burn", reputation: { med_viktор: -5 } }
        critical-success: { effect: "grant_combo", items: ["viktor-kinetic-shield", "viktor-neuro-damper"], reputation: { med_viktор: +12 } }
    - option-id: health-check
      text: "Проверка здоровья"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 17
      npc-response: "Сканер ничего не скрывает. Готов услышать правду?"
      outcomes:
        success: { effect: "remove_negative_status", flag: "flag.health.debuff", reputation: { med_viktор: +6 } }
        failure: { effect: "warning", message: "Need detox", reputation: { med_viktор: +2 } }

- node-id: emergency-stabilize
  label: Стабилизация киберпсихоза
  entry-condition: state == "emergency"
  player-options:
    - option-id: emergency-help
      text: "Помоги"
      requirements:
        - type: stat-check
          stat: Willpower
          dc: 20
      npc-response: "Не сопротивляйся. Вангельдовый стабилизатор держи в руке."
      outcomes:
        success: { effect: "reduce_cyber_stage", value: 1, reward: "psycho-stabilizer", reputation: { med_viktор: +9 } }
        failure: { effect: "relapse", penalty: "hp_damage", reputation: { med_viktор: -6 } }
        critical-success: { effect: "full_reset", flag: "flag.cyberpsychosis.stage", value: 0, reputation: { med_viktор: +12 } }
    - option-id: emergency-pride
      text: "Сам справлюсь"
      requirements: []
      npc-response: "Гордыня — первый шаг к психозу. Возвращайся, когда поймёшь."
      outcomes: { default: { effect: "increase_cyber_stage", value: 1, reputation: { med_viktор: -8 } } }

- node-id: romance-consult
  label: Советы по романтике
  entry-condition: state == "romance-consult"
  player-options:
    - option-id: romance-hanako
      text: "Hanako"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 18
      npc-response: "Корпораты не шутят. Держи себя в тонусе и соблюдай их ритуалы."
      outcomes:
        success: { effect: "grant_tip", item: "romance-hanako-tip", reputation: { romance_hanako: +5 } }
        failure: { effect: "romance_warning", message: "Hanako demands perfection", reputation: { romance_hanako: +2 } }
    - option-id: romance-judy
      text: "Judy"
      requirements:
        - type: stat-check
          stat: Empathy
          dc: 17
      npc-response: "С Judy будь честным. Она слышит ложь даже через имплант."
      outcomes:
        success: { effect: "grant_tip", item: "romance-judy-tip", reputation: { romance_judy: +5 } }
        failure: { effect: "romance_warning", message: "Judy needs trust", reputation: { romance_judy: +2 } }
```

### 3.3. Таблица проверок

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| intake.intake-status | Technical | 16 | `+2` Techie | Принят пациентом | Судороги | Имплант бонус | Мед. чрезвычайная ситуация |
| steady.upgrade-request | Technical | 19 | `+1` Cyberware affinity | Улучшенный имплант | Перегрузка | Комбо имплант | — |
| steady.health-check | Insight | 17 | `+2` при `flag.health.monitor` | Снятие дебаффа | Предупреждение | — | — |
| emergency.emergency-help | Willpower | 20 | `+2` при `item.psycho-stabilizer` | Снижение стадии | Рецидив | Полный сброс | — |
| romance.romance-hanako | Insight | 18 | `+1` при `flag.romance.hanako.date1` | Совет Hanako | Предупреждение | — | — |
| romance.romance-judy | Empathy | 17 | `+1` при `flag.romance.judy.date1` | Совет Judy | Предупреждение | — | — |

### 3.4. Реакции на события

- **Событие:** `world.event.med_crisis`
  - Реплика: «Город горит. Очередь под клиникой — пять кварталов. Не сдавайся.»
  - Последствия: скидка 10% на медуслуги, открытие ветки массовой помощи.

- **Событие:** `world.event.blackwall_breach`
  - Реплика: «Net снова сходит с ума. Проверяй импланты раз в шесть часов.»
  - Последствия: временное снижение DC в `emergency-stabilize` (-2).

---

## 4. Экспорт данных

```yaml
conversation:
  id: dialogue-npc-viktor-vektor
  entryNodes:
    - intake-check
  states:
    intake:
      requirements: {}
    steady:
      requirements:
        rep.med.viktor: ">=30"
        flag.viktor.loyal: true
    emergency:
      requirements:
        flag.cyberpsychosis.stage: ">=2"
    romance-consult:
      requirements:
        flag.romance.active: true
  nodes:
    intake-check:
      options:
        - id: intake-status
          checks:
            - stat: Technical
              dc: 16
          success:
            setFlags: [flag.viktor.intake]
            reputation:
              rep.med.viktор: 6
            items: [medkit.basic]
        - id: intake-wound
          success:
            heal: 0.5
            reputation:
              rep.med.viktor: 4
    steady-upgrade:
      onEnter: dialogue.steadyUpgrade()
      options:
        - id: upgrade-request
          checks:
            - stat: Technical
              dc: 19
          success:
            items: [viktor-kinetic-shield]
            reputation:
              rep.med.viktor: 10
          failure:
            penalties: [energy_burn]
            reputation:
              rep.med.viktор: -5
        - id: health-check
          checks:
            - stat: Insight
              dc: 17
          success:
            removeFlags: [flag.health.debuff]
            reputation:
              rep.med.viktor: 6
          failure:
            warnings: [Need detox]
            reputation:
              rep.med.viktор: 2
    emergency-stabilize:
      onEnter: dialogue.emergency()
      options:
        - id: emergency-help
          checks:
            - stat: Willpower
              dc: 20
          success:
            adjust:
              flag.cyberpsychosis.stage: -1
            reward: psycho-stabilizer
            reputation:
              rep.med.viktor: 9
          failure:
            penalties: [hp_damage]
            reputation:
              rep.med.viktор: -6
        - id: emergency-pride
          success:
            adjust:
              flag.cyberpsychosis.stage: +1
            reputation:
              rep.med.viktor: -8
    romance-consult:
      onEnter: dialogue.romance()
      options:
        - id: romance-hanako
          checks:
            - stat: Insight
              dc: 18
          success:
            tips: [romance-hanako-tip]
            reputation:
              romance_hanako: 5
        - id: romance-judy
          checks:
            - stat: Empathy
              dc: 17
          success:
            tips: [romance-judy-tip]
            reputation:
              romance_judy: 5
```

> Экспорт реализован скриптом `scripts/export-dialogues.ps1`, результат — `api/v1/narrative/dialogues/npc-viktor-vektor.yaml`.

---

## 5. REST / GraphQL API

| Endpoint | Метод | Описание |
| --- | --- | --- |
| `/narrative/dialogues/viktor-vektor` | `GET` | Получить актуальную структуру диалога с учётом здоровья и репутации |
| `/narrative/dialogues/viktor-vektor/state` | `POST` | Сохранить прогресс (флаги здоровья, стадию киберпсихоза, активные импланты) |
| `/narrative/dialogues/viktor-vektor/run-check` | `POST` | Выполнить проверку (Technical/Insight/Willpower/Empathy) |
| `/narrative/dialogues/viktor-vektor/telemetry` | `POST` | Отправить телеметрию: стадии психоза, апгрейды имплантов, советы Виктора |

GraphQL-поле `dialogue(id: ID!)` возвращает `DialogueNode` вместе с `medicalContext` (стадия киберпсихоза, активные импланты, романтические советы). При `flag.cyberpsychosis.stage ≥ 2` включается ветка `emergency-stabilize`; при `flag.romance.active=true` — `romance-consult`.

---

## 6. Телеметрия и валидация

- `validate-dialogue-flags.ps1` сверяет `flag.viktor.*`, `flag.cyberpsychosis.stage`, `flag.romance.*` с `combat/combat-cyberpsychosis.md`, `medical-balance-chain.md` и формулами репутации.
- `dialogue-simulator.ps1` прогоняет сценарии `intake`, `steady`, `emergency`, `romance-consult`, проверяя выдачу имплантов и изменение стадий психоза.
- Метрики: `medical-stabilization-rate` (цель ≥70%), `romance-consult-usage` (распределение советов). При рецидивах >35% автоматически создаётся тикет балансировки.

---

## 7. Награды и последствия

- **Репутация:** `rep.med.viktor` ±12; влияет на `flag.cyberpsychosis.stage` и романтические флаги.
- **Предметы:** `medkit.basic`, `viktor-kinetic-shield`, `viktor-neuro-damper`, `psycho-stabilizer`.
- **Флаги:** `flag.viktor.intake`, `flag.viktor.loyal`, `flag.cyberpsychosis.stage`, `flag.romance.hanako.tip1`, `flag.romance.judy.tip1`.
- **World-state:** события `medical_emergency`, `cyberpsychosis_outbreak`, рекомендации `romance-hanako-tip`, `romance-judy-tip`.

## 8. Связанные материалы

- `../npc-lore/important/viktor-vektor.md`
- `../quests/side/medical-balance-chain.md`
- `../../02-gameplay/combat/combat-cyberpsychosis.md`
- `../../02-gameplay/social/reputation-formulas.md`

## 9. История изменений

- 2025-11-07 18:05 — Расширен экспорт (все ветки), обновлены REST/GraphQL и метрики. Статус `ready`, версия 1.1.0.
- 2025-11-07 17:10 — Диалог оформлен с медицинскими ветками, проверками и экспортом.
# Диалоги — Виктор Вектор

**ID диалога:** `dialogue-npc-viktor-vektor`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 18:05  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/viktor-vektor.md`, `../quests/side/medical-balance-chain.md`, `../../02-gameplay/social/reputation-formulas.md`, `../../02-gameplay/combat/combat-cyberpsychosis.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 18:05
**api-readiness-notes:** «Диалог риппердока дополнен расширенным YAML-экспортом, REST/GraphQL контрактом и валидацией медицинских флагов. Готов к API задачам.»

---

## 1. Контекст и цели

- **NPC:** Виктор Вектор — независимый риппердок Downtown, наставник по имплантам и профилактике киберпсихоза.
- **Цели:** установка имплантов, медицинская помощь, контроль киберпсихоза, сопровождение романтики (советы).
- **Интеграции:** репутация `rep.med.viktor`, параметры здоровья, флаги киберпсихоза (`flag.cyberpsychosis.stage`), события лечебного протокола.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| intake | Первичный осмотр пациента | новое взаимодействие | `flag.viktor.intake` |
| steady | Постоянный пациент | `rep.med.viktor ≥ 30` и `flag.viktor.loyal == true` | `flag.viktor.loyal`, `rep.med.viktor` |
| emergency | Состояние риска киберпсихоза | `flag.cyberpsychosis.stage ≥ 2` | `flag.cyberpsychosis.stage` |
| romance-consult | Консультации по романтике | `flag.romance.active == true` | `flag.romance.active` |

- **Проверки:** Medicine (Technical), Empathy (Insight), Willpower, Deception.
- **События:** `world.event.med_crisis` (массовый киберпсихоз), `world.event.blackwall_breach` (повышенные риски)

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| intake | «Привет, чомбата. Снимай куртку, покажи, что импланты не дымятся.» | default | `["Нужна установка", "Меня ранили", "Я за советом"]` |
| steady | «Хорош видеть тебя. Как провода? Держал баланс?» | `rep.med.viktor ≥ 30` | `["Имплант апгрейд", "Проверка здоровья", "Поделиться новостями"]` |
| emergency | «Глаза стеклянные, пульс скачет. Сними импульс и дыши.» | `flag.cyberpsychosis.stage ≥ 2` | `["Помоги", "Сам справлюсь", "Мне нужен стабилизатор"]` |
| romance-consult | «Сердце не только железное, да? Рассказывай, кто пленил твой процессор.» | `flag.romance.active` | `["Hanako", "Judy", "Это сложно"]` |

### 3.2. Узлы диалога

```
- node-id: intake-check
  label: Первичная диагностика
  entry-condition: state == "intake" and not flag.viktor.intake
  player-options:
    - option-id: intake-status
      text: "Нужна установка"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 16
      npc-response: "Сначала проверим совместимость. Пульс держится?"
      outcomes:
        success: { effect: "set_flag", flag: "flag.viktor.intake", reputation: { med_viktor: +6 }, reward: "medkit.basic" }
        failure: { effect: "micro_seizure", penalty: "hp_damage", reputation: { med_viktor: -4 } }
        critical-success: { effect: "grant_implant", item: "viktor-basic-reflex", reputation: { med_viktor: +8 } }
        critical-failure: { effect: "implant_rejection", event: "medical_emergency", reputation: { med_viktor: -8 } }
    - option-id: intake-wound
      text: "Меня ранили"
      requirements: []
      npc-response: "Ложись. Спасём твою кожу и кредиты."
      outcomes: { default: { effect: "heal", value: 0.5, reputation: { med_viktor: +4 } } }

- node-id: steady-upgrade
  label: Продвинутый имплант
  entry-condition: state == "steady"
  player-options:
    - option-id: upgrade-request
      text: "Имплант апгрейд"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 19
      npc-response: "Подготовь нервную систему. Это не игрушка."
      outcomes:
        success: { effect: "grant_implant", item: "viktor-kinetic-shield", reputation: { med_viktor: +10 } }
        failure: { effect: "overload", penalty: "energy_burn", reputation: { med_viktor: -5 } }
        critical-success: { effect: "grant_combo", items: ["viktor-kinetic-shield", "viktor-neuro-damper"], reputation: { med_viktor: +12 } }
    - option-id: health-check
      text: "Проверка здоровья"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 17
      npc-response: "Сканер ничего не скрывает. Готов услышать правду?"
      outcomes:
        success: { effect: "remove_negative_status", flag: "flag.health.debuff", reputation: { med_viktor: +6 } }
        failure: { effect: "warning", message: "Need detox", reputation: { med_viktor: +2 } }

- node-id: emergency-stabilize
  label: Стабилизация киберпсихоза
  entry-condition: state == "emergency"
  player-options:
    - option-id: emergency-help
      text: "Помоги"
      requirements:
        - type: stat-check
          stat: Willpower
          dc: 20
      npc-response: "Не сопротивляйся. Вангельдовый стабилизатор держи в руке."
      outcomes:
        success: { effect: "reduce_cyber_stage", value: 1, reward: "psycho-stabilizer", reputation: { med_viktor: +9 } }
        failure: { effect: "relapse", penalty: "hp_damage", reputation: { med_viktor: -6 } }
        critical-success: { effect: "full_reset", flag: "flag.cyberpsychosis.stage", value: 0, reputation: { med_viktor: +12 } }
    - option-id: emergency-pride
      text: "Сам справлюсь"
      requirements: []
      npc-response: "Гордыня — первый шаг к психозу. Возвращайся, когда поймёшь."
      outcomes: { default: { effect: "increase_cyber_stage", value: 1, reputation: { med_viktor: -8 } } }

- node-id: romance-consult
  label: Советы по романтике
  entry-condition: state == "romance-consult"
  player-options:
    - option-id: romance-hanako
      text: "Hanako"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 18
      npc-response: "Корпораты не шутят. Держи себя в тонусе и соблюдай их ритуалы."
      outcomes:
        success: { effect: "grant_tip", item: "romance-hanako-tip", reputation: { romance_hanako: +5 } }
        failure: { effect: "romance_warning", message: "Hanako demands perfection", reputation: { romance_hanako: +2 } }
    - option-id: romance-judy
      text: "Judy"
      requirements:
        - type: stat-check
          stat: Empathy
          dc: 17
      npc-response: "С Judy будь честным. Она слышит ложь даже через имплант."
      outcomes:
        success: { effect: "grant_tip", item: "romance-judy-tip", reputation: { romance_judy: +5 } }
        failure: { effect: "romance_warning", message: "Judy needs trust", reputation: { romance_judy: +2 } }
```

### 3.3. Таблица проверок

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| intake.intake-status | Technical | 16 | `+2` Techie | Принят пациентом | Судороги | Имплант бонус | Мед. чрезвычайная ситуация |
| steady.upgrade-request | Technical | 19 | `+1` Cyberware affinity | Улучшенный имплант | Перегрузка | Комбо имплант | — |
| steady.health-check | Insight | 17 | `+2` при `flag.health.monitor` | Снятие дебаффа | Предупреждение | — | — |
| emergency.emergency-help | Willpower | 20 | `+2` при `item.psycho-stabilizer` | Снижение стадии | Рецидив | Полный сброс | — |
| romance.romance-hanako | Insight | 18 | `+1` при `flag.romance.hanako.date1` | Совет Hanako | Предупреждение | — | — |
| romance.romance-judy | Empathy | 17 | `+1` при `flag.romance.judy.date1` | Совет Judy | Предупреждение | — | — |

### 3.4. Реакции на события

- **Событие:** `world.event.med_crisis`
  - Реплика: «Город горит. Очередь под клиникой — пять кварталов. Не сдавайся.»
  - Последствия: скидка 10% на медуслуги, открытие временной ветки массовой помощи.

- **Событие:** `world.event.blackwall_breach`
  - Реплика: «Net снова сходит с ума. Проверяй импланты раз в шесть часов.»
  - Последствия: временное снижение DC в узле `emergency-stabilize` (-2).

## 4. Экспорт данных

```yaml
conversation:
  id: dialogue-npc-viktor-vektor
  entryNodes:
    - intake-check
  states:
    intake:
      requirements: {}
    steady:
      requirements:
        rep.med.viktor: ">=30"
        flag.viktor.loyal: true
    emergency:
      requirements:
        flag.cyberpsychosis.stage: ">=2"
    romance-consult:
      requirements:
        flag.romance.active: true
  nodes:
    intake-check:
      options:
        - id: intake-status
          checks:
            - stat: Technical
              dc: 16
          success:
            setFlags: [flag.viktor.intake]
            reputation:
              rep.med.viktor: 6
            items: [medkit.basic]
        - id: intake-wound
          success:
            heal: 0.5
            reputation:
              rep.med.viktor: 4
    steady-upgrade:
      onEnter: dialogue.steadyUpgrade()
      options:
        - id: upgrade-request
          checks:
            - stat: Technical
              dc: 19
          success:
            items: [viktor-kinetic-shield]
            reputation:
              rep.med.viktor: 10
          failure:
            penalties: [energy_burn]
            reputation:
              rep.med.viktor: -5
        - id: health-check
          checks:
            - stat: Insight
              dc: 17
          success:
            removeFlags: [flag.health.debuff]
            reputation:
              rep.med.viktor: 6
          failure:
            warnings: [Need detox]
            reputation:
              rep.med.viktor: 2
    emergency-stabilize:
      onEnter: dialogue.emergency()
      options:
        - id: emergency-help
          checks:
            - stat: Willpower
              dc: 20
          success:
            adjust:
              flag.cyberpsychosis.stage: -1
            reward: psycho-stabilizer
            reputation:
              rep.med.viktor: 9
          failure:
            penalties: [hp_damage]
            reputation:
              rep.med.viktor: -6
        - id: emergency-pride
          success:
            adjust:
              flag.cyberpsychosis.stage: +1
            reputation:
              rep.med.viktor: -8
    romance-consult:
      onEnter: dialogue.romance()
      options:
        - id: romance-hanako
          checks:
            - stat: Insight
              dc: 18
          success:
            tips: [romance-hanako-tip]
            reputation:
              romance_hanako: 5
        - id: romance-judy
          checks:
            - stat: Empathy
              dc: 17
          success:
            tips: [romance-judy-tip]
            reputation:
              romance_judy: 5
```

> Экспорт реализован скриптом `scripts/export-dialogues.ps1`, результат — `api/v1/narrative/dialogues/npc-viktor-vektor.yaml` (подхватывается narrative-service и фронтендом).

## 5. REST / GraphQL API

| Endpoint | Метод | Описание |
| --- | --- | --- |
| `/narrative/dialogues/viktor-vektor` | `GET` | Получить актуальную структуру диалога с учётом здоровья и репутации игрока |
| `/narrative/dialogues/viktor-vektor/state` | `POST` | Сохранить прогресс (флаги здоровья, стадию киберпсихоза, активные импланты) |
| `/narrative/dialogues/viktor-vektor/run-check` | `POST` | Выполнить проверку (Technical/Insight/Willpower/Empathy) и рассчитать исход |
| `/narrative/dialogues/viktor-vektor/telemetry` | `POST` | Отправить телеметрию: стадии психоза, заказы имплантов, использование советов |

GraphQL-поле `dialogue(id: ID!)` возвращает `DialogueNode` и `medicalContext` (стадии киберпсихоза, активные импланты, романтические советы). При `flag.cyberpsychosis.stage ≥ 2` добавляется ветка `emergency-stabilize`, при `flag.romance.active = true` — `romance-consult`.

## 6. Телеметрия и валидация

- `validate-dialogue-flags.ps1` сверяет `flag.viktor.*`, `flag.cyberpsychosis.stage`, `flag.romance.*` с документами `combat/combat-cyberpsychosis.md`, `medical-balance-chain.md`, формулами репутации.
- `dialogue-simulator.ps1` прогоняет сценарии `intake`, `steady`, `emergency`, `romance-consult`, сверяя выдачу имплантов, лечение и дельту репутации.
- Метрики: `medical-stabilization-rate` (цель ≥70% успехов) и `romance-consult-usage` (распределение советов, отслеживание перекоса). При рецидивах >35% автоматически создаётся тикет балансировки.

## 7. Награды и последствия

- **Репутация:** `rep.med.viktor` ±12, вторичные эффекты на `flag.cyberpsychosis.stage` и романтические флаги.
- **Предметы:** `medkit.basic`, `viktor-kinetic-shield`, `viktor-neuro-damper`, `psycho-stabilizer`.
- **Флаги:** `flag.viktor.intake`, `flag.viktor.loyal`, `flag.cyberpsychosis.stage`, `flag.romance.hanako.tip1`, `flag.romance.judy.tip1`.
- **World-state:** события `medical_emergency`, `cyberpsychosis_outbreak`, рекомендации `romance-hanako-tip`, `romance-judy-tip`.

## 8. Связанные материалы

- `../npc-lore/important/viktor-vektor.md`
- `../quests/side/medical-balance-chain.md`
- `../../02-gameplay/combat/combat-cyberpsychosis.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-framework.md`

## 9. История изменений

- 2025-11-07 18:05 — Расширен экспорт (все ветки), обновлены REST/GraphQL и метрики. Статус `ready`, версия 1.1.0.
- 2025-11-07 17:10 — Добавлен диалог Виктора Вектора с медицинскими ветками, экспортом и API спецификой.

