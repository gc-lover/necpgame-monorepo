---
**Статус:** review  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-06  
**Приоритет:** высокий  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-06 21:55
**api-readiness-notes:** Системный обзор всех origin stories. Backstories, perks, branching paths для всех классов.
---

---

- **Status:** created
- **Last Updated:** 2025-11-07 05:00
---

# ORIGIN STORIES SYSTEM: Полный обзор

## 1. Концепция системы

**Цель:** Дать каждому классу уникальную предысторию, которая влияет на геймплей, репутацию, доступные квесты.

**Механика:**
- При создании персонажа игрок выбирает класс
- Автоматически активируется origin story (3 квеста)
- Квесты уровня 1-3 (tutorial + backstory)
- Завершение даёт permanent perks и title
- Branching choices влияют на репутацию и доступные фракции

## 2. Классы и их origin stories

### SOLO — Military Veteran
**Backstory:** Бывший военный/corpo-security после DataKrash
**Quests:** 
- Quest 1: Первый контракт (прибытие в NC, встреча с Сарой)
- Quest 2: Построение репутации (серия контрактов)
- Quest 3: Соперник (дуэль, доказать себя)

**Origin Perks:**
- +2 TACTICS checks
- +1 AC permanently
- +20 NCPD reputation
- Title: «Solo of Night City»

**Branching:**
- Corpo-security path → NCPD contacts, corpo quests access
- Дезертир path → street cred, underground quests access

---

### NETRUNNER — Tech Genius
**Backstory:** Tech-genius из стрит-культуры, самоучка
**Quests:**
- Quest 1: Первое погружение (обучение netrunning, первый hack)
- Quest 2: Кража данных (привлечение внимания NetWatch)
- Quest 3: Предложение NetWatch (выбор: присоединиться OR independent)

**Origin Perks:**
- +2 INT checks (netrunning)
- Starting cyberdeck: `item-cyberdeck-basic`
- +25 NetWatch reputation (если присоединился)
- Title: «Ghost in the Net» OR «Independent Runner»

**Branching:**
- Join NetWatch → NetWatch quests, corpo-netrunning access
- Stay Independent → underground hacking, black market access

---

### FIXER — Badlands Kid
**Backstory:** Streetwise kid из Badlands, прибывает в NC
**Quests:**
- Quest 1: Уличные связи (прибытие, встреча с Марко)
- Quest 2: Построение сети (завербовать контакты)
- Quest 3: Первая империя (территория, конкуренты)

**Origin Perks:**
- +2 STREETWISE checks
- +2 COOL checks (negotiation)
- Starting contacts network (3 NPCs)
- Title: «Fixer in the Making»

**Branching:**
- Честная торговля → Independent reputation, trade access
- Чёрный рынок → underground, criminal contacts

---

### NOMAD — Clan Outcast
**Backstory:** Покинул клан номадов из-за конфликта
**Quests:**
- Quest 1: Отъезд из клана (конфликт, путь в NC)
- Quest 2: Прибытие в Night City (адаптация к городу)
- Quest 3: Примирение с кланом (вернуться OR создать свой)

**Origin Perks:**
- +2 SURVIVAL checks
- +2 TECH checks (vehicle repair)
- Starting vehicle: `item-nomad-vehicle`
- Title: «Road Warrior»

**Branching:**
- Вернуться в клан → +40 Nomads reputation, clan quests access
- Создать свой клан → Independent, leadership quests

---

### TECHIE — Tech Scavenger
**Backstory:** Tech-scavenger, выживал через ремонт и крафт
**Quests:**
- Quest 1: Первый ремонт (починка tech для клиента)
- Quest 2: Salvage операция (сбор компонентов)
- Quest 3: Создание первого устройства (крафт unique item)

**Origin Perks:**
- +3 TECH checks
- Crafting unlock (раньше других классов)
- Starting components: 10× `item-components-basic`
- Title: «Tech Savant»

**Branching:**
- Легальный ремонт → corpo contracts, legal tech access
- Scavenger life → black market, illegal mods

---

### ROCKERBOY — Street Performer
**Backstory:** Уличный музыкант, хочет изменить мир через музыку
**Quests:**
- Quest 1: Первое выступление (уличный концерт)
- Quest 2: Создание группы (recruit band members)
- Quest 3: Первый протест (концерт против корпораций)

**Origin Perks:**
- +3 PERFORMANCE checks
- +2 COOL checks (charisma)
- Starting fans: 100 followers
- Title: «Voice of the Streets»

**Branching:**
- Протест-музыка → anti-corpo, rebellion quests
- Коммерческая музыка → corpo sponsorship, fame

---

