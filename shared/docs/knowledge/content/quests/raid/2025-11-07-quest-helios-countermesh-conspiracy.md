# Квест — Helios Countermesh Conspiracy

**ID квеста:** `quest-helios-countermesh-conspiracy`  
**Тип:** raid-chain  
**Статус:** approved  
**Версия:** 1.3.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 22:00  
**Приоритет:** высокий  
**Связанные документы:** `../../../02-gameplay/world/helios-countermesh-ops.md`, `../../../02-gameplay/world/specter-hq.md`, `../../../02-gameplay/world/city-unrest-escalations.md`, `../raid/2025-11-07-raid-specter-surge.md`  
**target-domain:** narrative/raid  
**target-мicroservices:** narrative-service, world-service, combat-service, economy-service  
**target-frontend-модули:** modules/narrative/raids, modules/world, modules/social  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 22:00
**api-readiness-notes:** «Версия 1.3.0: полный диалог рейда с ветками Specter/Helios, пасхалками, YAML/REST и телеметрией.»

---

## 1. Контекст
- Helios запускает операцию `Countermesh Conspiracy`, стремясь перехватить контроль над Underlink через двойных агентов и Blackwall обратные каналы.
- Specter HQ пытается разоблачить заговор, сохранив сети Ghosts, удержать City Unrest и защитить независимые ячейки.
- Квест связывает рейд `Specter Surge`, операции Helios Countermesh, City Unrest escalation и NPC Kaede Ishikawa (двойной агент).
- Пасхалки: отсылки к NotPetya, SolarWinds, украинским 2022 сетевым атакам, AR-архивам Netflix «Cyberwar Chronicles», мем про «Ghost in the Wires».

| Фаза | Узел | Описание | Условия | Ключевые проверки/выборы |
|------|------|----------|---------|-------------------------|
| Phase 0 | `Signal Echo` | Kaori перехватывает аномалии Helios | После рейда Specter Surge | Insight (16) или Hacking (17) для диагностики |
| Phase 1 | `Deep Cover` | Инфильтрация Helios и сбор данных | `countermesh-alloy ×2` | Stealth (18), Hacking (20) |
| Phase 2 | `Split Allegiance` | Выбор: поддержать Helios или Specter | City Unrest ≥ 40 | Persuasion (21) или Intimidation (19) |
| Phase 3 | `Underlink Siege` | PvE/PvPvE битва; спасение насосов Underlink | Активирован `CM-Phalanx` | Tactics (22), Combat (d20 + моды) |
| Phase 4 | `Conspiracy Finale` | Финальная конфронтация с Dr. Lysander; решение Blackwall | Победа в Phase 3, активные флаги loyalty | Insight (20), Deception (21), «Blackwall Protocol» выбор |

## 2. Структура квеста

| Узел | Название | Описание | Условия | Ключевые проверки |
|------|----------|----------|---------|--------------------|
| Q0 | `Signal Echo` | Kaori «Signal» перехватывает аномалии в сети Helios | Доступно после рейда Specter Surge | Insight (DC 16) — понять источник. |
| Q1 | `Deep Cover` | Инfilтрация гильдии Helios, сбор данных о Countermesh | Требует `countermesh-alloy` ×2 | Stealth (DC 18), Hacking (DC 20). |
| Q2 | `Split Allegiance` | Игрок делает выбор: поддержать Helios или раскрыть Specter HQ | City Unrest ≥ 40 | Persuasion (DC 21) или Intimidation (DC 19). |
| Q3 | `Underlink Siege` | PvE/PvPvE битва на насосах Underlink | Активирован `CM-Phalanx` | Combat (DC 22) + тактические решения. |
| Q4 | `Conspiracy Finale` | Финальная конфронтация с Helios стратегом `Dr. Lysander` | Победа в Q3 | Narrative выбор + рейдовая механика. |

## 3. Узлы и сцены (псевдо-YAML)

