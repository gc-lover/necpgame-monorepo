# Диалог — Каэдэ Исикава

**ID диалога:** `dialogue-npc-kaede-ishikawa`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.3.0  
**Дата создания:** 2025-11-08  
**Последнее обновление:** 2025-11-07 22:04  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/npc-kaede-ishikawa.md`, `../quests/raid/2025-11-07-quest-helios-countermesh-conspiracy.md`, `../../02-gameplay/world/specter-hq.md`, `../../02-gameplay/world/helios-countermesh-ops.md`  
**target-domain:** narrative  
**target-мicroservice:** narrative-service  
**target-frontend-module:** modules/narrative/raids  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 22:04
**api-readiness-notes:** Версия 1.3.0: ветки Specter/Helios/Balanced, пасхалки и активные события связаны с рейдом Helios Countermesh.

---

---

## 1. Контекст
- Каэдэ — двойной агент Helios/Specter, связующая фигура в рейдах `Helios Countermesh Conspiracy` и `Specter Surge`.
- Реагирует на флаги `flag.player.helios_support`, `flag.player.specter_loyal`, `flag.helios.network_compromise`.
- Диалог интегрирован с `city_unrest` и world-events (`HELIOS_SPECTER_PROXY_WAR`, `BLACKWALL_GLITCH_ALERT`).
- Пасхалки: упоминание «SolarWinds Redux», «NotPetya 2077», шифр из манги «Ghost in the Wires» и референсы к AR-драмам 2050-х.

## 2. Состояния и условия

| State | Описание | Условия | Основные переходы |
|-------|----------|---------|-------------------|
| `neutral` | Каэдэ скрывает двойную игру, тестирует игрока | Базовое | `request-intel` успех → `specter`; `prove-helios` с успехом → `helios` |
| `specter` | Активная поддержка Specter HQ, доступ к Underlink | `flag.kaede.loyalty == specter` | `specter-loyalty` финал → `family_crisis` |
| `helios` | Лояльность Helios, заманивает в CM операции | `flag.kaede.loyalty == helios` | Выполнить `CM-Viper` → `helios-agent` |
| `balanced` | Стремится к компромиссу, продвигает mediator route | `flag.kaede.loyalty == balanced` | `balanced-contract` выполнен → `underlink-mediator` |
| `helios-agent` | Каэдэ курирует Helios ячейку в Night City | `flag.kaede.prove_helios == true` | Провал гильдии → `family_crisis` |
| `underlink-mediator` | Каэдэ запускает совместные операции Specter/Helios | `flag.kaede.network_compromise == true` | Срыв миссии → `family_crisis` |
| `family_crisis` | Семья Каэдэ под угрозой из-за войны сетей | `flag.kaede.family-threatened == true` | Успешное спасение → `specter` или `balanced`

## 3. Диалоговые узлы (YAML структуры)

```yaml
conversation:
  id: dialogue-npc-kaede-ishikawa
  entryNodes:
    - neutral-entry
  states:
    neutral:
      requirements: {}
    specter:
      requirements:
        flag.kaede.loyalty: "specter"
    helios:
      requirements:
        flag.kaede.loyalty: "helios"
    balanced:
      requirements:
        flag.kaede.loyalty: "balanced"
    helios-agent:
      requirements:
        flag.kaede.prove_helios: true
    underlink-mediator:
      requirements:
        flag.kaede.network_compromise: true
    family_crisis:
      requirements:
        flag.kaede.family-threatened: true

  nodes:
    neutral-entry:
      state: neutral
      onEnter:
        npcLine: "Баланс — это иллюзия. Но без него всё сгорит."
      options:
        - id: ask-loyalty
          text: "Кому ты служишь, Каэдэ?"
          npcResponse: "Сегодня Specter нужны мои данные. Завтра — Helios."
          outcomes:
            default:
              setState: balanced
              setFlags:
                - flag.kaede.trust_tested
        - id: request-intel
          text: "Мне нужен доступ к Countermesh логам."
          checks:
            - stat: Hacking
              dc: 18
              modifiers:
                - source: gear.cyberdeck.mantis
                  value: 1
                - source: reference.notpetya_memories
                  value: 1
          outcomes:
            success:
              npcResponse: "Здесь ключи. Не дай Lysander увидеть. Наши Ghosts называют это SolarWinds Redux."
              setFlags:
                - flag.kaede.loyalty:specter
                - flag.kaede.logs_shared
              grantItems:
                - countermesh-log
            failure:
              npcResponse: "Ты не готов. Уровень шумов, как во времена NotPetya 2077."
              addCityUnrest: 3
              setFlags:
                - flag.kaede.skeptical

    specter-briefing:
      state: specter
      onEnter:
        npcLine: "Specter держит город. Подключаемся к Underlink."
      options:
        - id: trigger-contract
          text: "Запускай Specter intel-contract."
          outcomes:
            default:
              npcResponse: "Принято. Контракт борда получит обновление с позывным Ghost in the Wires."
              triggerEvents:
                - SPECTER_INTEL_CONTRACT
                - HELIOS_SPECTER_PROXY_WAR
        - id: ask-kaori
          text: "Как связаться с Каори?"
          outcomes:
            default:
              npcResponse: "Передам твой вызов. Она всё ещё хранит AR-дневники, как в сериалe Cyberwar Chronicles."
              unlockCodex: kaori-signal-notes

    helios-oath:
      state: helios
      onEnter:
        npcLine: "Helios знает, что ты здесь. Покажи, что не предашь."
      options:
        - id: prove-helios
          text: "Как доказать лояльность Helios?"
          checks:
            - stat: Persuasion
              dc: 20
              modifiers:
                - source: rep.corp.helios
                  value: 2
          outcomes:
            success:
              npcResponse: "Выполни CM-Viper и вернись. Тогда спущу тебе коды от Blackwall ворот."
              setFlags:
                - flag.kaede.prove_helios
                - flag.kaede.loyalty:helios
            failure:
              npcResponse: "Helios не любит слабых. Пока остаёшься в тени."
              addCityUnrest: 2
        - id: leak-specter
          text: "Что Specter планируют?"
          requirements:
            - flag.player.double_agent: true
          outcomes:
            default:
              npcResponse: "Они готовят Underlink Mediator. Шум разнесётся по Net, как GameStop мемы 2021."
              unlockCodex: specter_mediator_brief

    balanced-dialogue:
      state: balanced
      onEnter:
        npcLine: "У Specter и Helios нет победителей. Только люди, которые тонут."
      options:
        - id: propose-balance
          text: "Предложить совместную операцию."
          checks:
            - stat: Insight
              dc: 19
              modifiers:
                - source: rep.specter
                  value: 1
                - source: rep.corp.helios
                  value: 1
          outcomes:
            success:
              npcResponse: "Создадим Underlink mediator. Придётся делиться частотами."
              setFlags:
                - flag.kaede.loyalty:balanced
                - flag.kaede.network_compromise
              triggerEvents:
                - BALANCED_CONTRACT_AVAILABLE
            failure:
              npcResponse: "Компромисс невозможен, пока город горит."
              addCityUnrest: 5
        - id: request-family-status
          text: "Как твоя семья?"
          outcomes:
            default:
              npcResponse: "Брат застрял в доках. Helios обрубили каналы."
              setFlags:
                - flag.kaede.family-threatened

    helios-agent-brief:
      state: helios-agent
      onEnter:
        npcLine: "Мы у двери Blackwall. Дальше только Helios."
      options:
        - id: launch-cm-viper
          text: "Запустить CM-Viper."
          outcomes:
            default:
              npcResponse: "Команда ждёт. Принеси мне лог с подписью Lysander."
              triggerEvents:
                - HELIOS_COUNTERMESH_OP
              grantActivity: cm-viper-raid
        - id: hint-evergiven
          text: "Напомнить о fiasco Ever Given."
          outcomes:
            default:
              npcResponse: "Ты ещё вспомни про блокаду Панамы 2048. Не отвлекайся."
              addRep:
                rep.corp.helios: 2

    mediator-operations:
      state: underlink-mediator
      onEnter:
        npcLine: "Underlink mediator активен. Балансируем фракции."
      options:
        - id: schedule-mediator-run
          text: "Назначить медиаторский забег."
          outcomes:
            default:
              npcResponse: "Передам координаты. Подготовься к AR-помехам, как в Netflix: Cyberwar Season 5."
              unlockActivity: underlink-mediator-run
        - id: query-war-meter
          text: "Как идёт война Specter vs Helios?"
          outcomes:
            default:
              npcResponse: "Прокси-война качнулась в сторону Helios. Memes уже на NightHub."
              provideMetrics:
                - war_ratio: 0.62

    family-crisis-node:
      state: family_crisis
      onEnter:
        npcLine: "Мой брат в Underlink. Helios закрыл шлюзы. Помощь нужна сейчас."
      options:
        - id: accept-rescue
          text: "Specter помогут."
          checks:
            - stat: Leadership
              dc: 21
              modifiers:
                - source: rep.city.gov
                  value: 1
          outcomes:
            success:
              npcResponse: "Я в долгу. Передам Каори твою метку."
              triggerEvents:
                - FAMILY_RESCUE_MISSION
              addRep:
                rep.fixers.neon: 3
              setFlags:
                - flag.kaede.family-saved
                - flag.kaede.loyalty:specter
            failure:
              npcResponse: "Опоздали. Подполье говорит, что Blackwall проглотил ещё одну мечту."
              addCityUnrest: 10
              setFlags:
                - flag.kaede.betrayal
        - id: refuse-help
          text: "Это твоя проблема."
          outcomes:
            default:
              npcResponse: "Тогда ты такой же, как Lysander. Helios запомнит."
              setFlags:
                - flag.kaede.loyalty:helios
              addRep:
                rep.corp.helios: 4
