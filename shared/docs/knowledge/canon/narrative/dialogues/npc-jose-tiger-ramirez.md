# Диалоги — Хосе «Тигр» Рамирес

**ID диалога:** `dialogue-npc-jose-tiger-ramirez`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.2.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 21:08  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/jose-tiger-ramirez.md`, `../quests/main/002-choose-path-dnd-nodes.md`, `../quests/side/heywood-valentinos-chain.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 21:08
**api-readiness-notes:** «Версия 1.2.0: расширенные состояния, пасхалки, проверки D&D, YAML/REST экспорт и телеметрия Valentinos/Heywood.»

---

---

## 1. Контекст и цели

- **Локация:** Heywood, баррио Vista del Rey. Базируется в кафе «La Última Nota» — бывшая студия, где ещё в 2020-х писали реггетон и записывали стримы против корпораций.
- **Тон:** смесь семейной теплоты и кровавой мести. Хосе балансирует между уважением к корням и хардкорным киберпанком — семейные традиции, mariachis с синт-струнами, AR-муралы.
- **Конфликт:** Valentinos воюют с Maelstrom за контроль над поставками имплантов, параллельно пытаясь не попасть под зачистки NCPD. Maelstrom пытается продавить район через Militech логистику.
- **Отсылки:** референсы к событиям GameStop 2021, кибертюнингу lowrider'ов, празднику Día de los Muertos (AR-офренда), инфлюенсерам TikTok 2075, мемам про Ever Given и транспортный кризис 2021.
- **Цели взаимодействия:** провести ритуал клятвы, выполнять семейные поручения, отрабатывать turf-war сценки, запускать double-cross сценарии, открывать побочные активности (AR-офренда, кавер-фиесты, street race).
- **Интеграции:** репутация Valentinos, квесты `heywood-valentinos-chain`, персонажи `npc-rita-moreno`, `npc-royce`, события `world.event.heywood_turf_war`, `world.event.metro_shutdown`, модуль `modules/social/informants`.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| base | Нейтральное отношение к новичку | `rep.gang.valentinos` 0–29 | `rep.gang.valentinos` |
| familia | Принятый член семьи | `rep.gang.valentinos ≥ 30` и `flag.valentinos.oath == true` | `rep.gang.valentinos`, `flag.valentinos.oath` |
| mistrust | Подозрение на связь с Maelstrom/NCPD | `flag.valentinos.maelstrom_contact == true` или `flag.valentinos.ncpd_informer == true` | соответствующие флаги |
| turf-war | Активная война за квартал | `world.event.heywood_turf_war == true` | `world.heywood_turf_war` |
| fiesta | Праздничное состояние (Día de los Muertos/семейный праздник) | `world.event.dia_de_los_muertos == true` | `world.event.dia_de_los_muertos` |
| memorial | Коммеморация павших (Maelstrom напал) | `flag.valentinos.memorial == true` | `flag.valentinos.memorial` |

- **Репутация:** бот Valentinos из `02-gameplay/social/reputation-formulas.md`.
- **Проверки D&D:** Используются узлы N-11 (gang check) и тактические проверки из `side/heywood-valentinos-chain.md`.
- **Мировые события:** `world.event.heywood_turf_war` определяется в `02-gameplay/world/events/world-events-framework.md` (локальная война).

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| base | «Эй, чомбата. Здесь Valentinos. Честь и семья или гуляй.» | default | `["Я хочу присоединиться", "Мне нужен проход", "Я просто мимо"]` |
| familia | «Брат/сестра! Дом всегда открыт. Что тревожит улицу?» | `rep.gang.valentinos ≥ 30` | `["Какие заказы?", "Как семья?", "Мне нужен отдых"]` |
| mistrust | «Слышал, ты шепчешь с Maelstrom. Объясняйся сейчас же.» | `flag.valentinos.maelstrom_contact` или `flag.valentinos.ncpd_informer` | `["Это план", "Это ложь", "Я ухожу"]` |
| turf-war | «Улицы горят. Maelstrom давит с завода. Нам нужны бойцы.» | `world.event.heywood_turf_war` | `["Дай цель", "Что произошло?", "Я вне игры"]` |

### 3.2. Узлы диалога

