# Каталог NPC для Найма

**Версия:** 2.0.0  
**Дата:** 2025-11-06  
**Статус:** Ready for API  
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 11:21  
**api-readiness-notes:** Перепроверено 2025-11-09 11:21: каталог NPC содержит структурированные данные и готов к генерации API.

---

- **Status:** created
- **Last Updated:** 2025-11-07 01:10
---

---

## 📋 СТРУКТУРА NPC

```yaml
npc:
  id: "unique_id"
  name: "Имя"
  role: "Специализация"
  tier: 1-5
  rarity: "Common|Uncommon|Rare|Epic|Legendary"
  
  stats:
    level: 1-50
    skills: {skill_name: rank}
    efficiency: 0.5-2.0 (multiplier)
  
  hire_cost:
    initial: 1000 # единоразовый
    daily: 100 # зарплата
    rep_required: 0 # минимальная репутация
  
  location: "Night City - Watson"
  faction: "6th Street"
  
  personality:
    traits: ["Loyal", "Aggressive"]
    likes: ["Money", "Combat"]
    dislikes: ["Corpo"]
  
  special_abilities: ["ability_id"]
  unique: false
```

---

## 💼 TIER 1: COMMON NPCs

### 1. **Marcus "Trigger" Johnson**

**Role:** Bodyguard  
**Tier:** 1  
**Rarity:** Common

**Stats:**
- Level: 5-10
- Combat: 2/5
- Loyalty: 3/5
- Cost: 500 €$ initial, 50 €$/day

**Skills:**
- Gunplay: Trained (R2)
- Defense: Novice (R1)
- Perception: Novice (R1)

**Location:** Watson, street corners  
**Faction:** None (Independent)

**Personality:**
- Traits: Reliable, Simple
- Likes: Easy money, beer
- Dislikes: Complex plans

**Use Case:**
- Basic protection
- Simple escort missions
- Deterrent (low-level threats)

**Availability:** Always available (10+ in city)

---

### 2. **"Fastfingers" Rodriguez**

**Role:** Street Vendor  
**Tier:** 1  
**Rarity:** Common

**Stats:**
- Level: 3-8
- Trading: 2/5
- Charisma: 2/5
- Cost: 300 €$ initial, 30 €$/day

**Skills:**
- Trading: Trained (R2)
- Persuasion: Novice (R1)

**Services:**
- Sells: Common items, ammo
- Generates: 100-300 €$/day profit
- Upkeep: -50 €$/day (stock)

**Location:** Any market  
**Net Profit:** +50-250 €$/day

---

### 3. **"Doc" Williams**

**Role:** Street Medic  
**Tier:** 1  
**Rarity:** Common

**Stats:**
- Level: 5-10
- Medicine: 3/5
- Cost: 800 €$ initial, 80 €$/day

**Skills:**
- Medicine: Trained (R2)
- Tech: Novice (R1)

**Services:**
- Heal: 100 HP for 50 €$ (vs 150 €$ at clinic)
- Basic implant install: -20% cost
- Field medic in missions

**Saves Money:** ~200-500 €$/week (if active player)

---

## 💎 TIER 2: UNCOMMON NPCs

### 4. **Mikhail "Bulldog" Volkov**

**Role:** Heavy Merc  
**Tier:** 2  
**Rarity:** Uncommon

**Stats:**
- Level: 15-20
- Combat: 4/5
- Loyalty: 4/5
- Cost: 5,000 €$ initial, 300 €$/day

**Skills:**
- Heavy Weapons: Expert (R3)
- Defense: Expert (R3)
- Intimidation: Trained (R2)

**Equipment:**
- LMG (personal)
- Heavy armor
- Grenade launcher

**Special Ability:**
- **Suppressive Fire** — Pin down enemies (tactical advantage)

**Location:** Nomad camps, mercenary bars  
**Faction:** Aldecaldos (friendly)

**Use Case:**
- Difficult missions
- Boss fights backup
- Defense contracts

**Availability:** 3-5 in city at any time

---

### 5. **"Cipher" Tanaka**

**Role:** Netrunner  
**Tier:** 2  
**Rarity:** Uncommon

**Stats:**
- Level: 12-18
- Hacking: 4/5
- Intelligence: 4/5
- Cost: 8,000 €$ initial, 500 €$/day

**Skills:**
- Hacking: Expert (R3)
- Cyberspace: Expert (R3)
- Tech: Trained (R2)

