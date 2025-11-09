---
**api-readiness:** needs-work  
**api-readiness-check-date:** 2025-11-09 04:43
**api-readiness-notes:** Перепроверено 2025-11-09 04:43. Документ фиксирует концепцию и примеры, но отсутствуют согласованные значения для всех зон, таблицы дропа по брендам и согласование с economy-service; требуется доработка перед постановкой API задач.

**target-domain:** economy  
**target-microservice:** economy-service (port 8085)  
**target-frontend-module:** modules/economy/loot
---

---

- **Status:** in-review
- **Last Updated:** 2025-11-09 04:43
---

# Лут-таблицы NECPGAME

## Базовая структура

### Редкость (Rarity)
- **Common (60%):** Базовые предметы, 50-200 ed
- **Uncommon (25%):** Качественные предметы, 200-500 ed
- **Rare (12%):** Редкие предметы, 500-1000 ed
- **Epic (2.5%):** Эпические предметы, 1000-2500 ed
- **Legendary (0.5%):** Легендарные предметы, 2500-5000 ed, уникальные

### Формула вероятности

```
P(rarity) = baseChance * (1 + luckModifier) * (1 + reputationModifier) * (1 + questModifier)

luckModifier = (LUCK - 10) / 20 (max 0.5)
reputationModifier = reputationBonus / 100 (max 0.3)
questModifier = questBonus / 100 (max 0.5)
```

## Лут-таблицы по типам

### Main Quest Loot (MQ)

#### MQ-2020-002 (Узел Триака)
```json
{
  "pathA": {
    "eddy": 500,
    "items": [
      {"itemId": "CleanNodeKey", "rarity": "rare", "chance": 100}
    ],
    "reputation": {"NetWatch": 15},
    "experience": 500
  },
  "pathB": {
    "eddy": 800,
    "items": [
      {"itemId": "EchoTraceImplant", "rarity": "epic", "chance": 100}
    ],
    "reputation": {"VoodooBoys": 15},
    "experience": 600
  },
  "lootTable": {
    "common": {
      "chance": 60,
      "eddy": {"min": 200, "max": 400},
      "items": [
        {"itemId": "DataFragment", "chance": 40},
        {"itemId": "BasicImplant", "chance": 20}
      ]
    },
    "rare": {
      "chance": 30,
      "eddy": {"min": 400, "max": 800},
      "items": [
        {"itemId": "RareDataFragment", "chance": 20},
        {"itemId": "QualityImplant", "chance": 10}
      ]
    },
    "epic": {
      "chance": 10,
      "eddy": {"min": 800, "max": 1500},
      "items": [
        {"itemId": "NetWatchAccessKey", "chance": 5},
        {"itemId": "AIArtifact", "chance": 5}
      ]
    }
  }
}
```

### Side Quest Loot (SQ)

#### SQ-2020-Solo-001 (Контракт Войтека)
```json
{
  "pathNetWatch": {
    "eddy": 500,
    "items": [
      {"itemId": "NetWatchContract", "rarity": "uncommon", "chance": 100}
    ],
    "reputation": {"NetWatch": 10},
    "experience": 300
  },
  "pathFixer": {
    "eddy": 800,
    "items": [
      {"itemId": "BlackMarketAccess", "rarity": "rare", "chance": 100}
    ],
    "reputation": {"Fixers": 10, "Gangs": 5},
    "experience": 400
  },
  "lootTable": {
    "common": {
      "chance": 60,
      "eddy": {"min": 150, "max": 300},
      "items": [
        {"itemId": "BasicWeapon", "chance": 30},
        {"itemId": "Ammo", "chance": 30}
      ]
    },
    "rare": {
      "chance": 30,
      "eddy": {"min": 300, "max": 600},
      "items": [
        {"itemId": "QualityWeapon", "chance": 15},
        {"itemId": "CombatImplant", "chance": 15}
      ]
    },
    "epic": {
      "chance": 10,
      "eddy": {"min": 600, "max": 1200},
      "items": [
        {"itemId": "EpicWeapon", "chance": 5},
        {"itemId": "RareImplant", "chance": 5}
      ]
    }
  }
}
```

### World Event Loot (WE)

#### WE-2020-001 (Утечка данных)
```json
{
  "common": {
    "chance": 60,
    "eddy": {"min": 50, "max": 100},
    "items": [
      {"itemId": "DataFragment", "chance": 40},
      {"itemId": "BasicTech", "chance": 20}
    ],
    "reputation": {"NetWatch": 1}
  },
  "rare": {
    "chance": 30,
    "eddy": {"min": 100, "max": 200},
    "items": [
      {"itemId": "RareDataFragment", "chance": 20},
      {"itemId": "QualityTech", "chance": 10}
    ],
    "reputation": {"NetWatch": 3}
  },
  "epic": {
    "chance": 10,
    "eddy": {"min": 200, "max": 500},
    "items": [
      {"itemId": "NetWatchAccessKey", "chance": 5},
      {"itemId": "RareTech", "chance": 5}
    ],
    "reputation": {"NetWatch": 5}
  }
}
```

