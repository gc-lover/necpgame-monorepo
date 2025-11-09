# Квест — Спасти семью Каэдэ

**ID квеста:** `quest-kaede-family-rescue`  
**Тип:** side-mission (raid support)  
**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-08  
**Последнее обновление:** 2025-11-08 00:45  
**Приоритет:** высокий  
**Связанные документы:** `../../dialogues/npc-kaede-ishikawa.md`, `../raid/2025-11-07-quest-helios-countermesh-conspiracy.md`, `../../cutscenes/2025-11-07-helios-conspiracy-cutscenes.md`, `../../../02-gameplay/world/city-unrest-escalations.md`  
**target-domain:** narrative/raid-support  
**target-мicroservices:** narrative-service, world-service, economy-service, social-service  
**target-frontend-модули:** modules/narrative/raids, modules/world/events  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-08 00:45
**api-readiness-notes:** Квест спасения семьи Каэдэ с фазами, проверками и влиянием на сезонные события готов к постановке API задач.

---

## 1. Контекст
- Семья Каэдэ Исикавы застряла в доках Underlink после Helios блокады (см. `flag.kaede.family-threatened`).
- Квест активируется при активной ветке Specter или balanced в диалоге Каэдэ, либо при уровне `city.unrest ≥ 60`.
- Выполнение миссии влияет на сезонное событие `Memorial Blackout` и War Meter Specter/Helios.

## 2. Структура и фазы

| Фаза | Название | Описание | Условия запуска | Основные проверки |
|------|----------|----------|-----------------|-------------------|
| F1 | `Evacuation Prep` | Подготовить транспорт и маршруты | После принятия квеста, `city.unrest ≥ 40` | Logistics (DC 18), Streetwise (DC 17) |
| F2 | `Dock Extraction` | PvE/PvPvE бой в доках Underlink | Успех F1 | Combat (DC 21), Tech (DC 19) |
| F3 | `Blackwall Bypass` | Взлом Helios шлюзов | Требуется `countermesh-log` или `helios-signet` | Hacking (DC 22) |
| F4 | `Safehouse Transfer` | Перевести семью в Specter safehouse или Helios enclave | Выбор игрока | Negotiation (DC 20) |
| F5 | `Aftermath` | Отражение на сезонных событиях | Завершение F4 | Leadership (DC 18) для поведения толпы |

## 3. Узлы и варианты (YAML)
```yaml
quest:
  id: quest-kaede-family-rescue
  prerequisites:
    - dialogue_flag: flag.kaede.family-threatened == true
    - city_unrest: >= 40
  rewards:
    specter:
      specter_prestige: 15
      rep.fixers.neon: 4
    helios:
      rep.corp.helios: 5
      helios_cred: 2000
  stages:
    - id: F1-prep
      tasks:
        - gather_item: "specter-evac-kit"
        - optional:
            hack_terminal:
              dc: 18
              success_effects:
                - set_flag: flag.kaede.route_clear
              failure_effects:
                - add_city_unrest: 4
    - id: F2-dock-extraction
      encounter:
        type: PvEvP
        enemies: ["Helios Drone Squad", "Blackwall Remnants"]
      success:
        - trigger_cutscene: CTS-HELIOS-001
        - add_rep: { rep.city.gov: 3 }
      failure:
        - trigger_event: MEMORIAL_BLACKOUT
    - id: F3-blackwall-bypass
      required_items: ["countermesh-log"] | ["helios-blackwall-signet"]
      check:
        stat: Hacking
        dc: 22
        critical_success:
          - unlock_activity: "blackwall-glitch-hunt"
        failure:
          - add_city_unrest: 6
          - set_flag: flag.kaede.betrayal
    - id: F4-safehouse-transfer
      branch:
        - id: specter_safehouse
          requirements:
            - flag.player.specter_loyal == true
          effects:
            - set_destination: "Specter Safehouse Delta"
            - add_rep: { rep.fixers.neon: 4 }
            - add_city_unrest: -8
        - id: helios_enclave
          requirements:
            - flag.player.helios_support == true
          effects:
            - set_destination: "Helios Enclave Gamma"
            - add_rep: { rep.corp.helios: 5 }
            - add_city_unrest: +5
            - trigger_event: HELIOS_SPECTER_PROXY_WAR
        - id: neutral_otomo
          requirements:
            - flag.kaede.loyalty == "balanced"
          effects:
            - set_destination: "Otomo Neutral Zone"
            - add_city_unrest: -3
            - unlock_activity: "neutral_supply_run"
    - id: F5-aftermath
      outcome:
        specter:
          trigger_event: FAMILY_RESCUE_MISSION_SUCCESS
          narrative: "Kaede благодарит, готова к совместным операциям."
        helios:
          trigger_event: HELIOS_DOUBLE_AGENT_HUNT
          narrative: "Lysander обещает защитить семью, но требует новых данных."
        neutral:
          trigger_event: UNDERLINK_MEDIATOR_UPDATE
          narrative: "Город получает сигнал о попытке баланса."
```

