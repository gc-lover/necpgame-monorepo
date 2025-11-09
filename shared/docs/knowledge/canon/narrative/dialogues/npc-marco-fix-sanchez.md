# Диалоги — Марко «Фикс» Санчес

**ID диалога:** `dialogue-npc-marco-fix-sanchez`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 17:32  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/marco-fix-sanchez.md`, `../quests/main/001-first-steps-dnd-nodes.md`, `../quests/main/002-choose-path-dnd-nodes.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 17:32
**api-readiness-notes:** «Диалог фикса Марко содержит полный набор состояний, YAML-экспорт, REST/GraphQL контракт и валидацию флагов репутации. Готов к API задачам.»

---

---

## 1. Контекст и цели

- **NPC:** Марко «Фикс» Санчес, независимый посредник в Watson.
- **Стадии:** знакомство новичка, сопровождение выбора фракции, повторные визиты, реакции на глобальные события.
- **Синопсис:** Марко ориентирует игрока в городе, предлагает контракты и проверяет готовность героя работать с разными фракциями, задавая тон «уличного наставника».

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| base | Первая встреча или нейтральное доверие | `rep.fixers.marco` от 0 до 39 | `rep.fixers.marco` |
| trusted | Высокое доверие и успешные контракты | `rep.fixers.marco ≥ 40` | `rep.fixers.marco` |
| hostile | Игрок провалил или продал Марко | `rep.fixers.marco ≤ -15` | `rep.fixers.marco`, `flag.marco.betrayal` |
| blackwall-alert | Город на взводе из-за событий Blackwall | `world.event.blackwall_breach == true` | `world.blackwall_breach` |

- **Репутация:** Используется шкала из `02-gameplay/social/reputation-formulas.md` (ветка Fixers).
- **Мировые события:** `world.blackwall_breach` синхронизирован с `02-gameplay/world/events/blackwall/blackwall-breach.md`.
- **Проверки D&D:** Узлы N-1, N-3 (социальные) и N-12 (классовые) из квестов 001 и 002.

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| base | «Привет, чомбата. Я Марко. Знаю, как пройти через этот бетонный лабиринт.» | default | `["Мне нужна работа", "Что тут происходит?", "Я просто смотрю"]` |
| trusted | «Хэй, легенда на подходе. Есть свежие контракты и пара инсайдов.» | `rep.fixers.marco ≥ 40` | `["Гони задания", "Расскажи новости", "Мне нужен перерыв"]` |
| hostile | «Ты обещал и слил. Говори быстро или проваливай.» | `rep.fixers.marco ≤ -15` | `["Я всё исправлю", "Мне нужны контакты", "Ухожу"]` |
| blackwall-alert | «Небо светится аугми, чомбата. Blackwall дрожит, заказы нервные.» | `world.blackwall_breach` | `["Дай срочные задания", "Что это значит?", "Лучше пережду"]` |

### 3.2. Узлы диалога