```
- node-id: oath
  label: Клятва Valentinos
  entry-condition: state == "base" and not flag.valentinos.oath
  player-options:
    - option-id: swear
      text: "Я хочу присоединиться"
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 17
          modifiers: [{ source: "tattoo.valentinos", value: +1 }]
      npc-response: "Честь наша дороже крови. Готов ли ты защищать улицу?"
      outcomes:
        success: { effect: "set_flag", flag: "flag.valentinos.oath", reputation: +10 }
        failure: { effect: "assign_trial", contract-id: "valentinos-trial", reputation: +2 }
        critical-success: { effect: "grant_token", item: "valentinos-medallion", reputation: +14 }
        critical-failure: { effect: "issue_test", encounter-id: "valentinos-gauntlet", reputation: -5 }
    - option-id: decline
      text: "Я просто мимо"
      requirements: []
      npc-response: "Тогда не задерживайся. Улицы слушают."
      outcomes: { default: { effect: "end_dialogue" } }

- node-id: familia-brief
  label: Советы семьи
  entry-condition: state == "familia"
  player-options:
    - option-id: request-mission
      text: "Какие заказы?"
      requirements:
        - type: stat-check
          stat: StreetSense
          dc: 18
      npc-response: "Есть долг перед доной Марией. Надо вытащить племянника."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "valentinos-family-rescue", reputation: +8 }
        failure: { effect: "assign_backup_task", contract-id: "valentinos-scout", reputation: +3 }
        critical-success: { effect: "grant_weapon", item: "smg-valentinos-custom", reputation: +12 }
        critical-failure: { effect: "family_disappointment", reputation: -6 }
    - option-id: ask-family
      text: "Как семья?"
      requirements: []
      npc-response: "Пока Maelstrom не лезет — всё спокойно. Но слухи дурные."
      outcomes: { default: { effect: "unlock_codex", codex-id: "valentinos-family-status" } }

- node-id: mistrust-interrogation
  label: Допрос о предательстве
  entry-condition: state == "mistrust"
  player-options:
    - option-id: explain-plan
      text: "Это план"
      requirements:
        - type: stat-check
          stat: Deception
          dc: 20
      npc-response: "Докажи, что играешь за Valentinos."
      outcomes:
        success: { effect: "clear_flags", flags: ["flag.valentinos.maelstrom_contact", "flag.valentinos.ncpd_informer"], reputation: +5 }
        failure: { effect: "impose_penalty", penalty: "tribute", reputation: -8 }
        critical-success: { effect: "assign_shadow_job", contract-id: "valentinos-double-blind", reputation: +7 }
        critical-failure: { effect: "banishment", flag: "flag.valentinos.exiled", reputation: -20 }
    - option-id: walk-away
      text: "Я ухожу"
      requirements: []
      npc-response: "Трус не семья. Если уйдёшь сейчас — дороги назад нет."
      outcomes: { default: { effect: "apply_flag", flag: "flag.valentinos.exiled", reputation: -15 } }

- node-id: turf-command
  label: Команда на войне
  entry-condition: world.heywood_turf_war == true
  player-options:
    - option-id: assign-target
      text: "Дай цель"
      requirements:
        - type: stat-check
          stat: CombatLeadership
          dc: 19
      npc-response: "Снимай турель Maelstrom на крыше завода."
      outcomes:
        success: { effect: "unlock_event", event-id: "valentinos-turf-counterstrike", reputation: +9 }
        failure: { effect: "support_role", reputation: +3 }
    - option-id: info
      text: "Что произошло?"
      requirements: []
      npc-response: "Maelstrom купили новых железяк. Они рвут наш баррио."
      outcomes: { default: { effect: "unlock_codex", codex-id: "heywood-turf-war" } }
```

### 3.3. Ветвление по проверкам

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| oath.swear | Intimidation | 17 | `+1` тату Valentinos | Клятва и +10 реп | Пробный квест | Медальон и +14 | Гаунтлет и -5 |
| familia-brief.request-mission | StreetSense | 18 | `+2` за активный флаг `flag.marco.gang` | Контракт спасения | Разведка | Кастом SMG | Разочарование семьи |
| mistrust-interrogation.explain-plan | Deception | 20 | `+2` за флаг `flag.marco.gang` | Снятие подозрений | Контрибуция | Скрытое задание | Изгнание |
| turf-command.assign-target | CombatLeadership | 19 | `+2` за активный отряд | Контратака | Резерв | — | — |

### 3.4. Реакции на события

- **Событие:** `world.event.heywood_turf_war`
  - **Условие:** активный конфликт между Valentinos и Maelstrom
  - **Реплика:** «Улицы Heywood — наша кровь. Сними железо Maelstrom и верни честь баррио.»
  - **Последствия:** открывается ветка `turf-command`, временный баф `valentinos_fury`.

## 4. Награды и последствия

- **Репутация:** `rep.gang.valentinos` ±20, косвенное влияние на `rep.gang.maelstrom` (снижается при успехах).
- **Предметы:** `valentinos-medallion`, `smg-valentinos-custom`, тактические чертежи.
- **Флаги:** `flag.valentinos.oath`, `flag.valentinos.maelstrom_contact`, `flag.valentinos.ncpd_informer`, `flag.valentinos.exiled`.
- **World-state:** события `valentinos-turf-counterstrike`, квестовые ветки `valentinos-family-rescue`.

## 5. Связанные материалы

- `../npc-lore/important/jose-tiger-ramirez.md`
- `../quests/main/002-choose-path-dnd-nodes.md`
- `../quests/side/heywood-valentinos-chain.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-framework.md`

## 6. История изменений

- 2025-11-07 21:08 — Версия 1.2.0: новые состояния (fiesta/memorial), пасхалки, активности, YAML/REST экспорт и телеметрия.
- 2025-11-07 16:42 — создан базовый набор диалогов для Хосе Рамиреса.

