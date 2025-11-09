---
title: "Neon Ghosts"
quest-id: "side-neon-ghosts"
status: "approved"
version: "1.0.0"
priority: "high"
date-created: "2025-11-07"
date-updated: "2025-11-07"
author: "Brain Manager"
target-microservices:
  - gameplay-service (8083)
  - social-service (8084)
  - economy-service (8085)
target-frontend-modules:
  - modules/quests
  - modules/world
  - modules/social
api-readiness: "ready"
**api-readiness-check-date:** "2025-11-07 17:45"
api-readiness-notes: "Побочный квест о ночных курьерах в неоновой изнанке. Описаны узлы, проверки, награды, мировые последствия и API точки. Готов к постановке задач."
---

# Квест «Neon Ghosts»

## 1. Синопсис
- **Жанр:** кооперативный сайд-квест на 2-4 игрока в сеттинге Underlink.
- **Завязка:** подпольная курьерская сеть «Neon Ghosts» похищает пакет данных корпорации Helios Throne. Игрок обязан вернуть данные, решив, сохранить ли сеть.
- **Цель:** проникнуть в слой Underlink, отследить курьеров, решить судьбу сети (поддержать, интегрировать в корпорацию или разоблачить).
- **Основные решения:** поддержать Ghosts, передать Helios Throne, слить Maelstrom. Каждое решение меняет мировые модификаторы и социальные индексы.

## 2. Структура квеста

| Этап | Узел | Описание | Локация | Связанные сервисы |
|------|------|----------|---------|-------------------|
| 1 | `intel-briefing` | Получить координаты курьеров у информатора «Echo Lynx» | NQ-Arrake Prime, район Neon Market | social-service (репутация), gameplay-service (диалог) |
| 2 | `underlink-entry` | Спуститься в Underlink, обойти ICE, найти ретранслятор | Underlink tunnels | gameplay-service (stealth), world-service (event trigger) |
| 3 | `ghost-confrontation` | Построить доверие с лидером Ghosts или арестовать их | Underlink core | social-service (резонанс), economy-service (контракты) |
| 4 | `resolution` | Выбрать исход: ally, corporate deal, expose | Helios data vault | world-service (modifiers), economy-service (market impact) |

## 3. Узлы и проверки

```yaml
- node-id: intel-briefing
  label: Брифинг от Echo Lynx
  entry-condition: quest.start
  player-options:
    - option-id: pay-info
      text: "Заплатить за координаты"
      requirements:
        - type: resource
          resource: eddies
          amount: 5000
      npc-response: "Neon Ghosts сменили канал. Вот свежий маршрут."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-entry", reputation: { "fixers.neon": +5 } }
    - option-id: persuade
      text: "Выбить информацию"
      requirements:
        - type: stat-check
          stat: Persuasion
          dc: 19
      npc-response: "Ладно, только не приводи Arasaka."
      outcomes:
        success: { effect: "unlock_node", node: "underlink-entry", reputation: { "fixers.neon": +2 } }
        failure: { effect: "spawn_encounter", encounter: "lynx-bodyguards", reputation: { "fixers.neon": -4 } }

- node-id: underlink-entry
  label: Проникновение в Underlink
  entry-condition: intel-briefing.completed
  player-options:
    - option-id: hack-gateway
      text: "Взломать шлюз"
      requirements:
        - type: stat-check
          stat: Hacking
          dc: 21
      npc-response: "ICE-сеть мерцает, путь открыт."
      outcomes:
        success: { effect: "set_flag", flag: "underlink.access", reward: { xp: 1200 } }
        failure: { effect: "raise_alert", alert-level: "medium", penalty: { hp: -150 } }
        critical-success: { effect: "disable_ice", duration: 180, reward: { item: "ghost-cypher" } }
    - option-id: stealth-route
      text: "Пройти скрытно"
      requirements:
        - type: stat-check
          stat: Stealth
          dc: 20
      npc-response: "Сканеры не заметили группу."
      outcomes:
        success: { effect: "grant_buff", buff: "shadow-step", duration: 600 }
        failure: { effect: "spawn_encounter", encounter: "ice-sentries" }

- node-id: ghost-confrontation
  label: Переговоры с лидером Ghosts
  entry-condition: underlink-entry.completed
  player-options:
    - option-id: build-trust
      text: "Доказать лояльность"
      requirements:
        - type: stat-check
          stat: Empathy
          dc: 18
        - type: reputation
          reputation: fixers.neon
          value: 10
      npc-response: "Ладно, мы расскажем кто заказчик."
      outcomes:
        success: { effect: "unlock_branch", branch: "ally" }
        failure: { effect: "reduce_trust", reputation: { "fixers.neon": -6 } }
    - option-id: threaten
      text: "Угрожать связями Helios"
      requirements:
        - type: stat-check
          stat: Intimidation
          dc: 20
      npc-response: "Ты не понимаешь, что поставлено на кон."
      outcomes:
        success: { effect: "unlock_branch", branch: "corporate" }
        failure: { effect: "agro", encounter: "ghost-lieutenant" }
    - option-id: double-agent
      text: "Слить информацию Maelstrom"
      requirements:
        - type: flag
          flag: "maelstrom.contact"
      npc-response: "Maelstrom заплатит за такое."
      outcomes:
        success: { effect: "unlock_branch", branch: "maelstrom" }

- node-id: resolution
  label: Финальное решение
  entry-condition: ghost-confrontation.branch-selected
  branches:
    ally:
      description: "Поддержать Ghosts и встроить в городскую сеть"
      effects:
        - type: world-modifier
          key: "underlink.delivery.boost"
          value: +12
        - type: reputation
          target: fixers.neon
          value: +15
        - type: unlock
          item: "ghost-network-key"
    corporate:
      description: "Передать Helios Throne, получить корпоративную награду"
      effects:
        - type: world-modifier
          key: "helio.security.index"
          value: +10
        - type: reputation
          target: corp.helios
          value: +20
        - type: contract
          id: "helios-shadow-ops"
    maelstrom:
      description: "Перекинуть данные Maelstrom и запустить хаос"
      effects:
        - type: world-modifier
          key: "city.chaos.level"
          value: +8
        - type: reputation
          target: gang.maelstrom
          value: +18
        - type: event
          id: "maelstrom-underlink-raid"
```

