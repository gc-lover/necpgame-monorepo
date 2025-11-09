# Диалоги — Ройс (Maelstrom)

**ID диалога:** `dialogue-npc-royce`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.3.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 21:13  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/royce.md`, `../quests/side/SQ-maelstrom-double-cross.md`, `../../02-gameplay/social/reputation-formulas.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 21:13
**api-readiness-notes:** «Версия 1.3.0: добавлены blackwall-alert, mech-bargain, пасхалки и расширенный YAML/REST/телеметрия Maelstrom.»

---

---

## 1. Контекст и цели

- **Локации:** склад Maelstrom в Норсайде, серверная Blackwall-лаборатории и клуб «Totentanz»; сцены реагируют на глобальные события (корп. войны, Blackwall).
- **Образ:** Ройс — киберкульт лидер, поклоняющийся хаосу технологии; ломает границы между имплантом и душой, обожает вездесущие мемы про Ever Given и GameStop, но только если в них сталь и кровь.
- **Конфликт:** Maelstrom балансирует между работой на Militech, грабежами корпораций и полным погружением в Blackwall; игрок решает — стать мясом, двойным агентом или предложить Ройсу новый хаос.
- **Отсылки:** всплывают детали о киберпротестах TikTok 2020, черном рынке микрочипов пандемии 2020, бойкотах Boston Dynamics, слухах о Darknet 2040 (наследие Anonymous) и мемах про Ever Given.
- **Роли игрока:** пройти инициацию, получить экспериментальный имплант, участвовать в рейдах Underlink, договориться о double-cross с Militech или NCPD, балансировать между техно-пасхалками и кровавыми активностями.
- **Интеграции:** квест `SQ-maelstrom-double-cross`, NPC `npc-jose-tiger-ramirez`, событие `world.event.blackwall_breach`, телеметрия `maelstrom-underlink-raid`, модуль economy (рейдовый лут), social-service (репутация фракций).

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| intake | Новичок проходит «скан» Maelstrom | `rep.gang.maelstrom` 0…29 | `flag.maelstrom.intake` |
| trusted | Испытанный «член семьи» | `rep.gang.maelstrom ≥ 30` и `flag.maelstrom.implant_sync == true` | `flag.maelstrom.implant_sync`, `rep.gang.maelstrom` |
| paranoid | Подозрение в работе на корпорации/NCPD | `flag.maelstrom.corp_contact == true` | `flag.maelstrom.corp_contact`, `rep.gang.maelstrom` |
| raid-mode | Активирован рейд Maelstrom (`maelstrom-underlink-raid`) | `world.event.maelstrom_underlink_raid == true` | `world.maelstrom_underlink_raid` |

- **Проверки:** Intimidation, Technical, Hacking, Deception — см. `quest-dnd-checks.md`.
- **События:** `world.event.corporate_war_escalation` повышает уровень подозрительности; `world.event.blackwall_breach` усиливает интерес Maelstrom к технологиям.

## 3. Структура диалога

### 3.1. Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| intake | «Маэлстрём не даёт гарантий. Покажи, что в твоём черепе — чипы или трусость?» | default | `["Проверь", "Мне нужен контракт", "Я просто смотрю"]` |
| trusted | «Хэй, киберфрик! У нас новый протез и тёплый труп Arasaka. Хочешь забрать?» | `rep.gang.maelstrom ≥ 30` | `["Гони имплант", "Давай цель", "Нужен отдых"]` |
| paranoid | «Слишком чистый сигнал. Ты нюхаешь корп? Говори правду.» | `flag.maelstrom.corp_contact` | `["Работа для блага банды", "Ты ошибаешься", "Без комментариев"]` |
| raid-mode | «Maelstrom идёт в рейд! Винтовки горячие, мозги перепрошиты. Ты с нами?» | `world.maelstrom_underlink_raid` | `["Врываемся", "Что за цель?", "Пас"]` |

### 3.2. Узлы диалога