## 4. D&D проверки

| Фаза | Проверка | DC | Модификаторы | Успех | Провал |
|------|----------|----|--------------|-------|--------|
| F1 Logistics | Logistics | 18 | +2 при `flag.kaede.route_clear` | Сокращение врагов в F2 | +2 Unrest |
| F2 Combat | Combat | 21 | +1 `specter-squad-ticket`; +1 Helios поддержка | Потерь нет, +Specter престиж | Потери Specter/Helios, Unrest +4 |
| F3 Bypass | Hacking | 22 | +1 `countermesh-log`; +1 пасхалка `ghost-in-the-wires` | Открывается секретный выход | Helios IDS, Unrest +6 |
| F4 Negotiation | Negotiation | 20 | +2 `rep.city.gov ≥ 40`; +1 `helios-blackwall-signet` | Успешная эвакуация без шума | Helios узнаёт, запускает охоту |
| F5 Leadership | Leadership | 18 | +1 `specter-prestige ≥ 120` | Толпа успокоена, Unrest -5 | Появляются протесты, Unrest +3 |

## 5. Связь с сезонными событиями
- **Memorial Blackout:** провал F2 или F3 активирует событие (AR-стены, рост Unrest +15). Успешное спасение отменяет событие и выдаёт `specter-family-cache`.
- **Helios Specter Proxy War:** выбор Helios в F4 усиливает War Meter и запускает дополнительные Helios Ops с повышенной наградой.
- **Winter Rush Season:** при проведении миссии в сезон зимы игроки получают бонус `winter_supply_crate`.
- **Сезонные рейтинги:** участники спасения получают очки в сезонном табло Guild Contract Board (`rescue_points`).

## 6. API и события
- narrative-service: `POST /api/v1/narrative/quests/kaede-family-rescue/start`, `PATCH /api/v1/narrative/quests/kaede-family-rescue/state`.
- world-service: `POST /api/v1/world/city-unrest/apply`, `POST /api/v1/world/war-meter/update`.
- economy-service: `POST /api/v1/economy/rewards/family-rescue`.
- Events: `FAMILY_RESCUE_MISSION_START`, `FAMILY_RESCUE_MISSION_SUCCESS`, `HELIOS_DOUBLE_AGENT_HUNT`, `MEMORIAL_BLACKOUT`, `WINTER_RUSH_BONUS`.

## 7. Telemetry и SLA
- Telemetry events: `family_rescue_phase`, `family_rescue_choice`, `family_rescue_outcome`.
- KPIs: успешность миссии ≥ 65%, средний Unrest после миссии ≤ 55, участие гильдий ≥ 3/неделю.
- Latency: `quest_state_update ≤ 200 мс`, `event_dispatch ≤ 220 мс`.
- Dashboards: `family-rescue-overview`, `specter-helios-war-meter`, `seasonal-rescue-participation`.

## 8. История изменений
- 2025-11-08 00:45 — Создан квест «Спасти семью Каэдэ», интегрированный с сезонными событиями и War Meter.


