# Кат-сцены — Helios Countermesh Conspiracy

**ID файла:** `cutscene-helios-conspiracy`  
**Тип:** narrative-cutscene  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 23:45  
**Приоритет:** высокий  
**Связанные документы:** `../quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`, `../../02-gameplay/world/helios-countermesh-ops.md`, `../../02-gameplay/world/specter-hq.md`  
**target-domain:** narrative  
**target-мicroservices:** narrative-service, world-service, social-service  
**target-frontend-модули:** modules/narrative/raids, modules/world  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 23:45
**api-readiness-notes:** Кат-сцены (Kaori/Dr. Lysander), диалоговые ветви и события готовы к реализации через narrative-service.

---

## 1. Кат-сцена `Blackwall Warning` (Specter ветка)
- **Триггер:** после выбора `expose-conspiracy` в Q2.
- **Описание:** Kaori и Specter команда собирают игроков, чтобы показать лог `Blackwall` и подготовить защиту.

```yaml
cutscene_id: CTS-HELIOS-001
title: "Blackwall Warning"
trigger:
  quest_flag: flag.player.specter_loyal
  stage: Q3
segments:
  - type: scene
    location: "Specter HQ Command Deck"
    characters: ["Kaori", "Player", "Specter Technician"]
    dialogue:
      - speaker: Kaori
        text: "У Helios есть fallback. Они хотят нарастить Blackwall и перекрыть Underlink."
      - speaker: Player
        options:
          - id: encourage
            text: "Готов работать на опережение."
            effect: set_mood: "determined"
          - id: doubt
            text: "Это может стоить нам сети."
            effect: add_city_unrest: 2
  - type: holo_feed
    content: "[{timestamp}] Helios Exec Lysander: initiate countermesh fallback"
  - type: briefing
    objectives:
      - "Защитить насосы Underlink"
      - "Собрать Blackwall ключи"
    rewards:
      - specter_prestige: 10
    events_emit:
      - SPECTER_BLACKWALL_WARNING
```

## 2. Кат-сцена `Helios Allegiance` (Helios ветка)
- **Триггер:** выбор `support-helios` в Q2.
- **Описание:** Dr. Lysander лично приветствует игрока на борту Helios флагмана, предлагая доступ к секретам.

```yaml
cutscene_id: CTS-HELIOS-002
title: "Helios Allegiance"
trigger:
  quest_flag: flag.player.helios_support
  stage: Q3
segments:
  - type: arrival
    location: "Helios Flagship — Obsidian Deck"
    music: "Helios_Oath"
  - type: scene
    characters: ["Dr. Lysander", "Player", "Helios Aide"]
    dialogue:
      - speaker: Lysander
        text: "Тебя впечатлила сила Specter, но они держат тебя на коротком поводке."
      - speaker: Player
        options:
          - id: loyalty
            text: "Helios платит за честность."
            effect: add_rep: { rep.corp.helios: 4 }
          - id: leverage
            text: "Мне нужны гарантии."
            effect: add_item: "helios-security-pass"
    branch:
      - condition: option == leverage
        action: set_flag: flag.helios.double_agent
  - type: hologram
    content: "Helios Tactical Map — Underlink Targets"
  - type: pledge
    vow_text: "Я, [Player], обязуюсь освободить Underlink от Specter"
    effect: trigger_event: HELIOS_PLEDGE
```

## 3. Диалог Kaori (пост-квест)
- **Описание:** Kaori реагирует на выбор игрока.

```yaml
dialogue_id: DLG-KAORI-POST
states:
  - id: specter_loyal
    condition: flag.player.specter_loyal == true
    lines:
      - "Мы удержали Underlink, но Helios не остановится."
      - options:
          - text: "Готов к следующему шагу"
            effect: unlock_contract: "intel-blackwall"
          - text: "Мне нужно время"
            effect: add_city_unrest: -3
  - id: helios_support
    condition: flag.player.helios_support == true
    lines:
      - "Ты выбрал Helios. Я не буду мешать, но не смей разрушить то, что мы строили."
      - options:
          - text: "Я делаю то, что должно"
            effect: add_rep: { rep.corp.helios: 2 }
          - text: "Specter всё равно победят"
            effect: set_flag: flag.kaori.trust -= 1
```

## 4. Диалог Dr. Lysander (пост-квест)
- **Описание:** Lysander оценивает вклад игрока, есть ветка двойного агента.

```yaml
dialogue_id: DLG-LYSANDER-POST
states:
  - id: helios_victory
    condition: quest_outcome == "helios"
    lines:
      - "Specter HQ сокращает активы, как ты и предрекал."
      - options:
          - text: "Обнови Countermesh"
            effect: trigger_event: HELIOS_COUNTERMESH_UPGRADE
          - text: "Мне нужна доля"
            effect: grant_reward: { item: "helios-drone-chip", amount: 1 }
  - id: double_agent
    condition: flag.helios.double_agent == true
    lines:
      - "Ты играешь в опасную игру, но я восхищён."
      - options:
          - text: "Specter — прикрытие"
            effect: add_rep: { rep.corp.helios: 3 }
          - text: "Я за баланс"
            effect: add_rep: { rep.fixers.neon: 2 }
```

## 5. API события
- narrative-service: `POST /api/v1/narrative/cutscenes/play`, `POST /api/v1/narrative/dialogues/specter-kaori`, `POST /api/v1/narrative/dialogues/helios-lysander`.
- Events: `SPECTER_BLACKWALL_WARNING`, `HELIOS_PLEDGE`, `HELIOS_COUNTERMESH_UPGRADE`.

## 6. Телеметрия
- События: `cutscene_viewed`, `dialog_choice`, `pledge_taken`, `kaori_trust_delta`.
- Grafana: `helios-conspiracy-narrative`, `player-choice-distribution`.
- SLA: запуск кат-сцены ≤ 2 сек, переходы диалога ≤ 150 мс.

## 7. История изменений
- 2025-11-07 23:45 — Добавлены кат-сцены и диалоги Helios Conspiracy (Kaori и Dr. Lysander).