```yaml
- id: Q0-signal-echo
  type: investigative
  npc: "Kaori Ishikawa"
  location: "Specter HQ – Signal Room"
  dialogue:
    - "Helios пакеты идут через Underlink-Delta. Их сигнатуры напоминают колониальные атаки 2020-х."
  checks:
    - skill: Insight
      dc: 16
      modifiers:
        - source: gear.cyberdeck.rare
          value: 1
      success:
        effects:
          - set_flag: flag.helios.suspected
          - journal_update: "Helios использует обратные каналы Countermesh"
          - unlock_activity: specter-scan-protocol
      failure:
        effects:
          - set_flag: flag.helios.confused
          - add_city_unrest: 3
          - spawn_event: HELIOS_FAKE_PACKET

- id: Q1-deep-cover
  type: infiltration
  requirements:
    - inventory: countermesh-alloy >= 2
    - flag.helios.suspected == true
  objectives:
    - "Подменить ключи доступа Helios"
    - "Извлечь логи Countermesh"
  scenes:
    - scene: "Warehouse infiltration"
      checks:
        - skill: Stealth
          dc: 18
          modifiers:
            - source: augment.ghost-cloak
              value: 2
          success:
            effects:
              - set_flag: flag.helios.keys_obtained
              - add_rep: { rep.specter: 5 }
          failure:
            effects:
              - add_city_unrest: 8
              - trigger_event: HELIOS_ALERT
    - scene: "Countermesh console"
      checks:
        - skill: Hacking
          dc: 20
          modifiers:
            - source: companion.viktor.analysis
              value: 1
          success:
            effects:
              - set_flag: flag.helios.logs_extracted
              - add_loot: { item: countermesh-log, quantity: 1 }
          failure:
            effects:
              - disable_option: Q2.support-helios
              - spawn_event: HELIOS_PATCH_DEPLOYED

- id: Q2-split-allegiance
  type: branch
  conditions:
    - flag.helios.keys_obtained == true
    - flag.helios.logs_extracted == true
    - city.unrest >= 40
  options:
    - id: support-helios
      description: "Поддержать Helios и их план Countermesh"
      requirements:
        - skill: Persuasion
          dc: 21
          modifiers:
            - source: rep.corp.helios
              value: 2
      effects:
        - set_flag: flag.player.helios_support
        - add_rep:
            rep.corp.helios: 6
            rep.specter: -10
        - add_city_unrest: -5 (Helios обещает стабилизировать зоны)
        - trigger_event: HELIOS_CONSPIRACY_ALLY
    - id: expose-conspiracy
      description: "Раскрыть Specter HQ заговор Helios"
      requirements:
        - skill: Intimidation
          dc: 19
          modifiers:
            - source: rep.specter
              value: 1
      effects:
        - set_flag: flag.player.specter_loyal
        - add_rep:
            rep.specter: 8
            rep.city.gov: 3
        - add_city_unrest: -10
        - trigger_event: SPECTER_CONSPIRACY_EXPOSED
    - id: play-double-agent
      description: "Сыграть двойную игру, предоставив Specter ложные данные"
      requirements:
        - skill: Deception
          dc: 20
      effects:
        - set_flag: flag.player.double_agent
        - add_rep: { rep.corp.helios: 3, rep.specter: 3 }
        - add_city_unrest: +6
        - unlock_activity: double-agent-contract

- id: Q3-underlink-siege
  type: raid
  prerequisites:
    - flag.player.helios_support or flag.player.specter_loyal or flag.player.double_agent
  combat_phases:
    - "Defense of Pump Station Delta" (PvE waves)
    - "Capture the Helios Countermesh Node" (PvPvE)
  mechanics:
    - check: Tactics (dc: 22, +1 per 25 specter-prestige)
      success:
        - spawn_ally: specter-ghost-squad
        - reduce_enemy_hp: 15%
      failure:
        - add_debuff: network-disarray
    - check: Combat d20 + modifiers vs Helios drone leader (AC 18)
      success:
        - set_flag: flag.helios.node_secured
      failure:
        - add_city_unrest: +5
        - trigger_event: HELIOS_COUNTERSWARM

- id: Q4-conspiracy-finale
  type: narrative
  location: "Underlink Core Vault"
  npcs: ["Dr. Lysander Helios", "Kaori Ishikawa"]
  dialogue:
    - Lysander: "Мы спасаем город от хаоса. Specter — реликт прошлого."
    - Kaori: "Ложь. Вы подчиняете Underlink корпорации."
  choices:
    - id: release-underlink
      description: "Открыть Underlink всем сетевым сообществам"
      requirements:
        - skill: Insight
          dc: 20
          modifiers:
            - source: flag.player.specter_loyal
              value: 1
      effects:
        - set_flag: flag.helios.underlink_open
        - add_city_unrest: -15
        - grant_reward: { item: specter-legendary-implant }
        - trigger_cutscene: HELIOS_BARRIER_BREAKDOWN
    - id: corporate-control
      description: "Разрешить Helios контролировать сеть"
      requirements:
        - skill: Persuasion
          dc: 21
          modifiers:
            - source: flag.player.helios_support
              value: 2
      effects:
        - set_flag: flag.helios.network_lock
        - add_rep: { rep.corp.helios: 12 }
        - grant_reward: { item: helios-mythic-core }
        - add_city_unrest: +12
        - trigger_cutscene: HELIOS_BLACKWALL_ENFORCED
    - id: selective-release
      description: "Создать модуль доступов (Specter + Helios + City)"
      requirements:
        - skill: Negotiation
          dc: 21
      effects:
        - set_flag: flag.helios.network_compromise
        - add_rep: { rep.specter: 6, rep.corp.helios: 6 }
        - add_city_unrest: -5
        - unlock_activity: underlink-mediator
```

