# [АРХИВ] Рейд: Экспедиция за Блэкволл

**Статус:** archived  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  
**Приоритет:** высокий

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 23:59
**api-readiness-notes:** Включает D&D проверки и устаревшие механики. Хранится только для истории.

> ⚠️ Shooter pivot: рейд потребуется переписать под shooter-механику, используя новые боевые петли.

---

- **Status:** created
- **Last Updated:** 2025-11-07 06:40
---

# ENDGAME RAID: Blackwall Expedition

## 1. Синопсис
Высочайший уровень сложности. Экспедиция за Blackwall для добычи артефактов DataKrash эпохи. 10-15 игроков. 3 фазы. AI-сущности, реальностные аномалии, безумие. Требует координации, gear level 12+, знания механик.

- ID: `raid-blackwall-expedition`
- Тип: `endgame-raid`
- Мин. игроков: 10
- Макс. игроков: 15
- Уровень: 12+
- Длительность: 60-90 минут
- Эпоха: 2077-2093

## 2. Требования входа
- Уровень: 12+
- Gear Score: 300+
- Завершённые квесты: `quest-side-2088-archive-expedition`, `quest-main-2093-simulation-reveal`
- Репутация NetWatch: 50+
- Специальный допуск: `item-blackwall-access-token`

## 3. Роли в рейде
**Обязательные роли:**
- **Tank (2):** Высокий AC (18+), HP pool, aggro management
- **DPS (5-7):** Burst damage, sustained damage, priority target focus
- **Healer/Support (2-3):** Healing, buffs, debuff removal, resurrection
- **Netrunner (2-3):** AI hacking, firewall management, breach control

## 4. ФАЗА 1: Пересечение Blackwall

### Цели:
1) Активировать 5 breach points одновременно
2) Выжить волны AI прокси (3 волны × 10-15 врагов)
3) Защитить netrunners, поддерживающих breach

### Враги:
- **AI Combat Proxy** (AC 17, HP 150, d10+5 neural damage)
- **Firewall Guardian** (AC 19, HP 200, d12+6 energy damage, immune to hacking)
- **Reality Distortion Node** (AC 15, HP 100, causes insanity, teleportation)

### Механики:
- **Simultaneous Breach:** Все 5 breach points должны быть активированы в течение 10 секунд
- **AI Waves:** 3 волны, каждая усиливается (+2 AC, +50 HP, +d4 damage)
- **Netrunner Protection:** Netrunners уязвимы при breach, требуют защиты
- **Insanity Meter:** Набор 100 insanity = временное безумие (случайные действия, 30 сек)

### D&D параметры:
- Инициатива: d20 + REF (priority system для координации)
- AC врагов: 15-19
- Saves: WIS 18 (resist insanity), INT 17 (understand mechanics)
- Damage: d10+5 to d12+6

### Награды фазы 1:
- Checkpoint save
- Temporary buffs (+2 AC, +d4 damage for phase 2)

## 5. ФАЗА 2: Архивы за заслоном

### Цели:
1) Навигация через 3 archive sectors
2) Сбор 10 data fragments (distributed across sectors)
3) Defeat Archive Keepers (3 mini-bosses)
4) Avoid reality collapses (environmental hazards)

### Враги:
- **Archive Keeper** (AC 20, HP 300, d12+8 reality damage, summons AI adds)
- **Data Corruption Entity** (AC 16, HP 120, corrupts inventory items, drains resources)
- **Memory Echo** (AC 18, HP 180, copies player abilities, adapts to tactics)

### Механики:
- **Reality Collapse:** Случайные зоны коллапсируют (instant death if caught, 15 sec warning)
- **Data Fragment Collection:** 10 fragments needed, каждый охраняется 5-10 врагами
- **Archive Keepers:** 3 mini-bosses, каждый с unique mechanic:
  - **Keeper Alpha:** Summons AI adds every 30 sec (must burn down adds quickly)
  - **Keeper Beta:** Reality shields (immune until shield broken by specific skill checks)
  - **Keeper Gamma:** Adaptive AI (learns player tactics, counters repeated actions)
- **Resource Drain:** Corruption entities drain ammo, medical supplies (inventory management critical)

### D&D параметры:
- AC врагов: 16-20
- HP: 120-300
- Saves: CON 19 (resist corruption), WIS 18 (resist reality damage)
- Damage: d12+8 to d20 (instant kill mechanics)

### Награды фазы 2:
- Checkpoint save
- +10 data fragments (currency for endgame gear)
- Temporary immunity to corruption (phase 3)

## 6. ФАЗА 3: Сердце архивов — ФИНАЛЬНЫЙ БОСС

