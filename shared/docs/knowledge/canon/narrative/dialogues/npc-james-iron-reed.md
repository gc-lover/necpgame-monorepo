# Диалоги — Джеймс «Железный» Рид

**ID диалога:** `dialogue-npc-james-iron-reed`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 17:18  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/james-iron-reed.md`, `../quests/main/002-choose-path-dnd-nodes.md`, `../quests/faction-world/arasaka-world-quests.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 17:18
**api-readiness-notes:** «Диалог Militech расширен: динамические состояния, YAML-экспорт, REST/GraphQL контракт, проверка флагов корпоративной войны. Готово для API задач.»

---

---

## 1. Контекст и цели

- **NPC:** Джеймс «Железный» Рид — военный агент Militech, куратор наёмников и внутренних операций.
- **Цель:** проверка лояльности, выдача боевых контрактов, реагирование на эскалации Arasaka.
- **Интеграции:** репутация Militech (`rep.corp.militech`), глобальные события корпоративных войн, флаги предательства/двойной игры.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| base | Первичная оценка бойца | `rep.corp.militech` от 0 до 39 | `rep.corp.militech` |
| loyal | Доверенный оперативник Militech | `rep.corp.militech ≥ 40` и `flag.militech.clearanceA == true` | `flag.militech.clearanceA`, `rep.corp.militech` |
| rival-suspect | Подозрение в работе на Arasaka | `flag.militech.arasaka_contact == true` | `flag.militech.arasaka_contact`, `rep.corp.militech` |
| war-alert | Режим «Corporate War Surge» | `world.event.corporate_war_escalation == true` | `world.corporate_war_escalation` |

- **Проверки:** Persuasion/Intimidation/Strategy/Technical checks (см. `quest-dnd-checks.md`).
- **Репутация:** Militech противопоставляется Arasaka, взаимодействует с глобальной системой фракций.
- **События:** `world.event.corporate_war_escalation` (массовая эскалация), `world.event.blackwall_breach` (каскад для тех веток).

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| base | «Militech не играет в угадайку. Докажи, что достоин стоять в строю.» | default | `["Отчитаться", "Что за операция?", "Мне нужен доступ"]` |
| loyal | «Офицер, у нас новая цель. Командование рассчитывает на тебя.» | `rep.corp.militech ≥ 40` | `["Приступаю", "Детали, сэр", "Нужен армор"]` |
| rival-suspect | «Смешно видеть тебя после твоих визитов к Arasaka. Объяснись.» | `flag.militech.arasaka_contact` | `["Это операция", "У вас нет доказательств", "Я ухожу"]` |
| war-alert | «Корпоративная война вышла на новый виток. Мы действуем без промедления.» | `world.corporate_war_escalation` | `["Готов к войне", "Цели?", "Нам нужен план"]` |

### 3.2. Узлы диалога

