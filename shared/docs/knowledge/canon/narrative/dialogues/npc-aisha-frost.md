# Диалоги — Айша Фрост

**ID диалога:** `dialogue-npc-aisha-frost`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.2.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 18:20  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/aisha-frost.md`, `../quests/side/2025-11-07-quest-neon-ghosts.md`, `../../02-gameplay/social/reputation-formulas.md`  
**target-domain:** narrative  
**target-мicroservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/side-quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 18:20
**api-readiness-notes:** Диалог обновлён: добавлены Specter-отряды, баланс city unrest и новые последствия для экономики.

---

---

## 1. Контекст и цели
- **NPC:** Айша Фрост — координатор подпольной сети курьеров Neon Ghosts.
- **Роль:** проверяет мотивацию игрока, выдает задания, реагирует на мировые события Underlink и корпоративное давление.
- **Цели:** определить союзника Ghosts, посредника Helios Throne или двойного агента для Maelstrom; активировать элитные поручения Specter и Specter-отряды.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| scout | Первичный контакт | `rep.fixers.neon` < 20 | `rep.fixers.neon` |
| trusted | Полное доверие Ghosts | `rep.fixers.neon` ≥ 20 и `flag.neon.support == true` | `rep.fixers.neon`, `flag.neon.support` |
| corporate | Подозрение в работе с Helios Throne | `flag.helios.deal == true` | `flag.helios.deal`, `rep.corp.helios` |
| exposed | Раскрыт как агент Maelstrom | `flag.maelstrom.double_agent == true` | `flag.maelstrom.double_agent` |
| crisis | Underlink в состоянии Lockdown | `world.event.neon_lockdown == true` | `world.event.neon_lockdown` |
| specter | Элитный статус «Specter» | `flag.neon.elite == true` | `flag.neon.elite`, `rep.fixers.neon` |

- **Репутации:** `rep.fixers.neon`, `rep.corp.helios`, `rep.gang.maelstrom`.
- **Проверки:** узлы `intel-briefing`, `ghost-confrontation`, `resolution` и `specter-directive`.
- **События:** `world.event.neon_lockdown`, `world.event.maelstrom_underlink_raid`, `world.event.city_unrest`.

## 3. Структура диалога

### 3.1 Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| scout | «Неоновые призраки не доверяют незнакомцам. Сначала скажи, зачем ты здесь.» | default | `["Ищу маршрут", "Нужен контакт", "Ухожу"]` |
| trusted | «Ты уже тех, кому доверяют. Готов вести колонну?» | `rep.fixers.neon` ≥ 20, `flag.neon.support` | `["Покажи бриф", "Мне нужен аналитик", "Передышка"]` |
| corporate | «Helios платит хладнокровно. Покажи, что ты всё ещё играешь за нас.» | `flag.helios.deal == true` | `["Я посредник", "Это временно", "Сделка разорвана"]` |
| exposed | «Маельстром купил твоё молчание? Три секунды, чтобы объясниться.» | `flag.maelstrom.double_agent == true` | `["Это легенда", "Маельстром платит", "Молчание"]` |
| crisis | «Lockdown. Любой сбой — и Underlink вспыхнет ICE-пожаром.» | `world.event.neon_lockdown == true` | `["Эвакуируем", "Держим шлюз", "Отступаем"]` |
| specter | «Specter не паникуют. У нас новый маяк и растущий хаос на улицах.» | `flag.neon.elite == true` | `["Готов", "Нужен статус", "Specter уходит"]` |

### 3.2 Узлы (YAML)

```yaml
- node-id: scout-evaluation
  label: Проверка намерений
  entry-condition: state == "scout"
  player-options:
    - option-id: pay-route
      text: "Заплатить за маршрут"
      requirements:
        - type: resource
          resource: eddies
          amount: 4000
      npc-response: "Кредиты не врут. Слушай частоту и не шумей."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-brief" }
    - option-id: persuade-trust
      text: "Убедить доверять"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
      npc-response: "Если врёшь, ICE догонит быстрее, чем ты мигнёшь."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-brief", reputation: { "rep.fixers.neon": +4 } }
        failure: { effect: "reduce_trust", reputation: { "rep.fixers.neon": -3 }, cooldown: 900 }

- node-id: underlink-brief
  label: Брифинг по Underlink
  entry-condition: node == "underlink-brief"
  player-options:
    - option-id: accept-job
      text: "Принять доставку"
      requirements:
        - type: flag
          flag: "quest.neon_ghosts.active"
      npc-response: "Маршрут ушёл на твой имплант. Канал держи закрытым."
      outcomes:
        success: { effect: "grant_buff", buff: "underlink-ghost", duration: 600 }
    - option-id: request-support
      text: "Запросить поддержку"
      requirements:
        - type: stat-check
          stat: Strategy
          dc: 19
      npc-response: "Выделю дрона-наблюдателя. Его лог нужен чистым."
      outcomes:
        success: { effect: "spawn_companion", companion: "ghost-drone" }
        failure: { effect: "deny_support", reputation: { "rep.fixers.neon": -2 } }

- node-id: trusted-routing
  label: Планирование Ghosts
  entry-condition: state == "trusted"
  player-options:
    - option-id: schedule-drop
      text: "Назначить ночную доставку"
      requirements:
        - type: stat-check
          stat: Logistics
          dc: 20
      npc-response: "Колонна стартует через тридцать. Призрак ведёт передовой блок."
      outcomes:
        success: { effect: "trigger_event", event: "neon_ghosts_night_run", reputation: { "rep.fixers.neon": +6 } }
        failure: { effect: "event_delay", delay: 1800 }
    - option-id: exchange-data
      text: "Обменять украденные пакеты"
      requirements:
        - type: resource
          resource: data_shard
          amount: 1
      npc-response: "Эти данные заставят Helios нервничать."
      outcomes:
        success: { effect: "unlock_reward", reward: "ghost-cache", reputation: { "rep.fixers.neon": +4 } }

