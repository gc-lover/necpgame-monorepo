# Content Creator Guide: Квесты и события

**Версия:** 1.0.0  
**Дата:** 2025-11-07 00:02

---

## Краткое описание

Гайд для контент-мейкеров по созданию квестов с ветвлениями и добавлению их в событийный граф.

---

## Создание квеста

### Шаг 1: Определить тип и место

**Вопросы:**
1. Какой тип квеста? (main, side, faction, romantic, daily, world)
2. Какая эпоха? (2020-2030, 2030-2045, 2045-2060, 2060-2077, 2077, 2078-2093)
3. Какой регион? (Night City, Tokyo, Berlin, и т.д.)
4. Для какого класса? (универсальный или class-specific)

**Пример:**
```yaml
quest_basics:
  type: side
  era: "2045-2060"
  region: "Night City, Watson"
  class_focus: Netrunner
  level: 25
```

### Шаг 2: Создать сюжет

**Структура сюжета:**
1. **Hook** - как игрок узнаёт о квесте
2. **Setup** - начальная информация
3. **Conflict** - проблема/задача
4. **Choices** - критические выборы (2-3 минимум)
5. **Resolution** - концовки (минимум 2)

**Пример:**
```markdown
## "Охота на фантомов" (SQ-2045-001)

Hook: NetWatch агент контактирует с игроком
Setup: Phantom programs угрожают NET
Conflict: Найти и уничтожить фантомы
Choices:
  - A: Уничтожить фантомы (NetWatch путь)
  - B: Изучить фантомы (ИИ-исследование путь)
  - C: Освободить фантомы (Voodoo Boys путь)
Resolution:
  - A: NetWatch благодарны, Voodoo Boys враги
  - B: Знание ИИ +1, все нейтральны
  - C: Voodoo Boys благодарны, NetWatch враги
```

### Шаг 3: Определить зависимости

**Prerequisites (что нужно до):**
```yaml
prerequisites:
  quests: ["MQ-2045-002"]  # Глушь-пакеты (открывает информацию)
  flags: ["netwatch_ally: true"]  # Только для союзников NetWatch
  reputation: {"NetWatch": 30}  # Минимум 30 репутации
  level: 25
```

**Unlocks (что открывает):**
```yaml
unlocks:
  immediate: ["SQ-2045-002"]  # Сразу
  next_era: ["SQ-2060-001"]  # В следующей эпохе
  conditional:
    - quest: "SQ-2078-004"  # Если выбор A
      condition: "choice_destroy_phantoms"
```

**Blocks (что блокирует):**
```yaml
blocks:
  permanent: ["SQ-2045-VB-001"]  # Voodoo Boys квест (если выбор A)
  temporary: []
```

### Шаг 4: Создать dialogue tree

**Формат:**
```yaml
dialogue_tree:
  root: 1
  nodes:
    - id: 1
      npc: "NetWatch Agent Vogel"
      text: "We have a problem. Phantom programs in the NET."
      type: dialogue
      next: 2
    
    - id: 2
      npc: "NetWatch Agent Vogel"
      text: "Will you help us hunt them down?"
      type: choice
      choices:
        - id: A1
          text: "[Accept] I'll destroy them."
          next: 3
          consequences:
            reputation: {"NetWatch": +15}
            sets_flags: ["hunting_phantoms"]
        
        - id: A2
          text: "[Refuse] Not interested."
          next: 99
          consequences:
            reputation: {"NetWatch": -5}
    
    - id: 3
      npc: "NetWatch Agent Vogel"
      text: "Good. Here's the location data."
      type: dialogue
      next: 4
    
    # ... more nodes ...
    
    - id: 99
      type: end
      outcome: "Quest declined"
```

---

## D&D Skill Checks

### Добавление skill check

```yaml
choice_with_check:
  - id: B1
    text: "[Hacking 16] Hack into the phantom code"
    skill_check:
      skill: hacking
      dc: 16
      advantage_conditions: ["netrunner_class"]
      disadvantage_conditions: ["cyberpsychosis > 50"]
    success:
      next: 10
      consequences:
        reputation: {"NetWatch": +20}
        gives_items: ["phantom_core_data"]
    failure:
      next: 11
      consequences:
        damage: 50
        reputation: {"NetWatch": -5}
```

---

## Шаблон квеста

```yaml
quest_id: "SQ-YYYY-NNN"
name: "Quest Name"
description: "Short description (2-3 sentences)"
type: side | main | faction | romantic | daily | world
era: "YYYY-YYYY"
region: "Location"

# Requirements
min_level: 1
max_level: null
required_quests: []
required_flags: []
required_reputation: {}
class_specific: null

# Structure
has_branches: true
dialogue_tree_root: 1
objectives:
  - id: 1
    text: "Objective text"
    type: kill | collect | talk | hack | stealth
    target: "target_id"
    count: 1

# Rewards
rewards:
  experience: 1000
  money: 5000
  items: ["item_id_1"]
  reputation:
    Faction1: 15
    Faction2: -10

# Dependencies
unlocks:
  immediate: []
  next_era: []
  conditional: []
blocks:
  permanent: []
  temporary: []

# Dialogue tree
dialogue_tree:
  # See dialogue tree format above
```

---

## Чеклист создания квеста

**Перед созданием:**
- [ ] Определён тип, эпоха, регион
- [ ] Проверена доступность NPC в эту эпоху
- [ ] Проверена доступность локации в эту эпоху
- [ ] Определены prerequisites

**При создании:**
- [ ] Сюжет имеет минимум 2 концовки
- [ ] Есть минимум 1 критический выбор
- [ ] Dialogue tree полный (нет dead ends)
- [ ] Skill checks сбалансированы

**После создания:**
- [ ] Квест добавлен в quest-dependencies.yaml
- [ ] Unlocks/blocks документированы
- [ ] Проверена temporal coherence
- [ ] Тестовый прогон выполнен

---

## Примеры квестов

**Простой квест (без ветвления):**
- See: `SQ-2020-001-vojtek-contract.md`

**Средний квест (2-3 ветки):**
- See: `SQ-2045-001-phantom-hunt.md`

**Сложный квест (4+ ветки, D&D nodes):**
- See: `001-first-steps-dnd-nodes.md`

---

## Linked Documents

- [Quest Template](../templates/quest-template-extended.md)
- [Developer Guide](../dev-guides/developer-guide.md)

---

## История изменений

- v1.0.0 (2025-11-07 00:02) - Content creator guide

