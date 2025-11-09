---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 02:34
**api-readiness-notes:** Clan War System. Войны кланов, территории, siege mechanics, rewards. ~390 строк.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 03:38
---
# Clan War System - Система клановых войн

**Статус:** approved  
**Версия:** 1.0.0  
**Дата создания:** 2025-11-07 02:34  
**Приоритет:** HIGH (Endgame PvP!)  
**Автор:** AI Brain Manager

**Микрофича:** Clan wars & territory control  
**Размер:** ~390 строк ✅

---

## Краткое описание

**Clan War System** - система масштабных клановых войн за территории и ресурсы.

**Ключевые возможности:**
- ✅ War Declaration (объявление войны)
- ✅ Territory Control (контроль территорий)
- ✅ Siege Mechanics (осада баз)
- ✅ War Phases (фазы войны)
- ✅ War Rewards (награды победителю)
- ✅ Alliance System (союзы кланов)

---

## Архитектура системы

```
Clan declares war on another clan
    ↓
24h Preparation Phase
    ↓
War Phase (7 days)
├─ Territory battles
├─ Siege events
└─ Kill scores
    ↓
Calculate winner
    ↓
Grant territory/rewards
    ↓
War ends
```

---

## War Phases

### Phase 1: Preparation (24 hours)

```
- War declaration
- Set war goals
- Recruit allies
- Stockpile resources
- Plan strategy
```

### Phase 2: Active War (7 days)

```
- Territory battles (3x per day)
- Siege events (1x per day)
- Open world PvP (bonus points)
- Resource gathering
- Kill tracking
```

### Phase 3: Resolution (Immediate)

```
- Calculate total points
- Determine winner
- Transfer territory
- Distribute rewards
- Archive war data
```

---

## Database Schema

### Таблица `clan_wars`

```sql
CREATE TABLE clan_wars (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Clans
    attacker_clan_id UUID NOT NULL,
    defender_clan_id UUID NOT NULL,
    
    -- Allies
    attacker_allies UUID[],
    defender_allies UUID[],
    
    -- Territory
    disputed_territory_id UUID,
    
    -- War details
    war_type VARCHAR(20) NOT NULL,
    declaration_reason TEXT,
    
    -- Status
    status VARCHAR(20) DEFAULT 'PREPARATION',
    
    -- Phases
    preparation_start TIMESTAMP NOT NULL,
    preparation_end TIMESTAMP NOT NULL,
    war_start TIMESTAMP,
    war_end TIMESTAMP,
    
    -- Scoring
    attacker_score INTEGER DEFAULT 0,
    defender_score INTEGER DEFAULT 0,
    
    -- Result
    winner_clan_id UUID,
    
    -- Rewards
    rewards_distributed BOOLEAN DEFAULT FALSE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_war_attacker FOREIGN KEY (attacker_clan_id) 
        REFERENCES guilds(id) ON DELETE CASCADE,
    CONSTRAINT fk_war_defender FOREIGN KEY (defender_clan_id) 
        REFERENCES guilds(id) ON DELETE CASCADE,
    CONSTRAINT fk_war_winner FOREIGN KEY (winner_clan_id) 
        REFERENCES guilds(id) ON DELETE SET NULL
);

CREATE INDEX idx_clan_wars_status ON clan_wars(status);
CREATE INDEX idx_clan_wars_clans ON clan_wars(attacker_clan_id, defender_clan_id);
```

### Таблица `war_battles`

```sql
CREATE TABLE war_battles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    war_id UUID NOT NULL,
    
    -- Battle info
    battle_type VARCHAR(50) NOT NULL,
    territory_id UUID,
    
    -- Timing
    scheduled_at TIMESTAMP NOT NULL,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    
    -- Participants
    attacker_players UUID[],
    defender_players UUID[],
    
    -- Result
    winner_side VARCHAR(20),
    attacker_kills INTEGER DEFAULT 0,
    defender_kills INTEGER DEFAULT 0,
    
    -- Points
    points_awarded INTEGER,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_battle_war FOREIGN KEY (war_id) 
        REFERENCES clan_wars(id) ON DELETE CASCADE
);

CREATE INDEX idx_war_battles_war ON war_battles(war_id);
CREATE INDEX idx_war_battles_scheduled ON war_battles(scheduled_at);
```

### Таблица `territories`

```sql
CREATE TABLE territories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Territory info
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    location VARCHAR(100) NOT NULL,
    
    -- Control
    controlling_clan_id UUID,
    controlled_since TIMESTAMP,
    
    -- Resources
    resource_type VARCHAR(50),
    resource_generation_rate INTEGER,
    
    -- Defense
    defense_rating INTEGER DEFAULT 100,
    
    -- Siege
    siege_difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_territory_clan FOREIGN KEY (controlling_clan_id) 
        REFERENCES guilds(id) ON DELETE SET NULL
);

CREATE INDEX idx_territories_clan ON territories(controlling_clan_id);
```

---

## Declare War

