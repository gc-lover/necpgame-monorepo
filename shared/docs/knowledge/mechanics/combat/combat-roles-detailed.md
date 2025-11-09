# Детальные Боевые Роли

**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-09 03:14  
**api-readiness-notes:** Подтверждено 2025-11-09 03:14: боевые роли (танк, DPS, саппорт и гибриды) описаны со статами, имплантами, синергиями и тактиками; документ готов к постановке API задач gameplay-service.

**Версия:** 1.0.0  
**Дата:** 2025-11-06  
**Статус:** approved

**target-domain:** gameplay-combat  
**target-microservice:** gameplay-service (port 8083)  
**target-frontend-module:** modules/combat/roles

---

- **Status:** created
- **Last Updated:** 2025-11-06 23:15
---

**API Tasks Status:**
- Status: created
- Tasks:
  - API-TASK-403: api/v1/gameplay/combat/roles/roles.yaml (2025-11-09)
- Last Updated: 2025-11-09 23:05
---

---

## 🎯 Концепция Ролей

### Философия
- **Гибридность:** Каждый игрок может играть несколько ролей
- **Специализация:** Чистые билды эффективнее
- **Адаптация:** Роли меняются в зависимости от контента
- **Синергия:** Команды из разных ролей сильнее

---

## 🛡️ TANK (Танк)

### Концепция
Первая линия. Принимает урон. Защищает команду. Контролирует поле боя.

### Основные Характеристики

**Primary Attributes:**
- BODY: 16-20 (выносливость)
- STR: 14-18 (сила для угрозы)
- WILL: 12-16 (устойчивость к контролю)

**Secondary:**
- AGI: 8-12 (мобильность)
- COOL: 10-14 (хладнокровие)

### Обязательные Импланты

1. **Subdermal Armor** (подкожная броня)
   - +200 HP
   - +30% physical resistance

2. **Reinforced Skeleton** (усиленный скелет)
   - +50% knockback resistance
   - Can't be staggered

3. **Pain Editor** (редактор боли)
   - Ignore pain effects
   - +20% damage resistance when HP < 30%

4. **Bio-Monitor** (биомонитор)
   - Auto-heal when HP < 20%
   - +50 HP/s for 5s

### Способности (Loadout)

**Q1: Shield Boost**
- Deploy personal shield: 500 HP
- Duration: 8s
- Cooldown: 20s

**Q2: Taunt**
- Force enemies to attack you
- Range: 15m, Duration: 5s
- +20% damage resistance during taunt

**E: Charge**
- Charge forward 12m
- Knockback enemies
- Stun 2s on impact

**R: Fortify**
- +80% damage resistance
- Cannot move
- Reflect 30% melee damage
- Duration: 15s, Cooldown: 120s

**Passive 1: Threat Generation**
- Generate 150% threat
- Enemies prioritize tank

**Passive 2: Last Stand**
- When HP reaches 0: survive at 1 HP for 5s
- Once per combat

**Passive 3: Team Shield**
- Allies within 5m: +10% resistance

### Тактики

**Aggro Management:**
1. Taunt ключевых врагов (healers, casters)
2. Positioning: блокировать узкие проходы
3. Cooldown rotation: всегда иметь defensive CD ready

**Positioning:**
- Frontline: между врагами и командой
- Doorways: block passage
- Boss fights: tank boss away from team

**Engagement:**
1. Charge in первым
2. Taunt dangerous targets
3. Shield Boost before big damage
4. Fortify для boss ultimates

### Синергии с Командой

**+ DPS:**
- Tank holds aggro → DPS free to burst
- Positioning: DPS behind tank

**+ Support:**
- Support heals tank → tank survives longer
- Tank shields support → support safe to cast

**+ Healer:**
- Primary heal target
- Reduces team damage taken

### Экипировка

**Weapon Priority:**
- Heavy weapons (LMG, Shotgun)
- High threat generation
- Slow but powerful

**Armor:**
- Maximum armor rating
- HP bonuses
- Resistance stats

**Mods:**
- +HP, +Armor, +Resistance
- Threat generation
- Damage reduction

### Контр-Пик

**Силён против:**
- Melee enemies (high armor)
- Swarms (AoE taunt)
- Low DPS enemies (can tank forever)