### Enemy Loot (по типам врагов)

#### Бандиты (Maelstrom, Valentinos, etc.)
```json
{
  "common": {
    "chance": 70,
    "eddy": {"min": 20, "max": 100},
    "items": [
      {"itemId": "BasicWeapon", "chance": 30},
      {"itemId": "Ammo", "chance": 40}
    ]
  },
  "uncommon": {
    "chance": 25,
    "eddy": {"min": 100, "max": 300},
    "items": [
      {"itemId": "QualityWeapon", "chance": 15},
      {"itemId": "BasicImplant", "chance": 10}
    ]
  },
  "rare": {
    "chance": 5,
    "eddy": {"min": 300, "max": 800},
    "items": [
      {"itemId": "RareWeapon", "chance": 3},
      {"itemId": "QualityImplant", "chance": 2}
    ]
  }
}
```

#### Корп-охрана (Arasaka, Militech)
```json
{
  "common": {
    "chance": 60,
    "eddy": {"min": 50, "max": 200},
    "items": [
      {"itemId": "CorpWeapon", "chance": 30},
      {"itemId": "CorpData", "chance": 30}
    ]
  },
  "rare": {
    "chance": 30,
    "eddy": {"min": 200, "max": 500},
    "items": [
      {"itemId": "CorpAccessKey", "chance": 15},
      {"itemId": "CorpImplant", "chance": 15}
    ]
  },
  "epic": {
    "chance": 10,
    "eddy": {"min": 500, "max": 1000},
    "items": [
      {"itemId": "CorpSecrets", "chance": 5},
      {"itemId": "RareCorpTech", "chance": 5}
    ]
  }
}
```

#### NetWatch / Rogue AI
```json
{
  "common": {
    "chance": 50,
    "eddy": {"min": 100, "max": 300},
    "items": [
      {"itemId": "DataFragment", "chance": 40},
      {"itemId": "NetTech", "chance": 10}
    ]
  },
  "rare": {
    "chance": 40,
    "eddy": {"min": 300, "max": 800},
    "items": [
      {"itemId": "NetAccessKey", "chance": 20},
      {"itemId": "AIFragment", "chance": 20}
    ]
  },
  "epic": {
    "chance": 10,
    "eddy": {"min": 800, "max": 2000},
    "items": [
      {"itemId": "BlackwallKey", "chance": 5},
      {"itemId": "AIArtifact", "chance": 5}
    ]
  }
}
```

## Модификаторы лута

### Уровень персонажа
```
levelModifier = (playerLevel - questLevel) / 10
if levelModifier > 0.5: levelModifier = 0.5
if levelModifier < -0.5: levelModifier = -0.5

finalChance = baseChance * (1 + levelModifier)
```

### Репутация
```
reputationModifier = reputationBonus / 100
if reputationModifier > 0.3: reputationModifier = 0.3

finalChance = baseChance * (1 + reputationModifier)
```

### Удача (LUCK)
```
luckModifier = (LUCK - 10) / 20
if luckModifier > 0.5: luckModifier = 0.5

finalChance = baseChance * (1 + luckModifier)
```

### Критический успех
```
criticalSuccessBonus = 1.5x (50% увеличение шансов редкого лута)
```

### Критический провал
```
criticalFailurePenalty = 0.5x (50% уменьшение лута или отсутствие лута)
```

## Формула финального лута

```
finalLoot = baseLoot * (1 + levelModifier) * (1 + reputationModifier) * (1 + luckModifier) * criticalMultiplier

criticalMultiplier = 1.5 (if criticalSuccess) or 0.5 (if criticalFailure) or 1.0 (normal)
```

## Примеры расчёта

### Пример 1: Уровень 8, репутация NetWatch +20, LUCK 12, критический успех
```
baseChance = 0.10 (epic)
levelModifier = (8-5)/10 = 0.3 (нормальный)
reputationModifier = 20/100 = 0.2
luckModifier = (12-10)/20 = 0.1
criticalMultiplier = 1.5

finalChance = 0.10 * 1.3 * 1.2 * 1.1 * 1.5 = 0.257 (25.7%)
```

### Пример 2: Уровень 3, репутация -5, LUCK 8, критический провал
```
baseChance = 0.10 (epic)
levelModifier = (3-5)/10 = -0.2
reputationModifier = -5/100 = -0.05
luckModifier = (8-10)/20 = -0.1
criticalMultiplier = 0.5

finalChance = 0.10 * 0.8 * 0.95 * 0.9 * 0.5 = 0.0342 (3.42%)
```



