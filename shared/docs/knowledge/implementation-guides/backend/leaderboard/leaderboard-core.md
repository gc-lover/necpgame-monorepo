---
**api-readiness:** ready  
**api-readiness-check-date:** 2025-11-07 01:59
**api-readiness-notes:** Leaderboard System. Глобальные и локальные рейтинги, различные категории, сезонность. ~400 строк.
---

- **Status:** queued
- **Last Updated:** 2025-11-08 06:45
---

## Краткое описание

**Leaderboard System** - система глобальных и локальных рейтингов для конкурентного аспекта MMORPG.

**Ключевые возможности:**
- ✅ Глобальные рейтинги (топ игроков мира)
- ✅ Региональные рейтинги (по городам/серверам)
- ✅ Категориальные рейтинги (combat/economy/social)
- ✅ Сезонные лиги (reset каждые 3-6 месяцев)
- ✅ Real-time updates (WebSocket)
- ✅ Rewards для топ игроков

---

## Архитектура системы

```
Player Achievement/Action
    ↓
Update Player Stats
    ↓
Calculate Leaderboard Score
    ↓
Update Redis Sorted Set (real-time)
    ↓
Batch Sync to PostgreSQL (every 5 min)
    ↓
WebSocket Push to Clients
```

---

## Типы рейтингов

### 1. Global Leaderboards (Глобальные)

**Всемирные рейтинги:**
- Overall Power (общая мощь)
- Level Ranking (уровень)
- Achievement Points (очки достижений)
- Net Worth (состояние)
- PvP Rating (ELO)

**Обновление:** Real-time

---

### 2. Regional Leaderboards (Региональные)

**По серверам/городам:**
- Night City Top 100
- Tokyo Server Champions
- Moscow Legends

**Обновление:** Real-time

---

### 3. Category Leaderboards (Категориальные)

**По активностям:**

**Combat:**
- Total Kills
- Boss Kills
- Arena Wins
- Headshot Accuracy

**Economy:**
- Richest Players
- Top Traders (trade volume)
- Crafting Masters (items crafted)
- Stock Market Tycoons

**Social:**
- Most Popular (friend count)
- Guild Leaders (guild size/power)
- Romance Champions (romance completions)

**Quests:**
- Quest Completionist (total quests)
- Speedrunners (fastest completions)
- Explorer (locations discovered)

---

### 4. Seasonal Leaderboards (Сезонные)

**Лиги:**
```
League 2093 (Season 1)
- Start: 2025-12-01
- End: 2026-03-01
- Reset: All progress reset to 0
- Rewards: Top 100 get exclusive titles/items
```

**Категории в лиге:**
- League Overall
- League PvP
- League PvE
- League Economy

---

## Database Schema

### Таблица `leaderboards`

```sql
CREATE TABLE leaderboards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Identification
    code VARCHAR(100) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    
    -- Type
    type VARCHAR(20) NOT NULL,
    category VARCHAR(50),
    
    -- Scope
    scope VARCHAR(20) DEFAULT 'GLOBAL',
    region_id VARCHAR(100),
    server_id VARCHAR(100),
    
    -- Season
    is_seasonal BOOLEAN DEFAULT FALSE,
    season_id UUID,
    season_start DATE,
    season_end DATE,
    
    -- Scoring
    scoring_metric VARCHAR(50) NOT NULL,
    sort_order VARCHAR(4) DEFAULT 'DESC',
    
    -- Display
    max_ranks INTEGER DEFAULT 100,
    update_frequency VARCHAR(20) DEFAULT 'REALTIME',
    
    -- Rewards
    rewards JSONB,
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_leaderboards_type ON leaderboards(type);
CREATE INDEX idx_leaderboards_season ON leaderboards(season_id) 
    WHERE is_seasonal = TRUE;
CREATE INDEX idx_leaderboards_active ON leaderboards(is_active) 
    WHERE is_active = TRUE;
```

### Таблица `leaderboard_entries`

