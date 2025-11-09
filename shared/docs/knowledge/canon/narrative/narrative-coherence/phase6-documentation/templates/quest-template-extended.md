# Quest Template with Branching

**Версия:** 1.0.0  
**Тип:** Template  
**Для:** Content Creators

---

## Метаданные

```yaml
quest_id: "SQ-YYYY-NNN"  # YYYY = year start, NNN = number
name: "[Quest Name]"
description: "[2-3 sentences describing the quest]"
type: side  # main | side | faction | romantic | daily | weekly | world | event
era: "YYYY-YYYY"
region: "[Region/City/District]"
estimated_duration: 30  # minutes

# Author info
author: "[Your name]"
created: "YYYY-MM-DD"
version: "1.0.0"
```

---

## Requirements

```yaml
requirements:
  min_level: 1
  max_level: null
  
  # Prerequisites
  required_quests: []
  required_flags: []
  required_reputation: {}
  
  # Class/Origin
  class_specific: null  # or "Netrunner", "Solo", etc
  origin_specific: null  # or "Nomad", "Corpo", "Street Kid"
  
  # Items
  required_items: []
```

---

## Quest Structure

```yaml
structure:
  has_branches: true  # or false for linear quest
  total_branches: 3  # number of different paths
  dialogue_tree_root: 1  # starting node ID
  
  # Objectives
  objectives:
    - id: 1
      text: "[Objective description]"
      type: talk  # kill | collect | talk | hack | stealth | escort | defend
      target: "npc_id" or "location_id"
      count: 1
      optional: false
```

---

## Rewards (base)

```yaml
rewards:
  experience: 1000
  money: 5000
  items:
    - item_id: "weapon_001"
      quantity: 1
  reputation:
    Faction1: 15
    Faction2: -10
  
  # Branch-specific rewards in branches section
```

---

## Dialogue Tree

```yaml
dialogue_tree:
  nodes:
    # === START ===
    - id: 1
      npc_id: "npc_id"
      npc_name: "[NPC Name]"
      text: "[Opening dialogue]"
      emotion: neutral  # angry | happy | sad | neutral | scared
      type: dialogue
      next: 2
    
    # === FIRST CHOICE ===
    - id: 2
      npc_id: "npc_id"
      npc_name: "[NPC Name]"
      text: "[Question or situation]"
      type: choice
      choices:
        - id: A1
          text: "[Option A - aggressive]"
          next: 10
          consequences:
            reputation: {"Faction": -5}
            sets_flags: ["aggressive_approach"]
        
        - id: A2
          text: "[Option B - diplomatic]"
          next: 20
          consequences:
            reputation: {"Faction": +5}
            sets_flags: ["diplomatic_approach"]
        
        - id: A3
          text: "[Hacking 16] [Option C - hack]"
          skill_check:
            skill: hacking
            dc: 16
            advantage: ["netrunner_class"]
          success:
            next: 30
            consequences:
              gives_items: ["secret_data"]
          failure:
            next: 11
            consequences:
              damage: 50
    
    # === PATH A (aggressive) ===
    - id: 10
      npc_id: "npc_id"
      text: "[Response to aggression]"
      type: dialogue
      next: 100
    
    # === PATH B (diplomatic) ===
    - id: 20
      npc_id: "npc_id"
      text: "[Response to diplomacy]"
      type: dialogue
      next: 100
    
    # === PATH C (hacking) ===
    - id: 30
      npc_id: "npc_id"
      text: "[Response to successful hack]"
      type: dialogue
      next: 100
    
    # === FINALE (paths converge) ===
    - id: 100
      type: end
      outcome: "Quest completed"
      branch_outcome: "[Description of result]"
```

---

## Quest Branches (optional)

```yaml
branches:
  # Path A
  - branch_id: "pathA"
    branch_name: "Aggressive Path"
    conditions:
      flags: ["aggressive_approach"]
    consequences:
      reputation_changes:
        Faction: -10
      sets_flags: ["known_as_aggressive"]
      unlocks_quests: ["SQ-NEXT-A"]
      blocks_quests: ["SQ-NEXT-B"]
  
  # Path B
  - branch_id: "pathB"
    branch_name: "Diplomatic Path"
    conditions:
      flags: ["diplomatic_approach"]
    consequences:
      reputation_changes:
        Faction: +15
      sets_flags: ["known_as_diplomat"]
      unlocks_quests: ["SQ-NEXT-B"]
  
  # Path C
  - branch_id: "pathC"
    branch_name: "Hacker Path"
    conditions:
      flags: ["hacked_successfully"]
      class: "Netrunner"
    consequences:
      gives_items: ["secret_data", "rare_program"]
      sets_flags: ["master_hacker"]
      unlocks_quests: ["SQ-NEXT-C"]
```