```java
@Service
public class ClanWarService {
    
    public ClanWar declareWar(UUID attackerClanId, 
                             UUID defenderClanId,
                             DeclareWarRequest request) {
        
        // Validate
        validateWarDeclaration(attackerClanId, defenderClanId);
        
        // Check cooldown
        if (hasRecentWar(attackerClanId)) {
            throw new WarCooldownException();
        }
        
        // Create war
        ClanWar war = new ClanWar();
        war.setAttackerClanId(attackerClanId);
        war.setDefenderClanId(defenderClanId);
        war.setDisputedTerritoryId(request.getTerritoryId());
        war.setWarType(request.getWarType());
        war.setDeclarationReason(request.getReason());
        war.setStatus(WarStatus.PREPARATION);
        
        // Set preparation phase (24h)
        Instant now = Instant.now();
        war.setPreparationStart(now);
        war.setPreparationEnd(now.plus(24, ChronoUnit.HOURS));
        
        war = warRepository.save(war);
        
        // Notify both clans
        notifyClanOfWarDeclaration(defenderClanId, attackerClanId, war);
        
        // Schedule war start
        scheduleWarStart(war.getId(), war.getPreparationEnd());
        
        log.warn("War declared: {} vs {}", attackerClanId, defenderClanId);
        
        return war;
    }
    
    private void validateWarDeclaration(UUID attackerClanId, UUID defenderClanId) {
        // Same clan check
        if (attackerClanId.equals(defenderClanId)) {
            throw new InvalidWarTargetException("Cannot declare war on yourself");
        }
        
        // Minimum clan size
        if (getClanMemberCount(attackerClanId) < 10) {
            throw new InsufficientClanSizeException("Need at least 10 members");
        }
        
        if (getClanMemberCount(defenderClanId) < 10) {
            throw new InvalidWarTargetException("Target clan too small");
        }
        
        // Already at war check
        if (isAtWar(attackerClanId) || isAtWar(defenderClanId)) {
            throw new AlreadyAtWarException();
        }
    }
}
```

---

## Territory Battle

```java
public void startTerritoryBattle(UUID warId, UUID territoryId) {
    ClanWar war = warRepository.findById(warId).orElseThrow();
    Territory territory = territoryRepository.findById(territoryId).orElseThrow();
    
    // Create battle event
    WarBattle battle = new WarBattle();
    battle.setWarId(warId);
    battle.setBattleType("TERRITORY_CONTROL");
    battle.setTerritoryId(territoryId);
    battle.setScheduledAt(Instant.now());
    battle.setStartedAt(Instant.now());
    
    battleRepository.save(battle);
    
    // Notify all war participants
    notifyBattleStart(war, territory);
    
    // Open PvP zone for 1 hour
    enablePvPZone(territoryId, Duration.ofHours(1));
    
    log.info("Territory battle started: war={}, territory={}", 
        warId, territoryId);
}

@EventListener
public void onBattleKill(PlayerKilledEvent event) {
    // Check if kill happened in war zone
    Optional<WarBattle> battle = findActiveBattle(event.getLocation());
    
    if (battle.isEmpty()) {
        return;
    }
    
    // Award points
    ClanWar war = warRepository.findById(battle.get().getWarId()).orElseThrow();
    
    UUID killerClan = getPlayerClan(event.getKillerId());
    
    if (killerClan.equals(war.getAttackerClanId())) {
        war.setAttackerScore(war.getAttackerScore() + 10);
    } else if (killerClan.equals(war.getDefenderClanId())) {
        war.setDefenderScore(war.getDefenderScore() + 10);
    }
    
    warRepository.save(war);
}
```

---

## War End & Rewards

```java
public void endWar(UUID warId) {
    ClanWar war = warRepository.findById(warId).orElseThrow();
    
    // Determine winner
    UUID winnerId;
    if (war.getAttackerScore() > war.getDefenderScore()) {
        winnerId = war.getAttackerClanId();
    } else {
        winnerId = war.getDefenderClanId();
    }
    
    war.setWinnerClanId(winnerId);
    war.setStatus(WarStatus.COMPLETED);
    war.setWarEnd(Instant.now());
    
    warRepository.save(war);
    
    // Transfer territory
    if (war.getDisputedTerritoryId() != null && 
        winnerId.equals(war.getAttackerClanId())) {
        transferTerritory(war.getDisputedTerritoryId(), winnerId);
    }
    
    // Distribute rewards
    distributeWarRewards(war);
    
    // Notify all participants
    announceWarEnd(war);
    
    log.info("War ended: {} - Winner: {}", warId, winnerId);
}

private void distributeWarRewards(ClanWar war) {
    // Winner rewards
    grantClanRewards(war.getWinnerClanId(), WinnerRewards.builder()
        .eddies(1000000)
        .reputation(5000)
        .warTokens(100)
        .exclusiveItem("WAR_TROPHY_SEASON_1")
        .build()
    );
    
    // Loser consolation
    UUID loserId = war.getWinnerClanId().equals(war.getAttackerClanId()) 
        ? war.getDefenderClanId() 
        : war.getAttackerClanId();
    
    grantClanRewards(loserId, ConsolationRewards.builder()
        .eddies(100000)
        .reputation(1000)
        .build()
    );
    
    war.setRewardsDistributed(true);
    warRepository.save(war);
}
```

---

## API Endpoints

**POST `/api/v1/clan-wars/declare`