- node-id: corporate-ultimatum
  label: Корпоративное давление Helios
  entry-condition: state == "corporate"
  player-options:
    - option-id: mediate
      text: "Предложить сделку Helios"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 21
      npc-response: "Helios режет горло, если что-то идёт не так. Убедишь — выиграем время."
      outcomes:
        success: { effect: "convert_state", new-state: "scout", reputation: { "rep.corp.helios": +6, "rep.fixers.neon": +3 } }
        failure: { effect: "call_off", flag: "flag.helios.deal_failed", reputation: { "rep.corp.helios": -5 } }
    - option-id: resist
      text: "Сорвать поставку Helios"
      requirements:
        - type: stat-check
          stat: Resolve
          dc: 19
      npc-response: "Тогда горим мосты и бежим первыми."
      outcomes:
        success: { effect: "trigger_event", event: "neon_ghosts_resistance", reputation: { "rep.fixers.neon": +5 } }
        failure: { effect: "increase_alert", alert: "helios", amount: 2 }

- node-id: crisis-directives
  label: Приказы во время Lockdown
  entry-condition: state == "crisis"
  player-options:
    - option-id: evacuation
      text: "Эвакуировать курьеров"
      requirements:
        - type: stat-check
          stat: Leadership
          dc: 20
      npc-response: "Ведёшь колонну через северный шлюз. Ghosts держат канал."
      outcomes:
        success: { effect: "grant_modifier", modifier: "underlink.stability", value: +8 }
        failure: { effect: "casualty_report", penalty: { reputation: { "rep.fixers.neon": -8 } } }
    - option-id: hold-line
      text: "Удерживать транспортный шлюз"
      requirements:
        - type: stat-check
          stat: Combat
          dc: 21
      npc-response: "Пока ты держишь ICE-отряд, мы выгружаем пакет."
      outcomes:
        success: { effect: "spawn_encounter", encounter: "neon-ice-squad", reward: { item: "ghost-countermeasure" } }
        failure: { effect: "lockdown_extend", duration: 600 }

- node-id: specter-directive
  label: Элитные поручения Specter
  entry-condition: state == "specter"
  player-options:
    - option-id: ghost-trace
      text: "Отследить корпоративный маяк"
      requirements:
        - type: stat-check
          stat: Investigation
          dc: 22
      npc-response: "Specter видит то, что скрыто. Возьми маяк так, чтобы Helios не заметил потери."
      outcomes:
        success: { effect: "reveal_node", node: "corporate-ultimatum", reward: { item: "ghost-trace-module" } }
        failure: { effect: "raise_alert", alert: "helios", amount: 3 }
        critical-success: { effect: "unlock_reward", reward: "specter-cache", reputation: { "rep.fixers.neon": +8 } }
        critical-failure: { effect: "apply_flag", flag: "flag.neon.blacklist", reputation: { "rep.fixers.neon": -12 } }
    - option-id: stabilize-city
      text: "Сдержать городские беспорядки"
      requirements:
        - type: stat-check
          stat: Leadership
          dc: 21
      npc-response: "Specter держит улицы. Возьми северный ринг под контроль."
      outcomes:
        success: { effect: "grant_modifier", modifier: "city.unrest.level", value: -6 }
        failure: { effect: "city_unrest_spike", amount: +4 }
        critical-success: { effect: "trigger_event", event: "neon_ghosts_city_support", reputation: { "rep.fixers.neon": +10 } }
        critical-failure: { effect: "spawn_encounter", encounter: "riot-response", penalty: { reputation: { "rep.corp.helios": +5, "rep.fixers.neon": -10 } } }
    - option-id: deploy-specter-squad
      text: "Развернуть отряд призраков"
      requirements:
        - type: stat-check
          stat: Tactics
          dc: 23
        - type: resource
          resource: ghost_squad_ticket
          amount: 1
      npc-response: "Specter-отряды двигаются без шума. Координируй их как своих теней."
      outcomes:
        success: { effect: "spawn_companion", companion: "specter-squad", reputation: { "rep.fixers.neon": +9 } }
        failure: { effect: "squad_losses", penalty: { reputation: { "rep.fixers.neon": -6 } } }
        critical-success: { effect: "unlock_reward", reward: "specter-asset-cache", reputation: { "rep.fixers.neon": +12 } }
        critical-failure: { effect: "call_helios_counterops", alert: "helios", amount: 4 }
