---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-05 19:35
**api-readiness-notes:** Репутационные формулы: расчёт репутации фракций, влияние на DC, доступы, бонусы, штрафы, heat.

**target-domain:** social  
**target-microservice:** social-service (port 8084)  
**target-frontend-module:** modules/social/reputation
---

---

- **Status:** created
- **Last Updated:** 2025-11-07 01:05
---

# Репутационные формулы NECPGAME

## Базовая структура репутации

### Диапазон репутации
- **-100 до -50:** Враг (Hostile)
- **-49 до -10:** Неприятель (Unfriendly)
- **-9 до 9:** Нейтрал (Neutral)
- **10 до 49:** Друг (Friendly)
- **50 до 99:** Союзник (Ally)
- **100+:** Легендарный (Legendary)

### Формула изменения репутации

```
reputationChange = baseChange * (1 + modifier1) * (1 + modifier2) * ...

modifiers:
- classBonus: +0.2 (если класс соответствует фракции)
- originBonus: +0.1 (если origin соответствует фракции)
- questBonus: +0.3 (если квест фракционный)
- skillCheckBonus: +0.5 (если критический успех) или -0.5 (критический провал)
```

## Влияние репутации на DC

### Модификатор DC от репутации

```
dcModifier = floor(reputation / 10) * -1

Примеры:
- reputation = 25 → dcModifier = -2
- reputation = 50 → dcModifier = -5
- reputation = -20 → dcModifier = +2
```

### Формула финального DC

```
finalDC = baseDC + dcModifier + otherModifiers

otherModifiers:
- heat: +1 per heat level
- injury: +2 per injury level
- advantage: -2 (если есть)
- disadvantage: +2 (если есть)
```

## Доступы и бонусы по репутации

### Доступ к ресурсам

```
accessLevel = floor((reputation + 50) / 25)

Уровни доступа:
- 0: Нет доступа (reputation < -25)
- 1: Базовый доступ (reputation -25 to 0)
- 2: Ограниченный доступ (reputation 0 to 25)
- 3: Полный доступ (reputation 25 to 50)
- 4: Привилегированный доступ (reputation 50 to 75)
- 5: Эксклюзивный доступ (reputation 75+)
```

### Скидки на товары

```
discount = min(floor(reputation / 10), 20) / 100

Примеры:
- reputation = 30 → discount = 20% (max)
- reputation = 50 → discount = 20% (max)
- reputation = 10 → discount = 10%
```

### Бонусы к репутации в других фракциях

```
alliedFactionBonus = reputation / 20 (если фракции союзники)
enemyFactionPenalty = reputation / 10 (если фракции враги)

Примеры:
- Arasaka reputation = 50 → Militech penalty = -5 (враги)
- NetWatch reputation = 30 → Voodoo Boys penalty = -3 (конфликт)
- Valentinos reputation = 40 → 6th Street penalty = -4 (враги)
```

## Heat (Охота)

### Формула heat

```
heat = baseHeat + reputationPenalties + questPenalties + skillCheckPenalties

baseHeat = 0 (по умолчанию)
reputationPenalties = -reputation / 20 (если репутация < 0)
questPenalties = +1 per failed quest или +2 per betrayal
skillCheckPenalties = +1 per critical failure (если критично)
```

### Влияние heat на геймплей

```
heatModifier = heat * 2

Влияние:
- DC всех проверок: +heatModifier
- Шанс события: +heatModifier% (негативные события)
- Доступ к ресурсам: -heatModifier (как репутация)
```

### Снижение heat

```
heatDecay = timePassed / cooldownTime

heatReduction = heatDecay * reputationBonus

Примеры:
- heat = 5, время прошло = 1 час, cooldown = 2 часа
- heatReduction = 0.5 * (1 + reputationBonus)
- Если reputation = 20 → finalHeat = 5 - 0.5 * 1.2 = 4.4 → 4
```

## Конфликты фракций

### Матрица конфликтов

```
conflictMatrix = {
  "Arasaka": {
    "Militech": -0.5,  // Враги
    "NetWatch": 0.1,   // Союзники
    "VoodooBoys": -0.3 // Конфликт
  },
  "Militech": {
    "Arasaka": -0.5,
    "NetWatch": 0.2,
    "VoodooBoys": -0.2
  },
  "NetWatch": {
    "VoodooBoys": -0.8, // Сильный конфликт
    "Arasaka": 0.1,
    "Militech": 0.2
  },
  "VoodooBoys": {
    "NetWatch": -0.8,
    "Arasaka": -0.3,
    "Militech": -0.2
  }
}
```

### Влияние конфликтов на репутацию