**Services:**
- Hack: Medium security systems
- Intel gathering: 70% success
- Cyberdeck maintenance: Free

**Special Ability:**
- **Remote Breach** — Hack from distance (1km)

**Location:** Netrunner dens, Voodoo Boys territory  
**Reputation Required:** Netrunners Guild +20

**Use Case:**
- Heist missions
- Corporate espionage
- Data theft

---

### 6. **Isabella "Smooth" Garcia**

**Role:** Fixer / Negotiator  
**Tier:** 2  
**Rarity:** Uncommon

**Stats:**
- Level: 10-15
- Charisma: 5/5
- Trading: 4/5
- Cost: 4,000 €$ initial, 200 €$/day

**Skills:**
- Persuasion: Expert (R3)
- Trading: Expert (R3)
- Deception: Trained (R2)

**Services:**
- Negotiate: +20% quest rewards
- Find buyers: +15% sell price
- Black market access

**Network:**
- 50+ contacts
- Can source rare items (48h wait)
- Insider info on deals

**Special Ability:**
- **Silver Tongue** — Talk out of fights (25% chance)

**ROI:** Pays for herself if active

---

## ⭐ TIER 3: RARE NPCs

### 7. **Viktor "Vik" Volkov**

**Role:** Ripperdoc  
**Tier:** 3  
**Rarity:** Rare

**Stats:**
- Level: 25-30
- Medicine: 5/5
- Tech: 4/5
- Cost: 25,000 €$ initial, 1,000 €$/day

**Skills:**
- Medicine: Master (R4)
- Cyberware: Master (R4)
- Surgery: Master (R4)

**Services:**
- Install: Epic-tier implants
- Discount: -30% on all cyberware
- Humanity restoration: +10 per week

**Special Ability:**
- **Master Surgeon** — 95% success rate (vs 80% standard)
- Can install experimental implants

**Location:** Watson clinic (after quest)  
**Reputation Required:** Rippers Guild +50

**Personal Story:**
- Knows Johnny Silverhand (lore connection)
- Has secret implant blueprints
- Quest chain: "The Doctor's Oath"

**Long-term Value:** Saves 100k+ €$ over time

---

### 8. **"Panam" Palmer** (Similar NPC)

**Role:** Nomad Scout / Driver  
**Tier:** 3  
**Rarity:** Rare

**Stats:**
- Level: 20-25
- Driving: 5/5
- Combat: 4/5
- Survival: 4/5
- Cost: 15,000 €$ initial, 800 €$/day

**Skills:**
- Driving: Master (R4)
- Gunplay: Expert (R3)
- Scavenging: Expert (R3)

**Services:**
- Driving: Best driver (escape artist)
- Badlands guide: Know all routes
- Vehicle repair: Free

**Equipment:**
- Modified truck (armored)
- High-end weapons

**Special Ability:**
- **Badlands Expert** — +50% speed in Badlands, know shortcuts

**Personal Story:**
- Aldecaldos family member
- Romance option (if solo player)
- Quest chain: "Nomad's Way"

**Location:** Aldecaldos camp  
**Reputation Required:** Aldecaldos +75

---

### 9. **"Rogue" Amendiares** (Inspired)

**Role:** Legendary Fixer  
**Tier:** 3  
**Rarity:** Rare

**Stats:**
- Level: 30-35
- Everything: 4-5/5
- Cost: 50,000 €$ initial, 2,000 €$/day

**Skills:**
- ALL social skills: Master+
- Combat: Expert
- Network: Legendary

**Services:**
- Access: EVERYTHING (black market)
- Intel: Best in city
- Contracts: Legendary-tier jobs

**Network:**
- 500+ contacts (entire city)
- Knows all fixers
- Can arrange anything (for a price)

**Special Ability:**
- **Queen of Fixers** — Can source impossible items

**Personal Story:**
- Knew Johnny Silverhand
- Living legend
- Quest chain: "The Queen's Trials"

**Location:** Afterlife bar (VIP)  
**Reputation Required:** Street Cred 800+

**Note:** Only 1 per server (unique)

---

## 💎 TIER 4: EPIC NPCs

### 10. **"Alt" Cunningham** (AI Copy)

**Role:** Rogue AI / Ultimate Netrunner  
**Tier:** 4  
**Rarity:** Epic

**Lore:** AI copy of legendary netrunner beyond Blackwall

**Stats:**
- Level: 40
- Hacking: 10/5 (beyond scale)
- Cost: 100,000 €$ initial, 5,000 €$/day