```

### 3.3 Таблица проверок D&D

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| scout-evaluation.persuade-trust | Persuasion | 18 | `+2` при `rep.fixers.neon ≥ 10` | Открывает брифинг, +4 репутации | -3 репутации, повтор через 900 c | Бафф `ghost-intel` | ICE-погоня, -8 репутации |
| underlink-brief.request-support | Strategy | 19 | `+1` за активного дрона | Компаньон-дрон | Отказ в поддержке | Бафф `system-map` | — |
| trusted-routing.schedule-drop | Logistics | 20 | `+1` при `flag.neon.support` | Событие `night_run`, +6 репутации | Задержка события | Доп. убежище `ghost-safehouse` | — |
| corporate-ultimatum.mediate | Negotiation | 21 | `+2` при `rep.corp.helios ≥ 25` | Снижает напряжение, +6 Helios | Провал сделки, -5 Helios | Протокол `helios-shadow` | Blacklist Ghosts |
| corporate-ultimatum.resist | Resolve | 19 | `+1` за мод `anti-ice` | Событие сопротивления | Рост тревоги Helios | Массовая поддержка Ghosts | Корпоративный рейд |
| crisis-directives.evacuation | Leadership | 20 | `+2` при `lockdown_progress < 50%` | +8 стабильности Underlink | Отчёт о потерях | Сокращение кризиса на 300 c | Потери, -12 репутации |
| crisis-directives.hold-line | Combat | 21 | `+1` за `ghost-intel` | Encounter с редким лутом | Продление локдауна | Лут `ghost-prototype` | — |
| specter-directive.ghost-trace | Investigation | 22 | `+2` при `flag.neon.elite` и `rep.fixers.neon ≥ 30` | Доступ к корпоративным узлам, модуль `ghost-trace` | Рост тревоги Helios | Награда `specter-cache` | Blacklist Ghosts |
| specter-directive.stabilize-city | Leadership | 21 | `+1` при `underlink.stability > 60` | Снижение беспорядков | Рост беспорядков | Событие `neon_ghosts_city_support` | Encounter riot-response |
| specter-directive.deploy-specter-squad | Tactics | 23 | `+1` при активном `specter-squad-ticket` | Элитная поддержка Ghosts | Потери отряда, -6 репутации | Награда `specter-asset-cache` | Контроперация Helios |

### 3.4 Реакции на события
- **Событие:** `world.event.neon_lockdown`
  - **Условие:** активен Lockdown, игрок поддерживает Ghosts.
  - **Реплика:** «У нас минуты, Specter. Выводи колонну.»
  - **Последствия:** доступ к узлу `crisis-directives`, успех повышает `underlink.stability`.
- **Событие:** `world.event.maelstrom_underlink_raid`
  - **Условие:** игрок сотрудничал с Maelstrom.
  - **Реплика:** «Маельстром жжёт наши туннели. Надеюсь, это стоило твоего доверия.»
  - **Последствия:** падение `rep.fixers.neon`, переход в состояние `exposed`.
- **Событие:** `contract.helios-shadow-ops`
  - **Условие:** заключено корпоративное соглашение.
  - **Реплика:** «Helios думает, что купил тебя. Докажи обратное.»
  - **Последствия:** доступ к ветке `corporate-ultimatum`.
- **Событие:** `world.event.city_unrest`
  - **Условие:** `city.unrest.level` > 50% и `flag.neon.elite` активен.
  - **Реплика:** «Улицы кипят. Specter, удержи районы, пока мы выводим грузы.»
  - **Последствия:** активируется `specter-directive`, успех снижает `city.unrest.level`.

## 4. Награды и последствия
- **Репутации:** `rep.fixers.neon` ±15, `rep.corp.helios` ±10, `rep.gang.maelstrom` ±12.
- **Флаги:** `flag.neon.support`, `flag.helios.deal`, `flag.maelstrom.double_agent`, `flag.neon.blacklist`, `flag.neon.elite`.
- **Предметы:** `ghost-drone`, `ghost-cache`, `ghost-countermeasure`, `ghost-prototype`, `ghost-trace-module`, `specter-cache`, `specter-asset-cache`.
- **World-state:** модификаторы `underlink.stability`, `city.unrest.level`, события `neon_ghosts_night_run`, `neon_ghosts_city_support`, `helios-crackdown`.
- **API:** `POST /api/v1/world/events`, `POST /api/v1/social/reputation/batch`, `POST /api/v1/economy/contracts/activate`.

## 5. Связанные материалы
- `../npc-lore/important/aisha-frost.md`
- `../quests/side/2025-11-07-quest-neon-ghosts.md`
- `../../02-gameplay/world/events/world-events-framework.md`
- `../../05-technical/global-state/global-state-management.md`
- `../../05-technical/ui/main-game/ui-system.md`

## 6. История изменений
- 2025-11-07 18:20 — Введён Specter-отряд и тактические проверки, обновлены последствия city unrest.
- 2025-11-07 18:05 — Добавлены уровень Specter, элитные поручения и реакция на городские беспорядки.
- 2025-11-07 17:55 — Первичная версия диалога, статусы ready.
# Диалоги — Айша Фрост

**ID диалога:** `dialogue-npc-aisha-frost`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.2.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 18:20  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/aisha-frost.md`, `../quests/side/2025-11-07-quest-neon-ghosts.md`, `../../02-gameplay/social/reputation-formulas.md`  
**target-domain:** narrative  
**target-мicroservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/side-quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 18:20
**api-readiness-notes:** Диалог обновлён: добавлены Specter-отряды, баланс city unrest и новые последствия для экономики.

---

## 1. Контекст и цели
- **NPC:** Айша Фрост — координатор подпольной сети курьеров Neon Ghosts.
- **Роль:** проверяет мотивацию игрока, выдает задания, реагирует на мировые события Underlink и корпоративное давление.
- **Цели:** определить союзника Ghosts, посредника Helios Throne или двойного агента для Maelstrom; активировать элитные поручения Specter.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| scout | Первичный контакт | `rep.fixers.neon` < 20 | `rep.fixers.neon` |
| trusted | Полное доверие Ghosts | `rep.fixers.neon` ≥ 20 и `flag.neon.support == true` | `rep.fixers.neon`, `flag.neon.support` |
| corporate | Подозрение в работе с Helios Throne | `flag.helios.deal == true` | `flag.helios.deal`, `rep.corp.helios` |
| exposed | Раскрыт как агент Maelstrom | `flag.maelstrom.double_agent == true` | `flag.maelstrom.double_agent` |
| crisis | Underlink в состоянии Lockdown | `world.event.neon_lockdown == true` | `world.event.neon_lockdown` |
| specter | Элитный статус «Specter» | `flag.neon.elite == true` | `flag.neon.elite`, `rep.fixers.neon` |