## 4. D&D проверки

| Фаза | Узел/Проверка | DC | Модификаторы | Успех | Провал | Критический успех | Критический провал |
|------|---------------|----|--------------|-------|--------|--------------------|----------------------|
| Phase 1 | Stealth (Deep Cover) | 18 | +2 `ghost-cloak`; +1 `companion.kaede` | Helios ключи, скрытый канал | Alarm, +8 Unrest | Helios логистический сундук | Raid перезапуск, Helios охотники |
| Phase 1 | Hacking (Deep Cover) | 20 | +1 `ghost-drone`; +1 `bd-sim` | Countermesh логи, доступ к Q2 Helios | Helios патч, -1 опция | Blackwall exploit к Q4 | Helios AI «Cerberus» появл. |
| Phase 2 | Persuasion (Support Helios) | 21 | +2 при `rep.corp.helios ≥ 20`; +1 эмиссар Helios | Helios доверяет, агент «Spectral» | Helios охотник в Q3 | Helios elite support в Q3 | Specter выявляет предателя |
| Phase 2 | Intimidation (Expose) | 19 | +1 при `rep.specter ≥ 30` | Specter лояльность, -10 Unrest | Helios контратака (extra wave) | Specter elite squad + Kaori buff | Helios закладывает Blackwall маяк |
| Phase 2 | Deception (Double Agent) | 20 | +1 implant «Mirror Veil» | обе фракции верят, unlock double-agent-contract | Specter доверие -8 | Доступ к Helios & Specter элитным магазинам | Обе стороны выясняют, massive war |
| Phase 3 | Tactics (Underlink Siege) | 22 | +1 per `25 specter-prestige`; +1 `drone-command` | Specter Ghost squad, boost morale | Specter HQ supply -1 | Specter deploys orbital recon | Specter HQ lockdown (shop закрыт 48h) |
| Phase 3 | Combat contested roll | AC 18/Helios drone leader | +mod gear/companions | Уничтожен лидер, Helios node secured | Лидер уходит, Helios fallback | Specter loot cache drop | Helios запускает Countermesh Phalanx (Ext raid) |
| Phase 4 | Insight (Release Underlink) | 20 | +2 `flag.specter_loyal`; +1 Kaori romance | Underlink открыт, -15 Unrest | Helios retaliates (new raid) | Specter legend slot unlock, world buff | Blackwall surge, city blackout |
| Phase 4 | Persuasion (Corporate Control) | 21 | +2 Helios support | Helios network lock, Helios Mythic Core | Specter revolt, +15 Unrest | Helios VIP contracts | City revolt, Helios assets attack |
| Phase 4 | Negotiation (Selective) | 21 | +1 при обоих флагах loyalty | Компромисс, mediator activity | Доверие обеих сторон падает | Underlink mediator unlock, Specter & Helios synergy | Black market кризис |

