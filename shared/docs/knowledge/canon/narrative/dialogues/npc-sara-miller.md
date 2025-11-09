# Диалоги — Сара Миллер

**ID диалога:** `dialogue-npc-sara-miller`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 17:05  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/sara-miller.md`, `../quests/main/002-choose-path-dnd-nodes.md`, `../quests/side/ncpd-patrol-chain.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 17:05
**api-readiness-notes:** «Диалог NCPD оформлен с состояниями, проверками, экспортом и REST/GraphQL контрактом. Инфракции синхронизированы с gameplay.»

---

---

## 1. Контекст и цели

- **NPC:** Сара Миллер, офицер NCPD, набирающая надёжных оперативников.
- **Стадии:** собеседование, операции по поддержанию порядка, реакция на чрезвычайные ситуации.
- **Синопсис:** Сара оценивает правдивость игрока, делегирует задания и строго следит за дисциплиной.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| base | Начальное собеседование | `rep.law.ncpd` от 0 до 29 | `rep.law.ncpd` |
| trusted | Доверенный оперативник | `rep.law.ncpd ≥ 30` и `flag.ncpd.badge == true` | `rep.law.ncpd`, `flag.ncpd.badge` |
| disciplinary | Игрок замечен в преступной активности | `flag.ncpd.infraction_points ≥ 3` | `flag.ncpd.infraction_points` |
| emergency | Активирован режим чрезвычайного положения | `world.event.city_emergency == true` | `world.city_emergency` |

- **Репутация:** шкала NCPD из `02-gameplay/social/reputation-formulas.md`.
- **Проверки D&D:** социальные проверки узла N-10, дисциплинарные сцены из `ncpd-patrol-chain.md`.
- **Мировые события:** `world.event.city_emergency` описан в `02-gameplay/world/events/world-events-framework.md` (городской кризис).

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| base | «Офицер Миллер. Представь резюме и пройди проверку на честность.» | default | `["Я готов", "Нужно больше информации", "Не сейчас"]` |
| trusted | «Агент badge-класса. Город полагается на вас. Какие задачи предпочтительны?» | `rep.law.ncpd ≥ 30` | `["Патруль", "Расследование", "Отпуск"]` |
| disciplinary | «Ваше досье набрало инфракций. Объяснитесь.» | `flag.ncpd.infraction_points ≥ 3` | `["Это провокация", "Готов принять штраф", "Ничего не скажу"]` |
| emergency | «Город в режиме «Amber». Нам нужны все руки.» | `world.event.city_emergency` | `["Вступаю в строй", "Каков масштаб?", "Я не готов"]` |

### 3.2. Узлы диалога

```
- node-id: interview
  label: Собеседование NCPD
  entry-condition: state == "base"
  player-options:
    - option-id: interview-ready
      text: "Я готов"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
          modifiers: [{ source: "credential.ncpd", value: +2 }]
      npc-response: "Тогда ответь: почему ты хочешь служить городу?"
      outcomes:
        success: { effect: "grant_badge", flag: "flag.ncpd.badge", reputation: +8 }
        failure: { effect: "schedule_retest", cooldown: 1800 }
        critical-success: { effect: "assign_case", contract-id: "ncpd-fast-track", reputation: +12 }
        critical-failure: { effect: "flag_infraction", value: 1, reputation: -5 }
    - option-id: interview-info
      text: "Нужно больше информации"
      requirements: []
      npc-response: "NCPD требует дисциплины. Вся деятельность фиксируется и проверяется."
      outcomes: { default: { effect: "unlock_codex", codex-id: "ncpd-onboarding" } }

- node-id: trusted-brief
  label: Брифинг для доверенных
  entry-condition: state == "trusted"
  player-options:
    - option-id: patrol
      text: "Патруль"
      requirements:
        - type: stat-check
          stat: Awareness
          dc: 17
      npc-response: "Сектор Downtown-12 нуждается в присутствии."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "ncpd-patrol-dt12", reputation: +6 }
        failure: { effect: "assign_partner", contract-id: "ncpd-support", reputation: +2 }
        critical-success: { effect: "grant_gadget", item: "ncpd-smart-drone", reputation: +10 }
        critical-failure: { effect: "flag_incident", value: 1, reputation: -6 }
    - option-id: investigation
      text: "Расследование"
      requirements:
        - type: stat-check
          stat: Logic
          dc: 19
      npc-response: "Есть дело по незаконным киберимплантам."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "ncpd-cybercrime-taskforce", reputation: +8 }
        failure: { effect: "partial_data", reputation: +3 }

- node-id: disciplinary-review
  label: Дисциплинарное слушание
  entry-condition: state == "disciplinary"
  player-options:
    - option-id: contest
      text: "Это провокация"
      requirements:
        - type: stat-check
          stat: Deception
          dc: 20
      npc-response: "Предоставьте доказательства прямо сейчас."
      outcomes:
        success: { effect: "reduce_infraction", value: 2, reputation: +4 }
        failure: { effect: "increase_infraction", value: 1, reputation: -8 }
        critical-success: { effect: "grant_internal_case", contract-id: "ncpd-ia-cleanup", reputation: +6 }
        critical-failure: { effect: "suspension", duration: 7200, reputation: -12 }
    - option-id: accept
      text: "Готов принять штраф"
      requirements: []
      npc-response: "Снимайте галочку в протоколе и платите штраф."
      outcomes: { default: { effect: "apply_fee", currency: "eddies", amount: 2000, reduce-infraction: 1 } }

- node-id: emergency-command
  label: Чрезвычайный режим
  entry-condition: world.city_emergency == true
  player-options:
    - option-id: deploy
      text: "Вступаю в строй"
      requirements:
        - type: stat-check
          stat: Resolve
          dc: 18
      npc-response: "Переходите на канал 77. Город горит."
      outcomes:
        success: { effect: "unlock_event", event-id: "ncpd-emergency-response", reputation: +9 }
        failure: { effect: "assign_civilian_support", reputation: +3 }
    - option-id: scope
      text: "Каков масштаб?"
      requirements: []
      npc-response: "Детонации на трёх станциях. Приоритет — эвакуация."
      outcomes: { default: { effect: "unlock_codex", codex-id: "city-emergency-report" } }
```