- **Репутации:** `rep.fixers.neon`, `rep.corp.helios`, `rep.gang.maelstrom`.
- **Проверки:** узлы `intel-briefing`, `ghost-confrontation`, `resolution` и новые поручения `specter-directive`.
- **События:** `world.event.neon_lockdown`, `world.event.maelstrom_underlink_raid`, `world.event.city_unrest`.

## 3. Структура диалога

### 3.1 Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| scout | «Неоновые призраки не доверяют незнакомцам. Сначала скажи, зачем ты здесь.» | default | `["Ищу маршрут", "Нужен контакт", "Ухожу"]` |
| trusted | «Ты уже тех, кому доверяют. Готов вести колонну?» | `rep.fixers.neon` ≥ 20, `flag.neon.support` | `["Покажи бриф", "Мне нужен аналитик", "Передышка"]` |
| corporate | «Helios платит хладнокровно. Покажи, что ты всё ещё играешь за нас.» | `flag.helios.deal == true` | `["Я посредник", "Это временно", "Сделка разорвана"]` |
| exposed | «Маельстром купил твоё молчание? Три секунды, чтобы объясниться.» | `flag.maelstrom.double_agent == true` | `["Это легенда", "Маельстром платит", "Молчание"]` |
| crisis | «Lockdown. Любой сбой — и Underlink вспыхнет ICE-пожаром.» | `world.event.neon_lockdown == true` | `["Эвакуируем", "Держим шлюз", "Отступаем"]` |
| specter | «Specter не падают в панику. У нас новый маяк и растущий хаос на улицах.» | `flag.neon.elite == true` | `["Готов", "Нужен статус", "Specter уходит"]` |

### 3.2 Узлы (YAML)

```yaml
- node-id: scout-evaluation
  label: Проверка намерений
  entry-condition: state == "scout"
  player-options:
    - option-id: pay-route
      text: "Заплатить за маршрут"
      requirements:
        - type: resource
          resource: eddies
          amount: 4000
      npc-response: "Кредиты не врут. Слушай частоту и не шумей."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-brief" }
    - option-id: persuade-trust
      text: "Убедить доверять"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
      npc-response: "Если врёшь, ICE догонит быстрее, чем ты мигнёшь."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-brief", reputation: { "rep.fixers.neon": +4 } }
        failure: { effect: "reduce_trust", reputation: { "rep.fixers.neon": -3 }, cooldown: 900 }

- node-id: underlink-brief
  label: Брифинг по Underlink
  entry-condition: node == "underlink-brief"
  player-options:
    - option-id: accept-job
      text: "Принять доставку"
      requirements:
        - type: flag
          flag: "quest.neon_ghosts.active"
      npc-response: "Маршрут ушёл на твой имплант. Канал держи закрытым."
      outcomes:
        success: { effect: "grant_buff", buff: "underlink-ghost", duration: 600 }
    - option-id: request-support
      text: "Запросить поддержку"
      requirements:
        - type: stat-check
          stat: Strategy
          dc: 19
      npc-response: "Выделю дрона-наблюдателя. Его лог нужен чистым."
      outcomes:
        success: { effect: "spawn_companion", companion: "ghost-drone" }
        failure: { effect: "deny_support", reputation: { "rep.fixers.neon": -2 } }

- node-id: trusted-routing
  label: Планирование Ghosts
  entry-condition: state == "trusted"
  player-options:
    - option-id: schedule-drop
      text: "Назначить ночную доставку"
      requirements:
        - type: stat-check
          stat: Logistics
          dc: 20
      npc-response: "Колонна стартует через тридцать. Призрак ведёт передовой блок."
      outcomes:
        success: { effect: "trigger_event", event: "neon_ghosts_night_run", reputation: { "rep.fixers.neon": +6 } }
        failure: { effect: "event_delay", delay: 1800 }
    - option-id: exchange-data
      text: "Обменять украденные пакеты"
      requirements:
        - type: resource
          resource: data_shard
          amount: 1
      npc-response: "Эти данные заставят Helios нервничать."
      outcomes:
        success: { effect: "unlock_reward", reward: "ghost-cache", reputation: { "rep.fixers.neon": +4 } }

- node-id: corporate-ultimatum
  label: Корпоративное давление Helios
  entry-condition: state == "corporate"
  player-options:
    - option-id: mediate
      text: "Предложить сделку Helios"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 21
      npc-response: "Helios режет горло, если что-то идёт не так. Убедишь — выиграем время."
      outcomes:
        success: { effect: "convert_state", new-state: "scout", reputation: { "rep.corp.helios": +6, "rep.fixers.neon": +3 } }
        failure: { effect: "call_off", flag: "flag.helios.deal_failed", reputation: { "rep.corp.helios": -5 } }
    - option-id: resist
      text: "Сорвать поставку Helios"
      requirements:
        - type: stat-check
          stat: Resolve
          dc: 19
      npc-response: "Тогда горим мосты и бежим первыми."
      outcomes:
        success: { effect: "trigger_event", event: "neon_ghosts_resistance", reputation: { "rep.fixers.neon": +5 } }
        failure: { effect: "increase_alert", alert: "helios", amount: 2 }

- node-id: crisis-directives
  label: Приказы во время Lockdown
  entry-condition: state == "crisis"
  player-options:
    - option-id: evacuation
      text: "Эвакуировать курьеров"
      requirements:
        - type: stat-check
          stat: Leadership
          dc: 20
      npc-response: "Ведёшь колонну через северный шлюз. Ghosts держат канал."
      outcomes:
        success: { effect: "grant_modifier", modifier: "underlink.stability", value: +8 }
        failure: { effect: "casualty_report", penalty: { reputation: { "rep.fixers.neon": -8 } } }
    - option-id: hold-line
      text: "Удерживать транспортный шлюз"
      requirements:
        - type: stat-check
          stat: Combat
          dc: 21
      npc-response: "Пока ты держишь ICE-отряд, мы выгружаем пакет."
      outcomes:
        success: { effect: "spawn_encounter", encounter: "neon-ice-squad", reward: { item: "ghost-countermeasure" } }
        failure: { effect: "lockdown_extend", duration: 600 }

- node-id: specter-directive
  label: Элитные поручения Specter
  entry-condition: state == "specter"
  player-options:
    - option-id: ghost-trace
      text: "Отследить корпоративный маяк"
      requirements:
        - type: stat-check
          stat: Investigation
          dc: 22
      npc-response: "Specter видит то, что скрыто. Возьми маяк так, чтобы Helios не заметил потери."
      outcomes:
        success: { effect: "reveal_node", node: "corporate-ultimatum", reward: { item: "ghost-trace-module" } }
        failure: { effect: "raise_alert", alert: "helios", amount: 3 }
        critical-success: { effect: "unlock_reward", reward: "specter-cache", reputation: { "rep.fixers.neon": +8 } }
        critical-failure: { effect: "apply_flag", flag: "flag.neon.blacklist", reputation: { "rep.fixers.neon": -12 } }
    - option-id: stabilize-city
      text: "Сдержать городские беспорядки"
      requirements:
        - type: stat-check
          stat: Leadership
          dc: 21
      npc-response: "Specter держит улицы. Возьми северный ринг под контроль."
      outcomes:
        success: { effect: "grant_modifier", modifier: "city.unrest.level", value: -6 }
        failure: { effect: "city_unrest_spike", amount: +4 }
        critical-success: { effect: "trigger_event", event: "neon_ghosts_city_support", reputation: { "rep.fixers.neon": +10 } }
        critical-failure: { effect: "spawn_encounter", encounter: "riot-response", penalty: { reputation: { "rep.corp.helios": +5, "rep.fixers.neon": -10 } } }
    - option-id: deploy-specter-squad
      text: "Развернуть отряд призраков"
      requirements:
        - type: stat-check
          stat: Tactics
          dc: 23
        - type: resource
          resource: ghost_squad_ticket
          amount: 1
      npc-response: "Specter-отряды двигаются без шума. Координируй их как своих теней."
      outcomes:
        success: { effect: "spawn_companion", companion: "specter-squad", reputation: { "rep.fixers.neon": +9 } }
        failure: { effect: "squad_losses", penalty: { reputation: { "rep.fixers.neon": -6 } } }
        critical-success: { effect: "unlock_reward", reward: "specter-asset-cache", reputation: { "rep.fixers.neon": +12 } }
        critical-failure: { effect: "call_helios_counterops", alert: "helios", amount: 4 }
```