```
- node-id: intake
  label: Первичная оценка
  entry-condition: state == "base" and not flag.militech.intake_completed
  player-options:
    - option-id: intake-report
      text: "Отчитаться"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
          modifiers: [{ source: "credential.militech", value: +2 }]
      npc-response: "Militech ценит честность. Где ты служил раньше?"
      outcomes:
        success: { effect: "grant_clearance", flag: "flag.militech.clearanceA", reputation: { corp_militech: +8 } }
        failure: { effect: "schedule_retest", cooldown: 1800 }
        critical-success: { effect: "grant_asset", asset-id: "militech-ops-brief", reputation: { corp_militech: +12 } }
        critical-failure: { effect: "issue_warning", flag: "flag.militech.scrutiny", reputation: { corp_militech: -6 } }
    - option-id: intake-info
      text: "Что за операция?"
      requirements: []
      npc-response: "Сначала протокол. Доклад, потом допуск."
      outcomes: { default: { effect: "remind_protocol" } }

- node-id: loyal-brief
  label: Брифинг доверенного агента
  entry-condition: state == "loyal"
  player-options:
    - option-id: loyal-strike
      text: "Приступаю"
      requirements:
        - type: stat-check
          stat: Strategy
          dc: 20
      npc-response: "Операция «Iron Dawn». Перехватить груз Arasaka в Сингапуре."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "militech-iron-dawn", reputation: { corp_militech: +10 } }
        failure: { effect: "assign_support", contract-id: "militech-support-wing", reputation: { corp_militech: +3 } }
        critical-success: { effect: "grant_weapon", item: "militech-garrison-AR", reputation: { corp_militech: +14 } }
        critical-failure: { effect: "call_review", flag: "flag.militech.scrutiny", reputation: { corp_militech: -8 } }
    - option-id: loyal-armor
      text: "Нужен армор"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 19
      npc-response: "Убедительно. Получишь экзоскелет, но доложишь по итогам."
      outcomes: { success: { effect: "grant_gear", item: "militech-exo-frame", reputation: { corp_militech: +5 } }, failure: { effect: "deny_request" } }

- node-id: rival-hearing
  label: Допрос о контактах с Arasaka
  entry-condition: state == "rival-suspect"
  player-options:
    - option-id: rival-cover
      text: "Это операция"
      requirements:
        - type: stat-check
          stat: Deception
          dc: 21
          modifiers: [{ source: "flag.marco.corp", value: +1 }]
      npc-response: "Докажи. Militech не терпит двойных агентов."
      outcomes:
        success: { effect: "clear_flags", flags: ["flag.militech.arasaka_contact"], reputation: { corp_militech: +4 } }
        failure: { effect: "apply_penalty", penalty: "salary_cut", reputation: { corp_militech: -8 } }
        critical-success: { effect: "assign_shadow_mission", contract-id: "militech-counterintel", reputation: { corp_militech: +6 } }
        critical-failure: { effect: "flag_blacklist", flag: "flag.militech.blacklist", reputation: { corp_militech: -15 } }
    - option-id: rival-silence
      text: "У вас нет доказательств"
      requirements: []
      npc-response: "Достаточно подозрений. Все ваши операции под наблюдением."
      outcomes: { default: { effect: "increase_surveillance", flag: "flag.militech.scrutiny", reputation: { corp_militech: -5 } } }

- node-id: war-order
  label: Приказы во время эскалации
  entry-condition: world.corporate_war_escalation == true
  player-options:
    - option-id: war-ready
      text: "Готов к войне"
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 20
      npc-response: "Хорошо. Снимаем блокировки. Возглавишь удар по активу Arasaka в Берлине."
      outcomes:
        success: { effect: "unlock_event", event-id: "militech-warfront-berlin", reputation: { corp_militech: +9 }, reputation_delta: { corp_arasaka: -10 } }
        failure: { effect: "assign_defense", contract-id: "militech-defense-grid", reputation: { corp_militech: +2 } }
    - option-id: war-plan
      text: "Нам нужен план"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 19
      npc-response: "Получишь синопсис угроз. Сканируешь сети NetWatch и докладываешь."
      outcomes: { success: { effect: "grant_brief", document: "militech-war-analysis", reputation: { corp_militech: +5 } }, failure: { effect: "delay_brief" } }
```

### 3.3. Таблица проверок

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| intake.intake-report | Persuasion | 18 | `+2` корпоративные рекомендации | Clearance A, +8 репутация | Повтор через 30 мин | Секретный бриф, +12 | Флаг scrutiny, −6 |
| loyal-brief.loyal-strike | Strategy | 20 | — | Контракт Iron Dawn | Резервная роль | Оружие Militech, +14 | Флаг scrutiny, −8 |
| loyal-brief.loyal-armor | Negotiation | 19 | — | Экзоскелет, +5 | Отказ | — | — |
| rival-hearing.rival-cover | Deception | 21 | `+1` при флаге `flag.marco.corp` | Снятие подозрений | Штраф и −8 | Контрразведка | Чёрный список |
| war-order.war-ready | Intimidation | 20 | — | Эвент Berlin Warfront | Оборонительная роль | — | — |
| war-order.war-plan | Technical | 19 | — | Аналитический бриф | Нет доступа | — | — |

### 3.4. Реакции на события

- **Событие:** `world.event.corporate_war_escalation`
  - **Условие:** глобальная эскалация Militech vs Arasaka.
  - **Реплика:** «Arasaka открыла новый фронт. Мы отвечаем силой. Доминируем или умираем.»
  - **Последствия:** открывается ветка `war-order`, временный баф `militech_war_readiness`.

