---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:34
**api-readiness-notes:** Companion System. Pets, drones, AI companions, abilities. ~390 строк.
---

# Companion System - Система компаньонов

---

- **Status:** queued
- **Last Updated:** 2025-11-07 22:25
---

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:34  
**Приоритет:** MEDIUM (Gameplay Enhancement!)  
**Автор:** AI Brain Manager

**Микрофича:** Pet & companion mechanics  
**Размер:** ~390 строк ✅

---

## Краткое описание

**Companion System** - система спутников игрока (pets, drones, AI companions).

**Ключевые возможности:**
- ✅ Combat Companions (боевые дроны)
- ✅ Utility Companions (утилитарные)
- ✅ Social Companions (питомцы)
- ✅ Companion Abilities (активные/пассивные)
- ✅ Companion Progression (прокачка)
- ✅ Companion Customization (кастомизация)

---

## Типы компаньонов

### 1. Combat Drones (Боевые дроны)

```
Attack Drone:
- Auto-attacks enemies
- 20% player damage
- Can be destroyed (respawn 5min)

Support Drone:
- Heals player (5% HP per 10s)
- Provides shields (+50 shield)
- Cannot attack

Recon Drone:
- Reveals enemies (50m radius)
- Marks targets (+10% damage)
- Fragile (1 hit = destroyed)
```

---

### 2. Utility Companions

```
Loot Collector:
- Auto-collects loot (10m radius)
- +20% loot carrying capacity
- Cannot fight

Hacking Assistant:
- +15% hacking success rate
- Reduces ICE difficulty
- Reveals hidden systems

Crafting Bot:
- -10% crafting time
- +5% craft quality
- Auto-repairs equipment (slowly)
```

---

### 3. Social Pets

```
Cat (Cyber-cat):
- Cosmetic only
- Can pet for +1 humanity
- Follows player

Dog (Robo-dog):
- Cosmetic only
- Tricks and emotes
- Loyalty system

Bird (Drone-bird):
- Cosmetic only
- Flies around player
- Can sit on shoulder
```

---

## Database Schema

### Таблица `companion_types`

```sql
CREATE TABLE companion_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Type info
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    
    -- Category
    companion_category VARCHAR(50) NOT NULL,
    
    -- Stats
    base_health INTEGER DEFAULT 100,
    base_damage INTEGER DEFAULT 0,
    
    -- Abilities
    active_abilities JSONB,
    passive_abilities JSONB,
    
    -- Rarity
    rarity VARCHAR(20) DEFAULT 'COMMON',
    
    -- Acquisition
    is_purchasable BOOLEAN DEFAULT TRUE,
    price INTEGER,
    currency VARCHAR(20) DEFAULT 'EDDIES',
    
    -- Assets
    model_path VARCHAR(255),
    icon VARCHAR(255),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_companion_types_category ON companion_types(companion_category);
```

### Таблица `player_companions`

```sql
CREATE TABLE player_companions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    companion_type_id UUID NOT NULL,
    
    -- Customization
    nickname VARCHAR(100),
    
    -- Progression
    level INTEGER DEFAULT 1,
    experience INTEGER DEFAULT 0,
    
    -- Stats (can improve with level)
    current_health INTEGER,
    max_health INTEGER,
    damage INTEGER,
    
    -- Equipment
    equipped_items UUID[],
    
    -- Status
    is_summoned BOOLEAN DEFAULT FALSE,
    last_summoned_at TIMESTAMP,
    
    -- Usage
    total_summon_count INTEGER DEFAULT 0,
    total_kills INTEGER DEFAULT 0,
    
    acquired_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_player_companion_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_player_companion_type FOREIGN KEY (companion_type_id) 
        REFERENCES companion_types(id) ON DELETE CASCADE,
    
    UNIQUE(player_id, companion_type_id)
);

CREATE INDEX idx_player_companions_player ON player_companions(player_id);
```

---

## Summon Companion