**Слаб против:**
- Kiting ranged (can't catch)
- Percent HP damage (bypasses armor)
- Anti-tank abilities (armor shred)

---

## ⚔️ DPS (Damage Dealer)

### Две Специализации

#### A. Burst DPS (Взрывной урон)

**Концепция:** Огромный урон за короткое время. Убить до ответа.

**Primary Attributes:**
- REF: 16-20 (точность, крит)
- AGI: 14-18 (мобильность)
- INT: 10-14 (тактика)

**Abilities:**

**Q: Precision Shot**
- 400 damage, guaranteed crit
- Cooldown: 15s

**E: Reload Cancel**
- Instant reload
- +50% damage next 3 shots

**R: Assassination Protocol**
- Mark 3 targets
- Teleport between them
- 800 damage each
- Cooldown: 180s

**Тактика:**
- Wait for tank aggro
- Burst combo on priority target
- Disengage and reload
- Repeat

**Оружие:** Snipers, Revolvers, Shotguns

---

#### B. Sustained DPS (Постоянный урон)

**Концепция:** Стабильный DPS длительное время.

**Primary Attributes:**
- REF: 14-18
- AGI: 12-16
- BODY: 10-14 (выносливость)

**Abilities:**

**Q: Rapid Fire**
- +100% fire rate
- Duration: 8s
- Cooldown: 20s

**E: Ammo Generator**
- Infinite ammo 10s
- +20% damage

**R: Bullet Storm**
- Fire in all directions
- 200 DPS to all in 10m
- Duration: 8s
- Cooldown: 120s

**Тактика:**
- Maintain DPS uptime
- Resource management
- Safe positioning

**Оружие:** Assault Rifles, SMGs, LMGs

---

## 🔧 SUPPORT (Поддержка)

### Концепция
Utility. Баффы для команды. Дебаффы врагам. Control.

**Primary Attributes:**
- INT: 14-18 (тактика)
- COOL: 12-16 (хладнокровие)
- EMP: 10-14 (эмпатия для баффов)

### Abilities

**Q: Buff Drone**
- +20% damage to ally
- Duration: 15s
- Cooldown: 20s

**E: Debuff Field**
- -30% enemy damage in 8m
- Duration: 10s

**R: Tactical Override**
- Hack all devices in 20m
- Turrets fight for you
- Cameras mark enemies
- Duration: 30s
- Cooldown: 150s

### Тактики

**Pre-Combat:**
- Scan enemies
- Deploy recon
- Buff team

**Mid-Combat:**
- Debuff priority targets
- Control enemy positioning
- Hack environment

**Utility:**
- Revive fallen
- Provide cover
- Hack escape routes

---

## 💚 HEALER (Лекарь)

### Концепция
Держать команду живой. Cleanse эффекты. Спасать критические ситуации.

**Primary Attributes:**
- TECH: 14-18 (медицина)
- EMP: 12-16 (забота о команде)
- INT: 10-14 (диагностика)

### Healing Priorities

1. **Tank** — primary target (70% heals)
2. **DPS** — when critical (20%)
3. **Self** — when safe (10%)
4. **Emergency** — anyone < 20% HP

### Abilities

**Q: Quick Heal**
- 200 HP instant
- Range: 15m
- Cooldown: 8s
- Charges: 3

**E: Healing Drone**
- Deploy drone: 60 HP/s to lowest HP ally
- Duration: 20s
- Cooldown: 30s

**R: Mass Resurrection**
- Revive all dead allies in 15m
- Restore 70% HP
- Cooldown: 300s

**Passive: Healing Aura**
- 10 HP/s to allies within 8m

### Медицинские Инструменты

- **Trauma Kit:** Remove bleeding, poison
- **Cyberware Repair Kit:** Restore disabled implants
- **Stim Pack:** Instant 150 HP
- **Antidote:** Cure toxins, radiation

---

## 🎯 HYBRID ROLES (Гибриды)

### Примеры Успешных Комбинаций

#### 1. Battle Medic
- 60% Healer + 40% DPS
- Can defend self while healing
- Loadout: Heal + Combat abilities

#### 2. Stealth DPS
- 70% DPS + 30% Stealth
- Assassinations and burst
- Loadout: Stealth + Damage

#### 3. Hacker Support
- 50% Netrunner + 50% Support
- Debuffs + Utility
- Loadout: Quickhacks + Buffs

#### 4. Tank DPS
- 60% Tank + 40% DPS
- Bruiser style
- Loadout: Survivability + Damage

---

## 📊 ROLE META & TIER LIST

### PvE Meta (Dungeons/Raids)

**S-Tier (Mandatory):**
- Tank (1 обязателен)
- Healer (1 обязателен)

**A-Tier (Highly Recommended):**
- Burst DPS (2-3)
- Support (1-2)

**B-Tier (Situational):**
- Sustained DPS
- Stealth
- Pure Netrunner

### PvP Meta (Arena)

**S-Tier:**
- Burst DPS
- Stealth Assassin
- Netrunner (Disabler)

**A-Tier:**
- Tank
- Battle Medic
- Support

**B-Tier:**
- Pure Healer
- Sustained DPS

---

## 🎮 ROLE PROGRESSION

### Tank Progression Path

```
Lvl 1-10:  Basic Tank
├─ Learn threat generation
├─ Basic defensive abilities
└─ Build HP pool

Lvl 11-25: Advanced Tank
├─ Unlock Shield abilities
├─ Team protection
└─ Boss mechanics

Lvl 26-50: Elite Tank
├─ Ultimate survivability
├─ Raid tanking
└─ Multiple bosses

Lvl 51+:   Legendary Tank
├─ Immortal builds
├─ World bosses
└─ Carry groups
```

---

## 📈 TEAM COMPOSITIONS

### Optimal (5-player)

**Raid Composition:**
1. Main Tank
2. Off-Tank / Bruiser
3. Healer
4. DPS (Burst)
5. DPS (Sustained) or Support

**PvP Composition:**
1. Tank / Initiator
2. Assassin (Stealth DPS)
3. Netrunner (Disabler)
4. Healer / Battle Medic
5. Burst DPS

**Solo Extraction (Tarkov style):**
- Hybrid DPS + Stealth
- Self-sustain (healing items)
- Escape abilities priority

---

## ✅ Готовность

- ✅ 5 основных ролей определены
- ✅ 4 гибридных роли предложены
- ✅ Тактики для каждой роли
- ✅ Team compositions
- ✅ Progression paths

**Следующий шаг:** Детальные комбо и синергии!