```
- node-id: intro
  label: Первое знакомство
  entry-condition: state == "base" and not flag.marco.met
  player-options:
    - option-id: intro-work
      text: "Мне нужна работа"
      requirements: []
      npc-response: "Есть учебный забег через рынок. Проверка Perception DC 10, чтобы не словить пулю."
      outcomes:
        success: { effect: "grant_quest", quest-id: "main-001", reputation: +5, set-flags: ["flag.marco.met"] }
        failure: { effect: "retry_allowed", reputation: 0, set-flags: ["flag.marco.met"] }
        critical-success: { effect: "bonus_loot", reputation: +7, set-flags: ["flag.marco.met"] }
        critical-failure: { effect: "apply_debuff", debuff: "perception_noise", duration: 120, reputation: -2, set-flags: ["flag.marco.met"] }
    - option-id: intro-info
      text: "Что тут происходит?"
      npc-response: "Watson кипит. Корпы воюют за доки, банды делят кварталы."
      outcomes: { default: { effect: "unlock_codex", codex-id: "watson-overview", set-flags: ["flag.marco.met"] } }

- node-id: choose-path
  label: Подбор фракции
  entry-condition: quest.main-001 == "completed" and state != "hostile"
  player-options:
    - option-id: corp-route
      text: "Хочу в корпорации"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
          modifiers: [{ source: "outfit.corporate", value: +2 }]
      npc-response: "Тогда скажи, что принесёшь из Arasaka."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "arasaka-entry", reputation: +8, set-flags: ["flag.marco.corp"] }
        failure: { effect: "fallback_offer", contract-id: "corp-runner-basic", reputation: +2 }
        critical-success: { effect: "bonus_reward", item: "corp-access-pass", reputation: +12 }
        critical-failure: { effect: "cooldown", duration: 3600, reputation: -5 }
    - option-id: gang-route
      text: "Улицы ближе."
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 17
          modifiers: [{ source: "tattoo.valentinos", value: +1 }]
      npc-response: "Тогда тебя ждут Valentinos. Докажи, что свой."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "valentinos-trial", reputation: +8, set-flags: ["flag.marco.gang"] }
        failure: { effect: "side_mission", contract-id: "gang-trust-test", reputation: 0 }
        critical-success: { effect: "rare_pointer", pointer-id: "valentinos-vault", reputation: +10 }
        critical-failure: { effect: "spawn_encounter", encounter-id: "valentinos-check", reputation: -6 }
    - option-id: freelance-route
      text: "Остаюсь независимым."
      requirements:
        - type: stat-check
          stat: ClassChoice
          dc: 0
          class-bonus: { netrunner: +2, techie: +2, solo: 0 }
      npc-response: "Независимость дорогая. Покажи, что потянешь."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "freelance-sprint", reputation: +6, set-flags: ["flag.marco.freelance"] }
        failure: { effect: "retry_allowed", cooldown: 600, reputation: -1 }

- node-id: betrayal
  label: Последствия предательства
  entry-condition: state == "hostile"
  player-options:
    - option-id: apologize
      text: "Я всё исправлю"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 20
      npc-response: "Сначала верни то, что украл, и приведи двоих свидетелей."
      outcomes:
        success: { effect: "restore_reputation", reputation: +10, clear-flags: ["flag.marco.betrayal"] }
        failure: { effect: "increase_hostility", reputation: -5 }
    - option-id: cash-out
      text: "Сколько стоит твое молчание?"
      requirements: []
      npc-response: "Пять тысяч эдди и контракт без гарантий."
      outcomes: { default: { effect: "apply_fee", currency: "eddies", amount: 5000, reputation: +2 } }

- node-id: blackwall
  label: Реакция на Blackwall
  entry-condition: world.blackwall_breach == true
  player-options:
    - option-id: urgent-job
      text: "Дай срочные задания"
      requirements:
        - type: stat-check
          stat: NetrunnerFocus
          dc: 22
      npc-response: "Net бешеный. Нужно отловить беглую ИИ-пиявку."
      outcomes:
        success: { effect: "unlock_event", event-id: "blackwall-containment", reputation: +9 }
        failure: { effect: "deny_offer", reputation: 0 }
    - option-id: info
      text: "Что это значит?"
      requirements: []
      npc-response: "Если Blackwall рвётся, корпам нужен любой, кто умеет держать сеть."
      outcomes: { default: { effect: "unlock_codex", codex-id: "blackwall-surge" } }
```

### 3.3. Ветвление по проверкам

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| intro.intro-work | Perception | 10 | `+1` за обучающий баф | Квест 001, +5 репутация | Повтор | Бонус лут | Дебафф «perception_noise» |
| choose-path.corp-route | Persuasion | 18 | `+2` корпоративный костюм, `+2` связи | Контракт Arasaka | Базовый оффер | Пропуск в корпоративный хаб | Кулдаун и -5 репутации |
| choose-path.gang-route | Intimidation | 17 | `+1` тату Valentinos | Контракт Valentinos | Мини-квест доверия | Наводка на сейф | Засада банды |
| choose-path.freelance-route | ClassChoice | 0 | `+2` Netrunner/Techie | Независимый контракт | Повтор через 10 мин | — | — |
| betrayal.apologize | Negotiation | 20 | — | Сброс hostility | Усиление hostility | — | — |
| blackwall.urgent-job | NetrunnerFocus | 22 | `+2` за активный Blackwall-щит | Ивент containment | Отклонение | — | — |

### 3.4. Реакции на события

- **Событие:** `world.blackwall_breach`
  - **Условие:** глобальный эвент активен, игрок завершил `main/042-black-barrier-heist`
  - **Реплика:** «Blackwall рычит, чомбата. Корпы мечутся, улицы пережидают.»
  - **Последствия:** `reputation.fixers +4`, открывается ветка `blackwall`