## 5. Награды и последствия
- **Helios ветка:** `helios-cred ×1200`, `helios-mythic-core`, доступ к контрактам Helios Countermesh (PvPvE), пасхалка — запись подкаста «Corporate Espionage Weekly» с намёком на реальный SolarWinds скандал.
- **Specter ветка:** `specter-prestige +40`, `specter-legendary-implant: underlink-sight`, снижение `city.unrest` на 15, unlock `ghost-net-defense` activity.
- **Double Agent ветка:** `underlink-mediator pass`, `credstick-obolisk`, временно оба магазина доступны, но City Unrest +6, появляется особый world event «Helios vs Specter Proxy War».
- **Глобальные последствия:** кат-сцена `Blackwall Barrier` (варианты в зависимости от выбора), апдейты world-state (`city_unrest_levels`, `specter_vs_helios_war`), обновление social-feed (мемы о «NotPetya 2077» и «Ghost in the Wire»).

## 6. API и события
- narrative-service: `POST /api/v1/narrative/quests/helios-conspiracy/start`, `PATCH /api/v1/narrative/quests/helios-conspiracy/state`.
- world-service: `POST /api/v1/world/helios-conspiracy/update`, `POST /api/v1/world/city-unrest/apply` (эскалации/снижения).
- economy-service: `POST /api/v1/economy/helios-conspiracy/rewards` (включая кредиты, предметы, unlock-пути).
- social-service: `POST /api/v1/social/feeds/helios-conspiracy/broadcast` (мемы, новости, рейтинги).
- Events: `HELIOS_CONSPIRACY_PROGRESS`, `HELIOS_CONSPIRACY_OUTCOME`, `HELIOS_SPECTER_PROXY_WAR`.

## 7. Telemetry и SLA
- Telemetry: `helios_conspiracy_choice`, `helios_conspiracy_phase`, `helios_conspiracy_reward`, `helios_vs_specter_war_state`.
- KPIs: доля игроков, прошедших Phase 3 ≥ 60%; снижение `city_unrest` ≥ 10 при Specter финале; удержание игроков в war-активностях ≥ 45 минут.
- Latency: `quest_state_update ≤ 200 мс`, `world_event_dispatch ≤ 250 мс`.
- Grafana: `helios-conspiracy-overview`, `specter-vs-helios-war`, `city-unrest-delta`.

## 8. Пасхалки и активности
- «NotPetya 2077» — AR новостной канал вспоминает глобальную атаку 2020-х, ставя её в параллель с Countermesh.
- «SolarWinds Redux» — Helios инженер шутит о старом плейбуке, вспоминая «SolarWinds» и «Stuxnet».
- Мини-активность: AR-охота за «Blackwall Glitches» в Underlink (разблокируется при Specter выборе).
- World activity: `Helios vs Specter Proxy War` — асинхронные PvP контракты за контроль зон.
- Социальные отсылки: мемный канал `GhostInTheWires2077` постит пародию на Kevin Mitnick, живая трансляция на «NightHub».

## 9. История изменений
- 2025-11-07 22:00 — Версия 1.3.0: расширены диалоговые сцены, добавлены ветки Specter/Helios/Double Agent, пасхалки и активности.
- 2025-11-07 23:25 — Создана сюжетная цепочка Helios Countermesh Conspiracy, описаны ветки, проверки и API.