**Skills:**
- Hacking: Legendary (R5+)
- Cyberspace: Beyond scale
- AI Control: Unique

**Services:**
- Hack: ANYTHING (100% success)
- Blackwall access
- AI insights

**Special Abilities:**
- **Beyond Blackwall** — Access forbidden knowledge
- **System God** — Control entire city grids
- **Ghost Protocol** — Untraceable hacking

**Location:** Quest reward only  
**Quest:** "Beyond the Blackwall" (Voodoo Boys chain)

**Risks:**
- NetWatch hunts you
- Cyberpsychosis risk +20
- Morally questionable

**Unique:** Only 1 per player, cannot transfer

---

### 11. **MaxTac Operative** (Defected)

**Role:** Elite Combat Specialist  
**Tier:** 4  
**Rarity:** Epic

**Stats:**
- Level: 35-40
- Combat: 10/5 (beyond scale)
- Cost: 75,000 €$ initial, 3,000 €$/day

**Skills:**
- ALL combat skills: Master+
- Cyberware: Legendary

**Equipment:**
- MaxTac armor (legendary)
- Experimental weapons
- Full combat loadout

**Special Abilities:**
- **Tactical Supremacy** — Plan perfect ambushes
- **Cyberpsycho Hunter** — +500% damage vs cyberpsychos
- **MaxTac Training** — Can train player

**Personal Story:**
- Defected from MaxTac
- Hunted by former team
- Quest chain: "The Deserter"

**Location:** Secret (quest reward)  
**Requirements:** Complete "MaxTac: The Beast" quest with spare option

**Combat Power:** Can solo most bosses

---

## 👑 TIER 5: LEGENDARY NPCs

### 12. **"V's Clone"** (Endgame)

**Role:** Perfect Partner  
**Tier:** 5  
**Rarity:** Legendary (Unique)

**Lore:** Clone of player character (experimental tech)

**Stats:**
- Level: Matches player
- All skills: Matches player
- Cost: 1,000,000 €$ initial, 10,000 €$/day

**Abilities:**
- Mirrors player build
- Can do ANYTHING player can
- Perfect synchronization

**Special:**
- **Perfect Team** — +100% effectiveness when together
- **Mirror Build** — Adapts to player changes
- **Existential Crisis** — Philosophical questions

**Location:** Biotechnica secret lab (quest)  
**Quest:** "The Mirror" (requires Legendary status with Biotechnica)

**Ethical Issues:**
- Is it slavery?
- Sentience questions
- Multiple endings based on treatment

**Unique:** 1 per player ever

---

### 13. **Adam Smasher** (Post-Boss)

**Role:** Ultimate Mercenary  
**Tier:** 5  
**Rarity:** Artifact (if possible)

**Lore:** IF you defeat and spare Adam Smasher...

**Stats:**
- Level: 50
- Combat: 15/5 (god-tier)
- Cost: 5,000,000 €$ initial, 50,000 €$/day

**Abilities:**
- **Unkillable** — Cannot die (respawns)
- **Full Borg** — 99% cyberware
- **Legendary Mercenary** — Wins any fight

**Services:**
- Bodyguard: Literally unkillable
- Intimidation: Everyone fears
- Combat: Solo any content

**Requirements:**
- Defeat Smasher (raid boss)
- Spare him (rare choice)
- 10M €$ in bank (prove worth)
- Arasaka Exalted (paradox!)

**Note:** Game-breaking powerful, balanced by insane cost

**Availability:** Maybe 1-2 per server ever

---

## 📊 HIRING ECONOMICS

### ROI Analysis

**Tier 1 NPCs:**
- Payback: 1-2 weeks
- Use: Early game
- Value: Convenience

**Tier 2 NPCs:**
- Payback: 2-4 weeks
- Use: Mid game
- Value: Efficiency

**Tier 3 NPCs:**
- Payback: 1-3 months
- Use: Late game
- Value: Quality

**Tier 4 NPCs:**
- Payback: 6-12 months
- Use: Endgame
- Value: Exclusivity

**Tier 5 NPCs:**
- Payback: Never (flex)
- Use: Ultra-endgame
- Value: Prestige

---

## ✅ Готовность

- ✅ 13 конкретных NPC созданы
- ✅ Tier 1-5 progression
- ✅ Costs, skills, services
- ✅ Personal stories
- ✅ Economic analysis
- ✅ Unique legendary options

**Для геймплея готово!** 💼