```sql
CREATE TABLE leaderboard_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    leaderboard_id UUID NOT NULL,
    player_id UUID NOT NULL,
    
    -- Ranking
    rank INTEGER NOT NULL,
    score BIGINT NOT NULL,
    previous_rank INTEGER,
    
    -- Player Info (denormalized для производительности)
    player_name VARCHAR(100),
    player_level INTEGER,
    player_class VARCHAR(50),
    guild_id UUID,
    guild_name VARCHAR(100),
    
    -- Timestamps
    first_entry_at TIMESTAMP,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Metadata
    metadata JSONB,
    
    CONSTRAINT fk_entry_leaderboard FOREIGN KEY (leaderboard_id) 
        REFERENCES leaderboards(id) ON DELETE CASCADE,
    CONSTRAINT fk_entry_player FOREIGN KEY (player_id) 
        REFERENCES players(id) ON DELETE CASCADE,
    
    UNIQUE(leaderboard_id, player_id)
);

CREATE INDEX idx_leaderboard_entries_board_rank 
    ON leaderboard_entries(leaderboard_id, rank);
CREATE INDEX idx_leaderboard_entries_player 
    ON leaderboard_entries(player_id);
CREATE INDEX idx_leaderboard_entries_score 
    ON leaderboard_entries(leaderboard_id, score DESC);
```

### Таблица `leaderboard_snapshots`

```sql
CREATE TABLE leaderboard_snapshots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    leaderboard_id UUID NOT NULL,
    
    -- Snapshot
    snapshot_date DATE NOT NULL,
    snapshot_data JSONB NOT NULL,
    
    -- Type
    snapshot_type VARCHAR(20) DEFAULT 'DAILY',
    
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_snapshot_leaderboard FOREIGN KEY (leaderboard_id) 
        REFERENCES leaderboards(id) ON DELETE CASCADE,
    
    UNIQUE(leaderboard_id, snapshot_date, snapshot_type)
);

CREATE INDEX idx_snapshots_leaderboard_date 
    ON leaderboard_snapshots(leaderboard_id, snapshot_date DESC);
```

---

## Redis Implementation

### Sorted Sets для Real-Time

**Key format:** `leaderboard:{code}:{scope}`

```redis
# Global combat kills leaderboard
ZADD leaderboard:combat_kills:global 15234 player:uuid1
ZADD leaderboard:combat_kills:global 12456 player:uuid2
ZADD leaderboard:combat_kills:global 10987 player:uuid3

# Get top 100
ZREVRANGE leaderboard:combat_kills:global 0 99 WITHSCORES

# Get player rank
ZREVRANK leaderboard:combat_kills:global player:uuid1

# Get player score
ZSCORE leaderboard:combat_kills:global player:uuid1

# Get players around rank
ZREVRANGE leaderboard:combat_kills:global 48 52 WITHSCORES  # Rank 49-53
```

### Update Score

```python
def update_leaderboard_score(leaderboard_code, player_id, new_score):
    key = f"leaderboard:{leaderboard_code}:global"
    
    # Update in Redis
    redis.zadd(key, {f"player:{player_id}": new_score})
    
    # Queue for DB sync
    leaderboard_sync_queue.add(leaderboard_code, player_id, new_score)
```

---

## Score Calculation

### Overall Power Score

```python
def calculate_overall_power(player):
    """Общая мощь игрока"""
    
    score = 0
    
    # Level (0-50,000)
    score += player.level * 1000
    
    # Equipment (0-30,000)
    score += calculate_equipment_score(player)
    
    # Skills (0-20,000)
    score += sum(player.skills.values()) * 100
    
    # Achievements (0-25,000)
    score += player.achievement_points
    
    # Net Worth (0-50,000)
    net_worth_normalized = min(player.net_worth / 1000000, 50) * 1000
    score += net_worth_normalized
    
    # Reputation (0-10,000)
    score += sum(player.reputation.values()) * 10
    
    # PvP Rating (0-15,000)
    score += player.pvp_rating
    
    return int(score)
```

### Combat Score

```python
def calculate_combat_score(player_stats):
    """Боевой рейтинг"""
    
    score = 0
    
    # Kills (weighted by difficulty)
    score += player_stats.total_kills * 10
    score += player_stats.boss_kills * 100
    score += player_stats.player_kills * 50  # PvP
    
    # Accuracy
    if player_stats.shots_fired > 0:
        accuracy = player_stats.shots_hit / player_stats.shots_fired
        score += accuracy * 10000
    
    # KDA Ratio
    if player_stats.deaths > 0:
        kda = (player_stats.kills + player_stats.assists) / player_stats.deaths
        score += kda * 1000
    
    # Damage dealt
    score += player_stats.total_damage / 1000
    
    return int(score)
```