## 4. Механики и интеграции

- **Репутация:**
  - `fixers.neon` (social-service) — доступ к подпольным контрактам.
  - `corp.helios` и `gang.maelstrom` — влияет на последующие ветки `Throne of Sand`.
- **World-state:**
  - `underlink.delivery.boost` отражается в World Pulse (UI World Interaction).
  - `city.chaos.level` при росте >15 активирует кризис «City Unrest».
- **Economy:**
  - Ally ветка открывает рынок «Ghost Relay» (скидки на нелегальные импланты).
  - Corporate ветка запускает контракт `economy/market/intervention` с бонусом к стабильности.
- **Social Resonance:**
  - Ally: +2 к `harmony` в Social Resonance.
  - Corporate: +3 к `order`, -2 к `romance` (жёсткий контроль).
  - Maelstrom: +4 к `unrest`, разблокирует событие `Neon Lockdown`.

## 5. Награды

| Ветка | EXP | Предметы | Денежная награда | Особенности |
|-------|-----|----------|------------------|-------------|
| Ally | 4500 | `ghost-network-key`, `augment-shadow-step` | 9 000 | Открывает ежедневные задания Ghosts |
| Corporate | 4200 | `helios-credstick`, `corp-access-pass` | 12 000 | Доступ к операции «Helios Shadow Ops» |
| Maelstrom | 4700 | `maelstrom-ripper-chip`, `riot-sim booster` | 8 500 | Запускает событие `maelstrom-underlink-raid` |

## 6. API контуры
- `GET /api/v1/quests/side-neon-ghosts` — структура квеста (gameplay-service).
- `POST /api/v1/quests/side-neon-ghosts/progress` — обновление узлов, передача результатов проверок.
- `POST /api/v1/world/modifiers` — применение ветвевых модификаторов (world-service).
- `POST /api/v1/social/reputation/batch` — пакетные изменения репутации.
- `POST /api/v1/economy/contracts/activate` — выдача контракта Helios/market perks.

## 7. Тестирование
- Unit: проверки ветвления и валидности репутационных условий.
- Integration: сценарио Ally/Corporate/Maelstrom с world-state и economy влиянием.
- E2E: проникновение, переговоры, финальное решение (включая co-op).
- Telemetry: `quest.neonGhosts.completion`, `quest.neonGhosts.branch`, `underlink.alert.level`.

## 8. История изменений
- 2025-11-07 — первоначальный релиз квеста, статус ready.