### 3.3. Ветвление по проверкам

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| interview.interview-ready | Persuasion | 18 | `+2` полицейские рекомендации | Значок NCPD | Пересдача | Быстрый кейс | Инфракция |
| trusted-brief.patrol | Awareness | 17 | `+1` активный дрон | Патруль DT12 | Поддержка | Смарт-дрон | Инцидент |
| trusted-brief.investigation | Logic | 19 | `+2` за имплант анализа | Кибер-кейс | Частичные данные | — | — |
| disciplinary-review.contest | Deception | 20 | — | Снятие 2 штрафов | +1 инфракция | Внутреннее дело | Отстранение |
| emergency-command.deploy | Resolve | 18 | `+1` за моральный баф | Эвент emergency | Поддержка | — | — |

### 3.4. Реакции на события

- **Событие:** `world.event.city_emergency`
  - **Условие:** активирован режим Amber в Night City
  - **Реплика:** «Город живёт благодаря порядку. Выполняем эвакуацию и держим периметр.»
  - **Последствия:** открывается ветка `emergency-command`, выдается баф `ncpd_focus`.

---

## 4. Экспорт данных

```yaml
conversation:
  id: dialogue-npc-sara-miller
  entryNodes:
    - interview
  states:
    base:
      requirements: { rep.law.ncpd: "0-29" }
    trusted:
      requirements: { rep.law.ncpd: ">=30", flag.ncpd.badge: true }
    disciplinary:
      requirements: { flag.ncpd.infraction_points: ">=3" }
    emergency:
      requirements: { world.event.city_emergency: true }
  nodes:
    interview:
      onEnter: dialogue.interview()
      options:
        - id: interview-ready
          text: "Я готов"
          checks:
            - stat: Persuasion
              dc: 18
              modifiers:
                - source: credential.ncpd
                  value: 2
          success:
            grantFlag: flag.ncpd.badge
            reputation:
              rep.law.ncpd: 8
          failure:
            cooldown: 1800
          critSuccess:
            contract: ncpd-fast-track
            reputation:
              rep.law.ncpd: 12
          critFailure:
            addInfractions: 1
            reputation:
              rep.law.ncpd: -5
```

> YAML-файл (`api/v1/narrative/dialogues/npc-sara-miller.yaml`) генерируется скриптом `scripts/export-dialogues.ps1` и используется narrative-service.

---

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/sara-miller` | `GET` | Получить предзагруженный диалог и текущие узлы |
| `/narrative/dialogues/sara-miller/state` | `POST` | Сохранить прогресс игрока (активные флаги, следующая нода) |
| `/narrative/dialogues/sara-miller/run-check` | `POST` | Выполнить проверку (Persuasion/Deception и т.п.) с расчётом исхода |
| `/narrative/dialogues/sara-miller/telemetry` | `POST` | Отправить телеметрию выбора игрока |

GraphQL-тип `DialogueNode` (поле `dialogue(id: ID!)`) отдаёт структуру узлов, проверок и возможных исходов для фронтенда. При `world.event.city_emergency=true` API возвращает дополнительные ветки `emergency-command`.

---

## 6. Валидация и телеметрия

- Валидация флагов выполняется скриптом `validate-dialogue-flags.ps1`, сверяющим наличие флагов в `02-gameplay/social/reputation-formulas.md` и `02-gameplay/world/events/world-events-framework.md`.
- Телеметрия (`ncpd-dialogue-usage`) отслеживает процент успехов/провалов по проверкам; при <40% успехов автоматически формируется тикет балансировки.
- Каждое изменение диалога запускает snapshot тесты: `dialogue-runner` прогоняет сценарии `base`, `trusted`, `disciplinary`, `emergency` и сверяет ожидаемые флаги.

---

## 7. Награды и последствия

- **Репутация:** `rep.law.ncpd` ±15, влияет на доступ к юридическим бонусам; снижение `rep.gang.valentinos` при высоких успехах полиции.
- **Предметы:** `ncpd-smart-drone`, доступ к базе данных расследований.
- **Флаги:** `flag.ncpd.badge`, `flag.ncpd.infraction_points`, `flag.ncpd.suspension`.
- **World-state:** события `ncpd-patrol-dt12`, `ncpd-emergency-response`, кейсы `ncpd-cybercrime-taskforce`.

## 8. Связанные материалы

- `../npc-lore/important/sara-miller.md`
- `../quests/main/002-choose-path-dnd-nodes.md`
- `../quests/side/ncpd-patrol-chain.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-framework.md`

## 9. История изменений

- 2025-11-07 17:05 — Диалог расширен: добавлены экспортные структуры, REST/GraphQL контракт и валидация флагов. Статус `ready`.
- 2025-11-07 16:42 — создан базовый набор диалогов для Сары Миллер.

