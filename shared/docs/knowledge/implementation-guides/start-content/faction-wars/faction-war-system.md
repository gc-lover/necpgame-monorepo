---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 22:05
**api-readiness-notes:** Системный обзор Faction Wars. 2 войны (Arasaka vs Militech, Valentinos vs Maelstrom), 5 стадий каждая, выбор стороны, массовые конфликты.
---

---

- **Status:** created
- **Last Updated:** 2025-11-07 05:05
---

# FACTION WARS SYSTEM: Полный обзор

## 1. Концепция системы

**Цель:** Масштабные конфликты между фракциями, где игрок выбирает сторону и влияет на исход войны.

**Механика:**
- Игрок выбирает сторону в конфликте (branching choice)
- 5 стадий войны (escalation → climax → resolution)
- Репутация с фракциями динамически меняется
- Исход войны влияет на мир (территории, доступные квесты, NPC relationships)
- Массовые бои (10-25 врагов), stealth опции, tактические решения

## 2. Faction War #1: Arasaka vs Militech

### Контекст:
- Эпоха: 2060-2077 (корпоративные войны)
- Причина: Территориальный контроль, ресурсы, влияние
- Ключевые персонажи: Элизабет Чен (Arasaka), Сара Миллер (neutral/Militech sympathizer)

### Стадии войны:

**Stage 1: Пограничная стычка** (`quest-factionwar-am-01-border-skirmish`)
- Выбор стороны: Arasaka OR Militech
- Первые столкновения на границе территорий
- 8 врагов, AC 14-16
- Rewards: 800 XP, +20 faction reputation

**Stage 2: Рейд на склады** (`quest-factionwar-am-02-supply-raid`)
- Саботаж поставок противника
- Stealth OR combat approach
- 10 врагов, environmental hazards
- Rewards: 950 XP, +30 faction reputation

**Stage 3: Assassination** (`quest-factionwar-am-03-assassination`)
- Устранение высокопоставленного офицера противника
- Infiltration + boss fight
- 12 врагов + 1 boss (AC 18, HP 250)
- Rewards: 1100 XP, +40 faction reputation, unique item

**Stage 4: Контроль территории** (`quest-factionwar-am-04-territory-control`)
- Захват и удержание ключевых точек
- 2-phase combat: assault (15 врагов) + defense (10 врагов)
- Rewards: 1300 XP, +50 faction reputation

**Stage 5: Решающая битва** (`quest-factionwar-am-05-decisive-battle`)
- Финальное столкновение
- Massive combat: 20 врагов + commander boss (AC 20, HP 400)
- Victory определяет контроль над территорией
- Rewards: 1500 XP, +100 faction reputation, war trophy, title

### Исход войны:
- **Arasaka Victory:** Arasaka контролирует Downtown, Militech quests locked, Arasaka quests expanded
- **Militech Victory:** Militech контролирует Downtown, Arasaka quests locked, Militech quests expanded
- **Мировое влияние:** NPC dialogues изменяются, территории переходят, цены на рынке меняются

---

## 3. Faction War #2: Valentinos vs Maelstrom

### Контекст:
- Эпоха: 2045-2060 (gang wars)
- Причина: Территория, наркотики, оружие
- Ключевые персонажи: Марко Фикс (neutral/Valentinos contact)

### Стадии войны:

**Stage 1: Спор о территории** (`quest-factionwar-gangs-01-turf-dispute`)
- Выбор банды: Valentinos OR Maelstrom
- Уличный бой за контроль квартала
- 6 врагов, AC 12-14
- Rewards: 600 XP, +20 gang reputation

**Stage 2: Оружейная сделка** (`quest-factionwar-gangs-02-weapons-deal`)
- Перехват оружейной сделки противника
- 8 врагов, loot weapons cache
- Rewards: 700 XP, +25 gang reputation, weapons

**Stage 3: Дуэль лидеров** (`quest-factionwar-gangs-03-gang-leader`)
- Вызов лидеру противоборствующей банды
- 1v1 duel + aftermath (10 врагов)
- Rewards: 900 XP, +35 gang reputation, gang trophy

**Stage 4: Кульминация gang war** (`quest-factionwar-gangs-04-gang-war-climax`)
- Массовое столкновение на улицах
- 15 врагов, destructible environment
- Victory = контроль над Watson
- Rewards: 1050 XP, +50 gang reputation

### Исход войны:
- **Valentinos Victory:** Valentinos контролируют Watson, культурный центр, honour code влияет на квесты
- **Maelstrom Victory:** Maelstrom контролируют Watson, tech-scavenging operations, chaos increases
- **Мировое влияние:** Gang territories shift, NPC vendors change, street safety decreases/increases

---