---

## Rank Tiers

```
Diamond (Top 100)       - Exclusive rewards
Platinum (Top 1,000)    - Premium rewards
Gold (Top 10,000)       - Good rewards
Silver (Top 50,000)     - Basic rewards
Bronze (Everyone else)  - Participation rewards
```

---

## Seasonal Reset

### Season End Process

```java
@Service
public class SeasonalLeaderboardService {
    
    @Scheduled(cron = "0 0 0 1 * *") // 1st of month at midnight
    public void checkSeasonEnd() {
        List<Leaderboard> endingSeasons = leaderboardRepository
            .findBySeasonEndDate(LocalDate.now());
        
        for (Leaderboard leaderboard : endingSeasons) {
            endSeason(leaderboard);
        }
    }
    
    private void endSeason(Leaderboard leaderboard) {
        // 1. Create snapshot (final standings)
        createFinalSnapshot(leaderboard.getId());
        
        // 2. Distribute rewards
        distributeSeasonRewards(leaderboard.getId());
        
        // 3. Archive old entries
        archiveSeasonEntries(leaderboard.getId());
        
        // 4. Reset leaderboard
        resetLeaderboard(leaderboard.getId());
        
        // 5. Start new season
        startNewSeason(leaderboard);
        
        log.info("Season ended for leaderboard: {}", leaderboard.getCode());
    }
    
    private void distributeSeasonRewards(UUID leaderboardId) {
        List<LeaderboardEntry> topPlayers = getTopPlayers(leaderboardId, 100);
        
        for (int i = 0; i < topPlayers.size(); i++) {
            LeaderboardEntry entry = topPlayers.get(i);
            int rank = i + 1;
            
            Rewards rewards = getRewardsForRank(rank);
            rewardService.grant(entry.getPlayerId(), rewards);
            
            // Send notification
            notificationService.send(entry.getPlayerId(),
                new SeasonEndNotification(rank, rewards));
        }
    }
}
```

---

## API Endpoints

**GET `/api/v1/leaderboards`** - список всех рейтингов

**GET `/api/v1/leaderboards/{code}`** - топ игроков

```json
Response:
{
  "leaderboardId": "uuid",
  "code": "combat_kills_global",
  "name": "Combat Kills - Global",
  "type": "COMBAT",
  "scope": "GLOBAL",
  "isSeasonal": false,
  
  "entries": [
    {
      "rank": 1,
      "playerId": "uuid",
      "playerName": "V",
      "score": 15234,
      "change": "+2",
      "guild": "Night City Legends"
    }
  ],
  
  "totalPlayers": 50000,
  "lastUpdated": "2025-11-07T01:59:00Z"
}
```

**GET `/api/v1/leaderboards/{code}/player/{id}`** - позиция игрока

```json
Response:
{
  "rank": 1523,
  "score": 8456,
  "percentile": 97.0,
  "change": "-5",
  "tier": "GOLD",
  
  "playersAhead": 1522,
  "playersBehind": 48477,
  
  "nearbyPlayers": [
    {"rank": 1521, "name": "Player A", "score": 8460},
    {"rank": 1522, "name": "Player B", "score": 8458},
    {"rank": 1523, "name": "You", "score": 8456},
    {"rank": 1524, "name": "Player C", "score": 8454},
    {"rank": 1525, "name": "Player D", "score": 8452}
  ]
}
```

**GET `/api/v1/leaderboards/season/{seasonId}/rewards`** - награды сезона

---

## Real-Time Updates

### WebSocket Channel

```
Channel: leaderboard.{code}.updates

Message format:
{
  "type": "RANK_CHANGE",
  "playerId": "uuid",
  "oldRank": 1524,
  "newRank": 1523,
  "scoreChange": +5
}

{
  "type": "NEW_LEADER",
  "playerId": "uuid",
  "playerName": "V",
  "score": 20000
}
```

---

## Связанные документы

- [Achievement System](../achievement/achievement-core.md)
- [PvP Rating System](../pvp/pvp-rating.md)