### 3.3 Таблица проверок D&D

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| scout-evaluation.persuade-trust | Persuasion | 18 | `+2` при `rep.fixers.neon ≥ 10` | Открывает брифинг, +4 репутации | -3 репутации, повтор через 900 c | Бафф `ghost-intel` | ICE-погоня, -8 репутации |
| underlink-brief.request-support | Strategy | 19 | `+1` за активного дрона | Компаньон-дрон | Отказ в поддержке | Бафф `system-map` | — |
| trusted-routing.schedule-drop | Logistics | 20 | `+1` при `flag.neon.support` | Событие `night_run`, +6 репутации | Задержка события | Доп. убежище `ghost-safehouse` | — |
| corporate-ultimatum.mediate | Negotiation | 21 | `+2` при `rep.corp.helios ≥ 25` | Снижает напряжение, +6 Helios | Провал сделки, -5 Helios | Протокол `helios-shadow` | Blacklist Ghosts |
| corporate-ultimatum.resist | Resolve | 19 | `+1` за мод `anti-ice` | Событие сопротивления | Рост тревоги Helios | Массовая поддержка Ghosts | Корпоративный рейд |
| crisis-directives.evacuation | Leadership | 20 | `+2` при `lockdown_progress < 50%` | +8 стабильности Underlink | Отчёт о потерях | Сокращение кризиса на 300 c | Потери, -12 репутации |
| crisis-directives.hold-line | Combat | 21 | `+1` за `ghost-intel` | Encounter с редким лутом | Продление локдауна | Лут `ghost-prototype` | — |
| specter-directive.ghost-trace | Investigation | 22 | `+2` при `flag.neon.elite` и `rep.fixers.neon ≥ 30` | Доступ к корпоративным узлам, модуль `ghost-trace` | Рост тревоги Helios | Награда `specter-cache` | Blacklist Ghosts |
| specter-directive.stabilize-city | Leadership | 21 | `+1` при `underlink.stability > 60` | Снижение беспорядков | Рост беспорядков | Событие `neon_ghosts_city_support` | Encounter riot-response |
| specter-directive.deploy-specter-squad | Tactics | 23 | `+1` при активном `specter-squad-ticket` | Вызывает элитных союзников Ghosts | Потери отряда, -6 репутации | Награда `specter-asset-cache` | Контроперация Helios |

### 3.4 Реакции на события
- **Событие:** `world.event.neon_lockdown`
  - **Условие:** активен Lockdown, игрок поддерживает Ghosts.
  - **Реплика:** «У нас минуты, Specter. Выводи колонну.»
  - **Последствия:** доступ к узлу `crisis-directives`, успех повышает `underlink.stability`.
- **Событие:** `world.event.maelstrom_underlink_raid`
  - **Условие:** игрок сотрудничал с Maelstrom.
  - **Реплика:** «Маельстром жжёт наши туннели. Надеюсь, это стоило твоего доверия.»
  - **Последствия:** падение `rep.fixers.neon`, переход в состояние `exposed`.
- **Событие:** `contract.helios-shadow-ops`
  - **Условие:** заключено корпоративное соглашение.
  - **Реплика:** «Helios думает, что купил тебя. Докажи обратное.»
  - **Последствия:** доступ к ветке `corporate-ultimatum`.
