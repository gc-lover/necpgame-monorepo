# Диалоги — Хироши Танака

**ID диалога:** `dialogue-npc-hiroshi-tanaka`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 17:45  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/hiroshi-tanaka.md`, `../quests/main/002-choose-path-dnd-nodes.md`, `../quests/faction-world/arasaka-world-quests.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 17:45
**api-readiness-notes:** Диалог структурирован по состояниям, проверкам и событиям Arasaka. Готов для API задач narrative-service.

---

---

## 1. Контекст и цели

- **NPC:** Хироши Танака, корпоративный агент Arasaka, отвечающий за набор оператов.
- **Стадии:** проверка кандидата, выдача корпоративных контрактов, эскалация корпоративной войны.
- **Синопсис:** Хироши оценивает компетенции игрока, награждает лояльность и жестко реагирует на двойную игру.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| base | Первичная оценка кандидата | `rep.corp.arasaka` от 0 до 39 | `rep.corp.arasaka` |
| loyal | Высокая лояльность, допуск к операциям уровня А | `rep.corp.arasaka ≥ 40` и `flag.arasaka.clearanceA == true` | `rep.corp.arasaka`, `flag.arasaka.clearanceA` |
| suspicious | Подозрения в сотрудничестве с конкурентами | `flag.arasaka.militech_contact == true` | `flag.arasaka.militech_contact`, `rep.corp.arasaka` |
| lockdown | Корпоративный режим повышенной готовности | `world.event.arasaka_lockdown == true` | `world.arasaka_lockdown` |

- **Репутация:** Корпоративная шкала из `02-gameplay/social/reputation-formulas.md` (ветка Arasaka).
- **Проверки D&D:** Узлы N-10 (переговоры) и N-12 (демонстрация навыка) из квестов 002, а также корпоративные проверки из `arasaka-world-quests.md`.
- **Мировые события:** `world.event.arasaka_lockdown` описан в `02-gameplay/world/events/world-events-framework.md` (корпоративный режим).

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| base | «Добро пожаловать в Arasaka. Представь результаты, прежде чем просить доступ.» | default | `["Я готов к тесту", "Мне нужны детали", "Я передумал"]` |
| loyal | «Сотрудник уровня А. Ваши показатели впечатляют. Готовы к новой операции?» | `rep.corp.arasaka ≥ 40` | `["Назначайте задачу", "Нужен брифинг", "Запросить отпуск"]` |
| suspicious | «Удивительно видеть вас после контактов с Militech. Объяснитесь.» | `flag.arasaka.militech_contact` | `["Это миссия", "Ничего не докажете", "Молчание"]` |
| lockdown | «Arasaka Tower перешёл на протокол «Kōsetsu». Работать будем быстро и без шума.» | `world.event.arasaka_lockdown` | `["Подтверждаю", "Что происходит?", "Я не участвую"]` |

### 3.2. Узлы диалога