- **Событие:** `world.event.blackwall_breach`
  - **Условие:** технологический кризис, влияющий на Militech сети.
  - **Реплика:** «Blackwall пробит. Инженеры уже горят. Поддержи сетевиков или потеряем фронт.»
  - **Последствия:** выдаётся побочная линия `militech-net-defence`, модификатор `+2` к техническим проверкам в node `war-plan` при активном участии.

---

## 4. Экспорт данных

```yaml
conversation:
  id: dialogue-npc-james-iron-reed
  entryNodes:
    - intake
  states:
    base:
      requirements: { rep.corp.militech: "0-39" }
    loyal:
      requirements: { rep.corp.militech: ">=40", flag.militech.clearanceA: true }
    rival-suspect:
      requirements: { flag.militech.arasaka_contact: true }
    war-alert:
      requirements: { world.event.corporate_war_escalation: true }
  nodes:
    loyal-brief:
      onEnter: dialogue.loyalBrief()
      options:
        - id: loyal-strike
          text: "Приступаю"
          checks:
            - stat: Strategy
              dc: 20
          success:
            contract: militech-iron-dawn
            reputation:
              rep.corp.militech: 10
          failure:
            contract: militech-support-wing
            reputation:
              rep.corp.militech: 3
          critSuccess:
            item: militech-garrison-AR
            reputation:
              rep.corp.militech: 14
          critFailure:
            flags:
              - flag.militech.scrutiny
            reputation:
              rep.corp.militech: -8
```

> YAML-файл (`api/v1/narrative/dialogues/npc-james-iron-reed.yaml`) генерируется скриптом `scripts/export-dialogues.ps1` и используется narrative-service.

---

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/james-iron-reed` | `GET` | Вернуть текущую структуру диалога и активные ветки |
| `/narrative/dialogues/james-iron-reed/state` | `POST` | Зафиксировать прогресс игрока и обновить флаги Militech |
| `/narrative/dialogues/james-iron-reed/run-check` | `POST` | Выполнить проверку (Strategy/Deception/Intimidation) и вернуть исход |
| `/narrative/dialogues/james-iron-reed/telemetry` | `POST` | Отправить данные выборов и частоту срабатывания корпоративных событий |

GraphQL-поле `dialogue(id: ID!)` возвращает тип `DialogueNode`, содержащий состояния, проверки и исходы. При `world.event.corporate_war_escalation=true` API добавляет ветку `war-order`; при наличии `flag.militech.arasaka_contact` возвращается ветка `rival-hearing`.

---

## 6. Валидация и телеметрия

- Скрипт `validate-dialogue-flags.ps1` сверяет использование флагов Militech с `02-gameplay/social/reputation-formulas.md` и `02-gameplay/world/events/global-events-2020-2093.md`.
- При экспорте запускается `dialogue-simulator.ps1`, выполняющий сценарии `base`, `loyal`, `rival-suspect`, `war-alert` и проверяющий корректность выданных контрактов.
- Метрика `militech-dialogue-success-rate` отслеживает процент успешных проверок; при падении ниже 45% формируется тикет на балансировку.

---

## 7. Награды и последствия

- **Репутация:** `rep.corp.militech` ±15, снижение `rep.corp.arasaka` при агрессивных решениях, влияние на `rep.freelance.global` при нейтральных исходах.
- **Предметы:** `militech-garrison-AR`, `militech-exo-frame`, аналитические брифы Militech.
- **Флаги:** `flag.militech.intake_completed`, `flag.militech.clearanceA`, `flag.militech.scrutiny`, `flag.militech.blacklist`, `flag.militech.arasaka_contact`.
- **World-state:** активируются события `militech-iron-dawn`, `militech-warfront-berlin`, контрразведывательные задачи `militech-counterintel`.

## 8. Связанные материалы

- `../npc-lore/important/james-iron-reed.md`
- `../quests/main/002-choose-path-dnd-nodes.md`
- `../quests/faction-world/arasaka-world-quests.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/global-events-2020-2093.md`

## 9. История изменений

- 2025-11-07 17:18 — Добавлены экспортные структуры, REST/GraphQL контракт, проверки телеметрии. Версия 1.1.0, статус `ready` подтверждён.
- 2025-11-07 16:58 — Диалог утверждён, добавлены состояния Militech и реакции на корпоративную войну.