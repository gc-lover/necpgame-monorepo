---
**Статус:** archived  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  
**Приоритет:** высокий

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 23:59
**api-readiness-notes:** D&D-параметры утратили актуальность. Сценарий хранится исключительно как архив.

> ⚠️ Shooter pivot: требуется новая версия под shooter-механику и UE5-ориентированную реализацию.
---

---

- **Status:** created
- **Last Updated:** 2025-11-07 06:45
---

# [АРХИВ] Рейд: Штурм башни корпорации

**Статус:** archived  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-05  
**Последнее обновление:** 2025-11-05  
**Приоритет:** высокий

**api-readiness:** not-applicable  
**api-readiness-check-date:** 2025-11-09 23:59
**api-readiness-notes:** D&D-параметры утратили актуальность. Сценарий хранится исключительно как архив.

> ⚠️ Shooter pivot: требуется новая версия под shooter-механику и UE5-ориентированную реализацию.
---

---

- **Status:** created
- **Last Updated:** 2025-11-07 06:45
---

# ENDGAME RAID: Corpo Tower Assault

## 1. Синопсис
Штурм корпоративной башни Arasaka/Militech. 10-15 игроков. 3 фазы: infiltration, combat floors, CEO boss fight. Stealth опции, heavy combat, dynamic encounter scaling. Требует coordination, gear level 12+, class diversity.

- ID: `raid-corpo-tower-assault`
- Тип: `endgame-raid`
- Мин. игроков: 10
- Макс. игроков: 15
- Уровень: 12+
- Длительность: 45-75 минут
- Эпоха: 2060-2077

## 2. Требования входа
- Уровень: 12+
- Gear Score: 300+
- Завершённые квесты: `quest-factionwar-am-05-decisive-battle` OR `quest-class-corpo-2073-border-war`
- Выбор стороны: Arasaka OR Militech (определяет противника)
- Специальный допуск: `item-tower-access-card`

## 3. Роли в рейде
**Обязательные роли:**
- **Tank (2):** Aggro management, frontline держать
- **DPS (5-7):** Priority targets, sustained damage
- **Healer/Support (2-3):** Healing, rez, buffs
- **Stealth Specialist (1-2):** Disable security, silent kills, reconnaissance
- **Hacker (1-2):** Door bypass, turret control, alarm disabling

## 4. ФАЗА 1: Infiltration (Ground → Floor 50)

### Цели:
1) Проникнуть в башню (stealth OR frontal assault)
2) Достичь Floor 50 (elevator OR stairs combat)
3) Disable security checkpoints (5 checkpoints)

### Варианты approach:

**STEALTH PATH:**
- Stealth Specialists ведут группу
- Disable cameras, guards (silent takedowns)
- Skip 80% combat encounters
- Bonus: +1000 XP, shorter time, surprise advantage Phase 2

**COMBAT PATH:**
- Frontal assault через main entrance
- Heavy combat каждый 10 floors (5 encounters × 10-15 guards)
- No surprise bonus, but more loot drops
- Громче = больше алармов = harder Phase 2

### Враги (Combat Path):
- **Corpo Guard** (AC 16, HP 100, d8+3 SMG damage)
- **Corpo Elite** (AC 18, HP 150, d10+4 assault rifle damage)
- **Security Turret** (AC 15, HP 80, d12+5 automated fire, hackable)

### Механики:
- **Alarm System:** 3 alarm levels (green→yellow→red)
  - Green: patrol guards only
  - Yellow: reinforcements every 60 sec
  - Red: lockdown, turrets active, +2 AC to all enemies
- **Elevator OR Stairs:** 
  - Elevator: faster, но trapped (hacker disarms OR combat encounter)
  - Stairs: slower, но controllable encounters
- **Security Checkpoints:** Must disable 5 (hack OR destroy, hack = silent)

### D&D параметры:
- Stealth checks: DEX 18 (avoid detection)
- Hacking checks: INT 17 (bypass systems)
- AC врагов: 15-18
- Damage: d8+3 to d12+5

### Награды фазы 1:
- Checkpoint save at Floor 50
- Stealth bonus (+1000 XP, surprise Phase 2) OR Combat loot (random gear drops)

## 5. ФАЗА 2: Combat Floors (Floor 50 → Floor 100)

### Цели:
1) Fight through 5 combat floors (50, 60, 70, 80, 90)
2) Defeat 3 Lieutenant Bosses (floors 60, 75, 90)
3) Reach Floor 100 (CEO penthouse)

### Combat Floors:
Каждые 10 floors = combat arena with 15-20 enemies + environmental hazards

**Floor 50:** Server Room
- 15× Corpo Guards
- Hazard: EMP pulses (disable cyberware 5 sec, d6 damage)

**Floor 60:** LIEUTENANT ALPHA (Security Chief)
- AC 20, HP 400
- Abilities: Summon guards (5 every 45 sec), Shield Dome (immune 10 sec, must destroy generators)
- Adds: 10× Corpo Elite

**Floor 70:** Executive Lounge
- 20× Corpo Elite + 2× Mechs
- Hazard: Fire suppression system (freezes random zones, slow movement)

**Floor 75:** LIEUTENANT BETA (Head of Operations)
- AC 21, HP 450
- Abilities: Orbital Strike (marks players, AoE d20 damage after 5 sec), Tactical Drones (3 drones, must destroy)
- Adds: 12× Corpo Elite

**Floor 80:** R&D Lab
- 15× Corpo Elite + 3× Security Turrets + 1× Prototype Mech
- Hazard: Chemical leaks (poison zones, d4 damage/sec)