- **Событие:** `world.event.city_unrest`
  - **Условие:** `city.unrest.level` > 50% и `flag.neon.elite` активен.
  - **Реплика:** «Улицы кипят. Specter, удержи районы, пока мы выводим грузы.»
  - **Последствия:** активируется `specter-directive`, успех снижает `city.unrest.level`.

## 4. Награды и последствия
- **Репутации:** `rep.fixers.neon` ±15, `rep.corp.helios` ±10, `rep.gang.maelstrom` ±12.
- **Флаги:** `flag.neon.support`, `flag.helios.deal`, `flag.maelstrom.double_agent`, `flag.neon.blacklist`, `flag.neon.elite`.
- **Предметы:** `ghost-drone`, `ghost-cache`, `ghost-countermeasure`, `ghost-prototype`, `ghost-trace-module`, `specter-cache`, `specter-asset-cache`.
- **World-state:** модификаторы `underlink.stability`, `city.unrest.level`, события `neon_ghosts_night_run`, `neon_ghosts_city_support`, `helios-crackdown`.
- **API:** `POST /api/v1/world/events`, `POST /api/v1/social/reputation/batch`, `POST /api/v1/economy/contracts/activate`.

## 5. Связанные материалы
- `../npc-lore/important/aisha-frost.md`
- `../quests/side/2025-11-07-quest-neon-ghosts.md`
- `../../02-gameplay/world/events/world-events-framework.md`
- `../../05-technical/global-state/global-state-management.md`
- `../../05-technical/ui/main-game/ui-system.md`

## 6. История изменений
- 2025-11-07 18:20 — Введён Specter-отряд и тактические проверки, обновлены последствия city unrest.
- 2025-11-07 18:05 — Добавлены уровень Specter, элитные поручения и реакция на городские беспорядки.
- 2025-11-07 17:55 — Первичная версия диалога, статусы ready.
# Диалоги — Айша Фрост

**ID диалога:** `dialogue-npc-aisha-frost`  
**Тип:** npc  
**Статус:** approved  
**Версия:** 1.1.0  
**Дата создания:** 2025-11-07  
**Последнее обновление:** 2025-11-07 18:05  
**Приоритет:** высокий  
**Связанные документы:** `../npc-lore/important/aisha-frost.md`, `../quests/side/2025-11-07-quest-neon-ghosts.md`, `../../02-gameplay/social/reputation-formulas.md`  
**target-domain:** narrative  
**target-microservice:** narrative-service (port 8087)  
**target-frontend-module:** modules/narrative/side-quests  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 18:05
**api-readiness-notes:** Диалог обновлён: добавлен уровень Specter, элитные поручения и реакция на городские беспорядки.

---

## 1. Контекст и цели
- **NPC:** Айша Фрост — координатор сети курьеров Neon Ghosts, оперирует из неоновой изнанки Underlink.
- **Роль:** устанавливает условия сотрудничества, реагирует на корпоративное давление, запускает события побочного квеста `Neon Ghosts`.
- **Цели:** определить, станет ли игрок союзником Ghosts, посредником Helios Throne или двойным агентом для Maelstrom.

## 2. Состояния и условия

| Состояние | Описание | Триггеры | Используемые флаги |
|-----------|----------|----------|---------------------|
| scout | Первичный контакт, Айша проверяет мотивы | `rep.fixers.neon` < 20 | `rep.fixers.neon` |
| trusted | Игрок доказал лояльность Ghosts | `rep.fixers.neon` ≥ 20 и `flag.neon.support == true` | `rep.fixers.neon`, `flag.neon.support` |
| corporate | Игрок сотрудничает с Helios Throne | `flag.helios.deal == true` | `flag.helios.deal`, `rep.corp.helios` |
| exposed | Айша узнала о связях с Maelstrom | `flag.maelstrom.double_agent == true` | `flag.maelstrom.double_agent` |
| crisis | Underlink в режиме блокировки | `world.event.neon_lockdown == true` | `world.event.neon_lockdown` |
| specter | Элитный статус «Specter» | `flag.neon.elite == true` | `flag.neon.elite`, `rep.fixers.neon` |

- **Репутации:** `rep.fixers.neon`, `rep.corp.helios`, `rep.gang.maelstrom`.
- **Связанные проверки:** узлы `intel-briefing`, `ghost-confrontation`, `resolution` из квеста `Neon Ghosts`.
- **Мировые события:** `world.event.neon_lockdown`, `world.event.maelstrom_underlink_raid`.

## 3. Структура диалога

### 3.1 Приветствия

| Состояние | Реплика NPC | Условия | Ответы игрока |
|-----------|-------------|---------|---------------|
| scout | «Ты новый контакт? Прежде чем доверять, я хочу знать, на чьей ты стороне.» | default | `["Мне нужны координаты", "Я пришёл с миром", "Уходя"]` |
| trusted | «Ghosts видят в тебе союзника. Готов к следующей поставке?» | `rep.fixers.neon` ≥ 20, `flag.neon.support` | `["Перевоз вечером", "Нужна аналитика", "Передохнуть"]` |
| corporate | «Helios купил твою тишину? Тогда обоснуй, почему я должна говорить с тобой.» | `flag.helios.deal == true` | `["Я посредник", "Это временно", "Контракт разорван"]` |
| exposed | «Маельстрому не место в наших туннелях. У тебя есть три секунды, чтобы объясниться.» | `flag.maelstrom.double_agent == true` | `["Это был блеф", "Маельстром платит лучше", "Молчать"]` |
| crisis | «Lockdown. Любое лишнее движение — и Underlink зальёт ICE пожаром.» | `world.event.neon_lockdown == true` | `["Покажи эвакуацию", "Мы удержим шлюз", "Ухожу"]` |