## 4. Система выбора стороны

### При выборе стороны:
- **Immediate:** +20 reputation с выбранной фракцией, −20 с противником
- **Quest access:** Unlocks faction-specific quests, locks opposing faction quests
- **NPC relationships:** Некоторые NPCs friendly/hostile зависит от выбора
- **Loot:** Faction-specific gear becomes available

### Neutrality Option:
- Игрок может остаться neutral (не выбирать сторону)
- Не получает faction war quests
- Может торговать с обеими сторонами
- No war trophy, no title
- Может присоединиться позже (до Stage 3)

### Switching Sides:
- До Stage 3: можно переметнуться (penalty: −40 reputation обе стороны, 24h cooldown)
- После Stage 3: locked in, нельзя изменить

## 5. Мировое влияние (World Impact)

### Территориальный контроль:
Победившая фракция контролирует территорию:
- Флаги/баннеры меняются визуально
- NPC vendors сменяются на faction-aligned
- Patrol guards меняются
- Quest givers изменяются
- Prices на товары меняются (±10-20%)

### Динамические события:
- Random encounters зависят от контроля территории
- Faction war events могут re-trigger (периодические конфликты)
- Players могут защищать OR атаковать контролируемые территории

### Reputation cascade:
- Arasaka Victory → Militech allies lose reputation with player
- Gang victory → Opposing gang становятся hostile

## 6. API Mapping

### Faction War Endpoints:
- POST `/api/v1/faction-war/choose-side` (warId, factionId, playerId)
- GET `/api/v1/faction-war/status` (warId) → returns current stage, winning faction, player participation
- POST `/api/v1/faction-war/stage-complete` (warId, stageId, playerId)
- GET `/api/v1/faction-war/world-state` → returns controlled territories, faction influence
- POST `/api/v1/faction-war/switch-side` (warId, newFactionId, playerId) → penalty applied

### Data Models:

```json
{
  "factionWar": {
    "id": "faction-war-arasaka-militech",
    "name": "Arasaka vs Militech",
    "currentStage": 3,
    "winningFaction": "arasaka",
    "participants": [
      { "playerId": "player-123", "faction": "arasaka", "contribution": 450 }
    ],
    "worldImpact": {
      "controlledTerritories": ["loc-downtown-001"],
      "npcChanges": ["npc-sarah-miller-hostile"],
      "marketPriceModifiers": { "weapons": 1.15, "armor": 0.90 }
    }
  }
}
```

## 7. UX/UI для Faction Wars

### Faction Choice Screen:
```
┌─────────────────────────────────────────┐
│ FACTION WAR: Arasaka vs Militech       │
│                                         │
│ [ARASAKA]                              │
│ Корпорация. Порядок. Технологии.       │
│ Allies: Elizabeth Chen, NetWatch       │
│ Rewards: High-tech gear, corpo access  │
│                                         │
│ [MILITECH]                             │
│ Военные. Сила. Свобода.                │
│ Allies: Sarah Miller, Independent      │
│ Rewards: Military gear, tactical bonus │
│                                         │
│ [NEUTRAL]                              │
│ Не участвовать в войне                 │
└─────────────────────────────────────────┘
```

### War Progress Tracker:
```
┌─────────────────────────────────────────┐
│ FACTION WAR: Arasaka vs Militech       │
│ Ваша сторона: Arasaka                  │
│ Прогресс: Стадия 3/5                   │
│                                         │
│ ✓ Пограничная стычка                   │
│ ✓ Рейд на склады                       │
│ ○ Assassination [АКТИВНО]              │
│ ○ Контроль территории                  │
│ ○ Решающая битва                       │
│                                         │
│ Репутация Arasaka: 90 (+20/quest)      │
│ Репутация Militech: 10 (противник)     │
└─────────────────────────────────────────┘
```

## 8. Баланс и Design

**Принципы:**
1. Обе стороны должны быть equally attractive (rewards, lore, NPCs)
2. Выбор должен быть meaningful (реальное влияние на мир)
3. Нельзя «выиграть всё» (choosing locks content)
4. Reputation penalties значительны (switching sides costly)

**Difficulty Scaling:**
- Stage 1-2: Medium (8-10 врагов)
- Stage 3-4: Hard (12-15 врагов, bosses)
- Stage 5: Very Hard (20+ врагов, commander boss)

## 9. Future Expansions

**Potential additions:**
- Faction War #3: NCPD vs Gangs
- Faction War #4: Nomads vs Corporations
- Three-way wars (3 фракции)
- Dynamic faction wars (player-initiated)
- Seasonal wars (recurring events)

## 10. История изменений
- v1.0.0 (2025-11-06) — системный обзор Faction Wars (2 войны, 9 квестов).