```

## 4. Таблица проверок D&D

| Узел | Проверка | DC | Модификаторы | Успех | Провал | Критический успех | Критический провал |
|------|----------|----|--------------|-------|--------|--------------------|----------------------|
| `request-intel` | Hacking | 18 | +1 `cyberdeck.mantis`; +1 пасхалка `notpetya_memories` | Выдаёт ключи, `loyalty → specter` | +3 Unrest, Kaede сомневается | Unlock `ghost-protocol` activity | Helios IDS помечает игрока, `city_unrest +6` |
| `prove-helios` | Persuasion | 20 | +2 при `rep.corp.helios ≥ 20`; +1 `helios-signet` | `loyalty → helios`, доступ к CM-Viper | Unrest +2, остаётся neutral | Helios предоставляет `blackwall-signet` | Specter раскрывает предательство, `specter-rep -12` |
| `propose-balance` | Insight | 19 | +1 `rep.specter`, +1 `rep.corp.helios` | unlock mediator, `loyalty → balanced` | Unrest +5 | Открывает `underlink-mediator-run`, `city_unrest -5` | Вызван `HELIOS_SPECTER_PROXY_WAR` с усиленной сложностью |
| `accept-rescue` | Leadership | 21 | +1 `rep.city.gov`, +1 companion `Kaori` | Запуск FAMILY_RESCUE, `loyalty → specter` | Unrest +10, `Kaede betrayal` | Семья спасена + Specter prestige 12 | Helios ликвидирует семью, world-event «Memorial Blackout» |
| `leak-specter` | Deception | 19 | +1 `flag.player.double_agent` | Helios доверяет, выдаёт intel | Specter доверие -5 | unlock `double-agent-contract`, Helios rep +6 | Specter объявляет игрока предателем, world bounty |
| `hint-evergiven` | Streetwise | 17 | +1 `history.2040s` perk | Helios rep +2, пасхалка | Kaede раздражена, опция блокируется | unlock secret cargo quest «Ever Given 2.0» | Helios «CM-Blockade» усиливает рейды |

## 5. Реакции на события и world-state
- **Событие:** `HELIOS_CONSPIRACY_ALLY`
  - Условие: `flag.player.helios_support == true`
  - Реплика: «Helios верит в тебя, но Specter уже готовят ответ.»
  - Последствия: `set_state -> helios-agent`, `unlock_activity -> cm-viper-raid`.
- **Событие:** `SPECTER_CONSPIRACY_EXPOSED`
  - Условие: `flag.player.specter_loyal == true`
  - Реплика: «Specter прислали новый протокол. Вспоминают кейс Stuxnet, смеются.»
  - Последствия: `set_state -> specter`, снижение `city_unrest` на 5.
- **Событие:** `HELIOS_SPECTER_PROXY_WAR`
  - Условие: `war_meter ≥ 0.6`
  - Реплика: «NightHub стримит мемы про кибервойну, как в 2024. Мы должны гасить пожар.»
  - Последствия: дополнительные опции `balanced` → `underlink-mediator`.
- **Событие:** `FAMILY_RESCUE_MISSION_SUCCESS`
  - Реплика: «Мой брат цел. Я обязана Specter долгом в крови.»
  - Последствия: `loyalty → specter`, unlock `specter-family-cache` (лут).

## 6. Награды, пасхалки и активности
- `loyalty:specter`: открывает `specter-intel-contract`, `specter-family-cache`, Easter egg — AR интервью Kaori о «NotPetya 2077».
- `loyalty:helios`: выдаёт `helios-blackwall-signet`, доступ к `cm-viper-raid`, Easter egg — внутренний форум Helios обсуждает «SolarWinds Redux».
- `loyalty:balanced`: unlock `underlink-mediator-run`, репутация Specter/Helios +6, активирует мемный feed `GhostInTheWires2077`.
- Провал `family_crisis`: world event «Memorial Blackout» — городские AR-стены показывают имена жертв, осуждая Helios.
- Дополнительная активность: `cm-viper-raid`, `underlink-mediator-run`, `double-agent-contract`, `ghost-protocol`.

## 7. API и события
- narrative-service: `POST /api/v1/narrative/dialogues/kaede/start`, `PATCH /api/v1/narrative/dialogues/kaede/state`, `POST /api/v1/narrative/dialogues/kaede/event`.
- world-service: `POST /api/v1/world/kaede-loyalty/update`, `POST /api/v1/world/city-unrest/apply`.
- social-service: `POST /api/v1/social/feeds/kaede/broadcast` (NightHub, GhostInTheWires memes).
- economy-service: `POST /api/v1/economy/contracts/kaede-rewards`.
- Events: `SPECTER_INTEL_CONTRACT`, `BALANCED_CONTRACT_AVAILABLE`, `FAMILY_RESCUE_MISSION`, `HELIOS_COUNTERMESH_OP`, `HELIOS_SPECTER_PROXY_WAR`.

## 8. Telemetry и SLA
- Telemetry: `kaede_dialogue_choice`, `kaede_loyalty_shift`, `kaede_activity_launch`, `kaede_family_outcome`.
- KPIs: удержание игроков в `underlink-mediator-run` ≥ 12 минут, конверсия в `cm-viper-raid` ≥ 55%, баланс WarMeter (0.45-0.55) при активном `balanced` состоянии.
- SLA: `dialogue_state_update ≤ 180 мс`, `event_dispatch ≤ 220 мс`.
- Grafana dashboards: `kaede-loyalty-map`, `specter-helios-war-meter`, `city-unrest-kaede-impact`.

## 9. История изменений
- 2025-11-07 22:04 — Версия 1.3.0: оформлены многоступенчатые ветки, пасхалки, активности и события, связка с рейдом Helios.
- 2025-11-08 00:25 — Создан диалог Kaede Ishikawa с ветками Specter/Helios и кризисной миссией.