### 3.2 Узлы (YAML)

```yaml
- node-id: scout-evaluation
  label: Проверка намерений
  entry-condition: state == "scout"
  player-options:
    - option-id: pay-route
      text: "Заплатить за маршрут"
      requirements:
        - type: resource
          resource: eddies
          amount: 4000
      npc-response: "Кредиты не пахнут. Слушай внимательно."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-brief" }
    - option-id: persuade-trust
      text: "Убедить доверять"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 18
      npc-response: "Вижу в глазах искренность. Держи частоту."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-brief", reputation: { "rep.fixers.neon": +4 } }
        failure: { effect: "reduce_trust", reputation: { "rep.fixers.neon": -3 }, cooldown: 900 }

- node-id: underlink-brief
  label: Брифинг по Underlink
  entry-condition: node == "underlink-brief"
  player-options:
    - option-id: accept-job
      text: "Принять задание"
      requirements:
        - type: flag
          flag: "quest.neon_ghosts.active"
      npc-response: "Маршрут залит на твой имплант. Держи канал в тени."
      outcomes:
        success: { effect: "grant_buff", buff: "underlink-ghost", duration: 600 }
    - option-id: request-support
      text: "Запросить поддержку"
      requirements:
        - type: stat-check
          stat: Strategy
          dc: 19
      npc-response: "Могу выделить дрона-наблюдателя. С тебя чистый возврат."
      outcomes:
        success: { effect: "spawn_companion", companion: "ghost-drone" }
        failure: { effect: "deny_support", reputation: { "rep.fixers.neon": -2 } }

- node-id: trusted-routing
  label: Планирование операций
  entry-condition: state == "trusted"
  player-options:
    - option-id: schedule-drop
      text: "Назначить ночную доставку"
      requirements:
        - type: stat-check
          stat: Logistics
          dc: 20
      npc-response: "Окей. Выдвигаемся через тридцать. Призрак ведёт колонну."
      outcomes:
        success: { effect: "trigger_event", event: "neon_ghosts_night_run", reputation: { "rep.fixers.neon": +6 } }
        failure: { effect: "event_delay", delay: 1800 }
    - option-id: exchange-data
      text: "Обменять украденные пакеты"
      requirements:
        - type: resource
          resource: data_shard
          amount: 1
      npc-response: "Хороший улов. Эти данные взорвут корпоративные аналитики."
      outcomes:
        success: { effect: "unlock_reward", reward: "ghost-cache", reputation: { "rep.fixers.neon": +4 } }

- node-id: corporate-ultimatum
  label: Корпоративное давление
  entry-condition: state == "corporate"
  player-options:
    - option-id: mediate
      text: "Предложить сделку Helios"
      requirements:
        - type: stat-check
          stat: Negotiation
          dc: 21
      npc-response: "Helios обожгёт нас при первом же сбое. Давай цифры."
      outcomes:
        success: { effect: "convert_state", new-state: "scout", reputation: { "rep.corp.helios": +6, "rep.fixers.neon": +3 } }
        failure: { effect: "call_off", flag: "flag.helios.deal_failed", reputation: { "rep.corp.helios": -5 } }
    - option-id: resist
      text: "Сорвать соглашение"
      requirements:
        - type: stat-check
          stat: Resolve
          dc: 19
      npc-response: "Хорошо. Тогда ломаем их шлюзы сами."
      outcomes:
        success: { effect: "trigger_event", event: "neon_ghosts_resistance", reputation: { "rep.fixers.neon": +5 } }
        failure: { effect: "increase_alert", alert: "helios", amount: 2 }

- node-id: crisis-directives
  label: Приказы во время Lockdown
  entry-condition: state == "crisis"
  player-options:
    - option-id: evacuation
      text: "Организовать эвакуацию курьеров"
      requirements:
        - type: stat-check
          stat: Leadership
          dc: 20
      npc-response: "Ghosts слушают твой приказ. Ведёшь колонну до выхода Sigma."
      outcomes:
        success: { effect: "grant_modifier", modifier: "underlink.stability", value: +8 }
        failure: { effect: "casualty_report", penalty: { reputation: { "rep.fixers.neon": -8 } } }
    - option-id: hold-line
      text: "Удерживать транспортный шлюз"
      requirements:
        - type: stat-check
          stat: Combat
          dc: 21
      npc-response: "Значит, ты прикрываешь нас, пока мы выгружаем пакет."
      outcomes:
        success: { effect: "spawn_encounter", encounter: "neon-ice-squad", reward: { item: "ghost-countermeasure" } }
        failure: { effect: "lockdown_extend", duration: 600 }

- node-id: specter-directive
  label: Элитные поручения Specter
  entry-condition: state == "specter"
  player-options:
    - option-id: ghost-trace
      text: "Отследить корпоративный маяк"
      requirements:
        - type: stat-check
          stat: Investigation
          dc: 22
      npc-response: "Specter обязан видеть то, что скрыто. Действуй тихо и без свидетелей."
      outcomes:
        success: { effect: "reveal_node", node: "corporate-ultimatum", reward: { item: "ghost-trace-module" } }
        failure: { effect: "raise_alert", alert: "helios", amount: 3 }
        critical-success: { effect: "unlock_reward", reward: "specter-cache", reputation: { "rep.fixers.neon": +8 } }
        critical-failure: { effect: "apply_flag", flag: "flag.neon.blacklist", reputation: { "rep.fixers.neon": -12 } }
    - option-id: stabilize-city
      text: "Сдержать городские беспорядки"
      requirements:
        - type: stat-check
          stat: Leadership
          dc: 21
      npc-response: "Specter держит город под контролем. Возьми на себя кварталы северного ринга."
      outcomes:
        success: { effect: "grant_modifier", modifier: "city.unrest.level", value: -6 }
        failure: { effect: "city_unrest_spike", amount: +4 }
        critical-success: { effect: "trigger_event", event: "neon_ghosts_city_support", reputation: { "rep.fixers.neon": +10 } }
        critical-failure: { effect: "spawn_encounter", encounter: "riot-response", penalty: { reputation: { "rep.corp.helios": +5, "rep.fixers.neon": -10 } } }
```