---

## Dependencies (важно!)

```yaml
dependencies:
  # Unlocks (этот квест открывает)
  unlocks:
    immediate:
      - "SQ-YYYY-NNN+1"
    next_era:
      - "SQ-NEXT-ERA-001"
    conditional:
      - quest: "SQ-CONDITIONAL"
        condition: "pathA_completed"
  
  # Blocks (этот квест блокирует)
  blocks:
    permanent:
      - "SQ-ENEMY-FACTION"  # Враждебная фракция
    temporary:
      - "SQ-COMPETING-QUEST"  # Конкурирующий квест
  
  # Influences (этот квест влияет на)
  influences:
    - quest: "MQ-MAIN-QUEST"
      impact: "Makes easier"
      modifier: +0.2
```

---

## World State Impact

```yaml
world_state_impact:
  # Changes to world
  changes:
    - key: "faction_power_Faction1"
      value_change: +10
      scope: server  # personal | server | faction
    
    - key: "territory_control_Watson"
      new_controller: "Faction1"
      scope: server
    
    - key: "npc_status_npc_id"
      new_status: "dead"  # alive | dead | exiled | imprisoned
      scope: server
  
  # Triggers for future
  triggers:
    - event: "faction_war_start"
      condition: "faction_power_Faction1 > 80"
    
    - event: "npc_revenge_quest"
      condition: "npc_status_ally == dead"
      delay: "1 month in-game"
```

---

## Чеклист создания квеста

### Design Phase
- [ ] Тип квеста определён
- [ ] Эпоха и регион определены
- [ ] NPC доступны в эту эпоху (check npc-lifecycle.yaml)
- [ ] Локация доступна (check location-timeline.yaml)
- [ ] Фракция существует (check faction-evolution.yaml)

### Writing Phase
- [ ] Сюжет написан (hook, setup, conflict, choices, resolution)
- [ ] Минимум 2 ветки (pathA, pathB)
- [ ] Dialogue tree полный (20-30 nodes для main, 10-15 для side)
- [ ] Skill checks balanced (DC соответствует уровню)

### Integration Phase
- [ ] Prerequisites документированы
- [ ] Unlocks определены (immediate, next_era, conditional)
- [ ] Blocks определены (если блокирует квесты)
- [ ] World state impact описан

### Testing Phase
- [ ] Temporal coherence проверена
- [ ] Dialogue tree пройден (нет dead ends)
- [ ] Все ветки имеют resolution
- [ ] Rewards сбалансированы

### Publication Phase
- [ ] Квест добавлен в quest-dependencies.yaml
- [ ] Квест добавлен в side-quests-matrix.yaml (если связан с main)
- [ ] README обновлён (если новая категория)
- [ ] Git commit сделан

---

## Примеры

**Simple quest (без ветвления):**
```yaml
quest_id: "SQ-2020-001"
name: "Контракт Войтека"
type: side
has_branches: false
dialogue_nodes: 8
choices: 1
outcomes: 1
```

**Medium quest (2-3 ветки):**
```yaml
quest_id: "SQ-2045-001"
name: "Охота на фантомов"
type: side
has_branches: true
branches: 3
dialogue_nodes: 15
choices: 3
outcomes: 3
```

**Complex quest (4+ ветки, critical):**
```yaml
quest_id: "MQ-2020-005"
name: "Чистый канал"
type: main
has_branches: true
branches: 4
dialogue_nodes: 30
choices: 5
outcomes: 4
finale_impact: true
```

---

## Связанные документы

- [Quest System](../../../../quest-system.md)
- [Quest D&D Checks](../../../../quest-dnd-checks.md)
- [QUEST-TEMPLATE-DND](../../../../quests/QUEST-TEMPLATE-DND.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:02) - Quest creation guide with template