```java
@Service
public class CompanionService {
    
    public void summonCompanion(UUID playerId, UUID companionId) {
        PlayerCompanion companion = playerCompanionRepository
            .findById(companionId)
            .orElseThrow();
        
        // Validate ownership
        if (!companion.getPlayerId().equals(playerId)) {
            throw new NotYourCompanionException();
        }
        
        // Dismiss current companion (if any)
        dismissCurrentCompanion(playerId);
        
        // Summon new
        companion.setIsSummoned(true);
        companion.setLastSummonedAt(Instant.now());
        companion.setTotalSummonCount(companion.getTotalSummonCount() + 1);
        
        playerCompanionRepository.save(companion);
        
        // Spawn in game world
        spawnCompanion(playerId, companion);
        
        // Apply passive bonuses
        applyCompanionBonuses(playerId, companion);
        
        log.info("Companion summoned: player={}, companion={}", 
            playerId, companionId);
    }
    
    public void dismissCompanion(UUID playerId) {
        Optional<PlayerCompanion> current = playerCompanionRepository
            .findByPlayerAndSummoned(playerId, true);
        
        if (current.isEmpty()) {
            return;
        }
        
        PlayerCompanion companion = current.get();
        companion.setIsSummoned(false);
        
        playerCompanionRepository.save(companion);
        
        // Remove from game world
        despawnCompanion(playerId, companion.getId());
        
        // Remove passive bonuses
        removeCompanionBonuses(playerId);
    }
}
```

---

## Companion Progression

### Level Up

```java
public void awardCompanionXP(UUID companionId, int xp) {
    PlayerCompanion companion = playerCompanionRepository
        .findById(companionId)
        .orElseThrow();
    
    companion.setExperience(companion.getExperience() + xp);
    
    // Check level up
    int xpForNextLevel = calculateXPForNextLevel(companion.getLevel());
    
    while (companion.getExperience() >= xpForNextLevel && 
           companion.getLevel() < 50) {
        
        // Level up!
        companion.setLevel(companion.getLevel() + 1);
        companion.setExperience(companion.getExperience() - xpForNextLevel);
        
        // Improve stats
        improveCompanionStats(companion);
        
        // Notify player
        notifyCompanionLevelUp(companion);
        
        xpForNextLevel = calculateXPForNextLevel(companion.getLevel());
    }
    
    playerCompanionRepository.save(companion);
}

private void improveCompanionStats(PlayerCompanion companion) {
    // +5% health per level
    companion.setMaxHealth((int)(companion.getMaxHealth() * 1.05));
    companion.setCurrentHealth(companion.getMaxHealth());
    
    // +3% damage per level
    companion.setDamage((int)(companion.getDamage() * 1.03));
}
```

---

## Companion Abilities

### Active Abilities

```json
{
  "ability": "ATTACK_COMMAND",
  "type": "ACTIVE",
  "cooldown": 30,
  "description": "Command drone to focus target",
  "effect": {
    "damage_bonus": 50,
    "duration": 10
  }
}

{
  "ability": "DEFENSIVE_MODE",
  "type": "ACTIVE",
  "cooldown": 60,
  "description": "Drone shields you",
  "effect": {
    "shield_amount": 200,
    "duration": 15
  }
}
```

### Passive Abilities

```json
{
  "ability": "COMBAT_ASSISTANCE",
  "type": "PASSIVE",
  "description": "Automatically attacks your targets",
  "effect": {
    "damage": "20% of player damage",
    "attack_speed": 1.0
  }
}

{
  "ability": "RESOURCE_GATHERING",
  "type": "PASSIVE",
  "description": "Auto-collects nearby loot",
  "effect": {
    "radius": 10,
    "auto_collect": true
  }
}
```

---

## API Endpoints

**GET `/api/v1/companions/available`** - доступные компаньоны

**POST `/api/v1/companions/purchase`** - купить компаньона

**GET `/api/v1/companions/owned`** - owned companions

**POST `/api/v1/companions/summon`** - призвать

**POST `/api/v1/companions/dismiss`** - отозвать

**POST `/api/v1/companions/{id}/rename`** - переименовать

**POST `/api/v1/companions/{id}/ability/use`** - использовать способность

---

## Связанные документы

- [Combat System](../../gameplay/combat/combat-overview.md)
- [Pet Customization](../../cosmetic/pet-customization.md)