### 3.3 Таблица проверок

| Узел | Тип проверки | DC | Модификаторы | Успех | Провал | Крит. успех | Крит. провал |
|------|--------------|----|--------------|-------|--------|-------------|--------------|
| scout-evaluation.persuade-trust | Persuasion | 18 | `+2` за `rep.fixers.neon ≥ 10` | доступ к брифингу, +4 репутации | -3 к репутации, перезапрос через 900 c | +6 репутации, бафф `ghost-intel` | -6 репутации, ICE-погоня |
| underlink-brief.request-support | Strategy | 19 | `+1` за активный дрон | Компаньон-дрон поддерживает миссию | Отказ в поддержке | Бафф `system-map` | — |
| trusted-routing.schedule-drop | Logistics | 20 | `+1` за `flag.neon.support` | Событие `night_run` | Задержка события | Доп. награда `ghost-safehouse` | — |
| corporate-ultimatum.mediate | Negotiation | 21 | `+2` за `rep.corp.helios ≥ 25` | Уменьшает напряжение, повышает репутации | Провал сделки, падение репутации | Получение протокола `helios-shadow` | Блокировка доступа (`flag.neon.blacklist`) |
| corporate-ultimatum.resist | Resolve | 19 | `+1` за активный мод `anti-ice` | Запускает событие сопротивления | Повышает тревогу Helios | Массовая поддержка (+10 репутации) | Корпоративный рейд (событие `helios-crackdown`) |
| crisis-directives.evacuation | Leadership | 20 | `+2` за `world.event.neon_lockdown` progress < 50% | Повышение стабильности Underlink | Отчёт о потерях | Сокращает кризис на 300 сек | Тяжёлые потери, падение репутации -12 |
| crisis-directives.hold-line | Combat | 21 | `+1` за активный бафф `ghost-intel` | Спавн encounter с лутом | Увеличение времени локдауна | Доп. лут `ghost-prototype` | — |
| specter-directive.ghost-trace | Investigation | 22 | `+2` при `flag.neon.elite` и `rep.fixers.neon ≥ 30` | Открывает корпоративные узлы, даёт модуль | Рост тревоги Helios | Награда `specter-cache` | Blacklist Ghosts |
| specter-directive.stabilize-city | Leadership | 21 | `+1` при `underlink.stability > 60` | Снижение беспорядков | Рост беспорядков | Событие `neon_ghosts_city_support` | Encounter riot-response |

### 3.4 Реакции на события
- **Событие:** `world.event.neon_lockdown`
  - **Условие:** уровень кризиса ≥ 2, игрок союзник Ghosts.
  - **Реплика:** «У нас пять минут, чтобы вывести курьеров. Ты ведёшь северный туннель.»
  - **Последствия:** активируется узел `crisis-directives`, повышается `underlink.stability` при успехе.
- **Событие:** `world.event.maelstrom_underlink_raid`
  - **Условие:** игрок сотрудничал с Maelstrom.
  - **Реплика:** «Маельстром жжёт наш маршрут. Надеюсь, это стоило тебе доверия.»
  - **Последствия:** снижение `rep.fixers.neon`, открытие ветки `exposed`. 
- **Событие:** `contract.helios-shadow-ops`
  - **Условие:** игрок заключил корпоративное соглашение.
  - **Реплика:** «Helios думает, что купил тебя. Давай посмотрим, кого ты выберешь, когда загорятся реальные туннели.»
  - **Последствия:** доступ к ветке `corporate-ultimatum`.

- **Событие:** `world.event.city_unrest`
  - **Условие:** `city.unrest.level` > 50% и `flag.neon.elite == true`.
  - **Реплика:** «Улицы кипят. Specter, держи толпу, пока мы вывозим остатки груза.»
  - **Последствия:** доступ к ветке `specter-directive`, успех снижает `city.unrest.level`, провал усиливает беспорядки.

## 4. Награды и последствия
- **Репутации:** `rep.fixers.neon` ±15, `rep.corp.helios` ±10, `rep.gang.maelstrom` ±12.
- **Флаги:** `flag.neon.support`, `flag.helios.deal`, `flag.maelstrom.double_agent`, `flag.neon.blacklist`, `flag.neon.elite`.
- **Предметы:** `ghost-drone`, `ghost-cache`, `ghost-countermeasure`, `ghost-prototype`, `ghost-trace-module`, `specter-cache`.
- **World-state:** модификаторы `underlink.stability`, `city.unrest.level`, события `neon_ghosts_night_run`, `neon_ghosts_city_support`, `helios-crackdown`.
- **События API:** `POST /api/v1/world/events` (запуск), `POST /api/v1/social/reputation/batch`, `POST /api/v1/economy/contracts/activate` (корпоративная ветка).

## 5. Связанные материалы
- `../npc-lore/important/aisha-frost.md`
- `../quests/side/2025-11-07-quest-neon-ghosts.md`
- `../../02-gameplay/world/events/world-events-framework.md`
- `../../05-technical/global-state/global-state-management.md`
- `../../05-technical/ui/main-game/ui-system.md`

## 6. История изменений
- 2025-11-07 18:05 — Добавлены уровень Specter, элитные поручения и реакция на городские беспорядки.
- 2025-11-07 17:55 — Первичная версия диалога, статусы ready.

