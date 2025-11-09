# Архитектура графа событий

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Последнее обновление:** 2025-11-06 23:29

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 23:29
**api-readiness-notes:** Архитектура для backend реализации графа событий

---

- **Status:** created
- **Last Updated:** 2025-11-07 20:16
---

## Краткое описание

Архитектура системы графа событий для динамической генерации непредсказуемого контента в AAA MMORPG.

---

## Типы узлов графа

### 1. Quest Node
```yaml
type: quest
id: quest_id
prerequisites: [quest_ids]
unlocks: [quest_ids]
blocks: [quest_ids]
probability: 0.0-1.0  # Вероятность появления
```

### 2. Choice Node
```yaml
type: choice
id: choice_id
options: [option_ids]
consequences: {option_id: [effects]}
```

### 3. World State Node
```yaml
type: world_state
id: state_id
value: any
triggers: [event_ids]
```

### 4. Event Node
```yaml
type: event
id: event_id
conditions: [conditions]
effects: [effects]
probability: 0.0-1.0
```

---

## Типы связей

### 1. REQUIRES (prerequisite)
```
NodeA --REQUIRES--> NodeB
```
NodeB доступен только после NodeA

### 2. UNLOCKS (trigger)
```
NodeA --UNLOCKS--> NodeB
```
NodeA открывает NodeB

### 3. BLOCKS (blocker)
```
NodeA --BLOCKS--> NodeB
```
NodeA блокирует NodeB навсегда или временно

### 4. INFLUENCES (soft dependency)
```
NodeA --INFLUENCES--> NodeB
```
NodeA влияет на NodeB (difficulty, rewards, dialogue)

---

## Динамическая генерация

### Probability System
```yaml
event_pool:
  - event_id: random_ambush
    base_probability: 0.15
    modifiers:
      danger_zone: +0.10
      night_time: +0.05
      high_reputation_gangs: -0.10
    final_probability: calculated
```

### Weight System
```yaml
quest_selection:
  - quest_id: SQ-2020-001
    weight: 10
    conditions:
      level: 1-5
      class: Solo
    adjusted_weight: calculated
```

---

## World State Integration

### Global State
```yaml
world_state:
  blackwall_integrity: 0-100
  corporate_control: 0-100
  faction_wars_active: boolean
  player_chaos_score: 0-100
```

### Triggers on State Change
```yaml
triggers:
  - condition: blackwall_integrity < 50
    effect: unlock_blackwall_incident_quests
  - condition: corporate_control > 80
    effect: increase_revolution_quest_probability
```

---

## Linked Documents

- [Quest Dependencies Graph](./graph/quest-dependencies.yaml)
- [World State Graph](./graph/world-state-graph.yaml)
- [Dynamic Content System](./dynamic/content-system.md)

---

## История изменений

- v1.0.0 (2025-11-06 23:29) - Создание архитектуры графа событий