### Босс: **PRIMORDIAL AI «ALPHA-OMEGA»**
- AC: 22
- HP: 5000 (распределено на 3 фазы босса: 2000/1500/1500)
- Damage: d20+10 (reality-ending attacks)
- Иммунитеты: Physical damage (requires netrunning), fire, cold
- Уязвимости: Neural hacking, coordinated burst damage

### Механики босса:

**PHASE 1 (100%-40% HP):**
- **Reality Fracture:** Каждые 60 sec, random 5 игроков teleport в pocket dimension (must escape in 30 sec or die)
- **AI Swarm:** Summons 20 AI prокси (must be controlled by netrunners, otherwise overwhelm)
- **Neural Pulse:** AoE d20 damage, stun 3 sec (must spread out)

**PHASE 2 (40%-20% HP):**
- **Adaptive Evolution:** Босс становится immune к последним 3 использованным abilities (tactics diversity required)
- **Reality Rewrite:** Changes terrain, removes cover, adds hazards (environmental adaptation)
- **Corruption Wave:** All players gain 50 insanity, corruption stacks (healers must cleanse)

**PHASE 3 (20%-0% HP):**
- **Desperate Measures:** Босс активирует enrage (+50% damage, +5 AC)
- **Final Reality Collapse:** Arena shrinks every 15 sec (players must stay in safe zone)
- **Self-Destruct:** At 5% HP, 60 sec timer to kill boss or wipe (DPS check)

### Координация:
- **Tanks:** Aggro management на AI adds, не босса (босс не tankable)
- **DPS:** Priority: AI adds → Boss weak points (appear every 45 sec)
- **Healers:** Cleanse corruption, resurrect fallen, manage insanity
- **Netrunners:** Hack boss shields, control AI swarms, prevent Reality Fractures

### Wipe Mechanics (instant fail):
- 5+ players dead simultaneously
- AI Swarm reaches 30+ units
- Reality Collapse engulfs entire arena
- Timer expires on Self-Destruct

## 7. Награды финала

**Guaranteed:**
- 3000 XP
- 5000 eddies
- `item-primordial-ai-core` (endgame crafting material)
- +100 репутация NetWatch
- Title: «Beyond the Blackwall»

**Random Loot Table (1-3 items per player):**
- Legendary Cyberdeck (NetWatch Prototype)
- Legendary Weapon (AI-Infused)
- Legendary Armor (Reality-Resistant)
- 50× `item-blackwall-data-fragment`
- Unique cosmetics (AI-themed skins)

**First Clear Bonus:**
- Achievement: «First Expedition»
- +1000 XP
- Exclusive mount/vehicle skin

## 8. Сложность и вариации

**Normal Mode:** Описано выше
**Hard Mode:**
- +30% HP на всех врагов
- +2 AC на всех врагов
- Сокращённые timers (−30%)
- Дополнительные wipe mechanics
- **Награды:** × 1.5 XP/money, +1 guaranteed legendary

**Nightmare Mode:**
- +50% HP
- +4 AC
- Timers −50%
- Permadeath (1 life per player, no resurrections)
- **Награды:** × 2.0 XP/money, +2 guaranteed legendaries, exclusive title

## 9. API Mapping

### Endpoints:
- POST `/api/v1/raids/blackwall-expedition/start`
  - Parameters: `playerIds[]`, `difficulty`, `leaderToken`
- POST `/api/v1/raids/blackwall-expedition/phase-complete`
  - Parameters: `raidId`, `phase`, `playerStates[]`
- GET `/api/v1/raids/blackwall-expedition/status`
  - Returns: `currentPhase`, `bossHP`, `playersAlive`, `timers`
- POST `/api/v1/raids/blackwall-expedition/complete`
  - Parameters: `raidId`, `victory`, `finalStats`
- POST `/api/v1/combat/raid-action`
  - Parameters: `raidId`, `playerId`, `action`, `target`

### WebSocket для real-time:
- `wss://api.necp.game/v1/gameplay/raids/{raidId}` — live boss HP, timers, player states

## 10. Логи и Тесты

**Логи:**
- Phase transitions
- Boss mechanics triggers
- Player deaths/resurrections
- Wipe conditions
- Loot drops

**Тесты:**
- 10 players успешно завершают Normal
- Hard mode clear (15 players)
- Wipe mechanics trigger correctly
- Simultaneous Breach timing
- Boss phase transitions at correct HP thresholds
- Loot distribution fair

## 11. Баланс и Tuning

**Ожидаемый success rate:**
- Normal: 40-60% (skilled groups)
- Hard: 20-30%
- Nightmare: 5-10%

**Ожидаемое время:**
- Normal: 60-75 min
- Hard: 75-90 min
- Nightmare: 90-120 min

**Gear check:**
- Минимум: Gear Score 300
- Рекомендуемо: Gear Score 350+
- Оптимально: Gear Score 400+

## 12. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация raid.