### CORPO — Corporate Climber
**Backstory:** Начинающий корпорат, стремится к власти
**Quests:**
- Quest 1: Тест на лояльность (доказать преданность)
- Quest 2: Первая интрига (саботировать конкурента)
- Quest 3: Повышение (получить promotion OR предать корпу)

**Origin Perks:**
- +2 INVESTIGATION checks
- +2 COOL checks (corporate composure)
- Starting Arasaka/Militech reputation +20
- Title: «Corporate Asset»

**Branching:**
- Лояльность → corpo quests, высокие награды, ограничения
- Предательство → independent, freedom, −corpo reputation

## 3. Общие механики origin stories

### Progression:
- Quest 1: Introduction (уровень 1, simple mechanics)
- Quest 2: Growth (уровень 2, skill checks introduced)
- Quest 3: Defining Choice (уровень 3, branching, finale)

### Rewards:
- XP: 200 → 350 → 500 (total 1050 XP)
- Money: 300 → 450 → 600 (total 1350 eddies)
- Items: Class-specific starter gear
- Reputation: Зависит от choices
- **Permanent Perks:** После завершения всех 3 квестов

### Branching Impact:
- Reputation с фракциями (±10 to ±40)
- Доступ к специфическим quest lines
- Starting contacts network
- Dialogue options в дальнейших квестах
- Cosmetic changes (titles, starting gear appearance)

## 4. API Mapping

### Origin System Endpoints:
- POST `/api/v1/character/create` (classId, backstoryChoice) → starts origin quest 1
- GET `/api/v1/origin/available-quests` (playerId) → returns active origin quests
- POST `/api/v1/origin/branching-choice` (playerId, choiceId) → records choice, affects reputation
- GET `/api/v1/origin/perks` (playerId) → returns unlocked origin perks
- POST `/api/v1/origin/complete` (playerId) → finalizes origin, applies permanent perks

### Data Models:

```json
{
  "originStory": {
    "playerId": "player-123",
    "class": "solo",
    "backstory": "military-veteran",
    "completedQuests": ["quest-origin-solo-01", "quest-origin-solo-02", "quest-origin-solo-03"],
    "branchingChoices": {
      "quest-origin-solo-01": "corpo-security-fired"
    },
    "perks": [
      { "id": "perk-tactics-bonus", "value": 2 },
      { "id": "perk-ac-bonus", "value": 1 }
    ],
    "title": "Solo of Night City",
    "completed": true
  }
}
```

## 5. UX/UI для Origin Stories

### Character Creation Screen:
```
┌─────────────────────────────────────────┐
│ ВЫБЕРИ КЛАСС                           │
│                                         │
│ [SOLO] Military Veteran                │
│ Бывший corpo-security. Дисциплина,     │
│ combat proficiency. +TACTICS, +AC      │
│                                         │
│ [NETRUNNER] Tech Genius                │
│ Самоучка-хакер. Интеллект, netrunning. │
│ +INT, starting cyberdeck               │
│                                         │
│ [FIXER] Badlands Kid                   │
│ Уличная смекалка. Сделки, связи.      │
│ +STREETWISE, +COOL                     │
│                                         │
│ ... (other classes)                    │
└─────────────────────────────────────────┘
```

### Origin Quest Tracker:
```
┌─────────────────────────────────────────┐
│ ORIGIN: Solo — Military Veteran        │
│ Прогресс: 2/3 квестов                  │
│                                         │
│ ✓ Первый контракт                      │
│ ✓ Построение репутации                 │
│ ○ Соперник [АКТИВНО]                   │
│                                         │
│ Perks при завершении:                  │
│ • +2 TACTICS, +1 AC                    │
│ • Title: Solo of Night City            │
└─────────────────────────────────────────┘
```

## 6. Баланс и Design Principles

**Принципы:**
1. Origin stories должны быть равноценны по наградам
2. Branching choices должны быть meaningful (влияние на репутацию, доступ к квестам)
3. Perks должны быть class-relevant, но не overpowered
4. Tutorial elements интегрированы естественно (combat, skills, dialogue)

**Баланс rewards:**
- Все origins: 1050 XP, ~1350 eddies
- Perks: +2-3 к ключевым навыкам класса
- Reputation: ±20-40 в зависимости от choices

## 7. Future Expansions

**Potential additions:**
- Дополнительные backstory варианты (2-3 на класс)
- Extended origin quests (optional Quest 4-5 для deep lore)
- Origin-specific companions (NPC allies из backstory)
- Flashback missions (играть события из прошлого)

## 8. История изменений
- v1.0.0 (2025-11-06) — системный обзор origin stories (7 классов).