**Floor 90:** LIEUTENANT GAMMA (Chief of Security)
- AC 22, HP 500
- Abilities: Adaptive Armor (reduces damage from repeated attack types), Flashbang Barrage (AoE stun 3 sec)
- Adds: 15× Corpo Elite + 5× Security Turrets

### Механики:
- **Reinforcements:** If combat > 5 min на floor, +5 guards spawn
- **Environmental Hazards:** Each floor unique, must adapt tactics
- **Lieutenant Mechanics:** Must coordinate focus fire, manage adds
- **Escape Routes:** Hidden shortcuts (find via Perception 19 checks, skip 1 floor)

### D&D параметры:
- AC врагов: 16-22
- HP: 100-500
- Saves: DEX 18 (avoid hazards), CON 17 (resist poison/EMP)
- Damage: d8+3 to d20

### Награды фазы 2:
- Checkpoint save at Floor 100
- +2000 XP
- Lieutenant loot (rare gear drops, crafting materials)

## 6. ФАЗА 3: CEO Boss Fight (Floor 100 Penthouse)

### Босс: **CORPO CEO «TAKASHI ARASAKA» / «GENERAL GRANT» (зависит от выбора стороны)**

- AC: 23
- HP: 6000 (3 phases: 2500/2000/1500)
- Damage: d20+12 (personal weapons + tech)
- Иммунитеты: Poison, stun (first 2 phases)
- Уязвимости: Coordinated burst damage, hacking (phase 3)

### Arena:
- Penthouse with destructible cover
- 360° windows (can shatter, instant death if fall)
- 4 weapon caches (refill ammo, one-time use)
- Central hologram console (hackable for boss debuff)

### Механики босса:

**PHASE 1 (100%-60% HP):**
- **Executive Shield:** Immune to damage первые 30 sec (must hack console OR destroy 4 shield generators)
- **Corpo Elite Squad:** Summons 10 guards every 45 sec (must control adds)
- **Precision Strike:** Targets random player, d20+12 damage (must dodge, DEX 19)
- **Cover Destruction:** Destroys 1 cover piece every 30 sec (adapting positioning required)

**PHASE 2 (60%-25% HP):**
- **Tactical Retreat:** Босс activates jetpack, flies around arena (ranged DPS priority)
- **Aerial Bombardment:** Drops grenades (AoE d12 damage, 5m radius)
- **Reinforcement Call:** 15 Corpo Elite + 3 Mechs spawn (biggest challenge)
- **Window Breach:** Shatters windows (creates death zones, reduces safe area)

**PHASE 3 (25%-0% HP):**
- **Desperation Mode:** +50% damage, +3 AC, immune to CC removed
- **Penthouse Collapse:** Arena starts collapsing (safe area shrinks every 20 sec)
- **Final Stand:** At 10% HP, activates self-destruct (45 sec to kill OR wipe)
- **Hacking Vulnerability:** Console can be hacked (INT 20) for −5 AC to boss

### Координация:
- **Tanks:** Manage Corpo Elite adds, not boss (boss ranged, untankable Phase 2)
- **DPS:** Priority: Shield generators → Adds → Boss
- **Healers:** Manage heavy AoE damage, rez players near windows
- **Hackers:** Phase 1 shield, Phase 3 debuff boss

### Wipe Mechanics:
- 6+ players dead simultaneously
- Fall through window
- Self-destruct timer expires
- Arena fully collapses (phase 3, 120 sec limit)

## 7. Награды финала

**Guaranteed:**
- 3500 XP
- 6000 eddies
- `item-corpo-executive-badge` (endgame trophy)
- +100 репутация Arasaka OR Militech (зависит от выбора)
- Title: «Tower Conqueror»

**Random Loot Table (1-3 items per player):**
- Legendary Weapon (Corpo-branded)
- Legendary Armor (Executive Suit, high AC + COOL bonus)
- Legendary Cyberware (Neural Implant, +INT/REF)
- 50× `item-corpo-data-fragment`
- Unique cosmetics (CEO office decorations)

**First Clear Bonus:**
- Achievement: «First Tower Clear»
- +1500 XP
- Exclusive vehicle (Corpo limousine)

## 8. Сложность и вариации

**Normal Mode:** Описано выше

**Hard Mode:**
- +40% HP на врагов
- +3 AC
- Reinforcements −50% cooldown
- Lieutenants spawn +50% adds
- **Награды:** × 1.5 XP/money, +1 legendary

**Nightmare Mode:**
- +60% HP
- +5 AC
- Alarm always red (max difficulty)
- Lieutenants +100% adds
- CEO boss enrage at 50% HP
- **Награды:** × 2.0 XP/money, +2 legendaries, exclusive title

## 9. API Mapping

### Endpoints:
- POST `/api/v1/raids/corpo-tower-assault/start`
- POST `/api/v1/raids/corpo-tower-assault/phase-complete`
- GET `/api/v1/raids/corpo-tower-assault/status`
- POST `/api/v1/raids/corpo-tower-assault/complete`
- POST `/api/v1/combat/raid-action`

### WebSocket:
- `wss://api.necp.game/v1/gameplay/raids/{raidId}` — live updates

## 10. Баланс

**Success Rate:**
- Normal: 50-70%
- Hard: 25-35%
- Nightmare: 10-15%

**Time:**
- Normal: 45-60 min
- Hard: 60-75 min
- Nightmare: 75-90 min

**Gear Check:**
- Min: 300
- Recommended: 350+
- Optimal: 400+

## 11. История изменений
- v1.0.0 (2025-11-06) — первичная спецификация raid.