```
- node-id: assessment
  label: Первичная оценка
  entry-condition: state == "base"
  player-options:
    - option-id: assessment-test
      text: "Я готов к тесту"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
          modifiers: [{ source: "credential.corporate", value: +2 }]
      npc-response: "Докажите, что понимаете корпоративные приоритеты."
      outcomes:
        success: { effect: "grant_clearance", flag: "flag.arasaka.clearanceA", reputation: +8 }
        failure: { effect: "schedule_followup", cooldown: 1800 }
        critical-success: { effect: "grant_access", resource: "arasaka.vault.alpha", reputation: +12 }
        critical-failure: { effect: "deny_access", reputation: -6 }
    - option-id: assessment-info
      text: "Мне нужны детали"
      requirements: []
      npc-response: "Arasaka требует дисциплины. Готовы ли вы работать под NDA?"
      outcomes: { default: { effect: "unlock_codex", codex-id: "arasaka-onboarding" } }

- node-id: loyalty-briefing
  label: Брифинг уровня А
  entry-condition: state == "loyal"
  player-options:
    - option-id: op-serenity
      text: "Назначайте задачу"
      requirements:
        - type: stat-check
          stat: Strategy
          dc: 20
      npc-response: "Операция «Serenity». Нужно перехватить актив Maelstrom."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "arasaka-serenity", reputation: +10 }
        failure: { effect: "assign_backup_role", reputation: +3 }
        critical-success: { effect: "grant_asset", asset-id: "arasaka-delta-suit", reputation: +14 }
        critical-failure: { effect: "call_supervisor", reputation: -8 }
    - option-id: request-brief
      text: "Нужен брифинг"
      requirements: []
      npc-response: "Подробности загружены на ваш шифроканал."
      outcomes: { default: { effect: "deliver_brief", document: "brief-serenity.pdf" } }

- node-id: suspicion
  label: Подозрение в двойной игре
  entry-condition: state == "suspicious"
  player-options:
    - option-id: cover-story
      text: "Это часть операции"
      requirements:
        - type: stat-check
          stat: Deception
          dc: 21
      npc-response: "Предоставьте доказательства."
      outcomes:
        success: { effect: "convert_state", new-state: "base", clear-flags: ["flag.arasaka.militech_contact"], reputation: +4 }
        failure: { effect: "issue_penalty", penalty: "salary_cut", reputation: -8 }
        critical-success: { effect: "grant_shadow_task", task-id: "arasaka-double-agent", reputation: +6 }
        critical-failure: { effect: "trigger_lockdown", flag: "flag.arasaka.blacklist", reputation: -15 }
    - option-id: remain-silent
      text: "Молчание"
      requirements: []
      npc-response: "Лояльность измеряется делами. Ваше досье отправлено в безопасность."
      outcomes: { default: { effect: "apply_flag", flag: "flag.arasaka.silence_note", reputation: -5 } }

- node-id: lockdown-directive
  label: Протокол Kōsetsu
  entry-condition: world.arasaka_lockdown == true
  player-options:
    - option-id: confirm
      text: "Подтверждаю"
      requirements:
        - type: stat-check
          stat: Composure
          dc: 19
      npc-response: "Выполните зачистку корпуса и удерживайте линию связи."
      outcomes:
        success: { effect: "unlock_event", event-id: "arasaka-lockdown-response", reputation: +9 }
        failure: { effect: "assign_support_role", reputation: +2 }
    - option-id: decline
      text: "Я не участвую"
      requirements: []
      npc-response: "Запрос на перевод подан. Ожидайте решения руководства."
      outcomes: { default: { effect: "put_on_hold", duration: 7200, reputation: -3 } }
```

### 3.3. Ветвление по проверкам

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| assessment.assessment-test | Persuasion | 18 | `+2` корпоративные креды | Clearance A | Повтор | Доступ к хранилищу | Отказ и -6 репутации |
| loyalty-briefing.op-serenity | Strategy | 20 | `+1` за активный дрон-аналитик | Контракт Serenity | Резервная роль | Delta-suit | Вызов начальства |
| suspicion.cover-story | Deception | 21 | `+2` за флаг `flag.marco.corp` | Снятие подозрений | Штраф | Теневая задача | Блок-лист |
| lockdown-directive.confirm | Composure | 19 | `+1` за имплант стабилизации | Открыт эвент lockdown | Роль поддержки | — | — |

### 3.4. Реакции на события

- **Событие:** `world.event.arasaka_lockdown`
  - **Условие:** активирован корпоративный протокол, игрок имеет Clearance A
  - **Реплика:** «Протокол Kōsetsu активен. Arasaka Tower закрыт до стабилизации сетей.»
  - **Последствия:** доступ к ветке `lockdown-directive`, временный баф `corporate_focus`.

## 4. Награды и последствия

- **Репутация:** `rep.corp.arasaka` ±15, вторичные сдвиги для `rep.corp.militech` (негатив при лояльности Arasaka).
- **Предметы:** `arasaka-delta-suit`, `corp-access-pass`, конфиденциальные брифы.
- **Флаги:** `flag.arasaka.clearanceA`, `flag.arasaka.militech_contact`, `flag.arasaka.blacklist`, `flag.arasaka.silence_note`.
- **World-state:** запуск контрактов `arasaka-serenity`, событий `arasaka-lockdown-response` в базе ветвлений.

## 5. Связанные материалы

- `../npc-lore/important/hiroshi-tanaka.md`
- `../quests/main/002-choose-path-dnd-nodes.md`
- `../quests/faction-world/arasaka-world-quests.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-framework.md`

## 6. История изменений

- 2025-11-07 17:45 — Диалог переведён в статус ready, добавлены проверки и последствия.
- 2025-11-07 16:42 — Создан базовый набор диалогов.