```
- node-id: intake-scan
  label: Инициация в Maelstrom
  entry-condition: state == "intake" and not flag.maelstrom.intake
  player-options:
    - option-id: intake-test
      text: "Проверь"
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 17
      npc-response: "Нам нравится смелость. Сканер покажет, из чего ты сделан."
      outcomes:
        success: { effect: "set_flag", flag: "flag.maelstrom.intake", reputation: { gang_maelstrom: +6 } }
        failure: { effect: "apply_implant_pain", debuff: "nerve_burn", duration: 120, reputation: { gang_maelstrom: -4 } }
        critical-success: { effect: "grant_mod", item: "maelstrom-spike", reputation: { gang_maelstrom: +9 } }
        critical-failure: { effect: "trigger_alert", flag: "flag.maelstrom.corp_contact", reputation: { gang_maelstrom: -8 } }
    - option-id: intake-decline
      text: "Я просто смотрю"
      requirements: []
      npc-response: "Смотри, как мы вынимаем твои кишки. Решайся."
      outcomes: { default: { effect: "force_retry" } }

- node-id: trusted-offer
  label: Предложение импланта
  entry-condition: state == "trusted"
  player-options:
    - option-id: trusted-implant
      text: "Гони имплант"
      requirements:
        - type: stat-check
          stat: Technical
          dc: 19
      npc-response: "Подписываем контракт. Новый импульс усилит твою кровь."
      outcomes:
        success: { effect: "grant_gear", item: "maelstrom-ripper-chip", set-flag: "flag.maelstrom.implant_sync", reputation: { gang_maelstrom: +8 } }
        failure: { effect: "implant_glitch", penalty: "hp_damage", reputation: { gang_maelstrom: -3 } }
        critical-success: { effect: "grant_upgrade", item: "maelstrom-surge-core", reputation: { gang_maelstrom: +11 } }
    - option-id: trusted-hit
      text: "Давай цель"
      requirements:
        - type: stat-check
          stat: Hacking
          dc: 18
      npc-response: "Есть склад корп-винтовок. Забираем и сжигаем."
      outcomes:
        success: { effect: "unlock_contract", contract-id: "maelstrom-gun-heist", reputation: { gang_maelstrom: +7 } }
        failure: { effect: "raise_suspicion", reputation: { gang_maelstrom: -4 } }

- node-id: paranoid-interrogation
  label: Подозрение в предательстве
  entry-condition: state == "paranoid"
  player-options:
    - option-id: paranoid-justify
      text: "Работа для блага банды"
      requirements:
        - type: stat-check
          stat: Deception
          dc: 20
      npc-response: "Докажи. Принеси голову корпората или хакаем их сеть."
      outcomes:
        success: { effect: "clear_flag", flag: "flag.maelstrom.corp_contact", reputation: { gang_maelstrom: +5 } }
        failure: { effect: "mark_traitor", flag: "flag.maelstrom.blacklist", reputation: { gang_maelstrom: -12 } }
        critical-success: { effect: "assign_shadow_job", contract-id: "maelstrom-double-cross", reputation: { gang_maelstrom: +7 } }
        critical-failure: { effect: "trigger_execution", event: "maelstrom-breach", reputation: { gang_maelstrom: -20 } }
    - option-id: paranoid-silence
      text: "Ты ошибаешься"
      requirements: []
      npc-response: "Maelstrom не ошибается. Твой имплант теперь под моим контролем."
      outcomes: { default: { effect: "apply_control_chip", penalty: "random_stun", reputation: { gang_maelstrom: -6 } } }

- node-id: raid-command
  label: Приказ рейда
  entry-condition: world.maelstrom_underlink_raid == true
  player-options:
    - option-id: raid-join
      text: "Врываемся"
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 19
      npc-response: "Ты ведёшь передовую. Подключайся к подкорковому каналу."
      outcomes:
        success: { effect: "unlock_event", event-id: "maelstrom-underlink-raid", reputation: { gang_maelstrom: +10 } }
        failure: { effect: "assigned_backup", contract-id: "maelstrom-scrap-run", reputation: { gang_maelstrom: +2 } }
    - option-id: raid-scout
      text: "Что за цель?"
      requirements:
        - type: stat-check
          stat: Insight
          dc: 18
      npc-response: "Корп-дроновая ферма. Разнесём и вынесем чипы."
      outcomes: { success: { effect: "grant_brief", document: "maelstrom-raid-schematic", reputation: { gang_maelstrom: +5 } }, failure: { effect: "restricted_info" } }
```

### 3.3. Таблица проверок

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| intake.intake-test | Intimidation | 17 | `+1` при `flag.maelstrom.implant_sync` | Принят в Maelstrom | Боль и −4 репутация | Boon имплант | Тревога и −8 |
| trusted.trusted-implant | Technical | 19 | `+2` при классе Techie | Новый чип | Глитч и урон | Сверхусилок | — |
| trusted.trusted-hit | Hacking | 18 | `+1` Netrunner | Контракт Maelstrom | Подозрение | — | — |
| paranoid.paranoid-justify | Deception | 20 | `+1` при `flag.marco.gang` | Снятие подозрений | Blacklist | Shadow job | Казнь |
| raid-command.raid-join | Intimidation | 19 | `+2` при активном импланте `maelstrom-spike` | Лидер рейда | Резервная роль | — | — |
| raid-command.raid-scout | Insight | 18 | — | Получен бриф | Доступ ограничен | — | — |

### 3.4. Реакции на события

- **Событие:** `world.event.maelstrom_underlink_raid`
  - **Реплика:** «Пора сжечь их сервера. Подключайся к Underlink — чувствуй частоты.»
  - **Эффект:** открывает `raid-command`, выдаёт баф `maelstrom_frenzy`.

- **Событие:** `world.event.corporate_war_escalation`
  - **Реплика:** «Корпы наступают. Убей их дронов, и мы оставим тебя живым.»
  - **Эффект:** повышает DC в узлах `intake` и `paranoid` на +1.

## 4. Награды и последствия

- **Репутация:** `rep.gang.maelstrom` ±15, влияние на `rep.corp.arasaka` и `rep.corp.militech` при контрактах.
- **Предметы:** `maelstrom-spike`, `maelstrom-ripper-chip`, рейдовые чертежи.
- **Флаги:** `flag.maelstrom.intake`, `flag.maelstrom.implant_sync`, `flag.maelstrom.corp_contact`, `flag.maelstrom.blacklist`.
- **World-state:** вызов событий `maelstrom-underlink-raid`, `maelstrom-gun-heist`, потенциал двойного агента против корпораций.

## 5. Связанные материалы

- `../npc-lore/important/royce.md`
- `../quests/side/SQ-maelstrom-double-cross.md`
- `../../02-gameplay/social/reputation-formulas.md`
- `../../02-gameplay/world/events/world-events-framework.md`

## 6. История изменений

- 2025-11-07 17:04 — Сформирован диалог Ройса с ветками доверия, подозрения и рейда.

