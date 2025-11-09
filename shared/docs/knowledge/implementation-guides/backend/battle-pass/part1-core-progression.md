# Battle Pass - Part 1: Core & Progression

**Статус:** approved  
**Версия:** 1.0.1  
**Дата:** 2025-11-07 02:40  
**api-readiness:** ready

---

- **Status:** queued
- **Last Updated:** 2025-11-07 23:58
---

[Навигация](./README.md) | [Part 2 →](./part2-rewards-challenges.md)

---

## Краткое описание

**Battle Pass System** - сезонная система прогресса с наградами для retention и monetization.

**Ключевые возможности:**
- ✅ Free Track (бесплатный) - все игроки
- ✅ Premium Track (премиум) - за покупку
- ✅ 100 Levels прогресса
- ✅ Unique Rewards (эксклюзивные)
- ✅ Weekly Challenges
- ✅ Season Duration (3 месяца)

---

## Database Schema

### Таблица `battle_pass_seasons`

```sql
CREATE TABLE battle_pass_seasons (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Season info
    season_number INTEGER UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    theme VARCHAR(100),
    description TEXT,
    
    -- Duration
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    
    -- Levels
    max_level INTEGER DEFAULT 100,
    xp_per_level INTEGER DEFAULT 1000,
    
    -- Pricing
    premium_price INTEGER NOT NULL,
    premium_currency VARCHAR(20) DEFAULT 'PREMIUM',
    
    -- Status
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_battle_pass_status ON battle_pass_seasons(status);
CREATE INDEX idx_battle_pass_dates ON battle_pass_seasons(start_date, end_date);
```

### Таблица `battle_pass_rewards`

```sql
CREATE TABLE battle_pass_rewards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    season_id UUID NOT NULL,
    
    -- Level
    level INTEGER NOT NULL,
    
    -- Track
    track VARCHAR(20) NOT NULL,
    
    -- Reward
    reward_type VARCHAR(50) NOT NULL,
    reward_data JSONB NOT NULL,
    
    -- Display
    display_name VARCHAR(200),
    icon VARCHAR(255),
    rarity VARCHAR(20),
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_bp_reward_season FOREIGN KEY (season_id) 
        REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    UNIQUE(season_id, level, track)
);

CREATE INDEX idx_bp_rewards_season_level ON battle_pass_rewards(season_id, level);
```

### Таблица `player_battle_pass_progress`

```sql
CREATE TABLE player_battle_pass_progress (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    player_id UUID NOT NULL,
    season_id UUID NOT NULL,
    
    -- Progress
    current_level INTEGER DEFAULT 1,
    current_xp INTEGER DEFAULT 0,
    total_xp_earned INTEGER DEFAULT 0,
    
    -- Premium status
    has_premium BOOLEAN DEFAULT FALSE,
    premium_purchased_at TIMESTAMP,
    
    -- Claimed rewards
    claimed_free_levels INTEGER[] DEFAULT '{}',
    claimed_premium_levels INTEGER[] DEFAULT '{}',
    
    -- Timestamps
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_xp_earned_at TIMESTAMP,
    
    CONSTRAINT fk_bp_progress_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_bp_progress_season FOREIGN KEY (season_id) 
        REFERENCES battle_pass_seasons(id) ON DELETE CASCADE,
    
    UNIQUE(player_id, season_id)
);

CREATE INDEX idx_bp_progress_player ON player_battle_pass_progress(player_id);
CREATE INDEX idx_bp_progress_season ON player_battle_pass_progress(season_id);
```

---

## XP System

### XP Sources

```java
public enum BattlePassXPSource {
    DAILY_QUEST(100),
    WEEKLY_CHALLENGE(500),
    MATCH_WIN(50),
    MATCH_PLAYED(25),
    QUEST_COMPLETE(200),
    ACHIEVEMENT_UNLOCK(150),
    PLAYTIME(10);
    
    private final int baseXP;
}
```

### Award XP (ПОЛНЫЙ метод)

```java
@Service
public class BattlePassService {
    
    public void awardXP(UUID playerId, BattlePassXPSource source, int multiplier) {
        // Get current season
        BattlePassSeason season = getCurrentSeason();
        
        // Get or create progress
        PlayerBattlePassProgress progress = getOrCreateProgress(playerId, season.getId());
        
        // Calculate XP
        int xpEarned = source.getBaseXP() * multiplier;
        
        // Add XP
        progress.setCurrentXP(progress.getCurrentXP() + xpEarned);
        progress.setTotalXPEarned(progress.getTotalXPEarned() + xpEarned);
        progress.setLastXPEarnedAt(Instant.now());
        
        // Check for level ups
        int xpPerLevel = season.getXpPerLevel();
        while (progress.getCurrentXP() >= xpPerLevel && 
               progress.getCurrentLevel() < season.getMaxLevel()) {
            
            // Level up!
            progress.setCurrentLevel(progress.getCurrentLevel() + 1);
            progress.setCurrentXP(progress.getCurrentXP() - xpPerLevel());
            
            // Notify player
            notifyLevelUp(playerId, progress.getCurrentLevel());
            
            // Track analytics
            analyticsService.trackBattlePassLevelUp(playerId, progress.getCurrentLevel());
        }
        
        progressRepository.save(progress);
        
        // Publish event
        eventBus.publish(new BattlePassXPEarnedEvent(playerId, xpEarned));
        
        log.info("Battle Pass XP awarded: player={}, source={}, xp={}", 
            playerId, source, xpEarned);
    }
}
```

---

## Premium Purchase (ПОЛНЫЙ метод)

```java
public void purchasePremium(UUID playerId) {
    PlayerBattlePassProgress progress = getProgress(playerId);
    BattlePassSeason season = seasonRepository.findById(progress.getSeasonId())
        .orElseThrow();
    
    // Check if already purchased
    if (progress.isHasPremium()) {
        throw new AlreadyPurchasedException();
    }
    
    // Check currency
    if (!hasPremiumCurrency(playerId, season.getPremiumPrice())) {
        throw new InsufficientCurrencyException();
    }
    
    // Deduct currency
    deductPremiumCurrency(playerId, season.getPremiumPrice());
    
    // Grant premium
    progress.setHasPremium(true);
    progress.setPremiumPurchasedAt(Instant.now());
    
    progressRepository.save(progress);
    
    // Notify player
    notificationService.send(playerId, 
        new BattlePassPremiumUnlockedNotification(season));
    
    // Analytics
    analyticsService.trackBattlePassPurchase(playerId, season.getId());
    
    log.info("Battle Pass premium purchased: player={}, season={}", 
        playerId, season.getId());
}
```

---

[Part 2: Rewards & Challenges →](./part2-rewards-challenges.md)

---

## История изменений

- v1.0.1 (2025-11-07 02:40) - Создан с полным Java кодом (schemas, XP, premium)
- v1.0.0 (2025-11-07 02:30) - Создан