```
reputationChange = baseChange * (1 + conflictModifier)

Пример:
- Базовая репутация Arasaka +10
- Конфликт с Militech = -0.5
- Финальная репутация Arasaka = 10 * (1 - 0.5) = 5
- Репутация Militech = -10 * 0.5 = -5 (автоматический штраф)
```

## Репутация по классам

### Классовые бонусы к репутации

```
classReputationBonus = {
  "Solo": {
    "Militech": 0.2,
    "Arasaka": 0.2,
    "Gangs": 0.3
  },
  "Netrunner": {
    "NetWatch": 0.2,
    "VoodooBoys": 0.3,
    "Corpos": 0.1
  },
  "Techie": {
    "Corpos": 0.2,
    "Nomads": 0.2,
    "Fixers": 0.2
  },
  "Fixer": {
    "Gangs": 0.3,
    "Fixers": 0.4,
    "Corpos": 0.1
  },
  "Rockerboy": {
    "Gangs": 0.2,
    "Media": 0.3,
    "Activists": 0.3
  },
  "Media": {
    "Activists": 0.3,
    "Politicians": 0.2,
    "Corpos": 0.1
  },
  "Nomad": {
    "Nomads": 0.4,
    "Fixers": 0.2,
    "Gangs": 0.1
  },
  "Corpo": {
    "Arasaka": 0.3,
    "Militech": 0.3,
    "Corpos": 0.4
  },
  "Lawman": {
    "NCPD": 0.4,
    "NetWatch": 0.2,
    "Corpos": 0.1
  },
  "Medtech": {
    "TraumaTeam": 0.4,
    "Clinics": 0.3,
    "Corpos": 0.1
  },
  "Politician": {
    "DAO": 0.4,
    "Corpos": 0.2,
    "Activists": 0.2
  },
  "Trader": {
    "Fixers": 0.3,
    "Gangs": 0.2,
    "Corpos": 0.1
  },
  "Teacher": {
    "DAO": 0.3,
    "Activists": 0.2,
    "Citizens": 0.3
  }
}
```

## Репутация по origins

### Origin бонусы

```
originReputationBonus = {
  "StreetKid": {
    "Gangs": 0.3,
    "Fixers": 0.2,
    "Corpos": -0.1
  },
  "Corpo": {
    "Corpos": 0.3,
    "Gangs": -0.2,
    "Activists": -0.1
  },
  "Nomad": {
    "Nomads": 0.4,
    "Gangs": 0.1,
    "Corpos": -0.1
  },
  "OutlawScholar": {
    "Activists": 0.2,
    "Media": 0.2,
    "Corpos": -0.1
  },
  "ClinicRat": {
    "Medtech": 0.3,
    "TraumaTeam": 0.2,
    "Gangs": -0.1
  },
  "DataOrphan": {
    "VoodooBoys": 0.3,
    "NetWatch": -0.2,
    "Activists": 0.1
  }
}
```

## Примеры расчёта

### Пример 1: Netrunner, Street Kid origin, критический успех
```
baseReputation = 15 (NetWatch)
classBonus = 0.2 (Netrunner → NetWatch)
originBonus = 0 (Street Kid → NetWatch)
skillCheckBonus = 0.5 (критический успех)

finalReputation = 15 * (1 + 0.2) * (1 + 0.5) = 15 * 1.2 * 1.5 = 27

Итого: +27 репутация NetWatch
```

### Пример 2: Solo, Corpo origin, предательство Arasaka
```
baseReputation = -20 (Arasaka, предательство)
classBonus = 0.2 (Solo → Arasaka, но предательство)
originBonus = 0.3 (Corpo → Arasaka, но предательство)
betrayalPenalty = -0.5

finalReputation = -20 * (1 + 0.2 + 0.3) * (1 - 0.5) = -20 * 1.5 * 0.5 = -15

Итого: -15 репутация Arasaka (дополнительно к базовому -20)
```

### Пример 3: Влияние репутации на DC
```
baseDC = 20 (Hacking)
reputation = 30 (NetWatch)
dcModifier = floor(30 / 10) * -1 = -3

finalDC = 20 - 3 = 17

Итого: DC снижен с 20 до 17
```

## Формула финальной репутации

```
finalReputation = currentReputation + reputationChange * modifiers

modifiers = (1 + classBonus) * (1 + originBonus) * (1 + questBonus) * (1 + skillCheckBonus) * (1 + conflictModifier)

Где:
- classBonus: бонус от класса (0-0.4)
- originBonus: бонус от origin (0-0.3)
- questBonus: бонус от типа квеста (0-0.3)
- skillCheckBonus: бонус от skill check (-0.5 до +0.5)
- conflictModifier: модификатор конфликта (-0.8 до +0.2)
```