---

## 4. Экспорт данных

```yaml
conversation:
  id: dialogue-npc-marco-fix-sanchez
  entryNodes:
    - intro
  states:
    base:
      requirements: { rep.fixers.marco: "0-39" }
    trusted:
      requirements: { rep.fixers.marco: ">=40" }
    hostile:
      requirements: { rep.fixers.marco: "<=-15", flag.marco.betrayal: true }
    blackwall-alert:
      requirements: { world.event.blackwall_breach: true }
  nodes:
    intro:
      onEnter: dialogue.intro()
      options:
        - id: intro-work
          text: "Мне нужна работа"
          checks:
            - stat: Perception
              dc: 10
          outcomes:
            success:
              quest: main-001
              reputation:
                rep.fixers.marco: 5
              setFlags:
                - flag.marco.met
            failure:
              retry: 0
              setFlags:
                - flag.marco.met
    choose-path:
      onEnter: dialogue.choosePath()
      options:
        - id: corp-route
          text: "Хочу в корпорации"
          checks:
            - stat: Persuasion
              dc: 18
              modifiers:
                - source: outfit.corporate
                  value: 2
          success:
            contract: arasaka-entry
            reputation:
              rep.fixers.marco: 8
            setFlags:
              - flag.marco.corp
          failure:
            contract: corp-runner-basic
            reputation:
              rep.fixers.marco: 2
```

> YAML-файл (`api/v1/narrative/dialogues/npc-marco-fix-sanchez.yaml`) формируется скриптом `scripts/export-dialogues.ps1` и подхватывается narrative-service.

---

## 5. REST / GraphQL API

| Endpoint | Метод | Назначение |
| --- | --- | --- |
| `/narrative/dialogues/marco-fix-sanchez` | `GET` | Получить структуру диалога с учётом репутации и событий |
| `/narrative/dialogues/marco-fix-sanchez/state` | `POST` | Сохранить прогресс, обновить флаги (`flag.marco.*`, репутацию) |
| `/narrative/dialogues/marco-fix-sanchez/run-check` | `POST` | Выполнить проверку (Perception/Persuasion/Intimidation/NetrunnerFocus) |
| `/narrative/dialogues/marco-fix-sanchez/telemetry` | `POST` | Отправить статистику выборов и исходов узлов |

GraphQL-поле `dialogue(id: ID!)` возвращает тип `DialogueNode`. При `flag.marco.betrayal=true` включается ветка `betrayal`, при `world.event.blackwall_breach=true` — ветка `blackwall`.

---

## 6. Валидация и телеметрия

- Скрипт `validate-dialogue-flags.ps1` проверяет наличие флагов в `02-gameplay/social/reputation-formulas.md` и `02-gameplay/world/events/blackwall/blackwall-breach.md`.
- `dialogue-simulator.ps1` прогоняет сценарии `base`, `trusted`, `hostile`, `blackwall-alert`, проверяя выдачу контрактов и репутационные изменения.
- Метрика `fixer-dialogue-success-rate` отслеживает процент успешных проверок; если показатель падает ниже 50%, формируется тикет балансировки.

---

## 7. Награды и последствия

- **Репутация:** `rep.fixers.marco` ±10, вторичные изменения для `rep.corp.arasaka` и `rep.gang.valentinos` через выдаваемые контракты.
- **Предметы:** `corp-access-pass`, `street-pointer`, случайные инфочипы из таблицы T1.
- **Флаги:** `flag.marco.met`, `flag.marco.corp`, `flag.marco.gang`, `flag.marco.freelance`, `flag.marco.betrayal`.
- **World-state:** контракты приводят к событиям `world.contract.arasaka.intro` или `world.contract.valentinos.intro` в базе ветвлений.

## 8. Связанные материалы

- `../npc-lore/important/marco-fix-sanchez.md`
- `../quests/main/001-first-steps-dnd-nodes.md`
- `../quests/main/002-choose-path-dnd-nodes.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/blackwall/blackwall-breach.md`

## 9. История изменений

- 2025-11-07 17:32 — Диалог расширен (YAML-экспорт, REST/GraphQL контракт, валидация флагов), статус `ready` подтверждён.
- 2025-11-07 16:42 — создан базовый набор диалогов для Марко Санчеса.

